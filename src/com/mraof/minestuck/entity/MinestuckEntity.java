package com.mraof.minestuck.entity;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.Objects;

public abstract class MinestuckEntity extends CreatureEntity
{
	private ResourceLocation textureResource;
	
	public MinestuckEntity(EntityType<? extends MinestuckEntity> type, World world)
	{
		super(type, world);
	}
	
	@Override
	protected void registerAttributes()
	{
		super.registerAttributes();
		this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(this.getMaximumHealth());
	}
	
	protected abstract float getMaximumHealth();
	
	protected ResourceLocation createTexture()
	{
		ResourceLocation entityName = Objects.requireNonNull(getType().getRegistryName(), () -> "Getting texture for entity without a registry name! "+this);
		
		return new ResourceLocation(entityName.getNamespace(), "textures/entity/" + entityName.getPath() + ".png");
	}

	public final ResourceLocation getTextureResource()
	{
		if(textureResource == null)
			textureResource = createTexture();
		return textureResource;
	}
	
	protected void clearTexture()
	{
		textureResource = null;
	}
}