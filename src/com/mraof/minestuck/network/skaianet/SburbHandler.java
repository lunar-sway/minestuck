package com.mraof.minestuck.network.skaianet;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase.SpawnListEntry;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.entity.underling.EntityBasilisk;
import com.mraof.minestuck.entity.underling.EntityGiclops;
import com.mraof.minestuck.entity.underling.EntityImp;
import com.mraof.minestuck.entity.underling.EntityOgre;
import com.mraof.minestuck.entity.underling.EntityUnderling;
import com.mraof.minestuck.tracker.MinestuckPlayerTracker;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.EnumAspect;
import com.mraof.minestuck.util.EnumClass;
import com.mraof.minestuck.util.GristHelper;
import com.mraof.minestuck.util.GristType;
import com.mraof.minestuck.util.MinestuckPlayerData;
import com.mraof.minestuck.util.Title;
import com.mraof.minestuck.util.UsernameHandler;
import com.mraof.minestuck.world.MinestuckDimensionHandler;
import com.mraof.minestuck.world.lands.LandAspectRegistry;
import com.mraof.minestuck.world.lands.LandAspectRegistry.AspectCombination;
import com.mraof.minestuck.world.lands.terrain.TerrainLandAspect;
import com.mraof.minestuck.world.lands.title.TitleLandAspect;

/**
 * A class for managing sbrub-related stuff from outside this package that is dependent on connections and sessions.
 * For example: Titles, land aspects, underling grist types, entry items etc.
 * @author kirderf1
 */
public class SburbHandler
{
	
	static void generateTitle(String player)
	{
		Session session = SessionHandler.getPlayerSession(player);
		EnumClass titleClass = null;
		EnumAspect titleAspect = null;
		if(session.predefinedPlayers.containsKey(player))
		{
			PredefineData data = session.predefinedPlayers.get(player);
			if(data.titleClass != null)
				titleClass = data.titleClass;
			if(data.titleAspect != null)
				titleAspect = data.titleAspect;
		}
		
		if(titleClass == null || titleAspect == null)
		{
			Random rand = new Random(Minestuck.worldSeed^player.hashCode());
			
			ArrayList<Title> usedTitles = new ArrayList<Title>();
			Set<String> playersEntered = new HashSet<String>();	//Used to avoid duplicates from both connections and predefined data
			playersEntered.add(player);
			for(SburbConnection c : session.connections)
				if(!c.getClientName().equals(player) && c.enteredGame)
				{
					usedTitles.add(MinestuckPlayerData.getTitle(c.getClientName()));
					playersEntered.add(c.getClientName());
				}
			
			for(Map.Entry<String, PredefineData> entry : session.predefinedPlayers.entrySet())
				if(!playersEntered.contains(entry.getKey()) && (entry.getValue().titleClass != null || entry.getValue().titleAspect != null))
					usedTitles.add(new Title(entry.getValue().titleClass, entry.getValue().titleAspect));
			
			if(usedTitles.size() < 12)	//Focus on getting an unused aspect and an unused class
			{
				if(titleClass == null)
				{
					EnumSet<EnumClass> usedClasses = EnumSet.noneOf(EnumClass.class);
					for(Title usedTitle : usedTitles)
						if(usedTitle.getHeroClass() != null)
							usedClasses.add(usedTitle.getHeroClass());
					
					titleClass = EnumClass.getRandomClass(usedClasses, rand);
				}
				if(titleAspect == null)
				{
					EnumSet<EnumAspect> usedAspects = EnumSet.noneOf(EnumAspect.class);
					for(Title usedTitle : usedTitles)
						if(usedTitle.getHeroAspect() != null)
							usedAspects.add(usedTitle.getHeroAspect());
					
					titleAspect = EnumAspect.getRandomAspect(usedAspects, rand);
				}
			}
			else if(usedTitles.size() < 144)	//Focus only on getting an unused title
			{
				if(titleClass == null)
				{
					int[] classFrequency = new int[12];
					int count = 0;
					for(Title usedTitle : usedTitles)
						if(usedTitle.getHeroClass() != null && usedTitle.getHeroClass().ordinal() < 12 && classFrequency[usedTitle.getHeroClass().ordinal()] != -1)
						{
							if(titleAspect == null || !titleAspect.equals(usedTitle.getHeroAspect()))
							{
								classFrequency[usedTitle.getHeroClass().ordinal()]++;
								count++;
							} else
							{
								count += 12 - classFrequency[usedTitle.getHeroClass().ordinal()];
								classFrequency[usedTitle.getHeroClass().ordinal()] = -1;
							}
						}
					
					int titleIndex = rand.nextInt(144 - count);
					
					for(int classIndex = 0; classIndex < 12; classIndex++)
					{
						if(classFrequency[classIndex] == -1)
							continue;
						int classChance = 12 - classFrequency[classIndex];
						if(titleIndex <= classChance)
						{
							titleClass = EnumClass.getClassFromInt(classIndex);
							break;
						}
						titleIndex -= classChance;
					}
					if(titleClass == null)
						throw new IllegalStateException("Finished for loop without generating a title class. This should not happen and is likely a bug.");
					
				} else if(titleAspect == null)
				{
					int[] aspectFrequency = new int[12];
					int count = 0;
					for(Title usedTitle : usedTitles)
						if(usedTitle.getHeroAspect() != null && aspectFrequency[usedTitle.getHeroAspect().ordinal()] != -1)
						{
							if(!titleClass.equals(usedTitle.getHeroClass()))
							{
								aspectFrequency[usedTitle.getHeroAspect().ordinal()]++;
								count++;
							} else
							{
								count += 12 - aspectFrequency[usedTitle.getHeroAspect().ordinal()];
								aspectFrequency[usedTitle.getHeroAspect().ordinal()] = -1;
							}
						}
					
					int titleIndex = rand.nextInt(144 - count);
					
					for(int aspectIndex = 0; aspectIndex < 12; aspectIndex++)
					{
						if(aspectFrequency[aspectIndex] == -1)
							continue;
						int aspectChance = 12 - aspectFrequency[aspectIndex];
						if(titleIndex <= aspectChance)
						{
							titleAspect = EnumAspect.getAspectFromInt(aspectIndex);
							break;
						}
						titleIndex -= aspectChance;
					}
					if(titleAspect == null)
						throw new IllegalStateException("Finished for loop without generating a title aspect. This should not happen and is likely a bug.");
				}
			}
		}
		
		MinestuckPlayerData.setTitle(player, new Title(titleClass, titleAspect));
		MinestuckPlayerTracker.instance.updateTitle(MinecraftServer.getServer().getConfigurationManager().getPlayerByUsername(UsernameHandler.decode(player)));
	}
	
