package com.mraof.minestuck;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import com.mraof.minestuck.editmode.ServerEditHandler;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.OpEntry;
import net.minecraft.world.GameType;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.ForgeConfigSpec;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.minecraftforge.common.ForgeConfigSpec.*;

public class MinestuckConfig
{
	private static final ForgeConfigSpec.Builder client_builder = new ForgeConfigSpec.Builder();
	public static final ForgeConfigSpec client_config;
	private static final ForgeConfigSpec.Builder server_builder = new ForgeConfigSpec.Builder();
	public static final ForgeConfigSpec server_config;
	
	//Machines
	public static BooleanValue disableGristWidget;
	public static BooleanValue cruxtruderIntake;
	public static IntValue alchemiterMaxStacks;
	public static DimensionType[] forbiddenDimensionsTpz = new DimensionType[0];
	public static ConfigValue<List<Integer>> cfg_forbiddenDimensionsTpz;
	
	//Medium
	public static BooleanValue keepDimensionsLoaded;
	public static BooleanValue canBreakGates;
	public static BooleanValue disableGiclops;
	
	//Ores
	public static BooleanValue generateCruxiteOre;
	public static BooleanValue generateUraniumOre;
	public static IntValue oreMultiplier;
	
	//Sylladex
	public static BooleanValue specialCardRenderer;
	public static BooleanValue dropItemsInCards;
	public static IntValue cardResolution;
	public static IntValue initialModusSize;
	public static String[] defaultModusTypes = new String[0];
	public static ConfigValue<List<String>> cfg_defaultModusTypes;
	public static IntValue modusMaxSize;
	public static byte treeModusSetting;
	public static ConfigValue<String> cfg_treeModusSetting;
	public static byte hashmapChatModusSetting;
	public static ConfigValue<String> cfg_hashmapChatModusSetting;
	/**
	 * An option related to dropping the sylladex on death
	 * If 0: only captchalouged items are dropped. If 1: Both captchalouged items and cards are dropped. If 2: All items, including the actual modus.
	 */
	public static byte sylladexDropMode;
	public static ConfigValue<String> cfg_sylladexDropMode;
	
	//Mechanics
	public static boolean hardMode = false; //Not fully fleshed out yet
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
	public static IntValue overworldEditRange;
	public static IntValue landEditRange;
	
	//Computer
	public static BooleanValue privateComputers;
	public static BooleanValue globalSession;
	public static BooleanValue allowSecondaryConnections;	//TODO make open server not an option after having a main connection (for when this is set to false)
	public static BooleanValue useUUID;
	public static BooleanValue skaianetCheck;
	public static byte dataCheckerPermission = 4;
	public static ConfigValue<String> cfg_dataCheckerPermission;
	/**
	 * 0: Make the player's new server player his/her old server player's server player
	 * 1: The player that lost his/her server player will have an idle main connection until someone without a client player connects to him/her.
	 * (Will try to put a better explanation somewhere else later)
	 */
	public static int escapeFailureMode;
	
	//Edit Mode
	public static BooleanValue giveItems;
	public static BooleanValue showGristChanges;
	public static BooleanValue gristRefund;
	public static boolean[] deployConfigurations = new boolean[2];	//TODO Reimplement this (Call DeployList.applyConfigValues(deployConfigurations); at an appropriate time)
	public static BooleanValue deployCard;
	public static BooleanValue portableMachines;
	
