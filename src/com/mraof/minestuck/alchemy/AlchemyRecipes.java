package com.mraof.minestuck.alchemy;

import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.item.ItemCruxiteArtifact;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.modSupport.*;
import com.mraof.minestuck.util.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.*;

import static com.mraof.minestuck.MinestuckConfig.oreMultiplier;
import static com.mraof.minestuck.block.MinestuckBlocks.*;
import static com.mraof.minestuck.item.MinestuckItems.*;
import static com.mraof.minestuck.alchemy.CombinationRegistry.Mode.*;

public class AlchemyRecipes
{
	private static Map<Item, GristSet> containerlessCosts = new HashMap<>();
	
	public static void registerContainerlessCost(Item item, GristSet cost)
	{
		if(containerlessCosts != null)
			containerlessCosts.put(item, cost);
	}
	
	public static void registerVanillaRecipes()
	{
		//Set up Alchemiter recipes
		//Blocks
		AlchemyCostRegistry.addGristConversion(Blocks.STONE, new GristSet(new GristType[] {GristType.BUILD}, new int[] {2}));
		AlchemyCostRegistry.addGristConversion(Blocks.SMOOTH_STONE, new GristSet(new GristType[] {GristType.BUILD}, new int[] {2}));
		AlchemyCostRegistry.addGristConversion(Blocks.GRANITE, new GristSet(new GristType[] {GristType.BUILD}, new int[] {2}));
		AlchemyCostRegistry.addGristConversion(Blocks.DIORITE, new GristSet(new GristType[] {GristType.BUILD}, new int[] {2}));
		AlchemyCostRegistry.addGristConversion(Blocks.ANDESITE, new GristSet(new GristType[] {GristType.BUILD}, new int[] {2}));
		AlchemyCostRegistry.addGristConversion(Blocks.COBBLESTONE, new GristSet(new GristType[] {GristType.BUILD}, new int[] {2}));
		AlchemyCostRegistry.addGristConversion(Blocks.DEAD_BUSH, new GristSet(new GristType[] {GristType.AMBER, GristType.SULFUR}, new int[] {2, 1}));
		AlchemyCostRegistry.addGristConversion(Blocks.DIRT, new GristSet(new GristType[] {GristType.BUILD}, new int[] {2}));
		AlchemyCostRegistry.addGristConversion(Blocks.DRAGON_EGG, new GristSet(new GristType[] {GristType.URANIUM, GristType.TAR, GristType.ZILLIUM}, new int[] {800, 800, 10}));
		AlchemyCostRegistry.addGristConversion(Blocks.END_STONE, new GristSet(new GristType[] {GristType.CAULK, GristType.BUILD}, new int[] {3, 4}));
		AlchemyCostRegistry.addGristConversion(Blocks.GLASS, new GristSet(new GristType[] {GristType.BUILD}, new int[] {1}));
		AlchemyCostRegistry.addGristConversion(Blocks.GRASS_BLOCK, new GristSet(new GristType[] {GristType.BUILD}, new int[] {2}));
		AlchemyCostRegistry.addGristConversion(Blocks.GRAVEL, new GristSet(new GristType[] {GristType.BUILD}, new int[] {3}));
		AlchemyCostRegistry.addGristConversion(Blocks.TERRACOTTA, new GristSet(new GristType[] {GristType.SHALE, GristType.MARBLE}, new int[] {12, 4}));
		//AlchemyCostRegistry.addGristConversion(Blocks.STAINED_HARDENED_CLAY), false, new GristSet(new GristType[] {GristType.SHALE, GristType.MARBLE}, new int[] {12, 4})); TODO Teracotta block tag
		AlchemyCostRegistry.addGristConversion(Blocks.ICE, new GristSet(new GristType[] {GristType.COBALT}, new int[] {2}));
		AlchemyCostRegistry.addGristConversion(ItemTags.LEAVES, new GristSet(new GristType[] {GristType.BUILD}, new int[] {1}));
		AlchemyCostRegistry.addGristConversion(ItemTags.LOGS, new GristSet(new GristType[] {GristType.BUILD}, new int[] {8}));
		AlchemyCostRegistry.addGristConversion(Blocks.MELON, new GristSet(new GristType[] {GristType.AMBER, GristType.CHALK, GristType.BUILD}, new int[] {8, 8, 2}));
		AlchemyCostRegistry.addGristConversion(Blocks.MYCELIUM, new GristSet(new GristType[] {GristType.IODINE, GristType.RUBY, GristType.BUILD}, new int[] {2, 2, 2}));
		AlchemyCostRegistry.addGristConversion(Blocks.NETHERRACK, new GristSet(new GristType[] {GristType.BUILD, GristType.TAR}, new int[] {2, 1}));
		AlchemyCostRegistry.addGristConversion(Blocks.OBSIDIAN, new GristSet(new GristType[] {GristType.COBALT, GristType.TAR, GristType.BUILD}, new int[] {8, 16, 6}));
		AlchemyCostRegistry.addGristConversion(Blocks.PUMPKIN, new GristSet(new GristType[] {GristType.AMBER, GristType.CAULK}, new int[] {12, 6}));
		AlchemyCostRegistry.addGristConversion(ItemTags.SAND, new GristSet(new GristType[] {GristType.BUILD}, new int[] {1}));
		AlchemyCostRegistry.addGristConversion(ItemTags.SAPLINGS, new GristSet(new GristType[] {GristType.BUILD}, new int[] {16}));
		AlchemyCostRegistry.addGristConversion(Blocks.SNOW_BLOCK, new GristSet(new GristType[] {GristType.COBALT, GristType.BUILD}, new int[] {5, 3}));
		AlchemyCostRegistry.addGristConversion(Blocks.SNOW, new GristSet(new GristType[] {GristType.COBALT}, new int[] {2}));
		AlchemyCostRegistry.addGristConversion(Blocks.SOUL_SAND, new GristSet(new GristType[] {GristType.SULFUR, GristType.CAULK, GristType.BUILD}, new int[] {5, 3, 2}));
		AlchemyCostRegistry.addGristConversion(Blocks.SPONGE, new GristSet(new GristType[] {GristType.AMBER, GristType.SULFUR}, new int[] {20, 30}));
		AlchemyCostRegistry.addGristConversion(Blocks.WET_SPONGE, new GristSet(new GristType[] {GristType.AMBER, GristType.SULFUR, GristType.COBALT}, new int[] {20, 30, 10}));
		AlchemyCostRegistry.addGristConversion(Blocks.CRACKED_STONE_BRICKS, new GristSet(new GristType[] {GristType.BUILD}, new int[] {2}));
		AlchemyCostRegistry.addGristConversion(Blocks.STONE_BRICK_STAIRS, new GristSet(new GristType[] {GristType.BUILD}, new int[] {3}));
		AlchemyCostRegistry.addGristConversion(Blocks.SANDSTONE_STAIRS, new GristSet(new GristType[] {GristType.BUILD}, new int[] {6}));
		AlchemyCostRegistry.addGristConversion(Blocks.COBWEB, new GristSet(new GristType[] {GristType.CHALK}, new int[] {18}));
		AlchemyCostRegistry.addGristConversion(Blocks.WHITE_WOOL, new GristSet(new GristType[] {GristType.CHALK}, new int[] {6}));
		AlchemyCostRegistry.addGristConversion(Blocks.ORANGE_WOOL, new GristSet(new GristType[] {GristType.CHALK, GristType.GARNET, GristType.AMBER}, new int[] {6, 1, 1}));
		AlchemyCostRegistry.addGristConversion(Blocks.PURPLE_WOOL, new GristSet(new GristType[] {GristType.CHALK, GristType.AMETHYST, GristType.GARNET}, new int[] {6, 1, 1}));
		AlchemyCostRegistry.addGristConversion(Blocks.BLUE_WOOL, new GristSet(new GristType[] {GristType.CHALK, GristType.AMETHYST}, new int[] {6, 2}));
		AlchemyCostRegistry.addGristConversion(Blocks.BROWN_WOOL, new GristSet(new GristType[] {GristType.CHALK, GristType.IODINE}, new int[] {6, 2}));
		AlchemyCostRegistry.addGristConversion(Blocks.GREEN_WOOL, new GristSet(new GristType[] {GristType.CHALK, GristType.AMBER}, new int[] {6, 2}));
		AlchemyCostRegistry.addGristConversion(Blocks.RED_WOOL, new GristSet(new GristType[] {GristType.CHALK, GristType.GARNET}, new int[] {6, 2}));
		AlchemyCostRegistry.addGristConversion(Blocks.BLACK_WOOL, new GristSet(new GristType[] {GristType.CHALK, GristType.TAR}, new int[] {6, 2}));
		AlchemyCostRegistry.addGristConversion(Blocks.MAGENTA_WOOL, new GristSet(new GristType[] {GristType.CHALK, GristType.AMETHYST, GristType.GARNET}, new int[] {6, 1, 2}));
		AlchemyCostRegistry.addGristConversion(Blocks.LIGHT_BLUE_WOOL, new GristSet(new GristType[] {GristType.CHALK, GristType.AMETHYST}, new int[] {8, 1}));
		AlchemyCostRegistry.addGristConversion(Blocks.YELLOW_WOOL, new GristSet(new GristType[] {GristType.CHALK, GristType.AMBER}, new int[] {6, 2}));
		AlchemyCostRegistry.addGristConversion(Blocks.LIME_WOOL, new GristSet(new GristType[] {GristType.CHALK, GristType.AMBER}, new int[] {6, 2}));
		AlchemyCostRegistry.addGristConversion(Blocks.PINK_WOOL, new GristSet(new GristType[] {GristType.CHALK, GristType.GARNET}, new int[] {6, 1}));
		AlchemyCostRegistry.addGristConversion(Blocks.GRAY_WOOL, new GristSet(new GristType[] {GristType.CHALK, GristType.TAR}, new int[] {6, 1}));
		AlchemyCostRegistry.addGristConversion(Blocks.LIGHT_GRAY_WOOL, new GristSet(new GristType[] {GristType.CHALK, GristType.TAR}, new int[] {6, 1}));
		AlchemyCostRegistry.addGristConversion(Blocks.CYAN_WOOL, new GristSet(new GristType[] {GristType.CHALK, GristType.AMETHYST, GristType.AMBER}, new int[] {6, 1, 1}));
		AlchemyCostRegistry.addGristConversion(Blocks.PACKED_ICE, new GristSet(new GristType[] {GristType.BUILD, GristType.COBALT}, new int[] {10, 6}));
		AlchemyCostRegistry.addGristConversion(Blocks.TORCH, new GristSet(GristType.BUILD, 2));
		AlchemyCostRegistry.addGristConversion(Blocks.PRISMARINE, new GristSet(new GristType[] {GristType.COBALT, GristType.BUILD}, new int[] {7, 12}));
		AlchemyCostRegistry.addGristConversion(Blocks.PRISMARINE_BRICKS, new GristSet(new GristType[] {GristType.COBALT, GristType.BUILD}, new int[] {12, 18}));
		AlchemyCostRegistry.addGristConversion(Blocks.DARK_PRISMARINE, new GristSet(new GristType[] {GristType.COBALT, GristType.TAR, GristType.BUILD}, new int[] {10, 2, 18}));
		AlchemyCostRegistry.addGristConversion(Blocks.SEA_LANTERN, new GristSet(new GristType[] {GristType.COBALT, GristType.DIAMOND, GristType.AMETHYST}, new int[] {32, 6, 12}));
		AlchemyCostRegistry.addGristConversion(Blocks.SANDSTONE, new GristSet(GristType.BUILD, 4));
		AlchemyCostRegistry.addGristConversion(Blocks.RED_SANDSTONE, new GristSet(GristType.BUILD, 4));
		AlchemyCostRegistry.addGristConversion(ItemTags.PLANKS, new GristSet(GristType.BUILD, 2));
		AlchemyCostRegistry.addGristConversion(Blocks.CHORUS_FLOWER, new GristSet(new GristType[] {GristType.BUILD, GristType.AMETHYST, GristType.SHALE}, new int[] {26, 23, 10}));
		AlchemyCostRegistry.addGristConversion(Blocks.PURPUR_PILLAR, new GristSet(new GristType[] {GristType.AMETHYST, GristType.SHALE}, new int[] {2, 4}));
		
		//Items
		AlchemyCostRegistry.addGristConversion(Items.BLAZE_ROD, new GristSet(new GristType[] {GristType.TAR, GristType.URANIUM}, new int[] {20, 2}));
		AlchemyCostRegistry.addGristConversion(Items.BONE, new GristSet(new GristType[] {GristType.CHALK}, new int[] {6}));
		AlchemyCostRegistry.addGristConversion(Items.BRICK, new GristSet(new GristType[] {GristType.SHALE, GristType.TAR}, new int[] {3, 1}));
		AlchemyCostRegistry.addGristConversion(Items.CHAINMAIL_BOOTS, new GristSet(new GristType[] {GristType.RUST, GristType.MERCURY}, new int[] {16, 8}));
		AlchemyCostRegistry.addGristConversion(Items.CHAINMAIL_CHESTPLATE, new GristSet(new GristType[] {GristType.RUST, GristType.MERCURY}, new int[] {32, 16}));
		AlchemyCostRegistry.addGristConversion(Items.CHAINMAIL_HELMET, new GristSet(new GristType[] {GristType.RUST, GristType.MERCURY}, new int[] {20, 10}));
		AlchemyCostRegistry.addGristConversion(Items.CHAINMAIL_LEGGINGS, new GristSet(new GristType[] {GristType.RUST, GristType.MERCURY}, new int[] {28, 14}));
		AlchemyCostRegistry.addGristConversion(Items.CLAY_BALL, new GristSet(new GristType[] {GristType.SHALE}, new int[] {3}));
		AlchemyCostRegistry.addGristConversion(Items.DIAMOND_HORSE_ARMOR, new GristSet(new GristType[] {GristType.DIAMOND}, new int[] {80}));
		AlchemyCostRegistry.addGristConversion(Items.INK_SAC, new GristSet(new GristType[] {GristType.TAR}, new int[] {4}));
		AlchemyCostRegistry.addGristConversion(Items.ROSE_RED, new GristSet(new GristType[] {GristType.GARNET}, new int[] {4}));
		AlchemyCostRegistry.addGristConversion(Items.LIME_DYE, new GristSet(new GristType[] {GristType.AMBER}, new int[] {3}));
		AlchemyCostRegistry.addGristConversion(Items.DANDELION_YELLOW, new GristSet(new GristType[] {GristType.AMBER}, new int[] {4}));
		AlchemyCostRegistry.addGristConversion(Items.LIGHT_BLUE_DYE, new GristSet(new GristType[] {GristType.AMETHYST, GristType.CHALK}, new int[] {2, 2}));
		AlchemyCostRegistry.addGristConversion(Items.MAGENTA_DYE, new GristSet(new GristType[] {GristType.AMETHYST, GristType.GARNET}, new int[] {1, 3}));
		AlchemyCostRegistry.addGristConversion(Items.ORANGE_DYE, new GristSet(new GristType[] {GristType.GARNET, GristType.AMBER}, new int[] {2, 2}));
		AlchemyCostRegistry.addGristConversion(Items.CACTUS_GREEN, new GristSet(new GristType[] {GristType.AMBER, GristType.IODINE}, new int[] {3, 1}));
		AlchemyCostRegistry.addGristConversion(Items.COCOA_BEANS, new GristSet(new GristType[] {GristType.IODINE, GristType.AMBER}, new int[] {3, 1}));
		AlchemyCostRegistry.addGristConversion(Items.LAPIS_LAZULI, new GristSet(new GristType[] {GristType.AMETHYST}, new int[] {4}));
		AlchemyCostRegistry.addGristConversion(Items.PURPLE_DYE, new GristSet(new GristType[] {GristType.AMETHYST, GristType.GARNET}, new int[] {2, 2}));
		AlchemyCostRegistry.addGristConversion(Items.CYAN_DYE, new GristSet(new GristType[] {GristType.AMETHYST, GristType.AMBER, GristType.IODINE}, new int[] {2, 2, 1}));
		AlchemyCostRegistry.addGristConversion(Items.LIGHT_GRAY_DYE, new GristSet(new GristType[] {GristType.TAR, GristType.CHALK}, new int[] {1, 3}));
		AlchemyCostRegistry.addGristConversion(Items.GRAY_DYE, new GristSet(new GristType[] {GristType.TAR, GristType.CHALK}, new int[] {3, 1}));
		AlchemyCostRegistry.addGristConversion(Items.PINK_DYE, new GristSet(new GristType[] {GristType.GARNET, GristType.CHALK}, new int[] {2, 2}));
		AlchemyCostRegistry.addGristConversion(Items.ENCHANTED_BOOK, new GristSet(new GristType[] {GristType.URANIUM, GristType.QUARTZ, GristType.DIAMOND, GristType.RUBY, GristType.CHALK, GristType.IODINE}, new int[] {8, 1, 4, 4, 16, 2}));
		AlchemyCostRegistry.addGristConversion(Items.ENDER_PEARL, new GristSet(new GristType[] {GristType.URANIUM, GristType.DIAMOND, GristType.MERCURY}, new int[] {13, 5, 8}));
		AlchemyCostRegistry.addGristConversion(Items.EXPERIENCE_BOTTLE, new GristSet(new GristType[] {GristType.URANIUM, GristType.QUARTZ, GristType.DIAMOND, GristType.RUBY}, new int[] {16, 3, 4, 6}));
		AlchemyCostRegistry.addGristConversion(Items.FEATHER, new GristSet(new GristType[] {GristType.CHALK}, new int[] {4}));
		AlchemyCostRegistry.addGristConversion(Items.FIREWORK_STAR, new GristSet(new GristType[] {GristType.SULFUR, GristType.CHALK}, new int[] {4, 2}));
		AlchemyCostRegistry.addGristConversion(Items.FIREWORK_ROCKET, new GristSet(new GristType[] {GristType.SULFUR, GristType.CHALK}, new int[] {4, 5}));
		AlchemyCostRegistry.addGristConversion(Items.FLINT, new GristSet(new GristType[] {GristType.BUILD}, new int[] {4}));
		AlchemyCostRegistry.addGristConversion(Items.GHAST_TEAR, new GristSet(new GristType[] {GristType.COBALT, GristType.CHALK}, new int[] {10, 19}));
		AlchemyCostRegistry.addGristConversion(Items.GLOWSTONE_DUST, new GristSet(new GristType[] {GristType.TAR, GristType.CHALK}, new int[] {4, 6}));
		AlchemyCostRegistry.addGristConversion(Items.GOLDEN_HORSE_ARMOR, new GristSet(new GristType[] {GristType.GOLD}, new int[] {40}));
		AlchemyCostRegistry.addGristConversion(Items.GUNPOWDER, new GristSet(new GristType[] {GristType.SULFUR, GristType.CHALK}, new int[] {3, 2}));
		AlchemyCostRegistry.addGristConversion(Items.IRON_HORSE_ARMOR, new GristSet(new GristType[] {GristType.RUST}, new int[] {40}));
		registerContainerlessCost(Items.LAVA_BUCKET, new GristSet(new GristType[] {GristType.TAR}, new int[] {16}));
		AlchemyCostRegistry.addGristConversion(Items.LEATHER, new GristSet(new GristType[] {GristType.IODINE, GristType.CHALK}, new int[] {3, 3}));
		AlchemyCostRegistry.addGristConversion(Items.MAP, new GristSet(new GristType[] {GristType.RUST, GristType.CHALK, GristType.GARNET}, new int[] {32, 10, 2}));
		registerContainerlessCost(Items.MILK_BUCKET, new GristSet(new GristType[] {GristType.CHALK}, new int[] {8}));
		AlchemyCostRegistry.addGristConversion(Items.NAME_TAG, new GristSet(new GristType[] {GristType.BUILD, GristType.CAULK, GristType.AMBER, GristType.CHALK}, new int[] {4, 10, 12, 8}));
		AlchemyCostRegistry.addGristConversion(Items.NETHER_STAR, new GristSet(new GristType[] {GristType.URANIUM, GristType.TAR, GristType.DIAMOND}, new int[] {344, 135, 92}));
		AlchemyCostRegistry.addGristConversion(Items.NETHER_WART, new GristSet(new GristType[] {GristType.IODINE, GristType.TAR}, new int[] {3, 5}));
		AlchemyCostRegistry.addGristConversion(Items.NETHER_BRICK, new GristSet(new GristType[] {GristType.BUILD, GristType.TAR}, new int[] {1, 2}));
		AlchemyCostRegistry.addGristConversion(Items.MUSIC_DISC_11, new GristSet(new GristType[] {GristType.BUILD, GristType.CAULK, GristType.TAR, GristType.MERCURY}, new int[] {10, 5, 2, 2}));
		AlchemyCostRegistry.addGristConversion(Items.MUSIC_DISC_13, new GristSet(new GristType[] {GristType.BUILD, GristType.CAULK, GristType.AMBER, GristType.CHALK}, new int[] {15, 8, 5, 5}));
		AlchemyCostRegistry.addGristConversion(Items.MUSIC_DISC_BLOCKS, new GristSet(new GristType[] {GristType.BUILD, GristType.CAULK, GristType.RUBY, GristType.RUST}, new int[] {15, 8, 5, 5}));
		AlchemyCostRegistry.addGristConversion(Items.MUSIC_DISC_CAT, new GristSet(new GristType[] {GristType.BUILD, GristType.CAULK, GristType.URANIUM, GristType.SHALE}, new int[] {15, 8, 2, 5}));
		AlchemyCostRegistry.addGristConversion(Items.MUSIC_DISC_CHIRP, new GristSet(new GristType[] {GristType.BUILD, GristType.CAULK, GristType.RUBY, GristType.GARNET}, new int[] {15, 8, 5, 5}));
		AlchemyCostRegistry.addGristConversion(Items.MUSIC_DISC_FAR, new GristSet(new GristType[] {GristType.BUILD, GristType.CAULK, GristType.URANIUM, GristType.SULFUR}, new int[] {15, 8, 2, 5}));
		AlchemyCostRegistry.addGristConversion(Items.MUSIC_DISC_MALL, new GristSet(new GristType[] {GristType.BUILD, GristType.CAULK, GristType.AMETHYST, GristType.TAR}, new int[] {15, 8, 5, 5}));
		AlchemyCostRegistry.addGristConversion(Items.MUSIC_DISC_MELLOHI, new GristSet(new GristType[] {GristType.BUILD, GristType.CAULK, GristType.MARBLE, GristType.SHALE}, new int[] {15, 8, 5, 5}));
		AlchemyCostRegistry.addGristConversion(Items.MUSIC_DISC_STAL, new GristSet(new GristType[] {GristType.BUILD, GristType.CAULK, GristType.TAR, GristType.MERCURY}, new int[] {15, 8, 5, 5}));
		AlchemyCostRegistry.addGristConversion(Items.MUSIC_DISC_STRAD, new GristSet(new GristType[] {GristType.BUILD, GristType.CAULK, GristType.CHALK, GristType.QUARTZ}, new int[] {15, 8, 5, 5}));
		AlchemyCostRegistry.addGristConversion(Items.MUSIC_DISC_WAIT, new GristSet(new GristType[] {GristType.BUILD, GristType.CAULK, GristType.COBALT, GristType.QUARTZ}, new int[] {15, 8, 5, 5}));
		AlchemyCostRegistry.addGristConversion(Items.MUSIC_DISC_WARD, new GristSet(new GristType[] {GristType.BUILD, GristType.CAULK, GristType.IODINE, GristType.GOLD}, new int[] {15, 8, 5, 2}));
		AlchemyCostRegistry.addGristConversion(Items.REDSTONE, new GristSet(new GristType[] {GristType.GARNET}, new int[] {4}));
		AlchemyCostRegistry.addGristConversion(Blocks.SUGAR_CANE, new GristSet(new GristType[] {GristType.AMBER, GristType.IODINE}, new int[] {3, 2}));
		AlchemyCostRegistry.addGristConversion(Items.ROTTEN_FLESH, new GristSet(new GristType[] {GristType.RUST, GristType.IODINE}, new int[] {1, 1}));
		AlchemyCostRegistry.addGristConversion(Items.SADDLE, new GristSet(new GristType[] {GristType.RUST, GristType.IODINE, GristType.CHALK}, new int[] {16, 7, 14}));
		AlchemyCostRegistry.addGristConversion(Items.SKELETON_SKULL, new GristSet(new GristType[] {GristType.CHALK}, new int[] {28}));
		AlchemyCostRegistry.addGristConversion(Items.WITHER_SKELETON_SKULL, new GristSet(new GristType[] {GristType.URANIUM, GristType.TAR, GristType.DIAMOND}, new int[] {35, 48, 16}));
		AlchemyCostRegistry.addGristConversion(Items.ZOMBIE_HEAD, new GristSet(new GristType[] {GristType.RUST, GristType.IODINE}, new int[] {5, 20}));
		AlchemyCostRegistry.addGristConversion(Items.PLAYER_HEAD, new GristSet(new GristType[] {GristType.ARTIFACT}, new int[] {10}));
		AlchemyCostRegistry.addGristConversion(Items.CREEPER_HEAD, new GristSet(new GristType[] {GristType.SULFUR, GristType.CHALK}, new int[] {10, 18}));
		AlchemyCostRegistry.addGristConversion(Items.DRAGON_HEAD, new GristSet(new GristType[] {GristType.URANIUM, GristType.TAR, GristType.ZILLIUM}, new int[] {25, 70, 1}));
		AlchemyCostRegistry.addGristConversion(Items.SLIME_BALL, new GristSet(new GristType[] {GristType.CAULK}, new int[] {8}));
		AlchemyCostRegistry.addGristConversion(Items.SNOWBALL, new GristSet(new GristType[] {GristType.COBALT}, new int[] {1}));
		AlchemyCostRegistry.addGristConversion(Items.SPIDER_EYE, new GristSet(new GristType[] {GristType.AMBER, GristType.IODINE}, new int[] {6, 2}));
		AlchemyCostRegistry.addGristConversion(Items.STRING, new GristSet(new GristType[] {GristType.CHALK}, new int[] {2}));
		AlchemyCostRegistry.addGristConversion(Items.SUGAR, new GristSet(new GristType[] {GristType.IODINE, GristType.AMBER}, new int[] {2, 3}));
		registerContainerlessCost(Items.WATER_BUCKET, new GristSet(new GristType[] {GristType.COBALT}, new int[] {4}));
		AlchemyCostRegistry.addGristConversion(Items.WHEAT, new GristSet(new GristType[] {GristType.IODINE}, new int[] {2}));
		AlchemyCostRegistry.addGristConversion(Items.WHEAT_SEEDS, new GristSet(new GristType[] {GristType.AMBER, GristType.IODINE}, new int[] {1, 1}));
		AlchemyCostRegistry.addGristConversion(Items.WRITABLE_BOOK, new GristSet(new GristType[] {GristType.CHALK, GristType.IODINE}, new int[] {16, 2}));
		AlchemyCostRegistry.addGristConversion(Items.PRISMARINE_CRYSTALS, new GristSet(new GristType[] {GristType.COBALT, GristType.DIAMOND, GristType.AMETHYST}, new int[] {5, 2, 4}));
		AlchemyCostRegistry.addGristConversion(Items.PRISMARINE_SHARD, new GristSet(new GristType[] {GristType.BUILD, GristType.COBALT}, new int[] {3, 3}));
		AlchemyCostRegistry.addGristConversion(Items.RABBIT_HIDE, new GristSet(new GristType[] {GristType.IODINE, GristType.CHALK}, new int[] {1, 1}));
		AlchemyCostRegistry.addGristConversion(Items.RABBIT_FOOT, new GristSet(new GristType[] {GristType.IODINE, GristType.CHALK, GristType.RUST}, new int[] {10, 12, 3}));
		AlchemyCostRegistry.addGristConversion(Items.CHORUS_FRUIT, new GristSet(new GristType[] {GristType.AMETHYST, GristType.SHALE}, new int[] {2, 4}));
		AlchemyCostRegistry.addGristConversion(Items.POPPED_CHORUS_FRUIT, new GristSet(new GristType[] {GristType.AMETHYST, GristType.SHALE}, new int[] {2, 4}));
		AlchemyCostRegistry.addGristConversion(Items.BEETROOT, new GristSet(new GristType[] {GristType.RUST, GristType.CAULK}, new int[] {1, 2}));
		AlchemyCostRegistry.addGristConversion(Items.BEETROOT_SEEDS, new GristSet(new GristType[] {GristType.RUST, GristType.IODINE}, new int[] {2, 2}));
		AlchemyCostRegistry.addGristConversion(Items.ELYTRA, new GristSet(new GristType[] {GristType.DIAMOND, GristType.SULFUR, GristType.CAULK}, new int[] {51, 38, 65}));
		AlchemyCostRegistry.addGristConversion(Items.DRAGON_BREATH, new GristSet(new GristType[] {GristType.RUBY, GristType.SULFUR}, new int[] {4, 13}));
		
		//Ores
		if(oreMultiplier != 0)
		{
			/*AlchemyCostRegistry.addGristConversion("oreCoal", new GristSet(new GristType[] {GristType.BUILD, GristType.TAR}, new int[] {4, 8*oreMultiplier}));	TODO Block tags
			AlchemyCostRegistry.addGristConversion("oreIron", new GristSet(new GristType[] {GristType.BUILD, GristType.RUST}, new int[] {4, 9*oreMultiplier}));
			AlchemyCostRegistry.addGristConversion("oreGold", new GristSet(new GristType[] {GristType.BUILD, GristType.GOLD}, new int[] {4, 9*oreMultiplier}));
			AlchemyCostRegistry.addGristConversion("oreRedstone", new GristSet(new GristType[] {GristType.GARNET, GristType.BUILD}, new int[] {16*oreMultiplier, 4}));
			AlchemyCostRegistry.addGristConversion("oreLapis", new GristSet(new GristType[] {GristType.AMETHYST, GristType.BUILD}, new int[] {16*oreMultiplier, 4}));
			AlchemyCostRegistry.addGristConversion("oreDiamond", new GristSet(new GristType[] {GristType.DIAMOND, GristType.BUILD}, new int[] {18*oreMultiplier, 4}));
			AlchemyCostRegistry.addGristConversion("oreEmerald", new GristSet(new GristType[] {GristType.RUBY, GristType.DIAMOND, GristType.BUILD}, new int[] {9*oreMultiplier, 9*oreMultiplier, 4}));
			AlchemyCostRegistry.addGristConversion("oreQuartz", new GristSet(new GristType[] {GristType.QUARTZ, GristType.MARBLE, GristType.BUILD}, new int[] {8*oreMultiplier, 2*oreMultiplier, 2}));*/
		}
		AlchemyCostRegistry.addGristConversion(Items.COAL, new GristSet(new GristType[] {GristType.TAR}, new int[] {8}));
		AlchemyCostRegistry.addGristConversion(Items.CHARCOAL, new GristSet(new GristType[] {GristType.TAR, GristType.AMBER}, new int[] {6, 2}));
		AlchemyCostRegistry.addGristConversion(Items.GOLD_INGOT, new GristSet(new GristType[] {GristType.GOLD}, new int[] {9}));
		AlchemyCostRegistry.addGristConversion(Items.IRON_INGOT, new GristSet(new GristType[] {GristType.RUST}, new int[] {9}));
		AlchemyCostRegistry.addGristConversion(Items.QUARTZ, new GristSet(new GristType[] {GristType.QUARTZ, GristType.MARBLE}, new int[] {4, 1}));
		AlchemyCostRegistry.addGristConversion(Blocks.QUARTZ_BLOCK, new GristSet(new GristType[] {GristType.QUARTZ, GristType.MARBLE}, new int[] {16, 4}));
		AlchemyCostRegistry.addGristConversion(Items.EMERALD, new GristSet(new GristType[] {GristType.RUBY, GristType.DIAMOND}, new int[] {9, 9}));
		AlchemyCostRegistry.addGristConversion(Items.DIAMOND, new GristSet(new GristType[] {GristType.DIAMOND}, new int[] {18}));
		
		//Plants
		AlchemyCostRegistry.addGristConversion(Blocks.DANDELION, new GristSet(new GristType[] {GristType.AMBER, GristType.IODINE}, new int[] {4, 1}));
		AlchemyCostRegistry.addGristConversion(Blocks.POPPY, new GristSet(new GristType[] {GristType.GARNET, GristType.IODINE}, new int[] {4, 1}));
		AlchemyCostRegistry.addGristConversion(Blocks.BLUE_ORCHID, new GristSet(new GristType[] {GristType.AMETHYST, GristType.CHALK, GristType.IODINE}, new int[] {2, 2, 1}));
		AlchemyCostRegistry.addGristConversion(Blocks.ALLIUM, new GristSet(new GristType[] {GristType.AMETHYST, GristType.GARNET, GristType.IODINE}, new int[] {1, 3, 1}));
		AlchemyCostRegistry.addGristConversion(Blocks.AZURE_BLUET, new GristSet(new GristType[] {GristType.TAR, GristType.CHALK, GristType.IODINE}, new int[] {1, 3, 1}));
		AlchemyCostRegistry.addGristConversion(Blocks.RED_TULIP, new GristSet(new GristType[] {GristType.TAR, GristType.CHALK, GristType.IODINE}, new int[] {1, 3, 1}));
		AlchemyCostRegistry.addGristConversion(Blocks.ORANGE_TULIP, new GristSet(new GristType[] {GristType.GARNET, GristType.AMBER, GristType.IODINE}, new int[] {2, 2, 1}));
		AlchemyCostRegistry.addGristConversion(Blocks.WHITE_TULIP, new GristSet(new GristType[] {GristType.AMETHYST, GristType.CHALK, GristType.IODINE}, new int[] {2, 2, 1}));
		AlchemyCostRegistry.addGristConversion(Blocks.PINK_TULIP, new GristSet(new GristType[] {GristType.GARNET, GristType.IODINE}, new int[] {4, 1}));
		AlchemyCostRegistry.addGristConversion(Blocks.OXEYE_DAISY, new GristSet(new GristType[] {GristType.TAR, GristType.CHALK, GristType.IODINE}, new int[] {1, 3, 1}));
		AlchemyCostRegistry.addGristConversion(Blocks.BROWN_MUSHROOM, new GristSet(new GristType[] {GristType.IODINE}, new int[] {5}));
		AlchemyCostRegistry.addGristConversion(Blocks.RED_MUSHROOM, new GristSet(new GristType[] {GristType.IODINE, GristType.RUBY}, new int[] {3, 2}));
		AlchemyCostRegistry.addGristConversion(Blocks.CACTUS, new GristSet(new GristType[] {GristType.AMBER, GristType.IODINE}, new int[] {4, 1}));
		AlchemyCostRegistry.addGristConversion(Blocks.GRASS, new GristSet(new GristType[] {GristType.BUILD}, new int[] {1}));
		AlchemyCostRegistry.addGristConversion(Blocks.VINE, new GristSet(new GristType[] {GristType.BUILD, GristType.AMBER}, new int[] {2, 1}));
		AlchemyCostRegistry.addGristConversion(Blocks.LILY_PAD, new GristSet(new GristType[] {GristType.AMBER, GristType.IODINE}, new int[] {4, 1}));
		AlchemyCostRegistry.addGristConversion(Blocks.SUNFLOWER, new GristSet(new GristType[] {GristType.AMBER, GristType.IODINE}, new int[] {7, 2}));
		AlchemyCostRegistry.addGristConversion(Blocks.LILAC, new GristSet(new GristType[] {GristType.AMETHYST, GristType.GARNET, GristType.IODINE}, new int[] {2, 5, 2}));
		AlchemyCostRegistry.addGristConversion(Blocks.TALL_GRASS, new GristSet(new GristType[] {GristType.AMBER}, new int[] {2}));
		AlchemyCostRegistry.addGristConversion(Blocks.LARGE_FERN, new GristSet(new GristType[] {GristType.AMBER}, new int[] {2}));
		AlchemyCostRegistry.addGristConversion(Blocks.ROSE_BUSH, new GristSet(new GristType[] {GristType.GARNET, GristType.IODINE}, new int[] {7, 2}));
		AlchemyCostRegistry.addGristConversion(Blocks.PEONY, new GristSet(new GristType[] {GristType.GARNET, GristType.CHALK, GristType.IODINE}, new int[] {4, 4, 2}));
		
		//Food
		AlchemyCostRegistry.addGristConversion(Items.APPLE, new GristSet(new GristType[] {GristType.AMBER, GristType.SHALE}, new int[] {2, 2}));
		AlchemyCostRegistry.addGristConversion(Items.CARROT, new GristSet(new GristType[] {GristType.AMBER, GristType.CHALK}, new int[] {3, 1}));
		AlchemyCostRegistry.addGristConversion(Items.POTATO, new GristSet(new GristType[] {GristType.AMBER}, new int[] {4}));
		AlchemyCostRegistry.addGristConversion(Items.POISONOUS_POTATO, new GristSet(new GristType[] {GristType.AMBER, GristType.IODINE}, new int[] {4, 2}));
		AlchemyCostRegistry.addGristConversion(Items.BAKED_POTATO, new GristSet(new GristType[] {GristType.AMBER, GristType.TAR}, new int[] {4, 1}));
		AlchemyCostRegistry.addGristConversion(Items.MELON_SLICE, new GristSet(new GristType[] {GristType.AMBER, GristType.CAULK}, new int[] {1, 1}));
		AlchemyCostRegistry.addGristConversion(Items.EGG, new GristSet(new GristType[] {GristType.AMBER}, new int[] {5}));
		AlchemyCostRegistry.addGristConversion(Items.BEEF, new GristSet(new GristType[] {GristType.IODINE}, new int[] {12}));
		AlchemyCostRegistry.addGristConversion(Items.PORKCHOP, new GristSet(new GristType[] {GristType.IODINE}, new int[] {10}));
		AlchemyCostRegistry.addGristConversion(Items.CHICKEN, new GristSet(new GristType[] {GristType.IODINE}, new int[] {10}));
		AlchemyCostRegistry.addGristConversion(Items.COD, new GristSet(new GristType[] {GristType.CAULK, GristType.AMBER, GristType.COBALT}, new int[] {4, 4, 2}));
		AlchemyCostRegistry.addGristConversion(Items.MUTTON, new GristSet(GristType.IODINE, 10));
		AlchemyCostRegistry.addGristConversion(Items.RABBIT, new GristSet(GristType.IODINE, 8));
		AlchemyCostRegistry.addGristConversion(Items.COOKED_BEEF, new GristSet(new GristType[] {GristType.IODINE, GristType.TAR}, new int[] {12, 1}));
		AlchemyCostRegistry.addGristConversion(Items.COOKED_PORKCHOP, new GristSet(new GristType[] {GristType.IODINE, GristType.TAR}, new int[] {10, 4}));
		AlchemyCostRegistry.addGristConversion(Items.COOKED_CHICKEN, new GristSet(new GristType[] {GristType.IODINE, GristType.TAR}, new int[] {10, 1}));
		AlchemyCostRegistry.addGristConversion(Items.COOKED_COD, new GristSet(new GristType[] {GristType.CAULK, GristType.AMBER, GristType.COBALT, GristType.TAR}, new int[] {4, 4, 2, 1}));
		AlchemyCostRegistry.addGristConversion(Items.COOKED_MUTTON, new GristSet(new GristType[] {GristType.IODINE, GristType.TAR}, new int[] {10, 1}));
		AlchemyCostRegistry.addGristConversion(Items.COOKED_RABBIT, new GristSet(new GristType[] {GristType.IODINE, GristType.TAR}, new int[] {8, 1}));
		AlchemyCostRegistry.addGristConversion(Items.ENCHANTED_GOLDEN_APPLE, new GristSet(new GristType[] {GristType.AMBER, GristType.GOLD, GristType.URANIUM}, new int[] {4, 150, 10}));
		
		//Potions
		/*GristRegistry.addGristConversion(Items.POTIONITEM, 1, 0), true, new GristSet(new GristType[] {GristType.BUILD, GristType.COBALT}, new int[] {1, 4}));	//water
		GristRegistry.addGristConversion(Items.POTIONITEM, 1, 8192), true, new GristSet(new GristType[] {GristType.BUILD, GristType.COBALT}, new int[] {1, 4}));	//mundane
		GristRegistry.addGristConversion(Items.POTIONITEM, 1, 64), true, new GristSet(new GristType[] {GristType.BUILD, GristType.COBALT, GristType.GARNET}, new int[] {1, 4, 1}));	//mundane from using redstone
		GristRegistry.addGristConversion(Items.POTIONITEM, 1, 16), true, new GristSet(new GristType[] {GristType.BUILD, GristType.COBALT, GristType.IODINE, GristType.TAR}, new int[] {1, 4, 1, 2}));	//awkward
		GristRegistry.addGristConversion(Items.POTIONITEM, 1, 32), true, new GristSet(new GristType[] {GristType.BUILD, GristType.COBALT, GristType.TAR, GristType.CHALK}, new int[] {1, 4, 1, 2}));	//thick
		GristRegistry.addGristConversion(Items.POTIONITEM, 1, 8200), true, new GristSet(new GristType[] {GristType.BUILD, GristType.COBALT, GristType.AMBER, GristType.IODINE}, new int[] {1, 4, 3, 2}));	//weakness
		GristRegistry.addGristConversion(Items.POTIONITEM, 1, 8193), true, new GristSet(new GristType[] {GristType.BUILD, GristType.COBALT, GristType.IODINE, GristType.TAR, GristType.CHALK}, new int[] {1, 8, 1, 2, 5}));	//regen*/
		//TODO Continue with potion grist costs
		/*GristRegistry.addGristConversion(Items.potionitem, 1, 10), true, new GristSet(new GristType[] {GristType.BUILD, GristType.COBALT, GristType.IODINE, GristType.AMBER, GristType.TAR}, new int[] {1, 4, 8, 6, 2}));	//slowness
		GristRegistry.addGristConversion(Items.potionitem, 1, 12), true, new GristSet(new GristType[] {GristType.BUILD, GristType.COBALT, GristType.GOLD, GristType.AMBER, GristType.IODINE, GristType.TAR, GristType.CHALK}, new int[] {1, 4, 16, 7, 4, 2, 1}));	//harming
		GristRegistry.addGristConversion(Items.potionitem, 1, 14), true, new GristSet(new GristType[] {GristType.BUILD, GristType.COBALT, GristType.AMBER, GristType.CHALK, GristType.GOLD, GristType.IODINE, GristType.TAR}, new int[] {1, 4, 9, 1, 16, 4, 2}));	//invisibility
		GristRegistry.addGristConversion(Items.potionitem, 1, 2), true, new GristSet(new GristType[] {GristType.BUILD, GristType.COBALT, GristType.IODINE}, new int[] {1, 4, 4}));	//speed
		GristRegistry.addGristConversion(Items.potionitem, 1, 3), true, new GristSet(new GristType[] {GristType.BUILD, GristType.COBALT, GristType.TAR, GristType.URANIUM, GristType.CAULK}, new int[] {1, 4, 8, 1, 8}));	//fire resistance
		GristRegistry.addGristConversion(Items.potionitem, 1, 4), true, new GristSet(new GristType[] {GristType.BUILD, GristType.COBALT, GristType.AMBER, GristType.IODINE}, new int[] {1, 4, 4, 2}));	//poison
		GristRegistry.addGristConversion(Items.potionitem, 1, 5), true, new GristSet(new GristType[] {GristType.BUILD, GristType.COBALT, GristType.GOLD, GristType.AMBER, GristType.CHALK}, new int[] {1, 4, 16, 1, 1}));	//healing
		GristRegistry.addGristConversion(Items.potionitem, 1, 6), true, new GristSet(new GristType[] {GristType.BUILD, GristType.COBALT, GristType.AMBER, GristType.CHALK, GristType.GOLD}, new int[] {1, 4, 3, 1, 16}));	//night vision
		GristRegistry.addGristConversion(Items.potionitem, 1, 9), true, new GristSet(new GristType[] {GristType.BUILD, GristType.TAR, GristType.URANIUM}, new int[] {1, 8, 1}));	//strength*/
		
		//Set up Punch Designix recipes
		
		//Wood
		final IItemProvider[][] woodItems = {
				{Blocks.OAK_LOG, Blocks.SPRUCE_LOG, Blocks.BIRCH_LOG, Blocks.JUNGLE_LOG, Blocks.ACACIA_LOG, Blocks.DARK_OAK_LOG},
				{Blocks.OAK_PLANKS, Blocks.SPRUCE_PLANKS, Blocks.BIRCH_PLANKS, Blocks.JUNGLE_PLANKS, Blocks.ACACIA_PLANKS, Blocks.DARK_OAK_PLANKS},
				{Blocks.OAK_SLAB, Blocks.SPRUCE_SLAB, Blocks.BIRCH_SLAB, Blocks.JUNGLE_SLAB, Blocks.ACACIA_SLAB, Blocks.DARK_OAK_SLAB},
				{Blocks.OAK_STAIRS, Blocks.SPRUCE_STAIRS, Blocks.BIRCH_STAIRS, Blocks.JUNGLE_STAIRS, Blocks.ACACIA_STAIRS, Blocks.DARK_OAK_STAIRS},
				{Blocks.OAK_SAPLING, Blocks.SPRUCE_SAPLING, Blocks.BIRCH_SAPLING, Blocks.JUNGLE_SAPLING, Blocks.ACACIA_SAPLING, Blocks.DARK_OAK_SAPLING},
				{Blocks.OAK_LEAVES, Blocks.SPRUCE_LEAVES, Blocks.BIRCH_LEAVES, Blocks.JUNGLE_LEAVES,Blocks.ACACIA_LEAVES, Blocks.DARK_OAK_LEAVES},
				{Blocks.OAK_DOOR, Blocks.SPRUCE_DOOR, Blocks.BIRCH_DOOR, Blocks.JUNGLE_DOOR, Blocks.ACACIA_DOOR, Blocks.DARK_OAK_DOOR},
				{Blocks.OAK_FENCE, Blocks.SPRUCE_FENCE, Blocks.BIRCH_FENCE, Blocks.JUNGLE_FENCE, Blocks.ACACIA_FENCE, Blocks.DARK_OAK_FENCE},
				{Blocks.OAK_FENCE_GATE, Blocks.SPRUCE_FENCE_GATE, Blocks.BIRCH_FENCE_GATE, Blocks.JUNGLE_FENCE_GATE, Blocks.ACACIA_FENCE_GATE, Blocks.DARK_OAK_FENCE_GATE}};
		
		for(int i1 = 0; i1 < woodItems.length; i1++)
		{
			CombinationRegistry.addCombination(woodItems[i1][0], woodItems[i1][1], MODE_OR, new ItemStack(woodItems[i1][5]));	//Oak | spruce -> dark oak
			CombinationRegistry.addCombination(woodItems[i1][2], woodItems[i1][3], MODE_OR, new ItemStack(woodItems[i1][4]));	//Birch | jungle -> acacia
		}
		for(int i2 = 0; i2 < woodItems[0].length; i2++)
		{
			CombinationRegistry.addCombination(woodItems[1][i2], woodItems[2][i2], MODE_OR, new ItemStack(woodItems[3][i2]));	//plank | slab -> stair
			CombinationRegistry.addCombination(woodItems[0][i2], woodItems[5][i2], MODE_OR, new ItemStack(woodItems[4][i2]));	//leaf | log -> sapling
			CombinationRegistry.addCombination(woodItems[6][i2], woodItems[7][i2], MODE_OR, new ItemStack(woodItems[8][i2]));	//door | fence -> fence gate
			CombinationRegistry.addCombination(Items.WHEAT_SEEDS, woodItems[5][i2], MODE_AND, new ItemStack(woodItems[4][i2]));
			CombinationRegistry.addCombination(Items.STICK, woodItems[5][i2], MODE_AND, new ItemStack(woodItems[4][i2]));
		}
		CombinationRegistry.addCombination(woodItems[1][0], woodItems[2][1], MODE_OR, new ItemStack(woodItems[3][5]));
		CombinationRegistry.addCombination(woodItems[2][0], woodItems[1][1], MODE_OR, new ItemStack(woodItems[3][5]));
		CombinationRegistry.addCombination(woodItems[0][0], woodItems[5][1], MODE_OR, new ItemStack(woodItems[4][5]));
		CombinationRegistry.addCombination(woodItems[5][0], woodItems[0][1], MODE_OR, new ItemStack(woodItems[4][5]));
		CombinationRegistry.addCombination(woodItems[6][0], woodItems[7][1], MODE_OR, new ItemStack(woodItems[8][5]));
		CombinationRegistry.addCombination(woodItems[7][0], woodItems[6][1], MODE_OR, new ItemStack(woodItems[8][5]));
		
		CombinationRegistry.addCombination(woodItems[1][2], woodItems[2][3], MODE_OR, new ItemStack(woodItems[3][4]));
		CombinationRegistry.addCombination(woodItems[2][2], woodItems[1][3], MODE_OR, new ItemStack(woodItems[3][4]));
		CombinationRegistry.addCombination(woodItems[0][2], woodItems[5][3], MODE_OR, new ItemStack(woodItems[4][4]));
		CombinationRegistry.addCombination(woodItems[5][2], woodItems[0][3], MODE_OR, new ItemStack(woodItems[4][4]));
		CombinationRegistry.addCombination(woodItems[6][2], woodItems[7][3], MODE_OR, new ItemStack(woodItems[8][4]));
		CombinationRegistry.addCombination(woodItems[7][2], woodItems[6][3], MODE_OR, new ItemStack(woodItems[8][4]));
		
		CombinationRegistry.addCombination(ItemTags.WOODEN_DOORS, Items.IRON_INGOT, MODE_AND, new ItemStack(Blocks.IRON_DOOR));
		//CombinationRegistry.addCombination("fenceWood", Blocks.NETHER_BRICKS, MODE_AND, new ItemStack(Blocks.NETHER_BRICK_FENCE));
		CombinationRegistry.addCombination(ItemTags.WOODEN_STAIRS, Blocks.NETHER_BRICKS, MODE_AND, new ItemStack(Blocks.NETHER_BRICK_STAIRS));
		//CombinationRegistry.addCombination("fenceWood", Items.NETHER_BRICK, MODE_AND, new ItemStack(Blocks.NETHER_BRICK_FENCE));
		CombinationRegistry.addCombination(ItemTags.WOODEN_STAIRS, Items.NETHER_BRICK, MODE_AND, new ItemStack(Blocks.NETHER_BRICK_STAIRS));
		//CombinationRegistry.addCombination("doorWood", "slabWood", MODE_AND, Blocks.TRAPDOOR)); TODO Add trapdoors to the contraption above
		CombinationRegistry.addCombination(ItemTags.LOGS, Items.COAL, MODE_AND, new ItemStack(Items.CHARCOAL));
		
		//Dye
		/*Block[] coloredBlocks = {Blocks.WOOL, Blocks.STAINED_HARDENED_CLAY, Blocks.STAINED_GLASS, Blocks.STAINED_GLASS_PANE};
		for(int i1 = 0; i1 < coloredBlocks.length; i1++) TODO Let me just get through this class and deal with colored stuff later...
		{
			for (EnumDyeColor color : EnumDyeColor.values())
			{
				if(color != EnumDyeColor.WHITE)
					CombinationRegistry.addCombination(new ItemStack(Items.DYE, 1, color.getDyeDamage()), coloredBlocks[i1], 1, EnumDyeColor.WHITE.getMetadata()), MODE_OR, new ItemStack(coloredBlocks[i1], 1, color.getMetadata()));
			}
		}
		for (EnumDyeColor color : EnumDyeColor.values())
		{
			CombinationRegistry.addCombination(Blocks.GLASS, Items.DYE, 1, color.getDyeDamage()), MODE_AND, Blocks.STAINED_GLASS, 1, color.getMetadata()));
			CombinationRegistry.addCombination(Blocks.GLASS_PANE, Items.DYE, 1, color.getDyeDamage()), MODE_AND, Blocks.STAINED_GLASS_PANE, 1, color.getMetadata()));
			CombinationRegistry.addCombination(Blocks.HARDENED_CLAY, Items.DYE, 1, color.getDyeDamage()), MODE_AND, Blocks.STAINED_HARDENED_CLAY, 1, color.getMetadata()));
		}*/
		
		//ore related
		CombinationRegistry.addCombination(Items.COAL, Blocks.STONE, MODE_OR, new ItemStack(Blocks.COAL_BLOCK));
		CombinationRegistry.addCombination(Items.DIAMOND, Blocks.STONE, MODE_OR, new ItemStack(Blocks.DIAMOND_BLOCK));
		CombinationRegistry.addCombination(Items.LAPIS_LAZULI, Blocks.STONE, MODE_OR, new ItemStack(Blocks.LAPIS_BLOCK));
		CombinationRegistry.addCombination(Items.EMERALD, Blocks.STONE, MODE_OR, new ItemStack(Blocks.EMERALD_BLOCK));
		CombinationRegistry.addCombination(Items.GOLD_INGOT, Blocks.STONE, MODE_OR, new ItemStack(Blocks.GOLD_BLOCK));
		CombinationRegistry.addCombination(Items.IRON_INGOT, Blocks.STONE, MODE_OR, new ItemStack(Blocks.IRON_BLOCK));
		CombinationRegistry.addCombination(Items.QUARTZ, Blocks.STONE, MODE_OR, new ItemStack(Blocks.QUARTZ_BLOCK));
		CombinationRegistry.addCombination(Items.REDSTONE, Blocks.STONE, MODE_OR, new ItemStack(Blocks.REDSTONE_BLOCK));
		
		if(oreMultiplier != 0)
		{
			CombinationRegistry.addCombination(Items.COAL, Blocks.STONE, MODE_AND, new ItemStack(Blocks.COAL_ORE));
			CombinationRegistry.addCombination(Items.DIAMOND, Blocks.STONE, MODE_AND, new ItemStack(Blocks.DIAMOND_ORE));
			CombinationRegistry.addCombination(Items.LAPIS_LAZULI, Blocks.STONE, MODE_AND, new ItemStack(Blocks.LAPIS_ORE));
			CombinationRegistry.addCombination(Items.EMERALD, Blocks.STONE, MODE_AND, new ItemStack(Blocks.EMERALD_ORE));
			CombinationRegistry.addCombination(Items.GOLD_INGOT, Blocks.STONE, MODE_AND, new ItemStack(Blocks.GOLD_ORE));
			CombinationRegistry.addCombination(Items.IRON_INGOT, Blocks.STONE, MODE_AND, new ItemStack(Blocks.IRON_ORE));
			CombinationRegistry.addCombination(Items.QUARTZ, Blocks.NETHERRACK, MODE_AND, new ItemStack(Blocks.NETHER_QUARTZ_ORE));
			CombinationRegistry.addCombination(Items.REDSTONE, Blocks.STONE, MODE_AND, new ItemStack(Blocks.REDSTONE_ORE));
		}
		
		CombinationRegistry.addCombination(Blocks.STONE, Blocks.CRACKED_STONE_BRICKS, MODE_AND, new ItemStack(Blocks.STONE_BRICKS));
		CombinationRegistry.addCombination(Blocks.STONE, Blocks.CRACKED_STONE_BRICKS, MODE_OR, new ItemStack(Blocks.COBBLESTONE));
		CombinationRegistry.addCombination(Blocks.STONE, Blocks.GRAVEL, MODE_OR, new ItemStack(Blocks.COBBLESTONE));
		CombinationRegistry.addCombination(Blocks.STONE, Blocks.SAND, MODE_OR, new ItemStack(Blocks.SANDSTONE));
		CombinationRegistry.addCombination(Blocks.COBBLESTONE, Blocks.CUT_SANDSTONE, MODE_AND, new ItemStack(Blocks.STONE));
		CombinationRegistry.addCombination(Blocks.COBBLESTONE, Blocks.CUT_SANDSTONE, MODE_OR, new ItemStack(Blocks.SANDSTONE));
		CombinationRegistry.addCombination(ItemTags.SAND, Blocks.COBBLESTONE, MODE_AND, new ItemStack(Blocks.GRAVEL));
		CombinationRegistry.addCombination(Blocks.COBBLESTONE, Blocks.SAND, MODE_OR, new ItemStack(Blocks.SANDSTONE));
		CombinationRegistry.addCombination(Blocks.GRAVEL, Blocks.CUT_SANDSTONE, MODE_OR, new ItemStack(Blocks.SANDSTONE));
		CombinationRegistry.addCombination(Blocks.CUT_SANDSTONE, Blocks.SAND, MODE_OR, new ItemStack(Blocks.SANDSTONE));
		
		//misc
		CombinationRegistry.addCombination(Blocks.COBBLESTONE, Items.COAL, MODE_AND, new ItemStack(Blocks.FURNACE));
		CombinationRegistry.addCombination(Blocks.COBBLESTONE, Items.WHEAT_SEEDS, MODE_OR, new ItemStack(Blocks.MOSSY_COBBLESTONE));
		CombinationRegistry.addCombination(Blocks.COBBLESTONE_WALL, Items.WHEAT_SEEDS, MODE_OR, new ItemStack(Blocks.MOSSY_COBBLESTONE_WALL));
		CombinationRegistry.addCombination(Blocks.DIRT, Blocks.GRASS, MODE_OR, new ItemStack(Blocks.GRASS_BLOCK));
		CombinationRegistry.addCombination(Blocks.DIRT, Items.WHEAT_SEEDS, MODE_AND, new ItemStack(Blocks.GRASS_BLOCK));
		CombinationRegistry.addCombination(Blocks.GRASS_BLOCK, Blocks.BROWN_MUSHROOM, MODE_AND, new ItemStack(Blocks.MYCELIUM));
		CombinationRegistry.addCombination(Blocks.GRASS_BLOCK, Blocks.RED_MUSHROOM, MODE_AND, new ItemStack(Blocks.MYCELIUM));
		CombinationRegistry.addCombination(Blocks.LADDER, Items.IRON_INGOT, MODE_AND, new ItemStack(Blocks.RAIL));
		CombinationRegistry.addCombination(Blocks.NETHERRACK, Blocks.BRICKS, MODE_AND, new ItemStack(Blocks.NETHER_BRICKS));
		CombinationRegistry.addCombination(Blocks.NETHERRACK, Items.BRICK, MODE_AND, new ItemStack(Blocks.NETHER_BRICKS));
		CombinationRegistry.addCombination(Blocks.NETHERRACK, Items.BRICK, MODE_OR, new ItemStack(Items.NETHER_BRICK));
		CombinationRegistry.addCombination(Blocks.NETHER_BRICKS, Items.BRICK, MODE_OR, new ItemStack(Items.NETHER_BRICK));
		CombinationRegistry.addCombination(Blocks.NETHERRACK, Items.GLOWSTONE_DUST, MODE_AND, new ItemStack(Blocks.GLOWSTONE));
		CombinationRegistry.addCombination(Blocks.NOTE_BLOCK, Items.DIAMOND, MODE_AND, new ItemStack(Blocks.JUKEBOX));
		CombinationRegistry.addCombination(ItemTags.PLANKS, Blocks.RAIL, MODE_AND, new ItemStack(Blocks.LADDER));
		CombinationRegistry.addCombination(Blocks.OAK_SAPLING, Items.WHEAT_SEEDS, MODE_AND, new ItemStack(Items.APPLE));
		CombinationRegistry.addCombination(Blocks.OAK_LEAVES, Items.WHEAT_SEEDS, MODE_OR, new ItemStack(Items.APPLE));
		CombinationRegistry.addCombination(Blocks.STONE, Items.ENDER_PEARL, MODE_AND, new ItemStack(Blocks.END_STONE));
		CombinationRegistry.addCombination(Blocks.STONE_BRICKS, Items.WHEAT_SEEDS, MODE_OR, new ItemStack(Blocks.MOSSY_STONE_BRICKS));
		CombinationRegistry.addCombination(Items.APPLE, Items.GOLD_INGOT, MODE_AND, new ItemStack(Items.GOLDEN_APPLE));
		CombinationRegistry.addCombination(Items.APPLE, Items.GOLD_NUGGET, MODE_AND, new ItemStack(Items.GOLDEN_APPLE));
		CombinationRegistry.addCombination(Items.APPLE, Blocks.GOLD_BLOCK, MODE_AND, new ItemStack(Items.ENCHANTED_GOLDEN_APPLE));
		CombinationRegistry.addCombination(Items.CARROT, Items.WHEAT_SEEDS, MODE_AND, new ItemStack(Items.POTATO));
		CombinationRegistry.addCombination(Items.CLOCK, Items.IRON_INGOT, MODE_AND, new ItemStack(Items.COMPASS));
		CombinationRegistry.addCombination(Items.COMPASS, Items.GOLD_INGOT, MODE_AND, new ItemStack(Items.CLOCK));
		CombinationRegistry.addCombination(Items.DIAMOND, Items.SADDLE, MODE_AND, new ItemStack(Items.DIAMOND_HORSE_ARMOR));
		CombinationRegistry.addCombination(Items.ENDER_EYE, Items.EGG, MODE_AND, new ItemStack(Blocks.DRAGON_EGG));
		CombinationRegistry.addCombination(Items.ENDER_PEARL, Items.BLAZE_POWDER, MODE_AND, new ItemStack(Items.ENDER_EYE));
		CombinationRegistry.addCombination(Items.GOLD_INGOT, Items.SADDLE, MODE_AND, new ItemStack(Items.GOLDEN_HORSE_ARMOR));
		CombinationRegistry.addCombination(ItemTags.SAND, Items.GUNPOWDER, MODE_AND, new ItemStack(Blocks.TNT));
		CombinationRegistry.addCombination(Items.IRON_INGOT, Blocks.GRASS_BLOCK, MODE_AND, new ItemStack(Items.SHEARS));
		CombinationRegistry.addCombination(Items.IRON_INGOT, Items.SADDLE, MODE_AND, new ItemStack(Items.IRON_HORSE_ARMOR));
		CombinationRegistry.addCombination(Items.POTATO, Items.WHEAT_SEEDS, MODE_OR, new ItemStack(Items.CARROT));
		CombinationRegistry.addCombination(Items.REDSTONE, Items.GOLD_INGOT, MODE_OR, new ItemStack(Items.CLOCK));
		CombinationRegistry.addCombination(Items.REDSTONE, Items.IRON_INGOT, MODE_OR, new ItemStack(Items.COMPASS));
		CombinationRegistry.addCombination(Items.ROTTEN_FLESH, Items.CARROT, MODE_OR, new ItemStack(Items.PORKCHOP));
		CombinationRegistry.addCombination(Items.ROTTEN_FLESH, Items.WATER_BUCKET, MODE_OR, new ItemStack(Items.LEATHER));
		CombinationRegistry.addCombination(Items.ROTTEN_FLESH, Items.WHEAT, MODE_OR, new ItemStack(Items.BEEF));
		CombinationRegistry.addCombination(Items.ROTTEN_FLESH, Items.WHEAT_SEEDS, MODE_OR, new ItemStack(Items.CHICKEN));
		CombinationRegistry.addCombination(Items.SLIME_BALL, Items.BLAZE_POWDER, MODE_AND, new ItemStack(Items.MAGMA_CREAM));
		CombinationRegistry.addCombination(Items.STICK, Items.LAVA_BUCKET, MODE_AND, new ItemStack(Items.BLAZE_ROD));
		CombinationRegistry.addCombination(Items.STRING, Items.LEATHER, MODE_AND, new ItemStack(Items.SADDLE));
		CombinationRegistry.addCombination(Items.REDSTONE, Items.LAVA_BUCKET, MODE_OR, new ItemStack(Items.BLAZE_POWDER));
		CombinationRegistry.addCombination(Items.REDSTONE, Blocks.NETHERRACK, MODE_OR, new ItemStack(Items.BLAZE_POWDER));
		CombinationRegistry.addCombination(Items.STICK, Items.BLAZE_POWDER, MODE_AND, new ItemStack(Items.BLAZE_ROD));
		CombinationRegistry.addCombination(ItemTags.BOATS, Blocks.RAIL, MODE_OR, new ItemStack(Items.MINECART));
		CombinationRegistry.addCombination(ItemTags.WOODEN_TRAPDOORS, Items.IRON_INGOT, MODE_OR, new ItemStack(Blocks.IRON_TRAPDOOR));
		CombinationRegistry.addCombination(ItemTags.WOODEN_TRAPDOORS, Blocks.IRON_BLOCK, MODE_OR, new ItemStack(Blocks.IRON_TRAPDOOR));
		CombinationRegistry.addCombination(Items.STICK, Blocks.IRON_BARS, MODE_AND, new ItemStack(Blocks.RAIL));
		CombinationRegistry.addCombination(Blocks.COBBLESTONE_WALL, Blocks.MOSSY_COBBLESTONE, MODE_OR, new ItemStack(Blocks.MOSSY_COBBLESTONE_WALL));
		CombinationRegistry.addCombination(Blocks.DISPENSER, Blocks.HOPPER, MODE_AND, new ItemStack(Blocks.DROPPER));
		CombinationRegistry.addCombination(Blocks.TORCH, Items.REDSTONE, MODE_AND, new ItemStack(Blocks.REDSTONE_TORCH));
		CombinationRegistry.addCombination(Blocks.TORCH, Items.REDSTONE, MODE_OR, new ItemStack(Items.GLOWSTONE_DUST));
		CombinationRegistry.addCombination(Items.MINECART, Blocks.CHEST, MODE_AND, new ItemStack(Items.CHEST_MINECART));
		CombinationRegistry.addCombination(Items.MINECART, Blocks.FURNACE, MODE_AND, new ItemStack(Items.FURNACE_MINECART));
		CombinationRegistry.addCombination(Items.MINECART, Blocks.TNT, MODE_AND, new ItemStack(Items.TNT_MINECART));
		CombinationRegistry.addCombination(Items.MINECART, Blocks.HOPPER, MODE_AND, new ItemStack(Items.HOPPER_MINECART));
		CombinationRegistry.addCombination(Blocks.RAIL, Blocks.REDSTONE_TORCH, MODE_AND, new ItemStack(Blocks.ACTIVATOR_RAIL));
		CombinationRegistry.addCombination(ItemTags.WOODEN_PRESSURE_PLATES, Blocks.RAIL, MODE_AND, new ItemStack(Blocks.DETECTOR_RAIL));
		CombinationRegistry.addCombination(Blocks.RAIL, Blocks.STONE_PRESSURE_PLATE, MODE_AND, new ItemStack(Blocks.DETECTOR_RAIL));
		CombinationRegistry.addCombination(Blocks.RAIL, Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE, MODE_AND, new ItemStack(Blocks.DETECTOR_RAIL));
		CombinationRegistry.addCombination(Blocks.RAIL, Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE, MODE_AND, new ItemStack(Blocks.DETECTOR_RAIL));
		CombinationRegistry.addCombination(Blocks.RAIL, Items.GOLD_INGOT, MODE_AND, new ItemStack(Blocks.POWERED_RAIL));
		CombinationRegistry.addCombination(Blocks.RAIL, Items.FURNACE_MINECART, MODE_OR, new ItemStack(Blocks.POWERED_RAIL));
		CombinationRegistry.addCombination(Blocks.GLOWSTONE, Blocks.REDSTONE_TORCH, MODE_AND, new ItemStack(Blocks.REDSTONE_LAMP));
		CombinationRegistry.addCombination(Items.PRISMARINE_SHARD, Items.DIAMOND, MODE_OR, new ItemStack(Items.PRISMARINE_CRYSTALS));
		CombinationRegistry.addCombination(Items.PRISMARINE_SHARD, Items.EMERALD, MODE_OR, new ItemStack(Items.PRISMARINE_CRYSTALS));
		CombinationRegistry.addCombination(ItemTags.SAND, Blocks.GRASS, MODE_OR, new ItemStack(Blocks.DEAD_BUSH));
		CombinationRegistry.addCombination(ItemTags.SAND, Blocks.GRASS, MODE_AND, new ItemStack(Blocks.CACTUS));
		CombinationRegistry.addCombination(ItemTags.SAPLINGS, ItemTags.SAND, MODE_AND, new ItemStack(Blocks.DEAD_BUSH));
		CombinationRegistry.addCombination(Items.ENDER_PEARL, Blocks.CHEST, MODE_AND, new ItemStack(Blocks.ENDER_CHEST));
		CombinationRegistry.addCombination(Blocks.GLASS, Blocks.SNOW, MODE_AND, new ItemStack(Blocks.ICE));
		CombinationRegistry.addCombination(Blocks.SPONGE, Items.WATER_BUCKET, MODE_AND, new ItemStack(Blocks.WET_SPONGE));
		CombinationRegistry.addCombination(Items.BLAZE_POWDER, Items.GUNPOWDER, MODE_OR, new ItemStack(Items.FIRE_CHARGE));
		CombinationRegistry.addCombination(Blocks.SAND, Blocks.STONE_BRICK_STAIRS, MODE_OR, new ItemStack(Blocks.SANDSTONE_STAIRS));
		CombinationRegistry.addCombination(Blocks.RED_SAND, Blocks.STONE_BRICK_STAIRS, MODE_OR, new ItemStack(Blocks.RED_SANDSTONE_STAIRS));
		CombinationRegistry.addCombination(Blocks.SAND, Items.ROSE_RED, MODE_AND, new ItemStack(Blocks.RED_SAND));
		CombinationRegistry.addCombination(Blocks.SANDSTONE, Items.ROSE_RED, MODE_AND, new ItemStack(Blocks.RED_SANDSTONE));
		CombinationRegistry.addCombination(Blocks.CUT_SANDSTONE, Items.ROSE_RED, MODE_AND, new ItemStack(Blocks.CUT_RED_SANDSTONE));
		CombinationRegistry.addCombination(Blocks.CHISELED_SANDSTONE, Items.ROSE_RED, MODE_AND, new ItemStack(Blocks.CHISELED_RED_SANDSTONE));
		CombinationRegistry.addCombination(Blocks.SMOOTH_SANDSTONE, Items.ROSE_RED, MODE_AND, new ItemStack(Blocks.SMOOTH_RED_SANDSTONE));
		CombinationRegistry.addCombination(Blocks.SANDSTONE_STAIRS, Items.ROSE_RED, MODE_AND, new ItemStack(Blocks.RED_SANDSTONE_STAIRS));
		CombinationRegistry.addCombination(ItemTags.PLANKS, Items.BOOK, MODE_OR, new ItemStack(Blocks.BOOKSHELF));
		//CombinationRegistry.addCombination("record", Blocks.NOTE_BLOCK, MODE_AND, new ItemStack(Blocks.JUKEBOX));
		CombinationRegistry.addCombination(Items.STICK, Blocks.VINE, MODE_AND, new ItemStack(Blocks.LADDER));
		CombinationRegistry.addCombination(ItemTags.LEAVES, Blocks.LADDER, MODE_OR, new ItemStack(Blocks.VINE));
		CombinationRegistry.addCombination(Items.PRISMARINE_SHARD, Blocks.COBBLESTONE, MODE_AND, new ItemStack(Blocks.PRISMARINE));
		CombinationRegistry.addCombination(Items.PRISMARINE_SHARD, Blocks.STONE_BRICKS, MODE_AND, new ItemStack(Blocks.PRISMARINE_BRICKS));
		CombinationRegistry.addCombination(Blocks.PRISMARINE, Items.INK_SAC, MODE_AND, new ItemStack(Blocks.DARK_PRISMARINE));
		CombinationRegistry.addCombination(Items.PRISMARINE_SHARD, Blocks.GLOWSTONE, MODE_OR, new ItemStack(Blocks.SEA_LANTERN));
		CombinationRegistry.addCombination(Items.PRISMARINE_CRYSTALS, Blocks.PRISMARINE, MODE_AND, new ItemStack(Blocks.SEA_LANTERN));
		CombinationRegistry.addCombination(Blocks.POPPY, Items.CHORUS_FRUIT, MODE_AND, new ItemStack(Blocks.CHORUS_FLOWER));
		CombinationRegistry.addCombination(Blocks.DANDELION, Items.CHORUS_FRUIT, MODE_AND, new ItemStack(Blocks.CHORUS_FLOWER));
		CombinationRegistry.addCombination(Blocks.RED_MUSHROOM, Blocks.SOUL_SAND, MODE_AND, new ItemStack(Items.NETHER_WART));
		CombinationRegistry.addCombination(Blocks.PRISMARINE, Items.FLINT, MODE_OR, new ItemStack(Items.PRISMARINE_SHARD));
		CombinationRegistry.addCombination(Blocks.PRISMARINE_BRICKS, Items.FLINT, MODE_OR, new ItemStack(Items.PRISMARINE_SHARD));
		CombinationRegistry.addCombination(Blocks.DARK_PRISMARINE, Items.FLINT, MODE_OR, new ItemStack(Items.PRISMARINE_SHARD));
		CombinationRegistry.addCombination(Blocks.COBBLESTONE, Blocks.STONE_BRICKS, MODE_AND, new ItemStack(Blocks.CRACKED_STONE_BRICKS));
		CombinationRegistry.addCombination(Items.GUNPOWDER, Blocks.STONE_BUTTON, MODE_OR, new ItemStack(Items.REDSTONE));
		CombinationRegistry.addCombination(Items.GUNPOWDER, Blocks.STONE_PRESSURE_PLATE, MODE_OR, new ItemStack(Items.REDSTONE));
		CombinationRegistry.addCombination(Items.GUNPOWDER, Blocks.LEVER, MODE_OR, new ItemStack(Items.REDSTONE));
		CombinationRegistry.addCombination(ItemTags.WOODEN_BUTTONS, Items.GUNPOWDER, MODE_OR, new ItemStack(Blocks.REDSTONE_TORCH));
		CombinationRegistry.addCombination(ItemTags.WOODEN_PRESSURE_PLATES, Items.GUNPOWDER, MODE_OR, new ItemStack(Blocks.REDSTONE_TORCH));
		CombinationRegistry.addCombination(Blocks.GRAVEL, Items.ROSE_RED, MODE_AND, new ItemStack(Items.REDSTONE));
		CombinationRegistry.addCombination(Blocks.PUMPKIN, STRAWBERRY, MODE_OR, new ItemStack(Blocks.MELON));
		CombinationRegistry.addCombination(Blocks.MELON, Items.CARROT, MODE_AND, new ItemStack(Blocks.PUMPKIN));
		CombinationRegistry.addCombination(Items.WITHER_SKELETON_SKULL, Items.BONE, MODE_AND, new ItemStack(Items.SKELETON_SKULL));
		CombinationRegistry.addCombination(Items.WITHER_SKELETON_SKULL, Items.BONE_MEAL, MODE_AND, new ItemStack(Items.SKELETON_SKULL));
		CombinationRegistry.addCombination(Items.SKELETON_SKULL, Items.ROTTEN_FLESH, MODE_OR, new ItemStack(Items.ZOMBIE_HEAD));
		CombinationRegistry.addCombination(ItemTags.SAND, Items.NETHER_WART, MODE_OR, new ItemStack(Blocks.SOUL_SAND));
		CombinationRegistry.addCombination(Items.BOOK, Blocks.CHEST, MODE_AND, new ItemStack(Blocks.BOOKSHELF));
		CombinationRegistry.addCombination(Items.WATER_BUCKET, Blocks.YELLOW_WOOL, MODE_AND, new ItemStack(Blocks.WET_SPONGE));
		CombinationRegistry.addCombination(Items.BOOK, Items.EXPERIENCE_BOTTLE, MODE_AND, new ItemStack(Blocks.ENCHANTING_TABLE));
		CombinationRegistry.addCombination(ItemTags.LEAVES, Items.WATER_BUCKET, MODE_OR, new ItemStack(Blocks.LILY_PAD));
		CombinationRegistry.addCombination(Blocks.CRAFTING_TABLE, Blocks.IRON_BLOCK, MODE_OR, new ItemStack(Blocks.ANVIL));
		CombinationRegistry.addCombination(Items.WOODEN_SWORD, Items.ARROW, MODE_OR, new ItemStack(Items.BOW));
		CombinationRegistry.addCombination(Blocks.POPPY, Items.BRICK, MODE_AND, new ItemStack(Blocks.FLOWER_POT));
		CombinationRegistry.addCombination(Blocks.DANDELION, Items.BRICK, MODE_AND, new ItemStack(Blocks.FLOWER_POT));
		CombinationRegistry.addCombination(Blocks.COBBLESTONE, Items.LAVA_BUCKET, MODE_AND, new ItemStack(Blocks.NETHERRACK));
		CombinationRegistry.addCombination(Items.EMERALD, Items.COAL, MODE_AND, new ItemStack(Items.DIAMOND));
		CombinationRegistry.addCombination(Items.EMERALD, Items.LAPIS_LAZULI, MODE_AND, new ItemStack(Items.DIAMOND));
		//CombinationRegistry.addCombination((Items.POTIONITEM, 1, 0), Items.ENCHANTED_BOOK, MODE_OR, new ItemStack(Items.EXPERIENCE_BOTTLE));
		CombinationRegistry.addCombination(Items.GLASS_BOTTLE, Items.ENCHANTED_BOOK, MODE_OR, new ItemStack(Items.EXPERIENCE_BOTTLE));
		CombinationRegistry.addCombination(Items.QUARTZ, Items.WATER_BUCKET, MODE_OR, new ItemStack(Items.PRISMARINE_CRYSTALS));
		CombinationRegistry.addCombination(Items.QUARTZ, Items.WATER_BUCKET, MODE_AND, new ItemStack(Items.PRISMARINE_SHARD));
		CombinationRegistry.addCombination(Items.FEATHER, Items.ENDER_PEARL, MODE_OR, new ItemStack(Items.ELYTRA));
		CombinationRegistry.addCombination(Items.COOKIE, Items.REDSTONE, MODE_AND, new ItemStack(Items.SUGAR));
	}
	
