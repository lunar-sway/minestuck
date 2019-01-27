package com.mraof.minestuck.entity.ai.frog;

import com.mraof.minestuck.entity.EntityFrog;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.pathfinding.PathNavigateFlying;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.math.Vec3d;

public class EntityAIWanderHop extends EntityAIWander
{
    private final EntityLiving entity;
    private boolean shouldHop = false;
    
    public EntityAIWanderHop(EntityCreature creatureIn, double speedIn)
    {
        this(creatureIn, speedIn, 120); 
    }

    public EntityAIWanderHop(EntityCreature creatureIn, double speedIn, int chance)
    {
    	
    	super(creatureIn, speedIn, chance);
    	
        this.entity = creatureIn;
        this.executionChance = chance;
        this.setMutexBits(1);
        
    }
    
    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if (!this.mustUpdate)
        {
            if (this.entity.getIdleTime() >= 100)
            {
                shouldHop =  false;
                return shouldHop;
            }

            if (this.entity.getRNG().nextInt(this.executionChance) != 0)
            {
                shouldHop = false;
                return shouldHop;
            }
        }

        Vec3d vec3d = this.getPosition();

        if (vec3d == null)
        {
            shouldHop =  false;
            return shouldHop;
        }
        else
        {
            this.x = vec3d.x;
            this.y = vec3d.y;
            this.z = vec3d.z;
            this.mustUpdate = false;
            shouldHop =  true;
            return shouldHop;
        }

    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    public void updateTask()
    {
        if (this.entity.getRNG().nextFloat() < 0.8F && shouldHop)
        {
            //((EntityFrog) this.entity).setJump(true);
        }
    }
}