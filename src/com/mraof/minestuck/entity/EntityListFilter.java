package com.mraof.minestuck.entity;

import java.util.List;
import java.util.function.Predicate;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;

public class EntityListFilter implements Predicate<Entity>
{
	public List<EntityType<?>> entityList;

	public boolean isEntityApplicable(Entity entity)
	{
	    for(EntityType<?> type : entityList) {
	    	if(entity.getType() == type) {
	    		return true;
		    }
	    }
		return false;
	}

	public EntityListFilter(List<EntityType<?>> entityList)
	{
		this.entityList = entityList;
	}
	
	@Override
	public boolean test(Entity entity)
	{
		return isEntityApplicable(entity);
	}
}