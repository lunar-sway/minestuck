package com.mraof.minestuck.player;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.inventory.captchalogue.*;
import com.mraof.minestuck.network.data.BoondollarDataPacket;
import com.mraof.minestuck.network.data.ModusDataPacket;
import com.mraof.minestuck.util.MSCapabilities;
import com.mraof.minestuck.world.MSDimensions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.LogicalSide;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.attachment.AttachmentHolder;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Stores and sends any data connected to a specific player.
 * This class is for server-side use only.
 * @author kirderf1
 */
@ParametersAreNonnullByDefault
@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class PlayerData extends AttachmentHolder
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	@SubscribeEvent
	public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event)
	{
		ServerPlayer player = (ServerPlayer) event.getEntity();
		PlayerSavedData.getData(player).onPlayerLoggedIn(player);
		MSDimensions.sendDimensionData(player);
	}
	
	@Nonnull
	final PlayerIdentifier identifier;
	
	private final MinecraftServer mcServer;
	
	private boolean givenModus;
	private Modus modus;
	
	private final Map<ResourceLocation, Integer> consortReputation = new HashMap<>();
	
	PlayerData(MinecraftServer mcServer, PlayerIdentifier player)
	{
		this.mcServer = mcServer;
		this.identifier = player;
	}
	
	PlayerData(MinecraftServer mcServer, CompoundTag nbt)
	{
		this.mcServer = mcServer;
		this.identifier = IdentifierHandler.loadOrThrow(nbt, "player");
		
		if(nbt.contains(ATTACHMENTS_NBT_KEY, Tag.TAG_COMPOUND))
			this.deserializeAttachments(nbt.getCompound(ATTACHMENTS_NBT_KEY));
		
		if (nbt.contains("modus"))
		{
			this.modus = CaptchaDeckHandler.readFromNBT(nbt.getCompound("modus"), LogicalSide.SERVER);
			givenModus = true;
		}
		else givenModus = nbt.getBoolean("given_modus");
		
		ListTag list = nbt.getList("consort_reputation", Tag.TAG_COMPOUND);
		for(int i = 0; i < list.size(); i++)
		{
			CompoundTag dimensionRep = list.getCompound(i);
			ResourceLocation dimension = ResourceLocation.tryParse(dimensionRep.getString("dim"));
			if(dimension != null)
				consortReputation.put(dimension, dimensionRep.getInt("rep"));
		}
	}
	
	CompoundTag writeToNBT()
	{
		CompoundTag nbt = new CompoundTag();
		identifier.saveToNBT(nbt, "player");
		
		CompoundTag attachments = this.serializeAttachments();
		if(attachments != null)
			nbt.put(ATTACHMENTS_NBT_KEY, attachments);
		
		if (this.modus != null)
			nbt.put("modus", CaptchaDeckHandler.writeToNBT(modus));
		else nbt.putBoolean("given_modus", givenModus);
		
		ListTag list = new ListTag();
		for(Map.Entry<ResourceLocation, Integer> entry : consortReputation.entrySet())
		{
			CompoundTag dimensionRep = new CompoundTag();
			dimensionRep.putString("dim", entry.getKey().toString());
			dimensionRep.putInt("rep", entry.getValue());
			list.add(dimensionRep);
		}
		nbt.put("consort_reputation", list);
		
		return nbt;
	}
	
	public Modus getModus()
	{
		return modus;
	}
	
	public void setModus(@Nullable Modus modus)
	{
		if(this.modus != modus)
		{
			this.modus = modus;
			if(modus != null)
				setGivenModus();
			ServerPlayer player = this.getPlayer();
			if(player != null)
				PacketDistributor.PLAYER.with(player).send(ModusDataPacket.create(modus));
		}
	}
	
	public boolean hasGivenModus()
	{
		return givenModus;
	}
	
	private void setGivenModus()
	{
		givenModus = true;
	}
	
	public long getBoondollars()
	{
		return this.getData(MSCapabilities.BOONDOLLARS);
	}
	
	public void addBoondollars(long amount)
	{
		if(amount < 0)
			throw new IllegalArgumentException("Boondollar amount may not be negative.");
		
		this.setBoondollars(this.getBoondollars() + amount);
	}
	
	public void takeBoondollars(long amount)
	{
		if(amount < 0)
			throw new IllegalArgumentException("Boondollar amount may not be negative.");
		
		this.setBoondollars(this.getBoondollars() - amount);
	}
	
	public boolean tryTakeBoondollars(long amount)
	{
		if(amount < 0)
			throw new IllegalArgumentException("Boondollar amount may not be negative.");
		
		long newAmount = this.getBoondollars() - amount;
		
		if(newAmount < 0)
			return false;
		
		this.setBoondollars(newAmount);
		return true;
	}
	
	public void setBoondollars(long amount)
	{
		if(amount < 0)
			throw new IllegalArgumentException("Boondollar amount may not be negative.");
		
		if(amount != this.getBoondollars())
		{
			this.setData(MSCapabilities.BOONDOLLARS, amount);
			sendBoondollars(getPlayer());
		}
	}
	
	public int getConsortReputation(ResourceKey<Level> dim)
	{
		return consortReputation.getOrDefault(dim.location(), 0);
	}
	
	public void addConsortReputation(int amount, ResourceKey<Level> dim)
	{
		int oldRep = getConsortReputation(dim);
		int newRep = Mth.clamp(oldRep + amount, -10000, 10000);
		
		if(newRep != oldRep)
			consortReputation.put(dim.location(), newRep);
	}
	
	private void tryGiveStartingModus(ServerPlayer player)
	{
		List<ModusType<?>> startingTypes = StartingModusManager.getStartingModusTypes();
		
		if(startingTypes.isEmpty())
			return;
		
		ModusType<?> type = startingTypes.get(player.level().random.nextInt(startingTypes.size()));
		if(type == null)
		{
			setGivenModus();
			return;
		}
		
		Modus modus = type.createServerSide();
		if(modus != null)
		{
			modus.initModus(null, player, null, MinestuckConfig.SERVER.initialModusSize.get());
			setModus(modus);
		} else LOGGER.warn("Couldn't create a starting modus type {}.", ModusTypes.REGISTRY.getKey(type));
	}
	
	public void onPlayerLoggedIn(ServerPlayer player)
	{
		Echeladder.get(this).updateEcheladderBonuses(player);
		
		if(getModus() != null)
			PacketDistributor.PLAYER.with(player).send(ModusDataPacket.create(getModus()));
		
		if(getModus() == null && !hasGivenModus())
			tryGiveStartingModus(player);
		
		Echeladder.get(this).sendInitialPacket(player);
		sendBoondollars(player);
	}
	
	private void sendBoondollars(ServerPlayer player)
	{
		if(player == null)
			return;
		BoondollarDataPacket packet = BoondollarDataPacket.create(getBoondollars());
		PacketDistributor.PLAYER.with(player).send(packet);
	}
	
	@Nullable
	ServerPlayer getPlayer()
	{
		return identifier.getPlayer(mcServer);
	}
	
	public MinecraftServer getMinecraftServer()
	{
		return this.mcServer;
	}
}