	public static void registerMinestuckRecipes()
	{
		//add grist conversions
		AlchemyCostRegistry.addGristConversion(BLUE_DIRT, new GristSet(new GristType[] {GristType.BUILD, GristType.SHALE}, new int[] {1, 1}));
		AlchemyCostRegistry.addGristConversion(THOUGHT_DIRT, new GristSet(new GristType[] {GristType.BUILD, GristType.CAULK}, new int[] {1, 1}));
		AlchemyCostRegistry.addGristConversion(GRIST_WIDGET, new GristSet(new GristType[] {GristType.BUILD, GristType.GARNET, GristType.RUBY}, new int[] {550, 55, 34}));
		AlchemyCostRegistry.addGristConversion(GENERIC_OBJECT, new GristSet(new GristType[] {GristType.BUILD}, new int[] {2}));
		AlchemyCostRegistry.addGristConversion(CHESSBOARD, new GristSet(new GristType[] {GristType.SHALE, GristType.MARBLE}, new int[] {25, 25}));
		AlchemyCostRegistry.addGristConversion(GRIMOIRE, new GristSet(new GristType[] {GristType.BUILD, GristType.AMETHYST, GristType.GARNET}, new int[] {120, 60, 33}));
		AlchemyCostRegistry.addGristConversion(LONG_FORGOTTEN_WARHORN, new GristSet(new GristType[] {GristType.BUILD, GristType.AMETHYST, GristType.TAR, GristType.GARNET}, new int[] {550, 120, 50, 80}));

		AlchemyCostRegistry.addGristConversion(CRUXITE_APPLE, new GristSet());
		AlchemyCostRegistry.addGristConversion(CRUXITE_POTION, new GristSet());
		
		AlchemyCostRegistry.addGristConversion(CAT_CLAWS_DRAWN, new GristSet(new GristType[] {GristType.BUILD,GristType.RUST},new int[] {15,5}));
		AlchemyCostRegistry.addGristConversion(CAT_CLAWS_SHEATHED, new GristSet(new GristType[] {GristType.BUILD,GristType.RUST},new int[] {15,5}));
		
		AlchemyCostRegistry.addGristConversion(CLAW_HAMMER, new GristSet(GristType.BUILD, 8));
		AlchemyCostRegistry.addGristConversion(SLEDGE_HAMMER, new GristSet(new GristType[] {GristType.BUILD, GristType.SHALE}, new int[] {10, 4}));
		AlchemyCostRegistry.addGristConversion(BLACKSMITH_HAMMER, new GristSet(new GristType[] {GristType.RUST, GristType.SULFUR, GristType.CAULK}, new int[] {8, 9, 5}));
		AlchemyCostRegistry.addGristConversion(POGO_HAMMER, new GristSet(new GristType[] {GristType.BUILD, GristType.SHALE}, new int[] {20, 16}));
		AlchemyCostRegistry.addGristConversion(TELESCOPIC_SASSACRUSHER, new GristSet(new GristType[] {GristType.SHALE, GristType.TAR, GristType.MERCURY}, new int[] {39, 18, 23}));
		AlchemyCostRegistry.addGristConversion(REGI_HAMMER, new GristSet(new GristType[] {GristType.AMETHYST, GristType.TAR,GristType.GOLD}, new int[] {25, 70, 34}));
		AlchemyCostRegistry.addGristConversion(FEAR_NO_ANVIL, new GristSet(new GristType[] {GristType.BUILD, GristType.GARNET, GristType.DIAMOND, GristType.GOLD, GristType.QUARTZ}, new int[] {999, 150, 54, 61, 1}));
		AlchemyCostRegistry.addGristConversion(MELT_MASHER, new GristSet(new GristType[] {GristType.BUILD, GristType.TAR, GristType.GARNET, GristType.DIAMOND, GristType.GOLD, GristType.RUBY, GristType.SULFUR}, new int[] {1000, 400, 200, 340, 100, 150, 450}));
		AlchemyCostRegistry.addGristConversion(Q_E_HAMMER_AXE, new GristSet(new GristType[] {GristType.BUILD, GristType.SHALE, GristType.URANIUM, GristType.RUST}, new int[] {8000, 1280, 640, 300}));
		AlchemyCostRegistry.addGristConversion(D_D_E_HAMMER_AXE, new GristSet(new GristType[] {GristType.ARTIFACT, GristType.BUILD}, new int[] {-100, 1}));
		
		AlchemyCostRegistry.addGristConversion(CACTUS_CUTLASS, new GristSet(new GristType[] {GristType.AMBER, GristType.MARBLE}, new int[] {7, 2}));
		AlchemyCostRegistry.addGristConversion(STEAK_SWORD, new GristSet(new GristType[] {GristType.IODINE, GristType.TAR}, new int[] {55, 18}));
		AlchemyCostRegistry.addGristConversion(SORD, new GristSet(GristType.BUILD, 0));
		AlchemyCostRegistry.addGristConversion(KATANA, new GristSet(new GristType[] {GristType.CHALK, GristType.QUARTZ, GristType.RUST}, new int[] {12, 10, 6}));
		AlchemyCostRegistry.addGristConversion(FIRE_POKER, new GristSet(new GristType[] {GristType.AMBER, GristType.RUBY, GristType.SULFUR, GristType.GOLD}, new int[] {41, 14, 38, 3}));
		AlchemyCostRegistry.addGristConversion(CLAYMORE, new GristSet(new GristType[] {GristType.BUILD, GristType.RUST}, new int[] {400, 240}));
		AlchemyCostRegistry.addGristConversion(HOT_HANDLE, new GristSet(new GristType[] {GristType.AMBER, GristType.RUBY, GristType.SULFUR}, new int[] {10, 15, 10}));
		AlchemyCostRegistry.addGristConversion(REGISWORD, new GristSet(new GristType[] {GristType.AMETHYST, GristType.TAR,GristType.GOLD}, new int[] {27, 62, 38}));
		AlchemyCostRegistry.addGristConversion(UNBREAKABLE_KATANA, new GristSet(new GristType[] {GristType.BUILD, GristType.URANIUM, GristType.QUARTZ, GristType.RUBY}, new int[] {1100, 63, 115, 54}));
		AlchemyCostRegistry.addGristConversion(COBALT_SABRE, new GristSet(new GristType[] {GristType.BUILD, GristType.URANIUM, GristType.COBALT, GristType.DIAMOND}, new int[] {1300, 90, 175, 30}));
		AlchemyCostRegistry.addGristConversion(QUAMTUM_SABRE, new GristSet(new GristType[] {GristType.BUILD, GristType.URANIUM}, new int[] {413, 11}));
		AlchemyCostRegistry.addGristConversion(SHATTER_BEACON, new GristSet(new GristType[] {GristType.BUILD, GristType.COBALT, GristType.DIAMOND, GristType.URANIUM}, new int[] {25, 15, 150, 400}));
		
		AlchemyCostRegistry.addGristConversion(WOODEN_SPOON, new GristSet(GristType.BUILD, 5));
		AlchemyCostRegistry.addGristConversion(SILVER_SPOON, new GristSet(new GristType[] {GristType.BUILD, GristType.MERCURY}, new int[] {6, 4}));
		AlchemyCostRegistry.addGristConversion(CROCKER_SPOON, new GristSet(new GristType[] {GristType.BUILD, GristType.IODINE, GristType.CHALK, GristType.RUBY}, new int[] {90, 34, 34, 6}));
		AlchemyCostRegistry.addGristConversion(CROCKER_FORK, new GristSet(new GristType[] {GristType.BUILD, GristType.IODINE, GristType.CHALK, GristType.RUBY}, new int[] {90, 34, 34, 6}));
		AlchemyCostRegistry.addGristConversion(SKAIA_FORK, new GristSet(new GristType[] {GristType.BUILD, GristType.QUARTZ, GristType.GOLD, GristType.AMETHYST}, new int[]{900, 94, 58, 63}));
		AlchemyCostRegistry.addGristConversion(GOLDEN_SPORK, new GristSet(new GristType[] {GristType.BUILD, GristType.GOLD, GristType.DIAMOND}, new int[]{70, 40, 1}));
		
		AlchemyCostRegistry.addGristConversion(BATLEACKS, new GristSet(GristType.BUILD, 0));
		AlchemyCostRegistry.addGristConversion(COPSE_CRUSHER, new GristSet(new GristType[] {GristType.BUILD, GristType.RUST}, new int[] {25, 15}));
		AlchemyCostRegistry.addGristConversion(BATTLEAXE, new GristSet(new GristType[] {GristType.BUILD, GristType.RUST}, new int[] {400, 240}));
		AlchemyCostRegistry.addGristConversion(BLACKSMITH_BANE, new GristSet (new GristType[] {GristType.BUILD, GristType.RUST, GristType.TAR}, new int[] {30, 15, 12}));
		AlchemyCostRegistry.addGristConversion(SCRAXE, new GristSet (new GristType[] {GristType.BUILD, GristType.TAR, GristType.RUST, GristType.RUBY}, new int[] {139, 86, 43, 8}));
		AlchemyCostRegistry.addGristConversion(Q_P_HAMMER_AXE, new GristSet(new GristType[] {GristType.BUILD, GristType.SHALE, GristType.RUST}, new int[] {150, 64, 15}));
		AlchemyCostRegistry.addGristConversion(RUBY_CROAK, new GristSet (new GristType[] {GristType.BUILD, GristType.GARNET, GristType.RUBY, GristType.DIAMOND}, new int [] {900, 103, 64, 16}));
		AlchemyCostRegistry.addGristConversion(HEPHAESTUS_LUMBER, new GristSet (new GristType[] {GristType.BUILD, GristType.GOLD, GristType.RUBY}, new int[] {625, 49, 36}));
		AlchemyCostRegistry.addGristConversion(Q_F_HAMMER_AXE, new GristSet(new GristType[] {GristType.BUILD, GristType.SHALE, GristType.URANIUM, GristType.RUST}, new int[] {800, 128, 64, 30}));
		
		AlchemyCostRegistry.addGristConversion(REGI_SICKLE, new GristSet(new GristType[] {GristType.AMETHYST, GristType.TAR, GristType.GOLD}, new int[] {25, 57, 33}));
		AlchemyCostRegistry.addGristConversion(SICKLE, new GristSet(new GristType[] {GristType.BUILD}, new int[] {8}));
		AlchemyCostRegistry.addGristConversion(HOMES_SMELL_YA_LATER, new GristSet(new GristType[] {GristType.BUILD, GristType.AMBER, GristType.AMETHYST}, new int[] {34, 19, 10}));
		AlchemyCostRegistry.addGristConversion(FUDGE_SICKLE, new GristSet(new GristType[] {GristType.IODINE, GristType.AMBER, GristType.CHALK}, new int[] {23, 15, 12}));
		AlchemyCostRegistry.addGristConversion(CLAW_OF_NRUBYIGLITH, new GristSet(new GristType[] {GristType.BUILD, GristType.AMETHYST, GristType.CHALK, GristType.GARNET, GristType.SHALE}, new int[] {333, 80, 6, 6, 6}));
		AlchemyCostRegistry.addGristConversion(CANDY_SICKLE, new GristSet(new GristType[] {GristType.IODINE, GristType.GOLD, GristType.CHALK, GristType.AMBER}, new int[] {65, 38, 53, 20}));
		
		AlchemyCostRegistry.addGristConversion(NIGHT_CLUB, new GristSet(new GristType[] {GristType.TAR, GristType.SHALE, GristType.COBALT}, new int[] {28, 19, 6}));
		AlchemyCostRegistry.addGristConversion(POGO_CLUB, new GristSet(new GristType[] {GristType.BUILD, GristType.SHALE}, new int[] {15, 12}));
		AlchemyCostRegistry.addGristConversion(METAL_BAT, new GristSet(new GristType[] {GristType.BUILD, GristType.MERCURY}, new int[] {35, 23}));
		AlchemyCostRegistry.addGristConversion(SPIKED_CLUB, new GristSet(new GristType[] {GristType.BUILD, GristType.GARNET, GristType.IODINE}, new int[] {46, 38, 13}));
		
		AlchemyCostRegistry.addGristConversion(SPEAR_CANE, new GristSet(new GristType[] {GristType.BUILD, GristType.MERCURY, GristType.AMBER}, new int[] {28, 14, 11}));
		AlchemyCostRegistry.addGristConversion(PARADISES_PORTABELLO, new GristSet(new GristType[] {GristType.BUILD, GristType.IODINE, GristType.RUBY}, new int[] {40, 30, 20}));
		AlchemyCostRegistry.addGristConversion(REGI_CANE, new GristSet(new GristType[] {GristType.AMETHYST, GristType.TAR,GristType.GOLD}, new int[] {30, 55, 32}));
		AlchemyCostRegistry.addGristConversion(POGO_CANE, new GristSet(new GristType[] {GristType.BUILD, GristType.SHALE}, new int[] {18, 14}));
		AlchemyCostRegistry.addGristConversion(UP_STICK, new GristSet(new GristType[] {GristType.URANIUM}, new int[] {1}));
		
		
		AlchemyCostRegistry.addGristConversion(CAPTCHAROID_CAMERA, new GristSet(new GristType[] {GristType.BUILD, GristType.CAULK, GristType.GOLD, GristType.MARBLE, GristType.MERCURY, GristType.SHALE}, new int[] {5000, 500, 500, 500, 500, 500}));
		AlchemyCostRegistry.addGristConversion(TRANSPORTALIZER, new GristSet(new GristType[] {GristType.BUILD, GristType.AMETHYST, GristType.RUST, GristType.URANIUM}, new int[] {350, 27, 36, 18}));
		AlchemyCostRegistry.addGristConversion(QUEUESTACK_MODUS_CARD, new GristSet(GristType.BUILD, 140));
		AlchemyCostRegistry.addGristConversion(TREE_MODUS_CARD, new GristSet(new GristType[] {GristType.BUILD, GristType.AMBER}, new int[] {400, 35}));
		AlchemyCostRegistry.addGristConversion(HASHMAP_MODUS_CARD, new GristSet(new GristType[] {GristType.BUILD, GristType.RUBY}, new int[] {280, 23}));
		AlchemyCostRegistry.addGristConversion(SET_MODUS_CARD, new GristSet(new GristType[] {GristType.BUILD, GristType.MERCURY}, new int[] {350, 29}));
		AlchemyCostRegistry.addGristConversion(IRON_BOAT, new GristSet(GristType.RUST, 30));
		AlchemyCostRegistry.addGristConversion(GOLD_BOAT, new GristSet(GristType.GOLD, 30));
		AlchemyCostRegistry.addGristConversion(LAYERED_SAND, new GristSet(GristType.BUILD, 1));
		AlchemyCostRegistry.addGristConversion(LAYERED_RED_SAND, new GristSet(GristType.BUILD, 1));
		/*registerContainerlessCost(new ItemStack(minestuckBucket, 1, 0), new GristSet(new GristType[] {GristType.TAR, GristType.SHALE}, new int[] {8, 8})); TODO Fluids
		registerContainerlessCost(new ItemStack(minestuckBucket, 1, 1), new GristSet(new GristType[] {GristType.GARNET, GristType.IODINE}, new int[] {8, 8}));
		registerContainerlessCost(new ItemStack(minestuckBucket, 1, 2), new GristSet(new GristType[] {GristType.AMETHYST, GristType.CHALK}, new int[] {8, 8}));
		registerContainerlessCost(new ItemStack(minestuckBucket, 1, 3), new GristSet(new GristType[] {GristType.AMETHYST, GristType.CHALK, GristType.GARNET, GristType.AMBER}, new int[] {4, 4, 4, 4}));
		registerContainerlessCost(new ItemStack(minestuckBucket, 1, 4), new GristSet(new GristType[] {GristType.MERCURY, GristType.URANIUM}, new int[] {8, 8}));*/
		registerContainerlessCost(OBSIDIAN_BUCKET, new GristSet(new GristType[] {GristType.COBALT, GristType.TAR, GristType.BUILD}, new int[] {8, 16, 4}));
		AlchemyCostRegistry.addGristConversion(GLOWING_MUSHROOM, new GristSet(new GristType[] {GristType.BUILD, GristType.SHALE, GristType.MERCURY}, new int[] {5, 3, 2}));
		AlchemyCostRegistry.addGristConversion(GLOWING_LOG, new GristSet(new GristType[] {GristType.BUILD, GristType.AMBER, GristType.MERCURY}, new int[] {8, 4, 4}));
		AlchemyCostRegistry.addGristConversion(GLOWING_PLANKS, new GristSet(new GristType[] {GristType.BUILD, GristType.AMBER, GristType.MERCURY}, new int[] {2, 1, 1}));
		AlchemyCostRegistry.addGristConversion(GLOWY_GOOP, new GristSet(new GristType[] {GristType.BUILD, GristType.CAULK, GristType.MERCURY}, new int[] {8, 8, 4}));
		
		AlchemyCostRegistry.addGristConversion(PETRIFIED_LOG, new GristSet(GristType.BUILD, 4));
		AlchemyCostRegistry.addGristConversion(FROST_PLANKS, new GristSet(GristType.BUILD, 1));
		AlchemyCostRegistry.addGristConversion(PETRIFIED_GRASS, new GristSet(GristType.BUILD, 1));
		AlchemyCostRegistry.addGristConversion(PETRIFIED_POPPY, new GristSet(new GristType[] {GristType.BUILD, GristType.IODINE}, new int[] {1, 1}));
		AlchemyCostRegistry.addGristConversion(BLOOMING_CACTUS, new GristSet(new GristType[] {GristType.AMBER, GristType.IODINE}, new int[] {1, 2}));
		AlchemyCostRegistry.addGristConversion(GOLD_SEEDS, new GristSet(GristType.GOLD, 3));
		AlchemyCostRegistry.addGristConversion(EMERALD_SWORD, new GristSet(new GristType[] {GristType.QUARTZ, GristType.DIAMOND, GristType.RUBY}, new int[] {44, 76, 72}));
		AlchemyCostRegistry.addGristConversion(EMERALD_AXE, new GristSet(new GristType[] {GristType.AMBER, GristType.DIAMOND, GristType.RUBY}, new int[] {40, 73, 70}));
		AlchemyCostRegistry.addGristConversion(EMERALD_PICKAXE, new GristSet(new GristType[] {GristType.RUST, GristType.DIAMOND, GristType.RUBY}, new int[] {42, 72, 70}));
		AlchemyCostRegistry.addGristConversion(EMERALD_SHOVEL, new GristSet(new GristType[] {GristType.CHALK, GristType.DIAMOND, GristType.RUBY}, new int[] {40, 70, 66}));
		AlchemyCostRegistry.addGristConversion(EMERALD_HOE, new GristSet(new GristType[] {GristType.IODINE, GristType.DIAMOND, GristType.RUBY}, new int[] {32, 50, 45}));
		AlchemyCostRegistry.addGristConversion(PRISMARINE_HELMET, new GristSet(new GristType[] {GristType.BUILD, GristType.COBALT, GristType.MARBLE}, new int[] {75, 30, 15}));
		AlchemyCostRegistry.addGristConversion(PRISMARINE_CHESTPLATE, new GristSet(new GristType[] {GristType.BUILD, GristType.COBALT, GristType.MARBLE}, new int[] {120, 48, 24}));
		AlchemyCostRegistry.addGristConversion(PRISMARINE_LEGGINGS, new GristSet(new GristType[] {GristType.BUILD, GristType.COBALT, GristType.MARBLE}, new int[] {105, 42, 21}));
		AlchemyCostRegistry.addGristConversion(PRISMARINE_BOOTS, new GristSet(new GristType[] {GristType.BUILD, GristType.COBALT, GristType.MARBLE}, new int[] {60, 24, 12}));
		AlchemyCostRegistry.addGristConversion(GLOWYSTONE_WIRE, new GristSet(new GristType[] {GristType.BUILD, GristType.AMBER}, new int[] {1, 1}));
		AlchemyCostRegistry.addGristConversion(RAW_CRUXITE, new GristSet(GristType.BUILD, 3));
		AlchemyCostRegistry.addGristConversion(RAW_URANIUM, new GristSet(GristType.URANIUM, 3));
		AlchemyCostRegistry.addGristConversion(GOLDEN_GRASSHOPPER, new GristSet(GristType.GOLD, 4000));
		AlchemyCostRegistry.addGristConversion(BUG_NET, new GristSet(new GristType[] {GristType.BUILD, GristType.CHALK}, new int[] {40, 25}));
		
		AlchemyCostRegistry.addGristConversion(SPORK, new GristSet(new GristType[]{GristType.BUILD}, new int[]{13}));
		AlchemyCostRegistry.addGristConversion(CANDY_CORN, new GristSet(new GristType[] {GristType.CHALK, GristType.SULFUR, GristType.IODINE}, new int[] {1, 1, 1}));
		
		for(GristType type : GristType.REGISTRY.getValues())
		{
			if(type.getRegistryName().getNamespace().equals("minestuck"))
				AlchemyCostRegistry.addGristConversion(type.getCandyItem().getItem(), new GristSet(type, 3));
		}
		
		AlchemyCostRegistry.addGristConversion(TAB, new GristSet(new GristType[] {GristType.COBALT, GristType.IODINE}, new int[] {1, 1}));		//Tab
		AlchemyCostRegistry.addGristConversion(FAYGO, new GristSet(new GristType[] {GristType.COBALT, GristType.AMBER}, new int[] {1, 1}));		//Faygo (orange)
		AlchemyCostRegistry.addGristConversion(FAYGO_CANDY_APPLE, new GristSet(new GristType[] {GristType.COBALT, GristType.SHALE}, new int[] {1, 1}));		//Candy apple Faygo
		AlchemyCostRegistry.addGristConversion(FAYGO_COLA, new GristSet(new GristType[] {GristType.COBALT, GristType.IODINE}, new int[] {1, 1}));		//Faygo cola
		AlchemyCostRegistry.addGristConversion(FAYGO_COTTON_CANDY, new GristSet(new GristType[] {GristType.COBALT, GristType.AMETHYST}, new int[] {1, 1}));	//Cotton Candy Faygo
		AlchemyCostRegistry.addGristConversion(FAYGO_CREME, new GristSet(new GristType[] {GristType.COBALT, GristType.CHALK}, new int[] {1, 1}));		//Creme Faygo
		AlchemyCostRegistry.addGristConversion(FAYGO_GRAPE, new GristSet(new GristType[] {GristType.COBALT, GristType.SHALE}, new int[] {1, 1}));		//Grape Faygo
		AlchemyCostRegistry.addGristConversion(FAYGO_MOON_MIST, new GristSet(new GristType[] {GristType.COBALT, GristType.AMBER}, new int[] {1, 1}));		//Moonmist Faygo
		AlchemyCostRegistry.addGristConversion(FAYGO_PEACH, new GristSet(new GristType[] {GristType.COBALT, GristType.GARNET}, new int[] {1, 1}));		//Peach Faygo
		AlchemyCostRegistry.addGristConversion(FAYGO_REDPOP, new GristSet(new GristType[] {GristType.COBALT, GristType.SULFUR}, new int[] {1, 1}));		//Redpop Faygo
		
		AlchemyCostRegistry.addGristConversion(SALAD, new GristSet(new GristType[]{GristType.BUILD,GristType.IODINE}, new int[]{1, 3}));
		AlchemyCostRegistry.addGristConversion(BUG_ON_A_STICK, new GristSet(new GristType[] {GristType.BUILD, GristType.CHALK}, new int[] {1, 1}));
		AlchemyCostRegistry.addGristConversion(CHOCOLATE_BEETLE, new GristSet(new GristType[] {GristType.CHALK, GristType.IODINE}, new int[] {2, 4}));
		AlchemyCostRegistry.addGristConversion(CONE_OF_FLIES, new GristSet(new GristType[] {GristType.BUILD, GristType.AMBER}, new int[] {2, 2}));
		AlchemyCostRegistry.addGristConversion(GRASSHOPPER, new GristSet(new GristType[] {GristType.IODINE, GristType.AMBER}, new int[] {3, 7}));
		AlchemyCostRegistry.addGristConversion(JAR_OF_BUGS, new GristSet(new GristType[] {GristType.BUILD, GristType.CHALK}, new int[] {5, 3}));
		AlchemyCostRegistry.addGristConversion(ONION, new GristSet(new GristType[] {GristType.IODINE}, new int[] {3}));
		AlchemyCostRegistry.addGristConversion(IRRADIATED_STEAK, new GristSet(new GristType[] {GristType.IODINE, GristType.URANIUM}, new int[] {12, 1}));
		AlchemyCostRegistry.addGristConversion(ROCK_COOKIE, new GristSet(new GristType[] {GristType.BUILD, GristType.MARBLE}, new int[] {10, 5}));
		AlchemyCostRegistry.addGristConversion(STRAWBERRY_CHUNK, new GristSet(new GristType[] {GristType.AMBER, GristType.BUILD, GristType.RUBY}, new int[] {2, 1, 2}));
		AlchemyCostRegistry.addGristConversion(DESERT_FRUIT, new GristSet(new GristType[] {GristType.AMBER, GristType.CAULK}, new int[] {1, 1}));
		AlchemyCostRegistry.addGristConversion(FUNGAL_SPORE, new GristSet(new GristType[] {GristType.IODINE, GristType.AMBER}, new int[] {2, 1}));
		AlchemyCostRegistry.addGristConversion(SPOREO, new GristSet(new GristType[] {GristType.IODINE, GristType.AMBER}, new int[] {3, 1}));
		AlchemyCostRegistry.addGristConversion(MOREL_MUSHROOM, new GristSet(new GristType[] {GristType.IODINE, GristType.AMBER}, new int[] {20, 12}));
		AlchemyCostRegistry.addGristConversion(FRENCH_FRY, new GristSet(new GristType[] {GristType.BUILD, GristType.IODINE}, new int[] {1, 1}));
		AlchemyCostRegistry.addGristConversion(SURPRISE_EMBRYO, new GristSet(new GristType[] {GristType.AMBER, GristType.IODINE}, new int[] {15, 5}));
		AlchemyCostRegistry.addGristConversion(UNKNOWABLE_EGG, new GristSet(new GristType[] {GristType.AMBER, GristType.AMETHYST, GristType.TAR}, new int[] {15, 15, 1}));
		
		AlchemyCostRegistry.addGristConversion(PRIMED_TNT, new GristSet(new GristType[] {GristType.BUILD, GristType.CHALK, GristType.SULFUR}, new int[] {8, 10, 14}));
		AlchemyCostRegistry.addGristConversion(UNSTABLE_TNT, new GristSet(new GristType[] {GristType.BUILD, GristType.CHALK, GristType.SULFUR}, new int[] {5, 11, 15}));
		AlchemyCostRegistry.addGristConversion(INSTANT_TNT, new GristSet(new GristType[] {GristType.BUILD, GristType.CHALK, GristType.SULFUR}, new int[] {6, 11, 17}));
		AlchemyCostRegistry.addGristConversion(STONE_EXPLOSIVE_BUTTON, new GristSet(new GristType[] {GristType.BUILD, GristType.CHALK, GristType.SULFUR}, new int[] {7, 5, 8}));
		AlchemyCostRegistry.addGristConversion(WOODEN_EXPLOSIVE_BUTTON, new GristSet(new GristType[] {GristType.BUILD, GristType.CHALK, GristType.SULFUR}, new int[] {7, 5, 8}));
		AlchemyCostRegistry.addGristConversion(COARSE_STONE, new GristSet(GristType.BUILD, 4));
		AlchemyCostRegistry.addGristConversion(CHISELED_COARSE_STONE, new GristSet(GristType.BUILD, 4));
		AlchemyCostRegistry.addGristConversion(SHADE_BRICKS, new GristSet(new GristType[] {GristType.BUILD, GristType.SHALE}, new int[] {2, 1}));
		AlchemyCostRegistry.addGristConversion(SMOOTH_SHADE_STONE, new GristSet(new GristType[] {GristType.BUILD, GristType.SHALE}, new int[] {2, 1}));
		AlchemyCostRegistry.addGristConversion(FROST_BRICKS, new GristSet(new GristType[] {GristType.BUILD, GristType.COBALT}, new int[] {2, 1}));
		AlchemyCostRegistry.addGristConversion(FROST_TILE, new GristSet(new GristType[] {GristType.BUILD, GristType.COBALT}, new int[] {2, 1}));
		AlchemyCostRegistry.addGristConversion(CHISELED_FROST_BRICKS, new GristSet(new GristType[] {GristType.BUILD, GristType.COBALT}, new int[] {3, 1}));
		AlchemyCostRegistry.addGristConversion(CAST_IRON, new GristSet(new GristType[] {GristType.BUILD, GristType.RUST}, new int[] {3, 2}));
		AlchemyCostRegistry.addGristConversion(CHISELED_CAST_IRON, new GristSet(new GristType[] {GristType.BUILD, GristType.RUST}, new int[] {3, 2}));
		AlchemyCostRegistry.addGristConversion(COARSE_END_STONE, new GristSet(new GristType[] {GristType.CAULK, GristType.BUILD}, new int[] {3, 4}));
		AlchemyCostRegistry.addGristConversion(END_GRASS, new GristSet(new GristType[] {GristType.CAULK, GristType.BUILD}, new int[] {3, 4}));
		AlchemyCostRegistry.addGristConversion(FLOWERY_VINE_LOG, new GristSet(new GristType[] {GristType.BUILD, GristType.IODINE}, new int[] {7, 1}));
		AlchemyCostRegistry.addGristConversion(FROST_LOG, new GristSet(new GristType[] {GristType.BUILD, GristType.COBALT}, new int[] {7, 1}));
		AlchemyCostRegistry.addGristConversion(FLOWERY_MOSS_BRICKS, new GristSet(new GristType[] {GristType.AMBER, GristType.BUILD, GristType.IODINE}, new int[] {1, 7, 1}));
		AlchemyCostRegistry.addGristConversion(FLOWERY_MOSS_STONE, new GristSet(new GristType[] {GristType.AMBER, GristType.BUILD, GristType.IODINE}, new int[] {1, 7, 1}));
		AlchemyCostRegistry.addGristConversion(TREATED_PLANKS, new GristSet(new GristType[] {GristType.BUILD}, new int[] {2}));
		AlchemyCostRegistry.addGristConversion(SBAHJ_POSTER, new GristSet(new GristType[] {GristType.BUILD}, new int[] {4}));
		AlchemyCostRegistry.addGristConversion(CREW_POSTER, new GristSet(new GristType[] {GristType.TAR, GristType.RUST}, new int[] {3, 2}));
		AlchemyCostRegistry.addGristConversion(THRESH_DVD, new GristSet(new GristType[] {GristType.IODINE, GristType.AMETHYST}, new int[] {3, 2}));
		AlchemyCostRegistry.addGristConversion(GAMEBRO_MAGAZINE, new GristSet(new GristType[] {GristType.CHALK, GristType.GARNET}, new int[] {3, 2}));
		AlchemyCostRegistry.addGristConversion(GAMEGRL_MAGAZINE, new GristSet(new GristType[] {GristType.CHALK, GristType.AMETHYST}, new int[] {3, 2}));
		AlchemyCostRegistry.addGristConversion(CARVING_TOOL, new GristSet(new GristType[] {GristType.BUILD, GristType.RUST}, new int[] {10, 2}));
		AlchemyCostRegistry.addGristConversion(CRUMPLY_HAT, new GristSet(new GristType[] {GristType.BUILD}, new int[] {20}));
		AlchemyCostRegistry.addGristConversion(MINI_FROG_STATUE, new GristSet(new GristType[] {GristType.BUILD}, new int[] {30}));
		AlchemyCostRegistry.addGristConversion(WOODEN_CACTUS, new GristSet(GristType.BUILD, 7));
		AlchemyCostRegistry.addGristConversion(BLUE_CAKE, new GristSet(new GristType[] {GristType.SHALE, GristType.MERCURY, GristType.COBALT, GristType.DIAMOND}, new int[] {24, 6, 5, 1}));
		AlchemyCostRegistry.addGristConversion(COLD_CAKE, new GristSet(new GristType[] {GristType.COBALT, GristType.MARBLE}, new int[] {15, 12}));
		AlchemyCostRegistry.addGristConversion(RED_CAKE, new GristSet(new GristType[] {GristType.RUST, GristType.CHALK, GristType.IODINE, GristType.GARNET}, new int[] {20, 9, 6, 1}));
		AlchemyCostRegistry.addGristConversion(HOT_CAKE, new GristSet(new GristType[] {GristType.SULFUR, GristType.IODINE}, new int[] {17, 10}));
		AlchemyCostRegistry.addGristConversion(REVERSE_CAKE, new GristSet(new GristType[] {GristType.AMBER, GristType.CHALK, GristType.IODINE}, new int[] {10, 24, 11}));
		AlchemyCostRegistry.addGristConversion(FUCHSIA_CAKE, new GristSet(new GristType[] {GristType.AMETHYST, GristType.GARNET, GristType.IODINE}, new int[] {85, 54, 40}));
		AlchemyCostRegistry.addGristConversion(CRUXTRUDER_LID, new GristSet(GristType.BUILD, 8));
		AlchemyCostRegistry.addGristConversion(BLENDER, new GristSet(new GristType[] {GristType.BUILD}, new int[] {16}));
		/*GristRegistry.addGristConversion(new ItemStack(shopPoster, 1, 0), new GristSet(new GristType[] {GristType.BUILD, GristType.COBALT, GristType.SHALE}, new int[] {8, 2, 2}));
		GristRegistry.addGristConversion(new ItemStack(shopPoster, 1, 1), new GristSet(new GristType[] {GristType.BUILD, GristType.IODINE}, new int[] {9, 3}));
		GristRegistry.addGristConversion(new ItemStack(shopPoster, 1, 2), new GristSet(new GristType[] {GristType.BUILD, GristType.CHALK, GristType.CAULK}, new int[] {8, 2, 2}));
		GristRegistry.addGristConversion(new ItemStack(shopPoster, 1, 3), new GristSet(new GristType[] {GristType.BUILD, GristType.RUST, GristType.AMBER}, new int[] {7, 3, 2}));
		GristRegistry.addGristConversion(new ItemStack(shopPoster, 1, 4), new GristSet(new GristType[] {GristType.BUILD, GristType.IODINE, GristType.CHALK}, new int[] {9, 2, 1}));*/
		AlchemyCostRegistry.addGristConversion(VEIN, new GristSet(new GristType[] {GristType.GARNET, GristType.IODINE}, new int[] {12, 8}));
		
		//add Designix and Lathe combinations
		
		//swords
		CombinationRegistry.addCombination(Items.WOODEN_SWORD, Blocks.CACTUS, MODE_AND, new ItemStack(CACTUS_CUTLASS));
		CombinationRegistry.addCombination(Items.WOODEN_SWORD, BLOOMING_CACTUS, MODE_AND, new ItemStack(CACTUS_CUTLASS));
		CombinationRegistry.addCombination(Items.WOODEN_SWORD, Items.COOKED_BEEF, MODE_OR, new ItemStack(STEAK_SWORD));
		CombinationRegistry.addCombination(Items.WOODEN_SWORD, Items.BEEF, MODE_OR, new ItemStack(BEEF_SWORD));
		CombinationRegistry.addCombination(Items.WOODEN_SWORD, IRRADIATED_STEAK, MODE_OR, new ItemStack(IRRADIATED_STEAK_SWORD));
		CombinationRegistry.addCombination(Items.STONE_SWORD, Items.COOKED_BEEF, MODE_OR, new ItemStack(STEAK_SWORD));
		CombinationRegistry.addCombination(Items.WOODEN_SWORD, SBAHJ_POSTER, MODE_AND, new ItemStack(SORD));
		CombinationRegistry.addCombination(Items.STONE_SWORD, SBAHJ_POSTER, MODE_AND, new ItemStack(SORD));
		CombinationRegistry.addCombination(Items.STONE_SWORD, Items.ROTTEN_FLESH, MODE_AND, new ItemStack(KATANA));
		CombinationRegistry.addCombination(Items.IRON_SWORD, Items.ROTTEN_FLESH, MODE_AND, new ItemStack(KATANA));
		CombinationRegistry.addCombination(Items.IRON_SWORD, Items.BLAZE_ROD, MODE_AND, new ItemStack(FIRE_POKER));
		CombinationRegistry.addCombination(Items.IRON_SWORD, Items.BLAZE_ROD, MODE_OR, new ItemStack(HOT_HANDLE));
		CombinationRegistry.addCombination(Items.IRON_SWORD, CHESSBOARD, MODE_AND, new ItemStack(REGISWORD));
		CombinationRegistry.addCombination(KATANA, CHESSBOARD, MODE_AND, new ItemStack(REGISWORD));
		CombinationRegistry.addCombination(Items.IRON_SWORD, Blocks.IRON_BLOCK, MODE_AND, new ItemStack(CLAYMORE));
		CombinationRegistry.addCombination(KATANA, Blocks.OBSIDIAN, MODE_AND, new ItemStack(UNBREAKABLE_KATANA));
		CombinationRegistry.addCombination(HOT_HANDLE, Blocks.LAPIS_BLOCK, MODE_OR, new ItemStack(COBALT_SABRE));
		CombinationRegistry.addCombination(CALEDSCRATCH, FROG, MODE_AND, new ItemStack(SCARLET_RIBBITAR));
		CombinationRegistry.addCombination(Blocks.BEACON, Items.DIAMOND_SWORD, MODE_AND, new ItemStack(SHATTER_BEACON));
		
		//axes
		CombinationRegistry.addCombination(Items.WOODEN_AXE, SBAHJ_POSTER, MODE_AND, new ItemStack(BATLEACKS));
		CombinationRegistry.addCombination(Items.STONE_AXE, SBAHJ_POSTER, MODE_AND, new ItemStack(BATLEACKS));
		CombinationRegistry.addCombination(Items.IRON_AXE, Blocks.PISTON, MODE_AND, new ItemStack(COPSE_CRUSHER));
		CombinationRegistry.addCombination(Items.IRON_AXE, Blocks.IRON_BLOCK, MODE_AND, new ItemStack(BATTLEAXE));
		CombinationRegistry.addCombination(Items.WOODEN_AXE, Blocks.ANVIL, MODE_AND, new ItemStack(BLACKSMITH_BANE));
		//CombinationRegistry.addCombination("record", Items.IRON_AXE, MODE_AND, new ItemStack(SCRAXE));
		CombinationRegistry.addCombination(COPSE_CRUSHER, POGO_HAMMER, MODE_AND, new ItemStack(Q_P_HAMMER_AXE));
		CombinationRegistry.addCombination(Q_P_HAMMER_AXE, ENERGY_CORE, MODE_AND, new ItemStack(Q_F_HAMMER_AXE));
		CombinationRegistry.addCombination(Items.GOLDEN_AXE, Items.LAVA_BUCKET, MODE_AND, new ItemStack(HEPHAESTUS_LUMBER));
		CombinationRegistry.addCombination(Items.IRON_AXE, FROG, MODE_AND, new ItemStack(RUBY_CROAK));
		
		//sickles
		CombinationRegistry.addCombination(Items.IRON_HOE, Items.WHEAT, MODE_AND, new ItemStack(SICKLE));
		CombinationRegistry.addCombination(SICKLE, THRESH_DVD, MODE_OR, new ItemStack(HOMES_SMELL_YA_LATER));
		CombinationRegistry.addCombination(SICKLE, Items.COCOA_BEANS, MODE_OR, new ItemStack (FUDGE_SICKLE));
		CombinationRegistry.addCombination(SICKLE, CHESSBOARD, MODE_AND, new ItemStack(REGI_SICKLE));
		CombinationRegistry.addCombination(SICKLE, CANDY_CORN, MODE_OR, new ItemStack(CANDY_SICKLE));
		CombinationRegistry.addCombination(FUDGE_SICKLE, Items.SUGAR, MODE_AND, new ItemStack(CANDY_SICKLE));
		CombinationRegistry.addCombination(CAT_CLAWS_DRAWN, GRIMOIRE, MODE_AND, new ItemStack(CLAW_OF_NRUBYIGLITH));
		CombinationRegistry.addCombination(CAT_CLAWS_SHEATHED, GRIMOIRE, MODE_AND, new ItemStack(CLAW_OF_NRUBYIGLITH));
		
		//clubs
		CombinationRegistry.addCombination(DEUCE_CLUB, Items.SLIME_BALL, MODE_AND, new ItemStack(POGO_CLUB));
		CombinationRegistry.addCombination(DEUCE_CLUB, CREW_POSTER, MODE_AND, new ItemStack(NIGHT_CLUB));
		CombinationRegistry.addCombination(DEUCE_CLUB, Items.IRON_INGOT, MODE_AND, new ItemStack(METAL_BAT));
		CombinationRegistry.addCombination(ItemTags.LOGS, METAL_BAT, MODE_OR, new ItemStack(SPIKED_CLUB));
		
		//hammers
		CombinationRegistry.addCombination(CLAW_HAMMER, Blocks.BRICKS, MODE_AND, new ItemStack(SLEDGE_HAMMER));
		CombinationRegistry.addCombination(CLAW_HAMMER, Blocks.COBBLESTONE, MODE_AND, new ItemStack(SLEDGE_HAMMER));
		CombinationRegistry.addCombination(Blocks.ANVIL, SLEDGE_HAMMER, MODE_AND, new ItemStack(BLACKSMITH_HAMMER));
		CombinationRegistry.addCombination(Blocks.IRON_BLOCK, SLEDGE_HAMMER, MODE_AND, new ItemStack(BLACKSMITH_HAMMER));
		CombinationRegistry.addCombination(Items.SLIME_BALL, SLEDGE_HAMMER, MODE_AND, new ItemStack(POGO_HAMMER));
		CombinationRegistry.addCombination(CLAW_HAMMER, CHESSBOARD, MODE_AND, new ItemStack(REGI_HAMMER));
		CombinationRegistry.addCombination(BLACKSMITH_HAMMER, Items.CLOCK, MODE_OR, new ItemStack(FEAR_NO_ANVIL));
		CombinationRegistry.addCombination(SLEDGE_HAMMER, Items.BOOK, MODE_AND, new ItemStack(TELESCOPIC_SASSACRUSHER));
		CombinationRegistry.addCombination(FEAR_NO_ANVIL, Items.LAVA_BUCKET, MODE_OR, new ItemStack(MELT_MASHER));
		CombinationRegistry.addCombination(Q_F_HAMMER_AXE, GAMEGRL_MAGAZINE, MODE_OR, new ItemStack(Q_E_HAMMER_AXE));
		CombinationRegistry.addCombination(Q_E_HAMMER_AXE, SBAHJ_POSTER, MODE_AND, new ItemStack(D_D_E_HAMMER_AXE));
		CombinationRegistry.addCombination(ZILLYHOO_HAMMER, FLUORITE_OCTET, MODE_AND, new ItemStack(POPAMATIC_VRILLYHOO));
		CombinationRegistry.addCombination(ZILLYHOO_HAMMER, FROG, MODE_AND, new ItemStack(SCARLET_ZILLYHOO));
		
		//canes
		CombinationRegistry.addCombination(CANE, Items.IRON_SWORD, MODE_OR, new ItemStack(SPEAR_CANE));
		CombinationRegistry.addCombination(CANE, KATANA, MODE_OR, new ItemStack(SPEAR_CANE));
		CombinationRegistry.addCombination(IRON_CANE, Items.STONE_SWORD, MODE_OR, new ItemStack(SPEAR_CANE));
		CombinationRegistry.addCombination(IRON_CANE, Items.IRON_SWORD, MODE_OR, new ItemStack(SPEAR_CANE));
		CombinationRegistry.addCombination(IRON_CANE, KATANA, MODE_OR, new ItemStack(SPEAR_CANE));
		CombinationRegistry.addCombination(Blocks.RED_MUSHROOM, Items.STICK, MODE_OR, new ItemStack(PARADISES_PORTABELLO));
		CombinationRegistry.addCombination(Blocks.BROWN_MUSHROOM, Items.STICK, MODE_OR, new ItemStack(PARADISES_PORTABELLO));
		CombinationRegistry.addCombination(CANE, CHESSBOARD, MODE_AND, new ItemStack(REGI_CANE));
		CombinationRegistry.addCombination(IRON_CANE, CHESSBOARD, MODE_AND, new ItemStack(REGI_CANE));
		CombinationRegistry.addCombination(Items.STICK, RAW_URANIUM, MODE_OR, new ItemStack(UP_STICK));
		
		//spoons/sporks/forks
		CombinationRegistry.addCombination(Items.WOODEN_SHOVEL, Items.BOWL, MODE_AND, new ItemStack(WOODEN_SPOON));
		CombinationRegistry.addCombination(Items.WOODEN_SHOVEL, Items.MUSHROOM_STEW, MODE_AND, new ItemStack(WOODEN_SPOON));
		CombinationRegistry.addCombination(Items.WOODEN_SHOVEL, Items.RABBIT_STEW, MODE_AND, new ItemStack(WOODEN_SPOON));
		CombinationRegistry.addCombination(Items.WOODEN_SHOVEL, Items.BEETROOT_SOUP, MODE_AND, new ItemStack(WOODEN_SPOON));
		CombinationRegistry.addCombination(Items.IRON_SHOVEL, Items.BOWL, MODE_AND, new ItemStack(SILVER_SPOON));
		CombinationRegistry.addCombination(Items.IRON_SHOVEL, Items.MUSHROOM_STEW, MODE_AND, new ItemStack(SILVER_SPOON));
		CombinationRegistry.addCombination(Items.IRON_SHOVEL, Items.RABBIT_STEW, MODE_AND, new ItemStack(SILVER_SPOON));
		CombinationRegistry.addCombination(Items.IRON_SHOVEL, Items.BEETROOT_SOUP, MODE_AND, new ItemStack(SILVER_SPOON));
		CombinationRegistry.addCombination(WOODEN_SPOON, Items.IRON_INGOT, MODE_AND, new ItemStack(SILVER_SPOON));
		CombinationRegistry.addCombination(SILVER_SPOON, Blocks.CAKE, MODE_AND, new ItemStack(CROCKER_SPOON));
		CombinationRegistry.addCombination(FORK, CHESSBOARD, MODE_AND, new ItemStack(SKAIA_FORK));
		CombinationRegistry.addCombination(FORK, WOODEN_SPOON, MODE_OR, new ItemStack(SPORK));
		CombinationRegistry.addCombination(SPORK, Items.GOLD_INGOT, MODE_OR, new ItemStack(GOLDEN_SPORK));
		
		CombinationRegistry.addCombination(CROCKER_SPOON, CAPTCHA_CARD, MODE_OR, new ItemStack(GRIST_WIDGET));
		CombinationRegistry.addCombination(CROCKER_FORK, CAPTCHA_CARD, MODE_OR, new ItemStack(GRIST_WIDGET));
		CombinationRegistry.addCombination(Items.ENDER_PEARL, Blocks.IRON_BLOCK, MODE_AND, new ItemStack(TRANSPORTALIZER));
		CombinationRegistry.addCombination(CAPTCHA_CARD, MinestuckBlocks.COMPUTER_OFF, MODE_AND, new ItemStack(CAPTCHAROID_CAMERA));
		CombinationRegistry.addCombination(CAPTCHA_CARD, Items.ENDER_EYE, MODE_OR, new ItemStack(CAPTCHAROID_CAMERA));
		
		CombinationRegistry.addCombination(STACK_MODUS_CARD, QUEUE_MODUS_CARD, MODE_AND, new ItemStack(QUEUESTACK_MODUS_CARD));
		/*CombinationRegistry.addCombination(Items.STICK, modusCard, MODE_OR, new ItemStack(TREE_MODUS_CARD)); TODO
		CombinationRegistry.addCombination(ItemTags.SAPLINGS, modusCard, MODE_OR, new ItemStack(TREE_MODUS_CARD));
		CombinationRegistry.addCombination(ItemTags.LEAVES, modusCard, MODE_OR, new ItemStack(TREE_MODUS_CARD));	//Not planks and logs though. Too little branch-related.
		CombinationRegistry.addCombination(modusCard, COMPUTER_OFF, MODE_AND, new ItemStack(HASHMAP_MODUS_CARD));
		CombinationRegistry.addCombination(modusCard, Items.ITEM_FRAME, MODE_AND, new ItemStack(SET_MODUS_CARD));*/
		CombinationRegistry.addCombination(Blocks.IRON_BARS, Items.LEATHER, MODE_AND, new ItemStack(CAT_CLAWS_SHEATHED));
		CombinationRegistry.addCombination(Items.WHEAT_SEEDS, Items.GOLD_NUGGET, MODE_AND, new ItemStack(GOLD_SEEDS));
		CombinationRegistry.addCombination(Items.WHEAT_SEEDS, Items.GOLD_INGOT, MODE_AND, new ItemStack(GOLD_SEEDS));
		CombinationRegistry.addCombination(ItemTags.BOATS, Items.MINECART, MODE_OR, new ItemStack(IRON_BOAT));
		CombinationRegistry.addCombination(ItemTags.BOATS, Items.IRON_INGOT, MODE_AND, new ItemStack(IRON_BOAT));
		CombinationRegistry.addCombination(ItemTags.BOATS, Blocks.IRON_BLOCK, MODE_AND, new ItemStack(IRON_BOAT));
		CombinationRegistry.addCombination(ItemTags.BOATS, Items.GOLD_INGOT, MODE_AND, new ItemStack(GOLD_BOAT));
		CombinationRegistry.addCombination(ItemTags.BOATS, Blocks.GOLD_BLOCK, MODE_AND, new ItemStack(GOLD_BOAT));
		CombinationRegistry.addCombination(Blocks.DIRT, Items.LAPIS_LAZULI, MODE_OR, new ItemStack(BLUE_DIRT));
		CombinationRegistry.addCombination(Blocks.DIRT, Items.LIME_DYE, MODE_OR, new ItemStack(THOUGHT_DIRT));
		CombinationRegistry.addCombination(Blocks.SAND, Blocks.SNOW, MODE_OR, new ItemStack(LAYERED_SAND));
		CombinationRegistry.addCombination(Blocks.RED_SAND, Blocks.SNOW, MODE_OR, new ItemStack(LAYERED_RED_SAND));
		CombinationRegistry.addCombination(Items.WATER_BUCKET, Items.LAVA_BUCKET, MODE_OR, new ItemStack(OBSIDIAN_BUCKET));	//water_bucket && lava bucket could make a bucket with liquid obsidian? (from a mod that adds liquid obsidian)
		CombinationRegistry.addCombination(Items.BUCKET, Blocks.OBSIDIAN, MODE_AND, new ItemStack(OBSIDIAN_BUCKET));	//bucket || obsidian could make a bucket made out of obsidian
		CombinationRegistry.addCombination(Blocks.BROWN_MUSHROOM, Items.GLOWSTONE_DUST, MODE_OR, new ItemStack(GLOWING_MUSHROOM));
		CombinationRegistry.addCombination(ItemTags.LOGS, GLOWING_MUSHROOM, MODE_OR, new ItemStack(GLOWING_LOG));
		CombinationRegistry.addCombination(ItemTags.PLANKS, GLOWING_MUSHROOM, MODE_OR, new ItemStack(GLOWING_PLANKS));
		CombinationRegistry.addCombination(Blocks.SLIME_BLOCK, GLOWING_MUSHROOM, MODE_OR, new ItemStack(GLOWY_GOOP));
		CombinationRegistry.addCombination(Blocks.STONE, Blocks.POPPY, MODE_OR, new ItemStack(PETRIFIED_POPPY));
		CombinationRegistry.addCombination(Blocks.GRAVEL, Blocks.POPPY, MODE_OR, new ItemStack(PETRIFIED_POPPY));
		CombinationRegistry.addCombination(Blocks.COBBLESTONE, Blocks.POPPY, MODE_OR, new ItemStack(PETRIFIED_POPPY));
		CombinationRegistry.addCombination(ItemTags.LOGS, Blocks.STONE, MODE_OR, new ItemStack(PETRIFIED_LOG));
		CombinationRegistry.addCombination(ItemTags.LOGS, Blocks.COBBLESTONE, MODE_OR, new ItemStack(PETRIFIED_LOG));
		CombinationRegistry.addCombination(ItemTags.LOGS, Blocks.GRAVEL, MODE_OR, new ItemStack(PETRIFIED_LOG));
		CombinationRegistry.addCombination(Blocks.CACTUS, Blocks.POPPY, MODE_AND, new ItemStack(BLOOMING_CACTUS));
		CombinationRegistry.addCombination(Blocks.CACTUS, Blocks.DANDELION, MODE_AND, new ItemStack(BLOOMING_CACTUS));
		CombinationRegistry.addCombination(Items.SUGAR, Items.WHEAT_SEEDS, MODE_AND, new ItemStack(CANDY_CORN));
		CombinationRegistry.addCombination(GRASSHOPPER, Items.GOLD_INGOT, MODE_OR, new ItemStack(GOLDEN_GRASSHOPPER));
		CombinationRegistry.addCombination(Items.STICK, Blocks.COBWEB, MODE_OR, new ItemStack(BUG_NET));
		CombinationRegistry.addCombination(Items.STRING, Items.BUCKET, MODE_AND, new ItemStack(BUG_NET));
		CombinationRegistry.addCombination(Items.COOKIE, Blocks.REDSTONE_BLOCK, MODE_AND, new ItemStack(SUGAR_CUBE));
		CombinationRegistry.addCombination(SURPRISE_EMBRYO, GRIMOIRE, MODE_OR, new ItemStack(UNKNOWABLE_EGG));
		CombinationRegistry.addCombination(Blocks.NOTE_BLOCK, GRIMOIRE, MODE_AND, new ItemStack(LONG_FORGOTTEN_WARHORN));
		
		if(oreMultiplier != 0)
		{
			CombinationRegistry.addCombination(Items.COAL, Blocks.NETHERRACK, MODE_AND, new ItemStack(COAL_ORE_NETHERRACK));
			CombinationRegistry.addCombination(Items.IRON_INGOT, Blocks.END_STONE, MODE_AND, new ItemStack(IRON_ORE_END_STONE));
			CombinationRegistry.addCombination(Items.IRON_INGOT, Blocks.SANDSTONE, MODE_AND, new ItemStack(IRON_ORE_SANDSTONE));
			CombinationRegistry.addCombination(Items.IRON_INGOT, Blocks.RED_SANDSTONE, MODE_AND, new ItemStack(IRON_ORE_SANDSTONE_RED));
			CombinationRegistry.addCombination(Items.GOLD_INGOT, Blocks.SANDSTONE, MODE_AND, new ItemStack(GOLD_ORE_SANDSTONE));
			CombinationRegistry.addCombination(Items.GOLD_INGOT, Blocks.RED_SANDSTONE, MODE_AND, new ItemStack(GOLD_ORE_SANDSTONE_RED));
			CombinationRegistry.addCombination(Items.REDSTONE, Blocks.END_STONE, MODE_AND, new ItemStack(REDSTONE_ORE_END_STONE));
		}
		
		CombinationRegistry.addCombination(Items.DIAMOND_SWORD, Items.EMERALD, MODE_OR, new ItemStack(EMERALD_SWORD));
		CombinationRegistry.addCombination(Items.DIAMOND_AXE, Items.EMERALD, MODE_OR, new ItemStack(EMERALD_AXE));
		CombinationRegistry.addCombination(Items.DIAMOND_PICKAXE, Items.EMERALD, MODE_OR, new ItemStack(EMERALD_PICKAXE));
		CombinationRegistry.addCombination(Items.DIAMOND_SHOVEL, Items.EMERALD, MODE_OR, new ItemStack(EMERALD_SHOVEL));
		CombinationRegistry.addCombination(Items.DIAMOND_HOE, Items.EMERALD, MODE_OR, new ItemStack(EMERALD_HOE));
		
		Item[] metalHelmets = new Item[] {Items.IRON_HELMET, Items.GOLDEN_HELMET, Items.DIAMOND_HELMET};
		Item[] metalChestplates = new Item[] {Items.IRON_CHESTPLATE, Items.GOLDEN_CHESTPLATE, Items.DIAMOND_CHESTPLATE};
		Item[] metalLeggings = new Item[] {Items.IRON_LEGGINGS, Items.GOLDEN_LEGGINGS, Items.DIAMOND_LEGGINGS};
		Item[] metalBoots = new Item[] {Items.IRON_BOOTS, Items.GOLDEN_BOOTS, Items.DIAMOND_BOOTS};
		for(int i = 0; i < metalHelmets.length; i++)	//Two out of three possible for-loops is enough for me
			for(IItemProvider prismarine : new IItemProvider[]{Items.PRISMARINE_SHARD, Blocks.PRISMARINE})
			{
				CombinationRegistry.addCombination(metalHelmets[i], prismarine, MODE_OR, new ItemStack(PRISMARINE_HELMET));
				CombinationRegistry.addCombination(metalChestplates[i], prismarine, MODE_OR, new ItemStack(PRISMARINE_CHESTPLATE));
				CombinationRegistry.addCombination(metalLeggings[i], prismarine, MODE_OR, new ItemStack(PRISMARINE_LEGGINGS));
				CombinationRegistry.addCombination(metalBoots[i], prismarine, MODE_OR, new ItemStack(PRISMARINE_BOOTS));
			}
		
		CombinationRegistry.addCombination(ItemTags.BUTTONS, Blocks.TNT, MODE_OR, new ItemStack(PRIMED_TNT));
		CombinationRegistry.addCombination(Blocks.TNT, Blocks.REDSTONE_TORCH, MODE_OR, new ItemStack(UNSTABLE_TNT));
		//CombinationRegistry.addCombination(Blocks.TNT, Items.POTIONITEM, 1, 8236, MODE_OR, true, true, new ItemStack(INSTANT_TNT));	//Instant damage potions
		//CombinationRegistry.addCombination(Blocks.TNT, Items.POTIONITEM, 1, 8268, MODE_OR, true, true, new ItemStack(INSTANT_TNT));
		CombinationRegistry.addCombination(Blocks.TNT, Blocks.STONE_BUTTON, MODE_AND, new ItemStack(STONE_EXPLOSIVE_BUTTON));
		CombinationRegistry.addCombination(INSTANT_TNT, Blocks.STONE_BUTTON, MODE_AND, new ItemStack(STONE_EXPLOSIVE_BUTTON));
		CombinationRegistry.addCombination(ItemTags.WOODEN_BUTTONS, Blocks.TNT, MODE_AND, new ItemStack(WOODEN_EXPLOSIVE_BUTTON));
		CombinationRegistry.addCombination(ItemTags.WOODEN_BUTTONS, INSTANT_TNT, MODE_AND, new ItemStack(WOODEN_EXPLOSIVE_BUTTON));
		CombinationRegistry.addCombination(Blocks.GRAVEL, Blocks.STONE, MODE_AND, new ItemStack(COARSE_STONE));
		CombinationRegistry.addCombination(Blocks.GRAVEL, Blocks.CHISELED_STONE_BRICKS, MODE_AND, new ItemStack(CHISELED_COARSE_STONE));
		CombinationRegistry.addCombination(COARSE_STONE, Blocks.CHISELED_STONE_BRICKS, MODE_AND, new ItemStack(CHISELED_COARSE_STONE));
		CombinationRegistry.addCombination(Blocks.STONE_BRICKS, BLUE_DIRT, MODE_OR, new ItemStack(SHADE_BRICKS));
		CombinationRegistry.addCombination(Blocks.STONE_BRICK_STAIRS, BLUE_DIRT, MODE_OR, new ItemStack(SHADE_BRICK_STAIRS));
		CombinationRegistry.addCombination(Blocks.STONE_BRICKS, Items.LAPIS_LAZULI, MODE_OR, new ItemStack(SHADE_BRICKS));
		CombinationRegistry.addCombination(Blocks.STONE_BRICK_STAIRS, Items.LAPIS_LAZULI, MODE_OR, new ItemStack(SHADE_BRICK_STAIRS));
		for(int i = 0; i <= 6; i+=2)	//Stone and polished stone
		{
			/*CombinationRegistry.addCombination(Blocks.STONE, 1, i, BLUE_DIRT, MODE_OR, new ItemStack(SMOOTH_SHADE_STONE));
			CombinationRegistry.addCombination(Blocks.STONE, 1, i, Items.LAPIS_LAZULI, MODE_OR, new ItemStack(SMOOTH_SHADE_STONE));
			CombinationRegistry.addCombination(Blocks.STONE, 1, i, Blocks.ICE, MODE_AND, new ItemStack(FROST_TILE));
			CombinationRegistry.addCombination(Blocks.STONE, 1, i, Blocks.PACKED_ICE, MODE_AND, new ItemStack(FROST_TILE));*/
		}
		CombinationRegistry.addCombination(Blocks.STONE_BRICKS, Blocks.ICE, MODE_AND, new ItemStack(FROST_BRICKS));
		CombinationRegistry.addCombination(Blocks.STONE_BRICKS, Blocks.PACKED_ICE, MODE_AND, new ItemStack(FROST_BRICKS));
		CombinationRegistry.addCombination(Blocks.CHISELED_STONE_BRICKS, Blocks.ICE, MODE_AND, new ItemStack(CHISELED_FROST_BRICKS));
		CombinationRegistry.addCombination(Blocks.CHISELED_STONE_BRICKS, Blocks.PACKED_ICE, MODE_AND, new ItemStack(CHISELED_FROST_BRICKS));
		CombinationRegistry.addCombination(Blocks.IRON_BLOCK, Items.LAVA_BUCKET, MODE_AND, new ItemStack(CAST_IRON));
		CombinationRegistry.addCombination(Blocks.CHISELED_STONE_BRICKS, CAST_IRON, MODE_OR, new ItemStack(CHISELED_CAST_IRON));
		CombinationRegistry.addCombination(Blocks.COARSE_DIRT, Blocks.END_STONE, MODE_OR, new ItemStack(COARSE_END_STONE));
		CombinationRegistry.addCombination(Blocks.GRASS_BLOCK, Blocks.END_STONE, MODE_OR, new ItemStack(END_GRASS));
		CombinationRegistry.addCombination(Blocks.MYCELIUM, Blocks.END_STONE, MODE_OR, new ItemStack(END_GRASS));
		CombinationRegistry.addCombination(END_GRASS, Blocks.DIRT, MODE_OR, new ItemStack(Blocks.GRASS));
		CombinationRegistry.addCombination(Blocks.OAK_LOG, Blocks.VINE, MODE_AND, new ItemStack(VINE_LOG));
		CombinationRegistry.addCombination(VINE_LOG, Blocks.DANDELION, MODE_OR, new ItemStack(FLOWERY_VINE_LOG));
		CombinationRegistry.addCombination(VINE_LOG, Blocks.POPPY, MODE_OR, new ItemStack(FLOWERY_VINE_LOG));
		CombinationRegistry.addCombination(Blocks.MOSSY_COBBLESTONE, Blocks.DANDELION, MODE_OR, new ItemStack(FLOWERY_MOSS_STONE));
		CombinationRegistry.addCombination(Blocks.MOSSY_COBBLESTONE, Blocks.POPPY, MODE_OR, new ItemStack(FLOWERY_MOSS_STONE));
		CombinationRegistry.addCombination(Blocks.MOSSY_STONE_BRICKS, Blocks.DANDELION, MODE_OR, new ItemStack(FLOWERY_MOSS_BRICKS));
		CombinationRegistry.addCombination(Blocks.MOSSY_STONE_BRICKS, Blocks.POPPY, MODE_OR, new ItemStack(FLOWERY_MOSS_BRICKS));
		CombinationRegistry.addCombination(ItemTags.PLANKS, Blocks.NETHERRACK,  MODE_OR, new ItemStack(TREATED_PLANKS));
		CombinationRegistry.addCombination(ItemTags.PLANKS, Blocks.SNOW_BLOCK, MODE_OR, new ItemStack(FROST_PLANKS));
		CombinationRegistry.addCombination(ItemTags.PLANKS, Blocks.SNOW, MODE_OR, new ItemStack(FROST_PLANKS));
		CombinationRegistry.addCombination(ItemTags.PLANKS, Items.SNOWBALL, MODE_OR, new ItemStack(FROST_PLANKS));
		CombinationRegistry.addCombination(ItemTags.LOGS, Blocks.SNOW_BLOCK, MODE_OR, new ItemStack(FROST_LOG));
		CombinationRegistry.addCombination(ItemTags.LOGS, Blocks.SNOW, MODE_OR, new ItemStack(FROST_LOG));
		CombinationRegistry.addCombination(ItemTags.LOGS, Items.SNOWBALL, MODE_OR, new ItemStack(FROST_LOG));
		CombinationRegistry.addCombination(Items.WOODEN_SWORD, Blocks.CACTUS, MODE_OR, new ItemStack(WOODEN_CACTUS));
		CombinationRegistry.addCombination(ItemTags.PLANKS, Blocks.CACTUS, MODE_OR, new ItemStack(WOODEN_CACTUS));
		CombinationRegistry.addCombination(ItemTags.LOGS, Blocks.CACTUS, MODE_OR, new ItemStack(WOODEN_CACTUS));
		CombinationRegistry.addCombination(RAINBOW_LEAVES, RAINBOW_LOG, MODE_OR, new ItemStack(RAINBOW_SAPLING));
		CombinationRegistry.addCombination(END_LEAVES, END_LOG, MODE_OR, new ItemStack(END_SAPLING));
		CombinationRegistry.addCombination(Blocks.STONE, CARVING_TOOL, MODE_AND, new ItemStack(STONE_SLAB));
		CombinationRegistry.addCombination(Items.REDSTONE, Items.GLOWSTONE_DUST, MODE_OR, new ItemStack(GLOWYSTONE_WIRE));
		CombinationRegistry.addCombination(Blocks.CAKE, Items.APPLE, MODE_OR, new ItemStack(APPLE_CAKE));
		CombinationRegistry.addCombination(Blocks.CAKE, GLOWING_MUSHROOM, MODE_OR, new ItemStack(BLUE_CAKE));
		CombinationRegistry.addCombination(Blocks.CAKE, Blocks.ICE, MODE_OR, new ItemStack(COLD_CAKE));
		CombinationRegistry.addCombination(Blocks.CAKE, Blocks.PACKED_ICE, MODE_OR, new ItemStack(COLD_CAKE));
		CombinationRegistry.addCombination(Blocks.CAKE, Items.MELON_SLICE, MODE_OR, new ItemStack(RED_CAKE));
		CombinationRegistry.addCombination(Blocks.CAKE, Items.GLISTERING_MELON_SLICE, MODE_OR, new ItemStack(RED_CAKE));
		CombinationRegistry.addCombination(Blocks.CAKE, Items.LAVA_BUCKET, MODE_OR, new ItemStack(HOT_CAKE));
		CombinationRegistry.addCombination(Blocks.CAKE, Items.BLAZE_POWDER, MODE_OR, new ItemStack(HOT_CAKE));
		CombinationRegistry.addCombination(Blocks.CAKE, Blocks.MAGMA_BLOCK, MODE_OR, new ItemStack(HOT_CAKE));
		CombinationRegistry.addCombination(Blocks.CAKE, Blocks.GLASS, MODE_OR, new ItemStack(REVERSE_CAKE));
		CombinationRegistry.addCombination(Blocks.CAKE, Blocks.GLASS_PANE, MODE_OR, new ItemStack(REVERSE_CAKE));
		CombinationRegistry.addCombination(Items.COOKIE, Blocks.STONE, MODE_AND, new ItemStack(ROCK_COOKIE));
		CombinationRegistry.addCombination(Items.COOKIE, Blocks.COBBLESTONE, MODE_AND, new ItemStack(ROCK_COOKIE));
		CombinationRegistry.addCombination(Items.COOKIE, Blocks.GRAVEL, MODE_AND, new ItemStack(ROCK_COOKIE));
		CombinationRegistry.addCombination(Items.WHEAT_SEEDS, Blocks.BROWN_MUSHROOM, MODE_OR, new ItemStack(FUNGAL_SPORE));
		CombinationRegistry.addCombination(Items.WHEAT_SEEDS, Blocks.RED_MUSHROOM, MODE_OR, new ItemStack(FUNGAL_SPORE));
		CombinationRegistry.addCombination(Items.COOKIE, FUNGAL_SPORE, MODE_AND, new ItemStack(SPOREO));
		CombinationRegistry.addCombination(Items.POTATO, Items.STICK, MODE_AND, new ItemStack(FRENCH_FRY));
		CombinationRegistry.addCombination(Items.POTATO, Items.BLAZE_ROD, MODE_AND, new ItemStack(FRENCH_FRY));
		CombinationRegistry.addCombination(Items.EGG, Blocks.PUMPKIN, MODE_AND, new ItemStack(SURPRISE_EMBRYO));
		
		//uranium-based non-weapon and uranium cooker recipes
		CombinationRegistry.addCombination(RAW_CRUXITE, RAW_URANIUM, MODE_AND, new ItemStack(ENERGY_CORE));
		CombinationRegistry.addCombination(RAW_URANIUM, Items.COOKED_BEEF, MODE_OR, new ItemStack(IRRADIATED_STEAK));
		CombinationRegistry.addCombination(UP_STICK, ENERGY_CORE, MODE_AND, new ItemStack(QUAMTUM_SABRE));
		
		//CombinationRegistry.addCombination(new ItemStack(Items.POTIONITEM, 1, 0), new ItemStack(Items.SUGAR), MODE_OR, false, false, new ItemStack(beverage, 1, 0));		//Tab
		//CombinationRegistry.addCombination(new ItemStack(Items.POTIONITEM, 1, 0), new ItemStack(Items.DYE, 1, 14), MODE_OR, false, true, new ItemStack(beverage, 1, 1));	//Orange F
		//CombinationRegistry.addCombination(new ItemStack(beverage, 1, 1), new ItemStack(Items.APPLE), MODE_OR, true, false, new ItemStack(beverage, 1, 2));				//CandyApple F
		//CombinationRegistry.addCombination(new ItemStack(beverage, 1, 1), new ItemStack(beverage, 1, 0), MODE_OR, true, true, new ItemStack(beverage, 1, 3));				//Cola F
		//CombinationRegistry.addCombination(new ItemStack(beverage, 1, 1), new ItemStack(Blocks.WOOL, 1, 3), MODE_OR, true, true, new ItemStack(beverage, 1, 4));			//Cotton Candy F
		//CombinationRegistry.addCombination(new ItemStack(beverage, 1, 1), new ItemStack(Items.MILK_BUCKET), MODE_OR, true, false, new ItemStack(beverage, 1, 5));			//Creme F
		//CombinationRegistry.addCombination(new ItemStack(beverage, 1, 1), new ItemStack(Items.CHORUS_FRUIT), MODE_OR, true, false, new ItemStack(beverage, 1, 6));		//Grape F
		//CombinationRegistry.addCombination(new ItemStack(beverage, 1, 1), new ItemStack(Items.DYE, 1, 10), MODE_OR, true, true, new ItemStack(beverage, 1, 7));			//Moonmist F
		//CombinationRegistry.addCombination(new ItemStack(beverage, 1, 2), new ItemStack(Items.DYE, 1, 9), MODE_AND, true, true, new ItemStack(beverage, 1, 8));			//Peach F
		//CombinationRegistry.addCombination(new ItemStack(beverage, 1, 1), new ItemStack(Blocks.TNT), MODE_OR, true, false, new ItemStack(beverage, 1, 9));				//Redpop F
		
	}
	
