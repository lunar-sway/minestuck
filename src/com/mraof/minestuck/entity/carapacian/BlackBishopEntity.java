package com.mraof.minestuck.entity.carapacian;

import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

public class BlackBishopEntity extends BishopEntity	//TODO This and similar classes are likely not needed anymore. Let's just pass EnumEntityKingdom as a constructor parameter
{
	
	public BlackBishopEntity(EntityType<? extends BlackBishopEntity> type, World world)
	{
		super(type, world);
	}
	
	@Override
	public EnumEntityKingdom getKingdom()
	{
		return EnumEntityKingdom.DERSITE;
	}
}
