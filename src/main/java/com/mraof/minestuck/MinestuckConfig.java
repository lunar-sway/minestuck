package com.mraof.minestuck;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import com.mraof.minestuck.editmode.EditData;
import com.mraof.minestuck.editmode.ServerEditHandler;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.OpEntry;
import net.minecraft.world.GameType;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.ForgeConfigSpec;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.minecraftforge.common.ForgeConfigSpec.*;

public class MinestuckConfig
{
	public static final ForgeConfigSpec CLIENT_CONFIG;
	public static final ForgeConfigSpec SERVER_CONFIG;
	
	private static final ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();
	private static final ForgeConfigSpec.Builder SERVER_BUILDER = new ForgeConfigSpec.Builder();
	
	//            Server	(Anything that need to be synced to the players. Is stored in a per-world config (need to be shared between server-side and client-side))
	//Machines
	public static BooleanValue disableGristWidget;
	public static IntValue alchemiterMaxStacks;
	
	//Sylladex
	public static EnumValue<AvailableOptions> treeModusSetting;
	public static EnumValue<AvailableOptions> hashmapChatModusSetting;
	
	//Mechanics
	public static boolean hardMode = false; //Not fully fleshed out yet
	
	//Computer
	public static BooleanValue allowSecondaryConnections;
	
	//Edit Mode
	public static IntValue overworldEditRange;
	public static IntValue landEditRange;
	public static BooleanValue giveItems;
	public static boolean[] deployConfigurations = new boolean[2];	//TODO Reimplement this (Call DeployList.applyConfigValues(deployConfigurations); at an appropriate time)
	public static BooleanValue deployCard;
	public static BooleanValue portableMachines;
	
	//            Common	(Anything that is needed for both dedicated server and client, but doesn't need to be synced to clients (needed server-side))
	//Machines
	public static BooleanValue cruxtruderIntake;
	public static DimensionType[] forbiddenDimensionsTpz = new DimensionType[0];
	public static ConfigValue<List<Integer>> cfg_forbiddenDimensionsTpz;
	
	//Medium
	public static BooleanValue canBreakGates;
	public static BooleanValue disableGiclops;
	
	//Ores
	public static BooleanValue generateCruxiteOre;
	public static BooleanValue generateUraniumOre;
	
	//Sylladex
	public static BooleanValue dropItemsInCards;
	public static IntValue initialModusSize;
	public static ConfigValue<List<String>> startingModusTypes;
	public static IntValue modusMaxSize;
	public static EnumValue<DropMode> sylladexDropMode;
	
	//Mechanics
	public static boolean forceMaxSize = true;
	public static BooleanValue echeladderProgress;
	public static BooleanValue aspectEffects;
	public static BooleanValue playerSelectedTitle;
	public static IntValue preEntryRungLimit;
	
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
	
	//Secret configuration options
	public static boolean secretConfig = false;
	public static boolean disableCruxite = false;
	public static boolean disableUranium = false;
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
	
	//            Client	(Anything that is only needed for clients (only needed client-side))
	public static byte echeladderAnimation;
	public static ConfigValue<String> cfg_echeladderAnimation;
	public static BooleanValue oldItemModels;
	public static BooleanValue loginColorSelector;
	public static boolean dataCheckerAccess;
	public static BooleanValue alchemyIcons;
	
