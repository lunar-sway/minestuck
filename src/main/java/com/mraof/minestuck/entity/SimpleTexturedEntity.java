package com.mraof.minestuck.entity;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

public abstract class SimpleTexturedEntity extends PathfinderMob
{
	private ResourceLocation textureResource;
	
	public SimpleTexturedEntity(EntityType<? extends SimpleTexturedEntity> type, Level level)
	{
		super(type, level);
	}
	
	protected ResourceLocation createTexture()
	{
		ResourceLocation entityName = Objects.requireNonNull(ForgeRegistries.ENTITY_TYPES.getKey(this.getType()), () -> "Getting texture for entity without a registry name! "+this);
		
		return new ResourceLocation(entityName.getNamespace(), "textures/entity/" + entityName.getPath() + ".png");
	}

	public final ResourceLocation getTextureResource()
	{
		if(textureResource == null)
			textureResource = createTexture();
		return textureResource;
	}
}