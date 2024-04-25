package com.mraof.minestuck.player;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.attachment.AttachmentHolder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Stores and sends any data connected to a specific player.
 * This class is for server-side use only.
 * @author kirderf1
 */
@ParametersAreNonnullByDefault
public final class PlayerData extends AttachmentHolder
{
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
	
	@Nullable
	public static PlayerData get(ServerPlayer player)
	{
		PlayerIdentifier identifier = IdentifierHandler.encode(player);
		if(identifier == null)
			return null;
		return PlayerSavedData.get(player.server).getData(identifier);
	}
	
	public static PlayerData get(PlayerIdentifier player, Level level)
	{
		return PlayerSavedData.get(level).getData(player);
	}
	
	public static PlayerData get(PlayerIdentifier player, MinecraftServer server)
	{
		return PlayerSavedData.get(server).getData(player);
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
