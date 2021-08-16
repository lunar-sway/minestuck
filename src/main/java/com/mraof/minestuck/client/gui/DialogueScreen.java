package com.mraof.minestuck.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.entity.consort.DialogueCard;
import com.mraof.minestuck.network.DialogueOptionPacket;
import com.mraof.minestuck.network.MSPacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.client.gui.widget.ExtendedButton;
import org.lwjgl.glfw.GLFW;

import java.util.List;

public class DialogueScreen extends Screen
{
	private static final ResourceLocation BACKGROUND = new ResourceLocation("minestuck", "textures/gui/dialogue.png");
	
	private static final int GUI_WIDTH = 256;
	private static final int GUI_HEIGHT = 106;
	private static final int PORTRAIT_SIZE = 32;
	
	private final List<DialogueCard> dialogueCards;
	private final List<String> responseOptions;
	
	private String renderedText;
	private int currentCardIndex;
	private boolean doneWriting;
	private int frame;
	
	public DialogueScreen(List<DialogueCard> dialogueCards, List<String> responseOptions)
	{
		super(new TranslationTextComponent("minestuck.dialogue"));
		this.dialogueCards = dialogueCards;
		this.currentCardIndex = 0;
		this.responseOptions = responseOptions;
		resetWriter();
	}
	
	@Override
	public void render(int mouseX, int mouseY, float partialTicks)
	{
		this.renderBackground();
		
		int xOffset = (width - GUI_WIDTH) / 2;
		int yOffset = (height - GUI_HEIGHT) / 2;
		int leftStart = xOffset + PORTRAIT_SIZE + 16;
		
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.minecraft.getTextureManager().bindTexture(BACKGROUND);
		this.blit(xOffset, yOffset, 0, 0, GUI_WIDTH, GUI_HEIGHT);
		
		DialogueCard currentDialogue = dialogueCards.get(currentCardIndex);
		font.drawSplitString(renderedText, leftStart, yOffset + 12, GUI_WIDTH - PORTRAIT_SIZE - 28, currentDialogue.getTextColor());
		ResourceLocation portrait = currentDialogue.getPortraitResource();
		
		if(portrait != null)
		{
			this.minecraft.getTextureManager().bindTexture(portrait);
			this.blit(xOffset + 8, yOffset + GUI_HEIGHT - PORTRAIT_SIZE - 8, getAnimationOffset() * PORTRAIT_SIZE, 0, PORTRAIT_SIZE, PORTRAIT_SIZE);
		}
		
		super.render(mouseX, mouseY, partialTicks);
	}
	
	@Override
	public void tick()
	{
		if(!doneWriting)
		{
			String text = dialogueCards.get(currentCardIndex).getText();
			int amount = Math.min(frame * MinestuckConfig.CLIENT.dialogueSpeed.get(), text.length());
			
			if(amount == text.length())
			{
				doneWriting = true;
				if(currentCardIndex >= dialogueCards.size() - 1)
				{
					addOptions();
				}
			}
			
			renderedText = text.substring(0, amount);
			frame++;
		}
	}
	
	@Override
	public boolean keyPressed(int keyCode, int scanCode, int i)
	{
		if(keyCode == GLFW.GLFW_KEY_SPACE)
		{
			if(!this.doneWriting)
			{
				renderedText = dialogueCards.get(currentCardIndex).getText();
				doneWriting = true;
				if(currentCardIndex >= dialogueCards.size() - 1)
				{
					addOptions();
				}
				
				return false;
			} else if(currentCardIndex < dialogueCards.size() - 1)
			{
				currentCardIndex++;
				resetWriter();
				return false;
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
	
	private void addOptions()
	{
		int xOffset = ((width - GUI_WIDTH) / 2) + PORTRAIT_SIZE + 16;
		int yOffset = ((height - GUI_HEIGHT) / 2) + GUI_HEIGHT + 8;
		
		for(int i = 0; i < responseOptions.size(); i++)
		{
			int optionIndex = i;
			Button.IPressable lambda = btn -> {
				MSPacketHandler.sendToServer(new DialogueOptionPacket(optionIndex));
				close();
			};
			addButton(new DialogueButton(xOffset, yOffset + (10 * i), GUI_WIDTH, 10, responseOptions.get(i), lambda));
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
	
	public void close()
	{
		this.minecraft.displayGuiScreen(null);
	}
	
	static class DialogueButton extends ExtendedButton
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
				color = 0xFFFFFF;
			}
			
			if(isFocused())
			{
				buttonText = "> " + buttonText;
				color = 0xFFFFFF;
				offset = 9;
			}
			
			this.drawString(mc.fontRenderer, buttonText, this.x - offset, this.y, color);
		}
	}
}
