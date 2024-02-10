package com.mraof.minestuck.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mraof.minestuck.entity.consort.ConsortEntity;
import com.mraof.minestuck.util.Dialogue;
import com.mraof.minestuck.util.DialogueManager;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.client.gui.widget.ExtendedButton;

import java.util.ArrayList;
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
	
	private final List<Button> responseButtons = new ArrayList<>();
	
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
		
		recreateResponseButtons();
		//addRenderableWidget(doneButton = new ExtendedButton(this.width / 2 - 20, yOffset + 50, 40, 20, Component.translatable(DONE_MESSAGE), button -> finish()));
		//doneButton.active = destinationTextField.getValue().length() == 4;
	}
	
	public void recreateResponseButtons()
	{
		responseButtons.forEach(this::removeWidget);
		responseButtons.clear();
		
		List<Dialogue.Response> responses = dialogue.getResponses();
		
		for(int i = 0; i < responses.size(); i++)
		{
			String response = responses.get(i).getResponse();
			int yPositionOffset = 20 * i;
			
			Component buttonComponent = Component.translatable(response);
			
			ExtendedButton entryButton = new ExtendedButton(xOffset + 20, yOffset + 40 + yPositionOffset, 190, 14, buttonComponent,
					button -> clickResponse(response));
			responseButtons.add(addRenderableWidget(entryButton));
		}
	}
	
	private void clickResponse(String responseMessage)
	{
		for(Dialogue.Response response : dialogue.getResponses())
		{
			if(response.getResponse().equals(responseMessage))
			{
				Dialogue nextDialogue = DialogueManager.getInstance().getDialogue(response.getNextDialoguePath());
				
				if(nextDialogue != null)
				{
					onClose();
					MSScreenFactories.displayDialogueScreen(entity, nextDialogue);
					break;
				}
			}
		}
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
			MutableComponent entityName = entity.getDisplayName().plainCopy();
			
			if(entity instanceof ConsortEntity consortEntity)
			{
				entityName.withStyle(consortEntity.getConsortType().getColor());
			}
			
			Component entityMessage = entityName.append(": ").append(Component.translatable(dialogueMessage));
			guiGraphics.drawWordWrap(font, entityMessage, xOffset + 10, yOffset + 20, 210, 0x000000);
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