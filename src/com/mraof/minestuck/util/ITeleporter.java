package com.mraof.minestuck.util;

import net.minecraft.entity.Entity;
import net.minecraft.world.WorldServer;

public interface ITeleporter 
{

	void makeDestination(Entity entity, WorldServer worldserver, WorldServer worldserver1);
	
}
