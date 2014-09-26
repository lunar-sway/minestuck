package com.mraof.minestuck.entity.carapacian;

import net.minecraft.world.World;

public class EntityBlackBishop extends EntityBishop 
{

	public EntityBlackBishop(World par1World)
	{
		super(par1World);
	}

	@Override
	public String getTexture() 
	{
		return "textures/mobs/DersiteBishop.png";
	}

	@Override
	public EnumEntityKingdom getKingdom()
	{
		return EnumEntityKingdom.DERSITE;
	}
}
