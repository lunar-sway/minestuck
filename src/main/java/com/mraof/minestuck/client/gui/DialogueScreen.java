package com.mraof.minestuck.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mraof.minestuck.util.Dialogue;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public class DialogueScreen extends Screen
{
	private static final int GUI_WIDTH = 224;
	private static final int GUI_HEIGHT = 176;
	
	private final ResourceLocation guiBackground;
	
	private final LivingEntity entity;
	private final Dialogue dialogue;
	
	private int xOffset;
	private int yOffset;
	
	private Button doneButton;
	
	DialogueScreen(LivingEntity entity, Dialogue dialogue)
	{
		super(Component.empty());
		
		this.entity = entity;
		this.guiBackground = dialogue.getGuiPath();
		this.dialogue = dialogue;
	}

	@Override
	public void init()
	{
		yOffset = (this.height / 2) - (GUI_HEIGHT / 2);
		xOffset = (this.width / 2) - (GUI_WIDTH / 2);
		
		//addRenderableWidget(doneButton = new ExtendedButton(this.width / 2 - 20, yOffset + 50, 40, 20, Component.translatable(DONE_MESSAGE), button -> finish()));
		//doneButton.active = destinationTextField.getValue().length() == 4;
	}
	
	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks)
	{
		this.renderBackground(guiGraphics);
		
		RenderSystem.setShaderColor(1, 1, 1, 1);
		
		if(guiBackground != null)
			guiGraphics.blit(guiBackground, xOffset, yOffset, 0, 0, GUI_WIDTH, GUI_HEIGHT);
		
		String dialogueMessage = dialogue.getMessage();
		if(dialogueMessage != null && !dialogueMessage.isEmpty())
		{
			Component entityMessage = entity.getDisplayName().plainCopy().append(" : ").append(Component.translatable(dialogueMessage));
			guiGraphics.drawString(font, entityMessage, (int) ((this.width / 2F) - font.width(entityMessage) / 2F), yOffset + 40, 0x000000, false);
		}
		
		List<Dialogue.Response> dialogueResponses = dialogue.getResponses();
		if(dialogueResponses != null && !dialogueResponses.isEmpty())
		{
			int i = 1;
			for(Dialogue.Response response : dialogueResponses)
			{
				String dialogueResponse = response.getResponse();
				guiGraphics.drawString(font, Component.translatable(dialogueResponse), (int) ((this.width / 2F) - font.width(Component.translatable(dialogueResponse)) / 2F), yOffset + 60 + (i * 10), 0xFFF000, false);
				i++;
			}
		}
		 
		
		super.render(guiGraphics, mouseX, mouseY, partialTicks);
	}

	private void finish()
	{
		/*if(this.destinationTextField.getValue().length() == 4)
		{
			//Debug.print("Sending transportalizer packet with destination of " + this.destinationTextField.getText());
			TransportalizerPacket packet = new TransportalizerPacket(be.getBlockPos(), destinationTextField.getValue().toUpperCase());
			MSPacketHandler.sendToServer(packet);
			this.minecraft.setScreen(null);
		}*/
	}
	
	@Override
	public boolean isPauseScreen()
	{
		return false;
	}
}