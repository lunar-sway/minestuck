package com.mraof.minestuck.client.renderer.entity;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mraof.minestuck.entity.item.HologramEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class HologramRenderer extends EntityRenderer<HologramEntity>
{

	public HologramRenderer(EntityRendererManager manager)
	{
		super(manager);
	}
	
//	@Override
//	public void doRender(HologramEntity entity, double x, double y, double z, float entityYaw, float partialTicks) {
//
//		ItemStack item = entity.getItem();
//		float f = (float)entity.innerRotation + partialTicks;
//		float scale = 0.8f;
//
//		GlStateManager.color4f(0f, 1f, 1f, 0.5f);
//
//		GlStateManager.pushMatrix();
//		//GlStateManager.rotate(180, 0, 0, 1);
//		GlStateManager.scalef(scale, scale, scale);
//        GlStateManager.translatef((float)x, (float)y+0.5F, (float)z);
//                GlStateManager.rotatef((f) / 20.0F * (180F / (float)Math.PI), 0.0F, 1.0F, 0.0F);
//		Minecraft.getInstance().getItemRenderer().renderItem(item, ItemCameraTransforms.TransformType.GROUND);
//		Minecraft.getInstance().getItemRenderer().renderIt
//
//      //  GlStateManager.translate((float)x, (float)y, (float)z);
//		GlStateManager.popMatrix();
//	}
	
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
