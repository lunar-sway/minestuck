package com.mraof.minestuck.client.renderer.entity;

import com.mraof.minestuck.entity.item.MetalBoatEntity;
import net.minecraft.client.renderer.entity.BoatRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.util.ResourceLocation;

public class MetalBoatRenderer extends BoatRenderer
{
	public MetalBoatRenderer(EntityRendererManager manager)
	{
		super(manager);
	}
	
	@Override
	public ResourceLocation getEntityTexture(BoatEntity entity)
	{
		MetalBoatEntity boat = (MetalBoatEntity) entity;
		
		return boat.boatType().getBoatTexture();
	}
}