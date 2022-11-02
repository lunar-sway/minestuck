package com.mraof.minestuck.entity;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;

public interface IEntityMultiPart
{
    boolean attackEntityFromPart(Entity entityPart, DamageSource source, float damage);
    void updatePartPositions();
    void addPart(Entity entityPart, int id);
    void onPartDeath(Entity entityPart, int id);
	
	default Entity asEntity()
	{
		return (Entity) this;
	}
}
