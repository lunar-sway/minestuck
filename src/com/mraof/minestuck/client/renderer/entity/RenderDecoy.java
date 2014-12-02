package com.mraof.minestuck.client.renderer.entity;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import com.mraof.minestuck.entity.EntityDecoy;

public class RenderDecoy extends RenderBiped
{
	
	public RenderDecoy(RenderManager manager)
	{
		super(manager, new ModelBiped(), 0.5F);
	}
	
	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return ((EntityDecoy)entity).getLocationSkin();
	}
	
}
