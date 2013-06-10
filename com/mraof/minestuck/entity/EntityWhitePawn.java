package com.mraof.minestuck.entity;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.world.World;

public class EntityWhitePawn extends EntityPawn 
{
	public EntityWhitePawn(World world) 
	{
		super(world);
		texture = "/mods/Minestuck/textures/mobs/Prospitian.png";
	}
	public EntityWhitePawn(World world, int type) 
	{
		super(world, type);
		texture = "/mods/Minestuck/textures/mobs/Prospitian.png";
	}
	@Override
	public void setEnemies() 
	{
		enemyClasses.add(EntityBlackPawn.class);
		enemyClasses.add(EntityBlackBishop.class);
		enemyClasses.add(EntityZombie.class);
		super.setEnemies();
	}
	@Override
	public void setAllies() 
	{
		allyClasses.add(EntityWhitePawn.class);
		allyClasses.add(EntityWhiteBishop.class);
	}
}
