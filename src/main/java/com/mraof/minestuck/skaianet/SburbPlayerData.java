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
import net.minecraft.server.level.ServerLevel;
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
	
	private final PlayerIdentifier playerId;
	private final MinecraftServer mcServer;
	private boolean hasEntered = false;    //If the player has entered. Is set to true after entry has finished
	@Nullable
	private ResourceKey<Level> landKey;    //The land info for this client player. This is initialized in preparation for entry
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
			this.landKey = Level.RESOURCE_KEY_CODEC.parse(NbtOps.INSTANCE, tag.get("ClientLand")).resultOrPartial(LOGGER::error).orElse(null);
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
		if(this.landKey != null)
		{
			Level.RESOURCE_KEY_CODEC.encodeStart(NbtOps.INSTANCE, this.landKey).resultOrPartial(LOGGER::error)
					.ifPresent(keyTag -> tag.put("ClientLand", keyTag));
			tag.putBoolean("has_entered", this.hasEntered);
		}
		
		tag.putInt("artifact", this.artifactType);
		MSNBTUtil.writeGristType(tag, "base_grist", this.baseGrist);
	}
	
	public PlayerIdentifier playerId()
	{
		return playerId;
	}
	
	public Optional<PlayerIdentifier> primaryServerPlayer(MinecraftServer mcServer)
	{
		return SkaianetHandler.get(mcServer).primaryConnectionForClient(this.playerId)
				.filter(SburbConnection::hasServerPlayer).map(SburbConnection::getServerIdentifier);
	}
	
	public boolean hasEntered()
	{
		return hasEntered;
	}
	
	@Nullable
	public ResourceKey<Level> getLandDimensionIfEntered()
	{
		return this.hasEntered() ? this.landKey : null;
	}
	
	/**
	 * @return The land dimension assigned to this player
	 */
	@Nullable
	public ResourceKey<Level> getLandDimension()
	{
		return this.landKey;
	}
	
	void setLand(ResourceKey<Level> dimension)
	{
		if(landKey != null)
			throw new IllegalStateException("Can't set land twice");
		else
		{
			landKey = dimension;
		}
	}
	
	void resetEntryState()
	{
		hasEntered = false;
		landKey = null;
		resendEntryState();
	}
	
	void setHasEntered()
	{
		if(landKey == null)
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
	
	public static Optional<SburbPlayerData> getForLand(ServerLevel level)
	{
		return getForLand(level.dimension(), level.getServer());
	}
	
	public static Optional<SburbPlayerData> getForLand(ResourceKey<Level> level, MinecraftServer mcServer)
	{
		return SkaianetHandler.get(mcServer).allPlayerData().stream().filter(data -> data.getLandDimension() == level).findAny();
	}
}
