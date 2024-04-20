package com.mraof.minestuck.player;

import com.mraof.minestuck.Minestuck;
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
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.attachment.AttachmentHolder;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
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
	@SubscribeEvent
	public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event)
	{
		ServerPlayer player = (ServerPlayer) event.getEntity();
		MSDimensions.sendDimensionData(player);
	}
	
	@Nonnull
	final PlayerIdentifier identifier;
	
	private final MinecraftServer mcServer;
	
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
	
	@Nullable
	public ServerPlayer getPlayer()
	{
		return identifier.getPlayer(mcServer);
	}
	
	public MinecraftServer getMinecraftServer()
	{
		return this.mcServer;
	}
}