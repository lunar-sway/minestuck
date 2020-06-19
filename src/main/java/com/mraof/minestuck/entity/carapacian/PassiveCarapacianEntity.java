package com.mraof.minestuck.entity.carapacian;

import com.mraof.minestuck.entity.MinestuckEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public abstract class PassiveCarapacianEntity extends MinestuckEntity
{
    public PassiveCarapacianEntity(EntityType<? extends PassiveCarapacianEntity> type, World world)
    {
        super(type, world);
    }

    @Override
    protected void registerGoals()
    {
        this.goalSelector.addGoal(1, new SwimGoal(this));
        this.goalSelector.addGoal(2, new PanicGoal(this, 0.4D));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomWalkingGoal(this, this.getWanderSpeed()));
        this.goalSelector.addGoal(6, new LookRandomlyGoal(this));
        this.goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 8.0F));
    }

    public abstract float getWanderSpeed();
}
