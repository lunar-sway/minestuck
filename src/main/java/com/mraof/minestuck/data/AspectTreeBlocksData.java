package com.mraof.minestuck.data;

import com.mraof.minestuck.data.loot_table.MSBlockLootTables;
import com.mraof.minestuck.data.tag.MinestuckBlockTagsProvider;
import com.mraof.minestuck.util.MSTags;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;

import static com.mraof.minestuck.block.AspectTreeBlocks.*;
import static com.mraof.minestuck.data.MSBlockStateProvider.texture;
import static com.mraof.minestuck.data.recipe.MinestuckRecipeProvider.has;

public final class AspectTreeBlocksData
{
	public static void addEnUsTranslations(MinestuckLanguageProvider provider)
	{
		provider.addBlock(BLOOD_ASPECT_LOG, "Blood Log");
		provider.addBlock(BLOOD_ASPECT_LEAVES, "Blood Leaves");
		provider.addBlock(BLOOD_ASPECT_SAPLING, "Blood Sapling");
		provider.addBlockTooltip(BLOOD_ASPECT_SAPLING, "Not yet implemented");
		provider.addBlock(BLOOD_ASPECT_PLANKS, "Blood Planks");
		provider.addBlock(BLOOD_ASPECT_STAIRS, "Blood Stairs");
		provider.addBlock(BLOOD_ASPECT_SLAB, "Blood Slab");
		provider.addBlock(BLOOD_ASPECT_FENCE, "Blood Fence");
		provider.addBlock(BLOOD_ASPECT_FENCE_GATE, "Blood Fence Gate");
		provider.addBlock(BLOOD_ASPECT_DOOR, "Blood Door");
		provider.addBlock(BLOOD_ASPECT_TRAPDOOR, "Blood Trapdoor");
		provider.addBlock(BLOOD_ASPECT_PRESSURE_PLATE, "Blood Pressure Plate");
		provider.addBlock(BLOOD_ASPECT_BUTTON, "Blood Button");
		provider.addBlock(BLOOD_ASPECT_BOOKSHELF, "Blood Bookshelf");
		provider.addBlock(BLOOD_ASPECT_LADDER, "Blood Ladder");
		
		provider.addBlock(BREATH_ASPECT_LOG, "Breath Log");
		provider.addBlock(BREATH_ASPECT_LEAVES, "Breath Leaves");
		provider.addBlock(BREATH_ASPECT_SAPLING, "Breath Sapling");
		provider.addBlockTooltip(BREATH_ASPECT_SAPLING, "Not yet implemented");
		provider.addBlock(BREATH_ASPECT_PLANKS, "Breath Planks");
		provider.addBlock(BREATH_ASPECT_STAIRS, "Breath Stairs");
		provider.addBlock(BREATH_ASPECT_SLAB, "Breath Slab");
		provider.addBlock(BREATH_ASPECT_FENCE, "Breath Fence");
		provider.addBlock(BREATH_ASPECT_FENCE_GATE, "Breath Fence Gate");
		provider.addBlock(BREATH_ASPECT_DOOR, "Breath Door");
		provider.addBlock(BREATH_ASPECT_TRAPDOOR, "Breath Trapdoor");
		provider.addBlock(BREATH_ASPECT_PRESSURE_PLATE, "Breath Pressure Plate");
		provider.addBlock(BREATH_ASPECT_BUTTON, "Breath Button");
		provider.addBlock(BREATH_ASPECT_BOOKSHELF, "Breath Bookshelf");
		provider.addBlock(BREATH_ASPECT_LADDER, "Breath Ladder");
		
		provider.addBlock(DOOM_ASPECT_LOG, "Doom Log");
		provider.addBlock(DOOM_ASPECT_LEAVES, "Doom Leaves");
		provider.addBlock(DOOM_ASPECT_SAPLING, "Doom Sapling");
		provider.addBlockTooltip(DOOM_ASPECT_SAPLING, "Not yet implemented");
		provider.addBlock(DOOM_ASPECT_PLANKS, "Doom Planks");
		provider.addBlock(DOOM_ASPECT_STAIRS, "Doom Stairs");
		provider.addBlock(DOOM_ASPECT_SLAB, "Doom Slab");
		provider.addBlock(DOOM_ASPECT_FENCE, "Doom Fence");
		provider.addBlock(DOOM_ASPECT_FENCE_GATE, "Doom Fence Gate");
		provider.addBlock(DOOM_ASPECT_DOOR, "Doom Door");
		provider.addBlock(DOOM_ASPECT_TRAPDOOR, "Doom Trapdoor");
		provider.addBlock(DOOM_ASPECT_PRESSURE_PLATE, "Doom Pressure Plate");
		provider.addBlock(DOOM_ASPECT_BUTTON, "Doom Button");
		provider.addBlock(DOOM_ASPECT_BOOKSHELF, "Doom Bookshelf");
		provider.addBlock(DOOM_ASPECT_LADDER, "Doom Ladder");
		
		provider.addBlock(HEART_ASPECT_LOG, "Heart Log");
		provider.addBlock(HEART_ASPECT_LEAVES, "Heart Leaves");
		provider.addBlock(HEART_ASPECT_SAPLING, "Heart Sapling");
		provider.addBlockTooltip(HEART_ASPECT_SAPLING, "Not yet implemented");
		provider.addBlock(HEART_ASPECT_PLANKS, "Heart Planks");
		provider.addBlock(HEART_ASPECT_STAIRS, "Heart Stairs");
		provider.addBlock(HEART_ASPECT_SLAB, "Heart Slab");
		provider.addBlock(HEART_ASPECT_FENCE, "Heart Fence");
		provider.addBlock(HEART_ASPECT_FENCE_GATE, "Heart Fence Gate");
		provider.addBlock(HEART_ASPECT_DOOR, "Heart Door");
		provider.addBlock(HEART_ASPECT_TRAPDOOR, "Heart Trapdoor");
		provider.addBlock(HEART_ASPECT_PRESSURE_PLATE, "Heart Pressure Plate");
		provider.addBlock(HEART_ASPECT_BUTTON, "Heart Button");
		provider.addBlock(HEART_ASPECT_BOOKSHELF, "Heart Bookshelf");
		provider.addBlock(HEART_ASPECT_LADDER, "Heart Ladder");
		
		provider.addBlock(HOPE_ASPECT_LOG, "Hope Log");
		provider.addBlock(HOPE_ASPECT_LEAVES, "Hope Leaves");
		provider.addBlock(HOPE_ASPECT_SAPLING, "Hope Sapling");
		provider.addBlockTooltip(HOPE_ASPECT_SAPLING, "Not yet implemented");
		provider.addBlock(HOPE_ASPECT_PLANKS, "Hope Planks");
		provider.addBlock(HOPE_ASPECT_STAIRS, "Hope Stairs");
		provider.addBlock(HOPE_ASPECT_SLAB, "Hope Slab");
		provider.addBlock(HOPE_ASPECT_FENCE, "Hope Fence");
		provider.addBlock(HOPE_ASPECT_FENCE_GATE, "Hope Fence Gate");
		provider.addBlock(HOPE_ASPECT_DOOR, "Hope Door");
		provider.addBlock(HOPE_ASPECT_TRAPDOOR, "Hope Trapdoor");
		provider.addBlock(HOPE_ASPECT_PRESSURE_PLATE, "Hope Pressure Plate");
		provider.addBlock(HOPE_ASPECT_BUTTON, "Hope Button");
		provider.addBlock(HOPE_ASPECT_BOOKSHELF, "Hope Bookshelf");
		provider.addBlock(HOPE_ASPECT_LADDER, "Hope Ladder");
		
		provider.addBlock(LIFE_ASPECT_LOG, "Life Log");
		provider.addBlock(LIFE_ASPECT_LEAVES, "Life Leaves");
		provider.addBlock(LIFE_ASPECT_SAPLING, "Life Sapling");
		provider.addBlockTooltip(LIFE_ASPECT_SAPLING, "Not yet implemented");
		provider.addBlock(LIFE_ASPECT_PLANKS, "Life Planks");
		provider.addBlock(LIFE_ASPECT_STAIRS, "Life Stairs");
		provider.addBlock(LIFE_ASPECT_SLAB, "Life Slab");
		provider.addBlock(LIFE_ASPECT_FENCE, "Life Fence");
		provider.addBlock(LIFE_ASPECT_FENCE_GATE, "Life Fence Gate");
		provider.addBlock(LIFE_ASPECT_DOOR, "Life Door");
		provider.addBlock(LIFE_ASPECT_TRAPDOOR, "Life Trapdoor");
		provider.addBlock(LIFE_ASPECT_PRESSURE_PLATE, "Life Pressure Plate");
		provider.addBlock(LIFE_ASPECT_BUTTON, "Life Button");
		provider.addBlock(LIFE_ASPECT_BOOKSHELF, "Life Bookshelf");
		provider.addBlock(LIFE_ASPECT_LADDER, "Life Ladder");
		
		provider.addBlock(LIGHT_ASPECT_LOG, "Light Log");
		provider.addBlock(LIGHT_ASPECT_LEAVES, "Light Leaves");
		provider.addBlock(LIGHT_ASPECT_SAPLING, "Light Sapling");
		provider.addBlockTooltip(LIGHT_ASPECT_SAPLING, "Not yet implemented");
		provider.addBlock(LIGHT_ASPECT_PLANKS, "Light Planks");
		provider.addBlock(LIGHT_ASPECT_STAIRS, "Light Stairs");
		provider.addBlock(LIGHT_ASPECT_SLAB, "Light Slab");
		provider.addBlock(LIGHT_ASPECT_FENCE, "Light Fence");
		provider.addBlock(LIGHT_ASPECT_FENCE_GATE, "Light Fence Gate");
		provider.addBlock(LIGHT_ASPECT_DOOR, "Light Door");
		provider.addBlock(LIGHT_ASPECT_TRAPDOOR, "Light Trapdoor");
		provider.addBlock(LIGHT_ASPECT_PRESSURE_PLATE, "Light Pressure Plate");
		provider.addBlock(LIGHT_ASPECT_BUTTON, "Light Button");
		provider.addBlock(LIGHT_ASPECT_BOOKSHELF, "Light Bookshelf");
		provider.addBlock(LIGHT_ASPECT_LADDER, "Light Ladder");
		
		provider.addBlock(MIND_ASPECT_LOG, "Mind Log");
		provider.addBlock(MIND_ASPECT_LEAVES, "Mind Leaves");
		provider.addBlock(MIND_ASPECT_SAPLING, "Mind Sapling");
		provider.addBlockTooltip(MIND_ASPECT_SAPLING, "Not yet implemented");
		provider.addBlock(MIND_ASPECT_PLANKS, "Mind Planks");
		provider.addBlock(MIND_ASPECT_STAIRS, "Mind Stairs");
		provider.addBlock(MIND_ASPECT_SLAB, "Mind Slab");
		provider.addBlock(MIND_ASPECT_FENCE, "Mind Fence");
		provider.addBlock(MIND_ASPECT_FENCE_GATE, "Mind Fence Gate");
		provider.addBlock(MIND_ASPECT_DOOR, "Mind Door");
		provider.addBlock(MIND_ASPECT_TRAPDOOR, "Mind Trapdoor");
		provider.addBlock(MIND_ASPECT_PRESSURE_PLATE, "Mind Pressure Plate");
		provider.addBlock(MIND_ASPECT_BUTTON, "Mind Button");
		provider.addBlock(MIND_ASPECT_BOOKSHELF, "Mind Bookshelf");
		provider.addBlock(MIND_ASPECT_LADDER, "Mind Ladder");
		
		provider.addBlock(RAGE_ASPECT_LOG, "Rage Log");
		provider.addBlock(RAGE_ASPECT_LEAVES, "Rage Leaves");
		provider.addBlock(RAGE_ASPECT_SAPLING, "Rage Sapling");
		provider.addBlockTooltip(RAGE_ASPECT_SAPLING, "Not yet implemented");
		provider.addBlock(RAGE_ASPECT_PLANKS, "Rage Planks");
		provider.addBlock(RAGE_ASPECT_STAIRS, "Rage Stairs");
		provider.addBlock(RAGE_ASPECT_SLAB, "Rage Slab");
		provider.addBlock(RAGE_ASPECT_FENCE, "Rage Fence");
		provider.addBlock(RAGE_ASPECT_FENCE_GATE, "Rage Fence Gate");
		provider.addBlock(RAGE_ASPECT_DOOR, "Rage Door");
		provider.addBlock(RAGE_ASPECT_TRAPDOOR, "Rage Trapdoor");
		provider.addBlock(RAGE_ASPECT_PRESSURE_PLATE, "Rage Pressure Plate");
		provider.addBlock(RAGE_ASPECT_BUTTON, "Rage Button");
		provider.addBlock(RAGE_ASPECT_BOOKSHELF, "Rage Bookshelf");
		provider.addBlock(RAGE_ASPECT_LADDER, "Rage Ladder");
		
		provider.addBlock(SPACE_ASPECT_LOG, "Space Log");
		provider.addBlock(SPACE_ASPECT_LEAVES, "Space Leaves");
		provider.addBlock(SPACE_ASPECT_SAPLING, "Space Sapling");
		provider.addBlockTooltip(SPACE_ASPECT_SAPLING, "Not yet implemented");
		provider.addBlock(SPACE_ASPECT_PLANKS, "Space Planks");
		provider.addBlock(SPACE_ASPECT_STAIRS, "Space Stairs");
		provider.addBlock(SPACE_ASPECT_SLAB, "Space Slab");
		provider.addBlock(SPACE_ASPECT_FENCE, "Space Fence");
		provider.addBlock(SPACE_ASPECT_FENCE_GATE, "Space Fence Gate");
		provider.addBlock(SPACE_ASPECT_DOOR, "Space Door");
		provider.addBlock(SPACE_ASPECT_TRAPDOOR, "Space Trapdoor");
		provider.addBlock(SPACE_ASPECT_PRESSURE_PLATE, "Space Pressure Plate");
		provider.addBlock(SPACE_ASPECT_BUTTON, "Space Button");
		provider.addBlock(SPACE_ASPECT_BOOKSHELF, "Space Bookshelf");
		provider.addBlock(SPACE_ASPECT_LADDER, "Space Ladder");
		
		provider.addBlock(TIME_ASPECT_LOG, "Time Log");
		provider.addBlock(TIME_ASPECT_LEAVES, "Time Leaves");
		provider.addBlock(TIME_ASPECT_SAPLING, "Time Sapling");
		provider.addBlockTooltip(TIME_ASPECT_SAPLING, "Not yet implemented");
		provider.addBlock(TIME_ASPECT_PLANKS, "Time Planks");
		provider.addBlock(TIME_ASPECT_STAIRS, "Time Stairs");
		provider.addBlock(TIME_ASPECT_SLAB, "Time Slab");
		provider.addBlock(TIME_ASPECT_FENCE, "Time Fence");
		provider.addBlock(TIME_ASPECT_FENCE_GATE, "Time Fence Gate");
		provider.addBlock(TIME_ASPECT_DOOR, "Time Door");
		provider.addBlock(TIME_ASPECT_TRAPDOOR, "Time Trapdoor");
		provider.addBlock(TIME_ASPECT_PRESSURE_PLATE, "Time Pressure Plate");
		provider.addBlock(TIME_ASPECT_BUTTON, "Time Button");
		provider.addBlock(TIME_ASPECT_BOOKSHELF, "Time Bookshelf");
		provider.addBlock(TIME_ASPECT_LADDER, "Time Ladder");
		
		provider.addBlock(VOID_ASPECT_LOG, "Void Log");
		provider.addBlock(VOID_ASPECT_LEAVES, "Void Leaves");
		provider.addBlock(VOID_ASPECT_SAPLING, "Void Sapling");
		provider.addBlockTooltip(VOID_ASPECT_SAPLING, "Not yet implemented");
		provider.addBlock(VOID_ASPECT_PLANKS, "Void Planks");
		provider.addBlock(VOID_ASPECT_STAIRS, "Void Stairs");
		provider.addBlock(VOID_ASPECT_SLAB, "Void Slab");
		provider.addBlock(VOID_ASPECT_FENCE, "Void Fence");
		provider.addBlock(VOID_ASPECT_FENCE_GATE, "Void Fence Gate");
		provider.addBlock(VOID_ASPECT_DOOR, "Void Door");
		provider.addBlock(VOID_ASPECT_TRAPDOOR, "Void Trapdoor");
		provider.addBlock(VOID_ASPECT_PRESSURE_PLATE, "Void Pressure Plate");
		provider.addBlock(VOID_ASPECT_BUTTON, "Void Button");
		provider.addBlock(VOID_ASPECT_BOOKSHELF, "Void Bookshelf");
		provider.addBlock(VOID_ASPECT_LADDER, "Void Ladder");
	}
	
