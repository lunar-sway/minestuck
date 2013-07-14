package com.mraof.minestuck.entity.carapacian;

import net.minecraft.world.World;

public class EntityBlackBishop extends EntityBishop 
{

	public EntityBlackBishop(World par1World)
	{
		super(par1World);
	}
	@Override
	public void setEnemies() 
	{
		setEnemies(EnumEntityKingdom.DERSITE);
		super.setEnemies();
	}
	@Override
	public void setAllies() 
	{
		setAllies(EnumEntityKingdom.DERSITE);
	}
	@Override
	public String getTexture() 
	{
		return "Minestuck:/textures/mobs/DersiteBishop.png";
	}

}
