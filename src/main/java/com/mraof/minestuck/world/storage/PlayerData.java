package com.mraof.minestuck.world.storage;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.computer.editmode.EditData;
import com.mraof.minestuck.computer.editmode.ServerEditHandler;
import com.mraof.minestuck.inventory.captchalogue.CaptchaDeckHandler;
import com.mraof.minestuck.inventory.captchalogue.Modus;
import com.mraof.minestuck.item.crafting.alchemy.GristSet;
import com.mraof.minestuck.item.crafting.alchemy.GristTypes;
import com.mraof.minestuck.item.crafting.alchemy.ImmutableGristSet;
import com.mraof.minestuck.item.crafting.alchemy.NonNegativeGristSet;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.network.data.*;
import com.mraof.minestuck.player.Echeladder;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerIdentifier;
import com.mraof.minestuck.player.Title;
import com.mraof.minestuck.skaianet.SburbConnection;
import com.mraof.minestuck.skaianet.SburbHandler;
import com.mraof.minestuck.skaianet.SkaianetHandler;
import com.mraof.minestuck.util.ColorHandler;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
		ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
		PlayerSavedData.getData(player).onPlayerLoggedIn(player);
	}
	
	@SubscribeEvent
	public static void onPlayerChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event)
	{
		ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
		PlayerSavedData.getData(player).sendConsortReputation(player);
	}
	
	final PlayerIdentifier identifier;
	
	private final PlayerSavedData savedData;
	private final Echeladder echeladder;
	private int color = ColorHandler.DEFAULT_COLOR;
	
	private boolean givenModus;
	private Modus modus;
	private long boondollars;
	private ImmutableGristSet gristCache;	//This is immutable in order to control where it can be changed
	
	private final Map<ResourceLocation, Integer> consortReputation = new HashMap<>();
	
	private Title title;
	private boolean effectToggle;
	
	private boolean hasLoggedIn;
	
	PlayerData(PlayerSavedData savedData, PlayerIdentifier player)
	{
		this.savedData = savedData;
		this.identifier = player;
		echeladder = new Echeladder(savedData, player);
		gristCache = new ImmutableGristSet(GristTypes.BUILD, 20);
		hasLoggedIn = false;
	}
	
	PlayerData(PlayerSavedData savedData, CompoundNBT nbt)
	{
		this.savedData = savedData;
		this.identifier = IdentifierHandler.load(nbt, "player");
		
		echeladder = new Echeladder(savedData, identifier);
		echeladder.loadEcheladder(nbt);
		if (nbt.contains("color"))
			this.color = nbt.getInt("color");
		
		if (nbt.contains("modus"))
		{
			this.modus = CaptchaDeckHandler.readFromNBT(nbt.getCompound("modus"), savedData);
			givenModus = true;
		}
		else givenModus = nbt.getBoolean("given_modus");
		boondollars = nbt.getLong("boondollars");
		gristCache = NonNegativeGristSet.read(nbt.getList("grist_cache", Constants.NBT.TAG_COMPOUND)).asImmutable();
		
		ListNBT list = nbt.getList("consort_reputation", Constants.NBT.TAG_COMPOUND);
		for(int i = 0; i < list.size(); i++)
		{
			CompoundNBT dimensionRep = list.getCompound(i);
			ResourceLocation dimension = ResourceLocation.tryCreate(dimensionRep.getString("dim"));
			if(dimension != null)
				consortReputation.put(dimension, dimensionRep.getInt("rep"));
		}
		
		title = Title.tryRead(nbt, "title");
		effectToggle = nbt.getBoolean("effect_toggle");
		
		hasLoggedIn = true;
	}
	
	CompoundNBT writeToNBT()
	{
		CompoundNBT nbt = new CompoundNBT();
		identifier.saveToNBT(nbt, "player");
		echeladder.saveEcheladder(nbt);
		nbt.putInt("color", color);
		
		if (this.modus != null)
			nbt.put("modus", CaptchaDeckHandler.writeToNBT(modus));
		else nbt.putBoolean("given_modus", givenModus);
		nbt.putLong("boondollars", boondollars);
		nbt.put("grist_cache", gristCache.write(new ListNBT()));
		
		ListNBT list = new ListNBT();
		for(Map.Entry<ResourceLocation, Integer> entry : consortReputation.entrySet())
		{
			CompoundNBT dimensionRep = new CompoundNBT();
			dimensionRep.putString("dim", entry.getKey().toString());
			dimensionRep.putInt("rep", entry.getValue());
			list.add(dimensionRep);
		}
		nbt.put("consort_reputation", list);
		
		if(title != null)
			title.write(nbt, "title");
		nbt.putBoolean("effect_toggle", effectToggle);
		
		return nbt;
	}
	
	private void markDirty()
	{
		savedData.markDirty();
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
		if(SburbHandler.canSelectColor(identifier, savedData.mcServer) && this.color != color)
		{
			this.color = color;
			markDirty();
			
			sendColor(getPlayer(), false);
		}
	}
	
	public Modus getModus()
	{
		return modus;
	}
	
	public void setModus(Modus modus)
	{
		if(this.modus != modus)
		{
			this.modus = modus;
			if(modus != null)
				setGivenModus();
			markDirty();
		}
	}
	
	public boolean hasGivenModus()
	{
		return givenModus;
	}
	
	private void setGivenModus()
	{
		if(!givenModus)
		{
			givenModus = true;
			markDirty();
		}
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
			markDirty();
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
			markDirty();
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
			markDirty();
			sendBoondollars(getPlayer());
		}
	}
	
	public int getConsortReputation(DimensionType dim)
	{
		return consortReputation.getOrDefault(dim.getRegistryName(), 0);
	}
	
	public void addConsortReputation(int amount, DimensionType dim)
	{
		int oldRep = getConsortReputation(dim);
		int newRep = MathHelper.clamp(oldRep + amount, -10000, 10000);
		
		if(newRep != oldRep)
		{
			consortReputation.put(dim.getRegistryName(), newRep);
			markDirty();
			sendConsortReputation(getPlayer());
		}
	}
	
	public ImmutableGristSet getGristCache()
	{
		return gristCache;
	}
	
	public void setGristCache(NonNegativeGristSet cache)
	{
		gristCache = cache.asImmutable();
		markDirty();
		updateGristCache(getPlayer());
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
			markDirty();
			sendTitle(getPlayer());
		} else throw new IllegalStateException("Can't set title for player "+ identifier.getUsername()+" because they already have one");
	}
	
	public boolean effectToggle()
	{
		return effectToggle;
	}
	
	public void effectToggle(boolean toggle)
	{
		if(effectToggle != toggle)
		{
			effectToggle = toggle;
			markDirty();
		}
	}
	
	private void tryGiveStartingModus(ServerPlayerEntity player)
	{
		List<String> startingTypes = MinestuckConfig.SERVER.startingModusTypes.get();
		if(!startingTypes.isEmpty())
		{
			String type = startingTypes.get(player.world.rand.nextInt(startingTypes.size()));
			if(type.isEmpty())
			{
				setGivenModus();
				return;
			}
			ResourceLocation name = ResourceLocation.tryCreate(type);
			if(name == null)
				LOGGER.error("Unable to parse starting modus type {} as a resource location!", type);
			else
			{
				Modus modus = CaptchaDeckHandler.createServerModus(name, savedData);
				if(modus != null)
				{
					modus.initModus(null, player, null, MinestuckConfig.SERVER.initialModusSize.get());
					setModus(modus);
				} else LOGGER.warn("Couldn't create a starting modus type by name {}.", type);
			}
		}
	}
	
	public void onPlayerLoggedIn(ServerPlayerEntity player)
	{
		getEcheladder().updateEcheladderBonuses(player);
		
		if(getModus() == null && !hasGivenModus())
			tryGiveStartingModus(player);
		
		if(getModus() != null)
		{
			Modus modus = getModus();
			MSPacketHandler.sendToPlayer(ModusDataPacket.create(CaptchaDeckHandler.writeToNBT(modus)), player);
		}
		
		echeladder.sendInitialPacket(player);
		sendColor(player, !hasLoggedIn);
		sendBoondollars(player);
		updateGristCache(player);
		sendTitle(player);
		
		hasLoggedIn = true;
	}
	
	private void sendColor(ServerPlayerEntity player, boolean firstTime)
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
	
	private void sendBoondollars(ServerPlayerEntity player)
	{
		if(player == null)
			return;
		BoondollarDataPacket packet = BoondollarDataPacket.create(getBoondollars());
		MSPacketHandler.sendToPlayer(packet, player);
	}
	
	private void sendConsortReputation(ServerPlayerEntity player)
	{
		if(player == null)
			return;
		ConsortReputationDataPacket packet = ConsortReputationDataPacket.create(getConsortReputation(player.dimension));
		//MSPacketHandler.sendToPlayer(packet, player);
	}
	
	private void updateGristCache(ServerPlayerEntity player)
	{
		GristSet gristSet = getGristCache();
		
		//Send to the player
		if(player != null)
		{
			GristCachePacket packet = new GristCachePacket(gristSet, false);
			MSPacketHandler.sendToPlayer(packet, player);
		}
		
		//Also send to the editing player, if there is any
		SburbConnection c = SkaianetHandler.get(savedData.mcServer).getActiveConnection(identifier);
		if(c != null)
		{
			EditData data = ServerEditHandler.getData(savedData.mcServer, c);
			if(data != null)
			{
				data.sendGristCacheToEditor();
			}
		}
	}
	
	private void sendTitle(ServerPlayerEntity player)
	{
		Title newTitle = getTitle();
		if(newTitle == null || player == null)
			return;
		TitleDataPacket packet = TitleDataPacket.create(newTitle);
		MSPacketHandler.sendToPlayer(packet, player);
	}
	
	private ServerPlayerEntity getPlayer()
	{
		return identifier.getPlayer(savedData.mcServer);
	}
}