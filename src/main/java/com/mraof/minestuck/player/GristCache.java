package com.mraof.minestuck.player;

import com.mraof.minestuck.alchemy.GristSet;
import com.mraof.minestuck.alchemy.GristTypes;
import com.mraof.minestuck.alchemy.ImmutableGristSet;
import com.mraof.minestuck.alchemy.NonNegativeGristSet;
import com.mraof.minestuck.computer.editmode.EditData;
import com.mraof.minestuck.computer.editmode.ServerEditHandler;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.network.data.GristCachePacket;
import com.mraof.minestuck.skaianet.SburbConnection;
import com.mraof.minestuck.skaianet.SessionHandler;
import com.mraof.minestuck.skaianet.SkaianetHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

import java.util.Objects;

public final class GristCache
{
	private final PlayerData data;
	private final MinecraftServer mcServer;
	private ImmutableGristSet gristSet;
	
	GristCache(PlayerData data, MinecraftServer mcServer)
	{
		this.data = data;
		this.mcServer = mcServer;
		this.gristSet = new ImmutableGristSet(GristTypes.BUILD, 20);
	}
	
	void read(CompoundTag nbt)
	{
		gristSet = NonNegativeGristSet.read(nbt.getList("grist_cache", Tag.TAG_COMPOUND)).asImmutable();
	}
	
	void write(CompoundTag nbt)
	{
		nbt.put("grist_cache", gristSet.write(new ListTag()));
	}
	
	public ImmutableGristSet getGristSet()
	{
		return this.gristSet;
	}
	
	public void addWithGutter(GristSet set)
	{
		GristSet overflowedGrist = this.addWithinCapacity(set);
		
		if(!overflowedGrist.isEmpty())
		{
			SessionHandler.get(mcServer).getPlayerSession(data.identifier)
					.getGristGutter().addGristFrom(overflowedGrist);
			
			ServerPlayer player = data.getPlayer();
			if(player != null && !overflowedGrist.isEmpty())
			{
				int gusherCount = player.getRandom().nextInt(6) > 0 ? 1 : 2;
				overflowedGrist.spawnGristEntities(player.getLevel(), player.getX(), player.getY(), player.getZ(), player.getRandom(),
						entity -> entity.setDeltaMovement(entity.getDeltaMovement().multiply(1.5, 0.5, 1.5)), 90, gusherCount);
			}
		}
	}
	
	public GristSet addWithinCapacity(GristSet set)
	{
		Objects.requireNonNull(set);
		
		NonNegativeGristSet newCache = new NonNegativeGristSet(this.getGristSet());
		
		GristSet excessGrist = newCache.addWithinCapacity(set, data.getEcheladder().getGristCapacity());
		
		this.set(newCache);
		return excessGrist;
	}
	
	public void set(NonNegativeGristSet cache)
	{
		gristSet = cache.asImmutable();
		data.markDirty();
		data.gristCache.sendPacket(data.getPlayer());
	}
	
	void sendPacket(ServerPlayer player)
	{
		GristSet gristSet = this.getGristSet();
		
		//Send to the player
		if(player != null)
		{
			GristCachePacket packet = new GristCachePacket(gristSet, false);
			MSPacketHandler.sendToPlayer(packet, player);
		}
		
		//Also send to the editing player, if there is any
		SburbConnection c = SkaianetHandler.get(mcServer).getActiveConnection(data.identifier);
		if(c != null)
		{
			EditData data = ServerEditHandler.getData(mcServer, c);
			if(data != null)
			{
				data.sendGristCacheToEditor();
			}
		}
	}
}
