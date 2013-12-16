package com.mraof.minestuck.network.skaianet;

import static com.mraof.minestuck.network.skaianet.SkaianetHandler.getAssociatedPartner;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.EnumChatFormatting;

import com.mraof.minestuck.Minestuck;

/**
 * Handles session related stuff like title generation, consort chooser, and other session management stuff.
 * @author kirderf1
 */
public class SessionHandler {
	
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
		if(!Minestuck.globalSession){
//			split();
			ChatMessageComponent message = new ChatMessageComponent();
			message.addText("[MINESTUCK] Non-global session worlds is currently not yet a finished feature.");
			message.setColor(EnumChatFormatting.YELLOW);
			MinecraftServer.getServer().sendChatToPlayer(message);
			singleSession = true;
		}
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
	 * Called when a new connection is created.
	 * @param c The connection created.
	 * @return 
	 */
	static boolean registerConnection(SburbConnection c){
		if(!canJoin(c.getClientName(), c.getServerName()))
			return false;
		
		Session cs = getPlayerSession(c.getClientName()), ss = getPlayerSession(c.getServerName());
		
		if(cs != null && ss != null && cs != ss){
			cs.connections.add(c);
			ss.connections.add(c);
		} else if(cs != null){
			cs.connections.add(c);
		} else if(ss != null){
			ss.connections.add(c);
		} else if(singleSession && sessions.size() != 0){
			sessions.get(0).connections.add(c);
		} else {
			Session s = new Session();
			s.connections.add(c);
			sessions.add(s);
		}
		return true;
	}
	
	/**
	 * Looks for the session that the player is a part of.
	 * @param player A string of the player's username.
	 * @return A session that contains at least one <b>main</b> connection, that the player is a part of.
	 */
	static Session getPlayerSession(String player){
		for(Session s : sessions)
			for(SburbConnection c : s.connections)
				if(c.isMain && (c.getClientName().equals(player) || c.getServerName().equals(player)))
					return s;
		return null;
	}
	
	/**
	 * Checks if the possible client-server pairing can be created.
	 * @param client A string of the clients name.
	 * @param server A string of the servers name.
	 * @return If they should be able to connect. Includes temporal connections.
	 */
	static boolean canJoin(String client, String server){	//Commented code is for the use of maxSize.
		Session cs = getPlayerSession(client);
		Session ss = getPlayerSession(server);
		if(cs == null && ss == null)
			if(singleSession && sessions.size() > 0){
				Session s = sessions.get(0);
				return true;	//maxSize >= s.getPlayerList().size()+(s.containsPlayer(client)?0:1)+(s.containsPlayer(server)?0:1);
			} else return true;
		
		if(cs == null)
			return true;	//maxSize >= ss.getPlayerList().size()+1;
		if(ss == null)
			return true;	//maxSize >= cs.getPlayerList().size()+1;
		
		if((!getAssociatedPartner(client, true).isEmpty() || !getAssociatedPartner(server, false).isEmpty()) && cs != ss)
			return false;
		if(cs != ss)
			return canMerge(cs, ss);	// && maxSize >= cs.getPlayerList().size()+ss.getPlayerList().size();
		return true;
	}
	
	static Session merge(Session cs, Session ss, SburbConnection sb){
		if(canMerge(cs, ss) && sb != null){
			ss.connections.remove(sb);
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
	 * It returns false if at least one parameter is null.
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
	
	static void closeConnection(String client, String server){
		Iterator<Session> iter0 = sessions.iterator();
		while(iter0.hasNext()){
			Session s = iter0.next();
			Iterator<SburbConnection> iter1 = s.connections.iterator();
			while(iter1.hasNext()){
				SburbConnection c = iter1.next();
				if(c.getClientName().equals(client) && c.getServerName().equals(server)){
					if(c.isMain){
						c.isActive = false;
						return;
					} else iter1.remove();
					break;
				}
			}
			if(s.connections.size() == 0)
				iter0.remove();
		}
	}
	
}
