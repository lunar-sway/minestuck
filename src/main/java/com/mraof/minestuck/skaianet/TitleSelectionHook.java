package com.mraof.minestuck.skaianet;

import com.mojang.datafixers.util.Pair;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.entry.EntryProcess;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.network.TitleSelectPacket;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerIdentifier;
import com.mraof.minestuck.player.PlayerSavedData;
import com.mraof.minestuck.player.Title;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * A class that determines when to stop entry and tell the player to pick a title,
 * and to then handle the selection once it's been sent back.
 */
@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus=Mod.EventBusSubscriber.Bus.FORGE)
public class TitleSelectionHook
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	// previous player position and specified entry point
	private static final Map<Player, Pair<Vec3, BlockPos>> playersInTitleSelection = new HashMap<>();
	
	/**
	 * Checks if the player has the go-ahead to enter.
	 * If the player should get the title selection screen, this will send that packet to the player and then return false.
	 */
	public static boolean performEntryCheck(ServerPlayer player, BlockPos savedPos)
	{
		if(!MinestuckConfig.SERVER.playerSelectedTitle.get())
			return true;
		
		PlayerIdentifier identifier = IdentifierHandler.encode(player);
		
		if(SkaianetHandler.get(player.server).predefineData(identifier).map(data -> data.getTitle() != null).orElse(false)
				|| PlayerSavedData.getData(identifier, player.server).getTitle() != null)
			return true;
		
		playersInTitleSelection.put(player, new Pair<>(new Vec3(player.getX(), player.getY(), player.getZ()), savedPos));
		TitleSelectPacket packet = new TitleSelectPacket();
		MSPacketHandler.sendToPlayer(packet, player);
		return false;
	}
	
	public static void cancelSelection(ServerPlayer player)
	{
		playersInTitleSelection.remove(player);
	}
	
	public static void handleTitleSelection(ServerPlayer player, Title title)
	{
		if(!MinestuckConfig.SERVER.playerSelectedTitle.get() || !playersInTitleSelection.containsKey(player))
		{
			LOGGER.warn("{} tried to select a title without entering.", player.getName().getString());
			return;
		}
		
		PlayerIdentifier identifier = IdentifierHandler.encode(player);
		
		if(title == null)
			SburbHandler.generateAndSetTitle(player.level(), identifier);
		else
			PlayerSavedData.getData(identifier, player.server).setTitle(title);
		
		//Once the title selection has finished successfully, restore player position and trigger entry
		var both = playersInTitleSelection.remove(player);
		Vec3 previousPlayerPos = both.getFirst();
		
		player.setPos(previousPlayerPos.x, previousPlayerPos.y, previousPlayerPos.z);
		
		BlockPos specifiedPos = both.getSecond();
		
		EntryProcess.enter(player, specifiedPos);
		
	}
	
	@SubscribeEvent
	public static void serverStopped(ServerStoppedEvent event)
	{
		playersInTitleSelection.clear();
	}
}