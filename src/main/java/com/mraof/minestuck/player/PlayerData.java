package com.mraof.minestuck.player;

import com.mojang.serialization.DataResult;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.attachment.AttachmentHolder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;

/**
 * Stores any data connected to a specific player.
 * Useful for data attachments that need to be available even when the specific player is offline.
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
	
	static DataResult<PlayerData> load(MinecraftServer mcServer, CompoundTag nbt)
	{
		return IdentifierHandler.load(nbt, "player").map(player -> {
			PlayerData playerData = new PlayerData(mcServer, player);
			
			if(nbt.contains(ATTACHMENTS_NBT_KEY, Tag.TAG_COMPOUND))
				playerData.deserializeAttachments(nbt.getCompound(ATTACHMENTS_NBT_KEY));
			
			return playerData;
		});
	}
	
	public static Optional<PlayerData> get(ServerPlayer player)
	{
		PlayerIdentifier identifier = IdentifierHandler.encode(player);
		if(identifier == null)
			return Optional.empty();
		return Optional.of(PlayerSavedData.get(player.server).getOrCreateData(identifier));
	}
	
	public static PlayerData get(PlayerIdentifier player, Level level)
	{
		return PlayerSavedData.get(level).getOrCreateData(player);
	}
	
	public static PlayerData get(PlayerIdentifier player, MinecraftServer server)
	{
		return PlayerSavedData.get(server).getOrCreateData(player);
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
