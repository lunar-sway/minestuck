package com.mraof.minestuck.util;

import com.mraof.minestuck.block.BlockMinestuckStone;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.modSupport.*;
import com.mraof.minestuck.modSupport.minetweaker.MinetweakerSupport;
import com.mraof.minestuck.world.storage.loot.conditions.LandAspectLootCondition;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPrismarine;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.conditions.LootConditionManager;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.Map.Entry;

import static com.mraof.minestuck.block.MinestuckBlocks.*;
import static com.mraof.minestuck.item.MinestuckItems.*;
import static com.mraof.minestuck.util.CombinationRegistry.MODE_AND;
import static com.mraof.minestuck.util.CombinationRegistry.MODE_OR;
import static net.minecraftforge.oredict.OreDictionary.WILDCARD_VALUE;

public class AlchemyRecipeHandler
{
	public static final ResourceLocation BASIC_MEDIUM_CHEST = new ResourceLocation("minestuck", "chests/medium_basic");
	public static final ResourceLocation CONSORT_JUNK_REWARD = new ResourceLocation("minestuck", "gameplay/consort_junk");
	
	private static HashMap<List<Object>, Object> recipeList;
	private static HashMap<List<Object>, Boolean> lookedOver;
	private static int returned = 0;
	private static IRecipe cardRecipe;
	private volatile static boolean cardRecipeAdded;

