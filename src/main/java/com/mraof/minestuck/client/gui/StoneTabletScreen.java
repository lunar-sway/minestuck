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
import net.minecraft.client.gui.fonts.TextInputUtil;
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
import net.minecraft.util.text.*;
import org.apache.commons.lang3.mutable.MutableInt;
import org.lwjgl.glfw.GLFW;


public class StoneTabletScreen extends Screen
{
	public static final ResourceLocation TABLET_TEXTURES = new ResourceLocation(Minestuck.MOD_ID, "textures/gui/stone_tablet.png");
	
	//GUI sizes
	public static final int GUI_WIDTH = 192;
	public static final int GUI_HEIGHT = 192;
	public static final int TEXT_WIDTH = 114;
	public static final int TEXT_OFFSET_X = 36, TEXT_OFFSET_Y = 32;
	
	private final boolean canEdit;
	private boolean isModified;
	private String text = "";
	private int updateCount;
	/**
	 * In milliseconds
	 */
	private long lastClickTime;
	//Player
	private final PlayerEntity editingPlayer;
	private final Hand hand;
	private final ItemStack tablet;
	//GUI buttons
	private Button buttonDone;
	private Button buttonCancel;
	
	private final TextInputUtil pageEditor = new TextInputUtil(() -> text, this::setText, this::getClipboard, this::setClipboard,
			text -> text.length() < 1024 && this.font.wordWrapHeight(text, TEXT_WIDTH) <= 128);
	
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
	public void removed()
	{
		this.minecraft.keyboardHandler.setSendRepeatsToGui(false);
	}
	
