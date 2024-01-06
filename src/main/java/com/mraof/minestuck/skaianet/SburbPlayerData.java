package com.mraof.minestuck.skaianet;

import com.mraof.minestuck.api.alchemy.GristType;
import com.mraof.minestuck.computer.editmode.DeployEntry;
import com.mraof.minestuck.computer.editmode.EditData;
import com.mraof.minestuck.computer.editmode.ServerEditHandler;
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
import java.util.Optional;
import java.util.Random;
import java.util.Set;

public final class SburbPlayerData
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	@Deprecated
	private final SburbConnection connection;
	private boolean hasEntered = false;    //If the player has entered. Is set to true after entry has finished
	@Nullable
	private ResourceKey<Level> clientLandKey;    //The land info for this client player. This is initialized in preparation for entry
	int artifactType;
	private GristType baseGrist;
	
	private final Set<String> givenItemList = new HashSet<>();
	
	//Only used by the edit handler
	private ListTag inventory;
	
	SburbPlayerData(SburbConnection connection)
	{
		this.connection = connection;
	}
	
	void read(CompoundTag tag, boolean isMain)
	{
		if(tag.contains("Inventory", Tag.TAG_LIST))
			this.inventory = tag.getList("Inventory", Tag.TAG_COMPOUND);
		
		if(isMain)
		{
			ListTag list = tag.getList("GivenItems", Tag.TAG_STRING);
			for(int i = 0; i < list.size(); i++)
			{
				this.givenItemList.add(list.getString(i));
			}
		}
		
		
		if(tag.contains("ClientLand"))
		{
			this.clientLandKey = Level.RESOURCE_KEY_CODEC.parse(NbtOps.INSTANCE, tag.get("ClientLand")).resultOrPartial(LOGGER::error).orElse(null);
			this.hasEntered = tag.contains("has_entered") ? tag.getBoolean("has_entered") : true;
		}
		
		this.artifactType = tag.getInt("artifact");
		this.baseGrist = MSNBTUtil.readGristType(tag, "base_grist", () -> SburbHandler.generateGristType(new Random()));
	}
	
	void write(CompoundTag tag, boolean isMain)
	{
		if(this.inventory != null)
			tag.put("Inventory", this.inventory);
		
		if(isMain)
		{
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
		}
		
		tag.putInt("artifact", this.artifactType);
		MSNBTUtil.writeGristType(tag, "base_grist", this.baseGrist);
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
		connection.skaianet.infoTracker.markDirty(connection);
	}
	
	void setHasEntered()
	{
		if(clientLandKey == null)
			throw new IllegalStateException("Land has not been initiated, can't have entered now!");
		if(hasEntered)
			throw new IllegalStateException("Can't have entered twice");
		hasEntered = true;
		connection.skaianet.infoTracker.markDirty(connection);
	}
	
	public boolean hasGivenItem(DeployEntry item)
	{
		return givenItemList.contains(item.getName());
	}
	
	public void setHasGivenItem(DeployEntry item)
	{
		if(givenItemList.add(item.getName()))
		{
			EditData data = ServerEditHandler.getData(connection.skaianet.mcServer, connection);
			if(data != null)
				data.sendGivenItemsToEditor();
		}
	}
	
	void resetGivenItems()
	{
		if(!givenItemList.isEmpty())
		{
			givenItemList.clear();
			EditData data = ServerEditHandler.getData(connection.skaianet.mcServer, connection);
			if(data != null)
				data.sendGivenItemsToEditor();
		}
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
	
	void copyFrom(SburbPlayerData other)
	{
		clientLandKey = other.clientLandKey;
		hasEntered = other.hasEntered;
		artifactType = other.artifactType;
		baseGrist = other.baseGrist;
		if(other.inventory != null)
			inventory = other.inventory.copy();
	}
	
	public static Optional<SburbPlayerData> get(PlayerIdentifier player, MinecraftServer mcServer)
	{
		return SkaianetHandler.get(mcServer).getPrimaryConnection(player, true)
				.map(connection -> connection.playerData);
	}
	
	public static boolean hasEntered(ServerPlayer player)
	{
		return get(IdentifierHandler.encode(player), player.server).map(SburbPlayerData::hasEntered).orElse(false);
	}
}
