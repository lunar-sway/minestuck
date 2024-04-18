package com.mraof.minestuck.client.gui;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.entity.dialogue.Dialogue;
import com.mraof.minestuck.entity.dialogue.DialogueAnimationData;
import com.mraof.minestuck.network.DialoguePackets;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.neoforged.neoforge.network.PacketDistributor;
import software.bernie.geckolib.cache.texture.AnimatableTexture;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * This is a GUI which pops up when talking with a dialogue compatible entity.
 * The corresponding dialogue json file controls the configurable gui texture, npc sprite texture, message, and responses that show up.
 */
public class DialogueScreen extends Screen
{
	private static final int GUI_WIDTH = 224;
	private static final int GUI_HEIGHT = 176;
	
	private static final int BUTTON_GAP = 2;
	
	private final int dialogueId;
	private final Dialogue.DialogueData dialogueData;
	private final DialogueAnimationData animationData;
	
	private int xOffset;
	private int yOffset;
	
	private List<FormattedCharSequence> messageLines;
	private final List<List<DialogueButton>> responseButtonPages = new ArrayList<>();
	private int responsePage = 0;
	
	//todo replace buttons/pages with a scrollbar
	private DialogueButton previousButton;
	private DialogueButton nextButton;
	
	private int animationTick = 0;
	
	DialogueScreen(int dialogueId, Dialogue.DialogueData dialogueData)
	{
		super(Component.empty());
		
		this.dialogueId = dialogueId;
		this.dialogueData = dialogueData;
		
		this.animationData = dialogueData.animationData();
	}
	
	@Override
	public void init()
	{
		yOffset = (this.height / 2) - (GUI_HEIGHT / 2);
		xOffset = (this.width / 2) - (GUI_WIDTH / 2) - (animationData.spriteWidth() / 2) - animationData.xOffset();
		
		Function<Component, Component> messageStyler = MinestuckConfig.CLIENT.npcDialogueTextColors.get()
				? Function.identity()
				: message -> message.copy().withStyle(style -> style.withColor((TextColor) null));
		
		this.messageLines = this.dialogueData.messages().stream()
				.map(messageStyler)
				.flatMap(message -> font.split(message, GUI_WIDTH - 20).stream())
				.toList();
		
		recreateResponseButtons();
	}
	
	public void recreateResponseButtons()
	{
		responseButtonPages.forEach(dialogueButtons -> dialogueButtons.forEach(this::removeWidget));
		responseButtonPages.clear();
		
		int startY = yOffset + 22 + (DialogueButton.TEXT_SPACING * messageLines.size());
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
			
			//TODO if messageLines is large, it could still push buttons over the edge of the gui
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
		PacketDistributor.SERVER.noArg().send(new DialoguePackets.TriggerResponse(responseData.index(), this.dialogueId));
	}
	
	@Override
	public void onClose()
	{
		PacketDistributor.SERVER.noArg().send(new DialoguePackets.OnCloseScreen(this.dialogueId));
		super.onClose();
	}
	
	@Override
	public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks)
	{
		super.renderBackground(guiGraphics, mouseX, mouseY, partialTicks);
		
		guiGraphics.blit(dialogueData.guiBackground(), xOffset, yOffset, 0, 0, GUI_WIDTH, GUI_HEIGHT);
		
		renderAnimation(guiGraphics);
	}
	
	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks)
	{
		super.render(guiGraphics, mouseX, mouseY, partialTicks);
		
		int pY = yOffset + 20;
		for(FormattedCharSequence line : messageLines)
		{
			guiGraphics.drawString(font, line, xOffset + 10, pY, 0x000000, false);
			pY += DialogueButton.TEXT_SPACING;
		}
	}
	
	/**
	 * Places a talksprite directly to the right of the gui. The center height of the sprite is level with that of the gui.
	 */
	private void renderAnimation(GuiGraphics guiGraphics)
	{
		ResourceLocation sprite = animationData.getRenderPath(dialogueData.spriteType());
		float scale = animationData.scale();
		int moddedWidth = (int) (animationData.spriteWidth() * scale);
		int moddedHeight = (int) (animationData.spriteHeight() * scale);
		
		//TODO single frame sprites with mcmeta files will cause a crash with setAndUpdate
		//if there is a .png.mcmeta file associated with the sprite, the animation for it is updated here
		AnimatableTexture.setAndUpdate(sprite, animationTick++);
		guiGraphics.blit(sprite, xOffset + GUI_WIDTH + animationData.xOffset(), (this.height / 2) - (animationData.spriteHeight() / 2), 0, 0, moddedWidth, moddedHeight, moddedWidth, moddedHeight);
	}
	
	@Override
	public boolean isPauseScreen()
	{
		return false; //should make experience consistent between singleplayer and multiplayer and should help prevent issues with the use of Triggers
	}
}