	static
	{
		SERVER_BUILDER.comment("Ores");
		generateCruxiteOre = SERVER_BUILDER.comment("If cruxite ore should be generated in the overworld.")
				.define("ores.generateCruxiteOre",true);
		generateUraniumOre = SERVER_BUILDER.comment("If uranium ore should be generated in the overworld.")
				.define("ores.generateUraniumOre",true);
		
		SERVER_BUILDER.comment("Mechanics");
		echeladderProgress = SERVER_BUILDER.comment("If this is true, players will be able to see their progress towards the next rung. This is server side and will only be active in multiplayer if the server/Lan host has it activated.")
				.define("mechanics.echeladderProgress", false);
		preEntryRungLimit = SERVER_BUILDER.comment("The highest rung you can get before entering medium. Note that the first rung is indexed as 0, the second as 1 and so on.")
				.defineInRange("mechanics.preEntryRungLimit", 6, 0, 49);
		aspectEffects = SERVER_BUILDER.comment("If this is true, players will gain certain potion effects once they reach a certain rung based on their aspect.")
				.define("mechanics.aspectEffects", true);
		playerSelectedTitle = SERVER_BUILDER.comment("Enable this to let players select their own title. They will however not be able to select the Lord or Muse as class.")
				.define("mechanics.playerSelectedTitle", false);
		
		
		SERVER_BUILDER.push("sylladex");
		dropItemsInCards = SERVER_BUILDER.comment("When sylladices may drop items and cards at the same time, this option determines if items should be dropped inside of cards or items and cards as different stacks.")
				.define("drop_items_in_cards", true);
		initialModusSize = SERVER_BUILDER.comment("The initial amount of captchalogue cards in your sylladex.")
				.defineInRange("initial_modus_size", 5, 0, Integer.MAX_VALUE);
		startingModusTypes = SERVER_BUILDER.comment("An array with the possible modus types to be assigned. Written with mod-id and modus name, for example \"minestuck:queue_stack\" or \"minestuck:hashmap\"")
				.define("starting_modus_types", new ArrayList<>(Arrays.asList("minestuck:stack","minestuck:queue")));
		modusMaxSize = SERVER_BUILDER.comment("The max size on a modus. Ignored if the value is 0.")
				.defineInRange("modus_max_size", 0, 0, Integer.MAX_VALUE);
		treeModusSetting = SERVER_BUILDER.comment("This determines if auto-balance should be forced. 'both' if the player should choose, 'on' if forced at on, and 'off' if forced at off.")
				.defineEnum("tree_modus_setting", AvailableOptions.BOTH);
		hashmapChatModusSetting = SERVER_BUILDER.comment("This determines if hashmap chat ejection should be forced. 'both' if the player should choose, 'on' if forced at on, and 'off' if forced at off.")
				.defineEnum("hashmap_modus_setting", AvailableOptions.BOTH);
		sylladexDropMode = SERVER_BUILDER.comment("Determines which items from the modus that are dropped on death. \"items\": Only the items are dropped. \"cards_and_items\": Both items and cards are dropped. (So that you have at most initial_modus_size amount of cards) \"all\": Everything is dropped, even the modus.")
				.defineEnum("drop_mode", DropMode.CARDS_AND_ITEMS);
		SERVER_BUILDER.pop();
		
		SERVER_BUILDER.push("computer");
		privateComputers = SERVER_BUILDER.comment("True if computers should only be able to be used by the owner.")
				.define("private_computers", true);
		globalSession = SERVER_BUILDER.comment("Whenether all connetions should be put into a single session or not.")
				.define("global_session",false);
		allowSecondaryConnections = SERVER_BUILDER.comment("Set this to true to allow so-called 'secondary connections' to be created.")
				.define("secondary_connections", true);
		skaianetCheck = SERVER_BUILDER.comment("If enabled, will during certain moments perform a check on all connections and computers that are in use. Recommended to turn off if there is a need to improve performance, however skaianet-related bugs might appear when done so.")
				.define("skaianet_check",true);
		dataCheckerPermission = SERVER_BUILDER.comment("Determines who's allowed to access the data checker. \"none\": No one is allowed. \"ops\": only those with a command permission of level 2 or more may access the data ckecker. (for single player, that would be if cheats are turned on) \"gamemode\": Only players with the creative or spectator gamemode may view the data checker. \"ops_or_gamemode\": Both ops and players in creative or spectator mode may view the data checker. \"anyone\": No access restrictions are used.")
				.defineEnum("data_checker_permission", PermissionType.OPS_OR_GAMEMODE);
		SERVER_BUILDER.pop();
		
		SERVER_BUILDER.comment("Edit Mode");
		giveItems = SERVER_BUILDER.comment("Setting this to true replaces editmode with the old Give Items button.")
				.define("editMode.giveItems", false);
		showGristChanges = SERVER_BUILDER.comment("If this is true, grist change messages will appear.")
				.define("editMode.showGristChanges",true);
		gristRefund = SERVER_BUILDER.comment("Enable this and players will get a (full) grist refund from breaking blocks in editmode.")
				.define("editMode.gristRefund", false);
		deployCard = SERVER_BUILDER.comment("Determines if a card with a captcha card punched on it should be added to the deploy list.")
				.define("editMode.deployCard",false);
		portableMachines = SERVER_BUILDER.comment("Determines if the small portable machines should be included in the deploy list.")
				.define("editMode.portableMachines", false);
		overworldEditRange = SERVER_BUILDER.comment("A number that determines how far away from the computer an editmode player may be before entry.")
				.defineInRange("editMode.overworldEditRange", 15, 1, Integer.MAX_VALUE);
		landEditRange = SERVER_BUILDER.comment("A number that determines how far away from the center of the brought land that an editmode player may be after entry.")
				.defineInRange("editMode.landEditRange", 30, 1, Integer.MAX_VALUE);
		
		
		SERVER_BUILDER.comment("Machines");
		disableGristWidget = SERVER_BUILDER.comment("Disable Grist Widget")
				.define("machines.disableGristWidget",false);
		cruxtruderIntake = SERVER_BUILDER.comment("If enabled, the regular cruxtruder will require raw cruxite to function, which is inserted through the pipe.")
				.define("machines.cruxtruderIntake",false);
		alchemiterMaxStacks = SERVER_BUILDER.comment("The number of stacks that can be alchemized at the same time with the alchemiter.")
				.defineInRange("machines.alchemiterMaxStacks",16,0,999);
		cfg_forbiddenDimensionsTpz = SERVER_BUILDER.comment("A list of dimension id's that you cannot travel to or from using transportalizers.")
				.define("machines.forbiddenDimensionsTpz", new ArrayList<>());
		
		SERVER_BUILDER.comment("Entry");
		entryCrater = SERVER_BUILDER.comment("Disable this to prevent craters from people entering the medium.")
				.define("entry.entryCrater",true);
		adaptEntryBlockHeight = SERVER_BUILDER.comment("Adapt the transferred height to make the top non-air block to be placed at y:128. Makes entry take slightly longer.")
				.define("entry.adaptEntryBlockHeight",true);
		stopSecondEntry = SERVER_BUILDER.comment("If this is true, players may only use an artifact once, even if they end up in the overworld again.")
				.define("entry.stopSecondEntry",false);
		needComputer = SERVER_BUILDER.comment("If this is true, players need to have a computer nearby to Enter")
				.define("entry.needComputer", false);
		artifactRange = SERVER_BUILDER.comment("Radius of the land brought into the medium.")
				.defineInRange("entry.artifactRange",30,0,Integer.MAX_VALUE);
		
		SERVER_BUILDER.comment("The Medium");
		canBreakGates = SERVER_BUILDER.comment("Lets gates be destroyed by explosions. Turning this off will make gates use the same explosion resistance as bedrock.")
				.define("medium.canBreakGates",true);
		disableGiclops = SERVER_BUILDER.comment("Right now, the giclops pathfinding is currently causing huge amounts of lag due to their size. This option is a short-term solution that will disable giclops spawning and remove all existing giclopes.")
				.define("medium.disableGiclops",true);
		
		CLIENT_BUILDER.comment("Client Side");
		oldItemModels = CLIENT_BUILDER.comment("Set this to true to have back all old 2D item models.")
				.define("client.oldItemModels", false);
		alchemyIcons = CLIENT_BUILDER.comment("Set this to true to replace grist names in alchemiter/grist widget with the grist icon.")
				.define("client.alchemyIcons", true);
		loginColorSelector = CLIENT_BUILDER.comment("Determines if the color selector should be displayed when entering a save file for the first time.")
				.define("client.loginColorSelector", true);
		cfg_echeladderAnimation = CLIENT_BUILDER.comment("Allows control of standard speed for the echeladder rung \"animation\", or if it should have one in the first place.")
				.comment("Range: [\"nothing\", \"slow\", \"normal\", \"fast\"]")
				.define("client.echeladderAnimation", "normal");
		
		CLIENT_CONFIG = CLIENT_BUILDER.build();
		SERVER_CONFIG = SERVER_BUILDER.build();
	}
	
