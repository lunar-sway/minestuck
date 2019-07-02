package com.mraof.minestuck.client.gui;

import com.mraof.minestuck.client.gui.playerStats.GuiPlayerStats;
import com.mraof.minestuck.inventory.ContainerConsortMerchant;
import com.mraof.minestuck.inventory.InventoryConsortMerchant;
import com.mraof.minestuck.world.storage.PlayerSavedData;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GuiConsortShop extends GuiContainer
{
	private ResourceLocation guiBackground = new ResourceLocation("minestuck", "textures/gui/consort_shop.png");
	private ResourceLocation portrait;
	public InventoryConsortMerchant inv;
	
	public GuiConsortShop(EntityPlayer player)
	{
		super(new ContainerConsortMerchant(player, new InventoryConsortMerchant()));
		inv = ((ContainerConsortMerchant) this.inventorySlots).inventory;
		xSize = 192;
		ySize = 137;
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		if(inv.getConsortType() == null || inv.getMerchantType() == null)
			return;
		
		if(portrait == null)
			portrait = new ResourceLocation("minestuck",
					"textures/gui/store/"+inv.getConsortType().name().toLowerCase()+"_"+inv.getMerchantType().name().toLowerCase()+".png");
		
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		
		this.mc.getTextureManager().bindTexture(guiBackground);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
		
		this.mc.getTextureManager().bindTexture(portrait);
		drawModalRectWithCustomSizedTexture(x+119, y+40, 0, 0, 64, 64, 64, 64);
		
		this.mc.getTextureManager().bindTexture(GuiPlayerStats.icons);
		this.drawTexturedModalRect(x + 5, y + 7, 238, 16, 18, 18);
		
		mc.fontRenderer.drawString(String.valueOf(PlayerSavedData.boondollars), x + 25, y + 12, 0x0094FF);
		
		for (int i = 0; i < 9; i++)
		{
			int price = inv.getField(i + 2);
			if (price == 0 || inv.getStackInSlot(i).isEmpty())
				continue;
			String cost = String.valueOf(price)+"£";
			mc.fontRenderer.drawString(cost, x + 25 - mc.fontRenderer.getStringWidth(cost)/2 + 35*(i%3), y + 54 + 33*(i/3), 0x000000);
		}
	}
	
	@Override
	public void render(int mouseX, int mouseY, float partialTicks)
	{
		this.drawDefaultBackground();
		super.render(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}
}
