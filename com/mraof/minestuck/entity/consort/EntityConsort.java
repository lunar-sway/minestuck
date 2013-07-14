package com.mraof.minestuck.entity.consort;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.world.World;

import com.mraof.minestuck.entity.EntityMinestuck;

public abstract class EntityConsort extends EntityMinestuck
{

	public EntityConsort(World world) 
	{
		super(world);
//		moveSpeed = 2.5F;
		
        this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a((double)(this.getMaxHealth()));
		this.setEntityHealth(this.getMaxHealth());
		
        setSize(0.6F, 1.5F);
        this.experienceValue = 1;
	}

	@Override
	protected float getMaxHealth() 
	{
		return 10;
	}
}