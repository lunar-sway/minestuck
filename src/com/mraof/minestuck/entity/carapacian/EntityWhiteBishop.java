package com.mraof.minestuck.entity.carapacian;

import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.world.World;

public class EntityWhiteBishop extends EntityBishop 
{

	public EntityWhiteBishop(World par1World) 
	{
		super(par1World);
	}
	@Override
	public void setEnemies() 
	{
		super.setEnemies();
		enemyClasses.add(EntityGhast.class);
	}
	@Override
	public String getTexture() 
	{
		return "textures/mobs/prospitian_bishop.png";
	}

	@Override
	public EnumEntityKingdom getKingdom()
	{
		return EnumEntityKingdom.PROSPITIAN;
	}
}