	public static void registerVanillaRecipes() {
		
		//Set up Alchemiter recipes
		//Blocks
		GristRegistry.addGristConversion(new ItemStack(Blocks.COBBLESTONE), false, new GristSet(new GristType[] {GristType.Build}, new int[] {2}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.DEADBUSH), false, new GristSet(new GristType[] {GristType.Amber, GristType.Sulfur}, new int[] {2, 1}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.DIRT, 1, 0), true, new GristSet(new GristType[] {GristType.Build}, new int[] {2}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.DIRT, 1, 2), true, new GristSet(new GristType[] {GristType.Build}, new int[] {2}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.DRAGON_EGG), false, new GristSet(new GristType[] {GristType.Uranium, GristType.Tar, GristType.Zillium}, new int[] {800, 800, 10}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.END_STONE), false, new GristSet(new GristType[] {GristType.Caulk, GristType.Build}, new int[] {3, 4}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.GLASS), false, new GristSet(new GristType[] {GristType.Build}, new int[] {1}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.GRASS), false, new GristSet(new GristType[] {GristType.Build}, new int[] {2}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.GRAVEL), false, new GristSet(new GristType[] {GristType.Build}, new int[] {3}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.HARDENED_CLAY), false, new GristSet(new GristType[] {GristType.Shale, GristType.Marble}, new int[] {12, 4}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.ICE), false, new GristSet(new GristType[] {GristType.Cobalt}, new int[] {2}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.LEAVES), false, new GristSet(new GristType[] {GristType.Build}, new int[] {1}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.LOG), false, new GristSet(new GristType[] {GristType.Build}, new int[] {8}));		
		GristRegistry.addGristConversion(new ItemStack(Blocks.LOG2), false, new GristSet(new GristType[] {GristType.Build}, new int[] {8}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.MELON_BLOCK), false, new GristSet(new GristType[] {GristType.Amber, GristType.Chalk, GristType.Build}, new int[] {8, 8, 2}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.MYCELIUM), false, new GristSet(new GristType[] {GristType.Iodine, GristType.Ruby, GristType.Build}, new int[] {2, 2, 2}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.NETHERRACK), false, new GristSet(new GristType[] {GristType.Build, GristType.Tar}, new int[] {2, 1}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.OBSIDIAN), false, new GristSet(new GristType[] {GristType.Cobalt, GristType.Tar, GristType.Build}, new int[] {8, 16, 6}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.PUMPKIN), false, new GristSet(new GristType[] {GristType.Amber, GristType.Caulk}, new int[] {12, 6}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.SAND), false, new GristSet(new GristType[] {GristType.Build}, new int[] {1}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.SAPLING), false, new GristSet(new GristType[] {GristType.Build}, new int[] {16}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.SNOW), false, new GristSet(new GristType[] {GristType.Cobalt, GristType.Build}, new int[] {5, 3}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.SNOW_LAYER), false, new GristSet(new GristType[] {GristType.Cobalt}, new int[] {2}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.SOUL_SAND), false, new GristSet(new GristType[] {GristType.Sulfur, GristType.Caulk, GristType.Build}, new int[] {5, 3, 2}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.SPONGE, 1, 0), new GristSet(new GristType[] {GristType.Amber, GristType.Sulfur}, new int[] {20, 30}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.SPONGE, 1, 1), new GristSet(new GristType[] {GristType.Amber, GristType.Sulfur, GristType.Cobalt}, new int[] {20, 30, 10}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.STAINED_HARDENED_CLAY), false, new GristSet(new GristType[] {GristType.Shale, GristType.Marble}, new int[] {12, 4}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.STONE), false, new GristSet(new GristType[] {GristType.Build}, new int[] {2}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.STONE_SLAB, 1, 5), true, new GristSet(new GristType[] {GristType.Build}, new int[] {1}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.STONEBRICK, 1, 2), true, new GristSet(new GristType[] {GristType.Build}, new int[] {2}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.STONE_BRICK_STAIRS), new GristSet(new GristType[] {GristType.Build}, new int[] {3}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.SANDSTONE_STAIRS), new GristSet(new GristType[] {GristType.Build}, new int[] {6}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.WEB), false, new GristSet(new GristType[] {GristType.Chalk}, new int[] {18}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.WOOL, 1, 0), true, new GristSet(new GristType[] {GristType.Chalk}, new int[] {6}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.WOOL, 1, 1), true, new GristSet(new GristType[] {GristType.Chalk, GristType.Garnet, GristType.Amber}, new int[] {6, 1, 1}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.WOOL, 1, 10), true, new GristSet(new GristType[] {GristType.Chalk, GristType.Amethyst, GristType.Garnet}, new int[] {6, 1, 1}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.WOOL, 1, 11), true, new GristSet(new GristType[] {GristType.Chalk, GristType.Amethyst}, new int[] {6, 2}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.WOOL, 1, 12), true, new GristSet(new GristType[] {GristType.Chalk, GristType.Iodine}, new int[] {6, 2}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.WOOL, 1, 13), true, new GristSet(new GristType[] {GristType.Chalk, GristType.Amber}, new int[] {6, 2}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.WOOL, 1, 14), true, new GristSet(new GristType[] {GristType.Chalk, GristType.Garnet}, new int[] {6, 2}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.WOOL, 1, 15), true, new GristSet(new GristType[] {GristType.Chalk, GristType.Tar}, new int[] {6, 2}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.WOOL, 1, 2), true, new GristSet(new GristType[] {GristType.Chalk, GristType.Amethyst, GristType.Garnet}, new int[] {6, 1, 2}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.WOOL, 1, 3), true, new GristSet(new GristType[] {GristType.Chalk, GristType.Amethyst}, new int[] {8, 1}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.WOOL, 1, 4), true, new GristSet(new GristType[] {GristType.Chalk, GristType.Amber}, new int[] {6, 2}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.WOOL, 1, 5), true, new GristSet(new GristType[] {GristType.Chalk, GristType.Amber}, new int[] {6, 2}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.WOOL, 1, 6), true, new GristSet(new GristType[] {GristType.Chalk, GristType.Garnet}, new int[] {6, 1}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.WOOL, 1, 7), true, new GristSet(new GristType[] {GristType.Chalk, GristType.Tar}, new int[] {6, 1}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.WOOL, 1, 8), true, new GristSet(new GristType[] {GristType.Chalk, GristType.Tar}, new int[] {6, 1}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.WOOL, 1, 9), true, new GristSet(new GristType[] {GristType.Chalk, GristType.Amethyst, GristType.Amber}, new int[] {6, 1, 1}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.PACKED_ICE), false, new GristSet(new GristType[] {GristType.Build, GristType.Cobalt}, new int[] {10, 6}));
		GristRegistry.addGristConversion(Blocks.TORCH, new GristSet(GristType.Build, 2));
		GristRegistry.addGristConversion(Blocks.PRISMARINE, BlockPrismarine.ROUGH_META, new GristSet(new GristType[] {GristType.Cobalt, GristType.Build}, new int[] {7, 12}));
		GristRegistry.addGristConversion(Blocks.PRISMARINE, BlockPrismarine.BRICKS_META, new GristSet(new GristType[] {GristType.Cobalt, GristType.Build}, new int[] {12, 18}));
		GristRegistry.addGristConversion(Blocks.PRISMARINE, BlockPrismarine.DARK_META, new GristSet(new GristType[] {GristType.Cobalt, GristType.Tar, GristType.Build}, new int[] {10, 2, 18}));
		GristRegistry.addGristConversion(Blocks.SEA_LANTERN, new GristSet(new GristType[] {GristType.Cobalt, GristType.Diamond, GristType.Amethyst}, new int[] {32, 6, 12}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.SANDSTONE), false, new GristSet(GristType.Build, 4));
		GristRegistry.addGristConversion(new ItemStack(Blocks.PLANKS), false, new GristSet(GristType.Build, 2));
		GristRegistry.addGristConversion(new ItemStack(Blocks.CHORUS_FLOWER), new GristSet(new GristType[] {GristType.Build, GristType.Amethyst, GristType.Shale}, new int[] {26, 23, 10}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.PURPUR_PILLAR), new GristSet(new GristType[] {GristType.Amethyst, GristType.Shale}, new int[] {2, 4}));
		
		//Items
		GristRegistry.addGristConversion(new ItemStack(Items.BLAZE_ROD), false, new GristSet(new GristType[] {GristType.Tar, GristType.Uranium}, new int[] {20, 2}));
		GristRegistry.addGristConversion(new ItemStack(Items.BONE), false, new GristSet(new GristType[] {GristType.Chalk}, new int[] {6}));
		GristRegistry.addGristConversion(new ItemStack(Items.BRICK), false, new GristSet(new GristType[] {GristType.Shale, GristType.Tar}, new int[] {3, 1}));
		GristRegistry.addGristConversion(new ItemStack(Items.CHAINMAIL_BOOTS), false, new GristSet(new GristType[] {GristType.Rust, GristType.Mercury}, new int[] {16, 8}));
		GristRegistry.addGristConversion(new ItemStack(Items.CHAINMAIL_CHESTPLATE), false, new GristSet(new GristType[] {GristType.Rust, GristType.Mercury}, new int[] {32, 16}));
		GristRegistry.addGristConversion(new ItemStack(Items.CHAINMAIL_HELMET), false, new GristSet(new GristType[] {GristType.Rust, GristType.Mercury}, new int[] {20, 10}));
		GristRegistry.addGristConversion(new ItemStack(Items.CHAINMAIL_LEGGINGS), false, new GristSet(new GristType[] {GristType.Rust, GristType.Mercury}, new int[] {28, 14}));
		GristRegistry.addGristConversion(new ItemStack(Items.CLAY_BALL), false, new GristSet(new GristType[] {GristType.Shale}, new int[] {3}));
		GristRegistry.addGristConversion(new ItemStack(Items.DIAMOND_HORSE_ARMOR), false, new GristSet(new GristType[] {GristType.Diamond}, new int[] {80}));
		GristRegistry.addGristConversion(new ItemStack(Items.DYE, 1, 0), true, new GristSet(new GristType[] {GristType.Tar}, new int[] {4}));
		GristRegistry.addGristConversion(new ItemStack(Items.DYE, 1, 1), true, new GristSet(new GristType[] {GristType.Garnet}, new int[] {4}));
		GristRegistry.addGristConversion(new ItemStack(Items.DYE, 1, 10), true, new GristSet(new GristType[] {GristType.Amber}, new int[] {3}));
		GristRegistry.addGristConversion(new ItemStack(Items.DYE, 1, 11), true, new GristSet(new GristType[] {GristType.Amber}, new int[] {4}));
		GristRegistry.addGristConversion(new ItemStack(Items.DYE, 1, 12), true, new GristSet(new GristType[] {GristType.Amethyst, GristType.Chalk}, new int[] {2, 2}));
		GristRegistry.addGristConversion(new ItemStack(Items.DYE, 1, 13), true, new GristSet(new GristType[] {GristType.Amethyst, GristType.Garnet}, new int[] {1, 3}));
		GristRegistry.addGristConversion(new ItemStack(Items.DYE, 1, 14), true, new GristSet(new GristType[] {GristType.Garnet, GristType.Amber}, new int[] {2, 2}));
		GristRegistry.addGristConversion(new ItemStack(Items.DYE, 1, 2), true, new GristSet(new GristType[] {GristType.Amber, GristType.Iodine}, new int[] {3, 1}));
		GristRegistry.addGristConversion(new ItemStack(Items.DYE, 1, 3), true, new GristSet(new GristType[] {GristType.Iodine, GristType.Amber}, new int[] {3, 1}));
		GristRegistry.addGristConversion(new ItemStack(Items.DYE, 1, 4), true, new GristSet(new GristType[] {GristType.Amethyst}, new int[] {4}));
		GristRegistry.addGristConversion(new ItemStack(Items.DYE, 1, 5), true, new GristSet(new GristType[] {GristType.Amethyst, GristType.Garnet}, new int[] {2, 2}));
		GristRegistry.addGristConversion(new ItemStack(Items.DYE, 1, 6), true, new GristSet(new GristType[] {GristType.Amethyst, GristType.Amber, GristType.Iodine}, new int[] {2, 2, 1}));
		GristRegistry.addGristConversion(new ItemStack(Items.DYE, 1, 7), true, new GristSet(new GristType[] {GristType.Tar, GristType.Chalk}, new int[] {1, 3}));
		GristRegistry.addGristConversion(new ItemStack(Items.DYE, 1, 8), true, new GristSet(new GristType[] {GristType.Tar, GristType.Chalk}, new int[] {3, 1}));
		GristRegistry.addGristConversion(new ItemStack(Items.DYE, 1, 9), true, new GristSet(new GristType[] {GristType.Garnet, GristType.Chalk}, new int[] {2, 2}));
		GristRegistry.addGristConversion(new ItemStack(Items.ENCHANTED_BOOK), false, new GristSet(new GristType[] {GristType.Uranium, GristType.Quartz, GristType.Diamond, GristType.Ruby, GristType.Chalk, GristType.Iodine}, new int[] {8, 1, 4, 4, 16, 2}));
		GristRegistry.addGristConversion(new ItemStack(Items.ENDER_PEARL), false, new GristSet(new GristType[] {GristType.Uranium, GristType.Diamond, GristType.Mercury}, new int[] {13, 5, 8}));
		GristRegistry.addGristConversion(new ItemStack(Items.EXPERIENCE_BOTTLE), false, new GristSet(new GristType[] {GristType.Uranium, GristType.Quartz, GristType.Diamond, GristType.Ruby}, new int[] {16, 3, 4, 6}));
		GristRegistry.addGristConversion(new ItemStack(Items.FEATHER), false, new GristSet(new GristType[] {GristType.Chalk}, new int[] {4}));
		GristRegistry.addGristConversion(new ItemStack(Items.FIREWORK_CHARGE), false, new GristSet(new GristType[] {GristType.Sulfur, GristType.Chalk}, new int[] {4, 2}));
		GristRegistry.addGristConversion(new ItemStack(Items.FIREWORKS), false, new GristSet(new GristType[] {GristType.Sulfur, GristType.Chalk}, new int[] {4, 5}));
		GristRegistry.addGristConversion(new ItemStack(Items.FLINT), false, new GristSet(new GristType[] {GristType.Build}, new int[] {4}));
		GristRegistry.addGristConversion(new ItemStack(Items.GHAST_TEAR), false, new GristSet(new GristType[] {GristType.Cobalt, GristType.Chalk}, new int[] {10, 19}));
		GristRegistry.addGristConversion(new ItemStack(Items.GLOWSTONE_DUST), false, new GristSet(new GristType[] {GristType.Tar, GristType.Chalk}, new int[] {4, 6}));
		GristRegistry.addGristConversion(new ItemStack(Items.GOLDEN_HORSE_ARMOR), false, new GristSet(new GristType[] {GristType.Gold}, new int[] {40}));
		GristRegistry.addGristConversion(new ItemStack(Items.GUNPOWDER), false, new GristSet(new GristType[] {GristType.Sulfur, GristType.Chalk}, new int[] {3, 2}));
		GristRegistry.addGristConversion(new ItemStack(Items.IRON_HORSE_ARMOR), false, new GristSet(new GristType[] {GristType.Rust}, new int[] {40}));
		GristRegistry.addGristConversion(new ItemStack(Items.LAVA_BUCKET), false, new GristSet(new GristType[] {GristType.Rust, GristType.Tar}, new int[] {27, 16}));
		GristRegistry.addGristConversion(new ItemStack(Items.LEATHER), false, new GristSet(new GristType[] {GristType.Iodine, GristType.Chalk}, new int[] {3, 3}));
		GristRegistry.addGristConversion(new ItemStack(Items.MAP), false, new GristSet(new GristType[] {GristType.Rust, GristType.Chalk, GristType.Garnet}, new int[] {32, 10, 2}));
		GristRegistry.addGristConversion(new ItemStack(Items.MILK_BUCKET), false, new GristSet(new GristType[] {GristType.Rust, GristType.Chalk}, new int[] {27, 8}));
		GristRegistry.addGristConversion(new ItemStack(Items.NAME_TAG), false, new GristSet(new GristType[] {GristType.Build, GristType.Caulk, GristType.Amber, GristType.Chalk}, new int[] {4, 10, 12, 8}));
		GristRegistry.addGristConversion(new ItemStack(Items.NETHER_STAR), false, new GristSet(new GristType[] {GristType.Uranium, GristType.Tar, GristType.Diamond}, new int[] {344, 135, 92}));
		GristRegistry.addGristConversion(new ItemStack(Items.NETHER_WART), false, new GristSet(new GristType[] {GristType.Iodine, GristType.Tar}, new int[] {3, 5}));
		GristRegistry.addGristConversion(new ItemStack(Items.NETHERBRICK), false, new GristSet(new GristType[] {GristType.Build, GristType.Tar}, new int[] {1, 2}));
		GristRegistry.addGristConversion(new ItemStack(Items.RECORD_11), false, new GristSet(new GristType[] {GristType.Build, GristType.Caulk, GristType.Tar, GristType.Mercury}, new int[] {10, 5, 2, 2}));
		GristRegistry.addGristConversion(new ItemStack(Items.RECORD_13), false, new GristSet(new GristType[] {GristType.Build, GristType.Caulk, GristType.Amber, GristType.Chalk}, new int[] {15, 8, 5, 5}));
		GristRegistry.addGristConversion(new ItemStack(Items.RECORD_BLOCKS), false, new GristSet(new GristType[] {GristType.Build, GristType.Caulk, GristType.Ruby, GristType.Rust}, new int[] {15, 8, 5, 5}));
		GristRegistry.addGristConversion(new ItemStack(Items.RECORD_CAT), false, new GristSet(new GristType[] {GristType.Build, GristType.Caulk, GristType.Uranium, GristType.Shale}, new int[] {15, 8, 2, 5}));
		GristRegistry.addGristConversion(new ItemStack(Items.RECORD_CHIRP), false, new GristSet(new GristType[] {GristType.Build, GristType.Caulk, GristType.Ruby, GristType.Garnet}, new int[] {15, 8, 5, 5}));
		GristRegistry.addGristConversion(new ItemStack(Items.RECORD_FAR), false, new GristSet(new GristType[] {GristType.Build, GristType.Caulk, GristType.Uranium, GristType.Sulfur}, new int[] {15, 8, 2, 5}));
		GristRegistry.addGristConversion(new ItemStack(Items.RECORD_MALL), false, new GristSet(new GristType[] {GristType.Build, GristType.Caulk, GristType.Amethyst, GristType.Tar}, new int[] {15, 8, 5, 5}));
		GristRegistry.addGristConversion(new ItemStack(Items.RECORD_MELLOHI), false, new GristSet(new GristType[] {GristType.Build, GristType.Caulk, GristType.Marble, GristType.Shale}, new int[] {15, 8, 5, 5}));
		GristRegistry.addGristConversion(new ItemStack(Items.RECORD_STAL), false, new GristSet(new GristType[] {GristType.Build, GristType.Caulk, GristType.Tar, GristType.Mercury}, new int[] {15, 8, 5, 5}));
		GristRegistry.addGristConversion(new ItemStack(Items.RECORD_STRAD), false, new GristSet(new GristType[] {GristType.Build, GristType.Caulk, GristType.Chalk, GristType.Quartz}, new int[] {15, 8, 5, 5}));
		GristRegistry.addGristConversion(new ItemStack(Items.RECORD_WAIT), false, new GristSet(new GristType[] {GristType.Build, GristType.Caulk, GristType.Cobalt, GristType.Quartz}, new int[] {15, 8, 5, 5}));
		GristRegistry.addGristConversion(new ItemStack(Items.RECORD_WARD), false, new GristSet(new GristType[] {GristType.Build, GristType.Caulk, GristType.Iodine, GristType.Gold}, new int[] {15, 8, 5, 2}));
		GristRegistry.addGristConversion(new ItemStack(Items.REDSTONE), false, new GristSet(new GristType[] {GristType.Garnet}, new int[] {4}));
		GristRegistry.addGristConversion(new ItemStack(Items.REEDS), false, new GristSet(new GristType[] {GristType.Amber, GristType.Iodine}, new int[] {3, 2}));
		GristRegistry.addGristConversion(new ItemStack(Items.ROTTEN_FLESH), false, new GristSet(new GristType[] {GristType.Rust, GristType.Iodine}, new int[] {1, 1}));
		GristRegistry.addGristConversion(new ItemStack(Items.SADDLE), false, new GristSet(new GristType[] {GristType.Rust, GristType.Iodine, GristType.Chalk}, new int[] {16, 7, 14}));
		GristRegistry.addGristConversion(new ItemStack(Items.SKULL, 1, 0), true, new GristSet(new GristType[] {GristType.Chalk}, new int[] {28}));
		GristRegistry.addGristConversion(new ItemStack(Items.SKULL, 1, 1), true, new GristSet(new GristType[] {GristType.Uranium, GristType.Tar, GristType.Diamond}, new int[] {35, 48, 16}));
		GristRegistry.addGristConversion(new ItemStack(Items.SKULL, 1, 2), true, new GristSet(new GristType[] {GristType.Rust, GristType.Iodine}, new int[] {5, 20}));
		GristRegistry.addGristConversion(new ItemStack(Items.SKULL, 1, 3), true, new GristSet(new GristType[] {GristType.Artifact}, new int[] {10}));
		GristRegistry.addGristConversion(new ItemStack(Items.SKULL, 1, 4), true, new GristSet(new GristType[] {GristType.Sulfur, GristType.Chalk}, new int[] {10, 18}));
		GristRegistry.addGristConversion(new ItemStack(Items.SKULL, 1, 5), true, new GristSet(new GristType[] {GristType.Uranium, GristType.Tar, GristType.Zillium}, new int[] {25, 70, 1}));
		GristRegistry.addGristConversion(new ItemStack(Items.SLIME_BALL), false, new GristSet(new GristType[] {GristType.Caulk}, new int[] {8}));
		GristRegistry.addGristConversion(new ItemStack(Items.SNOWBALL), false, new GristSet(new GristType[] {GristType.Cobalt}, new int[] {1}));
		GristRegistry.addGristConversion(new ItemStack(Items.SPIDER_EYE), false, new GristSet(new GristType[] {GristType.Amber, GristType.Iodine}, new int[] {6, 2}));
		GristRegistry.addGristConversion(new ItemStack(Items.STRING), false, new GristSet(new GristType[] {GristType.Chalk}, new int[] {2}));
		GristRegistry.addGristConversion(new ItemStack(Items.WATER_BUCKET), false, new GristSet(new GristType[] {GristType.Rust, GristType.Cobalt}, new int[] {27, 4}));
		GristRegistry.addGristConversion(new ItemStack(Items.WHEAT), false, new GristSet(new GristType[] {GristType.Iodine}, new int[] {2}));
		GristRegistry.addGristConversion(new ItemStack(Items.WHEAT_SEEDS), false, new GristSet(new GristType[] {GristType.Amber, GristType.Iodine}, new int[] {1, 1}));
		GristRegistry.addGristConversion(new ItemStack(Items.WRITABLE_BOOK), false, new GristSet(new GristType[] {GristType.Chalk, GristType.Iodine}, new int[] {16, 2}));
		GristRegistry.addGristConversion(new ItemStack(Items.PRISMARINE_CRYSTALS), new GristSet(new GristType[] {GristType.Cobalt, GristType.Diamond, GristType.Amethyst}, new int[] {5, 2, 4}));
		GristRegistry.addGristConversion(new ItemStack(Items.PRISMARINE_SHARD), new GristSet(new GristType[] {GristType.Build, GristType.Cobalt}, new int[] {3, 3}));
		GristRegistry.addGristConversion(new ItemStack(Items.RABBIT_HIDE), new GristSet(new GristType[] {GristType.Iodine, GristType.Chalk}, new int[] {1, 1}));
		GristRegistry.addGristConversion(new ItemStack(Items.RABBIT_FOOT), new GristSet(new GristType[] {GristType.Iodine, GristType.Chalk, GristType.Rust}, new int[] {10, 12, 3}));
		GristRegistry.addGristConversion(new ItemStack(Items.CHORUS_FRUIT), new GristSet(new GristType[] {GristType.Amethyst, GristType.Shale}, new int[] {2, 4}));
		GristRegistry.addGristConversion(new ItemStack(Items.CHORUS_FRUIT_POPPED), new GristSet(new GristType[] {GristType.Amethyst, GristType.Shale}, new int[] {2, 4}));
		GristRegistry.addGristConversion(new ItemStack(Items.BEETROOT), new GristSet(new GristType[] {GristType.Rust, GristType.Caulk}, new int[] {1, 2}));
		GristRegistry.addGristConversion(new ItemStack(Items.BEETROOT_SEEDS), new GristSet(new GristType[] {GristType.Rust, GristType.Iodine}, new int[] {2, 2}));
		GristRegistry.addGristConversion(new ItemStack(Items.ELYTRA), new GristSet(new GristType[] {GristType.Diamond, GristType.Sulfur, GristType.Caulk}, new int[] {51, 38, 65}));
		GristRegistry.addGristConversion(new ItemStack(Items.DRAGON_BREATH), new GristSet(new GristType[] {GristType.Ruby, GristType.Sulfur}, new int[] {4, 13}));
		
		//Ores
		GristRegistry.addGristConversion("oreCoal", new GristSet(new GristType[] {GristType.Build, GristType.Tar}, new int[] {4, 8}));
		GristRegistry.addGristConversion(new ItemStack(Items.COAL, 1, 0), true, new GristSet(new GristType[] {GristType.Tar}, new int[] {8}));
		GristRegistry.addGristConversion(new ItemStack(Items.COAL, 1, 1), true, new GristSet(new GristType[] {GristType.Tar, GristType.Amber}, new int[] {6, 2}));
		GristRegistry.addGristConversion("oreIron", new GristSet(new GristType[] {GristType.Build, GristType.Rust}, new int[] {4, 9}));
		GristRegistry.addGristConversion("ingotIron", new GristSet(new GristType[] {GristType.Rust}, new int[] {9}));
		GristRegistry.addGristConversion("oreGold", new GristSet(new GristType[] {GristType.Build, GristType.Gold}, new int[] {4, 9}));
		GristRegistry.addGristConversion("ingotGold", new GristSet(new GristType[] {GristType.Gold}, new int[] {9}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.REDSTONE_ORE), false, new GristSet(new GristType[] {GristType.Garnet, GristType.Build}, new int[] {16, 4}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.LAPIS_ORE), false, new GristSet(new GristType[] {GristType.Amethyst, GristType.Build}, new int[] {16, 4}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.DIAMOND_ORE), false, new GristSet(new GristType[] {GristType.Diamond, GristType.Build}, new int[] {18, 4}));
		GristRegistry.addGristConversion(new ItemStack(Items.DIAMOND), false, new GristSet(new GristType[] {GristType.Diamond}, new int[] {18}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.EMERALD_ORE), false, new GristSet(new GristType[] {GristType.Ruby, GristType.Diamond, GristType.Build}, new int[] {9, 9, 4}));
		GristRegistry.addGristConversion(new ItemStack(Items.EMERALD), false, new GristSet(new GristType[] {GristType.Ruby, GristType.Diamond}, new int[] {9, 9}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.QUARTZ_ORE), false, new GristSet(new GristType[] {GristType.Quartz, GristType.Marble, GristType.Build}, new int[] {8, 2, 2}));
		GristRegistry.addGristConversion(new ItemStack(Items.QUARTZ), false, new GristSet(new GristType[] {GristType.Quartz, GristType.Marble}, new int[] {4, 1}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.QUARTZ_BLOCK), false, new GristSet(new GristType[] {GristType.Quartz, GristType.Marble}, new int[] {16, 4}));
		
		//Plants
		GristRegistry.addGristConversion(new ItemStack(Blocks.LEAVES2), false, new GristSet(new GristType[] {GristType.Build}, new int[] {1}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.YELLOW_FLOWER), false, new GristSet(new GristType[] {GristType.Amber, GristType.Iodine}, new int[] {4, 1}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.RED_FLOWER, 1, 0), true, new GristSet(new GristType[] {GristType.Garnet, GristType.Iodine}, new int[] {4, 1}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.RED_FLOWER, 1, 1), true, new GristSet(new GristType[] {GristType.Amethyst, GristType.Chalk, GristType.Iodine}, new int[] {2, 2, 1}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.RED_FLOWER, 1, 2), true, new GristSet(new GristType[] {GristType.Amethyst, GristType.Garnet, GristType.Iodine}, new int[] {1, 3, 1}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.RED_FLOWER, 1, 3), true, new GristSet(new GristType[] {GristType.Tar, GristType.Chalk, GristType.Iodine}, new int[] {1, 3, 1}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.RED_FLOWER, 1, 4), true, new GristSet(new GristType[] {GristType.Tar, GristType.Chalk, GristType.Iodine}, new int[] {1, 3, 1}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.RED_FLOWER, 1, 5), true, new GristSet(new GristType[] {GristType.Garnet, GristType.Amber, GristType.Iodine}, new int[] {2, 2, 1}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.RED_FLOWER, 1, 6), true, new GristSet(new GristType[] {GristType.Amethyst, GristType.Chalk, GristType.Iodine}, new int[] {2, 2, 1}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.RED_FLOWER, 1, 7), true, new GristSet(new GristType[] {GristType.Garnet, GristType.Iodine}, new int[] {4, 1}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.RED_FLOWER, 1, 8), true, new GristSet(new GristType[] {GristType.Tar, GristType.Chalk, GristType.Iodine}, new int[] {1, 3, 1}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.BROWN_MUSHROOM), false, new GristSet(new GristType[] {GristType.Iodine}, new int[] {5}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.RED_MUSHROOM), false, new GristSet(new GristType[] {GristType.Iodine, GristType.Ruby}, new int[] {3, 2}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.CACTUS), false, new GristSet(new GristType[] {GristType.Amber, GristType.Iodine}, new int[] {4, 1}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.TALLGRASS), false, new GristSet(new GristType[] {GristType.Build}, new int[] {1}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.VINE), false, new GristSet(new GristType[] {GristType.Build, GristType.Amber}, new int[] {2, 1}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.WATERLILY), false, new GristSet(new GristType[] {GristType.Amber, GristType.Iodine}, new int[] {4, 1}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.DOUBLE_PLANT, 1, 0), true, new GristSet(new GristType[] {GristType.Amber, GristType.Iodine}, new int[] {7, 2}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.DOUBLE_PLANT, 1, 1), true, new GristSet(new GristType[] {GristType.Amethyst, GristType.Garnet, GristType.Iodine}, new int[] {2, 5, 2}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.DOUBLE_PLANT, 1, 2), true, new GristSet(new GristType[] {GristType.Amber}, new int[] {2}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.DOUBLE_PLANT, 1, 3), true, new GristSet(new GristType[] {GristType.Amber}, new int[] {2}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.DOUBLE_PLANT, 1, 4), true, new GristSet(new GristType[] {GristType.Garnet, GristType.Iodine}, new int[] {7, 2}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.DOUBLE_PLANT, 1, 5), true, new GristSet(new GristType[] {GristType.Garnet, GristType.Chalk, GristType.Iodine}, new int[] {4, 4, 2}));
		
		//Food
		GristRegistry.addGristConversion(new ItemStack(Items.APPLE), false, new GristSet(new GristType[] {GristType.Amber, GristType.Shale}, new int[] {2, 2}));
		GristRegistry.addGristConversion(new ItemStack(Items.CARROT), false, new GristSet(new GristType[] {GristType.Amber, GristType.Chalk}, new int[] {3, 1}));
		GristRegistry.addGristConversion(new ItemStack(Items.POTATO), false, new GristSet(new GristType[] {GristType.Amber}, new int[] {4}));
		GristRegistry.addGristConversion(new ItemStack(Items.POISONOUS_POTATO), false, new GristSet(new GristType[] {GristType.Amber, GristType.Iodine}, new int[] {4, 2}));
		GristRegistry.addGristConversion(new ItemStack(Items.BAKED_POTATO), false, new GristSet(new GristType[] {GristType.Amber, GristType.Tar}, new int[] {4, 1}));
		GristRegistry.addGristConversion(new ItemStack(Items.MELON), false, new GristSet(new GristType[] {GristType.Amber, GristType.Caulk}, new int[] {1, 1}));
		GristRegistry.addGristConversion(new ItemStack(Items.EGG), false, new GristSet(new GristType[] {GristType.Build, GristType.Caulk, GristType.Amber}, new int[] {2, 2, 2}));
		GristRegistry.addGristConversion(new ItemStack(Items.CAKE), false, new GristSet(new GristType[] {GristType.Build, GristType.Amber, GristType.Chalk, GristType.Iodine}, new int[] {4, 6, 52, 26}));
		GristRegistry.addGristConversion(new ItemStack(Items.BEEF), false, new GristSet(new GristType[] {GristType.Iodine}, new int[] {12}));
		GristRegistry.addGristConversion(new ItemStack(Items.PORKCHOP), false, new GristSet(new GristType[] {GristType.Iodine}, new int[] {10}));
		GristRegistry.addGristConversion(new ItemStack(Items.CHICKEN), false, new GristSet(new GristType[] {GristType.Iodine}, new int[] {10}));
		GristRegistry.addGristConversion(new ItemStack(Items.FISH, 1, 0), true, new GristSet(new GristType[] {GristType.Caulk, GristType.Amber, GristType.Cobalt}, new int[] {4, 4, 2}));
		GristRegistry.addGristConversion(new ItemStack(Items.MUTTON), new GristSet(GristType.Iodine, 10));
		GristRegistry.addGristConversion(new ItemStack(Items.RABBIT), new GristSet(GristType.Iodine, 8));
		GristRegistry.addGristConversion(new ItemStack(Items.COOKED_BEEF), false, new GristSet(new GristType[] {GristType.Iodine, GristType.Tar}, new int[] {12, 1}));
		GristRegistry.addGristConversion(new ItemStack(Items.COOKED_PORKCHOP), false, new GristSet(new GristType[] {GristType.Iodine, GristType.Tar}, new int[] {10, 4}));
		GristRegistry.addGristConversion(new ItemStack(Items.COOKED_CHICKEN), false, new GristSet(new GristType[] {GristType.Iodine, GristType.Tar}, new int[] {10, 1}));
		GristRegistry.addGristConversion(new ItemStack(Items.COOKED_FISH, 1, 0), true, new GristSet(new GristType[] {GristType.Caulk, GristType.Amber, GristType.Cobalt, GristType.Tar}, new int[] {4, 4, 2, 1}));
		GristRegistry.addGristConversion(new ItemStack(Items.COOKED_MUTTON), new GristSet(new GristType[] {GristType.Iodine, GristType.Tar}, new int[] {10, 1}));
		GristRegistry.addGristConversion(new ItemStack(Items.COOKED_RABBIT), new GristSet(new GristType[] {GristType.Iodine, GristType.Tar}, new int[] {8, 1}));
		GristRegistry.addGristConversion(new ItemStack(Items.GOLDEN_APPLE, 1, 1), new GristSet(new GristType[] {GristType.Amber, GristType.Gold, GristType.Uranium}, new int[] {4, 150, 10}));
		
		//Potions
		/*GristRegistry.addGristConversion(new ItemStack(Items.POTIONITEM, 1, 0), true, new GristSet(new GristType[] {GristType.Build, GristType.Cobalt}, new int[] {1, 4}));	//water
		GristRegistry.addGristConversion(new ItemStack(Items.POTIONITEM, 1, 8192), true, new GristSet(new GristType[] {GristType.Build, GristType.Cobalt}, new int[] {1, 4}));	//mundane
		GristRegistry.addGristConversion(new ItemStack(Items.POTIONITEM, 1, 64), true, new GristSet(new GristType[] {GristType.Build, GristType.Cobalt, GristType.Garnet}, new int[] {1, 4, 1}));	//mundane from using redstone
		GristRegistry.addGristConversion(new ItemStack(Items.POTIONITEM, 1, 16), true, new GristSet(new GristType[] {GristType.Build, GristType.Cobalt, GristType.Iodine, GristType.Tar}, new int[] {1, 4, 1, 2}));	//awkward
		GristRegistry.addGristConversion(new ItemStack(Items.POTIONITEM, 1, 32), true, new GristSet(new GristType[] {GristType.Build, GristType.Cobalt, GristType.Tar, GristType.Chalk}, new int[] {1, 4, 1, 2}));	//thick
		GristRegistry.addGristConversion(new ItemStack(Items.POTIONITEM, 1, 8200), true, new GristSet(new GristType[] {GristType.Build, GristType.Cobalt, GristType.Amber, GristType.Iodine}, new int[] {1, 4, 3, 2}));	//weakness
		GristRegistry.addGristConversion(new ItemStack(Items.POTIONITEM, 1, 8193), true, new GristSet(new GristType[] {GristType.Build, GristType.Cobalt, GristType.Iodine, GristType.Tar, GristType.Chalk}, new int[] {1, 8, 1, 2, 5}));	//regen*/
		//TODO Continue with potion grist costs
		/*GristRegistry.addGristConversion(new ItemStack(Items.potionitem, 1, 10), true, new GristSet(new GristType[] {GristType.Build, GristType.Cobalt, GristType.Iodine, GristType.Amber, GristType.Tar}, new int[] {1, 4, 8, 6, 2}));	//slowness
		GristRegistry.addGristConversion(new ItemStack(Items.potionitem, 1, 12), true, new GristSet(new GristType[] {GristType.Build, GristType.Cobalt, GristType.Gold, GristType.Amber, GristType.Iodine, GristType.Tar, GristType.Chalk}, new int[] {1, 4, 16, 7, 4, 2, 1}));	//harming
		GristRegistry.addGristConversion(new ItemStack(Items.potionitem, 1, 14), true, new GristSet(new GristType[] {GristType.Build, GristType.Cobalt, GristType.Amber, GristType.Chalk, GristType.Gold, GristType.Iodine, GristType.Tar}, new int[] {1, 4, 9, 1, 16, 4, 2}));	//invisibility
		GristRegistry.addGristConversion(new ItemStack(Items.potionitem, 1, 2), true, new GristSet(new GristType[] {GristType.Build, GristType.Cobalt, GristType.Iodine}, new int[] {1, 4, 4}));	//speed
		GristRegistry.addGristConversion(new ItemStack(Items.potionitem, 1, 3), true, new GristSet(new GristType[] {GristType.Build, GristType.Cobalt, GristType.Tar, GristType.Uranium, GristType.Caulk}, new int[] {1, 4, 8, 1, 8}));	//fire resistance
		GristRegistry.addGristConversion(new ItemStack(Items.potionitem, 1, 4), true, new GristSet(new GristType[] {GristType.Build, GristType.Cobalt, GristType.Amber, GristType.Iodine}, new int[] {1, 4, 4, 2}));	//poison
		GristRegistry.addGristConversion(new ItemStack(Items.potionitem, 1, 5), true, new GristSet(new GristType[] {GristType.Build, GristType.Cobalt, GristType.Gold, GristType.Amber, GristType.Chalk}, new int[] {1, 4, 16, 1, 1}));	//healing
		GristRegistry.addGristConversion(new ItemStack(Items.potionitem, 1, 6), true, new GristSet(new GristType[] {GristType.Build, GristType.Cobalt, GristType.Amber, GristType.Chalk, GristType.Gold}, new int[] {1, 4, 3, 1, 16}));	//night vision
		GristRegistry.addGristConversion(new ItemStack(Items.potionitem, 1, 9), true, new GristSet(new GristType[] {GristType.Build, GristType.Tar, GristType.Uranium}, new int[] {1, 8, 1}));	//strength*/
		
		//Set up Punch Designix recipes
		
		//Wood
		final String[] woodDict = {"logWood","plankWood","slabWood","stairWood","treeSapling","treeLeaves","doorWood","fenceWood","fencegateWood"};
		final ItemStack[][] woodItems = {
				{new ItemStack(Blocks.LOG,1,0),new ItemStack(Blocks.LOG,1,1),new ItemStack(Blocks.LOG,1,2),new ItemStack(Blocks.LOG,1,3),new ItemStack(Blocks.LOG2,1,0),new ItemStack(Blocks.LOG2,1,1)},
				{new ItemStack(Blocks.PLANKS,1,0),new ItemStack(Blocks.PLANKS,1,1),new ItemStack(Blocks.PLANKS,1,2),new ItemStack(Blocks.PLANKS,1,3),new ItemStack(Blocks.PLANKS,1,4),new ItemStack(Blocks.PLANKS,1,5)},
				{new ItemStack(Blocks.WOODEN_SLAB,1,0),new ItemStack(Blocks.WOODEN_SLAB,1,1),new ItemStack(Blocks.WOODEN_SLAB,1,2),new ItemStack(Blocks.WOODEN_SLAB,1,3),new ItemStack(Blocks.WOODEN_SLAB,1,4),new ItemStack(Blocks.WOODEN_SLAB,1,5)},
				{new ItemStack(Blocks.OAK_STAIRS),new ItemStack(Blocks.SPRUCE_STAIRS),new ItemStack(Blocks.BIRCH_STAIRS),new ItemStack(Blocks.JUNGLE_STAIRS),new ItemStack(Blocks.ACACIA_STAIRS),new ItemStack(Blocks.DARK_OAK_STAIRS)},
				{new ItemStack(Blocks.SAPLING,1,0),new ItemStack(Blocks.SAPLING,1,1),new ItemStack(Blocks.SAPLING,1,2),new ItemStack(Blocks.SAPLING,1,3),new ItemStack(Blocks.SAPLING,1,4),new ItemStack(Blocks.SAPLING,1,5)},
				{new ItemStack(Blocks.LEAVES,1,0),new ItemStack(Blocks.LEAVES,1,1),new ItemStack(Blocks.LEAVES,1,2),new ItemStack(Blocks.LEAVES,1,3),new ItemStack(Blocks.LEAVES2,1,0),new ItemStack(Blocks.LEAVES2,1,1)},
				{new ItemStack(Items.OAK_DOOR),new ItemStack(Items.SPRUCE_DOOR),new ItemStack(Items.BIRCH_DOOR),new ItemStack(Items.JUNGLE_DOOR),new ItemStack(Items.ACACIA_DOOR),new ItemStack(Items.DARK_OAK_DOOR)},
				{new ItemStack(Blocks.OAK_FENCE),new ItemStack(Blocks.SPRUCE_FENCE),new ItemStack(Blocks.BIRCH_FENCE),new ItemStack(Blocks.JUNGLE_FENCE),new ItemStack(Blocks.ACACIA_FENCE),new ItemStack(Blocks.DARK_OAK_FENCE)},
				{new ItemStack(Blocks.OAK_FENCE_GATE),new ItemStack(Blocks.SPRUCE_FENCE_GATE),new ItemStack(Blocks.BIRCH_FENCE_GATE),new ItemStack(Blocks.JUNGLE_FENCE_GATE),new ItemStack(Blocks.ACACIA_FENCE_GATE),new ItemStack(Blocks.DARK_OAK_FENCE_GATE)}};
		for(int i = 6; i < woodItems.length; i++)
			for(ItemStack stack : woodItems[i])
				checkRegistered(stack, woodDict[i]);
		
		for(int i1 = 0; i1 < woodItems.length; i1++)
		{
			CombinationRegistry.addCombination(woodItems[i1][0], woodItems[i1][1], MODE_OR, woodItems[i1][5]);	//Oak | spruce -> dark oak
			CombinationRegistry.addCombination(woodItems[i1][2], woodItems[i1][3], MODE_OR, woodItems[i1][4]);	//Birch | jungle -> acacia
		}
		for(int i2 = 0; i2 < woodItems[0].length; i2++)
		{
			CombinationRegistry.addCombination(woodItems[1][i2], woodItems[2][i2], MODE_OR, woodItems[3][i2]);	//plank | slab -> stair
			CombinationRegistry.addCombination(woodItems[0][i2], woodItems[5][i2], MODE_OR, woodItems[4][i2]);	//leaf | log -> sapling
			CombinationRegistry.addCombination(woodItems[6][i2], woodItems[7][i2], MODE_OR, woodItems[8][i2]);	//door | fence -> fence gate
			CombinationRegistry.addCombination(new ItemStack(Items.WHEAT_SEEDS), woodItems[5][i2], MODE_AND, new ItemStack(Blocks.SAPLING, 1, i2));
			CombinationRegistry.addCombination(new ItemStack(Items.STICK), woodItems[5][i2], MODE_AND, new ItemStack(Blocks.SAPLING, 1, i2));
		}
		CombinationRegistry.addCombination(woodItems[1][0], woodItems[2][1], MODE_OR, woodItems[3][5]);
		CombinationRegistry.addCombination(woodItems[2][0], woodItems[1][1], MODE_OR, woodItems[3][5]);
		CombinationRegistry.addCombination(woodItems[0][0], woodItems[5][1], MODE_OR, woodItems[4][5]);
		CombinationRegistry.addCombination(woodItems[5][0], woodItems[0][1], MODE_OR, woodItems[4][5]);
		CombinationRegistry.addCombination(woodItems[6][0], woodItems[7][1], MODE_OR, woodItems[8][5]);
		CombinationRegistry.addCombination(woodItems[7][0], woodItems[6][1], MODE_OR, woodItems[8][5]);
		
		CombinationRegistry.addCombination(woodItems[1][2], woodItems[2][3], MODE_OR, woodItems[3][4]);
		CombinationRegistry.addCombination(woodItems[2][2], woodItems[1][3], MODE_OR, woodItems[3][4]);
		CombinationRegistry.addCombination(woodItems[0][2], woodItems[5][3], MODE_OR, woodItems[4][4]);
		CombinationRegistry.addCombination(woodItems[5][2], woodItems[0][3], MODE_OR, woodItems[4][4]);
		CombinationRegistry.addCombination(woodItems[6][2], woodItems[7][3], MODE_OR, woodItems[8][4]);
		CombinationRegistry.addCombination(woodItems[7][2], woodItems[6][3], MODE_OR, woodItems[8][4]);
		
		CombinationRegistry.addCombination("doorWood", Items.IRON_INGOT, WILDCARD_VALUE, MODE_AND, new ItemStack(Items.IRON_DOOR));
		CombinationRegistry.addCombination("fenceWood", Blocks.NETHER_BRICK, WILDCARD_VALUE, MODE_AND, new ItemStack(Blocks.NETHER_BRICK_FENCE));
		CombinationRegistry.addCombination("stairWood", Blocks.NETHER_BRICK, WILDCARD_VALUE, MODE_AND, new ItemStack(Blocks.NETHER_BRICK_STAIRS));
		CombinationRegistry.addCombination("fenceWood", Items.NETHERBRICK, WILDCARD_VALUE, MODE_AND, new ItemStack(Blocks.NETHER_BRICK_FENCE));
		CombinationRegistry.addCombination("stairWood", Items.NETHERBRICK, WILDCARD_VALUE, MODE_AND, new ItemStack(Blocks.NETHER_BRICK_STAIRS));
		CombinationRegistry.addCombination("doorWood", "slabWood", MODE_AND, new ItemStack(Blocks.TRAPDOOR));
		CombinationRegistry.addCombination("logWood", Items.COAL, 0, MODE_AND, new ItemStack(Items.COAL, 1, 1));
		
		//Dye
		Block[] coloredBlocks = {Blocks.WOOL, Blocks.STAINED_HARDENED_CLAY, Blocks.STAINED_GLASS, Blocks.STAINED_GLASS_PANE};
		for(int i1 = 0; i1 < coloredBlocks.length; i1++)
		{
			for (EnumDyeColor color : EnumDyeColor.values())
			{
				if(color != EnumDyeColor.WHITE)
					CombinationRegistry.addCombination(new ItemStack(Items.DYE, 1, color.getDyeDamage()), new ItemStack(coloredBlocks[i1], 1, EnumDyeColor.WHITE.getMetadata()), MODE_OR, new ItemStack(coloredBlocks[i1], 1, color.getMetadata()));
			}
		}
		for (EnumDyeColor color : EnumDyeColor.values())
		{
			CombinationRegistry.addCombination(new ItemStack(Blocks.GLASS), new ItemStack(Items.DYE, 1, color.getDyeDamage()), MODE_AND, new ItemStack(Blocks.STAINED_GLASS, 1, color.getMetadata()));
			CombinationRegistry.addCombination(new ItemStack(Blocks.GLASS_PANE), new ItemStack(Items.DYE, 1, color.getDyeDamage()), MODE_AND, new ItemStack(Blocks.STAINED_GLASS_PANE, 1, color.getMetadata()));
			CombinationRegistry.addCombination(new ItemStack(Blocks.HARDENED_CLAY), new ItemStack(Items.DYE, 1, color.getDyeDamage()), MODE_AND, new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, color.getMetadata()));
		}
		
