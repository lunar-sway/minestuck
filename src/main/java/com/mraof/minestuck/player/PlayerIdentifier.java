package com.mraof.minestuck.player;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;

public abstract class PlayerIdentifier
{
	private final int id;
	
	PlayerIdentifier(int id)
	{
		this.id = id;
	}
	
	/**
	 * @return Id to be used for client-server communication without having to keep the client updated with all needed identifiers
	 */
	public int getId()
	{
		return id;
	}
	
	public abstract boolean appliesTo(Player player);
	
	public abstract String getUsername();
	
	public abstract String getCommandString();
	
	@Nullable
	public abstract ServerPlayer getPlayer(MinecraftServer server);
	
	public abstract CompoundTag saveToNBT(CompoundTag nbt, String key);
}