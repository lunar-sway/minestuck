package com.mraof.minestuck.entity;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public interface IEntityMultiPart
{
    Level getLevel();

    boolean attackEntityFromPart(Entity entityPart, DamageSource source, float damage);
    void updatePartPositions();
    void addPart(Entity entityPart, int id);
    void onPartDeath(Entity entityPart, int id);
}