	public static void registerModRecipes() 
	{
		/*TODO Item Tags
		AlchemyCostRegistry.addGristConversion("ingotCopper", new GristSet(new GristType[] {GristType.RUST, GristType.COBALT}, new int[] {16, 3}));
		AlchemyCostRegistry.addGristConversion("ingotTin", new GristSet(new GristType[] {GristType.RUST, GristType.CAULK}, new int[] {12, 8}));
		AlchemyCostRegistry.addGristConversion("ingotSilver", new GristSet(new GristType[] {GristType.RUST, GristType.MERCURY}, new int[] {12, 8}));
		AlchemyCostRegistry.addGristConversion("ingotLead", new GristSet(new GristType[] {GristType.RUST, GristType.COBALT, GristType.SHALE}, new int[] {12, 4, 4}));
		AlchemyCostRegistry.addGristConversion("ingotNickel", new GristSet(new GristType[] {GristType.RUST, GristType.SULFUR}, new int[] {12, 8}));
		AlchemyCostRegistry.addGristConversion("ingotInvar", new GristSet(new GristType[] {GristType.RUST, GristType.SULFUR}, new int[] {12, 5}));
		AlchemyCostRegistry.addGristConversion("ingotAluminium", new GristSet(new GristType[] {GristType.RUST, GristType.CHALK}, new int[] {12, 6}));
		
		AlchemyCostRegistry.addGristConversion("ingotCobalt", new GristSet(new GristType[] {GristType.COBALT}, new int[] {18}));
		AlchemyCostRegistry.addGristConversion("ingoTardite", new GristSet(new GristType[] {GristType.GARNET, GristType.SULFUR}, new int[] {12, 8}));
		AlchemyCostRegistry.addGristConversion("ingotRedAlloy", new GristSet(new GristType[] {GristType.RUST, GristType.GARNET}, new int[] {18, 32}));
		
		if(oreMultiplier != 0)
		{
			AlchemyCostRegistry.addGristConversion("oreCopper", new GristSet(new GristType[]{GristType.RUST, GristType.COBALT, GristType.BUILD}, new int[]{16*oreMultiplier, 3*oreMultiplier, 4}));
			AlchemyCostRegistry.addGristConversion("oreTin", new GristSet(new GristType[]{GristType.RUST, GristType.CAULK, GristType.BUILD}, new int[]{12*oreMultiplier, 8*oreMultiplier, 4}));
			AlchemyCostRegistry.addGristConversion("oreSilver", new GristSet(new GristType[]{GristType.RUST, GristType.MERCURY, GristType.BUILD}, new int[]{12*oreMultiplier, 8*oreMultiplier, 4}));
			AlchemyCostRegistry.addGristConversion("oreLead", new GristSet(new GristType[]{GristType.RUST, GristType.COBALT, GristType.SHALE, GristType.BUILD}, new int[]{12*oreMultiplier, 4*oreMultiplier, 4*oreMultiplier, 4}));
			AlchemyCostRegistry.addGristConversion("oreNickel", new GristSet(new GristType[]{GristType.RUST, GristType.SULFUR, GristType.BUILD}, new int[]{12*oreMultiplier, 8*oreMultiplier, 4}));
			AlchemyCostRegistry.addGristConversion("oreAluminium", new GristSet(new GristType[]{GristType.RUST, GristType.CHALK, GristType.BUILD}, new int[]{12*oreMultiplier, 6*oreMultiplier, 4}));
			
			AlchemyCostRegistry.addGristConversion("oreCobalt", new GristSet(new GristType[] {GristType.COBALT, GristType.BUILD}, new int[] {18*oreMultiplier, 4}));
			AlchemyCostRegistry.addGristConversion("oreArdite", new GristSet(new GristType[] {GristType.GARNET, GristType.SULFUR, GristType.BUILD}, new int[] {12*oreMultiplier, 8*oreMultiplier, 4}));
		}
		
		if(!OreDictionary.getOres("ingotRedAlloy").isEmpty())
			CombinationRegistry.addCombination(new ItemStack(Items.IRON_INGOT), new ItemStack(Items.REDSTONE), MODE_OR, OreDictionary.getOres("ingotRedAlloy").get(0));
		
		if(oreMultiplier != 0)
		{
			AlchemyCostRegistry.addGristConversion("oreCopper", new GristSet(new GristType[] {GristType.RUST, GristType.COBALT, GristType.BUILD}, new int[] {16*oreMultiplier, 3*oreMultiplier, 4}));
			AlchemyCostRegistry.addGristConversion("oreTin", new GristSet(new GristType[] {GristType.RUST, GristType.CAULK, GristType.BUILD}, new int[] {12*oreMultiplier, 8*oreMultiplier, 4}));
			AlchemyCostRegistry.addGristConversion("oreSilver", new GristSet(new GristType[] {GristType.RUST, GristType.MERCURY, GristType.BUILD}, new int[] {12*oreMultiplier, 8*oreMultiplier, 4}));
			AlchemyCostRegistry.addGristConversion("oreLead", new GristSet(new GristType[] {GristType.RUST, GristType.COBALT, GristType.SHALE, GristType.BUILD}, new int[] {12*oreMultiplier, 4*oreMultiplier, 4*oreMultiplier, 4}));
			AlchemyCostRegistry.addGristConversion("oreNickel", new GristSet(new GristType[] {GristType.RUST, GristType.SULFUR, GristType.BUILD}, new int[] {12*oreMultiplier, 8*oreMultiplier, 4}));
			AlchemyCostRegistry.addGristConversion("oreAluminium", new GristSet(new GristType[] {GristType.RUST, GristType.CHALK, GristType.BUILD}, new int[] {12*oreMultiplier, 6*oreMultiplier, 4}));
			
			AlchemyCostRegistry.addGristConversion("oreCobalt", new GristSet(new GristType[] {GristType.COBALT, GristType.BUILD}, new int[] {18*oreMultiplier, 4}));
			AlchemyCostRegistry.addGristConversion("oreArdite", new GristSet(new GristType[] {GristType.GARNET, GristType.SULFUR, GristType.BUILD}, new int[] {12*oreMultiplier, 8*oreMultiplier, 4}));
		}*/
		
		/*try
		{
			if(Loader.isModLoaded("IronChest"))
			{
				Block ironChest = ((Block) (Class.forName("cpw.mods.ironchest.IronChest").getField("ironChestBlock").get(null)));
				AlchemyCostRegistry.addGristConversion(ironChest, 0, new GristSet(new GristType[] {GristType.BUILD, GristType.RUST}, new int[] {16, 128}));
				CombinationRegistry.addCombination(new ItemStack(Blocks.CHEST), new ItemStack(Items.IRON_INGOT), MODE_AND, new ItemStack(ironChest, 1, 0));
			}
		}
		catch(Exception e) 
		{
			Debug.logger.warn("Exception while getting things for mod \"IronChest\".", e);
		}*/
		
		
		//registerRecipes(new Minegicka3Support(), "minegicka3", false);
		//registerRecipes(new NeverSayNetherSupport(), "nsn", false);
		//registerRecipes(new ExtraUtilitiesSupport(), "extrautils2", false);
		//registerRecipes(new TinkersConstructSupport(), "TConstruct", false);
		
	}
	
