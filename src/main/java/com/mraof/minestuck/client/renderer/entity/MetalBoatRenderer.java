package com.mraof.minestuck.client.renderer.entity;

import com.mraof.minestuck.entity.item.MetalBoatEntity;
import net.minecraft.client.renderer.entity.BoatRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.vehicle.Boat;

public class MetalBoatRenderer extends BoatRenderer
{
	public MetalBoatRenderer(EntityRendererProvider.Context context)
	{
		super(context);
	}
	
	@Override
	public ResourceLocation getTextureLocation(Boat entity)
	{
		MetalBoatEntity boat = (MetalBoatEntity) entity;
		
		return boat.boatType().getBoatTexture();
	}
}