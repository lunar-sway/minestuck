package com.mraof.minestuck.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mraof.minestuck.entity.dialogue.Dialogue;
import com.mraof.minestuck.entity.dialogue.DialogueAnimation;
import com.mraof.minestuck.entity.dialogue.DialogueEntity;
import com.mraof.minestuck.network.DialoguePackets;
import com.mraof.minestuck.network.MSPacketHandler;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraftforge.client.gui.widget.ExtendedButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Displays a screen when interacting with any dialogue capable entity that has a valid dialogue.
 * It is configurable in terms of what the gui image is, what sort of animated talk sprite shows up, and how many response options there are.
 */
public class DialogueScreen extends Screen
{
	private static final int GUI_WIDTH = 224;
	private static final int GUI_HEIGHT = 176;
	
	private static final int BUTTON_GAP = 2;
	
	private final int dialogueId;
	private final Dialogue.DialogueData dialogueData;
	
	private int xOffset;
	private int yOffset;
	
	private List<FormattedCharSequence> messageLines;
	private final List<List<DialogueButton>> responseButtonPages = new ArrayList<>();
	private int responsePage = 0;
	
	private DialogueButton previousButton;
	private DialogueButton nextButton;
	
	DialogueScreen(int dialogueId, Dialogue.DialogueData dialogueData)
	{
		super(Component.empty());
		
		this.dialogueId = dialogueId;
		this.dialogueData = dialogueData;
	}
	
	@Override
	public void init()
	{
		//TODO static gui height/width may not make sense with customizable gui sizing
		yOffset = (this.height / 2) - (GUI_HEIGHT / 2);
		xOffset = (this.width / 2) - (GUI_WIDTH / 2) + DialogueAnimation.DEFAULT_SPRITE_WIDTH;
		
		this.messageLines = font.split(this.dialogueData.message(), 210);
		
		recreateResponseButtons();
	}
	
	public void recreateResponseButtons()
	{
		responseButtonPages.forEach(dialogueButtons -> dialogueButtons.forEach(this::removeWidget));
		responseButtonPages.clear();
		
		int startY = yOffset + 22 + (9 * messageLines.size());
		int cumulativeButtonHeight = 0;
		List<DialogueButton> pageButtons = new ArrayList<>();
		
		for(Dialogue.ResponseData data : this.dialogueData.responses())
		{
			DialogueButton entryButton = new DialogueButton(dialogueData.guiBackground(), true, xOffset + 16, startY + cumulativeButtonHeight, 190, DialogueButton.NORMAL_DEFAULT_HEIGHT, data.message(),
					button -> clickResponse(data));
			
			int entryHeight = entryButton.trueHeight + BUTTON_GAP;
			
			cumulativeButtonHeight += entryHeight;
			
			data.conditionFailure().ifPresent(failure -> {
				entryButton.setTooltip(Tooltip.create(conditionFailMessage(failure.causes())));
				entryButton.active = false;
			});
			
			if(cumulativeButtonHeight > (DialogueButton.NORMAL_DEFAULT_HEIGHT + BUTTON_GAP) * 5)
			{
				responseButtonPages.add(pageButtons);
				pageButtons = new ArrayList<>(); // Create a new instance for the next page
				cumulativeButtonHeight = entryHeight;
				
				entryButton.setY(startY);
			}
			
			//is added after the check for height to prevent buttons that are too large from hanging out the bottom
			pageButtons.add(addRenderableWidget(entryButton));
		}
		
		//catches stragglers
		if(!pageButtons.isEmpty())
			responseButtonPages.add(pageButtons);
		
		this.previousButton = new DialogueButton(dialogueData.guiBackground(), false, xOffset + (GUI_WIDTH / 2) - 21, yOffset + 146, 16, 16, Component.literal("<"), button -> prevPage());
		this.nextButton = new DialogueButton(dialogueData.guiBackground(), false, xOffset + (GUI_WIDTH / 2) + 5, yOffset + 146, 16, 16, Component.literal(">"), button -> nextPage());
		addRenderableWidget(this.nextButton);
		addRenderableWidget(this.previousButton);
		
		updateButtonStates();
	}
	
	private void prevPage()
	{
		responsePage--;
		updateButtonStates();
	}
	
	private void nextPage()
	{
		responsePage++;
		updateButtonStates();
	}
	
	private void updateButtonStates()
	{
		//reset visibility of all
		responseButtonPages.forEach(pageButtons -> pageButtons.forEach(button -> button.visible = false));
		
		boolean hasPages = !responseButtonPages.isEmpty();
		boolean showButtons = hasPages && responsePage >= 0 && responsePage < responseButtonPages.size();
		
		previousButton.active = responsePage > 0;
		nextButton.active = responsePage < responseButtonPages.size() - 1;
		previousButton.visible = responseButtonPages.size() > 1;
		nextButton.visible = responseButtonPages.size() > 1;
		
		if(showButtons)
		{
			List<DialogueButton> currentPageButtons = responseButtonPages.get(responsePage);
			currentPageButtons.forEach(button -> button.visible = true);
		}
	}
	
	private static MutableComponent conditionFailMessage(Component causes)
	{
		//todo language key
		return Component.literal("Cannot be picked because:").append("\n").append(causes);
	}
	
	private void clickResponse(Dialogue.ResponseData responseData)
	{
		if(responseData.shouldClose())
			Objects.requireNonNull(this.minecraft).popGuiLayer();
		MSPacketHandler.sendToServer(new DialoguePackets.TriggerResponse(responseData.index(), this.dialogueId));
	}
	
	@Override
	public void onClose()
	{
		MSPacketHandler.sendToServer(new DialoguePackets.OnCloseScreen(this.dialogueId));
		super.onClose();
	}
	
	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks)
	{
		this.renderBackground(guiGraphics);
		
		RenderSystem.setShaderColor(1, 1, 1, 1);
		
		guiGraphics.blit(dialogueData.guiBackground(), xOffset, yOffset, 0, 0, GUI_WIDTH, GUI_HEIGHT);
		
		renderAnimation(guiGraphics);
		
		int pY = yOffset + 20;
		for(FormattedCharSequence line : messageLines)
		{
			guiGraphics.drawString(font, line, xOffset + 10, pY, 0x000000, false);
			pY += 9;
		}
		
		super.render(guiGraphics, mouseX, mouseY, partialTicks);
	}
	
	private void renderAnimation(GuiGraphics guiGraphics)
	{
		if(!(entity instanceof DialogueEntity dialogueEntity))
			return;
		
		DialogueAnimation animation = dialogueData.animation();
		Optional<ResourceLocation> potentialPath = animation.getRenderPath(dialogueEntity);
		
		if(potentialPath.isPresent())
		{
			//guiGraphics.pose().pushPose();
			//guiGraphics.pose().scale(0.25F,0.25F,0.25F);
			guiGraphics.blit(potentialPath.get(), xOffset - DialogueAnimation.DEFAULT_SPRITE_WIDTH, yOffset, 0, 0, DialogueAnimation.DEFAULT_SPRITE_WIDTH, DialogueAnimation.DEFAULT_SPRITE_HEIGHT);
			//guiGraphics.pose().popPose();
		}
	}
	
	@Override
	public boolean isPauseScreen()
	{
		return false; //should make experience consistent between singleplayer and multiplayer and should help prevent issues with the use of Triggers
	}
}