	public static void registerAutomaticRecipes()
	{
		AutoGristGenerator autogrist = new AutoGristGenerator();
		autogrist.prepare();
		
		//Register container costs such as for filled buckets
		for(Map.Entry<Item, GristSet> entry : containerlessCosts.entrySet())
		{
			Item item = entry.getKey();
			if(AlchemyCostRegistry.getGristConversion(item) != null)
				continue;
			Item container = item.getContainerItem();
			GristSet cost = entry.getValue();
			if(container != Items.AIR)
			{
				GristSet containerCost = autogrist.lookupCostForItem(container);
				if(containerCost == null)
				{
					Debug.warnf("Can't generate a cost for %s: %s does not have a cost.", item.getName(), container.getName());
					continue;
				} else cost.addGrist(containerCost);
			}
			
			AlchemyCostRegistry.addGristConversion(item, cost);
		}
		
		autogrist.excecute();
		
		//registerRecipes(new Minegicka3Support(), "minegicka3", true);
	}
	
	public static void onAlchemizedItem(ItemStack stack, EntityPlayerMP player)
	{
		if(!(stack.getItem() instanceof ItemCruxiteArtifact))
		{
			Echeladder e = MinestuckPlayerData.getData(player).echeladder;
			e.checkBonus(player.getServer(), Echeladder.ALCHEMY_BONUS_OFFSET);
		}
		
		GristSet set = AlchemyCostRegistry.getGristConversion(stack);
		if(set != null) //The only time the grist set should be null here is if it was a captchalogue card that was alchemized
		{
			double value = 0;
			for(GristType type : GristType.values())
			{
				int v = set.getGrist(type);
				float f = type == GristType.BUILD || type == GristType.ARTIFACT ? 0.5F : type == GristType.ZILLIUM ? 20 : type.getPower();
				if(v > 0)
					value += f*v/2;
			}
			
			Echeladder e = MinestuckPlayerData.getData(player).echeladder;
			if(value >= 50)
				e.checkBonus(player.getServer(), (byte) (Echeladder.ALCHEMY_BONUS_OFFSET + 1));
			if(value >= 500)
				e.checkBonus(player.getServer(), (byte) (Echeladder.ALCHEMY_BONUS_OFFSET + 2));
		}
	}
	
