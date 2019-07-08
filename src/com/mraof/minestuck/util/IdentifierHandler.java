package com.mraof.minestuck.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import com.google.common.collect.Lists;
import com.mraof.minestuck.MinestuckConfig;

import net.minecraft.command.CommandException;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraftforge.common.UsernameCache;
import net.minecraftforge.common.util.FakePlayer;

/**
 * Used to encode/decode player usernames, to handle uses with LAN.
 * This file is to now only be used serverside.
 * @author kirderf1
 */
public class IdentifierHandler {
	
	public static String host;	//This basically stores server.getServerOwner(), but for all players to access
	public static final PlayerIdentifier nullIdentifier = new PlayerIdentifier(".null");
	
	private static List<PlayerIdentifier> identifierList = new ArrayList<>();
	private static List<PlayerIdentifier> identifiersToChange = new ArrayList<>();
	private static int nextIdentifierId;
	private static int fakePlayerIndex = 0;
	
	/**
	 * Used to convert a player username to a stored version.
	 */
	private static String usernameEncode(String username) {
		
		if(username.equals(host))
			return ".client";
		else return username;
	}
	
	/**
	 * Used to decode an username for display. Actually only does something if the username equals ".client".
	 * Returns "SP Character" if the world is moved to a server where there isn't a direct player that is the host.
	 */
	private static String usernameDecode(String username)
	{
		if(username.equals(".client"))
			return host==null?"Unknown Player":host;
		else return username;
	}
	
	public static PlayerIdentifier encode(PlayerEntity player)
	{
		if(player instanceof FakePlayer || player.getName() == null)
			return null;
		
		for(PlayerIdentifier identifier : identifierList)
			if(identifier.appliesTo(player))
				return identifier;
		PlayerIdentifier identifier;
		if(MinestuckConfig.useUUID)
			identifier = new PlayerIdentifier(player.getGameProfile().getId());
		else identifier = new PlayerIdentifier(usernameEncode(player.getName().getString()));
		identifier.id = nextIdentifierId;
		nextIdentifierId++;
		identifierList.add(identifier);
		return identifier;
	}
	
	public static boolean hasIdentifier(CompoundNBT nbt, String key)
	{
		return nbt.contains(key, 8) || nbt.contains(key + "Most", 4) && nbt.contains(key + "Least", 4);
	}
	
	public static PlayerIdentifier load(INBT nbt, String key)
	{
		PlayerIdentifier identifier = new PlayerIdentifier(nbt, key);
		if(".null".equals(identifier.username))
			return nullIdentifier;
		
		List<PlayerIdentifier> list = MinestuckConfig.useUUID == identifier.useUUID ? identifierList : identifiersToChange;
		
		for(PlayerIdentifier id : list)
			if(id.equals(identifier))
				return id;
		if(MinestuckConfig.useUUID != identifier.useUUID)
		{
			/*EntityPlayer player = identifier.getPlayer();
			if(player != null) TODO
				return encode(player);*/
		}
		identifier.id = nextIdentifierId;
		nextIdentifierId++;
		list.add(identifier);
		return identifier;
	}
	
	public static void playerLoggedIn(ServerPlayerEntity player)
	{
		Iterator<PlayerIdentifier> iter = identifiersToChange.iterator();
		while(iter.hasNext())
		{
			PlayerIdentifier identifier = iter.next();
			
			if(identifier.appliesTo(player))
			{
				identifier.useUUID = MinestuckConfig.useUUID;
				if(identifier.useUUID)
					identifier.uuid = player.getGameProfile().getId();
				else identifier.username = usernameEncode(player.getName().getString());
				
				identifierList.add(identifier);
				iter.remove();
			}
		}
	}
	
	public static PlayerIdentifier getById(int id)
	{
		for(PlayerIdentifier identifier : identifierList)
			if(identifier.id == id)
				return identifier;
		for(PlayerIdentifier identifier : identifiersToChange)
			if(identifier.id == id)
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
		index: while(true)
		{
			for(PlayerIdentifier identifier : identifierList)
			{
				if(!identifier.useUUID && identifier.username.equals(".fake" + fakePlayerIndex))
				{
					fakePlayerIndex++;
					continue index;
				}
			}
			PlayerIdentifier identifier = new PlayerIdentifier(".fake" + fakePlayerIndex);
			identifier.id = nextIdentifierId;
			nextIdentifierId++;
			identifierList.add(identifier);
			fakePlayerIndex++;
			return identifier;
		}
	}
	
