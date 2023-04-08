package com.mraof.minestuck.client.model.entity;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.entity.underling.UnderlingEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

public class UnderlingModel<T extends UnderlingEntity> extends RotatingHeadAnimatedModel<T>
{
	@Override
	public ResourceLocation getModelResource(T entity)
	{
		return new ResourceLocation(Minestuck.MOD_ID, "geo/entity/underlings/" + getName(entity) + ".geo.json");
	}
	
	@Override
	public ResourceLocation getTextureResource(T entity)
	{
		return new ResourceLocation(Minestuck.MOD_ID, "textures/entity/underlings/" + getName(entity) + ".png");
	}
	
	@Override
	public ResourceLocation getAnimationResource(T entity)
	{
		return new ResourceLocation(Minestuck.MOD_ID, "animations/entity/underlings/" + getName(entity) + ".animation.json");
	}
	
	public static String getName(UnderlingEntity entity)
	{
		return Objects.requireNonNull(ForgeRegistries.ENTITY_TYPES.getKey(entity.getType()), () -> "Getting resource for entity without a registry name! " + entity).getPath();
	}
}