	/**
	 * @param player The username of the player, encoded.
	 * @return Damage value for the entry item
	 */
	public static int getEntryItem(String player)
	{
		int colorIndex = MinestuckPlayerData.getData(player).color;
		return colorIndex + 1;
	}
	
	public static int getColorForDimension(int dim)
	{
		SburbConnection c = getConnectionForDimension(dim);
		return c == null ? -1 : MinestuckPlayerData.getData(c.getClientName()).color;
	}
	
	public static SburbConnection getConnectionForDimension(int dim)
	{
		for(SburbConnection c : SkaianetHandler.connections)
			if(c.enteredGame && c.clientHomeLand == dim)
				return c;
		return null;
	}
	
	/**
	 * 
	 * @param client The username of the player, encoded.
	 */
	public static int availableTier(String client)
	{
		Session s = SessionHandler.getPlayerSession(client);
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
		Session session = SessionHandler.getPlayerSession(connection.getClientName());
		Title title = MinestuckPlayerData.getTitle(connection.getClientName());
		Debug.printf("aspectGen: " + aspectGen + " session: " + session + " title " + title);
		
		boolean frogs = false;
		ArrayList<TerrainLandAspect> usedTerrainAspects = new ArrayList<TerrainLandAspect>();
		ArrayList<TitleLandAspect> usedTitleAspects = new ArrayList<TitleLandAspect>();
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
		TitleLandAspect titleAspect = aspectGen.getTitleAspect(title.getHeroAspect(), usedTitleAspects);
		TerrainLandAspect terrainAspect = aspectGen.getLandAspect(titleAspect, usedTerrainAspects);
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
		
		int difficulty = (int) Math.round(Math.sqrt(new Vec3i(pos.getX() >> 4, 0, pos.getZ() >> 4).distanceSq(new Vec3i(spawn.getX() >> 4, 0, spawn.getZ() >> 4))));
		
		difficulty = Math.min(30, difficulty/3);
		
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
	
	static void onFirstItemGiven(SburbConnection connection) {
		
	}
	
	static void onGameEntered(SburbConnection connection) {
		generateTitle(connection.getClientName());
		SessionHandler.getPlayerSession(connection.getClientName()).checkIfCompleted();
		genLandAspects(connection);
		
		
	}
	
	public static boolean canSelectColor(EntityPlayerMP player)
	{
		String name = UsernameHandler.encode(player.getCommandSenderName());
		for(SburbConnection c : SkaianetHandler.connections)
			if(c.getClientName().equals(name))
				return false;
		return true;
	}
	
}