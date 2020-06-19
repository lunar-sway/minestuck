package com.mraof.minestuck.entity.carapacian;

import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

public class DersiteCitizenEntity extends PassiveCarapacianEntity
{
    public DersiteCitizenEntity(EntityType<? extends DersiteCitizenEntity> type, World world)
    {
        super(type, world);
    }

    @Override
    public float getWanderSpeed()
    {
        return .2F;
    }

    @Override
    protected float getMaximumHealth()
    {
        return 40;
    }
}
