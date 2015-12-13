package com.mraof.minestuck.network.skaianet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

/**
 * Was also an interface for the session system, but now just a data structure representing a session.
 * SessionHandler is the new class for session interface.
 * @author kirderf1
 */
public class Session {
	
	Map<String, PredefineData> predefinedPlayers;
	List<SburbConnection> connections;
	String name;
	
	/**
	 * If the "connection circle" is whole, unused if globalSession == true.
	 */
	boolean completed;
	boolean locked;
	
	//Unused, will later be 0 if not yet generated
	int skaiaId;
	int prospitId;
	int derseId;
	
	/**
	 * Checks if the variable completed should be true or false.
	 */
	void checkIfCompleted(){
		if(connections.isEmpty() || SessionHandler.singleSession){
			completed = false;
			return;
		}
		String start = connections.get(0).getClientName();
		String current = start;
		main: while(true){
			for(SburbConnection c : connections) {
				if(!c.enteredGame) {
					completed = false;
					return;
				}
				if(c.getServerName().equals(current)) {
					current = c.getClientName();
					if(start.equals(current)) {
						completed = true;
						return;
					} else continue main;
				}
			}
			completed = false;
			return;
		}
	}
	
	Session(){
		connections = new ArrayList<SburbConnection>();
		predefinedPlayers = new HashMap<String, PredefineData>();
	}
	
	/**
	 * Checks if a certain player is in the connection list.
	 * @param player The username of the player.
	 * @return If the player was found.
	 */
	boolean containsPlayer(String player)
	{
		if(predefinedPlayers.containsKey(player))
			return true;
		for(SburbConnection c : connections)
			if(c.getClientName().equals(player) || c.getServerName().equals(player))
				return true;
		return false;
	}
	
	/**
	 * Creates a list with all players in the session.
	 * @return Returns a list with the players usernames.
	 */
	Set<String> getPlayerList()
	{
		Set<String> list = new HashSet<String>();
		for(SburbConnection c : this.connections)
		{
			list.add(c.getClientName());
			if(!c.getServerName().equals(".null"))
				list.add(c.getServerName());
		}
		list.addAll(predefinedPlayers.keySet());
		return list;
	}
	
	/**
	 * Writes this session to an nbt tag.
	 * Note that this will only work as long as <code>SkaianetHandler.connections</code> remains unmodified.
	 * @return An NBTTagCompound representing this session.
	 */
	NBTTagCompound write()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		
		if(isCustom())
			nbt.setString("name", name);
		NBTTagList list = new NBTTagList();
		for(SburbConnection c : connections)
			list.appendTag(c.write());
		nbt.setTag("connections", list);
		NBTTagCompound predefineTag = new NBTTagCompound();
		for(Map.Entry<String, PredefineData> entry : predefinedPlayers.entrySet())
			predefineTag.setTag(entry.getKey(), entry.getValue().write());
		nbt.setTag("predefinedPlayers", predefineTag);
		nbt.setBoolean("locked", locked);
		nbt.setInteger("skaiaId", skaiaId);
		nbt.setInteger("derseId", derseId);
		nbt.setInteger("prospitId", prospitId);
		return nbt;
	}
	
	/**
	 * Reads data from the given nbt tag.
	 * @param nbt An NBTTagCompound to read from.
	 * @return This.
	 */
	Session read(NBTTagCompound nbt)
	{
		if(nbt.hasKey("name", 8))
			name = nbt.getString("name");
		else name = null;
		NBTTagList list = nbt.getTagList("connections", 10);
		for(int i = 0; i < list.tagCount(); i++)
			connections.add(new SburbConnection().read(list.getCompoundTagAt(i)));
		NBTTagCompound predefineTag = nbt.getCompoundTag("predefinedPlayers");
		for(String player : (Set<String>) predefineTag.getKeySet())
			predefinedPlayers.put(player, new PredefineData().read(predefineTag.getCompoundTag(player)));
		SkaianetHandler.connections.addAll(this.connections);
		locked = nbt.getBoolean("locked");
		
		checkIfCompleted();
		return this;
	}
	
	SburbConnection getConnection(String client, String server){
		for(SburbConnection c : connections)
			if(c.getClientName().equals(client) && c.getServerName().equals(server))
				return c;
		return null;
	}
	
	/**
	 * With being custom,
	 */
	boolean isCustom()
	{
		return name != null;
	}
}