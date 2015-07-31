package com.mraof.minestuck;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import codechicken.nei.NEIModContainer;

import com.mraof.minestuck.block.BlockChessTile;
import com.mraof.minestuck.block.BlockColoredDirt;
import com.mraof.minestuck.block.BlockComputerOff;
import com.mraof.minestuck.block.BlockComputerOn;
import com.mraof.minestuck.block.BlockFluid;
import com.mraof.minestuck.block.BlockGlowingMushroom;
import com.mraof.minestuck.block.BlockReturnNode;
import com.mraof.minestuck.block.BlockSkaiaPortal;
import com.mraof.minestuck.block.BlockGoldSeeds;
import com.mraof.minestuck.block.BlockLayered;
import com.mraof.minestuck.block.BlockMachine;
import com.mraof.minestuck.block.BlockStorage;
import com.mraof.minestuck.block.BlockTransportalizer;
import com.mraof.minestuck.block.OreCruxite;
import com.mraof.minestuck.client.ClientProxy;
import com.mraof.minestuck.client.gui.GuiHandler;
import com.mraof.minestuck.client.util.MinestuckModelManager;
import com.mraof.minestuck.editmode.ClientEditHandler;
import com.mraof.minestuck.editmode.DeployList;
import com.mraof.minestuck.editmode.ServerEditHandler;
import com.mraof.minestuck.entity.EntityDecoy;
import com.mraof.minestuck.entity.carapacian.EntityBlackBishop;
import com.mraof.minestuck.entity.carapacian.EntityBlackPawn;
import com.mraof.minestuck.entity.carapacian.EntityBlackRook;
import com.mraof.minestuck.entity.carapacian.EntityWhiteBishop;
import com.mraof.minestuck.entity.carapacian.EntityWhitePawn;
import com.mraof.minestuck.entity.carapacian.EntityWhiteRook;
import com.mraof.minestuck.entity.consort.EntityIguana;
import com.mraof.minestuck.entity.consort.EntityNakagator;
import com.mraof.minestuck.entity.consort.EntitySalamander;
import com.mraof.minestuck.entity.item.EntityGrist;
import com.mraof.minestuck.entity.item.EntityMetalBoat;
import com.mraof.minestuck.entity.underling.EntityBasilisk;
import com.mraof.minestuck.entity.underling.EntityGiclops;
import com.mraof.minestuck.entity.underling.EntityImp;
import com.mraof.minestuck.entity.underling.EntityOgre;
import com.mraof.minestuck.event.MinestuckFluidHandler;
import com.mraof.minestuck.event.ServerEventHandler;
import com.mraof.minestuck.inventory.captchalouge.CaptchaDeckHandler;
import com.mraof.minestuck.item.ItemCaptchaCard;
import com.mraof.minestuck.item.ItemComponent;
import com.mraof.minestuck.item.ItemCruxiteApple;
import com.mraof.minestuck.item.ItemCruxiteRaw;
import com.mraof.minestuck.item.ItemDisk;
import com.mraof.minestuck.item.ItemDowel;
import com.mraof.minestuck.item.ItemGoldSeeds;
import com.mraof.minestuck.item.ItemMetalBoat;
import com.mraof.minestuck.item.ItemMinestuckBucket;
import com.mraof.minestuck.item.ItemModus;
import com.mraof.minestuck.item.ItemObsidianBucket;
import com.mraof.minestuck.item.block.ItemBlockLayered;
import com.mraof.minestuck.item.block.ItemChessTile;
import com.mraof.minestuck.item.block.ItemColoredDirt;
import com.mraof.minestuck.item.block.ItemMachine;
import com.mraof.minestuck.item.block.ItemOreCruxite;
import com.mraof.minestuck.item.block.ItemStorageBlock;
import com.mraof.minestuck.item.weapon.EnumBladeType;
import com.mraof.minestuck.item.weapon.EnumCaneType;
import com.mraof.minestuck.item.weapon.EnumClubType;
import com.mraof.minestuck.item.weapon.EnumHammerType;
import com.mraof.minestuck.item.weapon.EnumSickleType;
import com.mraof.minestuck.item.weapon.EnumSporkType;
import com.mraof.minestuck.item.weapon.ItemBlade;
import com.mraof.minestuck.item.weapon.ItemCane;
import com.mraof.minestuck.item.weapon.ItemClub;
import com.mraof.minestuck.item.weapon.ItemHammer;
import com.mraof.minestuck.item.weapon.ItemSickle;
import com.mraof.minestuck.item.weapon.ItemSpork;
import com.mraof.minestuck.nei.NEIMinestuckConfig;
import com.mraof.minestuck.network.MinestuckChannelHandler;
import com.mraof.minestuck.network.skaianet.SessionHandler;
import com.mraof.minestuck.tileentity.TileEntityComputer;
import com.mraof.minestuck.tileentity.TileEntityGate;
import com.mraof.minestuck.tileentity.TileEntitySkaiaPortal;
import com.mraof.minestuck.tileentity.TileEntityMachine;
import com.mraof.minestuck.tileentity.TileEntityTransportalizer;
import com.mraof.minestuck.tracker.ConnectionListener;
import com.mraof.minestuck.tracker.MinestuckPlayerTracker;
import com.mraof.minestuck.util.CommandCheckLand;
import com.mraof.minestuck.util.ComputerProgram;
import com.mraof.minestuck.util.MinestuckAchievementHandler;
import com.mraof.minestuck.util.AlchemyRecipeHandler;
import com.mraof.minestuck.util.KindAbstratusList;
import com.mraof.minestuck.util.SburbClient;
import com.mraof.minestuck.util.SburbServer;
import com.mraof.minestuck.util.UpdateChecker;
import com.mraof.minestuck.world.MinestuckDimensionHandler;
import com.mraof.minestuck.world.WorldProviderLands;
import com.mraof.minestuck.world.WorldProviderSkaia;
import com.mraof.minestuck.world.biome.BiomeGenMinestuck;
import com.mraof.minestuck.world.gen.OreHandler;
import com.mraof.minestuck.world.gen.structure.StructureCastlePieces;
import com.mraof.minestuck.world.gen.structure.StructureCastleStart;
import com.mraof.minestuck.world.lands.LandAspectRegistry;
import com.mraof.minestuck.world.lands.structure.LandStructureHandler;
import com.mraof.minestuck.world.storage.MinestuckSaveHandler;

