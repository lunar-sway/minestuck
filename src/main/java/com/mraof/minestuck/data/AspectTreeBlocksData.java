package com.mraof.minestuck.data;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.data.loot_table.MSBlockLootTables;
import com.mraof.minestuck.data.recipe.CommonRecipes;
import com.mraof.minestuck.data.tag.MinestuckBlockTagsProvider;
import com.mraof.minestuck.util.MSTags;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.Tags;

import static com.mraof.minestuck.block.AspectTreeBlocks.*;
import static com.mraof.minestuck.data.MSBlockStateProvider.texture;
import static com.mraof.minestuck.data.recipe.MinestuckRecipeProvider.has;

public final class AspectTreeBlocksData
{
	public static void addEnUsTranslations(MinestuckLanguageProvider provider)
	{
		provider.addBlock(BLOOD_ASPECT_LOG.blockHolder(), "Blood Log");
		provider.addBlock(BLOOD_ASPECT_WOOD.blockHolder(), "Blood Wood");
		provider.addBlock(BLOOD_ASPECT_STRIPPED_LOG.blockHolder(), "Stripped Blood Log");
		provider.addBlock(BLOOD_ASPECT_STRIPPED_WOOD.blockHolder(), "Stripped Blood Wood");
		provider.addBlock(BLOOD_ASPECT_LEAVES.blockHolder(), "Blood Leaves");
		provider.addBlock(BLOOD_ASPECT_SAPLING.blockHolder(), "Blood Sapling");
		provider.addBlock(BLOOD_ASPECT_PLANKS.blockHolder(), "Blood Planks");
		provider.addBlock(BLOOD_ASPECT_CARVED_PLANKS.blockHolder(), "Carved Blood Planks");
		provider.addBlock(BLOOD_ASPECT_STAIRS.blockHolder(), "Blood Stairs");
		provider.addBlock(BLOOD_ASPECT_SLAB.blockHolder(), "Blood Slab");
		provider.addBlock(BLOOD_ASPECT_FENCE.blockHolder(), "Blood Fence");
		provider.addBlock(BLOOD_ASPECT_FENCE_GATE.blockHolder(), "Blood Fence Gate");
		provider.addBlock(BLOOD_ASPECT_DOOR.blockHolder(), "Blood Door");
		provider.addBlock(BLOOD_ASPECT_TRAPDOOR.blockHolder(), "Blood Trapdoor");
		provider.addBlock(BLOOD_ASPECT_PRESSURE_PLATE.blockHolder(), "Blood Pressure Plate");
		provider.addBlock(BLOOD_ASPECT_BUTTON.blockHolder(), "Blood Button");
		provider.addBlock(BLOOD_ASPECT_BOOKSHELF.blockHolder(), "Blood Bookshelf");
		provider.addBlock(BLOOD_ASPECT_LADDER.blockHolder(), "Blood Ladder");
		provider.addBlock(BLOOD_ASPECT_SIGN, "Blood Sign");
		provider.addBlock(BLOOD_ASPECT_HANGING_SIGN, "Blood Sign");
		
		provider.addBlock(BREATH_ASPECT_LOG.blockHolder(), "Breath Log");
		provider.addBlock(BREATH_ASPECT_WOOD.blockHolder(), "Breath Wood");
		provider.addBlock(BREATH_ASPECT_STRIPPED_LOG.blockHolder(), "Stripped Breath Log");
		provider.addBlock(BREATH_ASPECT_STRIPPED_WOOD.blockHolder(), "Stripped Breath Wood");
		provider.addBlock(BREATH_ASPECT_LEAVES.blockHolder(), "Breath Leaves");
		provider.addBlock(BREATH_ASPECT_SAPLING.blockHolder(), "Breath Sapling");
		provider.addBlock(BREATH_ASPECT_PLANKS.blockHolder(), "Breath Planks");
		provider.addBlock(BREATH_ASPECT_CARVED_PLANKS.blockHolder(), "Carved Breath Planks");
		provider.addBlock(BREATH_ASPECT_STAIRS.blockHolder(), "Breath Stairs");
		provider.addBlock(BREATH_ASPECT_SLAB.blockHolder(), "Breath Slab");
		provider.addBlock(BREATH_ASPECT_FENCE.blockHolder(), "Breath Fence");
		provider.addBlock(BREATH_ASPECT_FENCE_GATE.blockHolder(), "Breath Fence Gate");
		provider.addBlock(BREATH_ASPECT_DOOR.blockHolder(), "Breath Door");
		provider.addBlock(BREATH_ASPECT_TRAPDOOR.blockHolder(), "Breath Trapdoor");
		provider.addBlock(BREATH_ASPECT_PRESSURE_PLATE.blockHolder(), "Breath Pressure Plate");
		provider.addBlock(BREATH_ASPECT_BUTTON.blockHolder(), "Breath Button");
		provider.addBlock(BREATH_ASPECT_BOOKSHELF.blockHolder(), "Breath Bookshelf");
		provider.addBlock(BREATH_ASPECT_LADDER.blockHolder(), "Breath Ladder");
		provider.addBlock(BREATH_ASPECT_SIGN, "Breath Sign");
		provider.addBlock(BREATH_ASPECT_HANGING_SIGN, "Breath Sign");
		
		provider.addBlock(DOOM_ASPECT_LOG.blockHolder(), "Doom Log");
		provider.addBlock(DOOM_ASPECT_WOOD.blockHolder(), "Doom Wood");
		provider.addBlock(DOOM_ASPECT_STRIPPED_LOG.blockHolder(), "Stripped Doom Log");
		provider.addBlock(DOOM_ASPECT_STRIPPED_WOOD.blockHolder(), "Stripped Doom Wood");
		provider.addBlock(DOOM_ASPECT_LEAVES.blockHolder(), "Doom Leaves");
		provider.addBlock(DOOM_ASPECT_SAPLING.blockHolder(), "Doom Sapling");
		provider.addBlock(DOOM_ASPECT_PLANKS.blockHolder(), "Doom Planks");
		provider.addBlock(DOOM_ASPECT_CARVED_PLANKS.blockHolder(), "Carved Doom Planks");
		provider.addBlock(DOOM_ASPECT_STAIRS.blockHolder(), "Doom Stairs");
		provider.addBlock(DOOM_ASPECT_SLAB.blockHolder(), "Doom Slab");
		provider.addBlock(DOOM_ASPECT_FENCE.blockHolder(), "Doom Fence");
		provider.addBlock(DOOM_ASPECT_FENCE_GATE.blockHolder(), "Doom Fence Gate");
		provider.addBlock(DOOM_ASPECT_DOOR.blockHolder(), "Doom Door");
		provider.addBlock(DOOM_ASPECT_TRAPDOOR.blockHolder(), "Doom Trapdoor");
		provider.addBlock(DOOM_ASPECT_PRESSURE_PLATE.blockHolder(), "Doom Pressure Plate");
		provider.addBlock(DOOM_ASPECT_BUTTON.blockHolder(), "Doom Button");
		provider.addBlock(DOOM_ASPECT_BOOKSHELF.blockHolder(), "Doom Bookshelf");
		provider.addBlock(DOOM_ASPECT_LADDER.blockHolder(), "Doom Ladder");
		provider.addBlock(DOOM_ASPECT_SIGN, "Doom Sign");
		provider.addBlock(DOOM_ASPECT_HANGING_SIGN, "Doom Sign");
		
		provider.addBlock(HEART_ASPECT_LOG.blockHolder(), "Heart Log");
		provider.addBlock(HEART_ASPECT_WOOD.blockHolder(), "Heart Wood");
		provider.addBlock(HEART_ASPECT_STRIPPED_LOG.blockHolder(), "Stripped Heart Log");
		provider.addBlock(HEART_ASPECT_STRIPPED_WOOD.blockHolder(), "Stripped Heart Wood");
		provider.addBlock(HEART_ASPECT_LEAVES.blockHolder(), "Heart Leaves");
		provider.addBlock(HEART_ASPECT_SAPLING.blockHolder(), "Heart Sapling");
		provider.addBlock(HEART_ASPECT_PLANKS.blockHolder(), "Heart Planks");
		provider.addBlock(HEART_ASPECT_CARVED_PLANKS.blockHolder(), "Carved Heart Planks");
		provider.addBlock(HEART_ASPECT_STAIRS.blockHolder(), "Heart Stairs");
		provider.addBlock(HEART_ASPECT_SLAB.blockHolder(), "Heart Slab");
		provider.addBlock(HEART_ASPECT_FENCE.blockHolder(), "Heart Fence");
		provider.addBlock(HEART_ASPECT_FENCE_GATE.blockHolder(), "Heart Fence Gate");
		provider.addBlock(HEART_ASPECT_DOOR.blockHolder(), "Heart Door");
		provider.addBlock(HEART_ASPECT_TRAPDOOR.blockHolder(), "Heart Trapdoor");
		provider.addBlock(HEART_ASPECT_PRESSURE_PLATE.blockHolder(), "Heart Pressure Plate");
		provider.addBlock(HEART_ASPECT_BUTTON.blockHolder(), "Heart Button");
		provider.addBlock(HEART_ASPECT_BOOKSHELF.blockHolder(), "Heart Bookshelf");
		provider.addBlock(HEART_ASPECT_LADDER.blockHolder(), "Heart Ladder");
		provider.addBlock(HEART_ASPECT_SIGN, "Heart Sign");
		provider.addBlock(HEART_ASPECT_HANGING_SIGN, "Heart Sign");
		
		provider.addBlock(HOPE_ASPECT_LOG.blockHolder(), "Hope Log");
		provider.addBlock(HOPE_ASPECT_WOOD.blockHolder(), "Hope Wood");
		provider.addBlock(HOPE_ASPECT_STRIPPED_LOG.blockHolder(), "Stripped Hope Log");
		provider.addBlock(HOPE_ASPECT_STRIPPED_WOOD.blockHolder(), "Stripped Hope Wood");
		provider.addBlock(HOPE_ASPECT_LEAVES.blockHolder(), "Hope Leaves");
		provider.addBlock(HOPE_ASPECT_SAPLING.blockHolder(), "Hope Sapling");
		provider.addBlock(HOPE_ASPECT_PLANKS.blockHolder(), "Hope Planks");
		provider.addBlock(HOPE_ASPECT_CARVED_PLANKS.blockHolder(), "Carved Hope Planks");
		provider.addBlock(HOPE_ASPECT_STAIRS.blockHolder(), "Hope Stairs");
		provider.addBlock(HOPE_ASPECT_SLAB.blockHolder(), "Hope Slab");
		provider.addBlock(HOPE_ASPECT_FENCE.blockHolder(), "Hope Fence");
		provider.addBlock(HOPE_ASPECT_FENCE_GATE.blockHolder(), "Hope Fence Gate");
		provider.addBlock(HOPE_ASPECT_DOOR.blockHolder(), "Hope Door");
		provider.addBlock(HOPE_ASPECT_TRAPDOOR.blockHolder(), "Hope Trapdoor");
		provider.addBlock(HOPE_ASPECT_PRESSURE_PLATE.blockHolder(), "Hope Pressure Plate");
		provider.addBlock(HOPE_ASPECT_BUTTON.blockHolder(), "Hope Button");
		provider.addBlock(HOPE_ASPECT_BOOKSHELF.blockHolder(), "Hope Bookshelf");
		provider.addBlock(HOPE_ASPECT_LADDER.blockHolder(), "Hope Ladder");
		provider.addBlock(HOPE_ASPECT_SIGN, "Hope Sign");
		provider.addBlock(HOPE_ASPECT_HANGING_SIGN, "Hope Sign");
		
		provider.addBlock(LIFE_ASPECT_LOG.blockHolder(), "Life Log");
		provider.addBlock(LIFE_ASPECT_WOOD.blockHolder(), "Life Wood");
		provider.addBlock(LIFE_ASPECT_STRIPPED_LOG.blockHolder(), "Stripped Life Log");
		provider.addBlock(LIFE_ASPECT_STRIPPED_WOOD.blockHolder(), "Stripped Life Wood");
		provider.addBlock(LIFE_ASPECT_LEAVES.blockHolder(), "Life Leaves");
		provider.addBlock(LIFE_ASPECT_SAPLING.blockHolder(), "Life Sapling");
		provider.addBlock(LIFE_ASPECT_PLANKS.blockHolder(), "Life Planks");
		provider.addBlock(LIFE_ASPECT_CARVED_PLANKS.blockHolder(), "Carved Life Planks");
		provider.addBlock(LIFE_ASPECT_STAIRS.blockHolder(), "Life Stairs");
		provider.addBlock(LIFE_ASPECT_SLAB.blockHolder(), "Life Slab");
		provider.addBlock(LIFE_ASPECT_FENCE.blockHolder(), "Life Fence");
		provider.addBlock(LIFE_ASPECT_FENCE_GATE.blockHolder(), "Life Fence Gate");
		provider.addBlock(LIFE_ASPECT_DOOR.blockHolder(), "Life Door");
		provider.addBlock(LIFE_ASPECT_TRAPDOOR.blockHolder(), "Life Trapdoor");
		provider.addBlock(LIFE_ASPECT_PRESSURE_PLATE.blockHolder(), "Life Pressure Plate");
		provider.addBlock(LIFE_ASPECT_BUTTON.blockHolder(), "Life Button");
		provider.addBlock(LIFE_ASPECT_BOOKSHELF.blockHolder(), "Life Bookshelf");
		provider.addBlock(LIFE_ASPECT_LADDER.blockHolder(), "Life Ladder");
		provider.addBlock(LIFE_ASPECT_SIGN, "Life Sign");
		provider.addBlock(LIFE_ASPECT_HANGING_SIGN, "Life Sign");
		
		provider.addBlock(LIGHT_ASPECT_LOG.blockHolder(), "Light Log");
		provider.addBlock(LIGHT_ASPECT_WOOD.blockHolder(), "Light Wood");
		provider.addBlock(LIGHT_ASPECT_STRIPPED_LOG.blockHolder(), "Stripped Light Log");
		provider.addBlock(LIGHT_ASPECT_STRIPPED_WOOD.blockHolder(), "Stripped Light Wood");
		provider.addBlock(LIGHT_ASPECT_LEAVES.blockHolder(), "Light Leaves");
		provider.addBlock(LIGHT_ASPECT_SAPLING.blockHolder(), "Light Sapling");
		provider.addBlock(LIGHT_ASPECT_PLANKS.blockHolder(), "Light Planks");
		provider.addBlock(LIGHT_ASPECT_CARVED_PLANKS.blockHolder(), "Carved Light Planks");
		provider.addBlock(LIGHT_ASPECT_STAIRS.blockHolder(), "Light Stairs");
		provider.addBlock(LIGHT_ASPECT_SLAB.blockHolder(), "Light Slab");
		provider.addBlock(LIGHT_ASPECT_FENCE.blockHolder(), "Light Fence");
		provider.addBlock(LIGHT_ASPECT_FENCE_GATE.blockHolder(), "Light Fence Gate");
		provider.addBlock(LIGHT_ASPECT_DOOR.blockHolder(), "Light Door");
		provider.addBlock(LIGHT_ASPECT_TRAPDOOR.blockHolder(), "Light Trapdoor");
		provider.addBlock(LIGHT_ASPECT_PRESSURE_PLATE.blockHolder(), "Light Pressure Plate");
		provider.addBlock(LIGHT_ASPECT_BUTTON.blockHolder(), "Light Button");
		provider.addBlock(LIGHT_ASPECT_BOOKSHELF.blockHolder(), "Light Bookshelf");
		provider.addBlock(LIGHT_ASPECT_LADDER.blockHolder(), "Light Ladder");
		provider.addBlock(LIGHT_ASPECT_SIGN, "Light Sign");
		provider.addBlock(LIGHT_ASPECT_HANGING_SIGN, "Light Sign");
		
		provider.addBlock(MIND_ASPECT_LOG.blockHolder(), "Mind Log");
		provider.addBlock(MIND_ASPECT_WOOD.blockHolder(), "Mind Wood");
		provider.addBlock(MIND_ASPECT_STRIPPED_LOG.blockHolder(), "Stripped Mind Log");
		provider.addBlock(MIND_ASPECT_STRIPPED_WOOD.blockHolder(), "Stripped Mind Wood");
		provider.addBlock(MIND_ASPECT_LEAVES.blockHolder(), "Mind Leaves");
		provider.addBlock(MIND_ASPECT_SAPLING.blockHolder(), "Mind Sapling");
		provider.addBlock(MIND_ASPECT_PLANKS.blockHolder(), "Mind Planks");
		provider.addBlock(MIND_ASPECT_CARVED_PLANKS.blockHolder(), "Carved Mind Planks");
		provider.addBlock(MIND_ASPECT_STAIRS.blockHolder(), "Mind Stairs");
		provider.addBlock(MIND_ASPECT_SLAB.blockHolder(), "Mind Slab");
		provider.addBlock(MIND_ASPECT_FENCE.blockHolder(), "Mind Fence");
		provider.addBlock(MIND_ASPECT_FENCE_GATE.blockHolder(), "Mind Fence Gate");
		provider.addBlock(MIND_ASPECT_DOOR.blockHolder(), "Mind Door");
		provider.addBlock(MIND_ASPECT_TRAPDOOR.blockHolder(), "Mind Trapdoor");
		provider.addBlock(MIND_ASPECT_PRESSURE_PLATE.blockHolder(), "Mind Pressure Plate");
		provider.addBlock(MIND_ASPECT_BUTTON.blockHolder(), "Mind Button");
		provider.addBlock(MIND_ASPECT_BOOKSHELF.blockHolder(), "Mind Bookshelf");
		provider.addBlock(MIND_ASPECT_LADDER.blockHolder(), "Mind Ladder");
		provider.addBlock(MIND_ASPECT_SIGN, "Mind Sign");
		provider.addBlock(MIND_ASPECT_HANGING_SIGN, "Mind Sign");
		
		provider.addBlock(RAGE_ASPECT_LOG.blockHolder(), "Rage Log");
		provider.addBlock(RAGE_ASPECT_WOOD.blockHolder(), "Rage Wood");
		provider.addBlock(RAGE_ASPECT_STRIPPED_LOG.blockHolder(), "Stripped Rage Log");
		provider.addBlock(RAGE_ASPECT_STRIPPED_WOOD.blockHolder(), "Stripped Rage Wood");
		provider.addBlock(RAGE_ASPECT_LEAVES.blockHolder(), "Rage Leaves");
		provider.addBlock(RAGE_ASPECT_SAPLING.blockHolder(), "Rage Sapling");
		provider.addBlock(RAGE_ASPECT_PLANKS.blockHolder(), "Rage Planks");
		provider.addBlock(RAGE_ASPECT_CARVED_PLANKS.blockHolder(), "Carved Rage Planks");
		provider.addBlock(RAGE_ASPECT_STAIRS.blockHolder(), "Rage Stairs");
		provider.addBlock(RAGE_ASPECT_SLAB.blockHolder(), "Rage Slab");
		provider.addBlock(RAGE_ASPECT_FENCE.blockHolder(), "Rage Fence");
		provider.addBlock(RAGE_ASPECT_FENCE_GATE.blockHolder(), "Rage Fence Gate");
		provider.addBlock(RAGE_ASPECT_DOOR.blockHolder(), "Rage Door");
		provider.addBlock(RAGE_ASPECT_TRAPDOOR.blockHolder(), "Rage Trapdoor");
		provider.addBlock(RAGE_ASPECT_PRESSURE_PLATE.blockHolder(), "Rage Pressure Plate");
		provider.addBlock(RAGE_ASPECT_BUTTON.blockHolder(), "Rage Button");
		provider.addBlock(RAGE_ASPECT_BOOKSHELF.blockHolder(), "Rage Bookshelf");
		provider.addBlock(RAGE_ASPECT_LADDER.blockHolder(), "Rage Ladder");
		provider.addBlock(RAGE_ASPECT_SIGN, "Rage Sign");
		provider.addBlock(RAGE_ASPECT_HANGING_SIGN, "Rage Sign");
		
		provider.addBlock(SPACE_ASPECT_LOG.blockHolder(), "Space Log");
		provider.addBlock(SPACE_ASPECT_WOOD.blockHolder(), "Space Wood");
		provider.addBlock(SPACE_ASPECT_STRIPPED_LOG.blockHolder(), "Stripped Space Log");
		provider.addBlock(SPACE_ASPECT_STRIPPED_WOOD.blockHolder(), "Stripped Space Wood");
		provider.addBlock(SPACE_ASPECT_LEAVES.blockHolder(), "Space Leaves");
		provider.addBlock(SPACE_ASPECT_SAPLING.blockHolder(), "Space Sapling");
		provider.addBlock(SPACE_ASPECT_PLANKS.blockHolder(), "Space Planks");
		provider.addBlock(SPACE_ASPECT_CARVED_PLANKS.blockHolder(), "Carved Space Planks");
		provider.addBlock(SPACE_ASPECT_STAIRS.blockHolder(), "Space Stairs");
		provider.addBlock(SPACE_ASPECT_SLAB.blockHolder(), "Space Slab");
		provider.addBlock(SPACE_ASPECT_FENCE.blockHolder(), "Space Fence");
		provider.addBlock(SPACE_ASPECT_FENCE_GATE.blockHolder(), "Space Fence Gate");
		provider.addBlock(SPACE_ASPECT_DOOR.blockHolder(), "Space Door");
		provider.addBlock(SPACE_ASPECT_TRAPDOOR.blockHolder(), "Space Trapdoor");
		provider.addBlock(SPACE_ASPECT_PRESSURE_PLATE.blockHolder(), "Space Pressure Plate");
		provider.addBlock(SPACE_ASPECT_BUTTON.blockHolder(), "Space Button");
		provider.addBlock(SPACE_ASPECT_BOOKSHELF.blockHolder(), "Space Bookshelf");
		provider.addBlock(SPACE_ASPECT_LADDER.blockHolder(), "Space Ladder");
		provider.addBlock(SPACE_ASPECT_SIGN, "Space Sign");
		provider.addBlock(SPACE_ASPECT_HANGING_SIGN, "Space Sign");
		
		provider.addBlock(TIME_ASPECT_LOG.blockHolder(), "Time Log");
		provider.addBlock(TIME_ASPECT_WOOD.blockHolder(), "Time Wood");
		provider.addBlock(TIME_ASPECT_STRIPPED_LOG.blockHolder(), "Stripped Time Log");
		provider.addBlock(TIME_ASPECT_STRIPPED_WOOD.blockHolder(), "Stripped Time Wood");
		provider.addBlock(TIME_ASPECT_LEAVES.blockHolder(), "Time Leaves");
		provider.addBlock(TIME_ASPECT_SAPLING.blockHolder(), "Time Sapling");
		provider.addBlock(TIME_ASPECT_PLANKS.blockHolder(), "Time Planks");
		provider.addBlock(TIME_ASPECT_CARVED_PLANKS.blockHolder(), "Carved Time Planks");
		provider.addBlock(TIME_ASPECT_STAIRS.blockHolder(), "Time Stairs");
		provider.addBlock(TIME_ASPECT_SLAB.blockHolder(), "Time Slab");
		provider.addBlock(TIME_ASPECT_FENCE.blockHolder(), "Time Fence");
		provider.addBlock(TIME_ASPECT_FENCE_GATE.blockHolder(), "Time Fence Gate");
		provider.addBlock(TIME_ASPECT_DOOR.blockHolder(), "Time Door");
		provider.addBlock(TIME_ASPECT_TRAPDOOR.blockHolder(), "Time Trapdoor");
		provider.addBlock(TIME_ASPECT_PRESSURE_PLATE.blockHolder(), "Time Pressure Plate");
		provider.addBlock(TIME_ASPECT_BUTTON.blockHolder(), "Time Button");
		provider.addBlock(TIME_ASPECT_BOOKSHELF.blockHolder(), "Time Bookshelf");
		provider.addBlock(TIME_ASPECT_LADDER.blockHolder(), "Time Ladder");
		provider.addBlock(TIME_ASPECT_SIGN, "Time Sign");
		provider.addBlock(TIME_ASPECT_HANGING_SIGN, "Time Sign");
		
		provider.addBlock(VOID_ASPECT_LOG.blockHolder(), "Void Log");
		provider.addBlock(VOID_ASPECT_WOOD.blockHolder(), "Void Wood");
		provider.addBlock(VOID_ASPECT_STRIPPED_LOG.blockHolder(), "Stripped Void Log");
		provider.addBlock(VOID_ASPECT_STRIPPED_WOOD.blockHolder(), "Stripped Void Wood");
		provider.addBlock(VOID_ASPECT_LEAVES.blockHolder(), "Void Leaves");
		provider.addBlock(VOID_ASPECT_SAPLING.blockHolder(), "Void Sapling");
		provider.addBlock(VOID_ASPECT_PLANKS.blockHolder(), "Void Planks");
		provider.addBlock(VOID_ASPECT_CARVED_PLANKS.blockHolder(), "Carved Void Planks");
		provider.addBlock(VOID_ASPECT_STAIRS.blockHolder(), "Void Stairs");
		provider.addBlock(VOID_ASPECT_SLAB.blockHolder(), "Void Slab");
		provider.addBlock(VOID_ASPECT_FENCE.blockHolder(), "Void Fence");
		provider.addBlock(VOID_ASPECT_FENCE_GATE.blockHolder(), "Void Fence Gate");
		provider.addBlock(VOID_ASPECT_DOOR.blockHolder(), "Void Door");
		provider.addBlock(VOID_ASPECT_TRAPDOOR.blockHolder(), "Void Trapdoor");
		provider.addBlock(VOID_ASPECT_PRESSURE_PLATE.blockHolder(), "Void Pressure Plate");
		provider.addBlock(VOID_ASPECT_BUTTON.blockHolder(), "Void Button");
		provider.addBlock(VOID_ASPECT_BOOKSHELF.blockHolder(), "Void Bookshelf");
		provider.addBlock(VOID_ASPECT_LADDER.blockHolder(), "Void Ladder");
		provider.addBlock(VOID_ASPECT_SIGN, "Void Sign");
		provider.addBlock(VOID_ASPECT_HANGING_SIGN, "Void Sign");
		
	}
	
