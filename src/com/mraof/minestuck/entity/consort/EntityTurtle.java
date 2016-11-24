package com.mraof.minestuck.entity.consort;

import net.minecraft.world.World;

public class EntityTurtle extends EntityConsort
{
	public EntityTurtle(World world)
	{
		super(world);
	}
	
	@Override
	public String getTexture()
	{
		return "textures/mobs/turtle.png";
	}
}