package com.mraof.minestuck.client.renderer.entity;

import com.mraof.minestuck.entity.item.EntityHologram;
import com.mraof.minestuck.item.MinestuckItems;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class RenderHologram extends Render<EntityHologram>
{

	public RenderHologram(RenderManager manager) 
	{
		super(manager);
	}
	
	@Override
	public void doRender(EntityHologram entity, double x, double y, double z, float entityYaw, float partialTicks) {
		
		float f = (float)entity.innerRotation + partialTicks;
		
		GlStateManager.color(0f, 1f, 1f, 0.5f);
		
		GlStateManager.pushMatrix();
		//GlStateManager.rotate(180, 0, 0, 1);
		GlStateManager.scale(1, 1, 1);
        GlStateManager.translate((float)x, (float)y, (float)z);
                GlStateManager.rotate((f) / 20.0F * (180F / (float)Math.PI), 0.0F, 1.0F, 0.0F);
		Minecraft.getMinecraft().getRenderItem().renderItem(new ItemStack(MinestuckItems.itemFrog), ItemCameraTransforms.TransformType.FIXED);

      //  GlStateManager.translate((float)x, (float)y, (float)z);
		GlStateManager.popMatrix();
	}
	
	protected boolean canRenderName(EntityHologram entity)
    {
        return super.canRenderName(entity) && (entity.getAlwaysRenderNameTagForRender() || entity.hasCustomName() && entity == this.renderManager.pointedEntity);
    }

	@Override
	protected ResourceLocation getEntityTexture(EntityHologram entity) {
		// TODO Auto-generated method stub
		return null;
	}

}
