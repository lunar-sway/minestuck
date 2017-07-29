package com.mraof.minestuck.client.gui;

import com.mraof.minestuck.inventory.ContainerConsortMerchant;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiConsortShop extends GuiContainer //It probably doesn't even need to be a container to achieve this result
{
	private ResourceLocation guiBackground = new ResourceLocation("minestuck", "textures/gui/consort_shop.png");
	
	public GuiConsortShop(EntityPlayer player)
	{
		super(new ContainerConsortMerchant(player));
		xSize = 192;
		ySize = 137;
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		
		this.mc.getTextureManager().bindTexture(guiBackground);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
	}
}
