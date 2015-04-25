package com.mraof.minestuck.network.skaianet;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase.SpawnListEntry;
import net.minecraftforge.common.DimensionManager;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.tracker.MinestuckPlayerTracker;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.EnumAspect;
import com.mraof.minestuck.util.EnumClass;
import com.mraof.minestuck.util.MinestuckPlayerData;
import com.mraof.minestuck.util.Title;
import com.mraof.minestuck.util.TitleHelper;
import com.mraof.minestuck.util.UsernameHandler;
import com.mraof.minestuck.world.MinestuckDimensionHandler;
import com.mraof.minestuck.world.lands.LandAspectRegistry;
import com.mraof.minestuck.world.lands.LandAspectRegistry.AspectCombination;
import com.mraof.minestuck.world.lands.terrain.TerrainAspect;
import com.mraof.minestuck.world.lands.title.TitleAspect;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.entity.underling.EntityBasilisk;
import com.mraof.minestuck.entity.underling.EntityGiclops;
import com.mraof.minestuck.entity.underling.EntityImp;
import com.mraof.minestuck.entity.underling.EntityOgre;
import com.mraof.minestuck.entity.underling.EntityUnderling;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.GristHelper;
import com.mraof.minestuck.util.GristType;

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
	static List<Session> sessions = new ArrayList<Session>();
	
	/**
	 * Called when the server loads a new world, after
	 * Minestuck has loaded the sessions from file.
	 */
	public static void serverStarted() {
		singleSession = MinestuckConfig.globalSession;
		if(!MinestuckConfig.globalSession) {
			split();
		} else {
			mergeAll();
			if(sessions.size() == 0)
				sessions.add(new Session());
		}
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
			Session s = sessions.remove(i);
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
			sessions.remove(ss);
			cs.connections.add(sb);
			cs.connections.addAll(ss.connections);
			if(cs.skaiaId == 0) cs.skaiaId = ss.skaiaId;
			if(cs.prospitId == 0) cs.prospitId = ss.prospitId;
			if(cs.derseId == 0) cs.derseId = ss.derseId;
		}
		return s;
	}
	
	static String canMerge(Session s0, Session s1){
		if(MinestuckConfig.forceMaxSize && s0.getPlayerList().size()+s1.getPlayerList().size()>maxSize)
			return "session.bothSessionsFull";
		return null;
	}
	
	/**
	 * Splits up the main session into small sessions.
	 * Used for the conversion of a global session world to
	 * a non-global session.
	 */
	static void split() {
		if(MinestuckConfig.globalSession || sessions.size() != 1)
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
			if(s.connections.size() > 0)
				sessions.add(s);
			first = false;
		}
	}
	
	static void generateTitle(String player)
	{
		Session session = getPlayerSession(player);
		Random rand = new Random(Minestuck.worldSeed^player.hashCode());
		ArrayList<Title> usedTitles = new ArrayList<Title>();
		for(SburbConnection c : session.connections)
			if(!c.getClientName().equals(player) && c.enteredGame)
				usedTitles.add(MinestuckPlayerData.getTitle(c.getClientName()));
		
		Title title;
		if(usedTitles.size() < 12)	//Focus on getting an unused aspect and an unused class
		{
			EnumSet<EnumClass> usedClasses = EnumSet.noneOf(EnumClass.class);
			EnumSet<EnumAspect> usedAspects = EnumSet.noneOf(EnumAspect.class);
			for(Title usedTitle : usedTitles)
			{
				usedClasses.add(usedTitle.getHeroClass());
				usedAspects.add(usedTitle.getHeroAspect());
			}
			title = new Title(EnumClass.getRandomClass(usedClasses, rand), EnumAspect.RAGE/*getRandomAspect(usedAspects, rand)*/);
		}
		else	//Focus only on getting an unused title
		{
			int[] classFrequency = new int[12];
			for(Title usedTitle : usedTitles)
				classFrequency[TitleHelper.getIntFromClass(usedTitle.getHeroClass())]++;
			int titleIndex = rand.nextInt(144 - usedTitles.size());	//An identifier to identify which one of the (144 - usedTitles.size()) available titles that'll be given.
			
			EnumClass titleClass = null;
			for(int classIndex = 0; classIndex < 12; classIndex++)	//The class is extracted from the titleIndex in this for loop. (and preparing the index for retrieval of the aspect)
			{
				int classChance = 12 - classFrequency[classIndex];
				if(titleIndex <= classChance)
				{
					titleClass = TitleHelper.getClassFromInt(classIndex);
					break;
				}
				titleIndex -= classChance;
			}
			
			EnumSet<EnumAspect> usedAspects = EnumSet.noneOf(EnumAspect.class);
			for(Title usedTitle : usedTitles)
				if(usedTitle.getHeroClass() == titleClass)
					usedAspects.add(usedTitle.getHeroAspect());
			EnumAspect titleAspect = null;
			for(EnumAspect aspect : EnumAspect.values())
				if(!usedAspects.contains(aspect))
				{
					if(titleIndex == 0)
					{
						titleAspect = aspect;
						break;
					}
					titleIndex--;
				}
			title = new Title(titleClass, titleAspect);
		}
		
		MinestuckPlayerData.setTitle(player, title);
		MinestuckPlayerTracker.instance.updateTitle(MinecraftServer.getServer().getConfigurationManager().getPlayerByUsername(UsernameHandler.decode(player)));
	}
	
	/**
	 * @param player The username of the player, encoded.
	 * @return Damage value for the entry item
	 */
	public static int getEntryItem(String player) {
		return 0;
	}
	
	/**
	 * 
	 * @param client The username of the player, encoded.
	 */
	public static int availableTier(String client) {
		Session s = getPlayerSession(client);
		if(s == null) {
			return -1;
		}
		if(s.completed)
			return Integer.MAX_VALUE;
		SburbConnection c = SkaianetHandler.getClientConnection(client);
		int count = -1;
		for(SburbConnection conn : s.connections)
			if(conn.enteredGame)
				count++;
		if(!c.enteredGame)
			count++;
		return count;
	}
	
	private static void genLandAspects(SburbConnection connection)
	{
		LandAspectRegistry aspectGen = new LandAspectRegistry(Minestuck.worldSeed/connection.clientHomeLand);
		Session session = getPlayerSession(connection.getClientName());
		Title title = MinestuckPlayerData.getTitle(connection.getClientName());
		
		boolean frogs = false;
		ArrayList<TerrainAspect> usedTerrainAspects = new ArrayList<TerrainAspect>();
		ArrayList<TitleAspect> usedTitleAspects = new ArrayList<TitleAspect>();
		for(SburbConnection c : session.connections)
			if(c.enteredGame)
			{
				if(c == connection)
					continue;
				LandAspectRegistry.AspectCombination aspects = MinestuckDimensionHandler.getAspects(c.clientHomeLand);
				if(aspects.aspectTitle == LandAspectRegistry.frogAspect)
					frogs = true;
				else if(MinestuckPlayerData.getTitle(c.getClientName()).getHeroAspect() == title.getHeroAspect())
					usedTitleAspects.add(aspects.aspectTitle);
				usedTerrainAspects.add(aspects.aspectTerrain);
			}
		
//		if(title.getHeroAspect() == EnumAspect.SPACE && !frogs)
//			return landHelper.frogAspect;
		TitleAspect titleAspect = aspectGen.getTitleAspect(title.getHeroAspect(), usedTitleAspects);
		TerrainAspect terrainAspect = aspectGen.getLandAspect(titleAspect, usedTerrainAspects);
		MinestuckDimensionHandler.registerLandDimension(connection.clientHomeLand, new AspectCombination(terrainAspect, titleAspect));
		//MinestuckPlayerTracker.updateLands(); Lands need to be updated after setting the spawnpoint
	}
	
	public static GristType getUnderlingType(EntityUnderling entity)
	{
		return GristHelper.getPrimaryGrist();
	}
	
	private static List<SpawnListEntry>[] difficultyList = new ArrayList[31];
	
	public static List<SpawnListEntry> getUnderlingList(BlockPos pos, World world)
	{
		
		BlockPos spawn = world.getSpawnPoint();
		
		int difficulty = (int) Math.round(Math.pow(new Vec3i(pos.getX() >> 4, 0, pos.getZ() >> 4).distanceSq(new Vec3i(spawn.getX() >> 4, 0, spawn.getZ() >> 4)), 0.5));
		
		difficulty = Math.min(30, difficulty/2);
		
		if(difficultyList[difficulty] != null)
			return difficultyList[difficulty];
		
		ArrayList<SpawnListEntry> list = new ArrayList<SpawnListEntry>();
		
		int impWeight = 0, ogreWeight = 0, basiliskWeight = 0, giclopsWeight = 0;
		
		if(difficulty < 8)
			impWeight = difficulty + 1;
		else
		{
			impWeight = 8 - (difficulty - 8)/3;
			if(difficulty < 20)
				ogreWeight = (difficulty - 5)/3;
			else ogreWeight = 5 - (difficulty - 20)/3;
			
			if(difficulty >= 16)
			{
				if(difficulty < 26)
					basiliskWeight = (difficulty - 14)/2;
				else basiliskWeight = 6;
				if(difficulty >= 20)
					if(difficulty < 30)
						giclopsWeight = (difficulty - 17)/3;
					else giclopsWeight = 5;
			}
		}
		
		if(impWeight > 0)
			list.add(new SpawnListEntry(EntityImp.class, impWeight, Math.max(1, (int)(impWeight/2.5)), Math.max(3, impWeight)));
		if(ogreWeight > 0)
			list.add(new SpawnListEntry(EntityOgre.class, ogreWeight, ogreWeight >= 5 ? 2 : 1, Math.max(1, ogreWeight/2)));
		if(basiliskWeight > 0)
			list.add(new SpawnListEntry(EntityBasilisk.class, basiliskWeight, 1, Math.max(1, basiliskWeight/2)));
		if(giclopsWeight > 0)
			list.add(new SpawnListEntry(EntityGiclops.class, giclopsWeight, 1, Math.max(1, giclopsWeight/2)));
		
		difficultyList[difficulty] = list;
		
		return list;
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
			if(sessions.size() == 0)
				return "computer.messageConnectFailed";
			int i = (sessions.get(0).containsPlayer(connection.getClientName())?0:1)+(sessions.get(0).containsPlayer(connection.getServerName())?0:1);
			if(MinestuckConfig.forceMaxSize && sessions.get(0).getPlayerList().size()+i > maxSize)
				return "computer.singleSessionFull";
			else {
				sessions.get(0).connections.add(connection);
				return null;
			}
		} else {
			Session sClient = getPlayerSession(connection.getClientName()), sServer = getPlayerSession(connection.getServerName());
			if(sClient == null && sServer == null) {
				Session s = new Session();
				sessions.add(s);
				s.connections.add(connection);
				return null;
			} else if(sClient == null || sServer == null) {
				if(MinestuckConfig.forceMaxSize && (sClient == null?sServer:sClient).getPlayerList().size()+1 > maxSize)
					return "computer."+(sClient == null?"server":"client")+"SessionFull";
				(sClient == null?sServer:sClient).connections.add(connection);
				return null;
			} else {
				if(sClient == sServer) {
					sClient.connections.add(connection);
					return null;
				}
				else return merge(sClient, sServer, connection);
			}
		}
	}
	
	/**
	 * @param normal If the connection was closed by normal means.
	 * (includes everything but getting crushed by a meteor and other reasons for removal of a main connection)
	 */
	static void onConnectionClosed(SburbConnection connection, boolean normal) {
		Session s = getPlayerSession(connection.getClientName());
		
		if(!connection.isMain && !singleSession) {
			s.connections.remove(connection);
			if(s.connections.size() == 0)
				sessions.remove(s);
			else split(s);
		} else if(!normal) {
			s.connections.remove(connection);
			if(!SkaianetHandler.getAssociatedPartner(connection.getClientName(), false).isEmpty() && !connection.getServerName().equals(".null")) {
				SburbConnection c = SkaianetHandler.getConnection(SkaianetHandler.getAssociatedPartner(connection.getClientName(), false), connection.getClientName());
				if(c.isActive)
					SkaianetHandler.closeConnection(c.getClientName(), c.getServerName(), true);
				switch(MinestuckConfig.escapeFailureMode) {
				case 0:
					c.serverName = connection.getServerName();
					break;
				case 1:
					c.serverName = ".null";
					break;
				}
			}
			if(s.connections.size() == 0)
				sessions.remove(s);
		}
	}
	
	static void onFirstItemGiven(SburbConnection connection) {
		
	}
	
	static void onGameEntered(SburbConnection connection) {
		generateTitle(connection.getClientName());
		getPlayerSession(connection.getClientName()).checkIfCompleted();
		genLandAspects(connection);
		
		
	}
	
	static List<String> getServerList(String client) {
		ArrayList<String> list = new ArrayList<String>();
		for(String server : SkaianetHandler.serversOpen.keySet()) {
			if(canConnect(client, server))
				list.add(server);
		}
		return list;
	}
	
}
