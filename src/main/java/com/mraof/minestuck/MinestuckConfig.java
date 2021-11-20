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
	//Common is for configuration that doesn't need to sync to client, and doesn't make a difference in gameplay
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
	
	public static class Client
	{
		public final EnumValue<AnimationSpeed> echeladderAnimation;
		public final BooleanValue loginColorSelector;
		public final BooleanValue alchemyIcons;
		
		private Client(Builder builder)
		{
			builder.push("client");
			alchemyIcons = builder.comment("Set this to true to replace grist names in alchemiter/grist widget with the grist icon.")
					.define("alchemyIcons", true);
			loginColorSelector = builder.comment("Determines if the color selector should be displayed when entering a save file for the first time.")
					.define("loginColorSelector", true);
			echeladderAnimation = builder.comment("Allows control of standard speed for the echeladder rung \"animation\", or if it should have one in the first place.")
					.defineEnum("echeladderAnimation", AnimationSpeed.NORMAL);
			builder.pop();
		}
	}
	
	public static class Server
	{
		//Machines
		public final BooleanValue cruxtruderIntake;
		public final ConfigValue<List<String>> forbiddenDimensionTypesTpz;
		public final ConfigValue<List<String>> forbiddenModDimensionsTpz;
		public final BooleanValue disableGristWidget;
		public final IntValue alchemiterMaxStacks;
		
		//Medium
		public final BooleanValue canBreakGates;
		public final BooleanValue disableGiclops;
		public final BooleanValue naturalImpSpawn;
		public final BooleanValue naturalOgreSpawn;
		public final BooleanValue naturalBasiliskSpawn;
		public final BooleanValue naturalLichSpawn;
		public final BooleanValue allowSecondaryConnections;
		
		//Ores
		public final BooleanValue generateCruxiteOre;
		public final BooleanValue generateUraniumOre;
		
		//Sylladex
		public final BooleanValue dropItemsInCards;
		public final IntValue initialModusSize;
		public final ConfigValue<List<String>> startingModusTypes;
		public final IntValue modusMaxSize;
		public final EnumValue<DropMode> sylladexDropMode;
		public final EnumValue<AvailableOptions> treeModusSetting;
		public final EnumValue<AvailableOptions> hashmapChatModusSetting;
		
		//Mechanics
		public final boolean forceMaxSize = true;
		public final boolean hardMode = false; //Not fully fleshed out yet
		public final BooleanValue echeladderProgress;
		public final BooleanValue aspectEffects;
		public final BooleanValue playerSelectedTitle;
		public final IntValue preEntryRungLimit;
		public final BooleanValue rungHealthOnRespawn;
		public final IntValue dialogueRenewalSpeed;
		public final IntValue lotusRestorationTime;
		public final IntValue wirelessBlocksTickRate;
		
		//Entry
		public final BooleanValue entryCrater;
		public final BooleanValue adaptEntryBlockHeight;
		public final BooleanValue stopSecondEntry;
		public final BooleanValue needComputer;
		public final IntValue artifactRange;
		
		//Computer
		public final BooleanValue privateComputers;
		public final BooleanValue globalSession;
		public final BooleanValue skaianetCheck;
		public final EnumValue<PermissionType> dataCheckerPermission;
		
		public final EnumValue<FailedEscapeMode> escapeFailureMode = null;	//TODO once a connection can close from meteor failure
		
		//Edit Mode
		public final BooleanValue showGristChanges;
		public final BooleanValue gristRefund;
		public final BooleanValue deployCard;
		public final BooleanValue portableMachines;
		public final IntValue overworldEditRange;
		public final IntValue landEditRange;
		public final BooleanValue giveItems;
		
		private Server(Builder builder)
		{
			builder.push("ores");
			generateCruxiteOre = builder.comment("If cruxite ore should be generated in the overworld.")
					.define("generateCruxiteOre",true);
			generateUraniumOre = builder.comment("If uranium ore should be generated in the overworld.")
					.define("generateUraniumOre",true);
			builder.pop();
			
			builder.push("mechanics");
			echeladderProgress = builder.comment("If this is true, players will be able to see their progress towards the next rung. This is server side and will only be active in multiplayer if the server/Lan host has it activated.")
					.define("echeladderProgress", true);
			preEntryRungLimit = builder.comment("The highest rung you can get before entering medium. Note that the first rung is indexed as 0, the second as 1 and so on.")
					.defineInRange("preEntryRungLimit", 6, 0, 49);
			rungHealthOnRespawn = builder.comment("If true, players will respawn with full health, rung bonuses included. If false, health will be left alone (typically meaning that you respawn with 10 hearts)")
					.define("rungHealthOnRespawn", true);
			aspectEffects = builder.comment("If this is true, players will gain certain potion effects once they reach a certain rung based on their aspect.")
					.define("aspectEffects", true);
			playerSelectedTitle = builder.comment("Enable this to let players select their own title. They will however not be able to select the Lord or Muse as class.")
					.define("playerSelectedTitle", true);
			dialogueRenewalSpeed = builder.comment("Determines how quickly consort dialogue and store stocks are renewed.")
					.defineInRange("dialogueRenewalSpeed", 2, 0, 1000);
			lotusRestorationTime = builder.comment("Determines how many seconds it takes for the lotus blossom to regrow after the opening process has started.")
					.defineInRange("lotusRestorationTime", 600, 30, Integer.MAX_VALUE);
			wirelessBlocksTickRate = builder.comment("Determines the speed at which remote/wireless redstone blocks send/receive updates.")
					.defineInRange("wirelessBlocksTickRate", 6, 1, 200);
			builder.pop();
			
			builder.push("sylladex");
			dropItemsInCards = builder.comment("When sylladices may drop items and cards at the same time, this option determines if items should be dropped inside of cards or items and cards as different stacks.")
					.define("dropItemsInCards", true);
			initialModusSize = builder.comment("The initial amount of captchalogue cards in your sylladex.")
					.defineInRange("initialModusSize", 5, 0, Integer.MAX_VALUE);
			startingModusTypes = builder.comment("An array with the possible modus types to be assigned. Written with mod-id and modus name, for example \"minestuck:queue_stack\" or \"minestuck:hash_map\"")
					.define("startingModusTypes", new ArrayList<>(Arrays.asList("minestuck:stack","minestuck:queue")));
			modusMaxSize = builder.comment("The max size on a modus. Ignored if the value is 0.")
					.defineInRange("modusMaxSize", 0, 0, Integer.MAX_VALUE);
			sylladexDropMode = builder.comment("Determines which items from the modus that are dropped on death. \"items\": Only the items are dropped. \"cards_and_items\": Both items and cards are dropped. (So that you have at most initial_modus_size amount of cards) \"all\": Everything is dropped, even the modus.")
					.defineEnum("dropMode", DropMode.CARDS_AND_ITEMS);
			treeModusSetting = builder.comment("This determines if auto-balance should be forced. 'both' if the player should choose, 'on' if forced at on, and 'off' if forced at off.")
					.defineEnum("treeModusSetting", AvailableOptions.BOTH);
			hashmapChatModusSetting = builder.comment("This determines if hashmap chat ejection should be forced. 'both' if the player should choose, 'on' if forced at on, and 'off' if forced at off.")
					.defineEnum("hashmapModusSetting", AvailableOptions.BOTH);
			builder.pop();
			
			builder.push("computer");
			privateComputers = builder.comment("True if computers should only be able to be used by the owner.")
					.define("privateComputers", true);
			globalSession = builder.comment("Whenever all sburb connections should be put into a single session or not.")
					.define("globalSession",false);
			skaianetCheck = builder.comment("If enabled, will during certain moments perform a check on all connections and computers that are in use. Recommended to turn off if there is a need to improve performance, however skaianet-related bugs might appear when done so.")
					.define("skaianetCheck",true);
			dataCheckerPermission = builder.comment("Determines who's allowed to access the data checker. \"none\": No one is allowed. \"ops\": only those with a command permission of level 2 or more may access the data ckecker. (for single player, that would be if cheats are turned on) \"gamemode\": Only players with the creative or spectator gamemode may view the data checker. \"ops_or_gamemode\": Both ops and players in creative or spectator mode may view the data checker. \"anyone\": No access restrictions are used.")
					.defineEnum("dataCheckerPermission", PermissionType.OPS_OR_GAMEMODE);
			builder.pop();
			
			builder.push("editMode");
			showGristChanges = builder.comment("If this is true, grist change messages will appear.")
					.define("showGristChanges",true);
			gristRefund = builder.comment("Enable this and players will get a (full) grist refund from breaking blocks in editmode.")
					.define("gristRefund", false);
			deployCard = builder.comment("Determines if a card with a captcha card punched on it should be added to the deploy list.")
					.define("deployCard",false);
			portableMachines = builder.comment("Determines if the small portable machines should be included in the deploy list.")
					.define("portableMachines", false);
			giveItems = builder.comment("Setting this to true replaces editmode with the old Give Items button.")
					.define("giveItems", false);
			overworldEditRange = builder.comment("A number that determines how far away from the computer an editmode player may be before entry.")
					.defineInRange("overworldEditRange", 15, 1, Integer.MAX_VALUE);
			landEditRange = builder.comment("A number that determines how far away from the center of the brought land that an editmode player may be after entry.")
					.defineInRange("landEditRange", 30, 1, Integer.MAX_VALUE);
			builder.pop();
			
			builder.push("machines");
			disableGristWidget = builder.comment("Disable Grist Widget")
					.define("disableGristWidget",false);
			alchemiterMaxStacks = builder.comment("The number of stacks that can be alchemized at the same time with the alchemiter.")
					.defineInRange("alchemiterMaxStacks",16,0,999);
			cruxtruderIntake = builder.comment("If enabled, the regular cruxtruder will require raw cruxite to function, which is inserted through the pipe.")
					.define("cruxtruderIntake",true);
			forbiddenDimensionTypesTpz = builder.comment("A list of dimension types that you cannot travel to or from using transportalizers.")
					.define("forbiddenDimensionTypesTpz", new ArrayList<>());
			forbiddenModDimensionsTpz = builder.comment("A list of mod dimensions that you cannot travel to or from using transportalizers.")
					.define("forbiddenModDimensionsTpz", new ArrayList<>());
			builder.pop();
			
			builder.push("entry");
			entryCrater = builder.comment("Disable this to prevent craters from people entering the medium.")
					.define("entryCrater",true);
			adaptEntryBlockHeight = builder.comment("Adapt the transferred height to make the top non-air block to be placed at y:128. Makes entry take slightly longer.")
					.define("adaptEntryBlockHeight",true);
			stopSecondEntry = builder.comment("If this is true, players may only use an artifact once, even if they end up in the overworld again.")
					.define("stopSecondEntry",false);
			needComputer = builder.comment("If this is true, players need to have a computer nearby to Enter")
					.define("needComputer", false);
			artifactRange = builder.comment("Radius of the land brought into the medium.")
					.defineInRange("artifactRange",30,0,Integer.MAX_VALUE);
			builder.pop();
			
			builder.push("medium");
			canBreakGates = builder.comment("Lets gates be destroyed by explosions. Turning this off will make gates use the same explosion resistance as bedrock.")
					.define("canBreakGates",true);
			disableGiclops = builder.comment("Right now, the giclops pathfinding is currently causing huge amounts of lag due to their size. This option is a short-term solution that if true will disable giclops spawning and remove all existing giclopes.")
					.define("disableGiclops",true);
			naturalImpSpawn = builder.comment("Determines if imps will spawn naturally. Note that this does not affect other spawning methods or any imps that has already spawned.")
					.define("naturalImpSpawn",true);
			naturalOgreSpawn = builder.comment("Determines if ogres will spawn naturally. Note that this does not affect other spawning methods or any ogres that has already spawned.")
					.define("naturalOgreSpawn",true);
			naturalBasiliskSpawn = builder.comment("Determines if basilisks will spawn naturally. Note that this does not affect other spawning methods or any basilisks that has already spawned.")
					.define("naturalBasiliskSpawn",true);
			naturalLichSpawn = builder.comment("Determines if liches will spawn naturally. Note that this does not affect other spawning methods or any liches that has already spawned.")
					.define("naturalLichSpawn",false);
			allowSecondaryConnections = builder.comment("Set this to true to allow so-called 'secondary connections' to be created.")
					.define("secondaryConnections", true);
			builder.pop();
		}
	}
	
	static final ForgeConfigSpec commonSpec;
	public static final Common COMMON;
	static
	{
		Pair<Common, ForgeConfigSpec> pair = new ForgeConfigSpec.Builder().configure(Common::new);
		COMMON = pair.getLeft();
		commonSpec = pair.getRight();
	}
	
	static final ForgeConfigSpec clientSpec;
	public static final Client CLIENT;
	static
	{
		Pair<Client, ForgeConfigSpec> pair = new ForgeConfigSpec.Builder().configure(Client::new);
		CLIENT = pair.getLeft();
		clientSpec = pair.getRight();
	}
	
	
	static final ForgeConfigSpec serverSpec;
	public static final Server SERVER;
	static
	{
		Pair<Server, ForgeConfigSpec> pair = new ForgeConfigSpec.Builder().configure(Server::new);
		SERVER = pair.getLeft();
		serverSpec = pair.getRight();
	}
	
	@SubscribeEvent
	public static void onReload(final ModConfig.Reloading event)
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
	
	public enum FailedEscapeMode
	{
		/**
		 * Make the player's new server player his/her old server player's server player
		 */
		CLOSE,
		/**
		 * The player that lost his/her server player will have an idle main connection until someone without a client player connects to him/her.
		 */
		OPEN
	}
}