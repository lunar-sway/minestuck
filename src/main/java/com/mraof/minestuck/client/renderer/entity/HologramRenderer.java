package com.mraof.minestuck.client.renderer.entity;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mraof.minestuck.entity.item.HologramEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class HologramRenderer extends EntityRenderer<HologramEntity>
{

	public HologramRenderer(EntityRendererProvider.Context context)
	{
		super(context);
	}

	@Override
	public void render(HologramEntity entityIn, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn)
	{
		ItemStack item = entityIn.getItem();
		float f = (float) entityIn.innerRotation + partialTicks;
		float scale = 0.8f;

		RenderSystem.setShaderColor(0f, 1f, 1f, 0.5f);
		poseStack.mulPose(Axis.ZP.rotationDegrees(180.0f));
		poseStack.scale(scale, scale, scale);
		poseStack.translate(0.0D, 0.5D, 0.0D);
		poseStack.mulPose(Axis.YP.rotation(f / 20.0F));
		Minecraft.getInstance().getItemRenderer().renderStatic(item, ItemDisplayContext.GROUND, packedLightIn, OverlayTexture.NO_OVERLAY, poseStack, bufferIn, entityIn.level(), entityIn.getId());
		poseStack.translate(0.0F, 0.0F, 0.0F);

		super.render(entityIn, entityYaw, partialTicks, poseStack, bufferIn, packedLightIn);
	}
	
	protected boolean shouldShowName(HologramEntity entity)
    {
        return super.shouldShowName(entity) && (entity.shouldShowName() || entity.hasCustomName() && entity == this.entityRenderDispatcher.crosshairPickEntity);
    }

	@Override
	public ResourceLocation getTextureLocation(HologramEntity entity)
	{
		return null;
	}

}
