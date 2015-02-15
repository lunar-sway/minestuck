package com.mraof.minestuck;

import java.io.File;

import org.lwjgl.opengl.GLContext;

import com.mraof.minestuck.inventory.captchalouge.CaptchaDeckHandler;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.MinestuckAchievementHandler;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MinestuckConfig
{
	
	public static Configuration config;
	public static Side gameSide;
	
	@SideOnly(Side.CLIENT)
	public static int clientOverworldEditRange;
	@SideOnly(Side.CLIENT)
	public static int clientLandEditRange;
	@SideOnly(Side.CLIENT)
	public static int clientCardCost;
	@SideOnly(Side.CLIENT)
	public static byte clientTreeAutobalance;
	@SideOnly(Side.CLIENT)
	public static boolean clientHardMode;
	@SideOnly(Side.CLIENT)
	public static boolean clientGiveItems;
	@SideOnly(Side.CLIENT)
	public static boolean clientInfTreeModus;
	
	public static boolean hardMode = false;
	public static boolean generateCruxiteOre;
	public static boolean privateComputers;
	public static boolean globalSession;
	public static boolean forceMaxSize;
	public static boolean giveItems;
	public static boolean specialCardRenderer;
	public static boolean infiniteTreeModus;
	public static boolean cardRecipe;
	public static boolean cardLoot;
	public static String privateMessage;
	public static int artifactRange;
	public static int overworldEditRange;
	public static int landEditRange;
	public static int cardResolution;
	public static int initialModusSize;
	public static int[] defaultModusTypes;
	public static int modusMaxSize;
	public static int cardCost;
	/**
	 * 0: Make the player's new server player his/her old server player's server player
	 * 1: The player that lost his/her server player will have an idle main connection until someone without a client player connects to him/her.
	 * (Will try to put a better explanation somewhere else later)
	 */
	public static int escapeFailureMode;
	public static byte treeModusSetting;
	
	public static boolean[] deployConfigurations;
	
	static void loadConfigFile(File file, Side side)
	{
		gameSide = side;
		config = new Configuration(file, true);
		config.load();
		
		Minestuck.entityIdStart = config.get("IDs", "entityIdStart", 5050).setLanguageKey("minestuck.config.entityIdStart").getInt();
		Minestuck.skaiaProviderTypeId = config.get("IDs", "skaiaProviderTypeId", 2).setLanguageKey("minestuck.config.skaiaProviderTypeId").getInt();
		Minestuck.skaiaDimensionId = config.get("IDs", "skaiaDimensionId", 2).setLanguageKey("minestuck.config.skaiaDimensionId").getInt();
		Minestuck.landProviderTypeId = config.get("IDs", "landProviderTypeId", 3).setLanguageKey("minestuck.config.landProviderTypeId").getInt();
		Minestuck.landDimensionIdStart = config.get("IDs", "landDimensionIdStart", 3).setLanguageKey("minestuck.config.landDimensionIdStart").getInt();
		MinestuckAchievementHandler.idOffset = config.get("IDs", "statsIdStart", 413).setLanguageKey("minestuck.config.statsIdStart").getInt();
		Minestuck.biomeIdStart = config.get("IDs", "biomeIdStart", 50).setMinValue(40).setMaxValue(120).setLanguageKey("minestuck.config.biomeIdStart").getInt();
		config.getCategory("IDs").setRequiresMcRestart(true).setLanguageKey("minestuck.config.IDs");
		
		Debug.isDebugMode = config.get("General", "Print Debug Messages", true, "Whenether the game should print debug messages or not.").setShowInGui(false).getBoolean();
		
		cardLoot = config.get("General", "cardLoot", false, "Set this to true to make captcha cards appear in dungeon and stronghold chests.").setLanguageKey("minestuck.config.cardLoot").setRequiresMcRestart(true).getBoolean();
		
		loadBasicConfigOptions();
		
		config.save();
	}
	
	static void loadBasicConfigOptions()
	{
		initialModusSize = config.get("Modus", "initialModusSize", 5).setMinValue(0).setLanguageKey("minestuck.config.initialModusSize").getInt();
		defaultModusTypes = config.get("Modus", "defaultModusType", new int[] {0, 1},
				"An array with the possible modus types to be assigned. (0: Stack, 1: Queue, 2: QueueStack, 3: Tree)", 0, CaptchaDeckHandler.ModusType.values().length - 1).setLanguageKey("minestuck.config.defaultModusType").getIntList();
		modusMaxSize = config.get("Modus", "modusMaxSize", 0, "The max size on a modus. Ignored if the value is 0.").setMinValue(0).setLanguageKey("minestuck.config.modusMaxSize").getInt();
		if(initialModusSize > modusMaxSize && modusMaxSize > 0)
			initialModusSize = modusMaxSize;
		String setting = config.get("Modus", "forceAutobalance", "both", "This determines if auto-balance should be forced. 'both' if the player should choose, 'on' if forced at on, and 'off' if forced at off.", new String[] {"both", "off", "on"}).setRequiresWorldRestart(true).setLanguageKey("minestuck.config.forceAutobalance").getString();
		treeModusSetting = (byte) (setting.equals("both") ? 0 : setting.equals("on") ? 1 : 2);
		config.getCategory("Modus").setLanguageKey("minestuck.config.modus");
		
		privateComputers = config.get("General", "privateComputers", false, "True if computers should only be able to be used by the owner.").setLanguageKey("minestuck.config.privateComputers").getBoolean();
		privateMessage = config.get("General", "privateComputerMessage", "You are not allowed to access other players computers.", "The message sent when someone tries to access a computer that they aren't the owner of if 'Private Computers' is true.").setLanguageKey("minestuck.config.privateComputerMessage").getString();
		giveItems = config.get("General", "giveItems", false, "Setting this to true replaces editmode with the old Give Items button.").setLanguageKey("minestuck.config.giveItems").setRequiresWorldRestart(true).getBoolean();
		
		deployConfigurations = new boolean[1];
		deployConfigurations[0] = config.get("General", "deployCard", false, "Determines if a card with a captcha card punched on it should be added to the deploy list or not.").setLanguageKey("minestuck.config.deployCard").setRequiresWorldRestart(true).getBoolean();
		cardCost = config.get("General", "cardCost", 1, "An integer that determines how much a captchalouge card costs to alchemize").setMinValue(1).setLanguageKey("minestuck.config.cardCost").setRequiresWorldRestart(true).getInt();
		cardRecipe = config.get("General", "cardRecipe", true, "Set this to false to remove the captcha card crafting recipe.").setLanguageKey("minestuck.config.cardRecipe").setRequiresWorldRestart(true).getBoolean();
		
		generateCruxiteOre = config.get("General", "generateCruxiteOre", true, "If cruxite ore should be generated in the overworld.").setRequiresWorldRestart(true).setLanguageKey("minestuck.config.generateCruxiteOre").getBoolean();
		globalSession = config.get("General", "globalSession", true, "Whenether all connetions should be put into a single session or not.").setLanguageKey("minestuck.config.globalSession").getBoolean();
		overworldEditRange = config.get("General", "overworldEditRange", 15, "A number that determines how far away from the computer an editmode player may be before entry.", 3, 50).setRequiresWorldRestart(true).setLanguageKey("minestuck.config.overworldEditRange").getInt();
		landEditRange = config.get("General", "landEditRange", 30, "A number that determines how far away from the center of the brought land that an editmode player may be after entry.", 3, 50).setRequiresWorldRestart(true).setLanguageKey("minestuck.config.landEditRange").getInt();
		artifactRange = config.get("General", "artifactRange", 30, "Radius of the land brought into the medium.", 3, 50).setLanguageKey("minestuck.config.artifactRange").getInt();
		
		if(gameSide.isClient())	//Client sided config values
		{
			//specialCardRenderer = config.getBoolean("specialCardRenderer", "General", false, "Whenether to use the special render for cards or not.");
			if(specialCardRenderer && !GLContext.getCapabilities().GL_EXT_framebuffer_object)
			{
				specialCardRenderer = false;
				FMLLog.warning("[Minestuck] The FBO extension is not available and is required for the advanced rendering of captchalouge cards.");
			}
			//cardResolution = config.getInt("General", "cardResolution", 1, 0, 5, "The resolution of the item inside of a card. The width/height is computed by '8*2^x', where 'x' is this config value.");
		}
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event)
	{
		if(event.modID.equals(Minestuck.class.getAnnotation(Mod.class).modid()))
		{
			if(!event.isWorldRunning && !event.requiresMcRestart)
				loadBasicConfigOptions();	//Haven't put up a method for changing config options while the world is running
			
			config.save();
			
		}
	}
	
}
