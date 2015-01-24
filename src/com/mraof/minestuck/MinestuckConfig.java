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
	public static String privateMessage;
	public static int artifactRange;
	public static int overworldEditRange;
	public static int landEditRange;
	public static int cardResolution;
	public static int defaultModusSize;
	public static int defaultModusType;
	public static int modusMaxSize;
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
		
		Minestuck.entityIdStart = config.getInt("entitydIdStart", "Entity Ids", 5050, 0, Integer.MAX_VALUE, "From what id that minestuck entites should be registered with."); //The number 5050 might make it seem like this is meant to match up with item/block IDs, but it is not
		Minestuck.skaiaProviderTypeId = config.getInt("skaiaProviderTypeId", "Provider Type Ids", 2, 2, Integer.MAX_VALUE, "The id to be registered for the skaia provider.");
		Minestuck.skaiaDimensionId = config.getInt("skaiaDimensionId", "Dimension Ids", 2, 2, Integer.MAX_VALUE, "The id for the skaia dimension.");
		Minestuck.landProviderTypeId = config.getInt("landProviderTypeId", "Provider Type Ids", 3, 2, Integer.MAX_VALUE, "The id for the land provider.");
		Minestuck.landDimensionIdStart = config.getInt("landDimensionIdStart", "Dimension Ids", 3, 2, Integer.MAX_VALUE, "The starting id for the land dimensions.");
		Debug.isDebugMode = config.getBoolean("printDebugMessages", "General", true, "Whenether the game should print debug messages or not.");
		generateCruxiteOre = config.getBoolean("generateCruxiteOre", "General", true, "If cruxite ore should be generated in the overworld.");
		globalSession = config.getBoolean("globalSession", "General", true, "Whenether all connetions should be put into a single session or not.");
		privateComputers = config.getBoolean("privateComputers", "General", false, "True if computers should only be able to be used by the owner.");
		privateMessage = config.getString("privateMessage", "General", "You are not allowed to access other players computers.", "The message sent when someone tries to access a computer that they aren't the owner of if 'privateComputers' is true.");
		overworldEditRange = config.getInt("overWorldEditRange", "General", 15, 3, 50, "A number that determines how far away from the computer an editmode player may be before entry.");
		landEditRange = config.getInt("landEditRange", "General", 30, 3, 50, "A number that determines how far away from the center of the brought land that an editmode player may be after entry.");
		artifactRange = config.getInt("artifactRange", "General", 30, 3, 50, "Radius of the land brought into the medium.");
		MinestuckAchievementHandler.idOffset = config.getInt("statisticIdOffset", "General", 413, 100, Integer.MAX_VALUE, "The id offset used when registering achievements and other statistics.");
		//hardMode = config.getBoolean("hardMode", "General", false, "Used to determine if the editmode player can provide infinite cards. Will later also be used whenether there'll be a timer to enter the medium and things like that.");
		//forceMaxSize = config.getBoolean("forceMaxSize", "General", false); Unused for now.
		//escapeFailureMode = config.getInt("escapeFailureMode", "General", 0, 0, 2, "Used to determine what happens with related connections when a player fails to escape the meteor enabled by 'hardmode'.");
		giveItems = config.getBoolean("giveItems", "General", false, "Setting this to true replaces editmode with the old Give Items button.");
		defaultModusSize = config.getInt("defaultModusSize", "Modus", 5, 0, Integer.MAX_VALUE, "The initial size of a captchalouge deck.");
		defaultModusType = config.getInt("defaultModusType", "Modus", -1, -2, CaptchaDeckHandler.ModusType.values().length - 1,
				"The type of modus that is given to players without one. -1: Random modus given from a builtin list. -2: Any random modus. 0+: Certain modus given. Anything else: No modus given.");
		modusMaxSize = config.getInt("modusMaxSize", "Modus", 0, 0, Integer.MAX_VALUE, "The max size on a modus. Ignored if the value is 0.");
		if(defaultModusSize > modusMaxSize && modusMaxSize > 0)
			defaultModusSize = modusMaxSize;
		deployConfigurations = new boolean[1];
		deployConfigurations[0] = config.getBoolean("cardInDeploylist", "General", false, "Determines if a card with a captcha card punched on it should be added to the deploy list or not.");
		treeModusSetting = (byte) config.getInt("treeModusSetting", "Modus", 0, 0, 2, "This determines how autobalance should be. 0 if the player should choose, 1 if forced at on, and 2 if forced at off.");
		
		if(side.isClient()) {	//Client sided config values
			editmodeToolTip = config.getBoolean("editmodeToolTip", "General", true, "True if the grist cost on items should be shown. This only applies for editmode.");
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
