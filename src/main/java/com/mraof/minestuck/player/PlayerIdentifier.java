package com.mraof.minestuck.player;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.server.MinecraftServer;

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
	
	public abstract boolean appliesTo(PlayerEntity player);
	
	public abstract String getUsername();
	
	public abstract String getCommandString();
	
	public abstract ServerPlayerEntity getPlayer(MinecraftServer server);
	
	public abstract CompoundNBT saveToNBT(CompoundNBT nbt, String key);
}