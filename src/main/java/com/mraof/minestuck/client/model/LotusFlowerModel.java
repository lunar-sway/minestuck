package com.mraof.minestuck.client.model;

import com.mraof.minestuck.Minestuck;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class LotusFlowerModel extends AnimatedGeoModel
{
	@Override
	public ResourceLocation getAnimationFileLocation(Object entity)
	{
		return new ResourceLocation(Minestuck.MOD_ID, "animations/lotus_flower.animation.json");
	}
	
	@Override
	public ResourceLocation getModelLocation(Object animatable)
	{
		return new ResourceLocation(Minestuck.MOD_ID, "geo/lotus_flower.geo.json");
	}
	
	@Override
	public ResourceLocation getTextureLocation(Object entity)
	{
		return new ResourceLocation(Minestuck.MOD_ID, "textures/block/lotus_flower.png");
	}
}
