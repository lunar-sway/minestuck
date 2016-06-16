package com.mraof.minestuck.entity.carapacian;

import net.minecraft.world.World;

public class EntityWhiteRook extends EntityRook
{
	public EntityWhiteRook(World world) 
	{
		super(world);
	}

	@Override
	public String getTexture() 
	{
		return "textures/mobs/prospitian_rook.png";
	}

	@Override
	public EnumEntityKingdom getKingdom()
	{
		return EnumEntityKingdom.PROSPITIAN;
	}

}
