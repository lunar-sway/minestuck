package com.mraof.minestuck.computer;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

import javax.annotation.Nullable;
import java.util.Optional;

public final class SburbClientData implements ProgramType.Data
{
	private final Runnable markDirty;
	
	@Nullable
	private String eventMessage = null;
	private boolean isResuming = false;
	private boolean isConnectedToServer = false;
	
	public SburbClientData(Runnable markDirty)
	{
		this.markDirty = markDirty;
	}
	
	@Override
	public void read(CompoundTag tag)
	{
		if(tag.contains("message", Tag.TAG_STRING))
			this.eventMessage = tag.getString("message");
		else this.eventMessage = null;
		this.isResuming = tag.getBoolean("isResuming");
		this.isConnectedToServer = tag.getBoolean("connectedToServer");
	}
	
	@Override
	public CompoundTag write()
	{
		CompoundTag tag = new CompoundTag();
		if(this.eventMessage != null)
			tag.putString("message", this.eventMessage);
		tag.putBoolean("isResuming", this.isResuming);
		tag.putBoolean("connectedToServer", this.isConnectedToServer);
		return tag;
	}
	
	public Optional<String> getEventMessage()
	{
		return Optional.ofNullable(this.eventMessage);
	}
	
	public void setEventMessage(String message)
	{
		this.eventMessage = message;
		this.markDirty.run();
	}
	
	public void clearEventMessage()
	{
		this.eventMessage = null;
		this.markDirty.run();
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
	
	public void handleBeingDuplicated()
	{
		this.eventMessage = null;
		this.isResuming = false;
		this.isConnectedToServer = false;
		this.markDirty.run();
	}
}