		//ore related
		CombinationRegistry.addCombination(new ItemStack(Items.COAL),new ItemStack(Blocks.STONE),MODE_AND, new ItemStack(Blocks.COAL_ORE));
		CombinationRegistry.addCombination(new ItemStack(Items.COAL),new ItemStack(Blocks.STONE),MODE_OR, new ItemStack(Blocks.COAL_BLOCK));
		CombinationRegistry.addCombination(new ItemStack(Items.DIAMOND),new ItemStack(Blocks.STONE),MODE_AND, new ItemStack(Blocks.DIAMOND_ORE));
		CombinationRegistry.addCombination(new ItemStack(Items.DIAMOND),new ItemStack(Blocks.STONE),MODE_OR, new ItemStack(Blocks.DIAMOND_BLOCK));
		CombinationRegistry.addCombination(new ItemStack(Items.DYE,1,4),new ItemStack(Blocks.STONE),MODE_AND, new ItemStack(Blocks.LAPIS_ORE));
		CombinationRegistry.addCombination(new ItemStack(Items.DYE,1,4),new ItemStack(Blocks.STONE),MODE_OR, new ItemStack(Blocks.LAPIS_BLOCK));
		CombinationRegistry.addCombination(new ItemStack(Items.EMERALD),new ItemStack(Blocks.STONE),MODE_AND, new ItemStack(Blocks.EMERALD_ORE));
		CombinationRegistry.addCombination(new ItemStack(Items.EMERALD),new ItemStack(Blocks.STONE),MODE_OR, new ItemStack(Blocks.EMERALD_BLOCK));
		CombinationRegistry.addCombination(new ItemStack(Items.GOLD_INGOT),new ItemStack(Blocks.STONE),MODE_AND, new ItemStack(Blocks.GOLD_ORE));
		CombinationRegistry.addCombination(new ItemStack(Items.GOLD_INGOT),new ItemStack(Blocks.STONE),MODE_OR, new ItemStack(Blocks.GOLD_BLOCK));
		CombinationRegistry.addCombination(new ItemStack(Items.IRON_INGOT),new ItemStack(Blocks.STONE),MODE_AND, new ItemStack(Blocks.IRON_ORE));
		CombinationRegistry.addCombination(new ItemStack(Items.IRON_INGOT),new ItemStack(Blocks.STONE),MODE_OR, new ItemStack(Blocks.IRON_BLOCK));
		CombinationRegistry.addCombination(new ItemStack(Items.QUARTZ),new ItemStack(Blocks.NETHERRACK),MODE_AND, new ItemStack(Blocks.QUARTZ_ORE));
		CombinationRegistry.addCombination(new ItemStack(Items.QUARTZ),new ItemStack(Blocks.STONE),MODE_OR, new ItemStack(Blocks.QUARTZ_BLOCK));
		CombinationRegistry.addCombination(new ItemStack(Items.REDSTONE),new ItemStack(Blocks.STONE),MODE_AND, new ItemStack(Blocks.REDSTONE_ORE));
		CombinationRegistry.addCombination(new ItemStack(Items.REDSTONE),new ItemStack(Blocks.STONE),MODE_OR, new ItemStack(Blocks.REDSTONE_BLOCK));
		
		CombinationRegistry.addCombination(new ItemStack(Blocks.STONE), new ItemStack(Blocks.STONEBRICK, 1, 2), MODE_AND, new ItemStack(Blocks.STONEBRICK, 1, 0));
		CombinationRegistry.addCombination(new ItemStack(Blocks.STONE), new ItemStack(Blocks.STONEBRICK, 1, 2), MODE_OR, new ItemStack(Blocks.COBBLESTONE));
		CombinationRegistry.addCombination(new ItemStack(Blocks.STONE), new ItemStack(Blocks.GRAVEL), MODE_OR, new ItemStack(Blocks.COBBLESTONE));
		CombinationRegistry.addCombination(new ItemStack(Blocks.STONE), new ItemStack(Blocks.SAND, 1, 0), MODE_OR, new ItemStack(Blocks.SANDSTONE, 1, 0));
		CombinationRegistry.addCombination(new ItemStack(Blocks.COBBLESTONE), new ItemStack(Blocks.SANDSTONE, 1, 2), MODE_AND, new ItemStack(Blocks.STONE));
		CombinationRegistry.addCombination(new ItemStack(Blocks.COBBLESTONE), new ItemStack(Blocks.SANDSTONE, 1, 2), MODE_OR, new ItemStack(Blocks.SANDSTONE, 1, 0));
		CombinationRegistry.addCombination(new ItemStack(Blocks.COBBLESTONE), new ItemStack(Blocks.SAND), MODE_AND, true, false, new ItemStack(Blocks.GRAVEL));
		CombinationRegistry.addCombination(new ItemStack(Blocks.COBBLESTONE), new ItemStack(Blocks.SAND, 1, 0), MODE_OR, new ItemStack(Blocks.SANDSTONE, 1, 0));
		CombinationRegistry.addCombination(new ItemStack(Blocks.GRAVEL), new ItemStack(Blocks.SANDSTONE, 1, 2), MODE_OR, new ItemStack(Blocks.SANDSTONE, 1, 0));
		CombinationRegistry.addCombination(new ItemStack(Blocks.SANDSTONE, 1, 2), new ItemStack(Blocks.SAND, 1, 0), MODE_OR, new ItemStack(Blocks.SANDSTONE, 1, 0));
		
