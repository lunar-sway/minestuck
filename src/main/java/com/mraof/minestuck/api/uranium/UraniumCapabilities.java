package com.mraof.minestuck.api.uranium;

import com.mraof.minestuck.Minestuck;

import net.minecraft.core.Direction;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.EntityCapability;
import net.neoforged.neoforge.capabilities.ItemCapability;

public final class UraniumCapabilities
{
	public static BlockCapability<IUraniumHandler, Direction> BLOCK = BlockCapability.createSided(Minestuck.id("uranium"), IUraniumHandler.class);
	public static EntityCapability<IUraniumHandler, Direction> ENTITY = EntityCapability.createSided(Minestuck.id("uranium"), IUraniumHandler.class);
	public static ItemCapability<IUraniumHandler, Void> ITEM = ItemCapability.createVoid(Minestuck.id("uranium"), IUraniumHandler.class);
}
