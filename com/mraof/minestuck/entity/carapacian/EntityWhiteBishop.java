package com.mraof.minestuck.entity.carapacian;

import net.minecraft.entity.monster.EntityGhast;
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
		setEnemies(EnumEntityKingdom.PROSPITIAN);
		enemyClasses.add(EntityGhast.class);
		super.setEnemies();
	}
	@Override
	public void setAllies() 
	{
		setAllies(EnumEntityKingdom.PROSPITIAN);
	}

}