	public static void addModels(MSBlockStateProvider provider)
	{
		provider.axisWithItem(BLOOD_ASPECT_LOG.blockHolder(),
				id -> provider.models().cubeColumn(id.getPath(),
						texture(id),
						texture(id.withSuffix("_top"))));
		provider.axisWithItem(BLOOD_ASPECT_WOOD.blockHolder(),
				id -> provider.models().cubeColumn(id.getPath(),
						texture("blood_aspect_log"),
						texture("blood_aspect_log")));
		provider.axisWithItem(BLOOD_ASPECT_STRIPPED_LOG.blockHolder(),
				id -> provider.models().cubeColumn(id.getPath(),
						texture(id),
						texture(id.withSuffix("_top"))));
		provider.axisWithItem(BLOOD_ASPECT_STRIPPED_WOOD.blockHolder(),
				id -> provider.models().cubeColumn(id.getPath(),
						texture("blood_aspect_stripped_log"),
						texture("blood_aspect_stripped_log")));
		provider.simpleBlockWithItem(BLOOD_ASPECT_LEAVES);
		provider.simpleBlock(BLOOD_ASPECT_SAPLING.blockHolder(),
				id -> provider.models().cross(id.getPath(), texture(id)).renderType("cutout"));
		provider.flatItem(BLOOD_ASPECT_SAPLING.itemHolder(), MSBlockStateProvider::texture);
		provider.pottedSaplingBlock(POTTED_BLOOD_ASPECT_SAPLING, BLOOD_ASPECT_SAPLING.blockHolder());
		provider.simpleBlockWithItem(BLOOD_ASPECT_PLANKS);
		provider.simpleBlockWithItem(BLOOD_ASPECT_CARVED_PLANKS);
		provider.stairsWithItem(BLOOD_ASPECT_STAIRS.blockHolder(), BLOOD_ASPECT_PLANKS.blockHolder());
		provider.slabWithItem(BLOOD_ASPECT_SLAB.blockHolder(), BLOOD_ASPECT_PLANKS.blockHolder());
		provider.fenceWithItem(BLOOD_ASPECT_FENCE.blockHolder(), BLOOD_ASPECT_PLANKS.blockHolder());
		provider.fenceGateWithItem(BLOOD_ASPECT_FENCE_GATE.blockHolder(), BLOOD_ASPECT_PLANKS.blockHolder());
		provider.simpleDoorBlock(BLOOD_ASPECT_DOOR.blockHolder());
		provider.trapDoorWithItem(BLOOD_ASPECT_TRAPDOOR.blockHolder());
		provider.flatItem(BLOOD_ASPECT_DOOR.itemHolder(), MSBlockStateProvider::itemTexture);
		provider.pressurePlateWithItem(BLOOD_ASPECT_PRESSURE_PLATE.blockHolder(), BLOOD_ASPECT_PLANKS.blockHolder());
		provider.buttonWithItem(BLOOD_ASPECT_BUTTON.blockHolder(), BLOOD_ASPECT_PLANKS.blockHolder());
		provider.simpleBlockWithItem(BLOOD_ASPECT_BOOKSHELF.blockHolder(),
				id -> provider.models().cubeColumn(id.getPath(),
						texture(id),
						texture("blood_aspect_planks")));
		provider.simpleHorizontal(BLOOD_ASPECT_LADDER.blockHolder(), provider::ladder);
		provider.flatItem(BLOOD_ASPECT_LADDER.itemHolder(), MSBlockStateProvider::texture);
		
		provider.signBlock(BLOOD_ASPECT_SIGN.get(), BLOOD_ASPECT_WALL_SIGN.get(),
				provider.blockTexture(BLOOD_ASPECT_PLANKS.asBlock()));
		provider.flatItem(BLOOD_ASPECT_SIGN_ITEM, MSBlockStateProvider::itemTexture);
		provider.hangingSignBlock(BLOOD_ASPECT_HANGING_SIGN.get(), BLOOD_ASPECT_WALL_HANGING_SIGN.get(),
				provider.blockTexture(BLOOD_ASPECT_PLANKS.asBlock()));
		provider.flatItem(BLOOD_ASPECT_HANGING_SIGN_ITEM, MSBlockStateProvider::itemTexture);
		
		provider.axisWithItem(BREATH_ASPECT_LOG.blockHolder(),
				id -> provider.models().cubeColumn(id.getPath(),
						texture(id),
						texture(id.withSuffix("_top"))));
		provider.axisWithItem(BREATH_ASPECT_WOOD.blockHolder(),
				id -> provider.models().cubeColumn(id.getPath(),
						texture("breath_aspect_log"),
						texture("breath_aspect_log")));
		provider.axisWithItem(BREATH_ASPECT_STRIPPED_LOG.blockHolder(),
				id -> provider.models().cubeColumn(id.getPath(),
						texture(id),
						texture(id.withSuffix("_top"))));
		provider.axisWithItem(BREATH_ASPECT_STRIPPED_WOOD.blockHolder(),
				id -> provider.models().cubeColumn(id.getPath(),
						texture("breath_aspect_stripped_log"),
						texture("breath_aspect_stripped_log")));
		provider.simpleBlockWithItem(BREATH_ASPECT_LEAVES);
		provider.simpleBlock(BREATH_ASPECT_SAPLING.blockHolder(),
				id -> provider.models().cross(id.getPath(), texture(id)).renderType("cutout"));
		provider.flatItem(BREATH_ASPECT_SAPLING.itemHolder(), MSBlockStateProvider::texture);
		provider.pottedSaplingBlock(POTTED_BREATH_ASPECT_SAPLING, BREATH_ASPECT_SAPLING.blockHolder());
		provider.simpleBlockWithItem(BREATH_ASPECT_PLANKS);
		provider.simpleBlockWithItem(BREATH_ASPECT_CARVED_PLANKS);
		provider.stairsWithItem(BREATH_ASPECT_STAIRS.blockHolder(), BREATH_ASPECT_PLANKS.blockHolder());
		provider.slabWithItem(BREATH_ASPECT_SLAB.blockHolder(), BREATH_ASPECT_PLANKS.blockHolder());
		provider.fenceWithItem(BREATH_ASPECT_FENCE.blockHolder(), BREATH_ASPECT_PLANKS.blockHolder());
		provider.fenceGateWithItem(BREATH_ASPECT_FENCE_GATE.blockHolder(), BREATH_ASPECT_PLANKS.blockHolder());
		provider.simpleDoorBlock(BREATH_ASPECT_DOOR.blockHolder());
		provider.trapDoorWithItem(BREATH_ASPECT_TRAPDOOR.blockHolder());
		provider.flatItem(BREATH_ASPECT_DOOR.itemHolder(), MSBlockStateProvider::itemTexture);
		provider.pressurePlateWithItem(BREATH_ASPECT_PRESSURE_PLATE.blockHolder(), BREATH_ASPECT_PLANKS.blockHolder());
		provider.buttonWithItem(BREATH_ASPECT_BUTTON.blockHolder(), BREATH_ASPECT_PLANKS.blockHolder());
		provider.simpleBlockWithItem(BREATH_ASPECT_BOOKSHELF.blockHolder(),
				id -> provider.models().cubeColumn(id.getPath(),
						texture(id),
						texture("breath_aspect_planks")));
		provider.simpleHorizontal(BREATH_ASPECT_LADDER.blockHolder(), provider::ladder);
		provider.flatItem(BREATH_ASPECT_LADDER.itemHolder(), MSBlockStateProvider::texture);
		
		provider.signBlock(BREATH_ASPECT_SIGN.get(), BREATH_ASPECT_WALL_SIGN.get(),
				provider.blockTexture(BREATH_ASPECT_PLANKS.asBlock()));
		provider.flatItem(BREATH_ASPECT_SIGN_ITEM, MSBlockStateProvider::itemTexture);
		provider.hangingSignBlock(BREATH_ASPECT_HANGING_SIGN.get(), BREATH_ASPECT_WALL_HANGING_SIGN.get(),
				provider.blockTexture(BREATH_ASPECT_PLANKS.asBlock()));
		provider.flatItem(BREATH_ASPECT_HANGING_SIGN_ITEM, MSBlockStateProvider::itemTexture);
		
		provider.axisWithItem(DOOM_ASPECT_LOG.blockHolder(),
				id -> provider.models().cubeColumn(id.getPath(),
						texture(id),
						texture(id.withSuffix("_top"))));
		provider.axisWithItem(DOOM_ASPECT_WOOD.blockHolder(),
				id -> provider.models().cubeColumn(id.getPath(),
						texture("doom_aspect_log"),
						texture("doom_aspect_log")));
		provider.axisWithItem(DOOM_ASPECT_STRIPPED_LOG.blockHolder(),
				id -> provider.models().cubeColumn(id.getPath(),
						texture(id),
						texture(id.withSuffix("_top"))));
		provider.axisWithItem(DOOM_ASPECT_STRIPPED_WOOD.blockHolder(),
				id -> provider.models().cubeColumn(id.getPath(),
						texture("doom_aspect_stripped_log"),
						texture("doom_aspect_stripped_log")));
		provider.simpleBlockWithItem(DOOM_ASPECT_LEAVES);
		provider.simpleBlock(DOOM_ASPECT_SAPLING.blockHolder(),
				id -> provider.models().cross(id.getPath(), texture(id)).renderType("cutout"));
		provider.flatItem(DOOM_ASPECT_SAPLING.itemHolder(), MSBlockStateProvider::texture);
		provider.pottedSaplingBlock(POTTED_DOOM_ASPECT_SAPLING, DOOM_ASPECT_SAPLING.blockHolder());
		provider.simpleBlockWithItem(DOOM_ASPECT_PLANKS);
		provider.simpleBlockWithItem(DOOM_ASPECT_CARVED_PLANKS);
		provider.stairsWithItem(DOOM_ASPECT_STAIRS.blockHolder(), DOOM_ASPECT_PLANKS.blockHolder());
		provider.slabWithItem(DOOM_ASPECT_SLAB.blockHolder(), DOOM_ASPECT_PLANKS.blockHolder());
		provider.fenceWithItem(DOOM_ASPECT_FENCE.blockHolder(), DOOM_ASPECT_PLANKS.blockHolder());
		provider.fenceGateWithItem(DOOM_ASPECT_FENCE_GATE.blockHolder(), DOOM_ASPECT_PLANKS.blockHolder());
		provider.simpleDoorBlock(DOOM_ASPECT_DOOR.blockHolder());
		provider.trapDoorWithItem(DOOM_ASPECT_TRAPDOOR.blockHolder());
		provider.flatItem(DOOM_ASPECT_DOOR.itemHolder(), MSBlockStateProvider::itemTexture);
		provider.pressurePlateWithItem(DOOM_ASPECT_PRESSURE_PLATE.blockHolder(), DOOM_ASPECT_PLANKS.blockHolder());
		provider.buttonWithItem(DOOM_ASPECT_BUTTON.blockHolder(), DOOM_ASPECT_PLANKS.blockHolder());
		provider.simpleBlockWithItem(DOOM_ASPECT_BOOKSHELF.blockHolder(),
				id -> provider.models().cubeColumn(id.getPath(),
						texture(id),
						texture("doom_aspect_planks")));
		provider.simpleHorizontal(DOOM_ASPECT_LADDER.blockHolder(), provider::ladder);
		provider.flatItem(DOOM_ASPECT_LADDER.itemHolder(), MSBlockStateProvider::texture);
		
		provider.signBlock(DOOM_ASPECT_SIGN.get(), DOOM_ASPECT_WALL_SIGN.get(),
				provider.blockTexture(DOOM_ASPECT_PLANKS.asBlock()));
		provider.flatItem(DOOM_ASPECT_SIGN_ITEM, MSBlockStateProvider::itemTexture);
		provider.hangingSignBlock(DOOM_ASPECT_HANGING_SIGN.get(), DOOM_ASPECT_WALL_HANGING_SIGN.get(),
				provider.blockTexture(DOOM_ASPECT_PLANKS.asBlock()));
		provider.flatItem(DOOM_ASPECT_HANGING_SIGN_ITEM, MSBlockStateProvider::itemTexture);
		
		provider.axisWithItem(HEART_ASPECT_LOG.blockHolder(),
				id -> provider.models().cubeColumn(id.getPath(),
						texture(id),
						texture(id.withSuffix("_top"))));
		provider.axisWithItem(HEART_ASPECT_WOOD.blockHolder(),
				id -> provider.models().cubeColumn(id.getPath(),
						texture("heart_aspect_log"),
						texture("heart_aspect_log")));
		provider.axisWithItem(HEART_ASPECT_STRIPPED_LOG.blockHolder(),
				id -> provider.models().cubeColumn(id.getPath(),
						texture(id),
						texture(id.withSuffix("_top"))));
		provider.axisWithItem(HEART_ASPECT_STRIPPED_WOOD.blockHolder(),
				id -> provider.models().cubeColumn(id.getPath(),
						texture("heart_aspect_stripped_log"),
						texture("heart_aspect_stripped_log")));
		provider.simpleBlockWithItem(HEART_ASPECT_LEAVES);
		provider.simpleBlock(HEART_ASPECT_SAPLING.blockHolder(),
				id -> provider.models().cross(id.getPath(), texture(id)).renderType("cutout"));
		provider.flatItem(HEART_ASPECT_SAPLING.itemHolder(), MSBlockStateProvider::texture);
		provider.pottedSaplingBlock(POTTED_HEART_ASPECT_SAPLING, HEART_ASPECT_SAPLING.blockHolder());
		provider.simpleBlockWithItem(HEART_ASPECT_PLANKS);
		provider.simpleBlockWithItem(HEART_ASPECT_CARVED_PLANKS);
		provider.stairsWithItem(HEART_ASPECT_STAIRS.blockHolder(), HEART_ASPECT_PLANKS.blockHolder());
		provider.slabWithItem(HEART_ASPECT_SLAB.blockHolder(), HEART_ASPECT_PLANKS.blockHolder());
		provider.fenceWithItem(HEART_ASPECT_FENCE.blockHolder(), HEART_ASPECT_PLANKS.blockHolder());
		provider.fenceGateWithItem(HEART_ASPECT_FENCE_GATE.blockHolder(), HEART_ASPECT_PLANKS.blockHolder());
		provider.simpleDoorBlock(HEART_ASPECT_DOOR.blockHolder());
		provider.trapDoorWithItem(HEART_ASPECT_TRAPDOOR.blockHolder());
		provider.flatItem(HEART_ASPECT_DOOR.itemHolder(), MSBlockStateProvider::itemTexture);
		provider.pressurePlateWithItem(HEART_ASPECT_PRESSURE_PLATE.blockHolder(), HEART_ASPECT_PLANKS.blockHolder());
		provider.buttonWithItem(HEART_ASPECT_BUTTON.blockHolder(), HEART_ASPECT_PLANKS.blockHolder());
		provider.simpleBlockWithItem(HEART_ASPECT_BOOKSHELF.blockHolder(),
				id -> provider.models().cubeColumn(id.getPath(),
						texture(id),
						texture("heart_aspect_planks")));
		provider.simpleHorizontal(HEART_ASPECT_LADDER.blockHolder(), provider::ladder);
		provider.flatItem(HEART_ASPECT_LADDER.itemHolder(), MSBlockStateProvider::texture);
		
		provider.signBlock(HEART_ASPECT_SIGN.get(), HEART_ASPECT_WALL_SIGN.get(),
				provider.blockTexture(HEART_ASPECT_PLANKS.asBlock()));
		provider.flatItem(HEART_ASPECT_SIGN_ITEM, MSBlockStateProvider::itemTexture);
		provider.hangingSignBlock(HEART_ASPECT_HANGING_SIGN.get(), HEART_ASPECT_WALL_HANGING_SIGN.get(),
				provider.blockTexture(HEART_ASPECT_PLANKS.asBlock()));
		provider.flatItem(HEART_ASPECT_HANGING_SIGN_ITEM, MSBlockStateProvider::itemTexture);
		
		provider.axisWithItem(HOPE_ASPECT_LOG.blockHolder(),
				id -> provider.models().cubeColumn(id.getPath(),
						texture(id),
						texture(id.withSuffix("_top"))));
		provider.axisWithItem(HOPE_ASPECT_WOOD.blockHolder(),
				id -> provider.models().cubeColumn(id.getPath(),
						texture("hope_aspect_log"),
						texture("hope_aspect_log")));
		provider.axisWithItem(HOPE_ASPECT_STRIPPED_LOG.blockHolder(),
				id -> provider.models().cubeColumn(id.getPath(),
						texture(id),
						texture(id.withSuffix("_top"))));
		provider.axisWithItem(HOPE_ASPECT_STRIPPED_WOOD.blockHolder(),
				id -> provider.models().cubeColumn(id.getPath(),
						texture("hope_aspect_stripped_log"),
						texture("hope_aspect_stripped_log")));
		provider.simpleBlockWithItem(HOPE_ASPECT_LEAVES);
		provider.simpleBlock(HOPE_ASPECT_SAPLING.blockHolder(),
				id -> provider.models().cross(id.getPath(), texture(id)).renderType("cutout"));
		provider.flatItem(HOPE_ASPECT_SAPLING.itemHolder(), MSBlockStateProvider::texture);
		provider.pottedSaplingBlock(POTTED_HOPE_ASPECT_SAPLING, HOPE_ASPECT_SAPLING.blockHolder());
		provider.simpleBlockWithItem(HOPE_ASPECT_PLANKS);
		provider.simpleBlockWithItem(HOPE_ASPECT_CARVED_PLANKS);
		provider.stairsWithItem(HOPE_ASPECT_STAIRS.blockHolder(), HOPE_ASPECT_PLANKS.blockHolder());
		provider.slabWithItem(HOPE_ASPECT_SLAB.blockHolder(), HOPE_ASPECT_PLANKS.blockHolder());
		provider.fenceWithItem(HOPE_ASPECT_FENCE.blockHolder(), HOPE_ASPECT_PLANKS.blockHolder());
		provider.fenceGateWithItem(HOPE_ASPECT_FENCE_GATE.blockHolder(), HOPE_ASPECT_PLANKS.blockHolder());
		provider.simpleDoorBlock(HOPE_ASPECT_DOOR.blockHolder());
		provider.trapDoorWithItem(HOPE_ASPECT_TRAPDOOR.blockHolder());
		provider.flatItem(HOPE_ASPECT_DOOR.itemHolder(), MSBlockStateProvider::itemTexture);
		provider.pressurePlateWithItem(HOPE_ASPECT_PRESSURE_PLATE.blockHolder(), HOPE_ASPECT_PLANKS.blockHolder());
		provider.buttonWithItem(HOPE_ASPECT_BUTTON.blockHolder(), HOPE_ASPECT_PLANKS.blockHolder());
		provider.simpleBlockWithItem(HOPE_ASPECT_BOOKSHELF.blockHolder(),
				id -> provider.models().cubeColumn(id.getPath(),
						texture(id),
						texture("hope_aspect_planks")));
		provider.simpleHorizontal(HOPE_ASPECT_LADDER.blockHolder(), provider::ladder);
		provider.flatItem(HOPE_ASPECT_LADDER.itemHolder(), MSBlockStateProvider::texture);
		
		provider.signBlock(HOPE_ASPECT_SIGN.get(), HOPE_ASPECT_WALL_SIGN.get(),
				provider.blockTexture(HOPE_ASPECT_PLANKS.asBlock()));
		provider.flatItem(HOPE_ASPECT_SIGN_ITEM, MSBlockStateProvider::itemTexture);
		provider.hangingSignBlock(HOPE_ASPECT_HANGING_SIGN.get(), HOPE_ASPECT_WALL_HANGING_SIGN.get(),
				provider.blockTexture(HOPE_ASPECT_PLANKS.asBlock()));
		provider.flatItem(HOPE_ASPECT_HANGING_SIGN_ITEM, MSBlockStateProvider::itemTexture);
		
		provider.axisWithItem(LIFE_ASPECT_LOG.blockHolder(),
				id -> provider.models().cubeColumn(id.getPath(),
						texture(id),
						texture(id.withSuffix("_top"))));
		provider.axisWithItem(LIFE_ASPECT_WOOD.blockHolder(),
				id -> provider.models().cubeColumn(id.getPath(),
						texture("life_aspect_log"),
						texture("life_aspect_log")));
		provider.axisWithItem(LIFE_ASPECT_STRIPPED_LOG.blockHolder(),
				id -> provider.models().cubeColumn(id.getPath(),
						texture(id),
						texture(id.withSuffix("_top"))));
		provider.axisWithItem(LIFE_ASPECT_STRIPPED_WOOD.blockHolder(),
				id -> provider.models().cubeColumn(id.getPath(),
						texture("life_aspect_stripped_log"),
						texture("life_aspect_stripped_log")));
		provider.simpleBlockWithItem(LIFE_ASPECT_LEAVES);
		provider.simpleBlock(LIFE_ASPECT_SAPLING.blockHolder(),
				id -> provider.models().cross(id.getPath(), texture(id)).renderType("cutout"));
		provider.flatItem(LIFE_ASPECT_SAPLING.itemHolder(), MSBlockStateProvider::texture);
		provider.pottedSaplingBlock(POTTED_LIFE_ASPECT_SAPLING, LIFE_ASPECT_SAPLING.blockHolder());
		provider.simpleBlockWithItem(LIFE_ASPECT_PLANKS);
		provider.simpleBlockWithItem(LIFE_ASPECT_CARVED_PLANKS);
		provider.stairsWithItem(LIFE_ASPECT_STAIRS.blockHolder(), LIFE_ASPECT_PLANKS.blockHolder());
		provider.slabWithItem(LIFE_ASPECT_SLAB.blockHolder(), LIFE_ASPECT_PLANKS.blockHolder());
		provider.fenceWithItem(LIFE_ASPECT_FENCE.blockHolder(), LIFE_ASPECT_PLANKS.blockHolder());
		provider.fenceGateWithItem(LIFE_ASPECT_FENCE_GATE.blockHolder(), LIFE_ASPECT_PLANKS.blockHolder());
		provider.simpleDoorBlock(LIFE_ASPECT_DOOR.blockHolder());
		provider.trapDoorWithItem(LIFE_ASPECT_TRAPDOOR.blockHolder());
		provider.flatItem(LIFE_ASPECT_DOOR.itemHolder(), MSBlockStateProvider::itemTexture);
		provider.pressurePlateWithItem(LIFE_ASPECT_PRESSURE_PLATE.blockHolder(), LIFE_ASPECT_PLANKS.blockHolder());
		provider.buttonWithItem(LIFE_ASPECT_BUTTON.blockHolder(), LIFE_ASPECT_PLANKS.blockHolder());
		provider.simpleBlockWithItem(LIFE_ASPECT_BOOKSHELF.blockHolder(),
				id -> provider.models().cubeColumn(id.getPath(),
						texture(id),
						texture("life_aspect_planks")));
		provider.simpleHorizontal(LIFE_ASPECT_LADDER.blockHolder(), provider::ladder);
		provider.flatItem(LIFE_ASPECT_LADDER.itemHolder(), MSBlockStateProvider::texture);
		
		provider.signBlock(LIFE_ASPECT_SIGN.get(), LIFE_ASPECT_WALL_SIGN.get(),
				provider.blockTexture(LIFE_ASPECT_PLANKS.asBlock()));
		provider.flatItem(LIFE_ASPECT_SIGN_ITEM, MSBlockStateProvider::itemTexture);
		provider.hangingSignBlock(LIFE_ASPECT_HANGING_SIGN.get(), LIFE_ASPECT_WALL_HANGING_SIGN.get(),
				provider.blockTexture(LIFE_ASPECT_PLANKS.asBlock()));
		provider.flatItem(LIFE_ASPECT_HANGING_SIGN_ITEM, MSBlockStateProvider::itemTexture);
		
		provider.axisWithItem(LIGHT_ASPECT_LOG.blockHolder(),
				id -> provider.models().cubeColumn(id.getPath(),
						texture(id),
						texture(id.withSuffix("_top"))));
		provider.axisWithItem(LIGHT_ASPECT_WOOD.blockHolder(),
				id -> provider.models().cubeColumn(id.getPath(),
						texture("light_aspect_log"),
						texture("light_aspect_log")));
		provider.axisWithItem(LIGHT_ASPECT_STRIPPED_LOG.blockHolder(),
				id -> provider.models().cubeColumn(id.getPath(),
						texture(id),
						texture(id.withSuffix("_top"))));
		provider.axisWithItem(LIGHT_ASPECT_STRIPPED_WOOD.blockHolder(),
				id -> provider.models().cubeColumn(id.getPath(),
						texture("light_aspect_stripped_log"),
						texture("light_aspect_stripped_log")));
		provider.simpleBlockWithItem(LIGHT_ASPECT_LEAVES);
		provider.simpleBlock(LIGHT_ASPECT_SAPLING.blockHolder(),
				id -> provider.models().cross(id.getPath(), texture(id)).renderType("cutout"));
		provider.flatItem(LIGHT_ASPECT_SAPLING.itemHolder(), MSBlockStateProvider::texture);
		provider.pottedSaplingBlock(POTTED_LIGHT_ASPECT_SAPLING, LIGHT_ASPECT_SAPLING.blockHolder());
		provider.simpleBlockWithItem(LIGHT_ASPECT_PLANKS);
		provider.simpleBlockWithItem(LIGHT_ASPECT_CARVED_PLANKS);
		provider.stairsWithItem(LIGHT_ASPECT_STAIRS.blockHolder(), LIGHT_ASPECT_PLANKS.blockHolder());
		provider.slabWithItem(LIGHT_ASPECT_SLAB.blockHolder(), LIGHT_ASPECT_PLANKS.blockHolder());
		provider.fenceWithItem(LIGHT_ASPECT_FENCE.blockHolder(), LIGHT_ASPECT_PLANKS.blockHolder());
		provider.fenceGateWithItem(LIGHT_ASPECT_FENCE_GATE.blockHolder(), LIGHT_ASPECT_PLANKS.blockHolder());
		provider.simpleDoorBlock(LIGHT_ASPECT_DOOR.blockHolder());
		provider.trapDoorWithItem(LIGHT_ASPECT_TRAPDOOR.blockHolder());
		provider.flatItem(LIGHT_ASPECT_DOOR.itemHolder(), MSBlockStateProvider::itemTexture);
		provider.pressurePlateWithItem(LIGHT_ASPECT_PRESSURE_PLATE.blockHolder(), LIGHT_ASPECT_PLANKS.blockHolder());
		provider.buttonWithItem(LIGHT_ASPECT_BUTTON.blockHolder(), LIGHT_ASPECT_PLANKS.blockHolder());
		provider.simpleBlockWithItem(LIGHT_ASPECT_BOOKSHELF.blockHolder(),
				id -> provider.models().cubeColumn(id.getPath(),
						texture(id),
						texture("light_aspect_planks")));
		provider.simpleHorizontal(LIGHT_ASPECT_LADDER.blockHolder(), provider::ladder);
		provider.flatItem(LIGHT_ASPECT_LADDER.itemHolder(), MSBlockStateProvider::texture);
		
		provider.signBlock(LIGHT_ASPECT_SIGN.get(), LIGHT_ASPECT_WALL_SIGN.get(),
				provider.blockTexture(LIGHT_ASPECT_PLANKS.asBlock()));
		provider.flatItem(LIGHT_ASPECT_SIGN_ITEM, MSBlockStateProvider::itemTexture);
		provider.hangingSignBlock(LIGHT_ASPECT_HANGING_SIGN.get(), LIGHT_ASPECT_WALL_HANGING_SIGN.get(),
				provider.blockTexture(LIGHT_ASPECT_PLANKS.asBlock()));
		provider.flatItem(LIGHT_ASPECT_HANGING_SIGN_ITEM, MSBlockStateProvider::itemTexture);
		
		provider.axisWithItem(MIND_ASPECT_LOG.blockHolder(),
				id -> provider.models().cubeColumn(id.getPath(),
						texture(id),
						texture(id.withSuffix("_top"))));
		provider.axisWithItem(MIND_ASPECT_WOOD.blockHolder(),
				id -> provider.models().cubeColumn(id.getPath(),
						texture("mind_aspect_log"),
						texture("mind_aspect_log")));
		provider.axisWithItem(MIND_ASPECT_STRIPPED_LOG.blockHolder(),
				id -> provider.models().cubeColumn(id.getPath(),
						texture(id),
						texture(id.withSuffix("_top"))));
		provider.axisWithItem(MIND_ASPECT_STRIPPED_WOOD.blockHolder(),
				id -> provider.models().cubeColumn(id.getPath(),
						texture("mind_aspect_stripped_log"),
						texture("mind_aspect_stripped_log")));
		provider.simpleBlockWithItem(MIND_ASPECT_LEAVES);
		provider.simpleBlock(MIND_ASPECT_SAPLING.blockHolder(),
				id -> provider.models().cross(id.getPath(), texture(id)).renderType("cutout"));
		provider.flatItem(MIND_ASPECT_SAPLING.itemHolder(), MSBlockStateProvider::texture);
		provider.pottedSaplingBlock(POTTED_MIND_ASPECT_SAPLING, MIND_ASPECT_SAPLING.blockHolder());
		provider.simpleBlockWithItem(MIND_ASPECT_PLANKS);
		provider.simpleBlockWithItem(MIND_ASPECT_CARVED_PLANKS);
		provider.stairsWithItem(MIND_ASPECT_STAIRS.blockHolder(), MIND_ASPECT_PLANKS.blockHolder());
		provider.slabWithItem(MIND_ASPECT_SLAB.blockHolder(), MIND_ASPECT_PLANKS.blockHolder());
		provider.fenceWithItem(MIND_ASPECT_FENCE.blockHolder(), MIND_ASPECT_PLANKS.blockHolder());
		provider.fenceGateWithItem(MIND_ASPECT_FENCE_GATE.blockHolder(), MIND_ASPECT_PLANKS.blockHolder());
		provider.simpleDoorBlock(MIND_ASPECT_DOOR.blockHolder());
		provider.trapDoorWithItem(MIND_ASPECT_TRAPDOOR.blockHolder());
		provider.flatItem(MIND_ASPECT_DOOR.itemHolder(), MSBlockStateProvider::itemTexture);
		provider.pressurePlateWithItem(MIND_ASPECT_PRESSURE_PLATE.blockHolder(), MIND_ASPECT_PLANKS.blockHolder());
		provider.buttonWithItem(MIND_ASPECT_BUTTON.blockHolder(), MIND_ASPECT_PLANKS.blockHolder());
		provider.simpleBlockWithItem(MIND_ASPECT_BOOKSHELF.blockHolder(),
				id -> provider.models().cubeColumn(id.getPath(),
						texture(id),
						texture("mind_aspect_planks")));
		provider.simpleHorizontal(MIND_ASPECT_LADDER.blockHolder(), provider::ladder);
		provider.flatItem(MIND_ASPECT_LADDER.itemHolder(), MSBlockStateProvider::texture);
		
		provider.signBlock(MIND_ASPECT_SIGN.get(), MIND_ASPECT_WALL_SIGN.get(),
				provider.blockTexture(MIND_ASPECT_PLANKS.asBlock()));
		provider.flatItem(MIND_ASPECT_SIGN_ITEM, MSBlockStateProvider::itemTexture);
		provider.hangingSignBlock(MIND_ASPECT_HANGING_SIGN.get(), MIND_ASPECT_WALL_HANGING_SIGN.get(),
				provider.blockTexture(MIND_ASPECT_PLANKS.asBlock()));
		provider.flatItem(MIND_ASPECT_HANGING_SIGN_ITEM, MSBlockStateProvider::itemTexture);
		
		provider.axisWithItem(RAGE_ASPECT_LOG.blockHolder(),
				id -> provider.models().cubeColumn(id.getPath(),
						texture(id),
						texture(id.withSuffix("_top"))));
		provider.axisWithItem(RAGE_ASPECT_WOOD.blockHolder(),
				id -> provider.models().cubeColumn(id.getPath(),
						texture("rage_aspect_log"),
						texture("rage_aspect_log")));
		provider.axisWithItem(RAGE_ASPECT_STRIPPED_LOG.blockHolder(),
				id -> provider.models().cubeColumn(id.getPath(),
						texture(id),
						texture(id.withSuffix("_top"))));
		provider.axisWithItem(RAGE_ASPECT_STRIPPED_WOOD.blockHolder(),
				id -> provider.models().cubeColumn(id.getPath(),
						texture("rage_aspect_stripped_log"),
						texture("rage_aspect_stripped_log")));
		provider.simpleBlockWithItem(RAGE_ASPECT_LEAVES);
		provider.simpleBlock(RAGE_ASPECT_SAPLING.blockHolder(),
				id -> provider.models().cross(id.getPath(), texture(id)).renderType("cutout"));
		provider.flatItem(RAGE_ASPECT_SAPLING.itemHolder(), MSBlockStateProvider::texture);
		provider.pottedSaplingBlock(POTTED_RAGE_ASPECT_SAPLING, RAGE_ASPECT_SAPLING.blockHolder());
		provider.simpleBlockWithItem(RAGE_ASPECT_PLANKS);
		provider.simpleBlockWithItem(RAGE_ASPECT_CARVED_PLANKS);
		provider.stairsWithItem(RAGE_ASPECT_STAIRS.blockHolder(), RAGE_ASPECT_PLANKS.blockHolder());
		provider.slabWithItem(RAGE_ASPECT_SLAB.blockHolder(), RAGE_ASPECT_PLANKS.blockHolder());
		provider.fenceWithItem(RAGE_ASPECT_FENCE.blockHolder(), RAGE_ASPECT_PLANKS.blockHolder());
		provider.fenceGateWithItem(RAGE_ASPECT_FENCE_GATE.blockHolder(), RAGE_ASPECT_PLANKS.blockHolder());
		provider.simpleDoorBlock(RAGE_ASPECT_DOOR.blockHolder());
		provider.trapDoorWithItem(RAGE_ASPECT_TRAPDOOR.blockHolder());
		provider.flatItem(RAGE_ASPECT_DOOR.itemHolder(), MSBlockStateProvider::itemTexture);
		provider.pressurePlateWithItem(RAGE_ASPECT_PRESSURE_PLATE.blockHolder(), RAGE_ASPECT_PLANKS.blockHolder());
		provider.buttonWithItem(RAGE_ASPECT_BUTTON.blockHolder(), RAGE_ASPECT_PLANKS.blockHolder());
		provider.simpleBlockWithItem(RAGE_ASPECT_BOOKSHELF.blockHolder(),
				id -> provider.models().cubeColumn(id.getPath(),
						texture(id),
						texture("rage_aspect_planks")));
		provider.simpleHorizontal(RAGE_ASPECT_LADDER.blockHolder(), provider::ladder);
		provider.flatItem(RAGE_ASPECT_LADDER.itemHolder(), MSBlockStateProvider::texture);
		
		provider.signBlock(RAGE_ASPECT_SIGN.get(), RAGE_ASPECT_WALL_SIGN.get(),
				provider.blockTexture(RAGE_ASPECT_PLANKS.asBlock()));
		provider.flatItem(RAGE_ASPECT_SIGN_ITEM, MSBlockStateProvider::itemTexture);
		provider.hangingSignBlock(RAGE_ASPECT_HANGING_SIGN.get(), RAGE_ASPECT_WALL_HANGING_SIGN.get(),
				provider.blockTexture(RAGE_ASPECT_PLANKS.asBlock()));
		provider.flatItem(RAGE_ASPECT_HANGING_SIGN_ITEM, MSBlockStateProvider::itemTexture);
		
		provider.axisWithItem(SPACE_ASPECT_LOG.blockHolder(),
				id -> provider.models().cubeColumn(id.getPath(),
						texture(id),
						texture(id.withSuffix("_top"))));
		provider.axisWithItem(SPACE_ASPECT_WOOD.blockHolder(),
				id -> provider.models().cubeColumn(id.getPath(),
						texture("space_aspect_log"),
						texture("space_aspect_log")));
		provider.axisWithItem(SPACE_ASPECT_STRIPPED_LOG.blockHolder(),
				id -> provider.models().cubeColumn(id.getPath(),
						texture(id),
						texture(id.withSuffix("_top"))));
		provider.axisWithItem(SPACE_ASPECT_STRIPPED_WOOD.blockHolder(),
				id -> provider.models().cubeColumn(id.getPath(),
						texture("space_aspect_stripped_log"),
						texture("space_aspect_stripped_log")));
		provider.simpleBlockWithItem(SPACE_ASPECT_LEAVES);
		provider.simpleBlock(SPACE_ASPECT_SAPLING.blockHolder(),
				id -> provider.models().cross(id.getPath(), texture(id)).renderType("cutout"));
		provider.flatItem(SPACE_ASPECT_SAPLING.itemHolder(), MSBlockStateProvider::texture);
		provider.pottedSaplingBlock(POTTED_SPACE_ASPECT_SAPLING, SPACE_ASPECT_SAPLING.blockHolder());
		provider.simpleBlockWithItem(SPACE_ASPECT_PLANKS);
		provider.simpleBlockWithItem(SPACE_ASPECT_CARVED_PLANKS);
		provider.stairsWithItem(SPACE_ASPECT_STAIRS.blockHolder(), SPACE_ASPECT_PLANKS.blockHolder());
		provider.slabWithItem(SPACE_ASPECT_SLAB.blockHolder(), SPACE_ASPECT_PLANKS.blockHolder());
		provider.fenceWithItem(SPACE_ASPECT_FENCE.blockHolder(), SPACE_ASPECT_PLANKS.blockHolder());
		provider.fenceGateWithItem(SPACE_ASPECT_FENCE_GATE.blockHolder(), SPACE_ASPECT_PLANKS.blockHolder());
		provider.simpleDoorBlock(SPACE_ASPECT_DOOR.blockHolder());
		provider.trapDoorWithItem(SPACE_ASPECT_TRAPDOOR.blockHolder());
		provider.flatItem(SPACE_ASPECT_DOOR.itemHolder(), MSBlockStateProvider::itemTexture);
		provider.pressurePlateWithItem(SPACE_ASPECT_PRESSURE_PLATE.blockHolder(), SPACE_ASPECT_PLANKS.blockHolder());
		provider.buttonWithItem(SPACE_ASPECT_BUTTON.blockHolder(), SPACE_ASPECT_PLANKS.blockHolder());
		provider.simpleBlockWithItem(SPACE_ASPECT_BOOKSHELF.blockHolder(),
				id -> provider.models().cubeColumn(id.getPath(),
						texture(id),
						texture("space_aspect_planks")));
		provider.simpleHorizontal(SPACE_ASPECT_LADDER.blockHolder(), provider::ladder);
		provider.flatItem(SPACE_ASPECT_LADDER.itemHolder(), MSBlockStateProvider::texture);
		
		provider.signBlock(SPACE_ASPECT_SIGN.get(), SPACE_ASPECT_WALL_SIGN.get(),
				provider.blockTexture(SPACE_ASPECT_PLANKS.asBlock()));
		provider.flatItem(SPACE_ASPECT_SIGN_ITEM, MSBlockStateProvider::itemTexture);
		provider.hangingSignBlock(SPACE_ASPECT_HANGING_SIGN.get(), SPACE_ASPECT_WALL_HANGING_SIGN.get(),
				provider.blockTexture(SPACE_ASPECT_PLANKS.asBlock()));
		provider.flatItem(SPACE_ASPECT_HANGING_SIGN_ITEM, MSBlockStateProvider::itemTexture);
		
		provider.axisWithItem(TIME_ASPECT_LOG.blockHolder(),
				id -> provider.models().cubeColumn(id.getPath(),
						texture(id),
						texture(id.withSuffix("_top"))));
		provider.axisWithItem(TIME_ASPECT_WOOD.blockHolder(),
				id -> provider.models().cubeColumn(id.getPath(),
						texture("time_aspect_log"),
						texture("time_aspect_log")));
		provider.axisWithItem(TIME_ASPECT_STRIPPED_LOG.blockHolder(),
				id -> provider.models().cubeColumn(id.getPath(),
						texture(id),
						texture(id.withSuffix("_top"))));
		provider.axisWithItem(TIME_ASPECT_STRIPPED_WOOD.blockHolder(),
				id -> provider.models().cubeColumn(id.getPath(),
						texture("time_aspect_stripped_log"),
						texture("time_aspect_stripped_log")));
		provider.simpleBlockWithItem(TIME_ASPECT_LEAVES);
		provider.simpleBlock(TIME_ASPECT_SAPLING.blockHolder(),
				id -> provider.models().cross(id.getPath(), texture(id)).renderType("cutout"));
		provider.flatItem(TIME_ASPECT_SAPLING.itemHolder(), MSBlockStateProvider::texture);
		provider.pottedSaplingBlock(POTTED_TIME_ASPECT_SAPLING, TIME_ASPECT_SAPLING.blockHolder());
		provider.simpleBlockWithItem(TIME_ASPECT_PLANKS);
		provider.simpleBlockWithItem(TIME_ASPECT_CARVED_PLANKS);
		provider.stairsWithItem(TIME_ASPECT_STAIRS.blockHolder(), TIME_ASPECT_PLANKS.blockHolder());
		provider.slabWithItem(TIME_ASPECT_SLAB.blockHolder(), TIME_ASPECT_PLANKS.blockHolder());
		provider.fenceWithItem(TIME_ASPECT_FENCE.blockHolder(), TIME_ASPECT_PLANKS.blockHolder());
		provider.fenceGateWithItem(TIME_ASPECT_FENCE_GATE.blockHolder(), TIME_ASPECT_PLANKS.blockHolder());
		provider.simpleDoorBlock(TIME_ASPECT_DOOR.blockHolder());
		provider.trapDoorWithItem(TIME_ASPECT_TRAPDOOR.blockHolder());
		provider.flatItem(TIME_ASPECT_DOOR.itemHolder(), MSBlockStateProvider::itemTexture);
		provider.pressurePlateWithItem(TIME_ASPECT_PRESSURE_PLATE.blockHolder(), TIME_ASPECT_PLANKS.blockHolder());
		provider.buttonWithItem(TIME_ASPECT_BUTTON.blockHolder(), TIME_ASPECT_PLANKS.blockHolder());
		provider.simpleBlockWithItem(TIME_ASPECT_BOOKSHELF.blockHolder(),
				id -> provider.models().cubeColumn(id.getPath(),
						texture(id),
						texture("time_aspect_planks")));
		provider.simpleHorizontal(TIME_ASPECT_LADDER.blockHolder(), provider::ladder);
		provider.flatItem(TIME_ASPECT_LADDER.itemHolder(), MSBlockStateProvider::texture);
		
		provider.signBlock(TIME_ASPECT_SIGN.get(), TIME_ASPECT_WALL_SIGN.get(),
				provider.blockTexture(TIME_ASPECT_PLANKS.asBlock()));
		provider.flatItem(TIME_ASPECT_SIGN_ITEM, MSBlockStateProvider::itemTexture);
		provider.hangingSignBlock(TIME_ASPECT_HANGING_SIGN.get(), TIME_ASPECT_WALL_HANGING_SIGN.get(),
				provider.blockTexture(TIME_ASPECT_PLANKS.asBlock()));
		provider.flatItem(TIME_ASPECT_HANGING_SIGN_ITEM, MSBlockStateProvider::itemTexture);
		
		provider.axisWithItem(VOID_ASPECT_LOG.blockHolder(),
				id -> provider.models().cubeColumn(id.getPath(),
						texture(id),
						texture(id.withSuffix("_top"))));
		provider.axisWithItem(VOID_ASPECT_WOOD.blockHolder(),
				id -> provider.models().cubeColumn(id.getPath(),
						texture("void_aspect_log"),
						texture("void_aspect_log")));
		provider.axisWithItem(VOID_ASPECT_STRIPPED_LOG.blockHolder(),
				id -> provider.models().cubeColumn(id.getPath(),
						texture(id),
						texture(id.withSuffix("_top"))));
		provider.axisWithItem(VOID_ASPECT_STRIPPED_WOOD.blockHolder(),
				id -> provider.models().cubeColumn(id.getPath(),
						texture("void_aspect_stripped_log"),
						texture("void_aspect_stripped_log")));
		provider.simpleBlockWithItem(VOID_ASPECT_LEAVES);
		provider.simpleBlock(VOID_ASPECT_SAPLING.blockHolder(),
				id -> provider.models().cross(id.getPath(), texture(id)).renderType("cutout"));
		provider.flatItem(VOID_ASPECT_SAPLING.itemHolder(), MSBlockStateProvider::texture);
		provider.pottedSaplingBlock(POTTED_VOID_ASPECT_SAPLING, VOID_ASPECT_SAPLING.blockHolder());
		provider.simpleBlockWithItem(VOID_ASPECT_PLANKS);
		provider.simpleBlockWithItem(VOID_ASPECT_CARVED_PLANKS);
		provider.stairsWithItem(VOID_ASPECT_STAIRS.blockHolder(), VOID_ASPECT_PLANKS.blockHolder());
		provider.slabWithItem(VOID_ASPECT_SLAB.blockHolder(), VOID_ASPECT_PLANKS.blockHolder());
		provider.fenceWithItem(VOID_ASPECT_FENCE.blockHolder(), VOID_ASPECT_PLANKS.blockHolder());
		provider.fenceGateWithItem(VOID_ASPECT_FENCE_GATE.blockHolder(), VOID_ASPECT_PLANKS.blockHolder());
		provider.simpleDoorBlock(VOID_ASPECT_DOOR.blockHolder());
		provider.trapDoorWithItem(VOID_ASPECT_TRAPDOOR.blockHolder());
		provider.flatItem(VOID_ASPECT_DOOR.itemHolder(), MSBlockStateProvider::itemTexture);
		provider.pressurePlateWithItem(VOID_ASPECT_PRESSURE_PLATE.blockHolder(), VOID_ASPECT_PLANKS.blockHolder());
		provider.buttonWithItem(VOID_ASPECT_BUTTON.blockHolder(), VOID_ASPECT_PLANKS.blockHolder());
		provider.simpleBlockWithItem(VOID_ASPECT_BOOKSHELF.blockHolder(),
				id -> provider.models().cubeColumn(id.getPath(),
						texture(id),
						texture("void_aspect_planks")));
		provider.simpleHorizontal(VOID_ASPECT_LADDER.blockHolder(), provider::ladder);
		provider.flatItem(VOID_ASPECT_LADDER.itemHolder(), MSBlockStateProvider::texture);
		
		provider.signBlock(VOID_ASPECT_SIGN.get(), VOID_ASPECT_WALL_SIGN.get(),
				provider.blockTexture(VOID_ASPECT_PLANKS.asBlock()));
		provider.flatItem(VOID_ASPECT_SIGN_ITEM, MSBlockStateProvider::itemTexture);
		provider.hangingSignBlock(VOID_ASPECT_HANGING_SIGN.get(), VOID_ASPECT_WALL_HANGING_SIGN.get(),
				provider.blockTexture(VOID_ASPECT_PLANKS.asBlock()));
		provider.flatItem(VOID_ASPECT_HANGING_SIGN_ITEM, MSBlockStateProvider::itemTexture);
		
		
		
		
	}
	
