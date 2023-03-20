package com.mraof.minestuck.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mraof.minestuck.inventory.MiniCruxtruderMenu;
import com.mraof.minestuck.blockentity.machine.MiniCruxtruderBlockEntity;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
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
	public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks)
	{
		this.renderBackground(poseStack);
		super.render(poseStack, mouseX, mouseY, partialTicks);
		this.renderTooltip(poseStack, mouseX, mouseY);
	}
	
	@Override
	protected void renderBg(PoseStack poseStack, float partialTick, int mouseX, int mouseY)
	{
		int x = (width - imageWidth) / 2;
		int y = (height - imageHeight) / 2;
		
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1, 1, 1, 1);
		//draw background
		RenderSystem.setShaderTexture(0, BACKGROUND);
		this.blit(poseStack, x, y, 0, 0, imageWidth, imageHeight);
		
		//draw progress bar
		RenderSystem.setShaderTexture(0, PROGRESS_BAR);
		int height = MachineScreen.getScaledValue(menu.getProgress(), MiniCruxtruderBlockEntity.DEFAULT_MAX_PROGRESS, PROGRESS_BAR_HEIGHT);
		blit(poseStack, x + PROGRESS_BAR_X, y + PROGRESS_BAR_Y + PROGRESS_BAR_HEIGHT - height, 0, PROGRESS_BAR_HEIGHT - height, PROGRESS_BAR_WIDTH, height, PROGRESS_BAR_WIDTH, PROGRESS_BAR_HEIGHT);
	}
}