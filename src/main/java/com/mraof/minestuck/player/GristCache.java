package com.mraof.minestuck.player;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.alchemy.GristGutter;
import com.mraof.minestuck.alchemy.GristHelper;
import com.mraof.minestuck.api.alchemy.*;
import com.mraof.minestuck.computer.editmode.EditData;
import com.mraof.minestuck.computer.editmode.ServerEditHandler;
import com.mraof.minestuck.entity.item.GristEntity;
import com.mraof.minestuck.network.GristCachePacket;
import com.mraof.minestuck.network.GristToastPacket;
import com.mraof.minestuck.util.MSAttachments;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.Objects;

/**
 * An object specific to a player that stores all grist held by that player,
 * as well as providing the API to interact with this grist,
 * while making sure to sync the cache to client-side or mark it to be saved when relevant.
 *
 * @author kirderf1
 */
@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class GristCache implements INBTSerializable<Tag>
{
	public static final String MISSING_MESSAGE = "grist.missing";
	private static final Logger LOGGER = LogManager.getLogger();
	
	private final PlayerData data;
	private final MinecraftServer mcServer;
	private ImmutableGristSet gristSet;
	
	public GristCache(PlayerData data)
	{
		this.data = data;
		this.mcServer = data.getMinecraftServer();
		this.gristSet = GristTypes.BUILD.get().amount(20);
	}
	
	public static GristCache get(ServerPlayer player)
	{
		PlayerData data = PlayerData.get(player).orElseThrow();
		return get(data);
	}
	
	public static GristCache get(Level level, PlayerIdentifier player)
	{
		return get(PlayerData.get(player, level));
	}
	
	public static GristCache get(MinecraftServer mcServer, PlayerIdentifier player)
	{
		return get(PlayerData.get(player, mcServer));
	}
	
	public static GristCache get(PlayerData playerData)
	{
		return playerData.getData(MSAttachments.GRIST_CACHE);
	}
	
	public static Component createMissingMessage(GristSet gristSet)
	{
		return Component.translatable(MISSING_MESSAGE, gristSet.asTextComponent());
	}
	
	@SubscribeEvent
	private static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event)
	{
		ServerPlayer player = (ServerPlayer) event.getEntity();
		
		get(player).sendPacket(player);
	}
	
	/**
	 * Returns a grist set of how much space for each type that this grist cache has.
	 */
	public NonNegativeGristSet getCapacitySet()
	{
		long capacity = Echeladder.get(data).getGristCapacity();
		NonNegativeGristSet capacitySet = new NonNegativeGristSet();
		for(GristType type : GristTypes.REGISTRY)
		{
			long amountInCache = this.getGristSet().getGrist(type);
			if(amountInCache < capacity)
				capacitySet.add(type, capacity - amountInCache);
		}
		return capacitySet;
	}
	
	@Override
	public void deserializeNBT(Tag tag)
	{
		gristSet = ImmutableGristSet.NON_NEGATIVE_CODEC.parse(NbtOps.INSTANCE, tag)
				.resultOrPartial(LOGGER::error).orElse(GristSet.EMPTY);
	}
	
	@Nullable
	@Override
	public Tag serializeNBT()
	{
		return ImmutableGristSet.NON_NEGATIVE_CODEC.encodeStart(NbtOps.INSTANCE, this.gristSet)
				.resultOrPartial(LOGGER::error).orElse(null);
	}
	
	public ImmutableGristSet getGristSet()
	{
		return this.gristSet;
	}
	
	public long getRemainingCapacity(GristType type)
	{
		return Math.max(0, Echeladder.get(data).getGristCapacity() - gristSet.getGrist(type));
	}
	
	public boolean canAfford(GristSet cost)
	{
		return canAdd(cost.mutableCopy().scale(-1));
	}
	
	public boolean canAdd(GristSet addition)
	{
		return addWithinCapacity(this.gristSet.mutableCopy(), addition, Echeladder.get(data).getGristCapacity()).isEmpty();
	}
	
	public static boolean canAfford(GristSet source, GristSet cost, long limit)
	{
		return addWithinCapacity(source.mutableCopy(), cost.mutableCopy().scale(-1), limit).isEmpty();
	}
	
	/**
	 * Tries to take a specified amount of grist from this cache.
	 * If it fails (due to insufficient grist or capacity), the cache will remain unchanged.
	 * @param cost The amount of grist that it will try to take. Can be negative.
	 * @param source The source of this change. If not null, a grist toast will be displayed for this change and source if successful.
	 * @return true if it succeeded. false otherwise.
	 */
	public boolean tryTake(GristSet cost, @Nullable GristHelper.EnumSource source)
	{
		GristSet change = cost.mutableCopy().scale(-1);
		
		NonNegativeGristSet newCache = new NonNegativeGristSet(this.getGristSet());
		
		GristSet excessGrist = addWithinCapacity(newCache, change, Echeladder.get(data).getGristCapacity());
		
		if(excessGrist.isEmpty())
		{
			this.set(newCache);
			
			if(source != null)
				GristToastPacket.notify(mcServer, data.identifier, change, source);
			
			return true;
		} else
			return false;
	}
	
	/**
	 * Adds as much grist as can fit to this cache.
	 * Any excess grist will be sent to the gutter, if one exists.
	 * If there are still grist left over, it will spawn as entities at the player if they are online. Otherwise, the excess grist will be discarded.
	 *
	 * @param set The grist to add to this cache.
	 * @param source The source of this change. If not null, a grist toast will be displayed for this change and source.
	 */
	public void addWithGutter(GristSet set, @Nullable GristHelper.EnumSource source)
	{
		MutableGristSet overflowedGrist = this.addWithinCapacity(set, source);
		
		if(!overflowedGrist.isEmpty())
		{
			GristGutter.get(data.identifier, mcServer).addGristFrom(overflowedGrist);
			GristToastPacket.notify(mcServer, data.identifier, set, GristHelper.EnumSource.GUTTER); //still send a grist toast when adding to gutter
			
			ServerPlayer player = data.getPlayer();
			if(player != null && !overflowedGrist.isEmpty())
			{
				int gusherCount = player.getRandom().nextInt(6) > 0 ? 1 : 2;
				GristEntity.spawnGristEntities(overflowedGrist, player.level(), player.getX(), player.getY(), player.getZ(), player.getRandom(),
						entity -> entity.setDeltaMovement(entity.getDeltaMovement().multiply(1.5, 0.5, 1.5)), 90, gusherCount);
			}
		}
	}
	
	/**
	 * Adds as much grist as can fit into this cache, and returns any remaining grist.
	 *
	 * @param set The grist to add to this cache.
	 * @param source The source of this change. If not null, a grist toast will be displayed for this change and source.
	 * @return any grist that did not fit in the cache.
	 */
	public MutableGristSet addWithinCapacity(GristSet set, @Nullable GristHelper.EnumSource source)
	{
		Objects.requireNonNull(set);
		
		NonNegativeGristSet newCache = new NonNegativeGristSet(this.getGristSet());
		
		MutableGristSet excessGrist = addWithinCapacity(newCache, set, Echeladder.get(data).getGristCapacity());
		
		if(!excessGrist.equalContent(set))
		{
			this.set(newCache);
			if(source != null)
				GristToastPacket.notify(mcServer, data.identifier, set.mutableCopy().add(excessGrist.mutableCopy().scale(-1)), source);
		}
		
		return excessGrist;
	}
	
	/**
	 * Sets the grist of this cache to the given set,
	 * ignoring capacity limitations on this cache.
	 */
	public void set(NonNegativeGristSet cache)
	{
		gristSet = cache.asImmutable();
		this.sendPacket(data.getPlayer());
	}
	
	void sendPacket(ServerPlayer player)
	{
		
		//Send to the player
		if(player != null)
		{
			GristCachePacket packet = new GristCachePacket(this.getGristSet(), ClientPlayerData.CacheSource.PLAYER);
			player.connection.send(packet);
		}
		
		//Also send to the editing player, if there is any
		EditData data = ServerEditHandler.getData(mcServer, this.data.identifier);
		if(data != null)
		{
			data.sendGristCacheToEditor();
		}
	}
	
	/**
	 * Adds or removes grist such that the grist does not exceed the given capacity or falls below 0.
	 * This function should be able to handle a grist type already being out of bounds, for which it would behave as if it was right at the bound.
	 * Returns any excess grist.
	 */
	private static MutableGristSet addWithinCapacity(MutableGristSet target, GristSet source, long capacity)
	{
		if(capacity < 0)
			throw new IllegalArgumentException("Capacity under 0 not allowed.");
		
		MutableGristSet remainder = MutableGristSet.newDefault();
		
		for(GristAmount amount : source.asAmounts())
		{
			if(amount.amount() > 0)
			{
				long toAdd = Math.max(0, Math.min(capacity - target.getGrist(amount.type()), amount.amount()));
				long remainingAmount = amount.amount() - toAdd;
				if(toAdd != 0)
					target.add(amount.type(), toAdd);
				if(remainingAmount != 0)
					remainder.add(amount.type(), remainingAmount);
			} else if(amount.amount() < 0)
			{
				long toAdd = Math.max(amount.amount(), Math.min(-target.getGrist(amount.type()), 0));
				long remainingAmount = amount.amount() - toAdd;
				if(toAdd != 0)
					target.add(amount.type(), toAdd);
				if(remainingAmount != 0)
					remainder.add(amount.type(), remainingAmount);
			}
		}
		
		return remainder;
	}
}
