package com.mraof.minestuck.client.renderer.entity;

import com.mraof.minestuck.entity.EntityDecoy;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderDecoy extends RendererLivingEntity {	//Most stuff here will be directly copied from RenderPlayer.
	
	
	public RenderDecoy() {
		super(new ModelBiped(), 0.5F);
	}
	
	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return ((EntityDecoy)entity).getLocationSkin();
	}
	
}