	public static void addModels(MSBlockStateProvider provider)
	{
		provider.axisWithItem(BLOOD_ASPECT_LOG,
				id -> provider.models().cubeColumn(id.getPath(),
						texture(id),
						texture(id.withSuffix("_top"))));
		provider.simpleBlockWithItem(BLOOD_ASPECT_LEAVES);
		provider.simpleBlock(BLOOD_ASPECT_SAPLING,
				id -> provider.models().cross(id.getPath(), texture(id)).renderType("cutout"));
		provider.flatItem(BLOOD_ASPECT_SAPLING_ITEM, MSBlockStateProvider::texture);
		provider.simpleBlockWithItem(BLOOD_ASPECT_PLANKS);
		provider.stairsWithItem(BLOOD_ASPECT_STAIRS, BLOOD_ASPECT_PLANKS);
		provider.slabWithItem(BLOOD_ASPECT_SLAB, BLOOD_ASPECT_PLANKS);
		provider.fenceWithItem(BLOOD_ASPECT_FENCE, BLOOD_ASPECT_PLANKS);
		provider.fenceGateWithItem(BLOOD_ASPECT_FENCE_GATE, BLOOD_ASPECT_PLANKS);
		provider.simpleDoorBlock(BLOOD_ASPECT_DOOR);
		provider.trapDoorWithItem(BLOOD_ASPECT_TRAPDOOR);
		provider.flatItem(BLOOD_ASPECT_DOOR_ITEM, MSBlockStateProvider::itemTexture);
		provider.pressurePlateWithItem(BLOOD_ASPECT_PRESSURE_PLATE, BLOOD_ASPECT_PLANKS);
		provider.buttonWithItem(BLOOD_ASPECT_BUTTON, BLOOD_ASPECT_PLANKS);
		provider.simpleBlockWithItem(BLOOD_ASPECT_BOOKSHELF,
				id -> provider.models().cubeColumn(id.getPath(),
						texture(id),
						texture("blood_aspect_planks")));
		provider.simpleHorizontal(BLOOD_ASPECT_LADDER, provider::ladder);
		provider.flatItem(BLOOD_ASPECT_LADDER_ITEM, MSBlockStateProvider::texture);
		
		
		provider.axisWithItem(BREATH_ASPECT_LOG,
				id -> provider.models().cubeColumn(id.getPath(),
						texture(id),
						texture(id.withSuffix("_top"))));
		provider.simpleBlockWithItem(BREATH_ASPECT_LEAVES);
		provider.simpleBlock(BREATH_ASPECT_SAPLING,
				id -> provider.models().cross(id.getPath(), texture(id)).renderType("cutout"));
		provider.flatItem(BREATH_ASPECT_SAPLING_ITEM, MSBlockStateProvider::texture);
		provider.simpleBlockWithItem(BREATH_ASPECT_PLANKS);
		provider.stairsWithItem(BREATH_ASPECT_STAIRS, BREATH_ASPECT_PLANKS);
		provider.slabWithItem(BREATH_ASPECT_SLAB, BREATH_ASPECT_PLANKS);
		provider.fenceWithItem(BREATH_ASPECT_FENCE, BREATH_ASPECT_PLANKS);
		provider.fenceGateWithItem(BREATH_ASPECT_FENCE_GATE, BREATH_ASPECT_PLANKS);
		provider.simpleDoorBlock(BREATH_ASPECT_DOOR);
		provider.trapDoorWithItem(BREATH_ASPECT_TRAPDOOR);
		provider.flatItem(BREATH_ASPECT_DOOR_ITEM, MSBlockStateProvider::itemTexture);
		provider.pressurePlateWithItem(BREATH_ASPECT_PRESSURE_PLATE, BREATH_ASPECT_PLANKS);
		provider.buttonWithItem(BREATH_ASPECT_BUTTON, BREATH_ASPECT_PLANKS);
		provider.simpleBlockWithItem(BREATH_ASPECT_BOOKSHELF,
				id -> provider.models().cubeColumn(id.getPath(),
						texture(id),
						texture("breath_aspect_planks")));
		provider.simpleHorizontal(BREATH_ASPECT_LADDER, provider::ladder);
		provider.flatItem(BREATH_ASPECT_LADDER_ITEM, MSBlockStateProvider::texture);
		
		
		provider.axisWithItem(DOOM_ASPECT_LOG,
				id -> provider.models().cubeColumn(id.getPath(),
						texture(id),
						texture(id.withSuffix("_top"))));
		provider.simpleBlockWithItem(DOOM_ASPECT_LEAVES);
		provider.simpleBlock(DOOM_ASPECT_SAPLING,
				id -> provider.models().cross(id.getPath(), texture(id)).renderType("cutout"));
		provider.flatItem(DOOM_ASPECT_SAPLING_ITEM, MSBlockStateProvider::texture);
		provider.simpleBlockWithItem(DOOM_ASPECT_PLANKS);
		provider.stairsWithItem(DOOM_ASPECT_STAIRS, DOOM_ASPECT_PLANKS);
		provider.slabWithItem(DOOM_ASPECT_SLAB, DOOM_ASPECT_PLANKS);
		provider.fenceWithItem(DOOM_ASPECT_FENCE, DOOM_ASPECT_PLANKS);
		provider.fenceGateWithItem(DOOM_ASPECT_FENCE_GATE, DOOM_ASPECT_PLANKS);
		provider.simpleDoorBlock(DOOM_ASPECT_DOOR);
		provider.trapDoorWithItem(DOOM_ASPECT_TRAPDOOR);
		provider.flatItem(DOOM_ASPECT_DOOR_ITEM, MSBlockStateProvider::itemTexture);
		provider.pressurePlateWithItem(DOOM_ASPECT_PRESSURE_PLATE, DOOM_ASPECT_PLANKS);
		provider.buttonWithItem(DOOM_ASPECT_BUTTON, DOOM_ASPECT_PLANKS);
		provider.simpleBlockWithItem(DOOM_ASPECT_BOOKSHELF,
				id -> provider.models().cubeColumn(id.getPath(),
						texture(id),
						texture("doom_aspect_planks")));
		provider.simpleHorizontal(DOOM_ASPECT_LADDER, provider::ladder);
		provider.flatItem(DOOM_ASPECT_LADDER_ITEM, MSBlockStateProvider::texture);
		
		
		provider.axisWithItem(HEART_ASPECT_LOG,
				id -> provider.models().cubeColumn(id.getPath(),
						texture(id),
						texture(id.withSuffix("_top"))));
		provider.simpleBlockWithItem(HEART_ASPECT_LEAVES);
		provider.simpleBlock(HEART_ASPECT_SAPLING,
				id -> provider.models().cross(id.getPath(), texture(id)).renderType("cutout"));
		provider.flatItem(HEART_ASPECT_SAPLING_ITEM, MSBlockStateProvider::texture);
		provider.simpleBlockWithItem(HEART_ASPECT_PLANKS);
		provider.stairsWithItem(HEART_ASPECT_STAIRS, HEART_ASPECT_PLANKS);
		provider.slabWithItem(HEART_ASPECT_SLAB, HEART_ASPECT_PLANKS);
		provider.fenceWithItem(HEART_ASPECT_FENCE, HEART_ASPECT_PLANKS);
		provider.fenceGateWithItem(HEART_ASPECT_FENCE_GATE, HEART_ASPECT_PLANKS);
		provider.simpleDoorBlock(HEART_ASPECT_DOOR);
		provider.trapDoorWithItem(HEART_ASPECT_TRAPDOOR);
		provider.flatItem(HEART_ASPECT_DOOR_ITEM, MSBlockStateProvider::itemTexture);
		provider.pressurePlateWithItem(HEART_ASPECT_PRESSURE_PLATE, HEART_ASPECT_PLANKS);
		provider.buttonWithItem(HEART_ASPECT_BUTTON, HEART_ASPECT_PLANKS);
		provider.simpleBlockWithItem(HEART_ASPECT_BOOKSHELF,
				id -> provider.models().cubeColumn(id.getPath(),
						texture(id),
						texture("heart_aspect_planks")));
		provider.simpleHorizontal(HEART_ASPECT_LADDER, provider::ladder);
		provider.flatItem(HEART_ASPECT_LADDER_ITEM, MSBlockStateProvider::texture);
		
		
		provider.axisWithItem(HOPE_ASPECT_LOG,
				id -> provider.models().cubeColumn(id.getPath(),
						texture(id),
						texture(id.withSuffix("_top"))));
		provider.simpleBlockWithItem(HOPE_ASPECT_LEAVES);
		provider.simpleBlock(HOPE_ASPECT_SAPLING,
				id -> provider.models().cross(id.getPath(), texture(id)).renderType("cutout"));
		provider.flatItem(HOPE_ASPECT_SAPLING_ITEM, MSBlockStateProvider::texture);
		provider.simpleBlockWithItem(HOPE_ASPECT_PLANKS);
		provider.stairsWithItem(HOPE_ASPECT_STAIRS, HOPE_ASPECT_PLANKS);
		provider.slabWithItem(HOPE_ASPECT_SLAB, HOPE_ASPECT_PLANKS);
		provider.fenceWithItem(HOPE_ASPECT_FENCE, HOPE_ASPECT_PLANKS);
		provider.fenceGateWithItem(HOPE_ASPECT_FENCE_GATE, HOPE_ASPECT_PLANKS);
		provider.simpleDoorBlock(HOPE_ASPECT_DOOR);
		provider.trapDoorWithItem(HOPE_ASPECT_TRAPDOOR);
		provider.flatItem(HOPE_ASPECT_DOOR_ITEM, MSBlockStateProvider::itemTexture);
		provider.pressurePlateWithItem(HOPE_ASPECT_PRESSURE_PLATE, HOPE_ASPECT_PLANKS);
		provider.buttonWithItem(HOPE_ASPECT_BUTTON, HOPE_ASPECT_PLANKS);
		provider.simpleBlockWithItem(HOPE_ASPECT_BOOKSHELF,
				id -> provider.models().cubeColumn(id.getPath(),
						texture(id),
						texture("hope_aspect_planks")));
		provider.simpleHorizontal(HOPE_ASPECT_LADDER, provider::ladder);
		provider.flatItem(HOPE_ASPECT_LADDER_ITEM, MSBlockStateProvider::texture);
		
		
		provider.axisWithItem(LIFE_ASPECT_LOG,
				id -> provider.models().cubeColumn(id.getPath(),
						texture(id),
						texture(id.withSuffix("_top"))));
		provider.simpleBlockWithItem(LIFE_ASPECT_LEAVES);
		provider.simpleBlock(LIFE_ASPECT_SAPLING,
				id -> provider.models().cross(id.getPath(), texture(id)).renderType("cutout"));
		provider.flatItem(LIFE_ASPECT_SAPLING_ITEM, MSBlockStateProvider::texture);
		provider.simpleBlockWithItem(LIFE_ASPECT_PLANKS);
		provider.stairsWithItem(LIFE_ASPECT_STAIRS, LIFE_ASPECT_PLANKS);
		provider.slabWithItem(LIFE_ASPECT_SLAB, LIFE_ASPECT_PLANKS);
		provider.fenceWithItem(LIFE_ASPECT_FENCE, LIFE_ASPECT_PLANKS);
		provider.fenceGateWithItem(LIFE_ASPECT_FENCE_GATE, LIFE_ASPECT_PLANKS);
		provider.simpleDoorBlock(LIFE_ASPECT_DOOR);
		provider.trapDoorWithItem(LIFE_ASPECT_TRAPDOOR);
		provider.flatItem(LIFE_ASPECT_DOOR_ITEM, MSBlockStateProvider::itemTexture);
		provider.pressurePlateWithItem(LIFE_ASPECT_PRESSURE_PLATE, LIFE_ASPECT_PLANKS);
		provider.buttonWithItem(LIFE_ASPECT_BUTTON, LIFE_ASPECT_PLANKS);
		provider.simpleBlockWithItem(LIFE_ASPECT_BOOKSHELF,
				id -> provider.models().cubeColumn(id.getPath(),
						texture(id),
						texture("life_aspect_planks")));
		provider.simpleHorizontal(LIFE_ASPECT_LADDER, provider::ladder);
		provider.flatItem(LIFE_ASPECT_LADDER_ITEM, MSBlockStateProvider::texture);
		
		
		provider.axisWithItem(LIGHT_ASPECT_LOG,
				id -> provider.models().cubeColumn(id.getPath(),
						texture(id),
						texture(id.withSuffix("_top"))));
		provider.simpleBlockWithItem(LIGHT_ASPECT_LEAVES);
		provider.simpleBlock(LIGHT_ASPECT_SAPLING,
				id -> provider.models().cross(id.getPath(), texture(id)).renderType("cutout"));
		provider.flatItem(LIGHT_ASPECT_SAPLING_ITEM, MSBlockStateProvider::texture);
		provider.simpleBlockWithItem(LIGHT_ASPECT_PLANKS);
		provider.stairsWithItem(LIGHT_ASPECT_STAIRS, LIGHT_ASPECT_PLANKS);
		provider.slabWithItem(LIGHT_ASPECT_SLAB, LIGHT_ASPECT_PLANKS);
		provider.fenceWithItem(LIGHT_ASPECT_FENCE, LIGHT_ASPECT_PLANKS);
		provider.fenceGateWithItem(LIGHT_ASPECT_FENCE_GATE, LIGHT_ASPECT_PLANKS);
		provider.simpleDoorBlock(LIGHT_ASPECT_DOOR);
		provider.trapDoorWithItem(LIGHT_ASPECT_TRAPDOOR);
		provider.flatItem(LIGHT_ASPECT_DOOR_ITEM, MSBlockStateProvider::itemTexture);
		provider.pressurePlateWithItem(LIGHT_ASPECT_PRESSURE_PLATE, LIGHT_ASPECT_PLANKS);
		provider.buttonWithItem(LIGHT_ASPECT_BUTTON, LIGHT_ASPECT_PLANKS);
		provider.simpleBlockWithItem(LIGHT_ASPECT_BOOKSHELF,
				id -> provider.models().cubeColumn(id.getPath(),
						texture(id),
						texture("light_aspect_planks")));
		provider.simpleHorizontal(LIGHT_ASPECT_LADDER, provider::ladder);
		provider.flatItem(LIGHT_ASPECT_LADDER_ITEM, MSBlockStateProvider::texture);
		
		
		provider.axisWithItem(MIND_ASPECT_LOG,
				id -> provider.models().cubeColumn(id.getPath(),
						texture(id),
						texture(id.withSuffix("_top"))));
		provider.simpleBlockWithItem(MIND_ASPECT_LEAVES);
		provider.simpleBlock(MIND_ASPECT_SAPLING,
				id -> provider.models().cross(id.getPath(), texture(id)).renderType("cutout"));
		provider.flatItem(MIND_ASPECT_SAPLING_ITEM, MSBlockStateProvider::texture);
		provider.simpleBlockWithItem(MIND_ASPECT_PLANKS);
		provider.stairsWithItem(MIND_ASPECT_STAIRS, MIND_ASPECT_PLANKS);
		provider.slabWithItem(MIND_ASPECT_SLAB, MIND_ASPECT_PLANKS);
		provider.fenceWithItem(MIND_ASPECT_FENCE, MIND_ASPECT_PLANKS);
		provider.fenceGateWithItem(MIND_ASPECT_FENCE_GATE, MIND_ASPECT_PLANKS);
		provider.simpleDoorBlock(MIND_ASPECT_DOOR);
		provider.trapDoorWithItem(MIND_ASPECT_TRAPDOOR);
		provider.flatItem(MIND_ASPECT_DOOR_ITEM, MSBlockStateProvider::itemTexture);
		provider.pressurePlateWithItem(MIND_ASPECT_PRESSURE_PLATE, MIND_ASPECT_PLANKS);
		provider.buttonWithItem(MIND_ASPECT_BUTTON, MIND_ASPECT_PLANKS);
		provider.simpleBlockWithItem(MIND_ASPECT_BOOKSHELF,
				id -> provider.models().cubeColumn(id.getPath(),
						texture(id),
						texture("mind_aspect_planks")));
		provider.simpleHorizontal(MIND_ASPECT_LADDER, provider::ladder);
		provider.flatItem(MIND_ASPECT_LADDER_ITEM, MSBlockStateProvider::texture);
		
		
		provider.axisWithItem(RAGE_ASPECT_LOG,
				id -> provider.models().cubeColumn(id.getPath(),
						texture(id),
						texture(id.withSuffix("_top"))));
		provider.simpleBlockWithItem(RAGE_ASPECT_LEAVES);
		provider.simpleBlock(RAGE_ASPECT_SAPLING,
				id -> provider.models().cross(id.getPath(), texture(id)).renderType("cutout"));
		provider.flatItem(RAGE_ASPECT_SAPLING_ITEM, MSBlockStateProvider::texture);
		provider.simpleBlockWithItem(RAGE_ASPECT_PLANKS);
		provider.stairsWithItem(RAGE_ASPECT_STAIRS, RAGE_ASPECT_PLANKS);
		provider.slabWithItem(RAGE_ASPECT_SLAB, RAGE_ASPECT_PLANKS);
		provider.fenceWithItem(RAGE_ASPECT_FENCE, RAGE_ASPECT_PLANKS);
		provider.fenceGateWithItem(RAGE_ASPECT_FENCE_GATE, RAGE_ASPECT_PLANKS);
		provider.simpleDoorBlock(RAGE_ASPECT_DOOR);
		provider.trapDoorWithItem(RAGE_ASPECT_TRAPDOOR);
		provider.flatItem(RAGE_ASPECT_DOOR_ITEM, MSBlockStateProvider::itemTexture);
		provider.pressurePlateWithItem(RAGE_ASPECT_PRESSURE_PLATE, RAGE_ASPECT_PLANKS);
		provider.buttonWithItem(RAGE_ASPECT_BUTTON, RAGE_ASPECT_PLANKS);
		provider.simpleBlockWithItem(RAGE_ASPECT_BOOKSHELF,
				id -> provider.models().cubeColumn(id.getPath(),
						texture(id),
						texture("rage_aspect_planks")));
		provider.simpleHorizontal(RAGE_ASPECT_LADDER, provider::ladder);
		provider.flatItem(RAGE_ASPECT_LADDER_ITEM, MSBlockStateProvider::texture);
		
		
		provider.axisWithItem(SPACE_ASPECT_LOG,
				id -> provider.models().cubeColumn(id.getPath(),
						texture(id),
						texture(id.withSuffix("_top"))));
		provider.simpleBlockWithItem(SPACE_ASPECT_LEAVES);
		provider.simpleBlock(SPACE_ASPECT_SAPLING,
				id -> provider.models().cross(id.getPath(), texture(id)).renderType("cutout"));
		provider.flatItem(SPACE_ASPECT_SAPLING_ITEM, MSBlockStateProvider::texture);
		provider.simpleBlockWithItem(SPACE_ASPECT_PLANKS);
		provider.stairsWithItem(SPACE_ASPECT_STAIRS, SPACE_ASPECT_PLANKS);
		provider.slabWithItem(SPACE_ASPECT_SLAB, SPACE_ASPECT_PLANKS);
		provider.fenceWithItem(SPACE_ASPECT_FENCE, SPACE_ASPECT_PLANKS);
		provider.fenceGateWithItem(SPACE_ASPECT_FENCE_GATE, SPACE_ASPECT_PLANKS);
		provider.simpleDoorBlock(SPACE_ASPECT_DOOR);
		provider.trapDoorWithItem(SPACE_ASPECT_TRAPDOOR);
		provider.flatItem(SPACE_ASPECT_DOOR_ITEM, MSBlockStateProvider::itemTexture);
		provider.pressurePlateWithItem(SPACE_ASPECT_PRESSURE_PLATE, SPACE_ASPECT_PLANKS);
		provider.buttonWithItem(SPACE_ASPECT_BUTTON, SPACE_ASPECT_PLANKS);
		provider.simpleBlockWithItem(SPACE_ASPECT_BOOKSHELF,
				id -> provider.models().cubeColumn(id.getPath(),
						texture(id),
						texture("space_aspect_planks")));
		provider.simpleHorizontal(SPACE_ASPECT_LADDER, provider::ladder);
		provider.flatItem(SPACE_ASPECT_LADDER_ITEM, MSBlockStateProvider::texture);
		
		
		provider.axisWithItem(TIME_ASPECT_LOG,
				id -> provider.models().cubeColumn(id.getPath(),
						texture(id),
						texture(id.withSuffix("_top"))));
		provider.simpleBlockWithItem(TIME_ASPECT_LEAVES);
		provider.simpleBlock(TIME_ASPECT_SAPLING,
				id -> provider.models().cross(id.getPath(), texture(id)).renderType("cutout"));
		provider.flatItem(TIME_ASPECT_SAPLING_ITEM, MSBlockStateProvider::texture);
		provider.simpleBlockWithItem(TIME_ASPECT_PLANKS);
		provider.stairsWithItem(TIME_ASPECT_STAIRS, TIME_ASPECT_PLANKS);
		provider.slabWithItem(TIME_ASPECT_SLAB, TIME_ASPECT_PLANKS);
		provider.fenceWithItem(TIME_ASPECT_FENCE, TIME_ASPECT_PLANKS);
		provider.fenceGateWithItem(TIME_ASPECT_FENCE_GATE, TIME_ASPECT_PLANKS);
		provider.simpleDoorBlock(TIME_ASPECT_DOOR);
		provider.trapDoorWithItem(TIME_ASPECT_TRAPDOOR);
		provider.flatItem(TIME_ASPECT_DOOR_ITEM, MSBlockStateProvider::itemTexture);
		provider.pressurePlateWithItem(TIME_ASPECT_PRESSURE_PLATE, TIME_ASPECT_PLANKS);
		provider.buttonWithItem(TIME_ASPECT_BUTTON, TIME_ASPECT_PLANKS);
		provider.simpleBlockWithItem(TIME_ASPECT_BOOKSHELF,
				id -> provider.models().cubeColumn(id.getPath(),
						texture(id),
						texture("time_aspect_planks")));
		provider.simpleHorizontal(TIME_ASPECT_LADDER, provider::ladder);
		provider.flatItem(TIME_ASPECT_LADDER_ITEM, MSBlockStateProvider::texture);
		
		
		provider.axisWithItem(VOID_ASPECT_LOG,
				id -> provider.models().cubeColumn(id.getPath(),
						texture(id),
						texture(id.withSuffix("_top"))));
		provider.simpleBlockWithItem(VOID_ASPECT_LEAVES);
		provider.simpleBlock(VOID_ASPECT_SAPLING,
				id -> provider.models().cross(id.getPath(), texture(id)).renderType("cutout"));
		provider.flatItem(VOID_ASPECT_SAPLING_ITEM, MSBlockStateProvider::texture);
		provider.simpleBlockWithItem(VOID_ASPECT_PLANKS);
		provider.stairsWithItem(VOID_ASPECT_STAIRS, VOID_ASPECT_PLANKS);
		provider.slabWithItem(VOID_ASPECT_SLAB, VOID_ASPECT_PLANKS);
		provider.fenceWithItem(VOID_ASPECT_FENCE, VOID_ASPECT_PLANKS);
		provider.fenceGateWithItem(VOID_ASPECT_FENCE_GATE, VOID_ASPECT_PLANKS);
		provider.simpleDoorBlock(VOID_ASPECT_DOOR);
		provider.trapDoorWithItem(VOID_ASPECT_TRAPDOOR);
		provider.flatItem(VOID_ASPECT_DOOR_ITEM, MSBlockStateProvider::itemTexture);
		provider.pressurePlateWithItem(VOID_ASPECT_PRESSURE_PLATE, VOID_ASPECT_PLANKS);
		provider.buttonWithItem(VOID_ASPECT_BUTTON, VOID_ASPECT_PLANKS);
		provider.simpleBlockWithItem(VOID_ASPECT_BOOKSHELF,
				id -> provider.models().cubeColumn(id.getPath(),
						texture(id),
						texture("void_aspect_planks")));
		provider.simpleHorizontal(VOID_ASPECT_LADDER, provider::ladder);
		provider.flatItem(VOID_ASPECT_LADDER_ITEM, MSBlockStateProvider::texture);
	}
	
