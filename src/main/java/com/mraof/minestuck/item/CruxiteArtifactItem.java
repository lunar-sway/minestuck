package com.mraof.minestuck.item;

import com.mraof.minestuck.entry.EntryProcess;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;

public abstract class CruxiteArtifactItem extends Item implements AlchemizedColored
{
	public CruxiteArtifactItem(Properties properties)
	{
		super(properties);
	}
	
	public void onArtifactActivated(ServerPlayerEntity player)
	{
		EntryProcess process = new EntryProcess();
		process.onArtifactActivated(player);
	}
}