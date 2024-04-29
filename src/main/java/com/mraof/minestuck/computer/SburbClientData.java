package com.mraof.minestuck.computer;

import net.minecraft.nbt.CompoundTag;

public final class SburbClientData
{
	private final Runnable markDirty;
	
	private boolean isResuming = false;
	private boolean isConnectedToServer = false;
	
	public SburbClientData(Runnable markDirty)
	{
		this.markDirty = markDirty;
	}
	
	public void read(CompoundTag tag)
	{
		this.isResuming = tag.getBoolean("isResuming");
		this.isConnectedToServer = tag.getBoolean("connectedToServer");
	}
	
	public CompoundTag write()
	{
		CompoundTag tag = new CompoundTag();
		tag.putBoolean("isResuming", this.isResuming);
		tag.putBoolean("connectedToServer", this.isConnectedToServer);
		return tag;
	}
	
	public boolean isResuming()
	{
		return this.isResuming;
	}
	
	public void setIsResuming(boolean isResuming)
	{
		this.isResuming = isResuming;
		this.markDirty.run();
	}
	
	public boolean isConnectedToServer()
	{
		return this.isConnectedToServer;
	}
	
	public void setIsConnectedToServer(boolean isConnectedToServer)
	{
		this.isConnectedToServer = isConnectedToServer;
		this.markDirty.run();
	}
}
