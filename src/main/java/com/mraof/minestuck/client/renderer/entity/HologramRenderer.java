package com.mraof.minestuck.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mraof.minestuck.entity.item.HologramEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;

public class HologramRenderer extends EntityRenderer<HologramEntity>
{

	public HologramRenderer(EntityRendererManager manager)
	{
		super(manager);
	}

	@Override
	public void render(HologramEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn)
	{
		ItemStack item = entityIn.getItem();
		float f = (float) entityIn.innerRotation + partialTicks;
		float scale = 0.8f;

		RenderSystem.color4f(0f, 1f, 1f, 0.5f);
		matrixStackIn.mulPose(new Quaternion(Vector3f.ZP, 180.0f, true));
		matrixStackIn.scale(scale, scale, scale);
		matrixStackIn.translate(0.0D, 0.5D, 0.0D);
		matrixStackIn.mulPose(new Quaternion(Vector3f.YP, (f) / 20.0F * (180F / (float)Math.PI), true));
		Minecraft.getInstance().getItemRenderer().renderStatic(item, ItemCameraTransforms.TransformType.GROUND, packedLightIn, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn);
		matrixStackIn.translate(0.0F, 0.0F, 0.0F);

		super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
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
