package com.mraof.minestuck;

import java.io.File;

import org.lwjgl.opengl.GLContext;

import com.mraof.minestuck.inventory.captchalouge.CaptchaDeckHandler;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.MinestuckAchievementHandler;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MinestuckConfig
{
	
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
	@SideOnly(Side.CLIENT)
	public static boolean editmodeToolTip;
	
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
	public static int defaultModusSize;
	public static int defaultModusType;
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
		Configuration config = new Configuration(file);
		config.load();
		
		Minestuck.entityIdStart = config.getInt("Entity Id Start", "IDs", 5050, 0, Integer.MAX_VALUE, "From what id that minestuck entites should be registered with."); //The number 5050 might make it seem like this is meant to match up with item/block IDs, but it is not
		Minestuck.skaiaProviderTypeId = config.getInt("SkaiaProvider Type Id", "IDs", 2, 2, Integer.MAX_VALUE, "The id to be registered for the skaia provider.");
		Minestuck.skaiaDimensionId = config.getInt("Skaia Dimension Id", "IDs", 2, 2, Integer.MAX_VALUE, "The id for the skaia dimension.");
		Minestuck.landProviderTypeId = config.getInt("LandProvider Type Id", "IDs", 3, 2, Integer.MAX_VALUE, "The id for the land provider.");
		Minestuck.landDimensionIdStart = config.getInt("Land Dimension Id Start", "IDs", 3, 2, Integer.MAX_VALUE, "The starting id for the land dimensions.");
		MinestuckAchievementHandler.idOffset = config.getInt("Statistic Id Offset", "IDs", 413, 100, Integer.MAX_VALUE, "The id offset used when registering achievements and other statistics.");
		
		defaultModusSize = config.getInt("Default Modus Size", "Modus", 5, 0, Integer.MAX_VALUE, "The initial size of a captchalouge deck.");
		defaultModusType = config.getInt("Default Modus Type", "Modus", -1, -2, CaptchaDeckHandler.ModusType.values().length - 1,
				"The type of modus that is given to players without one. -1: Random modus given from a builtin list. -2: Any random modus. 0+: Certain modus given. Anything else: No modus given.");
		modusMaxSize = config.getInt("Modus Max Size", "Modus", 0, 0, Integer.MAX_VALUE, "The max size on a modus. Ignored if the value is 0.");
		if(defaultModusSize > modusMaxSize && modusMaxSize > 0)
			defaultModusSize = modusMaxSize;
		treeModusSetting = (byte) config.getInt("Tree Modus Setting", "Modus", 0, 0, 2, "This determines how autobalance should be. 0 if the player should choose, 1 if forced at on, and 2 if forced at off.");
		
		privateComputers = config.getBoolean("Private Computers", "General", false, "True if computers should only be able to be used by the owner.");
		privateMessage = config.getString("Private Message", "General", "You are not allowed to access other players computers.", "The message sent when someone tries to access a computer that they aren't the owner of if 'Private Computers' is true.");
		giveItems = config.getBoolean("Give Items Button", "General", false, "Setting this to true replaces editmode with the old Give Items button.");
		
		deployConfigurations = new boolean[1];
		deployConfigurations[0] = config.getBoolean("Card In Deploylist", "General", false, "Determines if a card with a captcha card punched on it should be added to the deploy list or not.");
		cardCost = config.getInt("Card Cost", "General", 1, 1, Integer.MAX_VALUE, "An integer that determines how much a captchalouge card costs to alchemize");
		cardRecipe = config.getBoolean("Include Captcha Card Recipe", "General", true, "Set this to false to remove the captcha card crafting recipe.");
		cardLoot = config.getBoolean("Generate Card As Loot", "General", false, "Set this to true to make captcha cards appear in dungeon and stronghold chests.");
		
		Debug.isDebugMode = config.getBoolean("Print Debug Messages", "General", true, "Whenether the game should print debug messages or not.");
		generateCruxiteOre = config.getBoolean("Generate Cruxite Ore", "General", true, "If cruxite ore should be generated in the overworld.");
		globalSession = config.getBoolean("Global Session", "General", true, "Whenether all connetions should be put into a single session or not.");
		overworldEditRange = config.getInt("Overworld Edit Range", "General", 15, 3, 50, "A number that determines how far away from the computer an editmode player may be before entry.");
		landEditRange = config.getInt("Land Edit Range", "General", 30, 3, 50, "A number that determines how far away from the center of the brought land that an editmode player may be after entry.");
		artifactRange = config.getInt("Artifact Range", "General", 30, 3, 50, "Radius of the land brought into the medium.");
		
		if(side.isClient()) {	//Client sided config values
			editmodeToolTip = config.getBoolean("Editmode Tool Tip", "General", true, "True if the grist cost on items should be shown. This only applies for editmode.");
			//specialCardRenderer = config.getBoolean("specialCardRenderer", "General", false, "Whenether to use the special render for cards or not.");
			if(specialCardRenderer && !GLContext.getCapabilities().GL_EXT_framebuffer_object)
			{
				specialCardRenderer = false;
				FMLLog.warning("[Minestuck] The FBO extension is not available and is required for the advanced rendering of captchalouge cards.");
			}
			//cardResolution = config.getInt("General", "cardResolution", 1, 0, 5, "The resulotion of the item inside of a card. The width/height is computed by '8*2^x', where 'x' is this config value.");
		}
		config.save();
	}
	
}
