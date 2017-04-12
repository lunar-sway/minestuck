package com.mraof.minestuck.item;

import com.google.common.base.Function;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.BlockColoredDirt;
import com.mraof.minestuck.block.BlockCrockerMachine;
import com.mraof.minestuck.block.BlockMinestuckLog;
import com.mraof.minestuck.block.BlockMinestuckStone;
import com.mraof.minestuck.item.block.ItemBlockLayered;
import com.mraof.minestuck.item.block.ItemSburbMachine;
import com.mraof.minestuck.item.weapon.*;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;

import static com.mraof.minestuck.block.MinestuckBlocks.*;

public class MinestuckItems
{
	//hammers
	public static Item clawHammer;
	public static Item sledgeHammer;
	public static Item blacksmithHammer;
	public static Item pogoHammer;
	public static Item telescopicSassacrusher;
	public static Item fearNoAnvil;
	public static Item zillyhooHammer;
	public static Item popamaticVrillyhoo;
	public static Item scarletZillyhoo;
	public static Item mwrthwl;
	//blades
	public static Item sord;
	public static Item cactusCutlass;
	public static Item katana;
	public static Item unbreakableKatana;	//Not actually unbreakable
	public static Item firePoker;
	public static Item hotHandle;
	public static Item caledscratch;
	public static Item caledfwlch;
	public static Item royalDeringer;
	public static Item zillywairCutlass;
	public static Item regisword;
	public static Item scarletRibbitar;
	public static Item doggMachete;
	//axes
	public static Item blacksmithBane;
	public static Item scraxe;
	public static Item rubyCroak;
	public static Item hephaestusLumber;
	public static Item copseCrusher;
	public static Item qPHammerAxe;
	//Dice
	public static Item dice;
	public static Item fluoriteOctet;
	//sickles
	public static Item sickle;
	public static Item homesSmellYaLater;
	public static Item fudgeSickle;
	public static Item regiSickle;
	public static Item clawSickle;
	public static Item candySickle;
	//clubs
	public static Item deuceClub;
	public static Item nightClub;
	public static Item pogoClub;
	public static Item metalBat;
	public static Item spikedClub;
	//canes
	public static Item cane;
	public static Item spearCane;
	public static Item dragonCane;
	//Spoons/forks
	public static Item woodenSpoon;
	public static Item silverSpoon;
	public static ItemSpork crockerSpork;
	public static Item skaiaFork;
	public static Item fork;
	public static Item spork;
	//Other
	public static Item rawCruxite;
	public static Item cruxiteDowel;
	public static Item captchaCard;
	public static ItemCruxiteArtifact cruxiteApple;
	public static Item cruxitePotion;
	public static Item disk;
	public static Item chessboard;
	public static ItemModus modusCard;
	public static ItemMinestuckBucket minestuckBucket;
	public static ItemGoldSeeds goldSeeds;	//This item is pretty much only a joke
	public static ItemMetalBoat metalBoat;
	public static Item obsidianBucket;
	public static Item emeraldSword;
	public static Item emeraldAxe;
	public static Item emeraldPickaxe;
	public static Item emeraldShovel;
	public static Item emeraldHoe;
	public static Item prismarineHelmet;
	public static Item prismarineChestplate;
	public static Item prismarineLeggings;
	public static Item prismarineBoots;
	public static Item candy;
	public static Item threshDvd;
	public static Item crewPoster;
	public static Item sbahjPoster;
	
	public static Item.ToolMaterial toolEmerald;
	public static ItemArmor.ArmorMaterial armorPrismarine;
	
