package com.mraof.minestuck.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class EntityBlackPawn extends EntityPawn
{
	public EntityBlackPawn(World world) 
	{
		super(world);
		texture = "/mods/Minestuck/textures/mobs/Dersite.png";
	}
	public EntityBlackPawn(World world, int type) 
	{
		super(world, type);
		texture = "/mods/Minestuck/textures/mobs/Dersite.png";
	}
	@Override
	public void setEnemies() 
	{
		enemyClasses.add(EntityWhitePawn.class);
		enemyClasses.add(EntityWhiteBishop.class);
		enemyClasses.add(EntitySlime.class);
		super.setEnemies();
	}
	@Override
	public void setAllies() 
	{
		allyClasses.add(EntityBlackPawn.class);
		allyClasses.add(EntityBlackBishop.class);
	}

}