	public static void addLootTables(MSBlockLootTables provider)
	{
		provider.dropSelf(BLOOD_ASPECT_LOG.get());
		provider.add(BLOOD_ASPECT_LEAVES.get(), block ->
				provider.createLeavesDrops(block, BLOOD_ASPECT_SAPLING.get(), MSBlockLootTables.SAPLING_CHANCES));
		provider.dropSelf(BLOOD_ASPECT_SAPLING.get());
		provider.dropSelf(BLOOD_ASPECT_PLANKS.get());
		provider.dropSelf(BLOOD_ASPECT_STAIRS.get());
		provider.add(BLOOD_ASPECT_SLAB.get(), provider::createSlabItemTable);
		provider.dropSelf(BLOOD_ASPECT_FENCE.get());
		provider.dropSelf(BLOOD_ASPECT_FENCE_GATE.get());
		provider.add(BLOOD_ASPECT_DOOR.get(), provider::createDoorTable);
		provider.dropSelf(BLOOD_ASPECT_TRAPDOOR.get());
		provider.dropSelf(BLOOD_ASPECT_PRESSURE_PLATE.get());
		provider.dropSelf(BLOOD_ASPECT_BUTTON.get());
		provider.add(BLOOD_ASPECT_BOOKSHELF.get(), provider::bookshelfDrop);
		provider.dropSelf(BLOOD_ASPECT_LADDER.get());
		
		provider.dropSelf(BREATH_ASPECT_LOG.get());
		provider.add(BREATH_ASPECT_LEAVES.get(), block ->
				provider.createLeavesDrops(block, BREATH_ASPECT_SAPLING.get(), MSBlockLootTables.SAPLING_CHANCES));
		provider.dropSelf(BREATH_ASPECT_SAPLING.get());
		provider.dropSelf(BREATH_ASPECT_PLANKS.get());
		provider.dropSelf(BREATH_ASPECT_STAIRS.get());
		provider.add(BREATH_ASPECT_SLAB.get(), provider::createSlabItemTable);
		provider.dropSelf(BREATH_ASPECT_FENCE.get());
		provider.dropSelf(BREATH_ASPECT_FENCE_GATE.get());
		provider.add(BREATH_ASPECT_DOOR.get(), provider::createDoorTable);
		provider.dropSelf(BREATH_ASPECT_TRAPDOOR.get());
		provider.dropSelf(BREATH_ASPECT_PRESSURE_PLATE.get());
		provider.dropSelf(BREATH_ASPECT_BUTTON.get());
		provider.add(BREATH_ASPECT_BOOKSHELF.get(), provider::bookshelfDrop);
		provider.dropSelf(BREATH_ASPECT_LADDER.get());
		
		provider.dropSelf(DOOM_ASPECT_LOG.get());
		provider.add(DOOM_ASPECT_LEAVES.get(), block ->
				provider.createLeavesDrops(block, DOOM_ASPECT_SAPLING.get(), MSBlockLootTables.SAPLING_CHANCES));
		provider.dropSelf(DOOM_ASPECT_SAPLING.get());
		provider.dropSelf(DOOM_ASPECT_PLANKS.get());
		provider.dropSelf(DOOM_ASPECT_STAIRS.get());
		provider.add(DOOM_ASPECT_SLAB.get(), provider::createSlabItemTable);
		provider.dropSelf(DOOM_ASPECT_FENCE.get());
		provider.dropSelf(DOOM_ASPECT_FENCE_GATE.get());
		provider.add(DOOM_ASPECT_DOOR.get(), provider::createDoorTable);
		provider.dropSelf(DOOM_ASPECT_TRAPDOOR.get());
		provider.dropSelf(DOOM_ASPECT_PRESSURE_PLATE.get());
		provider.dropSelf(DOOM_ASPECT_BUTTON.get());
		provider.add(DOOM_ASPECT_BOOKSHELF.get(), provider::bookshelfDrop);
		provider.dropSelf(DOOM_ASPECT_LADDER.get());
		
		provider.dropSelf(HEART_ASPECT_LOG.get());
		provider.add(HEART_ASPECT_LEAVES.get(), block ->
				provider.createLeavesDrops(block, HEART_ASPECT_SAPLING.get(), MSBlockLootTables.SAPLING_CHANCES));
		provider.dropSelf(HEART_ASPECT_SAPLING.get());
		provider.dropSelf(HEART_ASPECT_PLANKS.get());
		provider.dropSelf(HEART_ASPECT_STAIRS.get());
		provider.add(HEART_ASPECT_SLAB.get(), provider::createSlabItemTable);
		provider.dropSelf(HEART_ASPECT_FENCE.get());
		provider.dropSelf(HEART_ASPECT_FENCE_GATE.get());
		provider.add(HEART_ASPECT_DOOR.get(), provider::createDoorTable);
		provider.dropSelf(HEART_ASPECT_TRAPDOOR.get());
		provider.dropSelf(HEART_ASPECT_PRESSURE_PLATE.get());
		provider.dropSelf(HEART_ASPECT_BUTTON.get());
		provider.add(HEART_ASPECT_BOOKSHELF.get(), provider::bookshelfDrop);
		provider.dropSelf(HEART_ASPECT_LADDER.get());
		
		provider.dropSelf(HOPE_ASPECT_LOG.get());
		provider.add(HOPE_ASPECT_LEAVES.get(), block ->
				provider.createLeavesDrops(block, HOPE_ASPECT_SAPLING.get(), MSBlockLootTables.SAPLING_CHANCES));
		provider.dropSelf(HOPE_ASPECT_SAPLING.get());
		provider.dropSelf(HOPE_ASPECT_PLANKS.get());
		provider.dropSelf(HOPE_ASPECT_STAIRS.get());
		provider.add(HOPE_ASPECT_SLAB.get(), provider::createSlabItemTable);
		provider.dropSelf(HOPE_ASPECT_FENCE.get());
		provider.dropSelf(HOPE_ASPECT_FENCE_GATE.get());
		provider.add(HOPE_ASPECT_DOOR.get(), provider::createDoorTable);
		provider.dropSelf(HOPE_ASPECT_TRAPDOOR.get());
		provider.dropSelf(HOPE_ASPECT_PRESSURE_PLATE.get());
		provider.dropSelf(HOPE_ASPECT_BUTTON.get());
		provider.add(HOPE_ASPECT_BOOKSHELF.get(), provider::bookshelfDrop);
		provider.dropSelf(HOPE_ASPECT_LADDER.get());
		
		provider.dropSelf(LIFE_ASPECT_LOG.get());
		provider.add(LIFE_ASPECT_LEAVES.get(), block ->
				provider.createLeavesDrops(block, LIFE_ASPECT_SAPLING.get(), MSBlockLootTables.SAPLING_CHANCES));
		provider.dropSelf(LIFE_ASPECT_SAPLING.get());
		provider.dropSelf(LIFE_ASPECT_PLANKS.get());
		provider.dropSelf(LIFE_ASPECT_STAIRS.get());
		provider.add(LIFE_ASPECT_SLAB.get(), provider::createSlabItemTable);
		provider.dropSelf(LIFE_ASPECT_FENCE.get());
		provider.dropSelf(LIFE_ASPECT_FENCE_GATE.get());
		provider.add(LIFE_ASPECT_DOOR.get(), provider::createDoorTable);
		provider.dropSelf(LIFE_ASPECT_TRAPDOOR.get());
		provider.dropSelf(LIFE_ASPECT_PRESSURE_PLATE.get());
		provider.dropSelf(LIFE_ASPECT_BUTTON.get());
		provider.add(LIFE_ASPECT_BOOKSHELF.get(), provider::bookshelfDrop);
		provider.dropSelf(LIFE_ASPECT_LADDER.get());
		
		provider.dropSelf(LIGHT_ASPECT_LOG.get());
		provider.add(LIGHT_ASPECT_LEAVES.get(), block ->
				provider.createLeavesDrops(block, LIGHT_ASPECT_SAPLING.get(), MSBlockLootTables.SAPLING_CHANCES));
		provider.dropSelf(LIGHT_ASPECT_SAPLING.get());
		provider.dropSelf(LIGHT_ASPECT_PLANKS.get());
		provider.dropSelf(LIGHT_ASPECT_STAIRS.get());
		provider.add(LIGHT_ASPECT_SLAB.get(), provider::createSlabItemTable);
		provider.dropSelf(LIGHT_ASPECT_FENCE.get());
		provider.dropSelf(LIGHT_ASPECT_FENCE_GATE.get());
		provider.add(LIGHT_ASPECT_DOOR.get(), provider::createDoorTable);
		provider.dropSelf(LIGHT_ASPECT_TRAPDOOR.get());
		provider.dropSelf(LIGHT_ASPECT_PRESSURE_PLATE.get());
		provider.dropSelf(LIGHT_ASPECT_BUTTON.get());
		provider.add(LIGHT_ASPECT_BOOKSHELF.get(), provider::bookshelfDrop);
		provider.dropSelf(LIGHT_ASPECT_LADDER.get());
		
		provider.dropSelf(MIND_ASPECT_LOG.get());
		provider.add(MIND_ASPECT_LEAVES.get(), block ->
				provider.createLeavesDrops(block, MIND_ASPECT_SAPLING.get(), MSBlockLootTables.SAPLING_CHANCES));
		provider.dropSelf(MIND_ASPECT_SAPLING.get());
		provider.dropSelf(MIND_ASPECT_PLANKS.get());
		provider.dropSelf(MIND_ASPECT_STAIRS.get());
		provider.add(MIND_ASPECT_SLAB.get(), provider::createSlabItemTable);
		provider.dropSelf(MIND_ASPECT_FENCE.get());
		provider.dropSelf(MIND_ASPECT_FENCE_GATE.get());
		provider.add(MIND_ASPECT_DOOR.get(), provider::createDoorTable);
		provider.dropSelf(MIND_ASPECT_TRAPDOOR.get());
		provider.dropSelf(MIND_ASPECT_PRESSURE_PLATE.get());
		provider.dropSelf(MIND_ASPECT_BUTTON.get());
		provider.add(MIND_ASPECT_BOOKSHELF.get(), provider::bookshelfDrop);
		provider.dropSelf(MIND_ASPECT_LADDER.get());
		
		provider.dropSelf(RAGE_ASPECT_LOG.get());
		provider.add(RAGE_ASPECT_LEAVES.get(), block ->
				provider.createLeavesDrops(block, RAGE_ASPECT_SAPLING.get(), MSBlockLootTables.SAPLING_CHANCES));
		provider.dropSelf(RAGE_ASPECT_SAPLING.get());
		provider.dropSelf(RAGE_ASPECT_PLANKS.get());
		provider.dropSelf(RAGE_ASPECT_STAIRS.get());
		provider.add(RAGE_ASPECT_SLAB.get(), provider::createSlabItemTable);
		provider.dropSelf(RAGE_ASPECT_FENCE.get());
		provider.dropSelf(RAGE_ASPECT_FENCE_GATE.get());
		provider.add(RAGE_ASPECT_DOOR.get(), provider::createDoorTable);
		provider.dropSelf(RAGE_ASPECT_TRAPDOOR.get());
		provider.dropSelf(RAGE_ASPECT_PRESSURE_PLATE.get());
		provider.dropSelf(RAGE_ASPECT_BUTTON.get());
		provider.add(RAGE_ASPECT_BOOKSHELF.get(), provider::bookshelfDrop);
		provider.dropSelf(RAGE_ASPECT_LADDER.get());
		
		provider.dropSelf(SPACE_ASPECT_LOG.get());
		provider.add(SPACE_ASPECT_LEAVES.get(), block ->
				provider.createLeavesDrops(block, SPACE_ASPECT_SAPLING.get(), MSBlockLootTables.SAPLING_CHANCES));
		provider.dropSelf(SPACE_ASPECT_SAPLING.get());
		provider.dropSelf(SPACE_ASPECT_PLANKS.get());
		provider.dropSelf(SPACE_ASPECT_STAIRS.get());
		provider.add(SPACE_ASPECT_SLAB.get(), provider::createSlabItemTable);
		provider.dropSelf(SPACE_ASPECT_FENCE.get());
		provider.dropSelf(SPACE_ASPECT_FENCE_GATE.get());
		provider.add(SPACE_ASPECT_DOOR.get(), provider::createDoorTable);
		provider.dropSelf(SPACE_ASPECT_TRAPDOOR.get());
		provider.dropSelf(SPACE_ASPECT_PRESSURE_PLATE.get());
		provider.dropSelf(SPACE_ASPECT_BUTTON.get());
		provider.add(SPACE_ASPECT_BOOKSHELF.get(), provider::bookshelfDrop);
		provider.dropSelf(SPACE_ASPECT_LADDER.get());
		
		provider.dropSelf(TIME_ASPECT_LOG.get());
		provider.add(TIME_ASPECT_LEAVES.get(), block ->
				provider.createLeavesDrops(block, TIME_ASPECT_SAPLING.get(), MSBlockLootTables.SAPLING_CHANCES));
		provider.dropSelf(TIME_ASPECT_SAPLING.get());
		provider.dropSelf(TIME_ASPECT_PLANKS.get());
		provider.dropSelf(TIME_ASPECT_STAIRS.get());
		provider.add(TIME_ASPECT_SLAB.get(), provider::createSlabItemTable);
		provider.dropSelf(TIME_ASPECT_FENCE.get());
		provider.dropSelf(TIME_ASPECT_FENCE_GATE.get());
		provider.add(TIME_ASPECT_DOOR.get(), provider::createDoorTable);
		provider.dropSelf(TIME_ASPECT_TRAPDOOR.get());
		provider.dropSelf(TIME_ASPECT_PRESSURE_PLATE.get());
		provider.dropSelf(TIME_ASPECT_BUTTON.get());
		provider.add(TIME_ASPECT_BOOKSHELF.get(), provider::bookshelfDrop);
		provider.dropSelf(TIME_ASPECT_LADDER.get());
		
		provider.dropSelf(VOID_ASPECT_LOG.get());
		provider.add(VOID_ASPECT_LEAVES.get(), block ->
				provider.createLeavesDrops(block, VOID_ASPECT_SAPLING.get(), MSBlockLootTables.SAPLING_CHANCES));
		provider.dropSelf(VOID_ASPECT_SAPLING.get());
		provider.dropSelf(VOID_ASPECT_PLANKS.get());
		provider.dropSelf(VOID_ASPECT_STAIRS.get());
		provider.add(VOID_ASPECT_SLAB.get(), provider::createSlabItemTable);
		provider.dropSelf(VOID_ASPECT_FENCE.get());
		provider.dropSelf(VOID_ASPECT_FENCE_GATE.get());
		provider.add(VOID_ASPECT_DOOR.get(), provider::createDoorTable);
		provider.dropSelf(VOID_ASPECT_TRAPDOOR.get());
		provider.dropSelf(VOID_ASPECT_PRESSURE_PLATE.get());
		provider.dropSelf(VOID_ASPECT_BUTTON.get());
		provider.add(VOID_ASPECT_BOOKSHELF.get(), provider::bookshelfDrop);
		provider.dropSelf(VOID_ASPECT_LADDER.get());
	}
	
