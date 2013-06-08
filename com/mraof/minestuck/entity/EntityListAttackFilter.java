package com.mraof.minestuck.entity;

import java.util.List;

import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureAttribute;

public class EntityListAttackFilter implements IEntitySelector 
{
	public List entitiesToAttack;
	@Override
    public boolean isEntityApplicable(Entity par1Entity)
    {
        return entitiesToAttack.contains(par1Entity.getClass());
    }
	public EntityListAttackFilter(List<Class<? extends EntityLiving>> entitiesToAttack) 
	{
		this.entitiesToAttack = entitiesToAttack;
	}
}		
	

