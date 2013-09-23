package com.mraof.minestuck.entity;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public abstract class EntityMinestuck extends EntityCreature 
{
	protected ResourceLocation textureResource;
	public EntityMinestuck(World par1World, Object... objects) 
	{
		super(par1World);
		
		setCustomStartingVariables(objects);
		
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setAttribute((double)(this.getMaximumHealth()));
		this.setHealth(this.getMaximumHealth());
		
		this.textureResource = new ResourceLocation("minestuck", this.getTexture());
	}

	protected void setCustomStartingVariables(Object[] objects)
	{
		
	}
	protected abstract float getMaximumHealth();

	public abstract String getTexture();

	public ResourceLocation getTextureResource() 
	{
		return textureResource;
	}
	
	@Override
	public void updateFallState(double par1, boolean par3) {
		super.updateFallState(par1, par3);
	}

}
