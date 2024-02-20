package com.mraof.minestuck.client.model.entity;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.entity.underling.UnderlingEntity;
import net.minecraft.resources.ResourceLocation;

public class UnderlingModel<T extends UnderlingEntity> extends RotatingHeadAnimatedModel<T>
{
	private final String underlingName;
	
	public UnderlingModel(String underlingName)
	{
		this.underlingName = underlingName;
	}
	
	@Override
	public ResourceLocation getModelResource(T entity)
	{
		return Minestuck.id("geo/entity/underlings/" + this.underlingName + ".geo.json");
	}
	
	@Override
	public ResourceLocation getTextureResource(T entity)
	{
		return Minestuck.id("textures/entity/underlings/" + this.underlingName + ".png");
	}
	
	@Override
	public ResourceLocation getAnimationResource(T entity)
	{
		return Minestuck.id("animations/entity/underlings/" + this.underlingName + ".animation.json");
	}
}
