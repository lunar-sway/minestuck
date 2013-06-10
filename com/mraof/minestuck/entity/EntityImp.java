package com.mraof.minestuck.entity;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.IMob;
import net.minecraft.world.World;

public class EntityImp extends EntityMob implements IMob{

	public EntityImp(World world) 
	{
		super(world);
		moveSpeed = 2.5F;
        setSize(0.5F, 1.0F);
        this.experienceValue = 3;
		texture = "/mods/Minestuck/textures/mobs/ShaleImp.png";
	} 

	@Override
	public int getMaxHealth() 
	{
		return 10;
	}
	
	@Override
	public void onLivingUpdate() 
	{
		if(this.rand.nextDouble() < .0001)
		{
			this.motionX += rand.nextInt(33) - 16;
			this.motionZ += rand.nextInt(33) - 16;
		}
		super.onLivingUpdate();
	}

}
