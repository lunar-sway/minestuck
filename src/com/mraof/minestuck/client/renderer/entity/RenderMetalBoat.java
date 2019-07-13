package com.mraof.minestuck.client.renderer.entity;

import com.mraof.minestuck.entity.item.MetalBoatEntity;

import net.minecraft.client.renderer.entity.RenderBoat;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.util.ResourceLocation;

public class RenderMetalBoat extends RenderBoat
{
	
	private static final ResourceLocation ironVariant = new ResourceLocation("minestuck", "textures/entity/iron_boat.png");
	private static final ResourceLocation goldVariant = new ResourceLocation("minestuck", "textures/entity/gold_boat.png");
	
	public RenderMetalBoat(RenderManager manager)
	{
		super(manager);
	}
	
	@Override
	protected ResourceLocation getEntityTexture(EntityBoat entity)
	{
		MetalBoatEntity boat = (MetalBoatEntity) entity;
		
		if(boat.type == 1)
			return goldVariant;
		else return ironVariant;
	}
}