	@Override
	protected void init()
	{
		this.minecraft.keyboardHandler.setSendRepeatsToGui(true);
		
		
		if(canEdit)
		{
			this.buttonDone = this.addButton(new Button(this.width / 2 - 100, GUI_HEIGHT + 4, 98, 20, new TranslationTextComponent("gui.done"), (button) ->
			{
				this.minecraft.setScreen(null);
				this.sendTabletToServer();
			}));
			this.buttonCancel = this.addButton(new Button(this.width / 2 + 2, GUI_HEIGHT + 4, 98, 20, new TranslationTextComponent("gui.cancel"), (button) ->
			{
				minecraft.setScreen(null);
			}));
		} else
		{
			this.addButton(new Button(this.width / 2 - 100, GUI_HEIGHT + 4, 200, 20, new TranslationTextComponent("gui.done"), (p_214161_1_) -> {
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
		int topX = (this.width - GUI_WIDTH) / 2;
		int topY = 2;
		this.blit(matrixStack, topX, topY, 0, 0, GUI_WIDTH, GUI_HEIGHT);
		{
			
			MutableInt lineY = new MutableInt();
			font.getSplitter().splitLines(text, TEXT_WIDTH, Style.EMPTY, true, (style, start, end) -> {
				ITextComponent line = new StringTextComponent(text.substring(start, end)).setStyle(style);
				font.draw(matrixStack, line, (this.width - GUI_WIDTH) / 2F + TEXT_OFFSET_X, lineY.intValue() + TEXT_OFFSET_Y, 0xFFFFFF);
				lineY.add(font.lineHeight);
			});
			
			this.highlightSelectedText();
			if(canEdit && this.updateCount / 6 % 2 == 0)
			{
				Point point = StoneTabletUtils.createPointer(font, text, pageEditor.getCursorPos());
				if(this.font.isBidirectional())
				{
					StoneTabletUtils.adjustPointerAForBidi(font, point);
					point.x = point.x - 4;
				}
				
				StoneTabletUtils.pointerToPrecise(point, width);
				if(pageEditor.getCursorPos() < text.length())
					AbstractGui.fill(matrixStack, point.x, point.y - 1, point.x + 1, point.y + font.lineHeight, 0xFFFFFF);
				else
					this.font.draw(matrixStack, "_", (float) point.x, (float) point.y, 0);
			}
		}
		
		super.render(matrixStack, mouseX, mouseY, partialTicks);
	}
	
	/**
	 * Uses drawSelectionBox to draw one or more boxes behind any selected text
	 */
	private void highlightSelectedText()
	{
		if(pageEditor.isSelecting())
		{
			int cursor = pageEditor.getCursorPos(), selection = pageEditor.getSelectionPos();
			int selectionStart = Math.min(cursor, selection);
			int selectionEnd = Math.max(cursor, selection);
			
			MutableInt lineY = new MutableInt();
			font.getSplitter().splitLines(text, TEXT_WIDTH, Style.EMPTY, true, (style, start, end) -> {
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
		if(this.font.isBidirectional())
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
		if(super.keyPressed(keyCode, scanCode, modifiers))
			return true;
		else return this.handleSpecialKey(keyCode);
	}
	
	@Override
	public boolean charTyped(char keycode, int modifiers)
	{
		if(super.charTyped(keycode, modifiers))
			return true;
		else if(SharedConstants.isAllowedChatCharacter(keycode) && canEdit)
		{
			pageEditor.insertText(Character.toString(keycode));
			return true;
		} else
			return false;
		
	}
	
	private void sendTabletToServer()
	{
		if(isModified && text != null)
			MSPacketHandler.sendToServer(new StoneTabletPacket(text, hand));
	}
	
	private void setText(String text)
	{
		this.text = text;
		this.isModified = true;
	}
	
	private void setClipboard(String str)
	{
		if(minecraft != null)
			TextInputUtil.setClipboardContents(minecraft, str);
	}
	
	private String getClipboard()
	{
		return minecraft != null ? TextInputUtil.getClipboardContents(this.minecraft) : "";
	}
	
	
	/**
	 * Returns the width of text
	 */
	private int getTextWidth(String text)
	{
		return this.font.width(this.font.isBidirectional() ? this.font.bidirectionalShaping(text) : text);
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton)
	{
		if(mouseButton == 0)
		{
			long i = Util.getMillis();
			String s = this.text;
			if(!s.isEmpty())
			{
				Point point = new Point((int) mouseX, (int) mouseY);
				StoneTabletUtils.pointerToRelative(point, width);
				StoneTabletUtils.adjustPointerAForBidi(font, point);
				int clickedIndex = StoneTabletUtils.getSelectionIndex(font, s, point);
				if(clickedIndex >= 0)
				{
					if(i - this.lastClickTime < 250L)
					{
						if(!pageEditor.isSelecting())
						{
							int start = CharacterManager.getWordPosition(s, -1, clickedIndex, false);
							int end = CharacterManager.getWordPosition(s, 1, clickedIndex, false);
							pageEditor.setSelectionRange(start, end);
						} else
							pageEditor.selectAll();
					} else
						pageEditor.setCursorPos(clickedIndex, Screen.hasShiftDown());
				}
			}
			
			this.lastClickTime = i;
		}
		
		return super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double dx, double dy)
	{
		if(button == 0)
		{
			String s = this.text;
			Point point = new Point((int) mouseX, (int) mouseY);
			StoneTabletUtils.pointerToRelative(point, width);
			StoneTabletUtils.adjustPointerAForBidi(font, point);
			int index = StoneTabletUtils.getSelectionIndex(font, s, point);
			if(index >= 0)
				pageEditor.setCursorPos(index, true);
		}
		
		return super.mouseDragged(mouseX, mouseY, button, dx, dy);
	}
	
	/**
	 * Handles keypresses, clipboard functions, and page turning
	 */
	private boolean handleSpecialKey(int keyCode)
	{
		if(Screen.isSelectAll(keyCode))
		{
			pageEditor.selectAll();
			return true;
		} else if(Screen.isCopy(keyCode))
		{
			pageEditor.copy();
			return true;
		} else if(Screen.isPaste(keyCode) && canEdit)
		{
			pageEditor.paste();
			return true;
		} else if(Screen.isCut(keyCode) && canEdit)
		{
			pageEditor.cut();
			return true;
		} else if(canEdit)
		{
			switch(keyCode)
			{
				case GLFW.GLFW_KEY_ENTER:
				case GLFW.GLFW_KEY_KP_ENTER:
					pageEditor.insertText("\n");
					return true;
				case GLFW.GLFW_KEY_BACKSPACE:
					pageEditor.removeCharsFromCursor(-1);
					return true;
				case GLFW.GLFW_KEY_DELETE:
					pageEditor.removeCharsFromCursor(1);
					return true;
				case GLFW.GLFW_KEY_RIGHT:
					pageEditor.moveByChars(1, Screen.hasShiftDown());
					return true;
				case GLFW.GLFW_KEY_LEFT:
					pageEditor.moveByChars(-1, Screen.hasShiftDown());
					return true;
				case GLFW.GLFW_KEY_DOWN:
					this.downPressed();
					return true;
				case GLFW.GLFW_KEY_UP:
					this.upPressed();
					return true;
				case GLFW.GLFW_KEY_HOME:
					this.homePressed();
					return true;
				case GLFW.GLFW_KEY_END:
					this.endPressed();
					return true;
				default:
					return false;
			}
		} else return false;
	}
	
	/**
	 * Called when the up directional arrow on the keyboard is pressed
	 */
	private void upPressed()
	{
		if(!text.isEmpty())
		{
			Point point = StoneTabletUtils.createPointer(font, text, pageEditor.getCursorPos());
			if(point.y == 0)
				pageEditor.setCursorPos(0, Screen.hasShiftDown());
			else
			{
				int index = StoneTabletUtils.getSelectionIndex(font, text, new Point(point.x + StoneTabletUtils.getSelectionWidth(font, text, pageEditor.getCursorPos()) / 3, point.y - font.lineHeight));
				if(index >= 0)
					pageEditor.setCursorPos(index, Screen.hasShiftDown());
			}
		}
	}
	
	/**
	 * Called when the down arrow on the keyboard is pressed
	 */
	private void downPressed()
	{
		if(!text.isEmpty())
		{
			Point point = StoneTabletUtils.createPointer(font, text, pageEditor.getCursorPos());
			int textHeight = this.font.wordWrapHeight(text + "" + TextFormatting.BLACK + "_", TEXT_WIDTH);
			if(point.y + font.lineHeight == textHeight)
				pageEditor.setCursorPos(text.length(), Screen.hasShiftDown());
			else
			{
				int index = StoneTabletUtils.getSelectionIndex(font, text, new Point(point.x + StoneTabletUtils.getSelectionWidth(font, text, pageEditor.getCursorPos()) / 3, point.y + font.lineHeight));
				if(index >= 0)
					pageEditor.setCursorPos(index, Screen.hasShiftDown());
			}
		}
	}
	
	/**
	 * Called when the home button on the keyboard is pressed
	 */
	private void homePressed()
	{
		int index = StoneTabletUtils.getSelectionIndex(font, text, new Point(0, StoneTabletUtils.createPointer(font, text, pageEditor.getCursorPos()).y));
		pageEditor.setCursorPos(index, Screen.hasShiftDown());
	}
	
	/**
	 * Called when the end button on the keyboard is pressed
	 */
	private void endPressed()
	{
		int index = StoneTabletUtils.getSelectionIndex(font, text, new Point(TEXT_WIDTH - 1, StoneTabletUtils.createPointer(font, text, pageEditor.getCursorPos()).y));
		pageEditor.setCursorPos(index, Screen.hasShiftDown());
	}
	
}
