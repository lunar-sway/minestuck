package com.mraof.minestuck.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.network.StoneTabletPacket;
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
	public void render(int p_render_1_, int p_render_2_, float p_render_3_) {
		this.renderBackground();
		this.setFocused((IGuiEventListener)null);
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.minecraft.getTextureManager().bindTexture(TABLET_TEXTURES);
		int i = (this.width - 192) / 2;
		int j = 2;
		this.blit(i, 2, 0, 0, 192, 192);
		{
			String s5 = this.text;
			this.font.drawSplitString(s5, i + 36, 32, 114, 0);
			this.highlightSelectedText(s5);
			if (this.updateCount / 6 % 2 == 0) {
				Point point = this.func_214194_c(s5, this.selectionEnd);
				if (this.font.getBidiFlag()) {
					this.func_214227_a(point);
					point.x = point.x - 4;
				}
				
				this.func_214224_c(point);
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
		
		super.render(p_render_1_, p_render_2_, p_render_3_);
	}
	
	/**
	 * Uses drawSelectionBox to draw one or more boxes behind any selected text
	 *
	 * @param pageText Text on the current page as a string
	 */
	private void highlightSelectedText(String pageText) {
		if (this.selectionStart != this.selectionEnd) {
			int i = Math.min(this.selectionEnd, this.selectionStart);
			int j = Math.max(this.selectionEnd, this.selectionStart);
			String s = pageText.substring(i, j);
			int k = this.font.func_216863_a(pageText, 1, j, true);
			String s1 = pageText.substring(i, k);
			Point point = this.func_214194_c(pageText, i);
			Point point1 = new Point(point.x, point.y + 9);
			
			while(!s.isEmpty()) {
				int l = this.func_214216_b(s1, 114 - point.x);
				if (s.length() <= l) {
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
	private void drawSelectionBox(Point topLeft, Point bottomRight) {
		Point point = new Point(topLeft.x, topLeft.y);
		Point point1 = new Point(bottomRight.x, bottomRight.y);
		if (this.font.getBidiFlag()) {
			this.func_214227_a(point);
			this.func_214227_a(point1);
			int i = point1.x;
			point1.x = point.x;
			point.x = i;
		}
		
		this.func_214224_c(point);
		this.func_214224_c(point1);
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
	public boolean keyPressed(int p_keyPressed_1_, int p_keyPressed_2_, int p_keyPressed_3_) {
		if (super.keyPressed(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_) || !canEdit) {
			return true;
		} else {
			return this.keyPressedInBook(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_);
		}
	}
	
	@Override
	public boolean charTyped(char p_charTyped_1_, int p_charTyped_2_) {
		if (super.charTyped(p_charTyped_1_, p_charTyped_2_) || !canEdit) {
			return true;
		} else if (SharedConstants.isAllowedCharacter(p_charTyped_1_)) {
			this.insertTextIntoPage(Character.toString(p_charTyped_1_));
			return true;
		} else {
			return false;
		}
	}
	
	private void sendTabletToServer()
	{
		if(isModified && text != null)
			MSPacketHandler.sendToServer(new StoneTabletPacket(text, hand));
	}
	
	/**
	 * Returns a copy of the input string with character 127 (del) and character 167 (section sign) removed
	 */
	private String removeUnprintableChars(String text) {
		StringBuilder stringbuilder = new StringBuilder();
		
		for(char c0 : text.toCharArray()) {
			if (c0 != 167 && c0 != 127) {
				stringbuilder.append(c0);
			}
		}
		
		return stringbuilder.toString();
	}
	
	private void setText(String text) {
			this.text = text;
			this.isModified = true;
	}
	
	/**
	 * Inserts text into the current page at the between selectionStart and selectionEnd (erasing any highlighted text)
	 *
	 * @param text The text to insert
	 */
	private void insertTextIntoPage(String text) {
		if (this.selectionStart != this.selectionEnd) {
			this.removeSelectedText();
		}
		
		String s = this.text;
		this.selectionEnd = MathHelper.clamp(this.selectionEnd, 0, s.length());
		String s1 = (new StringBuilder(s)).insert(this.selectionEnd, text).toString();
		int i = this.font.getWordWrappedHeight(s1 + "" + TextFormatting.BLACK + "_", 114);
		if (i <= 128 && s1.length() < 1024) {
			this.setText(s1);
			this.selectionStart = this.selectionEnd = Math.min(this.text.length(), this.selectionEnd + text.length());
		}
		
	}
	
	/**
	 * Returns any selected text on the current page
	 */
	private String getSelectedText() {
		String s = this.text;
		int i = Math.min(this.selectionEnd, this.selectionStart);
		int j = Math.max(this.selectionEnd, this.selectionStart);
		return s.substring(i, j);
	}
	
	/**
	 * Returns the width of text
	 */
	private int getTextWidth(String text) {
		return this.font.getStringWidth(this.font.getBidiFlag() ? this.font.bidiReorder(text) : text);
	}
	
	public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
		if (p_mouseClicked_5_ == 0) {
			long i = Util.milliTime();
			String s = this.text;
			if (!s.isEmpty()) {
				Point point = new Point((int)p_mouseClicked_1_, (int)p_mouseClicked_3_);
				this.func_214210_b(point);
				this.func_214227_a(point);
				int j = this.func_214203_a(s, point);
				if (j >= 0) {
					if (i - this.lastClickTime < 250L) {
						if (this.selectionStart == this.selectionEnd) {
							this.selectionStart = this.font.func_216863_a(s, -1, j, false);
							this.selectionEnd = this.font.func_216863_a(s, 1, j, false);
						} else {
							this.selectionStart = 0;
							this.selectionEnd = this.text.length();
						}
					} else {
						this.selectionEnd = j;
						if (!Screen.hasShiftDown()) {
							this.selectionStart = this.selectionEnd;
						}
					}
				}
			}
			
			this.lastClickTime = i;
		}
		
		return super.mouseClicked(p_mouseClicked_1_, p_mouseClicked_3_, p_mouseClicked_5_);
	}
	
	public boolean mouseDragged(double p_mouseDragged_1_, double p_mouseDragged_3_, int p_mouseDragged_5_, double p_mouseDragged_6_, double p_mouseDragged_8_) {
		if (p_mouseDragged_5_ == 0) {
			String s = this.text;
			Point point = new Point((int)p_mouseDragged_1_, (int)p_mouseDragged_3_);
			this.func_214210_b(point);
			this.func_214227_a(point);
			int i = this.func_214203_a(s, point);
			if (i >= 0) {
				this.selectionEnd = i;
			}
		}
		
		return super.mouseDragged(p_mouseDragged_1_, p_mouseDragged_3_, p_mouseDragged_5_, p_mouseDragged_6_, p_mouseDragged_8_);
	}
	
	/**
	 * Handles keypresses, clipboard functions, and page turning
	 */
	private boolean keyPressedInBook(int keyCode, int scanCode, int modifiers) {
		String s = text;
		if (Screen.isSelectAll(keyCode)) {
			this.selectionStart = 0;
			this.selectionEnd = s.length();
			return true;
		} else if (Screen.isCopy(keyCode)) {
			this.minecraft.keyboardListener.setClipboardString(this.getSelectedText());
			return true;
		} else if (Screen.isPaste(keyCode)) {
			this.insertTextIntoPage(this.removeUnprintableChars(TextFormatting.getTextWithoutFormattingCodes(this.minecraft.keyboardListener.getClipboardString().replaceAll("\\r", ""))));
			this.selectionStart = this.selectionEnd;
			return true;
		} else if (Screen.isCut(keyCode)) {
			this.minecraft.keyboardListener.setClipboardString(this.getSelectedText());
			this.removeSelectedText();
			return true;
		} else {
			switch(keyCode) {
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
	private void backspacePressed(String pageText) {
		if (!pageText.isEmpty()) {
			if (this.selectionStart != this.selectionEnd) {
				this.removeSelectedText();
			} else if (this.selectionEnd > 0) {
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
	private void deletePressed(String pageText) {
		if (!pageText.isEmpty()) {
			if (this.selectionStart != this.selectionEnd) {
				this.removeSelectedText();
			} else if (this.selectionEnd < pageText.length()) {
				String s = (new StringBuilder(pageText)).deleteCharAt(Math.max(0, this.selectionEnd)).toString();
				this.setText(s);
			}
		}
		
	}
	
	/**
	 * Called when the left directional arrow on the keyboard is pressed
	 */
	private void leftPressed(String pageText) {
		int i = this.font.getBidiFlag() ? 1 : -1;
		if (Screen.hasControlDown()) {
			this.selectionEnd = this.font.func_216863_a(pageText, i, this.selectionEnd, true);
		} else {
			this.selectionEnd = Math.max(0, this.selectionEnd + i);
		}
		
		if (!Screen.hasShiftDown()) {
			this.selectionStart = this.selectionEnd;
		}
		
	}
	
	/**
	 * Called when the right directional arrow on the keyboard is pressed
	 */
	private void rightPressed(String pageText) {
		int i = this.font.getBidiFlag() ? -1 : 1;
		if (Screen.hasControlDown()) {
			this.selectionEnd = this.font.func_216863_a(pageText, i, this.selectionEnd, true);
		} else {
			this.selectionEnd = Math.min(pageText.length(), this.selectionEnd + i);
		}
		
		if (!Screen.hasShiftDown()) {
			this.selectionStart = this.selectionEnd;
		}
		
	}
	
	/**
	 * Called when the up directional arrow on the keyboard is pressed
	 */
	private void upPressed(String pageText) {
		if (!pageText.isEmpty()) {
			Point point = this.func_214194_c(pageText, this.selectionEnd);
			if (point.y == 0) {
				this.selectionEnd = 0;
				if (!Screen.hasShiftDown()) {
					this.selectionStart = this.selectionEnd;
				}
			} else {
				int i = this.func_214203_a(pageText, new Point(point.x + this.func_214206_a(pageText, this.selectionEnd) / 3, point.y - 9));
				if (i >= 0) {
					this.selectionEnd = i;
					if (!Screen.hasShiftDown()) {
						this.selectionStart = this.selectionEnd;
					}
				}
			}
		}
		
	}
	
	/**
	 * Called when the down arrow on the keyboard is pressed
	 */
	private void downPressed(String pageText) {
		if (!pageText.isEmpty()) {
			Point point = this.func_214194_c(pageText, this.selectionEnd);
			int i = this.font.getWordWrappedHeight(pageText + "" + TextFormatting.BLACK + "_", 114);
			if (point.y + 9 == i) {
				this.selectionEnd = pageText.length();
				if (!Screen.hasShiftDown()) {
					this.selectionStart = this.selectionEnd;
				}
			} else {
				int j = this.func_214203_a(pageText, new Point(point.x + this.func_214206_a(pageText, this.selectionEnd) / 3, point.y + 9));
				if (j >= 0) {
					this.selectionEnd = j;
					if (!Screen.hasShiftDown()) {
						this.selectionStart = this.selectionEnd;
					}
				}
			}
		}
		
	}
	
	/**
	 * Called when the home button on the keyboard is pressed
	 */
	private void homePressed(String pageText) {
		this.selectionEnd = this.func_214203_a(pageText, new Point(0, this.func_214194_c(pageText, this.selectionEnd).y));
		if (!Screen.hasShiftDown()) {
			this.selectionStart = this.selectionEnd;
		}
		
	}
	
	/**
	 * Called when the end button on the keyboard is pressed
	 */
	private void endPressed(String pageText) {
		this.selectionEnd = this.func_214203_a(pageText, new Point(113, this.func_214194_c(pageText, this.selectionEnd).y));
		if (!Screen.hasShiftDown()) {
			this.selectionStart = this.selectionEnd;
		}
		
	}
	
	/**
	 * Removes the text between selectionStart and selectionEnd from the currrent page
	 */
	private void removeSelectedText() {
		if (this.selectionStart != this.selectionEnd) {
			String s = this.text;
			int i = Math.min(this.selectionEnd, this.selectionStart);
			int j = Math.max(this.selectionEnd, this.selectionStart);
			String s1 = s.substring(0, i) + s.substring(j);
			this.selectionEnd = i;
			this.selectionStart = this.selectionEnd;
			this.setText(s1);
		}
	}
	
	//I'm not entirely sure about what these do
	
	private int func_214206_a(String p_214206_1_, int p_214206_2_) {
		return (int)this.font.getCharWidth(p_214206_1_.charAt(MathHelper.clamp(p_214206_2_, 0, p_214206_1_.length() - 1)));
	}
	
	private int func_214216_b(String p_214216_1_, int p_214216_2_) {
		return this.font.sizeStringToWidth(p_214216_1_, p_214216_2_);
	}
	
	private Point func_214194_c(String pageText, int p_214194_2_) {
		Point point = new Point();
		int i = 0;
		int j = 0;
		
		for(String s = pageText; !s.isEmpty(); j = i) {
			int k = this.func_214216_b(s, 114);
			if (s.length() <= k) {
				String s3 = s.substring(0, Math.min(Math.max(p_214194_2_ - j, 0), s.length()));
				point.x = point.x + this.getTextWidth(s3);
				break;
			}
			
			String s1 = s.substring(0, k);
			char c0 = s.charAt(k);
			boolean flag = c0 == ' ' || c0 == '\n';
			s = TextFormatting.getFormatString(s1) + s.substring(k + (flag ? 1 : 0));
			i += s1.length() + (flag ? 1 : 0);
			if (i - 1 >= p_214194_2_) {
				String s2 = s1.substring(0, Math.min(Math.max(p_214194_2_ - j, 0), s1.length()));
				point.x = point.x + this.getTextWidth(s2);
				break;
			}
			
			point.y = point.y + 9;
		}
		
		return point;
	}
	
	private void func_214227_a(Point p_214227_1_) {
		if (this.font.getBidiFlag()) {
			p_214227_1_.x = 114 - p_214227_1_.x;
		}
		
	}
	
	private void func_214210_b(Point p_214210_1_) {
		p_214210_1_.x = p_214210_1_.x - (this.width - 192) / 2 - 36;
		p_214210_1_.y = p_214210_1_.y - 32;
	}
	
	private void func_214224_c(Point p_214224_1_) {
		p_214224_1_.x = p_214224_1_.x + (this.width - 192) / 2 + 36;
		p_214224_1_.y = p_214224_1_.y + 32;
	}
	
	private int func_214226_d(String p_214226_1_, int p_214226_2_) {
		if (p_214226_2_ < 0) {
			return 0;
		} else {
			float f1 = 0.0F;
			boolean flag = false;
			String s = p_214226_1_ + " ";
			
			for(int i = 0; i < s.length(); ++i) {
				char c0 = s.charAt(i);
				float f2 = this.font.getCharWidth(c0);
				if (c0 == 167 && i < s.length() - 1) {
					++i;
					c0 = s.charAt(i);
					if (c0 != 'l' && c0 != 'L') {
						if (c0 == 'r' || c0 == 'R') {
							flag = false;
						}
					} else {
						flag = true;
					}
					
					f2 = 0.0F;
				}
				
				float f = f1;
				f1 += f2;
				if (flag && f2 > 0.0F) {
					++f1;
				}
				
				if ((float)p_214226_2_ >= f && (float)p_214226_2_ < f1) {
					return i;
				}
			}
			
			if ((float)p_214226_2_ >= f1) {
				return s.length() - 1;
			} else {
				return -1;
			}
		}
	}
	
	private int func_214203_a(String p_214203_1_, Point p_214203_2_) {
		int i = 16 * 9;
		if (p_214203_2_.y > i) {
			return -1;
		} else {
			int j = Integer.MIN_VALUE;
			int k = 9;
			int l = 0;
			
			for(String s = p_214203_1_; !s.isEmpty() && j < i; k += 9) {
				int i1 = this.func_214216_b(s, 114);
				if (i1 < s.length()) {
					String s1 = s.substring(0, i1);
					if (p_214203_2_.y >= j && p_214203_2_.y < k) {
						int k1 = this.func_214226_d(s1, p_214203_2_.x);
						return k1 < 0 ? -1 : l + k1;
					}
					
					char c0 = s.charAt(i1);
					boolean flag = c0 == ' ' || c0 == '\n';
					s = TextFormatting.getFormatString(s1) + s.substring(i1 + (flag ? 1 : 0));
					l += s1.length() + (flag ? 1 : 0);
				} else if (p_214203_2_.y >= j && p_214203_2_.y < k) {
					int j1 = this.func_214226_d(s, p_214203_2_.x);
					return j1 < 0 ? -1 : l + j1;
				}
				
				j = k;
			}
			
			return p_214203_1_.length();
		}
	}
	
	//Pointer Class
	@OnlyIn(Dist.CLIENT)
	class Point {
		private int x;
		private int y;
		
		Point() {
		}
		
		Point(int xIn, int yIn) {
			this.x = xIn;
			this.y = yIn;
		}
	}
}
