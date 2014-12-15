package com.mraof.minestuck.client.renderer.entity;

import com.mraof.minestuck.entity.item.EntityMetalBoat;

import net.minecraft.client.renderer.entity.RenderBoat;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.util.ResourceLocation;

public class RenderMetalBoat extends RenderBoat
{
	
	private static final ResourceLocation ironVariant = new ResourceLocation("minestuck", "textures/entity/IronBoat.png");
	
	public RenderMetalBoat(RenderManager manager)
	{
		super(manager);
	}
	
	@Override
	protected ResourceLocation getEntityTexture(EntityBoat entity)
	{
		EntityMetalBoat boat = (EntityMetalBoat) entity;
		
		return ironVariant;
	}
	
}
