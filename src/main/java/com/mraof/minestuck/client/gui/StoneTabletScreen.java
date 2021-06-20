package com.mraof.minestuck.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.network.StoneTabletPacket;
import com.mraof.minestuck.util.StoneTabletUtils;
import com.mraof.minestuck.util.StoneTabletUtils.Point;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.chat.NarratorChatListener;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SharedConstants;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.*;
import org.apache.commons.lang3.mutable.MutableInt;


public class StoneTabletScreen extends Screen
{
	public static final ResourceLocation TABLET_TEXTURES = new ResourceLocation(Minestuck.MOD_ID, "textures/gui/stone_tablet.png");
	
	private final boolean canEdit;
	private boolean isModified;
	private String text;
	private int updateCount;
	/** In milliseconds */
	private long lastClickTime;
	//Player
	private final PlayerEntity editingPlayer;
	private final Hand hand;
	private final ItemStack tablet;
	//GUI size
	private final int guiWidth = 192;
	private final int guiHeight = 192;
	//GUI buttons
	private Button buttonDone;
	private Button buttonCancel;
	private int cursor;
	private int selection;
	
	public StoneTabletScreen(PlayerEntity player, Hand hand, String text, boolean canEdit)
	{
		super(NarratorChatListener.NO_TITLE);
		
		this.canEdit = canEdit;
		this.hand = hand;
		this.tablet = player.getItemInHand(hand);
		editingPlayer = player;
		this.text = text;
	}
	
	@Override
	public void tick()
	{
		super.tick();
		++updateCount;
	}
	@Override
	public void removed() {
		this.minecraft.keyboardHandler.setSendRepeatsToGui(false);
	}
	@Override
	protected void init() {
		this.minecraft.keyboardHandler.setSendRepeatsToGui(true);
		
		
		if(canEdit)
		{
			this.buttonDone = this.addButton(new Button(this.width / 2 - 100, 196, 98, 20, new TranslationTextComponent("gui.done"), (button) ->
			{
				this.minecraft.setScreen(null);
				this.sendTabletToServer();
			}));
			this.buttonCancel = this.addButton(new Button(this.width / 2 + 2, 196, 98, 20, new TranslationTextComponent("gui.cancel"), (button) ->
			{
				minecraft.setScreen(null);
			}));
		}
		else
		{
			this.addButton(new Button(this.width / 2 - 100, 196, 200, 20, new TranslationTextComponent("gui.done"), (p_214161_1_) -> {
				this.minecraft.setScreen(null);
			}));
		}
	}
	
	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
	{
		this.renderBackground(matrixStack);
		this.setFocused(null);
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.minecraft.getTextureManager().bind(TABLET_TEXTURES);
		int topX = (this.width - 192) / 2;
		int topY = 2;
		this.blit(matrixStack, topX, topY, 0, 0, 192, 192);
		{
			String s5 = this.text;
			
			MutableInt lineY = new MutableInt();
			font.getSplitter().splitLines(s5, 114, Style.EMPTY, true, (style, start, end) -> {
				ITextComponent line = new StringTextComponent(s5.substring(start, end)).setStyle(style);
				font.draw(matrixStack, line, (this.width - 192) / 2F + 36, lineY.intValue() + 32, -16777216);
				lineY.add(font.lineHeight);
			});
			
			this.highlightSelectedText(s5);
			if (this.updateCount / 6 % 2 == 0) 
			{
				Point point = StoneTabletUtils.createPointer(font, s5, this.cursor);
				if (this.font.isBidirectional()) 
				{
					StoneTabletUtils.adjustPointerAForBidi(font, point);
					point.x = point.x - 4;
				}
				
				StoneTabletUtils.pointerToPrecise(point, width);
				if(canEdit)
				{
					if(this.cursor < s5.length())
					{
						AbstractGui.fill(matrixStack, point.x, point.y - 1, point.x + 1, point.y + 9, -16777216);
					} else
					{
						this.font.draw(matrixStack, "_", (float) point.x, (float) point.y, 0);
					}
				}
			}
		}
		
		super.render(matrixStack, mouseX, mouseY, partialTicks);
	}
	
