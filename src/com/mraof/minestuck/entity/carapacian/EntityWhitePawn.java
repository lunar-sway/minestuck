package com.mraof.minestuck.entity.carapacian;

import com.mraof.minestuck.entity.ModEntityTypes;
import net.minecraft.world.World;

public class EntityWhitePawn extends EntityPawn
{
	public EntityWhitePawn(World world)
	{
		super(ModEntityTypes.PROSPITIAN_PAWN, world);
	}
	public EntityWhitePawn(World world, int type)
	{
		super(ModEntityTypes.PROSPITIAN_PAWN, world, type);
	}
	@Override
	public String getTexture() 
	{
		return "textures/mobs/prospitian_pawn.png";
	}

	@Override
	public EnumEntityKingdom getKingdom()
	{
		return EnumEntityKingdom.PROSPITIAN;
	}
}
