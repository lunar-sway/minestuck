package com.mraof.minestuck.event;

import com.mraof.minestuck.player.PlayerIdentifier;
import net.minecraft.server.MinecraftServer;
import net.neoforged.bus.api.Event;

public final class OnEntryEvent extends Event
{
	private final MinecraftServer mcServer;
	private final PlayerIdentifier player;
	
	public OnEntryEvent(MinecraftServer mcServer, PlayerIdentifier player)
	{
		this.mcServer = mcServer;
		this.player = player;
	}
	
	public MinecraftServer getMcServer()
	{
		return mcServer;
	}
	
	public PlayerIdentifier getPlayer()
	{
		return player;
	}
}
