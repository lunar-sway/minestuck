package com.mraof.minestuck.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mraof.minestuck.MinestuckConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.client.gui.widget.ExtendedButton;
import org.lwjgl.glfw.GLFW;

public class DialogueScreen extends Screen
{
	public static final String TITLE = "minestuck.dialogue";
	private final DialogueBoxType dialogueBoxType;
	
	private static final int GUI_WIDTH = 256;
	private static final int GUI_HEIGHT = 180;
	private static final int PORTRAIT_SIZE = 32;
	
	private static final int RESPONSE_TEXT_OFFSET = 40;
	
	private String[] dialogueTexts;
	private String[] responseOptions;
	private ResourceLocation portrait;
	private String renderedText;
	
	private int currentTextIndex;
	private boolean doneWriting;
	private int frame;
	private int playerTextColor;
	
	public enum DialogueBoxType
	{
		STANDARD(new ResourceLocation("minestuck", "textures/gui/dialogue.png")),
		DARK(new ResourceLocation("minestuck", "textures/gui/dialogue_dark.png")); //same as dialogue but better fitted for light colored texts like iguanas
		
		public final ResourceLocation background;
		
		DialogueBoxType(ResourceLocation background)
		{
			this.background = background;
		}
		
		public final ResourceLocation getTextBackgroundBox()
		{
			return background;
		}
	}
	
	public DialogueScreen(String[] dialogueTexts, ResourceLocation portrait, String[] responseOptions, int color, DialogueBoxType dialogueBoxType)
	{
		super(new TranslationTextComponent(TITLE));
		this.dialogueTexts = dialogueTexts;
		this.portrait = portrait;
		this.currentTextIndex = 0;
		this.responseOptions = responseOptions;
		this.playerTextColor = color;
		this.dialogueBoxType = dialogueBoxType;
		resetWriter();
	}
	
	@Override
	public void render(int mouseX, int mouseY, float partialTicks)
	{
		this.renderBackground();
		
		int xOffset = (width - GUI_WIDTH) / 2;
		int yOffset = (height - GUI_HEIGHT) / 2;
		
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		
		this.minecraft.getTextureManager().bindTexture(dialogueBoxType.getTextBackgroundBox());
		this.blit(xOffset, yOffset, 0, 0, GUI_WIDTH, GUI_HEIGHT);
		
		int leftStart = xOffset + PORTRAIT_SIZE + 16;
		int topStart = yOffset + GUI_HEIGHT + 8;
		int lineHeight = 10;
		
		font.drawSplitString(renderedText, leftStart, yOffset + 12, GUI_WIDTH - PORTRAIT_SIZE - 28, 0x000000);
		
		this.minecraft.getTextureManager().bindTexture(portrait);
		this.blit(xOffset + 8, yOffset + 44, getAnimationOffset() * PORTRAIT_SIZE, 0, PORTRAIT_SIZE, PORTRAIT_SIZE);
		
		super.render(mouseX, mouseY, partialTicks);
	}
	
	public void test() {
		close();
	}
	
	@Override
	public void tick()
	{
		if(!doneWriting)
		{
			String text = dialogueTexts[currentTextIndex];
			int amount = Math.min(frame * MinestuckConfig.CLIENT.dialogueSpeed.get(), text.length());
			
			if(amount == text.length())
			{
				doneWriting = true;
			}
			
			renderedText = text.substring(0, amount);
			frame++;
		}
	}
	
	@Override
	public boolean keyPressed(int keyCode, int scanCode, int i)
	{
		if(keyCode == GLFW.GLFW_KEY_SPACE) {
			if(!this.doneWriting) {
				renderedText = dialogueTexts[currentTextIndex];
				doneWriting = true;
				return false;
			}
			else if(currentTextIndex < dialogueTexts.length - 1) {
				currentTextIndex++;
				resetWriter();
				return false;
			}
			else {
				showOptions();
			}
		}
		
		if(keyCode == GLFW.GLFW_KEY_UP || keyCode == GLFW.GLFW_KEY_W)
		{
			if(!this.changeFocus(false))
			{
				this.changeFocus(false);
			}
			return true;
		}
		
		if(keyCode == GLFW.GLFW_KEY_DOWN || keyCode == GLFW.GLFW_KEY_S)
		{
			if(!this.changeFocus(true))
			{
				this.changeFocus(true);
			}
			return true;
		}
		
		return super.keyPressed(keyCode, scanCode, i);
	}
	
	private void showOptions() {
		int xOffset = ((width - GUI_WIDTH) / 2) + PORTRAIT_SIZE + 16;
		int yOffset = ((height - GUI_HEIGHT) / 2) + GUI_HEIGHT + 8;
		
		for (int i = 0; i < responseOptions.length; i++) {
			addButton(new DialogueButton(xOffset, yOffset + (10 * i), GUI_WIDTH, 10, responseOptions[i], btn -> test()));
		}
		this.changeFocus(true);
	}
	
	private int getAnimationOffset()
	{
		if(!this.doneWriting)
			return (this.frame % 4) / 2;
		
		return 0;
	}
	
	private void resetWriter()
	{
		doneWriting = false;
		renderedText = "";
		frame = 1;
	}
	
	private void close()
	{
		this.minecraft.displayGuiScreen((Screen) null);
	}
	
	class DialogueButton extends ExtendedButton
	{
		
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
			
			if(isHovered)
			{
				color = playerTextColor;
			}
			
			if(isFocused())
			{
				buttonText = "> " + buttonText;
				color = playerTextColor;
				offset = 9;
			}
			
			this.drawString(mc.fontRenderer, buttonText, this.x - offset, this.y, color);
		}
	}
}
