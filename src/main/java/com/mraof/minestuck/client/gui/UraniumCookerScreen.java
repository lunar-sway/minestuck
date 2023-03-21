package com.mraof.minestuck.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mraof.minestuck.inventory.UraniumCookerMenu;
import com.mraof.minestuck.blockentity.machine.UraniumCookerBlockEntity;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class UraniumCookerScreen extends MachineScreen<UraniumCookerMenu>
{
	private static final ResourceLocation BACKGROUND = new ResourceLocation("minestuck:textures/gui/uranium_cooker.png");
	private static final ResourceLocation PROGRESS = new ResourceLocation("minestuck:textures/gui/progress/uranium_level.png");
	
	private int progressX;
	private int progressY;
	private int progressWidth;
	private int progressHeight;
	private int goX;
	private int goY;
	
	public UraniumCookerScreen(UraniumCookerMenu screenContainer, Inventory inv, Component titleIn)
	{
		super(UraniumCookerBlockEntity.TYPE, screenContainer, inv, titleIn);
		
		//sets progress bar information
		progressX = 67;
		progressY = 24;
		progressWidth = 35;
		progressHeight = 39;
		goX = 69;
		goY = 69;
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
		RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
		RenderSystem.setShaderColor(1, 1, 1, 1);
		RenderSystem.setShaderTexture(0, BACKGROUND);
		this.blit(poseStack, x, y, 0, 0, imageWidth, imageHeight);
		
		//draw progress bar
		RenderSystem.setShaderTexture(0, PROGRESS);
		int width = progressWidth;
		int height = getScaledValue(menu.getFuel(), UraniumCookerBlockEntity.MAX_FUEL, progressHeight);
		blit(poseStack, x+progressX, y+progressY+progressHeight-height, 0, progressHeight-height, width, height, progressWidth, progressHeight);
	}
	
	@Override
	public void init()
	{
		super.init();
		
		goButton = new GoButton((width - imageWidth) / 2 + goX, (height - imageHeight) / 2 + goY, 30, 12, Component.literal(menu.isLooping() ? "STOP" : "GO"));
		addRenderableWidget(goButton);
	}
}