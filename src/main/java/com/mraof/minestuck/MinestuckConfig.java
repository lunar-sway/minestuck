package com.mraof.minestuck;

import com.mraof.minestuck.computer.editmode.DeployList;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.minecraftforge.common.ForgeConfigSpec.*;

@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class MinestuckConfig
{
	//Uses the singleton design pattern, much like the forge config
	public static class Common
	{
		public final BooleanValue logIngredientItemsWithoutCosts;
		public final BooleanValue logItemsWithRecipeAndCost;
		
		private Common(ForgeConfigSpec.Builder builder)
		{
			builder.comment("If you're looking for a config option that isn't here, try looking in the world-specific config").push("logging");
			logIngredientItemsWithoutCosts = builder.comment("Makes the recipe-generated grist cost process log any items that are used as recipe ingredients, but is neither the output of a different recipe, or has a grist cost. Useful for finding items that probably need manual grist costs.")
					.define("logIngredientItemsWithoutCosts", false);
			logItemsWithRecipeAndCost = builder.comment("Makes the recipe-generated grist cost process log any items that has a grist cost, but which could also be provided as a recipe generated cost. Useful for finding items that probably do not need manual grist costs.")
					.define("logItemsWithRecipeAndCost", false);
			builder.pop();
		}
	}
	
	static final ForgeConfigSpec commonConfigSpec;
	public static final Common COMMON;
	static
	{
		Pair<Common, ForgeConfigSpec> pair = new ForgeConfigSpec.Builder().configure(Common::new);
		COMMON = pair.getLeft();
		commonConfigSpec = pair.getRight();
	}
	
	static final ForgeConfigSpec CLIENT_CONFIG;
	static final ForgeConfigSpec SERVER_CONFIG;
	
	//            Server	(The category for all config values that influence both client and server, and is appropriate to be per-world. Is stored in a per-world config and synced to any clients connected to the server)
	//Machines
	public static BooleanValue cruxtruderIntake;
	public static ConfigValue<List<String>> forbiddenDimensionTypesTpz;
	public static ConfigValue<List<String>> forbiddenModDimensionsTpz;
	public static BooleanValue disableGristWidget;
	public static IntValue alchemiterMaxStacks;
	
	//Medium
	public static BooleanValue canBreakGates;
	public static BooleanValue disableGiclops;
	public static BooleanValue allowSecondaryConnections;
	
	//Ores
	public static BooleanValue generateCruxiteOre;
	public static BooleanValue generateUraniumOre;
	public static int cruxiteVeinsPerChunk = 10;
	public static int uraniumVeinsPerChunk = 10;
	public static int baseCruxiteVeinSize = 6;
	public static int baseUraniumVeinSize = 3;
	public static int bonusCruxiteVeinSize = 3;
	public static int bonusUraniumVeinSize = 3;
	public static int cruxiteStratumMin = 0;
	public static int uraniumStratumMin = 0;
	public static int cruxiteStratumMax = 60;
	public static int uraniumStratumMax = 30;
	
	//Sylladex
	public static BooleanValue dropItemsInCards;
	public static IntValue initialModusSize;
	public static ConfigValue<List<String>> startingModusTypes;
	public static IntValue modusMaxSize;
	public static EnumValue<DropMode> sylladexDropMode;
	public static EnumValue<AvailableOptions> treeModusSetting;
	public static EnumValue<AvailableOptions> hashmapChatModusSetting;
	
	//Mechanics
	public static boolean forceMaxSize = true;
	public static boolean hardMode = false; //Not fully fleshed out yet
	public static BooleanValue echeladderProgress;
	public static BooleanValue aspectEffects;
	public static BooleanValue playerSelectedTitle;
	public static IntValue preEntryRungLimit;
	public static BooleanValue rungHealthOnRespawn;
	
	//Entry
	public static BooleanValue entryCrater;
	public static BooleanValue adaptEntryBlockHeight;
	public static BooleanValue stopSecondEntry;
	public static BooleanValue needComputer;
	public static IntValue artifactRange;
	
	//Computer
	public static BooleanValue privateComputers;
	public static BooleanValue globalSession;
	public static BooleanValue skaianetCheck;
	public static EnumValue<PermissionType> dataCheckerPermission;
	/**
	 * 0: Make the player's new server player his/her old server player's server player
	 * 1: The player that lost his/her server player will have an idle main connection until someone without a client player connects to him/her.
	 * (Will try to put a better explanation somewhere else later)
	 */
	public static int escapeFailureMode;
	
	//Edit Mode
	public static BooleanValue showGristChanges;
	public static BooleanValue gristRefund;
	public static BooleanValue deployCard;
	public static BooleanValue portableMachines;
	public static IntValue overworldEditRange;
	public static IntValue landEditRange;
	public static BooleanValue giveItems;
	
	//            Client	(Anything that is only needed for clients (only needed client-side))
	public static EnumValue<AnimationSpeed> echeladderAnimation;
	public static BooleanValue loginColorSelector;
	public static boolean dataCheckerAccess;
	public static BooleanValue alchemyIcons;
	
	static
	{
		ForgeConfigSpec.Builder SERVER_BUILDER = new ForgeConfigSpec.Builder();
		
		SERVER_BUILDER.push("ores");
		generateCruxiteOre = SERVER_BUILDER.comment("If cruxite ore should be generated in the overworld.")
				.define("generateCruxiteOre",true);
		generateUraniumOre = SERVER_BUILDER.comment("If uranium ore should be generated in the overworld.")
				.define("generateUraniumOre",true);
		SERVER_BUILDER.pop();
		
		SERVER_BUILDER.push("mechanics");
		echeladderProgress = SERVER_BUILDER.comment("If this is true, players will be able to see their progress towards the next rung. This is server side and will only be active in multiplayer if the server/Lan host has it activated.")
				.define("echeladderProgress", true);
		preEntryRungLimit = SERVER_BUILDER.comment("The highest rung you can get before entering medium. Note that the first rung is indexed as 0, the second as 1 and so on.")
				.defineInRange("preEntryRungLimit", 6, 0, 49);
		rungHealthOnRespawn = SERVER_BUILDER.comment("If true, players will respawn with full health, rung bonuses included. If false, health will be left alone (typically meaning that you respawn with 10 hearts)")
				.define("preEntryRungLimit", true);
		aspectEffects = SERVER_BUILDER.comment("If this is true, players will gain certain potion effects once they reach a certain rung based on their aspect.")
				.define("aspectEffects", true);
		playerSelectedTitle = SERVER_BUILDER.comment("Enable this to let players select their own title. They will however not be able to select the Lord or Muse as class.")
				.define("playerSelectedTitle", true);
		SERVER_BUILDER.pop();
		
		SERVER_BUILDER.push("sylladex");
		dropItemsInCards = SERVER_BUILDER.comment("When sylladices may drop items and cards at the same time, this option determines if items should be dropped inside of cards or items and cards as different stacks.")
				.define("dropItemsInCards", true);
		initialModusSize = SERVER_BUILDER.comment("The initial amount of captchalogue cards in your sylladex.")
				.defineInRange("initialModusSize", 5, 0, Integer.MAX_VALUE);
		startingModusTypes = SERVER_BUILDER.comment("An array with the possible modus types to be assigned. Written with mod-id and modus name, for example \"minestuck:queue_stack\" or \"minestuck:hash_map\"")
				.define("startingModusTypes", new ArrayList<>(Arrays.asList("minestuck:stack","minestuck:queue")));
		modusMaxSize = SERVER_BUILDER.comment("The max size on a modus. Ignored if the value is 0.")
				.defineInRange("modusMaxSize", 0, 0, Integer.MAX_VALUE);
		sylladexDropMode = SERVER_BUILDER.comment("Determines which items from the modus that are dropped on death. \"items\": Only the items are dropped. \"cards_and_items\": Both items and cards are dropped. (So that you have at most initial_modus_size amount of cards) \"all\": Everything is dropped, even the modus.")
				.defineEnum("dropMode", DropMode.CARDS_AND_ITEMS);
		treeModusSetting = SERVER_BUILDER.comment("This determines if auto-balance should be forced. 'both' if the player should choose, 'on' if forced at on, and 'off' if forced at off.")
				.defineEnum("treeModusSetting", AvailableOptions.BOTH);
		hashmapChatModusSetting = SERVER_BUILDER.comment("This determines if hashmap chat ejection should be forced. 'both' if the player should choose, 'on' if forced at on, and 'off' if forced at off.")
				.defineEnum("hashmapModusSetting", AvailableOptions.BOTH);
		SERVER_BUILDER.pop();
		
		SERVER_BUILDER.push("computer");
		privateComputers = SERVER_BUILDER.comment("True if computers should only be able to be used by the owner.")
				.define("privateComputers", true);
		globalSession = SERVER_BUILDER.comment("Whenever all sburb connections should be put into a single session or not.")
				.define("globalSession",false);
		skaianetCheck = SERVER_BUILDER.comment("If enabled, will during certain moments perform a check on all connections and computers that are in use. Recommended to turn off if there is a need to improve performance, however skaianet-related bugs might appear when done so.")
				.define("skaianetCheck",true);
		dataCheckerPermission = SERVER_BUILDER.comment("Determines who's allowed to access the data checker. \"none\": No one is allowed. \"ops\": only those with a command permission of level 2 or more may access the data ckecker. (for single player, that would be if cheats are turned on) \"gamemode\": Only players with the creative or spectator gamemode may view the data checker. \"ops_or_gamemode\": Both ops and players in creative or spectator mode may view the data checker. \"anyone\": No access restrictions are used.")
				.defineEnum("dataCheckerPermission", PermissionType.OPS_OR_GAMEMODE);
		SERVER_BUILDER.pop();
		
		SERVER_BUILDER.push("editMode");
		showGristChanges = SERVER_BUILDER.comment("If this is true, grist change messages will appear.")
				.define("showGristChanges",true);
		gristRefund = SERVER_BUILDER.comment("Enable this and players will get a (full) grist refund from breaking blocks in editmode.")
				.define("gristRefund", false);
		deployCard = SERVER_BUILDER.comment("Determines if a card with a captcha card punched on it should be added to the deploy list.")
				.define("deployCard",false);
		portableMachines = SERVER_BUILDER.comment("Determines if the small portable machines should be included in the deploy list.")
				.define("portableMachines", false);
		giveItems = SERVER_BUILDER.comment("Setting this to true replaces editmode with the old Give Items button.")
				.define("giveItems", false);
		overworldEditRange = SERVER_BUILDER.comment("A number that determines how far away from the computer an editmode player may be before entry.")
				.defineInRange("overworldEditRange", 15, 1, Integer.MAX_VALUE);
		landEditRange = SERVER_BUILDER.comment("A number that determines how far away from the center of the brought land that an editmode player may be after entry.")
				.defineInRange("landEditRange", 30, 1, Integer.MAX_VALUE);
		SERVER_BUILDER.pop();
		
		SERVER_BUILDER.push("machines");
		disableGristWidget = SERVER_BUILDER.comment("Disable Grist Widget")
				.define("disableGristWidget",false);
		alchemiterMaxStacks = SERVER_BUILDER.comment("The number of stacks that can be alchemized at the same time with the alchemiter.")
				.defineInRange("alchemiterMaxStacks",16,0,999);
		cruxtruderIntake = SERVER_BUILDER.comment("If enabled, the regular cruxtruder will require raw cruxite to function, which is inserted through the pipe.")
				.define("cruxtruderIntake",true);
		forbiddenDimensionTypesTpz = SERVER_BUILDER.comment("A list of dimension types that you cannot travel to or from using transportalizers.")
				.define("forbiddenDimensionTypesTpz", new ArrayList<>());
		forbiddenModDimensionsTpz = SERVER_BUILDER.comment("A list of mod dimensions that you cannot travel to or from using transportalizers.")
				.define("forbiddenModDimensionsTpz", new ArrayList<>());
		SERVER_BUILDER.pop();
		
		SERVER_BUILDER.push("entry");
		entryCrater = SERVER_BUILDER.comment("Disable this to prevent craters from people entering the medium.")
				.define("entryCrater",true);
		adaptEntryBlockHeight = SERVER_BUILDER.comment("Adapt the transferred height to make the top non-air block to be placed at y:128. Makes entry take slightly longer.")
				.define("adaptEntryBlockHeight",true);
		stopSecondEntry = SERVER_BUILDER.comment("If this is true, players may only use an artifact once, even if they end up in the overworld again.")
				.define("stopSecondEntry",false);
		needComputer = SERVER_BUILDER.comment("If this is true, players need to have a computer nearby to Enter")
				.define("needComputer", false);
		artifactRange = SERVER_BUILDER.comment("Radius of the land brought into the medium.")
				.defineInRange("artifactRange",30,0,Integer.MAX_VALUE);
		SERVER_BUILDER.pop();
		
		SERVER_BUILDER.push("medium");
		canBreakGates = SERVER_BUILDER.comment("Lets gates be destroyed by explosions. Turning this off will make gates use the same explosion resistance as bedrock.")
				.define("canBreakGates",true);
		disableGiclops = SERVER_BUILDER.comment("Right now, the giclops pathfinding is currently causing huge amounts of lag due to their size. This option is a short-term solution that will disable giclops spawning and remove all existing giclopes.")
				.define("disableGiclops",true);
		allowSecondaryConnections = SERVER_BUILDER.comment("Set this to true to allow so-called 'secondary connections' to be created.")
				.define("secondaryConnections", true);
		SERVER_BUILDER.pop();
		
		SERVER_CONFIG = SERVER_BUILDER.build();
		
		
		ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();
		CLIENT_BUILDER.push("client");
		alchemyIcons = CLIENT_BUILDER.comment("Set this to true to replace grist names in alchemiter/grist widget with the grist icon.")
				.define("alchemyIcons", true);
		loginColorSelector = CLIENT_BUILDER.comment("Determines if the color selector should be displayed when entering a save file for the first time.")
				.define("loginColorSelector", true);
		echeladderAnimation = CLIENT_BUILDER.comment("Allows control of standard speed for the echeladder rung \"animation\", or if it should have one in the first place.")
				.defineEnum("echeladderAnimation", AnimationSpeed.NORMAL);
		CLIENT_BUILDER.pop();
		
		CLIENT_CONFIG = CLIENT_BUILDER.build();
	}
	
	@SubscribeEvent
	public static void onReload(final ModConfig.ConfigReloading event)
	{
		MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
		if(server != null && server.isOnExecutionThread())	//TODO Check if this will be true after server start. If not, use a static boolean together with a tick event instead
			DeployList.onConditionsUpdated(server);
	}
	
	/**
	 * To determine how much of the sylladex that is dropped on player death (when keepInventory is turned off)
	 */
	public enum DropMode
	{
		/**
		 * Only captchalogued items are dropped.
		 */
		ITEMS,
		/**
		 * Both captchalogued items and cards are dropped. (So that you have at most initialModusSize amount of cards)
		 */
		CARDS_AND_ITEMS,
		/**
		 * Everything is dropped, even the modus card.
		 */
		ALL
	}
	
	public enum AvailableOptions
	{
		BOTH,
		ON,
		OFF
	}
	
	public enum PermissionType
	{
		NONE,
		OPS,
		GAMEMODE,
		OPS_OR_GAMEMODE,
		ANYONE
	}
	
	public enum AnimationSpeed
	{
		NOTHING(0),
		SLOW(1),
		NORMAL(2),
		FAST(4);
		
		private final int factor;
		
		AnimationSpeed(int factor)
		{
			this.factor = factor;
		}
		
		public int getSpeed()
		{
			return factor;
		}
	}
}