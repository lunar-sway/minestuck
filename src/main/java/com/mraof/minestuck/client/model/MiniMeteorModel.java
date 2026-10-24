package com.mraof.minestuck.client.model;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.entity.MiniMeteorEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class MiniMeteorModel extends GeoModel<MiniMeteorEntity>
{
	@Override
	public ResourceLocation getModelResource(MiniMeteorEntity entity)
	{
		return Minestuck.id("geo/meteor.geo.json");
	}
	
	@Override
	public ResourceLocation getTextureResource(MiniMeteorEntity entity)
	{
		int ticksPerFrame = 2;
		int frame = (entity.tickCount / ticksPerFrame) % 3;
		return switch(frame)
		{
			case 0 -> Minestuck.id("textures/entity/meteor_0.png");
			case 1 -> Minestuck.id("textures/entity/meteor_1.png");
			default -> Minestuck.id("textures/entity/meteor_2.png");
		};
	}
	
	@Override
	public ResourceLocation getAnimationResource(MiniMeteorEntity entity)
	{
		return Minestuck.id("animations/meteor.animation.json");
	}
}