	@Nonnull
	public static ItemStack getFirstOreItem(String name)
	{
		//if(OreDictionary.getOres(name).isEmpty())
			return ItemStack.EMPTY;
		//else return OreDictionary.getOres(name).get(0);
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
		return getDecodedItem(card, false);
	}
	
	@Nonnull
	public static ItemStack getDecodedItem(ItemStack card, boolean ignoreGhost)
	{
		if (!hasDecodedItem(card))
		{
			return ItemStack.EMPTY;
		}
		NBTTagCompound tag = card.getTag();
		
		Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(tag.getString(("contentID"))));
		if (item == null) {return ItemStack.EMPTY;}
		ItemStack newItem = new ItemStack(item);
		
		if(tag.hasKey("contentTags"))
			newItem.setTag(tag.getCompound("contentTags"));
		
		if(ignoreGhost && tag.hasKey("contentSize") && tag.getInt("contentSize") <= 0)
			newItem.setCount(0);
		else if(tag.hasKey("contentSize") && tag.getInt("contentSize") >= 1)
			newItem.setCount(tag.getInt("contentSize"));
		
		return newItem;
		
	}
	
	public static boolean isPunchedCard(ItemStack item)
	{
		return item.getItem() == MinestuckItems.CAPTCHA_CARD && item.hasTag() && item.getTag().getBoolean("punched");
	}
	
	public static boolean hasDecodedItem(ItemStack item)
	{
		return item.hasTag() && item.getTag().contains("contentID", 8);
	}
	
	/**
	 * Given a punched card, this method returns a new item that represents the encoded data,
	 * or it just returns the item directly if it's not a punched card.
	 */
	@Nonnull
	public static ItemStack getDecodedItemDesignix(ItemStack card)
	{
		
		if (card.isEmpty()) {return ItemStack.EMPTY;}
		
		if (card.getItem().equals(CAPTCHA_CARD) && card.hasTag() && card.getTag().hasKey("contentID"))
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
			nbt.setString("contentID", item.getItem().getRegistryName().toString());
		}
		ItemStack stack = new ItemStack(registerToCard ? CAPTCHA_CARD : CRUXITE_DOWEL);
		stack.setTag(nbt);
		return stack;
	}
	
	@Nonnull
	public static ItemStack createEncodedItem(ItemStack itemIn, ItemStack itemOut)
	{
		NBTTagCompound nbt = null;
		if(!itemIn.isEmpty())
		{
			nbt = new NBTTagCompound();
			nbt.setString("contentID", itemIn.getItem().getRegistryName().toString());
		}
		ItemStack stack = itemOut;
		
		
		stack.setTag(nbt);
		return stack;
	}
	
	@Nonnull
	public static ItemStack createEncodedItem(ItemStack itemIn, Item itemOut)
	{
		return createEncodedItem(itemIn, new ItemStack(itemOut));
	}
	
	@Nonnull
	public static ItemStack createCard(ItemStack item, boolean punched)
	{
		ItemStack stack = createEncodedItem(item, true);
		if(!stack.hasTag())
			stack.setTag(new NBTTagCompound());
		stack.getTag().setBoolean("punched", punched);
		if(!punched)
		{
			if(item.hasTag())
				stack.getTag().setTag("contentTags", item.getTag());
			stack.getTag().setInt("contentSize", item.getCount());
		}
		
		return stack;
	}
	
	@Nonnull
	public static ItemStack createGhostCard(ItemStack item)
	{
		ItemStack stack = createEncodedItem(item, true);
		if(!stack.hasTag())
			stack.setTag(new NBTTagCompound());
		stack.getTag().setBoolean("punched", false);
			if(item.hasTag())
				stack.getTag().setTag("contentTags", item.getTag());
			stack.getTag().setInt("contentSize", 0);
		return stack;
	}
	
	public static ItemStack changeEncodeSize(ItemStack stack, int size)
	{
		
		if(!stack.hasTag())
			stack.setTag(new NBTTagCompound());
		
			stack.getTag().setInt("contentSize", size);
		
		
		return stack;
	}
	
	public static ItemStack createShunt(ItemStack item)
	{
		ItemStack stack = createEncodedItem(item, SHUNT);
		if(!stack.hasTag())
			stack.setTag(new NBTTagCompound());
		stack.getTag().setBoolean("punched", true);
		
			if(item.hasTag())
				stack.getTag().setTag("contentTags", item.getTag());
			stack.getTag().setInt("contentSize", item.getCount());
		
		
		return stack;
	}
	
	private static void registerRecipes(ModSupport modSupport, String modname, boolean dynamic)
	{
		try
		{
			/*if(ModLoader.isModLoaded(modname))
			{
				if(dynamic)
					modSupport.registerDynamicRecipes();
				else modSupport.registerRecipes();
			}*/
		}
		catch(Exception e)
		{
			Debug.logger.error("Exception while creating"+(dynamic?" dynamic":"")+" recipes for mod \""+modname+"\":", e);
		}
	}
	
	public static List<ItemStack> getItems(Object item)
	{
		if(item instanceof ItemStack)
			return Arrays.asList((ItemStack)item);
		if(item instanceof Item)
			return Arrays.asList(new ItemStack((Item) item));
		else
		{
			//List<ItemStack> list = OreDictionary.getOres((String) item);
			//return list; TODO Item tags
			return null;
		}
	}
}
