package com.mraof.minestuck.skaianet;

import java.util.ArrayList;
import java.util.List;

public class Session {
	
	public static int maxSize;
	
	static boolean canJoin(String client, String server){
		Session cs = null;
		Session ss = null;
		for(Session s : sessions){
			for(SburbConnection c : s.players){
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
	
	static List<Session> sessions = new ArrayList();
	
	static Session merge(Session cs, Session ss, SburbConnection sb){
		if(canMerge(cs, ss) && sb != null){
			
		}
		return null;
	}
	
	static boolean canMerge(Session cs, Session ss){
		if(cs != null && ss != null){
			
		}
		return false;
	}
	
	//Non-static stuff
	
	List<SburbConnection> players;
	boolean completed;
	
	int size;
	
	//Unused
	int skaiaId;
	int prospitId;
	int derseId;
}
