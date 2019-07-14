package com.mraof.minestuck.entity.carapacian;

import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

public class BlackPawnEntity extends PawnEntity
{
	public BlackPawnEntity(EntityType<? extends BlackPawnEntity> type, World world)
	{
		super(type, world);
	}

	@Override
	public String getTexture() 
	{
		return "textures/entity/dersite_pawn.png";
	}

	@Override
	public EnumEntityKingdom getKingdom()
	{
		return EnumEntityKingdom.DERSITE;
	}
}
