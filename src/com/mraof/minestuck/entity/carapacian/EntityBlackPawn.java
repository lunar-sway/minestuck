package com.mraof.minestuck.entity.carapacian;

import com.mraof.minestuck.entity.ModEntityTypes;
import net.minecraft.world.World;

public class EntityBlackPawn extends EntityPawn
{
	public EntityBlackPawn(World world) 
	{
		super(ModEntityTypes.DERSITE_PAWN, world);
	}
	public EntityBlackPawn(World world, int type) 
	{
		super(ModEntityTypes.DERSITE_PAWN, world, type);
	}

	@Override
	public String getTexture() 
	{
		return "textures/mobs/dersite_pawn.png";
	}

	@Override
	public EnumEntityKingdom getKingdom()
	{
		return EnumEntityKingdom.DERSITE;
	}
}
