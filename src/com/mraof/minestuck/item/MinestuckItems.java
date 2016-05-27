package com.mraof.minestuck.item;

import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.mraof.minestuck.Minestuck;

import static com.mraof.minestuck.block.MinestuckBlocks.*;

import com.mraof.minestuck.item.block.ItemBlockLayered;
import com.mraof.minestuck.item.block.ItemChessTile;
import com.mraof.minestuck.item.block.ItemColoredDirt;
import com.mraof.minestuck.item.block.ItemMachine;
import com.mraof.minestuck.item.block.ItemCruxiteOre;
import com.mraof.minestuck.item.block.ItemStorageBlock;
import com.mraof.minestuck.item.weapon.EnumBattleaxeType;
import com.mraof.minestuck.item.weapon.EnumBladeType;
import com.mraof.minestuck.item.weapon.EnumCaneType;
import com.mraof.minestuck.item.weapon.EnumClubType;
import com.mraof.minestuck.item.weapon.EnumDiceType;
import com.mraof.minestuck.item.weapon.EnumHammerType;
import com.mraof.minestuck.item.weapon.EnumSickleType;
import com.mraof.minestuck.item.weapon.EnumSporkType;
import com.mraof.minestuck.item.weapon.ItemBattleaxe;
import com.mraof.minestuck.item.weapon.ItemBlade;
import com.mraof.minestuck.item.weapon.ItemCane;
import com.mraof.minestuck.item.weapon.ItemClub;
import com.mraof.minestuck.item.weapon.ItemDice;
import com.mraof.minestuck.item.weapon.ItemHammer;
import com.mraof.minestuck.item.weapon.ItemSickle;
import com.mraof.minestuck.item.weapon.ItemSpork;

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
	//blades
	public static Item sord;
	public static Item cactusCutlass;
	public static Item ninjaSword;
	public static Item katana;
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
	//Dice
	public static Item dice;
	public static Item fluoriteOctet;
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
	public static Item fork;
	public static Item spork;
	//Other
	public static Item rawCruxite;
	public static Item cruxiteDowel;
	public static Item captchaCard;
	public static ItemCruxiteArtifact cruxiteApple;
	public static Item cruxitePotion;
	public static Item disk;
	public static Item component;
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
	
	public static Item.ToolMaterial toolEmerald;
	public static ItemArmor.ArmorMaterial armorPrismarine;
	
	public static void registerItems()
	{
		//hammers
		clawHammer = GameRegistry.register(new ItemHammer(EnumHammerType.CLAW).setRegistryName("claw_hammer"));
		sledgeHammer = GameRegistry.register(new ItemHammer(EnumHammerType.SLEDGE).setRegistryName("sledge_hammer"));
		blacksmithHammer = GameRegistry.register(new ItemHammer(EnumHammerType.BLACKSMITH).setRegistryName("blacksmith_hammer"));
		pogoHammer = GameRegistry.register(new ItemHammer(EnumHammerType.POGO).setRegistryName("pogo_hammer"));
		telescopicSassacrusher = GameRegistry.register(new ItemHammer(EnumHammerType.TELESCOPIC).setRegistryName("telescopic_sassacrusher"));
		fearNoAnvil = GameRegistry.register(new ItemHammer(EnumHammerType.FEARNOANVIL).setRegistryName("fear_no_anvil"));
		zillyhooHammer = GameRegistry.register(new ItemHammer(EnumHammerType.ZILLYHOO).setRegistryName("zillyhoo_hammer"));
		popamaticVrillyhoo = GameRegistry.register(new ItemHammer(EnumHammerType.POPAMATIC).setRegistryName("popamatic_vrillyhoo"));
		scarletZillyhoo = GameRegistry.register(new ItemHammer(EnumHammerType.SCARLET).setRegistryName("scarlet_zillyhoo"));
		//blades
		sord = GameRegistry.register(new ItemBlade(EnumBladeType.SORD).setRegistryName("sord"));
		cactusCutlass = GameRegistry.register(new ItemBlade(EnumBladeType.CACTUS).setRegistryName("cactaceae_cutlass"));
		ninjaSword = GameRegistry.register(new ItemBlade(EnumBladeType.NINJA).setRegistryName("ninja_sword"));
		katana = GameRegistry.register(new ItemBlade(EnumBladeType.KATANA).setRegistryName("katana"));
		firePoker = GameRegistry.register(new ItemBlade(EnumBladeType.FIREPOKER).setRegistryName("fire_poker"));
		hotHandle = GameRegistry.register(new ItemBlade(EnumBladeType.HOTHANDLE).setRegistryName("too_hot_to_handle"));
		caledscratch = GameRegistry.register(new ItemBlade(EnumBladeType.CALEDSCRATCH).setRegistryName("caledscratch"));
		caledfwlch = GameRegistry.register(new ItemBlade(EnumBladeType.CALEDFWLCH).setRegistryName("caledfwlch"));
		royalDeringer = GameRegistry.register(new ItemBlade(EnumBladeType.DERINGER).setRegistryName("royal_deringer"));
		zillywairCutlass = GameRegistry.register(new ItemBlade(EnumBladeType.ZILLYWAIR).setRegistryName("cutlass_of_zillywair"));
		regisword = GameRegistry.register(new ItemBlade(EnumBladeType.REGISWORD).setRegistryName("regisword"));
		scarletRibbitar = GameRegistry.register(new ItemBlade(EnumBladeType.SCARLET).setRegistryName("scarlet_ribbitar"));
		doggMachete = GameRegistry.register(new ItemBlade(EnumBladeType.DOGG).setRegistryName("dogg_machete"));
		//axes
		blacksmithBane = GameRegistry.register(new ItemBattleaxe(EnumBattleaxeType.BANE).setRegistryName("blacksmiths_bane"));
		scraxe = GameRegistry.register(new ItemBattleaxe(EnumBattleaxeType.SCRAXE).setRegistryName("scraxe"));
		rubyCroak = GameRegistry.register(new ItemBattleaxe(EnumBattleaxeType.CROAK).setRegistryName("ruby_croak"));
		hephaestusLumber = GameRegistry.register(new ItemBattleaxe(EnumBattleaxeType.HEPH).setRegistryName("hephaestus_lumberjack"));
		//Dice
		dice = GameRegistry.register(new ItemDice(EnumDiceType.DICE).setRegistryName("dice"));
		fluoriteOctet = GameRegistry.register(new ItemDice(EnumDiceType.FLUORITE_OCTET).setRegistryName("fluorite_octet"));
		
		//sickles
		sickle = GameRegistry.register(new ItemSickle(EnumSickleType.SICKLE).setRegistryName("sickle"));
		homesSmellYaLater = GameRegistry.register(new ItemSickle(EnumSickleType.HOMES).setRegistryName("homes_smell_ya_later"));
		regiSickle = GameRegistry.register(new ItemSickle(EnumSickleType.REGISICKLE).setRegistryName("regi_sickle"));
		clawSickle = GameRegistry.register(new ItemSickle(EnumSickleType.CLAW).setRegistryName("claw_sickle"));
		//clubs
		deuceClub = GameRegistry.register(new ItemClub(EnumClubType.DEUCE).setRegistryName("deuce_club"));
		//canes
		cane = GameRegistry.register(new ItemCane(EnumCaneType.CANE).setRegistryName("cane"));
		spearCane = GameRegistry.register(new ItemCane(EnumCaneType.SPEAR).setRegistryName("spear_cane"));
		dragonCane = GameRegistry.register(new ItemCane(EnumCaneType.DRAGON).setRegistryName("dragon_cane"));
		//Spoons/forks
		woodenSpoon = GameRegistry.register(new ItemSpork(EnumSporkType.SPOON_WOOD).setRegistryName("spoon_wood"));
		silverSpoon = GameRegistry.register(new ItemSpork(EnumSporkType.SPOON_SILVER).setRegistryName("spoon_silver"));
		crockerSpork = (ItemSpork) GameRegistry.register(new ItemSpork(EnumSporkType.CROCKER).setRegistryName("crocker_spork"));
		skaiaFork = GameRegistry.register(new ItemSpork(EnumSporkType.SKAIA).setRegistryName("skaia_fork"));
		fork = GameRegistry.register(new ItemSpork(EnumSporkType.FORK).setRegistryName("fork"));
		spork = GameRegistry.register(new ItemSpork(EnumSporkType.SPORK).setRegistryName("spork"));
		
		toolEmerald = EnumHelper.addToolMaterial("EMERALD", 3, 1220, 12.0F, 4.0F, 12).setRepairItem(new ItemStack(Items.emerald));
		emeraldSword = GameRegistry.register(new ItemSword(toolEmerald).setRegistryName("emerald_sword")).setUnlocalizedName("swordEmerald").setCreativeTab(Minestuck.tabMinestuck);
		emeraldAxe = GameRegistry.register(new ItemMinestuckAxe(toolEmerald).setRegistryName("emerald_axe")).setUnlocalizedName("hatchetEmerald").setCreativeTab(Minestuck.tabMinestuck);
		emeraldPickaxe = GameRegistry.register(new ItemMinestuckPickaxe(toolEmerald).setRegistryName("emerald_pickaxe")).setUnlocalizedName("pickaxeEmerald").setCreativeTab(Minestuck.tabMinestuck);
		emeraldShovel = GameRegistry.register(new ItemSpade(toolEmerald).setRegistryName("emerald_shovel")).setUnlocalizedName("shovelEmerald").setCreativeTab(Minestuck.tabMinestuck);
		emeraldHoe = GameRegistry.register(new ItemHoe(toolEmerald).setRegistryName("emerald_hoe")).setUnlocalizedName("hoeEmerald").setCreativeTab(Minestuck.tabMinestuck);
		//armor
		armorPrismarine = EnumHelper.addArmorMaterial("PRISMARINE", "minestuck:prismarine", 20, new int[]{3, 7, 6, 2}, 15, SoundEvents.item_armor_equip_diamond);
		armorPrismarine.customCraftingMaterial = Items.prismarine_shard;
		prismarineHelmet = GameRegistry.register(new ItemArmor(armorPrismarine, 0, EntityEquipmentSlot.HEAD).setRegistryName("prismarine_helmet")).setUnlocalizedName("helmetPrismarine").setCreativeTab(Minestuck.tabMinestuck);
		prismarineChestplate = GameRegistry.register(new ItemArmor(armorPrismarine, 0, EntityEquipmentSlot.CHEST).setRegistryName("prismarine_chestplate")).setUnlocalizedName("chestplatePrismarine").setCreativeTab(Minestuck.tabMinestuck);
		prismarineLeggings = GameRegistry.register(new ItemArmor(armorPrismarine, 0, EntityEquipmentSlot.LEGS).setRegistryName("prismarine_leggings")).setUnlocalizedName("leggingsPrismarine").setCreativeTab(Minestuck.tabMinestuck);
		prismarineBoots = GameRegistry.register(new ItemArmor(armorPrismarine, 0, EntityEquipmentSlot.FEET).setRegistryName("prismarine_boots")).setUnlocalizedName("bootsPrismarine").setCreativeTab(Minestuck.tabMinestuck);
		//misc
		rawCruxite = GameRegistry.register(new ItemCruxiteRaw().setRegistryName("cruxite_raw"));
		cruxiteDowel = GameRegistry.register(new ItemDowel().setRegistryName("cruxite_dowel"));
		captchaCard = GameRegistry.register(new ItemCaptchaCard().setRegistryName("captcha_card"));
		cruxiteApple = (ItemCruxiteArtifact) GameRegistry.register(new ItemCruxiteApple().setRegistryName("cruxite_apple"));
		cruxitePotion = GameRegistry.register(new ItemCruxitePotion().setRegistryName("cruxite_potion"));
		disk = GameRegistry.register(new ItemDisk().setRegistryName("computer_disk"));
		component = GameRegistry.register(new ItemComponent().setRegistryName("component"));
		minestuckBucket = (ItemMinestuckBucket) GameRegistry.register(new ItemMinestuckBucket().setRegistryName("minestuck_bucket"));
		obsidianBucket = GameRegistry.register(new ItemObsidianBucket().setRegistryName("bucket_obsidian"));
		modusCard = (ItemModus) GameRegistry.register(new ItemModus().setRegistryName("modus_card"));
		goldSeeds = (ItemGoldSeeds) GameRegistry.register(new ItemGoldSeeds().setRegistryName("gold_seeds"));
		metalBoat = (ItemMetalBoat) GameRegistry.register(new ItemMetalBoat().setRegistryName("metal_boat"));
		candy = GameRegistry.register(new ItemMinestuckCandy().setRegistryName("candy"));
		
		minestuckBucket.addBlock(blockOil.getDefaultState());
		minestuckBucket.addBlock(blockBlood.getDefaultState());
		minestuckBucket.addBlock(blockBrainJuice.getDefaultState());
		
		registerItemBlock(new ItemChessTile(chessTile));
		registerItemBlock(new ItemBlock(skaiaPortal));
		
		registerItemBlock(new ItemCruxiteOre(oreCruxite));
		registerItemBlock(new ItemBlock(coalOreNetherrack));
		registerItemBlock(new ItemBlock(ironOreSandstone));
		registerItemBlock(new ItemBlock(ironOreSandstoneRed));
		registerItemBlock(new ItemBlock(goldOreSandstone));
		registerItemBlock(new ItemBlock(goldOreSandstoneRed));
		
		registerItemBlock(new ItemStorageBlock(blockStorage));
		registerItemBlock(new ItemMachine(blockMachine));
		registerItemBlock(new ItemBlock(blockComputerOff));
		registerItemBlock(new ItemBlock(transportalizer));
		
		registerItemBlock(new ItemBlockLayered(layeredSand));
		registerItemBlock(new ItemColoredDirt(coloredDirt));
		registerItemBlock(new ItemBlock(glowingMushroom));
		registerItemBlock(new ItemBlock(glowingLog));
		
		registerItemBlock(new ItemBlock(primedTnt));
		registerItemBlock(new ItemBlock(unstableTnt));
		registerItemBlock(new ItemBlock(instantTnt));
		registerItemBlock(new ItemBlock(woodenExplosiveButton));
		registerItemBlock(new ItemBlock(stoneExplosiveButton));
	}
	
	private static void registerItemBlock(ItemBlock item)
	{
		GameRegistry.register(item.setRegistryName(item.block.getRegistryName()));	//Is this enough?
	}
}