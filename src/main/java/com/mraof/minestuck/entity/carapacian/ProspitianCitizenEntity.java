package com.mraof.minestuck.entity.carapacian;

import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

public class ProspitianCitizenEntity extends PassiveCarapacianEntity
{
    public ProspitianCitizenEntity(EntityType<? extends ProspitianCitizenEntity> type, World world)
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