		//misc
		CombinationRegistry.addCombination(new ItemStack(Blocks.COBBLESTONE),new ItemStack(Items.COAL),MODE_AND, new ItemStack(Blocks.FURNACE));
		CombinationRegistry.addCombination(new ItemStack(Blocks.COBBLESTONE),new ItemStack(Items.WHEAT_SEEDS),MODE_OR, new ItemStack(Blocks.MOSSY_COBBLESTONE));
		CombinationRegistry.addCombination(new ItemStack(Blocks.COBBLESTONE_WALL),new ItemStack(Items.WHEAT_SEEDS),MODE_OR, new ItemStack(Blocks.COBBLESTONE_WALL,1,1));
		CombinationRegistry.addCombination(new ItemStack(Blocks.DIRT), new ItemStack(Blocks.TALLGRASS), MODE_OR, true, false, new ItemStack(Blocks.GRASS));
		CombinationRegistry.addCombination(new ItemStack(Blocks.DIRT),new ItemStack(Items.WHEAT_SEEDS),MODE_AND, new ItemStack(Blocks.GRASS));
		CombinationRegistry.addCombination(new ItemStack(Blocks.GRASS),new ItemStack(Blocks.BROWN_MUSHROOM),MODE_AND, new ItemStack(Blocks.MYCELIUM));
		CombinationRegistry.addCombination(new ItemStack(Blocks.GRASS),new ItemStack(Blocks.RED_MUSHROOM),MODE_AND, new ItemStack(Blocks.MYCELIUM));
		CombinationRegistry.addCombination(new ItemStack(Blocks.LADDER),new ItemStack(Items.IRON_INGOT),MODE_AND, new ItemStack(Blocks.RAIL));
		CombinationRegistry.addCombination(new ItemStack(Blocks.NETHERRACK), new ItemStack(Blocks.BRICK_BLOCK), MODE_AND, new ItemStack(Blocks.NETHER_BRICK));
		CombinationRegistry.addCombination(new ItemStack(Blocks.NETHERRACK), new ItemStack(Items.BRICK), MODE_AND, new ItemStack(Blocks.NETHER_BRICK));
		CombinationRegistry.addCombination(new ItemStack(Blocks.NETHERRACK), new ItemStack(Items.BRICK), MODE_OR, new ItemStack(Items.NETHERBRICK));
		CombinationRegistry.addCombination(new ItemStack(Blocks.NETHER_BRICK), new ItemStack(Items.BRICK), MODE_OR, new ItemStack(Items.NETHERBRICK));
		CombinationRegistry.addCombination(new ItemStack(Blocks.NETHERRACK),new ItemStack(Items.GLOWSTONE_DUST),MODE_AND, new ItemStack(Blocks.GLOWSTONE));
		CombinationRegistry.addCombination(new ItemStack(Blocks.NOTEBLOCK),new ItemStack(Items.DIAMOND),MODE_AND, new ItemStack(Blocks.JUKEBOX));
		CombinationRegistry.addCombination(new ItemStack(Blocks.RAIL), new ItemStack(Blocks.PLANKS), MODE_AND, true, false, new ItemStack(Blocks.LADDER));
		CombinationRegistry.addCombination(new ItemStack(Blocks.SAPLING, 1, 0),new ItemStack(Items.WHEAT_SEEDS),MODE_AND,true,false, new ItemStack(Items.APPLE));
		CombinationRegistry.addCombination(new ItemStack(Blocks.LEAVES, 1, 0),new ItemStack(Items.WHEAT_SEEDS),MODE_OR, true, false, new ItemStack(Items.APPLE));
		CombinationRegistry.addCombination(new ItemStack(Blocks.STONE),new ItemStack(Items.ENDER_PEARL),MODE_AND, new ItemStack(Blocks.END_STONE));
		CombinationRegistry.addCombination(new ItemStack(Blocks.STONEBRICK, 1, 0), new ItemStack(Items.WHEAT_SEEDS), MODE_OR, new ItemStack(Blocks.STONEBRICK, 1, 1));
		CombinationRegistry.addCombination(new ItemStack(Items.APPLE),new ItemStack(Items.GOLD_INGOT),MODE_AND, new ItemStack(Items.GOLDEN_APPLE, 1, 0));
		CombinationRegistry.addCombination(new ItemStack(Items.APPLE),new ItemStack(Items.GOLD_NUGGET),MODE_AND, new ItemStack(Items.GOLDEN_APPLE, 1, 0));
		CombinationRegistry.addCombination(new ItemStack(Items.APPLE),new ItemStack(Blocks.GOLD_BLOCK),MODE_AND, new ItemStack(Items.GOLDEN_APPLE, 1, 1));
		CombinationRegistry.addCombination(new ItemStack(Items.CARROT),new ItemStack(Items.WHEAT_SEEDS),MODE_AND, new ItemStack(Items.POTATO));
		CombinationRegistry.addCombination(new ItemStack(Items.CLOCK),new ItemStack(Items.IRON_INGOT),MODE_AND, new ItemStack(Items.COMPASS));
		CombinationRegistry.addCombination(new ItemStack(Items.COMPASS),new ItemStack(Items.GOLD_INGOT),MODE_AND, new ItemStack(Items.CLOCK));
		CombinationRegistry.addCombination(new ItemStack(Items.DIAMOND),new ItemStack(Items.SADDLE),MODE_AND, new ItemStack(Items.DIAMOND_HORSE_ARMOR));
		CombinationRegistry.addCombination(new ItemStack(Items.ENDER_EYE),new ItemStack(Items.EGG),MODE_AND, new ItemStack(Blocks.DRAGON_EGG));
		CombinationRegistry.addCombination(new ItemStack(Items.ENDER_PEARL),new ItemStack(Items.BLAZE_POWDER),MODE_AND, new ItemStack(Items.ENDER_EYE));
		CombinationRegistry.addCombination(new ItemStack(Items.GOLD_INGOT),new ItemStack(Items.SADDLE),MODE_AND, new ItemStack(Items.GOLDEN_HORSE_ARMOR));
		CombinationRegistry.addCombination(new ItemStack(Items.GUNPOWDER),new ItemStack(Blocks.SAND), MODE_AND, true, false, new ItemStack(Blocks.TNT));
		CombinationRegistry.addCombination(new ItemStack(Items.IRON_INGOT), new ItemStack(Blocks.TALLGRASS), MODE_AND, true, false, new ItemStack(Items.SHEARS));
		CombinationRegistry.addCombination(new ItemStack(Items.IRON_INGOT),new ItemStack(Items.SADDLE),MODE_AND, new ItemStack(Items.IRON_HORSE_ARMOR));
		CombinationRegistry.addCombination(new ItemStack(Items.POTATO),new ItemStack(Items.WHEAT_SEEDS),MODE_OR, new ItemStack(Items.CARROT));
		CombinationRegistry.addCombination(new ItemStack(Items.REDSTONE),new ItemStack(Items.GOLD_INGOT),MODE_OR, new ItemStack(Items.CLOCK));
		CombinationRegistry.addCombination(new ItemStack(Items.REDSTONE),new ItemStack(Items.IRON_INGOT),MODE_OR, new ItemStack(Items.COMPASS));
		CombinationRegistry.addCombination(new ItemStack(Items.ROTTEN_FLESH),new ItemStack(Items.CARROT),MODE_OR, new ItemStack(Items.PORKCHOP));
		CombinationRegistry.addCombination(new ItemStack(Items.ROTTEN_FLESH),new ItemStack(Items.WATER_BUCKET),MODE_OR, new ItemStack(Items.LEATHER));
		CombinationRegistry.addCombination(new ItemStack(Items.ROTTEN_FLESH),new ItemStack(Items.WHEAT),MODE_OR, new ItemStack(Items.BEEF));
		CombinationRegistry.addCombination(new ItemStack(Items.ROTTEN_FLESH),new ItemStack(Items.WHEAT_SEEDS),MODE_OR, new ItemStack(Items.CHICKEN));
		CombinationRegistry.addCombination(new ItemStack(Items.SLIME_BALL),new ItemStack(Items.BLAZE_POWDER),MODE_AND, new ItemStack(Items.MAGMA_CREAM));
		CombinationRegistry.addCombination("stickWood", Items.LAVA_BUCKET, WILDCARD_VALUE, MODE_AND, new ItemStack(Items.BLAZE_ROD));
		CombinationRegistry.addCombination(new ItemStack(Items.STRING),new ItemStack(Items.LEATHER),MODE_AND, new ItemStack(Items.SADDLE));
		CombinationRegistry.addCombination(new ItemStack(Items.REDSTONE), new ItemStack(Items.LAVA_BUCKET), MODE_OR, new ItemStack(Items.BLAZE_POWDER));
		CombinationRegistry.addCombination(new ItemStack(Items.REDSTONE), new ItemStack(Blocks.NETHERRACK), MODE_OR, new ItemStack(Items.BLAZE_POWDER));
		CombinationRegistry.addCombination("stickWood", Items.BLAZE_POWDER, WILDCARD_VALUE, MODE_AND, new ItemStack(Items.BLAZE_ROD));
		CombinationRegistry.addCombination(new ItemStack(Items.BOAT), new ItemStack(Blocks.RAIL), MODE_OR, new ItemStack(Items.MINECART));
		CombinationRegistry.addCombination(new ItemStack(Items.IRON_INGOT), new ItemStack(Blocks.TRAPDOOR), MODE_OR, new ItemStack(Blocks.IRON_TRAPDOOR));
		CombinationRegistry.addCombination(new ItemStack(Blocks.IRON_BLOCK), new ItemStack(Blocks.TRAPDOOR), MODE_OR, new ItemStack(Blocks.IRON_TRAPDOOR));
		CombinationRegistry.addCombination("stickWood", Blocks.IRON_BARS, WILDCARD_VALUE, MODE_AND, new ItemStack(Blocks.RAIL));
		CombinationRegistry.addCombination(new ItemStack(Blocks.COBBLESTONE_WALL), new ItemStack(Blocks.MOSSY_COBBLESTONE), MODE_OR, new ItemStack(Blocks.COBBLESTONE_WALL, 1, 1));
		CombinationRegistry.addCombination(new ItemStack(Blocks.DISPENSER), new ItemStack(Blocks.HOPPER), MODE_AND, new ItemStack(Blocks.DROPPER));
		CombinationRegistry.addCombination(new ItemStack(Blocks.TORCH), new ItemStack(Items.REDSTONE), MODE_AND, new ItemStack(Blocks.REDSTONE_TORCH));
		CombinationRegistry.addCombination(new ItemStack(Blocks.TORCH), new ItemStack(Items.REDSTONE), MODE_OR, new ItemStack(Items.GLOWSTONE_DUST));
		CombinationRegistry.addCombination(new ItemStack(Items.MINECART), new ItemStack(Blocks.CHEST), MODE_AND, new ItemStack(Items.CHEST_MINECART));
		CombinationRegistry.addCombination(new ItemStack(Items.MINECART), new ItemStack(Blocks.FURNACE), MODE_AND, new ItemStack(Items.FURNACE_MINECART));
		CombinationRegistry.addCombination(new ItemStack(Items.MINECART), new ItemStack(Blocks.TNT), MODE_AND, new ItemStack(Items.TNT_MINECART));
		CombinationRegistry.addCombination(new ItemStack(Items.MINECART), new ItemStack(Blocks.HOPPER), MODE_AND, new ItemStack(Items.HOPPER_MINECART));
		CombinationRegistry.addCombination(new ItemStack(Blocks.RAIL), new ItemStack(Blocks.REDSTONE_TORCH), MODE_AND, new ItemStack(Blocks.ACTIVATOR_RAIL));
		CombinationRegistry.addCombination(new ItemStack(Blocks.RAIL), new ItemStack(Blocks.WOODEN_PRESSURE_PLATE), MODE_AND, new ItemStack(Blocks.DETECTOR_RAIL));
		CombinationRegistry.addCombination(new ItemStack(Blocks.RAIL), new ItemStack(Blocks.STONE_PRESSURE_PLATE), MODE_AND, new ItemStack(Blocks.DETECTOR_RAIL));
		CombinationRegistry.addCombination(new ItemStack(Blocks.RAIL), new ItemStack(Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE), MODE_AND, new ItemStack(Blocks.DETECTOR_RAIL));
		CombinationRegistry.addCombination(new ItemStack(Blocks.RAIL), new ItemStack(Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE), MODE_AND, new ItemStack(Blocks.DETECTOR_RAIL));
		CombinationRegistry.addCombination(new ItemStack(Blocks.RAIL), new ItemStack(Items.GOLD_INGOT), MODE_AND, new ItemStack(Blocks.GOLDEN_RAIL));
		CombinationRegistry.addCombination(new ItemStack(Blocks.RAIL), new ItemStack(Items.FURNACE_MINECART), MODE_OR, new ItemStack(Blocks.GOLDEN_RAIL));
		CombinationRegistry.addCombination(new ItemStack(Blocks.GLOWSTONE), new ItemStack(Blocks.REDSTONE_TORCH), MODE_AND, new ItemStack(Blocks.REDSTONE_LAMP));
		CombinationRegistry.addCombination(new ItemStack(Items.PRISMARINE_SHARD), new ItemStack(Items.DIAMOND), MODE_OR, new ItemStack(Items.PRISMARINE_CRYSTALS));
		CombinationRegistry.addCombination(new ItemStack(Items.PRISMARINE_SHARD), new ItemStack(Items.EMERALD), MODE_OR, new ItemStack(Items.PRISMARINE_CRYSTALS));
		CombinationRegistry.addCombination(new ItemStack(Blocks.TALLGRASS), new ItemStack(Blocks.SAND), false, false, MODE_OR, new ItemStack(Blocks.DEADBUSH));
		CombinationRegistry.addCombination(new ItemStack(Blocks.TALLGRASS), new ItemStack(Blocks.SAND), false, false, MODE_AND, new ItemStack(Blocks.CACTUS));
		CombinationRegistry.addCombination("treeSapling", Blocks.SAND, WILDCARD_VALUE, MODE_AND, new ItemStack(Blocks.DEADBUSH));
		CombinationRegistry.addCombination(new ItemStack(Items.ENDER_PEARL), new ItemStack(Blocks.CHEST), MODE_AND, new ItemStack(Blocks.ENDER_CHEST));
		CombinationRegistry.addCombination(new ItemStack(Blocks.GLASS), new ItemStack(Blocks.SNOW), MODE_AND, new ItemStack(Blocks.ICE));
		CombinationRegistry.addCombination(new ItemStack(Blocks.SPONGE, 1, 0), new ItemStack(Items.WATER_BUCKET), MODE_AND, new ItemStack(Blocks.SPONGE, 1, 1));
		CombinationRegistry.addCombination(new ItemStack(Items.BLAZE_POWDER), new ItemStack(Items.GUNPOWDER), MODE_OR, new ItemStack(Items.FIRE_CHARGE));
		CombinationRegistry.addCombination(new ItemStack(Blocks.SAND, 1, 0), new ItemStack(Blocks.STONE_STAIRS), MODE_OR, new ItemStack(Blocks.SANDSTONE_STAIRS));
		CombinationRegistry.addCombination(new ItemStack(Blocks.SAND, 1, 1), new ItemStack(Blocks.STONE_STAIRS), MODE_OR, new ItemStack(Blocks.RED_SANDSTONE_STAIRS));
		CombinationRegistry.addCombination(new ItemStack(Blocks.SAND, 1, 0), new ItemStack(Items.DYE, 1, EnumDyeColor.RED.getDyeDamage()), MODE_AND, new ItemStack(Blocks.SAND, 1, 1));
		for(int i = 0; i <= 2; i++)
			CombinationRegistry.addCombination(new ItemStack(Blocks.SANDSTONE, 1, i), new ItemStack(Items.DYE, 1, EnumDyeColor.RED.getDyeDamage()), MODE_AND, new ItemStack(Blocks.RED_SANDSTONE, 1, i));
		CombinationRegistry.addCombination(new ItemStack(Blocks.SANDSTONE_STAIRS), new ItemStack(Items.DYE, 1, EnumDyeColor.RED.getDyeDamage()), MODE_AND, new ItemStack(Blocks.RED_SANDSTONE_STAIRS));
		CombinationRegistry.addCombination(new ItemStack(Items.BOOK), new ItemStack(Blocks.PLANKS), true, false, MODE_OR, new ItemStack(Blocks.BOOKSHELF));
		CombinationRegistry.addCombination("record", Blocks.NOTEBLOCK, WILDCARD_VALUE, MODE_AND, new ItemStack(Blocks.JUKEBOX));
		CombinationRegistry.addCombination("stickWood", Blocks.VINE, WILDCARD_VALUE, MODE_AND, new ItemStack(Blocks.LADDER));
		CombinationRegistry.addCombination("treeLeaves", Blocks.LADDER, WILDCARD_VALUE, MODE_OR, new ItemStack(Blocks.VINE));
		CombinationRegistry.addCombination(new ItemStack(Items.PRISMARINE_SHARD), new ItemStack(Blocks.COBBLESTONE), MODE_AND, new ItemStack(Blocks.PRISMARINE, 1, 0));
		CombinationRegistry.addCombination(new ItemStack(Items.PRISMARINE_SHARD), new ItemStack(Blocks.STONEBRICK, 1, 0), MODE_AND, new ItemStack(Blocks.PRISMARINE, 1, 1));
		CombinationRegistry.addCombination(new ItemStack(Blocks.PRISMARINE), new ItemStack(Items.DYE, 1, EnumDyeColor.BLACK.getDyeDamage()), MODE_AND, false, true, new ItemStack(Blocks.PRISMARINE, 1, 2));
		CombinationRegistry.addCombination(new ItemStack(Items.PRISMARINE_SHARD), new ItemStack(Blocks.GLOWSTONE), MODE_OR, new ItemStack(Blocks.SEA_LANTERN));
		CombinationRegistry.addCombination(new ItemStack(Items.PRISMARINE_CRYSTALS), new ItemStack(Blocks.PRISMARINE), MODE_AND, true, false, new ItemStack(Blocks.SEA_LANTERN));
		CombinationRegistry.addCombination(new ItemStack(Blocks.RED_MUSHROOM), new ItemStack(Blocks.SOUL_SAND), MODE_AND, new ItemStack(Items.NETHER_WART));
		CombinationRegistry.addCombination(new ItemStack(Blocks.PRISMARINE), new ItemStack(Items.FLINT), MODE_OR, false, true, new ItemStack(Items.PRISMARINE_SHARD));
		CombinationRegistry.addCombination(new ItemStack(Blocks.COBBLESTONE), new ItemStack(Blocks.STONEBRICK, 1, 0), MODE_AND, new ItemStack(Blocks.STONEBRICK, 1, 2));
		CombinationRegistry.addCombination(new ItemStack(Items.GUNPOWDER), new ItemStack(Blocks.STONE_BUTTON), MODE_OR, new ItemStack(Items.REDSTONE));
		CombinationRegistry.addCombination(new ItemStack(Items.GUNPOWDER), new ItemStack(Blocks.STONE_PRESSURE_PLATE), MODE_OR, new ItemStack(Items.REDSTONE));
		CombinationRegistry.addCombination(new ItemStack(Items.GUNPOWDER), new ItemStack(Blocks.LEVER), MODE_OR, new ItemStack(Items.REDSTONE));
		CombinationRegistry.addCombination(new ItemStack(Items.GUNPOWDER), new ItemStack(Blocks.WOODEN_BUTTON), MODE_OR, new ItemStack(Blocks.REDSTONE_TORCH));
		CombinationRegistry.addCombination(new ItemStack(Items.GUNPOWDER), new ItemStack(Blocks.WOODEN_PRESSURE_PLATE), MODE_OR, new ItemStack(Blocks.REDSTONE_TORCH));
		CombinationRegistry.addCombination(new ItemStack(Blocks.GRAVEL), new ItemStack(Items.DYE, 1, EnumDyeColor.RED.getDyeDamage()), MODE_AND, new ItemStack(Items.REDSTONE));
		CombinationRegistry.addCombination(new ItemStack(Blocks.MELON_BLOCK), new ItemStack(Items.CARROT), MODE_AND, new ItemStack(Blocks.PUMPKIN));
		CombinationRegistry.addCombination(new ItemStack(Items.SKULL, 1, 1), new ItemStack(Items.BONE), MODE_AND, new ItemStack(Items.SKULL, 1, 0));
		CombinationRegistry.addCombination(new ItemStack(Items.SKULL, 1, 1), new ItemStack(Items.DYE, 1, EnumDyeColor.WHITE.getDyeDamage()), MODE_AND, new ItemStack(Items.SKULL, 1, 0));
		CombinationRegistry.addCombination(new ItemStack(Items.SKULL, 1, 0), new ItemStack(Items.ROTTEN_FLESH), MODE_OR, new ItemStack(Items.SKULL, 1, 2));
		CombinationRegistry.addCombination(new ItemStack(Items.NETHER_WART), new ItemStack(Blocks.SAND), MODE_OR, new ItemStack(Blocks.SOUL_SAND));
		CombinationRegistry.addCombination(new ItemStack(Items.BOOK), new ItemStack(Blocks.CHEST), MODE_AND, new ItemStack(Blocks.BOOKSHELF));
		CombinationRegistry.addCombination(new ItemStack(Items.WATER_BUCKET), new ItemStack(Blocks.WOOL, 1, EnumDyeColor.YELLOW.getMetadata()), MODE_AND, new ItemStack(Blocks.SPONGE, 1, 1));
		CombinationRegistry.addCombination(new ItemStack(Items.BOOK), new ItemStack(Items.EXPERIENCE_BOTTLE), MODE_AND, new ItemStack(Blocks.ENCHANTING_TABLE));
		CombinationRegistry.addCombination("treeLeaves", new ItemStack(Items.WATER_BUCKET), true, MODE_OR, new ItemStack(Blocks.WATERLILY));
		CombinationRegistry.addCombination(new ItemStack(Blocks.CRAFTING_TABLE), new ItemStack(Blocks.IRON_BLOCK), MODE_OR, new ItemStack(Blocks.ANVIL));
		CombinationRegistry.addCombination(new ItemStack(Items.WOODEN_SWORD), new ItemStack(Items.ARROW), false, true, MODE_OR, new ItemStack(Items.BOW));
		CombinationRegistry.addCombination(new ItemStack(Blocks.RED_FLOWER), new ItemStack(Items.BRICK), false, true, MODE_AND, new ItemStack(Items.FLOWER_POT));
		CombinationRegistry.addCombination(new ItemStack(Blocks.YELLOW_FLOWER), new ItemStack(Items.BRICK), false, true, MODE_AND, new ItemStack(Items.FLOWER_POT));
		CombinationRegistry.addCombination(new ItemStack(Blocks.COBBLESTONE), new ItemStack(Items.LAVA_BUCKET), MODE_AND, new ItemStack(Blocks.NETHERRACK));
		CombinationRegistry.addCombination(new ItemStack(Items.EMERALD), new ItemStack(Items.COAL), MODE_AND, new ItemStack(Items.DIAMOND));
		CombinationRegistry.addCombination(new ItemStack(Items.EMERALD), new ItemStack(Items.DYE, 1, EnumDyeColor.BLUE.getDyeDamage()), MODE_AND, new ItemStack(Items.DIAMOND));
		CombinationRegistry.addCombination(new ItemStack(Items.POTIONITEM, 1, 0), new ItemStack(Items.ENCHANTED_BOOK), MODE_OR, true, false, new ItemStack(Items.EXPERIENCE_BOTTLE));
		CombinationRegistry.addCombination(new ItemStack(Items.GLASS_BOTTLE), new ItemStack(Items.ENCHANTED_BOOK), MODE_OR, true, false, new ItemStack(Items.EXPERIENCE_BOTTLE));
		CombinationRegistry.addCombination(new ItemStack(Items.QUARTZ), new ItemStack(Items.WATER_BUCKET), MODE_OR, new ItemStack(Items.PRISMARINE_CRYSTALS));
		CombinationRegistry.addCombination(new ItemStack(Items.QUARTZ), new ItemStack(Items.WATER_BUCKET), MODE_AND, new ItemStack(Items.PRISMARINE_SHARD));
		CombinationRegistry.addCombination(new ItemStack(Items.FEATHER), new ItemStack(Items.ENDER_PEARL), MODE_OR, new ItemStack(Items.ELYTRA));
		CombinationRegistry.addCombination(new ItemStack(Items.REDSTONE), new ItemStack(Items.GLOWSTONE_DUST), MODE_OR, new ItemStack(MinestuckItems.glowystoneDust));
	}
	
	public static void registerMinestuckRecipes() {
		
		//set up crafting recipes
		/*RecipeSorter.register("minestuck:notmirrored", CraftingRecipes.NonMirroredRecipe.class, RecipeSorter.Category.SHAPED, "before:minecraft:shaped");
		RecipeSorter.register("minestuck:emptycard", CraftingRecipes.EmptyCardRecipe.class, RecipeSorter.Category.SHAPED, "before:minecraft:shaped");
		
		GameRegistry.addRecipe(new ItemStack(blockComputerOff,1,0),new Object[]{ "XXX","XYX","XXX",'Y',new ItemStack(cruxiteBlock),'X',new ItemStack(Items.IRON_INGOT,1)});
		GameRegistry.addRecipe(new ItemStack(cruxiteBlock),new Object[]{ "XXX","XXX","XXX",'X',new ItemStack(rawCruxite, 1)});
		cardRecipe = GameRegistry.addShapedRecipe(new ItemStack(captchaCard,8,0),new Object[]{ "XXX","XYX","XXX",'Y',new ItemStack(rawCruxite, 1),'X',new ItemStack(Items.PAPER,1)});
		cardRecipeAdded = true;
		GameRegistry.addRecipe(new ItemStack(clawHammer),new Object[]{ " XX","XY "," Y ",'X',new ItemStack(Items.IRON_INGOT),'Y',new ItemStack(Items.STICK)});
		GameRegistry.addRecipe(new ItemStack(woodenSpoon),new Object[]{ " X "," Y "," Y ",'X',new ItemStack(Items.BOWL),'Y',new ItemStack(Items.STICK)});
		GameRegistry.addRecipe(new ItemStack(chessboard),new Object[]{ "XYX","YXY","XYX",'X',new ItemStack(Blocks.STAINED_HARDENED_CLAY,1,0),'Y',new ItemStack(Blocks.STAINED_HARDENED_CLAY,1,15)});
		GameRegistry.addRecipe(new ItemStack(chessboard),new Object[]{ "XYX","YXY","XYX",'Y',new ItemStack(Blocks.STAINED_HARDENED_CLAY,1,0),'X',new ItemStack(Blocks.STAINED_HARDENED_CLAY,1,15)});
		GameRegistry.addRecipe(new ItemStack(disk, 1, 0),new Object[]{ "X X"," Y ","X X",'X',new ItemStack(rawCruxite, 1),'Y',new ItemStack(Items.IRON_INGOT,1)});
		GameRegistry.addRecipe(new ItemStack(disk, 1, 1),new Object[]{ " X ","XYX"," X ",'X',new ItemStack(rawCruxite, 1),'Y',new ItemStack(Items.IRON_INGOT,1)});
		GameRegistry.addShapelessRecipe(new ItemStack(rawCruxite, 9), new ItemStack(cruxiteBlock));
		GameRegistry.addRecipe(new ItemStack(cane, 1), new Object[] {"  X", " X ", "X  ", 'X', new ItemStack(Items.STICK, 1)});
		GameRegistry.addRecipe(new ItemStack(deuceClub, 1), new Object[] {"  Y", " X ", "X  ", 'X', new ItemStack(Items.STICK, 1), 'Y', new ItemStack(Blocks.PLANKS, 1)});
		GameRegistry.addRecipe(new ItemStack(fork, 1), new Object[] {"X X"," X "," X ", 'X', new ItemStack(Blocks.STONE,1)});
		GameRegistry.addRecipe(new ItemStack(sickle, 1), new Object[] {" XX","X  "," Y ", 'X', new ItemStack(Items.IRON_INGOT), 'Y', new ItemStack(Items.STICK)});
		GameRegistry.addRecipe(new ItemStack(katana, 1), new Object[] {"  X"," X ","Y  ", 'X', new ItemStack(Items.IRON_INGOT), 'Y', new ItemStack(Items.STICK)});
		GameRegistry.addRecipe(new ItemStack(sledgeHammer, 1), new Object[] {"XZX"," Y "," Y ", 'X', new ItemStack(Items.IRON_INGOT), 'Y', new ItemStack(Items.STICK), 'Z', new ItemStack(Blocks.STONE)});
		ItemStack crux = new ItemStack(rawCruxite);
		ItemStack cruxBl = new ItemStack(cruxiteBlock);
		ItemStack card = new ItemStack(captchaCard);
		GameRegistry.addRecipe(new CraftingRecipes.EmptyCardRecipe(3, 1, new ItemStack[]{cruxBl.copy(), card.copy(), crux.copy()}, new ItemStack(modusCard, 1, 0)));
		GameRegistry.addRecipe(new CraftingRecipes.EmptyCardRecipe(3, 1, new ItemStack[]{crux.copy(), card.copy(), cruxBl.copy()}, new ItemStack(modusCard, 1, 1)));
		GameRegistry.addRecipe(new ItemStack(coarseStoneStairs, 4), new Object[] {"X  ", "XX ", "XXX", 'X', new ItemStack(stone, 1, BlockMinestuckStone.COARSE_META)});
		GameRegistry.addRecipe(new ItemStack(shadeBrickStairs, 4), new Object[] {"X  ", "XX ", "XXX", 'X', new ItemStack(stone, 1, BlockMinestuckStone.SHADE_META)});
		GameRegistry.addRecipe(new ItemStack(frostBrickStairs, 4), new Object[] {"X  ", "XX ", "XXX", 'X', new ItemStack(stone, 1, BlockMinestuckStone.FROST_META)});
		GameRegistry.addRecipe(new ItemStack(castIronStairs, 4), new Object[] {"X  ", "XX ", "XXX", 'X', new ItemStack(stone, 1, BlockMinestuckStone.CAST_META)});
		GameRegistry.addRecipe(new ItemStack(stone, 4, BlockMinestuckStone.SHADE_META), new Object[] {"XX", "XX", 'X', new ItemStack(stone, 1, BlockMinestuckStone.SHADE_SMOOTH_META)});
		GameRegistry.addRecipe(new ItemStack(Blocks.PLANKS, 3, BlockPlanks.EnumType.SPRUCE.getMetadata()), new Object[] {"X", 'X', new ItemStack(woodenCactus)});
		GameRegistry.addShapelessRecipe(new ItemStack(glowingPlanks, 4), new ItemStack(glowingLog));
		GameRegistry.addShapelessRecipe(new ItemStack(salad,1),new Object[]{ new ItemStack(Blocks.LEAVES,1,WILDCARD_VALUE),new ItemStack(Items.BOWL)});
		GameRegistry.addShapelessRecipe(new ItemStack(salad,1),new Object[]{ new ItemStack(Blocks.LEAVES2,1,WILDCARD_VALUE),new ItemStack(Items.BOWL)});
		GameRegistry.addShapelessRecipe(new ItemStack(bugOnAStick, 3), jarOfBugs, Items.STICK, Items.STICK, Items.STICK);*///TODO Figure out recipe names and groups
		GameRegistry.addSmelting(goldSeeds, new ItemStack(Items.GOLD_NUGGET), 0.1F);
		GameRegistry.addSmelting(ironOreSandstone, new ItemStack(Items.IRON_INGOT), 0.7F);
		GameRegistry.addSmelting(ironOreSandstoneRed, new ItemStack(Items.IRON_INGOT), 0.7F);
		GameRegistry.addSmelting(goldOreSandstone, new ItemStack(Items.GOLD_INGOT), 1.0F);
		GameRegistry.addSmelting(goldOreSandstoneRed, new ItemStack(Items.GOLD_INGOT), 1.0F);
		GameRegistry.addSmelting(woodenCactus, new ItemStack(Items.COAL, 1, 1), 0.15F);
		GameRegistry.addSmelting(cruxiteDowel, new ItemStack(MinestuckItems.rawCruxite), 0.1F);
		
		//Register ore dictionary entries
		OreDictionary.registerOre("oreCoal", coalOreNetherrack);
		OreDictionary.registerOre("oreIron", ironOreSandstone);
		OreDictionary.registerOre("oreIron", ironOreSandstoneRed);
		OreDictionary.registerOre("oreGold", goldOreSandstone);
		OreDictionary.registerOre("oreGold", goldOreSandstoneRed);
		
		coalOreNetherrack.setHarvestLevel("pickaxe", Blocks.COAL_ORE.getHarvestLevel(Blocks.COAL_ORE.getDefaultState()));
		ironOreSandstone.setHarvestLevel("pickaxe", Blocks.IRON_ORE.getHarvestLevel(Blocks.IRON_ORE.getDefaultState()));
		ironOreSandstoneRed.setHarvestLevel("pickaxe", Blocks.IRON_ORE.getHarvestLevel(Blocks.IRON_ORE.getDefaultState()));
		goldOreSandstone.setHarvestLevel("pickaxe", Blocks.GOLD_ORE.getHarvestLevel(Blocks.GOLD_ORE.getDefaultState()));
		goldOreSandstoneRed.setHarvestLevel("pickaxe", Blocks.GOLD_ORE.getHarvestLevel(Blocks.GOLD_ORE.getDefaultState()));
		
		//add grist conversions
		GristRegistry.addGristConversion(new ItemStack(coloredDirt, 1, 0), new GristSet(new GristType[] {GristType.Build, GristType.Shale}, new int[] {1, 1}));
		GristRegistry.addGristConversion(new ItemStack(coloredDirt, 1, 1), new GristSet(new GristType[] {GristType.Build, GristType.Caulk}, new int[] {1, 1}));
		GristRegistry.addGristConversion(new ItemStack(crockerMachine, 1, 0), true, new GristSet(new GristType[] {GristType.Build, GristType.Garnet, GristType.Ruby}, new int[] {550, 55, 34}));
		GristRegistry.addGristConversion(new ItemStack(genericObject), true, new GristSet(new GristType[] {GristType.Build}, new int[] {2}));
		GristRegistry.addGristConversion(new ItemStack(chessboard), true, new GristSet(new GristType[] {GristType.Shale, GristType.Marble}, new int[] {25, 25}));
		GristRegistry.addGristConversion(new ItemStack(cruxiteApple, 1), false, new GristSet());
		GristRegistry.addGristConversion(new ItemStack(cruxitePotion, 1), false, new GristSet());
		

		GristRegistry.addGristConversion(new ItemStack(CatClaws),false,new GristSet(new GristType[] {GristType.Build,GristType.Rust},new int[] {15,5}));

		
		GristRegistry.addGristConversion(new ItemStack(clawHammer), false, new GristSet(GristType.Build, 8));
		GristRegistry.addGristConversion(new ItemStack(sledgeHammer), false, new GristSet(new GristType[] {GristType.Build, GristType.Shale}, new int[] {10, 4}));
		GristRegistry.addGristConversion(new ItemStack(blacksmithHammer), false, new GristSet(new GristType[] {GristType.Rust, GristType.Sulfur, GristType.Caulk}, new int[] {8, 9, 5}));
		GristRegistry.addGristConversion(new ItemStack(pogoHammer), false, new GristSet(new GristType[] {GristType.Build, GristType.Shale}, new int[] {20, 16}));
		GristRegistry.addGristConversion(new ItemStack(telescopicSassacrusher), false, new GristSet(new GristType[] {GristType.Shale, GristType.Tar, GristType.Mercury}, new int[] {39, 18, 23}));
		GristRegistry.addGristConversion(new ItemStack(fearNoAnvil), false, new GristSet(new GristType[] {GristType.Build, GristType.Garnet, GristType.Diamond, GristType.Gold, GristType.Quartz}, new int[] {999, 150, 54, 61, 1}));
		
		GristRegistry.addGristConversion(new ItemStack(cactusCutlass), false, new GristSet(new GristType[] {GristType.Amber, GristType.Marble}, new int[] {7, 2}));
		GristRegistry.addGristConversion(new ItemStack(sord), false, new GristSet(GristType.Build, 0));
		GristRegistry.addGristConversion(new ItemStack(katana), false, new GristSet(new GristType[] {GristType.Chalk, GristType.Quartz, GristType.Rust}, new int[] {12, 10, 6}));
		GristRegistry.addGristConversion(new ItemStack(firePoker), false, new GristSet(new GristType[] {GristType.Amber, GristType.Ruby, GristType.Sulfur, GristType.Gold}, new int[] {41, 14, 38, 3}));
		GristRegistry.addGristConversion(new ItemStack(hotHandle), false, new GristSet(new GristType[] {GristType.Amber, GristType.Ruby, GristType.Sulfur}, new int[] {10, 15, 10}));
		GristRegistry.addGristConversion(new ItemStack(regisword), false, new GristSet(new GristType[] {GristType.Amethyst, GristType.Tar,GristType.Gold}, new int[] {27, 62, 38}));
		GristRegistry.addGristConversion(new ItemStack(unbreakableKatana), false, new GristSet(new GristType[] {GristType.Build, GristType.Uranium, GristType.Quartz, GristType.Ruby}, new int[] {1100, 63, 115, 54}));
		GristRegistry.addGristConversion(new ItemStack(cobaltSabre), false, new GristSet(new GristType[] {GristType.Build, GristType.Uranium, GristType.Cobalt, GristType.Diamond}, new int[] {1300, 90, 175, 30}));
		
		GristRegistry.addGristConversion(new ItemStack(woodenSpoon), false, new GristSet(GristType.Build, 5));
		GristRegistry.addGristConversion(new ItemStack(silverSpoon), false, new GristSet(new GristType[] {GristType.Build, GristType.Mercury}, new int[] {6, 4}));
		GristRegistry.addGristConversion(new ItemStack(crockerSpork), false, new GristSet(new GristType[] {GristType.Build, GristType.Iodine, GristType.Chalk, GristType.Ruby}, new int[] {90, 34, 34, 6}));
		GristRegistry.addGristConversion(new ItemStack(skaiaFork), new GristSet(new GristType[] {GristType.Build, GristType.Quartz, GristType.Gold, GristType.Amethyst}, new int[]{900, 94, 58, 63}));
		
		GristRegistry.addGristConversion(new ItemStack(copseCrusher), false, new GristSet(new GristType[] {GristType.Build, GristType.Rust}, new int[] {25, 15}));
		GristRegistry.addGristConversion(new ItemStack(blacksmithBane), false, new GristSet (new GristType[] {GristType.Build, GristType.Rust, GristType.Tar}, new int[] {30, 15, 12}));
		GristRegistry.addGristConversion(new ItemStack(scraxe), false, new GristSet (new GristType[] {GristType.Build, GristType.Tar, GristType.Rust, GristType.Ruby}, new int[] {139, 86, 43, 8}));
		GristRegistry.addGristConversion(new ItemStack(qPHammerAxe), false, new GristSet(new GristType[] {GristType.Build, GristType.Shale, GristType.Rust}, new int[] {150, 64, 15}));
		GristRegistry.addGristConversion(new ItemStack(rubyCroak), false, new GristSet (new GristType[] {GristType.Build, GristType.Garnet, GristType.Ruby, GristType.Diamond}, new int [] {900, 103, 64, 16}));
		GristRegistry.addGristConversion(new ItemStack(hephaestusLumber), false, new GristSet (new GristType[] {GristType.Build, GristType.Gold, GristType.Ruby}, new int[] {625, 49, 36}));
		
		GristRegistry.addGristConversion(new ItemStack(regiSickle), false, new GristSet(new GristType[] {GristType.Amethyst, GristType.Tar, GristType.Gold}, new int[] {25, 57, 33}));
		GristRegistry.addGristConversion(new ItemStack(sickle), false, new GristSet(new GristType[] {GristType.Build}, new int[] {8}));
		GristRegistry.addGristConversion(new ItemStack(homesSmellYaLater), false, new GristSet(new GristType[] {GristType.Build, GristType.Amber, GristType.Amethyst}, new int[] {34, 19, 10}));
		GristRegistry.addGristConversion(new ItemStack(fudgeSickle), false, new GristSet(new GristType[] {GristType.Iodine, GristType.Amber, GristType.Chalk}, new int[] {23, 15, 12}));
		GristRegistry.addGristConversion(new ItemStack(candySickle), false, new GristSet(new GristType[] {GristType.Iodine, GristType.Gold, GristType.Chalk, GristType.Amber}, new int[] {65, 38, 53, 20}));
		
		GristRegistry.addGristConversion(new ItemStack(nightClub), false, new GristSet(new GristType[] {GristType.Tar, GristType.Shale, GristType.Cobalt}, new int[] {28, 19, 6}));
		GristRegistry.addGristConversion(new ItemStack(pogoClub), false, new GristSet(new GristType[] {GristType.Build, GristType.Shale}, new int[] {15, 12}));
		GristRegistry.addGristConversion(new ItemStack(metalBat), false, new GristSet(new GristType[] {GristType.Build, GristType.Mercury}, new int[] {35, 23}));
		GristRegistry.addGristConversion(new ItemStack(spikedClub), false, new GristSet(new GristType[] {GristType.Build, GristType.Garnet, GristType.Iodine}, new int[] {46, 38, 13}));
		
		GristRegistry.addGristConversion(new ItemStack(spearCane), false, new GristSet(new GristType[] {GristType.Build, GristType.Mercury, GristType.Amber}, new int[] {28, 14, 11}));
		GristRegistry.addGristConversion(new ItemStack(transportalizer), false, new GristSet(new GristType[] {GristType.Build, GristType.Amethyst, GristType.Rust, GristType.Uranium}, new int[] {350, 27, 36, 18}));
		GristRegistry.addGristConversion(new ItemStack(modusCard, 1, 2), true, new GristSet(GristType.Build, 140));
		GristRegistry.addGristConversion(new ItemStack(modusCard, 1, 3), true, new GristSet(new GristType[] {GristType.Build, GristType.Amber}, new int[] {400, 35}));
		GristRegistry.addGristConversion(new ItemStack(modusCard, 1, 4), true, new GristSet(new GristType[] {GristType.Build, GristType.Ruby}, new int[] {280, 23}));
		GristRegistry.addGristConversion(new ItemStack(modusCard, 1, 5), true, new GristSet(new GristType[] {GristType.Build, GristType.Mercury}, new int[] {350, 29}));
		GristRegistry.addGristConversion(new ItemStack(metalBoat, 1, 0), true, new GristSet(GristType.Rust, 30));
		GristRegistry.addGristConversion(new ItemStack(metalBoat, 1, 1), true, new GristSet(GristType.Gold, 30));
		GristRegistry.addGristConversion(new ItemStack(layeredSand), new GristSet(GristType.Build, 1));
		GristRegistry.addGristConversion(new ItemStack(minestuckBucket, 1, 0), true, new GristSet(new GristType[] {GristType.Rust, GristType.Tar, GristType.Shale}, new int[] {27, 8, 8}));
		GristRegistry.addGristConversion(new ItemStack(minestuckBucket, 1, 1), true, new GristSet(new GristType[] {GristType.Rust, GristType.Garnet, GristType.Iodine}, new int[] {27, 8, 8}));
		GristRegistry.addGristConversion(new ItemStack(minestuckBucket, 1, 2), true, new GristSet(new GristType[] {GristType.Rust, GristType.Amethyst, GristType.Chalk}, new int[] {27, 8, 8}));
		GristRegistry.addGristConversion(new ItemStack(obsidianBucket), new GristSet(new GristType[] {GristType.Rust, GristType.Cobalt, GristType.Tar, GristType.Build}, new int[] {27, 8, 16, 4}));
		GristRegistry.addGristConversion(new ItemStack(glowingMushroom), new GristSet(new GristType[] {GristType.Build, GristType.Shale, GristType.Mercury}, new int[] {5, 3, 2}));
		GristRegistry.addGristConversion(new ItemStack(glowingLog), new GristSet(new GristType[] {GristType.Build, GristType.Amber, GristType.Mercury}, new int[] {8, 4, 4}));
		GristRegistry.addGristConversion(new ItemStack(glowingPlanks), new GristSet(new GristType[] {GristType.Build, GristType.Amber, GristType.Mercury}, new int[] {2, 1, 1}));
		GristRegistry.addGristConversion(new ItemStack(goldSeeds), new GristSet(GristType.Gold, 3));
		GristRegistry.addGristConversion(new ItemStack(emeraldSword), false, new GristSet(new GristType[] {GristType.Quartz, GristType.Diamond, GristType.Ruby}, new int[] {44, 76, 72}));
		GristRegistry.addGristConversion(new ItemStack(emeraldAxe), false, new GristSet(new GristType[] {GristType.Amber, GristType.Diamond, GristType.Ruby}, new int[] {40, 73, 70}));
		GristRegistry.addGristConversion(new ItemStack(emeraldPickaxe), false, new GristSet(new GristType[] {GristType.Rust, GristType.Diamond, GristType.Ruby}, new int[] {42, 72, 70}));
		GristRegistry.addGristConversion(new ItemStack(emeraldShovel), false, new GristSet(new GristType[] {GristType.Chalk, GristType.Diamond, GristType.Ruby}, new int[] {40, 70, 66}));
		GristRegistry.addGristConversion(new ItemStack(emeraldHoe), false, new GristSet(new GristType[] {GristType.Iodine, GristType.Diamond, GristType.Ruby}, new int[] {32, 50, 45}));
		GristRegistry.addGristConversion(new ItemStack(prismarineHelmet), new GristSet(new GristType[] {GristType.Build, GristType.Cobalt, GristType.Marble}, new int[] {75, 30, 15}));
		GristRegistry.addGristConversion(new ItemStack(prismarineChestplate), new GristSet(new GristType[] {GristType.Build, GristType.Cobalt, GristType.Marble}, new int[] {120, 48, 24}));
		GristRegistry.addGristConversion(new ItemStack(prismarineLeggings), new GristSet(new GristType[] {GristType.Build, GristType.Cobalt, GristType.Marble}, new int[] {105, 42, 21}));
		GristRegistry.addGristConversion(new ItemStack(prismarineBoots), new GristSet(new GristType[] {GristType.Build, GristType.Cobalt, GristType.Marble}, new int[] {60, 24, 12}));
		GristRegistry.addGristConversion(new ItemStack(glowystoneDust), new GristSet(new GristType[] {GristType.Build, GristType.Amber}, new int[] {1, 1}));
		GristRegistry.addGristConversion(new ItemStack(rawCruxite), new GristSet(GristType.Build, 3));

		GristRegistry.addGristConversion(new ItemStack(spork), new GristSet(new GristType[]{GristType.Build}, new int[]{13}));
		GristRegistry.addGristConversion(new ItemStack(candy, 1, 0), new GristSet(new GristType[] {GristType.Chalk, GristType.Sulfur, GristType.Iodine}, new int[] {1, 1, 1}));
		for(int i = 0; i < 21; i++)
			GristRegistry.addGristConversion(new ItemStack(candy, 1, i+1), new GristSet(GristType.values()[i], 3));
		GristRegistry.addGristConversion(new ItemStack(salad), new GristSet(new GristType[]{GristType.Build,GristType.Iodine}, new int[]{1, 3}));
		GristRegistry.addGristConversion(new ItemStack(bugOnAStick), new GristSet(new GristType[] {GristType.Build, GristType.Chalk}, new int[] {1, 1}));
		GristRegistry.addGristConversion(new ItemStack(chocolateBeetle), new GristSet(new GristType[] {GristType.Chalk, GristType.Iodine}, new int[] {2, 4}));
		GristRegistry.addGristConversion(new ItemStack(coneOfFlies), new GristSet(new GristType[] {GristType.Build, GristType.Amber}, new int[] {2, 2}));
		GristRegistry.addGristConversion(new ItemStack(grasshopper), new GristSet(new GristType[] {GristType.Iodine, GristType.Amber}, new int[] {3, 7}));
		GristRegistry.addGristConversion(new ItemStack(jarOfBugs), new GristSet(new GristType[] {GristType.Build, GristType.Chalk}, new int[] {5, 3}));
		GristRegistry.addGristConversion(new ItemStack(onion), new GristSet(new GristType[] {GristType.Iodine}, new int[] {3}));
		GristRegistry.addGristConversion(primedTnt, new GristSet(new GristType[] {GristType.Build, GristType.Chalk, GristType.Sulfur}, new int[] {8, 10, 14}));
		GristRegistry.addGristConversion(unstableTnt, new GristSet(new GristType[] {GristType.Build, GristType.Chalk, GristType.Sulfur}, new int[] {5, 11, 15}));
		GristRegistry.addGristConversion(instantTnt, new GristSet(new GristType[] {GristType.Build, GristType.Chalk, GristType.Sulfur}, new int[] {6, 11, 17}));
		GristRegistry.addGristConversion(stoneExplosiveButton, new GristSet(new GristType[] {GristType.Build, GristType.Chalk, GristType.Sulfur}, new int[] {7, 5, 8}));
		GristRegistry.addGristConversion(woodenExplosiveButton, new GristSet(new GristType[] {GristType.Build, GristType.Chalk, GristType.Sulfur}, new int[] {7, 5, 8}));
		GristRegistry.addGristConversion(new ItemStack(stone, 1, 0), new GristSet(GristType.Build, 4));
		GristRegistry.addGristConversion(new ItemStack(stone, 1, 1), new GristSet(GristType.Build, 4));
		GristRegistry.addGristConversion(new ItemStack(stone, 1, 2), new GristSet(new GristType[] {GristType.Build, GristType.Shale}, new int[] {2, 1}));
		GristRegistry.addGristConversion(new ItemStack(stone, 1, 3), new GristSet(new GristType[] {GristType.Build, GristType.Shale}, new int[] {2, 1}));
		GristRegistry.addGristConversion(new ItemStack(stone, 1, 4), new GristSet(new GristType[] {GristType.Build, GristType.Cobalt}, new int[] {2, 1}));
		GristRegistry.addGristConversion(new ItemStack(stone, 1, 5), new GristSet(new GristType[] {GristType.Build, GristType.Cobalt}, new int[] {2, 1}));
		GristRegistry.addGristConversion(new ItemStack(stone, 1, 6), new GristSet(new GristType[] {GristType.Build, GristType.Cobalt}, new int[] {3, 1}));
		GristRegistry.addGristConversion(new ItemStack(stone, 1, 7), new GristSet(new GristType[] {GristType.Build, GristType.Rust}, new int[] {3, 2}));
		GristRegistry.addGristConversion(new ItemStack(stone, 1, 8), new GristSet(new GristType[] {GristType.Build, GristType.Rust}, new int[] {3, 2}));
		GristRegistry.addGristConversion(new ItemStack(log, 1, 0), new GristSet(new GristType[] {GristType.Build}, new int[] {4}));
		GristRegistry.addGristConversion(new ItemStack(log, 1, 1), new GristSet(new GristType[] {GristType.Build, GristType.Iodine}, new int[] {4, 1}));
		GristRegistry.addGristConversion(new ItemStack(sbahjPoster), new GristSet(new GristType[] {GristType.Build}, new int[] {4}));
		GristRegistry.addGristConversion(new ItemStack(crewPoster), new GristSet(new GristType[] {GristType.Tar, GristType.Rust}, new int[] {3, 2}));
		GristRegistry.addGristConversion(new ItemStack(threshDvd), new GristSet(new GristType[] {GristType.Iodine, GristType.Amethyst}, new int[] {3, 2}));
		GristRegistry.addGristConversion(new ItemStack(carvingTool), new GristSet(new GristType[] {GristType.Build, GristType.Rust}, new int[] {10, 2}));
		GristRegistry.addGristConversion(new ItemStack(crumplyHat), new GristSet(new GristType[] {GristType.Build}, new int[] {20}));
		GristRegistry.addGristConversion(new ItemStack(frogStatueReplica), new GristSet(new GristType[] {GristType.Build}, new int[] {30}));
		GristRegistry.addGristConversion(new ItemStack(stoneSlab), new GristSet(new GristType[] {GristType.Build}, new int[] {5}));
		GristRegistry.addGristConversion(woodenCactus, new GristSet(GristType.Build, 7));
		
		//add Designix combinations
		
		//swords
		CombinationRegistry.addCombination(new ItemStack(Items.WOODEN_SWORD), new ItemStack(Blocks.CACTUS), MODE_AND, false, true, new ItemStack(cactusCutlass));
		CombinationRegistry.addCombination(new ItemStack(Items.WOODEN_SWORD), new ItemStack(sbahjPoster), MODE_AND, false, true, new ItemStack(sord));
		CombinationRegistry.addCombination(new ItemStack(Items.STONE_SWORD), new ItemStack(sbahjPoster), MODE_AND, false, true, new ItemStack(sord));
		CombinationRegistry.addCombination(new ItemStack(Items.STONE_SWORD), new ItemStack(Items.ROTTEN_FLESH), MODE_AND, false, true, new ItemStack(katana));
		CombinationRegistry.addCombination(new ItemStack(Items.IRON_SWORD), new ItemStack(Items.ROTTEN_FLESH), MODE_AND, false, true, new ItemStack(katana));
		CombinationRegistry.addCombination(new ItemStack(Items.IRON_SWORD), new ItemStack(Items.BLAZE_ROD), MODE_AND, false, true, new ItemStack(firePoker));
		CombinationRegistry.addCombination(new ItemStack(Items.IRON_SWORD), new ItemStack(Items.BLAZE_ROD), MODE_OR, false, true, new ItemStack(hotHandle));
		CombinationRegistry.addCombination(new ItemStack(Items.IRON_SWORD), new ItemStack(chessboard), MODE_AND, false, true, new ItemStack(regisword));
		CombinationRegistry.addCombination(new ItemStack(katana), new ItemStack(chessboard), MODE_AND, false, true, new ItemStack(regisword));
		CombinationRegistry.addCombination(new ItemStack(katana), new ItemStack(Blocks.OBSIDIAN), MODE_AND, new ItemStack(unbreakableKatana));
		CombinationRegistry.addCombination(new ItemStack(hotHandle), new ItemStack(Blocks.LAPIS_BLOCK), MODE_OR, new ItemStack(cobaltSabre));
		
		//axes
		CombinationRegistry.addCombination(new ItemStack(Items.IRON_AXE), new ItemStack(Blocks.PISTON), MODE_AND, false, true, new ItemStack(copseCrusher));
		CombinationRegistry.addCombination(new ItemStack(Items.WOODEN_AXE), new ItemStack(Blocks.ANVIL), MODE_AND, false, true, new ItemStack(blacksmithBane));
		CombinationRegistry.addCombination("record", Items.IRON_AXE, OreDictionary.WILDCARD_VALUE, MODE_AND, new ItemStack(scraxe));
		CombinationRegistry.addCombination(new ItemStack(copseCrusher), new ItemStack(pogoHammer), MODE_AND, false, true, new ItemStack(qPHammerAxe));
		CombinationRegistry.addCombination(new ItemStack(Items.IRON_AXE), new ItemStack(Blocks.REDSTONE_BLOCK), MODE_AND, false, true, new ItemStack(rubyCroak));
		CombinationRegistry.addCombination(new ItemStack(Items.GOLDEN_AXE), new ItemStack(Items.LAVA_BUCKET), MODE_AND, false, true, new ItemStack(hephaestusLumber));
		
		//sickles
		CombinationRegistry.addCombination(new ItemStack(Items.IRON_HOE), new ItemStack(Items.WHEAT), MODE_AND, false, true, new ItemStack(sickle));
		CombinationRegistry.addCombination(new ItemStack(sickle), new ItemStack(threshDvd), MODE_OR, false, true, new ItemStack(homesSmellYaLater));
		CombinationRegistry.addCombination(new ItemStack(sickle), new ItemStack(Items.DYE,1,3), MODE_OR, false, true, new ItemStack (fudgeSickle));
		CombinationRegistry.addCombination(new ItemStack(sickle), new ItemStack(chessboard), MODE_AND, false, true, new ItemStack(regiSickle));
		CombinationRegistry.addCombination(new ItemStack(sickle), new ItemStack(candy, 1, 0), MODE_OR, false, true, new ItemStack(candySickle));
		CombinationRegistry.addCombination(new ItemStack(fudgeSickle), new ItemStack(Items.SUGAR), MODE_AND, false, true, new ItemStack(candySickle));
		
		//clubs
		CombinationRegistry.addCombination(new ItemStack(deuceClub), new ItemStack(Items.SLIME_BALL), MODE_AND, false, true, new ItemStack(pogoClub));
		CombinationRegistry.addCombination(new ItemStack(deuceClub), new ItemStack(crewPoster), MODE_AND, false, true, new ItemStack(nightClub));
		CombinationRegistry.addCombination(new ItemStack(deuceClub), new ItemStack(Items.IRON_INGOT), MODE_AND, false, true, new ItemStack(metalBat));
		CombinationRegistry.addCombination("logWood", metalBat, OreDictionary.WILDCARD_VALUE, MODE_OR, new ItemStack(spikedClub));
		
		//hammers
		CombinationRegistry.addCombination(new ItemStack(clawHammer), new ItemStack(Blocks.BRICK_BLOCK), MODE_AND, false, false, new ItemStack(sledgeHammer));
		CombinationRegistry.addCombination(new ItemStack(clawHammer), new ItemStack(Blocks.COBBLESTONE), MODE_AND, false, false, new ItemStack(sledgeHammer));
		CombinationRegistry.addCombination(new ItemStack(Blocks.ANVIL), new ItemStack(sledgeHammer), MODE_AND, false, false, new ItemStack(blacksmithHammer));
		CombinationRegistry.addCombination(new ItemStack(Blocks.IRON_BLOCK), new ItemStack(sledgeHammer), MODE_AND, false, false, new ItemStack(blacksmithHammer));
		CombinationRegistry.addCombination(new ItemStack(Items.SLIME_BALL), new ItemStack(sledgeHammer), MODE_AND, false, false, new ItemStack(pogoHammer));
		CombinationRegistry.addCombination(new ItemStack(blacksmithHammer), new ItemStack(Items.CLOCK), MODE_OR, false, false, new ItemStack(fearNoAnvil));
		CombinationRegistry.addCombination(new ItemStack(sledgeHammer), new ItemStack(Items.BOOK), MODE_AND, false, false, new ItemStack(telescopicSassacrusher));
		
		//canes
		CombinationRegistry.addCombination(new ItemStack(cane), new ItemStack(Items.STONE_SWORD), MODE_OR, false, false, new ItemStack(spearCane));
		CombinationRegistry.addCombination(new ItemStack(cane), new ItemStack(Items.IRON_SWORD), MODE_OR, false, false, new ItemStack(spearCane));
		CombinationRegistry.addCombination(new ItemStack(cane), new ItemStack(katana), MODE_OR, false, false, new ItemStack(spearCane));
		
		//spoons/sporks/forks
		CombinationRegistry.addCombination(new ItemStack(woodenSpoon), new ItemStack(Items.IRON_INGOT), MODE_AND, false, false, new ItemStack(silverSpoon));
		CombinationRegistry.addCombination(new ItemStack(silverSpoon), new ItemStack(Items.CAKE), MODE_AND, false, false, new ItemStack(crockerSpork));
		
		CombinationRegistry.addCombination(new ItemStack(crockerSpork), new ItemStack(captchaCard), MODE_OR, false, true, new ItemStack(crockerMachine, 1, 0));
		CombinationRegistry.addCombination(new ItemStack(Items.ENDER_PEARL), new ItemStack(Blocks.IRON_BLOCK), MODE_AND, false, false, new ItemStack(transportalizer));
		
		CombinationRegistry.addCombination(new ItemStack(modusCard, 1, 0), new ItemStack(modusCard, 1, 1), MODE_AND, true, true, new ItemStack(modusCard, 1, 2));
		CombinationRegistry.addCombination("stickWood", modusCard, OreDictionary.WILDCARD_VALUE, MODE_OR, new ItemStack(modusCard, 1, 3));
		CombinationRegistry.addCombination("treeSapling", modusCard, OreDictionary.WILDCARD_VALUE, MODE_OR, new ItemStack(modusCard, 1, 3));
		CombinationRegistry.addCombination("treeLeaves", modusCard, OreDictionary.WILDCARD_VALUE, MODE_OR, new ItemStack(modusCard, 1, 3));	//Not planks and logs though. Too little branch-related.
		CombinationRegistry.addCombination(new ItemStack(modusCard), new ItemStack(blockComputerOff), MODE_AND, false, true, new ItemStack(modusCard, 1, 4));
		CombinationRegistry.addCombination(new ItemStack(modusCard), new ItemStack(Items.ITEM_FRAME), MODE_AND, false, true, new ItemStack(modusCard, 1, 5));
		CombinationRegistry.addCombination(new ItemStack(Blocks.IRON_BARS),new ItemStack(Items.LEATHER), MODE_AND, false, true, new ItemStack(CatClaws));
		CombinationRegistry.addCombination(new ItemStack(Items.WHEAT_SEEDS), new ItemStack(Items.GOLD_NUGGET), MODE_AND, new ItemStack(goldSeeds));
		CombinationRegistry.addCombination(new ItemStack(Items.WHEAT_SEEDS), new ItemStack(Items.GOLD_INGOT), MODE_AND, new ItemStack(goldSeeds));
		CombinationRegistry.addCombination(new ItemStack(Items.BOAT), new ItemStack(Items.MINECART), MODE_OR, new ItemStack(metalBoat, 1, 0));
		CombinationRegistry.addCombination(new ItemStack(Items.BOAT), new ItemStack(Items.IRON_INGOT), MODE_AND, new ItemStack(metalBoat, 1, 0));
		CombinationRegistry.addCombination(new ItemStack(Items.BOAT), new ItemStack(Blocks.IRON_BLOCK), MODE_AND, new ItemStack(metalBoat, 1, 0));
		CombinationRegistry.addCombination(new ItemStack(Items.BOAT), new ItemStack(Items.GOLD_INGOT), MODE_AND, new ItemStack(metalBoat, 1, 1));
		CombinationRegistry.addCombination(new ItemStack(Items.BOAT), new ItemStack(Blocks.GOLD_BLOCK), MODE_AND, new ItemStack(metalBoat, 1, 1));
		CombinationRegistry.addCombination(new ItemStack(Blocks.DIRT), new ItemStack(Items.DYE, 1, EnumDyeColor.BLUE.getDyeDamage()), MODE_OR, new ItemStack(coloredDirt, 1, 0));
		CombinationRegistry.addCombination(new ItemStack(Blocks.DIRT), new ItemStack(Items.DYE, 1, EnumDyeColor.LIME.getDyeDamage()), MODE_OR, new ItemStack(coloredDirt, 1, 1));
		CombinationRegistry.addCombination(new ItemStack(Blocks.SAND), new ItemStack(Blocks.SNOW_LAYER), MODE_OR, new ItemStack(layeredSand));
		CombinationRegistry.addCombination(new ItemStack(Items.WATER_BUCKET), new ItemStack(Items.LAVA_BUCKET), MODE_OR, new ItemStack(obsidianBucket));	//water_bucket && lava bucket could make a bucket with liquid obsidian? (from a mod that adds liquid obsidian)
		CombinationRegistry.addCombination(new ItemStack(Items.BUCKET), new ItemStack(Blocks.OBSIDIAN), MODE_AND, new ItemStack(obsidianBucket));	//bucket || obsidian could make a bucket made out of obsidian
		CombinationRegistry.addCombination(new ItemStack(Blocks.BROWN_MUSHROOM), new ItemStack(Items.GLOWSTONE_DUST), MODE_OR, new ItemStack(glowingMushroom));
		CombinationRegistry.addCombination(new ItemStack(Blocks.LOG), new ItemStack(glowingMushroom), MODE_OR, false, true, new ItemStack(glowingLog));
		CombinationRegistry.addCombination(new ItemStack(Blocks.LOG2), new ItemStack(glowingMushroom), MODE_OR, false, true, new ItemStack(glowingLog));
		CombinationRegistry.addCombination(new ItemStack(Blocks.PLANKS), new ItemStack(glowingMushroom), MODE_OR, false, true, new ItemStack(glowingPlanks));
		CombinationRegistry.addCombination(new ItemStack(Items.SUGAR), new ItemStack(Items.WHEAT_SEEDS), MODE_AND, new ItemStack(candy, 1, 0));
		CombinationRegistry.addCombination(new ItemStack(Items.WOODEN_SHOVEL), new ItemStack(Items.BOWL), MODE_AND, new ItemStack(woodenSpoon));
		CombinationRegistry.addCombination(new ItemStack(Items.WOODEN_SHOVEL), new ItemStack(Items.MUSHROOM_STEW), MODE_AND, new ItemStack(woodenSpoon));
		CombinationRegistry.addCombination(new ItemStack(Items.WOODEN_SHOVEL), new ItemStack(Items.RABBIT_STEW), MODE_AND, new ItemStack(woodenSpoon));
		CombinationRegistry.addCombination(new ItemStack(Items.WOODEN_SHOVEL), new ItemStack(Items.BEETROOT_SOUP), MODE_AND, new ItemStack(woodenSpoon));
		CombinationRegistry.addCombination(new ItemStack(Items.IRON_SHOVEL), new ItemStack(Items.BOWL), MODE_AND, new ItemStack(silverSpoon));
		CombinationRegistry.addCombination(new ItemStack(Items.IRON_SHOVEL), new ItemStack(Items.MUSHROOM_STEW), MODE_AND, new ItemStack(silverSpoon));
		CombinationRegistry.addCombination(new ItemStack(Items.IRON_SHOVEL), new ItemStack(Items.RABBIT_STEW), MODE_AND, new ItemStack(silverSpoon));
		CombinationRegistry.addCombination(new ItemStack(Items.IRON_SHOVEL), new ItemStack(Items.BEETROOT_SOUP), MODE_AND, new ItemStack(silverSpoon));
		
		CombinationRegistry.addCombination(new ItemStack(Items.COAL), new ItemStack(Blocks.NETHERRACK), MODE_AND, new ItemStack(coalOreNetherrack));
		CombinationRegistry.addCombination(new ItemStack(Items.IRON_INGOT), new ItemStack(Blocks.SANDSTONE), MODE_AND, new ItemStack(ironOreSandstone));
		CombinationRegistry.addCombination(new ItemStack(Items.IRON_INGOT), new ItemStack(Blocks.RED_SANDSTONE), MODE_AND, new ItemStack(ironOreSandstoneRed));
		CombinationRegistry.addCombination(new ItemStack(Items.GOLD_INGOT), new ItemStack(Blocks.SANDSTONE), MODE_AND, new ItemStack(goldOreSandstone));
		CombinationRegistry.addCombination(new ItemStack(Items.GOLD_INGOT), new ItemStack(Blocks.RED_SANDSTONE), MODE_AND, new ItemStack(goldOreSandstoneRed));
		
		CombinationRegistry.addCombination(new ItemStack(Items.DIAMOND_SWORD), new ItemStack(Items.EMERALD), MODE_OR, false, false, new ItemStack(emeraldSword));
		CombinationRegistry.addCombination(new ItemStack(Items.DIAMOND_AXE), new ItemStack(Items.EMERALD), MODE_OR, false, false, new ItemStack(emeraldAxe));
		CombinationRegistry.addCombination(new ItemStack(Items.DIAMOND_PICKAXE), new ItemStack(Items.EMERALD), MODE_OR, false, false, new ItemStack(emeraldPickaxe));
		CombinationRegistry.addCombination(new ItemStack(Items.DIAMOND_SHOVEL), new ItemStack(Items.EMERALD), MODE_OR, false, false, new ItemStack(emeraldShovel));
		CombinationRegistry.addCombination(new ItemStack(Items.DIAMOND_HOE), new ItemStack(Items.EMERALD), MODE_OR, false, false, new ItemStack(emeraldHoe));
		
		ItemArmor[] metalHelmets = new ItemArmor[] {Items.IRON_HELMET, Items.GOLDEN_HELMET, Items.DIAMOND_HELMET};
		ItemArmor[] metalChestplates = new ItemArmor[] {Items.IRON_CHESTPLATE, Items.GOLDEN_CHESTPLATE, Items.DIAMOND_CHESTPLATE};
		ItemArmor[] metalLeggings = new ItemArmor[] {Items.IRON_LEGGINGS, Items.GOLDEN_LEGGINGS, Items.DIAMOND_LEGGINGS};
		ItemArmor[] metalBoots = new ItemArmor[] {Items.IRON_BOOTS, Items.GOLDEN_BOOTS, Items.DIAMOND_BOOTS};
		for(int i = 0; i < metalHelmets.length; i++)	//Two out of three possible for-loops is enough for me
			for(ItemStack prismarine : new ItemStack[]{new ItemStack(Items.PRISMARINE_SHARD), new ItemStack(Blocks.PRISMARINE)})
			{
				CombinationRegistry.addCombination(new ItemStack(metalHelmets[i]), prismarine, MODE_OR, false, false, new ItemStack(prismarineHelmet));
				CombinationRegistry.addCombination(new ItemStack(metalChestplates[i]), prismarine, MODE_OR, false, false, new ItemStack(prismarineChestplate));
				CombinationRegistry.addCombination(new ItemStack(metalLeggings[i]), prismarine, MODE_OR, false, false, new ItemStack(prismarineLeggings));
				CombinationRegistry.addCombination(new ItemStack(metalBoots[i]), prismarine, MODE_OR, false, false, new ItemStack(prismarineBoots));
			}
		
		CombinationRegistry.addCombination(new ItemStack(fork), new ItemStack(chessboard), MODE_AND, false, true, new ItemStack(skaiaFork));
		CombinationRegistry.addCombination(new ItemStack(fork), new ItemStack(woodenSpoon), MODE_OR, false, false, new ItemStack(spork));
		CombinationRegistry.addCombination(new ItemStack(Blocks.TNT), new ItemStack(Blocks.STONE_BUTTON), MODE_OR, new ItemStack(primedTnt));
		CombinationRegistry.addCombination(new ItemStack(Blocks.TNT), new ItemStack(Blocks.WOODEN_BUTTON), MODE_OR, new ItemStack(primedTnt));
		CombinationRegistry.addCombination(new ItemStack(Blocks.TNT), new ItemStack(Blocks.REDSTONE_TORCH), MODE_OR, new ItemStack(unstableTnt));
		CombinationRegistry.addCombination(new ItemStack(Blocks.TNT), new ItemStack(Items.POTIONITEM, 1, 8236), MODE_OR, true, true, new ItemStack(instantTnt));	//Instant damage potions
		CombinationRegistry.addCombination(new ItemStack(Blocks.TNT), new ItemStack(Items.POTIONITEM, 1, 8268), MODE_OR, true, true, new ItemStack(instantTnt));
		CombinationRegistry.addCombination(new ItemStack(Blocks.TNT), new ItemStack(Blocks.STONE_BUTTON), MODE_AND, new ItemStack(stoneExplosiveButton));
		CombinationRegistry.addCombination(new ItemStack(instantTnt), new ItemStack(Blocks.STONE_BUTTON), MODE_AND, new ItemStack(stoneExplosiveButton));
		CombinationRegistry.addCombination(new ItemStack(Blocks.TNT), new ItemStack(Blocks.WOODEN_BUTTON), MODE_AND, new ItemStack(woodenExplosiveButton));
		CombinationRegistry.addCombination(new ItemStack(instantTnt), new ItemStack(Blocks.WOODEN_BUTTON), MODE_AND, new ItemStack(woodenExplosiveButton));
		CombinationRegistry.addCombination(new ItemStack(Blocks.GRAVEL), new ItemStack(Blocks.STONE), MODE_AND, new ItemStack(stone, 1, BlockMinestuckStone.COARSE_META));
		CombinationRegistry.addCombination(new ItemStack(Blocks.GRAVEL), new ItemStack(Blocks.STONEBRICK, 1, BlockStoneBrick.CHISELED_META), MODE_AND, new ItemStack(stone, 1, BlockMinestuckStone.COARSE_CHISELED_META));
		CombinationRegistry.addCombination(new ItemStack(stone, 1, BlockMinestuckStone.COARSE_META), new ItemStack(Blocks.STONEBRICK, 1, BlockStoneBrick.CHISELED_META), MODE_AND, new ItemStack(stone, 1, BlockMinestuckStone.COARSE_CHISELED_META));
		CombinationRegistry.addCombination(new ItemStack(Blocks.STONEBRICK), new ItemStack(coloredDirt, 1, 0), MODE_OR, new ItemStack(stone, 1, BlockMinestuckStone.SHADE_META));
		CombinationRegistry.addCombination(new ItemStack(Blocks.STONE_BRICK_STAIRS), new ItemStack(coloredDirt, 1, 0), MODE_OR, new ItemStack(shadeBrickStairs));
		CombinationRegistry.addCombination(new ItemStack(Blocks.STONEBRICK), new ItemStack(Items.DYE, 1, EnumDyeColor.BLUE.getDyeDamage()), MODE_OR, new ItemStack(stone, 1, BlockMinestuckStone.SHADE_META));
		CombinationRegistry.addCombination(new ItemStack(Blocks.STONE_BRICK_STAIRS), new ItemStack(Items.DYE, 1, EnumDyeColor.BLUE.getDyeDamage()), MODE_OR, new ItemStack(shadeBrickStairs));
		for(int i = 0; i <= 6; i+=2)	//Stone and polished stone
		{
			CombinationRegistry.addCombination(new ItemStack(Blocks.STONE, 1, i), new ItemStack(coloredDirt, 1, 0), MODE_OR, new ItemStack(stone, 1, BlockMinestuckStone.SHADE_SMOOTH_META));
			CombinationRegistry.addCombination(new ItemStack(Blocks.STONE, 1, i), new ItemStack(Items.DYE, 1, EnumDyeColor.BLUE.getDyeDamage()), MODE_OR, new ItemStack(stone, 1, BlockMinestuckStone.SHADE_SMOOTH_META));
			CombinationRegistry.addCombination(new ItemStack(Blocks.STONE, 1, i), new ItemStack(Blocks.ICE), MODE_AND, new ItemStack(stone, 1, BlockMinestuckStone.FROST_TILE_META));
			CombinationRegistry.addCombination(new ItemStack(Blocks.STONE, 1, i), new ItemStack(Blocks.PACKED_ICE), MODE_AND, new ItemStack(stone, 1, BlockMinestuckStone.FROST_TILE_META));
		}
		CombinationRegistry.addCombination(new ItemStack(Blocks.STONEBRICK), new ItemStack(Blocks.ICE), MODE_AND, new ItemStack(stone, 1, BlockMinestuckStone.FROST_META));
		CombinationRegistry.addCombination(new ItemStack(Blocks.STONEBRICK), new ItemStack(Blocks.PACKED_ICE), MODE_AND, new ItemStack(stone, 1, BlockMinestuckStone.FROST_META));
		CombinationRegistry.addCombination(new ItemStack(Blocks.STONEBRICK, 1, BlockStoneBrick.CHISELED_META), new ItemStack(Blocks.ICE), MODE_AND, new ItemStack(stone, 1, BlockMinestuckStone.FROST_CHISELED_META));
		CombinationRegistry.addCombination(new ItemStack(Blocks.STONEBRICK, 1, BlockStoneBrick.CHISELED_META), new ItemStack(Blocks.PACKED_ICE), MODE_AND, new ItemStack(stone, 1, BlockMinestuckStone.FROST_CHISELED_META));
		CombinationRegistry.addCombination(new ItemStack(Blocks.IRON_BLOCK), new ItemStack(Items.LAVA_BUCKET), MODE_AND, new ItemStack(stone, 1, BlockMinestuckStone.CAST_META));
		CombinationRegistry.addCombination(new ItemStack(Blocks.STONEBRICK, 1, BlockStoneBrick.CHISELED_META), new ItemStack(stone, 1, BlockMinestuckStone.CAST_META), MODE_OR, new ItemStack(stone, 1, BlockMinestuckStone.CAST_CHISELED_META));
		CombinationRegistry.addCombination(new ItemStack(Blocks.LOG, 1, 0), new ItemStack(Blocks.VINE), MODE_AND, new ItemStack(log, 1, 0));
		CombinationRegistry.addCombination(new ItemStack(log, 1, 0), new ItemStack(Blocks.YELLOW_FLOWER), MODE_OR, true, false, new ItemStack(log, 1, 1));
		CombinationRegistry.addCombination(new ItemStack(log, 1, 0), new ItemStack(Blocks.RED_FLOWER), MODE_OR, true, false, new ItemStack(log, 1, 1));
		CombinationRegistry.addCombination(new ItemStack(Items.WOODEN_SWORD), new ItemStack(Blocks.CACTUS), MODE_OR, false, true, new ItemStack(woodenCactus));
		CombinationRegistry.addCombination(new ItemStack(Blocks.PLANKS), new ItemStack(Blocks.CACTUS), MODE_OR, false, true, new ItemStack(woodenCactus));
		CombinationRegistry.addCombination(new ItemStack(Blocks.LOG), new ItemStack(Blocks.CACTUS), MODE_OR, false, true, new ItemStack(woodenCactus));
		CombinationRegistry.addCombination(new ItemStack(Blocks.LOG2), new ItemStack(Blocks.CACTUS), MODE_OR, false, true, new ItemStack(woodenCactus));
		CombinationRegistry.addCombination(new ItemStack(Blocks.STONE), new ItemStack(carvingTool), MODE_AND, false, true, new ItemStack(stoneSlab));
		
		//Register chest loot
		LootConditionManager.registerCondition(new LandAspectLootCondition.Serializer());
		/*if(MinestuckConfig.cardLoot)
		{
			ChestGenHooks.getInfo(ChestGenHooks.DUNGEON_CHEST).addItem(new WeightedRandomChestContent(captchaCard, 0, 1, 3, 10));
			ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CROSSING).addItem(new WeightedRandomChestContent(captchaCard, 0, 1, 2, 8));
			ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CORRIDOR).addItem(new WeightedRandomChestContent(captchaCard, 0, 1, 4, 10));
		}*/
		
	}
	
	public static void registerModRecipes() 
	{
		
		GristRegistry.addGristConversion("ingotCopper", new GristSet(new GristType[] {GristType.Rust, GristType.Cobalt}, new int[] {16, 3}));
		GristRegistry.addGristConversion("oreCopper", new GristSet(new GristType[] {GristType.Rust, GristType.Cobalt, GristType.Build}, new int[] {16, 3, 4}));
		GristRegistry.addGristConversion("ingotTin", new GristSet(new GristType[] {GristType.Rust, GristType.Caulk}, new int[] {12, 8}));
		GristRegistry.addGristConversion("oreTin", new GristSet(new GristType[] {GristType.Rust, GristType.Caulk, GristType.Build}, new int[] {12, 8, 4}));
		GristRegistry.addGristConversion("ingotSilver", new GristSet(new GristType[] {GristType.Rust, GristType.Mercury}, new int[] {12, 8}));
		GristRegistry.addGristConversion("oreSilver", new GristSet(new GristType[] {GristType.Rust, GristType.Mercury, GristType.Build}, new int[] {12, 8, 4}));
		GristRegistry.addGristConversion("ingotLead", new GristSet(new GristType[] {GristType.Rust, GristType.Cobalt, GristType.Shale}, new int[] {12, 4, 4}));
		GristRegistry.addGristConversion("oreLead", new GristSet(new GristType[] {GristType.Rust, GristType.Cobalt, GristType.Shale, GristType.Build}, new int[] {12, 4, 4, 4}));
		GristRegistry.addGristConversion("ingotNickel", new GristSet(new GristType[] {GristType.Rust, GristType.Sulfur}, new int[] {12, 8}));
		GristRegistry.addGristConversion("oreNickel", new GristSet(new GristType[] {GristType.Rust, GristType.Sulfur, GristType.Build}, new int[] {12, 8, 4}));
		GristRegistry.addGristConversion("ingotInvar", new GristSet(new GristType[] {GristType.Rust, GristType.Sulfur}, new int[] {12, 5}));
		GristRegistry.addGristConversion("ingotAluminium", new GristSet(new GristType[] {GristType.Rust, GristType.Chalk}, new int[] {12, 6}));
		GristRegistry.addGristConversion("oreAluminium", new GristSet(new GristType[] {GristType.Rust, GristType.Chalk, GristType.Build}, new int[] {12, 6, 4}));
		
		GristRegistry.addGristConversion("ingotCobalt", new GristSet(new GristType[] {GristType.Cobalt}, new int[] {18}));
		GristRegistry.addGristConversion("oreCobalt", new GristSet(new GristType[] {GristType.Cobalt, GristType.Build}, new int[] {18, 4}));
		GristRegistry.addGristConversion("ingotArdite", new GristSet(new GristType[] {GristType.Garnet, GristType.Sulfur}, new int[] {12, 8}));
		GristRegistry.addGristConversion("oreArdite", new GristSet(new GristType[] {GristType.Garnet, GristType.Sulfur, GristType.Build}, new int[] {12, 8, 4}));
		GristRegistry.addGristConversion("ingotRedAlloy", new GristSet(new GristType[] {GristType.Rust, GristType.Garnet}, new int[] {18, 32}));
		if(!OreDictionary.getOres("ingotRedAlloy").isEmpty())
			CombinationRegistry.addCombination(new ItemStack(Items.IRON_INGOT), new ItemStack(Items.REDSTONE), MODE_OR, OreDictionary.getOres("ingotRedAlloy").get(0));
		
		try 
		{
			if(Loader.isModLoaded("IronChest"))
			{
				Block ironChest = ((Block) (Class.forName("cpw.mods.ironchest.IronChest").getField("ironChestBlock").get(null)));
				GristRegistry.addGristConversion(ironChest, 0, new GristSet(new GristType[] {GristType.Build, GristType.Rust}, new int[] {16, 128}));
				CombinationRegistry.addCombination(new ItemStack(Blocks.CHEST), new ItemStack(Items.IRON_INGOT), true, new ItemStack(ironChest, 1, 0));
			}
		}
		catch(Exception e) 
		{
			Debug.logger.warn("Exception while getting things for mod \"IronChest\".", e);
		}
		
		
		registerRecipes(new Minegicka3Support(), "minegicka3", false);
		registerRecipes(new NeverSayNetherSupport(), "nsn", false);
		registerRecipes(new ExtraUtilitiesSupport(), "ExtraUtilities", false);
		registerRecipes(new TinkersConstructSupport(), "TConstruct", false);
		
		if(Loader.isModLoaded("crafttweaker"))
			MinetweakerSupport.registerClasses();
		
	}
	
	@Nonnull
	public static ItemStack getFirstOreItem(String name)
	{
		if(OreDictionary.getOres(name).isEmpty())
			return ItemStack.EMPTY;
		else return OreDictionary.getOres(name).get(0);
	}
	/**
	 * Given a punched card or a carved dowel, returns a new item that represents the encoded data.
	 * 
	 * @param card - The dowel or card with encoded data
	 * @return An item, or null if the data was invalid.
	 */
	@Nonnull
	public static ItemStack getDecodedItem(ItemStack card)
	{
		
		if (card.isEmpty()) {return ItemStack.EMPTY;}
		
		NBTTagCompound tag = card.getTagCompound();
		
		if (tag == null || !tag.hasKey("contentID"))
		{
			return ItemStack.EMPTY;
		}
		
		if (!Item.REGISTRY.containsKey(new ResourceLocation(tag.getString("contentID")))) {return ItemStack.EMPTY;}
		ItemStack newItem = new ItemStack((Item)Item.REGISTRY.getObject(new ResourceLocation(tag.getString(("contentID")))), 1, tag.getInteger("contentMeta"));
		
		if(tag.hasKey("contentTags"))
			newItem.setTagCompound(tag.getCompoundTag("contentTags"));
		if(tag.hasKey("contentSize"))
			newItem.setCount(tag.getInteger("contentSize"));
		
		return newItem;
		
	}
	
	/**
	 * Given a punched card, this method returns a new item that represents the encoded data,
	 * or it just returns the item directly if it's not a punched card.
	 */
	@Nonnull
	public static ItemStack getDecodedItemDesignix(ItemStack card)
	{
		
		if (card.isEmpty()) {return ItemStack.EMPTY;}
		
		if (card.getItem().equals(captchaCard) && card.hasTagCompound() && card.getTagCompound().hasKey("contentID"))
		{
			return getDecodedItem(card);
		}
		else
		{
			return card.copy();
		}
	}
	
	@Nonnull
	public static ItemStack createEncodedItem(ItemStack item, boolean registerToCard)
	{
		NBTTagCompound nbt = null;
		if(!item.isEmpty())
		{
			nbt = new NBTTagCompound();
			nbt.setString("contentID", Item.REGISTRY.getNameForObject(item.getItem()).toString());
			nbt.setInteger("contentMeta", item.getItemDamage());
		}
		ItemStack stack = new ItemStack(registerToCard ? captchaCard : cruxiteDowel);
		stack.setTagCompound(nbt);
		return stack;
	}
	
	@Nonnull
	public static ItemStack createCard(ItemStack item, boolean punched)
	{
		ItemStack stack = createEncodedItem(item, true);
		if(!stack.hasTagCompound())
			stack.setTagCompound(new NBTTagCompound());
		stack.getTagCompound().setBoolean("punched", punched);
		if(!punched)
		{
			if(item.hasTagCompound())
				stack.getTagCompound().setTag("contentTags", item.getTagCompound());
			stack.getTagCompound().setInteger("contentSize", item.getCount());
		}
		
		return stack;
	}
	
	/**
	 * Adds all the recipes that are based on the existing vanilla crafting registries, like grist conversions of items composed of oither things.
	 */
	public static void registerDynamicRecipes() {
		
		recipeList = new HashMap<List<Object>, Object>();
		int invalid = 0;
		
		Debug.debug("Looking for dynamic grist conversions...");
		for (IRecipe recipe : CraftingManager.REGISTRY)
		{
			try
			{
				if (recipe instanceof ShapedRecipes) {
					ShapedRecipes newRecipe = (ShapedRecipes) recipe;
					//Debug.print("Found the recipe for "+"ITEM"+", id "+newRecipe.getRecipeOutput());
					recipeList.put(Arrays.asList(newRecipe.getRecipeOutput().getItem(),newRecipe.getRecipeOutput().getItemDamage()), recipe);
				} else if (recipe instanceof ShapelessRecipes) {
					ShapelessRecipes newRecipe = (ShapelessRecipes) recipe;
					//Debug.print("Found the recipe for "+"ITEM"+", id "+newRecipe.getRecipeOutput().itemID+":"+newRecipe.getRecipeOutput().getItemDamage());
					recipeList.put(Arrays.asList(newRecipe.getRecipeOutput().getItem(),newRecipe.getRecipeOutput().getItemDamage()), recipe);
				} else if (recipe instanceof ShapedOreRecipe) {
					ShapedOreRecipe newRecipe = (ShapedOreRecipe) recipe;
					//Debug.print("Found the recipe for "+"ITEM"+", id "+newRecipe.getRecipeOutput().itemID+":"+newRecipe.getRecipeOutput().getItemDamage());
					recipeList.put(Arrays.asList(newRecipe.getRecipeOutput().getItem(),newRecipe.getRecipeOutput().getItemDamage()), recipe);
				} else if (recipe instanceof ShapelessOreRecipe) {
					ShapelessOreRecipe newRecipe = (ShapelessOreRecipe) recipe;
					//Debug.print("Found the recipe for "+"ITEM"+", id "+newRecipe.getRecipeOutput().itemID+":"+newRecipe.getRecipeOutput().getItemDamage());
					recipeList.put(Arrays.asList(newRecipe.getRecipeOutput().getItem(),newRecipe.getRecipeOutput().getItemDamage()), recipe);
				} else {
					//Debug.print("Found the recipe for unkown format: "+recipe.getClass());
					invalid++;
				}
			}
			catch (NullPointerException e)
			{
				Debug.warnf("a null pointer exception was thrown for %s", recipe);
			}
		}
		Debug.info("Found "+recipeList.size()+" valid recipes, and "+invalid+" unknown ones.");
		
		Debug.debug("Calculating grist conversion...");
		Iterator<Entry<List<Object>, Object>> it = recipeList.entrySet().iterator();
		while (it.hasNext())
		{
			Map.Entry<List<Object>, Object> pairs = it.next();
			//Debug.print("Getting recipe with key"+pairs.getKey()+" and value "+pairs.getValue());
			lookedOver = new HashMap<List<Object>, Boolean>();
			try
			{
				getRecipe(pairs.getValue());
			} catch(Exception e)
			{
				Debug.warnf("Failed to look over recipe \"%s\" for \"%s\":%d. Cause:", pairs.getValue(), pairs.getKey().get(0), pairs.getKey().get(1));
				e.printStackTrace();
			}
		}
		
		registerRecipes(new Minegicka3Support(), "minegicka3", true);
		
		Debug.info("Added "+returned+" grist conversions.");
	}
	
	private static boolean getRecipe(Object recipe) {
		if (recipe instanceof ShapedRecipes) {
			ShapedRecipes newRecipe = (ShapedRecipes) recipe;
			//Debug.print("found shaped recipe. Output of "+newRecipe.getRecipeOutput());
			if (lookedOver.get(Arrays.asList(newRecipe.getRecipeOutput().getItem(),newRecipe.getRecipeOutput().getHasSubtypes() ? newRecipe.getRecipeOutput().getItemDamage() : 0)) != null) {
				////Debug.print("	Recursive recipe! Recipe failed.");
				return false;
			} else {
				lookedOver.put(Arrays.asList(newRecipe.getRecipeOutput().getItem(),newRecipe.getRecipeOutput().getHasSubtypes() ? newRecipe.getRecipeOutput().getItemDamage() : 0),true);
			}
			if (GristRegistry.getGristConversion(newRecipe.getRecipeOutput()) != null) {return false;};
			GristSet set = new GristSet();
			for (Ingredient ingredient : newRecipe.recipeItems) {
				ItemStack item = ingredient == Ingredient.EMPTY ? ItemStack.EMPTY : ingredient.getMatchingStacks()[0];
				if (GristRegistry.getGristConversion(item) != null) {
					//Debug.print("	Adding compo: "+item);
					set.addGrist(GristRegistry.getGristConversion(item));
				} else if (!item.isEmpty())
				{
					Object subrecipe = recipeList.get(Arrays.asList(item.getItem(),item.getHasSubtypes() && !((Integer)item.getItemDamage()).equals(32767) ? item.getItemDamage() : 0));
					if (subrecipe != null) {
						//Debug.print("	Could not find "+item+". Looking up subrecipe... {");
						 if (getRecipe(subrecipe)) {
							 if (GristRegistry.getGristConversion(item) == null) {
								//Debug.print("	} Recipe failure! getRecipe did not return expeted boolean value.");
								 return false;
							 }
							set.addGrist(GristRegistry.getGristConversion(item));
							//Debug.print("	}");
						 } else {
							//Debug.print("	}");
							 return false;
						 }
					} else {
						//Debug.print("	Could not find "+"ITEM"+" ("+item+"). Recipe failed!");
						return false;
					}
				}
			}
			set.scaleGrist(1/(float)newRecipe.getRecipeOutput().getCount());
			GristRegistry.addGristConversion(newRecipe.getRecipeOutput(),newRecipe.getRecipeOutput().getHasSubtypes(),set);
		} else if (recipe instanceof ShapelessRecipes)
		{
			ShapelessRecipes newRecipe = (ShapelessRecipes) recipe;
			if (lookedOver.get(Arrays.asList(newRecipe.getRecipeOutput().getItem(),newRecipe.getRecipeOutput().getHasSubtypes() ? newRecipe.getRecipeOutput().getItemDamage() : 0)) != null)
			{
				return false;
			} else
			{
				lookedOver.put(Arrays.asList(newRecipe.getRecipeOutput().getItem(),newRecipe.getRecipeOutput().getHasSubtypes() ? newRecipe.getRecipeOutput().getItemDamage() : 0),true);
			}
			if (GristRegistry.getGristConversion(newRecipe.getRecipeOutput()) != null) {return false;};
			GristSet set = new GristSet();
			for (Ingredient ingredient : newRecipe.recipeItems)
			{
				ItemStack item = ingredient == Ingredient.EMPTY ? ItemStack.EMPTY : ingredient.getMatchingStacks()[0];
				if (GristRegistry.getGristConversion(item) != null)
				{
					set.addGrist(GristRegistry.getGristConversion(item));
				} else if (!item.isEmpty())
				{
					Object subrecipe = recipeList.get(Arrays.asList(item.getItem(),item.getHasSubtypes() && !((Integer)item.getItemDamage()).equals(32767) ? item.getItemDamage() : 0));
					if (subrecipe != null) {
						//Debug.print("	Could not find "+"ITEM"+". Looking up subrecipe... {");
						 if (getRecipe(subrecipe)) {
							 if (GristRegistry.getGristConversion(item) == null) {
								 //Debug.print("	} Recipe failure! getRecipe did not return expeted boolean value.");
								 return false;
							 }
							 set.addGrist(GristRegistry.getGristConversion(item));
							 //Debug.print("	}");
						 } else {
							 //Debug.print("	}");
							 return false;
						 }
					} else {
						//Debug.print("	Could not find "+"ITEM"+" ("+item.itemID+":"+item.getItemDamage()+"). Recipe failed!");
						return false;
					}
				}
			}
			set.scaleGrist(1/(float)newRecipe.getRecipeOutput().getCount());
			GristRegistry.addGristConversion(newRecipe.getRecipeOutput(),newRecipe.getRecipeOutput().getHasSubtypes(),set);
		} else if (recipe instanceof ShapedOreRecipe) {
			ShapedOreRecipe newRecipe = (ShapedOreRecipe) recipe;
			//Debug.print("found shaped oredict recipe. Output of "+newRecipe.getRecipeOutput());
			if (GristRegistry.getGristConversion(newRecipe.getRecipeOutput()) != null) {return false;};
			if (lookedOver.get(Arrays.asList(newRecipe.getRecipeOutput().getItem(),newRecipe.getRecipeOutput().getHasSubtypes() ? newRecipe.getRecipeOutput().getItemDamage() : 0)) != null) {
				//Debug.print("	Recursive recipe! Recipe failed.");
				return false;
			} else {
				lookedOver.put(Arrays.asList(newRecipe.getRecipeOutput().getItem(),newRecipe.getRecipeOutput().getHasSubtypes() ? newRecipe.getRecipeOutput().getItemDamage() : 0),true);
			}
			GristSet set = new GristSet();
			for (Ingredient ingredient : newRecipe.getIngredients())
			{
				ItemStack item = null;
				if (ingredient == Ingredient.EMPTY)
					continue;
				item = ingredient.getMatchingStacks()[0];
				
				if (GristRegistry.getGristConversion(item) != null) {
					//Debug.print("	Adding compo: "+item);
					set.addGrist(GristRegistry.getGristConversion(item));
				} else if (!item.isEmpty())
				{
					Object subrecipe = recipeList.get(Arrays.asList(item.getItem(),item.getHasSubtypes() && !((Integer)item.getItemDamage()).equals(32767) ? item.getItemDamage() : 0));
					if (subrecipe != null) {
						//Debug.print("	Could not find "+item+". Looking up subrecipe... {");
						 if (getRecipe(subrecipe)) {
							 if (GristRegistry.getGristConversion(item) == null) {
								//Debug.print("	} Recipe failure! getRecipe did not return expeted boolean value.");
								 return false;
							 }
							 set.addGrist(GristRegistry.getGristConversion(item));
							//Debug.print("	}");
						 } else {
							//Debug.print("	}");
							 return false;
						 }
					} else {
						//Debug.print("	Could not find "+item+". Recipe failed!");
						return false;
					}
				}
			}
			set.scaleGrist(1/(float)newRecipe.getRecipeOutput().getCount());
			GristRegistry.addGristConversion(newRecipe.getRecipeOutput(),newRecipe.getRecipeOutput().getHasSubtypes(),set);
		} else if (recipe instanceof ShapelessOreRecipe) {
			//Debug.print("found shapeless oredict recipe. Output of "+"ITEM");
			ShapelessOreRecipe newRecipe = (ShapelessOreRecipe) recipe;
			if (lookedOver.get(Arrays.asList(newRecipe.getRecipeOutput().getItem(),newRecipe.getRecipeOutput().getHasSubtypes() ? newRecipe.getRecipeOutput().getItemDamage() : 0)) != null) {
				//Debug.print("	Recursive recipe! Recipe failed.");
				return false;
			} else {
				lookedOver.put(Arrays.asList(newRecipe.getRecipeOutput().getItem(),newRecipe.getRecipeOutput().getHasSubtypes() ? newRecipe.getRecipeOutput().getItemDamage() : 0),true);
			}
			if (GristRegistry.getGristConversion(newRecipe.getRecipeOutput()) != null) {return false;};
			GristSet set = new GristSet();
			for (Ingredient ingredient : newRecipe.getIngredients()) {
				ItemStack item = null;
				if (ingredient == Ingredient.EMPTY) {break;}
				item = ingredient.getMatchingStacks()[0];
				if (GristRegistry.getGristConversion(item) != null)
				{
					set.addGrist(GristRegistry.getGristConversion(item));
				} else if (!item.isEmpty())
				{
					Object subrecipe = recipeList.get(Arrays.asList(item.getItem(),item.getHasSubtypes() && !((Integer)item.getItemDamage()).equals(32767) ? item.getItemDamage() : 0));
					if (subrecipe != null)
					{
						//Debug.print("	Could not find "+"ITEM"+". Looking up subrecipe... {");
						 if (getRecipe(subrecipe)) {
							 if (GristRegistry.getGristConversion(item) == null) {
								 //Debug.print("	} Recipe failure! getRecipe did not return expeted boolean value.");
								 return false;
							 }
							 set.addGrist(GristRegistry.getGristConversion(item));
							 //Debug.print("	}");
						 } else {
							 //Debug.print("	}");
							 return false;
						 }
					} else {
						//Debug.print("	Could not find "+"ITEM"+" ("+item.itemID+":"+item.getItemDamage()+"). Recipe failed!");
						return false;
					}
				}
			}
			set.scaleGrist(1/(float)newRecipe.getRecipeOutput().getCount());
			GristRegistry.addGristConversion(newRecipe.getRecipeOutput(),newRecipe.getRecipeOutput().getHasSubtypes(),set);
		} else {
			//Debug.print("found other recipe class: "+recipe.getClass());
		}
		
		returned ++;
		return true;
	}
	
	private static void registerRecipes(ModSupport modSupport, String modname, boolean dynamic)
	{
		try
		{
			if(Loader.isModLoaded(modname))
			{
				if(dynamic)
					modSupport.registerDynamicRecipes();
				else modSupport.registerRecipes();
			}
		}
		catch(Exception e)
		{
			Debug.logger.error("Exception while creating"+(dynamic?" dynamic":"")+" recipes for mod \""+modname+"\":", e);
		}
	}
	
	public static List<ItemStack> getItems(Object item, int damage)
	{
		if(item instanceof ItemStack)
			return Arrays.asList((ItemStack)item);
		if(item instanceof Item)
			return Arrays.asList(new ItemStack((Item) item, 1, damage));
		else
		{
			List<ItemStack> list = OreDictionary.getOres((String) item);
			return list;
		}
	}
	
	public static void checkRegistered(Block block, String name)
	{
		checkRegistered(new ItemStack(block, 1, WILDCARD_VALUE), name);
	}
	
	public static void checkRegistered(Item item, String name)
	{
		checkRegistered(new ItemStack(item, 1, WILDCARD_VALUE), name);
	}
	
	public static void checkRegistered(ItemStack item, String name)
	{
		String[] names = CombinationRegistry.getDictionaryNames(item);
		for(String toCompare : names)
			if(toCompare.equals(name))
				return;
		
		OreDictionary.registerOre(name, item);
	}
	
	public static void addOrRemoveRecipes(boolean addCardRecipe)
	{
		/*if(addCardRecipe && !cardRecipeAdded)
		{
			CraftingManager.getInstance().getRecipeList().add(cardRecipe);
			cardRecipeAdded = true;
		}
		else if(!addCardRecipe && cardRecipeAdded)
		{
			CraftingManager.getInstance().getRecipeList().remove(cardRecipe);
			cardRecipeAdded = false;
		}*/
	}
	
}
