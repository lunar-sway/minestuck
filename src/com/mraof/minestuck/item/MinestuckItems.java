package com.mraof.minestuck.item;

import com.mraof.minestuck.block.*;
import com.mraof.minestuck.entity.item.EntityCrewPoster;
import com.mraof.minestuck.entity.item.EntitySbahjPoster;
import com.mraof.minestuck.item.block.*;
import com.mraof.minestuck.item.weapon.*;
import com.mraof.minestuck.util.MinestuckSoundHandler;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityHanging;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

import static com.mraof.minestuck.block.MinestuckBlocks.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.mraof.minestuck.block.BlockAspectLog;
import com.mraof.minestuck.block.BlockAspectLog2;
import com.mraof.minestuck.block.BlockAspectLog3;
import com.mraof.minestuck.block.BlockAspectSapling;

public class MinestuckItems
{
	@Deprecated
	public static CreativeTabs tabMinestuck = TabMinestuck.instance;
	
	public static Item.ToolMaterial toolEmerald = EnumHelper.addToolMaterial("EMERALD", 3, 1220, 12.0F, 4.0F, 12).setRepairItem(new ItemStack(Items.EMERALD));
	public static ItemArmor.ArmorMaterial armorPrismarine = EnumHelper.addArmorMaterial("PRISMARINE", "minestuck:prismarine", 20, new int[]{3, 7, 6, 2}, 15, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0.0F);
	;
	public static Item.ToolMaterial toolUranium = EnumHelper.addToolMaterial("URANIUM", 3, 1220, 12.0F, 6.0F, 15);
	//hammers
	public static Item clawHammer = new ItemWeapon(131, 4.0D, -2.4D, 10, "clawHammer").setTool("pickaxe", 0, 1.0F);
	public static Item sledgeHammer = new ItemWeapon(250, 6.0D, -2.8D, 8, "sledgeHammer").setTool("pickaxe", 2, 4.0F);
	public static Item blacksmithHammer = new ItemWeapon(450, 7.0D, -2.8D, 10, "blacksmithHammer").setTool("pickaxe", 2, 3.5F);
	public static Item pogoHammer = new ItemPogoWeapon(400, 7.0D, -2.8D, 8, "pogoHammer", 0.7).setTool("pickaxe", 1, 2.0F);
	public static Item telescopicSassacrusher = new ItemWeapon(1024, 9.0D, -2.9D, 15, "telescopicSassacrusher").setTool("pickaxe", 2, 5.0F);
	public static Item regiHammer = new ItemWeapon(812, 6.0D, -2.4D, 5, "regiHammer");
	public static Item fearNoAnvil = new ItemPotionWeapon(2048, 10.0D, -2.8D, 12, "fearNoAnvil", new PotionEffect(MobEffects.SLOWNESS, 100, 3)).setTool("pickaxe", 3, 7.0F);
	public static Item zillyhooHammer = new ItemWeapon(3000, 11.0D, -2.8D, 30, "zillyhooHammer").setTool("pickaxe", 4, 15.0F);
	
