package com.mraof.minestuck.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.client.gui.StoneTabletUtils.Point;
import com.mraof.minestuck.network.CarveStoneTabletPacket;
import net.minecraft.ChatFormatting;
import net.minecraft.SharedConstants;
import net.minecraft.Util;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.StringSplitter;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.font.TextFieldHelper;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;
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
	private final Player editingPlayer;
	private final InteractionHand hand;
	private final ItemStack tablet;
	//GUI buttons
	private Button buttonDone;
	private Button buttonCancel;
	
	private final TextFieldHelper pageEditor = new TextFieldHelper(() -> text, this::setText, this::getClipboard, this::setClipboard,
			text -> text.length() < 1024 && this.font.wordWrapHeight(text, TEXT_WIDTH) <= 128);
	
	public StoneTabletScreen(Player player, InteractionHand hand, String text, boolean canEdit)
	{
		super(GameNarrator.NO_TITLE);
		
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
	protected void init()
	{
		if(canEdit)
		{
			this.buttonDone = this.addRenderableWidget(Button.builder(Component.translatable("gui.done"), (button) ->
			{
				this.minecraft.setScreen(null);
				this.sendTabletToServer();
			}).pos(this.width / 2 - 100, GUI_HEIGHT + 4).size(98, 20).build());
			this.buttonCancel = this.addRenderableWidget(Button.builder(Component.translatable("gui.cancel"), (button) ->
			{
				minecraft.setScreen(null);
			}).pos(this.width / 2 + 2, GUI_HEIGHT + 4).size(98, 20).build());
		} else
		{
			this.addRenderableWidget(Button.builder(Component.translatable("gui.done"), button -> {
				this.minecraft.setScreen(null);
			}).pos(this.width / 2 - 100, GUI_HEIGHT + 4).size(200, 20).build());
		}
	}
	
	@Override
	public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks)
	{
		super.renderBackground(guiGraphics, mouseX, mouseY, partialTicks);
		
		int topX = (this.width - GUI_WIDTH) / 2;
		int topY = 2;
		guiGraphics.blit(TABLET_TEXTURES, topX, topY, 0, 0, GUI_WIDTH, GUI_HEIGHT);
	}
	
	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks)
	{
		this.setFocused(null);
		
		super.render(guiGraphics, mouseX, mouseY, partialTicks);
		
		{
			
			MutableInt lineY = new MutableInt();
			font.getSplitter().splitLines(text, TEXT_WIDTH, Style.EMPTY, false, (style, start, end) -> {
				Component line = Component.literal(text.substring(start, end)).setStyle(style);
				guiGraphics.drawString(font, line, (this.width - GUI_WIDTH) / 2 + TEXT_OFFSET_X, lineY.intValue() + TEXT_OFFSET_Y, 0xFFFFFF, false);
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
					guiGraphics.fill(point.x, point.y - 1, point.x + 1, point.y + font.lineHeight, 0xFFFFFF);
				else
					guiGraphics.drawString(font, "_", (float) point.x, (float) point.y, 0, false);
			}
		}
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
		RenderSystem.enableColorLogicOp();
		RenderSystem.logicOp(GlStateManager.LogicOp.OR_REVERSE);
		
		RenderSystem.setShaderColor(0, 0, 1, 1);
		RenderSystem.setShader(GameRenderer::getPositionShader);
		
		Tesselator tesselator = Tesselator.getInstance();
		BufferBuilder bufferbuilder = tesselator.getBuilder();
		bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);
		bufferbuilder.vertex(point.x, point1.y, 0.0D).endVertex();
		bufferbuilder.vertex(point1.x, point1.y, 0.0D).endVertex();
		bufferbuilder.vertex(point1.x, point.y, 0.0D).endVertex();
		bufferbuilder.vertex(point.x, point.y, 0.0D).endVertex();
		tesselator.end();
		
		RenderSystem.disableColorLogicOp();
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
			PacketDistributor.SERVER.noArg().send(new CarveStoneTabletPacket(text, hand));
	}
	
	private void setText(String text)
	{
		this.text = text;
		this.isModified = true;
	}
	
	private void setClipboard(String str)
	{
		if(minecraft != null)
			TextFieldHelper.setClipboardContents(minecraft, str);
	}
	
	private String getClipboard()
	{
		return minecraft != null ? TextFieldHelper.getClipboardContents(this.minecraft) : "";
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
							int start = StringSplitter.getWordPosition(s, -1, clickedIndex, false);
							int end = StringSplitter.getWordPosition(s, 1, clickedIndex, false);
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
			int textHeight = this.font.wordWrapHeight(text + "" + ChatFormatting.BLACK + "_", TEXT_WIDTH);
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
