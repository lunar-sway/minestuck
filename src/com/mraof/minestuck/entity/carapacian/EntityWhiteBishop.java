package com.mraof.minestuck.entity.carapacian;

import com.mraof.minestuck.entity.ModEntityTypes;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.world.World;

public class EntityWhiteBishop extends EntityBishop 
{

	public EntityWhiteBishop(World par1World) 
	{
		super(ModEntityTypes.PROSPITIAN_BISHOP, par1World);
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
		return "textures/entity/prospitian_bishop.png";
	}

	@Override
	public EnumEntityKingdom getKingdom()
	{
		return EnumEntityKingdom.PROSPITIAN;
	}
}
