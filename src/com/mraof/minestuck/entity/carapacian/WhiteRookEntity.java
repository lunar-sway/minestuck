package com.mraof.minestuck.entity.carapacian;

import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

public class WhiteRookEntity extends RookEntity
{
	public WhiteRookEntity(EntityType<? extends WhiteRookEntity> type, World world)
	{
		super(type, world);
	}

	@Override
	public String getTexture() 
	{
		return "textures/entity/prospitian_rook.png";
	}

	@Override
	public EnumEntityKingdom getKingdom()
	{
		return EnumEntityKingdom.PROSPITIAN;
	}

}
