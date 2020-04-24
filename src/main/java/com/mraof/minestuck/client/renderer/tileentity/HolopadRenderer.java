package com.mraof.minestuck.client.renderer.tileentity;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mraof.minestuck.tileentity.HolopadTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.item.ItemStack;

public class HolopadRenderer extends TileEntityRenderer<HolopadTileEntity>
{
	@Override
	public void render(HolopadTileEntity tileEntityIn, double x, double y, double z, float partialTicks, int destroyStage)
	{
		super.render(tileEntityIn, x, y, z, partialTicks, destroyStage);
		
		if(tileEntityIn.hasCard())
		{
			ItemStack item = tileEntityIn.getHoloItem();
			float f = (float) tileEntityIn.innerRotation + partialTicks;
			GlStateManager.pushMatrix();
			GlStateManager.translatef((float) x + 0.5F, (float) y + 0.6F, (float) z + 0.5F);
			GlStateManager.rotatef(f / 20.0F * 57.295776F, 0.0F, 1.0F, 0.0F);
			Minecraft.getInstance().getItemRenderer().renderItem(item, ItemCameraTransforms.TransformType.GROUND);
			GlStateManager.popMatrix();
		}
		
	}
}
