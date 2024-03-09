package com.mraof.minestuck.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mraof.minestuck.entity.consort.ConsortEntity;
import com.mraof.minestuck.entity.dialogue.Dialogue;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.network.ResponseTriggerPacket;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.client.gui.widget.ExtendedButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Displays a screen when interacting with any dialogue capable entity that has a valid dialogue.
 * It is configurable in terms of what the gui image is, what sort of animated talk sprite shows up, and how many response options there are.
 */
public class DialogueScreen extends Screen
{
	private static final int GUI_WIDTH = 224;
	private static final int GUI_HEIGHT = 176;
	
	private final ResourceLocation guiBackground;
	
	private final LivingEntity entity;
	private final Dialogue dialogue;
	private final Dialogue.DialogueData dialogueData;
	
	private int xOffset;
	private int yOffset;
	
	private final List<Button> responseButtons = new ArrayList<>();
	
	DialogueScreen(LivingEntity entity, Dialogue dialogue, CompoundTag dialogueData)
	{
		super(Component.empty());
		
		this.entity = entity;
		this.guiBackground = dialogue.guiPath();
		this.dialogue = dialogue;
		this.dialogueData = Dialogue.DialogueData.read(dialogueData);
	}
	
	@Override
	public void init()
	{
		//TODO static gui height/width may not make sense with customizable gui sizing
		yOffset = (this.height / 2) - (GUI_HEIGHT / 2);
		xOffset = (this.width / 2) - (GUI_WIDTH / 2);
		
		recreateResponseButtons();
	}
	
	public void recreateResponseButtons()
	{
		responseButtons.forEach(this::removeWidget);
		responseButtons.clear();
		
		List<Dialogue.Response> filteredResponses = new ArrayList<>();
		
		//removes responses if they fail their conditions and should be hidden when that happens
		for(Dialogue.Response response : dialogue.responses())
		{
			Dialogue.ResponseData data = this.dialogueData.responsesMap().get(response.response().message());
			if(data == null)
				continue;
			
			filteredResponses.add(response);
		}
		
		for(int i = 0; i < filteredResponses.size(); i++)
		{
			Dialogue.Response response = filteredResponses.get(i);
			String responseMessage = response.response().message();
			int yPositionOffset = 20 * i;
			
			Dialogue.ResponseData data = this.dialogueData.responsesMap().get(responseMessage);
			if(data == null)
				continue;
			
			Component buttonComponent = Component.translatable(responseMessage, data.arguments());
			
			ExtendedButton entryButton = new ExtendedButton(xOffset + 20, yOffset + 40 + yPositionOffset, 190, 14, buttonComponent,
					button -> clickResponse(data.index()));
			
			data.conditionFailure().ifPresent(failure -> {
				entryButton.setTooltip(Tooltip.create(conditionFailMessage(failure.causes())));
				entryButton.active = false;
			});
			
			responseButtons.add(addRenderableWidget(entryButton));
		}
	}
	
	private static MutableComponent conditionFailMessage(List<String> causes)
	{
		//TODO Does not make sense linguistically with a hard coded failure tooltip in Condition and a Conditions.Type other than ALL
		MutableComponent tooltipMessage = Component.literal("Cannot be picked because: ");
		
		causes.forEach(tooltip -> tooltipMessage.append("\n").append(Component.translatable(tooltip)));
		
		return tooltipMessage;
	}
	
	private void clickResponse(int responseIndex)
	{
		onClose();
		MSPacketHandler.sendToServer(ResponseTriggerPacket.createPacket(responseIndex, dialogue.path(), entity));
	}
	
	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks)
	{
		this.renderBackground(guiGraphics);
		
		RenderSystem.setShaderColor(1, 1, 1, 1);
		
		if(guiBackground != null)
			guiGraphics.blit(guiBackground, xOffset, yOffset, 0, 0, GUI_WIDTH, GUI_HEIGHT);
		
		String dialogueMessage = dialogue.message().message();
		if(dialogueMessage != null && !dialogueMessage.isEmpty())
		{
			MutableComponent entityName = entity.getDisplayName().plainCopy();
			
			//consort names will have the same color as their type
			if(entity instanceof ConsortEntity consortEntity)
			{
				entityName.withStyle(consortEntity.getConsortType().getColor());
			}
			
			Component entityMessage = entityName.append(": ").append(Component.translatable(dialogueMessage, dialogueData.messageArguments()));
			guiGraphics.drawWordWrap(font, entityMessage, xOffset + 10, yOffset + 20, 210, 0x000000);
		}
		
		super.render(guiGraphics, mouseX, mouseY, partialTicks);
	}
	
	@Override
	public boolean isPauseScreen()
	{
		return false; //should make experience consistent between singleplayer and multiplayer and should help prevent issues with the use of Triggers
	}
}