	public static void addLootTables(MSBlockLootTables provider)
	{
		provider.dropSelf(BLOOD_ASPECT_LOG.asBlock());
		provider.dropSelf(BLOOD_ASPECT_WOOD.asBlock());
		provider.dropSelf(BLOOD_ASPECT_STRIPPED_LOG.asBlock());
		provider.dropSelf(BLOOD_ASPECT_STRIPPED_WOOD.asBlock());
		provider.add(BLOOD_ASPECT_LEAVES.asBlock(), block ->
				provider.createLeavesDrops(block, BLOOD_ASPECT_SAPLING.asBlock(), MSBlockLootTables.SAPLING_CHANCES));
		provider.dropSelf(BLOOD_ASPECT_SAPLING.asBlock());
		provider.dropPottedContents(POTTED_BLOOD_ASPECT_SAPLING.get());
		provider.dropSelf(BLOOD_ASPECT_PLANKS.asBlock());
		provider.dropSelf(BLOOD_ASPECT_CARVED_PLANKS.asBlock());
		provider.dropSelf(BLOOD_ASPECT_STAIRS.asBlock());
		provider.add(BLOOD_ASPECT_SLAB.asBlock(), provider::createSlabItemTable);
		provider.dropSelf(BLOOD_ASPECT_FENCE.asBlock());
		provider.dropSelf(BLOOD_ASPECT_FENCE_GATE.asBlock());
		provider.add(BLOOD_ASPECT_DOOR.asBlock(), provider::createDoorTable);
		provider.dropSelf(BLOOD_ASPECT_TRAPDOOR.asBlock());
		provider.dropSelf(BLOOD_ASPECT_PRESSURE_PLATE.asBlock());
		provider.dropSelf(BLOOD_ASPECT_BUTTON.asBlock());
		provider.add(BLOOD_ASPECT_BOOKSHELF.asBlock(), provider::bookshelfDrop);
		provider.dropSelf(BLOOD_ASPECT_LADDER.asBlock());
		provider.add(BLOOD_ASPECT_SIGN.get(), block ->
				provider.createSingleItemTable(BLOOD_ASPECT_SIGN_ITEM.get()));
		provider.add(BLOOD_ASPECT_WALL_SIGN.get(), block ->
				provider.createSingleItemTable(BLOOD_ASPECT_SIGN_ITEM.get()));
		provider.add(BLOOD_ASPECT_HANGING_SIGN.get(), block ->
				provider.createSingleItemTable(BLOOD_ASPECT_HANGING_SIGN_ITEM.get()));
		provider.add(BLOOD_ASPECT_WALL_HANGING_SIGN.get(), block ->
				provider.createSingleItemTable(BLOOD_ASPECT_HANGING_SIGN_ITEM.get()));
		
		provider.dropSelf(BREATH_ASPECT_LOG.asBlock());
		provider.dropSelf(BREATH_ASPECT_WOOD.asBlock());
		provider.dropSelf(BREATH_ASPECT_STRIPPED_LOG.asBlock());
		provider.dropSelf(BREATH_ASPECT_STRIPPED_WOOD.asBlock());
		provider.add(BREATH_ASPECT_LEAVES.asBlock(), block ->
				provider.createLeavesDrops(block, BREATH_ASPECT_SAPLING.asBlock(), MSBlockLootTables.SAPLING_CHANCES));
		provider.dropSelf(BREATH_ASPECT_SAPLING.asBlock());
		provider.dropPottedContents(POTTED_BREATH_ASPECT_SAPLING.get());
		provider.dropSelf(BREATH_ASPECT_PLANKS.asBlock());
		provider.dropSelf(BREATH_ASPECT_CARVED_PLANKS.asBlock());
		provider.dropSelf(BREATH_ASPECT_STAIRS.asBlock());
		provider.add(BREATH_ASPECT_SLAB.asBlock(), provider::createSlabItemTable);
		provider.dropSelf(BREATH_ASPECT_FENCE.asBlock());
		provider.dropSelf(BREATH_ASPECT_FENCE_GATE.asBlock());
		provider.add(BREATH_ASPECT_DOOR.asBlock(), provider::createDoorTable);
		provider.dropSelf(BREATH_ASPECT_TRAPDOOR.asBlock());
		provider.dropSelf(BREATH_ASPECT_PRESSURE_PLATE.asBlock());
		provider.dropSelf(BREATH_ASPECT_BUTTON.asBlock());
		provider.add(BREATH_ASPECT_BOOKSHELF.asBlock(), provider::bookshelfDrop);
		provider.dropSelf(BREATH_ASPECT_LADDER.asBlock());
		provider.add(BREATH_ASPECT_SIGN.get(), block ->
				provider.createSingleItemTable(BREATH_ASPECT_SIGN_ITEM.get()));
		provider.add(BREATH_ASPECT_WALL_SIGN.get(), block ->
				provider.createSingleItemTable(BREATH_ASPECT_SIGN_ITEM.get()));
		provider.add(BREATH_ASPECT_HANGING_SIGN.get(), block ->
				provider.createSingleItemTable(BREATH_ASPECT_HANGING_SIGN_ITEM.get()));
		provider.add(BREATH_ASPECT_WALL_HANGING_SIGN.get(), block ->
				provider.createSingleItemTable(BREATH_ASPECT_HANGING_SIGN_ITEM.get()));
		
		provider.dropSelf(DOOM_ASPECT_LOG.asBlock());
		provider.dropSelf(DOOM_ASPECT_WOOD.asBlock());
		provider.dropSelf(DOOM_ASPECT_STRIPPED_LOG.asBlock());
		provider.dropSelf(DOOM_ASPECT_STRIPPED_WOOD.asBlock());
		provider.add(DOOM_ASPECT_LEAVES.asBlock(), block ->
				provider.createLeavesDrops(block, DOOM_ASPECT_SAPLING.asBlock(), MSBlockLootTables.SAPLING_CHANCES));
		provider.dropSelf(DOOM_ASPECT_SAPLING.asBlock());
		provider.dropPottedContents(POTTED_DOOM_ASPECT_SAPLING.get());
		provider.dropSelf(DOOM_ASPECT_PLANKS.asBlock());
		provider.dropSelf(DOOM_ASPECT_CARVED_PLANKS.asBlock());
		provider.dropSelf(DOOM_ASPECT_STAIRS.asBlock());
		provider.add(DOOM_ASPECT_SLAB.asBlock(), provider::createSlabItemTable);
		provider.dropSelf(DOOM_ASPECT_FENCE.asBlock());
		provider.dropSelf(DOOM_ASPECT_FENCE_GATE.asBlock());
		provider.add(DOOM_ASPECT_DOOR.asBlock(), provider::createDoorTable);
		provider.dropSelf(DOOM_ASPECT_TRAPDOOR.asBlock());
		provider.dropSelf(DOOM_ASPECT_PRESSURE_PLATE.asBlock());
		provider.dropSelf(DOOM_ASPECT_BUTTON.asBlock());
		provider.add(DOOM_ASPECT_BOOKSHELF.asBlock(), provider::bookshelfDrop);
		provider.dropSelf(DOOM_ASPECT_LADDER.asBlock());
		provider.add(DOOM_ASPECT_SIGN.get(), block ->
				provider.createSingleItemTable(DOOM_ASPECT_SIGN_ITEM.get()));
		provider.add(DOOM_ASPECT_WALL_SIGN.get(), block ->
				provider.createSingleItemTable(DOOM_ASPECT_SIGN_ITEM.get()));
		provider.add(DOOM_ASPECT_HANGING_SIGN.get(), block ->
				provider.createSingleItemTable(DOOM_ASPECT_HANGING_SIGN_ITEM.get()));
		provider.add(DOOM_ASPECT_WALL_HANGING_SIGN.get(), block ->
				provider.createSingleItemTable(DOOM_ASPECT_HANGING_SIGN_ITEM.get()));
		
		provider.dropSelf(HEART_ASPECT_LOG.asBlock());
		provider.dropSelf(HEART_ASPECT_WOOD.asBlock());
		provider.dropSelf(HEART_ASPECT_STRIPPED_LOG.asBlock());
		provider.dropSelf(HEART_ASPECT_STRIPPED_WOOD.asBlock());
		provider.add(HEART_ASPECT_LEAVES.asBlock(), block ->
				provider.createLeavesDrops(block, HEART_ASPECT_SAPLING.asBlock(), MSBlockLootTables.SAPLING_CHANCES));
		provider.dropSelf(HEART_ASPECT_SAPLING.asBlock());
		provider.dropPottedContents(POTTED_HEART_ASPECT_SAPLING.get());
		provider.dropSelf(HEART_ASPECT_PLANKS.asBlock());
		provider.dropSelf(HEART_ASPECT_CARVED_PLANKS.asBlock());
		provider.dropSelf(HEART_ASPECT_STAIRS.asBlock());
		provider.add(HEART_ASPECT_SLAB.asBlock(), provider::createSlabItemTable);
		provider.dropSelf(HEART_ASPECT_FENCE.asBlock());
		provider.dropSelf(HEART_ASPECT_FENCE_GATE.asBlock());
		provider.add(HEART_ASPECT_DOOR.asBlock(), provider::createDoorTable);
		provider.dropSelf(HEART_ASPECT_TRAPDOOR.asBlock());
		provider.dropSelf(HEART_ASPECT_PRESSURE_PLATE.asBlock());
		provider.dropSelf(HEART_ASPECT_BUTTON.asBlock());
		provider.add(HEART_ASPECT_BOOKSHELF.asBlock(), provider::bookshelfDrop);
		provider.dropSelf(HEART_ASPECT_LADDER.asBlock());
		provider.add(HEART_ASPECT_SIGN.get(), block ->
				provider.createSingleItemTable(HEART_ASPECT_SIGN_ITEM.get()));
		provider.add(HEART_ASPECT_WALL_SIGN.get(), block ->
				provider.createSingleItemTable(HEART_ASPECT_SIGN_ITEM.get()));
		provider.add(HEART_ASPECT_HANGING_SIGN.get(), block ->
				provider.createSingleItemTable(HEART_ASPECT_HANGING_SIGN_ITEM.get()));
		provider.add(HEART_ASPECT_WALL_HANGING_SIGN.get(), block ->
				provider.createSingleItemTable(HEART_ASPECT_HANGING_SIGN_ITEM.get()));
		
		provider.dropSelf(HOPE_ASPECT_LOG.asBlock());
		provider.dropSelf(HOPE_ASPECT_WOOD.asBlock());
		provider.dropSelf(HOPE_ASPECT_STRIPPED_LOG.asBlock());
		provider.dropSelf(HOPE_ASPECT_STRIPPED_WOOD.asBlock());
		provider.add(HOPE_ASPECT_LEAVES.asBlock(), block ->
				provider.createLeavesDrops(block, HOPE_ASPECT_SAPLING.asBlock(), MSBlockLootTables.SAPLING_CHANCES));
		provider.dropSelf(HOPE_ASPECT_SAPLING.asBlock());
		provider.dropPottedContents(POTTED_HOPE_ASPECT_SAPLING.get());
		provider.dropSelf(HOPE_ASPECT_PLANKS.asBlock());
		provider.dropSelf(HOPE_ASPECT_CARVED_PLANKS.asBlock());
		provider.dropSelf(HOPE_ASPECT_STAIRS.asBlock());
		provider.add(HOPE_ASPECT_SLAB.asBlock(), provider::createSlabItemTable);
		provider.dropSelf(HOPE_ASPECT_FENCE.asBlock());
		provider.dropSelf(HOPE_ASPECT_FENCE_GATE.asBlock());
		provider.add(HOPE_ASPECT_DOOR.asBlock(), provider::createDoorTable);
		provider.dropSelf(HOPE_ASPECT_TRAPDOOR.asBlock());
		provider.dropSelf(HOPE_ASPECT_PRESSURE_PLATE.asBlock());
		provider.dropSelf(HOPE_ASPECT_BUTTON.asBlock());
		provider.add(HOPE_ASPECT_BOOKSHELF.asBlock(), provider::bookshelfDrop);
		provider.dropSelf(HOPE_ASPECT_LADDER.asBlock());
		provider.add(HOPE_ASPECT_SIGN.get(), block ->
				provider.createSingleItemTable(HOPE_ASPECT_SIGN_ITEM.get()));
		provider.add(HOPE_ASPECT_WALL_SIGN.get(), block ->
				provider.createSingleItemTable(HOPE_ASPECT_SIGN_ITEM.get()));
		provider.add(HOPE_ASPECT_HANGING_SIGN.get(), block ->
				provider.createSingleItemTable(HOPE_ASPECT_HANGING_SIGN_ITEM.get()));
		provider.add(HOPE_ASPECT_WALL_HANGING_SIGN.get(), block ->
				provider.createSingleItemTable(HOPE_ASPECT_HANGING_SIGN_ITEM.get()));
		
		provider.dropSelf(LIFE_ASPECT_LOG.asBlock());
		provider.dropSelf(LIFE_ASPECT_WOOD.asBlock());
		provider.dropSelf(LIFE_ASPECT_STRIPPED_LOG.asBlock());
		provider.dropSelf(LIFE_ASPECT_STRIPPED_WOOD.asBlock());
		provider.add(LIFE_ASPECT_LEAVES.asBlock(), block ->
				provider.createLeavesDrops(block, LIFE_ASPECT_SAPLING.asBlock(), MSBlockLootTables.SAPLING_CHANCES));
		provider.dropSelf(LIFE_ASPECT_SAPLING.asBlock());
		provider.dropPottedContents(POTTED_LIFE_ASPECT_SAPLING.get());
		provider.dropSelf(LIFE_ASPECT_PLANKS.asBlock());
		provider.dropSelf(LIFE_ASPECT_CARVED_PLANKS.asBlock());
		provider.dropSelf(LIFE_ASPECT_STAIRS.asBlock());
		provider.add(LIFE_ASPECT_SLAB.asBlock(), provider::createSlabItemTable);
		provider.dropSelf(LIFE_ASPECT_FENCE.asBlock());
		provider.dropSelf(LIFE_ASPECT_FENCE_GATE.asBlock());
		provider.add(LIFE_ASPECT_DOOR.asBlock(), provider::createDoorTable);
		provider.dropSelf(LIFE_ASPECT_TRAPDOOR.asBlock());
		provider.dropSelf(LIFE_ASPECT_PRESSURE_PLATE.asBlock());
		provider.dropSelf(LIFE_ASPECT_BUTTON.asBlock());
		provider.add(LIFE_ASPECT_BOOKSHELF.asBlock(), provider::bookshelfDrop);
		provider.dropSelf(LIFE_ASPECT_LADDER.asBlock());
		provider.add(LIFE_ASPECT_SIGN.get(), block ->
				provider.createSingleItemTable(LIFE_ASPECT_SIGN_ITEM.get()));
		provider.add(LIFE_ASPECT_WALL_SIGN.get(), block ->
				provider.createSingleItemTable(LIFE_ASPECT_SIGN_ITEM.get()));
		provider.add(LIFE_ASPECT_HANGING_SIGN.get(), block ->
				provider.createSingleItemTable(LIFE_ASPECT_HANGING_SIGN_ITEM.get()));
		provider.add(LIFE_ASPECT_WALL_HANGING_SIGN.get(), block ->
				provider.createSingleItemTable(LIFE_ASPECT_HANGING_SIGN_ITEM.get()));
		
		provider.dropSelf(LIGHT_ASPECT_LOG.asBlock());
		provider.dropSelf(LIGHT_ASPECT_WOOD.asBlock());
		provider.dropSelf(LIGHT_ASPECT_STRIPPED_LOG.asBlock());
		provider.dropSelf(LIGHT_ASPECT_STRIPPED_WOOD.asBlock());
		provider.add(LIGHT_ASPECT_LEAVES.asBlock(), block ->
				provider.createLeavesDrops(block, LIGHT_ASPECT_SAPLING.asBlock(), MSBlockLootTables.SAPLING_CHANCES));
		provider.dropSelf(LIGHT_ASPECT_SAPLING.asBlock());
		provider.dropPottedContents(POTTED_LIGHT_ASPECT_SAPLING.get());
		provider.dropSelf(LIGHT_ASPECT_PLANKS.asBlock());
		provider.dropSelf(LIGHT_ASPECT_CARVED_PLANKS.asBlock());
		provider.dropSelf(LIGHT_ASPECT_STAIRS.asBlock());
		provider.add(LIGHT_ASPECT_SLAB.asBlock(), provider::createSlabItemTable);
		provider.dropSelf(LIGHT_ASPECT_FENCE.asBlock());
		provider.dropSelf(LIGHT_ASPECT_FENCE_GATE.asBlock());
		provider.add(LIGHT_ASPECT_DOOR.asBlock(), provider::createDoorTable);
		provider.dropSelf(LIGHT_ASPECT_TRAPDOOR.asBlock());
		provider.dropSelf(LIGHT_ASPECT_PRESSURE_PLATE.asBlock());
		provider.dropSelf(LIGHT_ASPECT_BUTTON.asBlock());
		provider.add(LIGHT_ASPECT_BOOKSHELF.asBlock(), provider::bookshelfDrop);
		provider.dropSelf(LIGHT_ASPECT_LADDER.asBlock());
		provider.add(LIGHT_ASPECT_SIGN.get(), block ->
				provider.createSingleItemTable(LIGHT_ASPECT_SIGN_ITEM.get()));
		provider.add(LIGHT_ASPECT_WALL_SIGN.get(), block ->
				provider.createSingleItemTable(LIGHT_ASPECT_SIGN_ITEM.get()));
		provider.add(LIGHT_ASPECT_HANGING_SIGN.get(), block ->
				provider.createSingleItemTable(LIGHT_ASPECT_HANGING_SIGN_ITEM.get()));
		provider.add(LIGHT_ASPECT_WALL_HANGING_SIGN.get(), block ->
				provider.createSingleItemTable(LIGHT_ASPECT_HANGING_SIGN_ITEM.get()));
		
		provider.dropSelf(MIND_ASPECT_LOG.asBlock());
		provider.dropSelf(MIND_ASPECT_WOOD.asBlock());
		provider.dropSelf(MIND_ASPECT_STRIPPED_LOG.asBlock());
		provider.dropSelf(MIND_ASPECT_STRIPPED_WOOD.asBlock());
		provider.add(MIND_ASPECT_LEAVES.asBlock(), block ->
				provider.createLeavesDrops(block, MIND_ASPECT_SAPLING.asBlock(), MSBlockLootTables.SAPLING_CHANCES));
		provider.dropSelf(MIND_ASPECT_SAPLING.asBlock());
		provider.dropPottedContents(POTTED_MIND_ASPECT_SAPLING.get());
		provider.dropSelf(MIND_ASPECT_PLANKS.asBlock());
		provider.dropSelf(MIND_ASPECT_CARVED_PLANKS.asBlock());
		provider.dropSelf(MIND_ASPECT_STAIRS.asBlock());
		provider.add(MIND_ASPECT_SLAB.asBlock(), provider::createSlabItemTable);
		provider.dropSelf(MIND_ASPECT_FENCE.asBlock());
		provider.dropSelf(MIND_ASPECT_FENCE_GATE.asBlock());
		provider.add(MIND_ASPECT_DOOR.asBlock(), provider::createDoorTable);
		provider.dropSelf(MIND_ASPECT_TRAPDOOR.asBlock());
		provider.dropSelf(MIND_ASPECT_PRESSURE_PLATE.asBlock());
		provider.dropSelf(MIND_ASPECT_BUTTON.asBlock());
		provider.add(MIND_ASPECT_BOOKSHELF.asBlock(), provider::bookshelfDrop);
		provider.dropSelf(MIND_ASPECT_LADDER.asBlock());
		provider.add(MIND_ASPECT_SIGN.get(), block ->
				provider.createSingleItemTable(MIND_ASPECT_SIGN_ITEM.get()));
		provider.add(MIND_ASPECT_WALL_SIGN.get(), block ->
				provider.createSingleItemTable(MIND_ASPECT_SIGN_ITEM.get()));
		provider.add(MIND_ASPECT_HANGING_SIGN.get(), block ->
				provider.createSingleItemTable(MIND_ASPECT_HANGING_SIGN_ITEM.get()));
		provider.add(MIND_ASPECT_WALL_HANGING_SIGN.get(), block ->
				provider.createSingleItemTable(MIND_ASPECT_HANGING_SIGN_ITEM.get()));
		
		provider.dropSelf(RAGE_ASPECT_LOG.asBlock());
		provider.dropSelf(RAGE_ASPECT_WOOD.asBlock());
		provider.dropSelf(RAGE_ASPECT_STRIPPED_LOG.asBlock());
		provider.dropSelf(RAGE_ASPECT_STRIPPED_WOOD.asBlock());
		provider.add(RAGE_ASPECT_LEAVES.asBlock(), block ->
				provider.createLeavesDrops(block, RAGE_ASPECT_SAPLING.asBlock(), MSBlockLootTables.SAPLING_CHANCES));
		provider.dropSelf(RAGE_ASPECT_SAPLING.asBlock());
		provider.dropPottedContents(POTTED_RAGE_ASPECT_SAPLING.get());
		provider.dropSelf(RAGE_ASPECT_PLANKS.asBlock());
		provider.dropSelf(RAGE_ASPECT_CARVED_PLANKS.asBlock());
		provider.dropSelf(RAGE_ASPECT_STAIRS.asBlock());
		provider.add(RAGE_ASPECT_SLAB.asBlock(), provider::createSlabItemTable);
		provider.dropSelf(RAGE_ASPECT_FENCE.asBlock());
		provider.dropSelf(RAGE_ASPECT_FENCE_GATE.asBlock());
		provider.add(RAGE_ASPECT_DOOR.asBlock(), provider::createDoorTable);
		provider.dropSelf(RAGE_ASPECT_TRAPDOOR.asBlock());
		provider.dropSelf(RAGE_ASPECT_PRESSURE_PLATE.asBlock());
		provider.dropSelf(RAGE_ASPECT_BUTTON.asBlock());
		provider.add(RAGE_ASPECT_BOOKSHELF.asBlock(), provider::bookshelfDrop);
		provider.dropSelf(RAGE_ASPECT_LADDER.asBlock());
		provider.add(RAGE_ASPECT_SIGN.get(), block ->
				provider.createSingleItemTable(RAGE_ASPECT_SIGN_ITEM.get()));
		provider.add(RAGE_ASPECT_WALL_SIGN.get(), block ->
				provider.createSingleItemTable(RAGE_ASPECT_SIGN_ITEM.get()));
		provider.add(RAGE_ASPECT_HANGING_SIGN.get(), block ->
				provider.createSingleItemTable(RAGE_ASPECT_HANGING_SIGN_ITEM.get()));
		provider.add(RAGE_ASPECT_WALL_HANGING_SIGN.get(), block ->
				provider.createSingleItemTable(RAGE_ASPECT_HANGING_SIGN_ITEM.get()));
		
		provider.dropSelf(SPACE_ASPECT_LOG.asBlock());
		provider.dropSelf(SPACE_ASPECT_WOOD.asBlock());
		provider.dropSelf(SPACE_ASPECT_STRIPPED_LOG.asBlock());
		provider.dropSelf(SPACE_ASPECT_STRIPPED_WOOD.asBlock());
		provider.add(SPACE_ASPECT_LEAVES.asBlock(), block ->
				provider.createLeavesDrops(block, SPACE_ASPECT_SAPLING.asBlock(), MSBlockLootTables.SAPLING_CHANCES));
		provider.dropSelf(SPACE_ASPECT_SAPLING.asBlock());
		provider.dropPottedContents(POTTED_SPACE_ASPECT_SAPLING.get());
		provider.dropSelf(SPACE_ASPECT_PLANKS.asBlock());
		provider.dropSelf(SPACE_ASPECT_CARVED_PLANKS.asBlock());
		provider.dropSelf(SPACE_ASPECT_STAIRS.asBlock());
		provider.add(SPACE_ASPECT_SLAB.asBlock(), provider::createSlabItemTable);
		provider.dropSelf(SPACE_ASPECT_FENCE.asBlock());
		provider.dropSelf(SPACE_ASPECT_FENCE_GATE.asBlock());
		provider.add(SPACE_ASPECT_DOOR.asBlock(), provider::createDoorTable);
		provider.dropSelf(SPACE_ASPECT_TRAPDOOR.asBlock());
		provider.dropSelf(SPACE_ASPECT_PRESSURE_PLATE.asBlock());
		provider.dropSelf(SPACE_ASPECT_BUTTON.asBlock());
		provider.add(SPACE_ASPECT_BOOKSHELF.asBlock(), provider::bookshelfDrop);
		provider.dropSelf(SPACE_ASPECT_LADDER.asBlock());
		provider.add(SPACE_ASPECT_SIGN.get(), block ->
				provider.createSingleItemTable(SPACE_ASPECT_SIGN_ITEM.get()));
		provider.add(SPACE_ASPECT_WALL_SIGN.get(), block ->
				provider.createSingleItemTable(SPACE_ASPECT_SIGN_ITEM.get()));
		provider.add(SPACE_ASPECT_HANGING_SIGN.get(), block ->
				provider.createSingleItemTable(SPACE_ASPECT_HANGING_SIGN_ITEM.get()));
		provider.add(SPACE_ASPECT_WALL_HANGING_SIGN.get(), block ->
				provider.createSingleItemTable(SPACE_ASPECT_HANGING_SIGN_ITEM.get()));
		
		provider.dropSelf(TIME_ASPECT_LOG.asBlock());
		provider.dropSelf(TIME_ASPECT_WOOD.asBlock());
		provider.dropSelf(TIME_ASPECT_STRIPPED_LOG.asBlock());
		provider.dropSelf(TIME_ASPECT_STRIPPED_WOOD.asBlock());
		provider.add(TIME_ASPECT_LEAVES.asBlock(), block ->
				provider.createLeavesDrops(block, TIME_ASPECT_SAPLING.asBlock(), MSBlockLootTables.SAPLING_CHANCES));
		provider.dropSelf(TIME_ASPECT_SAPLING.asBlock());
		provider.dropPottedContents(POTTED_TIME_ASPECT_SAPLING.get());
		provider.dropSelf(TIME_ASPECT_PLANKS.asBlock());
		provider.dropSelf(TIME_ASPECT_CARVED_PLANKS.asBlock());
		provider.dropSelf(TIME_ASPECT_STAIRS.asBlock());
		provider.add(TIME_ASPECT_SLAB.asBlock(), provider::createSlabItemTable);
		provider.dropSelf(TIME_ASPECT_FENCE.asBlock());
		provider.dropSelf(TIME_ASPECT_FENCE_GATE.asBlock());
		provider.add(TIME_ASPECT_DOOR.asBlock(), provider::createDoorTable);
		provider.dropSelf(TIME_ASPECT_TRAPDOOR.asBlock());
		provider.dropSelf(TIME_ASPECT_PRESSURE_PLATE.asBlock());
		provider.dropSelf(TIME_ASPECT_BUTTON.asBlock());
		provider.add(TIME_ASPECT_BOOKSHELF.asBlock(), provider::bookshelfDrop);
		provider.dropSelf(TIME_ASPECT_LADDER.asBlock());
		provider.add(TIME_ASPECT_SIGN.get(), block ->
				provider.createSingleItemTable(TIME_ASPECT_SIGN_ITEM.get()));
		provider.add(TIME_ASPECT_WALL_SIGN.get(), block ->
				provider.createSingleItemTable(TIME_ASPECT_SIGN_ITEM.get()));
		provider.add(TIME_ASPECT_HANGING_SIGN.get(), block ->
				provider.createSingleItemTable(TIME_ASPECT_HANGING_SIGN_ITEM.get()));
		provider.add(TIME_ASPECT_WALL_HANGING_SIGN.get(), block ->
				provider.createSingleItemTable(TIME_ASPECT_HANGING_SIGN_ITEM.get()));
		
		provider.dropSelf(VOID_ASPECT_LOG.asBlock());
		provider.dropSelf(VOID_ASPECT_WOOD.asBlock());
		provider.dropSelf(VOID_ASPECT_STRIPPED_LOG.asBlock());
		provider.dropSelf(VOID_ASPECT_STRIPPED_WOOD.asBlock());
		provider.add(VOID_ASPECT_LEAVES.asBlock(), block ->
				provider.createLeavesDrops(block, VOID_ASPECT_SAPLING.asBlock(), MSBlockLootTables.SAPLING_CHANCES));
		provider.dropSelf(VOID_ASPECT_SAPLING.asBlock());
		provider.dropPottedContents(POTTED_VOID_ASPECT_SAPLING.get());
		provider.dropSelf(VOID_ASPECT_PLANKS.asBlock());
		provider.dropSelf(VOID_ASPECT_CARVED_PLANKS.asBlock());
		provider.dropSelf(VOID_ASPECT_STAIRS.asBlock());
		provider.add(VOID_ASPECT_SLAB.asBlock(), provider::createSlabItemTable);
		provider.dropSelf(VOID_ASPECT_FENCE.asBlock());
		provider.dropSelf(VOID_ASPECT_FENCE_GATE.asBlock());
		provider.add(VOID_ASPECT_DOOR.asBlock(), provider::createDoorTable);
		provider.dropSelf(VOID_ASPECT_TRAPDOOR.asBlock());
		provider.dropSelf(VOID_ASPECT_PRESSURE_PLATE.asBlock());
		provider.dropSelf(VOID_ASPECT_BUTTON.asBlock());
		provider.add(VOID_ASPECT_BOOKSHELF.asBlock(), provider::bookshelfDrop);
		provider.dropSelf(VOID_ASPECT_LADDER.asBlock());
		provider.add(VOID_ASPECT_SIGN.get(), block ->
				provider.createSingleItemTable(VOID_ASPECT_SIGN_ITEM.get()));
		provider.add(VOID_ASPECT_WALL_SIGN.get(), block ->
				provider.createSingleItemTable(VOID_ASPECT_SIGN_ITEM.get()));
		provider.add(VOID_ASPECT_HANGING_SIGN.get(), block ->
				provider.createSingleItemTable(VOID_ASPECT_HANGING_SIGN_ITEM.get()));
		provider.add(VOID_ASPECT_WALL_HANGING_SIGN.get(), block ->
				provider.createSingleItemTable(VOID_ASPECT_HANGING_SIGN_ITEM.get()));
	}
	
