package com.mraof.minestuck.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mraof.minestuck.client.gui.playerStats.PlayerStatsScreen;
import com.mraof.minestuck.inventory.ConsortMerchantContainer;
import com.mraof.minestuck.world.storage.PlayerSavedData;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
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
		if(container.inventory.getConsortType() == null || container.inventory.getMerchantType() == null)
			return;
		
		if(portrait == null)
			portrait = new ResourceLocation("minestuck",
					"textures/gui/store/"+container.inventory.getConsortType().name().toLowerCase()+"_"+container.inventory.getMerchantType().name().toLowerCase()+".png");
		
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		
		this.minecraft.getTextureManager().bindTexture(guiBackground);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		this.blit(x, y, 0, 0, xSize, ySize);
		
		this.minecraft.getTextureManager().bindTexture(portrait);
		blit(x+119, y+40, 0, 0, 64, 64, 64, 64);
		
		this.minecraft.getTextureManager().bindTexture(PlayerStatsScreen.icons);
		this.blit(x + 5, y + 7, 238, 16, 18, 18);
		
		font.drawString(String.valueOf(PlayerSavedData.boondollars), x + 25, y + 12, 0x0094FF);
		
		for (int i = 0; i < 9; i++)
		{
			int price = container.getPrice(i);
			if (price == 0 || container.inventory.getStackInSlot(i).isEmpty())
				continue;
			String cost = String.valueOf(price)+"£";
			font.drawString(cost, x + 25 - font.getStringWidth(cost)/2 + 35*(i%3), y + 54 + 33*(i/3), 0x000000);
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