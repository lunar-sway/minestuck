package com.mraof.minestuck.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mraof.minestuck.blockentity.HolopadBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemDisplayContext;
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
			poseStack.pushPose();
			poseStack.translate(0.5F, 0.6F, 0.5F);
			poseStack.mulPose(Axis.YP.rotation(((float) blockEntityIn.getRotationTickForRender() + partialTicks) / 20.0F));
			Minecraft.getInstance().getItemRenderer().renderStatic(item, ItemDisplayContext.GROUND, combinedLightIn, combinedOverlayIn, poseStack, bufferIn, blockEntityIn.getLevel(), (int)blockEntityIn.getBlockPos().asLong());
			poseStack.popPose();
		}
	}
}
