package com.mraof.minestuck.alchemy;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.api.alchemy.*;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.network.TorrentPackets;
import com.mraof.minestuck.player.*;
import com.mraof.minestuck.skaianet.SburbPlayerData;
import com.mraof.minestuck.skaianet.SessionHandler;
import com.mraof.minestuck.util.MSAttachments;
import com.mraof.minestuck.world.storage.MSExtraData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class TorrentHelper
{
	public static TorrentSession createPlayerTorrentSession(ServerPlayer player, MinecraftServer server)
	{
		return createPlayerTorrentSession(IdentifierHandler.encode(player), server);
	}
	
	public static TorrentSession createPlayerTorrentSession(PlayerIdentifier player, MinecraftServer server)
	{
		List<GristType> seeding = new ArrayList<>();
		
		if(MinestuckConfig.SERVER.gristTorrentSeedAll.get())
			seeding.addAll(GristTypes.REGISTRY.stream().toList());
		
		TorrentSession torrentSession = new TorrentSession(player, seeding, new ArrayList<>());
		
		MSExtraData.get(server).tryAddTorrentSession(torrentSession);
		
		return torrentSession;
	}
	
/*	public static TorrentSession createTestTorrentSession(int id, boolean seedAll)
	{
		List<GristType> seeding = new ArrayList<>();
		
		if(seedAll)
			seeding.addAll(GristTypes.REGISTRY.stream().toList());
		
		return new TorrentSession(IdentifierHandler.createNewFakeIdentifier(), seeding, new ArrayList<>());
	}*/
	
/*	public static void debugStuff(MinecraftServer server, MSExtraData data)
	{
		for(ServerPlayer player : server.getPlayerList().getPlayers())
			if(player.isHolding(MSItems.ALLWEDDOL.get()))
				data.removesSessions();
		for(ServerPlayer player : server.getPlayerList().getPlayers())
			if(player.isHolding(MSItems.MWRTHWL.get()))
			{
				TorrentSession playerSession = createPlayerTorrentSession(player, server);
				List<PlayerIdentifier> testIDs = new ArrayList<>();

				if(data.getTorrentSessions().stream().anyMatch(session -> session.getSeeder().getId() == 99))
					return;

				for(int i = 99; i < 102; i++)
				{
					TorrentSession iterateSession = createTestTorrentSession(i, true);
					data.tryAddTorrentSession(iterateSession);

					PlayerIdentifier testID = iterateSession.getSeeder();
					testIDs.add(testID);

					List<GristAmount> gristAmounts = new ArrayList<>();
					gristAmounts.add(new GristAmount(GristTypes.BUILD, 1000));
					gristAmounts.add(new GristAmount(GristTypes.CHALK, 1000));
					gristAmounts.add(new GristAmount(GristTypes.AMBER, 1000));
					gristAmounts.add(new GristAmount(GristTypes.DIAMOND, 1000));
					gristAmounts.add(new GristAmount(GristTypes.GOLD, 1000));
					gristAmounts.add(new GristAmount(GristTypes.AMETHYST, 1000));
					gristAmounts.add(new GristAmount(GristTypes.QUARTZ, 1000));
					gristAmounts.add(new GristAmount(GristTypes.ZILLIUM, 1000));
					gristAmounts.add(new GristAmount(GristTypes.SHALE, 1));

					GristCache.get(server, testID).set(new NonNegativeGristSet(gristAmounts));
				}

				List<GristType> leechGrist = new ArrayList<>();
				leechGrist.add(GristTypes.BUILD.get());
				playerSession.addLeech(new TorrentSession.Leech(testIDs.get(0), leechGrist));
				MSExtraData.get(server).tryAddTorrentSession(playerSession);
			}
	}*/
	
	public static void handleTorrent(TorrentSession torrentSession, List<TorrentSession> sessions, MinecraftServer server)
	{
		PlayerIdentifier seeder = torrentSession.getSeeder();
		
		PlayerData seederData = PlayerData.get(seeder, server);
		GristCache seederCache = GristCache.get(seederData);
		
		List<GristType> seeding = torrentSession.getViableSeeding(seederCache.getGristSet());
		
		int seedRateMod = getSeedRateMod(seeding);
		
		if(seedRateMod == 0)
			return;
		
		List<TorrentSession.Leech> leeching = torrentSession.getLeeching();
		
		for(GristType grist : seeding)
		{
			handleGristSeed(grist, leeching, seedRateMod, seederCache, server);
		}
	}
	
	public static void sendOutUpdates(List<TorrentSession> sessions, MinecraftServer server)
	{
		for (TorrentSession torrentSession : sessions){
			var seeder =  torrentSession.getSeeder();
			//TODO consider holding on to torrent data that may be sent to multiple players
			ServerPlayer seederPlayer = seeder.getPlayer(server);
			if(seederPlayer != null)
			{
				MinestuckConfig.TorrentVisibility visibilityConfig = MinestuckConfig.SERVER.gristTorrentVisibility.get();
				boolean sessionOnly = visibilityConfig.equals(MinestuckConfig.TorrentVisibility.SESSION);
				boolean globalVisibility = visibilityConfig.equals(MinestuckConfig.TorrentVisibility.GLOBAL);
				
				Map<Integer, TorrentSession.TorrentClientData> visibleTorrentData = new HashMap<>();
				sessions.forEach(session -> {
					PlayerIdentifier sessionPlayerID = session.getSeeder();
					
					//TODO add Land config option functionality
					//SkaianetData.get(server).getOrCreatePredefineData(sessionPlayerID);
					
					boolean inSameSession = SessionHandler.get(server).isInSameSession(seeder, sessionPlayerID);
					boolean isVisible = globalVisibility || (sessionOnly && inSameSession);
					
					//TODO remove this, temp testing
					isVisible = true;
					
					if(isVisible)
					{
						PlayerData leechData = PlayerData.get(sessionPlayerID, server);
						GristCache leechCache = GristCache.get(leechData);
						TorrentSession.LimitedCache leechLimitedCache = new TorrentSession.LimitedCache(leechCache.getGristSet(), Echeladder.get(leechData).getGristCapacity());
						
						boolean entered = SburbPlayerData.get(sessionPlayerID, server).hasEntered();
						boolean online = sessionPlayerID.getPlayer(server) != null;
						int status = !entered ? 0 : (!online ? 1 : 2);
						
						
						visibleTorrentData.put(sessionPlayerID.getId(), new TorrentSession.TorrentClientData(sessionPlayerID.getUsername(), leechData.getData(MSAttachments.PLAYER_COLOR), status, session.getSeeding(),
								session.getLeeching().stream().collect(Collectors.toMap(leech -> leech.id().getId(), TorrentSession.Leech::gristTypes)), leechLimitedCache));
					}
				});
				
				PacketDistributor.sendToPlayer(seederPlayer, new TorrentPackets.UpdateClient(visibleTorrentData));
			}
		}
	}
	
	public static void handleGristSeed(GristType grist, List<TorrentSession.Leech> leeching, int seedRateMod, GristCache seederCache, MinecraftServer server)
	{
		List<TorrentSession.Leech> eligibleLeeches = leeching.stream().filter(leech -> leech.gristTypes().contains(grist)).toList();
		int leechCount = eligibleLeeches.size();
		long available = seederCache.getGristSet().getGrist(grist);
		int combinedRate = Math.max(1, leechCount * seedRateMod);
		
		int finalRate = Math.min(combinedRate, (int) available);
		GristAmount gristAmount = new GristAmount(grist, finalRate);
		
		
		//TODO ensure that grist cannot enter the leeches gutter
		eligibleLeeches.forEach(leech -> {
			PlayerData leechData = PlayerData.get(leech.id(), server);
			GristCache leechCache = GristCache.get(leechData);
			
			//try to add the grist to the leeches cache and if successful then try to remove from seeder cache
			GristSet remainder = leechCache.addWithinCapacity(gristAmount, null);
			if(remainder.isEmpty())
				seederCache.tryTake(gristAmount, null);
		});
	}
	
	/**
	 * Takes a list of seeded grist types (removing any instances where there is no grist to seed),
	 * and returns a rate at which all available grist should be distributed
	 */
	public static int getSeedRateMod(List<GristType> seeding)
	{
		int numberSeeds = seeding.size();
		
		if(numberSeeds == 0)
			return 0; //TODO this may be an unwise way to handle this
		
		return Math.max(1, (int) (GristTypes.REGISTRY.stream().count() / numberSeeds));
	}
}
