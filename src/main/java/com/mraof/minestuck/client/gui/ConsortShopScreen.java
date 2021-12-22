package com.mraof.minestuck.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mraof.minestuck.client.gui.playerStats.PlayerStatsScreen;
import com.mraof.minestuck.inventory.ConsortMerchantContainer;
import com.mraof.minestuck.world.storage.ClientPlayerData;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class ConsortShopScreen extends ContainerScreen<ConsortMerchantContainer>
{
	private ResourceLocation guiBackground = new ResourceLocation("minestuck", "textures/gui/consort_shop.png");
	private ResourceLocation portrait;
	
	public ConsortShopScreen(ConsortMerchantContainer screenContainer, PlayerInventory inv, ITextComponent titleIn)
	{
		super(screenContainer, inv, titleIn);
		imageWidth = 192;
		imageHeight = 137;
	}
	
	@Override
	protected void renderBg(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY)
	{
		if(menu.getConsortType() == null || menu.getMerchantType() == null)
			return;
		
		if(portrait == null)
			portrait = new ResourceLocation("minestuck",
					"textures/gui/store/"+menu.getConsortType().name().toLowerCase()+"_"+menu.getMerchantType().name().toLowerCase()+".png");
		
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		
		this.minecraft.getTextureManager().bind(guiBackground);
		int x = (width - imageWidth) / 2;
		int y = (height - imageHeight) / 2;
		this.blit(matrixStack, x, y, 0, 0, imageWidth, imageHeight);
		
		this.minecraft.getTextureManager().bind(portrait);
		blit(matrixStack, x+119, y+40, 0, 0, 64, 64, 64, 64);
		
		this.minecraft.getTextureManager().bind(PlayerStatsScreen.icons);
		this.blit(matrixStack, x + 5, y + 7, 238, 16, 18, 18);
		
		font.draw(matrixStack, String.valueOf(ClientPlayerData.getBoondollars()), x + 25, y + 12, 0x0094FF);
		
		for (int i = 0; i < 9; i++)
		{
			int price = menu.getPrice(i);
			if (price == 0 || menu.getSlot(i).getItem().isEmpty())
				continue;
			String cost = price + "\u00A3";
			font.draw(matrixStack, cost, x + 25 - font.width(cost)/2F + 35*(i%3), y + 54 + 33*(i/3), 0x000000);
		}
	}
	
	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
	{
		this.renderBackground(matrixStack);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		this.renderTooltip(matrixStack, mouseX, mouseY);
	}
}