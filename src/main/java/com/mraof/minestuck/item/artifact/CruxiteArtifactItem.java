package com.mraof.minestuck.item.artifact;

import com.mraof.minestuck.entry.EntryProcess;
import com.mraof.minestuck.item.AlchemizedColored;
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