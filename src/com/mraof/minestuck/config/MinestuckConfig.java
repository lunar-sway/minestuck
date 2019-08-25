package com.mraof.minestuck.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import com.mraof.minestuck.Minestuck;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.ForgeConfigSpec;

import javax.security.auth.login.Configuration;

import static net.minecraftforge.common.ForgeConfigSpec.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
	public static IntValue cardCost;
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
	public static BooleanValue hardMode;
	public static BooleanValue forceMaxSize;
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
	/**
	 * 0: Make the player's new server player his/her old server player's server player
	 * 1: The player that lost his/her server player will have an idle main connection until someone without a client player connects to him/her.
	 * (Will try to put a better explanation somewhere else later)
	 */
	public static IntValue escapeFailureMode;
	
	//Edit Mode
	public static BooleanValue giveItems;
	public static BooleanValue showGristChanges;
	public static BooleanValue gristRefund;
	public static boolean[] deployConfigurations = new boolean[2];
	
	//Client side
	public static IntValue clientOverworldEditRange;
	public static IntValue clientLandEditRange;
	public static IntValue clientCardCost;
	public static IntValue clientAlchemiterStacks;
	public static byte clientTreeAutobalance;
	public static byte clientHashmapChat;
	public static byte echeladderAnimation;
	public static BooleanValue clientGiveItems;
	public static BooleanValue clientDisableGristWidget;
	public static BooleanValue clientHardMode;
	public static BooleanValue oldItemModels;
	public static BooleanValue loginColorSelector;
	public static BooleanValue dataCheckerAccess;
	public static BooleanValue alchemyIcons;
	public static BooleanValue preEntryEcheladder;
	
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
		cardCost = server_builder.comment("An integer that determines how much a captchalouge card costs to alchemize")
				.defineInRange("sylladex.cardCost", 1, 0, Integer.MAX_VALUE);
		cfg_treeModusSetting = server_builder.comment("This determines if auto-balance should be forced. 'both' if the player should choose, 'on' if forced at on, and 'off' if forced at off.")
				.define("sylladex.treeModusSetting", "both");
		cfg_hashmapChatModusSetting = server_builder.comment("This determines if hashmap chat ejection should be forced. 'both' if the player should choose, 'on' if forced at on, and 'off' if forced at off.")
				.define("sylladex.forceEjectByChat", "both");
		cfg_sylladexDropMode = server_builder.comment("Determines which items from the modus that are dropped on death. \"items\": Only the items are dropped. \"cardsAndItems\": Both items and cards are dropped. (So that you have at most initialModusSize amount of cards) \"all\": Everything is dropped, even the modus.")
				.define("sylladex.sylladexDropMode", "cardsAndItems");
		
		
		server_builder.comment("Computer");
		
		
		server_builder.comment("Edit Mode");
		
		
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
		
		
		server_builder.comment("The Medium");
		keepDimensionsLoaded = server_builder.comment("Keep the Medium Loaded")
				.define("medium.keepDimensionsLoaded",true);
		canBreakGates = server_builder.comment("Lets gates be destroyed by explosions. Turning this off will make gates use the same explosion resistance as bedrock.")
				.define("medium.canBreakGates",true);
		disableGiclops = server_builder.comment("Right now, the giclops pathfinding is currently causing huge amounts of lag due to their size. This option is a short-term solution that will disable giclops spawning and remove all existing giclopes.")
				.define("medium.disableGiclops",true);
		
		client_config = client_builder.build();
		server_config = server_builder.build();
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
		switch(tms)
		{
			case "all": sylladexDropMode = 2;
				break;
			case "items": sylladexDropMode = 0;
				break;
			default: sylladexDropMode = 1;
		}
		
	}
	public static void loadConfig(ForgeConfigSpec config, String path)
	{
		final CommentedFileConfig file = CommentedFileConfig.builder(new File(path)).sync().autosave().writingMode(WritingMode.REPLACE).build();
		file.load();
		config.setConfig(file);
	}
}
