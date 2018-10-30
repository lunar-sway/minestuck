package com.mraof.minestuck.entity;

import com.mraof.minestuck.entity.ai.frog.EntityAIPanicHop;
import com.mraof.minestuck.entity.ai.frog.EntityAIStopHopping;
import com.mraof.minestuck.entity.ai.frog.EntityAIWanderHop;

import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class EntityFrog extends EntityMinestuck
{
	
	public EntityFrog(World world)
	{
		super(world);
	}
	
	@Override
	public String getTexture()
	{
		return "textures/mobs/frog.png";
	}
	
	@Override
	protected float getMaximumHealth() {
		// TODO Auto-generated method stub
		return 5;
	}
	
	@Override
	protected void initEntityAI()
	{
		tasks.addTask(0, new EntityAISwimming(this));
		tasks.addTask(1, new EntityAIPanicHop(this, 1.0D));
		tasks.addTask(4, new EntityAIMoveTowardsRestriction(this, 0.6F));
		tasks.addTask(5, new EntityAIWanderHop(this, 0.6D));
		tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		tasks.addTask(7, new EntityAILookIdle(this));
		tasks.addTask(8, new EntityAIStopHopping(this));
		
	}
}