@Mod(modid = "Minestuck", name = "Minestuck", version = "@VERSION@", guiFactory = "com.mraof.minestuck.client.gui.MinestuckGuiFactory")
public class Minestuck
{
	
	/**
	 * True only if the minecraft application is client-sided
	 */
	public static boolean isClientRunning;
	/**
	 * True if the minecraft application is server-sided, or if there is an integrated server running
	 */
	public static volatile boolean isServerRunning;
	
	public static int entityIdStart;
	public static int skaiaProviderTypeId;
	public static int skaiaDimensionId;
	public static int landProviderTypeId;
	public static int landDimensionIdStart;
	public static int biomeIdStart;
	
	//hammers
	public static Item clawHammer;
	public static Item sledgeHammer;
	public static Item pogoHammer;
	public static Item telescopicSassacrusher;
	public static Item fearNoAnvil;
	public static Item zillyhooHammer;
	public static Item popamaticVrillyhoo;
	public static Item scarletZillyhoo;
	//blades
	public static Item sord;
	public static Item ninjaSword;
	public static Item katana;
	public static Item caledscratch;
	public static Item royalDeringer;
	public static Item regisword;
	public static Item scarletRibbitar;
	public static Item doggMachete;
	//sickles
	public static Item sickle;
	public static Item homesSmellYaLater;
	public static Item regiSickle;
	public static Item clawSickle;
	//clubs
	public static Item deuceClub;
	//canes
	public static Item cane;
	public static Item spearCane;
	public static Item dragonCane;
	//Spoons/forks
	public static Item woodenSpoon;
	public static Item silverSpoon;
	public static ItemSpork crockerSpork;
	public static Item skaiaFork;
	//Other
	public static Item rawCruxite;
	public static Item cruxiteDowel;
	public static Item captchaCard;
	public static Item cruxiteApple;
	public static Item disk;
	public static Item component;
	public static ItemModus modusCard;
	public static ItemMinestuckBucket minestuckBucket;
	public static ItemGoldSeeds goldSeeds;	//This item is pretty much only a joke
	public static ItemMetalBoat metalBoat;
	public static Item obsidianBucket;
	
