package com.mraof.minestuck.client.renderer.entity.frog;

import com.mraof.minestuck.client.model.ModelFrog;
import com.mraof.minestuck.entity.EntityFrog;
import com.mraof.minestuck.entity.EntityMinestuck;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderFrog extends RenderLivingBase<EntityFrog>
{

	public RenderFrog(RenderManager manager, ModelBase par1ModelBase, float par2) 
	{
		super(manager, new ModelFrog(), par2);
		this.addLayer(new LayerFrogSkin(this));
		this.addLayer(new LayerFrogEyes(this));
		this.addLayer(new LayerFrogBelly(this));
		
	}

	@Override
	protected void preRenderCallback(EntityFrog frog, float partialTickTime) 
	{
		float scale = frog.getFrogSize();
		GlStateManager.scale(scale,scale,scale);
	}
	
	@Override
	protected ResourceLocation getEntityTexture(EntityFrog entity) {
		return ((EntityMinestuck) entity).getTextureResource();
	}
	
	protected boolean canRenderName(EntityFrog entity)
    {
        return super.canRenderName(entity) && (entity.getAlwaysRenderNameTagForRender() || entity.hasCustomName() && entity == this.renderManager.pointedEntity);
    }

}
