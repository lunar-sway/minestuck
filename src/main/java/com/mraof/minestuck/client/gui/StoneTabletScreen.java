package com.mraof.minestuck.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.network.StoneTabletPacket;
import com.mraof.minestuck.util.StoneTabletUtils;
import com.mraof.minestuck.util.StoneTabletUtils.Point;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.chat.NarratorChatListener;
import net.minecraft.client.gui.screen.ReadBookScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SharedConstants;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;


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
	/** Note that this can be less than selectionStart if you select text right-to-left */
	private int selectionEnd;
	/** Note that this will be greater than selectionEnd if you select text right-to-left */
	private int selectionStart;
	
	public StoneTabletScreen(PlayerEntity player, Hand hand, String text, boolean canEdit)
	{
		super(NarratorChatListener.EMPTY);
		
		this.canEdit = canEdit;
		this.hand = hand;
		this.tablet = player.getHeldItem(hand);
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
		this.minecraft.keyboardListener.enableRepeatEvents(false);
	}
	@Override
	protected void init() {
		this.minecraft.keyboardListener.enableRepeatEvents(true);
		
		
		if(canEdit)
		{
			this.buttonDone = this.addButton(new Button(this.width / 2 - 100, 196, 98, 20, I18n.format("gui.done"), (button) ->
			{
				this.minecraft.displayGuiScreen(null);
				this.sendTabletToServer();
			}));
			this.buttonCancel = this.addButton(new Button(this.width / 2 + 2, 196, 98, 20, I18n.format("gui.cancel"), (button) ->
			{
				minecraft.displayGuiScreen(null);
			}));
		}
		else
		{
			this.addButton(new Button(this.width / 2 - 100, 196, 200, 20, I18n.format("gui.done"), (p_214161_1_) -> {
				this.minecraft.displayGuiScreen((Screen)null);
			}));
		}
	}
	
	@Override
	public void render(int mouseX, int mouseY, float partialTicks)
	{
		this.renderBackground();
		this.setFocused(null);
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.minecraft.getTextureManager().bindTexture(TABLET_TEXTURES);
		int i = (this.width - 192) / 2;
		int j = 2;
		this.blit(i, 2, 0, 0, 192, 192);
		{
			String s5 = this.text;
			this.font.drawSplitString(s5, i + 36, 32, 114, 0);
			this.highlightSelectedText(s5);
			if (this.updateCount / 6 % 2 == 0) 
			{
				Point point = StoneTabletUtils.createPointer(font, s5, this.selectionEnd);
				if (this.font.getBidiFlag()) 
				{
					StoneTabletUtils.adjustPointerAForBidi(font, point);
					point.x = point.x - 4;
				}
				
				StoneTabletUtils.adjustPointerB(point, width);
				if(canEdit)
				{
					if(this.selectionEnd < s5.length())
					{
						AbstractGui.fill(point.x, point.y - 1, point.x + 1, point.y + 9, -16777216);
					} else
					{
						this.font.drawString("_", (float) point.x, (float) point.y, 0);
					}
				}
			}
		}
		
		super.render(mouseX, mouseY, partialTicks);
	}
	
	/**
	 * Uses drawSelectionBox to draw one or more boxes behind any selected text
	 *
	 * @param pageText Text on the current page as a string
	 */
	private void highlightSelectedText(String pageText) 
	{
		if (this.selectionStart != this.selectionEnd) 
		{
			int i = Math.min(this.selectionEnd, this.selectionStart);
			int j = Math.max(this.selectionEnd, this.selectionStart);
			String s = pageText.substring(i, j);
			int k = this.font.func_216863_a(pageText, 1, j, true);
			String s1 = pageText.substring(i, k);
			Point point = StoneTabletUtils.createPointer(font, pageText, i);
			Point point1 = new Point(point.x, point.y + 9);
			
			while(!s.isEmpty()) 
			{
				int l = StoneTabletUtils.sizeStringToWidth(font, s1, 114 - point.x);
				if (s.length() <= l) 
				{
					point1.x = point.x + this.getTextWidth(s);
					this.drawSelectionBox(point, point1);
					break;
				}
				
				l = Math.min(l, s.length() - 1);
				String s2 = s.substring(0, l);
				char c0 = s.charAt(l);
				boolean flag = c0 == ' ' || c0 == '\n';
				s = TextFormatting.getFormatString(s2) + s.substring(l + (flag ? 1 : 0));
				s1 = TextFormatting.getFormatString(s2) + s1.substring(l + (flag ? 1 : 0));
				point1.x = point.x + this.getTextWidth(s2 + " ");
				this.drawSelectionBox(point, point1);
				point.x = 0;
				point.y = point.y + 9;
				point1.y = point1.y + 9;
			}
			
		}
	}
	
	/**
	 * Draws the blue text selection box, defined by the two point parameters
	 */
	private void drawSelectionBox(Point topLeft, Point bottomRight) 
	{
		Point point = new Point(topLeft.x, topLeft.y);
		Point point1 = new Point(bottomRight.x, bottomRight.y);
		if (this.font.getBidiFlag()) 
		{
			StoneTabletUtils.adjustPointerAForBidi(font, point);
			StoneTabletUtils.adjustPointerAForBidi(font, point1);
			int i = point1.x;
			point1.x = point.x;
			point.x = i;
		}
		
		StoneTabletUtils.adjustPointerB(point, width);
		StoneTabletUtils.adjustPointerB(point1, width);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		GlStateManager.color4f(0.0F, 0.0F, 255.0F, 255.0F);
		GlStateManager.disableTexture();
		GlStateManager.enableColorLogicOp();
		GlStateManager.logicOp(GlStateManager.LogicOp.OR_REVERSE);
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
		bufferbuilder.pos((double)point.x, (double)point1.y, 0.0D).endVertex();
		bufferbuilder.pos((double)point1.x, (double)point1.y, 0.0D).endVertex();
		bufferbuilder.pos((double)point1.x, (double)point.y, 0.0D).endVertex();
		bufferbuilder.pos((double)point.x, (double)point.y, 0.0D).endVertex();
		tessellator.draw();
		GlStateManager.disableColorLogicOp();
		GlStateManager.enableTexture();
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
		else if (SharedConstants.isAllowedCharacter(p_charTyped_1_)) 
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
		if (this.selectionStart != this.selectionEnd) 
			this.removeSelectedText();
		
		
		String s = this.text;
		this.selectionEnd = MathHelper.clamp(this.selectionEnd, 0, s.length());
		String s1 = (new StringBuilder(s)).insert(this.selectionEnd, text).toString();
		int i = this.font.getWordWrappedHeight(s1 + "" + TextFormatting.BLACK + "_", 114);
		if (i <= 128 && s1.length() < 1024) 
		{
			this.setText(s1);
			this.selectionStart = this.selectionEnd = Math.min(this.text.length(), this.selectionEnd + text.length());
		}
		
	}
	
	/**
	 * Returns any selected text on the current page
	 */
	private String getSelectedText() 
	{
		String s = this.text;
		int i = Math.min(this.selectionEnd, this.selectionStart);
		int j = Math.max(this.selectionEnd, this.selectionStart);
		return s.substring(i, j);
	}
	
	/**
	 * Returns the width of text
	 */
	private int getTextWidth(String text) 
	{
		return this.font.getStringWidth(this.font.getBidiFlag() ? this.font.bidiReorder(text) : text);
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
		if (mouseButton == 0) 
		{
			long i = Util.milliTime();
			String s = this.text;
			if (!s.isEmpty()) 
			{
				Point point = new Point((int)mouseX, (int)mouseY);
				StoneTabletUtils.adjustPointerA(point, width);
				StoneTabletUtils.adjustPointerAForBidi(font, point);
				int j = StoneTabletUtils.getSelectionIndex(font, s, point);
				if (j >= 0) 
				{
					if (i - this.lastClickTime < 250L) {
						if (this.selectionStart == this.selectionEnd) {
							this.selectionStart = this.font.func_216863_a(s, -1, j, false);
							this.selectionEnd = this.font.func_216863_a(s, 1, j, false);
						} else 
							{
							this.selectionStart = 0;
							this.selectionEnd = this.text.length();
						}
					} else 
					{
						this.selectionEnd = j;
						if (!Screen.hasShiftDown()) 
							this.selectionStart = this.selectionEnd;
						
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
			StoneTabletUtils.adjustPointerA(point, width);
			StoneTabletUtils.adjustPointerAForBidi(font, point);
			int i = StoneTabletUtils.getSelectionIndex(font, s, point);
			if (i >= 0) 
				this.selectionEnd = i;
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
			this.selectionStart = 0;
			this.selectionEnd = s.length();
			return true;
		} else if (Screen.isCopy(keyCode)) 
		{
			this.minecraft.keyboardListener.setClipboardString(this.getSelectedText());
			return true;
		} else if (Screen.isPaste(keyCode)) 
		{
			this.insertTextIntoPage(this.removeUnprintableChars(TextFormatting.getTextWithoutFormattingCodes(this.minecraft.keyboardListener.getClipboardString().replaceAll("\\r", ""))));
			this.selectionStart = this.selectionEnd;
			return true;
		} else if (Screen.isCut(keyCode)) 
		{
			this.minecraft.keyboardListener.setClipboardString(this.getSelectedText());
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
			if (this.selectionStart != this.selectionEnd) 
				this.removeSelectedText();
			else if (this.selectionEnd > 0) 
			{
				String s = (new StringBuilder(pageText)).deleteCharAt(Math.max(0, this.selectionEnd - 1)).toString();
				this.setText(s);
				this.selectionEnd = Math.max(0, this.selectionEnd - 1);
				this.selectionStart = this.selectionEnd;
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
			if (this.selectionStart != this.selectionEnd) 
				this.removeSelectedText();
			else if (this.selectionEnd < pageText.length()) 
			{
				String s = (new StringBuilder(pageText)).deleteCharAt(Math.max(0, this.selectionEnd)).toString();
				this.setText(s);
			}
		}
		
	}
	
	/**
	 * Called when the left directional arrow on the keyboard is pressed
	 */
	private void leftPressed(String pageText) 
	{
		int i = this.font.getBidiFlag() ? 1 : -1;
		if (Screen.hasControlDown()) 
			this.selectionEnd = this.font.func_216863_a(pageText, i, this.selectionEnd, true);
		else
			this.selectionEnd = Math.max(0, this.selectionEnd + i);
		
		if (!Screen.hasShiftDown())
			this.selectionStart = this.selectionEnd;
				
	}
	
	/**
	 * Called when the right directional arrow on the keyboard is pressed
	 */
	private void rightPressed(String pageText) 
	{
		int i = this.font.getBidiFlag() ? -1 : 1;
		if (Screen.hasControlDown())
			this.selectionEnd = this.font.func_216863_a(pageText, i, this.selectionEnd, true);
		else
			this.selectionEnd = Math.min(pageText.length(), this.selectionEnd + i);
				
		if (!Screen.hasShiftDown())
			this.selectionStart = this.selectionEnd;
		
	}
	
	/**
	 * Called when the up directional arrow on the keyboard is pressed
	 */
	private void upPressed(String pageText) 
	{
		if (!pageText.isEmpty()) 
		{
			Point point = StoneTabletUtils.createPointer(font, pageText, this.selectionEnd);
			if (point.y == 0) 
			{
				this.selectionEnd = 0;
				if (!Screen.hasShiftDown())
					this.selectionStart = this.selectionEnd;
				
			} else 
			{
				int i = StoneTabletUtils.getSelectionIndex(font, pageText, new Point(point.x + StoneTabletUtils.getSelectionWidth(font, pageText, this.selectionEnd) / 3, point.y - 9));
				if (i >= 0) 
				{
					this.selectionEnd = i;
					if (!Screen.hasShiftDown())
						this.selectionStart = this.selectionEnd;
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
			Point point = StoneTabletUtils.createPointer(font, pageText, this.selectionEnd);
			int i = this.font.getWordWrappedHeight(pageText + "" + TextFormatting.BLACK + "_", 114);
			if (point.y + 9 == i) 
			{
				this.selectionEnd = pageText.length();
				if (!Screen.hasShiftDown())
					this.selectionStart = this.selectionEnd;
			} else 
			{
				int j = StoneTabletUtils.getSelectionIndex(font, pageText, new Point(point.x + StoneTabletUtils.getSelectionWidth(font, pageText, this.selectionEnd) / 3, point.y + 9));
				if (j >= 0) 
				{
					this.selectionEnd = j;
					if (!Screen.hasShiftDown())
						this.selectionStart = this.selectionEnd;
				}
			}
		}
		
	}
	
	/**
	 * Called when the home button on the keyboard is pressed
	 */
	private void homePressed(String pageText) 
	{
		this.selectionEnd = StoneTabletUtils.getSelectionIndex(font, pageText, new Point(0, StoneTabletUtils.createPointer(font, pageText, this.selectionEnd).y));
		if (!Screen.hasShiftDown())
			this.selectionStart = this.selectionEnd;		
	}
	
	/**
	 * Called when the end button on the keyboard is pressed
	 */
	private void endPressed(String pageText) 
	{
		this.selectionEnd = StoneTabletUtils.getSelectionIndex(font, pageText, new Point(113, StoneTabletUtils.createPointer(font, pageText, this.selectionEnd).y));
		if (!Screen.hasShiftDown())
			this.selectionStart = this.selectionEnd;		
	}
	
	/**
	 * Removes the text between selectionStart and selectionEnd from the currrent page
	 */
	private void removeSelectedText() 
	{
		if (this.selectionStart != this.selectionEnd) 
		{
			String s = this.text;
			int i = Math.min(this.selectionEnd, this.selectionStart);
			int j = Math.max(this.selectionEnd, this.selectionStart);
			String s1 = s.substring(0, i) + s.substring(j);
			this.selectionEnd = i;
			this.selectionStart = this.selectionEnd;
			this.setText(s1);
		}
	}
	
}
