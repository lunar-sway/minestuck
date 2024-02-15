package com.mraof.minestuck.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mraof.minestuck.data.DialogueProvider;
import com.mraof.minestuck.entity.consort.ConsortEntity;
import com.mraof.minestuck.network.DialoguePacket;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.util.Dialogue;
import com.mraof.minestuck.util.DialogueManager;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.gui.widget.ExtendedButton;

import java.util.ArrayList;
import java.util.List;

public class DialogueScreen extends Screen
{
	private static final int GUI_WIDTH = 224;
	private static final int GUI_HEIGHT = 176;
	
	private final ResourceLocation guiBackground;
	
	private final LivingEntity entity;
	private final Player player;
	private final Dialogue dialogue;
	
	private int xOffset;
	private int yOffset;
	
	private final List<Button> responseButtons = new ArrayList<>();
	
	DialogueScreen(LivingEntity entity, Player player, Dialogue dialogue)
	{
		super(Component.empty());
		
		this.entity = entity;
		this.player = player;
		this.guiBackground = dialogue.getGuiPath();
		this.dialogue = dialogue;
	}
	
	@Override
	public void init()
	{
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
		for(Dialogue.Response response : dialogue.getResponses())
		{
			if(response.failsWhileImportant(entity, player))
				continue;
			
			filteredResponses.add(response);
		}
		
		for(int i = 0; i < filteredResponses.size(); i++)
		{
			Dialogue.Response response = filteredResponses.get(i);
			String responseMessage = response.getResponse();
			int yPositionOffset = 20 * i;
			
			Component buttonComponent = Component.translatable(responseMessage);
			
			ExtendedButton entryButton = new ExtendedButton(xOffset + 20, yOffset + 40 + yPositionOffset, 190, 14, buttonComponent,
					button -> clickResponse(responseMessage));
			
			if(!Dialogue.Condition.matchesAllConditions(entity, player, response.getConditions()))
			{
				MutableComponent tooltipMessage = Component.literal("Cannot be picked because: ");
				
				for(Dialogue.Condition condition : response.getConditions())
				{
					String tooltip = condition.getFailureTooltip();
					if(tooltip != null && !tooltip.isEmpty())
						tooltipMessage.append("\n").append(Component.translatable(tooltip));
				}
				
				entryButton.setTooltip(Tooltip.create(tooltipMessage));
				entryButton.active = false;
			}
			
			responseButtons.add(addRenderableWidget(entryButton));
		}
	}
	
	private void clickResponse(String responseMessage)
	{
		for(Dialogue.Response response : dialogue.getResponses())
		{
			if(response.getResponse().equals(responseMessage))
			{
				ResourceLocation nextPath = response.getNextDialoguePath();
				
				Dialogue nextDialogue = null;
				if(nextPath != null && nextPath != DialogueProvider.EMPTY_NEXT_PATH)
					nextDialogue = DialogueManager.getInstance().getDialogue(nextPath);
				
				List<Dialogue.Trigger> triggers = response.getTriggers();
				for(Dialogue.Trigger trigger : triggers)
				{
					DialoguePacket packet = DialoguePacket.createPacket(trigger, entity);
					MSPacketHandler.sendToServer(packet);
				}
				
				onClose();
				if(nextDialogue != null)
				{
					MSScreenFactories.displayDialogueScreen(entity, player, nextDialogue);
				}
				break;
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
	
	@Override
	public boolean isPauseScreen()
	{
		return false;
	}
}