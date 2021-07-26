package com.mraof.minestuck.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mraof.minestuck.computer.ComputerProgram;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.client.gui.GuiUtils;
import net.minecraftforge.fml.client.gui.widget.ExtendedButton;
import org.lwjgl.glfw.GLFW;

import java.util.List;
import java.util.Map;

public class DialogueScreen extends Screen
{
	public static final String TITLE = "minestuck.dialogue";
	private static final ResourceLocation TEXT_BACKGROUND = new ResourceLocation("minestuck", "textures/gui/dialogue.png");
	
	private static final int WRITE_SPEED = 6;
	
	private static final int GUI_WIDTH = 256;
	private static final int GUI_HEIGHT = 90;
	private static final int PORTRAIT_SIZE = 32;
	
	private String[] paragraphs;
	private String[] options;
	private ResourceLocation portrait;
	private String renderedText;
	
	private int currentTextIndex;
	private boolean doneWriting;
	private int frame;
	
	public DialogueScreen(String[] paragraphs, ResourceLocation portrait, String[] options)
	{
		super(new TranslationTextComponent(TITLE));
		this.paragraphs = paragraphs;
		this.portrait = portrait;
		this.options = options;
		this.currentTextIndex = 0;
		resetWriter();
	}
	
	@Override
	public void render(int mouseX, int mouseY, float partialTicks)
	{
		this.renderBackground();
		
		int xOffset = (width - GUI_WIDTH) / 2;
		int yOffset = (height - GUI_HEIGHT) / 2;
		
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		
		this.minecraft.getTextureManager().bindTexture(TEXT_BACKGROUND);
		this.blit(xOffset, yOffset, 0, 0, GUI_WIDTH, GUI_HEIGHT);
		
		int leftStart = xOffset + PORTRAIT_SIZE + 16;
		int topStart = yOffset + GUI_HEIGHT + 8;
		int lineHeight = 10;
		
		font.drawSplitString(renderedText, leftStart, yOffset + 12, GUI_WIDTH - PORTRAIT_SIZE - 28, 0x000000);
		
		this.minecraft.getTextureManager().bindTexture(portrait);
		this.blit(xOffset + 8, yOffset + 44, getAnimationOffset() * PORTRAIT_SIZE, 0, PORTRAIT_SIZE, PORTRAIT_SIZE);
		
		super.render(mouseX, mouseY, partialTicks);
	}
	
	@Override
	public void init() {
		super.init();
		
		int xOffset = ((width - GUI_WIDTH) / 2) + PORTRAIT_SIZE + 16;
		int yOffset = ((height - GUI_HEIGHT) / 2) + GUI_HEIGHT + 8;
		
		for (int i = 0; i < options.length; i++) {
			addButton(new DialogueButton(xOffset, yOffset + (10 * i), GUI_WIDTH, 10, options[i], btn -> test()));
		}
		
		this.changeFocus(true);
	}
	
	public void test() {
	
	}
	
	@Override
	public void tick() {
		if(!doneWriting) {
			String text = paragraphs[currentTextIndex];
			int amount = Math.min(frame * WRITE_SPEED, text.length());
			
			if (amount == text.length()) {
				doneWriting = true;
			}
			
			renderedText = text.substring(0, amount);
			frame++;
		}
	}
	
	@Override
	public boolean keyPressed(int keyCode, int scanCode, int i)
	{
		if(keyCode == GLFW.GLFW_KEY_SPACE && !this.doneWriting) {
			skip();
			return false;
		}
		
		if(keyCode == GLFW.GLFW_KEY_UP || keyCode == GLFW.GLFW_KEY_W) {
			if (!this.changeFocus(false)) {
				this.changeFocus(false);
			}
			return true;
		}
		
		if(keyCode == GLFW.GLFW_KEY_DOWN || keyCode == GLFW.GLFW_KEY_S) {
			if (!this.changeFocus(true)) {
				this.changeFocus(true);
			}
			return true;
		}
		
		return super.keyPressed(keyCode, scanCode, i);
	}
	
	private int getAnimationOffset() {
		if(!this.doneWriting)
			return (this.frame % 4) / 2;
		
		return 0;
	}
	
	private void resetWriter() {
		doneWriting = false;
		renderedText = "";
		frame = 1;
	}
	
	private void skip() {
		if(!doneWriting) {
			renderedText = paragraphs[currentTextIndex];
			doneWriting = true;
		}
		else if(currentTextIndex >= paragraphs.length - 1) {
			close();
		}
		else {
			currentTextIndex++;
			resetWriter();
		}
	}
	
	private void close() {
		this.minecraft.displayGuiScreen((Screen)null);
	}
	
	class DialogueButton extends ExtendedButton {
		
		DialogueButton(int xPos, int yPos, int width, int height, String displayString, IPressable handler)
		{
			super(xPos, yPos, width, height, displayString, handler);
		}
		
		@Override
		public void renderButton(int mouseX, int mouseY, float partial)
		{
			Minecraft mc = Minecraft.getInstance();
			String buttonText = this.getMessage();
			int color = 0xAFAFAF;
			int offset = 0;
			
			if(isHovered) {
				color = 0xFFFFFF;
			}
			
			if (isFocused()) {
				buttonText = "> " + buttonText;
				color = 0xFFFFFF;
				offset = 0;
			}
			this.drawString(mc.fontRenderer, buttonText, this.x - offset, this.y, color);
		}
	}
}
