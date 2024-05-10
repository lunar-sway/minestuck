package com.mraof.minestuck.client.gui;

import com.mraof.minestuck.blockentity.redstone.RemoteObserverBlockEntity;
import com.mraof.minestuck.network.block.RemoteObserverSettingsPacket;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.client.gui.widget.ExtendedButton;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Optional;

public class RemoteObserverScreen extends Screen
{
	public static final String TITLE = "minestuck.remote_observer";
	public static final String CURRENT_ENTITY_TYPE_MESSAGE = "minestuck.remote_observer.current_entity_type";
	private static final ResourceLocation GUI_BACKGROUND = new ResourceLocation("minestuck", "textures/gui/generic_medium.png");
	
	private static final int GUI_WIDTH = 150;
	private static final int GUI_HEIGHT = 98;
	
	private final RemoteObserverBlockEntity be;
	private RemoteObserverBlockEntity.ActiveType activeType;
	private int observingRange;
	
	private Button typeButton;
	private Button incrementButton;
	private Button decrementButton;
	private Button largeIncrementButton;
	private Button largeDecrementButton;
	
	private EditBox entityTypeTextField;
	
	RemoteObserverScreen(RemoteObserverBlockEntity be)
	{
		super(Component.translatable(TITLE));
		
		this.be = be;
		this.activeType = be.getActiveType();
		this.observingRange = be.getObservingRange();
	}
	
	@Override
	public void init()
	{
		int yOffset = (this.height / 2) - (GUI_HEIGHT / 2);
		
		addRenderableWidget(typeButton = new ExtendedButton(this.width / 2 - 67, (height - GUI_HEIGHT) / 2 + 5, 135, 20, Component.literal(activeType.getNameNoSpaces()), button -> changeActiveType()));
		
		this.entityTypeTextField = new EditBox(this.font, this.width / 2 - 53, yOffset + 29, 105, 18, Component.translatable(CURRENT_ENTITY_TYPE_MESSAGE));    //TODO Maybe look at other text fields for what the text should be
		this.entityTypeTextField.setValue(EntityType.getKey(be.getCurrentEntityType()).toString()); //TODO somewhere along the line, if the active type is not current entity present and the gui is exited, returning to current entity present active type has pig as the entity type
		addRenderableWidget(entityTypeTextField);
		entityTypeTextField.setVisible(activeType == RemoteObserverBlockEntity.ActiveType.CURRENT_ENTITY_PRESENT);
		
		addRenderableWidget(incrementButton = new ExtendedButton(this.width / 2 + 20, (height - GUI_HEIGHT) / 2 + 51, 20, 20, Component.literal("+"), button -> changeRange(1)));
		addRenderableWidget(decrementButton = new ExtendedButton(this.width / 2 - 40, (height - GUI_HEIGHT) / 2 + 51, 20, 20, Component.literal("-"), button -> changeRange(-1)));
		addRenderableWidget(largeIncrementButton = new ExtendedButton(this.width / 2 + 45, (height - GUI_HEIGHT) / 2 + 51, 20, 20, Component.literal("++"), button -> changeRange(10)));
		addRenderableWidget(largeDecrementButton = new ExtendedButton(this.width / 2 - 65, (height - GUI_HEIGHT) / 2 + 51, 20, 20, Component.literal("--"), button -> changeRange(-10)));
		
		addRenderableWidget(new ExtendedButton(this.width / 2 - 20, yOffset + 73, 40, 20, Component.literal("DONE"), button -> finish()));
	}
	
	/**
	 * Gets the ordinal of the current active type, and either cycles it back to the beginning at one if it is the last ordinal in the set or just sets it to the next ordinal in line, also refreshes the display for Current Entity Present
	 */
	private void changeActiveType()
	{
		activeType = RemoteObserverBlockEntity.ActiveType.fromInt(activeType.ordinal() < RemoteObserverBlockEntity.ActiveType.values().length - 1 ? activeType.ordinal() + 1 : 0);
		typeButton.setMessage(Component.literal(activeType.getNameNoSpaces()));
		
		entityTypeTextField.setVisible(activeType == RemoteObserverBlockEntity.ActiveType.CURRENT_ENTITY_PRESENT);
	}
	
	/**
	 * Attempts to increase or decrease the range at which the observer attempts to detect entities
	 */
	private void changeRange(int change)
	{
		observingRange = Mth.clamp(observingRange + change, 1, 64);
		incrementButton.active = observingRange < 64;
		decrementButton.active = observingRange > 1;
		largeIncrementButton.active = observingRange < 64;
		largeDecrementButton.active = observingRange > 1;
	}
	
	@Override
	public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks)
	{
		super.renderBackground(guiGraphics, mouseX, mouseY, partialTicks);
		
		int yOffset = (this.height / 2) - (GUI_HEIGHT / 2);
		guiGraphics.blit(GUI_BACKGROUND, (this.width / 2) - (GUI_WIDTH / 2), yOffset, 0, 0, GUI_WIDTH, GUI_HEIGHT);
	}
	
	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks)
	{
		super.render(guiGraphics, mouseX, mouseY, partialTicks);
		
		guiGraphics.drawString(font, Integer.toString(observingRange), (width / 2) - 5, (height - GUI_HEIGHT) / 2 + 55, 0x404040, false);
	}
	
	private void finish()
	{
		Optional<EntityType<?>> attemptedEntityType = EntityType.byString(entityTypeTextField.getValue());
		
		boolean isCurrentEntityActiveType = activeType == RemoteObserverBlockEntity.ActiveType.CURRENT_ENTITY_PRESENT;
		boolean isValidAndObservableEntityType = attemptedEntityType.isPresent() && RemoteObserverBlockEntity.entityCanBeObserved(attemptedEntityType.get());
		
		if(!isCurrentEntityActiveType || isValidAndObservableEntityType)
		{
			EntityType<?> entityType = be.getCurrentEntityType();
			if(isValidAndObservableEntityType)
				entityType = attemptedEntityType.get();
			
			PacketDistributor.SERVER.noArg().send(new RemoteObserverSettingsPacket(activeType, observingRange, be.getBlockPos(), entityType));
			onClose();
		} else
		{
			entityTypeTextField.setTextColor(0XFF0000); //changes text to red to indicate that it is an invalid type
		}
	}
}
