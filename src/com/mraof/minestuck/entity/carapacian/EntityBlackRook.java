package com.mraof.minestuck.entity.carapacian;

import net.minecraft.world.World;

public class EntityBlackRook extends EntityRook
{
	public EntityBlackRook(World world) 
	{
		super(world);
	}

	@Override
	public String getTexture() 
	{
		return "textures/mobs/dersite_rook.png";
	}

	@Override
	public EnumEntityKingdom getKingdom()
	{
		return EnumEntityKingdom.DERSITE;
	}
}
