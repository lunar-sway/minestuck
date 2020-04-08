package com.mraof.minestuck.data.recipe;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.item.crafting.alchemy.GristType;
import com.mraof.minestuck.item.crafting.alchemy.GristTypes;
import com.mraof.minestuck.util.ExtraForgeTags;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;

import static com.mraof.minestuck.item.crafting.alchemy.GristTypes.*;

public class MinestuckGristCostsProvider extends RecipeProvider
{
	public MinestuckGristCostsProvider(DataGenerator generator)
	{
		super(generator);
	}
	
	@Override
	protected void registerRecipes(Consumer<IFinishedRecipe> recipeSaver)
	{
		GeneratedGristCostBuilder.create().build(recipeSaver, new ResourceLocation(Minestuck.MOD_ID, "generated"));
		
		//Stone
		GristCostRecipeBuilder.of(Tags.Items.STONE).grist(BUILD, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Tags.Items.COBBLESTONE).grist(BUILD, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.STONE_SLAB).grist(BUILD, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.STONE_BRICKS).grist(BUILD, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.CRACKED_STONE_BRICKS).grist(BUILD, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.STONE_BRICK_STAIRS).grist(BUILD, 3).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.GRAVEL).grist(BUILD, 3).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(ItemTags.SAND).grist(BUILD, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.GLASS).grist(BUILD, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.SANDSTONE).grist(BUILD, 4).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.RED_SANDSTONE).grist(BUILD, 4).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.SANDSTONE_STAIRS).grist(BUILD, 6).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(ExtraForgeTags.Items.TERRACOTTA).grist(SHALE, 12).grist(MARBLE, 4).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.CLAY_BALL).grist(SHALE, 3).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.BRICK).grist(SHALE, 3).grist(TAR, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.FLINT).grist(BUILD, 4).buildFor(recipeSaver, Minestuck.MOD_ID);
		
		//Ore
		GristCostRecipeBuilder.of(Tags.Items.ORES_COAL).grist(BUILD, 4).grist(TAR, 8).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Tags.Items.ORES_IRON).grist(BUILD, 4).grist(RUST, 9).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Tags.Items.ORES_GOLD).grist(BUILD, 4).grist(GOLD, 9).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Tags.Items.ORES_REDSTONE).grist(BUILD, 4).grist(GARNET, 16).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Tags.Items.ORES_LAPIS).grist(BUILD, 4).grist(AMETHYST, 16).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Tags.Items.ORES_DIAMOND).grist(BUILD, 4).grist(DIAMOND, 18).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Tags.Items.ORES_EMERALD).grist(BUILD, 4).grist(RUBY, 9).grist(DIAMOND, 9).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Tags.Items.ORES_QUARTZ).grist(BUILD, 4).grist(QUARTZ, 8).grist(MARBLE, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.COAL).grist(TAR, 8).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.CHARCOAL).grist(TAR, 6).grist(AMBER, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.GOLD_INGOT).grist(GOLD, 9).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.IRON_INGOT).grist(RUST, 9).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.REDSTONE).grist(GARNET, 4).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.QUARTZ).grist(QUARTZ, 4).grist(MARBLE, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.QUARTZ_BLOCK).grist(QUARTZ, 16).grist(MARBLE, 4).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.EMERALD).grist(RUBY, 9).grist(DIAMOND, 9).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.DIAMOND).grist(DIAMOND, 18).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.LAPIS_LAZULI).grist(AMETHYST, 4).buildFor(recipeSaver, Minestuck.MOD_ID);
		
		//Dirt
		GristCostRecipeBuilder.of(Items.DIRT).grist(BUILD, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.GRASS_BLOCK).grist(BUILD, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.PODZOL).grist(BUILD, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.MYCELIUM).grist(IODINE, 2).grist(RUBY, 2).grist(BUILD, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		
		//Snow/ice
		GristCostRecipeBuilder.of(Items.SNOWBALL).grist(COBALT, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.SNOW_BLOCK).grist(BUILD, 5).grist(COBALT, 3).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.SNOW).grist(COBALT, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.ICE).grist(COBALT, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.PACKED_ICE).grist(BUILD, 10).grist(COBALT, 6).buildFor(recipeSaver, Minestuck.MOD_ID);
		
		GristCostRecipeBuilder.of(ItemTags.LOGS).grist(BUILD, 8).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(ItemTags.LEAVES).grist(BUILD, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(ItemTags.SAPLINGS).grist(BUILD, 16).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(ItemTags.PLANKS).grist(BUILD, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.DEAD_BUSH).grist(AMBER, 2).grist(SULFUR, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.MELON).grist(AMBER, 8).grist(CAULK, 8).grist(BUILD, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.NETHERRACK).grist(BUILD, 2).grist(TAR, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.OBSIDIAN).grist(BUILD, 6).grist(COBALT, 8).grist(TAR, 16).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.PUMPKIN).grist(AMBER, 12).grist(CAULK, 6).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.BAMBOO).grist(BUILD, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.MUSHROOM_STEM).grist(BUILD, 4).grist(IODINE, 3).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.BROWN_MUSHROOM_BLOCK).grist(BUILD, 2).grist(IODINE, 5).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.RED_MUSHROOM_BLOCK).grist(BUILD, 2).grist(IODINE, 3).grist(RUBY, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.COBWEB).grist(BUILD, 18).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.TORCH).grist(BUILD, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.DISPENSER).grist(BUILD, 17).grist(GARNET, 4).grist(CHALK, 6).buildFor(recipeSaver, Minestuck.MOD_ID);
		
		//Underwater
		GristCostRecipeBuilder.of(Items.SPONGE).grist(AMBER, 20).grist(SULFUR, 30).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.WET_SPONGE).grist(AMBER, 20).grist(SULFUR, 30).grist(COBALT, 10).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.PRISMARINE_CRYSTALS).grist(COBALT, 5).grist(DIAMOND, 2).grist(AMETHYST, 4).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.PRISMARINE_SHARD).grist(COBALT, 3).grist(BUILD, 3).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.PRISMARINE).grist(COBALT, 7).grist(BUILD, 12).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.PRISMARINE_BRICKS).grist(COBALT, 12).grist(BUILD, 18).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.DARK_PRISMARINE).grist(COBALT, 10).grist(TAR, 2).grist(BUILD, 18).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.SEA_LANTERN).grist(COBALT, 32).grist(DIAMOND, 6).grist(AMETHYST, 12).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.HEART_OF_THE_SEA).grist(COBALT, 120).grist(DIAMOND, 55).grist(AMETHYST, 20).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.NAUTILUS_SHELL).grist(COBALT, 10).grist(QUARTZ, 1).grist(SHALE, 5).grist(IODINE, 12).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.SCUTE).grist(COBALT, 5).grist(SHALE, 5).grist(IODINE, 4).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.TURTLE_EGG).grist(AMBER, 10).grist(COBALT, 5).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.SEAGRASS).grist(BUILD, 2).grist(COBALT, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.SEA_PICKLE).grist(BUILD, 4).grist(COBALT, 2).grist(AMBER, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.KELP).grist(IODINE, 4).grist(COBALT, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		
		//Coral
		GristCostRecipeBuilder.of(Items.TUBE_CORAL).grist(COBALT, 2).grist(AMETHYST, 4).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.FIRE_CORAL).grist(COBALT, 2).grist(GARNET, 4).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.BRAIN_CORAL).grist(COBALT, 2).grist(GARNET, 3).grist(CHALK, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.BUBBLE_CORAL).grist(COBALT, 2).grist(AMBER, 4).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.TUBE_CORAL_FAN).grist(COBALT, 1).grist(AMETHYST, 4).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.FIRE_CORAL_FAN).grist(COBALT, 1).grist(GARNET, 4).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.BRAIN_CORAL_FAN).grist(COBALT, 1).grist(GARNET, 3).grist(CHALK, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.BUBBLE_CORAL_FAN).grist(COBALT, 1).grist(AMBER, 4).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.TUBE_CORAL_BLOCK).grist(BUILD, 2).grist(COBALT, 2).grist(AMETHYST, 4).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.FIRE_CORAL_BLOCK).grist(BUILD, 2).grist(COBALT, 2).grist(GARNET, 4).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.BRAIN_CORAL_BLOCK).grist(BUILD, 2).grist(COBALT, 2).grist(GARNET, 3).grist(CHALK, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.BUBBLE_CORAL_BLOCK).grist(BUILD, 2).grist(COBALT, 2).grist(AMBER, 4).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.DEAD_TUBE_CORAL).grist(BUILD, 6).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.DEAD_FIRE_CORAL).grist(BUILD, 6).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.DEAD_BRAIN_CORAL).grist(BUILD, 6).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.DEAD_BUBBLE_CORAL).grist(BUILD, 6).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.DEAD_TUBE_CORAL_FAN).grist(BUILD, 5).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.DEAD_FIRE_CORAL_FAN).grist(BUILD, 5).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.DEAD_BRAIN_CORAL_FAN).grist(BUILD, 5).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.DEAD_BUBBLE_CORAL_FAN).grist(BUILD, 5).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.DEAD_TUBE_CORAL_BLOCK).grist(BUILD, 8).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.DEAD_FIRE_CORAL_BLOCK).grist(BUILD, 8).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.DEAD_BRAIN_CORAL_BLOCK).grist(BUILD, 8).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.DEAD_BUBBLE_CORAL_BLOCK).grist(BUILD, 8).buildFor(recipeSaver, Minestuck.MOD_ID);
		
		//End
		GristCostRecipeBuilder.of(Items.DRAGON_EGG).grist(URANIUM, 800).grist(TAR, 800).grist(ZILLIUM, 10).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.END_STONE).grist(CAULK, 3).grist(BUILD, 4).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.DRAGON_BREATH).grist(RUBY, 4).grist(SULFUR, 13).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.ELYTRA).grist(DIAMOND, 51).grist(SULFUR, 38).grist(CAULK, 65).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.CHORUS_FRUIT).grist(AMETHYST, 2).grist(SHALE, 4).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.POPPED_CHORUS_FRUIT).grist(AMETHYST, 2).grist(SHALE, 4).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.CHORUS_FLOWER).grist(BUILD, 26).grist(AMETHYST, 23).grist(SHALE, 10).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.PURPUR_PILLAR).grist(AMETHYST, 2).grist(SHALE, 4).buildFor(recipeSaver, Minestuck.MOD_ID);
		
		//Wool
		GristCostRecipeBuilder.of(Items.WHITE_WOOL).grist(CHALK, 6).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.ORANGE_WOOL).grist(CHALK, 6).grist(AMBER, 1).grist(GARNET, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.MAGENTA_WOOL).grist(CHALK, 6).grist(GARNET, 2).grist(AMETHYST, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.LIGHT_BLUE_WOOL).grist(CHALK, 8).grist(AMETHYST, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.YELLOW_WOOL).grist(CHALK, 6).grist(AMBER, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.LIME_WOOL).grist(CHALK, 6).grist(AMBER, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.PINK_WOOL).grist(CHALK, 6).grist(AMETHYST, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.GRAY_WOOL).grist(CHALK, 6).grist(TAR, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.LIGHT_GRAY_WOOL).grist(CHALK, 6).grist(TAR, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.CYAN_WOOL).grist(CHALK, 6).grist(AMBER, 1).grist(AMETHYST, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.PURPLE_WOOL).grist(CHALK, 6).grist(AMETHYST, 1).grist(GARNET, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.BLUE_WOOL).grist(CHALK, 6).grist(AMETHYST, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.BROWN_WOOL).grist(CHALK, 6).grist(IODINE, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.GREEN_WOOL).grist(CHALK, 6).grist(AMBER, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.RED_WOOL).grist(CHALK, 6).grist(GARNET, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.BLACK_WOOL).grist(CHALK, 6).grist(TAR, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		
		//Items
		GristCostRecipeBuilder.of(Items.DANDELION).grist(AMBER, 4).grist(IODINE, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.POPPY).grist(GARNET, 4).grist(IODINE, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.CORNFLOWER).grist(AMETHYST, 4).grist(IODINE, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.LILY_OF_THE_VALLEY).grist(CHALK, 2).grist(IODINE, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.BLUE_ORCHID).grist(AMETHYST, 2).grist(CHALK, 2).grist(IODINE, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.ALLIUM).grist(AMETHYST, 1).grist(GARNET, 3).grist(IODINE, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.AZURE_BLUET).grist(TAR, 1).grist(CHALK, 3).grist(IODINE, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.RED_TULIP).grist(TAR, 1).grist(CHALK, 3).grist(IODINE, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.ORANGE_TULIP).grist(GARNET, 2).grist(AMBER, 2).grist(IODINE, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.WHITE_TULIP).grist(AMETHYST, 2).grist(CHALK, 2).grist(IODINE, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.PINK_TULIP).grist(GARNET, 4).grist(IODINE, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.OXEYE_DAISY).grist(TAR, 1).grist(CHALK, 3).grist(IODINE, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.WITHER_ROSE).grist(TAR, 6).grist(URANIUM, 2).grist(IODINE, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.BROWN_MUSHROOM).grist(IODINE, 5).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.RED_MUSHROOM).grist(IODINE, 3).grist(RUBY, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.CACTUS).grist(AMBER, 4).grist(IODINE, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.GRASS).grist(BUILD, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.FERN).grist(BUILD, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.VINE).grist(BUILD, 2).grist(AMBER, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.LILY_PAD).grist(AMBER, 4).grist(IODINE, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.SUNFLOWER).grist(AMBER, 7).grist(IODINE, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.LILAC).grist(AMETHYST, 2).grist(GARNET, 5).grist(IODINE, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.TALL_GRASS).grist(AMBER, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.LARGE_FERN).grist(AMBER, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.ROSE_BUSH).grist(GARNET, 7).grist(IODINE, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.PEONY).grist(GARNET, 4).grist(CHALK, 4).grist(IODINE, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.WHEAT).grist(IODINE, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.WHEAT_SEEDS).grist(AMBER, 1).grist(IODINE, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.COCOA_BEANS).grist(IODINE, 3).grist(AMBER, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.SUGAR_CANE).grist(AMBER, 3).grist(IODINE, 3).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.SUGAR).grist(AMBER, 3).grist(IODINE, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.BEETROOT).grist(RUST, 1).grist(CHALK, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.BEETROOT_SEEDS).grist(RUST, 2).grist(IODINE, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.APPLE).grist(AMBER, 2).grist(SHALE, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.SWEET_BERRIES).grist(AMBER, 3).grist(IODINE, 2).grist(SHALE, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.CARROT).grist(AMBER, 3).grist(CHALK, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.POTATO).grist(AMBER, 4).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.POISONOUS_POTATO).grist(AMBER, 4).grist(IODINE, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.BAKED_POTATO).grist(AMBER, 4).grist(TAR, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.MELON_SLICE).grist(AMBER, 1).grist(CAULK, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.ENCHANTED_GOLDEN_APPLE).grist(AMBER, 4).grist(GOLD, 150).grist(URANIUM, 10).buildFor(recipeSaver, Minestuck.MOD_ID);
		
		//Animal drops
		GristCostRecipeBuilder.of(Items.FEATHER).grist(CHALK, 4).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.LEATHER).grist(IODINE, 3).grist(CHALK, 3).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.RABBIT_HIDE).grist(IODINE, 1).grist(CHALK, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.RABBIT_FOOT).grist(IODINE, 10).grist(CHALK, 12).grist(RUST, 3).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.EGG).grist(AMBER, 5).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.INK_SAC).grist(TAR, 4).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.BEEF).grist(IODINE, 12).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.PORKCHOP).grist(IODINE, 10).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.CHICKEN).grist(IODINE, 10).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.COD).grist(CAULK, 4).grist(AMBER, 4).grist(COBALT, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.SALMON).grist(CAULK, 4).grist(AMBER, 4).grist(COBALT, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.TROPICAL_FISH).grist(CAULK, 4).grist(AMBER, 4).grist(COBALT, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.PUFFERFISH).grist(IODINE, 2).grist(CAULK, 4).grist(AMBER, 6).grist(COBALT, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.MUTTON).grist(IODINE, 10).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.RABBIT).grist(IODINE, 8).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.COOKED_BEEF).grist(IODINE, 12).grist(TAR, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.COOKED_PORKCHOP).grist(IODINE, 10).grist(TAR, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.COOKED_CHICKEN).grist(IODINE, 10).grist(TAR, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.COOKED_COD).grist(CAULK, 4).grist(AMBER, 4).grist(COBALT, 2).grist(TAR, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.COOKED_MUTTON).grist(IODINE, 10).grist(TAR, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.COOKED_RABBIT).grist(IODINE, 8).grist(TAR, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		
		//Monster drops
		GristCostRecipeBuilder.of(Items.ROTTEN_FLESH).grist(RUST, 1).grist(IODINE, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.BONE).grist(CHALK, 6).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.STRING).grist(CHALK, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.SPIDER_EYE).grist(AMBER, 6).grist(IODINE, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.GUNPOWDER).grist(SULFUR, 3).grist(CHALK, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.SLIME_BALL).grist(CAULK, 8).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.ENDER_PEARL).grist(URANIUM, 13).grist(DIAMOND, 5).grist(MERCURY, 8).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.ZOMBIE_HEAD).grist(RUST, 5).grist(CHALK, 20).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.SKELETON_SKULL).grist(CHALK, 28).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.CREEPER_HEAD).grist(SULFUR, 10).grist(CHALK, 18).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.WITHER_SKELETON_SKULL).grist(URANIUM, 35).grist(TAR, 48).grist(DIAMOND, 16).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.DRAGON_HEAD).grist(URANIUM, 25).grist(TAR, 70).grist(ZILLIUM, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.PLAYER_HEAD).grist(ARTIFACT, 10).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.SHULKER_SHELL).grist(BUILD, 2500).grist(DIAMOND, 250).grist(URANIUM, 500).grist(MERCURY, 200).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.PHANTOM_MEMBRANE).grist(CAULK, 26).grist(SULFUR, 8).grist(MERCURY, 12).buildFor(recipeSaver, Minestuck.MOD_ID);
		
		//Nether
		GristCostRecipeBuilder.of(Items.SOUL_SAND).grist(BUILD, 2).grist(SULFUR, 5).grist(CAULK, 3).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.BLAZE_ROD).grist(TAR, 20).grist(URANIUM, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.GHAST_TEAR).grist(COBALT, 10).grist(CHALK, 19).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.GLOWSTONE_DUST).grist(TAR, 4).grist(CHALK, 6).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.NETHER_WART).grist(IODINE, 3).grist(TAR, 5).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.NETHER_BRICK).grist(BUILD, 1).grist(TAR, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.NETHER_STAR).grist(URANIUM, 344).grist(TAR, 135).grist(DIAMOND, 92).buildFor(recipeSaver, Minestuck.MOD_ID);
		
		GristCostRecipeBuilder.of(Items.CHAINMAIL_HELMET).grist(RUST, 20).grist(MERCURY, 10).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.CHAINMAIL_CHESTPLATE).grist(RUST, 32).grist(MERCURY, 16).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.CHAINMAIL_LEGGINGS).grist(RUST, 28).grist(MERCURY, 14).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.CHAINMAIL_BOOTS).grist(RUST, 16).grist(MERCURY, 8).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.IRON_HORSE_ARMOR).grist(RUST, 40).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.GOLDEN_HORSE_ARMOR).grist(GOLD, 40).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.DIAMOND_HORSE_ARMOR).grist(DIAMOND, 80).buildFor(recipeSaver, Minestuck.MOD_ID);
		
		//Dyes
		GristCostRecipeBuilder.of(Items.WHITE_DYE).grist(CHALK, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.BLACK_DYE).grist(TAR, 4).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.BROWN_DYE).grist(IODINE, 3).grist(AMBER, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.BLUE_DYE).grist(AMETHYST, 4).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.RED_DYE).grist(GARNET, 4).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.YELLOW_DYE).grist(AMBER, 4).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.GREEN_DYE).grist(AMBER, 3).grist(IODINE, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.LIME_DYE).grist(AMBER, 3).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.LIGHT_BLUE_DYE).grist(AMETHYST, 2).grist(CHALK, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.MAGENTA_DYE).grist(AMETHYST, 1).grist(GARNET, 3).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.ORANGE_DYE).grist(GARNET, 2).grist(AMBER, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.PURPLE_DYE).grist(AMETHYST, 2).grist(GARNET, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.CYAN_DYE).grist(AMETHYST, 2).grist(AMBER, 2).grist(IODINE, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.LIGHT_GRAY_DYE).grist(TAR, 1).grist(CHALK, 3).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.GRAY_DYE).grist(TAR, 3).grist(CHALK, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.PINK_DYE).grist(GARNET, 2).grist(CHALK, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		
		//Concrete
		GristCostRecipeBuilder.of(Items.WHITE_CONCRETE).grist(CHALK, 1).grist(BUILD, 3).grist(COBALT, 3).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.BLACK_CONCRETE).grist(TAR, 1).grist(BUILD, 3).grist(COBALT, 3).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.BROWN_CONCRETE).grist(IODINE, 1).grist(AMBER, 1).grist(BUILD, 3).grist(COBALT, 3).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.BLUE_CONCRETE).grist(AMETHYST, 1).grist(BUILD, 3).grist(COBALT, 3).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.RED_CONCRETE).grist(GARNET, 1).grist(BUILD, 3).grist(COBALT, 3).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.YELLOW_CONCRETE).grist(AMBER, 1).grist(BUILD, 3).grist(COBALT, 3).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.GREEN_CONCRETE).grist(AMBER, 1).grist(IODINE, 1).grist(BUILD, 3).grist(COBALT, 3).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.LIME_CONCRETE).grist(AMBER, 1).grist(BUILD, 3).grist(COBALT, 3).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.LIGHT_BLUE_CONCRETE).grist(AMETHYST, 1).grist(CHALK, 1).grist(BUILD, 3).grist(COBALT, 3).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.MAGENTA_CONCRETE).grist(AMETHYST, 1).grist(GARNET, 1).grist(BUILD, 3).grist(COBALT, 3).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.ORANGE_CONCRETE).grist(GARNET, 1).grist(AMBER, 1).grist(BUILD, 3).grist(COBALT, 3).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.PURPLE_CONCRETE).grist(AMETHYST, 1).grist(GARNET, 1).grist(BUILD, 3).grist(COBALT, 3).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.CYAN_CONCRETE).grist(AMETHYST, 1).grist(AMBER, 1).grist(IODINE, 1).grist(BUILD, 3).grist(COBALT, 3).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.LIGHT_GRAY_CONCRETE).grist(TAR, 1).grist(CHALK, 1).grist(BUILD, 3).grist(COBALT, 3).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.GRAY_CONCRETE).grist(TAR, 1).grist(CHALK, 1).grist(BUILD, 3).grist(COBALT, 3).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.PINK_CONCRETE).grist(GARNET, 1).grist(CHALK, 1).grist(BUILD, 3).grist(COBALT, 3).buildFor(recipeSaver, Minestuck.MOD_ID);
		
		//Misc.
		GristCostRecipeBuilder.of(Items.WRITABLE_BOOK).grist(CHALK, 16).grist(IODINE, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.ENCHANTED_BOOK).grist(URANIUM, 8).grist(QUARTZ, 1).grist(DIAMOND, 4).grist(RUBY, 4).grist(CHALK, 16).grist(IODINE, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.EXPERIENCE_BOTTLE).grist(URANIUM, 16).grist(QUARTZ, 3).grist(DIAMOND, 4).grist(RUBY, 6).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.FIREWORK_STAR).grist(SULFUR, 4).grist(CHALK, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.FIREWORK_ROCKET).grist(SULFUR, 4).grist(CHALK, 5).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.MAP).grist(RUST, 32).grist(CHALK, 10).grist(GARNET, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.NAME_TAG).grist(BUILD, 4).grist(CAULK, 10).grist(AMBER, 12).grist(CHALK, 8).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.SADDLE).grist(RUST, 16).grist(IODINE, 7).grist(CHALK, 14).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.TOTEM_OF_UNDYING).grist(DIAMOND, 200).grist(URANIUM, 350).grist(RUBY, 90).grist(GOLD, 90).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.TRIDENT).grist(BUILD, 180).grist(COBALT, 224).grist(DIAMOND, 24).grist(AMETHYST, 47).buildFor(recipeSaver, Minestuck.MOD_ID);
		
		//Music Discs
		GristCostRecipeBuilder.of(Items.MUSIC_DISC_11).grist(BUILD, 10).grist(CAULK, 5).grist(TAR, 2).grist(MERCURY, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.MUSIC_DISC_13).grist(BUILD, 15).grist(CAULK, 8).grist(AMBER, 5).grist(CHALK, 5).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.MUSIC_DISC_BLOCKS).grist(BUILD, 15).grist(CAULK, 8).grist(RUBY, 5).grist(RUST, 5).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.MUSIC_DISC_CAT).grist(BUILD, 15).grist(CAULK, 8).grist(URANIUM, 5).grist(SHALE, 5).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.MUSIC_DISC_CHIRP).grist(BUILD, 15).grist(CAULK, 8).grist(RUBY, 5).grist(GARNET, 5).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.MUSIC_DISC_FAR).grist(BUILD, 15).grist(CAULK, 8).grist(URANIUM, 5).grist(SULFUR, 5).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.MUSIC_DISC_MALL).grist(BUILD, 15).grist(CAULK, 8).grist(AMETHYST, 5).grist(TAR, 5).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.MUSIC_DISC_MELLOHI).grist(BUILD, 15).grist(CAULK, 8).grist(MARBLE, 5).grist(SHALE, 5).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.MUSIC_DISC_STAL).grist(BUILD, 15).grist(CAULK, 8).grist(TAR, 5).grist(MERCURY, 5).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.MUSIC_DISC_STRAD).grist(BUILD, 15).grist(CAULK, 8).grist(CHALK, 5).grist(QUARTZ, 5).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.MUSIC_DISC_WAIT).grist(BUILD, 15).grist(CAULK, 8).grist(COBALT, 5).grist(QUARTZ, 5).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.MUSIC_DISC_WARD).grist(BUILD, 15).grist(CAULK, 8).grist(IODINE, 5).grist(GOLD, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		
		//Liquid Buckets
		ContainerGristCostBuilder.of(Items.WATER_BUCKET).grist(COBALT, 4).buildFor(recipeSaver, Minestuck.MOD_ID);
		ContainerGristCostBuilder.of(Items.LAVA_BUCKET).grist(TAR, 16).buildFor(recipeSaver, Minestuck.MOD_ID);
		ContainerGristCostBuilder.of(Items.MILK_BUCKET).grist(CHALK, 8).buildFor(recipeSaver, Minestuck.MOD_ID);
		
		//------------------------MINESTUCK------------------------\\
		
		GristCostRecipeBuilder.of(MSBlocks.BLUE_DIRT).grist(BUILD, 1).grist(SHALE, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.THOUGHT_DIRT).grist(BUILD, 1).grist(CAULK, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.GRIST_WIDGET).grist(BUILD, 550).grist(GARNET, 55).grist(RUBY, 34).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.GENERIC_OBJECT).grist(BUILD, 2).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.CHESSBOARD).grist(SHALE, 25).grist(MARBLE, 25).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.GRIMOIRE).grist(BUILD, 120).grist(AMETHYST, 60).grist(GARNET, 33).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.LONG_FORGOTTEN_WARHORN).grist(BUILD, 550).grist(AMETHYST, 120).grist(TAR, 50).grist(GARNET, 80).build(recipeSaver);
		
		GristCostRecipeBuilder.of(MSItems.CRUXITE_APPLE).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.CRUXITE_POTION).build(recipeSaver);
		
		GristCostRecipeBuilder.of(MSItems.CAT_CLAWS_DRAWN).grist(BUILD, 15).grist(RUST, 5).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.CAT_CLAWS_SHEATHED).grist(BUILD, 15).grist(RUST, 5).build(recipeSaver);
		
		GristCostRecipeBuilder.of(MSItems.CLAW_HAMMER).grist(BUILD, 8).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.SLEDGE_HAMMER).grist(BUILD, 10).grist(SHALE, 4).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.BLACKSMITH_HAMMER).grist(RUST, 8).grist(SULFUR, 9).grist(CAULK, 5).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.POGO_HAMMER).grist(BUILD, 20).grist(SHALE, 16).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.TELESCOPIC_SASSACRUSHER).grist(SHALE, 39).grist(TAR, 18).grist(MERCURY, 23).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.REGI_HAMMER).grist(AMETHYST, 25).grist(TAR, 70).grist(GOLD, 34).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.FEAR_NO_ANVIL).grist(BUILD, 999).grist(GARNET, 150).grist(DIAMOND, 54).grist(GOLD, 61).grist(QUARTZ, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.MELT_MASHER).grist(BUILD, 1000).grist(TAR, 400).grist(GARNET, 200).grist(DIAMOND, 340).grist(GOLD, 100).grist(RUBY, 150).grist(SULFUR, 450).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.ESTROGEN_EMPOWERED_EVERYTHING_ERADICATOR).grist(BUILD, 8000).grist(SHALE, 1280).grist(URANIUM, 640).grist(RUST, 300).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.EEEEEEEEEEEE).grist(ARTIFACT, -100).grist(BUILD, 1).build(recipeSaver);
		
		GristCostRecipeBuilder.of(MSItems.CACTACEAE_CUTLASS).grist(AMBER, 7).grist(MARBLE, 2).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.STEAK_SWORD).grist(IODINE, 55).grist(TAR, 18).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.BEEF_SWORD).grist(IODINE, 55).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.IRRADIATED_STEAK_SWORD).grist(IODINE, 55).grist(TAR, 10).grist(URANIUM, 8).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.SORD).grist(BUILD, 0).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.KATANA).grist(CHALK, 12).grist(QUARTZ, 10).grist(RUST, 6).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.FIRE_POKER).grist(AMBER, 41).grist(RUBY, 14).grist(SULFUR, 38).grist(GOLD, 3).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.CLAYMORE).grist(BUILD, 400).grist(RUST, 240).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.TOO_HOT_TO_HANDLE).grist(AMBER, 10).grist(RUBY, 15).grist(SULFUR, 10).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.REGISWORD).grist(AMETHYST, 27).grist(TAR, 62).grist(GOLD, 38).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.UNBREAKABLE_KATANA).grist(BUILD, 1100).grist(URANIUM, 63).grist(QUARTZ, 115).grist(RUBY, 54).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.COBALT_SABRE).grist(BUILD, 1300).grist(URANIUM, 90).grist(COBALT, 175).grist(DIAMOND, 30).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.QUANTUM_SABRE).grist(BUILD, 413).grist(URANIUM, 11).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.SHATTER_BEACON).grist(BUILD, 25).grist(COBALT, 15).grist(DIAMOND, 150).grist(URANIUM, 400).build(recipeSaver);
		
		GristCostRecipeBuilder.of(MSItems.WOODEN_SPOON).grist(BUILD, 5).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.SILVER_SPOON).grist(BUILD, 6).grist(MERCURY, 4).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.CROCKER_SPOON).grist(BUILD, 90).grist(IODINE, 34).grist(CHALK, 34).grist(RUBY, 6).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.CROCKER_FORK).grist(BUILD, 90).grist(IODINE, 34).grist(CHALK, 34).grist(RUBY, 6).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.SKAIA_FORK).grist(BUILD, 900).grist(QUARTZ, 94).grist(GOLD, 58).grist(AMETHYST, 63).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.GOLDEN_SPORK).grist(BUILD, 70).grist(GOLD, 40).grist(DIAMOND, 1).build(recipeSaver);
		
		GristCostRecipeBuilder.of(MSItems.BATLEACKS).grist(BUILD, 0).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.COPSE_CRUSHER).grist(BUILD, 25).grist(RUST, 15).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.BATTLEAXE).grist(BUILD, 400).grist(RUST, 240).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.BLACKSMITH_BANE).grist(BUILD, 30).grist(RUST, 15).grist(TAR, 12).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.SCRAXE).grist(BUILD, 139).grist(TAR, 86).grist(RUST, 43).grist(RUBY, 8).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.PISTON_POWERED_POGO_AXEHAMMER).grist(BUILD, 150).grist(SHALE, 64).grist(RUST, 15).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.RUBY_CROAK).grist(BUILD, 900).grist(GARNET, 103).grist(RUBY, 64).grist(DIAMOND, 16).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.HEPHAESTUS_LUMBERJACK).grist(BUILD, 625).grist(GOLD, 49).grist(RUBY, 36).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.FISSION_FOCUSED_FAULT_FELLER).grist(BUILD, 800).grist(SHALE, 128).grist(URANIUM, 64).grist(RUST, 30).build(recipeSaver);
		
		GristCostRecipeBuilder.of(MSItems.REGISICKLE).grist(AMETHYST, 25).grist(TAR, 57).grist(GOLD, 33).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.SICKLE).grist(BUILD, 8).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.HOMES_SMELL_YA_LATER).grist(BUILD, 34).grist(AMBER, 19).grist(AMETHYST, 10).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.FUDGESICKLE).grist(IODINE, 23).grist(AMBER, 15).grist(CHALK, 12).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.CLAW_OF_NRUBYIGLITH).grist(BUILD, 333).grist(AMETHYST, 80).grist(CHALK, 6).grist(GARNET, 6).grist(SHALE, 6).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.CANDY_SICKLE).grist(IODINE, 65).grist(GOLD, 38).grist(CHALK, 53).grist(AMBER, 20).build(recipeSaver);
		
		GristCostRecipeBuilder.of(MSItems.NIGHT_CLUB).grist(TAR, 28).grist(SHALE, 19).grist(COBALT, 6).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.POGO_CLUB).grist(BUILD, 15).grist(SHALE, 12).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.METAL_BAT).grist(BUILD, 35).grist(MERCURY, 23).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.SPIKED_CLUB).grist(BUILD, 46).grist(GARNET, 38).grist(IODINE, 13).build(recipeSaver);
		
		GristCostRecipeBuilder.of(MSItems.SPEAR_CANE).grist(BUILD, 28).grist(MERCURY, 14).grist(AMBER, 11).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.PARADISES_PORTABELLO).grist(BUILD, 40).grist(IODINE, 30).grist(RUBY, 20).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.REGI_CANE).grist(AMETHYST, 30).grist(TAR, 55).grist(GOLD, 32).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.POGO_CANE).grist(BUILD, 18).grist(SHALE, 14).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.URANIUM_POWERED_STICK).grist(URANIUM, 1).build(recipeSaver);
		
		GristCostRecipeBuilder.of(MSItems.CAPTCHAROID_CAMERA).grist(BUILD, 5000).grist(CAULK, 500).grist(GOLD, 500).grist(MARBLE, 500).grist(MERCURY, 500).grist(SHALE, 500).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.TRANSPORTALIZER).grist(BUILD, 350).grist(AMETHYST, 27).grist(RUST, 36).grist(URANIUM, 18).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.QUEUESTACK_MODUS_CARD).grist(BUILD, 140).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.TREE_MODUS_CARD).grist(BUILD, 400).grist(AMBER, 35).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.HASHMAP_MODUS_CARD).grist(BUILD, 280).grist(RUBY, 23).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.SET_MODUS_CARD).grist(BUILD, 350).grist(MERCURY, 29).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.IRON_BOAT).grist(RUST, 30).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.GOLD_BOAT).grist(GOLD, 30).build(recipeSaver);
		
		GristCostRecipeBuilder.of(MSBlocks.GLOWING_MUSHROOM).grist(BUILD, 5).grist(SHALE, 3).grist(MERCURY, 2).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.GLOWING_LOG).grist(BUILD, 8).grist(AMBER, 4).grist(MERCURY, 4).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.GLOWING_PLANKS).grist(BUILD, 2).grist(AMBER, 1).grist(MERCURY, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.GLOWY_GOOP).grist(BUILD, 8).grist(CAULK, 8).grist(MERCURY, 4).build(recipeSaver);
		
		GristCostRecipeBuilder.of(MSBlocks.PETRIFIED_LOG).grist(BUILD, 4).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.FROST_PLANKS).grist(BUILD, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.PETRIFIED_GRASS).grist(BUILD, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.PETRIFIED_POPPY).grist(BUILD, 1).grist(IODINE, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.BLOOMING_CACTUS).grist(AMBER, 1).grist(IODINE, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.GOLD_SEEDS).grist(GOLD, 3).build(recipeSaver);
		
		GristCostRecipeBuilder.of(MSItems.EMERALD_SWORD).grist(QUARTZ, 44).grist(DIAMOND, 76).grist(RUBY, 72).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.EMERALD_AXE).grist(AMBER, 40).grist(DIAMOND, 73).grist(RUBY, 70).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.EMERALD_PICKAXE).grist(RUST, 42).grist(DIAMOND, 72).grist(RUBY, 70).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.EMERALD_SHOVEL).grist(CHALK, 40).grist(DIAMOND, 70).grist(RUBY, 66).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.EMERALD_HOE).grist(IODINE, 32).grist(DIAMOND, 50).grist(RUBY, 45).build(recipeSaver);
		
		GristCostRecipeBuilder.of(MSItems.PRISMARINE_HELMET).grist(BUILD, 75).grist(COBALT, 30).grist(MARBLE, 15).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.PRISMARINE_CHESTPLATE).grist(BUILD, 120).grist(COBALT, 48).grist(MARBLE, 24).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.PRISMARINE_LEGGINGS).grist(BUILD, 105).grist(COBALT, 42).grist(MARBLE, 21).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.PRISMARINE_BOOTS).grist(BUILD, 60).grist(COBALT, 24).grist(MARBLE, 12).build(recipeSaver);
		
		GristCostRecipeBuilder.of(MSBlocks.GLOWYSTONE_DUST).grist(BUILD, 1).grist(AMBER, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.RAW_CRUXITE).grist(BUILD, 3).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.RAW_URANIUM).grist(URANIUM, 3).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.GOLDEN_GRASSHOPPER).grist(GOLD, 4000).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.BUG_NET).grist(BUILD, 40).grist(CHALK, 25).build(recipeSaver);
		
		GristCostRecipeBuilder.of(MSItems.SPORK).grist(BUILD, 13).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.CANDY_CORN).grist(CHALK, 1).grist(SULFUR, 1).grist(IODINE, 1).build(recipeSaver);
		for(GristType type : GristTypes.values())
		{
			if(type.getRegistryName().getNamespace().equals(Minestuck.MOD_ID))
				GristCostRecipeBuilder.of(type.getCandyItem().getItem()).grist(type, 3).build(recipeSaver);
		}
		GristCostRecipeBuilder.of(MSItems.TAB).grist(COBALT, 1).grist(IODINE, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.ORANGE_FAYGO).grist(COBALT, 1).grist(AMBER, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.CANDY_APPLE_FAYGO).grist(COBALT, 1).grist(SHALE, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.FAYGO_COLA).grist(COBALT, 1).grist(IODINE, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.COTTON_CANDY_FAYGO).grist(COBALT, 1).grist(AMETHYST, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.CREME_SODA_FAYGO).grist(COBALT, 1).grist(CHALK, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.GRAPE_FAYGO).grist(COBALT, 1).grist(SHALE, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.MOON_MIST_FAYGO).grist(COBALT, 1).grist(AMBER, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.PEACH_FAYGO).grist(COBALT, 1).grist(GARNET, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.REDPOP_FAYGO).grist(COBALT, 1).grist(SULFUR, 1).build(recipeSaver);
		
		GristCostRecipeBuilder.of(MSItems.SALAD).grist(BUILD, 1).grist(IODINE, 3).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.BUG_ON_A_STICK).grist(BUILD, 1).grist(CHALK, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.CHOCOLATE_BEETLE).grist(CHALK, 2).grist(IODINE, 4).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.CONE_OF_FLIES).grist(BUILD, 2).grist(AMBER, 2).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.GRASSHOPPER).grist(IODINE, 3).grist(AMBER, 7).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.JAR_OF_BUGS).grist(BUILD, 5).grist(CHALK, 3).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.ONION).grist(IODINE, 3).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.IRRADIATED_STEAK).grist(IODINE, 12).grist(URANIUM, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.ROCK_COOKIE).grist(BUILD, 10).grist(MARBLE, 5).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.STRAWBERRY_CHUNK).grist(AMBER, 2).grist(BUILD, 1).grist(RUBY, 2).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.DESERT_FRUIT).grist(AMBER, 1).grist(CAULK, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.FUNGAL_SPORE).grist(IODINE, 2).grist(AMBER, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.SPOREO).grist(IODINE, 3).grist(AMBER, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.MOREL_MUSHROOM).grist(IODINE, 20).grist(AMBER, 12).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.FRENCH_FRY).grist(BUILD, 1).grist(IODINE, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.SURPRISE_EMBRYO).grist(AMBER, 15).grist(IODINE, 5).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.UNKNOWABLE_EGG).grist(AMBER, 15).grist(AMETHYST, 15).grist(TAR, 1).build(recipeSaver);
		
		GristCostRecipeBuilder.of(MSBlocks.PRIMED_TNT).grist(BUILD, 8).grist(CHALK, 10).grist(SULFUR, 14).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.UNSTABLE_TNT).grist(BUILD, 5).grist(CHALK, 11).grist(SULFUR, 15).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.INSTANT_TNT).grist(BUILD, 6).grist(CHALK, 11).grist(SULFUR, 17).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.STONE_EXPLOSIVE_BUTTON).grist(BUILD, 7).grist(CHALK, 5).grist(SULFUR, 8).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.WOODEN_EXPLOSIVE_BUTTON).grist(BUILD, 7).grist(CHALK, 5).grist(SULFUR, 8).build(recipeSaver);
		
		GristCostRecipeBuilder.of(MSBlocks.COARSE_STONE).grist(BUILD, 4).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.CHISELED_COARSE_STONE).grist(BUILD, 4).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.SHADE_BRICKS).grist(BUILD, 2).grist(SHALE, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.SMOOTH_SHADE_STONE).grist(BUILD, 2).grist(SHALE, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.FROST_BRICKS).grist(BUILD, 2).grist(COBALT, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.FROST_TILE).grist(BUILD, 2).grist(COBALT, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.CHISELED_FROST_BRICKS).grist(BUILD, 3).grist(COBALT, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.CAST_IRON).grist(BUILD, 3).grist(RUST, 2).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.CHISELED_CAST_IRON).grist(BUILD, 3).grist(RUST, 2).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.COARSE_END_STONE).grist(BUILD, 4).grist(CAULK, 3).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.END_GRASS).grist(BUILD, 4).grist(CAULK, 3).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.FLOWERY_VINE_LOG).grist(BUILD, 7).grist(IODINE, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.FROST_LOG).grist(BUILD, 7).grist(COBALT, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.FLOWERY_MOSSY_STONE_BRICKS).grist(BUILD, 7).grist(AMBER, 1).grist(IODINE, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.FLOWERY_MOSSY_COBBLESTONE).grist(BUILD, 7).grist(AMBER, 1).grist(IODINE, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.TREATED_PLANKS).grist(BUILD, 2).build(recipeSaver);
		
		GristCostRecipeBuilder.of(MSItems.SBAHJ_POSTER).grist(BUILD, 4).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.CREW_POSTER).grist(TAR, 3).grist(RUST, 2).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.THRESH_DVD).grist(IODINE, 3).grist(AMETHYST, 2).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.GAMEBRO_MAGAZINE).grist(CHALK, 3).grist(GARNET, 2).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.GAMEGRL_MAGAZINE).grist(CHALK, 3).grist(AMBER, 2).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.CARVING_TOOL).grist(BUILD, 10).grist(RUST, 2).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.CRUMPLY_HAT).grist(BUILD, 20).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.MINI_FROG_STATUE).grist(BUILD, 30).build(recipeSaver);
		
		GristCostRecipeBuilder.of(MSBlocks.WOODEN_CACTUS).grist(BUILD, 7).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.BLUE_CAKE).grist(SHALE, 24).grist(MERCURY, 6).grist(COBALT, 5).grist(DIAMOND, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.COLD_CAKE).grist(COBALT, 15).grist(MARBLE, 12).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.RED_CAKE).grist(RUST, 20).grist(CHALK, 9).grist(IODINE, 6).grist(GARNET, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.HOT_CAKE).grist(SULFUR, 17).grist(IODINE, 10).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.REVERSE_CAKE).grist(AMBER, 10).grist(CHALK, 24).grist(IODINE, 11).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.FUCHSIA_CAKE).grist(AMETHYST, 85).grist(GARNET, 54).grist(IODINE, 40).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.CRUXTRUDER_LID).grist(BUILD, 8).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.BLENDER).grist(BUILD, 16).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.VEIN).grist(GARNET, 12).grist(IODINE, 8).build(recipeSaver);
		
		ContainerGristCostBuilder.of(MSItems.OIL_BUCKET).grist(TAR, 8).grist(SHALE, 8).build(recipeSaver);
		ContainerGristCostBuilder.of(MSItems.BLOOD_BUCKET).grist(GARNET, 8).grist(IODINE, 8).build(recipeSaver);
		ContainerGristCostBuilder.of(MSItems.BRAIN_JUICE_BUCKET).grist(AMETHYST, 8).grist(CHALK, 8).build(recipeSaver);
		ContainerGristCostBuilder.of(MSItems.WATER_COLORS_BUCKET).grist(AMETHYST, 4).grist(CHALK, 4).grist(GARNET, 4).grist(AMBER, 4).build(recipeSaver);
		ContainerGristCostBuilder.of(MSItems.ENDER_BUCKET).grist(MERCURY, 8).grist(URANIUM, 8).build(recipeSaver);
		ContainerGristCostBuilder.of(MSItems.OBSIDIAN_BUCKET).grist(BUILD, 4).grist(COBALT, 8).grist(TAR, 16).build(recipeSaver);
		
		WildcardGristCostBuilder.of(MSItems.CAPTCHA_CARD).cost(1).build(recipeSaver);
		
		
		GristCostRecipeBuilder.of(ExtraForgeTags.Items.COPPER_INGOTS).grist(RUST, 16).grist(COBALT, 3).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(ExtraForgeTags.Items.TIN_INGOTS).grist(RUST, 12).grist(CAULK, 8).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(ExtraForgeTags.Items.SILVER_INGOTS).grist(RUST, 12).grist(MERCURY, 8).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(ExtraForgeTags.Items.LEAD_INGOTS).grist(RUST, 12).grist(COBALT, 4).grist(SHALE, 4).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(ExtraForgeTags.Items.NICKEL_INGOTS).grist(RUST, 12).grist(SULFUR, 8).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(ExtraForgeTags.Items.INVAR_INGOTS).grist(RUST, 12).grist(SULFUR, 5).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(ExtraForgeTags.Items.ALUMINIUM_INGOTS).grist(RUST, 12).grist(CHALK, 6).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(ExtraForgeTags.Items.COBALT_INGOTS).grist(COBALT, 18).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(ExtraForgeTags.Items.ARDITE_INGOTS).grist(GARNET, 12).grist(SULFUR, 8).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(ExtraForgeTags.Items.RED_ALLOY_INGOTS).grist(RUST, 18).grist(GARNET, 32).buildFor(recipeSaver, Minestuck.MOD_ID);
		
		oreCost(ExtraForgeTags.Items.COPPER_ORES, ExtraForgeTags.Items.COPPER_INGOTS, 1, recipeSaver, Minestuck.MOD_ID);
		oreCost(ExtraForgeTags.Items.TIN_ORES, ExtraForgeTags.Items.TIN_INGOTS, 1, recipeSaver, Minestuck.MOD_ID);
		oreCost(ExtraForgeTags.Items.SILVER_ORES, ExtraForgeTags.Items.SILVER_INGOTS, 1, recipeSaver, Minestuck.MOD_ID);
		oreCost(ExtraForgeTags.Items.LEAD_ORES, ExtraForgeTags.Items.LEAD_INGOTS, 1, recipeSaver, Minestuck.MOD_ID);
		oreCost(ExtraForgeTags.Items.NICKEL_ORES, ExtraForgeTags.Items.NICKEL_INGOTS, 1, recipeSaver, Minestuck.MOD_ID);
		oreCost(ExtraForgeTags.Items.ALUMINIUM_ORES, ExtraForgeTags.Items.ALUMINIUM_INGOTS, 1, recipeSaver, Minestuck.MOD_ID);
		oreCost(ExtraForgeTags.Items.COBALT_ORES, ExtraForgeTags.Items.COBALT_INGOTS, 1, recipeSaver, Minestuck.MOD_ID);
		oreCost(ExtraForgeTags.Items.ARDITE_ORES, ExtraForgeTags.Items.ARDITE_INGOTS, 1, recipeSaver, Minestuck.MOD_ID);
	}
	
	public static void oreCost(Tag<Item> ores, Tag<Item> material, float multiplier, Consumer<IFinishedRecipe> recipeSaver, String modId)
	{
		TagSourceGristCostBuilder.of(ores).source(material).multiplier(multiplier).grist(BUILD, 4).buildFor(recipeSaver, modId);
	}
	
	@Override
	public String getName()
	{
		return "Minestuck Grist Costs";
	}
}