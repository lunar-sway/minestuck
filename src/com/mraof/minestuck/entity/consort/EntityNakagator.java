package com.mraof.minestuck.entity.consort;

import net.minecraft.world.World;

public class EntityNakagator extends EntityConsort
{
	public EntityNakagator(World world)
	{
		super(world);
	}
	
	@Override
	public String getTexture()
	{
		return "textures/mobs/nakagator.png";
	}
}