	public static Item popamaticVrillyhoo = new ItemWeapon(3000, 0.0D, -2.8D, 30, "popamaticVrillyhoo").setTool("pickaxe", 4, 15.0F);
	public static Item scarletZillyhoo = new ItemFireWeapon(2000, 11.0D, -2.8D, 16, "scarletZillyhoo", 50).setTool("pickaxe", 3, 4.0F);
	public static Item mwrthwl = new ItemWeapon(2000, 10.5D, -2.8D, 16, "mwrthwl").setTool("pickaxe", 3, 4.0F);
	//blades
	public static Item sord = new ItemSord(59, 2, -2.4D, 5, "sord");
	public static Item cactusCutlass = new ItemWeapon(104, 4, -2.4D, 10, "cactaceaeCutlass").setTool("sword", 0, 15.0F);	//The sword tool is only used against webs, hence the high efficiency.
	public static Item steakSword = new ItemConsumableWeapon(250, 4, -2.4D, 5, "steakSword", 8, 1F);
	public static Item katana = new ItemWeapon(250, 5, -2.4D, 15, "ninjaSword").setTool("sword", 0, 15.0F);
	public static Item unbreakableKatana = new ItemWeapon(2200, 7, -2.4D, 20, "katana").setTool("sword", 0, 15.0F);    //Not actually unbreakable
	public static Item firePoker = new ItemFireWeapon(250, 6, -2.4D, 15, "firePoker", 30).setTool("sword", 0, 15.0F);
	public static Item hotHandle = new ItemFireWeapon(350, 5, -2.4D, 15, "hotHandle", 10).setTool("sword", 0, 15.0F);
	public static Item caledscratch = new ItemWeapon(1561, 6, -2.4D, 30, "caledscratch").setTool("sword", 0, 15.0F);
	public static Item caledfwlch = new ItemWeapon(1025, 6, -2.4D, 30, "caledfwlch").setTool("sword", 0, 15.0F);
	public static Item royalDeringer = new ItemWeapon(1561, 7, -2.4D, 30, "royalDeringer").setTool("sword", 0, 15.0F);
	public static Item zillywairCutlass = new ItemWeapon(2500, 8, -2.4D, 30, "zillywairCutlass").setTool("sword", 0, 15.0F);
	public static Item regisword = new ItemWeapon(812, 6, -2.4D, 10, "regisword").setTool("sword", 0, 15.0F);
	public static Item scarletRibbitar = new ItemWeapon(2000, 7, -2.4D, 30, "scarletRibbitar").setTool("sword", 0, 15.0F);
	public static Item doggMachete = new ItemWeapon(1000, 5, -2.4D, 30, "doggMachete").setTool("sword", 0, 15.0F);
	public static Item cobaltSabre = new ItemFireWeapon(300, 7, -2.4D, 10, "cobaltSabre", 30).setTool("sword", 0, 15.0F);
	public static Item quantumSabre = new ItemPotionWeapon(toolUranium, 600, 8, -2.4D, 5, "quantumSabre", new PotionEffect(MobEffects.WITHER, 100, 1)).setTool("sword", 0, 15.0F);
	//axes
	public static Item copseCrusher = new ItemFarmine(400, 6.0D, -3.0D, 20, "copseCrusher", Integer.MAX_VALUE, 20).setTool("axe", 2, 6.0F);
	public static Item blacksmithBane = new ItemWeapon(413, 9.0D, -3.0D, 15, "blacksmithBane").setTool("axe", 2, 6.0F);
	public static Item scraxe = new ItemWeapon(500, 10.0D, -3.0D, 20, "scraxe").setTool("axe", 2, 7.0F);
	public static Item qPHammerAxe = new ItemPogoFarmine(800, 8.0D, -3.0D, 30, "qPHammerAxe", Integer.MAX_VALUE, 50, 0.6).setTool("pickaxe", 1, 2.0F).setTool("axe", 2, 7.0F);
	public static Item rubyCroak = new ItemWeapon(2000, 11.0D, -3.0D, 30, "rubyCroak").setTool("axe", 3, 8.0F);
	public static Item hephaestusLumber = new ItemFireWeapon(3000, 11.0D, -3.0D, 30, "hephaestusLumber", 30).setTool("axe", 3, 9.0F);
	public static Item qFHammerAxe = new ItemPogoFarmine(toolUranium, 2048, 11.0D, -3.0D, 0, "qFHammerAxe", Integer.MAX_VALUE, 100, 0.7).setTool("pickaxe", 2, 5.0F).setTool("axe", 3, 9.0F);
	//Dice
	public static Item dice = new ItemWeapon(51, 6, 3, 6, "dice");
	public static Item fluoriteOctet = new ItemWeapon(67, 15, 6, 8, "fluoriteOctet");
	//misc weapons
	public static Item catClaws = new ItemDualWeapon(500, 4.0D, 1.0D, -1.5D, -1.0D, 6, "catclaws");
	//sickles
	public static Item sickle = new ItemWeapon(220, 4.0D, -2.4D, 8, "sickle").setTool("sickle", 0, 1.5F);
	public static Item homesSmellYaLater = new ItemWeapon(400, 5.5D, -2.4D, 10, "homesSmellYaLater").setTool("sickle", 0, 3.0F);
	public static Item fudgeSickle = new ItemConsumableWeapon(450, 5.5D, -2.4D, 10, "fudgeSickle", 7, 0.6F).setTool("sickle", 0, 1.0F);
	public static Item regiSickle = new ItemWeapon(812, 6.0D, -2.4D, 5, "regiSickle").setTool("sickle", 0, 4.0F);
	public static Item clawSickle = new ItemWeapon(2048, 7.0D, -2.4D, 15, "clawSickle").setTool("sickle", 0, 4.0F);
	public static Item candySickle = new ItemCandyWeapon(96, 6.0D, -2.4D, 15, "candySickle").setTool("sickle", 0, 2.5F);
	//clubs
	public static Item deuceClub = new ItemWeapon(1024, 2.5D, -2.2D, 15, "deuceClub");
	public static Item nightClub = new ItemWeapon(600, 4.0D, -2.2D, 20, "nightClub");
	public static Item pogoClub = new ItemPogoWeapon(600, 3.5D, -2.2D, 15, "pogoClub", 0.5);
	public static Item metalBat = new ItemWeapon(750, 5.0D, -2.2D, 5, "metalBat");
	public static Item spikedClub = new ItemWeapon(500, 5.5D, -2.2D, 5, "spikedClub");
	//canes
	public static Item cane = new ItemWeapon(100, 2.0D, -2.0D, 15, "cane");
	public static Item ironCane = new ItemWeapon(450, 3.5D, -2.0D, 10, "ironCane");
	public static Item spearCane = new ItemWeapon(300, 5.0D, -2.0D, 13, "spearCane");
	public static Item regiCane = new ItemWeapon(812, 6.0D, -2.0D, 7, "regiCane");
	public static Item dragonCane = new ItemWeapon(300, 6.5D, -2.0D, 20, "dragonCane");
	public static Item pogoCane = new ItemPogoWeapon(500, 3.0D, -2.0D, 15, "pogoCane", 0.6);
	public static Item upStick = new ItemWeapon(toolUranium, 1, 0.0D, 0.0D, 0, "upStick").setUnbreakable();	//Never runs out of uranium!
	//Spoons/forks
	public static Item woodenSpoon = new ItemWeapon(59, 2.0D, -2.2D, 5, "woodenSpoon");
	public static Item silverSpoon = new ItemWeapon(250, 2.5D, -2.2D, 12, "silverSpoon");
	public static ItemSpork crockerSpork = (ItemSpork) new ItemSpork(512, 4.0D, -2.2D, 15, "crocker");
	public static Item skaiaFork = new ItemWeapon(2048, 8.5D, -2.2D, 10, "skaiaFork");
	public static Item fork = new ItemWeapon(100, 4.0D, -2.2D, 3, "fork");
	public static Item spork = new ItemWeapon(120, 4.5D, -2.3D, 5, "spork");
	public static Item goldenSpork = new ItemWeapon(45, 5D, -2.3D, 22, "goldenSpork");
	//Material tools
	public static Item emeraldSword = new ItemSword(toolEmerald).setUnlocalizedName("swordEmerald").setCreativeTab(TabMinestuck.instance);
	public static Item emeraldAxe = new ItemMinestuckAxe(toolEmerald, 9.0F, -3.0F).setUnlocalizedName("hatchetEmerald").setCreativeTab(TabMinestuck.instance);
	public static Item emeraldPickaxe = new ItemMinestuckPickaxe(toolEmerald).setUnlocalizedName("pickaxeEmerald").setCreativeTab(TabMinestuck.instance);
	public static Item emeraldShovel = new ItemSpade(toolEmerald).setUnlocalizedName("shovelEmerald").setCreativeTab(TabMinestuck.instance);
	public static Item emeraldHoe = new ItemHoe(toolEmerald).setUnlocalizedName("hoeEmerald").setCreativeTab(TabMinestuck.instance);
	//Armor
	public static Item prismarineHelmet = new ItemArmor(armorPrismarine, 0, EntityEquipmentSlot.HEAD).setUnlocalizedName("helmetPrismarine").setCreativeTab(TabMinestuck.instance);
	public static Item prismarineChestplate = new ItemArmor(armorPrismarine, 0, EntityEquipmentSlot.CHEST).setUnlocalizedName("chestplatePrismarine").setCreativeTab(TabMinestuck.instance);
	public static Item prismarineLeggings = new ItemArmor(armorPrismarine, 0, EntityEquipmentSlot.LEGS).setUnlocalizedName("leggingsPrismarine").setCreativeTab(TabMinestuck.instance);
	public static Item prismarineBoots = new ItemArmor(armorPrismarine, 0, EntityEquipmentSlot.FEET).setUnlocalizedName("bootsPrismarine").setCreativeTab(TabMinestuck.instance);
	//Food
	public static Item candy = new ItemMinestuckCandy();
	public static Item beverage = new ItemMinestuckBeverage();
	public static Item bugOnAStick = new ItemFood(1, 0.1F, false).setUnlocalizedName("bugOnAStick").setCreativeTab(TabMinestuck.instance);
	public static Item chocolateBeetle = new ItemFood(3, 0.4F, false).setUnlocalizedName("chocolateBeetle").setCreativeTab(TabMinestuck.instance);
	public static Item coneOfFlies = new ItemFood(2, 0.1F, false).setUnlocalizedName("coneOfFlies").setCreativeTab(TabMinestuck.instance);
	public static Item grasshopper = new ItemFood(4, 0.5F, false).setUnlocalizedName("grasshopper").setCreativeTab(TabMinestuck.instance);
	public static Item jarOfBugs = new ItemFood(3, 0.2F, false).setUnlocalizedName("jarOfBugs").setCreativeTab(TabMinestuck.instance);
	public static Item onion = new ItemFood(2, 0.2F, false).setUnlocalizedName("onion").setCreativeTab(TabMinestuck.instance);
	public static Item salad = new ItemSoup(1).setUnlocalizedName("salad").setCreativeTab(TabMinestuck.instance);
	public static Item desertFruit = new ItemFood(1, 0.1F, false).setUnlocalizedName("desertFruit").setCreativeTab(TabMinestuck.instance);
	public static Item irradiatedSteak = new ItemFood(4, 0.4F, true).setPotionEffect(new PotionEffect(MobEffects.WITHER, 100, 1), 0.9F).setUnlocalizedName("irradiatedSteak").setCreativeTab(TabMinestuck.instance);
	public static Item rockCookie = new Item().setUnlocalizedName("rockCookie").setCreativeTab(TabMinestuck.instance);
	public static Item surpriseEmbryo = new ItemSurpriseEmbryo(3, 0.2F, false);
	//Other
	public static Item boondollars = new ItemBoondollars();
	public static Item rawCruxite = new Item().setUnlocalizedName("rawCruxite").setCreativeTab(TabMinestuck.instance);
	public static Item rawUranium = new Item().setUnlocalizedName("rawUranium").setCreativeTab(TabMinestuck.instance);
	public static Item energyCore = new Item().setUnlocalizedName("energyCore").setCreativeTab(TabMinestuck.instance);
	public static ItemDowel cruxiteDowel = new ItemDowel(MinestuckBlocks.blockCruxiteDowel);
	public static Item captchaCard = new ItemCaptchaCard();
	public static Item cruxiteApple = new ItemCruxiteApple();
	public static Item cruxitePotion = new ItemCruxitePotion();
	public static Item disk = new ItemDisk();
	public static Item chessboard = new Item().setUnlocalizedName("chessboard").setMaxStackSize(1).setCreativeTab(TabMinestuck.instance);
	public static Item minestuckBucket = new ItemMinestuckBucket();
	public static Item obsidianBucket = new ItemObsidianBucket();
	public static Item modusCard = new ItemModus();
	public static Item goldSeeds = new ItemGoldSeeds();
	public static Item metalBoat = new ItemMetalBoat();
	public static Item threshDvd = new Item().setUnlocalizedName("threshDvd").setMaxStackSize(1).setCreativeTab(TabMinestuck.instance);
	public static Item crewPoster = new ItemHanging()
	{
		@Override
		public EntityHanging createEntity(World worldIn, BlockPos pos, EnumFacing facing, ItemStack stack)
		{
			return new EntityCrewPoster(worldIn, pos, facing);
		}
	}.setUnlocalizedName("crewPoster").setMaxStackSize(1).setCreativeTab(TabMinestuck.instance);
	public static Item sbahjPoster = new ItemHanging()
	{
		@Override
		public EntityHanging createEntity(World worldIn, BlockPos pos, EnumFacing facing, ItemStack stack)
		{
			return new EntitySbahjPoster(worldIn, pos, facing);
		}
	}.setUnlocalizedName("sbahjPoster").setMaxStackSize(1).setCreativeTab(TabMinestuck.instance);
	
