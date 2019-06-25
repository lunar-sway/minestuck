package com.mraof.minestuck.entity.carapacian;

import com.mraof.minestuck.entity.ModEntityTypes;
import net.minecraft.world.World;

public class EntityBlackBishop extends EntityBishop 
{

	public EntityBlackBishop(World par1World)
	{
		super(ModEntityTypes.DERSITE_BISHOP, par1World);
	}

	@Override
	public String getTexture() 
	{
		return "textures/mobs/dersite_bishop.png";
	}

	@Override
	public EnumEntityKingdom getKingdom()
	{
		return EnumEntityKingdom.DERSITE;
	}
}
