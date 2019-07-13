package com.mraof.minestuck.entity.carapacian;

import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

public class BlackBishopEntity extends BishopEntity
{
	
	public BlackBishopEntity(EntityType<? extends BlackBishopEntity> type, World world)
	{
		super(type, world);
	}
	
	@Override
	public String getTexture() 
	{
		return "textures/entity/dersite_bishop.png";
	}
	
	@Override
	public EnumEntityKingdom getKingdom()
	{
		return EnumEntityKingdom.DERSITE;
	}
}
