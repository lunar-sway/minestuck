package com.mraof.minestuck.entity;

import com.mraof.minestuck.entity.consort.EntityConsort;
import com.mraof.minestuck.entity.consort.EnumConsort;

import net.minecraft.world.World;

public class EntityFrog extends EntityConsort
{
	
	public EntityFrog(World world)
	{
		super(world);
	}
	
	@Override
	public String getTexture()
	{
		return "textures/mobs/frog.png";
	}
	
	@Override
	public EnumConsort getConsortType()
	{
		return EnumConsort.IGUANA;
	}
}