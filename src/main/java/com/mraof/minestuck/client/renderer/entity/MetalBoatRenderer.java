package com.mraof.minestuck.client.renderer.entity;

import com.mraof.minestuck.entity.item.MetalBoatEntity;
import net.minecraft.client.renderer.entity.BoatRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.util.ResourceLocation;

public class MetalBoatRenderer extends BoatRenderer
{
	
	private static final ResourceLocation ironVariant = new ResourceLocation("minestuck", "textures/entity/iron_boat.png");
	private static final ResourceLocation goldVariant = new ResourceLocation("minestuck", "textures/entity/gold_boat.png");
	
	public MetalBoatRenderer(EntityRendererManager manager)
	{
		super(manager);
	}
	
	@Override
	protected ResourceLocation getEntityTexture(BoatEntity entity)
	{
		MetalBoatEntity boat = (MetalBoatEntity) entity;
		
		if(boat.boatType() == 1)
			return goldVariant;
		else return ironVariant;
	}
}