	public static void addToBlockTags(MinestuckBlockTagsProvider provider)
	{
		provider.tag(BlockTags.MINEABLE_WITH_AXE).add(BLOOD_ASPECT_LOG.get(), BLOOD_ASPECT_PLANKS.get(),
				BLOOD_ASPECT_STAIRS.get(), BLOOD_ASPECT_SLAB.get(),
				BLOOD_ASPECT_FENCE.get(), BLOOD_ASPECT_FENCE_GATE.get(),
				BLOOD_ASPECT_PRESSURE_PLATE.get(), BLOOD_ASPECT_BUTTON.get(),
				BLOOD_ASPECT_BOOKSHELF.get(), BLOOD_ASPECT_LADDER.get());
		provider.tag(BlockTags.MINEABLE_WITH_AXE).add(BREATH_ASPECT_LOG.get(), BREATH_ASPECT_PLANKS.get(),
				BREATH_ASPECT_STAIRS.get(), BREATH_ASPECT_SLAB.get(),
				BREATH_ASPECT_FENCE.get(), BREATH_ASPECT_FENCE_GATE.get(),
				BREATH_ASPECT_PRESSURE_PLATE.get(), BREATH_ASPECT_BUTTON.get(),
				BREATH_ASPECT_BOOKSHELF.get(), BREATH_ASPECT_LADDER.get());
		provider.tag(BlockTags.MINEABLE_WITH_AXE).add(DOOM_ASPECT_LOG.get(), DOOM_ASPECT_PLANKS.get(),
				DOOM_ASPECT_STAIRS.get(), DOOM_ASPECT_SLAB.get(),
				DOOM_ASPECT_FENCE.get(), DOOM_ASPECT_FENCE_GATE.get(),
				DOOM_ASPECT_PRESSURE_PLATE.get(), DOOM_ASPECT_BUTTON.get(),
				DOOM_ASPECT_BOOKSHELF.get(), DOOM_ASPECT_LADDER.get());
		provider.tag(BlockTags.MINEABLE_WITH_AXE).add(HEART_ASPECT_LOG.get(), HEART_ASPECT_PLANKS.get(),
				HEART_ASPECT_STAIRS.get(), HEART_ASPECT_SLAB.get(),
				HEART_ASPECT_FENCE.get(), HEART_ASPECT_FENCE_GATE.get(),
				HEART_ASPECT_PRESSURE_PLATE.get(), HEART_ASPECT_BUTTON.get(),
				HEART_ASPECT_BOOKSHELF.get(), HEART_ASPECT_LADDER.get());
		provider.tag(BlockTags.MINEABLE_WITH_AXE).add(HOPE_ASPECT_LOG.get(), HOPE_ASPECT_PLANKS.get(),
				HOPE_ASPECT_STAIRS.get(), HOPE_ASPECT_SLAB.get(),
				HOPE_ASPECT_FENCE.get(), HOPE_ASPECT_FENCE_GATE.get(),
				HOPE_ASPECT_PRESSURE_PLATE.get(), HOPE_ASPECT_BUTTON.get(),
				HOPE_ASPECT_BOOKSHELF.get(), HOPE_ASPECT_LADDER.get());
		provider.tag(BlockTags.MINEABLE_WITH_AXE).add(LIFE_ASPECT_LOG.get(), LIFE_ASPECT_PLANKS.get(),
				LIFE_ASPECT_STAIRS.get(), LIFE_ASPECT_SLAB.get(),
				LIFE_ASPECT_FENCE.get(), LIFE_ASPECT_FENCE_GATE.get(),
				LIFE_ASPECT_PRESSURE_PLATE.get(), LIFE_ASPECT_BUTTON.get(),
				LIFE_ASPECT_BOOKSHELF.get(), LIFE_ASPECT_LADDER.get());
		provider.tag(BlockTags.MINEABLE_WITH_AXE).add(LIGHT_ASPECT_LOG.get(), LIGHT_ASPECT_PLANKS.get(),
				LIGHT_ASPECT_STAIRS.get(), LIGHT_ASPECT_SLAB.get(),
				LIGHT_ASPECT_FENCE.get(), LIGHT_ASPECT_FENCE_GATE.get(),
				LIGHT_ASPECT_PRESSURE_PLATE.get(), LIGHT_ASPECT_BUTTON.get(),
				LIGHT_ASPECT_BOOKSHELF.get(), LIGHT_ASPECT_LADDER.get());
		provider.tag(BlockTags.MINEABLE_WITH_AXE).add(MIND_ASPECT_LOG.get(), MIND_ASPECT_PLANKS.get(),
				MIND_ASPECT_STAIRS.get(), MIND_ASPECT_SLAB.get(),
				MIND_ASPECT_FENCE.get(), MIND_ASPECT_FENCE_GATE.get(),
				MIND_ASPECT_PRESSURE_PLATE.get(), MIND_ASPECT_BUTTON.get(),
				MIND_ASPECT_BOOKSHELF.get(), MIND_ASPECT_LADDER.get());
		provider.tag(BlockTags.MINEABLE_WITH_AXE).add(RAGE_ASPECT_LOG.get(), RAGE_ASPECT_PLANKS.get(),
				RAGE_ASPECT_STAIRS.get(), RAGE_ASPECT_SLAB.get(),
				RAGE_ASPECT_FENCE.get(), RAGE_ASPECT_FENCE_GATE.get(),
				RAGE_ASPECT_PRESSURE_PLATE.get(), RAGE_ASPECT_BUTTON.get(),
				RAGE_ASPECT_BOOKSHELF.get(), RAGE_ASPECT_LADDER.get());
		provider.tag(BlockTags.MINEABLE_WITH_AXE).add(SPACE_ASPECT_LOG.get(), SPACE_ASPECT_PLANKS.get(),
				SPACE_ASPECT_STAIRS.get(), SPACE_ASPECT_SLAB.get(),
				SPACE_ASPECT_FENCE.get(), SPACE_ASPECT_FENCE_GATE.get(),
				SPACE_ASPECT_PRESSURE_PLATE.get(), SPACE_ASPECT_BUTTON.get(),
				SPACE_ASPECT_BOOKSHELF.get(), SPACE_ASPECT_LADDER.get());
		provider.tag(BlockTags.MINEABLE_WITH_AXE).add(TIME_ASPECT_LOG.get(), TIME_ASPECT_PLANKS.get(),
				TIME_ASPECT_STAIRS.get(), TIME_ASPECT_SLAB.get(),
				TIME_ASPECT_FENCE.get(), TIME_ASPECT_FENCE_GATE.get(),
				TIME_ASPECT_PRESSURE_PLATE.get(), TIME_ASPECT_BUTTON.get(),
				TIME_ASPECT_BOOKSHELF.get(), TIME_ASPECT_LADDER.get());
		provider.tag(BlockTags.MINEABLE_WITH_AXE).add(VOID_ASPECT_LOG.get(), VOID_ASPECT_PLANKS.get(),
				VOID_ASPECT_STAIRS.get(), VOID_ASPECT_SLAB.get(),
				VOID_ASPECT_FENCE.get(), VOID_ASPECT_FENCE_GATE.get(),
				VOID_ASPECT_PRESSURE_PLATE.get(), VOID_ASPECT_BUTTON.get(),
				VOID_ASPECT_BOOKSHELF.get(), VOID_ASPECT_LADDER.get());
		
		provider.tag(MSTags.Blocks.ASPECT_LOGS).add(BLOOD_ASPECT_LOG.get(), BREATH_ASPECT_LOG.get(), DOOM_ASPECT_LOG.get(), HEART_ASPECT_LOG.get(),
				HOPE_ASPECT_LOG.get(), LIFE_ASPECT_LOG.get(), LIGHT_ASPECT_LOG.get(), MIND_ASPECT_LOG.get(),
				RAGE_ASPECT_LOG.get(), SPACE_ASPECT_LOG.get(), TIME_ASPECT_LOG.get(), VOID_ASPECT_LOG.get());
		provider.tag(MSTags.Blocks.ASPECT_PLANKS).add(BLOOD_ASPECT_PLANKS.get(), BREATH_ASPECT_PLANKS.get(), DOOM_ASPECT_PLANKS.get(), HEART_ASPECT_PLANKS.get(),
				HOPE_ASPECT_PLANKS.get(), LIFE_ASPECT_PLANKS.get(), LIGHT_ASPECT_PLANKS.get(), MIND_ASPECT_PLANKS.get(),
				RAGE_ASPECT_PLANKS.get(), SPACE_ASPECT_PLANKS.get(), TIME_ASPECT_PLANKS.get(), VOID_ASPECT_PLANKS.get());
		provider.tag(MSTags.Blocks.ASPECT_LEAVES).add(BLOOD_ASPECT_LEAVES.get(), BREATH_ASPECT_LEAVES.get(), DOOM_ASPECT_LEAVES.get(), HEART_ASPECT_LEAVES.get(),
				HOPE_ASPECT_LEAVES.get(), LIFE_ASPECT_LEAVES.get(), LIGHT_ASPECT_LEAVES.get(), MIND_ASPECT_LEAVES.get(),
				RAGE_ASPECT_LEAVES.get(), SPACE_ASPECT_LEAVES.get(), TIME_ASPECT_LEAVES.get(), VOID_ASPECT_LEAVES.get());
		provider.tag(MSTags.Blocks.ASPECT_SAPLINGS).add(BLOOD_ASPECT_SAPLING.get(), BREATH_ASPECT_SAPLING.get(), DOOM_ASPECT_SAPLING.get(), HEART_ASPECT_SAPLING.get(),
				HOPE_ASPECT_SAPLING.get(), LIFE_ASPECT_SAPLING.get(), LIGHT_ASPECT_SAPLING.get(), MIND_ASPECT_SAPLING.get(),
				RAGE_ASPECT_SAPLING.get(), SPACE_ASPECT_SAPLING.get(), TIME_ASPECT_SAPLING.get(), VOID_ASPECT_SAPLING.get());
		provider.tag(MSTags.Blocks.ASPECT_BOOKSHELVES).add(BLOOD_ASPECT_BOOKSHELF.get(), BREATH_ASPECT_BOOKSHELF.get(), DOOM_ASPECT_BOOKSHELF.get(), HEART_ASPECT_BOOKSHELF.get(),
				HOPE_ASPECT_BOOKSHELF.get(), LIFE_ASPECT_BOOKSHELF.get(), LIGHT_ASPECT_BOOKSHELF.get(), MIND_ASPECT_BOOKSHELF.get(),
				RAGE_ASPECT_BOOKSHELF.get(), SPACE_ASPECT_BOOKSHELF.get(), TIME_ASPECT_BOOKSHELF.get(), VOID_ASPECT_BOOKSHELF.get());
		provider.tag(MSTags.Blocks.ASPECT_LADDERS).add(BLOOD_ASPECT_LADDER.get(), BREATH_ASPECT_LADDER.get(), DOOM_ASPECT_LADDER.get(), HEART_ASPECT_LADDER.get(),
				HOPE_ASPECT_LADDER.get(), LIFE_ASPECT_LADDER.get(), LIGHT_ASPECT_LADDER.get(), MIND_ASPECT_LADDER.get(),
				RAGE_ASPECT_LADDER.get(), SPACE_ASPECT_LADDER.get(), TIME_ASPECT_LADDER.get(), VOID_ASPECT_LADDER.get());
		
		provider.tag(BlockTags.LOGS).addTag(MSTags.Blocks.ASPECT_LOGS);
		provider.tag(BlockTags.LEAVES).addTag(MSTags.Blocks.ASPECT_LEAVES);
		provider.tag(BlockTags.SAPLINGS).addTag(MSTags.Blocks.ASPECT_SAPLINGS);
		provider.tag(BlockTags.PLANKS).addTag(MSTags.Blocks.ASPECT_PLANKS);
		provider.tag(BlockTags.FENCES).add(BLOOD_ASPECT_FENCE.get(), BREATH_ASPECT_FENCE.get(), DOOM_ASPECT_FENCE.get(), HEART_ASPECT_FENCE.get(),
				HOPE_ASPECT_FENCE.get(), LIFE_ASPECT_FENCE.get(), LIGHT_ASPECT_FENCE.get(), MIND_ASPECT_FENCE.get(),
				RAGE_ASPECT_FENCE.get(), SPACE_ASPECT_FENCE.get(), TIME_ASPECT_FENCE.get(), VOID_ASPECT_FENCE.get());
		provider.tag(BlockTags.FENCE_GATES).add(BLOOD_ASPECT_FENCE_GATE.get(), BREATH_ASPECT_FENCE_GATE.get(), DOOM_ASPECT_FENCE_GATE.get(), HEART_ASPECT_FENCE_GATE.get(),
				HOPE_ASPECT_FENCE_GATE.get(), LIFE_ASPECT_FENCE_GATE.get(), LIGHT_ASPECT_FENCE_GATE.get(), MIND_ASPECT_FENCE_GATE.get(),
				RAGE_ASPECT_FENCE_GATE.get(), SPACE_ASPECT_FENCE_GATE.get(), TIME_ASPECT_FENCE_GATE.get(), VOID_ASPECT_FENCE_GATE.get());
		provider.tag(BlockTags.PRESSURE_PLATES).add(BLOOD_ASPECT_PRESSURE_PLATE.get(), BREATH_ASPECT_PRESSURE_PLATE.get(), DOOM_ASPECT_PRESSURE_PLATE.get(), HEART_ASPECT_PRESSURE_PLATE.get(),
				HOPE_ASPECT_PRESSURE_PLATE.get(), LIFE_ASPECT_PRESSURE_PLATE.get(), LIGHT_ASPECT_PRESSURE_PLATE.get(), MIND_ASPECT_PRESSURE_PLATE.get(),
				RAGE_ASPECT_PRESSURE_PLATE.get(), SPACE_ASPECT_PRESSURE_PLATE.get(), TIME_ASPECT_PRESSURE_PLATE.get(), VOID_ASPECT_PRESSURE_PLATE.get());
		provider.tag(Tags.Blocks.BOOKSHELVES).addTag(MSTags.Blocks.ASPECT_BOOKSHELVES);
		provider.tag(BlockTags.CLIMBABLE).addTag(MSTags.Blocks.ASPECT_LADDERS);
	}
	
