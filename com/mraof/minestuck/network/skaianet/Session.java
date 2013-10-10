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
	
	public static int maxSize;
	
	static List<Session> sessions = new ArrayList();
	
	public static void serverStarted() {
		if(!Minestuck.globalSession)
			split();
		else mergeAll();
	}
	
	static void mergeAll() {
		if(!canMergeAll() || sessions.size() < 2)
			return;
		
	}
	
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
	
	static void registerConnection(SburbConnection c){
		if(!canJoin(c.getClientName(), c.getServerName()))
			return;
		
	}
	
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
			return canMerge(cs, ss);
		
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
	
	static boolean canMerge(Session cs, Session ss){
		return (cs != null && ss != null && cs.getPlayerList().size()+ss.getPlayerList().size()<=maxSize && !cs.completed && !ss.completed &&
				(cs.skaiaId == 0 || ss.skaiaId == 0) && (cs.prospitId == 0 || ss.prospitId == 0) && (cs.derseId == 0 || ss.derseId == 0));
	}
	
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
	
	List<SburbConnection> connections;
	boolean completed;	//If the "connection circle" is whole, unused if globalSession == true.
	
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
