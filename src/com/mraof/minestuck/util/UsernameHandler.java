package com.mraof.minestuck.util;

import net.minecraft.entity.player.EntityPlayer;

/**
 * Used to encode/decode player usernames, to handle uses with LAN.
 * @author kirderf1
 */
public class UsernameHandler {
	
	public static String host;	//This basically stores server.getServerOwner(), but for all players to access
	
	/**
	 * Used to convert a player username to a stored version.
	 */
	public static String encode(String username) {
		
		if(username.equals(host))
			return ".client";
		else return username;
	}
	
	/**
	 * Used to decode an username for display. Actually only does something if the username equals ".client".
	 * Returns "SP Character" if the world is moved to a server where there isn't a direct player that is the host.
	 */
	public static String decode(String username) {
		if(username.equals(".client"))
			return host==null?"SP Character":host;
		else return username;
	}
	
	public static String encode(EntityPlayer player)
	{
		return encode(player.getCommandSenderName());
	}
	
}
