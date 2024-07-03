package com.mraof.minestuck.client.gui;

import com.mraof.minestuck.blockentity.redstone.StructureCoreBlockEntity;
import com.mraof.minestuck.network.block.StructureCoreSettingsPacket;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.neoforged.neoforge.client.gui.widget.ExtendedButton;
import net.neoforged.neoforge.network.PacketDistributor;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class StructureCoreScreen extends Screen
{
	public static final String TITLE = "minestuck.structure_core";
	private static final ResourceLocation GUI_BACKGROUND = new ResourceLocation("minestuck", "textures/gui/generic_medium.png");
	
	private static final int GUI_WIDTH = 150;
	private static final int GUI_HEIGHT = 98;
	
	private final StructureCoreBlockEntity be;
	private StructureCoreBlockEntity.ActionType actionType;
	private int shutdownRange;
	
	private Button incrementButton;
	private Button decrementButton;
	private Button largeIncrementButton;
	private Button largeDecrementButton;
	private Button typeButton;
	
	
	StructureCoreScreen(StructureCoreBlockEntity be)
	{
		super(Component.translatable(TITLE));
		
		this.be = be;
		this.actionType = be.getActionType();
		this.shutdownRange = be.getShutdownRange();
	}
	
	@Override
	public void init()
	{
		int yOffset = (this.height / 2) - (GUI_HEIGHT / 2);
		
		addRenderableWidget(incrementButton = new ExtendedButton(this.width / 2 + 20, (height - GUI_HEIGHT) / 2 + 12, 20, 20, Component.literal("+"), button -> changeRange(1)));
		addRenderableWidget(decrementButton = new ExtendedButton(this.width / 2 - 40, (height - GUI_HEIGHT) / 2 + 12, 20, 20, Component.literal("-"), button -> changeRange(-1)));
		addRenderableWidget(largeIncrementButton = new ExtendedButton(this.width / 2 + 45, (height - GUI_HEIGHT) / 2 + 12, 20, 20, Component.literal("++"), button -> changeRange(10)));
		addRenderableWidget(largeDecrementButton = new ExtendedButton(this.width / 2 - 65, (height - GUI_HEIGHT) / 2 + 12, 20, 20, Component.literal("--"), button -> changeRange(-10)));
		
		addRenderableWidget(typeButton = new ExtendedButton(this.width / 2 - 67, yOffset + 40, 135, 20, Component.literal(actionType.getNameNoSpaces()), button -> changeActionType()));
		
		addRenderableWidget(new ExtendedButton(this.width / 2 - 18, yOffset + 70, 40, 20, Component.literal("DONE"), button -> finish()));
	}
	
	/**
	 * Gets the ordinal of the current action type, and either cycles it back to the beginning at one if it is the last ordinal in the set or just sets it to the next ordinal in line
	 */
	private void changeActionType()
	{
		actionType = StructureCoreBlockEntity.ActionType.fromInt(actionType.ordinal() < StructureCoreBlockEntity.ActionType.values().length - 1 ? actionType.ordinal() + 1 : 0);
		typeButton.setMessage(Component.literal(actionType.getNameNoSpaces()));
	}
	
	/**
	 * Attempts to increase or decrease the range at which the block sends out a shutdown "pulse" if the block is in receiving mode
	 */
	private void changeRange(int change)
	{
		shutdownRange = Mth.clamp(shutdownRange + change, 1, 64);
		incrementButton.active = shutdownRange < 64;
		decrementButton.active = shutdownRange > 1;
		largeIncrementButton.active = shutdownRange < 64;
		largeDecrementButton.active = shutdownRange > 1;
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
		
		guiGraphics.drawString(font, Integer.toString(shutdownRange), (width / 2) - 5, (height - GUI_HEIGHT) / 2 + 16, 0x404040, false);
	}
	
	private void finish()
	{
		PacketDistributor.SERVER.noArg().send(new StructureCoreSettingsPacket(actionType, shutdownRange, be.getBlockPos()));
		onClose();
	}
}
