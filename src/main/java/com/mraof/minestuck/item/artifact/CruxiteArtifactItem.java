package com.mraof.minestuck.item.artifact;

import com.mraof.minestuck.entry.EntryProcess;
import com.mraof.minestuck.item.AlchemizedColored;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;

public abstract class CruxiteArtifactItem extends Item implements AlchemizedColored
{
	public CruxiteArtifactItem(Properties properties)
	{
		super(properties);
	}
	
	public void onArtifactActivated(ServerPlayer player)
	{
		EntryProcess.enter(player);
	}
}