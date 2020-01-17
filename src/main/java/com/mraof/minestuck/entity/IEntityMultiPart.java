package com.mraof.minestuck.entity;

import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public interface IEntityMultiPart
{
    World getWorld();

    boolean attackEntityFromPart(Entity entityPart, DamageSource source, float damage);
    void updatePartPositions();
    void addPart(Entity entityPart, int id);
    void onPartDeath(Entity entityPart, int id);
}
