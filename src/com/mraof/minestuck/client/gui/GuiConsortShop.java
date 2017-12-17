package com.mraof.minestuck.client.gui;

import com.mraof.minestuck.inventory.ContainerConsortMerchant;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiConsortShop extends GuiContainer //It probably doesn't even need to be a container to achieve this result
{
	private ResourceLocation guiBackground = new ResourceLocation("minestuck", "textures/gui/consort_shop.png");
	private ResourceLocation portrait;
	public ContainerConsortMerchant container;
	
	public GuiConsortShop(EntityPlayer player)
	{
		super(new ContainerConsortMerchant(player));
		container = (ContainerConsortMerchant) this.inventorySlots;
		xSize = 192;
		ySize = 137;
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		if(container.consortType == null || container.merchantType == null)
			return;
		
		if(portrait == null)
			portrait = new ResourceLocation("minestuck",
					"textures/gui/store/"+container.consortType.name().toLowerCase()+"_"+container.merchantType.name().toLowerCase()+".png");
		
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		
		this.mc.getTextureManager().bindTexture(guiBackground);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
		
		this.mc.getTextureManager().bindTexture(portrait);
		drawModalRectWithCustomSizedTexture(x+119, y+40, 0, 0, 64, 64, 64, 64);
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}
}
