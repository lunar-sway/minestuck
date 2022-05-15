package com.mraof.minestuck.data.recipe;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.BlockCollections;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.util.MSTags;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.common.Tags;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class MinestuckCombinationsProvider extends RecipeProvider
{
	public MinestuckCombinationsProvider(DataGenerator generatorIn)
	{
		super(generatorIn);
	}
	
	@Override
	protected void buildShapelessRecipes(Consumer<IFinishedRecipe> consumer)
	{
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
				{Blocks.OAK_FENCE_GATE, Blocks.SPRUCE_FENCE_GATE, Blocks.BIRCH_FENCE_GATE, Blocks.JUNGLE_FENCE_GATE, Blocks.ACACIA_FENCE_GATE, Blocks.DARK_OAK_FENCE_GATE},
				{Blocks.OAK_TRAPDOOR, Blocks.SPRUCE_TRAPDOOR, Blocks.BIRCH_TRAPDOOR, Blocks.JUNGLE_TRAPDOOR, Blocks.ACACIA_TRAPDOOR, Blocks.DARK_OAK_TRAPDOOR}};
		//0: oak, 1: spruce, 2: birch, 3: jungle, 4: acacia, 5: dark oak
		// [0] || [1] -> [2]
		int[][] woodCombinations = {{0, 1, 5}, {2, 3, 4}};
		//0: log, 1: planks, 2: slab, 3: stairs, 4: sapling, 5: leaves, 6: door, 7: fence, 8: fence gate, 9: trapdoor
		// [0] || [1] -> [2]
		int[][] itemCombinations = {{1, 2, 3}, {0, 5, 4}, {6, 7, 8}, {6, 2, 9}};
		
		for(IItemProvider[] itemType : woodItems)
		{
			for(int[] combination : woodCombinations)
				CombinationRecipeBuilder.of(itemType[combination[2]]).input(itemType[combination[0]]).or().input(itemType[combination[1]]).namedSource("wood_combination").buildFor(consumer, Minestuck.MOD_ID);
		}
		for(int woodType = 0; woodType < woodItems[0].length; woodType++)
		{
			for(int[] combination : itemCombinations)
				CombinationRecipeBuilder.of(woodItems[combination[2]][woodType]).input(woodItems[combination[0]][woodType]).or().input(woodItems[combination[1]][woodType]).namedSource("item_combination").buildFor(consumer, Minestuck.MOD_ID);
			CombinationRecipeBuilder.of(woodItems[4][woodType]).namedInput(Items.WHEAT_SEEDS).and().input(woodItems[5][woodType]).buildFor(consumer, Minestuck.MOD_ID);	//seeds || leaves -> sapling
			CombinationRecipeBuilder.of(woodItems[4][woodType]).namedInput(Items.STICK).and().input(woodItems[5][woodType]).buildFor(consumer, Minestuck.MOD_ID);	//stick || leaves -> sapling
		}
		for(int[] wood : woodCombinations)
		{
			for(int[] item : itemCombinations)
			{
				CombinationRecipeBuilder.of(woodItems[item[2]][wood[2]]).namedInput(woodItems[item[0]][wood[0]]).or().namedInput(woodItems[item[1]][wood[1]]).buildFor(consumer, Minestuck.MOD_ID);
				CombinationRecipeBuilder.of(woodItems[item[2]][wood[2]]).namedInput(woodItems[item[0]][wood[1]]).or().namedInput(woodItems[item[1]][wood[0]]).buildFor(consumer, Minestuck.MOD_ID);
			}
		}
		
		List<Function<DyeColor, Block>> coloredBlocks = Arrays.asList(BlockCollections::coloredWool, BlockCollections::coloredTerracotta, BlockCollections::coloredGlass, BlockCollections::coloredGlassPane);
		for(Function<DyeColor, Block> blockProvider : coloredBlocks)
		{
			for (DyeColor color : DyeColor.values())
			{
				if(color != DyeColor.WHITE)
					CombinationRecipeBuilder.of(blockProvider.apply(color)).input(color.getTag()).or().namedInput(blockProvider.apply(DyeColor.WHITE)).buildFor(consumer, Minestuck.MOD_ID);
			}
		}
		for (DyeColor color : DyeColor.values())
		{
			CombinationRecipeBuilder.of(BlockCollections.coloredGlass(color)).namedInput(Items.GLASS).and().input(color.getTag()).buildFor(consumer, Minestuck.MOD_ID);
			CombinationRecipeBuilder.of(BlockCollections.coloredGlassPane(color)).namedInput(Items.GLASS_PANE).and().input(color.getTag()).buildFor(consumer, Minestuck.MOD_ID);
			CombinationRecipeBuilder.of(BlockCollections.coloredTerracotta(color)).namedInput(Items.TERRACOTTA).and().input(color.getTag()).buildFor(consumer, Minestuck.MOD_ID);
			CombinationRecipeBuilder.of(BlockCollections.coloredConcrete(color)).namedInput(BlockCollections.coloredConcretePowder(color)).or().input(Items.WATER_BUCKET).buildFor(consumer, Minestuck.MOD_ID);
			
			if(color != DyeColor.LIGHT_GRAY)
				CombinationRecipeBuilder.of(BlockCollections.coloredConcretePowder(color)).input(color.getTag()).or().namedInput(BlockCollections.coloredConcretePowder(DyeColor.LIGHT_GRAY)).buildFor(consumer, Minestuck.MOD_ID);
		}
		//Precious Blocks
		CombinationRecipeBuilder.of(Items.COAL_BLOCK).input(Items.COAL).or().input(Items.STONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.DIAMOND_BLOCK).input(Items.DIAMOND).or().input(Items.STONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.LAPIS_BLOCK).input(Items.LAPIS_LAZULI).or().input(Items.STONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.EMERALD_BLOCK).input(Items.EMERALD).or().input(Items.STONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.GOLD_BLOCK).input(Items.GOLD_INGOT).or().input(Items.STONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.IRON_BLOCK).input(Items.IRON_INGOT).or().input(Items.STONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.QUARTZ_BLOCK).input(Items.QUARTZ).or().input(Items.STONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.REDSTONE_BLOCK).input(Items.REDSTONE).or().input(Items.STONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.NETHERITE_BLOCK).input(Items.NETHERITE_INGOT).or().input(Items.STONE).buildFor(consumer, Minestuck.MOD_ID);
		//Ore blocks
		CombinationRecipeBuilder.of(Items.COAL_ORE).input(Items.COAL).and().input(Items.STONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.DIAMOND_ORE).input(Items.DIAMOND).and().input(Items.STONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.LAPIS_ORE).input(Items.LAPIS_LAZULI).and().input(Items.STONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.EMERALD_ORE).input(Items.EMERALD).and().input(Items.STONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.GOLD_ORE).input(Items.GOLD_INGOT).and().input(Items.STONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.IRON_ORE).input(Items.IRON_INGOT).and().input(Items.STONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.NETHER_QUARTZ_ORE).input(Items.QUARTZ).and().input(Items.STONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.REDSTONE_ORE).input(Items.REDSTONE).and().input(Items.STONE).buildFor(consumer, Minestuck.MOD_ID);
		//Ores
		CombinationRecipeBuilder.of(Items.DIAMOND).input(Items.EMERALD).and().namedInput(Items.COAL).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.DIAMOND).input(Items.EMERALD).and().namedInput(Items.LAPIS_LAZULI).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.CHARCOAL).input(ItemTags.LOGS).and().input(Items.COAL).buildFor(consumer, Minestuck.MOD_ID);
		//Construction Blocks
		CombinationRecipeBuilder.of(Items.STONE_BRICKS).input(Items.STONE).and().input(Items.CRACKED_STONE_BRICKS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.CRACKED_STONE_BRICKS).input(Items.COBBLESTONE).and().input(Items.STONE_BRICKS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.COBBLESTONE).input(Items.STONE).or().namedInput(Items.CRACKED_STONE_BRICKS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.COBBLESTONE).input(Items.STONE).or().namedInput(Items.GRAVEL).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.GRAVEL).input(ItemTags.SAND).and().input(Items.COBBLESTONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.STONE).input(Items.COBBLESTONE).and().namedInput(Items.CUT_SANDSTONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.SANDSTONE).namedInput(Items.COBBLESTONE).or().namedInput(Items.CUT_SANDSTONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.SANDSTONE).namedInput(Items.STONE).or().namedInput(Items.SAND).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.SANDSTONE).namedInput(Items.COBBLESTONE).or().namedInput(Items.SAND).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.SANDSTONE).namedInput(Items.GRAVEL).or().namedInput(Items.CUT_SANDSTONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.SANDSTONE).namedInput(Items.CUT_SANDSTONE).or().namedInput(Items.SAND).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.STONE).input(Items.COBBLESTONE).and().namedInput(Items.CUT_RED_SANDSTONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.RED_SANDSTONE).namedInput(Items.COBBLESTONE).or().namedInput(Items.CUT_RED_SANDSTONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.RED_SANDSTONE).namedInput(Items.COBBLESTONE).or().namedInput(Items.RED_SAND).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.RED_SANDSTONE).namedInput(Items.GRAVEL).or().namedInput(Items.CUT_RED_SANDSTONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.RED_SANDSTONE).namedInput(Items.CUT_RED_SANDSTONE).or().namedInput(Items.RED_SAND).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.SANDSTONE_STAIRS).input(Items.SAND).or().input(Items.STONE_BRICK_STAIRS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.RED_SANDSTONE_STAIRS).input(Items.RED_SAND).or().input(Items.STONE_BRICK_STAIRS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.RED_SAND).input(Items.SAND).and().input(Items.RED_DYE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.RED_SANDSTONE).input(Items.SANDSTONE).and().input(Items.RED_DYE).namedSource("dye").buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.CUT_RED_SANDSTONE).input(Items.CUT_SANDSTONE).and().input(Items.RED_DYE).namedSource("dye").buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.CHISELED_RED_SANDSTONE).input(Items.CHISELED_SANDSTONE).and().input(Items.RED_DYE).namedSource("dye").buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.SMOOTH_RED_SANDSTONE).input(Items.SMOOTH_SANDSTONE).and().input(Items.RED_DYE).namedSource("dye").buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.RED_SANDSTONE_STAIRS).input(Items.SANDSTONE_STAIRS).and().input(Items.RED_DYE).namedSource("dye").buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.LIGHT_GRAY_CONCRETE_POWDER).input(Items.GRAVEL).or().input(Items.SAND).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.MOSSY_COBBLESTONE).input(Items.COBBLESTONE).or().input(Items.WHEAT_SEEDS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.MOSSY_COBBLESTONE_WALL).input(Items.COBBLESTONE_WALL).or().namedInput(Items.WHEAT_SEEDS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.MOSSY_COBBLESTONE_WALL).input(Items.COBBLESTONE_WALL).or().namedInput(Items.MOSSY_COBBLESTONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.MOSSY_STONE_BRICKS).input(Items.STONE_BRICKS).or().input(Items.WHEAT_SEEDS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.CLAY).input(Items.SAND).and().input(Items.WATER_BUCKET).buildFor(consumer, Minestuck.MOD_ID);
		//miscellaneous
		CombinationRecipeBuilder.of(Items.FURNACE).input(Items.COBBLESTONE).and().input(Items.COAL).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.JUKEBOX).input(Items.NOTE_BLOCK).and().namedInput(Items.DIAMOND).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.JUKEBOX).input(Items.NOTE_BLOCK).and().namedInput(ItemTags.MUSIC_DISCS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.TNT).input(ItemTags.SAND).and().input(Items.GUNPOWDER).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.IRON_TRAPDOOR).input(ItemTags.WOODEN_TRAPDOORS).or().namedInput(Items.IRON_INGOT).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.IRON_TRAPDOOR).input(ItemTags.WOODEN_TRAPDOORS).or().namedInput(Items.IRON_BLOCK).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.DROPPER).input(Items.DISPENSER).and().input(Items.HOPPER).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.ENDER_CHEST).input(Items.ENDER_PEARL).and().input(Items.CHEST).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.BOOKSHELF).input(Items.BOOK).or().namedInput(ItemTags.PLANKS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.BOOKSHELF).input(Items.BOOK).and().namedInput(Items.CHEST).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.ENCHANTING_TABLE).input(Items.BOOK).and().input(Items.EXPERIENCE_BOTTLE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.ANVIL).input(Items.CRAFTING_TABLE).or().input(Items.IRON_BLOCK).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.IRON_DOOR).input(ItemTags.WOODEN_DOORS).and().input(Items.IRON_INGOT).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.COMPASS).namedInput(Items.CLOCK).or().input(Items.IRON_INGOT).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.COMPASS).namedInput(Items.REDSTONE).or().input(Items.IRON_INGOT).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.CLOCK).namedInput(Items.COMPASS).and().input(Items.GOLD_INGOT).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.CLOCK).namedInput(Items.REDSTONE).and().input(Items.GOLD_INGOT).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.BELL).namedInput(MSItems.HORN).and().input(Items.CLOCK).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.GLOWSTONE_DUST).input(Items.TORCH).or().input(Items.REDSTONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.WRITABLE_BOOK).input(Items.BOOK).or().input(ItemTags.SIGNS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.NAME_TAG).input(ItemTags.SIGNS).and().input(Items.LEAD).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.BOOK).input(Items.INK_SAC).and().input(Items.PAPER).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.CARTOGRAPHY_TABLE).input(Items.CRAFTING_TABLE).or().input(Items.MAP).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.COBWEB).input(Items.SLIME_BLOCK).or().input(Items.STRING).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.TORCH).input(Items.REDSTONE_TORCH).or().input(ItemTags.COALS).buildFor(consumer, Minestuck.MOD_ID);
		//Plants
		CombinationRecipeBuilder.of(Items.GRASS_BLOCK).input(Items.DIRT).and().namedInput(Items.GRASS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.GRASS_BLOCK).input(Items.DIRT).and().namedInput(Items.WHEAT_SEEDS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.MYCELIUM).input(Items.GRASS_BLOCK).and().input(Tags.Items.MUSHROOMS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.CACTUS).input(Items.SAND).and().input(Items.GRASS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.DEAD_BUSH).input(Items.SAND).or().namedInput(Items.GRASS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.DEAD_BUSH).input(Items.SAND).or().namedInput(ItemTags.SAPLINGS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.LILY_PAD).input(ItemTags.LEAVES).or().input(Items.WATER_BUCKET).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.VINE).input(ItemTags.LEAVES).and().input(Items.LADDER).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.ROSE_BUSH).input(Items.LARGE_FERN).or().input(Items.POPPY).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.SUGAR_CANE).input(Items.BAMBOO).and().input(Items.SUGAR).buildFor(consumer, Minestuck.MOD_ID);
		//Transport
		CombinationRecipeBuilder.of(Items.MINECART).input(ItemTags.BOATS).or().input(Items.RAIL).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.CHEST_MINECART).input(Items.MINECART).and().input(Items.CHEST).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.FURNACE_MINECART).input(Items.MINECART).and().input(Items.FURNACE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.TNT_MINECART).input(Items.MINECART).and().input(Items.TNT).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.HOPPER_MINECART).input(Items.MINECART).and().input(Items.HOPPER).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.RAIL).namedInput(Items.IRON_BARS).and().input(Items.STICK).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.RAIL).namedInput(Items.LADDER).and().input(Items.IRON_INGOT).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.ACTIVATOR_RAIL).input(Items.RAIL).and().input(Items.REDSTONE_TORCH).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.DETECTOR_RAIL).input(Items.RAIL).and().namedInput(ItemTags.WOODEN_PRESSURE_PLATES).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.DETECTOR_RAIL).input(Items.RAIL).and().namedInput(Items.STONE_PRESSURE_PLATE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.DETECTOR_RAIL).input(Items.RAIL).and().namedInput(Items.LIGHT_WEIGHTED_PRESSURE_PLATE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.DETECTOR_RAIL).input(Items.RAIL).and().namedInput(Items.HEAVY_WEIGHTED_PRESSURE_PLATE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.POWERED_RAIL).input(Items.RAIL).and().namedInput(Items.GOLD_INGOT).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.POWERED_RAIL).input(Items.RAIL).and().namedInput(Items.FURNACE_MINECART).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.LADDER).input(ItemTags.PLANKS).or().namedInput(Items.RAIL).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.LADDER).input(Items.STICK).or().input(Items.VINE).buildFor(consumer, Minestuck.MOD_ID);
		//Redstone
		CombinationRecipeBuilder.of(Items.REDSTONE_TORCH).input(Items.TORCH).and().input(Items.REDSTONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.REDSTONE_LAMP).input(Items.GLOWSTONE).and().input(Items.REDSTONE_TORCH).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.REDSTONE).input(Items.GUNPOWDER).or().namedInput(Items.STONE_BUTTON).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.REDSTONE).input(Items.GUNPOWDER).or().namedInput(Items.STONE_PRESSURE_PLATE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.REDSTONE).input(Items.GUNPOWDER).or().namedInput(Items.LEVER).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.REDSTONE).namedInput(Items.GRAVEL).and().input(Items.RED_DYE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.REDSTONE_TORCH).input(Items.GUNPOWDER).or().namedInput(ItemTags.WOODEN_BUTTONS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.REDSTONE_TORCH).input(Items.GUNPOWDER).or().namedInput(ItemTags.WOODEN_PRESSURE_PLATES).buildFor(consumer, Minestuck.MOD_ID);
		//Nether
		CombinationRecipeBuilder.of(Items.NETHERRACK).input(Items.COBBLESTONE).and().input(Items.LAVA_BUCKET).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.SOUL_SAND).input(ItemTags.SAND).or().input(Items.NETHER_WART).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.SOUL_SOIL).input(Items.DIRT).or().input(Items.NETHER_WART).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.BLACKSTONE).input(Items.STONE).or().namedInput(Items.BLACK_DYE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.BASALT).input(Items.LAVA_BUCKET).or().namedInput(Items.QUARTZ_PILLAR).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.BASALT).input(Items.LAVA_BUCKET).or().namedInput(Items.PURPUR_PILLAR).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.CRYING_OBSIDIAN).input(Items.OBSIDIAN).or().namedInput(Items.GHAST_TEAR).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.NETHER_WART).input(Items.RED_MUSHROOM).and().input(Items.SOUL_SAND).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.NETHER_BRICKS).input(Items.NETHERRACK).and().namedInput(Items.BRICKS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.NETHER_BRICKS).input(Items.NETHERRACK).and().namedInput(Items.BRICK).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.NETHER_BRICK).namedInput(Items.NETHERRACK).or().input(Items.BRICK).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.NETHER_BRICK).namedInput(Items.NETHER_BRICKS).or().input(Items.BRICK).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.NETHER_BRICK_FENCE).input(ItemTags.WOODEN_FENCES).and().namedInput(Blocks.NETHER_BRICKS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.NETHER_BRICK_FENCE).input(ItemTags.WOODEN_FENCES).and().namedInput(Items.NETHER_BRICK).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.NETHER_BRICK_STAIRS).input(ItemTags.WOODEN_STAIRS).and().namedInput(Blocks.NETHER_BRICKS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.NETHER_BRICK_STAIRS).input(ItemTags.WOODEN_STAIRS).and().namedInput(Items.NETHER_BRICK).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.GLOWSTONE).input(Items.NETHERRACK).and().input(Items.GLOWSTONE_DUST).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.CRIMSON_STEM).input(ItemTags.LOGS).or().input(Items.CRIMSON_FUNGUS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.WARPED_STEM).input(ItemTags.LOGS).or().input(Items.WARPED_FUNGUS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.CRIMSON_ROOTS).input(Items.GRASS).or().input(Items.CRIMSON_FUNGUS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.WARPED_ROOTS).input(Items.GRASS).or().input(Items.WARPED_FUNGUS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.WEEPING_VINES).input(Items.VINE).and().input(Items.CRIMSON_ROOTS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.TWISTING_VINES).input(Items.VINE).and().input(Items.WARPED_ROOTS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.CRIMSON_NYLIUM).input(Items.MYCELIUM).or().input(Items.CRIMSON_FUNGUS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.WARPED_NYLIUM).input(Items.MYCELIUM).or().input(Items.WARPED_FUNGUS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.WARPED_WART_BLOCK).input(ItemTags.LEAVES).and().input(Items.WARPED_FUNGUS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.SHROOMLIGHT).input(Items.GLOWSTONE).or().namedInput(Items.CRIMSON_FUNGUS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.SHROOMLIGHT).input(Items.GLOWSTONE).or().namedInput(Items.WARPED_FUNGUS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.MAGMA_CREAM).input(Items.SLIME_BALL).and().input(Items.BLAZE_POWDER).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.MAGMA_BLOCK).input(Items.LAVA_BUCKET).and().input(Items.STONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.BLAZE_ROD).input(Items.STICK).and().namedInput(Items.LAVA_BUCKET).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.BLAZE_ROD).input(Items.STICK).and().namedInput(Items.BLAZE_POWDER).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.BLAZE_POWDER).input(Items.REDSTONE).or().namedInput(Items.LAVA_BUCKET).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.BLAZE_POWDER).input(Items.REDSTONE).or().namedInput(Items.NETHERRACK).buildFor(consumer, Minestuck.MOD_ID);
		//food
		CombinationRecipeBuilder.of(Items.APPLE).namedInput(Items.OAK_SAPLING).and().input(Items.WHEAT_SEEDS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.APPLE).namedInput(Items.OAK_LEAVES).and().input(Items.WHEAT_SEEDS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.GOLDEN_APPLE).input(Items.APPLE).and().namedInput(Items.GOLD_INGOT).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.GOLDEN_APPLE).input(Items.APPLE).and().namedInput(Items.GOLD_NUGGET).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.ENCHANTED_GOLDEN_APPLE).input(Items.APPLE).and().input(Items.GOLD_BLOCK).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.POTATO).input(Items.CARROT).and().input(Items.WHEAT_SEEDS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.CARROT).input(Items.POTATO).or().input(Items.WHEAT_SEEDS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.MELON).input(Items.PUMPKIN).or().input(MSBlocks.STRAWBERRY).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.PUMPKIN).input(Items.MELON).and().input(Items.CARROT).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.SUGAR).input(Items.COOKIE).and().input(Items.REDSTONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.PORKCHOP).input(Items.ROTTEN_FLESH).or().input(Items.CARROT).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.LEATHER).input(Items.ROTTEN_FLESH).or().input(Items.WATER_BUCKET).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.BEEF).input(Items.ROTTEN_FLESH).or().input(Items.WHEAT).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.CHICKEN).input(Items.ROTTEN_FLESH).or().input(Items.WHEAT_SEEDS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.POISONOUS_POTATO).input(Items.POTATO).or().input(Items.PUFFERFISH).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.FERMENTED_SPIDER_EYE).input(Items.SPIDER_EYE).or().input(Items.ROTTEN_FLESH).buildFor(consumer, Minestuck.MOD_ID);
		//end
		CombinationRecipeBuilder.of(Items.END_STONE).input(Items.STONE).and().input(Items.ENDER_PEARL).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.DRAGON_EGG).input(Items.ENDER_EYE).and().input(Items.EGG).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.ENDER_EYE).input(Items.ENDER_PEARL).and().input(Items.BLAZE_POWDER).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.CHORUS_FLOWER).input(ItemTags.SMALL_FLOWERS).and().input(Items.CHORUS_FRUIT).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.ELYTRA).namedInput(Items.FEATHER).or().input(Items.CHORUS_FRUIT).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.ELYTRA).namedInput(Items.LEATHER_CHESTPLATE).and().input(Items.PHANTOM_MEMBRANE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.CHORUS_FRUIT).input(Items.ENDER_PEARL).and().input(Items.POTATO).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.DRAGON_BREATH).input(Items.GLASS_BOTTLE).or().input(Items.DRAGON_HEAD).buildFor(consumer, Minestuck.MOD_ID);
		//Fighting
		CombinationRecipeBuilder.of(Items.DIAMOND_HORSE_ARMOR).input(Items.DIAMOND).and().input(Items.SADDLE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.GOLDEN_HORSE_ARMOR).input(Items.GOLD_INGOT).and().input(Items.SADDLE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.IRON_HORSE_ARMOR).input(Items.IRON_INGOT).and().input(Items.SADDLE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.SADDLE).input(Items.STRING).and().input(Items.LEATHER).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.SHEARS).input(Items.IRON_INGOT).and().input(Items.GRASS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.FIRE_CHARGE).input(Items.BLAZE_POWDER).or().input(Items.GUNPOWDER).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.BOW).input(Items.WOODEN_SWORD).or().input(Items.ARROW).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.FLOWER_POT).input(ItemTags.SMALL_FLOWERS).and().input(Items.BRICK).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.EXPERIENCE_BOTTLE).namedInput(Items.POTION).or().input(Items.ENCHANTED_BOOK).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.EXPERIENCE_BOTTLE).namedInput(Items.GLASS_BOTTLE).or().input(Items.ENCHANTED_BOOK).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.TRIDENT).namedInput(Items.PRISMARINE_SHARD).and().input(MSItems.FORK).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.TURTLE_HELMET).namedInput(Items.IRON_HELMET).or().input(Items.SEAGRASS).buildFor(consumer, Minestuck.MOD_ID);
		//watery stuff
		CombinationRecipeBuilder.of(Items.BUCKET).input(Items.BOWL).or().namedInput(Items.IRON_INGOT).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.WET_SPONGE).input(Items.WATER_BUCKET).and().namedInput(Items.SPONGE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.WET_SPONGE).input(Items.WATER_BUCKET).and().namedInput(Items.YELLOW_WOOL).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.ICE).input(Items.GLASS).and().input(Items.SNOW_BLOCK).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.PRISMARINE_CRYSTALS).input(Items.PRISMARINE_SHARD).or().namedInput(Items.DIAMOND).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.PRISMARINE_CRYSTALS).input(Items.PRISMARINE_SHARD).or().namedInput(Items.EMERALD).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.PRISMARINE).input(Items.PRISMARINE_SHARD).and().input(Items.COBBLESTONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.PRISMARINE_BRICKS).input(Items.PRISMARINE_SHARD).and().input(Items.STONE_BRICKS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.DARK_PRISMARINE).input(Items.PRISMARINE).and().input(Items.BLACK_DYE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.SEA_LANTERN).input(Items.PRISMARINE_SHARD).or().namedInput(Items.GLOWSTONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.SEA_LANTERN).input(Items.PRISMARINE).and().namedInput(Items.PRISMARINE_CRYSTALS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.PRISMARINE_SHARD).namedInput(Items.PRISMARINE).or().input(Items.FLINT).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.PRISMARINE_SHARD).namedInput(Items.PRISMARINE_BRICKS).or().input(Items.FLINT).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.PRISMARINE_SHARD).namedInput(Items.DARK_PRISMARINE).or().input(Items.FLINT).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.PRISMARINE_CRYSTALS).namedInput(Items.QUARTZ).or().input(Items.WATER_BUCKET).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.PRISMARINE_SHARD).namedInput(Items.QUARTZ).and().input(Items.WATER_BUCKET).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.HEART_OF_THE_SEA).namedInput(Items.NAUTILUS_SHELL).and().input(Items.DIAMOND).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.INK_SAC).input(ItemTags.FISHES).and().input(Items.BLACK_DYE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.PACKED_ICE).input(Items.ICE).or().input(Items.CHEST).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.BLUE_ICE).input(Items.BLUE_DYE).and().input(Items.ICE).buildFor(consumer, Minestuck.MOD_ID);
		//Mob loot
		CombinationRecipeBuilder.of(Items.SKELETON_SKULL).input(Items.WITHER_SKELETON_SKULL).and().namedInput(Items.BONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.SKELETON_SKULL).input(Items.WITHER_SKELETON_SKULL).and().namedInput(Items.BONE_MEAL).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.ZOMBIE_HEAD).input(Items.SKELETON_SKULL).or().input(Items.ROTTEN_FLESH).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.CREEPER_HEAD).input(Items.VINE).or().input(Items.GUNPOWDER).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.BONE).input(Items.STICK).or().input(Items.SKELETON_SKULL).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(Items.GHAST_TEAR).input(Items.PHANTOM_MEMBRANE).and().input(Items.GUNPOWDER).buildFor(consumer, Minestuck.MOD_ID);

		/////////////////	MINESTUCK	\\\\\\\\\\\\\\\\\
		
		CombinationRecipeBuilder.of(MSItems.PAPER_SWORD).input(Items.PAPER).or().namedInput(MSItems.KATANA).build(consumer);
		CombinationRecipeBuilder.of(MSItems.PAPER_SWORD).input(Items.PAPER).or().namedInput(Items.WOODEN_SWORD).build(consumer);
		CombinationRecipeBuilder.of(MSItems.PAPER_SWORD).input(Items.PAPER).or().namedInput(Items.STONE_SWORD).build(consumer);
		CombinationRecipeBuilder.of(MSItems.SWONGE).input(Items.WOODEN_SWORD).and().namedInput(Items.SPONGE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.PUMORD).input(MSItems.SWONGE).or().namedInput(Tags.Items.STONE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CACTACEAE_CUTLASS).input(Items.WOODEN_SWORD).and().namedInput(Items.CACTUS).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CACTACEAE_CUTLASS).input(Items.WOODEN_SWORD).and().namedInput(MSBlocks.BLOOMING_CACTUS).build(consumer);
		CombinationRecipeBuilder.of(MSItems.STEAK_SWORD).namedInput(Items.WOODEN_SWORD).or().input(Items.COOKED_BEEF).build(consumer);
		CombinationRecipeBuilder.of(MSItems.STEAK_SWORD).namedInput(Items.STONE_SWORD).or().input(Items.COOKED_BEEF).build(consumer);
		CombinationRecipeBuilder.of(MSItems.BEEF_SWORD).input(Items.WOODEN_SWORD).or().input(Items.BEEF).build(consumer);
		CombinationRecipeBuilder.of(MSItems.IRRADIATED_STEAK_SWORD).input(Items.WOODEN_SWORD).or().input(MSItems.IRRADIATED_STEAK).build(consumer);
		CombinationRecipeBuilder.of(MSItems.SORD).namedInput(MSItems.KATANA).and().input(MSItems.SBAHJ_POSTER).build(consumer);
		CombinationRecipeBuilder.of(MSItems.SORD).namedInput(Items.WOODEN_SWORD).and().input(MSItems.SBAHJ_POSTER).build(consumer);
		CombinationRecipeBuilder.of(MSItems.FROSTY_MACUAHUITL).namedInput(MSItems.MACUAHUITL).or().input(MSItems.ICE_SHARD).build(consumer);
		CombinationRecipeBuilder.of(MSItems.KATANA).namedInput(Items.STONE_SWORD).and().input(Items.ROTTEN_FLESH).build(consumer);
		CombinationRecipeBuilder.of(MSItems.KATANA).namedInput(Items.IRON_SWORD).and().input(Items.ROTTEN_FLESH).build(consumer);
		CombinationRecipeBuilder.of(MSItems.FIRE_POKER).input(Items.IRON_SWORD).and().input(Items.BLAZE_ROD).build(consumer);
		CombinationRecipeBuilder.of(MSItems.TOO_HOT_TO_HANDLE).input(Items.IRON_SWORD).or().input(Items.BLAZE_ROD).build(consumer);
		CombinationRecipeBuilder.of(MSItems.REGISWORD).namedInput(Items.IRON_SWORD).and().input(MSBlocks.CHESSBOARD).build(consumer);
		CombinationRecipeBuilder.of(MSItems.REGISWORD).namedInput(MSItems.KATANA).and().input(MSBlocks.CHESSBOARD).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CRUEL_FATE_CRUCIBLE).namedInput(Items.IRON_SWORD).or().input(MSItems.GRIMOIRE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CLAYMORE).input(Items.IRON_SWORD).and().input(Items.IRON_BLOCK).build(consumer);
		CombinationRecipeBuilder.of(MSItems.UNBREAKABLE_KATANA).input(MSItems.KATANA).and().input(Items.BEDROCK).build(consumer);
		CombinationRecipeBuilder.of(MSItems.ANGEL_APOCALYPSE).input(Items.IRON_SWORD).and().input(Items.FEATHER).build(consumer);
		CombinationRecipeBuilder.of(MSItems.COBALT_SABRE).input(MSItems.TOO_HOT_TO_HANDLE).or().input(Items.LAPIS_BLOCK).build(consumer);
		CombinationRecipeBuilder.of(MSItems.SCARLET_RIBBITAR).input(MSItems.CALEDSCRATCH).and().input(MSItems.FROG).build(consumer);
		CombinationRecipeBuilder.of(MSItems.SHATTER_BEACON).input(Items.BEACON).and().input(Items.DIAMOND_SWORD).build(consumer);
		CombinationRecipeBuilder.of(MSItems.SHATTER_BACON).input(Items.PORKCHOP).and().input(MSItems.SHATTER_BEACON).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CALEDFWLCH).input(MSItems.ROYAL_DERINGER).or().input(MSItems.CUEBALL).build(consumer);
		
		CombinationRecipeBuilder.of(MSItems.NIFE).input(MSItems.DAGGER).and().input(MSItems.SBAHJ_POSTER).build(consumer);
		CombinationRecipeBuilder.of(MSItems.LIGHT_OF_MY_KNIFE).input(MSItems.DAGGER).and().input(Items.LANTERN).build(consumer);
		CombinationRecipeBuilder.of(MSItems.STARSHARD_TRI_BLADE).input(MSItems.LIGHT_OF_MY_KNIFE).or().input(Items.NETHER_STAR).build(consumer);
		CombinationRecipeBuilder.of(MSItems.TOOTHRIPPER).input(MSItems.DAGGER).or().namedInput(Items.ROTTEN_FLESH).build(consumer);
		CombinationRecipeBuilder.of(MSItems.TOOTHRIPPER).input(MSItems.DAGGER).or().namedInput(Items.BONE).build(consumer);
		
		CombinationRecipeBuilder.of(MSItems.BATLEACKS).namedInput(MSItems.BATTLEAXE).and().input(MSItems.SBAHJ_POSTER).build(consumer);
		CombinationRecipeBuilder.of(MSItems.BATLEACKS).namedInput(Items.WOODEN_AXE).and().input(MSItems.SBAHJ_POSTER).build(consumer);
		CombinationRecipeBuilder.of(MSItems.COPSE_CRUSHER).input(Items.IRON_AXE).and().input(Items.PISTON).build(consumer);
		CombinationRecipeBuilder.of(MSItems.QUENCH_CRUSHER).input(MSItems.COPSE_CRUSHER).or().input(MSItems.DESERT_FRUIT).build(consumer);
		CombinationRecipeBuilder.of(MSItems.MELONSBANE).input(MSItems.QUENCH_CRUSHER).or().namedInput(Items.MELON_SLICE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.MELONSBANE).input(MSItems.QUENCH_CRUSHER).or().namedInput(Items.MELON).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CROP_CHOP).input(Items.GOLDEN_AXE).or().namedInput(Items.POTATO).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CROP_CHOP).input(Items.GOLDEN_AXE).or().namedInput(Items.CARROT).build(consumer);
		CombinationRecipeBuilder.of(MSItems.THE_LAST_STRAW).input(MSItems.CROP_CHOP).and().input(Items.HAY_BLOCK).build(consumer);
		CombinationRecipeBuilder.of(MSItems.BATTLEAXE).input(Items.IRON_AXE).and().input(Items.IRON_BLOCK).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CANDY_BATTLEAXE).input(MSItems.BATTLEAXE).or().input(MSItems.CANDY_CORN).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CHOCO_LOCO_WOODSPLITTER).input(MSItems.CANDY_BATTLEAXE).or().input(Items.COCOA_BEANS).build(consumer);
		CombinationRecipeBuilder.of(MSItems.STEEL_EDGE_CANDYCUTTER).input(MSItems.CHOCO_LOCO_WOODSPLITTER).and().input(Items.IRON_INGOT).build(consumer);
		CombinationRecipeBuilder.of(MSItems.BLACKSMITH_BANE).input(Items.WOODEN_AXE).and().input(Items.ANVIL).build(consumer);
		CombinationRecipeBuilder.of(MSItems.REGIAXE).input(MSItems.BLACKSMITH_BANE).and().input(MSBlocks.CHESSBOARD).build(consumer);
		CombinationRecipeBuilder.of(MSItems.GOTHY_AXE).input(MSItems.REGIAXE).or().input(Items.SPIDER_EYE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.SURPRISE_AXE).input(MSItems.BLACKSMITH_BANE).and().input(MSItems.SURPRISE_EMBRYO).build(consumer);
		CombinationRecipeBuilder.of(MSItems.SHOCK_AXE).input(MSItems.SURPRISE_AXE).and().input(MSItems.BATTERY).build(consumer);
		CombinationRecipeBuilder.of(MSItems.SCRAXE).input(Items.IRON_AXE).and().input(ItemTags.MUSIC_DISCS).build(consumer);
		CombinationRecipeBuilder.of(MSItems.LORENTZ_DISTRANSFORMATIONER).input(Items.IRON_AXE).and().namedInput(MSBlocks.TRANSPORTALIZER).build(consumer);
		CombinationRecipeBuilder.of(MSItems.LORENTZ_DISTRANSFORMATIONER).input(Items.IRON_AXE).and().namedInput(Items.CHORUS_FRUIT).build(consumer);
		CombinationRecipeBuilder.of(MSItems.PISTON_POWERED_POGO_AXEHAMMER).input(MSItems.COPSE_CRUSHER).and().input(MSItems.POGO_HAMMER).build(consumer);
		CombinationRecipeBuilder.of(MSItems.FISSION_FOCUSED_FAULT_FELLER).input(MSItems.PISTON_POWERED_POGO_AXEHAMMER).and().input(MSItems.ENERGY_CORE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.HEPHAESTUS_LUMBERJACK).input(Items.GOLDEN_AXE).and().input(Items.LAVA_BUCKET).build(consumer);
		CombinationRecipeBuilder.of(MSItems.RUBY_CROAK).input(MSItems.EMERALD_AXE).and().input(MSItems.FROG).build(consumer);
		CombinationRecipeBuilder.of(MSItems.BISECTOR).input(MSItems.BI_DYE).and().input(Items.IRON_AXE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.FINE_CHINA_AXE).input(Items.FLOWER_POT).or().input(Items.DIAMOND_AXE).build(consumer);
		
		CombinationRecipeBuilder.of(MSItems.SICKLE).input(Items.IRON_HOE).and().input(Items.WHEAT).build(consumer);
		CombinationRecipeBuilder.of(MSItems.OW_THE_EDGE).input(MSItems.HEMEOREAPER).or().input(MSItems.SBAHJ_POSTER).build(consumer);
		CombinationRecipeBuilder.of(MSItems.THORNY_SUBJECT).input(MSItems.SICKLE).and().input(Items.ROSE_BUSH).build(consumer);
		CombinationRecipeBuilder.of(MSItems.HOMES_SMELL_YA_LATER).input(MSItems.SICKLE).or().input(MSItems.THRESH_DVD).build(consumer);
		CombinationRecipeBuilder.of(MSItems.HEMEOREAPER).input(MSItems.HOMES_SMELL_YA_LATER).or().input(MSItems.BLOOD_BUCKET).build(consumer);
		CombinationRecipeBuilder.of(MSItems.FUDGESICKLE).input(MSItems.SICKLE).or().input(Items.COCOA_BEANS).build(consumer);
		CombinationRecipeBuilder.of(MSItems.REGISICKLE).input(MSItems.SICKLE).and().input(MSBlocks.CHESSBOARD).build(consumer);
		CombinationRecipeBuilder.of(MSItems.HERETICUS_AURURM).input(MSItems.REGISICKLE).and().input(Items.GOLD_INGOT).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CANDY_SICKLE).input(MSItems.SICKLE).or().namedInput(MSItems.CANDY_CORN).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CANDY_SICKLE).namedInput(MSItems.FUDGESICKLE).and().input(Items.SUGAR).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CLAW_SICKLE).namedInput(MSItems.CAT_CLAWS_DRAWN).and().input(MSItems.HEMEOREAPER).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CLAW_SICKLE).namedInput(MSItems.CAT_CLAWS_SHEATHED).and().input(MSItems.HEMEOREAPER).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CLAW_OF_NRUBYIGLITH).namedInput(MSItems.CLAW_SICKLE).and().input(MSItems.GRIMOIRE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.SCYTHE).input(MSItems.SICKLE).and().input(Items.WOODEN_SWORD).build(consumer);
		CombinationRecipeBuilder.of(MSItems.EIGHTBALL_SCYTHE).input(MSItems.SCYTHE).and().input(MSItems.EIGHTBALL).build(consumer);
		
		CombinationRecipeBuilder.of(MSItems.STALE_BAGUETTE).input(Items.BREAD).or().namedInput(Items.STICK).build(consumer);
		CombinationRecipeBuilder.of(MSItems.STALE_BAGUETTE).input(Items.BREAD).or().namedInput(MSItems.DEUCE_CLUB).build(consumer);
		CombinationRecipeBuilder.of(MSItems.GLUB_CLUB).input(MSItems.DEUCE_CLUB).or().namedInput(Items.COD).build(consumer);
		CombinationRecipeBuilder.of(MSItems.GLUB_CLUB).input(MSItems.DEUCE_CLUB).or().namedInput(Items.SALMON).build(consumer);
		CombinationRecipeBuilder.of(MSItems.GLUB_CLUB).input(MSItems.DEUCE_CLUB).or().namedInput(Items.TROPICAL_FISH).build(consumer);
		CombinationRecipeBuilder.of(MSItems.PRISMARINE_BASHER).input(MSItems.GLUB_CLUB).or().namedInput(Items.PRISMARINE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.PRISMARINE_BASHER).input(MSItems.GLUB_CLUB).or().namedInput(Items.PRISMARINE_SHARD).build(consumer);
		CombinationRecipeBuilder.of(MSItems.PRISMARINE_BASHER).input(MSItems.GLUB_CLUB).or().namedInput(Items.DARK_PRISMARINE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.PRISMARINE_BASHER).input(MSItems.GLUB_CLUB).or().namedInput(Items.PRISMARINE_BRICKS).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CLUB_ZERO).input(MSItems.PRISMARINE_BASHER).and().namedInput(Items.ICE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CLUB_ZERO).input(MSItems.PRISMARINE_BASHER).and().namedInput(Items.PACKED_ICE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CLUB_ZERO).input(MSItems.PRISMARINE_BASHER).and().namedInput(Items.BLUE_ICE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CLUB_ZERO).input(MSItems.PRISMARINE_BASHER).and().namedInput(MSItems.ICE_SHARD).build(consumer);
		CombinationRecipeBuilder.of(MSItems.POGO_CLUB).input(MSItems.DEUCE_CLUB).and().input(Items.SLIME_BALL).build(consumer);
		CombinationRecipeBuilder.of(MSItems.BARBER_BASHER).input(MSItems.DEUCE_CLUB).and().input(MSItems.BARBASOL).build(consumer);
		CombinationRecipeBuilder.of(MSItems.NIGHT_CLUB).input(MSItems.DEUCE_CLUB).and().input(MSItems.CREW_POSTER).build(consumer);
		CombinationRecipeBuilder.of(MSItems.NIGHTSTICK).input(MSItems.NIGHT_CLUB).and().input(MSItems.SCALEMATE_APPLESCAB).build(consumer);
		CombinationRecipeBuilder.of(MSItems.RED_EYES).input(MSItems.NIGHT_CLUB).and().namedInput(Items.SPIDER_EYE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.METAL_BAT).input(MSItems.DEUCE_CLUB).and().input(Items.IRON_INGOT).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CLOWN_CLUB).input(MSItems.METAL_BAT).or().input(MSItems.HORN).build(consumer);
		CombinationRecipeBuilder.of(MSItems.SPIKED_CLUB).input(MSItems.METAL_BAT).or().input(ItemTags.LOGS).build(consumer);
		CombinationRecipeBuilder.of(MSItems.M_ACE).input(MSItems.MACE).and().input(MSItems.NONBINARY_CODE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.DESOLATOR_MACE).input(MSItems.MACE).or().input(Items.FLINT).build(consumer);
		CombinationRecipeBuilder.of(MSItems.BLAZING_GLORY).input(MSItems.MACE).and().namedInput(Items.BLAZE_POWDER).build(consumer);
		
		CombinationRecipeBuilder.of(MSItems.SLEDGE_HAMMER).input(MSItems.CLAW_HAMMER).and().namedInput(Items.BRICKS).build(consumer);
		CombinationRecipeBuilder.of(MSItems.SLEDGE_HAMMER).input(MSItems.CLAW_HAMMER).and().namedInput(Items.COBBLESTONE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.MAILBOX).input(MSItems.SLEDGE_HAMMER).and().namedInput(Items.CHEST).build(consumer);
		CombinationRecipeBuilder.of(MSItems.BLACKSMITH_HAMMER).input(MSItems.SLEDGE_HAMMER).and().namedInput(Items.ANVIL).build(consumer);
		CombinationRecipeBuilder.of(MSItems.BLACKSMITH_HAMMER).input(MSItems.SLEDGE_HAMMER).and().namedInput(Items.IRON_BLOCK).build(consumer);
		CombinationRecipeBuilder.of(MSItems.POGO_HAMMER).input(MSItems.SLEDGE_HAMMER).and().input(Items.SLIME_BALL).build(consumer);
		CombinationRecipeBuilder.of(MSItems.WRINKLEFUCKER).input(MSItems.POGO_HAMMER).or().input(MSItems.CLOTHES_IRON).build(consumer);
		CombinationRecipeBuilder.of(MSItems.DEMOCRATIC_DEMOLITIONER).input(MSItems.MAILBOX).or().input(MSItems.FOOD_CAN).build(consumer);
		CombinationRecipeBuilder.of(MSItems.REGI_HAMMER).input(MSItems.CLAW_HAMMER).and().input(MSBlocks.CHESSBOARD).build(consumer);
		CombinationRecipeBuilder.of(MSItems.FEAR_NO_ANVIL).input(MSItems.BLACKSMITH_HAMMER).or().input(Items.CLOCK).build(consumer);
		CombinationRecipeBuilder.of(MSItems.TELESCOPIC_SASSACRUSHER).input(MSItems.SLEDGE_HAMMER).and().input(MSItems.SASSACRE_TEXT).build(consumer);
		CombinationRecipeBuilder.of(MSItems.MELT_MASHER).input(MSItems.FEAR_NO_ANVIL).or().input(Items.LAVA_BUCKET).build(consumer);
		CombinationRecipeBuilder.of(MSItems.ESTROGEN_EMPOWERED_EVERYTHING_ERADICATOR).input(MSItems.FISSION_FOCUSED_FAULT_FELLER).or().input(MSItems.GAMEGRL_MAGAZINE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.EEEEEEEEEEEE).input(MSItems.ESTROGEN_EMPOWERED_EVERYTHING_ERADICATOR).and().input(MSItems.SBAHJ_POSTER).build(consumer);
		CombinationRecipeBuilder.of(MSItems.POPAMATIC_VRILLYHOO).input(MSItems.ZILLYHOO_HAMMER).and().input(MSItems.FLUORITE_OCTET).build(consumer);
		CombinationRecipeBuilder.of(MSItems.SCARLET_ZILLYHOO).input(MSItems.ZILLYHOO_HAMMER).and().input(MSItems.FROG).build(consumer);
		CombinationRecipeBuilder.of(MSItems.MWRTHWL).input(MSItems.REGI_HAMMER).or().input(MSItems.CUEBALL).build(consumer);
		
		CombinationRecipeBuilder.of(MSItems.VAUDEVILLE_HOOK).namedInput(MSItems.CANE).and().input(Items.FISHING_ROD).build(consumer);
		CombinationRecipeBuilder.of(MSItems.VAUDEVILLE_HOOK).namedInput(Items.STICK).or().input(MSItems.WISEGUY).build(consumer);
		CombinationRecipeBuilder.of(MSItems.BEAR_POKING_STICK).namedInput(MSItems.CANE).or().input(Items.LEATHER).build(consumer);
		CombinationRecipeBuilder.of(MSItems.BEAR_POKING_STICK).namedInput(Items.STICK).or().input(Items.LEATHER).build(consumer);
		CombinationRecipeBuilder.of(MSItems.UMBRELLA).input(Items.STICK).and().namedInput(Items.BLACK_WOOL).build(consumer);
		CombinationRecipeBuilder.of(MSItems.UMBRELLA).input(Items.STICK).and().namedInput(Items.SHIELD).build(consumer);
		CombinationRecipeBuilder.of(MSItems.UMBRELLA).namedInput(MSItems.CANE).and().namedInput(Items.BLACK_WOOL).build(consumer);
		CombinationRecipeBuilder.of(MSItems.UMBRELLA).namedInput(MSItems.CANE).and().namedInput(Items.SHIELD).build(consumer);
		CombinationRecipeBuilder.of(MSItems.UPPER_CRUST_CRUST_CANE).input(MSItems.PRIM_AND_PROPER_WALKING_POLE).or().namedInput(MSItems.STALE_BAGUETTE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.UPPER_CRUST_CRUST_CANE).input(MSItems.PRIM_AND_PROPER_WALKING_POLE).or().namedInput(Items.BREAD).build(consumer);
		CombinationRecipeBuilder.of(MSItems.ZEPHYR_CANE).namedInput(MSItems.IRON_CANE).or().namedInput(Items.FEATHER).build(consumer);
		CombinationRecipeBuilder.of(MSItems.ZEPHYR_CANE).namedInput(MSItems.IRON_CANE).or().namedInput(Items.PHANTOM_MEMBRANE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.SPEAR_CANE).input(MSItems.CANE).or().namedInput(Items.IRON_SWORD).build(consumer);
		CombinationRecipeBuilder.of(MSItems.SPEAR_CANE).input(MSItems.CANE).or().namedInput(MSItems.KATANA).build(consumer);
		CombinationRecipeBuilder.of(MSItems.SPEAR_CANE).input(MSItems.IRON_CANE).or().namedInput(Items.STONE_SWORD).build(consumer);
		CombinationRecipeBuilder.of(MSItems.SPEAR_CANE).namedInput(MSItems.IRON_CANE).or().namedInput(Items.IRON_SWORD).build(consumer);
		CombinationRecipeBuilder.of(MSItems.SPEAR_CANE).namedInput(MSItems.IRON_CANE).or().namedInput(MSItems.KATANA).build(consumer);
		CombinationRecipeBuilder.of(MSItems.PARADISES_PORTABELLO).input(Items.STICK).or().namedInput(Items.RED_MUSHROOM).build(consumer);
		CombinationRecipeBuilder.of(MSItems.PARADISES_PORTABELLO).input(Items.STICK).or().namedInput(Items.BROWN_MUSHROOM).build(consumer);
		CombinationRecipeBuilder.of(MSItems.REGI_CANE).namedInput(MSItems.CANE).and().input(MSBlocks.CHESSBOARD).build(consumer);
		CombinationRecipeBuilder.of(MSItems.REGI_CANE).namedInput(MSItems.IRON_CANE).and().input(MSBlocks.CHESSBOARD).build(consumer);
		CombinationRecipeBuilder.of(MSItems.URANIUM_POWERED_STICK).input(Items.STICK).or().input(MSItems.RAW_URANIUM).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CANDY_CANE).input(MSItems.VAUDEVILLE_HOOK).and().namedInput(MSItems.CANDY_CORN).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CANDY_CANE).input(MSItems.VAUDEVILLE_HOOK).and().namedInput(MSItems.DIAMOND_MINT).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CANDY_CANE).input(MSItems.VAUDEVILLE_HOOK).or().namedInput(MSItems.CANDY_SICKLE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.PRIM_AND_PROPER_WALKING_POLE).namedInput(MSItems.CANE).and().input(Items.GOLD_INGOT).build(consumer);
		CombinationRecipeBuilder.of(MSItems.PRIM_AND_PROPER_WALKING_POLE).namedInput(MSItems.VAUDEVILLE_HOOK).and().input(Items.GOLD_INGOT).build(consumer);
		CombinationRecipeBuilder.of(MSItems.LESS_PROPER_WALKING_STICK).input(MSItems.PRIM_AND_PROPER_WALKING_POLE).and().namedInput(MSItems.KATANA).build(consumer);
		CombinationRecipeBuilder.of(MSItems.LESS_PROPER_WALKING_STICK).input(MSItems.PRIM_AND_PROPER_WALKING_POLE).and().namedInput(MSItems.UNBREAKABLE_KATANA).build(consumer);
		CombinationRecipeBuilder.of(MSItems.LESS_PROPER_WALKING_STICK).input(MSItems.PRIM_AND_PROPER_WALKING_POLE).and().namedInput(MSItems.IRON_CANE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.ROCKEFELLERS_WALKING_BLADECANE).input(MSItems.LESS_PROPER_WALKING_STICK).and().input(Items.OBSIDIAN).build(consumer);
		CombinationRecipeBuilder.of(MSItems.DRAGON_CANE).input(MSItems.ROCKEFELLERS_WALKING_BLADECANE).or().namedInput(Items.DRAGON_BREATH).build(consumer);
		CombinationRecipeBuilder.of(MSItems.DRAGON_CANE).input(MSItems.ROCKEFELLERS_WALKING_BLADECANE).or().namedInput(Items.DRAGON_HEAD).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CHANCEWYRMS_EXTRA_FORTUNATE_STABBING_IMPLEMENT).input(MSItems.DRAGON_CANE).and().namedInput(MSItems.FLUORITE_OCTET).build(consumer);
		
		CombinationRecipeBuilder.of(MSItems.WOODEN_SPOON).input(Items.WOODEN_SHOVEL).and().namedInput(Items.BOWL).build(consumer);
		CombinationRecipeBuilder.of(MSItems.WOODEN_SPOON).input(Items.WOODEN_SHOVEL).and().namedInput(Items.MUSHROOM_STEW).build(consumer);
		CombinationRecipeBuilder.of(MSItems.WOODEN_SPOON).input(Items.WOODEN_SHOVEL).and().namedInput(Items.RABBIT_STEW).build(consumer);
		CombinationRecipeBuilder.of(MSItems.WOODEN_SPOON).input(Items.WOODEN_SHOVEL).and().namedInput(Items.BEETROOT_SOUP).build(consumer);
		CombinationRecipeBuilder.of(MSItems.SILVER_SPOON).input(Items.IRON_SHOVEL).and().namedInput(Items.BOWL).build(consumer);
		CombinationRecipeBuilder.of(MSItems.SILVER_SPOON).input(Items.IRON_SHOVEL).and().namedInput(Items.MUSHROOM_STEW).build(consumer);
		CombinationRecipeBuilder.of(MSItems.SILVER_SPOON).input(Items.IRON_SHOVEL).and().namedInput(Items.RABBIT_STEW).build(consumer);
		CombinationRecipeBuilder.of(MSItems.SILVER_SPOON).input(Items.IRON_SHOVEL).and().namedInput(Items.BEETROOT_SOUP).build(consumer);
		CombinationRecipeBuilder.of(MSItems.SILVER_SPOON).namedInput(MSItems.WOODEN_SPOON).and().input(Items.IRON_INGOT).build(consumer);
		CombinationRecipeBuilder.of(MSItems.MELONBALLER).input(MSItems.SILVER_SPOON).or().namedInput(Items.MELON_SLICE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.SIGHTSEEKER).input(MSItems.SILVER_SPOON).or().namedInput(Items.SPIDER_EYE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.TERRAIN_FLATENATOR).input(Items.IRON_SHOVEL).or().namedInput(Items.PISTON).build(consumer);
		CombinationRecipeBuilder.of(MSItems.NOSFERATU_SPOON).input(MSItems.SILVER_SPOON).or().input(MSItems.GRIMOIRE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CROCKER_SPOON).input(MSItems.SILVER_SPOON).and().input(Items.CAKE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CANDY_FORK).input(MSItems.FORK).or().input(MSItems.CANDY_CORN).build(consumer);
		CombinationRecipeBuilder.of(MSItems.TUNING_FORK).input(MSItems.FORK).and().input(Items.NOTE_BLOCK).build(consumer);
		CombinationRecipeBuilder.of(MSItems.ELECTRIC_FORK).input(MSItems.FORK).or().input(MSItems.BATTERY).build(consumer);
		CombinationRecipeBuilder.of(MSItems.EATING_FORK_GEM).input(MSItems.NOSFERATU_SPOON).and().input(Items.PRISMARINE_CRYSTALS).build(consumer);
		CombinationRecipeBuilder.of(MSItems.DEVIL_FORK).input(MSItems.NOSFERATU_SPOON).and().input(Items.BLAZE_POWDER).build(consumer);
		CombinationRecipeBuilder.of(MSItems.SKAIA_FORK).input(MSItems.FORK).and().input(MSBlocks.CHESSBOARD).build(consumer);
		CombinationRecipeBuilder.of(MSItems.SPORK).input(MSItems.FORK).or().input(MSItems.WOODEN_SPOON).build(consumer);
		CombinationRecipeBuilder.of(MSItems.GOLDEN_SPORK).input(MSItems.SPORK).or().input(Items.GOLD_INGOT).build(consumer);
		CombinationRecipeBuilder.of(MSItems.BIDENT).input(MSItems.BI_DYE).and().input(Items.TRIDENT).build(consumer);
		CombinationRecipeBuilder.of(MSItems.EDISONS_FURY).input(MSItems.ELECTRIC_FORK).or().input(Items.REDSTONE_LAMP).build(consumer);
		
		CombinationRecipeBuilder.of(MSItems.CAT_CLAWS_DRAWN).input(Items.IRON_BARS).and().input(Items.LEATHER).build(consumer);
		CombinationRecipeBuilder.of(MSItems.SKELETONIZER_DRAWN).input(MSItems.CAT_CLAWS_DRAWN).or().input(Items.BONE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.SKELETON_DISPLACER_DRAWN).input(MSItems.SKELETONIZER_DRAWN).and().input(Items.ENDER_EYE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.TEARS_OF_THE_ENDERLICH_DRAWN).input(MSItems.SKELETON_DISPLACER_DRAWN).and().input(Items.GHAST_TEAR).build(consumer);
		CombinationRecipeBuilder.of(MSItems.ACTION_CLAWS_DRAWN).input(MSItems.CAT_CLAWS_DRAWN).and().input(MSItems.FLUORITE_OCTET).build(consumer);
		CombinationRecipeBuilder.of(MSItems.LIPSTICK).input(MSItems.LIP_BALM).and().input(Items.GREEN_DYE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.THISTLEBLOWER).input(MSItems.LIPSTICK_CHAINSAW).or().input(Items.ROSE_BUSH).build(consumer);
		CombinationRecipeBuilder.of(MSItems.EMERALD_IMMOLATOR).input(MSItems.LIPSTICK_CHAINSAW).and().input(MSItems.EMERALD_SWORD).build(consumer);
		CombinationRecipeBuilder.of(MSItems.FROSTTOOTH).input(MSItems.EMERALD_IMMOLATOR).or().input(MSItems.CLUB_ZERO).build(consumer);
		CombinationRecipeBuilder.of(MSItems.OBSIDIATOR).input(MSItems.FROSTTOOTH).and().input(Items.OBSIDIAN).build(consumer);
		CombinationRecipeBuilder.of(MSItems.JOUSTING_LANCE).input(Items.IRON_SWORD).and().input(Tags.Items.RODS_WOODEN).build(consumer);
        CombinationRecipeBuilder.of(MSItems.CIGARETTE_LANCE).input(MSItems.JOUSTING_LANCE).or().input(MSItems.EIGHTBALL).build(consumer);
		CombinationRecipeBuilder.of(MSItems.LUCERNE_HAMMER).input(MSItems.SPEAR_CANE).and().namedInput(MSItems.CLAW_HAMMER).build(consumer);
		CombinationRecipeBuilder.of(MSItems.LUCERNE_HAMMER_OF_UNDYING).input(MSItems.LUCERNE_HAMMER).or().namedInput(Items.TOTEM_OF_UNDYING).build(consumer);
		CombinationRecipeBuilder.of(MSItems.TYPHONIC_TRIVIALIZER).input(MSItems.FAN).or().namedInput(MSBlocks.SMOOTH_SHADE_STONE).build(consumer);
		
		CombinationRecipeBuilder.of(MSItems.NEEDLE_WAND).input(MSItems.KNITTING_NEEDLE).and().namedInput(MSBlocks.MINI_WIZARD_STATUE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.ARTIFUCKER).input(MSItems.POINTER_WAND).and().namedInput(MSItems.SBAHJ_POSTER).build(consumer);
		CombinationRecipeBuilder.of(MSItems.POINTER_WAND).input(MSItems.NEEDLE_WAND).and().namedInput(MSBlocks.COMPUTER).build(consumer);
		CombinationRecipeBuilder.of(MSItems.POOL_CUE_WAND).input(MSItems.NEEDLE_WAND).and().namedInput(MSItems.CUESTICK).build(consumer);
		CombinationRecipeBuilder.of(MSItems.THORN_OF_OGLOGOTH).input(MSItems.NEEDLE_WAND).and().namedInput(MSItems.GRIMOIRE).build(consumer);
		
		CombinationRecipeBuilder.of(MSItems.SBAHJARANG).input(MSItems.SHURIKEN).and().namedInput(MSItems.SBAHJ_POSTER).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CLUBS_SUITARANG).input(MSItems.SHURIKEN).and().namedInput(MSItems.ACE_OF_CLUBS).build(consumer);
		CombinationRecipeBuilder.of(MSItems.DIAMONDS_SUITARANG).input(MSItems.SHURIKEN).and().namedInput(MSItems.ACE_OF_DIAMONDS).build(consumer);
		CombinationRecipeBuilder.of(MSItems.HEARTS_SUITARANG).input(MSItems.SHURIKEN).and().namedInput(MSItems.ACE_OF_HEARTS).build(consumer);
		CombinationRecipeBuilder.of(MSItems.SPADES_SUITARANG).input(MSItems.SHURIKEN).and().namedInput(MSItems.ACE_OF_SPADES).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CHAKRAM).input(MSItems.SHURIKEN).or().namedInput(Items.ENDER_EYE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.UMBRAL_INFILTRATOR).input(MSItems.CHAKRAM).or().namedInput(Items.OBSIDIAN).build(consumer);
		CombinationRecipeBuilder.of(MSItems.SORCERERS_PINBALL).input(Items.IRON_BLOCK).or().namedInput(MSBlocks.MINI_WIZARD_STATUE).build(consumer);
		
        CombinationRecipeBuilder.of(MSBlocks.LAPTOP).input(MSBlocks.COMPUTER).and().namedInput(MSItems.BATTERY).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.LAPTOP).input(MSBlocks.COMPUTER).and().namedInput(Items.BOOK).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.CROCKERTOP).input(MSBlocks.LAPTOP).and().namedInput(MSItems.CROCKER_SPOON).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.CROCKERTOP).input(MSBlocks.LAPTOP).and().namedInput(MSItems.CROCKER_FORK).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.HUBTOP).input(MSBlocks.LAPTOP).and().input(MSBlocks.URANIUM_BLOCK).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.LUNCHTOP).input(MSBlocks.LAPTOP).and().input(Items.APPLE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.OLD_COMPUTER).input(MSBlocks.COMPUTER).and().input(Items.CLOCK).build(consumer);
		
		CombinationRecipeBuilder.of(MSBlocks.GRIST_WIDGET).namedInput(MSItems.CROCKER_SPOON).or().input(MSItems.CAPTCHA_CARD).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.GRIST_WIDGET).namedInput(MSItems.CROCKER_FORK).or().input(MSItems.CAPTCHA_CARD).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.TRANSPORTALIZER).input(Items.ENDER_PEARL).and().input(Items.IRON_BLOCK).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CAPTCHAROID_CAMERA).input(MSItems.CAPTCHA_CARD).and().namedInput(MSBlocks.COMPUTER).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CAPTCHAROID_CAMERA).input(MSItems.CAPTCHA_CARD).or().namedInput(Items.ENDER_EYE).build(consumer);
		
		CombinationRecipeBuilder.of(MSItems.QUEUESTACK_MODUS_CARD).input(MSItems.STACK_MODUS_CARD).and().input(MSItems.QUEUE_MODUS_CARD).build(consumer);
		CombinationRecipeBuilder.of(MSItems.TREE_MODUS_CARD).input(MSTags.Items.MODUS_CARD).or().namedInput(Items.STICK).build(consumer);
		CombinationRecipeBuilder.of(MSItems.TREE_MODUS_CARD).input(MSTags.Items.MODUS_CARD).or().namedInput(ItemTags.SAPLINGS).build(consumer);
		CombinationRecipeBuilder.of(MSItems.TREE_MODUS_CARD).input(MSTags.Items.MODUS_CARD).or().namedInput(ItemTags.LEAVES).build(consumer);
		CombinationRecipeBuilder.of(MSItems.HASHMAP_MODUS_CARD).input(MSTags.Items.MODUS_CARD).and().input(MSBlocks.COMPUTER).build(consumer);
		CombinationRecipeBuilder.of(MSItems.SET_MODUS_CARD).input(MSTags.Items.MODUS_CARD).and().input(Items.ITEM_FRAME).build(consumer);
		
		CombinationRecipeBuilder.of(MSItems.IRON_BOAT).input(ItemTags.BOATS).and().namedInput(Items.MINECART).build(consumer);
		CombinationRecipeBuilder.of(MSItems.IRON_BOAT).input(ItemTags.BOATS).and().namedInput(Items.IRON_INGOT).build(consumer);
		CombinationRecipeBuilder.of(MSItems.IRON_BOAT).input(ItemTags.BOATS).and().namedInput(Items.IRON_BLOCK).build(consumer);
		CombinationRecipeBuilder.of(MSItems.GOLD_BOAT).input(ItemTags.BOATS).and().namedInput(Items.GOLD_INGOT).build(consumer);
		CombinationRecipeBuilder.of(MSItems.GOLD_BOAT).input(ItemTags.BOATS).and().namedInput(Items.GOLD_BLOCK).build(consumer);
		
		CombinationRecipeBuilder.of(MSItems.CARDBOARD_TUBE).input(Items.STICK).or().input(Items.PAPER).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.GOLD_SEEDS).input(Items.WHEAT_SEEDS).and().namedInput(Items.GOLD_NUGGET).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.GOLD_SEEDS).input(Items.WHEAT_SEEDS).and().namedInput(Items.GOLD_INGOT).build(consumer);
		CombinationRecipeBuilder.of(MSItems.OBSIDIAN_BUCKET).input(Items.WATER_BUCKET).or().input(Items.LAVA_BUCKET).namedSource("buckets").build(consumer);
		CombinationRecipeBuilder.of(MSItems.OBSIDIAN_BUCKET).input(Items.BUCKET).and().namedInput(Items.OBSIDIAN).build(consumer);
		CombinationRecipeBuilder.of(MSItems.STONE_SLAB).input(Items.STONE).or().namedInput(MSItems.CARVING_TOOL).build(consumer);
		CombinationRecipeBuilder.of(MSItems.STONE_SLAB).input(Items.STONE).or().namedInput(Items.WRITABLE_BOOK).build(consumer);
		CombinationRecipeBuilder.of(MSItems.STONE_SLAB).input(Items.STONE).or().namedInput(MSItems.CAPTCHA_CARD).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.GLOWYSTONE_DUST).input(Items.REDSTONE).or().input(Items.GLOWSTONE_DUST).build(consumer);
		CombinationRecipeBuilder.of(MSItems.COCOA_WART).input(Items.COCOA_BEANS).and().input(Items.NETHER_WART).build(consumer);
		
		CombinationRecipeBuilder.of(MSItems.CASSETTE_13).input(Items.MUSIC_DISC_13).or().namedInput(MSBlocks.CASSETTE_PLAYER).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CASSETTE_CAT).input(Items.MUSIC_DISC_CAT).or().namedInput(MSBlocks.CASSETTE_PLAYER).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CASSETTE_BLOCKS).input(Items.MUSIC_DISC_BLOCKS).or().namedInput(MSBlocks.CASSETTE_PLAYER).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CASSETTE_CHIRP).input(Items.MUSIC_DISC_CHIRP).or().namedInput(MSBlocks.CASSETTE_PLAYER).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CASSETTE_FAR).input(Items.MUSIC_DISC_FAR).or().namedInput(MSBlocks.CASSETTE_PLAYER).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CASSETTE_MALL).input(Items.MUSIC_DISC_MALL).or().namedInput(MSBlocks.CASSETTE_PLAYER).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CASSETTE_MELLOHI).input(Items.MUSIC_DISC_MELLOHI).or().namedInput(MSBlocks.CASSETTE_PLAYER).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CASSETTE_EMISSARY).input(MSItems.MUSIC_DISC_EMISSARY_OF_DANCE).or().namedInput(MSBlocks.CASSETTE_PLAYER).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CASSETTE_DANCE_STAB).input(MSItems.MUSIC_DISC_DANCE_STAB_DANCE).or().namedInput(MSBlocks.CASSETTE_PLAYER).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CASSETTE_RETRO_BATTLE).input(MSItems.MUSIC_DISC_RETRO_BATTLE).or().namedInput(MSBlocks.CASSETTE_PLAYER).build(consumer);
		
		CombinationRecipeBuilder.of(MSBlocks.BLACK_CHESS_DIRT).input(Items.DIRT).and().input(Items.BLACK_DYE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.DARK_GRAY_CHESS_DIRT).input(Items.DIRT).and().input(Items.GRAY_DYE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.LIGHT_GRAY_CHESS_DIRT).input(Items.DIRT).and().input(Items.LIGHT_GRAY_DYE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.WHITE_CHESS_DIRT).input(Items.DIRT).and().input(Items.WHITE_DYE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.BLACK_CASTLE_BRICKS).input(Items.STONE_BRICKS).or().input(MSBlocks.BLACK_CHESS_DIRT).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.DARK_GRAY_CASTLE_BRICKS).input(Items.STONE_BRICKS).or().input(MSBlocks.DARK_GRAY_CHESS_DIRT).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.LIGHT_GRAY_CASTLE_BRICKS).input(Items.STONE_BRICKS).or().input(MSBlocks.LIGHT_GRAY_CHESS_DIRT).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.WHITE_CASTLE_BRICKS).input(Items.STONE_BRICKS).or().input(MSBlocks.WHITE_CHESS_DIRT).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.BLACK_CASTLE_BRICK_SMOOTH).input(Items.STONE).or().input(MSBlocks.BLACK_CASTLE_BRICKS).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.DARK_GRAY_CASTLE_BRICK_SMOOTH).input(Items.STONE).or().input(MSBlocks.DARK_GRAY_CASTLE_BRICKS).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.LIGHT_GRAY_CASTLE_BRICK_SMOOTH).input(Items.STONE).or().input(MSBlocks.LIGHT_GRAY_CASTLE_BRICKS).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.WHITE_CASTLE_BRICK_SMOOTH).input(Items.STONE).or().input(MSBlocks.WHITE_CASTLE_BRICKS).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.BLACK_CASTLE_BRICK_TRIM).input(Items.YELLOW_WOOL).and().input(MSBlocks.BLACK_CASTLE_BRICKS).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.DARK_GRAY_CASTLE_BRICK_TRIM).input(Items.YELLOW_WOOL).and().input(MSBlocks.DARK_GRAY_CASTLE_BRICKS).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.LIGHT_GRAY_CASTLE_BRICK_TRIM).input(Items.YELLOW_WOOL).and().input(MSBlocks.LIGHT_GRAY_CASTLE_BRICKS).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.WHITE_CASTLE_BRICK_TRIM).input(Items.YELLOW_WOOL).and().input(MSBlocks.WHITE_CASTLE_BRICKS).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.CHECKERED_STAINED_GLASS).input(Items.BLUE_STAINED_GLASS).and().input(MSBlocks.CHESSBOARD).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.BLACK_PAWN_STAINED_GLASS).input(Items.BLACK_STAINED_GLASS).and().input(MSBlocks.CHESSBOARD).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.WHITE_PAWN_STAINED_GLASS).input(Items.WHITE_STAINED_GLASS).and().input(MSBlocks.CHESSBOARD).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.BLACK_CROWN_STAINED_GLASS).input(MSBlocks.BLACK_PAWN_STAINED_GLASS).and().input(MSItems.PRIM_AND_PROPER_WALKING_POLE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.WHITE_CROWN_STAINED_GLASS).input(MSBlocks.WHITE_PAWN_STAINED_GLASS).and().input(MSItems.PRIM_AND_PROPER_WALKING_POLE).build(consumer);
		
		CombinationRecipeBuilder.of(MSBlocks.BLUE_DIRT).input(Items.DIRT).or().input(Items.BLUE_DYE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.THOUGHT_DIRT).input(Items.DIRT).or().input(Items.LIME_DYE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.COARSE_END_STONE).input(Items.COARSE_DIRT).or().input(Items.END_STONE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.END_GRASS).namedInput(Items.GRASS_BLOCK).or().input(Items.END_STONE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.END_GRASS).namedInput(Items.MYCELIUM).or().input(Items.END_STONE).build(consumer);
		CombinationRecipeBuilder.of(Items.GRASS_BLOCK).namedInput(MSBlocks.END_GRASS).and().input(Items.DIRT).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(MSBlocks.VINE_LOG).input(Items.OAK_LOG).and().input(Items.VINE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.FLOWERY_VINE_LOG).input(MSBlocks.VINE_LOG).or().input(ItemTags.SMALL_FLOWERS).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.GLOWING_MUSHROOM).input(Items.BROWN_MUSHROOM).or().input(Items.GLOWSTONE_DUST).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.GLOWING_LOG).input(ItemTags.LOGS).or().input(MSBlocks.GLOWING_MUSHROOM).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.GLOWING_PLANKS).input(ItemTags.PLANKS).or().input(MSBlocks.GLOWING_MUSHROOM).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.GLOWY_GOOP).input(Items.SLIME_BLOCK).or().input(MSBlocks.GLOWING_MUSHROOM).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.COAGULATED_BLOOD).input(Items.SLIME_BLOCK).and().input(MSItems.BLOOD_BUCKET).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.PETRIFIED_POPPY).namedInput(Items.STONE).or().input(Items.POPPY).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.PETRIFIED_POPPY).namedInput(Items.GRAVEL).or().input(Items.POPPY).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.PETRIFIED_POPPY).namedInput(Items.COBBLESTONE).or().input(Items.POPPY).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.PETRIFIED_LOG).namedInput(Items.STONE).or().input(ItemTags.LOGS).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.PETRIFIED_LOG).namedInput(Items.GRAVEL).or().input(ItemTags.LOGS).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.PETRIFIED_LOG).namedInput(Items.COBBLESTONE).or().input(ItemTags.LOGS).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.BLOOMING_CACTUS).input(Items.CACTUS).and().input(ItemTags.SMALL_FLOWERS).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.GLOWFLOWER).input(Items.GLOWSTONE_DUST).and().input(ItemTags.SMALL_FLOWERS).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.SUGAR_CUBE).input(Items.COOKIE).and().input(Items.REDSTONE_BLOCK).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.WOODEN_CACTUS).input(Items.CACTUS).or().namedInput(Items.WOODEN_SWORD).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.WOODEN_CACTUS).input(Items.CACTUS).or().namedInput(ItemTags.PLANKS).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.WOODEN_CACTUS).input(Items.CACTUS).or().namedInput(ItemTags.LOGS).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.RAINBOW_SAPLING).input(MSBlocks.RAINBOW_LEAVES).or().input(MSBlocks.RAINBOW_LOG).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.END_SAPLING).input(MSBlocks.END_LEAVES).or().input(MSBlocks.END_LOG).build(consumer);
		
		CombinationRecipeBuilder.of(MSBlocks.SPIKES).input(Items.CACTUS).and().input(Items.IRON_SWORD).build(consumer);
		
		CombinationRecipeBuilder.of(MSBlocks.COARSE_STONE).input(Items.STONE).and().input(Items.GRAVEL).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.CHISELED_COARSE_STONE).input(Items.CHISELED_STONE_BRICKS).and().namedInput(Items.GRAVEL).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.CHISELED_COARSE_STONE).input(Items.CHISELED_STONE_BRICKS).and().namedInput(MSBlocks.COARSE_STONE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.SHADE_STONE).input(Items.STONE).or().namedInput(MSBlocks.BLUE_DIRT).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.SHADE_STONE).input(Items.STONE).or().namedInput(Items.BLUE_DYE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.SHADE_BRICKS).input(Items.STONE_BRICKS).or().namedInput(MSBlocks.BLUE_DIRT).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.SHADE_BRICKS).input(Items.STONE_BRICKS).or().namedInput(Items.LAPIS_LAZULI).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.SMOOTH_SHADE_STONE).input(Items.SMOOTH_STONE).or().namedInput(MSBlocks.BLUE_DIRT).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.SMOOTH_SHADE_STONE).input(Items.SMOOTH_STONE).or().namedInput(Items.LAPIS_LAZULI).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.SHADE_BRICK_STAIRS).input(Items.STONE_BRICK_STAIRS).or().namedInput(MSBlocks.BLUE_DIRT).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.SHADE_BRICK_STAIRS).input(Items.STONE_BRICK_STAIRS).or().namedInput(Items.LAPIS_LAZULI).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.TAR_SHADE_BRICKS).input(MSBlocks.SHADE_BRICKS).and().namedInput(MSItems.OIL_BUCKET).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.FROST_TILE).input(Tags.Items.STONE).and().namedInput(Items.ICE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.FROST_TILE).input(Tags.Items.STONE).and().namedInput(Items.PACKED_ICE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.FROST_BRICKS).input(Items.STONE_BRICKS).and().namedInput(Items.ICE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.FROST_BRICKS).input(Items.STONE_BRICKS).and().namedInput(Items.PACKED_ICE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.CHISELED_FROST_BRICKS).input(Items.CHISELED_STONE_BRICKS).and().namedInput(Items.ICE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.CHISELED_FROST_BRICKS).input(Items.CHISELED_STONE_BRICKS).and().namedInput(Items.PACKED_ICE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.CAST_IRON).input(Items.IRON_BLOCK).and().input(Items.LAVA_BUCKET).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.CHISELED_CAST_IRON).input(Items.CHISELED_STONE_BRICKS).or().input(MSBlocks.CAST_IRON).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.STEEL_BEAM).input(Items.QUARTZ_PILLAR).and().input(MSBlocks.CAST_IRON).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.MYCELIUM_STONE).input(Tags.Items.STONE).and().input(Items.MYCELIUM).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.BLACK_STONE).input(Items.STONE).and().namedInput(Items.BLACK_DYE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.BLACK_STONE).input(Items.STONE).or().namedInput(Items.MAGMA_BLOCK).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.BLACK_SAND).input(Items.SAND).and().namedInput(Items.BLACK_DYE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.BLACK_SAND).input(Items.SAND).or().namedInput(Items.MAGMA_BLOCK).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.FLOWERY_MOSSY_COBBLESTONE).input(Items.MOSSY_COBBLESTONE).or().input(ItemTags.SMALL_FLOWERS).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.FLOWERY_MOSSY_STONE_BRICKS).input(Items.MOSSY_STONE_BRICKS).or().input(ItemTags.SMALL_FLOWERS).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.CHALK).input(Tags.Items.STONE).or().namedInput(Items.NAUTILUS_SHELL).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.CHALK).input(Tags.Items.STONE).and().namedInput(Items.WHITE_DYE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.PINK_STONE).input(Tags.Items.STONE).and().namedInput(Items.PINK_DYE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.BROWN_STONE).input(Tags.Items.STONE).and().namedInput(Items.BROWN_DYE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.GREEN_STONE).input(Tags.Items.STONE).and().namedInput(Items.GREEN_DYE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.UNCARVED_WOOD).input(ItemTags.LOGS).and().input(Items.STONE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.CHIPBOARD).input(ItemTags.LOGS).and().input(Items.COBBLESTONE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.WOOD_SHAVINGS).input(ItemTags.LOGS).or().namedInput(Items.STONECUTTER).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.WOOD_SHAVINGS).input(ItemTags.LOGS).or().namedInput(MSItems.CARVING_TOOL).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.TREATED_PLANKS).input(ItemTags.PLANKS).or().input(Items.NETHERRACK).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.FROST_PLANKS).input(ItemTags.PLANKS).or().namedInput(Items.SNOW_BLOCK).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.FROST_PLANKS).input(ItemTags.PLANKS).or().namedInput(Items.SNOW).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.FROST_PLANKS).input(ItemTags.PLANKS).or().namedInput(Items.SNOWBALL).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.FROST_LOG).input(ItemTags.LOGS).or().namedInput(Items.SNOW_BLOCK).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.FROST_LOG).input(ItemTags.LOGS).or().namedInput(Items.SNOW).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.FROST_LOG).input(ItemTags.LOGS).or().namedInput(Items.SNOWBALL).build(consumer);
		
		CombinationRecipeBuilder.of(MSItems.CANDY_CORN).input(Items.SUGAR).and().input(Items.WHEAT_SEEDS).build(consumer);
		CombinationRecipeBuilder.of(MSItems.TUIX_BAR).input(Items.COCOA_BEANS).or().input(Items.BRICK).build(consumer);
		CombinationRecipeBuilder.of(MSItems.GOLDEN_GRASSHOPPER).input(MSItems.GRASSHOPPER).or().input(Items.GOLD_INGOT).build(consumer);
		CombinationRecipeBuilder.of(MSItems.BUG_NET).input(Items.STICK).or().namedInput(Items.COBWEB).build(consumer);
		CombinationRecipeBuilder.of(MSItems.BUG_NET).input(Items.STRING).and().namedInput(Items.BUCKET).build(consumer);
		
		CombinationRecipeBuilder.of(MSItems.UNKNOWABLE_EGG).input(MSItems.SURPRISE_EMBRYO).or().input(MSItems.GRIMOIRE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.LONG_FORGOTTEN_WARHORN).input(Items.NOTE_BLOCK).and().input(MSItems.GRIMOIRE).build(consumer);
		
		CombinationRecipeBuilder.of(MSBlocks.CRUXITE_BLOCK).input(MSItems.RAW_CRUXITE).or().input(Items.STONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(MSBlocks.URANIUM_BLOCK).input(MSItems.RAW_URANIUM).or().input(Items.STONE).buildFor(consumer, Minestuck.MOD_ID);
		
		CombinationRecipeBuilder.of(MSBlocks.PIPE).input(MSBlocks.VEIN).or().namedInput(Items.IRON_INGOT).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(MSBlocks.PIPE).input(Items.HOPPER).or().namedInput(Items.BAMBOO).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(MSBlocks.PIPE_INTERSECTION).input(MSBlocks.CHALK).and().input(Items.CHISELED_STONE_BRICKS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(MSBlocks.PARCEL_PYXIS).input(MSBlocks.PIPE).or().input(MSItems.MAILBOX).buildFor(consumer, Minestuck.MOD_ID);
		
		CombinationRecipeBuilder.of(MSBlocks.TRAJECTORY_BLOCK).input(Items.POWERED_RAIL).or().input(Items.SLIME_BLOCK).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(MSBlocks.STAT_STORER).input(Items.COMPARATOR).and().input(MSBlocks.REMOTE_OBSERVER).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(MSBlocks.REMOTE_OBSERVER).input(Items.OBSERVER).or().input(MSItems.PLUTONIUM_CORE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(MSBlocks.WIRELESS_REDSTONE_TRANSMITTER).input(Items.REPEATER).or().input(MSItems.PLUTONIUM_CORE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(MSBlocks.WIRELESS_REDSTONE_RECEIVER).input(Items.COMPARATOR).or().input(MSItems.PLUTONIUM_CORE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(MSBlocks.SOLID_SWITCH).input(Items.LEVER).and().input(Items.REDSTONE_LAMP).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(MSBlocks.ONE_SECOND_INTERVAL_TIMED_SOLID_SWITCH).input(MSBlocks.SOLID_SWITCH).or().input(Items.STONE_BUTTON).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(MSBlocks.TWO_SECOND_INTERVAL_TIMED_SOLID_SWITCH).input(MSBlocks.SOLID_SWITCH).or().input(ItemTags.WOODEN_BUTTONS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(MSBlocks.PLATFORM_GENERATOR).input(Items.PISTON).or().input(Items.SHIELD).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(MSBlocks.PLATFORM_RECEPTACLE).input(Items.DAYLIGHT_DETECTOR).or().input(Items.IRON_BLOCK).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(MSBlocks.ITEM_MAGNET).input(MSItems.PLUTONIUM_CORE).or().input(Items.FISHING_ROD).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(MSBlocks.ROTATOR).input(Items.PISTON).or().input(Items.COMPASS).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(MSBlocks.FALL_PAD).input(ItemTags.WOOL).or().input(Items.HAY_BLOCK).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(MSBlocks.FRAGILE_STONE).input(Tags.Items.STONE).or().input(Items.GRAVEL).buildFor(consumer, Minestuck.MOD_ID);
		
		CombinationRecipeBuilder.of(MSBlocks.STONE_CRUXITE_ORE).input(MSItems.RAW_CRUXITE).and().input(Items.STONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(MSBlocks.NETHERRACK_CRUXITE_ORE).input(MSItems.RAW_CRUXITE).and().input(Items.NETHERRACK).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(MSBlocks.COBBLESTONE_CRUXITE_ORE).input(MSItems.RAW_CRUXITE).and().input(Items.COBBLESTONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(MSBlocks.SANDSTONE_CRUXITE_ORE).input(MSItems.RAW_CRUXITE).and().input(Items.SANDSTONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(MSBlocks.RED_SANDSTONE_CRUXITE_ORE).input(MSItems.RAW_CRUXITE).and().input(Items.RED_SANDSTONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(MSBlocks.END_STONE_CRUXITE_ORE).input(MSItems.RAW_CRUXITE).and().input(Items.END_STONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(MSBlocks.SHADE_STONE_CRUXITE_ORE).input(MSItems.RAW_CRUXITE).and().input(MSBlocks.SHADE_STONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(MSBlocks.PINK_STONE_CRUXITE_ORE).input(MSItems.RAW_CRUXITE).and().input(MSBlocks.PINK_STONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(MSBlocks.STONE_URANIUM_ORE).input(MSItems.RAW_URANIUM).and().input(Items.STONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(MSBlocks.NETHERRACK_URANIUM_ORE).input(MSItems.RAW_URANIUM).and().input(Items.NETHERRACK).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(MSBlocks.COBBLESTONE_URANIUM_ORE).input(MSItems.RAW_URANIUM).and().input(Items.COBBLESTONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(MSBlocks.SANDSTONE_URANIUM_ORE).input(MSItems.RAW_URANIUM).and().input(Items.SANDSTONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(MSBlocks.RED_SANDSTONE_URANIUM_ORE).input(MSItems.RAW_URANIUM).and().input(Items.RED_SANDSTONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(MSBlocks.END_STONE_URANIUM_ORE).input(MSItems.RAW_URANIUM).and().input(Items.END_STONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(MSBlocks.SHADE_STONE_URANIUM_ORE).input(MSItems.RAW_URANIUM).and().input(MSBlocks.SHADE_STONE).buildFor(consumer, Minestuck.MOD_ID);
		CombinationRecipeBuilder.of(MSBlocks.PINK_STONE_URANIUM_ORE).input(MSItems.RAW_URANIUM).and().input(MSBlocks.PINK_STONE).buildFor(consumer, Minestuck.MOD_ID);
		
		CombinationRecipeBuilder.of(MSBlocks.NETHERRACK_COAL_ORE).input(Items.COAL).and().input(Items.NETHERRACK).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.SHADE_STONE_COAL_ORE).input(Items.COAL).and().input(MSBlocks.SHADE_STONE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.PINK_STONE_COAL_ORE).input(Items.COAL).and().input(MSBlocks.PINK_STONE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.END_STONE_IRON_ORE).input(Items.IRON_INGOT).and().input(Items.END_STONE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.SANDSTONE_IRON_ORE).input(Items.IRON_INGOT).and().input(Items.SANDSTONE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.RED_SANDSTONE_IRON_ORE).input(Items.IRON_INGOT).and().input(Items.RED_SANDSTONE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.SANDSTONE_GOLD_ORE).input(Items.GOLD_INGOT).and().input(Items.SANDSTONE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.RED_SANDSTONE_GOLD_ORE).input(Items.GOLD_INGOT).and().input(Items.RED_SANDSTONE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.SHADE_STONE_GOLD_ORE).input(Items.GOLD_INGOT).and().input(MSBlocks.SHADE_STONE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.PINK_STONE_GOLD_ORE).input(Items.GOLD_INGOT).and().input(MSBlocks.PINK_STONE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.END_STONE_REDSTONE_ORE).input(Items.REDSTONE).and().input(Items.END_STONE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.STONE_QUARTZ_ORE).input(Items.QUARTZ).and().input(Items.STONE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.PINK_STONE_LAPIS_ORE).input(Items.LAPIS_LAZULI).and().input(MSBlocks.PINK_STONE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.PINK_STONE_DIAMOND_ORE).input(Items.DIAMOND).and().input(MSBlocks.PINK_STONE).build(consumer);
		
		CombinationRecipeBuilder.of(MSItems.EMERALD_SWORD).input(Items.DIAMOND_SWORD).or().input(Items.EMERALD).build(consumer);
		CombinationRecipeBuilder.of(MSItems.EMERALD_AXE).input(Items.DIAMOND_AXE).or().input(Items.EMERALD).build(consumer);
		CombinationRecipeBuilder.of(MSItems.EMERALD_PICKAXE).input(Items.DIAMOND_PICKAXE).or().input(Items.EMERALD).build(consumer);
		CombinationRecipeBuilder.of(MSItems.EMERALD_SHOVEL).input(Items.DIAMOND_SHOVEL).or().input(Items.EMERALD).build(consumer);
		CombinationRecipeBuilder.of(MSItems.EMERALD_HOE).input(Items.DIAMOND_HOE).or().input(Items.EMERALD).build(consumer);
		//CombinationRecipeBuilder.of(MSItems.MINE_AND_GRIST).input(Items.DIAMOND_PICKAXE).and().input(MSBlocks.GRIST_WIDGET).build(consumer);
		
		Item[] metalHelmets = new Item[] {Items.IRON_HELMET, Items.GOLDEN_HELMET, Items.NETHERITE_HELMET};
		Item[] metalChestplates = new Item[] {Items.IRON_CHESTPLATE, Items.GOLDEN_CHESTPLATE, Items.NETHERITE_CHESTPLATE};
		Item[] metalLeggings = new Item[] {Items.IRON_LEGGINGS, Items.GOLDEN_LEGGINGS, Items.NETHERITE_LEGGINGS};
		Item[] metalBoots = new Item[] {Items.IRON_BOOTS, Items.GOLDEN_BOOTS, Items.NETHERITE_BOOTS};
		for(int i = 0; i < metalHelmets.length; i++)	//Two out of three possible for-loops is enough for me
			for(IItemProvider prismarine : new IItemProvider[]{Items.PRISMARINE_SHARD, Blocks.PRISMARINE})
			{
				CombinationRecipeBuilder.of(MSItems.PRISMARINE_HELMET).namedInput(metalHelmets[i]).or().namedInput(prismarine).build(consumer);
				CombinationRecipeBuilder.of(MSItems.PRISMARINE_CHESTPLATE).namedInput(metalChestplates[i]).or().namedInput(prismarine).build(consumer);
				CombinationRecipeBuilder.of(MSItems.PRISMARINE_LEGGINGS).namedInput(metalLeggings[i]).or().namedInput(prismarine).build(consumer);
				CombinationRecipeBuilder.of(MSItems.PRISMARINE_BOOTS).namedInput(metalBoots[i]).or().namedInput(prismarine).build(consumer);
			}
		
		CombinationRecipeBuilder.of(MSItems.IRON_LASS_GLASSES).namedInput(Items.IRON_HELMET).and().input(MSItems.ENERGY_CORE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.IRON_LASS_CHESTPLATE).namedInput(Items.IRON_CHESTPLATE).and().input(MSItems.ENERGY_CORE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.IRON_LASS_SKIRT).namedInput(Items.IRON_LEGGINGS).and().input(MSItems.ENERGY_CORE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.IRON_LASS_SHOES).namedInput(Items.IRON_BOOTS).and().input(MSItems.ENERGY_CORE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.PROSPIT_CIRCLET).namedInput(Items.GOLDEN_HELMET).and().input(MSBlocks.WHITE_CROWN_STAINED_GLASS).build(consumer);
		CombinationRecipeBuilder.of(MSItems.PROSPIT_SHIRT).namedInput(Items.LEATHER_CHESTPLATE).and().input(MSBlocks.WHITE_CROWN_STAINED_GLASS).build(consumer);
		CombinationRecipeBuilder.of(MSItems.PROSPIT_PANTS).namedInput(Items.LEATHER_LEGGINGS).and().input(MSBlocks.WHITE_CROWN_STAINED_GLASS).build(consumer);
		CombinationRecipeBuilder.of(MSItems.PROSPIT_SHOES).namedInput(Items.LEATHER_BOOTS).and().input(MSBlocks.WHITE_CROWN_STAINED_GLASS).build(consumer);
		CombinationRecipeBuilder.of(MSItems.DERSE_CIRCLET).namedInput(Items.GOLDEN_HELMET).or().input(MSBlocks.BLACK_CROWN_STAINED_GLASS).build(consumer);
		CombinationRecipeBuilder.of(MSItems.DERSE_SHIRT).namedInput(Items.LEATHER_CHESTPLATE).or().input(MSBlocks.BLACK_CROWN_STAINED_GLASS).build(consumer);
		CombinationRecipeBuilder.of(MSItems.DERSE_PANTS).namedInput(Items.LEATHER_LEGGINGS).or().input(MSBlocks.BLACK_CROWN_STAINED_GLASS).build(consumer);
		CombinationRecipeBuilder.of(MSItems.DERSE_SHOES).namedInput(Items.LEATHER_BOOTS).or().input(MSBlocks.BLACK_CROWN_STAINED_GLASS).build(consumer);
		
		CombinationRecipeBuilder.of(MSBlocks.PRIMED_TNT).input(Items.TNT).or().input(ItemTags.BUTTONS).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.UNSTABLE_TNT).input(Items.TNT).or().input(Items.REDSTONE_TORCH).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.INSTANT_TNT).input(Items.TNT).or().input(Items.REDSTONE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.STONE_EXPLOSIVE_BUTTON).namedInput(Items.TNT).and().input(Items.STONE_BUTTON).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.STONE_EXPLOSIVE_BUTTON).namedInput(MSBlocks.INSTANT_TNT).and().input(Items.STONE_BUTTON).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.WOODEN_EXPLOSIVE_BUTTON).namedInput(Items.TNT).and().input(ItemTags.WOODEN_BUTTONS).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.WOODEN_EXPLOSIVE_BUTTON).namedInput(MSBlocks.INSTANT_TNT).and().input(ItemTags.WOODEN_BUTTONS).build(consumer);
		
		CombinationRecipeBuilder.of(MSBlocks.APPLE_CAKE).input(Items.CAKE).or().input(Items.APPLE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.BLUE_CAKE).input(Items.CAKE).or().input(MSBlocks.GLOWING_MUSHROOM).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.COLD_CAKE).input(Items.CAKE).or().namedInput(Items.ICE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.COLD_CAKE).input(Items.CAKE).or().namedInput(Items.PACKED_ICE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.RED_CAKE).input(Items.CAKE).or().namedInput(Items.MELON_SLICE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.RED_CAKE).input(Items.CAKE).or().namedInput(Items.GLISTERING_MELON_SLICE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.HOT_CAKE).input(Items.CAKE).or().namedInput(Items.LAVA_BUCKET).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.HOT_CAKE).input(Items.CAKE).or().namedInput(Items.BLAZE_POWDER).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.HOT_CAKE).input(Items.CAKE).or().namedInput(Items.MAGMA_BLOCK).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.REVERSE_CAKE).input(Items.CAKE).or().namedInput(Items.GLASS).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.REVERSE_CAKE).input(Items.CAKE).or().namedInput(Items.GLASS_PANE).build(consumer);
		CombinationRecipeBuilder.of(MSBlocks.NEGATIVE_CAKE).input(MSBlocks.REVERSE_CAKE).and().input(MSBlocks.FUCHSIA_CAKE).build(consumer);	//Had a drop in creativity, but I guess its fine
		CombinationRecipeBuilder.of(MSBlocks.CARROT_CAKE).input(Items.CAKE).or().input(Items.CARROT).build(consumer);
		
		CombinationRecipeBuilder.of(MSItems.ROCK_COOKIE).input(Items.COOKIE).and().namedInput(Items.STONE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.ROCK_COOKIE).input(Items.COOKIE).and().namedInput(Items.COBBLESTONE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.ROCK_COOKIE).input(Items.COOKIE).and().namedInput(Items.GRAVEL).build(consumer);
		CombinationRecipeBuilder.of(MSItems.WOODEN_CARROT).input(Items.CARROT).and().namedInput(ItemTags.PLANKS).build(consumer);
		CombinationRecipeBuilder.of(MSItems.WOODEN_CARROT).input(Items.CARROT).and().namedInput(ItemTags.LOGS).build(consumer);
		CombinationRecipeBuilder.of(MSItems.FUNGAL_SPORE).input(Items.WHEAT_SEEDS).or().input(Tags.Items.MUSHROOMS).build(consumer);
		CombinationRecipeBuilder.of(MSItems.SPOREO).input(Items.COOKIE).and().input(MSItems.FUNGAL_SPORE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.FRENCH_FRY).input(Items.POTATO).and().namedInput(Items.STICK).build(consumer);
		CombinationRecipeBuilder.of(MSItems.FRENCH_FRY).input(Items.POTATO).and().namedInput(Items.BLAZE_ROD).build(consumer);
		CombinationRecipeBuilder.of(MSItems.SURPRISE_EMBRYO).input(Items.EGG).and().input(Items.PUMPKIN).build(consumer);
		CombinationRecipeBuilder.of(MSItems.FOOD_CAN).input(Items.COOKED_BEEF).and().input(Items.IRON_INGOT).build(consumer);
		
		CombinationRecipeBuilder.of(MSItems.INK_SQUID_PRO_QUO).input(MSItems.PAPER_SWORD).or().namedInput(Items.INK_SAC).build(consumer);
		CombinationRecipeBuilder.of(MSItems.EIGHTBALL).input(MSItems.DICE).or().namedInput(Items.BUCKET).build(consumer);
		
		CombinationRecipeBuilder.of(MSItems.PHLEGM_GUSHERS).input(MSItems.BUILD_GUSHERS).and().namedInput(Items.SLIME_BALL).build(consumer);
		CombinationRecipeBuilder.of(MSItems.SORROW_GUSHERS).input(MSItems.PHLEGM_GUSHERS).and().namedInput(MSItems.INK_SQUID_PRO_QUO).build(consumer);
		
		CombinationRecipeBuilder.of(MSItems.BARBASOL_BOMB).input(MSItems.BUILD_GUSHERS).and().input(MSItems.BARBASOL).build(consumer);
		CombinationRecipeBuilder.of(MSItems.SCALEMATE_APPLESCAB).input(Items.WHITE_WOOL).and().input(Items.DRAGON_BREATH).build(consumer);
		
		CombinationRecipeBuilder.of(MSItems.ENERGY_CORE).input(MSItems.RAW_CRUXITE).and().input(MSItems.RAW_URANIUM).build(consumer);
		CombinationRecipeBuilder.of(MSItems.IRRADIATED_STEAK).input(MSItems.RAW_URANIUM).or().input(Items.COOKED_BEEF).build(consumer);
		CombinationRecipeBuilder.of(MSItems.QUANTUM_SABRE).input(MSItems.URANIUM_POWERED_STICK).and().input(MSItems.ENERGY_CORE).build(consumer);
		
		CombinationRecipeBuilder.of(MSItems.APPLE_JUICE).input(Items.GLASS_BOTTLE).and().input(Items.APPLE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.TAB).input(Items.POTION).or().input(Items.SUGAR).build(consumer);
		CombinationRecipeBuilder.of(MSItems.ORANGE_FAYGO).input(Items.POTION).or().input(Items.ORANGE_DYE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CANDY_APPLE_FAYGO).input(MSItems.ORANGE_FAYGO).or().input(Items.APPLE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.FAYGO_COLA).input(MSItems.ORANGE_FAYGO).or().input(MSItems.TAB).build(consumer);
		CombinationRecipeBuilder.of(MSItems.COTTON_CANDY_FAYGO).input(MSItems.ORANGE_FAYGO).or().input(Items.LIGHT_BLUE_WOOL).build(consumer);
		CombinationRecipeBuilder.of(MSItems.CREME_SODA_FAYGO).input(MSItems.ORANGE_FAYGO).or().input(Items.MILK_BUCKET).build(consumer);
		CombinationRecipeBuilder.of(MSItems.GRAPE_FAYGO).input(MSItems.ORANGE_FAYGO).or().input(Items.CHORUS_FRUIT).build(consumer);
		CombinationRecipeBuilder.of(MSItems.MOON_MIST_FAYGO).input(MSItems.ORANGE_FAYGO).or().input(Items.LIME_DYE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.PEACH_FAYGO).input(MSItems.CANDY_APPLE_FAYGO).and().input(Items.PINK_DYE).build(consumer);
		CombinationRecipeBuilder.of(MSItems.REDPOP_FAYGO).input(MSItems.ORANGE_FAYGO).or().input(Items.TNT).build(consumer);
		
	}
	
	@Override
	public String getName()
	{
		return "Minestuck combination recipes";
	}
}
