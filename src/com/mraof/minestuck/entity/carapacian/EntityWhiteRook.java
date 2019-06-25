package com.mraof.minestuck.entity.carapacian;

import com.mraof.minestuck.entity.ModEntityTypes;
import net.minecraft.world.World;

public class EntityWhiteRook extends EntityRook
{
	public EntityWhiteRook(World world) 
	{
		super(ModEntityTypes.PROSPITIAN_ROOK, world);
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
