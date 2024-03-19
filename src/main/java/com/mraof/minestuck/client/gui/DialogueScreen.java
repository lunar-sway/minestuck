package com.mraof.minestuck.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mraof.minestuck.entity.dialogue.Dialogue;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.network.ResponseTriggerPacket;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.FormattedCharSequence;
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
	
	private final LivingEntity entity;
	private final Dialogue.NodeReference nodeReference;
	private final Dialogue.DialogueData dialogueData;
	
	private int xOffset;
	private int yOffset;
	
	private List<FormattedCharSequence> messageLines;
	private final List<Button> responseButtons = new ArrayList<>();
	
	DialogueScreen(LivingEntity entity, Dialogue.NodeReference nodeReference, Dialogue.DialogueData dialogueData)
	{
		super(Component.empty());
		
		this.entity = entity;
		this.nodeReference = nodeReference;
		this.dialogueData = dialogueData;
	}
	
	@Override
	public void init()
	{
		//TODO static gui height/width may not make sense with customizable gui sizing
		yOffset = (this.height / 2) - (GUI_HEIGHT / 2);
		xOffset = (this.width / 2) - (GUI_WIDTH / 2);
		
		this.messageLines = font.split(this.dialogueData.message(), 210);
		
		recreateResponseButtons();
	}
	
	public void recreateResponseButtons()
	{
		responseButtons.forEach(this::removeWidget);
		responseButtons.clear();
		
		int startY = yOffset + 20 + 9 * messageLines.size() + 2;
		for(int i = 0; i < this.dialogueData.responses().size(); i++)
		{
			Dialogue.ResponseData data = this.dialogueData.responses().get(i);
			
			ExtendedButton entryButton = new ExtendedButton(xOffset + 20, startY + 20 * i, 190, 14, data.message(),
					button -> clickResponse(data));
			
			data.conditionFailure().ifPresent(failure -> {
				entryButton.setTooltip(Tooltip.create(conditionFailMessage(failure.causes())));
				entryButton.active = false;
			});
			
			responseButtons.add(addRenderableWidget(entryButton));
		}
	}
	
	private static MutableComponent conditionFailMessage(Component causes)
	{
		return Component.literal("Cannot be picked because:").append("\n").append(causes);
	}
	
	private void clickResponse(Dialogue.ResponseData responseData)
	{
		if(responseData.shouldClose())
			onClose();
		MSPacketHandler.sendToServer(ResponseTriggerPacket.createPacket(responseData.index(), nodeReference, entity));
	}
	
	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks)
	{
		this.renderBackground(guiGraphics);
		
		RenderSystem.setShaderColor(1, 1, 1, 1);
		
		guiGraphics.blit(dialogueData.guiBackground(), xOffset, yOffset, 0, 0, GUI_WIDTH, GUI_HEIGHT);
		
		int pY = yOffset + 20;
		for(FormattedCharSequence line : messageLines)
		{
			guiGraphics.drawString(font, line, xOffset + 10, pY, 0x000000, false);
			pY += 9;
		}
		
		super.render(guiGraphics, mouseX, mouseY, partialTicks);
	}
	
	@Override
	public boolean isPauseScreen()
	{
		return false; //should make experience consistent between singleplayer and multiplayer and should help prevent issues with the use of Triggers
	}
}