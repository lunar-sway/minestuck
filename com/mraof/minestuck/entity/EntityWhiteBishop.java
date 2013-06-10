package com.mraof.minestuck.entity;

import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.world.World;

public class EntityWhiteBishop extends EntityBishop 
{

	public EntityWhiteBishop(World par1World) 
	{
		super(par1World);
		this.texture = "/mods/Minestuck/textures/mobs/ProspitianBishop.png";
	}
	@Override
	public void setEnemies() 
	{
		enemyClasses.add(EntityBlackPawn.class);
		enemyClasses.add(EntityBlackBishop.class);
		enemyClasses.add(EntityGhast.class);
		super.setEnemies();
	}
	@Override
	public void setAllies() 
	{
		allyClasses.add(EntityWhitePawn.class);
		allyClasses.add(EntityWhiteBishop.class);
	}

}