	public static void clear()
	{
		identifierList.clear();
		identifiersToChange.clear();
		nextIdentifierId = 0;
		host = null;
	}
	
	public static class PlayerIdentifier implements Comparable
	{
		private boolean useUUID;
		private UUID uuid;
		private String username;
		
		private int id = -1;
		
		private PlayerIdentifier(UUID id)
		{
			uuid = id;
			useUUID = true;
		}
		
		private PlayerIdentifier(String name)
		{
			username = name;
			useUUID = false;
		}
		
		private PlayerIdentifier(INBT nbt, String key)
		{
			if(nbt instanceof CompoundNBT)
			{
				username = ((StringNBT) nbt).getString();
				useUUID = false;
			} else if(nbt instanceof ListNBT)
			{
				long most = ((LongNBT)((ListNBT) nbt).get(0)).getLong();
				long least = ((LongNBT)((ListNBT) nbt).get(1)).getLong();
				uuid = new UUID(most, least);
				useUUID = true;
			}
			else
			{
				CompoundNBT compound = (CompoundNBT) nbt;
				if(compound.contains(key, 8))
				{
					username = compound.getString(key);
					useUUID = false;
				} else
				{
					long most = compound.getLong(key+"Most");
					long least = compound.getLong(key+"Least");
					uuid = new UUID(most, least);
					useUUID = true;
				}
			}
		}
		
		public ListNBT saveToNBT(ListNBT nbt, String key)
		{
			if(this.useUUID)
			{
				ListNBT list = new ListNBT();
				list.add(new LongNBT(uuid.getMostSignificantBits()));
				list.add(new LongNBT(uuid.getLeastSignificantBits()));
				nbt.add(list);
			} else nbt.add(new StringNBT(username));
			return nbt;
		}
		
		public CompoundNBT saveToNBT(CompoundNBT nbt, String key)
		{
			if(this.useUUID)
			{
				nbt.putLong(key+"Most", uuid.getMostSignificantBits());
				nbt.putLong(key+"Least", uuid.getLeastSignificantBits());
			} else nbt.putString(key, username);
			return nbt;
		}
		
		public boolean appliesTo(PlayerEntity player)
		{
			if(this.useUUID)
				return player.getGameProfile().getId().equals(uuid);
			else return usernameEncode(player.getName().getString()).equals(username);
		}
		
		@Override
		public boolean equals(Object obj)
		{
			if(obj instanceof PlayerIdentifier)
			{
				PlayerIdentifier id = (PlayerIdentifier) obj;
				if(this.useUUID == id.useUUID)
					if(useUUID)
						return this.uuid.equals(id.uuid);
					else return this.username.equals(id.username);
			}
			return false;
		}
		
		public String getUsername()
		{
			if(this.useUUID)
				return UsernameCache.containsUUID(uuid) ? UsernameCache.getLastKnownUsername(uuid) : "Unknown ("+getId()+")";
			else return usernameDecode(username);
		}
		
		public ServerPlayerEntity getPlayer(MinecraftServer server)
		{
			PlayerList list = server == null ? null : server.getPlayerList();
			if(list == null)
				return null;
			if(this.useUUID)
				return list.getPlayerByUUID(uuid);
			else return list.getPlayerByUsername(usernameDecode(username));
		}
		
		/**
		 * @return Id to be used for client-server communication without having to keep the client updated with all needed identifiers
		 */
		public int getId()
		{
			return id;
		}
		
		public String getString()
		{
			return useUUID ? uuid.toString() : username;
		}
		
		@Override
		public int compareTo(Object arg0)
		{
			return this.getString().compareTo(((PlayerIdentifier) arg0).getString());
		}
		
		@Override
		public String toString()
		{
			return getUsername();
		}
		
		@Override
		public int hashCode()
		{
			return useUUID ? uuid.hashCode() : username.hashCode();
		}
		
	}
}