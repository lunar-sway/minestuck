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
	    for(Class<? extends EntityLivingBase> clazz : entityList) {
	    	if(clazz.isInstance(par1Entity)) {
	    		return true;
		    }
	    }
		return false;
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
