package com.mraof.minestuck.computer;

import com.mraof.minestuck.skaianet.SburbConnections;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;

import java.util.OptionalInt;

public final class SburbServerData
{
	private final Runnable markDirty;
	
	private boolean isOpen;
	private int connectedClient = -1;
	
	public SburbServerData(Runnable markDirty)
	{
		this.markDirty = markDirty;
	}
	
	public void read(CompoundTag tag)
	{
		this.isOpen = tag.getBoolean("isOpen");
		if(tag.contains("connectedClient", Tag.TAG_INT))
			this.connectedClient = tag.getInt("connectedClient");
	}
	
	public CompoundTag write()
	{
		CompoundTag tag = new CompoundTag();
		tag.putBoolean("isOpen", isOpen);
		return tag;
	}
	
	public CompoundTag writeForUpdatePacket(ISburbComputer computer, MinecraftServer mcServer)
	{
		CompoundTag tag = this.write();
		SburbConnections.get(mcServer).getServerConnection(computer).ifPresent(c ->
				tag.putInt("connectedClient", c.client().getId()));
		return tag;
	}
	
	public boolean isOpen()
	{
		return this.isOpen;
	}
	
	public void setIsOpen(boolean isOpen)
	{
		this.isOpen = isOpen;
		this.markDirty.run();
	}
	
	public OptionalInt getConnectedClientId()
	{
		if(this.connectedClient != -1)
			return OptionalInt.of(this.connectedClient);
		else
			return OptionalInt.empty();
	}
	
	public void setIsConnected()
	{
		this.markDirty.run();
	}
}
