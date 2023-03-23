package com.mraof.minestuck.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mraof.minestuck.inventory.MiniTotemLatheMenu;
import com.mraof.minestuck.blockentity.machine.MiniTotemLatheBlockEntity;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class MiniTotemLatheScreen extends MachineScreen<MiniTotemLatheMenu>
{
	private static final ResourceLocation BACKGROUND = new ResourceLocation("minestuck:textures/gui/totem_lathe.png");
	private static final ResourceLocation PROGRESS = new ResourceLocation("minestuck:textures/gui/progress/totem_lathe.png");
	
	private static final int progressX = 81;
	private static final int progressY = 33;
	private static final int progressWidth = 44;
	private static final int progressHeight = 17;
	private static final int goX = 85;
	private static final int goY = 53;
	
	public MiniTotemLatheScreen(MiniTotemLatheMenu screenContainer, Inventory inv, Component titleIn)
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
	protected void renderBg(PoseStack poseStack, float par1, int par2, int par3)
	{
		int x = (width - imageWidth) / 2;
		int y = (height - imageHeight) / 2;

		//draw background
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1, 1, 1, 1);
		RenderSystem.setShaderTexture(0, BACKGROUND);
		this.blit(poseStack, x, y, 0, 0, imageWidth, imageHeight);

		//draw progress bar
		RenderSystem.setShaderTexture(0, PROGRESS);
		int width = getScaledValue(menu.getProgress(), MiniTotemLatheBlockEntity.MAX_PROGRESS, progressWidth);
		blit(poseStack, x + progressX, y + progressY, 0, 0, width, progressHeight, progressWidth, progressHeight);
	}

	@Override
	public void init()
	{
		super.init();
		
		goButton = new GoButton(this.leftPos + goX, this.topPos + goY, 30, 12, this.menu, false);
		addRenderableWidget(goButton);
	}
}