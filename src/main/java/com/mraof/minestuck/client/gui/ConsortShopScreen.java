package com.mraof.minestuck.client.gui;

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
		xSize = 192;
		ySize = 137;
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		if(container.getConsortType() == null || container.getMerchantType() == null)
			return;
		
		if(portrait == null)
			portrait = new ResourceLocation("minestuck",
					"textures/gui/store/"+container.getConsortType().name().toLowerCase()+"_"+container.getMerchantType().name().toLowerCase()+".png");
		
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		
		this.minecraft.getTextureManager().bindTexture(guiBackground);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		this.blit(x, y, 0, 0, xSize, ySize);
		
		this.minecraft.getTextureManager().bindTexture(portrait);
		blit(x+119, y+40, 0, 0, 64, 64, 64, 64);
		
		this.minecraft.getTextureManager().bindTexture(PlayerStatsScreen.icons);
		this.blit(x + 5, y + 7, 238, 16, 18, 18);
		
		font.drawString(String.valueOf(ClientPlayerData.getBoondollars()), x + 25, y + 12, 0x0094FF);
		
		for (int i = 0; i < 9; i++)
		{
			int price = container.getPrice(i);
			if (price == 0 || container.getSlot(i).getStack().isEmpty())
				continue;
			String cost = price + "\u00A3";
			font.drawString(cost, x + 25 - font.getStringWidth(cost)/2F + 35*(i%3), y + 54 + 33*(i/3), 0x000000);
		}
	}
	
	@Override
	public void render(int mouseX, int mouseY, float partialTicks)
	{
		this.renderBackground();
		super.render(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}
}