	public static Item carvingTool = new Item().setUnlocalizedName("carvingTool").setMaxStackSize(1).setCreativeTab(TabMinestuck.instance);
	public static Item crumplyHat = new Item().setUnlocalizedName("crumplyHat").setMaxStackSize(1).setCreativeTab(TabMinestuck.instance);
	public static Item frogStatueReplica = new Item().setUnlocalizedName("frogStatueReplica").setCreativeTab(TabMinestuck.instance);
	public static Item stoneEyeballs = new Item().setUnlocalizedName("stoneEyeballs").setCreativeTab(TabMinestuck.instance);
	public static Item stoneSlab = new Item().setUnlocalizedName("stoneSlab").setCreativeTab(TabMinestuck.instance);
	public static Item glowystoneDust = new ItemGlowystoneDust().setUnlocalizedName("glowystoneDust").setCreativeTab(TabMinestuck.instance);
	public static Item fakeArms = new Item().setUnlocalizedName("fakeArms").setCreativeTab(null);
	//Music disks
	public static Item recordEmissaryOfDance = new ItemMinestuckRecord("emissary", MinestuckSoundHandler.soundEmissaryOfDance).setUnlocalizedName("record");
	public static Item recordDanceStab = new ItemMinestuckRecord("danceStab", MinestuckSoundHandler.soundDanceStabDance).setUnlocalizedName("record");
	
	
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event)
	{
		armorPrismarine.repairMaterial = new ItemStack(Items.PRISMARINE_SHARD);
		
		IForgeRegistry<Item> registry = event.getRegistry();
		registerItemBlock(registry, new ItemMultiTexture(chessTile, chessTile, new String[]{"black", "white", "darkgrey", "lightgrey"}));
		registerItemBlock(registry, new ItemBlock(skaiaPortal));
		
		registerItemBlock(registry, new ItemMultiTexture(oreCruxite, oreCruxite, new String[0])
		{
			@Override
			public String getUnlocalizedName(ItemStack stack)
			{
				return block.getUnlocalizedName();
			}
		});
		registerItemBlock(registry, new ItemMultiTexture(oreUranium, oreUranium, new String[0])
		{
			@Override
			public String getUnlocalizedName(ItemStack stack)
			{
				return block.getUnlocalizedName();
			}
		});
		registerItemBlock(registry, new ItemBlock(coalOreNetherrack));
		registerItemBlock(registry, new ItemBlock(ironOreEndStone));
		registerItemBlock(registry, new ItemBlock(ironOreSandstone));
		registerItemBlock(registry, new ItemBlock(ironOreSandstoneRed));
		registerItemBlock(registry, new ItemBlock(goldOreSandstone));
		registerItemBlock(registry, new ItemBlock(goldOreSandstoneRed));
		registerItemBlock(registry, new ItemBlock(redstoneOreEndStone));
		
		registerItemBlock(registry, new ItemBlockCraftingTab(cruxiteBlock, CreativeTabs.BUILDING_BLOCKS));
		registerItemBlock(registry, new ItemBlockCraftingTab(uraniumBlock, CreativeTabs.BUILDING_BLOCKS));
		registerItemBlock(registry, new ItemBlock(genericObject));
		registerItemBlock(registry, new ItemSburbMachine(sburbMachine));
		registerItemBlock(registry, new ItemMultiTexture(crockerMachine, crockerMachine,
				(ItemStack input) -> BlockCrockerMachine.MachineType.values()[input.getItemDamage() % BlockCrockerMachine.MachineType.values().length].getUnlocalizedName()));
		registerItemBlock(registry, new ItemBlock(blockComputerOff));
		registerItemBlock(registry, new ItemTransportalizer(transportalizer));
		
		registerItemBlock(registry, new ItemPunchDesignix(punchDesignix));
		registerItemBlock(registry, new ItemTotemLathe(totemlathe[0]));
		registerItemBlock(registry, new ItemAlchemiter(alchemiter[0]));
		registerItemBlock(registry, new ItemCruxtruder(cruxtruder));
		registerItemBlock(registry, new ItemBlock(cruxtruderLid));
		registerItemBlock(registry, cruxiteDowel);
		
		registerItemBlock(registry, new ItemBlockLayered(layeredSand));
		registerItemBlock(registry, new ItemMultiTexture(coloredDirt, coloredDirt,
				(ItemStack input) -> BlockColoredDirt.BlockType.values()[input.getItemDamage() % BlockColoredDirt.BlockType.values().length].getName()));
		registerItemBlock(registry, new ItemBlock(petrifiedLog));
		registerItemBlock(registry, new ItemBlock(petrifiedPoppy));
		registerItemBlock(registry, new ItemBlock(petrifiedGrass));
		registerItemBlock(registry, new ItemBlock(bloomingCactus));
		registerItemBlock(registry, new ItemBlock(desertBush));
		registerItemBlock(registry, new ItemBlock(glowingMushroom));
		registerItemBlock(registry, new ItemBlock(glowingLog));
		registerItemBlock(registry, new ItemBlockCraftingTab(glowingPlanks, CreativeTabs.BUILDING_BLOCKS));
		registerItemBlock(registry, new ItemBlock(frostPlanks));
		registerItemBlock(registry, new ItemMultiTexture(stone, stone,
				(ItemStack input) -> BlockMinestuckStone.BlockType.getFromMeta(input.getMetadata()).getUnlocalizedName()));
		registerItemBlock(registry, new ItemBlockCraftingTab(coarseStoneStairs, CreativeTabs.BUILDING_BLOCKS));
		registerItemBlock(registry, new ItemBlockCraftingTab(shadeBrickStairs, CreativeTabs.BUILDING_BLOCKS));
		registerItemBlock(registry, new ItemBlockCraftingTab(frostBrickStairs, CreativeTabs.BUILDING_BLOCKS));
		registerItemBlock(registry, new ItemBlockCraftingTab(castIronStairs, CreativeTabs.BUILDING_BLOCKS));
		
		registerItemBlock(registry, new ItemMultiTexture(log, log,
				(ItemStack input) -> BlockMinestuckLog.BlockType.values()[input.getItemDamage() % BlockMinestuckLog.BlockType.values().length].getUnlocalizedName()));
		
		registerItemBlock(registry, new ItemMultiTexture(leaves1, leaves1,
				(ItemStack input) -> BlockMinestuckLeaves1.BlockType.values()[input.getItemDamage() % BlockMinestuckLeaves1.BlockType.values().length].getUnlocalizedName()));
		registerItemBlock(registry, new ItemMultiTexture(planks, planks,
				(ItemStack input) -> BlockMinestuckPlanks.BlockType.values()[Math.min(input.getItemDamage(), BlockMinestuckPlanks.BlockType.values().length - 1)].getUnlocalizedName()));
				//Temporarily changed to this mechanism for handling larger inputs so that any leftover blocks at value 15 will convert properly.
		
		registerItemBlock(registry, new ItemMultiTexture(aspectSapling, aspectSapling,
				(ItemStack input) -> BlockAspectSapling.BlockType.values()[input.getItemDamage() % BlockAspectSapling.BlockType.values().length].getUnlocalizedName()));
		
		registerItemBlock(registry, new ItemBlock(rainbowSapling));
		
		registerItemBlock(registry, new ItemMultiTexture(aspectLog1, aspectLog1,
				(ItemStack input) -> BlockAspectLog.BlockType.values()[input.getItemDamage() % BlockAspectLog.BlockType.values().length].getUnlocalizedName()));
		registerItemBlock(registry, new ItemMultiTexture(aspectLog2, aspectLog2,
				(ItemStack input) -> BlockAspectLog2.BlockType.values()[input.getItemDamage() % BlockAspectLog2.BlockType.values().length].getUnlocalizedName()));
		registerItemBlock(registry, new ItemMultiTexture(aspectLog3, aspectLog3,
				(ItemStack input) -> BlockAspectLog3.BlockType.values()[input.getItemDamage() % BlockAspectLog3.BlockType.values().length].getUnlocalizedName()));
		
		registerItemBlock(registry, new ItemMultiTexture(blockLaptopOff, blockLaptopOff,
				(ItemStack input) -> BlockVanityLaptopOff.BlockType.values()[input.getItemDamage() % BlockVanityLaptopOff.BlockType.values().length].getUnlocalizedName()));
		
		registerItemBlock(registry, new ItemBlock(woodenCactus));
		registerItemBlock(registry, new ItemBlock(sugarCube));
		registerItemBlock(registry, new ItemBlock(appleCake)).setMaxStackSize(1);
		registerItemBlock(registry, new ItemBlock(blueCake)).setMaxStackSize(1);
		registerItemBlock(registry, new ItemBlock(coldCake)).setMaxStackSize(1);
		registerItemBlock(registry, new ItemBlock(redCake)).setMaxStackSize(1);
		registerItemBlock(registry, new ItemBlock(hotCake)).setMaxStackSize(1);
		registerItemBlock(registry, new ItemBlock(reverseCake)).setMaxStackSize(1);
		
		registerItemBlock(registry, new ItemBlock(floweryMossBrick));
		registerItemBlock(registry, new ItemBlock(floweryMossStone));
		registerItemBlock(registry, new ItemBlock(treatedPlanks));
		registerItemBlock(registry, new ItemBlock(coarseEndStone));
		registerItemBlock(registry, new ItemBlock(endLog));
		registerItemBlock(registry, new ItemBlock(endLeaves));
		registerItemBlock(registry, new ItemBlock(endPlanks));
		registerItemBlock(registry, new ItemBlock(endSapling));
		registerItemBlock(registry, new ItemBlock(endGrass));
		
		registerItemBlock(registry, new ItemBlock(primedTnt));
		registerItemBlock(registry, new ItemBlock(unstableTnt));
		registerItemBlock(registry, new ItemBlock(instantTnt));
		registerItemBlock(registry, new ItemBlock(woodenExplosiveButton));
		registerItemBlock(registry, new ItemBlock(stoneExplosiveButton));
		
		registerItemBlock(registry, new ItemBlock(uraniumCooker));
		
		//hammers
		registry.register(clawHammer.setRegistryName("claw_hammer"));
		registry.register(sledgeHammer.setRegistryName("sledge_hammer"));
		registry.register(blacksmithHammer.setRegistryName("blacksmith_hammer"));
		registry.register(pogoHammer.setRegistryName("pogo_hammer"));
		registry.register(telescopicSassacrusher.setRegistryName("telescopic_sassacrusher"));
		registry.register(regiHammer.setRegistryName("regi_hammer"));
		registry.register(fearNoAnvil.setRegistryName("fear_no_anvil"));
		registry.register(zillyhooHammer.setRegistryName("zillyhoo_hammer"));
		registry.register(popamaticVrillyhoo.setRegistryName("popamatic_vrillyhoo"));
		registry.register(scarletZillyhoo.setRegistryName("scarlet_zillyhoo"));
		registry.register(mwrthwl.setRegistryName("mwrthwl"));
		
		//blades
		registry.register(sord.setRegistryName("sord"));
		registry.register(cactusCutlass.setRegistryName("cactaceae_cutlass"));
		registry.register(steakSword.setRegistryName("steak_sword"));
		registry.register(katana.setRegistryName("katana"));
		registry.register(unbreakableKatana.setRegistryName("unbreakable_katana"));
		registry.register(firePoker.setRegistryName("fire_poker"));
		registry.register(hotHandle.setRegistryName("too_hot_to_handle"));
		registry.register(caledscratch.setRegistryName("caledscratch"));
		registry.register(caledfwlch.setRegistryName("caledfwlch"));
		registry.register(royalDeringer.setRegistryName("royal_deringer"));
		registry.register(zillywairCutlass.setRegistryName("cutlass_of_zillywair"));
		registry.register(regisword.setRegistryName("regisword"));
		registry.register(scarletRibbitar.setRegistryName("scarlet_ribbitar"));
		registry.register(doggMachete.setRegistryName("dogg_machete"));
		registry.register(cobaltSabre.setRegistryName("cobalt_sabre"));
		registry.register(quantumSabre.setRegistryName("quantum_sabre"));
		
		//axes
		registry.register(copseCrusher.setRegistryName("copse_crusher"));
		registry.register(blacksmithBane.setRegistryName("blacksmith_bane"));
		registry.register(scraxe.setRegistryName("scraxe"));
		registry.register(qPHammerAxe.setRegistryName("piston_powered_pogo_axehammer"));
		registry.register(rubyCroak.setRegistryName("ruby_croak"));
		registry.register(hephaestusLumber.setRegistryName("hephaestus_lumberjack"));
		registry.register(qFHammerAxe.setRegistryName("fission_focused_fault_feller"));
		
		//Dice
		registry.register(dice.setRegistryName("dice"));
		registry.register(fluoriteOctet.setRegistryName("fluorite_octet"));
		//misc weapons
		registry.register(catClaws.setRegistryName("catclaws"));
		//sickles
		registry.register(sickle.setRegistryName("sickle"));
		registry.register(homesSmellYaLater.setRegistryName("homes_smell_ya_later"));
		registry.register(fudgeSickle.setRegistryName("fudgesickle"));
		registry.register(regiSickle.setRegistryName("regisickle"));
		registry.register(clawSickle.setRegistryName("claw_sickle"));
		registry.register(candySickle.setRegistryName("candy_sickle"));
		
		//clubs
		registry.register(deuceClub.setRegistryName("deuce_club"));
		registry.register(nightClub.setRegistryName("nightclub"));
		registry.register(pogoClub.setRegistryName("pogo_club"));
		registry.register(metalBat.setRegistryName("metal_bat"));
		registry.register(spikedClub.setRegistryName("spiked_club"));
		
		//canes
		registry.register(cane.setRegistryName("cane"));
		registry.register(ironCane.setRegistryName("iron_cane"));
		registry.register(spearCane.setRegistryName("spear_cane"));
		registry.register(regiCane.setRegistryName("regi_cane"));
		registry.register(dragonCane.setRegistryName("dragon_cane"));
		registry.register(pogoCane.setRegistryName("pogo_cane"));
		registry.register(upStick.setRegistryName("uranium_powered_stick"));
		//Spoons/forks
		registry.register(woodenSpoon.setRegistryName("wooden_spoon"));
		registry.register(silverSpoon.setRegistryName("silver_spoon"));
		registry.register(crockerSpork.setRegistryName("crocker_spork"));
		registry.register(skaiaFork.setRegistryName("skaia_fork"));
		registry.register(fork.setRegistryName("fork"));
		registry.register(spork.setRegistryName("spork"));
		registry.register(goldenSpork.setRegistryName("golden_spork"));
		
		registry.register(emeraldSword.setRegistryName("emerald_sword"));
		registry.register(emeraldAxe.setRegistryName("emerald_axe"));
		registry.register(emeraldPickaxe.setRegistryName("emerald_pickaxe"));
		registry.register(emeraldShovel.setRegistryName("emerald_shovel"));
		registry.register(emeraldHoe.setRegistryName("emerald_hoe"));
		
		//armor
		registry.register(prismarineHelmet.setRegistryName("prismarine_helmet"));
		registry.register(prismarineChestplate.setRegistryName("prismarine_chestplate"));
		registry.register(prismarineLeggings.setRegistryName("prismarine_leggings"));
		registry.register(prismarineBoots.setRegistryName("prismarine_boots"));
		
		//food
		registry.register(candy.setRegistryName("candy"));
		registry.register(beverage.setRegistryName("beverage"));
		registry.register(bugOnAStick.setRegistryName("bug_on_stick"));
		registry.register(chocolateBeetle.setRegistryName("chocolate_beetle"));
		registry.register(coneOfFlies.setRegistryName("cone_of_flies"));
		registry.register(grasshopper.setRegistryName("grasshopper"));
		registry.register(jarOfBugs.setRegistryName("jar_of_bugs"));
		registry.register(onion.setRegistryName("onion"));
		registry.register(salad.setRegistryName("salad"));
		registry.register(desertFruit.setRegistryName("desert_fruit"));
		registry.register(irradiatedSteak.setRegistryName("irradiated_steak"));
		registry.register(rockCookie.setRegistryName("rock_cookie"));
		registry.register(surpriseEmbryo.setRegistryName("surprise_embryo"));
		
		//misc
		registry.register(boondollars.setRegistryName("boondollars"));
		registry.register(rawCruxite.setRegistryName("raw_cruxite"));
		registry.register(rawUranium.setRegistryName("raw_uranium"));
		registry.register(energyCore.setRegistryName("energy_core"));
		registry.register(captchaCard.setRegistryName("captcha_card"));
		registry.register(cruxiteApple.setRegistryName("cruxite_apple"));
		registry.register(cruxitePotion.setRegistryName("cruxite_potion"));
		registry.register(disk.setRegistryName("computer_disk"));
		registry.register(chessboard.setRegistryName("chessboard"));
		registry.register(minestuckBucket.setRegistryName("minestuck_bucket"));
		registry.register(obsidianBucket.setRegistryName("bucket_obsidian"));
		registry.register(modusCard.setRegistryName("modus_card"));
		registry.register(goldSeeds.setRegistryName("gold_seeds"));
		registry.register(metalBoat.setRegistryName("metal_boat"));
		registry.register(threshDvd.setRegistryName("thresh_dvd"));
		registry.register(crewPoster.setRegistryName("crew_poster"));
		registry.register(sbahjPoster.setRegistryName("sbahj_poster"));
		registry.register(carvingTool.setRegistryName("carving_tool"));
		registry.register(crumplyHat.setRegistryName("crumply_hat"));
		registry.register(frogStatueReplica.setRegistryName("frog_statue_replica"));
		registry.register(stoneEyeballs.setRegistryName("stone_eyeballs"));
		registry.register(stoneSlab.setRegistryName("stone_slab"));
		registry.register(glowystoneDust.setRegistryName("glowystone_dust"));
		registry.register(fakeArms.setRegistryName("fake_arms"));
		
		//Music disks
		registry.register(recordEmissaryOfDance.setRegistryName("record_emissary"));
		registry.register(recordDanceStab.setRegistryName("record_dance_stab"));
		
		((ItemMinestuckBucket) minestuckBucket).addBlock(blockOil.getDefaultState());
		((ItemMinestuckBucket) minestuckBucket).addBlock(blockBlood.getDefaultState());
		((ItemMinestuckBucket) minestuckBucket).addBlock(blockBrainJuice.getDefaultState());
		((ItemMinestuckBucket) minestuckBucket).addBlock(blockWatercolors.getDefaultState());
		((ItemMinestuckBucket) minestuckBucket).addBlock(blockEnder.getDefaultState());
		/*for(Block block : liquidGrists)
		{
			minestuckBucket.addBlock(block.getDefaultState());
		}*/
		
		toolUranium.setRepairItem(new ItemStack(rawUranium));
		ItemWeapon.addToolMaterial("pickaxe", Arrays.asList(Material.IRON, Material.ANVIL, Material.ROCK));
		ItemWeapon.addToolMaterial("axe", Arrays.asList(Material.WOOD, Material.PLANTS, Material.VINE));
		ItemWeapon.addToolMaterial("shovel", Arrays.asList(Material.SNOW, Material.CRAFTED_SNOW, Material.CLAY, Material.GRASS, Material.GROUND, Material.SAND));
		ItemWeapon.addToolMaterial("sword", Arrays.asList(Material.WEB));
		ItemWeapon.addToolMaterial("sickle", Arrays.asList(Material.WEB, Material.LEAVES, Material.PLANTS, Material.VINE));
	}

	private static Item registerItemBlock(IForgeRegistry<Item> registry, ItemBlock item)
	{
		registry.register(item.setRegistryName(item.getBlock().getRegistryName()));
		return item;
	}
}
