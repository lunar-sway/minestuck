package com.mraof.minestuck.entity.consort;

import net.minecraft.world.World;

public class EntityIguana extends EntityConsort {

	public EntityIguana(World world) {
		super(world);
	}

	@Override
	public String getTexture() 
	{
		return "textures/mobs/iguana.png";
	}

}