	public static void addRecipes(Consumer<FinishedRecipe> recipeSaver)
	{
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BLOOD_ASPECT_STAIRS.get(), 4).group("wooden_stairs")
				.define('#', BLOOD_ASPECT_PLANKS.get()).pattern("#  ").pattern("## ").pattern("###")
				.unlockedBy("has_blood_aspect_planks", has(BLOOD_ASPECT_PLANKS.get())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BLOOD_ASPECT_SLAB.get(), 6).group("wooden_slab")
				.define('#', BLOOD_ASPECT_PLANKS.get()).pattern("###")
				.unlockedBy("has_blood_aspect_planks", has(BLOOD_ASPECT_PLANKS.get())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BLOOD_ASPECT_FENCE.get(), 3).group("wooden_fence")
				.define('#', BLOOD_ASPECT_PLANKS.get()).define('$', Items.STICK).pattern("#$#").pattern("#$#")
				.unlockedBy("has_blood_aspect_planks", has(BLOOD_ASPECT_PLANKS.get())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BLOOD_ASPECT_FENCE_GATE.get(), 1).group("wooden_fence_gate")
				.define('#', BLOOD_ASPECT_PLANKS.get()).define('$', Items.STICK).pattern("$#$").pattern("$#$")
				.unlockedBy("has_blood_aspect_planks", has(BLOOD_ASPECT_PLANKS.get())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BLOOD_ASPECT_DOOR.get(), 3).group("wooden_door")
				.define('#', BLOOD_ASPECT_PLANKS.get()).pattern("## ").pattern("## ").pattern("## ")
				.unlockedBy("has_blood_aspect_planks", has(BLOOD_ASPECT_PLANKS.get())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BLOOD_ASPECT_TRAPDOOR.get(), 2).group("wooden_trapdoor")
				.define('#', BLOOD_ASPECT_PLANKS.get()).pattern("###").pattern("###")
				.unlockedBy("has_blood_aspect_planks", has(BLOOD_ASPECT_PLANKS.get())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BLOOD_ASPECT_PRESSURE_PLATE.get(), 1).group("stone_pressure_plate")
				.define('#', BLOOD_ASPECT_PLANKS.get()).pattern("##")
				.unlockedBy("has_blood_aspect_planks", has(BLOOD_ASPECT_PLANKS.get())).save(recipeSaver);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, BLOOD_ASPECT_BUTTON.get()).requires(BLOOD_ASPECT_PLANKS.get())
				.unlockedBy("has_blood_aspect_planks", has(BLOOD_ASPECT_PLANKS.get())).save(recipeSaver);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BREATH_ASPECT_STAIRS.get(), 4).group("wooden_stairs")
				.define('#', BREATH_ASPECT_PLANKS.get()).pattern("#  ").pattern("## ").pattern("###")
				.unlockedBy("has_breath_aspect_planks", has(BREATH_ASPECT_PLANKS.get())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BREATH_ASPECT_SLAB.get(), 6).group("wooden_slab")
				.define('#', BREATH_ASPECT_PLANKS.get()).pattern("###")
				.unlockedBy("has_breath_aspect_planks", has(BREATH_ASPECT_PLANKS.get())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BREATH_ASPECT_FENCE.get(), 3).group("wooden_fence")
				.define('#', BREATH_ASPECT_PLANKS.get()).define('$', Items.STICK).pattern("#$#").pattern("#$#")
				.unlockedBy("has_breath_aspect_planks", has(BREATH_ASPECT_PLANKS.get())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BREATH_ASPECT_FENCE_GATE.get(), 1).group("wooden_fence_gate")
				.define('#', BREATH_ASPECT_PLANKS.get()).define('$', Items.STICK).pattern("$#$").pattern("$#$")
				.unlockedBy("has_breath_aspect_planks", has(BREATH_ASPECT_PLANKS.get())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BREATH_ASPECT_DOOR.get(), 3).group("wooden_door")
				.define('#', BREATH_ASPECT_PLANKS.get()).pattern("## ").pattern("## ").pattern("## ")
				.unlockedBy("has_breath_aspect_planks", has(BREATH_ASPECT_PLANKS.get())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BREATH_ASPECT_TRAPDOOR.get(), 2).group("wooden_trapdoor")
				.define('#', BREATH_ASPECT_PLANKS.get()).pattern("###").pattern("###")
				.unlockedBy("has_breath_aspect_planks", has(BREATH_ASPECT_PLANKS.get())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BREATH_ASPECT_PRESSURE_PLATE.get(), 1).group("stone_pressure_plate")
				.define('#', BREATH_ASPECT_PLANKS.get()).pattern("##")
				.unlockedBy("has_breath_aspect_planks", has(BREATH_ASPECT_PLANKS.get())).save(recipeSaver);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, BREATH_ASPECT_BUTTON.get()).requires(BREATH_ASPECT_PLANKS.get())
				.unlockedBy("has_breath_aspect_planks", has(BREATH_ASPECT_PLANKS.get())).save(recipeSaver);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, DOOM_ASPECT_STAIRS.get(), 4).group("wooden_stairs")
				.define('#', DOOM_ASPECT_PLANKS.get()).pattern("#  ").pattern("## ").pattern("###")
				.unlockedBy("has_doom_aspect_planks", has(DOOM_ASPECT_PLANKS.get())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, DOOM_ASPECT_SLAB.get(), 6).group("wooden_slab")
				.define('#', DOOM_ASPECT_PLANKS.get()).pattern("###")
				.unlockedBy("has_doom_aspect_planks", has(DOOM_ASPECT_PLANKS.get())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, DOOM_ASPECT_FENCE.get(), 3).group("wooden_fence")
				.define('#', DOOM_ASPECT_PLANKS.get()).define('$', Items.STICK).pattern("#$#").pattern("#$#")
				.unlockedBy("has_doom_aspect_planks", has(DOOM_ASPECT_PLANKS.get())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, DOOM_ASPECT_FENCE_GATE.get(), 1).group("wooden_fence_gate")
				.define('#', DOOM_ASPECT_PLANKS.get()).define('$', Items.STICK).pattern("$#$").pattern("$#$")
				.unlockedBy("has_doom_aspect_planks", has(DOOM_ASPECT_PLANKS.get())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, DOOM_ASPECT_DOOR.get(), 3).group("wooden_door")
				.define('#', DOOM_ASPECT_PLANKS.get()).pattern("## ").pattern("## ").pattern("## ")
				.unlockedBy("has_doom_aspect_planks", has(DOOM_ASPECT_PLANKS.get())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, DOOM_ASPECT_TRAPDOOR.get(), 2).group("wooden_trapdoor")
				.define('#', DOOM_ASPECT_PLANKS.get()).pattern("###").pattern("###")
				.unlockedBy("has_doom_aspect_planks", has(DOOM_ASPECT_PLANKS.get())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, DOOM_ASPECT_PRESSURE_PLATE.get(), 1).group("stone_pressure_plate")
				.define('#', DOOM_ASPECT_PLANKS.get()).pattern("##")
				.unlockedBy("has_doom_aspect_planks", has(DOOM_ASPECT_PLANKS.get())).save(recipeSaver);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, DOOM_ASPECT_BUTTON.get()).requires(DOOM_ASPECT_PLANKS.get())
				.unlockedBy("has_doom_aspect_planks", has(DOOM_ASPECT_PLANKS.get())).save(recipeSaver);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HEART_ASPECT_STAIRS.get(), 4).group("wooden_stairs")
				.define('#', HEART_ASPECT_PLANKS.get()).pattern("#  ").pattern("## ").pattern("###")
				.unlockedBy("has_heart_aspect_planks", has(HEART_ASPECT_PLANKS.get())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HEART_ASPECT_SLAB.get(), 6).group("wooden_slab")
				.define('#', HEART_ASPECT_PLANKS.get()).pattern("###")
				.unlockedBy("has_heart_aspect_planks", has(HEART_ASPECT_PLANKS.get())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HEART_ASPECT_FENCE.get(), 3).group("wooden_fence")
				.define('#', HEART_ASPECT_PLANKS.get()).define('$', Items.STICK).pattern("#$#").pattern("#$#")
				.unlockedBy("has_heart_aspect_planks", has(HEART_ASPECT_PLANKS.get())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HEART_ASPECT_FENCE_GATE.get(), 1).group("wooden_fence_gate")
				.define('#', HEART_ASPECT_PLANKS.get()).define('$', Items.STICK).pattern("$#$").pattern("$#$")
				.unlockedBy("has_heart_aspect_planks", has(HEART_ASPECT_PLANKS.get())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HEART_ASPECT_DOOR.get(), 3).group("wooden_door")
				.define('#', HEART_ASPECT_PLANKS.get()).pattern("## ").pattern("## ").pattern("## ")
				.unlockedBy("has_heart_aspect_planks", has(HEART_ASPECT_PLANKS.get())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HEART_ASPECT_TRAPDOOR.get(), 2).group("wooden_trapdoor")
				.define('#', HEART_ASPECT_PLANKS.get()).pattern("###").pattern("###")
				.unlockedBy("has_heart_aspect_planks", has(HEART_ASPECT_PLANKS.get())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HEART_ASPECT_PRESSURE_PLATE.get(), 1).group("stone_pressure_plate")
				.define('#', HEART_ASPECT_PLANKS.get()).pattern("##")
				.unlockedBy("has_heart_aspect_planks", has(HEART_ASPECT_PLANKS.get())).save(recipeSaver);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, HEART_ASPECT_BUTTON.get()).requires(HEART_ASPECT_PLANKS.get())
				.unlockedBy("has_heart_aspect_planks", has(HEART_ASPECT_PLANKS.get())).save(recipeSaver);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HOPE_ASPECT_STAIRS.get(), 4).group("wooden_stairs")
				.define('#', HOPE_ASPECT_PLANKS.get()).pattern("#  ").pattern("## ").pattern("###")
				.unlockedBy("has_hope_aspect_planks", has(HOPE_ASPECT_PLANKS.get())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HOPE_ASPECT_SLAB.get(), 6).group("wooden_slab")
				.define('#', HOPE_ASPECT_PLANKS.get()).pattern("###")
				.unlockedBy("has_hope_aspect_planks", has(HOPE_ASPECT_PLANKS.get())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HOPE_ASPECT_FENCE.get(), 3).group("wooden_fence")
				.define('#', HOPE_ASPECT_PLANKS.get()).define('$', Items.STICK).pattern("#$#").pattern("#$#")
				.unlockedBy("has_hope_aspect_planks", has(HOPE_ASPECT_PLANKS.get())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HOPE_ASPECT_FENCE_GATE.get(), 1).group("wooden_fence_gate")
				.define('#', HOPE_ASPECT_PLANKS.get()).define('$', Items.STICK).pattern("$#$").pattern("$#$")
				.unlockedBy("has_hope_aspect_planks", has(HOPE_ASPECT_PLANKS.get())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HOPE_ASPECT_DOOR.get(), 3).group("wooden_door")
				.define('#', HOPE_ASPECT_PLANKS.get()).pattern("## ").pattern("## ").pattern("## ")
				.unlockedBy("has_hope_aspect_planks", has(HOPE_ASPECT_PLANKS.get())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HOPE_ASPECT_TRAPDOOR.get(), 2).group("wooden_trapdoor")
				.define('#', HOPE_ASPECT_PLANKS.get()).pattern("###").pattern("###")
				.unlockedBy("has_hope_aspect_planks", has(HOPE_ASPECT_PLANKS.get())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HOPE_ASPECT_PRESSURE_PLATE.get(), 1).group("stone_pressure_plate")
				.define('#', HOPE_ASPECT_PLANKS.get()).pattern("##")
				.unlockedBy("has_hope_aspect_planks", has(HOPE_ASPECT_PLANKS.get())).save(recipeSaver);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, HOPE_ASPECT_BUTTON.get()).requires(HOPE_ASPECT_PLANKS.get())
				.unlockedBy("has_hope_aspect_planks", has(HOPE_ASPECT_PLANKS.get())).save(recipeSaver);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, LIFE_ASPECT_STAIRS.get(), 4).group("wooden_stairs")
				.define('#', LIFE_ASPECT_PLANKS.get()).pattern("#  ").pattern("## ").pattern("###")
				.unlockedBy("has_life_aspect_planks", has(LIFE_ASPECT_PLANKS.get())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, LIFE_ASPECT_SLAB.get(), 6).group("wooden_slab")
				.define('#', LIFE_ASPECT_PLANKS.get()).pattern("###")
				.unlockedBy("has_life_aspect_planks", has(LIFE_ASPECT_PLANKS.get())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, LIFE_ASPECT_FENCE.get(), 3).group("wooden_fence")
				.define('#', LIFE_ASPECT_PLANKS.get()).define('$', Items.STICK).pattern("#$#").pattern("#$#")
				.unlockedBy("has_life_aspect_planks", has(LIFE_ASPECT_PLANKS.get())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, LIFE_ASPECT_FENCE_GATE.get(), 1).group("wooden_fence_gate")
				.define('#', LIFE_ASPECT_PLANKS.get()).define('$', Items.STICK).pattern("$#$").pattern("$#$")
				.unlockedBy("has_life_aspect_planks", has(LIFE_ASPECT_PLANKS.get())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, LIFE_ASPECT_DOOR.get(), 3).group("wooden_door")
				.define('#', LIFE_ASPECT_PLANKS.get()).pattern("## ").pattern("## ").pattern("## ")
				.unlockedBy("has_life_aspect_planks", has(LIFE_ASPECT_PLANKS.get())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, LIFE_ASPECT_TRAPDOOR.get(), 2).group("wooden_trapdoor")
				.define('#', LIFE_ASPECT_PLANKS.get()).pattern("###").pattern("###")
				.unlockedBy("has_life_aspect_planks", has(LIFE_ASPECT_PLANKS.get())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, LIFE_ASPECT_PRESSURE_PLATE.get(), 1).group("stone_pressure_plate")
				.define('#', LIFE_ASPECT_PLANKS.get()).pattern("##")
				.unlockedBy("has_life_aspect_planks", has(LIFE_ASPECT_PLANKS.get())).save(recipeSaver);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, LIFE_ASPECT_BUTTON.get()).requires(LIFE_ASPECT_PLANKS.get())
				.unlockedBy("has_life_aspect_planks", has(LIFE_ASPECT_PLANKS.get())).save(recipeSaver);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, LIGHT_ASPECT_STAIRS.get(), 4).group("wooden_stairs")
				.define('#', LIGHT_ASPECT_PLANKS.get()).pattern("#  ").pattern("## ").pattern("###")
				.unlockedBy("has_light_aspect_planks", has(LIGHT_ASPECT_PLANKS.get())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, LIGHT_ASPECT_SLAB.get(), 6).group("wooden_slab")
				.define('#', LIGHT_ASPECT_PLANKS.get()).pattern("###")
				.unlockedBy("has_light_aspect_planks", has(LIGHT_ASPECT_PLANKS.get())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, LIGHT_ASPECT_FENCE.get(), 3).group("wooden_fence")
				.define('#', LIGHT_ASPECT_PLANKS.get()).define('$', Items.STICK).pattern("#$#").pattern("#$#")
				.unlockedBy("has_light_aspect_planks", has(LIGHT_ASPECT_PLANKS.get())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, LIGHT_ASPECT_FENCE_GATE.get(), 1).group("wooden_fence_gate")
				.define('#', LIGHT_ASPECT_PLANKS.get()).define('$', Items.STICK).pattern("$#$").pattern("$#$")
				.unlockedBy("has_light_aspect_planks", has(LIGHT_ASPECT_PLANKS.get())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, LIGHT_ASPECT_DOOR.get(), 3).group("wooden_door")
				.define('#', LIGHT_ASPECT_PLANKS.get()).pattern("## ").pattern("## ").pattern("## ")
				.unlockedBy("has_light_aspect_planks", has(LIGHT_ASPECT_PLANKS.get())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, LIGHT_ASPECT_TRAPDOOR.get(), 2).group("wooden_trapdoor")
				.define('#', LIGHT_ASPECT_PLANKS.get()).pattern("###").pattern("###")
				.unlockedBy("has_light_aspect_planks", has(LIGHT_ASPECT_PLANKS.get())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, LIGHT_ASPECT_PRESSURE_PLATE.get(), 1).group("stone_pressure_plate")
				.define('#', LIGHT_ASPECT_PLANKS.get()).pattern("##")
				.unlockedBy("has_light_aspect_planks", has(LIGHT_ASPECT_PLANKS.get())).save(recipeSaver);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, LIGHT_ASPECT_BUTTON.get()).requires(LIGHT_ASPECT_PLANKS.get())
				.unlockedBy("has_light_aspect_planks", has(LIGHT_ASPECT_PLANKS.get())).save(recipeSaver);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, MIND_ASPECT_STAIRS.get(), 4).group("wooden_stairs")
				.define('#', MIND_ASPECT_PLANKS.get()).pattern("#  ").pattern("## ").pattern("###")
				.unlockedBy("has_mind_aspect_planks", has(MIND_ASPECT_PLANKS.get())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, MIND_ASPECT_SLAB.get(), 6).group("wooden_slab")
				.define('#', MIND_ASPECT_PLANKS.get()).pattern("###")
				.unlockedBy("has_mind_aspect_planks", has(MIND_ASPECT_PLANKS.get())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, MIND_ASPECT_FENCE.get(), 3).group("wooden_fence")
				.define('#', MIND_ASPECT_PLANKS.get()).define('$', Items.STICK).pattern("#$#").pattern("#$#")
				.unlockedBy("has_mind_aspect_planks", has(MIND_ASPECT_PLANKS.get())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, MIND_ASPECT_FENCE_GATE.get(), 1).group("wooden_fence_gate")
				.define('#', MIND_ASPECT_PLANKS.get()).define('$', Items.STICK).pattern("$#$").pattern("$#$")
				.unlockedBy("has_mind_aspect_planks", has(MIND_ASPECT_PLANKS.get())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, MIND_ASPECT_DOOR.get(), 3).group("wooden_door")
				.define('#', MIND_ASPECT_PLANKS.get()).pattern("## ").pattern("## ").pattern("## ")
				.unlockedBy("has_mind_aspect_planks", has(MIND_ASPECT_PLANKS.get())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, MIND_ASPECT_TRAPDOOR.get(), 2).group("wooden_trapdoor")
				.define('#', MIND_ASPECT_PLANKS.get()).pattern("###").pattern("###")
				.unlockedBy("has_mind_aspect_planks", has(MIND_ASPECT_PLANKS.get())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, MIND_ASPECT_PRESSURE_PLATE.get(), 1).group("stone_pressure_plate")
				.define('#', MIND_ASPECT_PLANKS.get()).pattern("##")
				.unlockedBy("has_mind_aspect_planks", has(MIND_ASPECT_PLANKS.get())).save(recipeSaver);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, MIND_ASPECT_BUTTON.get()).requires(MIND_ASPECT_PLANKS.get())
				.unlockedBy("has_mind_aspect_planks", has(MIND_ASPECT_PLANKS.get())).save(recipeSaver);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, RAGE_ASPECT_STAIRS.get(), 4).group("wooden_stairs")
				.define('#', RAGE_ASPECT_PLANKS.get()).pattern("#  ").pattern("## ").pattern("###")
				.unlockedBy("has_rage_aspect_planks", has(RAGE_ASPECT_PLANKS.get())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, RAGE_ASPECT_SLAB.get(), 6).group("wooden_slab")
				.define('#', RAGE_ASPECT_PLANKS.get()).pattern("###")
				.unlockedBy("has_rage_aspect_planks", has(RAGE_ASPECT_PLANKS.get())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, RAGE_ASPECT_FENCE.get(), 3).group("wooden_fence")
				.define('#', RAGE_ASPECT_PLANKS.get()).define('$', Items.STICK).pattern("#$#").pattern("#$#")
				.unlockedBy("has_rage_aspect_planks", has(RAGE_ASPECT_PLANKS.get())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, RAGE_ASPECT_FENCE_GATE.get(), 1).group("wooden_fence_gate")
				.define('#', RAGE_ASPECT_PLANKS.get()).define('$', Items.STICK).pattern("$#$").pattern("$#$")
				.unlockedBy("has_rage_aspect_planks", has(RAGE_ASPECT_PLANKS.get())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, RAGE_ASPECT_DOOR.get(), 3).group("wooden_door")
				.define('#', RAGE_ASPECT_PLANKS.get()).pattern("## ").pattern("## ").pattern("## ")
				.unlockedBy("has_rage_aspect_planks", has(RAGE_ASPECT_PLANKS.get())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, RAGE_ASPECT_TRAPDOOR.get(), 2).group("wooden_trapdoor")
				.define('#', RAGE_ASPECT_PLANKS.get()).pattern("###").pattern("###")
				.unlockedBy("has_rage_aspect_planks", has(RAGE_ASPECT_PLANKS.get())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, RAGE_ASPECT_PRESSURE_PLATE.get(), 1).group("stone_pressure_plate")
				.define('#', RAGE_ASPECT_PLANKS.get()).pattern("##")
				.unlockedBy("has_rage_aspect_planks", has(RAGE_ASPECT_PLANKS.get())).save(recipeSaver);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, RAGE_ASPECT_BUTTON.get()).requires(RAGE_ASPECT_PLANKS.get())
				.unlockedBy("has_rage_aspect_planks", has(RAGE_ASPECT_PLANKS.get())).save(recipeSaver);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, SPACE_ASPECT_STAIRS.get(), 4).group("wooden_stairs")
				.define('#', SPACE_ASPECT_PLANKS.get()).pattern("#  ").pattern("## ").pattern("###")
				.unlockedBy("has_space_aspect_planks", has(SPACE_ASPECT_PLANKS.get())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, SPACE_ASPECT_SLAB.get(), 6).group("wooden_slab")
				.define('#', SPACE_ASPECT_PLANKS.get()).pattern("###")
				.unlockedBy("has_space_aspect_planks", has(SPACE_ASPECT_PLANKS.get())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, SPACE_ASPECT_FENCE.get(), 3).group("wooden_fence")
				.define('#', SPACE_ASPECT_PLANKS.get()).define('$', Items.STICK).pattern("#$#").pattern("#$#")
				.unlockedBy("has_space_aspect_planks", has(SPACE_ASPECT_PLANKS.get())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, SPACE_ASPECT_FENCE_GATE.get(), 1).group("wooden_fence_gate")
				.define('#', SPACE_ASPECT_PLANKS.get()).define('$', Items.STICK).pattern("$#$").pattern("$#$")
				.unlockedBy("has_space_aspect_planks", has(SPACE_ASPECT_PLANKS.get())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, SPACE_ASPECT_DOOR.get(), 3).group("wooden_door")
				.define('#', SPACE_ASPECT_PLANKS.get()).pattern("## ").pattern("## ").pattern("## ")
				.unlockedBy("has_space_aspect_planks", has(SPACE_ASPECT_PLANKS.get())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, SPACE_ASPECT_TRAPDOOR.get(), 2).group("wooden_trapdoor")
				.define('#', SPACE_ASPECT_PLANKS.get()).pattern("###").pattern("###")
				.unlockedBy("has_space_aspect_planks", has(SPACE_ASPECT_PLANKS.get())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, SPACE_ASPECT_PRESSURE_PLATE.get(), 1).group("stone_pressure_plate")
				.define('#', SPACE_ASPECT_PLANKS.get()).pattern("##")
				.unlockedBy("has_space_aspect_planks", has(SPACE_ASPECT_PLANKS.get())).save(recipeSaver);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, SPACE_ASPECT_BUTTON.get()).requires(SPACE_ASPECT_PLANKS.get())
				.unlockedBy("has_space_aspect_planks", has(SPACE_ASPECT_PLANKS.get())).save(recipeSaver);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, TIME_ASPECT_STAIRS.get(), 4).group("wooden_stairs")
				.define('#', TIME_ASPECT_PLANKS.get()).pattern("#  ").pattern("## ").pattern("###")
				.unlockedBy("has_time_aspect_planks", has(TIME_ASPECT_PLANKS.get())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, TIME_ASPECT_SLAB.get(), 6).group("wooden_slab")
				.define('#', TIME_ASPECT_PLANKS.get()).pattern("###")
				.unlockedBy("has_time_aspect_planks", has(TIME_ASPECT_PLANKS.get())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, TIME_ASPECT_FENCE.get(), 3).group("wooden_fence")
				.define('#', TIME_ASPECT_PLANKS.get()).define('$', Items.STICK).pattern("#$#").pattern("#$#")
				.unlockedBy("has_time_aspect_planks", has(TIME_ASPECT_PLANKS.get())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, TIME_ASPECT_FENCE_GATE.get(), 1).group("wooden_fence_gate")
				.define('#', TIME_ASPECT_PLANKS.get()).define('$', Items.STICK).pattern("$#$").pattern("$#$")
				.unlockedBy("has_time_aspect_planks", has(TIME_ASPECT_PLANKS.get())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, TIME_ASPECT_DOOR.get(), 3).group("wooden_door")
				.define('#', TIME_ASPECT_PLANKS.get()).pattern("## ").pattern("## ").pattern("## ")
				.unlockedBy("has_time_aspect_planks", has(TIME_ASPECT_PLANKS.get())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, TIME_ASPECT_TRAPDOOR.get(), 2).group("wooden_trapdoor")
				.define('#', TIME_ASPECT_PLANKS.get()).pattern("###").pattern("###")
				.unlockedBy("has_time_aspect_planks", has(TIME_ASPECT_PLANKS.get())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, TIME_ASPECT_PRESSURE_PLATE.get(), 1).group("stone_pressure_plate")
				.define('#', TIME_ASPECT_PLANKS.get()).pattern("##")
				.unlockedBy("has_time_aspect_planks", has(TIME_ASPECT_PLANKS.get())).save(recipeSaver);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, TIME_ASPECT_BUTTON.get()).requires(TIME_ASPECT_PLANKS.get())
				.unlockedBy("has_time_aspect_planks", has(TIME_ASPECT_PLANKS.get())).save(recipeSaver);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, VOID_ASPECT_STAIRS.get(), 4).group("wooden_stairs")
				.define('#', VOID_ASPECT_PLANKS.get()).pattern("#  ").pattern("## ").pattern("###")
				.unlockedBy("has_void_aspect_planks", has(VOID_ASPECT_PLANKS.get())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, VOID_ASPECT_SLAB.get(), 6).group("wooden_slab")
				.define('#', VOID_ASPECT_PLANKS.get()).pattern("###")
				.unlockedBy("has_void_aspect_planks", has(VOID_ASPECT_PLANKS.get())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, VOID_ASPECT_FENCE.get(), 3).group("wooden_fence")
				.define('#', VOID_ASPECT_PLANKS.get()).define('$', Items.STICK).pattern("#$#").pattern("#$#")
				.unlockedBy("has_void_aspect_planks", has(VOID_ASPECT_PLANKS.get())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, VOID_ASPECT_FENCE_GATE.get(), 1).group("wooden_fence_gate")
				.define('#', VOID_ASPECT_PLANKS.get()).define('$', Items.STICK).pattern("$#$").pattern("$#$")
				.unlockedBy("has_void_aspect_planks", has(VOID_ASPECT_PLANKS.get())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, VOID_ASPECT_DOOR.get(), 3).group("wooden_door")
				.define('#', VOID_ASPECT_PLANKS.get()).pattern("## ").pattern("## ").pattern("## ")
				.unlockedBy("has_void_aspect_planks", has(VOID_ASPECT_PLANKS.get())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, VOID_ASPECT_TRAPDOOR.get(), 2).group("wooden_trapdoor")
				.define('#', VOID_ASPECT_PLANKS.get()).pattern("###").pattern("###")
				.unlockedBy("has_void_aspect_planks", has(VOID_ASPECT_PLANKS.get())).save(recipeSaver);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, VOID_ASPECT_PRESSURE_PLATE.get(), 1).group("stone_pressure_plate")
				.define('#', VOID_ASPECT_PLANKS.get()).pattern("##")
				.unlockedBy("has_void_aspect_planks", has(VOID_ASPECT_PLANKS.get())).save(recipeSaver);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, VOID_ASPECT_BUTTON.get()).requires(VOID_ASPECT_PLANKS.get())
				.unlockedBy("has_void_aspect_planks", has(VOID_ASPECT_PLANKS.get())).save(recipeSaver);
	}
}
