package com.mraof.minestuck.client.model.entity;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.entity.consort.ConsortEntity;
import net.minecraft.resources.ResourceLocation;

public class ConsortModel<T extends ConsortEntity> extends RotatingHeadAnimatedModel<T>
{
	@Override
	public ResourceLocation getModelLocation(T entity)
	{
		return new ResourceLocation(Minestuck.MOD_ID, "geo/entity/consort/" + getName(entity) + ".geo.json");
	}
	
	@Override
	public ResourceLocation getTextureLocation(T entity)
	{
		return new ResourceLocation(Minestuck.MOD_ID, "textures/entity/consort/" + getName(entity) + ".png");
	}
	
	@Override
	public ResourceLocation getAnimationFileLocation(T entity)
	{
		return new ResourceLocation(Minestuck.MOD_ID, "animations/entity/consort/" + getName(entity) + ".animation.json");
	}
	
	public static String getName(ConsortEntity consort)
	{
		return consort.getConsortType().getName();
	}
}
