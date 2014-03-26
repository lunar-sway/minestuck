package com.mraof.minestuck.entity;

import java.util.List;

import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

public class EntityListAttackFilter implements IEntitySelector 
{
	public List<Class<? extends EntityLivingBase>> entitiesToAttack;
	@Override
    public boolean isEntityApplicable(Entity par1Entity)
    {
        return entitiesToAttack.contains(par1Entity.getClass());
    }
	public EntityListAttackFilter(List<Class<? extends EntityLivingBase>> entitiesToAttack) 
	{
		this.entitiesToAttack = entitiesToAttack;
	}
}		
	