	public static void setClientValues()
	{
		String ea = cfg_echeladderAnimation.get().toLowerCase();
		switch(ea)
		{
			case "nothing": echeladderAnimation = 0;
				break;
			case "slow": echeladderAnimation = 4;
				break;
			case "fast": echeladderAnimation = 1;
				break;
			default: echeladderAnimation = 2;
		}
	}
	
	public static void setConfigVariables()
	{
		List<Integer> fdt = cfg_forbiddenDimensionsTpz.get();
		forbiddenDimensionsTpz = new DimensionType[fdt.size()];
		for(int i = 0; i < fdt.size(); i++)
			forbiddenDimensionsTpz[i] = DimensionType.getById(fdt.get(i));
	}
	
	public static void loadConfig(ForgeConfigSpec config, Path path)
	{
		final CommentedFileConfig file = CommentedFileConfig.builder(path).sync().autosave().writingMode(WritingMode.REPLACE).build();
		file.load();
		config.setConfig(file);
	}
	
	public static boolean getDataCheckerPermissionFor(ServerPlayerEntity player)
	{
		switch(dataCheckerPermission.get())
		{
			case ANYONE: return true;
			case OPS: return hasOp(player);
			case GAMEMODE: return hasGamemodePermission(player);
			case OPS_OR_GAMEMODE: return hasOp(player) || hasGamemodePermission(player);
			case NONE: default: return false;
		}
	}
	
	private static boolean hasGamemodePermission(ServerPlayerEntity player)
	{
		GameType gameType = player.interactionManager.getGameType();
		
		EditData data = ServerEditHandler.getData(player);
		if(data != null)
			gameType = data.getDecoy().gameType;
		
		return !gameType.isSurvivalOrAdventure();
	}
	
	private static boolean hasOp(ServerPlayerEntity player)
	{
		MinecraftServer server = player.getServer();
		if(server != null && server.getPlayerList().canSendCommands(player.getGameProfile()))
		{
			OpEntry entry = server.getPlayerList().getOppedPlayers().getEntry(player.getGameProfile());
			return (entry != null ? entry.getPermissionLevel() : server.getOpPermissionLevel()) >= 2;
		}
		return false;
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
}