	public static void registerItems()
	{
		//hammers
		clawHammer = GameRegistry.register(new ItemWeapon(131, 4.0D, -2.4D, 10, "clawHammer").setTool("pickaxe", 0, 1.0F).setRegistryName("claw_hammer"));
		sledgeHammer = GameRegistry.register(new ItemWeapon(250, 6.0D, -2.8D, 8, "sledgeHammer").setTool("pickaxe", 2, 4.0F).setRegistryName("sledge_hammer"));
		blacksmithHammer = GameRegistry.register(new ItemWeapon(450, 7.0D, -2.8D, 10, "blacksmithHammer").setTool("pickaxe", 2, 3.5F).setRegistryName("blacksmith_hammer"));
		pogoHammer = GameRegistry.register(new ItemPogoWeapon(400, 7.0D, -2.8D, 8, "pogoHammer", 0.7).setTool("pickaxe", 1, 2.0F).setRegistryName("pogo_hammer"));
		telescopicSassacrusher = GameRegistry.register(new ItemWeapon(1024, 9.0D, -2.9D, 15, "telescopicSassacrusher").setTool("pickaxe", 2, 5.0F).setRegistryName("telescopic_sassacrusher"));
		fearNoAnvil = GameRegistry.register(new ItemPotionWeapon(2048, 10.0D, -2.8D, 12, "fearNoAnvil", new PotionEffect(MobEffects.SLOWNESS,100,3)).setTool("pickaxe", 3, 7.0F).setRegistryName("fear_no_anvil"));
		zillyhooHammer = GameRegistry.register(new ItemWeapon(3000, 11.0D, -2.8D, 30, "zillyhooHammer").setTool("pickaxe", 4, 15.0F).setRegistryName("zillyhoo_hammer"));
		popamaticVrillyhoo = GameRegistry.register(new ItemWeapon(3000, 0.0D, -2.8D, 30, "popamaticVrillyhoo").setTool("pickaxe", 4, 15.0F).setRegistryName("popamatic_vrillyhoo"));
		scarletZillyhoo = GameRegistry.register(new ItemFireWeapon(2000, 11.0D, -2.8D, 16, "scarletZillyhoo", 50).setTool("pickaxe", 3, 4.0F).setRegistryName("scarlet_zillyhoo"));
		mwrthwl = GameRegistry.register(new ItemWeapon(2000, 10.5D, -2.8D, 16, "mwrthwl").setTool("pickaxe", 3, 4.0F).setRegistryName("mwrthwl"));
				
		//blades
		sord = GameRegistry.register(new ItemSord(59, 2, -2.4D, 5, "sord").setRegistryName("sord"));
		cactusCutlass = GameRegistry.register(new ItemWeapon(104, 4, -2.4D, 10, "cactaceaeCutlass").setRegistryName("cactaceae_cutlass"));
		katana = GameRegistry.register(new ItemWeapon(250, 5, -2.4D, 15, "ninjaSword").setRegistryName("katana"));
		unbreakableKatana = GameRegistry.register(new ItemWeapon(2200, 7, -2.4D, 20, "katana").setRegistryName("unbreakable_katana"));
		firePoker = GameRegistry.register(new ItemFireWeapon(250, 6, -2.4D, 15, "firePoker", 30).setRegistryName("fire_poker"));
		hotHandle = GameRegistry.register(new ItemFireWeapon(350, 5, -2.4D, 15, "hotHandle", 10).setRegistryName("too_hot_to_handle"));
		caledscratch = GameRegistry.register(new ItemWeapon(1561, 6, -2.4D, 30, "caledscratch").setRegistryName("caledscratch"));
		caledfwlch = GameRegistry.register(new ItemWeapon(1025, 6, -2.4D, 30, "caledfwlch").setRegistryName("caledfwlch"));
		royalDeringer = GameRegistry.register(new ItemWeapon(1561, 7, -2.4D, 30, "royalDeringer").setRegistryName("royal_deringer"));
		zillywairCutlass = GameRegistry.register(new ItemWeapon(2500, 8, -2.4D, 30, "zillywairCutlass").setRegistryName("cutlass_of_zillywair"));
		regisword = GameRegistry.register(new ItemWeapon(812, 6, -2.4D, 10, "regisword").setRegistryName("regisword"));
		scarletRibbitar = GameRegistry.register(new ItemWeapon(2000, 7, -2.4D, 30, "scarletRibbitar").setRegistryName("scarlet_ribbitar"));
		doggMachete = GameRegistry.register(new ItemWeapon(1000, 5, -2.4D, 30, "doggMachete").setRegistryName("dogg_machete"));

		//axes
		blacksmithBane = GameRegistry.register(new ItemWeapon(413, 9.0D, -3.0D, 15, "blacksmithBane").setTool("axe", 2, 6.0F).setRegistryName("blacksmith_bane"));
		scraxe = GameRegistry.register(new ItemWeapon(500, 10.0D, -3.0D, 20, "scraxe").setTool("axe", 2, 7.0F).setRegistryName("scraxe"));
		rubyCroak = GameRegistry.register(new ItemWeapon(2000, 11.0D, -2.9D, 30, "rubyCroak").setTool("axe", 3, 8.0F).setRegistryName("ruby_croak"));
		hephaestusLumber = GameRegistry.register(new ItemFireWeapon(3000, 11.0D, -3.0D, 30, "hephaestusLumber", 30).setTool("axe", 3, 9.0F).setRegistryName("hephaestus_lumberjack"));
		copseCrusher = GameRegistry.register(new ItemWeapon(500, 10.0D, -3.0D, 20, "copseCrusher", 5).setTool("axe", 2, 7.0F).setRegistryName("copse_crusher"));
		qPHammerAxe = GameRegistry.register(new ItemPogoWeapon(1000, 10.0D, -2.5D, 30, "qPHammerAxe", 0.6).setTool("axe", 2, 7.0F).setTool("pickaxe", 1, 2.0F).setRadius(10).setRegistryName("piston_powered_pogo_axehammer"));
		
		//Dice
		dice = GameRegistry.register(new ItemWeapon(51, 6, 3, 6, "dice").setRegistryName("dice"));
		fluoriteOctet = GameRegistry.register(new ItemWeapon(67, 15, 6, 8, "fluoriteOctet").setRegistryName("fluorite_octet"));
		
		//sickles
		sickle = GameRegistry.register(new ItemWeapon(220, 4.0D, -2.4D, 8, "sickle").setRegistryName("sickle"));
		homesSmellYaLater = GameRegistry.register(new ItemWeapon(400, 5.5D, -2.4D, 10, "homesSmellYaLater").setRegistryName("homes_smell_ya_later"));
		fudgeSickle = GameRegistry.register(new ItemWeapon(450, 5.5D, -2.4D, 10, "fudgeSickle").setRegistryName("fudgesickle"));
		regiSickle = GameRegistry.register(new ItemWeapon(812, 6.0D, -2.4D, 5, "regiSickle").setRegistryName("regisickle"));
		clawSickle = GameRegistry.register(new ItemWeapon(2048, 7.0D, -2.4D, 15, "clawSickle").setRegistryName("claw_sickle"));
		candySickle = GameRegistry.register(new ItemCandyWeapon(96, 6.0D, -2.4D, 15, "candySickle").setRegistryName("candy_sickle"));

		//clubs
		deuceClub = GameRegistry.register(new ItemWeapon(1024, 2.5D, -2.2D, 15, "deuceClub").setRegistryName("deuce_club"));
		nightClub = GameRegistry.register(new ItemWeapon(600, 4.0D, -2.2D, 20, "nightClub").setRegistryName("nightclub"));
		pogoClub = GameRegistry.register(new ItemPogoWeapon(600, 3.5D, -2.2D, 15, "pogoClub", 0.5).setRegistryName("pogo_club"));
		metalBat = GameRegistry.register(new ItemWeapon(750, 5.0D, -2.2D, 5, "metalBat").setRegistryName("metal_bat"));
		spikedClub = GameRegistry.register(new ItemWeapon(500, 5.5D, -2.2D, 5, "spikedClub").setRegistryName("spiked_club"));

		//canes
		cane = GameRegistry.register(new ItemWeapon(100, 2.0D, -2.0D, 15, "cane").setRegistryName("cane"));
		spearCane = GameRegistry.register(new ItemWeapon(300, 4.0D, -2.0D, 13, "spearCane").setRegistryName("spear_cane"));
		dragonCane = GameRegistry.register(new ItemWeapon(300, 6.5D, -2.0D, 20, "dragonCane").setRegistryName("dragon_cane"));
		
		//Spoons/forks
		woodenSpoon = GameRegistry.register(new ItemWeapon(59, 2.0D, -2.2D, 5, "woodenSpoon").setRegistryName("wooden_spoon"));
		silverSpoon = GameRegistry.register(new ItemWeapon(250, 2.5D, -2.2D, 12, "silverSpoon").setRegistryName("silver_spoon"));
		crockerSpork = (ItemSpork) GameRegistry.register(new ItemSpork(512, 4.0D, -2.2D, 15, "crocker").setRegistryName("crocker_spork"));
		skaiaFork = GameRegistry.register(new ItemWeapon(2048, 8.5D, -2.2D, 10, "skaiaFork").setRegistryName("skaia_fork"));
		fork = GameRegistry.register(new ItemWeapon(100, 4.0D, -2.2D, 3, "fork").setRegistryName("fork"));
		spork = GameRegistry.register(new ItemWeapon(120, 4.5D, -2.3D, 5, "spork").setRegistryName("spork"));
		
		toolEmerald = EnumHelper.addToolMaterial("EMERALD", 3, 1220, 12.0F, 4.0F, 12).setRepairItem(new ItemStack(Items.EMERALD));
		emeraldSword = GameRegistry.register(new ItemSword(toolEmerald).setRegistryName("emerald_sword")).setUnlocalizedName("swordEmerald").setCreativeTab(Minestuck.tabMinestuck);
		emeraldAxe = GameRegistry.register(new ItemMinestuckAxe(toolEmerald, 9.0F, -3.0F).setRegistryName("emerald_axe")).setUnlocalizedName("hatchetEmerald").setCreativeTab(Minestuck.tabMinestuck);
		emeraldPickaxe = GameRegistry.register(new ItemMinestuckPickaxe(toolEmerald).setRegistryName("emerald_pickaxe")).setUnlocalizedName("pickaxeEmerald").setCreativeTab(Minestuck.tabMinestuck);
		emeraldShovel = GameRegistry.register(new ItemSpade(toolEmerald).setRegistryName("emerald_shovel")).setUnlocalizedName("shovelEmerald").setCreativeTab(Minestuck.tabMinestuck);
		emeraldHoe = GameRegistry.register(new ItemHoe(toolEmerald).setRegistryName("emerald_hoe")).setUnlocalizedName("hoeEmerald").setCreativeTab(Minestuck.tabMinestuck);
		
		//armor
		armorPrismarine = EnumHelper.addArmorMaterial("PRISMARINE", "minestuck:prismarine", 20, new int[]{3, 7, 6, 2}, 15, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0.0F);
		armorPrismarine.customCraftingMaterial = Items.PRISMARINE_SHARD;
		prismarineHelmet = GameRegistry.register(new ItemArmor(armorPrismarine, 0, EntityEquipmentSlot.HEAD).setRegistryName("prismarine_helmet")).setUnlocalizedName("helmetPrismarine").setCreativeTab(Minestuck.tabMinestuck);
		prismarineChestplate = GameRegistry.register(new ItemArmor(armorPrismarine, 0, EntityEquipmentSlot.CHEST).setRegistryName("prismarine_chestplate")).setUnlocalizedName("chestplatePrismarine").setCreativeTab(Minestuck.tabMinestuck);
		prismarineLeggings = GameRegistry.register(new ItemArmor(armorPrismarine, 0, EntityEquipmentSlot.LEGS).setRegistryName("prismarine_leggings")).setUnlocalizedName("leggingsPrismarine").setCreativeTab(Minestuck.tabMinestuck);
		prismarineBoots = GameRegistry.register(new ItemArmor(armorPrismarine, 0, EntityEquipmentSlot.FEET).setRegistryName("prismarine_boots")).setUnlocalizedName("bootsPrismarine").setCreativeTab(Minestuck.tabMinestuck);
		
		//misc
		rawCruxite = GameRegistry.register(new Item().setRegistryName("raw_cruxite")).setUnlocalizedName("rawCruxite").setCreativeTab(Minestuck.tabMinestuck);
		cruxiteDowel = GameRegistry.register(new ItemDowel().setRegistryName("cruxite_dowel"));
		captchaCard = GameRegistry.register(new ItemCaptchaCard().setRegistryName("captcha_card"));
		cruxiteApple = (ItemCruxiteArtifact) GameRegistry.register(new ItemCruxiteApple().setRegistryName("cruxite_apple"));
		cruxitePotion = GameRegistry.register(new ItemCruxitePotion().setRegistryName("cruxite_potion"));
		disk = GameRegistry.register(new ItemDisk().setRegistryName("computer_disk"));
		chessboard = GameRegistry.register(new Item().setRegistryName("chessboard")).setUnlocalizedName("chessboard").setMaxStackSize(1).setCreativeTab(Minestuck.tabMinestuck);
		minestuckBucket = (ItemMinestuckBucket) GameRegistry.register(new ItemMinestuckBucket().setRegistryName("minestuck_bucket"));
		obsidianBucket = GameRegistry.register(new ItemObsidianBucket().setRegistryName("bucket_obsidian"));
		modusCard = (ItemModus) GameRegistry.register(new ItemModus().setRegistryName("modus_card"));
		goldSeeds = (ItemGoldSeeds) GameRegistry.register(new ItemGoldSeeds().setRegistryName("gold_seeds"));
		metalBoat = (ItemMetalBoat) GameRegistry.register(new ItemMetalBoat().setRegistryName("metal_boat"));
		candy = GameRegistry.register(new ItemMinestuckCandy().setRegistryName("candy"));
		threshDvd = GameRegistry.register(new Item().setRegistryName("thresh_dvd")).setUnlocalizedName("threshDvd").setMaxStackSize(1).setCreativeTab(Minestuck.tabMinestuck);
		crewPoster = GameRegistry.register(new Item().setRegistryName("crew_poster")).setUnlocalizedName("crewPoster").setMaxStackSize(1).setCreativeTab(Minestuck.tabMinestuck);
		sbahjPoster = GameRegistry.register(new Item().setRegistryName("sbahj_poster")).setUnlocalizedName("sbahjPoster").setMaxStackSize(1).setCreativeTab(Minestuck.tabMinestuck);
		
		minestuckBucket.addBlock(blockOil.getDefaultState());
		minestuckBucket.addBlock(blockBlood.getDefaultState());
		minestuckBucket.addBlock(blockBrainJuice.getDefaultState());
		/*for(Block block : liquidGrists)
		{
		    minestuckBucket.addBlock(block.getDefaultState());
		}*/
		
		registerItemBlock(new ItemMultiTexture(chessTile, chessTile, new String[] {"black", "white", "darkgrey", "lightgrey"}));
		registerItemBlock(new ItemBlock(skaiaPortal));
		
		registerItemBlock(new ItemMultiTexture(oreCruxite, oreCruxite, new String[0])
		{
			@Override
			public String getUnlocalizedName(ItemStack stack)
			{
				return theBlock.getUnlocalizedName();
			}
		});
		registerItemBlock(new ItemBlock(coalOreNetherrack));
		registerItemBlock(new ItemBlock(ironOreSandstone));
		registerItemBlock(new ItemBlock(ironOreSandstoneRed));
		registerItemBlock(new ItemBlock(goldOreSandstone));
		registerItemBlock(new ItemBlock(goldOreSandstoneRed));
		
		registerItemBlock(new ItemBlock(cruxiteBlock));
		registerItemBlock(new ItemBlock(genericObject));
		registerItemBlock(new ItemSburbMachine(sburbMachine));
		registerItemBlock(new ItemMultiTexture(crockerMachine, crockerMachine, new Function<ItemStack, String>()
				{
					@Override
					public String apply(ItemStack input)
					{
						return BlockCrockerMachine.MachineType.values()[input.getItemDamage()].getUnlocalizedName();
					}
				}));
		registerItemBlock(new ItemBlock(blockComputerOff));
		registerItemBlock(new ItemBlock(transportalizer));
		
		registerItemBlock(new ItemBlockLayered(layeredSand));
		registerItemBlock(new ItemMultiTexture(coloredDirt, coloredDirt, new Function<ItemStack, String>()
				{
					@Override
					public String apply(ItemStack input)
					{
						return BlockColoredDirt.BlockType.values()[input.getItemDamage()].getName();
					}
				}));
		registerItemBlock(new ItemBlock(glowingMushroom));
		registerItemBlock(new ItemBlock(glowingLog));
		registerItemBlock(new ItemMultiTexture(stone, stone, new Function<ItemStack, String>()
		{
			@Override
			public String apply(ItemStack input)
			{
				return BlockMinestuckStone.BlockType.getFromMeta(input.getMetadata()).getUnlocalizedName();
			}
		}));
		registerItemBlock(new ItemBlock(coarseStoneStairs));
		registerItemBlock(new ItemBlock(shadeBrickStairs));
		registerItemBlock(new ItemBlock(frostBrickStairs));
		registerItemBlock(new ItemBlock(castIronStairs));
		registerItemBlock(new ItemMultiTexture(log, log, new Function<ItemStack, String>()
		{
			@Override
			public String apply(ItemStack input)
			{
				return BlockMinestuckLog.BlockType.values()[input.getItemDamage()].getUnlocalizedName();
			}
		}));
		registerItemBlock(new ItemBlock(woodenCactus));
		
		registerItemBlock(new ItemBlock(primedTnt));
		registerItemBlock(new ItemBlock(unstableTnt));
		registerItemBlock(new ItemBlock(instantTnt));
		registerItemBlock(new ItemBlock(woodenExplosiveButton));
		registerItemBlock(new ItemBlock(stoneExplosiveButton));
	}
	
	private static Item registerItemBlock(ItemBlock item)
	{
		return GameRegistry.register(item.setRegistryName(item.block.getRegistryName()));
	}
}