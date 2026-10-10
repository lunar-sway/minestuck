package com.mraof.minestuck.client.model;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.entity.MeteorEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class MeteorModel extends GeoModel<MeteorEntity>
{
	
	@Override
	public ResourceLocation getModelResource(MeteorEntity entity)
	{
		return Minestuck.id("geo/meteor.geo.json");
	}
	
	@Override
	public ResourceLocation getTextureResource(MeteorEntity entity)
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
	public ResourceLocation getAnimationResource(MeteorEntity entity)
	{
		return Minestuck.id("animations/meteor.animation.json");
	}
}