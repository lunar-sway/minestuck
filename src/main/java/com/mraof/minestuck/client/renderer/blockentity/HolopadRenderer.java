package com.mraof.minestuck.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.mraof.minestuck.blockentity.HolopadBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemStack;

public class HolopadRenderer implements BlockEntityRenderer<HolopadBlockEntity>
{
	public HolopadRenderer(BlockEntityRendererProvider.Context context)
	{
	
	}

	@Override
	public void render(HolopadBlockEntity blockEntityIn, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn)
	{
		if(blockEntityIn.hasCard())
		{
			ItemStack item = blockEntityIn.getHoloItem();
			float f = (float) blockEntityIn.innerRotation + partialTicks;
			poseStack.pushPose();
			poseStack.translate(0.5F, 0.6F, 0.5F);
			poseStack.mulPose(Vector3f.YP.rotation((f / 20.0F * 57.295776F) / 75));
			Minecraft.getInstance().getItemRenderer().renderStatic(item, ItemTransforms.TransformType.GROUND, combinedLightIn, combinedOverlayIn, poseStack, bufferIn, (int)blockEntityIn.getBlockPos().asLong());
			poseStack.popPose();
		}
	}
}
