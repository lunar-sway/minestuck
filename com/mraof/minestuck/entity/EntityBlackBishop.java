package com.mraof.minestuck.entity;

import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.world.World;

public class EntityBlackBishop extends EntityBishop 
{

	public EntityBlackBishop(World par1World)
	{
		super(par1World);
		this.texture = "/mods/Minestuck/textures/mobs/DersiteBishop.png";
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