	public static void addToBlockTags(MinestuckBlockTagsProvider provider)
	{
		provider.tag(BlockTags.MINEABLE_WITH_AXE).add(BLOOD_ASPECT_LOG.asBlock(), BLOOD_ASPECT_WOOD.asBlock(),
				BLOOD_ASPECT_STRIPPED_LOG.asBlock(), BLOOD_ASPECT_STRIPPED_WOOD.asBlock(),
				BLOOD_ASPECT_PLANKS.asBlock(), BLOOD_ASPECT_CARVED_PLANKS.asBlock(),
				BLOOD_ASPECT_STAIRS.asBlock(), BLOOD_ASPECT_SLAB.asBlock(),
				BLOOD_ASPECT_FENCE.asBlock(), BLOOD_ASPECT_FENCE_GATE.asBlock(),
				BLOOD_ASPECT_PRESSURE_PLATE.asBlock(), BLOOD_ASPECT_BUTTON.asBlock(),
				BLOOD_ASPECT_BOOKSHELF.asBlock(), BLOOD_ASPECT_LADDER.asBlock(),
				BLOOD_ASPECT_DOOR.asBlock(),BLOOD_ASPECT_TRAPDOOR.asBlock());
		provider.tag(BlockTags.MINEABLE_WITH_AXE).add(BREATH_ASPECT_LOG.asBlock(), BREATH_ASPECT_WOOD.asBlock(),
				BREATH_ASPECT_STRIPPED_LOG.asBlock(), BREATH_ASPECT_STRIPPED_WOOD.asBlock(),
				BREATH_ASPECT_PLANKS.asBlock(), BREATH_ASPECT_CARVED_PLANKS.asBlock(),
				BREATH_ASPECT_STAIRS.asBlock(), BREATH_ASPECT_SLAB.asBlock(),
				BREATH_ASPECT_FENCE.asBlock(), BREATH_ASPECT_FENCE_GATE.asBlock(),
				BREATH_ASPECT_PRESSURE_PLATE.asBlock(), BREATH_ASPECT_BUTTON.asBlock(),
				BREATH_ASPECT_BOOKSHELF.asBlock(), BREATH_ASPECT_LADDER.asBlock(),
				BREATH_ASPECT_DOOR.asBlock(), BREATH_ASPECT_TRAPDOOR.asBlock());
		provider.tag(BlockTags.MINEABLE_WITH_AXE).add(DOOM_ASPECT_LOG.asBlock(), DOOM_ASPECT_WOOD.asBlock(),
				DOOM_ASPECT_STRIPPED_LOG.asBlock(), DOOM_ASPECT_STRIPPED_WOOD.asBlock(),
				DOOM_ASPECT_PLANKS.asBlock(), DOOM_ASPECT_CARVED_PLANKS.asBlock(),
				DOOM_ASPECT_STAIRS.asBlock(), DOOM_ASPECT_SLAB.asBlock(),
				DOOM_ASPECT_FENCE.asBlock(), DOOM_ASPECT_FENCE_GATE.asBlock(),
				DOOM_ASPECT_PRESSURE_PLATE.asBlock(), DOOM_ASPECT_BUTTON.asBlock(),
				DOOM_ASPECT_BOOKSHELF.asBlock(), DOOM_ASPECT_LADDER.asBlock(),
				DOOM_ASPECT_DOOR.asBlock(), DOOM_ASPECT_TRAPDOOR.asBlock());
		provider.tag(BlockTags.MINEABLE_WITH_AXE).add(HEART_ASPECT_LOG.asBlock(), HEART_ASPECT_WOOD.asBlock(),
				HEART_ASPECT_STRIPPED_LOG.asBlock(), HEART_ASPECT_STRIPPED_WOOD.asBlock(),
				HEART_ASPECT_PLANKS.asBlock(), HEART_ASPECT_CARVED_PLANKS.asBlock(),
				HEART_ASPECT_STAIRS.asBlock(), HEART_ASPECT_SLAB.asBlock(),
				HEART_ASPECT_FENCE.asBlock(), HEART_ASPECT_FENCE_GATE.asBlock(),
				HEART_ASPECT_PRESSURE_PLATE.asBlock(), HEART_ASPECT_BUTTON.asBlock(),
				HEART_ASPECT_BOOKSHELF.asBlock(), HEART_ASPECT_LADDER.asBlock(),
				HEART_ASPECT_DOOR.asBlock(), HEART_ASPECT_TRAPDOOR.asBlock());
		provider.tag(BlockTags.MINEABLE_WITH_AXE).add(HOPE_ASPECT_LOG.asBlock(), HOPE_ASPECT_WOOD.asBlock(),
				HOPE_ASPECT_STRIPPED_LOG.asBlock(), HOPE_ASPECT_STRIPPED_WOOD.asBlock(),
				HOPE_ASPECT_PLANKS.asBlock(), HOPE_ASPECT_CARVED_PLANKS.asBlock(),
				HOPE_ASPECT_STAIRS.asBlock(), HOPE_ASPECT_SLAB.asBlock(),
				HOPE_ASPECT_FENCE.asBlock(), HOPE_ASPECT_FENCE_GATE.asBlock(),
				HOPE_ASPECT_PRESSURE_PLATE.asBlock(), HOPE_ASPECT_BUTTON.asBlock(),
				HOPE_ASPECT_BOOKSHELF.asBlock(), HOPE_ASPECT_LADDER.asBlock(),
				HOPE_ASPECT_DOOR.asBlock(), HOPE_ASPECT_TRAPDOOR.asBlock());
		provider.tag(BlockTags.MINEABLE_WITH_AXE).add(LIFE_ASPECT_LOG.asBlock(), LIFE_ASPECT_WOOD.asBlock(),
				LIFE_ASPECT_STRIPPED_LOG.asBlock(), LIFE_ASPECT_STRIPPED_WOOD.asBlock(),
				LIFE_ASPECT_PLANKS.asBlock(), LIFE_ASPECT_CARVED_PLANKS.asBlock(),
				LIFE_ASPECT_STAIRS.asBlock(), LIFE_ASPECT_SLAB.asBlock(),
				LIFE_ASPECT_FENCE.asBlock(), LIFE_ASPECT_FENCE_GATE.asBlock(),
				LIFE_ASPECT_PRESSURE_PLATE.asBlock(), LIFE_ASPECT_BUTTON.asBlock(),
				LIFE_ASPECT_BOOKSHELF.asBlock(), LIFE_ASPECT_LADDER.asBlock(),
				LIFE_ASPECT_DOOR.asBlock(), LIFE_ASPECT_TRAPDOOR.asBlock());
		provider.tag(BlockTags.MINEABLE_WITH_AXE).add(LIGHT_ASPECT_LOG.asBlock(), LIGHT_ASPECT_WOOD.asBlock(),
				LIGHT_ASPECT_STRIPPED_LOG.asBlock(), LIGHT_ASPECT_STRIPPED_WOOD.asBlock(),
				LIGHT_ASPECT_PLANKS.asBlock(), LIGHT_ASPECT_CARVED_PLANKS.asBlock(),
				LIGHT_ASPECT_STAIRS.asBlock(), LIGHT_ASPECT_SLAB.asBlock(),
				LIGHT_ASPECT_FENCE.asBlock(), LIGHT_ASPECT_FENCE_GATE.asBlock(),
				LIGHT_ASPECT_PRESSURE_PLATE.asBlock(), LIGHT_ASPECT_BUTTON.asBlock(),
				LIGHT_ASPECT_BOOKSHELF.asBlock(), LIGHT_ASPECT_LADDER.asBlock(),
				LIGHT_ASPECT_DOOR.asBlock(), LIGHT_ASPECT_TRAPDOOR.asBlock());
		provider.tag(BlockTags.MINEABLE_WITH_AXE).add(MIND_ASPECT_LOG.asBlock(), MIND_ASPECT_WOOD.asBlock(),
				MIND_ASPECT_STRIPPED_LOG.asBlock(), MIND_ASPECT_STRIPPED_WOOD.asBlock(),
				MIND_ASPECT_PLANKS.asBlock(), MIND_ASPECT_CARVED_PLANKS.asBlock(),
				MIND_ASPECT_STAIRS.asBlock(), MIND_ASPECT_SLAB.asBlock(),
				MIND_ASPECT_FENCE.asBlock(), MIND_ASPECT_FENCE_GATE.asBlock(),
				MIND_ASPECT_PRESSURE_PLATE.asBlock(), MIND_ASPECT_BUTTON.asBlock(),
				MIND_ASPECT_BOOKSHELF.asBlock(), MIND_ASPECT_LADDER.asBlock(),
				MIND_ASPECT_DOOR.asBlock(), MIND_ASPECT_TRAPDOOR.asBlock());
		provider.tag(BlockTags.MINEABLE_WITH_AXE).add(RAGE_ASPECT_LOG.asBlock(), RAGE_ASPECT_WOOD.asBlock(),
				RAGE_ASPECT_STRIPPED_LOG.asBlock(), RAGE_ASPECT_STRIPPED_WOOD.asBlock(),
				RAGE_ASPECT_PLANKS.asBlock(), RAGE_ASPECT_CARVED_PLANKS.asBlock(),
				RAGE_ASPECT_STAIRS.asBlock(), RAGE_ASPECT_SLAB.asBlock(),
				RAGE_ASPECT_FENCE.asBlock(), RAGE_ASPECT_FENCE_GATE.asBlock(),
				RAGE_ASPECT_PRESSURE_PLATE.asBlock(), RAGE_ASPECT_BUTTON.asBlock(),
				RAGE_ASPECT_BOOKSHELF.asBlock(), RAGE_ASPECT_LADDER.asBlock(),
				RAGE_ASPECT_DOOR.asBlock(), RAGE_ASPECT_TRAPDOOR.asBlock());
		provider.tag(BlockTags.MINEABLE_WITH_AXE).add(SPACE_ASPECT_LOG.asBlock(), SPACE_ASPECT_WOOD.asBlock(),
				SPACE_ASPECT_STRIPPED_LOG.asBlock(), SPACE_ASPECT_STRIPPED_WOOD.asBlock(),
				SPACE_ASPECT_PLANKS.asBlock(), SPACE_ASPECT_CARVED_PLANKS.asBlock(),
				SPACE_ASPECT_STAIRS.asBlock(), SPACE_ASPECT_SLAB.asBlock(),
				SPACE_ASPECT_FENCE.asBlock(), SPACE_ASPECT_FENCE_GATE.asBlock(),
				SPACE_ASPECT_PRESSURE_PLATE.asBlock(), SPACE_ASPECT_BUTTON.asBlock(),
				SPACE_ASPECT_BOOKSHELF.asBlock(), SPACE_ASPECT_LADDER.asBlock(),
				SPACE_ASPECT_DOOR.asBlock(), SPACE_ASPECT_TRAPDOOR.asBlock());
		provider.tag(BlockTags.MINEABLE_WITH_AXE).add(TIME_ASPECT_LOG.asBlock(), TIME_ASPECT_WOOD.asBlock(),
				TIME_ASPECT_STRIPPED_LOG.asBlock(), TIME_ASPECT_STRIPPED_WOOD.asBlock(),
				TIME_ASPECT_PLANKS.asBlock(), TIME_ASPECT_CARVED_PLANKS.asBlock(),
				TIME_ASPECT_STAIRS.asBlock(), TIME_ASPECT_SLAB.asBlock(),
				TIME_ASPECT_FENCE.asBlock(), TIME_ASPECT_FENCE_GATE.asBlock(),
				TIME_ASPECT_PRESSURE_PLATE.asBlock(), TIME_ASPECT_BUTTON.asBlock(),
				TIME_ASPECT_BOOKSHELF.asBlock(), TIME_ASPECT_LADDER.asBlock(),
				TIME_ASPECT_DOOR.asBlock(), TIME_ASPECT_TRAPDOOR.asBlock());
		provider.tag(BlockTags.MINEABLE_WITH_AXE).add(VOID_ASPECT_LOG.asBlock(), VOID_ASPECT_WOOD.asBlock(),
				VOID_ASPECT_STRIPPED_LOG.asBlock(), VOID_ASPECT_STRIPPED_WOOD.asBlock(),
				VOID_ASPECT_PLANKS.asBlock(), VOID_ASPECT_CARVED_PLANKS.asBlock(),
				VOID_ASPECT_STAIRS.asBlock(), VOID_ASPECT_SLAB.asBlock(),
				VOID_ASPECT_FENCE.asBlock(), VOID_ASPECT_FENCE_GATE.asBlock(),
				VOID_ASPECT_PRESSURE_PLATE.asBlock(), VOID_ASPECT_BUTTON.asBlock(),
				VOID_ASPECT_BOOKSHELF.asBlock(), VOID_ASPECT_LADDER.asBlock(),
				VOID_ASPECT_DOOR.asBlock(), VOID_ASPECT_TRAPDOOR.asBlock());
		
		provider.tag(MSTags.Blocks.ASPECT_LOGS).add(BLOOD_ASPECT_LOG.asBlock(), BREATH_ASPECT_LOG.asBlock(), DOOM_ASPECT_LOG.asBlock(), HEART_ASPECT_LOG.asBlock(),
				HOPE_ASPECT_LOG.asBlock(), LIFE_ASPECT_LOG.asBlock(), LIGHT_ASPECT_LOG.asBlock(), MIND_ASPECT_LOG.asBlock(),
				RAGE_ASPECT_LOG.asBlock(), SPACE_ASPECT_LOG.asBlock(), TIME_ASPECT_LOG.asBlock(), VOID_ASPECT_LOG.asBlock(),
				BLOOD_ASPECT_STRIPPED_LOG.asBlock(), BREATH_ASPECT_STRIPPED_LOG.asBlock(), DOOM_ASPECT_STRIPPED_LOG.asBlock(), HEART_ASPECT_STRIPPED_LOG.asBlock(),
				HOPE_ASPECT_STRIPPED_LOG.asBlock(), LIFE_ASPECT_STRIPPED_LOG.asBlock(), LIGHT_ASPECT_STRIPPED_LOG.asBlock(), MIND_ASPECT_STRIPPED_LOG.asBlock(),
				RAGE_ASPECT_STRIPPED_LOG.asBlock(), SPACE_ASPECT_STRIPPED_LOG.asBlock(), TIME_ASPECT_STRIPPED_LOG.asBlock(), VOID_ASPECT_STRIPPED_LOG.asBlock());
		provider.tag(MSTags.Blocks.ASPECT_WOOD).add(BLOOD_ASPECT_WOOD.asBlock(), BREATH_ASPECT_WOOD.asBlock(), DOOM_ASPECT_WOOD.asBlock(), HEART_ASPECT_WOOD.asBlock(),
				HOPE_ASPECT_WOOD.asBlock(), LIFE_ASPECT_WOOD.asBlock(), LIGHT_ASPECT_WOOD.asBlock(), MIND_ASPECT_WOOD.asBlock(),
				RAGE_ASPECT_WOOD.asBlock(), SPACE_ASPECT_WOOD.asBlock(), TIME_ASPECT_WOOD.asBlock(), VOID_ASPECT_WOOD.asBlock(),
				BLOOD_ASPECT_STRIPPED_WOOD.asBlock(), BREATH_ASPECT_STRIPPED_WOOD.asBlock(), DOOM_ASPECT_STRIPPED_WOOD.asBlock(), HEART_ASPECT_STRIPPED_WOOD.asBlock(),
				HOPE_ASPECT_STRIPPED_WOOD.asBlock(), LIFE_ASPECT_STRIPPED_WOOD.asBlock(), LIGHT_ASPECT_STRIPPED_WOOD.asBlock(), MIND_ASPECT_STRIPPED_WOOD.asBlock(),
				RAGE_ASPECT_STRIPPED_WOOD.asBlock(), SPACE_ASPECT_STRIPPED_WOOD.asBlock(), TIME_ASPECT_STRIPPED_WOOD.asBlock(), VOID_ASPECT_STRIPPED_WOOD.asBlock());
		provider.tag(MSTags.Blocks.ASPECT_PLANKS).add(BLOOD_ASPECT_PLANKS.asBlock(), BREATH_ASPECT_PLANKS.asBlock(), DOOM_ASPECT_PLANKS.asBlock(), HEART_ASPECT_PLANKS.asBlock(),
				HOPE_ASPECT_PLANKS.asBlock(), LIFE_ASPECT_PLANKS.asBlock(), LIGHT_ASPECT_PLANKS.asBlock(), MIND_ASPECT_PLANKS.asBlock(),
				RAGE_ASPECT_PLANKS.asBlock(), SPACE_ASPECT_PLANKS.asBlock(), TIME_ASPECT_PLANKS.asBlock(), VOID_ASPECT_PLANKS.asBlock());
		provider.tag(MSTags.Blocks.ASPECT_SLABS).add(BLOOD_ASPECT_SLAB.asBlock(), BREATH_ASPECT_SLAB.asBlock(), DOOM_ASPECT_SLAB.asBlock(), HEART_ASPECT_SLAB.asBlock(),
				HOPE_ASPECT_SLAB.asBlock(), LIFE_ASPECT_SLAB.asBlock(), LIGHT_ASPECT_SLAB.asBlock(), MIND_ASPECT_SLAB.asBlock(),
				RAGE_ASPECT_SLAB.asBlock(), SPACE_ASPECT_SLAB.asBlock(), TIME_ASPECT_SLAB.asBlock(), VOID_ASPECT_SLAB.asBlock());
		provider.tag(MSTags.Blocks.ASPECT_LEAVES).add(BLOOD_ASPECT_LEAVES.asBlock(), BREATH_ASPECT_LEAVES.asBlock(), DOOM_ASPECT_LEAVES.asBlock(), HEART_ASPECT_LEAVES.asBlock(),
				HOPE_ASPECT_LEAVES.asBlock(), LIFE_ASPECT_LEAVES.asBlock(), LIGHT_ASPECT_LEAVES.asBlock(), MIND_ASPECT_LEAVES.asBlock(),
				RAGE_ASPECT_LEAVES.asBlock(), SPACE_ASPECT_LEAVES.asBlock(), TIME_ASPECT_LEAVES.asBlock(), VOID_ASPECT_LEAVES.asBlock());
		provider.tag(MSTags.Blocks.ASPECT_SAPLINGS).add(BLOOD_ASPECT_SAPLING.asBlock(), BREATH_ASPECT_SAPLING.asBlock(), DOOM_ASPECT_SAPLING.asBlock(), HEART_ASPECT_SAPLING.asBlock(),
				HOPE_ASPECT_SAPLING.asBlock(), LIFE_ASPECT_SAPLING.asBlock(), LIGHT_ASPECT_SAPLING.asBlock(), MIND_ASPECT_SAPLING.asBlock(),
				RAGE_ASPECT_SAPLING.asBlock(), SPACE_ASPECT_SAPLING.asBlock(), TIME_ASPECT_SAPLING.asBlock(), VOID_ASPECT_SAPLING.asBlock());
		provider.tag(MSTags.Blocks.ASPECT_BOOKSHELVES).add(BLOOD_ASPECT_BOOKSHELF.asBlock(), BREATH_ASPECT_BOOKSHELF.asBlock(), DOOM_ASPECT_BOOKSHELF.asBlock(), HEART_ASPECT_BOOKSHELF.asBlock(),
				HOPE_ASPECT_BOOKSHELF.asBlock(), LIFE_ASPECT_BOOKSHELF.asBlock(), LIGHT_ASPECT_BOOKSHELF.asBlock(), MIND_ASPECT_BOOKSHELF.asBlock(),
				RAGE_ASPECT_BOOKSHELF.asBlock(), SPACE_ASPECT_BOOKSHELF.asBlock(), TIME_ASPECT_BOOKSHELF.asBlock(), VOID_ASPECT_BOOKSHELF.asBlock());
		provider.tag(MSTags.Blocks.ASPECT_LADDERS).add(BLOOD_ASPECT_LADDER.asBlock(), BREATH_ASPECT_LADDER.asBlock(), DOOM_ASPECT_LADDER.asBlock(), HEART_ASPECT_LADDER.asBlock(),
				HOPE_ASPECT_LADDER.asBlock(), LIFE_ASPECT_LADDER.asBlock(), LIGHT_ASPECT_LADDER.asBlock(), MIND_ASPECT_LADDER.asBlock(),
				RAGE_ASPECT_LADDER.asBlock(), SPACE_ASPECT_LADDER.asBlock(), TIME_ASPECT_LADDER.asBlock(), VOID_ASPECT_LADDER.asBlock());
		provider.tag(MSTags.Blocks.ASPECT_POTTED_SAPLINGS).add(POTTED_BLOOD_ASPECT_SAPLING.get(), POTTED_BREATH_ASPECT_SAPLING.get(), POTTED_DOOM_ASPECT_SAPLING.get(), POTTED_HEART_ASPECT_SAPLING.get(),
				POTTED_HOPE_ASPECT_SAPLING.get(), POTTED_LIFE_ASPECT_SAPLING.get(), POTTED_LIGHT_ASPECT_SAPLING.get(), POTTED_MIND_ASPECT_SAPLING.get(),
				POTTED_RAGE_ASPECT_SAPLING.get(), POTTED_SPACE_ASPECT_SAPLING.get(), POTTED_TIME_ASPECT_SAPLING.get(), POTTED_VOID_ASPECT_SAPLING.get());
				
		provider.tag(BlockTags.LOGS).addTag(MSTags.Blocks.ASPECT_LOGS).addTag(MSTags.Blocks.ASPECT_WOOD);
		provider.tag(BlockTags.LEAVES).addTag(MSTags.Blocks.ASPECT_LEAVES);
		provider.tag(BlockTags.SAPLINGS).addTag(MSTags.Blocks.ASPECT_SAPLINGS);
		provider.tag(BlockTags.PLANKS).addTag(MSTags.Blocks.ASPECT_PLANKS);
		provider.tag(BlockTags.WOODEN_SLABS).addTag(MSTags.Blocks.ASPECT_SLABS);
		provider.tag(BlockTags.FENCES).add(BLOOD_ASPECT_FENCE.asBlock(), BREATH_ASPECT_FENCE.asBlock(), DOOM_ASPECT_FENCE.asBlock(), HEART_ASPECT_FENCE.asBlock(),
				HOPE_ASPECT_FENCE.asBlock(), LIFE_ASPECT_FENCE.asBlock(), LIGHT_ASPECT_FENCE.asBlock(), MIND_ASPECT_FENCE.asBlock(),
				RAGE_ASPECT_FENCE.asBlock(), SPACE_ASPECT_FENCE.asBlock(), TIME_ASPECT_FENCE.asBlock(), VOID_ASPECT_FENCE.asBlock());
		provider.tag(BlockTags.WOODEN_FENCES).add(BLOOD_ASPECT_FENCE.asBlock(), BREATH_ASPECT_FENCE.asBlock(), DOOM_ASPECT_FENCE.asBlock(), HEART_ASPECT_FENCE.asBlock(),
				HOPE_ASPECT_FENCE.asBlock(), LIFE_ASPECT_FENCE.asBlock(), LIGHT_ASPECT_FENCE.asBlock(), MIND_ASPECT_FENCE.asBlock(),
				RAGE_ASPECT_FENCE.asBlock(), SPACE_ASPECT_FENCE.asBlock(), TIME_ASPECT_FENCE.asBlock(), VOID_ASPECT_FENCE.asBlock());
		provider.tag(BlockTags.FENCE_GATES).add(BLOOD_ASPECT_FENCE_GATE.asBlock(), BREATH_ASPECT_FENCE_GATE.asBlock(), DOOM_ASPECT_FENCE_GATE.asBlock(), HEART_ASPECT_FENCE_GATE.asBlock(),
				HOPE_ASPECT_FENCE_GATE.asBlock(), LIFE_ASPECT_FENCE_GATE.asBlock(), LIGHT_ASPECT_FENCE_GATE.asBlock(), MIND_ASPECT_FENCE_GATE.asBlock(),
				RAGE_ASPECT_FENCE_GATE.asBlock(), SPACE_ASPECT_FENCE_GATE.asBlock(), TIME_ASPECT_FENCE_GATE.asBlock(), VOID_ASPECT_FENCE_GATE.asBlock());
		provider.tag(BlockTags.PRESSURE_PLATES).add(BLOOD_ASPECT_PRESSURE_PLATE.asBlock(), BREATH_ASPECT_PRESSURE_PLATE.asBlock(), DOOM_ASPECT_PRESSURE_PLATE.asBlock(), HEART_ASPECT_PRESSURE_PLATE.asBlock(),
				HOPE_ASPECT_PRESSURE_PLATE.asBlock(), LIFE_ASPECT_PRESSURE_PLATE.asBlock(), LIGHT_ASPECT_PRESSURE_PLATE.asBlock(), MIND_ASPECT_PRESSURE_PLATE.asBlock(),
				RAGE_ASPECT_PRESSURE_PLATE.asBlock(), SPACE_ASPECT_PRESSURE_PLATE.asBlock(), TIME_ASPECT_PRESSURE_PLATE.asBlock(), VOID_ASPECT_PRESSURE_PLATE.asBlock());
		provider.tag(Tags.Blocks.BOOKSHELVES).addTag(MSTags.Blocks.ASPECT_BOOKSHELVES);
		provider.tag(BlockTags.CLIMBABLE).addTag(MSTags.Blocks.ASPECT_LADDERS);
		provider.tag(BlockTags.FLOWER_POTS).addTag(MSTags.Blocks.ASPECT_POTTED_SAPLINGS);
	}
	
