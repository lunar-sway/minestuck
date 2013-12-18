package com.mraof.minestuck.network.skaianet;

import static com.mraof.minestuck.network.skaianet.SkaianetHandler.getAssociatedPartner;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.EnumChatFormatting;

import com.mraof.minestuck.Minestuck;

/**
 * Handles session related stuff like title generation, consort choosing, and other session management stuff.
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
	 * Looks for the session that the player is a part of.
	 * @param player A string of the player's username.
	 * @return A session that contains at least one connection, that the player is a part of.
	 */
	static Session getPlayerSession(String player){
		for(Session s : sessions)
			for(SburbConnection c : s.connections)
				if(c.getClientName().equals(player) || c.getServerName().equals(player))
					return s;
		return null;
	}
	
	static String merge(Session cs, Session ss, SburbConnection sb) {
		String s = canMerge(cs, ss);
		if(s == null) {
			ss.connections.remove(sb);
			cs.connections.addAll(ss.connections);
			if(cs.skaiaId == 0) cs.skaiaId = ss.skaiaId;
			if(cs.prospitId == 0) cs.prospitId = ss.prospitId;
			if(cs.derseId == 0) cs.derseId = ss.derseId;
			return null;
		}
		return s;
	}
	
	static String canMerge(Session s0, Session s1){
		if(s0.getPlayerList().size()+s1.getPlayerList().size()>maxSize)
			return "session.bothSessionsFull";
		return null;
	}
	
	/**
	 * Splits up the main session into small sessions.
	 * Used for the conversion of a global session world to
	 * a non-global session.
	 */
	static void split() {
		if(Minestuck.globalSession || sessions.size() != 1)
			return;
		
		split(sessions.get(0));
	}
	
	static void split(Session session) {
		sessions.remove(session);
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
	
	//Being replaced
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
	
	//Empty
	static void generateTitle(String player) {
		
	}
	
	/**
	 * Will check if two players can connect based on their main connections and sessions.
	 * Does NOT include session size checking.
	 * @return True if client connection is not null and client and server session is the same or 
	 * client connection is null and server connection is null.
	 */
	static boolean canConnect(String client, String server) {
		Session sClient = getPlayerSession(client), sServer = getPlayerSession(server);
		SburbConnection cClient = SkaianetHandler.getConnection(client, SkaianetHandler.getAssociatedPartner(client, true));
		SburbConnection cServer = SkaianetHandler.getConnection(server, SkaianetHandler.getAssociatedPartner(server, false));
		return cClient != null && sClient == sServer || cClient == null && cServer == null;
	}
	
	/**
	 * @return Null if successful or an unlocalized error message describing reason.
	 */
	static String onConnectionCreated(SburbConnection connection) {
		if(!canConnect(connection.getClientName(), connection.getServerName()))
			return "computer.messageConnectFailed";
		if(singleSession) {
			int i = (sessions.get(0).containsPlayer(connection.getClientName())?0:1)+(sessions.get(0).containsPlayer(connection.getServerName())?0:1);
			if(Minestuck.forceMaxSize && sessions.get(0).getPlayerList().size()+i > maxSize)
				return "computer.singleSessionFull";
			else {
				sessions.get(0).connections.add(connection);
				return null;
			}
		} else {
			Session sClient = getPlayerSession(connection.getClientName()), sServer = getPlayerSession(connection.getServerName());
			if(sClient == null && sServer == null) {
				Session s = new Session();
				s.connections.add(connection);
				s.checkIfCompleted();	//In case a player connects to himself.
				return null;
			} else if(sClient == null || sServer == null) {
				if(Minestuck.forceMaxSize && (sClient == null?sServer:sClient).getPlayerList().size()+1 > maxSize)
					return "computer."+(sClient == null?"server":"client")+"SessionFull";
				(sClient == null?sServer:sClient).connections.add(connection);
				return null;
			} else {
				String s = merge(sClient, sServer, connection);
			}
			return null;
		}
	}
	
	/**
	 * @param normal If the connection was closed by normal means. (includes everything but getting crushed by a meteor)
	 */
	static void onConnectionClosed(SburbConnection connection, boolean normal) {
		if(!connection.isMain && !singleSession)
			split(getPlayerSession(connection.getClientName()));
		else if(!normal) {
			if(SkaianetHandler.getAssociatedPartner(connection.getClientName(), false) != null) {
				SburbConnection c = SkaianetHandler.getConnection(SkaianetHandler.getAssociatedPartner(connection.getClientName(), false), connection.getClientName());
				if(c == null) {
					getPlayerSession(connection.getClientName()).connections.remove(connection);
					SkaianetHandler.connections.remove(connection);
				} //else	What should happen with that connection?
			}
		}
	}
	
	static void onFirstItemGiven(SburbConnection connection) {
		
	}
	
	static void onGameEntered(SburbConnection connection) {
		
	}
	
	static void load(NBTTagList list) {
		for(int i = 0; i < list.tagCount(); i++) {
			sessions.add(new Session().read((NBTTagCompound) list.tagAt(i)));
		}
	}
	
	static List getServerList(String client) {
		ArrayList list = new ArrayList();
		for(String server : SkaianetHandler.serversOpen.keySet()) {
			if(canConnect(client, server))
				list.add(server);
		}
		return list;
	}
	
}
