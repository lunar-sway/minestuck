package com.mraof.minestuck.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mraof.minestuck.client.gui.playerStats.PlayerStatsScreen;
import com.mraof.minestuck.inventory.ConsortMerchantMenu;
import com.mraof.minestuck.player.ClientPlayerData;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class ConsortShopScreen extends AbstractContainerScreen<ConsortMerchantMenu>
{
	private final ResourceLocation guiBackground = new ResourceLocation("minestuck", "textures/gui/consort_shop.png");
	private ResourceLocation portrait;
	
	public ConsortShopScreen(ConsortMerchantMenu screenContainer, Inventory inv, Component titleIn)
	{
		super(screenContainer, inv, titleIn);
		imageWidth = 192;
		imageHeight = 137;
	}
	
	@Override
	protected void renderBg(PoseStack poseStack, float partialTicks, int mouseX, int mouseY)
	{
		if(menu.getConsortType() == null || menu.getMerchantType() == null)
			return;
		
		if(portrait == null)
			portrait = new ResourceLocation("minestuck",
					"textures/gui/store/"+menu.getConsortType().name().toLowerCase()+"_"+menu.getMerchantType().name().toLowerCase()+".png");
		
		int x = (width - imageWidth) / 2;
		int y = (height - imageHeight) / 2;
		
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1, 1, 1, 1);
		RenderSystem.setShaderTexture(0, guiBackground);
		blit(poseStack, x, y, 0, 0, imageWidth, imageHeight);
		
		RenderSystem.setShaderTexture(0, portrait);
		blit(poseStack, x+119, y+40, 0, 0, 64, 64, 64, 64);
		
		RenderSystem.setShaderTexture(0, PlayerStatsScreen.icons);
		blit(poseStack, x + 5, y + 7, 238, 16, 18, 18);
		
		font.draw(poseStack, String.valueOf(ClientPlayerData.getBoondollars()), x + 25, y + 12, 0x0094FF);
		
		for (int i = 0; i < 9; i++)
		{
			int price = menu.getPrice(i);
			if (price == 0 || menu.getSlot(i).getItem().isEmpty())
				continue;
			String cost = price + "\u00A3";
			font.draw(poseStack, cost, x + 25 - font.width(cost)/2F + 35*(i%3), y + 54 + 33*(i/3), 0x000000);
		}
	}
	
	@Override
	protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY)
	{
	}
	
	@Override
	public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks)
	{
		this.renderBackground(poseStack);
		super.render(poseStack, mouseX, mouseY, partialTicks);
		this.renderTooltip(poseStack, mouseX, mouseY);
	}
}