package com.mraof.minestuck.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mraof.minestuck.blockentity.machine.MiniCruxtruderBlockEntity;
import com.mraof.minestuck.inventory.MiniCruxtruderMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class MiniCruxtruderScreen extends AbstractContainerScreen<MiniCruxtruderMenu>
{
	private static final ResourceLocation BACKGROUND = new ResourceLocation("minestuck:textures/gui/cruxtruder.png");
	private static final ResourceLocation PROGRESS_BAR = new ResourceLocation("minestuck:textures/gui/progress/cruxtruder.png");
	
	public static final int PROGRESS_BAR_X = 82;
	public static final int PROGRESS_BAR_Y = 42;
	public static final int PROGRESS_BAR_WIDTH = 10;
	public static final int PROGRESS_BAR_HEIGHT = 13;
	
	public MiniCruxtruderScreen(MiniCruxtruderMenu screenContainer, Inventory inv, Component titleIn)
	{
		super(screenContainer, inv, titleIn);
	}
	
	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks)
	{
		super.render(guiGraphics, mouseX, mouseY, partialTicks);
		this.renderTooltip(guiGraphics, mouseX, mouseY);
	}
	
	@Override
	protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY)
	{
		int x = (width - imageWidth) / 2;
		int y = (height - imageHeight) / 2;
		
		RenderSystem.setShaderColor(1, 1, 1, 1);
		//draw background
		guiGraphics.blit(BACKGROUND, x, y, 0, 0, imageWidth, imageHeight);
		
		//draw progress bar
		int height = MachineScreen.getScaledValue(menu.getProgress(), MiniCruxtruderBlockEntity.MAX_PROGRESS, PROGRESS_BAR_HEIGHT);
		guiGraphics.blit(PROGRESS_BAR, x + PROGRESS_BAR_X, y + PROGRESS_BAR_Y + PROGRESS_BAR_HEIGHT - height, 0, PROGRESS_BAR_HEIGHT - height, PROGRESS_BAR_WIDTH, height, PROGRESS_BAR_WIDTH, PROGRESS_BAR_HEIGHT);
	}
}