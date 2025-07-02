package com.mraof.minestuck.player;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.api.alchemy.GristSet;
import com.mraof.minestuck.client.ClientRungData;
import com.mraof.minestuck.client.gui.ColorSelectorScreen;
import com.mraof.minestuck.client.gui.MSScreenFactories;
import com.mraof.minestuck.inventory.captchalogue.CaptchaDeckHandler;
import com.mraof.minestuck.inventory.captchalogue.Modus;
import com.mraof.minestuck.network.*;
import com.mraof.minestuck.network.editmode.EditmodeCacheLimitPacket;
import com.mraof.minestuck.util.ColorHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.core.HolderLookup;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.LogicalSide;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Contains static field for any {@link PlayerData} fields that also need client access.
 * @author kirderf1
 */
@EventBusSubscriber(modid = Minestuck.MOD_ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public final class ClientPlayerData
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	private static Modus modus;
	private static Title title;
	private static int rung;
	private static float rungProgress;
	private static long boondollars;
	private static GristSet playerGrist, targetGrist;
	private static long targetCacheLimit;
	private static int playerColor;
	private static boolean displaySelectionGui;
	private static boolean dataCheckerAccess;
	
	@SubscribeEvent
	private static void onLoggedIn(ClientPlayerNetworkEvent.LoggingIn event)
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
	
	public static ClientCache getGristCache(CacheSource cacheSource)
	{
		return switch(cacheSource)
		{
			case PLAYER -> new ClientCache(ClientPlayerData.playerGrist, ClientRungData.getData(ClientPlayerData.getRung()).gristCapacity());
			case EDITMODE -> new ClientCache(ClientPlayerData.targetGrist, targetCacheLimit);
		};
	}
	
	public record ClientCache(GristSet set, long limit)
	{
		public boolean canAfford(GristSet cost)
		{
			return GristCache.canAfford(this.set, cost, this.limit);
		}
	}
	
	public enum CacheSource
	{
		PLAYER,
		EDITMODE,
	}
	
	public static int getPlayerColor()
	{
		return playerColor;
	}
	
	public static void selectColor(int colorIndex)
	{
		PacketDistributor.sendToServer(new PlayerColorPackets.SelectIndex(colorIndex));
		playerColor = ColorHandler.BuiltinColors.getColor(colorIndex);
	}
	
	public static void selectColorRGB(int color)
	{
		if (color < 0 || color > 256*256*256) return;
		
		PacketDistributor.sendToServer(new PlayerColorPackets.SelectRGB(color));
		playerColor = color;
	}
	
	public static boolean hasDataCheckerAccess()
	{
		return dataCheckerAccess;
	}
	
	public static void handleDataPacket(CaptchaDeckPackets.ModusData packet, HolderLookup.Provider provider)
	{
		modus = CaptchaDeckHandler.readFromNBT(packet.nbt(), LogicalSide.CLIENT, provider);
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
		boondollars = packet.amount();
	}
	
	public static void handleDataPacket(GristCachePacket packet)
	{
		switch(packet.cacheSource())
		{
			case PLAYER -> playerGrist = packet.gristCache();
			case EDITMODE -> targetGrist = packet.gristCache();
		}
	}
	
	public static void handleDataPacket(EditmodeCacheLimitPacket packet)
	{
		targetCacheLimit = packet.limit();
	}
	
	public static void handleDataPacket(PlayerColorPackets.OpenSelection packet)
	{
		ClientPlayerData.playerColor = ColorHandler.BuiltinColors.DEFAULT_COLOR;
		ClientPlayerData.displaySelectionGui = true;
	}
	
	public static void handleDataPacket(PlayerColorPackets.Data packet)
	{
		ClientPlayerData.playerColor = packet.color();
	}
	
	public static void handleDataPacket(DataCheckerPackets.Permission packet)
	{
		dataCheckerAccess = packet.isAvailable();
	}
	
	@SubscribeEvent
	private static void onClientTick(ClientTickEvent.Post event)
	{
		if(displaySelectionGui && Minecraft.getInstance().screen == null)
		{
			displaySelectionGui = false;
			if(MinestuckConfig.CLIENT.loginColorSelector.get())
				Minecraft.getInstance().setScreen(new ColorSelectorScreen(true));
		}
	}
}
