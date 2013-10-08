package com.mraof.minestuck.skaianet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Session {
	
	public static int maxSize;
	
	static List<Session> sessions = new ArrayList();
	
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
		}
		return null;
	}
	
	static boolean canMerge(Session cs, Session ss){
		return (cs != null && ss != null && cs.getPlayerList().size()+ss.getPlayerList().size()<=maxSize && !cs.completed && !ss.completed);
	}
	
	static Session load(DataInputStream stream) throws IOException {
		Session s = new Session();
		byte size = stream.readByte();
		s.completed = stream.readBoolean();
		s.skaiaId = stream.readByte();
		s.prospitId = stream.readByte();
		s.derseId = stream.readByte();
		for(int i = 0; i < size; i++)
			s.connections.add(SburbConnection.load(stream));
		return s;
	}
	
	
	//Non-static stuff
	
	void save(DataOutputStream stream) throws IOException {
		stream.writeByte(this.connections.size());
		stream.writeBoolean(this.completed);
		stream.writeByte(this.skaiaId);
		stream.writeByte(this.prospitId);
		stream.writeByte(this.derseId);
//		for(SburbConnection c : this.connections)
//			c.save(stream);
	}
	
	List<SburbConnection> connections;
	boolean completed;	//If the "connection circle" is whole, unused if globalSession == true.
	
	//Unused, will later be 0 if not yet generated
	int skaiaId;
	int prospitId;
	int derseId;
	
	Session(){
		connections = new ArrayList<SburbConnection>();
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
	
}
