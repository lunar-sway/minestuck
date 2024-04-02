package com.mraof.minestuck.player;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.computer.editmode.EditmodeLocations;
import com.mraof.minestuck.inventory.captchalogue.*;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.network.data.*;
import com.mraof.minestuck.skaianet.SburbHandler;
import com.mraof.minestuck.util.ColorHandler;
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
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Stores and sends any data connected to a specific player.
 * This class is for server-side use only.
 * @author kirderf1
 */
@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class PlayerData
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	@SubscribeEvent
	public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event)
	{
		ServerPlayer player = (ServerPlayer) event.getEntity();
		PlayerSavedData.getData(player).onPlayerLoggedIn(player);
		MSDimensions.sendDimensionData(player);
	}
	
	@SubscribeEvent
	public static void onPlayerChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event)
	{
		ServerPlayer player = (ServerPlayer) event.getEntity();
		PlayerSavedData.getData(player).sendConsortReputation(player);
	}
	
	@Nonnull
	final PlayerIdentifier identifier;
	
	private final MinecraftServer mcServer;
	private final Echeladder echeladder;
	private int color = ColorHandler.DEFAULT_COLOR;
	
	private boolean givenModus;
	private Modus modus;
	private long boondollars;
	final GristCache gristCache;
	private double gutterMultiplier = 1;
	
	private final Map<ResourceLocation, Integer> consortReputation = new HashMap<>();
	
	private Title title;
	private boolean effectToggle;
	public final EditmodeLocations editmodeLocations;
	
	private boolean hasLoggedIn;
	
	PlayerData(MinecraftServer mcServer, @Nonnull PlayerIdentifier player)
	{
		this.mcServer = mcServer;
		this.identifier = player;
		echeladder = new Echeladder(mcServer, player);
		gristCache = new GristCache(this, mcServer);
		editmodeLocations = new EditmodeLocations();
		hasLoggedIn = false;
	}
	
	PlayerData(MinecraftServer mcServer, CompoundTag nbt)
	{
		this.mcServer = mcServer;
		this.identifier = IdentifierHandler.loadOrThrow(nbt, "player");
		
		echeladder = new Echeladder(mcServer, identifier);
		echeladder.loadEcheladder(nbt);
		if (nbt.contains("color"))
			this.color = nbt.getInt("color");
		
		if (nbt.contains("modus"))
		{
			this.modus = CaptchaDeckHandler.readFromNBT(nbt.getCompound("modus"), LogicalSide.SERVER);
			givenModus = true;
		}
		else givenModus = nbt.getBoolean("given_modus");
		boondollars = nbt.getLong("boondollars");
		
		gristCache = new GristCache(this, mcServer);
		gristCache.read(nbt);
		
		ListTag list = nbt.getList("consort_reputation", Tag.TAG_COMPOUND);
		for(int i = 0; i < list.size(); i++)
		{
			CompoundTag dimensionRep = list.getCompound(i);
			ResourceLocation dimension = ResourceLocation.tryParse(dimensionRep.getString("dim"));
			if(dimension != null)
				consortReputation.put(dimension, dimensionRep.getInt("rep"));
		}
		
		title = Title.tryRead(nbt, "title");
		effectToggle = nbt.getBoolean("effect_toggle");
		editmodeLocations = EditmodeLocations.read(nbt.getCompound("editmode_locations"));
		
		hasLoggedIn = true;
	}
	
	CompoundTag writeToNBT()
	{
		CompoundTag nbt = new CompoundTag();
		identifier.saveToNBT(nbt, "player");
		echeladder.saveEcheladder(nbt);
		nbt.putInt("color", color);
		
		if (this.modus != null)
			nbt.put("modus", CaptchaDeckHandler.writeToNBT(modus));
		else nbt.putBoolean("given_modus", givenModus);
		nbt.putLong("boondollars", boondollars);
		gristCache.write(nbt);
		
		ListTag list = new ListTag();
		for(Map.Entry<ResourceLocation, Integer> entry : consortReputation.entrySet())
		{
			CompoundTag dimensionRep = new CompoundTag();
			dimensionRep.putString("dim", entry.getKey().toString());
			dimensionRep.putInt("rep", entry.getValue());
			list.add(dimensionRep);
		}
		nbt.put("consort_reputation", list);
		
		if(title != null)
			title.write(nbt, "title");
		nbt.putBoolean("effect_toggle", effectToggle);
		
		nbt.put("editmode_locations", editmodeLocations.write());
		
		return nbt;
	}
	
	public Echeladder getEcheladder()
	{
		return echeladder;
	}
	
	public int getColor()
	{
		return color;
	}
	
	public void trySetColor(int color)
	{
		if(SburbHandler.canSelectColor(identifier, mcServer) && this.color != color)
		{
			this.color = color;
			
			sendColor(getPlayer(), false);
		}
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
				MSPacketHandler.sendToPlayer(ModusDataPacket.create(modus), player);
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
	public double getGutterMultipler()
	{
		return gutterMultiplier;
	}
	public void addGutterMultiplier(double amount)
	{
		if(amount < 0)
			throw new IllegalArgumentException("Multiplier amount may not be negative.");
		gutterMultiplier += amount;
	}
	
	
	public long getBoondollars()
	{
		return boondollars;
	}
	
	public void addBoondollars(long amount)
	{
		if(amount < 0)
			throw new IllegalArgumentException("Boondollar amount may not be negative.");
		else if(amount > 0)
		{
			boondollars += amount;
			sendBoondollars(getPlayer());
		}
	}
	
	public void takeBoondollars(long amount)
	{
		if(amount < 0)
			throw new IllegalArgumentException("Boondollar amount may not be negative.");
		else if(amount > 0)
		{
			if(boondollars - amount < 0)
				throw new IllegalStateException("Can't go to negative boondollars");
			
			boondollars -= amount;
			sendBoondollars(getPlayer());
		}
	}
	
	public boolean tryTakeBoondollars(long amount)
	{
		if(getBoondollars() - amount < 0)
			return false;
		else
		{
			takeBoondollars(amount);
			return true;
		}
	}
	
	public void setBoondollars(long amount)
	{
		if(amount < 0)
			throw new IllegalArgumentException("Boondollar amount may not be negative.");
		else if(amount != boondollars)
		{
			boondollars = amount;
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
		{
			consortReputation.put(dim.location(), newRep);
			sendConsortReputation(getPlayer());
		}
	}
	
	public GristCache getGristCache()
	{
		return gristCache;
	}
	
	public Title getTitle()
	{
		return title;
	}
	
	public void setTitle(Title newTitle)
	{
		if(title == null)
		{
			title = Objects.requireNonNull(newTitle);
			sendTitle(getPlayer());
		} else throw new IllegalStateException("Can't set title for player "+ identifier.getUsername()+" because they already have one");
	}
	
	public boolean effectToggle()
	{
		return effectToggle;
	}
	
	public void effectToggle(boolean toggle)
	{
		effectToggle = toggle;
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
		} else LOGGER.warn("Couldn't create a starting modus type {}.", ModusTypes.REGISTRY.get().getKey(type));
	}
	
	public void onPlayerLoggedIn(ServerPlayer player)
	{
		getEcheladder().updateEcheladderBonuses(player);
		
		if(getModus() != null)
			MSPacketHandler.sendToPlayer(ModusDataPacket.create(getModus()), player);
		
		if(getModus() == null && !hasGivenModus())
			tryGiveStartingModus(player);
		
		echeladder.sendInitialPacket(player);
		sendColor(player, !hasLoggedIn);
		sendBoondollars(player);
		gristCache.sendPacket(player);
		sendTitle(player);
		
		hasLoggedIn = true;
	}
	
	private void sendColor(ServerPlayer player, boolean firstTime)
	{
		if(player == null)
			return;
		if(firstTime && !player.isSpectator())
			MSPacketHandler.sendToPlayer(ColorDataPacket.selector(), player);
		else
		{
			ColorDataPacket packet = ColorDataPacket.data(getColor());
			MSPacketHandler.sendToPlayer(packet, player);
		}
	}
	
	private void sendBoondollars(ServerPlayer player)
	{
		if(player == null)
			return;
		BoondollarDataPacket packet = BoondollarDataPacket.create(getBoondollars());
		MSPacketHandler.sendToPlayer(packet, player);
	}
	
	private void sendConsortReputation(ServerPlayer player)
	{
		if(player == null)
			return;
		ConsortReputationDataPacket packet = ConsortReputationDataPacket.create(getConsortReputation(player.level().dimension()));
		//MSPacketHandler.sendToPlayer(packet, player);
	}
	
	private void sendTitle(ServerPlayer player)
	{
		Title newTitle = getTitle();
		if(newTitle == null || player == null)
			return;
		TitleDataPacket packet = TitleDataPacket.create(newTitle);
		MSPacketHandler.sendToPlayer(packet, player);
	}
	
	@Nullable
	ServerPlayer getPlayer()
	{
		return identifier.getPlayer(mcServer);
	}
}