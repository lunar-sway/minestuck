package com.mraof.minestuck.data.tag;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.data.AspectTreeBlocksData;
import com.mraof.minestuck.util.ExtraForgeTags;
import com.mraof.minestuck.util.MSTags;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.concurrent.CompletableFuture;

import static com.mraof.minestuck.block.MSBlocks.*;
import static com.mraof.minestuck.util.MSTags.Blocks.*;
import static net.minecraft.tags.BlockTags.*;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class MinestuckBlockTagsProvider extends BlockTagsProvider
{
	public MinestuckBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper)
	{
		super(output, lookupProvider, Minestuck.MOD_ID, existingFileHelper);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	protected void addTags(HolderLookup.Provider provider)
	{
		AspectTreeBlocksData.addToBlockTags(this);
		
		tag(DIRT).add(BLUE_DIRT.get());
		tag(PLANKS).add(GLOWING_PLANKS.get(), SHADEWOOD_PLANKS.get(), FROST_PLANKS.get(), RAINBOW_PLANKS.get(), END_PLANKS.get(), DEAD_PLANKS.get(), CARVED_PLANKS.get(), TREATED_PLANKS.get(), LACQUERED_PLANKS.get(), CINDERED_PLANKS.get());
		tag(STONE_BRICKS).add(DECREPIT_STONE_BRICKS.get(), MOSSY_DECREPIT_STONE_BRICKS.get(), FLOWERY_MOSSY_STONE_BRICKS.get());
		tag(WOODEN_BUTTONS).add(WOODEN_EXPLOSIVE_BUTTON.get());
		tag(BUTTONS).add(STONE_EXPLOSIVE_BUTTON.get());
		tag(WOODEN_STAIRS).add(RAINBOW_PLANKS_STAIRS.get(), END_PLANKS_STAIRS.get(), DEAD_PLANKS_STAIRS.get(), CARVED_STAIRS.get(),  TREATED_PLANKS_STAIRS.get(), LACQUERED_STAIRS.get(), CINDERED_STAIRS.get());
		tag(WOODEN_SLABS).add(RAINBOW_PLANKS_SLAB.get(), END_PLANKS_SLAB.get(), DEAD_PLANKS_SLAB.get(), CARVED_SLAB.get(), TREATED_PLANKS_SLAB.get(), LACQUERED_SLAB.get(), CINDERED_SLAB.get());
		tag(SAPLINGS).add(RAINBOW_SAPLING.get(), END_SAPLING.get(), SHADEWOOD_SAPLING.get());
		tag(LOGS).addTags(GLOWING_LOGS, FROST_LOGS, RAINBOW_LOGS, END_LOGS, VINE_LOGS, FLOWERY_VINE_LOGS, DEAD_LOGS, PETRIFIED_LOGS, ASPECT_LOGS, SHADEWOOD_LOGS, CINDERED_LOGS);
		tag(ENDERMAN_HOLDABLE).add(THOUGHT_DIRT.get());
		tag(STAIRS).add(BLACK_CHESS_BRICK_STAIRS.get(), DARK_GRAY_CHESS_BRICK_STAIRS.get(), LIGHT_GRAY_CHESS_BRICK_STAIRS.get(), WHITE_CHESS_BRICK_STAIRS.get(), COARSE_STONE_STAIRS.get(), COARSE_STONE_BRICK_STAIRS.get(), SHADE_STAIRS.get(), SHADE_BRICK_STAIRS.get(), FROST_TILE_STAIRS.get(), FROST_BRICK_STAIRS.get(), CAST_IRON_STAIRS.get(), BLACK_STONE_STAIRS.get(), BLACK_STONE_BRICK_STAIRS.get(), MYCELIUM_STAIRS.get(), MYCELIUM_BRICK_STAIRS.get(), CHALK_STAIRS.get(), CHALK_BRICK_STAIRS.get(), PINK_STONE_BRICK_STAIRS.get(), BROWN_STONE_BRICK_STAIRS.get(), GREEN_STONE_BRICK_STAIRS.get(), FLOWERY_MOSSY_STONE_BRICK_STAIRS.get());
		tag(SLABS).add(COARSE_STONE_SLAB.get(), COARSE_STONE_BRICK_SLAB.get(), BLACK_CHESS_BRICK_SLAB.get(), DARK_GRAY_CHESS_BRICK_SLAB.get(), LIGHT_GRAY_CHESS_BRICK_SLAB.get(), WHITE_CHESS_BRICK_SLAB.get(), CHALK_SLAB.get(), CHALK_BRICK_SLAB.get(), PINK_STONE_BRICK_SLAB.get(), BROWN_STONE_BRICK_SLAB.get(), GREEN_STONE_BRICK_SLAB.get(), BLACK_STONE_SLAB.get(), BLACK_STONE_BRICK_SLAB.get(), MYCELIUM_SLAB.get(), MYCELIUM_BRICK_SLAB.get(), FLOWERY_MOSSY_STONE_BRICK_SLAB.get(), FROST_TILE_SLAB.get(), FROST_BRICK_SLAB.get(), SHADE_SLAB.get(), SHADE_BRICK_SLAB.get());
		tag(LEAVES).add(FROST_LEAVES.get(), RAINBOW_LEAVES.get(), END_LEAVES.get()).addTags(MSTags.Blocks.SHADEWOOD_LEAVES);
		tag(CLIMBABLE).add(GREEN_STONE_BRICK_EMBEDDED_LADDER.get(), GLOWING_MUSHROOM_VINES.get(), GLOWING_LADDER.get(), FROST_LADDER.get(), RAINBOW_LADDER.get(), END_LADDER.get(), DEAD_LADDER.get(), TREATED_LADDER.get());
		tag(GUARDED_BY_PIGLINS).add(GOLD_SEEDS.get());
		tag(MUSHROOM_GROW_BLOCK).add(BLUE_DIRT.get());
		tag(ENDERMAN_HOLDABLE).add(GLOWING_MUSHROOM.get(), DESERT_BUSH.get(), BLOOMING_CACTUS.get(), STRAWBERRY.get());
		tag(IMPERMEABLE).add(PLATFORM_BLOCK.get(), CHECKERED_STAINED_GLASS.get(), BLACK_CROWN_STAINED_GLASS.get(), BLACK_PAWN_STAINED_GLASS.get(), WHITE_CROWN_STAINED_GLASS.get(), WHITE_PAWN_STAINED_GLASS.get());
		tag(SAND).add(BLACK_SAND.get());
		tag(BEE_GROWABLES).add(BLOOMING_CACTUS.get(), GLOWFLOWER.get());
		tag(PORTALS).add(SKAIA_PORTAL.get(), GATE.get(), GATE_MAIN.get(), RETURN_NODE.get(), RETURN_NODE_MAIN.get());
		tag(NETHER_CARVER_REPLACEABLES).add(BLACK_STONE.get());
		tag(Tags.Blocks.COBBLESTONE).add(FLOWERY_MOSSY_COBBLESTONE.get(), MYCELIUM_COBBLESTONE.get(), BLACK_COBBLESTONE.get());
		tag(Tags.Blocks.END_STONES).add(COARSE_END_STONE.get());
		tag(Tags.Blocks.ORES).addTags(CRUXITE_ORES, ExtraForgeTags.Blocks.URANIUM_ORES);
		tag(BlockTags.COAL_ORES).addTag(MSTags.Blocks.COAL_ORES);
		tag(BlockTags.DIAMOND_ORES).addTag(MSTags.Blocks.DIAMOND_ORES);
		tag(BlockTags.GOLD_ORES).addTag(MSTags.Blocks.GOLD_ORES);
		tag(BlockTags.IRON_ORES).addTag(MSTags.Blocks.IRON_ORES);
		tag(BlockTags.LAPIS_ORES).addTag(MSTags.Blocks.LAPIS_ORES);
		tag(Tags.Blocks.ORES_QUARTZ).addTag(QUARTZ_ORES);
		tag(BlockTags.REDSTONE_ORES).addTag(MSTags.Blocks.REDSTONE_ORES);
		tag(BlockTags.EMERALD_ORES).addTag(MSTags.Blocks.EMERALD_ORES);
		tag(BlockTags.DAMPENS_VIBRATIONS).add(DENSE_CLOUD.get(), BRIGHT_DENSE_CLOUD.get());
		tag(Tags.Blocks.STONE).add(COARSE_STONE.get(), SHADE_STONE.get(), MYCELIUM_STONE.get(), BLACK_STONE.get(), COARSE_END_STONE.get(), PINK_STONE.get(), BROWN_STONE.get(), GREEN_STONE.get());
		tag(Tags.Blocks.STORAGE_BLOCKS).addTags(CRUXITE_STORAGE_BLOCKS, ExtraForgeTags.Blocks.URANIUM_STORAGE_BLOCKS);
		tag(Tags.Blocks.STAINED_GLASS).add(CHECKERED_STAINED_GLASS.get(), BLACK_CROWN_STAINED_GLASS.get(), BLACK_PAWN_STAINED_GLASS.get(), WHITE_CROWN_STAINED_GLASS.get(), WHITE_PAWN_STAINED_GLASS.get());
		tag(Tags.Blocks.BOOKSHELVES).add(GLOWING_BOOKSHELF.get(), FROST_BOOKSHELF.get(), RAINBOW_BOOKSHELF.get(), END_BOOKSHELF.get(), DEAD_BOOKSHELF.get(), TREATED_BOOKSHELF.get());
		
		tag(WOOD_TERRAIN_BLOCKS).add(UNCARVED_WOOD.get(), CHIPBOARD.get(), WOOD_SHAVINGS.get(), CARVED_HEAVY_PLANKS.get(), CARVED_PLANKS.get(), TREATED_UNCARVED_WOOD.get(), TREATED_CHIPBOARD.get(), TREATED_WOOD_SHAVINGS.get(), TREATED_HEAVY_PLANKS.get(), TREATED_PLANKS.get(), LACQUERED_UNCARVED_WOOD.get(), LACQUERED_CHIPBOARD.get(), LACQUERED_WOOD_SHAVINGS.get(), LACQUERED_HEAVY_PLANKS.get(), LACQUERED_PLANKS.get());
		tag(HEAT_TERRAIN_BLOCKS).add(IGNEOUS_STONE.get(), PUMICE_STONE.get(), BLACK_STONE.get());
		
		tag(MINEABLE_WITH_SHOVEL).add(BLACK_CHESS_DIRT.get(), WHITE_CHESS_DIRT.get(), DARK_GRAY_CHESS_DIRT.get(), LIGHT_GRAY_CHESS_DIRT.get());
		
		needsWoodPickaxe(BLACK_CHESS_BRICKS.get(), DARK_GRAY_CHESS_BRICKS.get(), LIGHT_GRAY_CHESS_BRICKS.get(), WHITE_CHESS_BRICKS.get(), BLACK_CHESS_BRICK_SMOOTH_BUTTON.get(), BLACK_CHESS_BRICK_WALL.get(), DARK_GRAY_CHESS_BRICK_WALL.get(), LIGHT_GRAY_CHESS_BRICK_WALL.get(), WHITE_CHESS_BRICK_WALL.get());
		needsWoodPickaxe(BLACK_CHESS_BRICK_SMOOTH.get(), DARK_GRAY_CHESS_BRICK_SMOOTH.get(), LIGHT_GRAY_CHESS_BRICK_SMOOTH.get(), WHITE_CHESS_BRICK_SMOOTH.get(),BLACK_CHESS_BRICK_SMOOTH_BUTTON.get(), BLACK_CHESS_BRICK_SMOOTH_PRESSURE_PLATE.get(), BLACK_CHESS_BRICK_SMOOTH_SLAB.get(), BLACK_CHESS_BRICK_SMOOTH_STAIRS.get(), BLACK_CHESS_BRICK_SMOOTH_WALL.get(), BLACK_CHESS_BRICK_SMOOTH_BUTTON.get(), BLACK_CHESS_BRICK_SMOOTH_PRESSURE_PLATE.get(), BLACK_CHESS_BRICK_SMOOTH_SLAB.get(), BLACK_CHESS_BRICK_SMOOTH_STAIRS.get(), BLACK_CHESS_BRICK_SMOOTH_WALL.get(), DARK_GRAY_CHESS_BRICK_SMOOTH_BUTTON.get(), DARK_GRAY_CHESS_BRICK_SMOOTH_PRESSURE_PLATE.get(), DARK_GRAY_CHESS_BRICK_SMOOTH_SLAB.get(), DARK_GRAY_CHESS_BRICK_SMOOTH_STAIRS.get(), DARK_GRAY_CHESS_BRICK_SMOOTH_WALL.get(), LIGHT_GRAY_CHESS_BRICK_SMOOTH_BUTTON.get(), LIGHT_GRAY_CHESS_BRICK_SMOOTH_PRESSURE_PLATE.get(), LIGHT_GRAY_CHESS_BRICK_SMOOTH_SLAB.get(), LIGHT_GRAY_CHESS_BRICK_SMOOTH_STAIRS.get(), LIGHT_GRAY_CHESS_BRICK_SMOOTH_WALL.get(), SMOOTH_SHADE_STONE_SLAB.get(), SMOOTH_SHADE_STONE_STAIRS.get(), SMOOTH_SHADE_STONE_WALL.get(), WHITE_CHESS_BRICK_SMOOTH_BUTTON.get(), WHITE_CHESS_BRICK_SMOOTH_PRESSURE_PLATE.get(), WHITE_CHESS_BRICK_SMOOTH_SLAB.get(), WHITE_CHESS_BRICK_SMOOTH_STAIRS.get(), WHITE_CHESS_BRICK_SMOOTH_WALL.get());
		needsWoodPickaxe(BLACK_CHESS_BRICK_TRIM.get(), DARK_GRAY_CHESS_BRICK_TRIM.get(), LIGHT_GRAY_CHESS_BRICK_TRIM.get(), WHITE_CHESS_BRICK_TRIM.get());
		
		needsWoodPickaxe(STONE_CRUXITE_ORE.get(), NETHERRACK_CRUXITE_ORE.get(), COBBLESTONE_CRUXITE_ORE.get(), SANDSTONE_CRUXITE_ORE.get(), RED_SANDSTONE_CRUXITE_ORE.get(), END_STONE_CRUXITE_ORE.get(), SHADE_STONE_CRUXITE_ORE.get(), PINK_STONE_CRUXITE_ORE.get(), MYCELIUM_STONE_CRUXITE_ORE.get());
		needsStonePickaxe(STONE_URANIUM_ORE.get(), DEEPSLATE_URANIUM_ORE.get(), NETHERRACK_URANIUM_ORE.get(), COBBLESTONE_URANIUM_ORE.get(), SANDSTONE_URANIUM_ORE.get(), RED_SANDSTONE_URANIUM_ORE.get(), END_STONE_URANIUM_ORE.get(), SHADE_STONE_URANIUM_ORE.get(), PINK_STONE_URANIUM_ORE.get(), MYCELIUM_STONE_URANIUM_ORE.get());
		needsWoodPickaxe(NETHERRACK_COAL_ORE.get(), SHADE_STONE_COAL_ORE.get(), PINK_STONE_COAL_ORE.get());
		needsStonePickaxe(END_STONE_IRON_ORE.get(), SANDSTONE_IRON_ORE.get(), RED_SANDSTONE_IRON_ORE.get());
		needsIronPickaxe(SANDSTONE_GOLD_ORE.get(), RED_SANDSTONE_GOLD_ORE.get(), SHADE_STONE_GOLD_ORE.get(), PINK_STONE_GOLD_ORE.get());
		needsIronPickaxe(END_STONE_REDSTONE_ORE.get());
		needsWoodPickaxe(STONE_QUARTZ_ORE.get());
		needsStonePickaxe(PINK_STONE_LAPIS_ORE.get());
		needsIronPickaxe(PINK_STONE_DIAMOND_ORE.get());
		tag(MINEABLE_WITH_AXE).add(UNCARVED_WOOD_IRON_ORE.get(), UNCARVED_WOOD_REDSTONE_ORE.get(), UNCARVED_WOOD_EMERALD_ORE.get(), UNCARVED_WOOD_CRUXITE_ORE.get(), UNCARVED_WOOD_URANIUM_ORE.get());
		tag(NEEDS_STONE_TOOL).add(UNCARVED_WOOD_IRON_ORE.get(), UNCARVED_WOOD_URANIUM_ORE.get());
		tag(NEEDS_IRON_TOOL).add(UNCARVED_WOOD_REDSTONE_ORE.get(), UNCARVED_WOOD_EMERALD_ORE.get());
		
		needsWoodPickaxe(CRUXITE_BLOCK.get());
		needsWoodPickaxe(URANIUM_BLOCK.get());
		
		needsWoodPickaxe(COARSE_STONE.get(), CHISELED_COARSE_STONE.get(), COARSE_STONE_BRICKS.get(), COARSE_STONE_COLUMN.get(), CHISELED_COARSE_STONE_BRICKS.get(), CRACKED_COARSE_STONE_BRICKS.get(), MOSSY_COARSE_STONE_BRICKS.get());
		needsWoodPickaxe(SHADE_STONE.get(), SMOOTH_SHADE_STONE.get(), SHADE_BRICKS.get(), SHADE_COLUMN.get(), CHISELED_SHADE_BRICKS.get(), CRACKED_SHADE_BRICKS.get(), MOSSY_SHADE_BRICKS.get(), BLOOD_SHADE_BRICKS.get(), TAR_SHADE_BRICKS.get());
		needsWoodPickaxe(FROST_TILE.get(), CHISELED_FROST_TILE.get(), FROST_BRICKS.get(), FROST_COLUMN.get(), CHISELED_FROST_BRICKS.get(), CRACKED_FROST_BRICKS.get(), FLOWERY_FROST_BRICKS.get());
		needsWoodPickaxe(CAST_IRON.get(), CHISELED_CAST_IRON.get());
		needsWoodPickaxe(STEEL_BEAM.get());
		needsWoodPickaxe(MYCELIUM_COBBLESTONE.get(), MYCELIUM_STONE.get(), POLISHED_MYCELIUM_STONE.get(), MYCELIUM_BRICKS.get(), MYCELIUM_COLUMN.get(), CHISELED_MYCELIUM_BRICKS.get(), CRACKED_MYCELIUM_BRICKS.get(), SUSPICIOUS_CHISELED_MYCELIUM_BRICKS.get(), MOSSY_MYCELIUM_BRICKS.get(), FLOWERY_MYCELIUM_BRICKS.get());
		needsWoodPickaxe(BLACK_STONE.get(), POLISHED_BLACK_STONE.get(), BLACK_COBBLESTONE.get(), BLACK_STONE_BRICKS.get(), BLACK_STONE_COLUMN.get(), CRACKED_BLACK_STONE_BRICKS.get(), CHISELED_BLACK_STONE_BRICKS.get(), BLACK_COBBLESTONE_SLAB.get(), BLACK_COBBLESTONE_STAIRS.get(), BLACK_COBBLESTONE_WALL.get(), BLACK_STONE_BRICK_WALL.get(), BLACK_STONE_BUTTON.get(), BLACK_STONE_PRESSURE_PLATE.get(), BLACK_STONE_WALL.get());
		tag(MINEABLE_WITH_SHOVEL).add(BLACK_SAND.get());
		needsWoodPickaxe(DECREPIT_STONE_BRICKS.get(), FLOWERY_MOSSY_COBBLESTONE.get(), MOSSY_DECREPIT_STONE_BRICKS.get(), FLOWERY_MOSSY_STONE_BRICKS.get());
		needsWoodPickaxe(COARSE_END_STONE.get(), END_GRASS.get());
		needsWoodPickaxe(CHALK.get(), POLISHED_CHALK.get(), CHALK_BRICKS.get(), CHALK_COLUMN.get(), CHISELED_CHALK_BRICKS.get(), MOSSY_CHALK_BRICKS.get(), FLOWERY_CHALK_BRICKS.get());
		needsWoodPickaxe(PINK_STONE.get(), POLISHED_PINK_STONE.get(), PINK_STONE_BRICKS.get(), PINK_STONE_COLUMN.get(), CHISELED_PINK_STONE_BRICKS.get(), CRACKED_PINK_STONE_BRICKS.get(), MOSSY_PINK_STONE_BRICKS.get());
		needsWoodPickaxe(IGNEOUS_STONE.get(), IGNEOUS_SPIKE.get(), PUMICE_STONE.get());
		
		needsStonePickaxe(BROWN_STONE.get(), POLISHED_BROWN_STONE.get(), BROWN_STONE_BRICKS.get(), CRACKED_BROWN_STONE_BRICKS.get(), BROWN_STONE_COLUMN.get());
		needsStonePickaxe(GREEN_STONE.get(), POLISHED_GREEN_STONE.get(), GREEN_STONE_BRICKS.get(), GREEN_STONE_COLUMN.get(), CHISELED_GREEN_STONE_BRICKS.get());
		needsStonePickaxe(HORIZONTAL_GREEN_STONE_BRICKS.get(), VERTICAL_GREEN_STONE_BRICKS.get(), GREEN_STONE_BRICK_EMBEDDED_LADDER.get(), GREEN_STONE_BRICK_TRIM.get());
		needsStonePickaxe(GREEN_STONE_BRICK_FROG.get(), GREEN_STONE_BRICK_IGUANA_LEFT.get(), GREEN_STONE_BRICK_IGUANA_RIGHT.get(), GREEN_STONE_BRICK_LOTUS.get(),
				GREEN_STONE_BRICK_NAK_LEFT.get(), GREEN_STONE_BRICK_NAK_RIGHT.get(), GREEN_STONE_BRICK_SALAMANDER_LEFT.get(), GREEN_STONE_BRICK_SALAMANDER_RIGHT.get(),
				GREEN_STONE_BRICK_SKAIA.get(), GREEN_STONE_BRICK_TURTLE.get());
		needsStonePickaxe(BROWN_STONE_BRICK_WALL.get(), BROWN_STONE_BUTTON.get(), BROWN_STONE_PRESSURE_PLATE.get(), BROWN_STONE_WALL.get(), POLISHED_BROWN_STONE_SLAB.get(), POLISHED_BROWN_STONE_STAIRS.get(), POLISHED_BROWN_STONE_WALL.get());
		needsStonePickaxe(GREEN_STONE_BRICK_WALL.get(), GREEN_STONE_BUTTON.get(), GREEN_STONE_PRESSURE_PLATE.get(), HORIZONTAL_GREEN_STONE_BRICK_SLAB.get(), HORIZONTAL_GREEN_STONE_BRICK_STAIRS.get(), HORIZONTAL_GREEN_STONE_BRICK_WALL.get(), POLISHED_GREEN_STONE_SLAB.get(), POLISHED_GREEN_STONE_STAIRS.get(), POLISHED_GREEN_STONE_WALL.get(), VERTICAL_GREEN_STONE_BRICK_SLAB.get(), VERTICAL_GREEN_STONE_BRICK_STAIRS.get(), VERTICAL_GREEN_STONE_BRICK_WALL.get());
		needsWoodPickaxe(SANDSTONE_COLUMN.get(), CHISELED_SANDSTONE_COLUMN.get(), RED_SANDSTONE_COLUMN.get(), CHISELED_RED_SANDSTONE_COLUMN.get());
		
		tag(MINEABLE_WITH_AXE).add(UNCARVED_WOOD.get(), UNCARVED_WOOD_BUTTON.get(), UNCARVED_WOOD_FENCE.get(), UNCARVED_WOOD_FENCE_GATE.get(), UNCARVED_WOOD_PRESSURE_PLATE.get(), UNCARVED_WOOD_SLAB.get(), UNCARVED_WOOD_STAIRS.get(), CHIPBOARD.get(), CHIPBOARD_BUTTON.get(), CHIPBOARD_FENCE.get(), CHIPBOARD_FENCE_GATE.get(), CHIPBOARD_PRESSURE_PLATE.get(), CHIPBOARD_SLAB.get(), CHIPBOARD_STAIRS.get(), CARVED_HEAVY_PLANKS.get(), CARVED_HEAVY_PLANK_SLAB.get(), CARVED_HEAVY_PLANK_STAIRS.get(), CARVED_PLANKS.get(), CARVED_BUTTON.get(), CARVED_FENCE.get(), CARVED_FENCE_GATE.get(), CARVED_PRESSURE_PLATE.get(), CARVED_SLAB.get(), CARVED_STAIRS.get(), CARVED_KNOTTED_WOOD.get(), POLISHED_UNCARVED_WOOD.get(), POLISHED_UNCARVED_SLAB.get(), POLISHED_UNCARVED_STAIRS.get(), WOODEN_GRASS.get(), TREATED_UNCARVED_WOOD.get(), TREATED_UNCARVED_WOOD_BUTTON.get(), TREATED_UNCARVED_WOOD_FENCE.get(), TREATED_UNCARVED_WOOD_FENCE_GATE.get(), TREATED_UNCARVED_WOOD_PRESSURE_PLATE.get(), TREATED_UNCARVED_WOOD_SLAB.get(), TREATED_UNCARVED_WOOD_STAIRS.get(), TREATED_CHIPBOARD.get(), TREATED_CHIPBOARD_BUTTON.get(), TREATED_CHIPBOARD_FENCE.get(), TREATED_CHIPBOARD_FENCE_GATE.get(), TREATED_CHIPBOARD_PRESSURE_PLATE.get(), TREATED_CHIPBOARD_SLAB.get(), TREATED_CHIPBOARD_STAIRS.get(), TREATED_HEAVY_PLANKS.get(), TREATED_HEAVY_PLANK_SLAB.get(), TREATED_HEAVY_PLANK_STAIRS.get(), TREATED_PLANKS.get(), TREATED_BUTTON.get(), TREATED_FENCE.get(), TREATED_FENCE_GATE.get(), TREATED_PRESSURE_PLATE.get(), TREATED_PLANKS_SLAB.get(), TREATED_PLANKS_STAIRS.get(), TREATED_CARVED_KNOTTED_WOOD.get(), POLISHED_TREATED_UNCARVED_WOOD.get(), POLISHED_TREATED_UNCARVED_SLAB.get(), POLISHED_TREATED_UNCARVED_STAIRS.get(), TREATED_WOODEN_GRASS.get(), LACQUERED_UNCARVED_WOOD.get(), LACQUERED_UNCARVED_WOOD_BUTTON.get(), LACQUERED_UNCARVED_WOOD_FENCE.get(), LACQUERED_UNCARVED_WOOD_FENCE_GATE.get(), LACQUERED_UNCARVED_WOOD_PRESSURE_PLATE.get(), LACQUERED_UNCARVED_WOOD_SLAB.get(), LACQUERED_UNCARVED_WOOD_STAIRS.get(), LACQUERED_CHIPBOARD.get(), LACQUERED_CHIPBOARD_BUTTON.get(), LACQUERED_CHIPBOARD_FENCE.get(), LACQUERED_CHIPBOARD_FENCE_GATE.get(), LACQUERED_CHIPBOARD_PRESSURE_PLATE.get(), LACQUERED_CHIPBOARD_SLAB.get(), LACQUERED_CHIPBOARD_STAIRS.get(), LACQUERED_HEAVY_PLANKS.get(), LACQUERED_HEAVY_PLANK_SLAB.get(), LACQUERED_HEAVY_PLANK_STAIRS.get(), LACQUERED_PLANKS.get(), LACQUERED_BUTTON.get(), LACQUERED_FENCE.get(), LACQUERED_FENCE_GATE.get(), LACQUERED_PRESSURE_PLATE.get(), LACQUERED_SLAB.get(), LACQUERED_STAIRS.get(), LACQUERED_CARVED_KNOTTED_WOOD.get(), POLISHED_LACQUERED_UNCARVED_WOOD.get(), POLISHED_LACQUERED_UNCARVED_SLAB.get(), POLISHED_LACQUERED_UNCARVED_STAIRS.get(), LACQUERED_WOODEN_MUSHROOM.get(), WOODEN_LAMP.get());
		tag(NEEDS_STONE_TOOL).add(UNCARVED_WOOD.get(), UNCARVED_WOOD_BUTTON.get(), UNCARVED_WOOD_FENCE.get(), UNCARVED_WOOD_FENCE_GATE.get(), UNCARVED_WOOD_PRESSURE_PLATE.get(), UNCARVED_WOOD_SLAB.get(), UNCARVED_WOOD_STAIRS.get(), CHIPBOARD.get(), CHIPBOARD_BUTTON.get(), CHIPBOARD_FENCE.get(), CHIPBOARD_FENCE_GATE.get(), CHIPBOARD_PRESSURE_PLATE.get(), CHIPBOARD_SLAB.get(), CHIPBOARD_STAIRS.get(), CARVED_HEAVY_PLANKS.get(), CARVED_HEAVY_PLANK_SLAB.get(), CARVED_HEAVY_PLANK_STAIRS.get(), CARVED_KNOTTED_WOOD.get(), POLISHED_UNCARVED_WOOD.get(), POLISHED_UNCARVED_SLAB.get(), POLISHED_UNCARVED_STAIRS.get(), TREATED_UNCARVED_WOOD.get(), TREATED_UNCARVED_WOOD_BUTTON.get(), TREATED_UNCARVED_WOOD_FENCE.get(), TREATED_UNCARVED_WOOD_FENCE_GATE.get(), TREATED_UNCARVED_WOOD_PRESSURE_PLATE.get(), TREATED_UNCARVED_WOOD_SLAB.get(), TREATED_UNCARVED_WOOD_STAIRS.get(), TREATED_CHIPBOARD.get(), TREATED_CHIPBOARD_BUTTON.get(), TREATED_CHIPBOARD_FENCE.get(), TREATED_CHIPBOARD_FENCE_GATE.get(), TREATED_CHIPBOARD_PRESSURE_PLATE.get(), TREATED_CHIPBOARD_SLAB.get(), TREATED_CHIPBOARD_STAIRS.get(), TREATED_HEAVY_PLANKS.get(), TREATED_HEAVY_PLANK_SLAB.get(), TREATED_HEAVY_PLANK_STAIRS.get(), TREATED_CARVED_KNOTTED_WOOD.get(), POLISHED_TREATED_UNCARVED_WOOD.get(), POLISHED_TREATED_UNCARVED_SLAB.get(), POLISHED_TREATED_UNCARVED_STAIRS.get(), LACQUERED_UNCARVED_WOOD.get(), LACQUERED_UNCARVED_WOOD_BUTTON.get(), LACQUERED_UNCARVED_WOOD_FENCE.get(), LACQUERED_UNCARVED_WOOD_FENCE_GATE.get(), LACQUERED_UNCARVED_WOOD_PRESSURE_PLATE.get(), LACQUERED_UNCARVED_WOOD_SLAB.get(), LACQUERED_UNCARVED_WOOD_STAIRS.get(), LACQUERED_CHIPBOARD.get(), LACQUERED_CHIPBOARD_BUTTON.get(), LACQUERED_CHIPBOARD_FENCE.get(), LACQUERED_CHIPBOARD_FENCE_GATE.get(), LACQUERED_CHIPBOARD_PRESSURE_PLATE.get(), LACQUERED_CHIPBOARD_SLAB.get(), LACQUERED_CHIPBOARD_STAIRS.get(), LACQUERED_HEAVY_PLANKS.get(), LACQUERED_HEAVY_PLANK_SLAB.get(), LACQUERED_HEAVY_PLANK_STAIRS.get(), LACQUERED_CARVED_KNOTTED_WOOD.get(), POLISHED_LACQUERED_UNCARVED_WOOD.get(), POLISHED_LACQUERED_UNCARVED_SLAB.get(), POLISHED_LACQUERED_UNCARVED_STAIRS.get());
		tag(MINEABLE_WITH_SHOVEL).add(WOOD_SHAVINGS.get(), TREATED_WOOD_SHAVINGS.get(), LACQUERED_WOOD_SHAVINGS.get());
		needsWoodPickaxe(SPIKES.get());
		
		tag(MINEABLE_WITH_AXE).add(GLOWING_LOG.get(), GLOWING_WOOD.get(), GLOWING_PLANKS.get(), GLOWING_BUTTON.get(), GLOWING_FENCE.get(), GLOWING_FENCE_GATE.get(), GLOWING_PRESSURE_PLATE.get(), GLOWING_SLAB.get(), GLOWING_STAIRS.get(),
				SHADEWOOD_LOG.get(), SHADEWOOD.get(), STRIPPED_SHADEWOOD_LOG.get(), STRIPPED_SHADEWOOD.get(), SCARRED_SHADEWOOD_LOG.get(), SCARRED_SHADEWOOD.get(),
				SHADEWOOD_BUTTON.get(), SHADEWOOD_FENCE.get(), SHADEWOOD_FENCE_GATE.get(), SHADEWOOD_PRESSURE_PLATE.get(), SHADEWOOD_SLAB.get(), SHADEWOOD_STAIRS.get(),
				STRIPPED_SCARRED_SHADEWOOD_LOG.get(), STRIPPED_SCARRED_SHADEWOOD.get(), ROTTED_SHADEWOOD_LOG.get(), ROTTED_SHADEWOOD.get(), STRIPPED_ROTTED_SHADEWOOD_LOG.get(), STRIPPED_ROTTED_SHADEWOOD.get(), SHADEWOOD_PLANKS.get());
		tag(MINEABLE_WITH_AXE).add(FROST_LOG.get(), FROST_WOOD.get(), FROST_PLANKS.get(), FROST_BUTTON.get(), FROST_FENCE.get(), FROST_FENCE_GATE.get(), FROST_PRESSURE_PLATE.get(), FROST_SLAB.get(), FROST_STAIRS.get());
		tag(MINEABLE_WITH_AXE).add(RAINBOW_LOG.get(), RAINBOW_WOOD.get(), RAINBOW_PLANKS.get(), RAINBOW_BUTTON.get(), RAINBOW_FENCE.get(), RAINBOW_FENCE_GATE.get(), RAINBOW_PRESSURE_PLATE.get());
		tag(MINEABLE_WITH_AXE).add(END_LOG.get(), END_WOOD.get(), END_PLANKS.get(), END_BUTTON.get(), END_FENCE.get(), END_FENCE_GATE.get(), END_PRESSURE_PLATE.get());
		tag(MINEABLE_WITH_AXE).add(VINE_LOG.get(), VINE_WOOD.get());
		tag(MINEABLE_WITH_AXE).add(FLOWERY_VINE_LOG.get(), FLOWERY_VINE_WOOD.get());
		tag(MINEABLE_WITH_AXE).add(DEAD_LOG.get(), DEAD_WOOD.get(), DEAD_PLANKS.get(), DEAD_BUTTON.get(), DEAD_FENCE.get(), DEAD_FENCE_GATE.get(), DEAD_PRESSURE_PLATE.get());
		tag(MINEABLE_WITH_AXE).add(PETRIFIED_LOG.get(), PETRIFIED_WOOD.get());
		tag(MINEABLE_WITH_AXE).add(CINDERED_LOG.get(), CINDERED_WOOD.get());
		tag(MINEABLE_WITH_AXE).add(GLOWING_BOOKSHELF.get(), FROST_BOOKSHELF.get(), RAINBOW_BOOKSHELF.get(), END_BOOKSHELF.get(), DEAD_BOOKSHELF.get(), TREATED_BOOKSHELF.get());
		tag(MINEABLE_WITH_AXE).add(GLOWING_LADDER.get(), FROST_LADDER.get(), RAINBOW_LADDER.get(), END_LADDER.get(), DEAD_LADDER.get(), TREATED_LADDER.get());
		
		needsWoodPickaxe(PETRIFIED_GRASS.get(), PETRIFIED_POPPY.get());
		tag(MINEABLE_WITH_SHOVEL).add(GLOWY_GOOP.get(), COAGULATED_BLOOD.get());
		needsWoodPickaxe(PIPE.get(), PIPE_INTERSECTION.get(), STONE_TABLET.get());
		
		needsWoodPickaxe(BLACK_CHESS_BRICK_STAIRS.get(), DARK_GRAY_CHESS_BRICK_STAIRS.get(), LIGHT_GRAY_CHESS_BRICK_STAIRS.get(), WHITE_CHESS_BRICK_STAIRS.get());
		needsWoodPickaxe(COARSE_STONE_STAIRS.get(), COARSE_STONE_BRICK_STAIRS.get());
		needsWoodPickaxe(SHADE_STAIRS.get(), SHADE_BRICK_STAIRS.get());
		needsWoodPickaxe(FROST_TILE_STAIRS.get(), FROST_BRICK_STAIRS.get());
		needsWoodPickaxe(CAST_IRON_STAIRS.get());
		needsWoodPickaxe(BLACK_STONE_STAIRS.get(), BLACK_STONE_BRICK_STAIRS.get());
		needsWoodPickaxe(MYCELIUM_STAIRS.get(), MYCELIUM_BRICK_STAIRS.get());
		needsWoodPickaxe(CHALK_STAIRS.get(), CHALK_BRICK_STAIRS.get());
		needsWoodPickaxe(PINK_STONE_STAIRS.get(), PINK_STONE_BRICK_STAIRS.get());
		needsStonePickaxe(BROWN_STONE_STAIRS.get(), BROWN_STONE_BRICK_STAIRS.get());
		needsStonePickaxe(GREEN_STONE_STAIRS.get(), GREEN_STONE_BRICK_STAIRS.get());
		tag(MINEABLE_WITH_AXE).add(RAINBOW_PLANKS_STAIRS.get(), END_PLANKS_STAIRS.get(), DEAD_PLANKS_STAIRS.get(), TREATED_PLANKS_STAIRS.get());
		needsStonePickaxe(STEEP_GREEN_STONE_BRICK_STAIRS_BASE.get(), STEEP_GREEN_STONE_BRICK_STAIRS_TOP.get());
		
		needsWoodPickaxe(BLACK_CHESS_BRICK_SLAB.get(), DARK_GRAY_CHESS_BRICK_SLAB.get(), LIGHT_GRAY_CHESS_BRICK_SLAB.get(), WHITE_CHESS_BRICK_SLAB.get());
		needsWoodPickaxe(CHALK_SLAB.get(), CHALK_BRICK_SLAB.get());
		needsWoodPickaxe(PINK_STONE_SLAB.get(), PINK_STONE_BRICK_SLAB.get());
		needsWoodPickaxe(BLACK_STONE_SLAB.get(), BLACK_STONE_BRICK_SLAB.get());
		needsWoodPickaxe(MYCELIUM_SLAB.get(), MYCELIUM_BRICK_SLAB.get());
		needsWoodPickaxe(FLOWERY_MOSSY_STONE_BRICK_SLAB.get());
		needsWoodPickaxe(FROST_TILE_SLAB.get(), FROST_BRICK_SLAB.get());
		needsWoodPickaxe(SHADE_SLAB.get(), SHADE_BRICK_SLAB.get());
		needsWoodPickaxe(COARSE_STONE_SLAB.get(), COARSE_STONE_BRICK_SLAB.get());
		needsStonePickaxe(BROWN_STONE_SLAB.get(), BROWN_STONE_BRICK_SLAB.get());
		needsStonePickaxe(GREEN_STONE_SLAB.get(), GREEN_STONE_BRICK_SLAB.get());
		tag(MINEABLE_WITH_AXE).add(RAINBOW_PLANKS_SLAB.get(), END_PLANKS_SLAB.get(), DEAD_PLANKS_SLAB.get(), TREATED_PLANKS_SLAB.get());
		
		needsWoodPickaxe(BLOOD_SHADE_BRICK_SLAB.get(), BLOOD_SHADE_BRICK_STAIRS.get(), BLOOD_SHADE_BRICK_WALL.get());
		needsWoodPickaxe(CAST_IRON_BUTTON.get(), CAST_IRON_PRESSURE_PLATE.get(), CAST_IRON_SLAB.get(), CAST_IRON_WALL.get());
		needsWoodPickaxe(CHALK_BRICK_WALL.get(), CHALK_BUTTON.get(), CHALK_PRESSURE_PLATE.get(), CHALK_WALL.get(), FLOWERY_CHALK_BRICK_SLAB.get(), FLOWERY_CHALK_BRICK_STAIRS.get(), FLOWERY_CHALK_BRICK_WALL.get(), MOSSY_CHALK_BRICK_SLAB.get(), MOSSY_CHALK_BRICK_STAIRS.get(), MOSSY_CHALK_BRICK_WALL.get(), POLISHED_CHALK_SLAB.get(), POLISHED_CHALK_STAIRS.get(), POLISHED_CHALK_WALL.get());
		needsWoodPickaxe(COARSE_STONE_BRICK_WALL.get(), COARSE_STONE_BUTTON.get(), COARSE_STONE_PRESSURE_PLATE.get(), COARSE_STONE_WALL.get());
		needsWoodPickaxe(CRUXITE_BUTTON.get(), CRUXITE_PRESSURE_PLATE.get(), CRUXITE_SLAB.get(), CRUXITE_STAIRS.get(), CRUXITE_WALL.get());
		needsWoodPickaxe(DECREPIT_STONE_BRICK_SLAB.get(), DECREPIT_STONE_BRICK_STAIRS.get(), DECREPIT_STONE_BRICK_WALL.get(), MOSSY_DECREPIT_STONE_BRICK_SLAB.get(), MOSSY_DECREPIT_STONE_BRICK_STAIRS.get(), MOSSY_DECREPIT_STONE_BRICK_WALL.get());
		needsWoodPickaxe(FLOWERY_MOSSY_COBBLESTONE_SLAB.get(), FLOWERY_MOSSY_COBBLESTONE_STAIRS.get(), FLOWERY_MOSSY_COBBLESTONE_WALL.get());
		needsWoodPickaxe(MYCELIUM_BRICK_WALL.get(), MYCELIUM_COBBLESTONE_SLAB.get(), MYCELIUM_COBBLESTONE_STAIRS.get(), MYCELIUM_COBBLESTONE_WALL.get(), MYCELIUM_STONE_BUTTON.get(), MYCELIUM_STONE_PRESSURE_PLATE.get(), MYCELIUM_STONE_WALL.get(), FLOWERY_MYCELIUM_BRICK_SLAB.get(), FLOWERY_MYCELIUM_BRICK_STAIRS.get(), FLOWERY_MYCELIUM_BRICK_WALL.get(), MOSSY_MYCELIUM_BRICK_SLAB.get(), MOSSY_MYCELIUM_BRICK_STAIRS.get(), MOSSY_MYCELIUM_BRICK_WALL.get(), POLISHED_MYCELIUM_STONE_SLAB.get(), POLISHED_MYCELIUM_STONE_STAIRS.get(), POLISHED_MYCELIUM_STONE_WALL.get());
		needsWoodPickaxe(FROST_BRICK_WALL.get(), FROST_TILE_WALL.get(), FLOWERY_FROST_BRICK_SLAB.get(), FLOWERY_FROST_BRICK_STAIRS.get(), FLOWERY_FROST_BRICK_WALL.get());
		needsWoodPickaxe(PINK_STONE_BRICK_WALL.get(), PINK_STONE_BUTTON.get(), PINK_STONE_PRESSURE_PLATE.get(), PINK_STONE_WALL.get(), MOSSY_PINK_STONE_BRICK_SLAB.get(), MOSSY_PINK_STONE_BRICK_STAIRS.get(), MOSSY_PINK_STONE_BRICK_WALL.get(), POLISHED_PINK_STONE_SLAB.get(), POLISHED_PINK_STONE_STAIRS.get(), POLISHED_PINK_STONE_WALL.get());
		needsWoodPickaxe(SHADE_BRICK_WALL.get(), SHADE_BUTTON.get(), SHADE_PRESSURE_PLATE.get(), SHADE_WALL.get(), MOSSY_SHADE_BRICK_SLAB.get(), MOSSY_SHADE_BRICK_STAIRS.get(), MOSSY_SHADE_BRICK_WALL.get(), TAR_SHADE_BRICK_SLAB.get(), TAR_SHADE_BRICK_STAIRS.get(), TAR_SHADE_BRICK_WALL.get());
		needsWoodPickaxe(POLISHED_BLACK_STONE_SLAB.get(), POLISHED_BLACK_STONE_STAIRS.get(), POLISHED_BLACK_STONE_WALL.get());
		needsWoodPickaxe(URANIUM_BUTTON.get(), URANIUM_PRESSURE_PLATE.get(), URANIUM_SLAB.get(), URANIUM_STAIRS.get(), URANIUM_WALL.get());
		
		needsWoodPickaxe(TRAJECTORY_BLOCK.get(), STAT_STORER.get(), REMOTE_OBSERVER.get());
		needsWoodPickaxe(WIRELESS_REDSTONE_TRANSMITTER.get(), WIRELESS_REDSTONE_RECEIVER.get());
		needsWoodPickaxe(SOLID_SWITCH.get(), VARIABLE_SOLID_SWITCH.get(), ONE_SECOND_INTERVAL_TIMED_SOLID_SWITCH.get(), TWO_SECOND_INTERVAL_TIMED_SOLID_SWITCH.get());
		needsWoodPickaxe(SUMMONER.get(), AREA_EFFECT_BLOCK.get());
		needsWoodPickaxe(PLATFORM_GENERATOR.get(), PLATFORM_RECEPTACLE.get());
		needsWoodPickaxe(ITEM_MAGNET.get(), REDSTONE_CLOCK.get());
		needsWoodPickaxe(ROTATOR.get(), TOGGLER.get());
		needsWoodPickaxe(REMOTE_COMPARATOR.get(), STRUCTURE_CORE.get(), FALL_PAD.get(), FRAGILE_STONE.get());
		needsWoodPickaxe(RETRACTABLE_SPIKES.get(), BLOCK_PRESSURE_PLATE.get(), PUSHABLE_BLOCK.get());
		needsWoodPickaxe(AND_GATE_BLOCK.get(), OR_GATE_BLOCK.get(), XOR_GATE_BLOCK.get(), NAND_GATE_BLOCK.get(), NOR_GATE_BLOCK.get(), XNOR_GATE_BLOCK.get());
		
		needsWoodPickaxe(CRUXTRUDER_LID.get());
		
		needsWoodPickaxe(MINI_CRUXTRUDER.get(), MINI_TOTEM_LATHE.get(), MINI_ALCHEMITER.get(), MINI_PUNCH_DESIGNIX.get());
		needsWoodPickaxe(HOLOPAD.get());
		needsWoodPickaxe(COMPUTER.get(), LAPTOP.get(), CROCKERTOP.get(), HUBTOP.get(), LUNCHTOP.get(), OLD_COMPUTER.get());
		needsWoodPickaxe(TRANSPORTALIZER.get(), TRANS_PORTALIZER.get());
		needsWoodPickaxe(SENDIFICATOR.get());
		needsWoodPickaxe(GRIST_WIDGET.get());
		needsWoodPickaxe(URANIUM_COOKER.get());
		needsWoodPickaxe(GRIST_COLLECTOR.get());
		needsWoodPickaxe(ANTHVIL.get());
		needsWoodPickaxe(SKAIANET_DENIER.get());
		
		tag(MINEABLE_WITH_AXE).add(WOODEN_CACTUS.get(), MIRROR.get());
		needsWoodPickaxe(BLENDER.get());
		needsWoodPickaxe(CHESSBOARD.get());
		needsWoodPickaxe(MINI_FROG_STATUE.get(), MINI_WIZARD_STATUE.get(), MINI_TYPHEUS_STATUE.get());
		needsWoodPickaxe(CASSETTE_PLAYER.get());
		needsWoodPickaxe(PARCEL_PYXIS.get(), PYXIS_LID.get());
		
		tag(ExtraForgeTags.Blocks.URANIUM_ORES).addTag(URANIUM_ORES);
		tag(ExtraForgeTags.Blocks.URANIUM_STORAGE_BLOCKS).add(URANIUM_BLOCK.get());
		tag(ExtraForgeTags.Blocks.TERRACOTTA).add(Blocks.TERRACOTTA, Blocks.BLACK_GLAZED_TERRACOTTA, Blocks.BLACK_TERRACOTTA, Blocks.BLUE_GLAZED_TERRACOTTA, Blocks.BLUE_TERRACOTTA, Blocks.BROWN_GLAZED_TERRACOTTA, Blocks.BROWN_TERRACOTTA, Blocks.CYAN_GLAZED_TERRACOTTA, Blocks.CYAN_TERRACOTTA, Blocks.GRAY_GLAZED_TERRACOTTA, Blocks.GRAY_TERRACOTTA, Blocks.GREEN_GLAZED_TERRACOTTA, Blocks.GREEN_TERRACOTTA, Blocks.LIGHT_BLUE_GLAZED_TERRACOTTA, Blocks.LIGHT_BLUE_TERRACOTTA, Blocks.LIGHT_GRAY_GLAZED_TERRACOTTA, Blocks.LIGHT_GRAY_TERRACOTTA, Blocks.LIME_GLAZED_TERRACOTTA, Blocks.LIME_TERRACOTTA, Blocks.MAGENTA_GLAZED_TERRACOTTA, Blocks.MAGENTA_TERRACOTTA, Blocks.ORANGE_GLAZED_TERRACOTTA, Blocks.ORANGE_TERRACOTTA, Blocks.PINK_GLAZED_TERRACOTTA, Blocks.PINK_TERRACOTTA, Blocks.PURPLE_GLAZED_TERRACOTTA, Blocks.PURPLE_TERRACOTTA, Blocks.RED_GLAZED_TERRACOTTA, Blocks.RED_TERRACOTTA, Blocks.WHITE_GLAZED_TERRACOTTA, Blocks.WHITE_TERRACOTTA, Blocks.YELLOW_GLAZED_TERRACOTTA, Blocks.YELLOW_TERRACOTTA);
		
		tag(GREEN_HIEROGLYPHS).add(GREEN_STONE_BRICK_FROG.get(), GREEN_STONE_BRICK_LOTUS.get(), GREEN_STONE_BRICK_IGUANA_LEFT.get(), GREEN_STONE_BRICK_IGUANA_RIGHT.get(), GREEN_STONE_BRICK_NAK_LEFT.get(), GREEN_STONE_BRICK_NAK_RIGHT.get(), GREEN_STONE_BRICK_SALAMANDER_LEFT.get(), GREEN_STONE_BRICK_SALAMANDER_RIGHT.get(), GREEN_STONE_BRICK_SKAIA.get(), GREEN_STONE_BRICK_TURTLE.get());
		tag(GLOWING_LOGS).add(GLOWING_LOG.get(), GLOWING_WOOD.get());
		tag(FROST_LOGS).add(FROST_LOG.get(), FROST_WOOD.get());
		tag(RAINBOW_LOGS).add(RAINBOW_LOG.get(), RAINBOW_WOOD.get());
		tag(END_LOGS).add(END_LOG.get(), END_WOOD.get());
		tag(VINE_LOGS).add(VINE_LOG.get(), VINE_WOOD.get());
		tag(FLOWERY_VINE_LOGS).add(FLOWERY_VINE_LOG.get(), FLOWERY_VINE_WOOD.get());
		tag(DEAD_LOGS).add(DEAD_LOG.get(), DEAD_WOOD.get());
		tag(CINDERED_LOGS).add(CINDERED_LOG.get(), CINDERED_WOOD.get());
		tag(PETRIFIED_LOGS).add(PETRIFIED_LOG.get(), PETRIFIED_WOOD.get());
		tag(SHADEWOOD_LOGS).add(SHADEWOOD_LOG.get(), SHADEWOOD.get(), STRIPPED_SHADEWOOD_LOG.get(), STRIPPED_SHADEWOOD.get(), SCARRED_SHADEWOOD_LOG.get(), SCARRED_SHADEWOOD.get(), STRIPPED_SCARRED_SHADEWOOD_LOG.get(), STRIPPED_SCARRED_SHADEWOOD.get(), ROTTED_SHADEWOOD_LOG.get(), ROTTED_SHADEWOOD.get(), STRIPPED_ROTTED_SHADEWOOD_LOG.get(), STRIPPED_ROTTED_SHADEWOOD.get());
		tag(MSTags.Blocks.SHADEWOOD_LEAVES).add(MSBlocks.SHADEWOOD_LEAVES.get(), SHROOMY_SHADEWOOD_LEAVES.get());
		tag(CRUXITE_ORES).add(STONE_CRUXITE_ORE.get(), NETHERRACK_CRUXITE_ORE.get(), COBBLESTONE_CRUXITE_ORE.get(), SANDSTONE_CRUXITE_ORE.get(), RED_SANDSTONE_CRUXITE_ORE.get(), END_STONE_CRUXITE_ORE.get(), PINK_STONE_CRUXITE_ORE.get(), SHADE_STONE_CRUXITE_ORE.get(), MYCELIUM_STONE_CRUXITE_ORE.get(), UNCARVED_WOOD_CRUXITE_ORE.get());
		tag(URANIUM_ORES).add(STONE_URANIUM_ORE.get(), DEEPSLATE_URANIUM_ORE.get(), NETHERRACK_URANIUM_ORE.get(), COBBLESTONE_URANIUM_ORE.get(), SANDSTONE_URANIUM_ORE.get(), RED_SANDSTONE_URANIUM_ORE.get(), END_STONE_URANIUM_ORE.get(), PINK_STONE_URANIUM_ORE.get(), SHADE_STONE_URANIUM_ORE.get(), MYCELIUM_STONE_URANIUM_ORE.get(), UNCARVED_WOOD_URANIUM_ORE.get());
		tag(MSTags.Blocks.COAL_ORES).add(NETHERRACK_COAL_ORE.get(), SHADE_STONE_COAL_ORE.get(), PINK_STONE_COAL_ORE.get());
		tag(MSTags.Blocks.IRON_ORES).add(END_STONE_IRON_ORE.get(), SANDSTONE_IRON_ORE.get(), RED_SANDSTONE_IRON_ORE.get(), UNCARVED_WOOD_IRON_ORE.get());
		tag(MSTags.Blocks.GOLD_ORES).add(SANDSTONE_GOLD_ORE.get(), RED_SANDSTONE_GOLD_ORE.get(), SHADE_STONE_GOLD_ORE.get(), PINK_STONE_GOLD_ORE.get());
		tag(MSTags.Blocks.REDSTONE_ORES).add(END_STONE_REDSTONE_ORE.get(), UNCARVED_WOOD_REDSTONE_ORE.get());
		tag(QUARTZ_ORES).add(STONE_QUARTZ_ORE.get());
		tag(MSTags.Blocks.LAPIS_ORES).add(PINK_STONE_LAPIS_ORE.get());
		tag(MSTags.Blocks.DIAMOND_ORES).add(PINK_STONE_DIAMOND_ORE.get());
		tag(MSTags.Blocks.EMERALD_ORES).add(UNCARVED_WOOD_EMERALD_ORE.get());
		tag(CRUXITE_STORAGE_BLOCKS).add(CRUXITE_BLOCK.get());
		tag(END_SAPLING_DIRT).addTag(Tags.Blocks.END_STONES).add(END_GRASS.get());
		tag(WALLS).add(PERFECTLY_GENERIC_WALL.get(), BLACK_CHESS_BRICK_WALL.get(), DARK_GRAY_CHESS_BRICK_WALL.get(), LIGHT_GRAY_CHESS_BRICK_WALL.get(), WHITE_CHESS_BRICK_WALL.get(), BLACK_CHESS_BRICK_SMOOTH_WALL.get(), DARK_GRAY_CHESS_BRICK_SMOOTH_WALL.get(), LIGHT_GRAY_CHESS_BRICK_SMOOTH_WALL.get(), WHITE_CHESS_BRICK_SMOOTH_WALL.get(), CRUXITE_WALL.get(), URANIUM_WALL.get(), COARSE_STONE_WALL.get(), COARSE_STONE_BRICK_WALL.get(), SHADE_WALL.get(), SHADE_BRICK_WALL.get(), SMOOTH_SHADE_STONE_WALL.get(), MOSSY_SHADE_BRICK_WALL.get(), BLOOD_SHADE_BRICK_WALL.get(), TAR_SHADE_BRICK_WALL.get(), FROST_TILE_WALL.get(), FROST_BRICK_WALL.get(), FLOWERY_FROST_BRICK_WALL.get(), CAST_IRON_WALL.get(), MYCELIUM_STONE_WALL.get(), MYCELIUM_COBBLESTONE_WALL.get(), POLISHED_MYCELIUM_STONE_WALL.get(), MOSSY_MYCELIUM_BRICK_WALL.get(), FLOWERY_MYCELIUM_BRICK_WALL.get(), BLACK_STONE_WALL.get(), BLACK_COBBLESTONE_WALL.get(), POLISHED_BLACK_STONE_WALL.get(), BLACK_STONE_BRICK_WALL.get(), FLOWERY_MOSSY_COBBLESTONE_WALL.get(), CHALK_WALL.get(), POLISHED_CHALK_WALL.get(), CHALK_BRICK_WALL.get(), MOSSY_CHALK_BRICK_WALL.get(), FLOWERY_CHALK_BRICK_WALL.get(), PINK_STONE_WALL.get(), POLISHED_PINK_STONE_WALL.get(), PINK_STONE_BRICK_WALL.get(), MOSSY_PINK_STONE_BRICK_WALL.get(), BROWN_STONE_WALL.get(), POLISHED_BROWN_STONE_WALL.get(), BROWN_STONE_BRICK_WALL.get(), GREEN_STONE_WALL.get(), POLISHED_GREEN_STONE_WALL.get(), GREEN_STONE_BRICK_WALL.get(), HORIZONTAL_GREEN_STONE_BRICK_WALL.get(), VERTICAL_GREEN_STONE_BRICK_WALL.get(), FLOWERY_MOSSY_STONE_BRICK_WALL.get());
		tag(FENCES).add(PERFECTLY_GENERIC_FENCE.get(), GLOWING_FENCE.get(), UNCARVED_WOOD_FENCE.get(), CHIPBOARD_FENCE.get(), CARVED_FENCE.get(), TREATED_UNCARVED_WOOD_FENCE.get(), TREATED_CHIPBOARD_FENCE.get(), TREATED_FENCE.get(), LACQUERED_UNCARVED_WOOD_FENCE.get(), LACQUERED_CHIPBOARD_FENCE.get(), LACQUERED_FENCE.get(), FROST_FENCE.get(), RAINBOW_FENCE.get(), END_FENCE.get(), DEAD_FENCE.get(), SHADEWOOD_FENCE.get());
		tag(FENCE_GATES).add(PERFECTLY_GENERIC_FENCE_GATE.get(), GLOWING_FENCE_GATE.get(), UNCARVED_WOOD_FENCE_GATE.get(), CHIPBOARD_FENCE_GATE.get(), CARVED_FENCE_GATE.get(), TREATED_UNCARVED_WOOD_FENCE_GATE.get(), TREATED_CHIPBOARD_FENCE_GATE.get(), TREATED_FENCE_GATE.get(), LACQUERED_UNCARVED_WOOD_FENCE_GATE.get(), LACQUERED_CHIPBOARD_FENCE_GATE.get(), LACQUERED_FENCE_GATE.get(), FROST_FENCE_GATE.get(), RAINBOW_FENCE_GATE.get(), END_FENCE_GATE.get(), DEAD_FENCE_GATE.get(), SHADEWOOD_FENCE_GATE.get());
		tag(PRESSURE_PLATES).add(PERFECTLY_GENERIC_PRESSURE_PLATE.get(), GLOWING_PRESSURE_PLATE.get(), UNCARVED_WOOD_PRESSURE_PLATE.get(), CHIPBOARD_PRESSURE_PLATE.get(), CARVED_PRESSURE_PLATE.get(), TREATED_UNCARVED_WOOD_PRESSURE_PLATE.get(), TREATED_CHIPBOARD_PRESSURE_PLATE.get(), TREATED_PRESSURE_PLATE.get(), LACQUERED_UNCARVED_WOOD_PRESSURE_PLATE.get(), LACQUERED_CHIPBOARD_PRESSURE_PLATE.get(), LACQUERED_PRESSURE_PLATE.get(), FROST_PRESSURE_PLATE.get(), RAINBOW_PRESSURE_PLATE.get(), END_PRESSURE_PLATE.get(), DEAD_PRESSURE_PLATE.get(), SHADEWOOD_PRESSURE_PLATE.get());
		tag(DOORS).add(PERFECTLY_GENERIC_DOOR.get());
		tag(TRAPDOORS).add(PERFECTLY_GENERIC_TRAPDOOR.get());
		
		tag(ROTATOR_WHITELISTED).add(Blocks.REPEATER, Blocks.COMPARATOR, AND_GATE_BLOCK.get(), OR_GATE_BLOCK.get(), XOR_GATE_BLOCK.get(), NAND_GATE_BLOCK.get(), NOR_GATE_BLOCK.get(), XNOR_GATE_BLOCK.get(), AREA_EFFECT_BLOCK.get(), WIRELESS_REDSTONE_TRANSMITTER.get(), REMOTE_COMPARATOR.get(), PLATFORM_GENERATOR.get(), ITEM_MAGNET.get());
		tag(PLATFORM_ABSORBING).addTag(Tags.Blocks.OBSIDIAN).add(Blocks.BEDROCK, Blocks.NETHER_PORTAL, Blocks.END_PORTAL, Blocks.END_PORTAL_FRAME, Blocks.REINFORCED_DEEPSLATE, PUSHABLE_BLOCK.get()); //excludes Platform Receptacle blocks as they only absorb conditionally
		tag(PUSHABLE_BLOCK_REPLACEABLE).addTags(SAPLINGS, FLOWERS);
		tag(PETRIFIED_FLORA_PLACEABLE).addTags(Tags.Blocks.STONE, Tags.Blocks.COBBLESTONE, Tags.Blocks.GRAVEL);
		tag(EDITMODE_BREAK_BLACKLIST).addTags(BlockTags.PORTALS);
	}
	
	@Override
	public IntrinsicTagAppender<Block> tag(TagKey<Block> tag)
	{
		return super.tag(tag);
	}
	
	private void needsWoodPickaxe(Block... blocks)
	{
		assertRequiresTool(blocks);
		tag(MINEABLE_WITH_PICKAXE).add(blocks);
		tag(Tags.Blocks.NEEDS_WOOD_TOOL).add(blocks);
	}
	
	private void needsStonePickaxe(Block... blocks)
	{
		assertRequiresTool(blocks);
		tag(MINEABLE_WITH_PICKAXE).add(blocks);
		tag(NEEDS_STONE_TOOL).add(blocks);
	}
	
	private void needsIronPickaxe(Block... blocks)
	{
		assertRequiresTool(blocks);
		tag(MINEABLE_WITH_PICKAXE).add(blocks);
		tag(NEEDS_IRON_TOOL).add(blocks);
	}
	
	private void assertRequiresTool(Block... blocks)
	{
		for(Block block : blocks)
		{
			if(!block.defaultBlockState().requiresCorrectToolForDrops())
				throw new IllegalStateException("You forgot to set requiresCorrectToolForDrops for block %s. It is needed to prevent drops when mining without any tool.".formatted(block));
		}
	}
	
	@Override
	public String getName()
	{
		return "Minestuck Block Tags";
	}
}
