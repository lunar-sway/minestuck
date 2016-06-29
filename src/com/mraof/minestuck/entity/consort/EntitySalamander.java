package com.mraof.minestuck.entity.consort;

import net.minecraft.world.World;

public class EntitySalamander extends EntityConsort
{
	public EntitySalamander(World world) 
	{
		super(world);
	}

	@Override
	public String getTexture() 
	{
		return "textures/mobs/salamander.png";
	}
}