package com.mraof.minestuck.network.skaianet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.mraof.minestuck.Minestuck;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class Session {
	
	/**
	 * The max numbers of players per session.
	 */
	public static int maxSize;
	
	/**
	 * If the current Minecraft world will act as if Minestuck.globalSession is true or not.
	 * Will be for example false even if Minestuck.globalSession is true if it can't merge all
	 * sessions into a single session.
	 */
	public static boolean singleSession;
	
	/**
	 * An array list of the current worlds sessions.
	 */
	static List<Session> sessions = new ArrayList();
	
	/**
	 * Called when the server loads a new world, after
	 * Minestuck has loaded the sessions from file.
	 */
	public static void serverStarted() {
		singleSession = Minestuck.globalSession;
		if(!Minestuck.globalSession)
			split();
		else mergeAll();
	}
	
	/**
	 * Merges all available sessions into one if it can.
	 * Used in the conversion of a non-global session world
	 * to a global session world.
	 */
	static void mergeAll() {
		if(!canMergeAll() || sessions.size() < 2){
			singleSession = sessions.size() < 2;
			return;
		}
		
		Session session = sessions.get(0);
		for(int i = 1; i < sessions.size(); i++){
			Session s = sessions.get(i);
			session.connections.addAll(s.connections);
			if(s.skaiaId != 0) session.skaiaId = s.skaiaId;
			if(s.prospitId != 0) session.prospitId = s.prospitId;
			if(s.derseId != 0) session.derseId = s.derseId;
		}
		session.completed = false;
	}
	
	/**
	 * Checks if it can merge all sessions in the current world into one.
	 * @return False if all registered players is more than maxSize, or if there exists more
	 * than one skaia, prospit, or derse dimension.
	 */
	static boolean canMergeAll() {
		int players = 0;
		boolean skaiaUsed = false, prospitUsed = false, derseUsed = false;
		for(Session s : sessions) {
			if(s.skaiaId != 0)
				if(skaiaUsed) return false;
				else skaiaUsed = true;
			if(s.prospitId != 0)
				if(prospitUsed) return false;
				else prospitUsed = true;
			if(s.derseId != 0)
				if(derseUsed) return false;
				else derseUsed = true;
			players += s.getPlayerList().size();
		}
		if(players > maxSize)
			return false;
		else return true;
	}
	
	/**
	 * UNFINISHED
	 * Called when a new main connection is created.
	 * @param c The connection created.
	 */
	static void registerConnection(SburbConnection c){
		if(!canJoin(c.getClientName(), c.getServerName()))
			return;
		
	}
	
	/**
	 * Checks if the possible client-server pairing can be created.
	 * @param client A string of the clients name.
	 * @param server A string of the servers name.
	 * @return If they should be able to connect. Includes temporal connections.
	 */
	static boolean canJoin(String client, String server){
		Session cs = null;
		Session ss = null;
		for(Session s : sessions){
			for(SburbConnection c : s.connections){
				if(c.getClientName().equals(client) || c.getServerName().equals(server))
					return false;
				if(c.getClientName().equals(server))
					ss = s;
				if(c.getServerName().equals(client))
					cs = s;
			}
		}
		
		if(cs != null && ss != null)
			if(cs == ss)
				return true;
			else return canMerge(cs, ss);
		
		return true;
	}
	
	static Session merge(Session cs, Session ss, SburbConnection sb){
		if(canMerge(cs, ss) && sb != null && cs.containsPlayer(sb.getClientName(), false) && ss.containsPlayer(sb.getServerName(), true)){
			cs.connections.add(sb);
			cs.connections.addAll(ss.connections);
			if(cs.skaiaId == 0) cs.skaiaId = ss.skaiaId;
			if(cs.prospitId == 0) cs.prospitId = ss.prospitId;
			if(cs.derseId == 0) cs.derseId = ss.derseId;
			return cs;
		}
		return null;
	}
	
	/**
	 * If it can merge two sessions together.
	 * @param s0 A session.
	 * @param s1 A second session.
	 * @return If they can be merged.
	 */
	static boolean canMerge(Session s0, Session s1){
		return (s0 != null && s1 != null && s0.getPlayerList().size()+s1.getPlayerList().size()<=maxSize && !s0.completed && !s1.completed &&
				(s0.skaiaId == 0 || s1.skaiaId == 0) && (s0.prospitId == 0 || s1.prospitId == 0) && (s0.derseId == 0 || s1.derseId == 0));
	}
	
	/**
	 * Splits up the main session into small sessions.
	 * Used for the conversion of a global session world to
	 * a non-global session.
	 */
	static void split() {
		if(Minestuck.globalSession || sessions.size() != 1)
			return;
		
		Session session = sessions.remove(0);
		boolean first = true;
		while(!session.connections.isEmpty()){
			Session s = new Session();
			if(!first) s.connections.add(session.connections.remove(0));
			else {
				s.skaiaId = session.skaiaId;
				s.prospitId = session.prospitId;
				s.derseId = session.derseId;
			}
			boolean found;
			do {
				found = false;
				Iterator<SburbConnection> iter = session.connections.iterator();
				while(iter.hasNext()){
					SburbConnection c = iter.next();
					if(s.containsPlayer(c.getClientName()) || s.containsPlayer(c.getServerName()) || first && !c.canSplit){
						found = true;
						iter.remove();
						s.connections.add(c);
					}
				}
			} while(found);
			s.checkIfCompleted();
			sessions.add(s);
			first = false;
		}
	}
	
	//Non-static stuff
	
	/**
	 * Will only store main connections.
	 */
	List<SburbConnection> connections;
	
	/**
	 * If the "connection circle" is whole, unused if globalSession == true.
	 */
	boolean completed;
	
	//Unused, will later be 0 if not yet generated
	int skaiaId;
	int prospitId;
	int derseId;
	
	void checkIfCompleted(){
		if(connections.isEmpty()){
			completed = false;
			return;
		}
		String start = connections.get(0).getClientName();
		String current = start;
		while(true){
			for(SburbConnection c : connections)
				if(c.getServerName().equals(current)){
					current = c.getClientName();
					if(start.equals(current)){
						completed = true;
						return;
					} else continue;
				}
			completed = false;
			return;
		}
	}
	
	Session(){
		connections = new ArrayList<SburbConnection>();
	}
	
	boolean containsPlayer(String s){
		for(SburbConnection c : connections)
			if(s.equals(c.getClientName()) || s.equals(c.getServerName()))
				return true;
		return false;
	}
	
	boolean containsPlayer(String s, boolean isClient){
		for(SburbConnection c : connections)
			if(s.equals(isClient?c.getClientName():c.getServerName()))
				return true;
		return false;
	}
	
	List<String> getPlayerList(){
		List<String> list = new ArrayList();
		for(SburbConnection c : this.connections){
			if(!list.contains(c.getClientName()))
				list.add(c.getClientName());
			if(!list.contains(c.getServerName()))
				list.add(c.getServerName());
		}
		return list;
	}
	
	NBTTagCompound write() {
		NBTTagCompound nbt = new NBTTagCompound();
		NBTTagList list = new NBTTagList();
		for(SburbConnection c : connections)
			list.appendTag(c.write());
		nbt.setTag("connections", list);
		nbt.setInteger("skaiaId", skaiaId);
		nbt.setInteger("derseId", derseId);
		nbt.setInteger("prospitId", prospitId);
		nbt.setBoolean("completed", completed);
		return nbt;
	}
	
	Session read(NBTTagCompound nbt) {
		NBTTagList list = nbt.getTagList("connections");
		for(int i = 0; i < list.tagCount(); i++)
			connections.add(new SburbConnection().read((NBTTagCompound) list.tagAt(i)));
		
		return this;
	}
	
}
