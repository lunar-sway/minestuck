package com.mraof.minestuck.entity;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public abstract class EntityMinestuck extends EntityCreature 
{
	protected ResourceLocation textureResource;
	
	public EntityMinestuck(World par1World) 
	{
		super(par1World);
	}
	
	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue((double)(this.getMaximumHealth()));
	}
	
	protected abstract float getMaximumHealth();
	
	/**
	 * Returns null if it handles the texture itself.
	 * @return
	 */
	public abstract String getTexture();

	public ResourceLocation getTextureResource() 
	{
		if(textureResource == null && this.getTexture() != null)
			textureResource = new ResourceLocation("minestuck", this.getTexture());
		return textureResource;
	}
	
}
