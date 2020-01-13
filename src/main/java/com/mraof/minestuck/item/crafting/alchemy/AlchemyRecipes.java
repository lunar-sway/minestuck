package com.mraof.minestuck.item.crafting.alchemy;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.event.AlchemyEvent;
import com.mraof.minestuck.item.CruxiteArtifactItem;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.util.Echeladder;
import com.mraof.minestuck.util.MSTags;
import com.mraof.minestuck.world.storage.PlayerSavedData;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;

import static com.mraof.minestuck.block.MSBlocks.*;
import static com.mraof.minestuck.item.MSItems.*;
import static com.mraof.minestuck.item.crafting.alchemy.CombinationRegistry.Mode.MODE_AND;
import static com.mraof.minestuck.item.crafting.alchemy.CombinationRegistry.Mode.MODE_OR;

@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AlchemyRecipes
{
	
	public static void registerVanillaRecipes()
	{
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
		CombinationRegistry.addCombination(ItemTags.WOODEN_FENCES, Blocks.NETHER_BRICKS, MODE_AND, new ItemStack(Blocks.NETHER_BRICK_FENCE));
		CombinationRegistry.addCombination(ItemTags.WOODEN_STAIRS, Blocks.NETHER_BRICKS, MODE_AND, new ItemStack(Blocks.NETHER_BRICK_STAIRS));
		CombinationRegistry.addCombination(ItemTags.WOODEN_FENCES, Items.NETHER_BRICK, MODE_AND, new ItemStack(Blocks.NETHER_BRICK_FENCE));
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
		CombinationRegistry.addCombination(Blocks.GLASS, Blocks.SNOW_BLOCK, MODE_AND, new ItemStack(Blocks.ICE));
		CombinationRegistry.addCombination(Blocks.SPONGE, Items.WATER_BUCKET, MODE_AND, new ItemStack(Blocks.WET_SPONGE));
		CombinationRegistry.addCombination(Items.BLAZE_POWDER, Items.GUNPOWDER, MODE_OR, new ItemStack(Items.FIRE_CHARGE));
		CombinationRegistry.addCombination(Blocks.SAND, Blocks.STONE_BRICK_STAIRS, MODE_OR, new ItemStack(Blocks.SANDSTONE_STAIRS));
		CombinationRegistry.addCombination(Blocks.RED_SAND, Blocks.STONE_BRICK_STAIRS, MODE_OR, new ItemStack(Blocks.RED_SANDSTONE_STAIRS));
		CombinationRegistry.addCombination(Blocks.SAND, Items.RED_DYE, MODE_AND, new ItemStack(Blocks.RED_SAND));
		CombinationRegistry.addCombination(Blocks.SANDSTONE, Items.RED_DYE, MODE_AND, new ItemStack(Blocks.RED_SANDSTONE));
		CombinationRegistry.addCombination(Blocks.CUT_SANDSTONE, Items.RED_DYE, MODE_AND, new ItemStack(Blocks.CUT_RED_SANDSTONE));
		CombinationRegistry.addCombination(Blocks.CHISELED_SANDSTONE, Items.RED_DYE, MODE_AND, new ItemStack(Blocks.CHISELED_RED_SANDSTONE));
		CombinationRegistry.addCombination(Blocks.SMOOTH_SANDSTONE, Items.RED_DYE, MODE_AND, new ItemStack(Blocks.SMOOTH_RED_SANDSTONE));
		CombinationRegistry.addCombination(Blocks.SANDSTONE_STAIRS, Items.RED_DYE, MODE_AND, new ItemStack(Blocks.RED_SANDSTONE_STAIRS));
		CombinationRegistry.addCombination(ItemTags.PLANKS, Items.BOOK, MODE_OR, new ItemStack(Blocks.BOOKSHELF));
		CombinationRegistry.addCombination(ItemTags.MUSIC_DISCS, Blocks.NOTE_BLOCK, MODE_AND, new ItemStack(Blocks.JUKEBOX));
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
		CombinationRegistry.addCombination(Blocks.GRAVEL, Items.RED_DYE, MODE_AND, new ItemStack(Items.REDSTONE));
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
		CombinationRegistry.addCombination(Items.POTION, Items.ENCHANTED_BOOK, MODE_OR, new ItemStack(Items.EXPERIENCE_BOTTLE));
		CombinationRegistry.addCombination(Items.GLASS_BOTTLE, Items.ENCHANTED_BOOK, MODE_OR, new ItemStack(Items.EXPERIENCE_BOTTLE));
		CombinationRegistry.addCombination(Items.QUARTZ, Items.WATER_BUCKET, MODE_OR, new ItemStack(Items.PRISMARINE_CRYSTALS));
		CombinationRegistry.addCombination(Items.QUARTZ, Items.WATER_BUCKET, MODE_AND, new ItemStack(Items.PRISMARINE_SHARD));
		CombinationRegistry.addCombination(Items.FEATHER, Items.ENDER_PEARL, MODE_OR, new ItemStack(Items.ELYTRA));
		CombinationRegistry.addCombination(Items.COOKIE, Items.REDSTONE, MODE_AND, new ItemStack(Items.SUGAR));
	}
	
	public static void registerMinestuckRecipes()
	{
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
		CombinationRegistry.addCombination(KATANA, Blocks.BEDROCK, MODE_AND, new ItemStack(UNBREAKABLE_KATANA));
		CombinationRegistry.addCombination(HOT_HANDLE, Blocks.LAPIS_BLOCK, MODE_OR, new ItemStack(COBALT_SABRE));
		CombinationRegistry.addCombination(CALEDSCRATCH, FROG, MODE_AND, new ItemStack(SCARLET_RIBBITAR));
		CombinationRegistry.addCombination(Blocks.BEACON, Items.DIAMOND_SWORD, MODE_AND, new ItemStack(SHATTER_BEACON));
		
		//axes
		CombinationRegistry.addCombination(Items.WOODEN_AXE, SBAHJ_POSTER, MODE_AND, new ItemStack(BATLEACKS));
		CombinationRegistry.addCombination(Items.STONE_AXE, SBAHJ_POSTER, MODE_AND, new ItemStack(BATLEACKS));
		CombinationRegistry.addCombination(Items.IRON_AXE, Blocks.PISTON, MODE_AND, new ItemStack(COPSE_CRUSHER));
		CombinationRegistry.addCombination(Items.IRON_AXE, Blocks.IRON_BLOCK, MODE_AND, new ItemStack(BATTLEAXE));
		CombinationRegistry.addCombination(Items.WOODEN_AXE, Blocks.ANVIL, MODE_AND, new ItemStack(BLACKSMITH_BANE));
		CombinationRegistry.addCombination(ItemTags.MUSIC_DISCS, Items.IRON_AXE, MODE_AND, new ItemStack(SCRAXE));
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
		CombinationRegistry.addCombination(CAPTCHA_CARD, MSBlocks.COMPUTER, MODE_AND, new ItemStack(CAPTCHAROID_CAMERA));
		CombinationRegistry.addCombination(CAPTCHA_CARD, Items.ENDER_EYE, MODE_OR, new ItemStack(CAPTCHAROID_CAMERA));
		
		CombinationRegistry.addCombination(STACK_MODUS_CARD, QUEUE_MODUS_CARD, MODE_AND, new ItemStack(QUEUESTACK_MODUS_CARD));
		CombinationRegistry.addCombination(MSTags.Items.MODUS_CARD, Items.STICK, MODE_OR, new ItemStack(TREE_MODUS_CARD));
		CombinationRegistry.addCombination(MSTags.Items.MODUS_CARD, ItemTags.SAPLINGS, MODE_OR, new ItemStack(TREE_MODUS_CARD));
		CombinationRegistry.addCombination(MSTags.Items.MODUS_CARD, ItemTags.LEAVES, MODE_OR, new ItemStack(TREE_MODUS_CARD));	//Not planks and logs though. Too little branch-related.
		CombinationRegistry.addCombination(MSTags.Items.MODUS_CARD, COMPUTER, MODE_AND, new ItemStack(HASHMAP_MODUS_CARD));
		CombinationRegistry.addCombination(MSTags.Items.MODUS_CARD, Items.ITEM_FRAME, MODE_AND, new ItemStack(SET_MODUS_CARD));
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
		
		{
			CombinationRegistry.addCombination(Items.COAL, Blocks.NETHERRACK, MODE_AND, new ItemStack(NETHERRACK_COAL_ORE));
			CombinationRegistry.addCombination(Items.IRON_INGOT, Blocks.END_STONE, MODE_AND, new ItemStack(END_STONE_IRON_ORE));
			CombinationRegistry.addCombination(Items.IRON_INGOT, Blocks.SANDSTONE, MODE_AND, new ItemStack(SANDSTONE_IRON_ORE));
			CombinationRegistry.addCombination(Items.IRON_INGOT, Blocks.RED_SANDSTONE, MODE_AND, new ItemStack(RED_SANDSTONE_IRON_ORE));
			CombinationRegistry.addCombination(Items.GOLD_INGOT, Blocks.SANDSTONE, MODE_AND, new ItemStack(SANDSTONE_GOLD_ORE));
			CombinationRegistry.addCombination(Items.GOLD_INGOT, Blocks.RED_SANDSTONE, MODE_AND, new ItemStack(RED_SANDSTONE_GOLD_ORE));
			CombinationRegistry.addCombination(Items.REDSTONE, Blocks.END_STONE, MODE_AND, new ItemStack(END_STONE_REDSTONE_ORE));
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
		CombinationRegistry.addCombination(Blocks.TNT, Items.REDSTONE, MODE_OR, new ItemStack(INSTANT_TNT));
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
		
		CombinationRegistry.addCombination(Tags.Items.STONE, BLUE_DIRT, MODE_OR, new ItemStack(SMOOTH_SHADE_STONE));
		CombinationRegistry.addCombination(Tags.Items.STONE, Items.LAPIS_LAZULI, MODE_OR, new ItemStack(SMOOTH_SHADE_STONE));
		CombinationRegistry.addCombination(Tags.Items.STONE, Blocks.ICE, MODE_AND, new ItemStack(FROST_TILE));
		CombinationRegistry.addCombination(Tags.Items.STONE, Blocks.PACKED_ICE, MODE_AND, new ItemStack(FROST_TILE));
		
		CombinationRegistry.addCombination(Blocks.STONE_BRICKS, Blocks.ICE, MODE_AND, new ItemStack(FROST_BRICKS));
		CombinationRegistry.addCombination(Blocks.STONE_BRICKS, Blocks.PACKED_ICE, MODE_AND, new ItemStack(FROST_BRICKS));
		CombinationRegistry.addCombination(Blocks.CHISELED_STONE_BRICKS, Blocks.ICE, MODE_AND, new ItemStack(CHISELED_FROST_BRICKS));
		CombinationRegistry.addCombination(Blocks.CHISELED_STONE_BRICKS, Blocks.PACKED_ICE, MODE_AND, new ItemStack(CHISELED_FROST_BRICKS));
		CombinationRegistry.addCombination(Blocks.IRON_BLOCK, Items.LAVA_BUCKET, MODE_AND, new ItemStack(CAST_IRON));
		CombinationRegistry.addCombination(Blocks.CHISELED_STONE_BRICKS, CAST_IRON, MODE_OR, new ItemStack(CHISELED_CAST_IRON));
		CombinationRegistry.addCombination(Blocks.COARSE_DIRT, Blocks.END_STONE, MODE_OR, new ItemStack(COARSE_END_STONE));
		CombinationRegistry.addCombination(Blocks.GRASS_BLOCK, Blocks.END_STONE, MODE_OR, new ItemStack(END_GRASS));
		CombinationRegistry.addCombination(Blocks.MYCELIUM, Blocks.END_STONE, MODE_OR, new ItemStack(END_GRASS));
		CombinationRegistry.addCombination(END_GRASS, Blocks.DIRT, MODE_OR, new ItemStack(Blocks.GRASS_BLOCK));
		CombinationRegistry.addCombination(Blocks.OAK_LOG, Blocks.VINE, MODE_AND, new ItemStack(VINE_LOG));
		CombinationRegistry.addCombination(VINE_LOG, Blocks.DANDELION, MODE_OR, new ItemStack(FLOWERY_VINE_LOG));
		CombinationRegistry.addCombination(VINE_LOG, Blocks.POPPY, MODE_OR, new ItemStack(FLOWERY_VINE_LOG));
		CombinationRegistry.addCombination(Blocks.MOSSY_COBBLESTONE, Blocks.DANDELION, MODE_OR, new ItemStack(FLOWERY_MOSSY_COBBLESTONE));
		CombinationRegistry.addCombination(Blocks.MOSSY_COBBLESTONE, Blocks.POPPY, MODE_OR, new ItemStack(FLOWERY_MOSSY_COBBLESTONE));
		CombinationRegistry.addCombination(Blocks.MOSSY_STONE_BRICKS, Blocks.DANDELION, MODE_OR, new ItemStack(FLOWERY_MOSSY_STONE_BRICKS));
		CombinationRegistry.addCombination(Blocks.MOSSY_STONE_BRICKS, Blocks.POPPY, MODE_OR, new ItemStack(FLOWERY_MOSSY_STONE_BRICKS));
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
		CombinationRegistry.addCombination(Items.REDSTONE, Items.GLOWSTONE_DUST, MODE_OR, new ItemStack(GLOWYSTONE_DUST));
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
		CombinationRegistry.addCombination(UP_STICK, ENERGY_CORE, MODE_AND, new ItemStack(QUANTUM_SABRE));
		
		CombinationRegistry.addCombination(Items.POTION, Items.SUGAR, MODE_OR, new ItemStack(TAB));
		CombinationRegistry.addCombination(Items.POTION, Items.ORANGE_DYE, MODE_OR, new ItemStack(FAYGO));
		CombinationRegistry.addCombination(FAYGO, Items.APPLE, MODE_OR, new ItemStack(FAYGO_CANDY_APPLE));
		CombinationRegistry.addCombination(FAYGO, TAB, MODE_OR, new ItemStack(FAYGO_COLA));
		CombinationRegistry.addCombination(FAYGO, Items.LIGHT_BLUE_WOOL, MODE_OR, new ItemStack(FAYGO_COTTON_CANDY));
		CombinationRegistry.addCombination(FAYGO, Items.MILK_BUCKET, MODE_OR, new ItemStack(FAYGO_CREME));
		CombinationRegistry.addCombination(FAYGO, Items.CHORUS_FRUIT, MODE_OR, new ItemStack(FAYGO_GRAPE));
		CombinationRegistry.addCombination(FAYGO, Items.LIME_DYE, MODE_OR, new ItemStack(FAYGO_MOON_MIST));
		CombinationRegistry.addCombination(FAYGO_CANDY_APPLE, Items.PINK_DYE, MODE_AND, new ItemStack(FAYGO_PEACH));
		CombinationRegistry.addCombination(FAYGO, Items.TNT, MODE_OR, new ItemStack(FAYGO_REDPOP));
	}
	
	@SubscribeEvent
	public static void onAlchemizedItem(AlchemyEvent event)
	{
		Echeladder e = PlayerSavedData.getData(event.getPlayer(), event.getWorld()).getEcheladder();
		
		if(!(event.getItemResult().getItem() instanceof CruxiteArtifactItem))
		{
			e.checkBonus(Echeladder.ALCHEMY_BONUS_OFFSET);
			GristSet cost = event.getCost();
			double value = cost.getValue();
			if(value >= 50)
				e.checkBonus((byte) (Echeladder.ALCHEMY_BONUS_OFFSET + 1));
			if(value >= 500)
				e.checkBonus((byte) (Echeladder.ALCHEMY_BONUS_OFFSET + 2));
		}
		
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
		CompoundNBT tag = card.getTag();
		
		Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(tag.getString(("contentID"))));
		if (item == null) {return ItemStack.EMPTY;}
		ItemStack newItem = new ItemStack(item);
		
		if(tag.contains("contentTags"))
			newItem.setTag(tag.getCompound("contentTags"));
		
		if(ignoreGhost && tag.contains("contentSize") && tag.getInt("contentSize") <= 0)
			newItem.setCount(0);
		else if(tag.contains("contentSize") && tag.getInt("contentSize") >= 1)
			newItem.setCount(tag.getInt("contentSize"));
		
		return newItem;
		
	}
	
	public static boolean isPunchedCard(ItemStack item)
	{
		return item.getItem() == MSItems.CAPTCHA_CARD && item.hasTag() && item.getTag().getBoolean("punched");
	}
	
	public static boolean isGhostCard(ItemStack item)
	{
		return item.getItem() == MSItems.CAPTCHA_CARD && hasDecodedItem(item) && item.getTag().getInt("contentSize") <= 0;
	}
	
	public static boolean hasDecodedItem(ItemStack item)
	{
		return item.hasTag() && item.getTag().contains("contentID", Constants.NBT.TAG_STRING);
	}
	
	/**
	 * Given a punched card, this method returns a new item that represents the encoded data,
	 * or it just returns the item directly if it's not a punched card.
	 */
	@Nonnull
	public static ItemStack getDecodedItemDesignix(ItemStack card)
	{
		
		if (card.isEmpty()) {return ItemStack.EMPTY;}
		
		if (card.getItem().equals(CAPTCHA_CARD) && card.hasTag() && card.getTag().contains("contentID"))
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
		CompoundNBT nbt = null;
		if(!item.isEmpty())
		{
			nbt = new CompoundNBT();
			nbt.putString("contentID", item.getItem().getRegistryName().toString());
		}
		ItemStack stack = new ItemStack(registerToCard ? CAPTCHA_CARD : CRUXITE_DOWEL);
		stack.setTag(nbt);
		return stack;
	}
	
	@Nonnull
	public static ItemStack createEncodedItem(ItemStack itemIn, ItemStack itemOut)
	{
		CompoundNBT nbt = null;
		if(!itemIn.isEmpty())
		{
			nbt = new CompoundNBT();
			nbt.putString("contentID", itemIn.getItem().getRegistryName().toString());
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
			stack.setTag(new CompoundNBT());
		stack.getTag().putBoolean("punched", punched);
		if(!punched)
		{
			if(item.hasTag())
				stack.getTag().put("contentTags", item.getTag());
			stack.getTag().putInt("contentSize", item.getCount());
		}
		
		return stack;
	}
	
	@Nonnull
	public static ItemStack createGhostCard(ItemStack item)
	{
		ItemStack stack = createEncodedItem(item, true);
		if(!stack.hasTag())
			stack.setTag(new CompoundNBT());
		stack.getTag().putBoolean("punched", false);
			if(item.hasTag())
				stack.getTag().put("contentTags", item.getTag());
			stack.getTag().putInt("contentSize", 0);
		return stack;
	}
	
	public static ItemStack changeEncodeSize(ItemStack stack, int size)
	{
		
		if(!stack.hasTag())
			stack.setTag(new CompoundNBT());
		
			stack.getTag().putInt("contentSize", size);
		
		
		return stack;
	}
	
	public static ItemStack createShunt(ItemStack item)
	{
		ItemStack stack = createEncodedItem(item, SHUNT);
		if(!stack.hasTag())
			stack.setTag(new CompoundNBT());
		stack.getTag().putBoolean("punched", true);
		
			if(item.hasTag())
				stack.getTag().put("contentTags", item.getTag());
			stack.getTag().putInt("contentSize", item.getCount());
		
		
		return stack;
	}
}