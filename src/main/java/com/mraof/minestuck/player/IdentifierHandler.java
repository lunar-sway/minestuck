package com.mraof.minestuck.player;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraftforge.common.UsernameCache;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.FakePlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Used to encode/decode player usernames, to handle uses with LAN.
 * This file is to now only be used serverside.
 * @author kirderf1
 */
public class IdentifierHandler
{
	public static final PlayerIdentifier NULL_IDENTIFIER = new NullIdentifier();
	
	private static List<PlayerIdentifier> identifierList = new ArrayList<>();
	private static int nextIdentifierId;
	private static int fakePlayerIndex = 0;
	
	public static PlayerIdentifier encode(PlayerEntity player)
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
	
	public static boolean hasIdentifier(CompoundNBT nbt, String key)
	{
		return nbt.contains(key, Constants.NBT.TAG_STRING) || nbt.contains(key + "Most", Constants.NBT.TAG_LONG) && nbt.contains(key + "Least", Constants.NBT.TAG_LONG);
	}
	
	public static PlayerIdentifier load(CompoundNBT nbt, String key)
	{
		PlayerIdentifier identifier;
		String type = nbt.getString(key);
		switch(type)
		{
			case "null":
				return NULL_IDENTIFIER;
			case "uuid":
				identifier = new UUIDIdentifier(nextIdentifierId, nbt.getUUID(key + "_uuid"));
				break;
			case "fake":
				identifier = new FakeIdentifier(nextIdentifierId, nbt.getInt(key+"_count"));
				break;
			default: throw new IllegalArgumentException("Can't parse identifier type "+type);
		}
		
		for(PlayerIdentifier id : identifierList)
			if(id.equals(identifier))
				return id;
		
		nextIdentifierId++;
		identifierList.add(identifier);
		return identifier;
	}
	
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
		private UUID uuid;
		
		private UUIDIdentifier(int id, UUID uuid)
		{
			super(id);
			this.uuid = uuid;
		}
		
		@Override
		public boolean appliesTo(PlayerEntity player)
		{
			return player.getGameProfile().getId().equals(uuid);
		}
		
		@Override
		public String getUsername()
		{
			return UsernameCache.containsUUID(uuid) ? UsernameCache.getLastKnownUsername(uuid) : "Unknown ("+getId()+")";
		}
		
		@Override
		public ServerPlayerEntity getPlayer(MinecraftServer server)
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
		public CompoundNBT saveToNBT(CompoundNBT nbt, String key)
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
	
	private static class NullIdentifier extends PlayerIdentifier
	{
		public NullIdentifier()
		{
			super(-1);
		}
		
		@Override
		public boolean appliesTo(PlayerEntity player)
		{
			return false;
		}
		
		@Override
		public String getUsername()
		{
			return "Invalid Player";
		}
		
		@Override
		public ServerPlayerEntity getPlayer(MinecraftServer server)
		{
			return null;
		}
		
		@Override
		public String getCommandString()
		{
			return "null";
		}
		
		@Override
		public CompoundNBT saveToNBT(CompoundNBT nbt, String key)
		{
			nbt.putString(key, "null");
			return nbt;
		}
		
		@Override
		public String toString()
		{
			return "Identifier:null";
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
		public boolean appliesTo(PlayerEntity player)
		{
			return false;
		}
		
		@Override
		public String getUsername()
		{
			return "Fake player "+count;
		}
		
		@Override
		public ServerPlayerEntity getPlayer(MinecraftServer server)
		{
			return null;
		}
		
		@Override
		public String getCommandString()
		{
			return "fake"+count;
		}
		
		@Override
		public CompoundNBT saveToNBT(CompoundNBT nbt, String key)
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