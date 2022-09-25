package com.mraof.minestuck.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mraof.minestuck.inventory.MiniCruxtruderMenu;
import com.mraof.minestuck.blockentity.machine.MiniCruxtruderBlockEntity;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class MiniCruxtruderScreen extends MachineScreen<MiniCruxtruderMenu>
{
	
	private final ResourceLocation BACKGROUND = new ResourceLocation("minestuck:textures/gui/cruxtruder.png");
	private final ResourceLocation PROGRESS = new ResourceLocation("minestuck:textures/gui/progress/cruxtruder.png");
	//private EntityPlayer player;
	private int progressX;
	private int progressY;
	private int progressWidth;
	private int progressHeight;
	private int goX;
	private int goY;
	
	public MiniCruxtruderScreen(MiniCruxtruderMenu screenContainer, Inventory inv, Component titleIn)
	{
		super(MiniCruxtruderBlockEntity.TYPE, screenContainer, inv, titleIn);
		
		//sets progress bar information
		progressX = 82;
		progressY = 42;
		progressWidth = 10;
		progressHeight = 13;
	}
	
	@Override
	public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks)
	{
		this.renderBackground(poseStack);
		super.render(poseStack, mouseX, mouseY, partialTicks);
		this.renderTooltip(poseStack, mouseX, mouseY);
	}
	
	@Override
	protected void renderBg(PoseStack matrixStack, float par1, int par2, int par3)
	{
		int x = (width - imageWidth) / 2;
		int y = (height - imageHeight) / 2;

		//draw background
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1, 1, 1, 1);
		RenderSystem.setShaderTexture(0, BACKGROUND);
		this.blit(matrixStack, x, y, 0, 0, imageWidth, imageHeight);

		//draw progress bar
		RenderSystem.setShaderTexture(0, PROGRESS);
		int width = progressWidth;
		int height = getScaledValue(menu.getProgress(), MiniCruxtruderBlockEntity.DEFAULT_MAX_PROGRESS, progressHeight);
		blit(matrixStack, x + progressX, y + progressY + progressHeight - height, 0, progressHeight - height, width, height, progressWidth, progressHeight);
	}
}