package com.mraof.minestuck.skaianet;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.entry.EntryProcess;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.network.TitleSelectPacket;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerIdentifier;
import com.mraof.minestuck.player.Title;
import com.mraof.minestuck.player.PlayerSavedData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * A class that determines when to stop entry and tell the player to pick a title,
 * and to then handle the selection once it's been sent back.
 */
public class TitleSelectionHook
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	static Map<Player, Vec3> playersInTitleSelection = new HashMap<>();
	
	/**
	 * Checks if the player has the go-ahead to enter.
	 * If the player should get the title selection screen, this will send that packet to the player and then return false.
	 */
	public static boolean performEntryCheck(ServerPlayer player)
	{
		if(!MinestuckConfig.SERVER.playerSelectedTitle.get())
			return true;
		
		PlayerIdentifier identifier = IdentifierHandler.encode(player);
		Session s = SessionHandler.get(player.level).getPlayerSession(identifier);
		
		if(s != null && s.predefinedPlayers.containsKey(identifier) && s.predefinedPlayers.get(identifier).getTitle() != null
				|| PlayerSavedData.getData(identifier, player.server).getTitle() != null)
			return true;
		
		playersInTitleSelection.put(player, new Vec3(player.getX(), player.getY(), player.getZ()));
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
		if(MinestuckConfig.SERVER.playerSelectedTitle.get() && playersInTitleSelection.containsKey(player))
		{
			PlayerIdentifier identifier = IdentifierHandler.encode(player);
			
			if(title == null)
				SburbHandler.generateAndSetTitle(player.level, identifier);
			else
			{
				Session s = SessionHandler.get(player.server).getPlayerSession(identifier);
				if(s != null && s.getUsedTitles(identifier).contains(title))
				{
					// Title is already used in session; inform the player that they can't pick this title
					MSPacketHandler.sendToPlayer(new TitleSelectPacket(title), player);
					return;
				}
				
				PlayerSavedData.getData(identifier, player.server).setTitle(title);
			}
			
			//Once the title selection has finished successfully, restore player position and trigger entry
			Vec3 pos = playersInTitleSelection.remove(player);
			
			player.setPos(pos.x, pos.y, pos.z);
			
			EntryProcess.onArtifactActivated(player);
			
		} else LOGGER.warn("{} tried to select a title without entering.", player.getName().getString());
	}
}