	//Client side
	public static int clientOverworldEditRange;
	public static int clientLandEditRange;
	public static int clientAlchemiterStacks;
	public static byte clientTreeAutobalance;
	public static byte clientHashmapChat;
	public static byte echeladderAnimation;
	public static ConfigValue<String> cfg_echeladderAnimation;
	public static boolean clientGiveItems;
	public static boolean clientDisableGristWidget;
	public static boolean clientHardMode;
	public static BooleanValue oldItemModels;
	public static BooleanValue loginColorSelector;
	public static boolean dataCheckerAccess;
	public static BooleanValue alchemyIcons;
	public static boolean preEntryEcheladder;
	
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
	
	
	static
	{
		server_builder.comment("Ores");
		generateCruxiteOre = server_builder.comment("If cruxite ore should be generated in the overworld.")
				.define("ores.generateCruxiteOre",true);
		generateUraniumOre = server_builder.comment("If uranium ore should be generated in the overworld.")
				.define("ores.generateUraniumOre",true);
		oreMultiplier = server_builder.comment("Multiplies the cost for the 'contents' of an ore. Set to 0 to disable alchemizing ores.")
				.defineInRange("ores.oreMultiplier",1,0,Integer.MAX_VALUE);
		
		server_builder.comment("Mechanics");
		echeladderProgress = server_builder.comment("If this is true, players will be able to see their progress towards the next rung. This is server side and will only be active in multiplayer if the server/Lan host has it activated.")
				.define("mechanics.echeladderProgress", false);
		preEntryRungLimit = server_builder.comment("The highest rung you can get before entering medium. Note that the first rung is indexed as 0, the second as 1 and so on.")
				.defineInRange("mechanics.preEntryRungLimit", 6, 0, 49);
		aspectEffects = server_builder.comment("If this is true, players will gain certain potion effects once they reach a certain rung based on their aspect.")
				.define("mechanics.aspectEffects", true);
		playerSelectedTitle = server_builder.comment("Enable this to let players select their own title. They will however not be able to select the Lord or Muse as class.")
				.define("mechanics.playerSelectedTitle", false);
		
		
		server_builder.comment("Sylladex");
		//specialCardRenderer = server_builder.comment("Whenether to use the special render for cards or not.").define("sylladex.specialCardRenderer", false);
		dropItemsInCards = server_builder.comment("When sylladexes are droppable, this option determines if items should be dropped inside of cards or items and cards as different stacks.")
				.define("sylladex.dropItemsInCards", true);
		//cardResolution = server_builder.comment("The resolution of the item inside of a card. The width/height is computed by '8*2^x', where 'x' is this config value.").defineInRange("sylladex.cardResolution", 1, 0 ,5);
		initialModusSize = server_builder.comment("The initial ammount of captchalogue cards in your sylladex.")
				.defineInRange("sylladex.initialModusSize", 5, 0, Integer.MAX_VALUE);
		cfg_defaultModusTypes = server_builder.comment("An array with the possible modus types to be assigned. Written with mod-id and modus name, for example \"minestuck:queue_stack\" or \"minestuck:hashmap\"")
				.define("sylladex.defaultModusTypes", new ArrayList<>(Arrays.asList("minestuck:stack","minestuck:queue")));
		modusMaxSize = server_builder.comment("The max size on a modus. Ignored if the value is 0.")
				.defineInRange("sylladex.modusMaxSize", 0, 0, Integer.MAX_VALUE);
		cfg_treeModusSetting = server_builder.comment("This determines if auto-balance should be forced. 'both' if the player should choose, 'on' if forced at on, and 'off' if forced at off.")
				.define("sylladex.treeModusSetting", "both");
		cfg_hashmapChatModusSetting = server_builder.comment("This determines if hashmap chat ejection should be forced. 'both' if the player should choose, 'on' if forced at on, and 'off' if forced at off.")
				.define("sylladex.forceEjectByChat", "both");
		cfg_sylladexDropMode = server_builder.comment("Determines which items from the modus that are dropped on death. \"items\": Only the items are dropped. \"cardsAndItems\": Both items and cards are dropped. (So that you have at most initialModusSize amount of cards) \"all\": Everything is dropped, even the modus.")
				.define("sylladex.sylladexDropMode", "cardsAndItems");
		
		
		server_builder.comment("Computer");
		privateComputers = server_builder.comment("True if computers should only be able to be used by the owner.")
				.define("computer.privateComputers", true);
		globalSession = server_builder.comment("Whenether all connetions should be put into a single session or not.")
				.define("computer.globalSession",false);
		allowSecondaryConnections = server_builder.comment("Set this to true to allow so-called 'secondary connections' to be created.")
				.define("computer.secondaryConnections", true);
		useUUID = server_builder.comment("If this is set to true, minestuck will use uuids to refer to players in the saved data. On false it will instead use the old method based on usernames.")
				.define("computer.useUUID", true);
		skaianetCheck = server_builder.comment("If enabled, will during certain moments perform a check on all connections and computers that are in use. Recommended to turn off if there is a need to improve performance, however skaianet-related bugs might appear when done so.")
				.define("computer.skaianetCheck",true);
		cfg_dataCheckerPermission = server_builder.comment("Determines who's allowed to access the data checker. \"none\": No one is allowed. \"ops\": only those with a command permission of level 2 or more may access the data ckecker. (for single player, that would be if cheats are turned on) \"gamemode\": Only players with the creative or spectator gamemode may view the data checker. \"opsAndGamemode\": Combination of \"ops\" and \"gamemode\". \"anyone\": No access restrictions are used.")
				.define("computer.dataCheckerPermission", "opsAndGamemode");
		
		
		server_builder.comment("Edit Mode");
		giveItems = server_builder.comment("Setting this to true replaces editmode with the old Give Items button.")
				.define("editMode.giveItems", false);
		showGristChanges = server_builder.comment("If this is true, grist change messages will appear.")
				.define("editMode.showGristChanges",true);
		gristRefund = server_builder.comment("Enable this and players will get a (full) grist refund from breaking blocks in editmode.")
				.define("editMode.gristRefund", false);
		deployCard = server_builder.comment("Determines if a card with a captcha card punched on it should be added to the deploy list.")
				.define("editMode.deployCard",false);
		portableMachines = server_builder.comment("Determines if the small portable machines should be included in the deploy list.")
				.define("editMode.portableMachines", false);
		overworldEditRange = server_builder.comment("A number that determines how far away from the computer an editmode player may be before entry.")
				.defineInRange("editMode.overworldEditRange", 15, 1, Integer.MAX_VALUE);
		landEditRange = server_builder.comment("A number that determines how far away from the center of the brought land that an editmode player may be after entry.")
				.defineInRange("editMode.landEditRange", 30, 1, Integer.MAX_VALUE);
		
		
		server_builder.comment("Machines");
		disableGristWidget = server_builder.comment("Disable Grist Widget")
				.define("machines.disableGristWidget",false);
		cruxtruderIntake = server_builder.comment("If enabled, the regular cruxtruder will require raw cruxite to function, which is inserted through the pipe.")
				.define("machines.cruxtruderIntake",false);
		alchemiterMaxStacks = server_builder.comment("The number of stacks that can be alchemized at the same time with the alchemiter.")
				.defineInRange("machines.alchemiterMaxStacks",16,0,999);
		cfg_forbiddenDimensionsTpz = server_builder.comment("A list of dimension id's that you cannot travel to or from using transportalizers.")
				.define("machines.forbiddenDimensionsTpz", new ArrayList<>());
		
		server_builder.comment("Entry");
		entryCrater = server_builder.comment("Disable this to prevent craters from people entering the medium.")
				.define("entry.entryCrater",true);
		adaptEntryBlockHeight = server_builder.comment("Adapt the transferred height to make the top non-air block to be placed at y:128. Makes entry take slightly longer.")
				.define("entry.adaptEntryBlockHeight",true);
		stopSecondEntry = server_builder.comment("If this is true, players may only use an artifact once, even if they end up in the overworld again.")
				.define("entry.stopSecondEntry",false);
		needComputer = server_builder.comment("If this is true, players need to have a computer nearby to Enter")
				.define("entry.needComputer", false);
		artifactRange = server_builder.comment("Radius of the land brought into the medium.")
				.defineInRange("entry.artifactRange",30,0,Integer.MAX_VALUE);
		
		server_builder.comment("The Medium");
		keepDimensionsLoaded = server_builder.comment("Keep the Medium Loaded")
				.define("medium.keepDimensionsLoaded",true);
		canBreakGates = server_builder.comment("Lets gates be destroyed by explosions. Turning this off will make gates use the same explosion resistance as bedrock.")
				.define("medium.canBreakGates",true);
		disableGiclops = server_builder.comment("Right now, the giclops pathfinding is currently causing huge amounts of lag due to their size. This option is a short-term solution that will disable giclops spawning and remove all existing giclopes.")
				.define("medium.disableGiclops",true);
		
		client_builder.comment("Client Side");
		oldItemModels = client_builder.comment("Set this to true to have back all old 2D item models.")
				.define("client.oldItemModels", false);
		alchemyIcons = client_builder.comment("Set this to true to replace grist names in alchemiter/grist widget with the grist icon.")
				.define("client.alchemyIcons", true);
		loginColorSelector = client_builder.comment("Determines if the color selector should be displayed when entering a save file for the first time.")
				.define("client.loginColorSelector", true);
		cfg_echeladderAnimation = client_builder.comment("Allows control of standard speed for the echeladder rung \"animation\", or if it should have one in the first place.")
				.comment("Range: [\"nothing\", \"slow\", \"normal\", \"fast\"]")
				.define("client.echeladderAnimation", "normal");
		
		client_config = client_builder.build();
		server_config = server_builder.build();
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
		
		List<String> dmt = cfg_defaultModusTypes.get();
		defaultModusTypes = new String[dmt.size()];
		for(int i = 0; i < dmt.size(); i++)
			defaultModusTypes[i] = dmt.get(i);
		
		String tms = cfg_treeModusSetting.get().toLowerCase();
		switch(tms)
		{
			case "on": treeModusSetting = 1;
				break;
			case "off": treeModusSetting = 2;
				break;
			default: treeModusSetting = 0;
		}
		String hms = cfg_hashmapChatModusSetting.get().toLowerCase();
		switch(tms)
		{
			case "on": hashmapChatModusSetting = 1;
				break;
			case "off": hashmapChatModusSetting = 2;
				break;
			default: hashmapChatModusSetting = 0;
		}
		String sdm = cfg_sylladexDropMode.get().toLowerCase();
		switch(sdm)
		{
			case "all": sylladexDropMode = 2;
				break;
			case "items": sylladexDropMode = 0;
				break;
			default: sylladexDropMode = 1;
		}
		
		String dcp = cfg_dataCheckerPermission.get().toLowerCase();
		switch(dcp)
		{
			case "none": dataCheckerPermission = 0;
			case "ops": dataCheckerPermission = 1;
			case "gamemode": dataCheckerPermission = 2;
			case "anyone": dataCheckerPermission = 4;
			default: dataCheckerPermission = 3;
		}
	}
	public static void loadConfig(ForgeConfigSpec config, String path)	//TODO Use ModLoadingContext.registerConfig?
	{
		final CommentedFileConfig file = CommentedFileConfig.builder(new File(path)).sync().autosave().writingMode(WritingMode.REPLACE).build();
		file.load();
		config.setConfig(file);
	}
	
	public static boolean getDataCheckerPermissionFor(ServerPlayerEntity player)
	{
		if((dataCheckerPermission & 3) != 0)
		{
			if((dataCheckerPermission & 1) != 0)
			{
				MinecraftServer server = player.getServer();
				if (server.getPlayerList().canSendCommands(player.getGameProfile()))
				{
					OpEntry userlistopsentry = server.getPlayerList().getOppedPlayers().getEntry(player.getGameProfile());
					if((userlistopsentry != null ? userlistopsentry.getPermissionLevel() : server.getOpPermissionLevel()) >= 2)
						return true;
				}
			}
			if((dataCheckerPermission & 2) != 0)
			{
				GameType gameType = player.interactionManager.getGameType();
				if(ServerEditHandler.getData(player) != null)
					gameType = ServerEditHandler.getData(player).getDecoy().gameType;
				if(!gameType.isSurvivalOrAdventure())
					return true;
			}
			return false;
		} else return dataCheckerPermission != 0;
	}
}

