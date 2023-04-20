package com.mraof.minestuck.player;

import com.mraof.minestuck.alchemy.*;
import com.mraof.minestuck.computer.editmode.EditData;
import com.mraof.minestuck.computer.editmode.ServerEditHandler;
import com.mraof.minestuck.entity.item.GristEntity;
import com.mraof.minestuck.network.GristToastPacket;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.network.data.GristCachePacket;
import com.mraof.minestuck.skaianet.SburbConnection;
import com.mraof.minestuck.skaianet.Session;
import com.mraof.minestuck.skaianet.SessionHandler;
import com.mraof.minestuck.skaianet.SkaianetHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
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
public final class GristCache
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	private final PlayerData data;
	private final MinecraftServer mcServer;
	private IImmutableGristSet gristSet;
	
	GristCache(PlayerData data, MinecraftServer mcServer)
	{
		this.data = data;
		this.mcServer = mcServer;
		this.gristSet = new ImmutableGristSet(GristTypes.BUILD, 20);
	}
	
	public static GristCache get(ServerPlayer player)
	{
		return PlayerSavedData.getData(player).getGristCache();
	}
	
	public static GristCache get(Level level, PlayerIdentifier player)
	{
		return PlayerSavedData.getData(player, level).getGristCache();
	}
	
	public static GristCache get(MinecraftServer mcServer, PlayerIdentifier player)
	{
		return PlayerSavedData.getData(player, mcServer).getGristCache();
	}
	
	/**
	 * Returns a grist set of how much space for each type that this grist cache has.
	 */
	public NonNegativeGristSet getCapacitySet()
	{
		long capacity = data.getEcheladder().getGristCapacity();
		NonNegativeGristSet capacitySet = new NonNegativeGristSet();
		for(GristType type : GristTypes.values())
		{
			long amountInCache = this.getGristSet().getGrist(type);
			if(amountInCache < capacity)
				capacitySet.addGrist(type, capacity - amountInCache);
		}
		return capacitySet;
	}
	
	void read(CompoundTag nbt)
	{
		gristSet = IImmutableGristSet.NON_NEGATIVE_CODEC.parse(NbtOps.INSTANCE, nbt.get("grist_cache"))
				.resultOrPartial(LOGGER::error).orElse(new ImmutableGristSet());
	}
	
	void write(CompoundTag nbt)
	{
		IImmutableGristSet.NON_NEGATIVE_CODEC.encodeStart(NbtOps.INSTANCE, this.gristSet)
				.resultOrPartial(LOGGER::error).ifPresent(tag -> nbt.put("grist_cache", tag));
	}
	
	public IImmutableGristSet getGristSet()
	{
		return this.gristSet;
	}
	
	public long getRemainingCapacity(GristType type)
	{
		return Math.max(0, data.getEcheladder().getGristCapacity() - gristSet.getGrist(type));
	}
	
	public boolean canAfford(IGristSet cost)
	{
		return canAdd(cost.mutableCopy().scale(-1));
	}
	
	public boolean canAdd(GristSet addition)
	{
		return addWithinCapacity(this.gristSet.mutableCopy(), addition, data.getEcheladder().getGristCapacity()).isEmpty();
	}
	
	public static boolean canAfford(IGristSet source, IGristSet cost, long limit)
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
	public boolean tryTake(IGristSet cost, @Nullable GristHelper.EnumSource source)
	{
		GristSet change = cost.mutableCopy().scale(-1);
		
		NonNegativeGristSet newCache = new NonNegativeGristSet(this.getGristSet());
		
		GristSet excessGrist = addWithinCapacity(newCache, change, data.getEcheladder().getGristCapacity());
		
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
	public void addWithGutter(IGristSet set, @Nullable GristHelper.EnumSource source)
	{
		GristSet overflowedGrist = this.addWithinCapacity(set, source);
		
		if(!overflowedGrist.isEmpty())
		{
			Session session = SessionHandler.get(mcServer).getPlayerSession(data.identifier);
			if(session != null)
			{
				session.getGristGutter().addGristFrom(overflowedGrist);
				GristToastPacket.notify(mcServer, data.identifier, set, GristHelper.EnumSource.GUTTER); //still send a grist toast when adding to gutter
			}
			
			ServerPlayer player = data.getPlayer();
			if(player != null && !overflowedGrist.isEmpty())
			{
				int gusherCount = player.getRandom().nextInt(6) > 0 ? 1 : 2;
				GristEntity.spawnGristEntities(overflowedGrist, player.getLevel(), player.getX(), player.getY(), player.getZ(), player.getRandom(),
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
	public GristSet addWithinCapacity(IGristSet set, @Nullable GristHelper.EnumSource source)
	{
		Objects.requireNonNull(set);
		
		NonNegativeGristSet newCache = new NonNegativeGristSet(this.getGristSet());
		
		GristSet excessGrist = addWithinCapacity(newCache, set, data.getEcheladder().getGristCapacity());
		
		if(!excessGrist.equalContent(set))
		{
			this.set(newCache);
			if(source != null)
				GristToastPacket.notify(mcServer, data.identifier, set.mutableCopy().addGrist(excessGrist.mutableCopy().scale(-1)), source);
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
		data.markDirty();
		data.gristCache.sendPacket(data.getPlayer());
	}
	
	void sendPacket(ServerPlayer player)
	{
		
		//Send to the player
		if(player != null)
		{
			GristCachePacket packet = new GristCachePacket(this.getGristSet(), false);
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
	
	/**
	 * Adds or removes grist such that the grist does not exceed the given capacity or falls below 0.
	 * This function should be able to handle a grist type already being out of bounds, for which it would behave as if it was right at the bound.
	 * Returns any excess grist.
	 */
	private static GristSet addWithinCapacity(GristSet target, IGristSet source, long capacity)
	{
		if(capacity < 0)
			throw new IllegalArgumentException("Capacity under 0 not allowed.");
		
		GristSet remainder = new GristSet();
		
		for(GristAmount amount : source.asAmounts())
		{
			if(amount.amount() > 0)
			{
				long toAdd = Mth.clamp(capacity - target.getGrist(amount.type()), 0, amount.amount());
				long remainingAmount = amount.amount() - toAdd;
				if(toAdd != 0)
					target.addGrist(amount.type(), toAdd);
				if(remainingAmount != 0)
					remainder.addGrist(amount.type(), remainingAmount);
			} else if(amount.amount() < 0)
			{
				long toAdd = Mth.clamp(-target.getGrist(amount.type()), amount.amount(), 0);
				long remainingAmount = amount.amount() - toAdd;
				if(toAdd != 0)
					target.addGrist(amount.type(), toAdd);
				if(remainingAmount != 0)
					remainder.addGrist(amount.type(), remainingAmount);
			}
		}
		
		return remainder;
	}
}
