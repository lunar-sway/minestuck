package com.mraof.minestuck.data.recipe;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.item.crafting.alchemy.GristType;
import com.mraof.minestuck.item.crafting.alchemy.GristTypes;
import com.mraof.minestuck.util.ExtraForgeTags;
import com.mraof.minestuck.util.MSTags;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
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
	protected void buildShapelessRecipes(Consumer<IFinishedRecipe> recipeSaver)
	{
		GeneratedGristCostBuilder.create().build(recipeSaver, new ResourceLocation(Minestuck.MOD_ID, "generated"));
		
		//Stone
		GristCostRecipeBuilder.of(Tags.Items.STONE).grist(BUILD, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Tags.Items.COBBLESTONE).grist(BUILD, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.STONE_BRICKS).grist(BUILD, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.CRACKED_STONE_BRICKS).grist(BUILD, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.GRAVEL).grist(BUILD, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(ItemTags.SAND).grist(BUILD, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.GLASS).grist(BUILD, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		SourceGristCostBuilder.of(ExtraForgeTags.Items.TERRACOTTA).source(Items.CLAY).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.CLAY_BALL).grist(SHALE, 1).grist(BUILD, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.FLINT).grist(BUILD, 4).buildFor(recipeSaver, Minestuck.MOD_ID);
		
		//Ore
		oreCost(Tags.Items.ORES_COAL, Items.COAL, 1, recipeSaver, Minestuck.MOD_ID);
		oreCost(Tags.Items.ORES_IRON, Items.IRON_INGOT, 1, recipeSaver, Minestuck.MOD_ID);
		oreCost(Tags.Items.ORES_GOLD, Items.GOLD_INGOT, 1, recipeSaver, Minestuck.MOD_ID);
		oreCost(Tags.Items.ORES_REDSTONE, Items.REDSTONE, 4, recipeSaver, Minestuck.MOD_ID);
		oreCost(Tags.Items.ORES_LAPIS, Items.LAPIS_LAZULI, 4, recipeSaver, Minestuck.MOD_ID);
		oreCost(Tags.Items.ORES_DIAMOND, Items.DIAMOND, 1, recipeSaver, Minestuck.MOD_ID);
		oreCost(Tags.Items.ORES_EMERALD, Items.EMERALD, 1, recipeSaver, Minestuck.MOD_ID);
		oreCost(Tags.Items.ORES_QUARTZ, Items.QUARTZ, 2, recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.COAL).grist(TAR, 8).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.CHARCOAL).grist(TAR, 6).grist(AMBER, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.GOLD_INGOT).grist(GOLD, 9).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.IRON_INGOT).grist(RUST, 9).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.REDSTONE).grist(GARNET, 4).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.QUARTZ).grist(QUARTZ, 4).grist(MARBLE, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.EMERALD).grist(RUBY, 9).grist(DIAMOND, 9).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.DIAMOND).grist(DIAMOND, 18).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.LAPIS_LAZULI).grist(AMETHYST, 4).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.ANCIENT_DEBRIS).grist(RUST, 24).grist(SULFUR, 48).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.NETHERITE_SCRAP).grist(RUST, 24).grist(SULFUR, 48).buildFor(recipeSaver, Minestuck.MOD_ID);
		
		//Dirt
		GristCostRecipeBuilder.of(Items.DIRT).grist(BUILD, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.GRASS_BLOCK).grist(BUILD, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.PODZOL).grist(BUILD, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.MYCELIUM).grist(IODINE, 2).grist(RUBY, 2).grist(BUILD, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		
		//Snow/ice
		GristCostRecipeBuilder.of(Items.SNOWBALL).grist(COBALT, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.SNOW).grist(COBALT, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.ICE).grist(COBALT, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.PACKED_ICE).grist(BUILD, 10).grist(COBALT, 6).buildFor(recipeSaver, Minestuck.MOD_ID);
		
		GristCostRecipeBuilder.of(ItemTags.LOGS).grist(BUILD, 8).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(ItemTags.LEAVES).grist(BUILD, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(ItemTags.SAPLINGS).grist(BUILD, 16).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(ItemTags.PLANKS).grist(BUILD, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.DEAD_BUSH).grist(AMBER, 2).grist(SULFUR, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.NETHERRACK).grist(BUILD, 1).grist(TAR, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.OBSIDIAN).grist(BUILD, 6).grist(COBALT, 6).grist(TAR, 16).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.PUMPKIN).grist(AMBER, 12).grist(CAULK, 6).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.BAMBOO).grist(BUILD, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.MUSHROOM_STEM).grist(BUILD, 4).grist(IODINE, 3).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.BROWN_MUSHROOM_BLOCK).grist(BUILD, 2).grist(IODINE, 5).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.RED_MUSHROOM_BLOCK).grist(BUILD, 2).grist(IODINE, 3).grist(RUBY, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.COBWEB).grist(BUILD, 18).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.TORCH).grist(BUILD, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		
		//Underwater
		GristCostRecipeBuilder.of(Items.SPONGE).grist(AMBER, 20).grist(SULFUR, 30).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.WET_SPONGE).grist(AMBER, 20).grist(SULFUR, 30).grist(COBALT, 10).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.PRISMARINE_CRYSTALS).grist(COBALT, 2).grist(DIAMOND, 2).grist(AMETHYST, 4).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.PRISMARINE_SHARD).grist(COBALT, 1).grist(BUILD, 3).buildFor(recipeSaver, Minestuck.MOD_ID);
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
		GristCostRecipeBuilder.of(Items.ELYTRA).grist(DIAMOND, 64).grist(SULFUR, 112).grist(CAULK, 135).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.CHORUS_FRUIT).grist(AMETHYST, 2).grist(SHALE, 4).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.POPPED_CHORUS_FRUIT).grist(AMETHYST, 2).grist(SHALE, 4).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.CHORUS_FLOWER).grist(BUILD, 26).grist(AMETHYST, 23).grist(SHALE, 10).buildFor(recipeSaver, Minestuck.MOD_ID);
		
		//Organic
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
		GristCostRecipeBuilder.of(Items.RED_MUSHROOM).grist(IODINE, 3).grist(RUBY, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
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
		GristCostRecipeBuilder.of(Items.HONEYCOMB).grist(BUILD, 5).grist(AMBER, 1).grist(GOLD, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.HONEY_BOTTLE).grist(BUILD, 5).grist(AMBER, 1).grist(GOLD, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.BEETROOT).grist(RUST, 1).grist(CHALK, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.BEETROOT_SEEDS).grist(RUST, 2).grist(IODINE, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.APPLE).grist(AMBER, 2).grist(SHALE, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.SWEET_BERRIES).grist(AMBER, 3).grist(IODINE, 2).grist(SHALE, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.CARROT).grist(AMBER, 3).grist(CHALK, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.POTATO).grist(AMBER, 4).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.POISONOUS_POTATO).grist(AMBER, 4).grist(IODINE, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
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
		SourceGristCostBuilder.of(Items.COD_BUCKET).source(Items.COD).source(Items.WATER_BUCKET).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.SALMON).grist(CAULK, 4).grist(AMBER, 4).grist(COBALT, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		SourceGristCostBuilder.of(Items.SALMON_BUCKET).source(Items.SALMON).source(Items.WATER_BUCKET).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.TROPICAL_FISH).grist(CAULK, 4).grist(AMBER, 4).grist(COBALT, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		SourceGristCostBuilder.of(Items.TROPICAL_FISH_BUCKET).source(Items.TROPICAL_FISH).source(Items.WATER_BUCKET).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.PUFFERFISH).grist(IODINE, 2).grist(CAULK, 4).grist(AMBER, 6).grist(COBALT, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		SourceGristCostBuilder.of(Items.PUFFERFISH_BUCKET).source(Items.PUFFERFISH).source(Items.WATER_BUCKET).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.MUTTON).grist(IODINE, 10).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.RABBIT).grist(IODINE, 8).buildFor(recipeSaver, Minestuck.MOD_ID);
		
		//Monster drops
		GristCostRecipeBuilder.of(Items.ROTTEN_FLESH).grist(RUST, 1).grist(IODINE, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.BONE).grist(CHALK, 6).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.STRING).grist(CHALK, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
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
		GristCostRecipeBuilder.of(Items.SOUL_SOIL).grist(BUILD, 2).grist(SULFUR, 5).grist(CAULK, 3).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.BLACKSTONE).grist(BUILD, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.GILDED_BLACKSTONE).grist(BUILD, 2).grist(GOLD, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.BASALT).grist(BUILD, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.BLAZE_ROD).grist(TAR, 20).grist(URANIUM, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.GHAST_TEAR).grist(COBALT, 10).grist(CHALK, 19).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.GLOWSTONE_DUST).grist(TAR, 4).grist(CHALK, 6).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.NETHER_WART).grist(IODINE, 3).grist(TAR, 5).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.WARPED_WART_BLOCK).grist(IODINE, 6).grist(TAR, 2).grist(MERCURY, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.CRIMSON_ROOTS).grist(SHALE, 3).grist(MERCURY, 4).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.WARPED_ROOTS).grist(CHALK, 3).grist(MERCURY, 4).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.NETHER_SPROUTS).grist(CHALK, 2).grist(MERCURY, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.CRIMSON_FUNGUS).grist(SHALE, 2).grist(MERCURY, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.WARPED_FUNGUS).grist(CHALK, 2).grist(MERCURY, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.SHROOMLIGHT).grist(AMBER, 8).grist(MERCURY, 10).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.WEEPING_VINES).grist(SHALE, 4).grist(MERCURY, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.TWISTING_VINES).grist(CHALK, 4).grist(MERCURY, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.CRYING_OBSIDIAN).grist(BUILD, 8).grist(COBALT, 8).grist(TAR, 20).grist(RUBY, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.CRIMSON_NYLIUM).grist(BUILD, 1).grist(SHALE, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.WARPED_NYLIUM).grist(BUILD, 1).grist(CHALK, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.NETHER_STAR).grist(URANIUM, 344).grist(TAR, 135).grist(DIAMOND, 92).buildFor(recipeSaver, Minestuck.MOD_ID);
		
		GristCostRecipeBuilder.of(Items.CHAINMAIL_HELMET).grist(RUST, 20).grist(MERCURY, 10).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.CHAINMAIL_CHESTPLATE).grist(RUST, 32).grist(MERCURY, 16).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.CHAINMAIL_LEGGINGS).grist(RUST, 28).grist(MERCURY, 14).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.CHAINMAIL_BOOTS).grist(RUST, 16).grist(MERCURY, 8).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.IRON_HORSE_ARMOR).grist(RUST, 40).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.GOLDEN_HORSE_ARMOR).grist(GOLD, 40).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.DIAMOND_HORSE_ARMOR).grist(DIAMOND, 80).buildFor(recipeSaver, Minestuck.MOD_ID);
		
		//Dyes
		GristCostRecipeBuilder.of(Items.RED_DYE).grist(GARNET, 4).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.YELLOW_DYE).grist(AMBER, 4).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.GREEN_DYE).grist(AMBER, 4).buildFor(recipeSaver, Minestuck.MOD_ID);
		
		//Concrete
		SourceGristCostBuilder.of(Items.WHITE_CONCRETE).source(Items.WHITE_CONCRETE_POWDER).grist(COBALT, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		SourceGristCostBuilder.of(Items.BLACK_CONCRETE).source(Items.BLACK_CONCRETE_POWDER).grist(COBALT, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		SourceGristCostBuilder.of(Items.BROWN_CONCRETE).source(Items.BROWN_CONCRETE_POWDER).grist(COBALT, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		SourceGristCostBuilder.of(Items.BLUE_CONCRETE).source(Items.BLUE_CONCRETE_POWDER).grist(COBALT, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		SourceGristCostBuilder.of(Items.RED_CONCRETE).source(Items.RED_CONCRETE_POWDER).grist(COBALT, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		SourceGristCostBuilder.of(Items.YELLOW_CONCRETE).source(Items.YELLOW_CONCRETE_POWDER).grist(COBALT, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		SourceGristCostBuilder.of(Items.GREEN_CONCRETE).source(Items.GREEN_CONCRETE_POWDER).grist(COBALT, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		SourceGristCostBuilder.of(Items.LIME_CONCRETE).source(Items.LIME_CONCRETE_POWDER).grist(COBALT, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		SourceGristCostBuilder.of(Items.LIGHT_BLUE_CONCRETE).source(Items.LIGHT_BLUE_CONCRETE_POWDER).grist(COBALT, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		SourceGristCostBuilder.of(Items.MAGENTA_CONCRETE).source(Items.MAGENTA_CONCRETE_POWDER).grist(COBALT, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		SourceGristCostBuilder.of(Items.ORANGE_CONCRETE).source(Items.ORANGE_CONCRETE_POWDER).grist(COBALT, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		SourceGristCostBuilder.of(Items.PURPLE_CONCRETE).source(Items.PURPLE_CONCRETE_POWDER).grist(COBALT, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		SourceGristCostBuilder.of(Items.CYAN_CONCRETE).source(Items.CYAN_CONCRETE_POWDER).grist(COBALT, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		SourceGristCostBuilder.of(Items.LIGHT_GRAY_CONCRETE).source(Items.LIGHT_GRAY_CONCRETE_POWDER).grist(COBALT, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		SourceGristCostBuilder.of(Items.GRAY_CONCRETE).source(Items.GRAY_CONCRETE_POWDER).grist(COBALT, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		SourceGristCostBuilder.of(Items.PINK_CONCRETE).source(Items.PINK_CONCRETE_POWDER).grist(COBALT, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		
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
		GristCostRecipeBuilder.of(Items.BELL).grist(BUILD, 5000).grist(GOLD, 50).grist(RUST, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.GLOBE_BANNER_PATTER).grist(BUILD, 100).grist(MARBLE, 15).grist(SHALE, 15).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.PIGLIN_BANNER_PATTERN).grist(BUILD, 120).grist(TAR, 17).grist(GOLD, 12).buildFor(recipeSaver, Minestuck.MOD_ID);
		
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
		GristCostRecipeBuilder.of(Items.MUSIC_DISC_PIGSTEP).grist(BUILD, 20).grist(CAULK, 12).grist(SULFUR, 7).grist(GOLD, 3).buildFor(recipeSaver, Minestuck.MOD_ID);
		
		//Liquid Buckets
		ContainerGristCostBuilder.of(Items.WATER_BUCKET).grist(COBALT, 4).buildFor(recipeSaver, Minestuck.MOD_ID);
		ContainerGristCostBuilder.of(Items.LAVA_BUCKET).grist(TAR, 16).buildFor(recipeSaver, Minestuck.MOD_ID);
		ContainerGristCostBuilder.of(Items.MILK_BUCKET).grist(CHALK, 8).buildFor(recipeSaver, Minestuck.MOD_ID);
		
		//------------------------MINESTUCK------------------------\\
		
		oreCost(MSTags.Items.URANIUM_ORES, MSItems.RAW_URANIUM, 1, recipeSaver, Minestuck.MOD_ID);
		oreCost(MSTags.Items.CRUXITE_ORES, MSItems.RAW_CRUXITE, 4, recipeSaver, Minestuck.MOD_ID);
		
		GristCostRecipeBuilder.of(MSBlocks.BLUE_DIRT).grist(BUILD, 1).grist(SHALE, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.THOUGHT_DIRT).grist(BUILD, 1).grist(CAULK, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.GRIST_WIDGET).grist(BUILD, 550).grist(GARNET, 55).grist(RUBY, 34).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.GENERIC_OBJECT).grist(BUILD, 2).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.CHESSBOARD).grist(SHALE, 25).grist(MARBLE, 25).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.GRIMOIRE).grist(BUILD, 120).grist(AMETHYST, 60).grist(GARNET, 33).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.LONG_FORGOTTEN_WARHORN).grist(BUILD, 550).grist(AMETHYST, 120).grist(TAR, 50).grist(GARNET, 80).build(recipeSaver);
		
		GristCostRecipeBuilder.of(MSItems.SBURB_CODE).grist(CHALK, 16).grist(IODINE, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(MSItems.COMPUTER_PARTS).grist(BUILD, 20).grist(RUST, 5).grist(GOLD, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.BLANK_DISK).grist(BUILD, 5).build(recipeSaver);
		
		GristCostRecipeBuilder.of(MSItems.CRUXITE_APPLE).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.CRUXITE_POTION).build(recipeSaver);
		
		GristCostRecipeBuilder.of(MSItems.FLUORITE_OCTET).grist(DIAMOND, 5600).grist(COBALT, 8).grist(CHALK, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.CAT_CLAWS_DRAWN).grist(BUILD, 105).grist(RUST, 9).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.SKELETONIZER_DRAWN).grist(BUILD, 93).grist(CHALK, 20).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.SKELETON_DISPLACER_DRAWN).grist(URANIUM, 209).grist(RUST, 314).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.TEARS_OF_THE_ENDERLICH_DRAWN).grist(QUARTZ, 1425).grist(RUST, 3205).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.LIPSTICK_CHAINSAW).grist(BUILD, 45).grist(MARBLE, 18).grist(SHALE, 30).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.LIPSTICK).grist(BUILD, 45).grist(MARBLE, 18).grist(SHALE, 30).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.THISTLEBLOWER).grist(SHALE, 56).grist(MERCURY, 45).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.EMERALD_IMMOLATOR).grist(BUILD, 198).grist(TAR, 99).grist(RUBY, 59).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.FROSTTOOTH).grist(BUILD, 291).grist(TAR, 145).grist(AMETHYST, 87).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.OBSIDIATOR).grist(BUILD, 405).grist(TAR, 405).grist(GARNET, 365).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.JOUSTING_LANCE).grist(RUST, 113).grist(QUARTZ, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.CIGARETTE_LANCE).grist(SHALE, 74).grist(TAR, 111).grist(DIAMOND, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.LUCERNE_HAMMER).grist(BUILD, 35).grist(RUST, 11).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.LUCERNE_HAMMER_OF_UNDYING).grist(BUILD, 836).grist(DIAMOND, 76).grist(CHALK, 190).grist(MARBLE, 152).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.TYPHONIC_TRIVIALIZER).grist(BUILD, 7860).grist(RUBY, 983).grist(DIAMOND, 524).grist(SHALE, 1310).grist(URANIUM, 5).build(recipeSaver);
		
		GristCostRecipeBuilder.of(MSItems.MAILBOX).grist(BUILD, 19).grist(MERCURY, 3).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.BLACKSMITH_HAMMER).grist(RUST, 11).grist(SULFUR, 15).grist(CAULK, 13).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.POGO_HAMMER).grist(BUILD, 152).grist(SHALE, 19).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.WRINKLEFUCKER).grist(BUILD, 238).grist(SHALE, 25).grist(TAR, 31).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.TELESCOPIC_SASSACRUSHER).grist(SHALE, 87).grist(TAR, 53).grist(MERCURY, 56).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.DEMOCRATIC_DEMOLITIONER).grist(BUILD, 50).grist(GOLD, 1).grist(MARBLE, 10).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.REGI_HAMMER).grist(AMETHYST, 32).grist(TAR, 77).grist(GOLD, 32).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.FEAR_NO_ANVIL).grist(BUILD, 5150).grist(GARNET, 3157).grist(DIAMOND, 206).grist(GOLD, 247).grist(QUARTZ, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.MELT_MASHER).grist(BUILD, 4566).grist(TAR, 913).grist(GARNET, 274).grist(DIAMOND, 310).grist(GOLD, 91).grist(SULFUR, 274).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.ESTROGEN_EMPOWERED_EVERYTHING_ERADICATOR).grist(BUILD, 1108).grist(SHALE, 92).grist(URANIUM, 17).grist(RUST, 13).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.EEEEEEEEEEEE).grist(ARTIFACT, -17).grist(BUILD, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.POPAMATIC_VRILLYHOO).grist(BUILD, 5500).grist(QUARTZ, 13200).grist(DIAMOND, 5500).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.SCARLET_ZILLYHOO).grist(BUILD, 2000).grist(RUBY, 3600).grist(DIAMOND, 1600).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.MWRTHWL).grist(BUILD, 4063).grist(GOLD, 3088).grist(RUST, 1950).build(recipeSaver);
		
		GristCostRecipeBuilder.of(MSItems.SORD).grist(BUILD, 0).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.PAPER_SWORD).grist(BUILD, 12).grist(TAR, 3).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.SWONGE).grist(IODINE, 35).grist(QUARTZ, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.PUMORD).grist(MARBLE, 125).grist(GARNET, 5).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.CACTACEAE_CUTLASS).grist(AMBER, 20).grist(MARBLE, 4).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.BEEF_SWORD).grist(IODINE, 22).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.IRRADIATED_STEAK_SWORD).grist(IODINE, 3).grist(TAR, 3).grist(URANIUM, 8).build(recipeSaver); //does not follow grist cost
		GristCostRecipeBuilder.of(MSItems.MACUAHUITL).grist(BUILD, 52).grist(COBALT, 31).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.FROSTY_MACUAHUITL).grist(BUILD, 69).grist(COBALT, 69).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.UNBREAKABLE_KATANA).grist(BUILD, 4375).grist(URANIUM, 6325).grist(QUARTZ, 1200).grist(RUBY, 600).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.ANGEL_APOCALYPSE).grist(BUILD, 1300).grist(AMBER, 25).grist(QUARTZ, 80).grist(GOLD, 30).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.FIRE_POKER).grist(AMBER, 75).grist(RUBY, 9).grist(SULFUR, 40).grist(GOLD, 4).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.TOO_HOT_TO_HANDLE).grist(AMBER, 40).grist(RUBY, 36).grist(SULFUR, 24).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.CALEDSCRATCH).grist(BUILD, 6000).grist(GARNET, 360).grist(MERCURY, 720).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.CALEDFWLCH).grist(BUILD, 6563).grist(GOLD, 4988).grist(RUST, 3150).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.ROYAL_DERINGER).grist(BUILD, 6150).grist(GOLD, 4715).grist(MARBLE, 4510).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.CLAYMORE).grist(BUILD, 210).grist(RUST, 45).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.REGISWORD).grist(AMETHYST, 32).grist(TAR, 117).grist(GOLD, 32).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.CRUEL_FATE_CRUCIBLE).grist(BUILD, 7500).grist(TAR, 5250).grist(COBALT, 400).grist(GOLD, 200).grist(QUARTZ, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.SCARLET_RIBBITAR).grist(BUILD, 1250).grist(RUBY, 1725).grist(DIAMOND, 400).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.DOGG_MACHETE).grist(BUILD, 400).grist(CHALK, 200).grist(MERCURY, 160).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.COBALT_SABRE).grist(BUILD, 850).grist(URANIUM, 34).grist(COBALT, 204).grist(DIAMOND, 34).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.QUANTUM_SABRE).grist(BUILD, 413).grist(URANIUM, 796).grist(CAULK, 1659).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.SHATTER_BEACON).grist(BUILD, 315).grist(COBALT, 294).grist(DIAMOND, 210).grist(URANIUM, 630).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.SHATTER_BACON).grist(BUILD, 50).grist(IODINE, 25).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.SUBTRACTSHUMIDIRE_ZOMORRODNEGATIVE).grist(BUILD, 666).grist(AMBER, 1167).grist(GARNET, 600).grist(DIAMOND, 333).build(recipeSaver);
		
		GristCostRecipeBuilder.of(MSItems.NIFE).grist(ARTIFACT, -2).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.LIGHT_OF_MY_KNIFE).grist(URANIUM, 43).grist(MERCURY, 85).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.STARSHARD_TRI_BLADE).grist(URANIUM, 474).grist(DIAMOND, 474).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.TOOTHRIPPER).grist(RUST, 73).grist(CAULK, 121).build(recipeSaver);
		
		GristCostRecipeBuilder.of(MSItems.SILVER_SPOON).grist(BUILD, 10).grist(MERCURY, 8).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.MELONBALLER).grist(BUILD, 53).grist(RUBY, 16).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.SIGHTSEEKER).grist(BUILD, 53).grist(RUST, 16).grist(CAULK, 27).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.TERRAIN_FLATENATOR).grist(BUILD, 170).grist(RUST, 4).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.NOSFERATU_SPOON).grist(BUILD, 380).grist(MERCURY, 15).grist(AMETHYST, 46).grist(GARNET, 34).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.CROCKER_SPOON).grist(BUILD, 850).grist(IODINE, 170).grist(CHALK, 170).grist(RUBY, 51).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.CROCKER_FORK).grist(BUILD, 850).grist(IODINE, 170).grist(CHALK, 170).grist(RUBY, 51).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.SKAIA_FORK).grist(BUILD, 225).grist(QUARTZ, 364).grist(GOLD, 58).grist(AMETHYST, 63).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.CANDY_FORK).grist(BUILD, 42).grist(IODINE, 18).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.TUNING_FORK).grist(BUILD, 27).grist(RUST, 16).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.ELECTRIC_FORK).grist(BUILD, 180).grist(RUST, 122).grist(MERCURY, 144).grist(URANIUM, 63).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.EATING_FORK_GEM).grist(BUILD, 99).grist(AMBER, 7).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.DEVIL_FORK).grist(BUILD, 666).grist(SULFUR, 821).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.GOLDEN_SPORK).grist(BUILD, 19).grist(GOLD, 11).grist(DIAMOND, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.BIDENT).grist(BUILD, 239).grist(GARNET, 11).grist(AMETHYST, 11).grist(QUARTZ, 8).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.EDISONS_FURY).grist(RUST, 280).grist(GARNET, 240).grist(MERCURY, 267).grist(URANIUM, 27).grist(TAR, 67).build(recipeSaver);
		
		GristCostRecipeBuilder.of(MSItems.BATLEACKS).grist(BUILD, 0).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.COPSE_CRUSHER).grist(BUILD, 62).grist(RUST, 28).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.QUENCH_CRUSHER).grist(BUILD, 134).grist(IODINE, 15).grist(CAULK, 15).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.MELONSBANE).grist(BUILD, 216).grist(IODINE, 24).grist(CAULK, 24).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.CROP_CHOP).grist(BUILD, 78).grist(IODINE, 39).grist(AMBER, 22).grist(GOLD, 9).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.THE_LAST_STRAW).grist(BUILD, 180).grist(GOLD, 36).grist(IODINE, 90).grist(AMBER, 45).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.BATTLEAXE).grist(BUILD, 272).grist(RUST, 122).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.CANDY_BATTLEAXE).grist(IODINE, 93).grist(GOLD, 19).grist(CHALK, 47).grist(AMBER, 16).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.CHOCO_LOCO_WOODSPLITTER).grist(BUILD, 100).grist(IODINE, 75).grist(GOLD, 25).grist(CHALK, 38).grist(AMBER, 25).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.STEEL_EDGE_CANDYCUTTER).grist(BUILD, 400).grist(RUST, 180).grist(IODINE, 250).grist(GOLD, 60).grist(CHALK, 100).grist(AMBER, 50).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.BLACKSMITH_BANE).grist(BUILD, 82).grist(RUST, 49).grist(TAR, 82).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.REGIAXE).grist(AMETHYST, 32).grist(TAR, 92).grist(GOLD, 32).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.GOTHY_AXE).grist(RUST, 77).grist(TAR, 85).grist(RUBY, 77).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.SURPRISE_AXE).grist(BUILD, 159).grist(RUST, 64).grist(AMBER, 53).grist(IODINE, 27).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.SHOCK_AXE).grist(BUILD, 675).grist(RUST, 270).grist(GOLD, 135).grist(AMBER, 113).grist(URANIUM, 45).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.SCRAXE).grist(BUILD, 272).grist(TAR, 102).grist(RUST, 41).grist(RUBY, 20).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.LORENTZ_DISTRANSFORMATIONER).grist(BUILD, 640).grist(URANIUM, 96).grist(TAR, 160).grist(RUST, 48).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.PISTON_POWERED_POGO_AXEHAMMER).grist(BUILD, 304).grist(SHALE, 152).grist(RUST, 46).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.RUBY_CROAK).grist(BUILD, 2800).grist(GARNET, 630).grist(RUBY, 420).grist(DIAMOND, 140).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.HEPHAESTUS_LUMBERJACK).grist(BUILD, 2150).grist(GOLD, 258).grist(RUBY, 258).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.FISSION_FOCUSED_FAULT_FELLER).grist(BUILD, 1360).grist(SHALE, 510).grist(URANIUM, 136).grist(RUST, 102).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.BISECTOR).grist(BUILD, 239).grist(GARNET, 81).grist(AMETHYST, 81).grist(QUARTZ, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.FINE_CHINA_AXE).grist(MARBLE, 269).grist(SHALE, 84).build(recipeSaver);
		
		GristCostRecipeBuilder.of(MSItems.BISICKLE).grist(BUILD, 35).grist(RUST, 11).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.OW_THE_EDGE).grist(ARTIFACT, -1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.THORNY_SUBJECT).grist(BUILD, 87).grist(AMETHYST, 9).grist(RUBY, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.HOMES_SMELL_YA_LATER).grist(BUILD, 88).grist(AMBER, 44).grist(AMETHYST, 13).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.HEMEOREAPER).grist(BUILD, 195).grist(RUST, 23).grist(GARNET, 23).grist(IODINE, 20).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.FUDGESICKLE).grist(IODINE, 30).grist(AMBER, 25).grist(CHALK, 20).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.REGISICKLE).grist(AMETHYST, 32).grist(TAR, 77).grist(GOLD, 32).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.HERETICUS_AURURM).grist(GOLD, 240).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.CLAW_SICKLE).grist(BUILD, 264).grist(IODINE, 116).grist(CHALK, 66).grist(GARNET, 10).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.CLAW_OF_NRUBYIGLITH).grist(BUILD, 1056).grist(AMETHYST, 264).grist(CHALK, 44).grist(GARNET, 26).grist(SHALE, 44).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.CANDY_SICKLE).grist(IODINE, 45).grist(GOLD, 12).grist(CHALK, 45).grist(AMBER, 15).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.SCYTHE).grist(BUILD, 56).grist(RUST, 25).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.EIGHTBALL_SCYTHE).grist(BUILD, 715).grist(TAR, 743).build(recipeSaver);
		
		GristCostRecipeBuilder.of(MSItems.STALE_BAGUETTE).grist(IODINE, 6).grist(AMBER, 4).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.GLUB_CLUB).grist(BUILD, 10).grist(CAULK, 8).grist(AMBER, 3).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.NIGHT_CLUB).grist(TAR, 8).grist(SHALE, 8).grist(COBALT, 2).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.NIGHTSTICK).grist(TAR, 18).grist(MARBLE, 7).grist(GOLD, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.RED_EYES).grist(TAR, 45).grist(SULFUR, 45).grist(AMETHYST, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.PRISMARINE_BASHER).grist(BUILD, 27).grist(COBALT, 18).grist(MARBLE, 7).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.CLUB_ZERO).grist(BUILD, 44).grist(COBALT, 26).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.POGO_CLUB).grist(BUILD, 60).grist(SHALE, 35).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.BARBER_BASHER).grist(BUILD, 53).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.METAL_BAT).grist(BUILD, 36).grist(MERCURY, 22).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.CLOWN_CLUB).grist(BUILD, 356).grist(MERCURY, 71).grist(COBALT, 71).grist(IODINE, 89).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.MACE).grist(BUILD, 90).grist(RUST, 15).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.M_ACE).grist(BUILD, 135).grist(MARBLE, 18).grist(CHALK, 30).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.DESOLATOR_MACE).grist(BUILD, 595).grist(RUST, 102).grist(TAR, 170).grist(MARBLE, 170).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.BLAZING_GLORY).grist(BUILD, 470).grist(SULFUR, 282).grist(GOLD, 5).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.SPIKED_CLUB).grist(BUILD, 40).grist(GARNET, 2).grist(IODINE, 3).build(recipeSaver);
		
		GristCostRecipeBuilder.of(MSItems.HORSE_HITCHER).grist(BUILD, 1080).grist(QUARTZ, 48).grist(URANIUM, 3).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.CLUB_OF_FELONY).grist(BUILD, 1080).grist(COBALT, 48).grist(URANIUM, 3).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.CUESTICK).grist(BUILD, 1080).grist(MERCURY, 48).grist(URANIUM, 3).build(recipeSaver);
		
		GristCostRecipeBuilder.of(MSItems.VAUDEVILLE_HOOK).grist(IODINE, 6).grist(TAR, 4).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.UPPER_CRUST_CRUST_CANE).grist(BUILD, 12).grist(IODINE, 8).grist(AMBER, 6).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.ZEPHYR_CANE).grist(BUILD, 840).grist(SHALE, 180).grist(CAULK, 180).grist(AMBER, 120).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.SPEAR_CANE).grist(BUILD, 70).grist(MERCURY, 12).grist(AMBER, 10).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.PARADISES_PORTABELLO).grist(BUILD, 18).grist(IODINE, 5).grist(RUBY, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.REGI_CANE).grist(AMETHYST, 32).grist(TAR, 72).grist(GOLD, 32).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.POGO_CANE).grist(BUILD, 66).grist(SHALE, 22).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.URANIUM_POWERED_STICK).grist(URANIUM, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.CANDY_CANE).grist(IODINE, 24).grist(CHALK, 20).grist(AMBER, 16).build(recipeSaver); //recipe is slightly lower than what is should be for the sharp variety
		GristCostRecipeBuilder.of(MSItems.SHARP_CANDY_CANE).grist(IODINE, 24).grist(CHALK, 20).grist(AMBER, 16).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.PRIM_AND_PROPER_WALKING_POLE).grist(BUILD, 15).grist(IODINE, 10).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.LESS_PROPER_WALKING_STICK).grist(BUILD, 75).grist(RUST, 18).grist(IODINE, 8).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.ROCKEFELLERS_WALKING_BLADECANE).grist(BUILD, 413).grist(RUST, 222).grist(TAR, 22).grist(DIAMOND, 2).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.DRAGON_CANE).grist(BUILD, 7166).grist(RUBY, 666).grist(RUST, 111).grist(CHALK, 222).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.CHANCEWYRMS_EXTRA_FORTUNATE_STABBING_IMPLEMENT).grist(BUILD, 41300).grist(COBALT, 22254).build(recipeSaver);
		
		GristCostRecipeBuilder.of(MSItems.NEEDLE_WAND).grist(DIAMOND, 20).grist(CHALK, 30).grist(GARNET, 40).grist(GOLD, 16).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.ARTIFUCKER).grist(ARTIFACT, -100).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.POINTER_WAND).grist(BUILD, 200).grist(CAULK, 40).grist(AMBER, 50).grist(RUST, 30).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.POOL_CUE_WAND).grist(QUARTZ, 50).grist(CHALK, 70).grist(TAR, 90).grist(URANIUM, 45).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.THORN_OF_OGLOGOTH).grist(IODINE, 200).grist(CHALK, 160).grist(AMETHYST, 120).grist(GOLD, 100).grist(TAR, 64).build(recipeSaver);
		
		GristCostRecipeBuilder.of(MSItems.SBAHJARANG).grist(ARTIFACT, 10).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.CLUBS_SUITARANG).grist(BUILD, 20).grist(TAR, 4).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.DIAMONDS_SUITARANG).grist(BUILD, 20).grist(TAR, 4).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.HEARTS_SUITARANG).grist(BUILD, 20).grist(TAR, 4).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.SPADES_SUITARANG).grist(BUILD, 20).grist(TAR, 4).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.CHAKRAM).grist(BUILD, 200).grist(RUBY, 10).grist(RUST, 20).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.UMBRAL_INFILTRATOR).grist(BUILD, 600).grist(TAR, 450).grist(DIAMOND, 55).grist(MERCURY, 55).grist(QUARTZ, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.SORCERERS_PINBALL).grist(BUILD, 200).grist(MERCURY, 30).grist(IODINE, 5).build(recipeSaver);
		
		GristCostRecipeBuilder.of(MSItems.CAPTCHAROID_CAMERA).grist(BUILD, 5000).grist(CAULK, 500).grist(GOLD, 500).grist(MARBLE, 500).grist(MERCURY, 500).grist(SHALE, 500).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.TRANSPORTALIZER).grist(BUILD, 350).grist(AMETHYST, 27).grist(RUST, 36).grist(URANIUM, 18).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.QUEUESTACK_MODUS_CARD).grist(BUILD, 140).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.TREE_MODUS_CARD).grist(BUILD, 400).grist(AMBER, 35).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.HASHMAP_MODUS_CARD).grist(BUILD, 280).grist(RUBY, 23).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.SET_MODUS_CARD).grist(BUILD, 350).grist(MERCURY, 29).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.IRON_BOAT).grist(RUST, 30).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.GOLD_BOAT).grist(GOLD, 30).build(recipeSaver);
		
		GristCostRecipeBuilder.of(MSBlocks.LAPTOP).grist(BUILD, 80).grist(RUST, 30).grist(QUARTZ, 2).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.CROCKERTOP).grist(BUILD, 20).grist(RUST, 5).grist(MERCURY, 5).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.HUBTOP).grist(BUILD, 60).grist(RUST, 10).grist(URANIUM, 10).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.LUNCHTOP).grist(BUILD, 30).grist(RUST, 10).grist(MARBLE, 20).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.OLD_COMPUTER).grist(BUILD, 80).grist(RUST, 30).grist(SULFUR, 10).build(recipeSaver);
		
		GristCostRecipeBuilder.of(MSBlocks.CASSETTE_PLAYER).grist(BUILD, 400).grist(MARBLE, 40).grist(RUST, 20).grist(AMBER, 20).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(MSItems.MUSIC_DISC_EMISSARY_OF_DANCE).grist(BUILD, 15).grist(CAULK, 8).grist(GOLD, 5).grist(MARBLE, 5).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(MSItems.MUSIC_DISC_DANCE_STAB_DANCE).grist(BUILD, 15).grist(CAULK, 8).grist(COBALT, 5).grist(SULFUR, 5).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(MSItems.MUSIC_DISC_RETRO_BATTLE).grist(BUILD, 15).grist(CAULK, 8).grist(QUARTZ, 5).grist(RUST, 5).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(MSItems.CASSETTE_13).grist(BUILD, 15).grist(CAULK, 8).grist(AMBER, 5).grist(CHALK, 5).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(MSItems.CASSETTE_BLOCKS).grist(BUILD, 15).grist(CAULK, 8).grist(RUBY, 5).grist(RUST, 5).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(MSItems.CASSETTE_CAT).grist(BUILD, 15).grist(CAULK, 8).grist(URANIUM, 5).grist(SHALE, 5).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(MSItems.CASSETTE_CHIRP).grist(BUILD, 15).grist(CAULK, 8).grist(RUBY, 5).grist(GARNET, 5).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(MSItems.CASSETTE_FAR).grist(BUILD, 15).grist(CAULK, 8).grist(URANIUM, 5).grist(SULFUR, 5).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(MSItems.CASSETTE_MALL).grist(BUILD, 15).grist(CAULK, 8).grist(AMETHYST, 5).grist(TAR, 5).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(MSItems.CASSETTE_MELLOHI).grist(BUILD, 15).grist(CAULK, 8).grist(MARBLE, 5).grist(SHALE, 5).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(MSItems.CASSETTE_EMISSARY).grist(BUILD, 15).grist(CAULK, 8).grist(GOLD, 5).grist(MARBLE, 5).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(MSItems.CASSETTE_DANCE_STAB).grist(BUILD, 15).grist(CAULK, 8).grist(COBALT, 5).grist(SULFUR, 5).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(MSItems.CASSETTE_RETRO_BATTLE).grist(BUILD, 15).grist(CAULK, 8).grist(QUARTZ, 5).grist(RUST, 5).buildFor(recipeSaver, Minestuck.MOD_ID);
		
		GristCostRecipeBuilder.of(MSBlocks.GLOWING_MUSHROOM).grist(BUILD, 5).grist(SHALE, 3).grist(MERCURY, 2).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.GLOWING_LOG).grist(BUILD, 8).grist(AMBER, 4).grist(MERCURY, 4).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.GLOWING_PLANKS).grist(BUILD, 2).grist(AMBER, 1).grist(MERCURY, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.GLOWY_GOOP).grist(BUILD, 8).grist(CAULK, 8).grist(MERCURY, 4).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.COAGULATED_BLOOD).grist(GARNET, 8).grist(IODINE, 8).build(recipeSaver);
		
		GristCostRecipeBuilder.of(MSBlocks.PIPE).grist(BUILD, 8).grist(RUST, 4).grist(MERCURY, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.PIPE_INTERSECTION).grist(BUILD, 4).grist(MARBLE, 4).grist(CHALK, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.PARCEL_PYXIS).grist(BUILD, 25).grist(RUST, 10).grist(MERCURY, 5).grist(CHALK,2).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.PYXIS_LID).grist(BUILD, 2).grist(RUST, 1).build(recipeSaver);
		
		GristCostRecipeBuilder.of(MSBlocks.TRAJECTORY_BLOCK).grist(URANIUM, 2).grist(AMBER, 40).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.STAT_STORER).grist(GARNET, 40).grist(MARBLE, 14).grist(DIAMOND, 20).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.REMOTE_OBSERVER).grist(GARNET, 60).grist(MARBLE, 16).grist(AMBER, 10).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.PLATFORM_GENERATOR).grist(TAR, 200).grist(COBALT, 75).grist(AMETHYST, 5).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.PLATFORM_RECEPTACLE).grist(QUARTZ, 5).grist(MERCURY, 5).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.ROTATOR).grist(SULFUR, 20).grist(GOLD, 2).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.FALL_PAD).grist(CAULK, 5).grist(SHALE, 2).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.FRAGILE_STONE).grist(SHALE, 1).build(recipeSaver);
		
		GristCostRecipeBuilder.of(MSBlocks.PETRIFIED_LOG).grist(BUILD, 4).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.FROST_PLANKS).grist(BUILD, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.PETRIFIED_GRASS).grist(BUILD, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.PETRIFIED_POPPY).grist(BUILD, 1).grist(IODINE, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.BLOOMING_CACTUS).grist(AMBER, 1).grist(IODINE, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.GLOWFLOWER).grist(CHALK, 10).grist(AMBER, 2).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.GOLD_SEEDS).grist(GOLD, 3).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.COCOA_WART).grist(AMBER, 5).build(recipeSaver);
		
		GristCostRecipeBuilder.of(MSItems.EMERALD_SWORD).grist(QUARTZ, 44).grist(DIAMOND, 76).grist(RUBY, 72).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.EMERALD_AXE).grist(AMBER, 40).grist(DIAMOND, 73).grist(RUBY, 70).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.EMERALD_PICKAXE).grist(RUST, 42).grist(DIAMOND, 72).grist(RUBY, 70).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.EMERALD_SHOVEL).grist(CHALK, 40).grist(DIAMOND, 70).grist(RUBY, 66).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.EMERALD_HOE).grist(IODINE, 32).grist(DIAMOND, 50).grist(RUBY, 45).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.MINE_AND_GRIST).grist(BUILD, 10000).grist(DIAMOND, 1).grist(RUBY, 1).grist(AMETHYST, 1).grist(QUARTZ, 1).build(recipeSaver);
		
		GristCostRecipeBuilder.of(MSItems.PRISMARINE_HELMET).grist(BUILD, 75).grist(COBALT, 30).grist(MARBLE, 15).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.PRISMARINE_CHESTPLATE).grist(BUILD, 120).grist(COBALT, 48).grist(MARBLE, 24).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.PRISMARINE_LEGGINGS).grist(BUILD, 105).grist(COBALT, 42).grist(MARBLE, 21).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.PRISMARINE_BOOTS).grist(BUILD, 60).grist(COBALT, 24).grist(MARBLE, 12).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.IRON_LASS_GLASSES).grist(BUILD, 3500).grist(RUBY, 700).grist(AMBER, 350).grist(URANIUM, 125).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.IRON_LASS_CHESTPLATE).grist(BUILD, 3500).grist(RUBY, 700).grist(AMBER, 350).grist(URANIUM, 125).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.IRON_LASS_SKIRT).grist(BUILD, 3500).grist(RUBY, 700).grist(AMBER, 350).grist(URANIUM, 125).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.IRON_LASS_SHOES).grist(BUILD, 3500).grist(RUBY, 700).grist(AMBER, 350).grist(URANIUM, 125).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.PROSPIT_CIRCLET).grist(CHALK, 30).grist(AMBER, 5).grist(QUARTZ, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.PROSPIT_SHIRT).grist(CHALK, 45).grist(AMBER, 5).grist(QUARTZ, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.PROSPIT_PANTS).grist(CHALK, 40).grist(AMBER, 5).grist(QUARTZ, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.PROSPIT_SHOES).grist(CHALK, 25).grist(AMBER, 5).grist(QUARTZ, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.DERSE_CIRCLET).grist(CHALK, 30).grist(SHALE, 5).grist(MERCURY, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.DERSE_SHIRT).grist(CHALK, 45).grist(SHALE, 5).grist(MERCURY, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.DERSE_PANTS).grist(CHALK, 40).grist(SHALE, 5).grist(MERCURY, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.DERSE_SHOES).grist(CHALK, 25).grist(SHALE, 5).grist(MERCURY, 1).build(recipeSaver);
		
		GristCostRecipeBuilder.of(MSItems.CARDBOARD_TUBE).grist(BUILD, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.GLOWYSTONE_DUST).grist(BUILD, 1).grist(AMBER, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.RAW_CRUXITE).grist(BUILD, 3).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.RAW_URANIUM).grist(URANIUM, 3).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.GOLDEN_GRASSHOPPER).grist(GOLD, 4000).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.BUG_NET).grist(BUILD, 40).grist(CHALK, 25).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.STONE_SLAB).grist(BUILD, 30).build(recipeSaver);
		
		GristCostRecipeBuilder.of(MSItems.SPORK).grist(BUILD, 50).grist(RUST, 9).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.CANDY_CORN).grist(CHALK, 1).grist(SULFUR, 1).grist(IODINE, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.TUIX_BAR).grist(BUILD, 5).grist(IODINE, 1).build(recipeSaver);
		for(GristType type : GristTypes.values())
		{
			if(type.getRegistryName().getNamespace().equals(Minestuck.MOD_ID))
				GristCostRecipeBuilder.of(type.getCandyItem().getItem()).grist(type, 3).build(recipeSaver);
		}
		GristCostRecipeBuilder.of(MSItems.APPLE_JUICE).grist(AMBER, 4).grist(SULFUR, 1).build(recipeSaver);
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
		
		GristCostRecipeBuilder.of(MSItems.PHLEGM_GUSHERS).grist(BUILD, 24).grist(SHALE, 30).grist(MERCURY, 18).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.SORROW_GUSHERS).grist(TAR, 50).build(recipeSaver);
		
		GristCostRecipeBuilder.of(MSItems.BUG_ON_A_STICK).grist(BUILD, 1).grist(CHALK, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.CHOCOLATE_BEETLE).grist(CHALK, 2).grist(IODINE, 4).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.CONE_OF_FLIES).grist(BUILD, 2).grist(AMBER, 2).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.GRASSHOPPER).grist(IODINE, 3).grist(AMBER, 7).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.CICADA).grist(CAULK, 3).grist(MARBLE, 8).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.JAR_OF_BUGS).grist(BUILD, 5).grist(CHALK, 3).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.ONION).grist(IODINE, 3).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.IRRADIATED_STEAK).grist(IODINE, 12).grist(URANIUM, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.ROCK_COOKIE).grist(BUILD, 10).grist(MARBLE, 5).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.WOODEN_CARROT).grist(BUILD, 8).grist(AMBER, 3).grist(CHALK, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.STRAWBERRY_CHUNK).grist(AMBER, 2).grist(BUILD, 1).grist(RUBY, 2).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.FOOD_CAN).grist(IODINE, 10).grist(TAR, 1).build(recipeSaver);
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
		
		GristCostRecipeBuilder.of(MSBlocks.BLACK_CHESS_DIRT).grist(BUILD, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.DARK_GRAY_CHESS_DIRT).grist(BUILD, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.LIGHT_GRAY_CHESS_DIRT).grist(BUILD, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.WHITE_CHESS_DIRT).grist(BUILD, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.BLACK_CASTLE_BRICKS).grist(BUILD, 2).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.DARK_GRAY_CASTLE_BRICKS).grist(BUILD, 2).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.LIGHT_GRAY_CASTLE_BRICKS).grist(BUILD, 2).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.WHITE_CASTLE_BRICKS).grist(BUILD, 2).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.BLACK_CASTLE_BRICK_SMOOTH).grist(BUILD, 2).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.DARK_GRAY_CASTLE_BRICK_SMOOTH).grist(BUILD, 2).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.LIGHT_GRAY_CASTLE_BRICK_SMOOTH).grist(BUILD, 2).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.WHITE_CASTLE_BRICK_SMOOTH).grist(BUILD, 2).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.BLACK_CASTLE_BRICK_TRIM).grist(BUILD, 3).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.DARK_GRAY_CASTLE_BRICK_TRIM).grist(BUILD, 3).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.LIGHT_GRAY_CASTLE_BRICK_TRIM).grist(BUILD, 3).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.WHITE_CASTLE_BRICK_TRIM).grist(BUILD, 3).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.CHECKERED_STAINED_GLASS).grist(BUILD, 5).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.BLACK_PAWN_STAINED_GLASS).grist(BUILD, 5).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.BLACK_CROWN_STAINED_GLASS).grist(BUILD, 5).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.WHITE_PAWN_STAINED_GLASS).grist(BUILD, 5).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.WHITE_CROWN_STAINED_GLASS).grist(BUILD, 5).build(recipeSaver);
		
		GristCostRecipeBuilder.of(MSBlocks.TAR_SHADE_BRICKS).grist(BUILD, 2).grist(TAR, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.FROST_TILE).grist(BUILD, 2).grist(COBALT, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.CAST_IRON).grist(BUILD, 3).grist(RUST, 2).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.STEEL_BEAM).grist(BUILD, 2).grist(RUST, 3).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.BLACK_SAND).grist(BUILD, 2).grist(SULFUR, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.COARSE_END_STONE).grist(BUILD, 4).grist(CAULK, 3).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.CHALK).grist(BUILD, 2).grist(CHALK, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.UNCARVED_WOOD).grist(BUILD, 4).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.CHIPBOARD).grist(BUILD, 4).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.WOOD_SHAVINGS).grist(BUILD, 4).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.END_GRASS).grist(BUILD, 4).grist(CAULK, 3).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.FLOWERY_VINE_LOG).grist(BUILD, 7).grist(IODINE, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.FROST_LOG).grist(BUILD, 7).grist(COBALT, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.TREATED_PLANKS).grist(BUILD, 2).build(recipeSaver);
		
		GristCostRecipeBuilder.of(MSItems.SBAHJ_POSTER).grist(BUILD, 4).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.CREW_POSTER).grist(TAR, 3).grist(RUST, 2).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.FLARP_MANUAL).grist(AMBER, 10).grist(TAR, 4).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.SASSACRE_TEXT).grist(CAULK, 10).grist(MERCURY, 4).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.WISEGUY).grist(CHALK, 10).grist(SULFUR, 4).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.TABLESTUCK_MANUAL).grist(MARBLE, 10).grist(COBALT, 4).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.TILLDEATH_HANDBOOK).grist(TAR, 10).grist(AMETHYST, 4).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.BINARY_CODE).grist(IODINE, 10).grist(SHALE, 4).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.NONBINARY_CODE).grist(BUILD, 10).grist(URANIUM, 4).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.THRESH_DVD).grist(IODINE, 3).grist(AMETHYST, 2).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.GAMEBRO_MAGAZINE).grist(CHALK, 3).grist(GARNET, 2).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.GAMEGRL_MAGAZINE).grist(CHALK, 3).grist(AMBER, 2).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.ICE_SHARD).grist(COBALT, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.BATTERY).grist(GOLD, 4).grist(URANIUM, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.INK_SQUID_PRO_QUO).grist(TAR, 8).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.HORN).grist(BUILD, 4).grist(RUST, 4).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.CAKE_MIX).grist(BUILD, 2).grist(IODINE, 2).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.BARBASOL).grist(BUILD, 4).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.BARBASOL_BOMB).grist(BUILD, 10).grist(SHALE, 10).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.CLOTHES_IRON).grist(BUILD, 4).grist(SHALE, 4).grist(RUST, 4).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.CARVING_TOOL).grist(BUILD, 10).grist(RUST, 2).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.CRUMPLY_HAT).grist(BUILD, 20).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.MINI_FROG_STATUE).grist(BUILD, 30).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.MINI_WIZARD_STATUE).grist(BUILD, 30).build(recipeSaver);
		
		GristCostRecipeBuilder.of(MSItems.SCALEMATE_APPLESCAB).grist(BUILD, 8).grist(RUBY, 8).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.SCALEMATE_BERRYBREATH).grist(BUILD, 8).grist(COBALT, 8).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.SCALEMATE_CINNAMONWHIFF).grist(BUILD, 8).grist(IODINE, 8).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.SCALEMATE_HONEYTONGUE).grist(BUILD, 8).grist(AMBER, 8).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.SCALEMATE_LEMONSNOUT).grist(BUILD, 8).grist(AMBER, 8).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.SCALEMATE_PINESNOUT).grist(BUILD, 8).grist(COBALT, 8).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.SCALEMATE_PUCEFOOT).grist(BUILD, 8).grist(GARNET, 8).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.SCALEMATE_PUMPKINSNUFFLE).grist(BUILD, 8).grist(IODINE, 8).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.SCALEMATE_PYRALSPITE).grist(BUILD, 8).grist(CAULK, 8).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.SCALEMATE_WITNESS).grist(BUILD, 8).grist(URANIUM, 8).build(recipeSaver);
		
		GristCostRecipeBuilder.of(MSBlocks.WOODEN_CACTUS).grist(BUILD, 7).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.BLUE_CAKE).grist(SHALE, 24).grist(MERCURY, 6).grist(COBALT, 5).grist(DIAMOND, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.COLD_CAKE).grist(COBALT, 15).grist(MARBLE, 12).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.RED_CAKE).grist(RUST, 20).grist(CHALK, 9).grist(IODINE, 6).grist(GARNET, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.HOT_CAKE).grist(SULFUR, 17).grist(IODINE, 10).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.REVERSE_CAKE).grist(AMBER, 10).grist(CHALK, 24).grist(IODINE, 11).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.FUCHSIA_CAKE).grist(AMETHYST, 85).grist(GARNET, 54).grist(IODINE, 40).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.NEGATIVE_CAKE).grist(CAULK, 25).grist(SHALE, 19).grist(TAR, 22).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.CARROT_CAKE).grist(CHALK, 20).grist(AMETHYST, 10).grist(MARBLE, 5).build(recipeSaver);
		
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
	
	public static void oreCost(ITag<Item> ores, ITag<Item> material, float multiplier, Consumer<IFinishedRecipe> recipeSaver, String modId)
	{
		SourceGristCostBuilder.of(ores).source(material).multiplier(multiplier).grist(BUILD, 4).buildFor(recipeSaver, modId);
	}
	
	public static void oreCost(ITag<Item> ores, Item material, float multiplier, Consumer<IFinishedRecipe> recipeSaver, String modId)
	{
		SourceGristCostBuilder.of(ores).source(material).multiplier(multiplier).grist(BUILD, 4).buildFor(recipeSaver, modId);
	}
	
	@Override
	public String getName()
	{
		return "Minestuck Grist Costs";
	}
}