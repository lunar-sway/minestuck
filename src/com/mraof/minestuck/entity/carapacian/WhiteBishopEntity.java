package com.mraof.minestuck.entity.carapacian;

import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

public class WhiteBishopEntity extends BishopEntity
{

	public WhiteBishopEntity(EntityType<? extends WhiteBishopEntity> type, World world)
	{
		super(type, world);
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
