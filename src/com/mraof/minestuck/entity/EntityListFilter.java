package com.mraof.minestuck.entity;

import java.util.List;

import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

public class EntityListFilter implements IEntitySelector 
{
	public List<Class<? extends EntityLivingBase>> entityList;
	@Override
	public boolean isEntityApplicable(Entity par1Entity)
	{
		return entityList.contains(par1Entity.getClass());
	}
	public EntityListFilter(List<Class<? extends EntityLivingBase>> entityList) 
	{
		this.entityList = entityList;
	}
}		


