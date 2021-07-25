package com.mraof.minestuck.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import org.lwjgl.glfw.GLFW;

import java.util.List;

public class DialogueScreen extends Screen
{
	public static final String TITLE = "minestuck.dialogue";
	private static final ResourceLocation TEXT_BACKGROUND = new ResourceLocation("minestuck", "textures/gui/dialogue.png");
	
	private static final int WRITE_SPEED = 6;
	private static final int GUI_WIDTH = 256;
	private static final int GUI_HEIGHT = 90;
	
	private List<String> paragraphs;
	private String renderedText;
	
	private int currentTextIndex;
	private boolean doneWriting;
	protected int frame;
	
	protected DialogueScreen(List<String> paragraphs)
	{
		super(new TranslationTextComponent(TITLE));
		this.paragraphs = paragraphs;
		this.currentTextIndex = 0;
		resetWriter();
	}
	
	@Override
	public void render(int mouseX, int mouseY, float partialTicks)
	{
		this.renderBackground();
		
		int xOffset = getXOffset();
		int yOffset = getYOffset();
		
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		
		this.minecraft.getTextureManager().bindTexture(TEXT_BACKGROUND);
		this.blit(xOffset, yOffset, 0, 0, GUI_WIDTH, GUI_HEIGHT);
		
		font.drawSplitString(renderedText, xOffset + 12, yOffset + 10, GUI_WIDTH - 20, 0x000000);
		super.render(mouseX, mouseY, partialTicks);
	}
	
	@Override
	public boolean keyPressed(int keyCode, int scanCode, int i)
	{
		if(keyCode == GLFW.GLFW_KEY_SPACE)
			skip();
		
		return super.keyPressed(keyCode, scanCode, i);
	}
	
	@Override
	public void tick() {
		if(!doneWriting) {
			String text = paragraphs.get(currentTextIndex);
			int amount = Math.min(frame * WRITE_SPEED, text.length());
			
			if (amount == text.length()) {
				doneWriting = true;
			}
			
			renderedText = text.substring(0, amount);
			frame++;
		}
	}
	
	protected int getXOffset() {
		return (width - GUI_WIDTH) / 2;
	}
	
	protected int getYOffset() {
		return (height - GUI_HEIGHT) / 2;
	}
	
	private void resetWriter() {
		doneWriting = false;
		renderedText = "";
		frame = 1;
	}
	
	private void skip() {
		if(!doneWriting) {
			renderedText = paragraphs.get(currentTextIndex);
			doneWriting = true;
		}
		else if(currentTextIndex >= paragraphs.size() - 1) {
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
}
