package com.mraof.minestuck.skaianet;

import com.mraof.minestuck.api.alchemy.GristType;
import com.mraof.minestuck.computer.editmode.DeployEntry;
import com.mraof.minestuck.computer.editmode.EditData;
import com.mraof.minestuck.computer.editmode.ServerEditHandler;
import com.mraof.minestuck.item.MSItems;
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
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Supplier;

public final class SburbPlayerData
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	private final PlayerIdentifier playerId;
	private final MinecraftServer mcServer;
	
	private boolean hasPrimaryConnection;
	private PlayerIdentifier primaryServerPlayer = IdentifierHandler.NULL_IDENTIFIER;
	private boolean hasEntered = false;
	@Nullable
	private ResourceKey<Level> landKey;
	ArtifactType artifactType;
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
		this.hasPrimaryConnection = tag.getBoolean("IsMain");
		if(this.hasPrimaryConnection)
			this.primaryServerPlayer = IdentifierHandler.load(tag, "server");
		
		if(tag.contains("inventory", Tag.TAG_LIST))
			this.inventory = tag.getList("inventory", Tag.TAG_COMPOUND);
		
		ListTag list = tag.getList("given_items", Tag.TAG_STRING);
		for(int i = 0; i < list.size(); i++)
			this.givenItemList.add(list.getString(i));
		
		if(tag.contains("land"))
		{
			this.landKey = Level.RESOURCE_KEY_CODEC.parse(NbtOps.INSTANCE, tag.get("land"))
					.resultOrPartial(LOGGER::error).orElse(null);
			this.hasEntered = tag.getBoolean("has_entered");
		}
		
		this.artifactType = ArtifactType.fromInt(tag.getInt("artifact"));
		this.baseGrist = MSNBTUtil.readGristType(tag, "base_grist", () -> SburbHandler.generateGristType(new Random()));
	}
	
	void write(CompoundTag tag)
	{
		tag.putBoolean("IsMain", this.hasPrimaryConnection);
		if(this.hasPrimaryConnection)
			this.primaryServerPlayer.saveToNBT(tag, "server");
		
		if(this.inventory != null)
			tag.put("inventory", this.inventory);
		
		ListTag list = new ListTag();
		for(String name : this.givenItemList)
			list.add(StringTag.valueOf(name));
		
		tag.put("given_items", list);
		if(this.landKey != null)
		{
			Level.RESOURCE_KEY_CODEC.encodeStart(NbtOps.INSTANCE, this.landKey)
					.resultOrPartial(LOGGER::error).ifPresent(keyTag -> tag.put("land", keyTag));
			tag.putBoolean("has_entered", this.hasEntered);
		}
		
		tag.putInt("artifact", this.artifactType.ordinal());
		MSNBTUtil.writeGristType(tag, "base_grist", this.baseGrist);
	}
	
	public PlayerIdentifier playerId()
	{
		return playerId;
	}
	
	/**
	 * @return the server player that is locked in to this player. If this exists, {@link SburbPlayerData#hasPrimaryConnection()} is assumed to return true.
	 * Note that a primary server player might be missing even if {@link SburbPlayerData#hasPrimaryConnection()} returns true.
	 */
	public Optional<PlayerIdentifier> primaryServerPlayer()
	{
		return this.primaryServerPlayer != IdentifierHandler.NULL_IDENTIFIER ? Optional.of(this.primaryServerPlayer) : Optional.empty();
	}
	
	public boolean isPrimaryServerPlayer(PlayerIdentifier serverPlayer)
	{
		return this.primaryServerPlayer().map(serverPlayer::equals).orElse(false);
	}
	
	void removeServerPlayer()
	{
		SkaianetHandler skaianet = SkaianetHandler.get(this.mcServer);
		skaianet.getActiveConnection(this.playerId()).ifPresent(skaianet::closeConnection);
		if(this.primaryServerPlayer != IdentifierHandler.NULL_IDENTIFIER)
		{
			skaianet.infoTracker.markDirty(this.primaryServerPlayer);
			if(this.hasEntered())
				skaianet.infoTracker.markLandChainDirty();
			
			PlayerIdentifier oldServerPlayer = this.primaryServerPlayer;
			this.primaryServerPlayer = IdentifierHandler.NULL_IDENTIFIER;
			skaianet.sessionHandler.onDisconnect(this.playerId(), oldServerPlayer);
		}
	}
	
	void setNewServerPlayer(PlayerIdentifier server)
	{
		if(!this.hasPrimaryConnection())
			throw new IllegalStateException();
		if(this.primaryServerPlayer != IdentifierHandler.NULL_IDENTIFIER)
			throw new IllegalStateException("Connection already has a server player");
		SkaianetHandler skaianet = SkaianetHandler.get(this.mcServer);
		if(!skaianet.canMakeNewRegularConnectionAsServer(server))
			throw new IllegalStateException("Server player already has a connection");
		
		this.primaryServerPlayer = Objects.requireNonNull(server);
		skaianet.sessionHandler.onConnect(this.playerId(), server);
		skaianet.infoTracker.markDirty(server);
		if(this.hasEntered())
			skaianet.infoTracker.markLandChainDirty();
	}
	
	/**
	 * @return true if this player has started getting items for entry and should be locked in with their current server player.
	 * @see SburbPlayerData#primaryServerPlayer()
	 */
	public boolean hasPrimaryConnection()
	{
		return this.hasPrimaryConnection;
	}
	
	void setHasPrimaryConnection(PlayerIdentifier serverPlayer)
	{
		if(hasPrimaryConnection)
			return;
		
		hasPrimaryConnection = true;
		primaryServerPlayer = serverPlayer;
		SkaianetHandler skaianetHandler = SkaianetHandler.get(mcServer);
		skaianetHandler.sessionHandler.onConnect(this.playerId(), serverPlayer);
		skaianetHandler.infoTracker.markDirty(this.playerId());
		this.primaryServerPlayer().ifPresent(skaianetHandler.infoTracker::markDirty);
	}
	
	public boolean hasEntered()
	{
		return hasEntered;
	}
	
	/**
	 * @return The land dimension assigned to this player, or null if the player hasn't entered.
	 * @see SburbPlayerData#getLandDimension()
	 */
	@Nullable
	public ResourceKey<Level> getLandDimensionIfEntered()
	{
		return this.hasEntered() ? this.landKey : null;
	}
	
	/**
	 * @return The land dimension assigned to this player.
	 * It is non-null as long as a land dimension has been initiated, even if the player hasn't entered yet.
	 * @see SburbPlayerData#getLandDimensionIfEntered()
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
		landKey = dimension;
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
	
	public void resetGivenItems()
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
	
	enum ArtifactType {
		APPLE(MSItems.CRUXITE_APPLE),
		POTION(MSItems.CRUXITE_POTION);
		
		private final Supplier<Item> item;
		
		ArtifactType(Supplier<Item> item)
		{
			this.item = item;
		}
		
		ItemStack createItemStack()
		{
			return new ItemStack(this.item.get());
		}
		
		static ArtifactType fromInt(int ordinal)
		{
			return values()[Mth.clamp(ordinal, 0, values().length - 1)];
		}
	}
}
