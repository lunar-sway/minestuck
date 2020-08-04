package com.mraof.minestuck.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mraof.minestuck.entity.item.HologramEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.IRendersAsItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

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
		matrixStackIn.rotate(new Quaternion(Vector3f.ZP, 180.0f, true));
		matrixStackIn.scale(scale, scale, scale);
		matrixStackIn.translate(0.0D, 0.5D, 0.0D);
		matrixStackIn.rotate(new Quaternion(Vector3f.YP, (f) / 20.0F * (180F / (float)Math.PI), true));
		Minecraft.getInstance().getItemRenderer().renderItem(item, ItemCameraTransforms.TransformType.GROUND, packedLightIn, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn);
		matrixStackIn.translate(0.0F, 0.0F, 0.0F);

		super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	}
	
	protected boolean canRenderName(HologramEntity entity)
    {
        return super.canRenderName(entity) && (entity.getAlwaysRenderNameTagForRender() || entity.hasCustomName() && entity == this.renderManager.pointedEntity);
    }

	@Override
	public ResourceLocation getEntityTexture(HologramEntity entity)
	{
		return null;
	}

}
