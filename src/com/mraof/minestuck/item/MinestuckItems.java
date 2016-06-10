package com.mraof.minestuck.item;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.item.weapon.EnumBladeType;
import com.mraof.minestuck.item.weapon.EnumCaneType;
import com.mraof.minestuck.item.weapon.EnumClubType;
import com.mraof.minestuck.item.weapon.EnumDiceType;
import com.mraof.minestuck.item.weapon.EnumHammerType;
import com.mraof.minestuck.item.weapon.EnumSickleType;
import com.mraof.minestuck.item.weapon.EnumSporkType;
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
	public static Item caledscratch;
	public static Item caledfwlch;
	public static Item royalDeringer;
	public static Item regisword;
	public static Item scarletRibbitar;
	public static Item doggMachete;
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
		//items
				//hammers
				clawHammer = new ItemHammer(EnumHammerType.CLAW);
				sledgeHammer = new ItemHammer(EnumHammerType.SLEDGE);
				blacksmithHammer = new ItemHammer(EnumHammerType.BLACKSMITH);
				pogoHammer = new ItemHammer(EnumHammerType.POGO);
				telescopicSassacrusher = new ItemHammer(EnumHammerType.TELESCOPIC);
				fearNoAnvil = new ItemHammer(EnumHammerType.FEARNOANVIL);
				zillyhooHammer = new ItemHammer(EnumHammerType.ZILLYHOO);
				popamaticVrillyhoo = new ItemHammer(EnumHammerType.POPAMATIC);
				scarletZillyhoo = new ItemHammer(EnumHammerType.SCARLET);
				//blades
				sord = new ItemBlade(EnumBladeType.SORD);
				cactusCutlass = new ItemBlade(EnumBladeType.CACTUS);
				ninjaSword = new ItemBlade(EnumBladeType.NINJA);
				katana = new ItemBlade(EnumBladeType.KATANA);
				caledscratch = new ItemBlade(EnumBladeType.CALEDSCRATCH);
				caledfwlch = new ItemBlade(EnumBladeType.CALEDFWLCH);
				royalDeringer = new ItemBlade(EnumBladeType.DERINGER);
				regisword = new ItemBlade(EnumBladeType.REGISWORD);
				scarletRibbitar = new ItemBlade(EnumBladeType.SCARLET);
				doggMachete = new ItemBlade(EnumBladeType.DOGG);
				//Dice
				dice = new ItemDice(EnumDiceType.DICE);
				fluoriteOctet = new ItemDice(EnumDiceType.FLUORITE_OCTET);
				
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
				fork = new ItemSpork(EnumSporkType.FORK);
				spork = new ItemSpork(EnumSporkType.SPORK);
				
				toolEmerald = EnumHelper.addToolMaterial("EMERALD", 3, 1220, 12.0F, 4.0F, 12).setRepairItem(new ItemStack(Items.emerald));
				emeraldSword = new ItemSword(toolEmerald).setUnlocalizedName("swordEmerald").setCreativeTab(Minestuck.tabMinestuck);
				emeraldAxe = new ItemMinestuckAxe(toolEmerald).setUnlocalizedName("hatchetEmerald").setCreativeTab(Minestuck.tabMinestuck);
				emeraldPickaxe = new ItemMinestuckPickaxe(toolEmerald).setUnlocalizedName("pickaxeEmerald").setCreativeTab(Minestuck.tabMinestuck);
				emeraldShovel = new ItemSpade(toolEmerald).setUnlocalizedName("shovelEmerald").setCreativeTab(Minestuck.tabMinestuck);
				emeraldHoe = new ItemHoe(toolEmerald).setUnlocalizedName("hoeEmerald").setCreativeTab(Minestuck.tabMinestuck);
				//armor
				armorPrismarine = EnumHelper.addArmorMaterial("PRISMARINE", "minestuck:prismarine", 20, new int[]{3, 7, 6, 2}, 15);
				armorPrismarine.customCraftingMaterial = Items.prismarine_shard;
				prismarineHelmet = new ItemArmor(armorPrismarine, 0, 0).setUnlocalizedName("helmetPrismarine").setCreativeTab(Minestuck.tabMinestuck);
				prismarineChestplate = new ItemArmor(armorPrismarine, 0, 1).setUnlocalizedName("chestplatePrismarine").setCreativeTab(Minestuck.tabMinestuck);
				prismarineLeggings = new ItemArmor(armorPrismarine, 0, 2).setUnlocalizedName("leggingsPrismarine").setCreativeTab(Minestuck.tabMinestuck);
				prismarineBoots = new ItemArmor(armorPrismarine, 0, 3).setUnlocalizedName("bootsPrismarine").setCreativeTab(Minestuck.tabMinestuck);
				//misc
				rawCruxite = new ItemCruxiteRaw();
				cruxiteDowel = new ItemDowel();
				captchaCard = new ItemCaptchaCard();
				cruxiteApple = new ItemCruxiteApple();
				cruxitePotion = new ItemCruxitePotion();
				disk = new ItemDisk();
				component = new ItemComponent();
				minestuckBucket = new ItemMinestuckBucket();
				obsidianBucket = new ItemObsidianBucket();
				modusCard = new ItemModus();
				goldSeeds = new ItemGoldSeeds();
				metalBoat = new ItemMetalBoat();
				candy = new ItemMinestuckCandy();
				
				minestuckBucket.addBlock(MinestuckBlocks.blockOil);
				minestuckBucket.addBlock(MinestuckBlocks.blockBlood);
				minestuckBucket.addBlock(MinestuckBlocks.blockBrainJuice);
				
				GameRegistry.registerItem(clawHammer, "claw_hammer");
				GameRegistry.registerItem(sledgeHammer, "sledge_hammer");
				GameRegistry.registerItem(blacksmithHammer, "blacksmith_hammer");
				GameRegistry.registerItem(pogoHammer, "pogo_hammer");
				GameRegistry.registerItem(telescopicSassacrusher, "telescopic_sassacrusher");
				GameRegistry.registerItem(fearNoAnvil, "fear_no_anvil");
				GameRegistry.registerItem(zillyhooHammer, "zillyhoo_hammer");
				GameRegistry.registerItem(popamaticVrillyhoo, "popamatic_vrillyhoo");
				GameRegistry.registerItem(scarletZillyhoo, "scarlet_zillyhoo");
				
				GameRegistry.registerItem(sord, "sord");
				GameRegistry.registerItem(cactusCutlass, "cactaceae_cutlass");
				GameRegistry.registerItem(ninjaSword, "ninja_sword");
				GameRegistry.registerItem(katana, "katana");
				GameRegistry.registerItem(caledscratch, "caledscratch");
				GameRegistry.registerItem(caledfwlch, "caledfwlch");
				GameRegistry.registerItem(zillywairCutlass, "cutlass_of_zillywair");
				GameRegistry.registerItem(royalDeringer, "royal_deringer");
				GameRegistry.registerItem(regisword, "regisword");
				GameRegistry.registerItem(scarletRibbitar, "scarlet_ribbitar");
				GameRegistry.registerItem(doggMachete, "dogg_machete");
				
				GameRegistry.registerItem(dice,"dice");
				GameRegistry.registerItem(fluoriteOctet, "fluorite_octet");
				
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
				GameRegistry.registerItem(fork, "fork");
				GameRegistry.registerItem(spork,"spork");
				
				GameRegistry.registerItem(emeraldSword, "emerald_sword");
				GameRegistry.registerItem(emeraldAxe, "emerald_axe");
				GameRegistry.registerItem(emeraldPickaxe, "emerald_pickaxe");
				GameRegistry.registerItem(emeraldShovel, "emerald_shovel");
				GameRegistry.registerItem(emeraldHoe, "emerald_hoe");
				
				GameRegistry.registerItem(prismarineHelmet, "prismarine_helmet");
				GameRegistry.registerItem(prismarineChestplate, "prismarine_chestplate");
				GameRegistry.registerItem(prismarineLeggings, "prismarine_leggings");
				GameRegistry.registerItem(prismarineBoots, "prismarine_boots");
				
				GameRegistry.registerItem(rawCruxite, "cruxite_raw");
				GameRegistry.registerItem(cruxiteDowel, "cruxite_dowel");
				GameRegistry.registerItem(captchaCard, "captcha_card");
				GameRegistry.registerItem(cruxiteApple, "cruxite_artifact");	//TODO change to "cruxite_apple" when there's no risk of messing with existing save files
				GameRegistry.registerItem(cruxitePotion, "cruxite_potion");
				GameRegistry.registerItem(disk, "computer_disk");
				GameRegistry.registerItem(component, "component");
				GameRegistry.registerItem(minestuckBucket, "minestuck_bucket");
				GameRegistry.registerItem(obsidianBucket, "bucket_obsidian");
				GameRegistry.registerItem(modusCard, "modus_card");
				GameRegistry.registerItem(goldSeeds, "gold_seeds");
				GameRegistry.registerItem(metalBoat, "metal_boat");
				GameRegistry.registerItem(candy, "candy");
	}
}