package com.mraof.minestuck.client.renderer.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mraof.minestuck.tileentity.HolopadTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;

public class HolopadRenderer extends TileEntityRenderer<HolopadTileEntity>
{
	public HolopadRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
		super(rendererDispatcherIn);
	}

	@Override
	public void render(HolopadTileEntity tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn)
	{
		if(tileEntityIn.hasCard())
		{
			ItemStack item = tileEntityIn.getHoloItem();
			float f = (float) tileEntityIn.innerRotation + partialTicks;
			matrixStackIn.push();
			matrixStackIn.translate(0.5F, 0.6F, 0.5F);
			matrixStackIn.rotate(Vector3f.YP.rotation((f / 20.0F * 57.295776F) / 75));
			Minecraft.getInstance().getItemRenderer().renderItem(item, ItemCameraTransforms.TransformType.GROUND, combinedLightIn, combinedOverlayIn, matrixStackIn, bufferIn);
			matrixStackIn.pop();
		}
	}
}