	/**
	 * Uses drawSelectionBox to draw one or more boxes behind any selected text
	 *
	 * @param pageText Text on the current page as a string
	 */
	private void highlightSelectedText(String pageText) 
	{
		if (this.selection != this.cursor)
		{
			int selectionStart = Math.min(this.cursor, this.selection);
			int selectionEnd = Math.max(this.cursor, this.selection);
			
			MutableInt lineY = new MutableInt();
			font.getSplitter().splitLines(text, 114, Style.EMPTY, true, (style, start, end) -> {
				if(selectionEnd >= start && selectionStart <= end)
				{
					int startIndex = Math.max(selectionStart, start);
					int endIndex = Math.min(selectionEnd, end);
					Point startPos = new Point(font.width(text.substring(start, startIndex)), lineY.intValue());
					Point endPos = new Point(font.width(text.substring(start, endIndex)), lineY.intValue() + font.lineHeight);
					drawSelectionBox(startPos, endPos);
				}
				lineY.add(font.lineHeight);
			});
		}
	}
	
	/**
	 * Draws the blue text selection box, defined by the two point parameters
	 */
	private void drawSelectionBox(Point topLeft, Point bottomRight) 
	{
		Point point = new Point(topLeft.x, topLeft.y);
		Point point1 = new Point(bottomRight.x, bottomRight.y);
		if (this.font.isBidirectional()) 
		{
			StoneTabletUtils.adjustPointerAForBidi(font, point);
			StoneTabletUtils.adjustPointerAForBidi(font, point1);
			int i = point1.x;
			point1.x = point.x;
			point.x = i;
		}
		
		StoneTabletUtils.pointerToPrecise(point, width);
		StoneTabletUtils.pointerToPrecise(point1, width);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuilder();
		RenderSystem.color4f(0.0F, 0.0F, 1.0F, 1.0F);
		RenderSystem.disableTexture();
		RenderSystem.enableColorLogicOp();
		RenderSystem.logicOp(GlStateManager.LogicOp.OR_REVERSE);
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
		bufferbuilder.vertex(point.x, point1.y, 0.0D).endVertex();
		bufferbuilder.vertex(point1.x, point1.y, 0.0D).endVertex();
		bufferbuilder.vertex(point1.x, point.y, 0.0D).endVertex();
		bufferbuilder.vertex(point.x, point.y, 0.0D).endVertex();
		tessellator.end();
		RenderSystem.disableColorLogicOp();
		RenderSystem.enableTexture();
	}
	
	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) 
	{
		if (super.keyPressed(keyCode, scanCode, modifiers) || !canEdit) 
			return true;
		 else 
			return this.keyPressedInBook(keyCode);
	}
	
	@Override
	public boolean charTyped(char p_charTyped_1_, int p_charTyped_2_) 
	{
		if (super.charTyped(p_charTyped_1_, p_charTyped_2_) || !canEdit) 
			return true;
		else if (SharedConstants.isAllowedChatCharacter(p_charTyped_1_)) 
		{
			this.insertTextIntoPage(Character.toString(p_charTyped_1_));
			return true;
		} else
			return false;
		
	}
	
	private void sendTabletToServer()
	{
		if(isModified && text != null)
			MSPacketHandler.sendToServer(new StoneTabletPacket(text, hand));
	}
	
	/**
	 * Returns a copy of the input string with character 127 (del) and character 167 (section sign) removed
	 */
	private String removeUnprintableChars(String text) 
	{
		StringBuilder stringbuilder = new StringBuilder();
		
		for(char c0 : text.toCharArray())
			if (c0 != 167 && c0 != 127) 
				stringbuilder.append(c0);
			
		
		
		return stringbuilder.toString();
	}
	
	private void setText(String text) 
	{
			this.text = text;
			this.isModified = true;
	}
	
	/**
	 * Inserts text into the current page at the between selectionStart and selectionEnd (erasing any highlighted text)
	 *
	 * @param text The text to insert
	 */
	private void insertTextIntoPage(String text) 
	{
		if (this.selection != this.cursor)
			this.removeSelectedText();
		
		
		String s = this.text;
		this.cursor = MathHelper.clamp(this.cursor, 0, s.length());
		String s1 = (new StringBuilder(s)).insert(this.cursor, text).toString();
		int i = this.font.wordWrapHeight(s1 + "" + TextFormatting.BLACK + "_", 114);
		if (i <= 128 && s1.length() < 1024) 
		{
			this.setText(s1);
			this.selection = this.cursor = Math.min(this.text.length(), this.cursor + text.length());
		}
		
	}
	
	/**
	 * Returns any selected text on the current page
	 */
	private String getSelectedText() 
	{
		String s = this.text;
		int i = Math.min(this.cursor, this.selection);
		int j = Math.max(this.cursor, this.selection);
		return s.substring(i, j);
	}
	
	/**
	 * Returns the width of text
	 */
	private int getTextWidth(String text) 
	{
		return this.font.width(this.font.isBidirectional() ? this.font.bidirectionalShaping(text) : text);
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
		if (mouseButton == 0) 
		{
			long i = Util.getMillis();
			String s = this.text;
			if (!s.isEmpty()) 
			{
				Point point = new Point((int)mouseX, (int)mouseY);
				StoneTabletUtils.pointerToRelative(point, width);
				StoneTabletUtils.adjustPointerAForBidi(font, point);
				int j = StoneTabletUtils.getSelectionIndex(font, s, point);
				if (j >= 0) 
				{
					if (i - this.lastClickTime < 250L) {
						if (this.selection == this.cursor) {
							this.selection = CharacterManager.getWordPosition(s, -1, j, false);
							this.cursor = CharacterManager.getWordPosition(s, 1, j, false);
						} else 
							{
							this.selection = 0;
							this.cursor = this.text.length();
						}
					} else 
					{
						this.cursor = j;
						if (!Screen.hasShiftDown()) 
							this.selection = this.cursor;
						
					}
				}
			}
			
			this.lastClickTime = i;
		}
		
		return super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	@Override
	public boolean mouseDragged(double p_mouseDragged_1_, double p_mouseDragged_3_, int p_mouseDragged_5_, double p_mouseDragged_6_, double p_mouseDragged_8_) {
		if (p_mouseDragged_5_ == 0) 
		{
			String s = this.text;
			Point point = new Point((int)p_mouseDragged_1_, (int)p_mouseDragged_3_);
			StoneTabletUtils.pointerToRelative(point, width);
			StoneTabletUtils.adjustPointerAForBidi(font, point);
			int i = StoneTabletUtils.getSelectionIndex(font, s, point);
			if (i >= 0) 
				this.cursor = i;
		}
		
		return super.mouseDragged(p_mouseDragged_1_, p_mouseDragged_3_, p_mouseDragged_5_, p_mouseDragged_6_, p_mouseDragged_8_);
	}
	
	/**
	 * Handles keypresses, clipboard functions, and page turning
	 */
	private boolean keyPressedInBook(int keyCode) {
		String s = text;
		if (Screen.isSelectAll(keyCode)) 
		{
			this.selection = 0;
			this.cursor = s.length();
			return true;
		} else if (Screen.isCopy(keyCode)) 
		{
			this.minecraft.keyboardHandler.setClipboard(this.getSelectedText());
			return true;
		} else if (Screen.isPaste(keyCode)) 
		{
			this.insertTextIntoPage(this.removeUnprintableChars(TextFormatting.stripFormatting(this.minecraft.keyboardHandler.getClipboard().replaceAll("\\r", ""))));
			this.selection = this.cursor;
			return true;
		} else if (Screen.isCut(keyCode)) 
		{
			this.minecraft.keyboardHandler.setClipboard(this.getSelectedText());
			this.removeSelectedText();
			return true;
		} else 
			{
			switch(keyCode) 
			{
				case 257:
				case 335:
					this.insertTextIntoPage("\n");
					return true;
				case 259:
					this.backspacePressed(s);
					return true;
				case 261:
					this.deletePressed(s);
					return true;
				case 262:
					this.rightPressed(s);
					return true;
				case 263:
					this.leftPressed(s);
					return true;
				case 264:
					this.downPressed(s);
					return true;
				case 265:
					this.upPressed(s);
					return true;
				case 268:
					this.homePressed(s);
					return true;
				case 269:
					this.endPressed(s);
					return true;
				default:
					return false;
			}
		}
	}
	
	/**
	 * Called when backspace is pressed
	 * Removes the character to the left of the cursor (or the entire selection)
	 */
	private void backspacePressed(String pageText) 
	{
		if (!pageText.isEmpty()) 
		{
			if (this.selection != this.cursor)
				this.removeSelectedText();
			else if (this.cursor > 0)
			{
				String s = (new StringBuilder(pageText)).deleteCharAt(Math.max(0, this.cursor - 1)).toString();
				this.setText(s);
				this.cursor = Math.max(0, this.cursor - 1);
				this.selection = this.cursor;
			}
		}
		
	}
	
	/**
	 * Called when delete is pressed
	 * Removes the character to the right of the cursor (or the entire selection)
	 */
	private void deletePressed(String pageText) 
	{
		if (!pageText.isEmpty()) 
		{
			if (this.selection != this.cursor)
				this.removeSelectedText();
			else if (this.cursor < pageText.length())
			{
				String s = (new StringBuilder(pageText)).deleteCharAt(Math.max(0, this.cursor)).toString();
				this.setText(s);
			}
		}
		
	}
	
	/**
	 * Called when the left directional arrow on the keyboard is pressed
	 */
	private void leftPressed(String pageText) 
	{
		int i = this.font.isBidirectional() ? 1 : -1;
		if (Screen.hasControlDown()) 
			this.cursor = CharacterManager.getWordPosition(pageText, i, this.cursor, true);
		else
			this.cursor = Math.max(0, this.cursor + i);
		
		if (!Screen.hasShiftDown())
			this.selection = this.cursor;
				
	}
	
	/**
	 * Called when the right directional arrow on the keyboard is pressed
	 */
	private void rightPressed(String pageText) 
	{
		int i = this.font.isBidirectional() ? -1 : 1;
		if (Screen.hasControlDown())
			this.cursor = CharacterManager.getWordPosition(pageText, i, this.cursor, true);
		else
			this.cursor = Math.min(pageText.length(), this.cursor + i);
				
		if (!Screen.hasShiftDown())
			this.selection = this.cursor;
		
	}
	
	/**
	 * Called when the up directional arrow on the keyboard is pressed
	 */
	private void upPressed(String pageText) 
	{
		if (!pageText.isEmpty()) 
		{
			Point point = StoneTabletUtils.createPointer(font, pageText, this.cursor);
			if (point.y == 0) 
			{
				this.cursor = 0;
				if (!Screen.hasShiftDown())
					this.selection = this.cursor;
				
			} else 
			{
				int i = StoneTabletUtils.getSelectionIndex(font, pageText, new Point(point.x + StoneTabletUtils.getSelectionWidth(font, pageText, this.cursor) / 3, point.y - 9));
				if (i >= 0) 
				{
					this.cursor = i;
					if (!Screen.hasShiftDown())
						this.selection = this.cursor;
				}
			}
		}
		
	}
	
	/**
	 * Called when the down arrow on the keyboard is pressed
	 */
	private void downPressed(String pageText) 
	{
		if (!pageText.isEmpty()) 
		{
			Point point = StoneTabletUtils.createPointer(font, pageText, this.cursor);
			int i = this.font.wordWrapHeight(pageText + "" + TextFormatting.BLACK + "_", 114);
			if (point.y + 9 == i) 
			{
				this.cursor = pageText.length();
				if (!Screen.hasShiftDown())
					this.selection = this.cursor;
			} else 
			{
				int j = StoneTabletUtils.getSelectionIndex(font, pageText, new Point(point.x + StoneTabletUtils.getSelectionWidth(font, pageText, this.cursor) / 3, point.y + 9));
				if (j >= 0) 
				{
					this.cursor = j;
					if (!Screen.hasShiftDown())
						this.selection = this.cursor;
				}
			}
		}
		
	}
	
	/**
	 * Called when the home button on the keyboard is pressed
	 */
	private void homePressed(String pageText) 
	{
		this.cursor = StoneTabletUtils.getSelectionIndex(font, pageText, new Point(0, StoneTabletUtils.createPointer(font, pageText, this.cursor).y));
		if (!Screen.hasShiftDown())
			this.selection = this.cursor;
	}
	
	/**
	 * Called when the end button on the keyboard is pressed
	 */
	private void endPressed(String pageText) 
	{
		this.cursor = StoneTabletUtils.getSelectionIndex(font, pageText, new Point(113, StoneTabletUtils.createPointer(font, pageText, this.cursor).y));
		if (!Screen.hasShiftDown())
			this.selection = this.cursor;
	}
	
	/**
	 * Removes the text between selectionStart and selectionEnd from the currrent page
	 */
	private void removeSelectedText() 
	{
		if (this.selection != this.cursor)
		{
			String s = this.text;
			int i = Math.min(this.cursor, this.selection);
			int j = Math.max(this.cursor, this.selection);
			String s1 = s.substring(0, i) + s.substring(j);
			this.cursor = i;
			this.selection = this.cursor;
			this.setText(s1);
		}
	}
	
}
