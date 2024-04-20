package com.mraof.minestuck.player;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.world.MSDimensions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.attachment.AttachmentHolder;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

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
	}
	
	CompoundTag writeToNBT()
	{
		CompoundTag nbt = new CompoundTag();
		identifier.saveToNBT(nbt, "player");
		
		CompoundTag attachments = this.serializeAttachments();
		if(attachments != null)
			nbt.put(ATTACHMENTS_NBT_KEY, attachments);
		
		return nbt;
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
