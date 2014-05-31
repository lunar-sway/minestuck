package com.mraof.minestuck.entity.carapacian;

import net.minecraft.world.World;

public class EntityBlackPawn extends EntityPawn
{
	public EntityBlackPawn(World world) 
	{
		super(world);
	}
	public EntityBlackPawn(World world, int type) 
	{
		super(world, type);
	}

	@Override
	public String getTexture() 
	{
		return "textures/mobs/DersitePawn.png";
	}

	@Override
	public EnumEntityKingdom getKingdom()
	{
		return EnumEntityKingdom.DERSITE;
	}
}
