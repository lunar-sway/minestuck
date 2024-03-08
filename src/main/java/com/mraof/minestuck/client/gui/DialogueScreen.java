package com.mraof.minestuck.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mraof.minestuck.entity.consort.ConsortEntity;
import com.mraof.minestuck.entity.dialogue.Condition;
import com.mraof.minestuck.entity.dialogue.Dialogue;
import com.mraof.minestuck.entity.dialogue.DialogueMessage;
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
import java.util.Collections;
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
	private final CompoundTag conditionChecks;
	private final DialogueMessage.ArgumentsData messageArgs;
	
	private int xOffset;
	private int yOffset;
	
	private final List<Button> responseButtons = new ArrayList<>();
	
	DialogueScreen(LivingEntity entity, Dialogue dialogue, CompoundTag conditionChecks, CompoundTag messageArgs)
	{
		super(Component.empty());
		
		this.entity = entity;
		this.guiBackground = dialogue.guiPath();
		this.dialogue = dialogue;
		this.conditionChecks = conditionChecks;
		this.messageArgs = DialogueMessage.ArgumentsData.read(messageArgs);
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
			if(responseFailedCheck(response.response().message()) && response.hideIfFailed())
				continue;
			
			filteredResponses.add(response);
		}
		
		for(int i = 0; i < filteredResponses.size(); i++)
		{
			Dialogue.Response response = filteredResponses.get(i);
			String responseMessage = response.response().message();
			int yPositionOffset = 20 * i;
			
			Component buttonComponent = Component.translatable(responseMessage, messageArgs.responseArgumentsMap().getOrDefault(responseMessage, Collections.emptyList()));
			
			int index = dialogue.responses().indexOf(response);
			ExtendedButton entryButton = new ExtendedButton(xOffset + 20, yOffset + 40 + yPositionOffset, 190, 14, buttonComponent,
					button -> clickResponse(index));
			
			if(responseFailedCheck(responseMessage))
			{
				entryButton.setTooltip(Tooltip.create(conditionFailMessage(response)));
				entryButton.active = false;
			}
			
			responseButtons.add(addRenderableWidget(entryButton));
		}
	}
	
	private boolean responseFailedCheck(String responseMessage)
	{
		if(conditionChecks.contains(responseMessage))
		{
			return !conditionChecks.getBoolean(responseMessage);
		}
		
		return true;
	}
	
	private static MutableComponent conditionFailMessage(Dialogue.Response response)
	{
		//TODO Does not make sense linguistically with a hard coded failure tooltip in Condition and a Conditions.Type other than ALL
		MutableComponent tooltipMessage = Component.literal("Cannot be picked because: ");
		
		for(Condition condition : response.conditions().conditionList())
		{
			String tooltip = condition.getFailureTooltip();
			if(!tooltip.isEmpty())
				tooltipMessage.append("\n").append(Component.translatable(tooltip));
		}
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
			
			Component entityMessage = entityName.append(": ").append(Component.translatable(dialogueMessage, messageArgs.dialogueArguments()));
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