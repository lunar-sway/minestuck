package com.mraof.minestuck.data;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.util.ExtraForgeTags;
import com.mraof.minestuck.util.MSTags;
import net.minecraft.block.Blocks;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;

import static com.mraof.minestuck.block.MSBlocks.*;
import static com.mraof.minestuck.util.MSTags.Blocks.*;
import static net.minecraft.tags.BlockTags.*;

public class MinestuckBlockTagsProvider extends BlockTagsProvider
{
	public MinestuckBlockTagsProvider(DataGenerator generator, @Nullable ExistingFileHelper existingFileHelper)
	{
		super(generator, Minestuck.MOD_ID, existingFileHelper);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	protected void addTags()
	{
		tag(PLANKS).add(GLOWING_PLANKS, FROST_PLANKS, RAINBOW_PLANKS, END_PLANKS, DEAD_PLANKS, TREATED_PLANKS).addTag(ASPECT_PLANKS);
		tag(STONE_BRICKS).add(FLOWERY_MOSSY_STONE_BRICKS);
		tag(WOODEN_BUTTONS).add(WOODEN_EXPLOSIVE_BUTTON);
		tag(BUTTONS).add(STONE_EXPLOSIVE_BUTTON);
		tag(WOODEN_STAIRS).add(RAINBOW_PLANKS_STAIRS, END_PLANKS_STAIRS, DEAD_PLANKS_STAIRS, TREATED_PLANKS_STAIRS);
		tag(WOODEN_SLABS).add(RAINBOW_PLANKS_SLAB, END_PLANKS_SLAB, DEAD_PLANKS_SLAB, TREATED_PLANKS_SLAB);
		tag(SAPLINGS).add(RAINBOW_SAPLING, END_SAPLING).addTag(ASPECT_SAPLINGS);
		tag(LOGS).addTags(GLOWING_LOGS, FROST_LOGS, RAINBOW_LOGS, END_LOGS, VINE_LOGS, FLOWERY_VINE_LOGS, DEAD_LOGS, PETRIFIED_LOGS, ASPECT_LOGS);
		tag(ENDERMAN_HOLDABLE).add(BLUE_DIRT, THOUGHT_DIRT);
		tag(STAIRS).add(BLACK_CASTLE_BRICK_STAIRS, DARK_GRAY_CASTLE_BRICK_STAIRS, LIGHT_GRAY_CASTLE_BRICK_STAIRS, WHITE_CASTLE_BRICK_STAIRS, COARSE_STONE_STAIRS, SHADE_BRICK_STAIRS, FROST_BRICK_STAIRS, CAST_IRON_STAIRS, MYCELIUM_BRICK_STAIRS, CHALK_STAIRS, CHALK_BRICK_STAIRS, PINK_STONE_BRICK_STAIRS, BROWN_STONE_BRICK_STAIRS, GREEN_STONE_BRICK_STAIRS, FLOWERY_MOSSY_STONE_BRICK_STAIRS);
		tag(SLABS).add(COARSE_STONE_SLAB, BLACK_CASTLE_BRICK_SLAB, DARK_GRAY_CASTLE_BRICK_SLAB, LIGHT_GRAY_CASTLE_BRICK_SLAB, WHITE_CASTLE_BRICK_SLAB, CHALK_SLAB, CHALK_BRICK_SLAB, PINK_STONE_BRICK_SLAB, BROWN_STONE_BRICK_SLAB, GREEN_STONE_BRICK_SLAB, MYCELIUM_BRICK_SLAB, FLOWERY_MOSSY_STONE_BRICK_SLAB, FROST_BRICK_SLAB, SHADE_BRICK_SLAB);
		tag(LEAVES).add(FROST_LEAVES, RAINBOW_LEAVES, END_LEAVES).addTag(ASPECT_LEAVES);
		tag(Tags.Blocks.COBBLESTONE).add(FLOWERY_MOSSY_COBBLESTONE);
		tag(Tags.Blocks.END_STONES).add(COARSE_END_STONE);
		tag(Tags.Blocks.ORES).addTags(CRUXITE_ORES, ExtraForgeTags.Blocks.URANIUM_ORES);
		tag(Tags.Blocks.ORES_COAL).addTag(COAL_ORES);
		tag(Tags.Blocks.ORES_DIAMOND).addTag(DIAMOND_ORES);
		tag(Tags.Blocks.ORES_GOLD).addTag(MSTags.Blocks.GOLD_ORES);
		tag(Tags.Blocks.ORES_IRON).addTag(IRON_ORES);
		tag(Tags.Blocks.ORES_LAPIS).addTag(LAPIS_ORES);
		tag(Tags.Blocks.ORES_QUARTZ).addTag(QUARTZ_ORES);
		tag(Tags.Blocks.ORES_REDSTONE).addTag(REDSTONE_ORES);
		tag(Tags.Blocks.STONE).add(COARSE_STONE, SHADE_STONE, BLACK_STONE, COARSE_END_STONE, PINK_STONE, BROWN_STONE, GREEN_STONE);
		tag(Tags.Blocks.STORAGE_BLOCKS).addTags(CRUXITE_STORAGE_BLOCKS, ExtraForgeTags.Blocks.URANIUM_STORAGE_BLOCKS);
		
		tag(ExtraForgeTags.Blocks.URANIUM_ORES).addTag(URANIUM_ORES);
		tag(ExtraForgeTags.Blocks.URANIUM_STORAGE_BLOCKS).add(URANIUM_BLOCK);
		tag(ExtraForgeTags.Blocks.TERRACOTTA).add(Blocks.TERRACOTTA, Blocks.BLACK_GLAZED_TERRACOTTA, Blocks.BLACK_TERRACOTTA, Blocks.BLUE_GLAZED_TERRACOTTA, Blocks.BLUE_TERRACOTTA, Blocks.BROWN_GLAZED_TERRACOTTA, Blocks.BROWN_TERRACOTTA, Blocks.CYAN_GLAZED_TERRACOTTA, Blocks.CYAN_TERRACOTTA, Blocks.GRAY_GLAZED_TERRACOTTA, Blocks.GRAY_TERRACOTTA, Blocks.GREEN_GLAZED_TERRACOTTA, Blocks.GREEN_TERRACOTTA, Blocks.LIGHT_BLUE_GLAZED_TERRACOTTA, Blocks.LIGHT_BLUE_TERRACOTTA, Blocks.LIGHT_GRAY_GLAZED_TERRACOTTA, Blocks.LIGHT_GRAY_TERRACOTTA, Blocks.LIME_GLAZED_TERRACOTTA, Blocks.LIME_TERRACOTTA, Blocks.MAGENTA_GLAZED_TERRACOTTA, Blocks.MAGENTA_TERRACOTTA, Blocks.ORANGE_GLAZED_TERRACOTTA, Blocks.ORANGE_TERRACOTTA, Blocks.PINK_GLAZED_TERRACOTTA, Blocks.PINK_TERRACOTTA, Blocks.PURPLE_GLAZED_TERRACOTTA, Blocks.PURPLE_TERRACOTTA, Blocks.RED_GLAZED_TERRACOTTA, Blocks.RED_TERRACOTTA, Blocks.WHITE_GLAZED_TERRACOTTA, Blocks.WHITE_TERRACOTTA, Blocks.YELLOW_GLAZED_TERRACOTTA, Blocks.YELLOW_TERRACOTTA);
		
		tag(GLOWING_LOGS).add(GLOWING_LOG, GLOWING_WOOD);
		tag(FROST_LOGS).add(FROST_LOG, FROST_WOOD);
		tag(RAINBOW_LOGS).add(RAINBOW_LOG, RAINBOW_WOOD);
		tag(END_LOGS).add(END_LOG, END_WOOD);
		tag(VINE_LOGS).add(VINE_LOG, VINE_WOOD);
		tag(FLOWERY_VINE_LOGS).add(FLOWERY_VINE_LOG, FLOWERY_VINE_WOOD);
		tag(DEAD_LOGS).add(DEAD_LOG, DEAD_WOOD);
		tag(PETRIFIED_LOGS).add(PETRIFIED_LOG, PETRIFIED_WOOD);
		tag(ASPECT_LOGS).add(BLOOD_ASPECT_LOG, BREATH_ASPECT_LOG, DOOM_ASPECT_LOG, HEART_ASPECT_LOG, HOPE_ASPECT_LOG, LIFE_ASPECT_LOG, LIGHT_ASPECT_LOG, MIND_ASPECT_LOG, RAGE_ASPECT_LOG, SPACE_ASPECT_LOG, TIME_ASPECT_LOG, VOID_ASPECT_LOG);
		tag(ASPECT_PLANKS).add(BLOOD_ASPECT_PLANKS, BREATH_ASPECT_PLANKS, DOOM_ASPECT_PLANKS, HEART_ASPECT_PLANKS, HOPE_ASPECT_PLANKS, LIFE_ASPECT_PLANKS, LIGHT_ASPECT_PLANKS, MIND_ASPECT_PLANKS, RAGE_ASPECT_PLANKS, SPACE_ASPECT_PLANKS, TIME_ASPECT_PLANKS, VOID_ASPECT_PLANKS);
		tag(ASPECT_LEAVES).add(BLOOD_ASPECT_LEAVES, BREATH_ASPECT_LEAVES, DOOM_ASPECT_LEAVES, HEART_ASPECT_LEAVES, HOPE_ASPECT_LEAVES, LIFE_ASPECT_LEAVES, LIGHT_ASPECT_LEAVES, MIND_ASPECT_LEAVES, RAGE_ASPECT_LEAVES, SPACE_ASPECT_LEAVES, TIME_ASPECT_LEAVES, VOID_ASPECT_LEAVES);
		tag(ASPECT_SAPLINGS).add(BLOOD_ASPECT_SAPLING, BREATH_ASPECT_SAPLING, DOOM_ASPECT_SAPLING, HEART_ASPECT_SAPLING, HOPE_ASPECT_SAPLING, LIFE_ASPECT_SAPLING, LIGHT_ASPECT_SAPLING, MIND_ASPECT_SAPLING, RAGE_ASPECT_SAPLING, SPACE_ASPECT_SAPLING, TIME_ASPECT_SAPLING, VOID_ASPECT_SAPLING);
		tag(CRUXITE_ORES).add(STONE_CRUXITE_ORE, NETHERRACK_CRUXITE_ORE, COBBLESTONE_CRUXITE_ORE, SANDSTONE_CRUXITE_ORE, RED_SANDSTONE_CRUXITE_ORE, END_STONE_CRUXITE_ORE, PINK_STONE_CRUXITE_ORE, SHADE_STONE_CRUXITE_ORE);
		tag(URANIUM_ORES).add(STONE_URANIUM_ORE, NETHERRACK_URANIUM_ORE, COBBLESTONE_URANIUM_ORE, SANDSTONE_URANIUM_ORE, RED_SANDSTONE_URANIUM_ORE, END_STONE_URANIUM_ORE, PINK_STONE_URANIUM_ORE, SHADE_STONE_URANIUM_ORE);
		tag(COAL_ORES).add(NETHERRACK_COAL_ORE, SHADE_STONE_COAL_ORE, PINK_STONE_COAL_ORE);
		tag(IRON_ORES).add(END_STONE_IRON_ORE, SANDSTONE_IRON_ORE, RED_SANDSTONE_IRON_ORE);
		tag(MSTags.Blocks.GOLD_ORES).add(SANDSTONE_GOLD_ORE, RED_SANDSTONE_GOLD_ORE, SHADE_STONE_GOLD_ORE, PINK_STONE_GOLD_ORE);
		tag(REDSTONE_ORES).add(END_STONE_REDSTONE_ORE);
		tag(QUARTZ_ORES).add(STONE_QUARTZ_ORE);
		tag(LAPIS_ORES).add(PINK_STONE_LAPIS_ORE);
		tag(DIAMOND_ORES).add(PINK_STONE_DIAMOND_ORE);
		tag(CRUXITE_STORAGE_BLOCKS).add(CRUXITE_BLOCK);
		tag(END_SAPLING_DIRT).addTag(Tags.Blocks.END_STONES).add(END_GRASS);
		tag(RULE_EXEMPT_ROTATABLE).add(AND_GATE_BLOCK, OR_GATE_BLOCK, XOR_GATE_BLOCK, NAND_GATE_BLOCK, NOR_GATE_BLOCK, XNOR_GATE_BLOCK);
		tag(PLATFORM_ABSORBING).addTag(Tags.Blocks.OBSIDIAN).add(Blocks.BEDROCK, Blocks.NETHER_PORTAL, Blocks.END_PORTAL, Blocks.END_PORTAL_FRAME, PORTABLE_BLOCK); //excludes Platform Receptacle blocks as they only absorb conditionally
	}
	
	@Override
	public String getName()
	{
		return "Minestuck Block Tags";
	}
}
