package com.mraof.minestuck.world.storage;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.client.gui.MSScreenFactories;
import com.mraof.minestuck.computer.editmode.ClientEditHandler;
import com.mraof.minestuck.inventory.captchalogue.CaptchaDeckHandler;
import com.mraof.minestuck.inventory.captchalogue.Modus;
import com.mraof.minestuck.item.crafting.alchemy.GristSet;
import com.mraof.minestuck.network.ColorSelectPacket;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.network.data.*;
import com.mraof.minestuck.player.Title;
import com.mraof.minestuck.util.ColorHandler;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Contains static field for any {@link PlayerData} fields that also need client access.
 * @author kirderf1
 */
@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientPlayerData
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	private static Modus modus;
	private static Title title;
	private static int rung;
	private static float rungProgress;
	private static long boondollars;
	private static int consortReputation;
	private static GristSet playerGrist;
	private static GristSet targetGrist;
	private static int playerColor;
	private static boolean displaySelectionGui;
	private static boolean dataCheckerAccess;
	
	@SubscribeEvent
	public static void onLoggedIn(ClientPlayerNetworkEvent.LoggedInEvent event)
	{
		modus = null;
		title = null;
		rung = -1;
		playerColor = -1;
		displaySelectionGui = false;
	}
	
	public static Modus getModus()
	{
		return modus;
	}
	
	public static Title getTitle()
	{
		return title;
	}
	
	public static int getRung()
	{
		return rung;
	}
	
	/**
	 * Note: Unlike the value used on the logical server side, this vale is a fraction going from 0 to 1
	 */
	public static float getRungProgress()
	{
		return rungProgress;
	}
	
	public static long getBoondollars()
	{
		return boondollars;
	}
	
	public static int getConsortReputation()
	{
		return consortReputation;
	}
	
	public static GristSet getClientGrist()
	{
		return ClientEditHandler.isActive() ? targetGrist : playerGrist;
	}
	
	public static int getPlayerColor()
	{
		return playerColor;
	}
	
	public static void selectColor(int colorIndex)
	{
		MSPacketHandler.sendToServer(new ColorSelectPacket(colorIndex));
		playerColor = ColorHandler.getColor(colorIndex);
	}
	
	public static boolean shouDisplayColorSelection()
	{
		return displaySelectionGui;
	}
	
	public static void clearDisplayColorSelection()
	{
		displaySelectionGui = false;
	}
	
	public static boolean hasDataCheckerAccess()
	{
		return dataCheckerAccess;
	}
	
	public static void handleDataPacket(ModusDataPacket packet)
	{
		modus = CaptchaDeckHandler.readFromNBT(packet.getNBT(), null);
		if(modus != null)
			MSScreenFactories.updateSylladexScreen();
		else LOGGER.debug("Player lost their modus after update packet");
	}
	
	public static void handleDataPacket(TitleDataPacket packet)
	{
		title = packet.getTitle();
	}
	
	public static void handleDataPacket(EcheladderDataPacket packet)
	{
		rung = packet.getRung();
		rungProgress = packet.getProgress();
	}
	
	public static void handleDataPacket(BoondollarDataPacket packet)
	{
		boondollars = packet.getBoondollars();
	}
	
	public static void handleDataPacket(ConsortReputationDataPacket packet)
	{
		consortReputation = packet.getCount();
	}
	
	public static void handleDataPacket(GristCachePacket packet)
	{
		if(packet.isEditmode)
			targetGrist = packet.gristCache;
		else playerGrist = packet.gristCache;
	}
	
	public static void handleDataPacket(ColorDataPacket packet)
	{
		if(packet.hasNoColor())
		{
			ClientPlayerData.playerColor = ColorHandler.DEFAULT_COLOR;
			ClientPlayerData.displaySelectionGui = true;
		} else ClientPlayerData.playerColor = packet.getColor();
	}
	
	public static void handleDataPacket(DataCheckerPermissionPacket packet)
	{
		dataCheckerAccess = packet.isDataCheckerAvailable();
	}
}