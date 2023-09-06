package com.mraof.minestuck.client.model.blockentity;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.blockentity.machine.AlchemiterBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class AlchemiterModel extends GeoModel<AlchemiterBlockEntity>
{
	@Override
	public ResourceLocation getModelResource(AlchemiterBlockEntity object)
	{
		return new ResourceLocation(Minestuck.MOD_ID, "geo/blockentity/alchemiter.geo.json");
	}
	
	@Override
	public ResourceLocation getTextureResource(AlchemiterBlockEntity object)
	{
		return new ResourceLocation(Minestuck.MOD_ID, "textures/block/machine/alchemiter.png");
	}
	
	@Override
	public ResourceLocation getAnimationResource(AlchemiterBlockEntity animatable)
	{
		return new ResourceLocation(Minestuck.MOD_ID, "animations/blockentity/alchemiter.animation.json");
	}
}