	public static void addRecipes(RecipeOutput recipeSaver)
	{
		CommonRecipes.stairsRecipe(BLOOD_ASPECT_STAIRS, BLOOD_ASPECT_PLANKS).group("wooden_stairs").save(recipeSaver);
		CommonRecipes.slabRecipe(BLOOD_ASPECT_SLAB, BLOOD_ASPECT_PLANKS).group("wooden_slab").save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BLOOD_ASPECT_FENCE.asBlock(), 3).group("wooden_fence")
				.define('#', BLOOD_ASPECT_PLANKS.asBlock()).define('$', Items.STICK).pattern("#$#").pattern("#$#")
				.unlockedBy("has_blood_aspect_planks", has(BLOOD_ASPECT_PLANKS.asBlock())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BLOOD_ASPECT_FENCE_GATE.asBlock(), 1).group("wooden_fence_gate")
				.define('#', BLOOD_ASPECT_PLANKS.asBlock()).define('$', Items.STICK).pattern("$#$").pattern("$#$")
				.unlockedBy("has_blood_aspect_planks", has(BLOOD_ASPECT_PLANKS.asBlock())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BLOOD_ASPECT_DOOR.asBlock(), 3).group("wooden_door")
				.define('#', BLOOD_ASPECT_PLANKS.asBlock()).pattern("## ").pattern("## ").pattern("## ")
				.unlockedBy("has_blood_aspect_planks", has(BLOOD_ASPECT_PLANKS.asBlock())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BLOOD_ASPECT_TRAPDOOR.asBlock(), 2).group("wooden_trapdoor")
				.define('#', BLOOD_ASPECT_PLANKS.asBlock()).pattern("###").pattern("###")
				.unlockedBy("has_blood_aspect_planks", has(BLOOD_ASPECT_PLANKS.asBlock())).save(recipeSaver);
		CommonRecipes.pressurePlateRecipe(BLOOD_ASPECT_PRESSURE_PLATE, BLOOD_ASPECT_PLANKS).group("stone_pressure_plate").save(recipeSaver);
		CommonRecipes.buttonRecipe(BLOOD_ASPECT_BUTTON, BLOOD_ASPECT_PLANKS).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BLOOD_ASPECT_CARVED_PLANKS.asBlock(), 8).group("aspect_carved_planks")
				.define('#', BLOOD_ASPECT_PLANKS.asBlock()).define('$', BLOOD_ASPECT_STRIPPED_LOG.asBlock()).pattern("#$").pattern("$#")
				.unlockedBy("has_blood_aspect_planks", has(BLOOD_ASPECT_PLANKS.asBlock())).save(recipeSaver);
		CommonRecipes.planksRecipe(BLOOD_ASPECT_PLANKS, BLOOD_ASPECT_LOG).group("aspect_planks").save(recipeSaver);
		CommonRecipes.planksRecipe(BLOOD_ASPECT_PLANKS, BLOOD_ASPECT_STRIPPED_LOG).group("aspect_planks").save(recipeSaver, new ResourceLocation(Minestuck.MOD_ID, "blood_aspect_planks_from_stripped_log"));
		CommonRecipes.planksRecipe(BLOOD_ASPECT_PLANKS, BLOOD_ASPECT_WOOD).group("aspect_planks").save(recipeSaver, new ResourceLocation(Minestuck.MOD_ID, "blood_aspect_planks_from_wood"));
		CommonRecipes.planksRecipe(BLOOD_ASPECT_PLANKS, BLOOD_ASPECT_STRIPPED_WOOD).group("aspect_planks").save(recipeSaver, new ResourceLocation(Minestuck.MOD_ID, "blood_aspect_planks_from_stripped_wood"));
		CommonRecipes.woodRecipe(BLOOD_ASPECT_WOOD, BLOOD_ASPECT_LOG).group("aspect_wood").save(recipeSaver);
		CommonRecipes.woodRecipe(BLOOD_ASPECT_STRIPPED_WOOD, BLOOD_ASPECT_STRIPPED_LOG).group("aspect_stripped_wood").save(recipeSaver);
		CommonRecipes.bookshelfRecipe(BLOOD_ASPECT_BOOKSHELF, BLOOD_ASPECT_PLANKS, BLOOD_ASPECT_SLAB).group("aspect_bookshelf").save(recipeSaver);
		CommonRecipes.ladderRecipe(BLOOD_ASPECT_LADDER, BLOOD_ASPECT_PLANKS).group("aspect_ladder").save(recipeSaver);
		CommonRecipes.signRecipe(BLOOD_ASPECT_SIGN, BLOOD_ASPECT_PLANKS).group("aspect_sign").save(recipeSaver);
		CommonRecipes.hangingSignRecipe(BLOOD_ASPECT_HANGING_SIGN, BLOOD_ASPECT_STRIPPED_LOG).group("aspect_hanging_sign").save(recipeSaver);
		
		CommonRecipes.stairsRecipe(BREATH_ASPECT_STAIRS, BREATH_ASPECT_PLANKS).group("wooden_stairs").save(recipeSaver);
		CommonRecipes.slabRecipe(BREATH_ASPECT_SLAB, BREATH_ASPECT_PLANKS).group("wooden_slab").save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BREATH_ASPECT_FENCE.asItem(), 3).group("wooden_fence")
				.define('#', BREATH_ASPECT_PLANKS.asItem()).define('$', Items.STICK).pattern("#$#").pattern("#$#")
				.unlockedBy("has_breath_aspect_planks", has(BREATH_ASPECT_PLANKS.asItem())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BREATH_ASPECT_FENCE_GATE.asItem(), 1).group("wooden_fence_gate")
				.define('#', BREATH_ASPECT_PLANKS.asItem()).define('$', Items.STICK).pattern("$#$").pattern("$#$")
				.unlockedBy("has_breath_aspect_planks", has(BREATH_ASPECT_PLANKS.asItem())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BREATH_ASPECT_DOOR.asItem(), 3).group("wooden_door")
				.define('#', BREATH_ASPECT_PLANKS.asItem()).pattern("## ").pattern("## ").pattern("## ")
				.unlockedBy("has_breath_aspect_planks", has(BREATH_ASPECT_PLANKS.asItem())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BREATH_ASPECT_TRAPDOOR.asItem(), 2).group("wooden_trapdoor")
				.define('#', BREATH_ASPECT_PLANKS.asItem()).pattern("###").pattern("###")
				.unlockedBy("has_breath_aspect_planks", has(BREATH_ASPECT_PLANKS.asItem())).save(recipeSaver);
		CommonRecipes.pressurePlateRecipe(BREATH_ASPECT_PRESSURE_PLATE, BREATH_ASPECT_PLANKS).group("stone_pressure_plate").save(recipeSaver);
		CommonRecipes.buttonRecipe(BREATH_ASPECT_BUTTON, BREATH_ASPECT_PLANKS).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BREATH_ASPECT_CARVED_PLANKS.asItem(), 8).group("aspect_carved_planks")
				.define('#', BREATH_ASPECT_PLANKS.asItem()).define('$', BREATH_ASPECT_STRIPPED_LOG.asItem()).pattern("#$").pattern("$#")
				.unlockedBy("has_breath_aspect_planks", has(BREATH_ASPECT_PLANKS.asItem())).save(recipeSaver);
		CommonRecipes.woodRecipe(BREATH_ASPECT_WOOD, BREATH_ASPECT_LOG).group("aspect_wood").save(recipeSaver);
		CommonRecipes.woodRecipe(BREATH_ASPECT_STRIPPED_WOOD, BREATH_ASPECT_STRIPPED_LOG).group("aspect_stripped_wood").save(recipeSaver);
		CommonRecipes.planksRecipe(BREATH_ASPECT_PLANKS, BREATH_ASPECT_LOG).group("aspect_planks").save(recipeSaver);
		CommonRecipes.planksRecipe(BREATH_ASPECT_PLANKS, BREATH_ASPECT_STRIPPED_LOG).group("aspect_planks").save(recipeSaver, new ResourceLocation(Minestuck.MOD_ID, "breath_aspect_planks_from_stripped_log"));
		CommonRecipes.planksRecipe(BREATH_ASPECT_PLANKS, BREATH_ASPECT_WOOD).group("aspect_planks").save(recipeSaver, new ResourceLocation(Minestuck.MOD_ID, "breath_aspect_planks_from_wood"));
		CommonRecipes.planksRecipe(BREATH_ASPECT_PLANKS, BREATH_ASPECT_STRIPPED_WOOD).group("aspect_planks").save(recipeSaver, new ResourceLocation(Minestuck.MOD_ID, "breath_aspect_planks_from_stripped_wood"));
		CommonRecipes.bookshelfRecipe(BREATH_ASPECT_BOOKSHELF, BREATH_ASPECT_PLANKS, BREATH_ASPECT_SLAB).group("aspect_bookshelf").save(recipeSaver);
		CommonRecipes.ladderRecipe(BREATH_ASPECT_LADDER, BREATH_ASPECT_PLANKS).group("aspect_ladder").save(recipeSaver);
		CommonRecipes.signRecipe(BREATH_ASPECT_SIGN, BREATH_ASPECT_PLANKS).group("aspect_sign").save(recipeSaver);
		CommonRecipes.hangingSignRecipe(BREATH_ASPECT_HANGING_SIGN, BREATH_ASPECT_STRIPPED_LOG).group("aspect_hanging_sign").save(recipeSaver);
		
		CommonRecipes.stairsRecipe(DOOM_ASPECT_STAIRS, DOOM_ASPECT_PLANKS).group("wooden_stairs").save(recipeSaver);
		CommonRecipes.slabRecipe(DOOM_ASPECT_SLAB, DOOM_ASPECT_PLANKS).group("wooden_slab").save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, DOOM_ASPECT_FENCE.asItem(), 3).group("wooden_fence")
				.define('#', DOOM_ASPECT_PLANKS.asItem()).define('$', Items.STICK).pattern("#$#").pattern("#$#")
				.unlockedBy("has_doom_aspect_planks", has(DOOM_ASPECT_PLANKS.asItem())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, DOOM_ASPECT_FENCE_GATE.asItem(), 1).group("wooden_fence_gate")
				.define('#', DOOM_ASPECT_PLANKS.asItem()).define('$', Items.STICK).pattern("$#$").pattern("$#$")
				.unlockedBy("has_doom_aspect_planks", has(DOOM_ASPECT_PLANKS.asItem())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, DOOM_ASPECT_DOOR.asItem(), 3).group("wooden_door")
				.define('#', DOOM_ASPECT_PLANKS.asItem()).pattern("## ").pattern("## ").pattern("## ")
				.unlockedBy("has_doom_aspect_planks", has(DOOM_ASPECT_PLANKS.asItem())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, DOOM_ASPECT_TRAPDOOR.asItem(), 2).group("wooden_trapdoor")
				.define('#', DOOM_ASPECT_PLANKS.asItem()).pattern("###").pattern("###")
				.unlockedBy("has_doom_aspect_planks", has(DOOM_ASPECT_PLANKS.asItem())).save(recipeSaver);
		CommonRecipes.pressurePlateRecipe(DOOM_ASPECT_PRESSURE_PLATE, DOOM_ASPECT_PLANKS).group("stone_pressure_plate").save(recipeSaver);
		CommonRecipes.buttonRecipe(DOOM_ASPECT_BUTTON, DOOM_ASPECT_PLANKS).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, DOOM_ASPECT_CARVED_PLANKS.asItem(), 8).group("aspect_carved_planks")
				.define('#', DOOM_ASPECT_PLANKS.asItem()).define('$', DOOM_ASPECT_STRIPPED_LOG.asItem()).pattern("#$").pattern("$#")
				.unlockedBy("has_doom_aspect_planks", has(DOOM_ASPECT_PLANKS.asItem())).save(recipeSaver);
		CommonRecipes.woodRecipe(DOOM_ASPECT_WOOD, DOOM_ASPECT_LOG).group("aspect_wood").save(recipeSaver);
		CommonRecipes.woodRecipe(DOOM_ASPECT_STRIPPED_WOOD, DOOM_ASPECT_STRIPPED_LOG).group("aspect_stripped_wood").save(recipeSaver);
		CommonRecipes.planksRecipe(DOOM_ASPECT_PLANKS, DOOM_ASPECT_LOG).group("aspect_planks").save(recipeSaver);
		CommonRecipes.planksRecipe(DOOM_ASPECT_PLANKS, DOOM_ASPECT_STRIPPED_LOG).group("aspect_planks").save(recipeSaver, new ResourceLocation(Minestuck.MOD_ID, "doom_aspect_planks_from_stripped_log"));
		CommonRecipes.planksRecipe(DOOM_ASPECT_PLANKS, DOOM_ASPECT_WOOD).group("aspect_planks").save(recipeSaver, new ResourceLocation(Minestuck.MOD_ID, "doom_aspect_planks_from_wood"));
		CommonRecipes.planksRecipe(DOOM_ASPECT_PLANKS, DOOM_ASPECT_STRIPPED_WOOD).group("aspect_planks").save(recipeSaver, new ResourceLocation(Minestuck.MOD_ID, "doom_aspect_planks_from_stripped_wood"));
		CommonRecipes.bookshelfRecipe(DOOM_ASPECT_BOOKSHELF, DOOM_ASPECT_PLANKS, DOOM_ASPECT_SLAB).group("aspect_bookshelf").save(recipeSaver);
		CommonRecipes.ladderRecipe(DOOM_ASPECT_LADDER, DOOM_ASPECT_PLANKS).group("aspect_ladder").save(recipeSaver);
		CommonRecipes.signRecipe(DOOM_ASPECT_SIGN, DOOM_ASPECT_PLANKS).group("aspect_sign").save(recipeSaver);
		CommonRecipes.hangingSignRecipe(DOOM_ASPECT_HANGING_SIGN, DOOM_ASPECT_STRIPPED_LOG).group("aspect_hanging_sign").save(recipeSaver);
		
		CommonRecipes.stairsRecipe(HEART_ASPECT_STAIRS, HEART_ASPECT_PLANKS).group("wooden_stairs").save(recipeSaver);
		CommonRecipes.slabRecipe(HEART_ASPECT_SLAB, HEART_ASPECT_PLANKS).group("wooden_slab").save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HEART_ASPECT_FENCE.asItem(), 3).group("wooden_fence")
				.define('#', HEART_ASPECT_PLANKS.asItem()).define('$', Items.STICK).pattern("#$#").pattern("#$#")
				.unlockedBy("has_heart_aspect_planks", has(HEART_ASPECT_PLANKS.asItem())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HEART_ASPECT_FENCE_GATE.asItem(), 1).group("wooden_fence_gate")
				.define('#', HEART_ASPECT_PLANKS.asItem()).define('$', Items.STICK).pattern("$#$").pattern("$#$")
				.unlockedBy("has_heart_aspect_planks", has(HEART_ASPECT_PLANKS.asItem())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HEART_ASPECT_DOOR.asItem(), 3).group("wooden_door")
				.define('#', HEART_ASPECT_PLANKS.asItem()).pattern("## ").pattern("## ").pattern("## ")
				.unlockedBy("has_heart_aspect_planks", has(HEART_ASPECT_PLANKS.asItem())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HEART_ASPECT_TRAPDOOR.asItem(), 2).group("wooden_trapdoor")
				.define('#', HEART_ASPECT_PLANKS.asItem()).pattern("###").pattern("###")
				.unlockedBy("has_heart_aspect_planks", has(HEART_ASPECT_PLANKS.asItem())).save(recipeSaver);
		CommonRecipes.pressurePlateRecipe(HEART_ASPECT_PRESSURE_PLATE, HEART_ASPECT_PLANKS).group("stone_pressure_plate").save(recipeSaver);
		CommonRecipes.buttonRecipe(HEART_ASPECT_BUTTON, HEART_ASPECT_PLANKS).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HEART_ASPECT_CARVED_PLANKS.asItem(), 8).group("aspect_carved_planks")
				.define('#', HEART_ASPECT_PLANKS.asItem()).define('$', HEART_ASPECT_STRIPPED_LOG.asItem()).pattern("#$").pattern("$#")
				.unlockedBy("has_heart_aspect_planks", has(HEART_ASPECT_PLANKS.asItem())).save(recipeSaver);
		CommonRecipes.woodRecipe(HEART_ASPECT_WOOD, HEART_ASPECT_LOG).group("aspect_wood").save(recipeSaver);
		CommonRecipes.woodRecipe(HEART_ASPECT_STRIPPED_WOOD, HEART_ASPECT_STRIPPED_LOG).group("aspect_stripped_wood").save(recipeSaver);
		CommonRecipes.planksRecipe(HEART_ASPECT_PLANKS, HEART_ASPECT_LOG).group("aspect_planks").save(recipeSaver);
		CommonRecipes.planksRecipe(HEART_ASPECT_PLANKS, HEART_ASPECT_STRIPPED_LOG).group("aspect_planks").save(recipeSaver, new ResourceLocation(Minestuck.MOD_ID, "heart_aspect_planks_from_stripped_log"));
		CommonRecipes.planksRecipe(HEART_ASPECT_PLANKS, HEART_ASPECT_WOOD).group("aspect_planks").save(recipeSaver, new ResourceLocation(Minestuck.MOD_ID, "heart_aspect_planks_from_wood"));
		CommonRecipes.planksRecipe(HEART_ASPECT_PLANKS, HEART_ASPECT_STRIPPED_WOOD).group("aspect_planks").save(recipeSaver, new ResourceLocation(Minestuck.MOD_ID, "heart_aspect_planks_from_stripped_wood"));
		CommonRecipes.bookshelfRecipe(HEART_ASPECT_BOOKSHELF, HEART_ASPECT_PLANKS, HEART_ASPECT_SLAB).group("aspect_bookshelf").save(recipeSaver);
		CommonRecipes.ladderRecipe(HEART_ASPECT_LADDER, HEART_ASPECT_PLANKS).group("aspect_ladder").save(recipeSaver);
		CommonRecipes.signRecipe(HEART_ASPECT_SIGN, HEART_ASPECT_PLANKS).group("aspect_sign").save(recipeSaver);
		CommonRecipes.hangingSignRecipe(HEART_ASPECT_HANGING_SIGN, HEART_ASPECT_STRIPPED_LOG).group("aspect_hanging_sign").save(recipeSaver);
		
		CommonRecipes.stairsRecipe(HOPE_ASPECT_STAIRS, HOPE_ASPECT_PLANKS).group("wooden_stairs").save(recipeSaver);
		CommonRecipes.slabRecipe(HOPE_ASPECT_SLAB, HOPE_ASPECT_PLANKS).group("wooden_slab").save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HOPE_ASPECT_FENCE.asItem(), 3).group("wooden_fence")
				.define('#', HOPE_ASPECT_PLANKS.asItem()).define('$', Items.STICK).pattern("#$#").pattern("#$#")
				.unlockedBy("has_hope_aspect_planks", has(HOPE_ASPECT_PLANKS.asItem())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HOPE_ASPECT_FENCE_GATE.asItem(), 1).group("wooden_fence_gate")
				.define('#', HOPE_ASPECT_PLANKS.asItem()).define('$', Items.STICK).pattern("$#$").pattern("$#$")
				.unlockedBy("has_hope_aspect_planks", has(HOPE_ASPECT_PLANKS.asItem())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HOPE_ASPECT_DOOR.asItem(), 3).group("wooden_door")
				.define('#', HOPE_ASPECT_PLANKS.asItem()).pattern("## ").pattern("## ").pattern("## ")
				.unlockedBy("has_hope_aspect_planks", has(HOPE_ASPECT_PLANKS.asItem())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HOPE_ASPECT_TRAPDOOR.asItem(), 2).group("wooden_trapdoor")
				.define('#', HOPE_ASPECT_PLANKS.asItem()).pattern("###").pattern("###")
				.unlockedBy("has_hope_aspect_planks", has(HOPE_ASPECT_PLANKS.asItem())).save(recipeSaver);
		CommonRecipes.pressurePlateRecipe(HOPE_ASPECT_PRESSURE_PLATE, HOPE_ASPECT_PLANKS).group("stone_pressure_plate").save(recipeSaver);
		CommonRecipes.buttonRecipe(HOPE_ASPECT_BUTTON, HOPE_ASPECT_PLANKS).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HOPE_ASPECT_CARVED_PLANKS.asItem(), 8).group("aspect_carved_planks")
				.define('#', HOPE_ASPECT_PLANKS.asItem()).define('$', HOPE_ASPECT_STRIPPED_LOG.asItem()).pattern("#$").pattern("$#")
				.unlockedBy("has_hope_aspect_planks", has(HOPE_ASPECT_PLANKS.asItem())).save(recipeSaver);
		CommonRecipes.woodRecipe(HOPE_ASPECT_WOOD, HOPE_ASPECT_LOG).group("aspect_wood").save(recipeSaver);
		CommonRecipes.woodRecipe(HOPE_ASPECT_STRIPPED_WOOD, HOPE_ASPECT_STRIPPED_LOG).group("aspect_stripped_wood").save(recipeSaver);
		CommonRecipes.planksRecipe(HOPE_ASPECT_PLANKS, HOPE_ASPECT_LOG).group("aspect_planks").save(recipeSaver);
		CommonRecipes.planksRecipe(HOPE_ASPECT_PLANKS, HOPE_ASPECT_STRIPPED_LOG).group("aspect_planks").save(recipeSaver, new ResourceLocation(Minestuck.MOD_ID, "hope_aspect_planks_from_stripped_log"));
		CommonRecipes.planksRecipe(HOPE_ASPECT_PLANKS, HOPE_ASPECT_WOOD).group("aspect_planks").save(recipeSaver, new ResourceLocation(Minestuck.MOD_ID, "hope_aspect_planks_from_wood"));
		CommonRecipes.planksRecipe(HOPE_ASPECT_PLANKS, HOPE_ASPECT_STRIPPED_WOOD).group("aspect_planks").save(recipeSaver, new ResourceLocation(Minestuck.MOD_ID, "hope_aspect_planks_from_stripped_wood"));
		CommonRecipes.bookshelfRecipe(HOPE_ASPECT_BOOKSHELF, HOPE_ASPECT_PLANKS, HOPE_ASPECT_SLAB).group("aspect_bookshelf").save(recipeSaver);
		CommonRecipes.ladderRecipe(HOPE_ASPECT_LADDER, HOPE_ASPECT_PLANKS).group("aspect_ladder").save(recipeSaver);
		CommonRecipes.signRecipe(HOPE_ASPECT_SIGN, HOPE_ASPECT_PLANKS).group("aspect_sign").save(recipeSaver);
		CommonRecipes.hangingSignRecipe(HOPE_ASPECT_HANGING_SIGN, HOPE_ASPECT_STRIPPED_LOG).group("aspect_hanging_sign").save(recipeSaver);
		
		CommonRecipes.stairsRecipe(LIFE_ASPECT_STAIRS, LIFE_ASPECT_PLANKS).group("wooden_stairs").save(recipeSaver);
		CommonRecipes.slabRecipe(LIFE_ASPECT_SLAB, LIFE_ASPECT_PLANKS).group("wooden_slab").save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, LIFE_ASPECT_FENCE.asItem(), 3).group("wooden_fence")
				.define('#', LIFE_ASPECT_PLANKS.asItem()).define('$', Items.STICK).pattern("#$#").pattern("#$#")
				.unlockedBy("has_life_aspect_planks", has(LIFE_ASPECT_PLANKS.asItem())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, LIFE_ASPECT_FENCE_GATE.asItem(), 1).group("wooden_fence_gate")
				.define('#', LIFE_ASPECT_PLANKS.asItem()).define('$', Items.STICK).pattern("$#$").pattern("$#$")
				.unlockedBy("has_life_aspect_planks", has(LIFE_ASPECT_PLANKS.asItem())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, LIFE_ASPECT_DOOR.asItem(), 3).group("wooden_door")
				.define('#', LIFE_ASPECT_PLANKS.asItem()).pattern("## ").pattern("## ").pattern("## ")
				.unlockedBy("has_life_aspect_planks", has(LIFE_ASPECT_PLANKS.asItem())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, LIFE_ASPECT_TRAPDOOR.asItem(), 2).group("wooden_trapdoor")
				.define('#', LIFE_ASPECT_PLANKS.asItem()).pattern("###").pattern("###")
				.unlockedBy("has_life_aspect_planks", has(LIFE_ASPECT_PLANKS.asItem())).save(recipeSaver);
		CommonRecipes.pressurePlateRecipe(LIFE_ASPECT_PRESSURE_PLATE, LIFE_ASPECT_PLANKS).group("stone_pressure_plate").save(recipeSaver);
		CommonRecipes.buttonRecipe(LIFE_ASPECT_BUTTON, LIFE_ASPECT_PLANKS).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, LIFE_ASPECT_CARVED_PLANKS.asItem(), 8).group("aspect_carved_planks")
				.define('#', LIFE_ASPECT_PLANKS.asItem()).define('$', LIFE_ASPECT_STRIPPED_LOG.asItem()).pattern("#$").pattern("$#")
				.unlockedBy("has_life_aspect_planks", has(LIFE_ASPECT_PLANKS.asItem())).save(recipeSaver);
		CommonRecipes.woodRecipe(LIFE_ASPECT_WOOD, LIFE_ASPECT_LOG).group("aspect_wood").save(recipeSaver);
		CommonRecipes.woodRecipe(LIFE_ASPECT_STRIPPED_WOOD, LIFE_ASPECT_STRIPPED_LOG).group("aspect_stripped_wood").save(recipeSaver);
		CommonRecipes.planksRecipe(LIFE_ASPECT_PLANKS, LIFE_ASPECT_LOG).group("aspect_planks").save(recipeSaver);
		CommonRecipes.planksRecipe(LIFE_ASPECT_PLANKS, LIFE_ASPECT_STRIPPED_LOG).group("aspect_planks").save(recipeSaver, new ResourceLocation(Minestuck.MOD_ID, "life_aspect_planks_from_stripped_log"));
		CommonRecipes.planksRecipe(LIFE_ASPECT_PLANKS, LIFE_ASPECT_WOOD).group("aspect_planks").save(recipeSaver, new ResourceLocation(Minestuck.MOD_ID, "life_aspect_planks_from_wood"));
		CommonRecipes.planksRecipe(LIFE_ASPECT_PLANKS, LIFE_ASPECT_STRIPPED_WOOD).group("aspect_planks").save(recipeSaver, new ResourceLocation(Minestuck.MOD_ID, "life_aspect_planks_from_stripped_wood"));
		CommonRecipes.bookshelfRecipe(LIFE_ASPECT_BOOKSHELF, LIFE_ASPECT_PLANKS, LIFE_ASPECT_SLAB).group("aspect_bookshelf").save(recipeSaver);
		CommonRecipes.ladderRecipe(LIFE_ASPECT_LADDER, LIFE_ASPECT_PLANKS).group("aspect_ladder").save(recipeSaver);
		CommonRecipes.signRecipe(LIFE_ASPECT_SIGN, LIFE_ASPECT_PLANKS).group("aspect_sign").save(recipeSaver);
		CommonRecipes.hangingSignRecipe(LIFE_ASPECT_HANGING_SIGN, LIFE_ASPECT_STRIPPED_LOG).group("aspect_hanging_sign").save(recipeSaver);
		
		CommonRecipes.stairsRecipe(LIGHT_ASPECT_STAIRS, LIGHT_ASPECT_PLANKS).group("wooden_stairs").save(recipeSaver);
		CommonRecipes.slabRecipe(LIGHT_ASPECT_SLAB, LIGHT_ASPECT_PLANKS).group("wooden_slab").save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, LIGHT_ASPECT_FENCE.asItem(), 3).group("wooden_fence")
				.define('#', LIGHT_ASPECT_PLANKS.asItem()).define('$', Items.STICK).pattern("#$#").pattern("#$#")
				.unlockedBy("has_light_aspect_planks", has(LIGHT_ASPECT_PLANKS.asItem())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, LIGHT_ASPECT_FENCE_GATE.asItem(), 1).group("wooden_fence_gate")
				.define('#', LIGHT_ASPECT_PLANKS.asItem()).define('$', Items.STICK).pattern("$#$").pattern("$#$")
				.unlockedBy("has_light_aspect_planks", has(LIGHT_ASPECT_PLANKS.asItem())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, LIGHT_ASPECT_DOOR.asItem(), 3).group("wooden_door")
				.define('#', LIGHT_ASPECT_PLANKS.asItem()).pattern("## ").pattern("## ").pattern("## ")
				.unlockedBy("has_light_aspect_planks", has(LIGHT_ASPECT_PLANKS.asItem())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, LIGHT_ASPECT_TRAPDOOR.asItem(), 2).group("wooden_trapdoor")
				.define('#', LIGHT_ASPECT_PLANKS.asItem()).pattern("###").pattern("###")
				.unlockedBy("has_light_aspect_planks", has(LIGHT_ASPECT_PLANKS.asItem())).save(recipeSaver);
		CommonRecipes.pressurePlateRecipe(LIGHT_ASPECT_PRESSURE_PLATE, LIGHT_ASPECT_PLANKS).group("stone_pressure_plate").save(recipeSaver);
		CommonRecipes.buttonRecipe(LIGHT_ASPECT_BUTTON, LIGHT_ASPECT_PLANKS).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, LIGHT_ASPECT_CARVED_PLANKS.asItem(), 8).group("aspect_carved_planks")
				.define('#', LIGHT_ASPECT_PLANKS.asItem()).define('$', LIGHT_ASPECT_STRIPPED_LOG.asItem()).pattern("#$").pattern("$#")
				.unlockedBy("has_light_aspect_planks", has(LIGHT_ASPECT_PLANKS.asItem())).save(recipeSaver);
		CommonRecipes.woodRecipe(LIGHT_ASPECT_WOOD, LIGHT_ASPECT_LOG).group("aspect_wood").save(recipeSaver);
		CommonRecipes.woodRecipe(LIGHT_ASPECT_STRIPPED_WOOD, LIGHT_ASPECT_STRIPPED_LOG).group("aspect_stripped_wood").save(recipeSaver);
		CommonRecipes.planksRecipe(LIGHT_ASPECT_PLANKS, LIGHT_ASPECT_LOG).group("aspect_planks").save(recipeSaver);
		CommonRecipes.planksRecipe(LIGHT_ASPECT_PLANKS, LIGHT_ASPECT_STRIPPED_LOG).group("aspect_planks").save(recipeSaver, new ResourceLocation(Minestuck.MOD_ID, "light_aspect_planks_from_stripped_log"));
		CommonRecipes.planksRecipe(LIGHT_ASPECT_PLANKS, LIGHT_ASPECT_WOOD).group("aspect_planks").save(recipeSaver, new ResourceLocation(Minestuck.MOD_ID, "light_aspect_planks_from_wood"));
		CommonRecipes.planksRecipe(LIGHT_ASPECT_PLANKS, LIGHT_ASPECT_STRIPPED_WOOD).group("aspect_planks").save(recipeSaver, new ResourceLocation(Minestuck.MOD_ID, "light_aspect_planks_from_stripped_wood"));
		CommonRecipes.bookshelfRecipe(LIGHT_ASPECT_BOOKSHELF, LIGHT_ASPECT_PLANKS, LIGHT_ASPECT_SLAB).group("aspect_bookshelf").save(recipeSaver);
		CommonRecipes.ladderRecipe(LIGHT_ASPECT_LADDER, LIGHT_ASPECT_PLANKS).group("aspect_ladder").save(recipeSaver);
		CommonRecipes.signRecipe(LIGHT_ASPECT_SIGN, LIGHT_ASPECT_PLANKS).group("aspect_sign").save(recipeSaver);
		CommonRecipes.hangingSignRecipe(LIGHT_ASPECT_HANGING_SIGN, LIGHT_ASPECT_STRIPPED_LOG).group("aspect_hanging_sign").save(recipeSaver);
		
		CommonRecipes.stairsRecipe(MIND_ASPECT_STAIRS, MIND_ASPECT_PLANKS).group("wooden_stairs").save(recipeSaver);
		CommonRecipes.slabRecipe(MIND_ASPECT_SLAB, MIND_ASPECT_PLANKS).group("wooden_slab").save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, MIND_ASPECT_FENCE.asItem(), 3).group("wooden_fence")
				.define('#', MIND_ASPECT_PLANKS.asItem()).define('$', Items.STICK).pattern("#$#").pattern("#$#")
				.unlockedBy("has_mind_aspect_planks", has(MIND_ASPECT_PLANKS.asItem())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, MIND_ASPECT_FENCE_GATE.asItem(), 1).group("wooden_fence_gate")
				.define('#', MIND_ASPECT_PLANKS.asItem()).define('$', Items.STICK).pattern("$#$").pattern("$#$")
				.unlockedBy("has_mind_aspect_planks", has(MIND_ASPECT_PLANKS.asItem())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, MIND_ASPECT_DOOR.asItem(), 3).group("wooden_door")
				.define('#', MIND_ASPECT_PLANKS.asItem()).pattern("## ").pattern("## ").pattern("## ")
				.unlockedBy("has_mind_aspect_planks", has(MIND_ASPECT_PLANKS.asItem())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, MIND_ASPECT_TRAPDOOR.asItem(), 2).group("wooden_trapdoor")
				.define('#', MIND_ASPECT_PLANKS.asItem()).pattern("###").pattern("###")
				.unlockedBy("has_mind_aspect_planks", has(MIND_ASPECT_PLANKS.asItem())).save(recipeSaver);
		CommonRecipes.pressurePlateRecipe(MIND_ASPECT_PRESSURE_PLATE, MIND_ASPECT_PLANKS).group("stone_pressure_plate").save(recipeSaver);
		CommonRecipes.buttonRecipe(MIND_ASPECT_BUTTON, MIND_ASPECT_PLANKS).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, MIND_ASPECT_CARVED_PLANKS.asItem(), 8).group("aspect_carved_planks")
				.define('#', MIND_ASPECT_PLANKS.asItem()).define('$', MIND_ASPECT_STRIPPED_LOG.asItem()).pattern("#$").pattern("$#")
				.unlockedBy("has_mind_aspect_planks", has(MIND_ASPECT_PLANKS.asItem())).save(recipeSaver);
		CommonRecipes.woodRecipe(MIND_ASPECT_WOOD, MIND_ASPECT_LOG).group("aspect_wood").save(recipeSaver);
		CommonRecipes.woodRecipe(MIND_ASPECT_STRIPPED_WOOD, MIND_ASPECT_STRIPPED_LOG).group("aspect_stripped_wood").save(recipeSaver);
		CommonRecipes.planksRecipe(MIND_ASPECT_PLANKS, MIND_ASPECT_LOG).group("aspect_planks").save(recipeSaver);
		CommonRecipes.planksRecipe(MIND_ASPECT_PLANKS, MIND_ASPECT_STRIPPED_LOG).group("aspect_planks").save(recipeSaver, new ResourceLocation(Minestuck.MOD_ID, "mind_aspect_planks_from_stripped_log"));
		CommonRecipes.planksRecipe(MIND_ASPECT_PLANKS, MIND_ASPECT_WOOD).group("aspect_planks").save(recipeSaver, new ResourceLocation(Minestuck.MOD_ID, "mind_aspect_planks_from_wood"));
		CommonRecipes.planksRecipe(MIND_ASPECT_PLANKS, MIND_ASPECT_STRIPPED_WOOD).group("aspect_planks").save(recipeSaver, new ResourceLocation(Minestuck.MOD_ID, "mind_aspect_planks_from_stripped_wood"));
		CommonRecipes.bookshelfRecipe(MIND_ASPECT_BOOKSHELF, MIND_ASPECT_PLANKS, MIND_ASPECT_SLAB).group("aspect_bookshelf").save(recipeSaver);
		CommonRecipes.ladderRecipe(MIND_ASPECT_LADDER, MIND_ASPECT_PLANKS).group("aspect_ladder").save(recipeSaver);
		CommonRecipes.signRecipe(MIND_ASPECT_SIGN, MIND_ASPECT_PLANKS).group("aspect_sign").save(recipeSaver);
		CommonRecipes.hangingSignRecipe(MIND_ASPECT_HANGING_SIGN, MIND_ASPECT_STRIPPED_LOG).group("aspect_hanging_sign").save(recipeSaver);
		
		CommonRecipes.stairsRecipe(RAGE_ASPECT_STAIRS, RAGE_ASPECT_PLANKS).group("wooden_stairs").save(recipeSaver);
		CommonRecipes.slabRecipe(RAGE_ASPECT_SLAB, RAGE_ASPECT_PLANKS).group("wooden_slab").save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, RAGE_ASPECT_FENCE.asItem(), 3).group("wooden_fence")
				.define('#', RAGE_ASPECT_PLANKS.asItem()).define('$', Items.STICK).pattern("#$#").pattern("#$#")
				.unlockedBy("has_rage_aspect_planks", has(RAGE_ASPECT_PLANKS.asItem())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, RAGE_ASPECT_FENCE_GATE.asItem(), 1).group("wooden_fence_gate")
				.define('#', RAGE_ASPECT_PLANKS.asItem()).define('$', Items.STICK).pattern("$#$").pattern("$#$")
				.unlockedBy("has_rage_aspect_planks", has(RAGE_ASPECT_PLANKS.asItem())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, RAGE_ASPECT_DOOR.asItem(), 3).group("wooden_door")
				.define('#', RAGE_ASPECT_PLANKS.asItem()).pattern("## ").pattern("## ").pattern("## ")
				.unlockedBy("has_rage_aspect_planks", has(RAGE_ASPECT_PLANKS.asItem())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, RAGE_ASPECT_TRAPDOOR.asItem(), 2).group("wooden_trapdoor")
				.define('#', RAGE_ASPECT_PLANKS.asItem()).pattern("###").pattern("###")
				.unlockedBy("has_rage_aspect_planks", has(RAGE_ASPECT_PLANKS.asItem())).save(recipeSaver);
		CommonRecipes.pressurePlateRecipe(RAGE_ASPECT_PRESSURE_PLATE, RAGE_ASPECT_PLANKS).group("stone_pressure_plate").save(recipeSaver);
		CommonRecipes.buttonRecipe(RAGE_ASPECT_BUTTON, RAGE_ASPECT_PLANKS).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, RAGE_ASPECT_CARVED_PLANKS.asItem(), 8).group("aspect_carved_planks")
				.define('#', RAGE_ASPECT_PLANKS.asItem()).define('$', RAGE_ASPECT_STRIPPED_LOG.asItem()).pattern("#$").pattern("$#")
				.unlockedBy("has_rage_aspect_planks", has(RAGE_ASPECT_PLANKS.asItem())).save(recipeSaver);
		CommonRecipes.woodRecipe(RAGE_ASPECT_WOOD, RAGE_ASPECT_LOG).group("aspect_wood").save(recipeSaver);
		CommonRecipes.woodRecipe(RAGE_ASPECT_STRIPPED_WOOD, RAGE_ASPECT_STRIPPED_LOG).group("aspect_stripped_wood").save(recipeSaver);
		CommonRecipes.planksRecipe(RAGE_ASPECT_PLANKS, RAGE_ASPECT_LOG).group("aspect_planks").save(recipeSaver);
		CommonRecipes.planksRecipe(RAGE_ASPECT_PLANKS, RAGE_ASPECT_STRIPPED_LOG).group("aspect_planks").save(recipeSaver, new ResourceLocation(Minestuck.MOD_ID, "rage_aspect_planks_from_stripped_log"));
		CommonRecipes.planksRecipe(RAGE_ASPECT_PLANKS, RAGE_ASPECT_WOOD).group("aspect_planks").save(recipeSaver, new ResourceLocation(Minestuck.MOD_ID, "rage_aspect_planks_from_wood"));
		CommonRecipes.planksRecipe(RAGE_ASPECT_PLANKS, RAGE_ASPECT_STRIPPED_WOOD).group("aspect_planks").save(recipeSaver, new ResourceLocation(Minestuck.MOD_ID, "rage_aspect_planks_from_stripped_wood"));
		CommonRecipes.bookshelfRecipe(RAGE_ASPECT_BOOKSHELF, RAGE_ASPECT_PLANKS, RAGE_ASPECT_SLAB).group("aspect_bookshelf").save(recipeSaver);
		CommonRecipes.ladderRecipe(RAGE_ASPECT_LADDER, RAGE_ASPECT_PLANKS).group("aspect_ladder").save(recipeSaver);
		CommonRecipes.signRecipe(RAGE_ASPECT_SIGN, RAGE_ASPECT_PLANKS).group("aspect_sign").save(recipeSaver);
		CommonRecipes.hangingSignRecipe(RAGE_ASPECT_HANGING_SIGN, RAGE_ASPECT_STRIPPED_LOG).group("aspect_hanging_sign").save(recipeSaver);
		
		CommonRecipes.stairsRecipe(SPACE_ASPECT_STAIRS, SPACE_ASPECT_PLANKS).group("wooden_stairs").save(recipeSaver);
		CommonRecipes.slabRecipe(SPACE_ASPECT_SLAB, SPACE_ASPECT_PLANKS).group("wooden_slab").save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, SPACE_ASPECT_FENCE.asItem(), 3).group("wooden_fence")
				.define('#', SPACE_ASPECT_PLANKS.asItem()).define('$', Items.STICK).pattern("#$#").pattern("#$#")
				.unlockedBy("has_space_aspect_planks", has(SPACE_ASPECT_PLANKS.asItem())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, SPACE_ASPECT_FENCE_GATE.asItem(), 1).group("wooden_fence_gate")
				.define('#', SPACE_ASPECT_PLANKS.asItem()).define('$', Items.STICK).pattern("$#$").pattern("$#$")
				.unlockedBy("has_space_aspect_planks", has(SPACE_ASPECT_PLANKS.asItem())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, SPACE_ASPECT_DOOR.asItem(), 3).group("wooden_door")
				.define('#', SPACE_ASPECT_PLANKS.asItem()).pattern("## ").pattern("## ").pattern("## ")
				.unlockedBy("has_space_aspect_planks", has(SPACE_ASPECT_PLANKS.asItem())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, SPACE_ASPECT_TRAPDOOR.asItem(), 2).group("wooden_trapdoor")
				.define('#', SPACE_ASPECT_PLANKS.asItem()).pattern("###").pattern("###")
				.unlockedBy("has_space_aspect_planks", has(SPACE_ASPECT_PLANKS.asItem())).save(recipeSaver);
		CommonRecipes.pressurePlateRecipe(SPACE_ASPECT_PRESSURE_PLATE, SPACE_ASPECT_PLANKS).group("stone_pressure_plate").save(recipeSaver);
		CommonRecipes.buttonRecipe(SPACE_ASPECT_BUTTON, SPACE_ASPECT_PLANKS).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, SPACE_ASPECT_CARVED_PLANKS.asItem(), 8).group("aspect_carved_planks")
				.define('#', SPACE_ASPECT_PLANKS.asItem()).define('$', SPACE_ASPECT_STRIPPED_LOG.asItem()).pattern("#$").pattern("$#")
				.unlockedBy("has_space_aspect_planks", has(SPACE_ASPECT_PLANKS.asItem())).save(recipeSaver);
		CommonRecipes.woodRecipe(SPACE_ASPECT_WOOD, SPACE_ASPECT_LOG).group("aspect_wood").save(recipeSaver);
		CommonRecipes.woodRecipe(SPACE_ASPECT_STRIPPED_WOOD, SPACE_ASPECT_STRIPPED_LOG).group("aspect_stripped_wood").save(recipeSaver);
		CommonRecipes.planksRecipe(SPACE_ASPECT_PLANKS, SPACE_ASPECT_LOG).group("aspect_planks").save(recipeSaver);
		CommonRecipes.planksRecipe(SPACE_ASPECT_PLANKS, SPACE_ASPECT_STRIPPED_LOG).group("aspect_planks").save(recipeSaver, new ResourceLocation(Minestuck.MOD_ID, "space_aspect_planks_from_stripped_log"));
		CommonRecipes.planksRecipe(SPACE_ASPECT_PLANKS, SPACE_ASPECT_WOOD).group("aspect_planks").save(recipeSaver, new ResourceLocation(Minestuck.MOD_ID, "space_aspect_planks_from_wood"));
		CommonRecipes.planksRecipe(SPACE_ASPECT_PLANKS, SPACE_ASPECT_STRIPPED_WOOD).group("aspect_planks").save(recipeSaver, new ResourceLocation(Minestuck.MOD_ID, "space_aspect_planks_from_stripped_wood"));
		CommonRecipes.bookshelfRecipe(SPACE_ASPECT_BOOKSHELF, SPACE_ASPECT_PLANKS, SPACE_ASPECT_SLAB).group("aspect_bookshelf").save(recipeSaver);
		CommonRecipes.ladderRecipe(SPACE_ASPECT_LADDER, SPACE_ASPECT_PLANKS).group("aspect_ladder").save(recipeSaver);
		CommonRecipes.signRecipe(SPACE_ASPECT_SIGN, SPACE_ASPECT_PLANKS).group("aspect_sign").save(recipeSaver);
		CommonRecipes.hangingSignRecipe(SPACE_ASPECT_HANGING_SIGN, SPACE_ASPECT_STRIPPED_LOG).group("aspect_hanging_sign").save(recipeSaver);
		
		CommonRecipes.stairsRecipe(TIME_ASPECT_STAIRS, TIME_ASPECT_PLANKS).group("wooden_stairs").save(recipeSaver);
		CommonRecipes.slabRecipe(TIME_ASPECT_SLAB, TIME_ASPECT_PLANKS).group("wooden_slab").save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, TIME_ASPECT_FENCE.asItem(), 3).group("wooden_fence")
				.define('#', TIME_ASPECT_PLANKS.asItem()).define('$', Items.STICK).pattern("#$#").pattern("#$#")
				.unlockedBy("has_time_aspect_planks", has(TIME_ASPECT_PLANKS.asItem())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, TIME_ASPECT_FENCE_GATE.asItem(), 1).group("wooden_fence_gate")
				.define('#', TIME_ASPECT_PLANKS.asItem()).define('$', Items.STICK).pattern("$#$").pattern("$#$")
				.unlockedBy("has_time_aspect_planks", has(TIME_ASPECT_PLANKS.asItem())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, TIME_ASPECT_DOOR.asItem(), 3).group("wooden_door")
				.define('#', TIME_ASPECT_PLANKS.asItem()).pattern("## ").pattern("## ").pattern("## ")
				.unlockedBy("has_time_aspect_planks", has(TIME_ASPECT_PLANKS.asItem())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, TIME_ASPECT_TRAPDOOR.asItem(), 2).group("wooden_trapdoor")
				.define('#', TIME_ASPECT_PLANKS.asItem()).pattern("###").pattern("###")
				.unlockedBy("has_time_aspect_planks", has(TIME_ASPECT_PLANKS.asItem())).save(recipeSaver);
		CommonRecipes.pressurePlateRecipe(TIME_ASPECT_PRESSURE_PLATE, TIME_ASPECT_PLANKS).group("stone_pressure_plate").save(recipeSaver);
		CommonRecipes.buttonRecipe(TIME_ASPECT_BUTTON, TIME_ASPECT_PLANKS).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, TIME_ASPECT_CARVED_PLANKS.asItem(), 8).group("aspect_carved_planks")
				.define('#', TIME_ASPECT_PLANKS.asItem()).define('$', TIME_ASPECT_STRIPPED_LOG.asItem()).pattern("#$").pattern("$#")
				.unlockedBy("has_time_aspect_planks", has(TIME_ASPECT_PLANKS.asItem())).save(recipeSaver);
		CommonRecipes.woodRecipe(TIME_ASPECT_WOOD, TIME_ASPECT_LOG).group("aspect_wood").save(recipeSaver);
		CommonRecipes.woodRecipe(TIME_ASPECT_STRIPPED_WOOD, TIME_ASPECT_STRIPPED_LOG).group("aspect_stripped_wood").save(recipeSaver);
		CommonRecipes.planksRecipe(TIME_ASPECT_PLANKS, TIME_ASPECT_LOG).group("aspect_planks").save(recipeSaver);
		CommonRecipes.planksRecipe(TIME_ASPECT_PLANKS, TIME_ASPECT_STRIPPED_LOG).group("aspect_planks").save(recipeSaver, new ResourceLocation(Minestuck.MOD_ID, "time_aspect_planks_from_stripped_log"));
		CommonRecipes.planksRecipe(TIME_ASPECT_PLANKS, TIME_ASPECT_WOOD).group("aspect_planks").save(recipeSaver, new ResourceLocation(Minestuck.MOD_ID, "time_aspect_planks_from_wood"));
		CommonRecipes.planksRecipe(TIME_ASPECT_PLANKS, TIME_ASPECT_STRIPPED_WOOD).group("aspect_planks").save(recipeSaver, new ResourceLocation(Minestuck.MOD_ID, "time_aspect_planks_from_stripped_wood"));
		CommonRecipes.bookshelfRecipe(TIME_ASPECT_BOOKSHELF, TIME_ASPECT_PLANKS, TIME_ASPECT_SLAB).group("aspect_bookshelf").save(recipeSaver);
		CommonRecipes.ladderRecipe(TIME_ASPECT_LADDER, TIME_ASPECT_PLANKS).group("aspect_ladder").save(recipeSaver);
		CommonRecipes.signRecipe(TIME_ASPECT_SIGN, TIME_ASPECT_PLANKS).group("aspect_sign").save(recipeSaver);
		CommonRecipes.hangingSignRecipe(TIME_ASPECT_HANGING_SIGN, TIME_ASPECT_STRIPPED_LOG).group("aspect_hanging_sign").save(recipeSaver);
		
		CommonRecipes.stairsRecipe(VOID_ASPECT_STAIRS, VOID_ASPECT_PLANKS).group("wooden_stairs").save(recipeSaver);
		CommonRecipes.slabRecipe(VOID_ASPECT_SLAB, VOID_ASPECT_PLANKS).group("wooden_slab").save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, VOID_ASPECT_FENCE.asItem(), 3).group("wooden_fence")
				.define('#', VOID_ASPECT_PLANKS.asItem()).define('$', Items.STICK).pattern("#$#").pattern("#$#")
				.unlockedBy("has_void_aspect_planks", has(VOID_ASPECT_PLANKS.asItem())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, VOID_ASPECT_FENCE_GATE.asItem(), 1).group("wooden_fence_gate")
				.define('#', VOID_ASPECT_PLANKS.asItem()).define('$', Items.STICK).pattern("$#$").pattern("$#$")
				.unlockedBy("has_void_aspect_planks", has(VOID_ASPECT_PLANKS.asItem())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, VOID_ASPECT_DOOR.asItem(), 3).group("wooden_door")
				.define('#', VOID_ASPECT_PLANKS.asItem()).pattern("## ").pattern("## ").pattern("## ")
				.unlockedBy("has_void_aspect_planks", has(VOID_ASPECT_PLANKS.asItem())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, VOID_ASPECT_TRAPDOOR.asItem(), 2).group("wooden_trapdoor")
				.define('#', VOID_ASPECT_PLANKS.asItem()).pattern("###").pattern("###")
				.unlockedBy("has_void_aspect_planks", has(VOID_ASPECT_PLANKS.asItem())).save(recipeSaver);
		CommonRecipes.pressurePlateRecipe(VOID_ASPECT_PRESSURE_PLATE, VOID_ASPECT_PLANKS).group("stone_pressure_plate").save(recipeSaver);
		CommonRecipes.buttonRecipe(VOID_ASPECT_BUTTON, VOID_ASPECT_PLANKS).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, VOID_ASPECT_CARVED_PLANKS.asItem(), 8).group("aspect_carved_planks")
				.define('#', VOID_ASPECT_PLANKS.asItem()).define('$', VOID_ASPECT_STRIPPED_LOG.asItem()).pattern("#$").pattern("$#")
				.unlockedBy("has_void_aspect_planks", has(VOID_ASPECT_PLANKS.asItem())).save(recipeSaver);
		CommonRecipes.woodRecipe(VOID_ASPECT_WOOD, VOID_ASPECT_LOG).group("aspect_wood").save(recipeSaver);
		CommonRecipes.woodRecipe(VOID_ASPECT_STRIPPED_WOOD, VOID_ASPECT_STRIPPED_LOG).group("aspect_stripped_wood").save(recipeSaver);
		CommonRecipes.planksRecipe(VOID_ASPECT_PLANKS, VOID_ASPECT_LOG).group("aspect_planks").save(recipeSaver);
		CommonRecipes.planksRecipe(VOID_ASPECT_PLANKS, VOID_ASPECT_STRIPPED_LOG).group("aspect_planks").save(recipeSaver, new ResourceLocation(Minestuck.MOD_ID, "void_aspect_planks_from_stripped_log"));
		CommonRecipes.planksRecipe(VOID_ASPECT_PLANKS, VOID_ASPECT_WOOD).group("aspect_planks").save(recipeSaver, new ResourceLocation(Minestuck.MOD_ID, "void_aspect_planks_from_wood"));
		CommonRecipes.planksRecipe(VOID_ASPECT_PLANKS, VOID_ASPECT_STRIPPED_WOOD).group("aspect_planks").save(recipeSaver, new ResourceLocation(Minestuck.MOD_ID, "void_aspect_planks_from_stripped_wood"));
		CommonRecipes.bookshelfRecipe(VOID_ASPECT_BOOKSHELF, VOID_ASPECT_PLANKS, VOID_ASPECT_SLAB).group("aspect_bookshelf").save(recipeSaver);
		CommonRecipes.ladderRecipe(VOID_ASPECT_LADDER, VOID_ASPECT_PLANKS).group("aspect_ladder").save(recipeSaver);
		CommonRecipes.signRecipe(VOID_ASPECT_SIGN, VOID_ASPECT_PLANKS).group("aspect_sign").save(recipeSaver);
		CommonRecipes.hangingSignRecipe(VOID_ASPECT_HANGING_SIGN, VOID_ASPECT_STRIPPED_LOG).group("aspect_hanging_sign").save(recipeSaver);
		
	}
}
