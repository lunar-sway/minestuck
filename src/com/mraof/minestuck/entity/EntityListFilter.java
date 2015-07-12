package com.mraof.minestuck.entity;

import java.util.List;

import com.google.common.base.Predicate;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

public class EntityListFilter implements Predicate 
{
	public List<Class<? extends EntityLivingBase>> entityList;
	
	public boolean isEntityApplicable(Entity par1Entity)
	{
		return entityList.contains(par1Entity.getClass());
	}
	
	public EntityListFilter(List<Class<? extends EntityLivingBase>> entityList) 
	{
		this.entityList = entityList;
	}
	
	@Override
	public boolean apply(Object input)
	{
		return input instanceof Entity && isEntityApplicable((Entity) input);
	}
	
}
