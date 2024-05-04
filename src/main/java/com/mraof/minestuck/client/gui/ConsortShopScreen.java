package com.mraof.minestuck.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mraof.minestuck.client.gui.playerStats.PlayerStatsScreen;
import com.mraof.minestuck.inventory.ConsortMerchantMenu;
import com.mraof.minestuck.player.ClientPlayerData;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
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
	protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY)
	{
		if(menu.getConsortType() == null || menu.getMerchantType() == null)
			return;
		
		if(portrait == null)
			portrait = new ResourceLocation("minestuck",
					"textures/gui/store/"+menu.getConsortType().name().toLowerCase()+"_"+menu.getMerchantType().name().toLowerCase()+".png");
		
		int x = (width - imageWidth) / 2;
		int y = (height - imageHeight) / 2;
		
		RenderSystem.setShaderColor(1, 1, 1, 1);
		guiGraphics.blit(guiBackground, x, y, 0, 0, imageWidth, imageHeight);
		
		guiGraphics.blit(portrait, x+119, y+40, 0, 0, 64, 64, 64, 64);
		
		guiGraphics.blit(PlayerStatsScreen.icons, x + 5, y + 7, 238, 16, 18, 18);
		
		guiGraphics.drawString(font, String.valueOf(ClientPlayerData.getBoondollars()), x + 25, y + 12, 0x0094FF, false);
		
		for (int i = 0; i < 9; i++)
		{
			int price = menu.getPrice(i);
			if (price == 0 || menu.getSlot(i).getItem().isEmpty())
				continue;
			String cost = price + "\u00A3";
			guiGraphics.drawString(font, cost, x + 25 - font.width(cost)/2F + 35*(i%3), y + 54 + 33*(i/3), 0x000000, false);
		}
	}
	
	@Override
	protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY)
	{
	}
	
	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks)
	{
		super.render(guiGraphics, mouseX, mouseY, partialTicks);
		this.renderTooltip(guiGraphics, mouseX, mouseY);
	}
}