	//Blocks
	public static Block chessTile;
	public static BlockColoredDirt coloredDirt;
	public static Block gatePortal;
	public static OreCruxite oreCruxite;
	public static Block blockStorage;
	public static Block blockMachine;
	public static Block blockComputerOn;
	public static Block blockComputerOff;
	public static Block transportalizer;
	public static BlockGoldSeeds blockGoldSeeds;
	public static Block returnNode;
	public static BlockGlowingMushroom glowingMushroom;
	
	public static Block blockOil;
	public static Block blockBlood;
	public static Block blockBrainJuice;
	public static Block layeredSand;

	public static Fluid fluidOil;
	public static Fluid fluidBlood;
	public static Fluid fluidBrainJuice;
	
	// The instance of your mod that Forge uses.
	@Instance("Minestuck")
	public static Minestuck instance;

	// Says where the client and server 'proxy' code is loaded.
	@SidedProxy(clientSide="com.mraof.minestuck.client.ClientProxy", serverSide="com.mraof.minestuck.CommonProxy")

	//The proxy to be used by client and server
	public static CommonProxy proxy;
	public static CreativeTabs tabMinestuck;

	public int currentEntityIdOffset = 0;
	public static long worldSeed = 0;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) 
	{
		isClientRunning = event.getSide().isClient();
		
		MinestuckConfig.loadConfigFile(event.getSuggestedConfigurationFile(), event.getSide());
		
		//(new UpdateChecker()).start();
		
		//Register the Minestuck creative tab
		tabMinestuck = new CreativeTabs("tabMinestuck")
		{
			@Override
			public Item getTabIconItem() {
				return zillyhooHammer;
			}
		};
		
		//blocks
		chessTile = GameRegistry.registerBlock(new BlockChessTile(), ItemChessTile.class, "chess_tile");
		gatePortal = GameRegistry.registerBlock(new BlockSkaiaPortal(Material.portal), "gate_portal");
		oreCruxite = (OreCruxite) GameRegistry.registerBlock(new OreCruxite(), ItemOreCruxite.class, "ore_cruxite");
		layeredSand = GameRegistry.registerBlock(new BlockLayered(Blocks.sand), ItemBlockLayered.class, "layered_sand").setUnlocalizedName("layeredSand");
		coloredDirt = (BlockColoredDirt) GameRegistry.registerBlock(new BlockColoredDirt(), ItemColoredDirt.class, "colored_dirt").setUnlocalizedName("coloredDirt").setHardness(0.5F);
		blockStorage = GameRegistry.registerBlock(new BlockStorage(),ItemStorageBlock.class,"storage_block");
		blockMachine = GameRegistry.registerBlock(new BlockMachine(), ItemMachine.class,"machine_block");
		blockComputerOff = GameRegistry.registerBlock(new BlockComputerOff(),"computer_standard");
		blockComputerOn = GameRegistry.registerBlock(new BlockComputerOn(), null, "computer_standard_on");
		transportalizer = GameRegistry.registerBlock(new BlockTransportalizer(), "transportalizer");
		blockGoldSeeds = (BlockGoldSeeds) GameRegistry.registerBlock(new BlockGoldSeeds(), null, "gold_seeds");
		returnNode = GameRegistry.registerBlock(new BlockReturnNode(), null, "return_node");
		glowingMushroom = (BlockGlowingMushroom) GameRegistry.registerBlock(new BlockGlowingMushroom(), "glowing_mushroom");
		//fluids
		fluidOil = new Fluid("Oil", new ResourceLocation("minestuck", "blocks/OilStill"), new ResourceLocation("minestuck", "blocks/OilFlowing"));
		FluidRegistry.registerFluid(fluidOil);
		fluidBlood = new Fluid("Blood", new ResourceLocation("minestuck", "blocks/BloodStill"), new ResourceLocation("minestuck", "blocks/BloodFlowing"));
		FluidRegistry.registerFluid(fluidBlood);
		fluidBrainJuice = new Fluid("BrainJuice", new ResourceLocation("minestuck", "blocks/BrainJuiceStill"), new ResourceLocation("minestuck", "blocks/BrainJuiceFlowing"));
		FluidRegistry.registerFluid(fluidBrainJuice);
		blockOil = GameRegistry.registerBlock(new BlockFluid(fluidOil, Material.water).setUnlocalizedName("oil"), null, "block_oil");
		blockBlood = GameRegistry.registerBlock(new BlockFluid(fluidBlood, Material.water).setUnlocalizedName("blood"), null, "block_blood");
		blockBrainJuice = GameRegistry.registerBlock(new BlockFluid(fluidBrainJuice, Material.water).setUnlocalizedName("brainJuice"), null, "block_brain_juice");

		//items
		//hammers
		clawHammer = new ItemHammer(EnumHammerType.CLAW);
		sledgeHammer = new ItemHammer(EnumHammerType.SLEDGE);
		pogoHammer = new ItemHammer(EnumHammerType.POGO);
		telescopicSassacrusher = new ItemHammer(EnumHammerType.TELESCOPIC);
		fearNoAnvil = new ItemHammer(EnumHammerType.FEARNOANVIL);
		zillyhooHammer = new ItemHammer(EnumHammerType.ZILLYHOO);
		popamaticVrillyhoo = new ItemHammer(EnumHammerType.POPAMATIC);
		scarletZillyhoo = new ItemHammer(EnumHammerType.SCARLET);
		//blades
		sord = new ItemBlade(EnumBladeType.SORD);
		ninjaSword = new ItemBlade(EnumBladeType.NINJA);
		katana = new ItemBlade(EnumBladeType.KATANA);
		caledscratch = new ItemBlade(EnumBladeType.CALEDSCRATCH);
		royalDeringer = new ItemBlade(EnumBladeType.DERINGER);
		regisword = new ItemBlade(EnumBladeType.REGISWORD);
		scarletRibbitar = new ItemBlade(EnumBladeType.SCARLET);
		doggMachete = new ItemBlade(EnumBladeType.DOGG);
		//sickles
		sickle = new ItemSickle(EnumSickleType.SICKLE);
		homesSmellYaLater = new ItemSickle(EnumSickleType.HOMES);
		regiSickle = new ItemSickle(EnumSickleType.REGISICKLE);
		clawSickle = new ItemSickle(EnumSickleType.CLAW);
		//clubs
		deuceClub = new ItemClub(EnumClubType.DEUCE);
		//canes
		cane = new ItemCane(EnumCaneType.CANE);
		spearCane = new ItemCane(EnumCaneType.SPEAR);
		dragonCane = new ItemCane(EnumCaneType.DRAGON);
		//Spoons/forks
		woodenSpoon = new ItemSpork(EnumSporkType.SPOON_WOOD);
		silverSpoon = new ItemSpork(EnumSporkType.SPOON_SILVER);
		crockerSpork = new ItemSpork(EnumSporkType.CROCKER);
		skaiaFork = new ItemSpork(EnumSporkType.SKAIA);
		//items
		rawCruxite = new ItemCruxiteRaw();
		cruxiteDowel = new ItemDowel();
		captchaCard = new ItemCaptchaCard();
		cruxiteApple = new ItemCruxiteApple();
		disk = new ItemDisk();
		component = new ItemComponent();
		minestuckBucket = new ItemMinestuckBucket();
		obsidianBucket = new ItemObsidianBucket();
		modusCard = new ItemModus();
		goldSeeds = new ItemGoldSeeds();
		metalBoat = new ItemMetalBoat();
		
		minestuckBucket.addBlock(blockOil);
		minestuckBucket.addBlock(blockBlood);
		minestuckBucket.addBlock(blockBrainJuice);
		
		GameRegistry.registerItem(clawHammer, "claw_hammer");
		GameRegistry.registerItem(sledgeHammer, "sledge_hammer");
		GameRegistry.registerItem(pogoHammer, "pogo_hammer");
		GameRegistry.registerItem(telescopicSassacrusher, "telescopic_sassacrusher");
		GameRegistry.registerItem(fearNoAnvil, "fear_no_anvil");
		GameRegistry.registerItem(zillyhooHammer, "zillyhoo_hammer");
		GameRegistry.registerItem(popamaticVrillyhoo, "popamatic_vrillyhoo");
		GameRegistry.registerItem(scarletZillyhoo, "scarlet_zillyhoo");
		
		GameRegistry.registerItem(sord, "sord");
		GameRegistry.registerItem(ninjaSword, "ninja_sword");
		GameRegistry.registerItem(katana, "katana");
		GameRegistry.registerItem(caledscratch, "caledscratch");
		GameRegistry.registerItem(royalDeringer, "royal_deringer");
		GameRegistry.registerItem(regisword, "regisword");
		GameRegistry.registerItem(scarletRibbitar, "scarlet_ribbitar");
		GameRegistry.registerItem(doggMachete, "dogg_machete");
		
		GameRegistry.registerItem(sickle, "sickle");
		GameRegistry.registerItem(homesSmellYaLater, "homes_smell_ya_later");
		GameRegistry.registerItem(regiSickle, "regi_sickle");
		GameRegistry.registerItem(clawSickle, "claw_sickle");
		
		GameRegistry.registerItem(deuceClub, "deuce_club");
		
		GameRegistry.registerItem(cane, "cane");
		GameRegistry.registerItem(spearCane, "spear_cane");
		GameRegistry.registerItem(dragonCane, "dragon_cane");
		
		GameRegistry.registerItem(woodenSpoon, "spoon_wood");
		GameRegistry.registerItem(silverSpoon, "spoon_silver");
		GameRegistry.registerItem(crockerSpork, "crocker_spork");
		GameRegistry.registerItem(skaiaFork, "skaia_fork");
		
		GameRegistry.registerItem(rawCruxite, "cruxite_raw");
		GameRegistry.registerItem(cruxiteDowel, "cruxite_dowel");
		GameRegistry.registerItem(captchaCard, "captcha_card");
		GameRegistry.registerItem(cruxiteApple, "cruxite_artifact");	//TODO change to "cruxite_apple" when there's no risk of messing with existing save files
		GameRegistry.registerItem(disk, "computer_disk");
		GameRegistry.registerItem(component, "component");
		GameRegistry.registerItem(minestuckBucket, "minestuck_bucket");
		GameRegistry.registerItem(obsidianBucket, "bucket_obsidian");
		GameRegistry.registerItem(modusCard, "modus_card");
		GameRegistry.registerItem(goldSeeds, "gold_seeds");
		GameRegistry.registerItem(metalBoat, "metal_boat");
		
		if(isClientRunning)
		{
			ClientProxy.registerSided();
			MinestuckModelManager.registerVariants();
		}
		
		MinestuckAchievementHandler.prepareAchievementPage();
		
	}
	
	@EventHandler
	public void load(FMLInitializationEvent event) 
	{
		
		fluidOil.setUnlocalizedName(blockOil.getUnlocalizedName());
		fluidBlood.setUnlocalizedName(blockBlood.getUnlocalizedName());
		fluidBrainJuice.setUnlocalizedName(blockBrainJuice.getUnlocalizedName());
		
		
		//register entities
		this.registerAndMapEntity(EntitySalamander.class, "Salamander", 0xffe62e, 0xfffb53);
		this.registerAndMapEntity(EntityNakagator.class, "Nakagator", 0xff0000, 0xff6a00);
		this.registerAndMapEntity(EntityIguana.class, "Iguana", 0x0026ff, 0x0094ff);
		this.registerAndMapEntity(EntityImp.class, "Imp", 0x000000, 0xffffff);
		this.registerAndMapEntity(EntityOgre.class, "Ogre", 0x000000, 0xffffff);
		this.registerAndMapEntity(EntityBasilisk.class, "Basilisk", 0x400040, 0xffffff);
		this.registerAndMapEntity(EntityGiclops.class, "Giclops", 0x000000, 0xffffff);
		this.registerAndMapEntity(EntityBlackPawn.class, "dersitePawn", 0x0f0f0f, 0xf0f0f0);
		this.registerAndMapEntity(EntityWhitePawn.class, "prospitianPawn", 0xf0f0f0, 0x0f0f0f);
		this.registerAndMapEntity(EntityBlackBishop.class, "dersiteBishop", 0x000000, 0xc121d9);
		this.registerAndMapEntity(EntityWhiteBishop.class, "prospitianBishop", 0xffffff, 0xfde500);
		this.registerAndMapEntity(EntityBlackRook.class, "dersiteRook", 0x000000, 0xc121d9);
		this.registerAndMapEntity(EntityWhiteRook.class, "prospitianRook", 0xffffff, 0xfde500);
		
		//register entities with fml
		EntityRegistry.registerModEntity(EntityDecoy.class, "minestuck.playerDecoy", currentEntityIdOffset, this, 80, 3, true);
		currentEntityIdOffset++;
		EntityRegistry.registerModEntity(EntityMetalBoat.class, "minestuck.metalBoat", currentEntityIdOffset, this, 80, 3, true);
		currentEntityIdOffset++;
		EntityRegistry.registerModEntity(EntityGrist.class, "minestuck.grist", currentEntityIdOffset, this, 512, 1, true);
		
		//register Tile Entities
		GameRegistry.registerTileEntity(TileEntitySkaiaPortal.class, "minstuck.gatePortal");
		GameRegistry.registerTileEntity(TileEntityMachine.class, "minestuck.containerMachine");
		GameRegistry.registerTileEntity(TileEntityComputer.class, "minestuck.computerSburb");
		GameRegistry.registerTileEntity(TileEntityTransportalizer.class, "minestuck.transportalizer");
		GameRegistry.registerTileEntity(TileEntityGate.class, "minestuck.gate");
		//register world generators
		DimensionManager.registerProviderType(skaiaProviderTypeId, WorldProviderSkaia.class, false);
		DimensionManager.registerDimension(skaiaDimensionId, skaiaProviderTypeId);
		DimensionManager.registerProviderType(landProviderTypeId, WorldProviderLands.class, MinestuckConfig.keepDimensionsLoaded);
		
		BiomeGenMinestuck.mediumNormal = new BiomeGenMinestuck(biomeIdStart).setBiomeName("The Medium");
		BiomeGenMinestuck.mediumCold = new BiomeGenMinestuck(biomeIdStart+1).setBiomeName("The Medium (Cold)").setEnableSnow().setTemperatureRainfall(0.0F, 0.5F);
		
		//register ore generation
		OreHandler oreHandler = new OreHandler();
		GameRegistry.registerWorldGenerator(oreHandler, 0);
		
		//register GUI handler
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
		
		//Register textures and renders
		if(isClientRunning)
		{
			MinestuckModelManager.registerTextures();
			ClientProxy.registerRenderers();
		}
		
		//Register event handlers
		MinecraftForge.EVENT_BUS.register(new MinestuckSaveHandler());
		MinecraftForge.EVENT_BUS.register(new MinestuckFluidHandler());
		MinecraftForge.EVENT_BUS.register(ServerEditHandler.instance);
		MinecraftForge.EVENT_BUS.register(MinestuckAchievementHandler.instance);
		MinecraftForge.EVENT_BUS.register(MinestuckPlayerTracker.instance);
		
		FMLCommonHandler.instance().bus().register(MinestuckPlayerTracker.instance);
		FMLCommonHandler.instance().bus().register(ServerEditHandler.instance);
		FMLCommonHandler.instance().bus().register(MinestuckChannelHandler.instance);
		FMLCommonHandler.instance().bus().register(new ConnectionListener());
		FMLCommonHandler.instance().bus().register(new ServerEventHandler());
		
		if(event.getSide().isClient())
		{
			MinecraftForge.EVENT_BUS.register(ClientEditHandler.instance);
			FMLCommonHandler.instance().bus().register(ClientEditHandler.instance);
			FMLCommonHandler.instance().bus().register(new MinestuckConfig());
		}
		
		//register channel handler
		MinestuckChannelHandler.setupChannel();
		
		//Register structures
		MapGenStructureIO.registerStructure(StructureCastleStart.class, "SkaiaCastle");
		StructureCastlePieces.registerComponents();
		LandStructureHandler.registerStructures();
		
		//register recipes
		AlchemyRecipeHandler.registerVanillaRecipes();
		AlchemyRecipeHandler.registerMinestuckRecipes();
		AlchemyRecipeHandler.registerModRecipes();
		
		LandAspectRegistry.registerLandAspects();
		
		KindAbstratusList.registerTypes();
		DeployList.registerItems();
		
		ComputerProgram.registerProgram(0, SburbClient.class, new ItemStack(disk, 1, 0));	//This idea was kind of bad and should be replaced
		ComputerProgram.registerProgram(1, SburbServer.class, new ItemStack(disk, 1, 1));
		
		SessionHandler.maxSize = 144;//acceptTitleCollision?(generateSpecialClasses?168:144):12;
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) 
	{
		AlchemyRecipeHandler.registerDynamicRecipes();

		//register NEI stuff
		if (Loader.isModLoaded("NotEnoughItems")) {
			NEIModContainer.plugins.add(new NEIMinestuckConfig());
		}
	}
	//registers entity with forge and minecraft, and increases currentEntityIdOffset by one in order to prevent id collision
	public void registerAndMapEntity(Class<? extends Entity> entityClass, String name, int eggColor, int eggSpotColor)
	{
		this.registerAndMapEntity(entityClass, name, eggColor, eggSpotColor, 80, 3, true);
	}

	public void registerAndMapEntity(Class<? extends Entity> entityClass, String name, int eggColor, int eggSpotColor, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates)
	{
		EntityList.addMapping(entityClass, "minestuck."+name, entityIdStart + currentEntityIdOffset, eggColor, eggSpotColor);
		EntityRegistry.registerModEntity(entityClass, "minestuck."+name, currentEntityIdOffset, this, trackingRange, updateFrequency, sendsVelocityUpdates);
		currentEntityIdOffset++;
	}

	@EventHandler 
	public void serverAboutToStart(FMLServerAboutToStartEvent event)
	{
		isServerRunning = true;
		AlchemyRecipeHandler.addOrRemoveRecipes(MinestuckConfig.cardRecipe);
		TileEntityTransportalizer.transportalizers.clear();
		DeployList.applyConfigValues(MinestuckConfig.deployConfigurations);
	}
	
	@EventHandler
	public void serverClosed(FMLServerStoppedEvent event)
	{
		MinestuckDimensionHandler.unregisterDimensions();
		isServerRunning = !isClientRunning;
	}
	
	@EventHandler
	public void serverStarting(FMLServerStartingEvent event)
	{
		
		event.registerServerCommand(new CommandCheckLand());
		
		worldSeed = event.getServer().worldServers[0].getSeed();
		CaptchaDeckHandler.rand = new Random();
		
		MinestuckSaveHandler.onWorldLoad(event.getServer().worldServers[0].getSaveHandler());
	}
	
	@EventHandler
	public void serverStopping(FMLServerStoppingEvent event)
	{
		ServerEditHandler.onServerStopping();
	}
}
