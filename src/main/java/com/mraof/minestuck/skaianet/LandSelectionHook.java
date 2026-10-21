package com.mraof.minestuck.skaianet;

import com.mojang.datafixers.util.Pair;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.entry.EntryProcess;
import com.mraof.minestuck.network.LandSelectPackets;
import com.mraof.minestuck.player.EnumAspect;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerIdentifier;
import com.mraof.minestuck.player.Title;
import com.mraof.minestuck.world.lands.gen.LandTypeSelection;
import com.mraof.minestuck.world.lands.terrain.TerrainLandType;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@EventBusSubscriber(modid = Minestuck.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class LandSelectionHook
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	private static final Map<Player, Pair<Vec3, BlockPos>> playersInLandSelection = new HashMap<>();
	private static final Set<PlayerIdentifier> confirmedPlayers = new HashSet<>();
	
	public static boolean performEntryCheck(ServerPlayer player, BlockPos savedPos)
	{
		if(!MinestuckConfig.SERVER.playerSelectedLand.get()) return true;
		
		PlayerIdentifier identifier = IdentifierHandler.encode(player);
		
		if(confirmedPlayers.remove(identifier)) return true;
		
		SkaianetData skaianetData = SkaianetData.get(player.server);
		Optional<PredefineData> predefineData = skaianetData.getOrCreatePredefineData(identifier);
		if(predefineData.isEmpty() || predefineData.get().getTerrainLandType() != null || predefineData.get().getTitleLandType() != null)
			return true;
		
		if(Title.getTitle(identifier, player.server).isEmpty())
			SburbHandler.generateAndSetTitle(identifier, player.server);
		
		Optional<Title> title = Title.getTitle(identifier, player.server);
		if(title.isEmpty()) return true;
		
		List<TerrainLandType> terrainTypes = availableTerrainTypes(title.get().heroAspect());
		if(terrainTypes.isEmpty()) return true;
		
		playersInLandSelection.put(player, new Pair<>(new Vec3(player.getX(), player.getY(), player.getZ()), savedPos));
		PacketDistributor.sendToPlayer(player, new LandSelectPackets.OpenScreen(terrainTypes));
		return false;
	}
	
	public static void cancelSelection(ServerPlayer player)
	{
		playersInLandSelection.remove(player);
	}
	
	public static void handleLandSelection(ServerPlayer player, @Nullable TerrainLandType terrainType)
	{
		if(!MinestuckConfig.SERVER.playerSelectedLand.get() || !playersInLandSelection.containsKey(player))
		{
			LOGGER.warn("{} tried to select a land without entering.", player.getName().getString());
			return;
		}
		
		PlayerIdentifier identifier = IdentifierHandler.encode(player);
		
		Optional<Title> title = Title.getTitle(identifier, player.server);
		if(title.isEmpty() || terrainType != null && !availableTerrainTypes(title.get().heroAspect()).contains(terrainType))
		{
			LOGGER.warn("{} tried to select an invalid land.", player.getName().getString());
			return;
		}
		
		SkaianetData.get(player.server).getOrCreatePredefineData(identifier).ifPresent(data -> data.predefineSelectedTerrainLandType(terrainType));
		
		var both = playersInLandSelection.remove(player);
		Vec3 previousPlayerPos = both.getFirst();
		
		player.setPos(previousPlayerPos.x, previousPlayerPos.y, previousPlayerPos.z);
		
		BlockPos specifiedPos = both.getSecond();
		
		confirmedPlayers.add(identifier);
		try
		{
			EntryProcess.enter(player, specifiedPos);
		} finally
		{
			confirmedPlayers.remove(identifier);
		}
	}
	
	private static List<TerrainLandType> availableTerrainTypes(EnumAspect aspect)
	{
		return LandTypeSelection.terrainAlternatives().stream().flatMap(List::stream).distinct().filter(terrainType -> !LandTypeSelection.compatibleTitleTypes(terrainType, aspect).isEmpty()).toList();
	}
	
	@SubscribeEvent
	public static void serverStopped(ServerStoppedEvent event)
	{
		playersInLandSelection.clear();
		confirmedPlayers.clear();
	}
}
