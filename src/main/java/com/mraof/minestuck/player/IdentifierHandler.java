package com.mraof.minestuck.player;

import com.mojang.serialization.DataResult;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.UsernameCache;
import net.neoforged.neoforge.common.util.FakePlayer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

/**
 * Used to encode/decode player usernames, to handle uses with LAN.
 * This file is to now only be used serverside.
 * @author kirderf1
 */
public class IdentifierHandler
{
	private static final List<PlayerIdentifier> identifierList = new ArrayList<>();
	private static int nextIdentifierId;
	private static int fakePlayerIndex = 0;
	
	@Nullable
	public static PlayerIdentifier encode(Player player)
	{
		if(player instanceof FakePlayer || player.getGameProfile() == null)
			return null;
		
		for(PlayerIdentifier identifier : identifierList)
			if(identifier.appliesTo(player))
				return identifier;
		PlayerIdentifier identifier = new UUIDIdentifier(nextIdentifierId++, player.getGameProfile().getId());
		identifierList.add(identifier);
		return identifier;
	}
	
	@Deprecated	//Prefer the DataResult functions
	@Nonnull
	public static PlayerIdentifier loadOrThrow(CompoundTag tag, String key) throws RuntimeException
	{
		return load(tag, key).getOrThrow(false, message -> {});
	}
	
	public static DataResult<Optional<PlayerIdentifier>> loadOptional(CompoundTag tag, String key)
	{
		if(!tag.contains(key, Tag.TAG_STRING))
			return DataResult.success(Optional.empty());
		return load(tag, key).map(Optional::of);
	}
	
	/**
	 * Tries to parse a player identifier and returns the result. It will not return a {@code NULL_IDENTIFIER}.
	 */
	public static DataResult<PlayerIdentifier> load(CompoundTag tag, String key)
	{
		return loadNullable(tag, key).flatMap(id -> id.map(DataResult::success).orElse(DataResult.error(() -> "Null identifiers not supported here")));
	}
	
	public static DataResult<Optional<PlayerIdentifier>> loadNullable(CompoundTag tag, String key)
	{
		String type = tag.getString(key);
		PlayerIdentifier identifier;
		switch(type)
		{
			case "null" ->
			{
				return DataResult.success(Optional.empty());
			}
			case "uuid" -> identifier = new UUIDIdentifier(nextIdentifierId, tag.getUUID(key + "_uuid"));
			case "fake" -> identifier = new FakeIdentifier(nextIdentifierId, tag.getInt(key + "_count"));
			default ->
			{
				return DataResult.error(() -> "Can't parse identifier type " + type);
			}
		}
		
		for(PlayerIdentifier id : identifierList)
			if(id.equals(identifier))
				return DataResult.success(Optional.of(id));
		
		nextIdentifierId++;
		identifierList.add(identifier);
		return DataResult.success(Optional.of(identifier));
	}
	
	@Nullable
	public static PlayerIdentifier getById(int id)
	{
		for(PlayerIdentifier identifier : identifierList)
			if(identifier.getId() == id)
				return identifier;
		return null;
	}
	
	/*public static PlayerIdentifier getForCommand(MinecraftServer server, ICommandSender sender, String playerName) throws CommandException
	{
		if(playerName.startsWith("@"))	//Refer directly to an identifier
		{
			playerName = playerName.substring(1);
			
			for(PlayerIdentifier identifier : identifierList)
				if(identifier.getString().equals(playerName))
					return identifier;
			for(PlayerIdentifier identifier : identifiersToChange)
				if(identifier.getString().equals(playerName))
					return identifier;
			
			throw new CommandException("Failed to find an identifier that fits \"%s\". The player has to be online at least once to have an identifier.", playerName);
		} else	//Refer to an online player
		{
			EntityPlayer player = CommandBase.getPlayer(server, sender, playerName);
			
			return encode(player);
		}
		
	}
	
	public static List<String> getCommandAutocomplete(MinecraftServer server, String[] args)
	{
		String arg = args[args.length - 1];
		if(arg.startsWith("@"))
		{
			arg = arg.substring(1);
			List<String> list = Lists.newArrayList();
			for(PlayerIdentifier identifier : identifierList)
				if(identifier.getString().startsWith(arg))
					list.add("@"+identifier.getString());
			for(PlayerIdentifier identifier : identifiersToChange)
				if(identifier.getString().startsWith(arg))
					list.add("@"+identifier.getString());
			return list;
		} else
		{
			return CommandBase.getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
		}
	}*/
	
	public static PlayerIdentifier createNewFakeIdentifier()
	{
		PlayerIdentifier identifier;
		do
		{
			identifier = new FakeIdentifier(nextIdentifierId, fakePlayerIndex++);
		} while(identifierList.contains(identifier));
		
		nextIdentifierId++;
		identifierList.add(identifier);
		return identifier;
	}
	
	public static void clear()
	{
		identifierList.clear();
		nextIdentifierId = 0;
		fakePlayerIndex = 0;
	}
	
	private static class UUIDIdentifier extends PlayerIdentifier
	{
		private final UUID uuid;
		
		private UUIDIdentifier(int id, UUID uuid)
		{
			super(id);
			this.uuid = uuid;
		}
		
		@Override
		public boolean appliesTo(Player player)
		{
			return player.getGameProfile().getId().equals(uuid);
		}
		
		@Override
		public String getUsername()
		{
			return UsernameCache.containsUUID(uuid) ? UsernameCache.getLastKnownUsername(uuid) : "Unknown ("+getId()+")";
		}
		
		@Override
		public ServerPlayer getPlayer(MinecraftServer server)
		{
			PlayerList list = server == null ? null : server.getPlayerList();
			if(list == null)
				return null;
			return list.getPlayer(uuid);
		}
		
		@Override
		public String getCommandString()
		{
			return uuid.toString();
		}
		
		@Override
		public CompoundTag saveToNBT(CompoundTag nbt, String key)
		{
			nbt.putString(key, "uuid");
			nbt.putUUID(key + "_uuid", uuid);
			return nbt;
		}
		
		@Override
		public String toString()
		{
			return "Identifier:"+getUsername();
		}
		
		@Override
		public boolean equals(Object o)
		{
			if(this == o) return true;
			if(o == null || getClass() != o.getClass()) return false;
			UUIDIdentifier that = (UUIDIdentifier) o;
			return Objects.equals(uuid, that.uuid);
		}
		
		@Override
		public int hashCode()
		{
			return Objects.hash(uuid);
		}
	}
	
	private static class FakeIdentifier extends PlayerIdentifier
	{
		private final int count;
		
		FakeIdentifier(int id, int count)
		{
			super(id);
			this.count = count;
		}
		
		@Override
		public boolean appliesTo(Player player)
		{
			return false;
		}
		
		@Override
		public String getUsername()
		{
			return "Fake player "+count;
		}
		
		@Override
		public ServerPlayer getPlayer(MinecraftServer server)
		{
			return null;
		}
		
		@Override
		public String getCommandString()
		{
			return "fake"+count;
		}
		
		@Override
		public CompoundTag saveToNBT(CompoundTag nbt, String key)
		{
			nbt.putString(key, "fake");
			nbt.putInt(key+"_count", count);
			return nbt;
		}
		
		@Override
		public String toString()
		{
			return "Identifier:fake_"+count;
		}
		
		@Override
		public boolean equals(Object o)
		{
			if(this == o) return true;
			if(o == null || getClass() != o.getClass()) return false;
			FakeIdentifier that = (FakeIdentifier) o;
			return count == that.count;
		}
		
		@Override
		public int hashCode()
		{
			return Objects.hash(count);
		}
	}
}