package com.mraof.minestuck.entity.underling;

import net.minecraft.world.World;

import com.mraof.minestuck.util.GristSet;
import com.mraof.minestuck.util.GristType;

public class EntityBasilisk extends EntityUnderling {

	public EntityBasilisk(World par1World, GristType type, String underlingName) 
	{
		super(par1World, type, underlingName);
	}

	@Override
	public GristSet getGristSpoils() 
	{
		return null;
	}

	@Override
	protected void setCombatTask() 
	{

	}

	@Override
	protected float getMaxHealth() 
		{
		return 0;
	}

	@Override
	protected float getWanderSpeed() {
		// TODO Auto-generated method stub
		return 0;
	}

}
