package com.mraof.minestuck.entity.ai.frog;

import com.mraof.minestuck.entity.EntityFrog;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.PathNavigateFlying;
import net.minecraft.pathfinding.PathNavigateGround;

public class EntityAIStopHopping extends EntityAIBase 
{

	 private final EntityLiving entity;

	 public EntityAIStopHopping(EntityLiving entityIn)
	    {
	        this.entity = entityIn;
	        this.setMutexBits(4);
	    }


		/**
	     * Keep ticking a continuous task that has already been started
	     */
	    public void updateTask()
	    {
	            this.entity.getJumpHelper().setJumping();
	    }
	
	@Override
	public boolean shouldExecute() {
		// TODO Auto-generated method stub
		return false;
	}

}
