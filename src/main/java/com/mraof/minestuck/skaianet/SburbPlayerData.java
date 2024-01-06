package com.mraof.minestuck.skaianet;

import com.mraof.minestuck.api.alchemy.GristType;
import com.mraof.minestuck.computer.editmode.DeployEntry;
import com.mraof.minestuck.computer.editmode.EditData;
import com.mraof.minestuck.computer.editmode.ServerEditHandler;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.network.computer.SkaianetInfoPacket;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerIdentifier;
import com.mraof.minestuck.util.MSNBTUtil;
import net.minecraft.nbt.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public final class SburbPlayerData
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	private final PlayerIdentifier playerId;
	private final MinecraftServer mcServer;
	private boolean hasEntered = false;    //If the player has entered. Is set to true after entry has finished
	@Nullable
	private ResourceKey<Level> clientLandKey;    //The land info for this client player. This is initialized in preparation for entry
	int artifactType;
	private GristType baseGrist;
	
	private final Set<String> givenItemList = new HashSet<>();
	
	//Only used by the edit handler
	private ListTag inventory;
	
	SburbPlayerData(PlayerIdentifier playerId, MinecraftServer mcServer)
	{
		this.playerId = playerId;
		this.mcServer = mcServer;
	}
	
	void read(CompoundTag tag)
	{
		if(tag.contains("Inventory", Tag.TAG_LIST))
			this.inventory = tag.getList("Inventory", Tag.TAG_COMPOUND);
		
		ListTag list = tag.getList("GivenItems", Tag.TAG_STRING);
		for(int i = 0; i < list.size(); i++)
		{
			this.givenItemList.add(list.getString(i));
		}
		
		if(tag.contains("ClientLand"))
		{
			this.clientLandKey = Level.RESOURCE_KEY_CODEC.parse(NbtOps.INSTANCE, tag.get("ClientLand")).resultOrPartial(LOGGER::error).orElse(null);
			this.hasEntered = tag.contains("has_entered") ? tag.getBoolean("has_entered") : true;
		}
		
		this.artifactType = tag.getInt("artifact");
		this.baseGrist = MSNBTUtil.readGristType(tag, "base_grist", () -> SburbHandler.generateGristType(new Random()));
	}
	
	void write(CompoundTag tag)
	{
		if(this.inventory != null)
			tag.put("Inventory", this.inventory);
		
		ListTag list = new ListTag();
		for(String name : this.givenItemList)
			list.add(StringTag.valueOf(name));
		
		tag.put("GivenItems", list);
		if(this.clientLandKey != null)
		{
			Level.RESOURCE_KEY_CODEC.encodeStart(NbtOps.INSTANCE, this.clientLandKey).resultOrPartial(LOGGER::error)
					.ifPresent(keyTag -> tag.put("ClientLand", keyTag));
			tag.putBoolean("has_entered", this.hasEntered);
		}
		
		tag.putInt("artifact", this.artifactType);
		MSNBTUtil.writeGristType(tag, "base_grist", this.baseGrist);
	}
	
	public PlayerIdentifier getPlayerId()
	{
		return playerId;
	}
	
	public boolean hasEntered()
	{
		return hasEntered;
	}
	
	@Nullable
	public ResourceKey<Level> getLandDimensionIfEntered()
	{
		return this.hasEntered() ? this.clientLandKey : null;
	}
	
	/**
	 * @return The land dimension assigned to the client player.
	 */
	@Nullable
	public ResourceKey<Level> getClientDimension()
	{
		return this.clientLandKey;
	}
	
	void setLand(ResourceKey<Level> dimension)
	{
		if(clientLandKey != null)
			throw new IllegalStateException("Can't set land twice");
		else
		{
			clientLandKey = dimension;
		}
	}
	
	void resetEntryState()
	{
		hasEntered = false;
		clientLandKey = null;
		resendEntryState();
	}
	
	void setHasEntered()
	{
		if(clientLandKey == null)
			throw new IllegalStateException("Land has not been initiated, can't have entered now!");
		if(hasEntered)
			throw new IllegalStateException("Can't have entered twice");
		hasEntered = true;
		resendEntryState();
	}
	
	private void resendEntryState()
	{
		ServerPlayer player = this.playerId.getPlayer(this.mcServer);
		if(player != null)
			MSPacketHandler.sendToPlayer(new SkaianetInfoPacket.HasEntered(this.hasEntered), player);
	}
	
	public boolean hasGivenItem(DeployEntry item)
	{
		return givenItemList.contains(item.getName());
	}
	
	public void setHasGivenItem(DeployEntry item)
	{
		if(givenItemList.add(item.getName()))
			resendGivenItems();
	}
	
	void resetGivenItems()
	{
		if(!givenItemList.isEmpty())
		{
			givenItemList.clear();
			resendGivenItems();
		}
	}
	
	private void resendGivenItems()
	{
		EditData data = ServerEditHandler.getData(this.mcServer, this.playerId);
		if(data != null)
			data.sendGivenItemsToEditor();
	}
	
	public ListTag getEditmodeInventory()
	{
		return inventory == null ? null : inventory.copy();
	}
	
	public void putEditmodeInventory(ListTag nbt)
	{
		inventory = nbt;
	}
	
	public GristType getBaseGrist()
	{
		return baseGrist;
	}
	
	void setBaseGrist(GristType type)
	{
		if(baseGrist != null)
			throw new IllegalStateException("base grist type has already been set!");
		baseGrist = type;
	}
	
	public static SburbPlayerData get(ServerPlayer player)
	{
		return get(IdentifierHandler.encode(player), player.server);
	}
	
	public static SburbPlayerData get(PlayerIdentifier player, MinecraftServer mcServer)
	{
		return SkaianetHandler.get(mcServer).getOrCreateData(player);
	}
}
