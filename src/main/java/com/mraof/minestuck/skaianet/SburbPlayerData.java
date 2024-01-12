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
import java.util.HashSet;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.function.Supplier;

public final class SburbPlayerData
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	private final PlayerIdentifier playerId;
	private final MinecraftServer mcServer;
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
	
	public Optional<PlayerIdentifier> primaryServerPlayer()
	{
		return SkaianetHandler.get(this.mcServer).primaryPartnerForClient(playerId());
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
