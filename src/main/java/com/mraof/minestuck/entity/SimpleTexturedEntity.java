package com.mraof.minestuck.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.Objects;

public abstract class SimpleTexturedEntity extends AnimatedCreatureEntity
{
	private ResourceLocation textureResource;
	
	public SimpleTexturedEntity(EntityType<? extends SimpleTexturedEntity> type, World world)
	{
		super(type, world);
	}
	
	protected ResourceLocation createTexture()
	{
		ResourceLocation entityName = Objects.requireNonNull(getType().getRegistryName(), () -> "Getting texture for entity without a registry name! " + this);
		
		return new ResourceLocation(entityName.getNamespace(), "textures/entity/" + entityName.getPath() + ".png");
	}
	
	public final ResourceLocation getTextureResource()
	{
		if(textureResource == null)
			textureResource = createTexture();
		return textureResource;
	}
}