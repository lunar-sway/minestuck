package com.mraof.minestuck.data.recipe;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.api.alchemy.GristType;
import com.mraof.minestuck.api.alchemy.GristTypes;
import com.mraof.minestuck.api.alchemy.recipe.ContainerGristCostBuilder;
import com.mraof.minestuck.api.alchemy.recipe.GristCostRecipeBuilder;
import com.mraof.minestuck.api.alchemy.recipe.SourceGristCostBuilder;
import com.mraof.minestuck.api.alchemy.recipe.WildcardGristCostBuilder;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.util.ExtraForgeTags;
import com.mraof.minestuck.util.MSTags;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.Tags;

import static com.mraof.minestuck.api.alchemy.GristTypes.*;

public final class MinestuckGristCostsProvider
{
	public static void buildRecipes(RecipeOutput recipeSaver)
	{
		GeneratedGristCostBuilder.create().build(recipeSaver, new ResourceLocation(Minestuck.MOD_ID, "generated"));
		
		//Stone
		GristCostRecipeBuilder.of(Tags.Items.STONE).grist(BUILD, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Tags.Items.COBBLESTONE).grist(BUILD, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.STONE_BRICKS).grist(BUILD, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.CRACKED_STONE_BRICKS).grist(BUILD, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.GRAVEL).grist(BUILD, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(ItemTags.SAND).grist(BUILD, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		SourceGristCostBuilder.of(Items.SUSPICIOUS_SAND).source(Items.SAND).grist(DIAMOND, 10).buildFor(recipeSaver, Minestuck.MOD_ID);
		SourceGristCostBuilder.of(Items.SUSPICIOUS_GRAVEL).source(Items.GRAVEL).grist(DIAMOND, 10).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.GLASS).grist(BUILD, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		SourceGristCostBuilder.of(ItemTags.TERRACOTTA).source(Items.CLAY).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.CLAY_BALL).grist(SHALE, 1).grist(BUILD, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.FLINT).grist(BUILD, 4).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.POINTED_DRIPSTONE).grist(BUILD, 4).grist(AMBER, 3).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.CALCITE).grist(BUILD, 1).grist(CHALK, 4).buildFor(recipeSaver, Minestuck.MOD_ID);
		
		//Ore
		oreCost(Tags.Items.ORES_COAL, Items.COAL, 3, recipeSaver, Minestuck.MOD_ID); //2.5 coal per ore on avg with fortune 3
		oreCost(Tags.Items.ORES_COPPER, Items.RAW_COPPER, 8, recipeSaver, Minestuck.MOD_ID); //7.7 raw copper per ore on avg with fortune 3
		oreCost(Tags.Items.ORES_IRON, Items.RAW_IRON, 3, recipeSaver, Minestuck.MOD_ID); //2.5 raw iron per ore on avg with fortune 3
		oreCost(Tags.Items.ORES_GOLD, Items.RAW_GOLD, 3, recipeSaver, Minestuck.MOD_ID); //2.5 raw gold per ore on avg with fortune 3
		oreCost(Tags.Items.ORES_REDSTONE, Items.REDSTONE, 6, recipeSaver, Minestuck.MOD_ID); //6 redstone dust per ore on avg with fortune 3
		oreCost(Tags.Items.ORES_LAPIS, Items.LAPIS_LAZULI, 20, recipeSaver, Minestuck.MOD_ID); //20 lapis per ore on avg with fortune 3
		oreCost(Tags.Items.ORES_DIAMOND, Items.DIAMOND, 3, recipeSaver, Minestuck.MOD_ID); //2.2 diamonds per ore on avg with fortune 3
		oreCost(Tags.Items.ORES_EMERALD, Items.EMERALD, 1, recipeSaver, Minestuck.MOD_ID);
		oreCost(Tags.Items.ORES_QUARTZ, Items.QUARTZ, 2, recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.COAL).grist(TAR, 8).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.RAW_COPPER).grist(SHALE, 9).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.RAW_GOLD).grist(GOLD, 9).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.RAW_IRON).grist(RUST, 9).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.CHARCOAL).grist(TAR, 6).grist(AMBER, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.COPPER_INGOT).grist(SHALE, 9).buildFor(recipeSaver, Minestuck.MOD_ID);
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
		GristCostRecipeBuilder.of(Items.MUD).grist(BUILD, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.PODZOL).grist(BUILD, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.ROOTED_DIRT).grist(MERCURY, 1).grist(BUILD, 4).buildFor(recipeSaver, Minestuck.MOD_ID);
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
		GristCostRecipeBuilder.of(Items.BAMBOO).grist(BUILD, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		SourceGristCostBuilder.of(Items.STRIPPED_BAMBOO_BLOCK).source(Items.BAMBOO_BLOCK).buildFor(recipeSaver, Minestuck.MOD_ID);
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
		GristCostRecipeBuilder.of(Items.SNIFFER_EGG).grist(AMBER, 2).grist(RUST, 40).buildFor(recipeSaver, Minestuck.MOD_ID);
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
		GristCostRecipeBuilder.of(Items.PINK_PETALS).grist(CHALK, 1).grist(IODINE, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.TORCHFLOWER).grist(GARNET, 4).grist(AMETHYST, 1).grist(IODINE, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.TORCHFLOWER_SEEDS).grist(GARNET, 1).grist(AMETHYST, 1).grist(IODINE, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.WITHER_ROSE).grist(TAR, 6).grist(URANIUM, 2).grist(IODINE, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.BROWN_MUSHROOM).grist(IODINE, 5).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.RED_MUSHROOM).grist(IODINE, 3).grist(RUBY, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.CACTUS).grist(AMBER, 4).grist(IODINE, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.SHORT_GRASS).grist(BUILD, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.FERN).grist(BUILD, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.VINE).grist(BUILD, 2).grist(AMBER, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.LILY_PAD).grist(AMBER, 4).grist(IODINE, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.SUNFLOWER).grist(AMBER, 7).grist(IODINE, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.LILAC).grist(AMETHYST, 2).grist(GARNET, 5).grist(IODINE, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.TALL_GRASS).grist(AMBER, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.LARGE_FERN).grist(AMBER, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.ROSE_BUSH).grist(GARNET, 7).grist(IODINE, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.PEONY).grist(GARNET, 4).grist(CHALK, 4).grist(IODINE, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.PITCHER_PLANT).grist(CAULK, 4).grist(RUST, 2).grist(IODINE, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.PITCHER_POD).grist(CAULK, 1).grist(RUST, 1).grist(IODINE, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
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
		GristCostRecipeBuilder.of(Items.BIG_DRIPLEAF).grist(BUILD, 2).grist(SHALE, 2).grist(MERCURY, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.SMALL_DRIPLEAF).grist(BUILD, 1).grist(SHALE, 1).grist(MERCURY, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.HANGING_ROOTS).grist(MERCURY, 1).grist(BUILD, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.MOSS_BLOCK).grist(MARBLE, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.SPORE_BLOSSOM).grist(CAULK, 4).grist(IODINE, 4).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.GLOW_BERRIES).grist(AMBER, 1).grist(SULFUR, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.GLOW_LICHEN).grist(SULFUR, 3).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.MANGROVE_ROOTS).grist(BUILD, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.SCULK).grist(TAR, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.SCULK_VEIN).grist(TAR, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.SCULK_CATALYST).grist(TAR, 24).grist(IODINE, 4).grist(AMETHYST, 4).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.SCULK_SENSOR).grist(TAR, 16).grist(GARNET, 6).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.SCULK_SHRIEKER).grist(TAR, 22).grist(RUST, 8).buildFor(recipeSaver, Minestuck.MOD_ID);
		
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
		GristCostRecipeBuilder.of(Items.GLOBE_BANNER_PATTERN).grist(BUILD, 100).grist(MARBLE, 15).grist(SHALE, 15).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.PIGLIN_BANNER_PATTERN).grist(BUILD, 120).grist(TAR, 17).grist(GOLD, 12).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.GLOW_INK_SAC).grist(COBALT, 7).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.PEARLESCENT_FROGLIGHT).grist(MERCURY, 8).grist(IODINE, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.VERDANT_FROGLIGHT).grist(MERCURY, 8).grist(IODINE, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.OCHRE_FROGLIGHT).grist(MERCURY, 8).grist(IODINE, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.ECHO_SHARD).grist(TAR, 18).grist(QUARTZ, 8).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.AMETHYST_SHARD).grist(AMETHYST, 3).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.SMALL_AMETHYST_BUD).grist(AMETHYST, 3).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.MEDIUM_AMETHYST_BUD).grist(AMETHYST, 5).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.LARGE_AMETHYST_BUD).grist(AMETHYST, 7).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.AMETHYST_CLUSTER).grist(AMETHYST, 9).buildFor(recipeSaver, Minestuck.MOD_ID); //8.8 amethyst shards per cluster on avg with fortune 3
		GristCostRecipeBuilder.of(Items.EXPOSED_COPPER).grist(SHALE, 81).grist(RUST, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.WEATHERED_COPPER).grist(SHALE, 81).grist(RUST, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.OXIDIZED_COPPER).grist(SHALE, 81).grist(RUST, 3).buildFor(recipeSaver, Minestuck.MOD_ID);
		
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
		GristCostRecipeBuilder.of(Items.MUSIC_DISC_RELIC).grist(BUILD, 20).grist(MARBLE, 12).grist(RUST, 3).grist(SHALE, 5).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.DISC_FRAGMENT_5).grist(BUILD, 25).grist(TAR, 15).grist(QUARTZ, 8).grist(DIAMOND, 4).buildFor(recipeSaver, Minestuck.MOD_ID);
		
		//Liquid Buckets
		ContainerGristCostBuilder.of(Items.WATER_BUCKET).grist(COBALT, 4).buildFor(recipeSaver, Minestuck.MOD_ID);
		ContainerGristCostBuilder.of(Items.LAVA_BUCKET).grist(TAR, 16).buildFor(recipeSaver, Minestuck.MOD_ID);
		ContainerGristCostBuilder.of(Items.MILK_BUCKET).grist(CHALK, 8).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.POWDER_SNOW_BUCKET).grist(RUST, 27).grist(CHALK, 4).buildFor(recipeSaver, Minestuck.MOD_ID); //TODO find way to handle SolidBucketItem instances like BucketItem
		
		//------------------------MINESTUCK------------------------\\
		
		oreCost(MSTags.Items.URANIUM_ORES, MSItems.RAW_URANIUM.get(), 1, recipeSaver, Minestuck.MOD_ID);
		oreCost(MSTags.Items.CRUXITE_ORES, MSItems.RAW_CRUXITE.get(), 4, recipeSaver, Minestuck.MOD_ID);
		
		GristCostRecipeBuilder.of(MSBlocks.BLUE_DIRT.get()).grist(BUILD, 1).grist(SHALE, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.THOUGHT_DIRT.get()).grist(BUILD, 1).grist(CAULK, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.GENERIC_OBJECT.get()).grist(BUILD, 2).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.CHESSBOARD.get()).grist(SHALE, 25).grist(MARBLE, 25).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.GRIMOIRE.get()).grist(BUILD, 120).grist(AMETHYST, 60).grist(GARNET, 33).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.LONG_FORGOTTEN_WARHORN.get()).grist(BUILD, 550).grist(AMETHYST, 120).grist(TAR, 50).grist(GARNET, 80).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.EIGHTBALL.get()).grist(TAR, 888).grist(COBALT, 888).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.DICE.get()).grist(BUILD, 26).grist(CHALK, 6).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.MIRROR.get()).grist(BUILD, 20).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.GUTTER_BALL.get()).grist(BUILD, 1000).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.ANCIENT_THUMB_DRIVE.get()).grist(BUILD, 10000).build(recipeSaver);
		
		GristCostRecipeBuilder.of(MSItems.SBURB_CODE.get()).grist(CHALK, 16).grist(IODINE, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(MSItems.COMPLETED_SBURB_CODE.get()).grist(CHALK, 16).grist(IODINE, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(MSItems.COMPUTER_PARTS.get()).grist(BUILD, 20).grist(RUST, 5).grist(GOLD, 1).build(recipeSaver);
		
		GristCostRecipeBuilder.of(MSBlocks.GRIST_WIDGET.get()).grist(BUILD, 550).grist(GARNET, 55).grist(RUBY, 34).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.GRIST_COLLECTOR.get()).grist(BUILD, 5000).grist(URANIUM, 300).grist(DIAMOND, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.ANTHVIL.get()).grist(BUILD, 500).grist(URANIUM, 400).grist(RUST, 300).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.POWER_HUB.get()).grist(BUILD, 20).grist(URANIUM, 60).grist(QUARTZ, 5).build(recipeSaver);
		
		GristCostRecipeBuilder.of(MSItems.CRUXITE_APPLE.get()).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.CRUXITE_POTION.get()).build(recipeSaver);
		
		GristCostRecipeBuilder.of(MSItems.FLUORITE_OCTET.get()).grist(DIAMOND, 5600).grist(COBALT, 8).grist(CHALK, 1).build(recipeSaver);
		
		GristCostRecipeBuilder.of(MSItems.CAT_CLAWS_DRAWN.get()).grist(BUILD, 105).grist(RUST, 9).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.COFFEE_CLAWS_DRAWN.get()).grist(BUILD, 30).grist(IODINE, 15).grist(MARBLE, 12).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.POGO_CLAWS.get()).grist(BUILD, 100).grist(SHALE, 50).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.ATOMIKITTY_KATAR_DRAWN.get()).grist(BUILD, 413).grist(CAULK, 95).grist(URANIUM, 228).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.SKELETONIZER_DRAWN.get()).grist(BUILD, 93).grist(CHALK, 20).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.SKELETON_DISPLACER_DRAWN.get()).grist(URANIUM, 209).grist(RUST, 314).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.TEARS_OF_THE_ENDERLICH_DRAWN.get()).grist(QUARTZ, 1425).grist(RUST, 3205).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.LION_LACERATORS_DRAWN.get()).grist(COBALT, 2310).grist(AMETHYST, 1732).grist(RUST, 1732).grist(DIAMOND, 3465).grist(URANIUM, 2074).build(recipeSaver);
		
		GristCostRecipeBuilder.of(MSItems.LIPSTICK_CHAINSAW.get()).grist(BUILD, 45).grist(MARBLE, 18).grist(SHALE, 30).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.LIPSTICK.get()).grist(BUILD, 45).grist(MARBLE, 18).grist(SHALE, 30).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.CAKESAW.get()).grist(BUILD, 30).grist(IODINE, 15).grist(MARBLE, 12).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.MAGENTA_MAULER.get()).grist(AMBER, 16).grist(IODINE, 16).grist(AMETHYST, 38).grist(GARNET, 28).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.THISTLEBLOWER.get()).grist(SHALE, 56).grist(MERCURY, 45).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.EMERALD_IMMOLATOR.get()).grist(BUILD, 198).grist(TAR, 99).grist(RUBY, 59).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.FROSTTOOTH.get()).grist(BUILD, 291).grist(TAR, 145).grist(AMETHYST, 87).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.OBSIDIATOR.get()).grist(BUILD, 405).grist(TAR, 405).grist(GARNET, 365).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.DEVILS_DELIGHT.get()).grist(BUILD, 400).grist(TAR, 525).grist(GARNET, 350).grist(RUST, 105).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.DEMONBANE_RAGRIPPER.get()).grist(BUILD, 1374).grist(TAR, 1374).grist(SULFUR, 666).grist(GARNET, 1236).grist(RUBY, 824).grist(RUST, 413).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.HAND_CRANKED_VAMPIRE_ERASER.get()).grist(BUILD, 150).build(recipeSaver);
		
		GristCostRecipeBuilder.of(MSItems.LANEC.get()).grist(ARTIFACT, -2).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.JOUSTING_LANCE.get()).grist(RUST, 113).grist(QUARTZ, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.POGO_LANCE.get()).grist(BUILD, 98).grist(SHALE, 12).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.LANCELOTS_LOLLY.get()).grist(BUILD, 24).grist(IODINE, 36).grist(COBALT, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.DRAGON_LANCE.get()).grist(GARNET, 62).grist(DIAMOND, 7).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.SKY_PIERCER.get()).grist(AMBER, 596).grist(CAULK, 596).grist(DIAMOND, 239).grist(GOLD, 239).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.FIDUSPAWN_LANCE.get()).grist(AMBER, 180).grist(RUBY, 108).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.REGILANCE.get()).grist(TAR, 32).grist(AMETHYST, 19).grist(GOLD, 25).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.CIGARETTE_LANCE.get()).grist(SHALE, 1621).grist(TAR, 1621).grist(DIAMOND, 648).build(recipeSaver);
		
		GristCostRecipeBuilder.of(MSItems.LUCERNE_HAMMER.get()).grist(BUILD, 35).grist(RUST, 11).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.LUCERNE_HAMMER_OF_UNDYING.get()).grist(BUILD, 836).grist(DIAMOND, 76).grist(CHALK, 190).grist(MARBLE, 152).build(recipeSaver);
		
		GristCostRecipeBuilder.of(MSItems.CANDY_FAN.get()).grist(BUILD, 10).grist(IODINE, 5).grist(MARBLE, 4).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.SPINES_OF_FLUTHLU.get()).grist(BUILD, 454).grist(MERCURY, 66).grist(SULFUR, 66).grist(AMETHYST, 90).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.RAZOR_FAN.get()).grist(BUILD, 24).grist(RUST, 14).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.MOTOR_FAN.get()).grist(BUILD, 70).grist(RUST, 21).grist(GOLD, 14).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.ATOMIC_VAPORIZER.get()).grist(BUILD, 709).grist(TAR, 355).grist(MERCURY, 284).grist(URANIUM, 284).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.SHAVING_FAN.get()).grist(BUILD, 16).grist(RUBY, 10).grist(RUST, 15).grist(DIAMOND, 10).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.FIRESTARTER.get()).grist(AMBER, 60).grist(SULFUR, 48).grist(RUBY, 72).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.STAR_RAY.get()).grist(AMBER, 48).grist(TAR, 48).grist(SULFUR, 38).grist(RUBY, 58).grist(GOLD, 98).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.TYPHONIC_TRIVIALIZER.get()).grist(BUILD, 7860).grist(RUBY, 983).grist(DIAMOND, 524).grist(SHALE, 1310).grist(URANIUM, 5).build(recipeSaver);
		
		GristCostRecipeBuilder.of(MSItems.MAILBOX.get()).grist(BUILD, 19).grist(MERCURY, 3).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.BLACKSMITH_HAMMER.get()).grist(RUST, 11).grist(SULFUR, 15).grist(CAULK, 13).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.POGO_HAMMER.get()).grist(BUILD, 152).grist(SHALE, 19).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.WRINKLEFUCKER.get()).grist(BUILD, 238).grist(SHALE, 25).grist(TAR, 31).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.TELESCOPIC_SASSACRUSHER.get()).grist(SHALE, 87).grist(TAR, 53).grist(MERCURY, 56).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.DEMOCRATIC_DEMOLITIONER.get()).grist(BUILD, 50).grist(GOLD, 1).grist(MARBLE, 10).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.REGI_HAMMER.get()).grist(AMETHYST, 32).grist(TAR, 77).grist(GOLD, 32).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.FEAR_NO_ANVIL.get()).grist(BUILD, 5150).grist(GARNET, 3157).grist(DIAMOND, 206).grist(GOLD, 247).grist(QUARTZ, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.MELT_MASHER.get()).grist(BUILD, 4566).grist(TAR, 913).grist(GARNET, 274).grist(DIAMOND, 310).grist(GOLD, 91).grist(SULFUR, 274).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.ESTROGEN_EMPOWERED_EVERYTHING_ERADICATOR.get()).grist(BUILD, 1108).grist(SHALE, 92).grist(URANIUM, 17).grist(RUST, 13).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.EEEEEEEEEEEE.get()).grist(ARTIFACT, -17).grist(BUILD, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.POPAMATIC_VRILLYHOO.get()).grist(BUILD, 5500).grist(QUARTZ, 13200).grist(DIAMOND, 5500).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.SCARLET_ZILLYHOO.get()).grist(BUILD, 2000).grist(RUBY, 3600).grist(DIAMOND, 1600).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.MWRTHWL.get()).grist(BUILD, 4063).grist(GOLD, 3088).grist(RUST, 1950).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.BOOMBOX_BEATER.get()).grist(BUILD, 3908).grist(AMBER, 977).grist(MARBLE, 782).grist(RUST, 586).build(recipeSaver);
		
		GristCostRecipeBuilder.of(MSItems.SORD.get()).grist(BUILD, 0).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.PAPER_SWORD.get()).grist(BUILD, 12).grist(TAR, 3).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.SWONGE.get()).grist(IODINE, 35).grist(QUARTZ, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.PUMORD.get()).grist(MARBLE, 125).grist(GARNET, 5).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.CACTACEAE_CUTLASS.get()).grist(AMBER, 20).grist(MARBLE, 4).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.BEEF_SWORD.get()).grist(IODINE, 22).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.IRRADIATED_STEAK_SWORD.get()).grist(IODINE, 3).grist(TAR, 3).grist(URANIUM, 8).build(recipeSaver); //does not follow grist cost
		GristCostRecipeBuilder.of(MSItems.MACUAHUITL.get()).grist(BUILD, 52).grist(COBALT, 31).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.FROSTY_MACUAHUITL.get()).grist(BUILD, 69).grist(COBALT, 69).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.UNBREAKABLE_KATANA.get()).grist(BUILD, 4375).grist(URANIUM, 6325).grist(QUARTZ, 1200).grist(RUBY, 600).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.ANGEL_APOCALYPSE.get()).grist(BUILD, 1300).grist(AMBER, 25).grist(QUARTZ, 80).grist(GOLD, 30).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.FIRE_POKER.get()).grist(AMBER, 75).grist(RUBY, 9).grist(SULFUR, 40).grist(GOLD, 4).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.TOO_HOT_TO_HANDLE.get()).grist(AMBER, 40).grist(RUBY, 36).grist(SULFUR, 24).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.CALEDSCRATCH.get()).grist(BUILD, 6000).grist(GARNET, 360).grist(MERCURY, 720).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.CALEDFWLCH.get()).grist(BUILD, 6563).grist(GOLD, 4988).grist(RUST, 3150).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.ROYAL_DERINGER.get()).grist(BUILD, 6150).grist(GOLD, 4715).grist(MARBLE, 4510).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.CLAYMORE.get()).grist(BUILD, 210).grist(RUST, 45).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.REGISWORD.get()).grist(AMETHYST, 32).grist(TAR, 117).grist(GOLD, 32).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.CRUEL_FATE_CRUCIBLE.get()).grist(BUILD, 7500).grist(TAR, 5250).grist(COBALT, 400).grist(GOLD, 200).grist(QUARTZ, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.SCARLET_RIBBITAR.get()).grist(BUILD, 1250).grist(RUBY, 1725).grist(DIAMOND, 400).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.DOGG_MACHETE.get()).grist(BUILD, 400).grist(CHALK, 200).grist(MERCURY, 160).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.COBALT_SABRE.get()).grist(BUILD, 850).grist(URANIUM, 34).grist(COBALT, 204).grist(DIAMOND, 34).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.QUANTUM_SABRE.get()).grist(BUILD, 413).grist(URANIUM, 796).grist(CAULK, 1659).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.SHATTER_BEACON.get()).grist(BUILD, 315).grist(COBALT, 294).grist(DIAMOND, 210).grist(URANIUM, 630).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.SHATTER_BACON.get()).grist(BUILD, 50).grist(IODINE, 25).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.SUBTRACTSHUMIDIRE_ZOMORRODNEGATIVE.get()).grist(BUILD, 666).grist(AMBER, 1167).grist(GARNET, 600).grist(DIAMOND, 333).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.PILLOW_TALK.get()).grist(BUILD, 4217).grist(AMBER, 2108).grist(QUARTZ, 5061).grist(RUBY, 2530).grist(URANIUM, 6748).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.KRAKENS_EYE.get()).grist(BUILD, 323).grist(COBALT, 129).grist(DIAMOND, 65).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.CINNAMON_SWORD.get()).grist(BUILD,20).grist(IODINE, 10).grist(AMBER, 10).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.UNION_BUSTER.get()).grist(BUILD, 8966).grist(TAR, 4483).grist(DIAMOND, 1).grist(GOLD,10760).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.CHAINSAW_KATANA.get()).grist(BUILD, 127).grist(URANIUM, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.THORN_IN_YOUR_SIDE.get()).grist(BUILD, 26).grist(RUBY, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.ROSE_PROTOCOL.get()).grist(BUILD, 458).grist(IODINE, 114).grist(MERCURY, 92).grist(RUBY, 35).grist(URANIUM, 1).build(recipeSaver);
		
		GristCostRecipeBuilder.of(MSItems.DIAMOND_DAGGER.get()).grist(BUILD, 16).grist(DIAMOND, 36).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.PIGLINS_PRIDE.get()).grist(BUILD, 1730).grist(TAR, 865).grist(SULFUR, 692).grist(GOLD, 346).grist(URANIUM, 346).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.BASILISK_BREATH_DRAGONSLAYER.get()).grist(BUILD, 8176).grist(SHALE, 4088).grist(QUARTZ, 3270).grist(SULFUR, 3270).grist(AMETHYST, 2453).grist(URANIUM, 1635).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.HALLOWED_SKEWER.get()).grist(RUBY, 250).grist(DIAMOND, 166).grist(GOLD, 166).grist(URANIUM, 166).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.GENESIS_GODSTABBER.get()).grist(BUILD, 1025).grist(AMETHYST, 547).grist(RUBY, 547).grist(DIAMOND, 547).grist(URANIUM, 547).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.NIFE.get()).grist(ARTIFACT, -2).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.LIGHT_OF_MY_KNIFE.get()).grist(URANIUM, 43).grist(MERCURY, 85).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.THOUSAND_DEGREE_KNIFE.get()).grist(TAR, 460).grist(MERCURY, 369).grist(RUBY, 277).grist(URANIUM, 460).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.STARSHARD_TRI_BLADE.get()).grist(URANIUM, 474).grist(DIAMOND, 474).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.TOOTHRIPPER.get()).grist(RUST, 73).grist(CAULK, 121).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.SHADOWRAZOR.get()).grist(TAR, 115).grist(SULFUR, 115).grist(CHALK, 231).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.PRINCESS_PERIL.get()).grist(CHALK, 113).grist(TAR, 225).grist(SULFUR, 180).grist(SHALE, 113).grist(MARBLE, 180).grist(URANIUM, 1).build(recipeSaver);
		
		GristCostRecipeBuilder.of(MSItems.KEYBLADE.get()).grist(BUILD, 7).grist(RUST, 10).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.CANDY_KEY.get()).grist(BUILD, 7).grist(CHALK, 1).grist(IODINE, 4).grist(RUBY, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.LOCKSOFTENER.get()).grist(BUILD, 13).grist(COBALT, 5).grist(RUST, 20).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.BISEKEYAL.get()).grist(BUILD, 30).grist(GARNET, 18).grist(AMETHYST, 18).grist(QUARTZ, 12).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.KEY_TO_THE_MACHINE.get()).grist(BUILD, 40).grist(GARNET, 12).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.KEY_TO_THE_CITY.get()).grist(BUILD, 20).grist(MARBLE, 6).grist(GOLD, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.LATCHMELTER.get()).grist(BUILD, 45).grist(MARBLE, 13).grist(GOLD, 3).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.DRAGON_KEY.get()).grist(BUILD, 42).grist(RUBY, 38).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.TRUE_BLUE.get()).grist(BUILD, 536).grist(TAR, 268).grist(COBALT, 215).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.BLUE_BEAMS.get()).grist(COBALT, 632).grist(DIAMOND, 105).grist(URANIUM, 105).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.INKSPLOCKER_UNLOCKER.get()).grist(BUILD, 15).grist(TAR, 30).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.INKSQUIDDER_DEPTHKEY.get()).grist(TAR, 13).grist(COBALT, 43).grist(DIAMOND, 5).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.REGIKEY.get()).grist(TAR, 66).grist(AMETHYST, 26).grist(GOLD, 26).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.CLOCKKEEPER.get()).grist(TAR, 300).grist(GARNET, 180).grist(RUST, 180).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.HOME_BY_MIDNIGHT.get()).grist(MARBLE, 466).grist(AMETHYST, 350).grist(DIAMOND, 233).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.KEYTAR.get()).grist(BUILD, 120).grist(RUST, 18).grist(COBALT, 12).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.NATURES_HEART.get()).grist(BUILD, 240).grist(AMETHYST, 36).grist(RUBY, 16).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.CRIMSON_LEAP.get()).grist(BUILD, 60).grist(RUBY, 72).grist(DIAMOND, 12).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.NO_TIME_FOR_FLIES.get()).grist(TAR, 166).grist(GARNET, 50).grist(RUBY, 50).grist(DIAMOND, 16).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.LOCH_PICK.get()).grist(BUILD, 194).grist(CAULK, 48).grist(URANIUM, 10).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.INNER_HEART.get()).grist(BUILD, 58).grist(CHALK, 14).grist(MARBLE, 17).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.YALDABAOTHS_KEYTON.get()).grist(BUILD, 24500).grist(AMBER, 10000).grist(GOLD, 1).build(recipeSaver);
		
		GristCostRecipeBuilder.of(MSItems.BO_STAFF.get()).grist(BUILD, 15).grist(IODINE, 10).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.BAMBOO_BEATSTICK.get()).grist(BUILD, 65).grist(AMBER, 22).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.TELESCOPIC_BEATDOWN_BRUISER.get()).grist(BUILD, 90).grist(CAULK, 22).grist(RUST, 5).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.BARBERS_MAGIC_TOUCH.get()).grist(BUILD, 135).grist(SHALE, 68).grist(GARNET, 41).grist(GOLD, 3).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.ION_DESTABILIZER.get()).grist(BUILD, 2572).grist(AMBER, 857).grist(RUBY, 650).grist(URANIUM, 85).build(recipeSaver);
		
		GristCostRecipeBuilder.of(MSItems.WAND.get()).grist(BUILD, 20).build(recipeSaver);
		
		GristCostRecipeBuilder.of(MSItems.CONDUCTORS_BATON.get()).grist(BUILD, 75).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.SHARP_NOTE.get()).grist(BUILD, 100).grist(RUBY, 50).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.URANIUM_BATON.get()).grist(BUILD, 150).grist(URANIUM, 60).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.WIND_WAKER.get()).grist(CHALK, 155).grist(COBALT, 62).grist(QUARTZ, 20).grist(DIAMOND, 10).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.CELESTIAL_FULCRUM.get()).grist(CHALK, 418).grist(MERCURY, 250).grist(AMETHYST, 250).grist(GOLD, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.HYMN_FOR_HORRORTERRORS.get()).grist(BUILD, 773).grist(CHALK, 386).grist(SHALE, 386).grist(AMETHYST, 232).grist(GARNET, 116).build(recipeSaver);
		
		GristCostRecipeBuilder.of(MSItems.SILVER_SPOON.get()).grist(BUILD, 10).grist(MERCURY, 8).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.MELONBALLER.get()).grist(BUILD, 53).grist(RUBY, 16).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.SIGHTSEEKER.get()).grist(BUILD, 53).grist(RUST, 16).grist(CAULK, 27).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.TERRAIN_FLATENATOR.get()).grist(BUILD, 170).grist(RUST, 4).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.NOSFERATU_SPOON.get()).grist(BUILD, 380).grist(MERCURY, 15).grist(AMETHYST, 46).grist(GARNET, 34).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.THRONGLER.get()).grist(CAULK, 423).grist(QUARTZ, 113).grist(GARNET, 85).grist(ARTIFACT, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.WET_MEAT_SHIT_THRONGLER.get()).grist(CAULK, 500).grist(IODINE, 1).grist(QUARTZ, 266).grist(RUST, 200).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.CROCKER_SPOON.get()).grist(BUILD, 850).grist(IODINE, 170).grist(CHALK, 170).grist(RUBY, 51).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.CROCKER_FORK.get()).grist(BUILD, 850).grist(IODINE, 170).grist(CHALK, 170).grist(RUBY, 51).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.SKAIA_FORK.get()).grist(BUILD, 225).grist(QUARTZ, 364).grist(GOLD, 58).grist(AMETHYST, 63).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.SKAIAN_CROCKER_ROCKER.get()).grist(BUILD, 1821).grist(CHALK, 2732).grist(IODINE, 2732).grist(QUARTZ, 2914).grist(RUBY, 1639).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.CANDY_FORK.get()).grist(BUILD, 42).grist(IODINE, 18).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.TUNING_FORK.get()).grist(BUILD, 27).grist(RUST, 16).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.ELECTRIC_FORK.get()).grist(BUILD, 180).grist(RUST, 122).grist(MERCURY, 144).grist(URANIUM, 63).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.EATING_FORK_GEM.get()).grist(BUILD, 99).grist(AMBER, 7).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.DEVIL_FORK.get()).grist(BUILD, 666).grist(SULFUR, 821).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.GOLDEN_SPORK.get()).grist(BUILD, 19).grist(GOLD, 11).grist(DIAMOND, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.MEATFORK.get()).grist(BUILD, 150).grist(IODINE, 75).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.BIDENT.get()).grist(BUILD, 239).grist(GARNET, 11).grist(AMETHYST, 11).grist(QUARTZ, 8).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.DOUBLE_ENDED_TRIDENT.get()).grist(BUILD, 240).grist(COBALT, 448).grist(DIAMOND, 32).grist(AMETHYST, 64).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.EDISONS_FURY.get()).grist(RUST, 280).grist(GARNET, 240).grist(MERCURY, 267).grist(URANIUM, 27).grist(TAR, 67).build(recipeSaver);
		
		GristCostRecipeBuilder.of(MSItems.BATLEACKS.get()).grist(BUILD, 0).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.COPSE_CRUSHER.get()).grist(BUILD, 62).grist(RUST, 28).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.QUENCH_CRUSHER.get()).grist(BUILD, 134).grist(IODINE, 15).grist(CAULK, 15).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.MELONSBANE.get()).grist(BUILD, 216).grist(IODINE, 24).grist(CAULK, 24).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.CROP_CHOP.get()).grist(BUILD, 78).grist(IODINE, 39).grist(AMBER, 22).grist(GOLD, 9).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.THE_LAST_STRAW.get()).grist(BUILD, 180).grist(GOLD, 36).grist(IODINE, 90).grist(AMBER, 45).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.BATTLEAXE.get()).grist(BUILD, 272).grist(RUST, 122).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.CANDY_BATTLEAXE.get()).grist(IODINE, 93).grist(GOLD, 19).grist(CHALK, 47).grist(AMBER, 16).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.CHOCO_LOCO_WOODSPLITTER.get()).grist(BUILD, 100).grist(IODINE, 75).grist(GOLD, 25).grist(CHALK, 38).grist(AMBER, 25).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.STEEL_EDGE_CANDYCUTTER.get()).grist(BUILD, 400).grist(RUST, 180).grist(IODINE, 250).grist(GOLD, 60).grist(CHALK, 100).grist(AMBER, 50).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.BLACKSMITH_BANE.get()).grist(BUILD, 82).grist(RUST, 49).grist(TAR, 82).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.REGIAXE.get()).grist(AMETHYST, 32).grist(TAR, 92).grist(GOLD, 32).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.GOTHY_AXE.get()).grist(RUST, 77).grist(TAR, 85).grist(RUBY, 77).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.SURPRISE_AXE.get()).grist(BUILD, 159).grist(RUST, 64).grist(AMBER, 53).grist(IODINE, 27).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.SHOCK_AXE.get()).grist(BUILD, 675).grist(RUST, 270).grist(GOLD, 135).grist(AMBER, 113).grist(URANIUM, 45).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.SCRAXE.get()).grist(BUILD, 272).grist(TAR, 102).grist(RUST, 41).grist(RUBY, 20).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.LORENTZ_DISTRANSFORMATIONER.get()).grist(BUILD, 640).grist(URANIUM, 96).grist(TAR, 160).grist(RUST, 48).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.PISTON_POWERED_POGO_AXEHAMMER.get()).grist(BUILD, 304).grist(SHALE, 152).grist(RUST, 46).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.RUBY_CROAK.get()).grist(BUILD, 2800).grist(GARNET, 630).grist(RUBY, 420).grist(DIAMOND, 140).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.HEPHAESTUS_LUMBERJACK.get()).grist(BUILD, 2150).grist(GOLD, 258).grist(RUBY, 258).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.FISSION_FOCUSED_FAULT_FELLER.get()).grist(BUILD, 1360).grist(SHALE, 510).grist(URANIUM, 136).grist(RUST, 102).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.BISECTOR.get()).grist(BUILD, 239).grist(GARNET, 81).grist(AMETHYST, 81).grist(QUARTZ, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.FINE_CHINA_AXE.get()).grist(MARBLE, 269).grist(SHALE, 84).build(recipeSaver);
		
		GristCostRecipeBuilder.of(MSItems.BISICKLE.get()).grist(BUILD, 35).grist(RUST, 11).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.OW_THE_EDGE.get()).grist(ARTIFACT, -1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.THORNY_SUBJECT.get()).grist(BUILD, 87).grist(AMETHYST, 9).grist(RUBY, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.SNOW_WHITE_DREAM.get()).grist(CAULK, 219).grist(CHALK, 109).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.HOMES_SMELL_YA_LATER.get()).grist(BUILD, 88).grist(AMBER, 44).grist(AMETHYST, 13).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.HEMEOREAPER.get()).grist(BUILD, 195).grist(RUST, 23).grist(GARNET, 23).grist(IODINE, 20).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.FUDGESICKLE.get()).grist(IODINE, 30).grist(AMBER, 25).grist(CHALK, 20).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.REGISICKLE.get()).grist(AMETHYST, 32).grist(TAR, 77).grist(GOLD, 32).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.HERETICUS_AURURM.get()).grist(GOLD, 240).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.CLAW_SICKLE.get()).grist(BUILD, 264).grist(IODINE, 116).grist(CHALK, 66).grist(GARNET, 10).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.CLAW_OF_NRUBYIGLITH.get()).grist(BUILD, 1056).grist(AMETHYST, 264).grist(CHALK, 44).grist(GARNET, 26).grist(SHALE, 44).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.CANDY_SICKLE.get()).grist(IODINE, 45).grist(GOLD, 12).grist(CHALK, 45).grist(AMBER, 15).build(recipeSaver);
		
		GristCostRecipeBuilder.of(MSItems.SCYTHE.get()).grist(BUILD, 56).grist(RUST, 25).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.MARASCHINO_CHERRY_SCYTHE.get()).grist(BUILD, 56).grist(IODINE, 28).grist(RUBY, 17).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.PROSPECTING_PICKSCYTHE.get()).grist(BUILD, 165).grist(GOLD, 33).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.EIGHTBALL_SCYTHE.get()).grist(BUILD, 715).grist(TAR, 743).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.TIME_FLAYER.get()).grist(BUILD, 1148).grist(TAR, 574).grist(COBALT, 459).grist(DIAMOND, 230).grist(GOLD, 230).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.DESTINY_DECIMATOR.get()).grist(TAR, 270).grist(URANIUM, 108).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.SUNRAY_HARVESTER.get()).grist(TAR, 271).grist(GOLD, 434).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.GREEN_SUN_RAYREAPER.get()).grist(BUILD, 18533).grist(TAR, 9266).grist(URANIUM, 22240).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.SKAITHE.get()).grist(BUILD, 2000).grist(QUARTZ, 2400).grist(AMETHYST, 512).grist(GOLD, 512).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.HELLBRINGERS_HOE_INACTIVE.get()).grist(BUILD, 420).grist(TAR, 420).grist(SULFUR, 168).grist(GARNET, 378).grist(RUBY, 252).build(recipeSaver);
		
		GristCostRecipeBuilder.of(MSItems.STALE_BAGUETTE.get()).grist(IODINE, 6).grist(AMBER, 4).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.GLUB_CLUB.get()).grist(BUILD, 10).grist(CAULK, 8).grist(AMBER, 3).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.NIGHT_CLUB.get()).grist(TAR, 8).grist(SHALE, 8).grist(COBALT, 2).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.NIGHTSTICK.get()).grist(TAR, 18).grist(MARBLE, 7).grist(GOLD, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.RED_EYES.get()).grist(TAR, 45).grist(SULFUR, 45).grist(AMETHYST, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.PRISMARINE_BASHER.get()).grist(BUILD, 27).grist(COBALT, 18).grist(MARBLE, 7).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.CLUB_ZERO.get()).grist(BUILD, 44).grist(COBALT, 26).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.POGO_CLUB.get()).grist(BUILD, 60).grist(SHALE, 35).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.BARBER_BASHER.get()).grist(BUILD, 53).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.METAL_BAT.get()).grist(BUILD, 36).grist(MERCURY, 22).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.CRICKET_BAT.get()).grist(BUILD, 26).grist(AMBER, 13).grist(IODINE, 26).grist(RUST, 8).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.CLOWN_CLUB.get()).grist(BUILD, 356).grist(MERCURY, 71).grist(COBALT, 71).grist(IODINE, 89).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.DOCTOR_DETERRENT.get()).grist(BUILD, 618).grist(AMETHYST, 556).grist(DIAMOND, 124).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.MACE.get()).grist(BUILD, 90).grist(RUST, 15).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.M_ACE.get()).grist(BUILD, 135).grist(MARBLE, 18).grist(CHALK, 30).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.DESOLATOR_MACE.get()).grist(BUILD, 595).grist(RUST, 102).grist(TAR, 170).grist(MARBLE, 170).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.BLAZING_GLORY.get()).grist(BUILD, 470).grist(SULFUR, 282).grist(GOLD, 5).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.SPIKED_CLUB.get()).grist(BUILD, 40).grist(GARNET, 2).grist(IODINE, 3).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.RUBIKS_MACE.get()).grist(BUILD, 80).grist(COBALT, 64).grist(MARBLE, 32).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.M_ACE_OF_CLUBS.get()).grist(BUILD, 90).grist(TAR, 10).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.HOME_GROWN_MACE.get()).grist(BUILD, 125).grist(IODINE, 10).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.TOFFEE_CLUB.get()).grist(BUILD, 150).grist(CHALK, 30).grist(IODINE, 20).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.CARNIE_CLUB.get()).grist(TAR, 182).grist(SULFUR, 145).grist(GARNET, 72).grist(RUST, 110).grist(GOLD, 1).build(recipeSaver);
		
		GristCostRecipeBuilder.of(MSItems.HORSE_HITCHER.get()).grist(BUILD, 1080).grist(QUARTZ, 48).grist(URANIUM, 3).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.CLUB_OF_FELONY.get()).grist(BUILD, 1080).grist(COBALT, 48).grist(URANIUM, 3).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.CUESTICK.get()).grist(BUILD, 1080).grist(MERCURY, 48).grist(URANIUM, 3).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.TV_ANTENNA.get()).grist(BUILD, 1080).grist(RUBY, 48).grist(URANIUM, 3).build(recipeSaver);
		
		GristCostRecipeBuilder.of(MSItems.WIZARD_STAFF.get()).grist(BUILD, 46).grist(AMBER, 46).grist(CAULK, 46).grist(MARBLE, 46).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.WATER_STAFF.get()).grist(CAULK, 85).grist(MERCURY, 85).grist(AMETHYST, 85).grist(GARNET, 85).grist(QUARTZ, 5).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.FIRE_STAFF.get()).grist(AMBER, 116).grist(MERCURY, 116).grist(SULFUR, 116).grist(RUBY, 116).grist(QUARTZ, 5).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.PRIME_STAFF.get()).grist(BUILD, 161973).grist(QUARTZ, 64789).grist(DIAMOND, 32395).grist(ARTIFACT, 311812).build(recipeSaver);
		
		GristCostRecipeBuilder.of(MSItems.VAUDEVILLE_HOOK.get()).grist(IODINE, 6).grist(TAR, 4).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.BARBERS_BEST_FRIEND.get()).grist(SHALE, 5).grist(MERCURY, 12).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.UPPER_CRUST_CRUST_CANE.get()).grist(BUILD, 12).grist(IODINE, 8).grist(AMBER, 6).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.KISSY_CUTIE_HEART_HITTER.get()).grist(BUILD, 160).grist(MARBLE, 64).grist(COBALT, 20).grist(GARNET, 20).grist(GOLD, 69).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.KISSY_CUTIE_HEART_SPLITTER.get()).grist(BUILD, 160).grist(MARBLE, 64).grist(COBALT, 20).grist(GARNET, 20).grist(GOLD, 69).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.MUTANT_CUTIE_CELL_PUTTER.get()).grist(BUILD, 580).grist(TAR, 290).grist(MARBLE, 174).grist(URANIUM, 30).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.MUTANT_CUTIE_CELL_CUTTER.get()).grist(BUILD, 580).grist(TAR, 290).grist(MARBLE, 174).grist(URANIUM, 30).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.ZEPHYR_CANE.get()).grist(BUILD, 840).grist(SHALE, 180).grist(CAULK, 180).grist(AMBER, 120).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.SPEAR_CANE.get()).grist(BUILD, 70).grist(MERCURY, 12).grist(AMBER, 10).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.PARADISES_PORTABELLO.get()).grist(BUILD, 18).grist(IODINE, 5).grist(RUBY, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.REGI_CANE.get()).grist(AMETHYST, 32).grist(TAR, 72).grist(GOLD, 32).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.POGO_CANE.get()).grist(BUILD, 66).grist(SHALE, 22).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.URANIUM_POWERED_STICK.get()).grist(URANIUM, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.CANDY_CANE.get()).grist(IODINE, 24).grist(CHALK, 20).grist(AMBER, 16).build(recipeSaver); //recipe is slightly lower than what is should be for the sharp variety
		GristCostRecipeBuilder.of(MSItems.SHARP_CANDY_CANE.get()).grist(IODINE, 24).grist(CHALK, 20).grist(AMBER, 16).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.PRIM_AND_PROPER_WALKING_POLE.get()).grist(BUILD, 15).grist(IODINE, 10).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.LESS_PROPER_WALKING_STICK.get()).grist(BUILD, 75).grist(RUST, 18).grist(IODINE, 8).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.ROCKEFELLERS_WALKING_BLADECANE.get()).grist(BUILD, 413).grist(RUST, 222).grist(TAR, 22).grist(DIAMOND, 2).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.DRAGON_CANE.get()).grist(BUILD, 7166).grist(RUBY, 666).grist(RUST, 111).grist(CHALK, 222).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.CHANCEWYRMS_EXTRA_FORTUNATE_STABBING_IMPLEMENT.get()).grist(BUILD, 41300).grist(COBALT, 22254).build(recipeSaver);
		
		GristCostRecipeBuilder.of(MSItems.NEEDLE_WAND.get()).grist(DIAMOND, 20).grist(CHALK, 30).grist(GARNET, 40).grist(GOLD, 16).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.ARTIFUCKER.get()).grist(ARTIFACT, -100).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.POINTER_WAND.get()).grist(BUILD, 200).grist(CAULK, 40).grist(AMBER, 50).grist(RUST, 30).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.POOL_CUE_WAND.get()).grist(QUARTZ, 50).grist(CHALK, 70).grist(TAR, 90).grist(URANIUM, 45).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.THORN_OF_OGLOGOTH.get()).grist(IODINE, 200).grist(CHALK, 160).grist(AMETHYST, 120).grist(GOLD, 100).grist(TAR, 64).build(recipeSaver);
		
		GristCostRecipeBuilder.of(MSItems.SBAHJARANG.get()).grist(ARTIFACT, 10).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.CLUBS_SUITARANG.get()).grist(BUILD, 20).grist(TAR, 4).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.DIAMONDS_SUITARANG.get()).grist(BUILD, 20).grist(TAR, 4).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.HEARTS_SUITARANG.get()).grist(BUILD, 20).grist(TAR, 4).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.SPADES_SUITARANG.get()).grist(BUILD, 20).grist(TAR, 4).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.CHAKRAM.get()).grist(BUILD, 200).grist(RUBY, 10).grist(RUST, 20).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.UMBRAL_INFILTRATOR.get()).grist(BUILD, 600).grist(TAR, 450).grist(DIAMOND, 55).grist(MERCURY, 55).grist(QUARTZ, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.SORCERERS_PINBALL.get()).grist(BUILD, 200).grist(MERCURY, 30).grist(IODINE, 5).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.MUSIC_SWORD.get()).grist(BUILD, 18760).grist(MARBLE, 1876 ).grist(RUST, 1407).grist(AMBER, 2345 ).build(recipeSaver);
		
		GristCostRecipeBuilder.of(MSItems.CAPTCHAROID_CAMERA.get()).grist(BUILD, 5000).grist(CAULK, 500).grist(GOLD, 500).grist(MARBLE, 500).grist(MERCURY, 500).grist(SHALE, 500).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.TRANSPORTALIZER.get()).grist(BUILD, 350).grist(AMETHYST, 27).grist(RUST, 36).grist(URANIUM, 18).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.QUEUESTACK_MODUS_CARD.get()).grist(BUILD, 140).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.TREE_MODUS_CARD.get()).grist(BUILD, 400).grist(AMBER, 35).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.HASHMAP_MODUS_CARD.get()).grist(BUILD, 280).grist(RUBY, 23).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.SET_MODUS_CARD.get()).grist(BUILD, 350).grist(MERCURY, 29).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.IRON_BOAT.get()).grist(RUST, 30).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.GOLD_BOAT.get()).grist(GOLD, 30).build(recipeSaver);
		
		GristCostRecipeBuilder.of(MSBlocks.LAPTOP.get()).grist(BUILD, 80).grist(RUST, 30).grist(QUARTZ, 2).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.CROCKERTOP.get()).grist(BUILD, 20).grist(RUST, 5).grist(MERCURY, 5).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.HUBTOP.get()).grist(BUILD, 60).grist(RUST, 10).grist(URANIUM, 10).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.LUNCHTOP.get()).grist(BUILD, 30).grist(RUST, 10).grist(MARBLE, 20).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.OLD_COMPUTER.get()).grist(BUILD, 80).grist(RUST, 30).grist(SULFUR, 10).build(recipeSaver);
		
		GristCostRecipeBuilder.of(MSBlocks.CASSETTE_PLAYER.get()).grist(BUILD, 400).grist(MARBLE, 40).grist(RUST, 20).grist(AMBER, 20).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(MSItems.MUSIC_DISC_EMISSARY_OF_DANCE.get()).grist(BUILD, 15).grist(CAULK, 8).grist(GOLD, 5).grist(MARBLE, 5).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(MSItems.MUSIC_DISC_DANCE_STAB_DANCE.get()).grist(BUILD, 15).grist(CAULK, 8).grist(COBALT, 5).grist(SULFUR, 5).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(MSItems.MUSIC_DISC_RETRO_BATTLE.get()).grist(BUILD, 15).grist(CAULK, 8).grist(QUARTZ, 5).grist(RUST, 5).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(MSItems.CASSETTE_13.get()).grist(BUILD, 15).grist(CAULK, 8).grist(AMBER, 5).grist(CHALK, 5).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(MSItems.CASSETTE_BLOCKS.get()).grist(BUILD, 15).grist(CAULK, 8).grist(RUBY, 5).grist(RUST, 5).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(MSItems.CASSETTE_CAT.get()).grist(BUILD, 15).grist(CAULK, 8).grist(URANIUM, 5).grist(SHALE, 5).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(MSItems.CASSETTE_CHIRP.get()).grist(BUILD, 15).grist(CAULK, 8).grist(RUBY, 5).grist(GARNET, 5).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(MSItems.CASSETTE_FAR.get()).grist(BUILD, 15).grist(CAULK, 8).grist(URANIUM, 5).grist(SULFUR, 5).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(MSItems.CASSETTE_MALL.get()).grist(BUILD, 15).grist(CAULK, 8).grist(AMETHYST, 5).grist(TAR, 5).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(MSItems.CASSETTE_MELLOHI.get()).grist(BUILD, 15).grist(CAULK, 8).grist(MARBLE, 5).grist(SHALE, 5).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(MSItems.CASSETTE_EMISSARY.get()).grist(BUILD, 15).grist(CAULK, 8).grist(GOLD, 5).grist(MARBLE, 5).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(MSItems.CASSETTE_DANCE_STAB.get()).grist(BUILD, 15).grist(CAULK, 8).grist(COBALT, 5).grist(SULFUR, 5).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(MSItems.CASSETTE_RETRO_BATTLE.get()).grist(BUILD, 15).grist(CAULK, 8).grist(QUARTZ, 5).grist(RUST, 5).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(MSItems.CASSETTE_11.get()).grist(BUILD, 15).grist(CAULK, 8).grist(TAR, 5).grist(MERCURY, 5).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(MSItems.CASSETTE_PIGSTEP.get()).grist(BUILD, 15).grist(CAULK, 8).grist(SULFUR, 5).grist(RUBY, 5).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(MSItems.CASSETTE_STAL.get()).grist(BUILD, 15).grist(CAULK, 8).grist(TAR, 5).grist(RUST, 5).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(MSItems.CASSETTE_STRAD.get()).grist(BUILD, 15).grist(CAULK, 8).grist(TAR, 5).grist(CHALK, 5).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(MSItems.CASSETTE_WAIT.get()).grist(BUILD, 15).grist(CAULK, 8).grist(COBALT, 5).grist(DIAMOND, 5).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(MSItems.CASSETTE_WARD.get()).grist(BUILD, 15).grist(CAULK, 8).grist(URANIUM, 5).grist(MERCURY, 5).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(MSItems.CASSETTE_OTHERSIDE.get()).grist(BUILD, 60).grist(CAULK, 32).grist(URANIUM, 20).grist(COBALT, 20).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(MSItems.CASSETTE_5.get()).grist(BUILD, 15).grist(CAULK, 8).grist(DIAMOND, 5).grist(COBALT, 5).buildFor(recipeSaver, Minestuck.MOD_ID);
		
		GristCostRecipeBuilder.of(MSBlocks.GLOWING_MUSHROOM.get()).grist(BUILD, 5).grist(SHALE, 3).grist(MERCURY, 2).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.GLOWING_MUSHROOM_VINES.get()).grist(BUILD, 8).grist(SHALE, 4).grist(MERCURY, 3).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.GLOWING_LOG.get()).grist(BUILD, 8).grist(AMBER, 4).grist(MERCURY, 4).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.GLOWING_PLANKS.get()).grist(BUILD, 2).grist(AMBER, 1).grist(MERCURY, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.GLOWY_GOOP.get()).grist(BUILD, 8).grist(CAULK, 8).grist(MERCURY, 4).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.COAGULATED_BLOOD.get()).grist(GARNET, 8).grist(IODINE, 8).build(recipeSaver);
		
		GristCostRecipeBuilder.of(MSBlocks.PIPE.get()).grist(BUILD, 8).grist(RUST, 4).grist(MERCURY, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.PIPE_INTERSECTION.get()).grist(BUILD, 4).grist(MARBLE, 4).grist(CHALK, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.PARCEL_PYXIS.get()).grist(BUILD, 25).grist(RUST, 10).grist(MERCURY, 5).grist(CHALK,2).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.PYXIS_LID.get()).grist(BUILD, 2).grist(RUST, 1).build(recipeSaver);
		
		GristCostRecipeBuilder.of(MSBlocks.TRAJECTORY_BLOCK.get()).grist(URANIUM, 2).grist(AMBER, 40).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.STAT_STORER.get()).grist(GARNET, 40).grist(MARBLE, 14).grist(DIAMOND, 20).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.REMOTE_OBSERVER.get()).grist(GARNET, 60).grist(MARBLE, 16).grist(AMBER, 10).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.PLATFORM_GENERATOR.get()).grist(TAR, 200).grist(COBALT, 75).grist(AMETHYST, 5).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.PLATFORM_RECEPTACLE.get()).grist(QUARTZ, 5).grist(MERCURY, 5).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.FALL_PAD.get()).grist(CAULK, 5).grist(SHALE, 2).build(recipeSaver);
		
		GristCostRecipeBuilder.of(MSBlocks.PETRIFIED_LOG.get()).grist(BUILD, 4).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.FROST_PLANKS.get()).grist(BUILD, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.PETRIFIED_GRASS.get()).grist(BUILD, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.PETRIFIED_POPPY.get()).grist(BUILD, 1).grist(IODINE, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.BLOOMING_CACTUS.get()).grist(AMBER, 1).grist(IODINE, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.GLOWFLOWER.get()).grist(CHALK, 10).grist(AMBER, 2).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.GOLD_SEEDS.get()).grist(GOLD, 3).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.COCOA_WART.get()).grist(AMBER, 5).build(recipeSaver);
		
		GristCostRecipeBuilder.of(MSItems.EMERALD_SWORD.get()).grist(QUARTZ, 44).grist(DIAMOND, 76).grist(RUBY, 72).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.EMERALD_AXE.get()).grist(AMBER, 40).grist(DIAMOND, 73).grist(RUBY, 70).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.EMERALD_PICKAXE.get()).grist(RUST, 42).grist(DIAMOND, 72).grist(RUBY, 70).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.EMERALD_SHOVEL.get()).grist(CHALK, 40).grist(DIAMOND, 70).grist(RUBY, 66).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.EMERALD_HOE.get()).grist(IODINE, 32).grist(DIAMOND, 50).grist(RUBY, 45).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.MINE_AND_GRIST.get()).grist(BUILD, 10000).grist(DIAMOND, 1).grist(RUBY, 1).grist(AMETHYST, 1).grist(QUARTZ, 1).build(recipeSaver);
		
		GristCostRecipeBuilder.of(MSItems.PRISMARINE_HELMET.get()).grist(BUILD, 75).grist(COBALT, 30).grist(MARBLE, 15).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.PRISMARINE_CHESTPLATE.get()).grist(BUILD, 120).grist(COBALT, 48).grist(MARBLE, 24).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.PRISMARINE_LEGGINGS.get()).grist(BUILD, 105).grist(COBALT, 42).grist(MARBLE, 21).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.PRISMARINE_BOOTS.get()).grist(BUILD, 60).grist(COBALT, 24).grist(MARBLE, 12).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.IRON_LASS_GLASSES.get()).grist(BUILD, 3500).grist(RUBY, 700).grist(AMBER, 350).grist(URANIUM, 125).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.IRON_LASS_CHESTPLATE.get()).grist(BUILD, 3500).grist(RUBY, 700).grist(AMBER, 350).grist(URANIUM, 125).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.IRON_LASS_SKIRT.get()).grist(BUILD, 3500).grist(RUBY, 700).grist(AMBER, 350).grist(URANIUM, 125).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.IRON_LASS_SHOES.get()).grist(BUILD, 3500).grist(RUBY, 700).grist(AMBER, 350).grist(URANIUM, 125).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.PROSPIT_CIRCLET.get()).grist(CHALK, 30).grist(AMBER, 5).grist(QUARTZ, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.PROSPIT_SHIRT.get()).grist(CHALK, 45).grist(AMBER, 5).grist(QUARTZ, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.PROSPIT_PANTS.get()).grist(CHALK, 40).grist(AMBER, 5).grist(QUARTZ, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.PROSPIT_SHOES.get()).grist(CHALK, 25).grist(AMBER, 5).grist(QUARTZ, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.DERSE_CIRCLET.get()).grist(CHALK, 30).grist(SHALE, 5).grist(MERCURY, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.DERSE_SHIRT.get()).grist(CHALK, 45).grist(SHALE, 5).grist(MERCURY, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.DERSE_PANTS.get()).grist(CHALK, 40).grist(SHALE, 5).grist(MERCURY, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.DERSE_SHOES.get()).grist(CHALK, 25).grist(SHALE, 5).grist(MERCURY, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.AMPHIBEANIE.get()).grist(CAULK, 20).grist(MARBLE, 10).grist(AMBER, 2).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.NOSTRILDAMUS.get()).grist(TAR, 20).grist(MARBLE, 10).grist(GOLD, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.PONYTAIL.get()).grist(AMBER, 20).grist(MARBLE, 3).grist(QUARTZ, 2).build(recipeSaver);
		
		GristCostRecipeBuilder.of(MSItems.CARDBOARD_TUBE.get()).grist(BUILD, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.GLOWYSTONE_DUST.get()).grist(BUILD, 1).grist(AMBER, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.RAW_CRUXITE.get()).grist(BUILD, 3).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.RAW_URANIUM.get()).grist(URANIUM, 3).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.GOLDEN_GRASSHOPPER.get()).grist(GOLD, 4000).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.BUG_NET.get()).grist(BUILD, 40).grist(CHALK, 25).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.STONE_TABLET.get()).grist(BUILD, 30).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.HORSE_CLOCK.getMainBlock()).grist(BUILD, 500).grist(GOLD, 50).buildFor(recipeSaver, Minestuck.MOD_ID);
		
		GristCostRecipeBuilder.of(MSItems.SPORK.get()).grist(BUILD, 50).grist(RUST, 9).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.CANDY_CORN.get()).grist(CHALK, 1).grist(SULFUR, 1).grist(IODINE, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.TUIX_BAR.get()).grist(BUILD, 5).grist(IODINE, 1).build(recipeSaver);
		for(GristType type : GristTypes.REGISTRY)
		{
			if(type.getIdOrThrow().getNamespace().equals(Minestuck.MOD_ID))
				GristCostRecipeBuilder.of(type.getCandyItem().getItem()).grist(type, 3).build(recipeSaver);
		}
		GristCostRecipeBuilder.of(MSItems.SOPOR_SLIME_PIE.get()).grist(URANIUM, 8).grist(RUST, 2).grist(MERCURY, 6).grist(CHALK, 5).grist(ARTIFACT, 3).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.APPLE_JUICE.get()).grist(AMBER, 4).grist(SULFUR, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.TAB.get()).grist(COBALT, 1).grist(IODINE, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.ORANGE_FAYGO.get()).grist(COBALT, 1).grist(AMBER, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.CANDY_APPLE_FAYGO.get()).grist(COBALT, 1).grist(SHALE, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.FAYGO_COLA.get()).grist(COBALT, 1).grist(IODINE, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.COTTON_CANDY_FAYGO.get()).grist(COBALT, 1).grist(AMETHYST, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.CREME_SODA_FAYGO.get()).grist(COBALT, 1).grist(CHALK, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.GRAPE_FAYGO.get()).grist(COBALT, 1).grist(SHALE, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.MOON_MIST_FAYGO.get()).grist(COBALT, 1).grist(AMBER, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.PEACH_FAYGO.get()).grist(COBALT, 1).grist(GARNET, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.REDPOP_FAYGO.get()).grist(COBALT, 1).grist(SULFUR, 1).build(recipeSaver);
		
		GristCostRecipeBuilder.of(MSItems.PHLEGM_GUSHERS.get()).grist(BUILD, 24).grist(SHALE, 30).grist(MERCURY, 18).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.SORROW_GUSHERS.get()).grist(TAR, 50).build(recipeSaver);
		
		GristCostRecipeBuilder.of(MSItems.BUG_ON_A_STICK.get()).grist(BUILD, 1).grist(CHALK, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.CHOCOLATE_BEETLE.get()).grist(CHALK, 2).grist(IODINE, 4).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.CONE_OF_FLIES.get()).grist(BUILD, 2).grist(AMBER, 2).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.GRASSHOPPER.get()).grist(IODINE, 3).grist(AMBER, 7).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.CICADA.get()).grist(CAULK, 3).grist(MARBLE, 8).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.JAR_OF_BUGS.get()).grist(BUILD, 5).grist(CHALK, 3).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.ONION.get()).grist(IODINE, 3).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.IRRADIATED_STEAK.get()).grist(IODINE, 12).grist(URANIUM, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.ROCK_COOKIE.get()).grist(BUILD, 10).grist(MARBLE, 5).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.WOODEN_CARROT.get()).grist(BUILD, 8).grist(AMBER, 3).grist(CHALK, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.STRAWBERRY_CHUNK.get()).grist(AMBER, 2).grist(BUILD, 1).grist(RUBY, 2).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.FOOD_CAN.get()).grist(IODINE, 10).grist(TAR, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.DESERT_FRUIT.get()).grist(AMBER, 1).grist(CAULK, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.FUNGAL_SPORE.get()).grist(IODINE, 2).grist(AMBER, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.SPOREO.get()).grist(IODINE, 3).grist(AMBER, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.MOREL_MUSHROOM.get()).grist(IODINE, 20).grist(AMBER, 12).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.FRENCH_FRY.get()).grist(BUILD, 1).grist(IODINE, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.SURPRISE_EMBRYO.get()).grist(AMBER, 15).grist(IODINE, 5).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.UNKNOWABLE_EGG.get()).grist(AMBER, 15).grist(AMETHYST, 15).grist(TAR, 1).build(recipeSaver);
		
		GristCostRecipeBuilder.of(MSBlocks.PRIMED_TNT.get()).grist(BUILD, 8).grist(CHALK, 10).grist(SULFUR, 14).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.UNSTABLE_TNT.get()).grist(BUILD, 5).grist(CHALK, 11).grist(SULFUR, 15).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.INSTANT_TNT.get()).grist(BUILD, 6).grist(CHALK, 11).grist(SULFUR, 17).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.STONE_EXPLOSIVE_BUTTON.get()).grist(BUILD, 7).grist(CHALK, 5).grist(SULFUR, 8).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.WOODEN_EXPLOSIVE_BUTTON.get()).grist(BUILD, 7).grist(CHALK, 5).grist(SULFUR, 8).build(recipeSaver);
		
		GristCostRecipeBuilder.of(MSBlocks.TAR_SHADE_BRICKS.get()).grist(BUILD, 2).grist(TAR, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.FROST_TILE.get()).grist(BUILD, 2).grist(COBALT, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.CAST_IRON.get()).grist(BUILD, 3).grist(RUST, 2).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.STEEL_BEAM.get()).grist(BUILD, 2).grist(RUST, 3).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.BLACK_SAND.get()).grist(BUILD, 2).grist(SULFUR, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.IGNEOUS_STONE.get()).grist(BUILD, 2).grist(TAR, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.IGNEOUS_SPIKE.get()).grist(BUILD, 1).grist(TAR, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.PUMICE_STONE.get()).grist(BUILD, 2).grist(TAR, 1).build(recipeSaver);
		
		GristCostRecipeBuilder.of(MSBlocks.COARSE_END_STONE.get()).grist(BUILD, 4).grist(CAULK, 3).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.CHALK.get()).grist(BUILD, 2).grist(CHALK, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.CARVED_LOG.get()).grist(BUILD, 6).grist(AMBER, 2).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.CARVED_WOODEN_LEAF.get()).grist(BUILD, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.UNCARVED_WOOD.get()).grist(BUILD, 4).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.CHIPBOARD.get()).grist(BUILD, 4).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.WOOD_SHAVINGS.get()).grist(BUILD, 4).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.WOODEN_GRASS.get()).grist(BUILD, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.TREATED_UNCARVED_WOOD.get()).grist(BUILD, 3).grist(AMBER, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.TREATED_CHIPBOARD.get()).grist(BUILD, 3).grist(AMBER, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.TREATED_WOOD_SHAVINGS.get()).grist(BUILD, 3).grist(AMBER, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.TREATED_WOODEN_GRASS.get()).grist(BUILD, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.LACQUERED_UNCARVED_WOOD.get()).grist(BUILD, 3).grist(AMBER, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.LACQUERED_CHIPBOARD.get()).grist(BUILD, 3).grist(AMBER, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.LACQUERED_WOOD_SHAVINGS.get()).grist(BUILD, 3).grist(AMBER, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.LACQUERED_WOODEN_MUSHROOM.get()).grist(BUILD, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.WOODEN_LAMP.get()).grist(AMBER, 4).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.END_GRASS.get()).grist(BUILD, 4).grist(CAULK, 3).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.SINGED_FOLIAGE.get()).grist(AMBER, 3).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.SINGED_GRASS.get()).grist(AMBER, 3).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.DEAD_FOLIAGE.get()).grist(AMBER, 2).grist(SULFUR, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.SANDY_GRASS.get()).grist(AMBER, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.SULFUR_BUBBLE.get()).grist(SULFUR, 4).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.FLOWERY_VINE_LOG.get()).grist(BUILD, 7).grist(IODINE, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.FROST_LOG.get()).grist(BUILD, 7).grist(COBALT, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.NATIVE_SULFUR.get()).grist(SULFUR, 30).build(recipeSaver);
		
		GristCostRecipeBuilder.of(MSItems.SBAHJ_POSTER.get()).grist(BUILD, 4).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.CREW_POSTER.get()).grist(TAR, 3).grist(RUST, 2).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.FLARP_MANUAL.get()).grist(AMBER, 10).grist(TAR, 4).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.SASSACRE_TEXT.get()).grist(CAULK, 10).grist(MERCURY, 4).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.WISEGUY.get()).grist(CHALK, 10).grist(SULFUR, 4).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.TABLESTUCK_MANUAL.get()).grist(MARBLE, 10).grist(COBALT, 4).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.TILLDEATH_HANDBOOK.get()).grist(TAR, 10).grist(AMETHYST, 4).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.BINARY_CODE.get()).grist(IODINE, 10).grist(SHALE, 4).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.NONBINARY_CODE.get()).grist(BUILD, 10).grist(URANIUM, 4).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.THRESH_DVD.get()).grist(IODINE, 3).grist(AMETHYST, 2).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.GAMEBRO_MAGAZINE.get()).grist(CHALK, 3).grist(GARNET, 2).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.GAMEGRL_MAGAZINE.get()).grist(CHALK, 3).grist(AMBER, 2).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.ICE_SHARD.get()).grist(COBALT, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.BATTERY.get()).grist(GOLD, 4).grist(URANIUM, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.INK_SQUID_PRO_QUO.get()).grist(TAR, 8).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.HORN.get()).grist(BUILD, 4).grist(RUST, 4).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.CAKE_MIX.get()).grist(BUILD, 2).grist(IODINE, 2).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.BARBASOL.get()).grist(BUILD, 4).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.BARBASOL_BOMB.get()).grist(BUILD, 10).grist(SHALE, 10).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.CLOTHES_IRON.get()).grist(BUILD, 4).grist(SHALE, 4).grist(RUST, 4).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.CARVING_TOOL.get()).grist(BUILD, 10).grist(RUST, 2).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.CRUMPLY_HAT.get()).grist(BUILD, 20).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.MINI_FROG_STATUE.get()).grist(BUILD, 30).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.MINI_WIZARD_STATUE.get()).grist(BUILD, 30).build(recipeSaver);
		
		GristCostRecipeBuilder.of(MSItems.SCALEMATE_APPLESCAB.get()).grist(BUILD, 8).grist(RUBY, 8).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.SCALEMATE_BERRYBREATH.get()).grist(BUILD, 8).grist(COBALT, 8).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.SCALEMATE_CINNAMONWHIFF.get()).grist(BUILD, 8).grist(IODINE, 8).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.SCALEMATE_HONEYTONGUE.get()).grist(BUILD, 8).grist(AMBER, 8).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.SCALEMATE_LEMONSNOUT.get()).grist(BUILD, 8).grist(AMBER, 8).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.SCALEMATE_PINESNORT.get()).grist(BUILD, 8).grist(COBALT, 8).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.SCALEMATE_PUCEFOOT.get()).grist(BUILD, 8).grist(GARNET, 8).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.SCALEMATE_PUMPKINSNUFFLE.get()).grist(BUILD, 8).grist(IODINE, 8).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.SCALEMATE_PYRALSPITE.get()).grist(BUILD, 8).grist(CAULK, 8).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.SCALEMATE_WITNESS.get()).grist(BUILD, 8).grist(URANIUM, 8).build(recipeSaver);
		
		GristCostRecipeBuilder.of(MSItems.PLUSH_SALAMANDER.get()).grist(SHALE, 6).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.PLUSH_NAKAGATOR.get()).grist(AMBER, 6).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.PLUSH_IGUANA.get()).grist(URANIUM, 6).build(recipeSaver);
		GristCostRecipeBuilder.of(MSItems.PLUSH_TURTLE.get()).grist(CHALK, 6).build(recipeSaver);
		
		GristCostRecipeBuilder.of(MSItems.PLUSH_MUTATED_CAT.get()).grist(BUILD, 4).grist(URANIUM,  1).build(recipeSaver);
		
		GristCostRecipeBuilder.of(MSItems.PARTICLE_ACCELERATOR.get()).grist(BUILD, 14).grist(URANIUM, 3).build(recipeSaver);
		
		GristCostRecipeBuilder.of(MSBlocks.WOODEN_CACTUS.get()).grist(BUILD, 7).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.BLUE_CAKE.get()).grist(SHALE, 24).grist(MERCURY, 6).grist(COBALT, 5).grist(DIAMOND, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.COLD_CAKE.get()).grist(COBALT, 15).grist(MARBLE, 12).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.RED_CAKE.get()).grist(RUST, 20).grist(CHALK, 9).grist(IODINE, 6).grist(GARNET, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.HOT_CAKE.get()).grist(SULFUR, 17).grist(IODINE, 10).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.REVERSE_CAKE.get()).grist(AMBER, 10).grist(CHALK, 24).grist(IODINE, 11).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.FUCHSIA_CAKE.get()).grist(AMETHYST, 85).grist(GARNET, 54).grist(IODINE, 40).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.NEGATIVE_CAKE.get()).grist(CAULK, 25).grist(SHALE, 19).grist(TAR, 22).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.CARROT_CAKE.get()).grist(CHALK, 20).grist(AMETHYST, 10).grist(MARBLE, 5).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.LARGE_CAKE.get()).grist(BUILD, 1).grist(IODINE, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.PINK_FROSTED_TOP_LARGE_CAKE.get()).grist(BUILD, 1).grist(IODINE, 1).grist(CHALK, 1).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.CHOCOLATEY_CAKE.get()).grist(AMBER, 20).grist(CHALK, 10).grist(IODINE, 5).grist(RUBY, 1).build(recipeSaver);
		
		GristCostRecipeBuilder.of(MSBlocks.CRUXTRUDER_LID.get()).grist(BUILD, 8).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.BLENDER.get()).grist(BUILD, 16).build(recipeSaver);
		GristCostRecipeBuilder.of(MSBlocks.SUSPICIOUS_CHISELED_MYCELIUM_BRICKS.get()).grist(BUILD, 2).build(recipeSaver);
		
		ContainerGristCostBuilder.of(MSItems.OIL_BUCKET.get()).grist(SHALE, 16).build(recipeSaver);
		ContainerGristCostBuilder.of(MSItems.BLOOD_BUCKET.get()).grist(GARNET, 8).grist(IODINE, 8).build(recipeSaver);
		ContainerGristCostBuilder.of(MSItems.BRAIN_JUICE_BUCKET.get()).grist(AMETHYST, 8).grist(CHALK, 8).build(recipeSaver);
		ContainerGristCostBuilder.of(MSItems.WATER_COLORS_BUCKET.get()).grist(AMETHYST, 4).grist(CHALK, 4).grist(GARNET, 4).grist(AMBER, 4).build(recipeSaver);
		ContainerGristCostBuilder.of(MSItems.ENDER_BUCKET.get()).grist(MERCURY, 8).grist(URANIUM, 8).build(recipeSaver);
		ContainerGristCostBuilder.of(MSItems.OBSIDIAN_BUCKET.get()).grist(BUILD, 4).grist(COBALT, 8).grist(TAR, 16).build(recipeSaver);
		ContainerGristCostBuilder.of(MSItems.CAULK_BUCKET.get()).grist(CAULK, 16).build(recipeSaver);
		ContainerGristCostBuilder.of(MSItems.MOLTEN_AMBER_BUCKET.get()).grist(AMBER, 16).build(recipeSaver);
		
		WildcardGristCostBuilder.of(MSItems.CAPTCHA_CARD.get()).cost(1).build(recipeSaver);
		
		
		GristCostRecipeBuilder.of(ExtraForgeTags.Items.TIN_RAW_MATERIALS).grist(RUST, 12).grist(CAULK, 8).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(ExtraForgeTags.Items.SILVER_RAW_MATERIALS).grist(RUST, 12).grist(MERCURY, 8).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(ExtraForgeTags.Items.LEAD_RAW_MATERIALS).grist(RUST, 12).grist(COBALT, 4).grist(SHALE, 4).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(ExtraForgeTags.Items.NICKEL_RAW_MATERIALS).grist(RUST, 12).grist(SULFUR, 8).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(ExtraForgeTags.Items.ZINC_RAW_MATERIALS).grist(RUST, 12).grist(SHALE, 4).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(ExtraForgeTags.Items.ALUMINUM_RAW_MATERIALS).grist(RUST, 12).grist(CHALK, 6).buildFor(recipeSaver, Minestuck.MOD_ID);
		SourceGristCostBuilder.of(ExtraForgeTags.Items.ALUMINIUM_RAW_MATERIALS).source(ExtraForgeTags.Items.ALUMINUM_RAW_MATERIALS).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(ExtraForgeTags.Items.COBALT_RAW_MATERIALS).grist(COBALT, 18).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(ExtraForgeTags.Items.ARDITE_RAW_MATERIALS).grist(GARNET, 12).grist(SULFUR, 8).buildFor(recipeSaver, Minestuck.MOD_ID);
		
		GristCostRecipeBuilder.of(ExtraForgeTags.Items.URANIUM_INGOTS).grist(URANIUM, 8).buildFor(recipeSaver, Minestuck.MOD_ID);
		SourceGristCostBuilder.of(ExtraForgeTags.Items.TIN_INGOTS).source(ExtraForgeTags.Items.TIN_RAW_MATERIALS).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(ExtraForgeTags.Items.BRASS_INGOTS).grist(RUST, 12).grist(CAULK, 8).buildFor(recipeSaver, Minestuck.MOD_ID);
		SourceGristCostBuilder.of(ExtraForgeTags.Items.SILVER_INGOTS).source(ExtraForgeTags.Items.SILVER_RAW_MATERIALS).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(ExtraForgeTags.Items.ELECTRUM_INGOTS).grist(MERCURY, 6).grist(RUST, 10).grist(GOLD, 6).buildFor(recipeSaver, Minestuck.MOD_ID);
		SourceGristCostBuilder.of(ExtraForgeTags.Items.LEAD_INGOTS).source(ExtraForgeTags.Items.LEAD_RAW_MATERIALS).buildFor(recipeSaver, Minestuck.MOD_ID);
		SourceGristCostBuilder.of(ExtraForgeTags.Items.NICKEL_INGOTS).source(ExtraForgeTags.Items.NICKEL_RAW_MATERIALS).buildFor(recipeSaver, Minestuck.MOD_ID);
		SourceGristCostBuilder.of(ExtraForgeTags.Items.ZINC_INGOTS).source(ExtraForgeTags.Items.ZINC_RAW_MATERIALS).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(ExtraForgeTags.Items.INVAR_INGOTS).grist(RUST, 12).grist(SULFUR, 5).buildFor(recipeSaver, Minestuck.MOD_ID);
		SourceGristCostBuilder.of(ExtraForgeTags.Items.ALUMINUM_INGOTS).source(ExtraForgeTags.Items.ALUMINUM_RAW_MATERIALS).buildFor(recipeSaver, Minestuck.MOD_ID);
		SourceGristCostBuilder.of(ExtraForgeTags.Items.ALUMINIUM_INGOTS).source(ExtraForgeTags.Items.ALUMINUM_INGOTS).buildFor(recipeSaver, Minestuck.MOD_ID);
		SourceGristCostBuilder.of(ExtraForgeTags.Items.COBALT_INGOTS).source(ExtraForgeTags.Items.COBALT_RAW_MATERIALS).buildFor(recipeSaver, Minestuck.MOD_ID);
		SourceGristCostBuilder.of(ExtraForgeTags.Items.ARDITE_INGOTS).source(ExtraForgeTags.Items.ARDITE_RAW_MATERIALS).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(ExtraForgeTags.Items.RED_ALLOY_INGOTS).grist(RUST, 18).grist(GARNET, 32).buildFor(recipeSaver, Minestuck.MOD_ID);
		
		//TODO consider factoring in the Forge "ore_rates" item tags
		oreCost(ExtraForgeTags.Items.TIN_ORES, ExtraForgeTags.Items.TIN_RAW_MATERIALS, 1, recipeSaver, Minestuck.MOD_ID);
		oreCost(ExtraForgeTags.Items.SILVER_ORES, ExtraForgeTags.Items.SILVER_RAW_MATERIALS, 1, recipeSaver, Minestuck.MOD_ID);
		oreCost(ExtraForgeTags.Items.LEAD_ORES, ExtraForgeTags.Items.LEAD_RAW_MATERIALS, 1, recipeSaver, Minestuck.MOD_ID);
		oreCost(ExtraForgeTags.Items.GALENA_ORES, ExtraForgeTags.Items.LEAD_RAW_MATERIALS, 1, recipeSaver, Minestuck.MOD_ID);
		oreCost(ExtraForgeTags.Items.NICKEL_ORES, ExtraForgeTags.Items.NICKEL_RAW_MATERIALS, 1, recipeSaver, Minestuck.MOD_ID);
		oreCost(ExtraForgeTags.Items.ZINC_ORES, ExtraForgeTags.Items.ZINC_RAW_MATERIALS, 1, recipeSaver, Minestuck.MOD_ID);
		oreCost(ExtraForgeTags.Items.ALUMINIUM_ORES, ExtraForgeTags.Items.ALUMINUM_RAW_MATERIALS, 1, recipeSaver, Minestuck.MOD_ID);
		oreCost(ExtraForgeTags.Items.ALUMINUM_ORES, ExtraForgeTags.Items.ALUMINUM_RAW_MATERIALS, 1, recipeSaver, Minestuck.MOD_ID);
		oreCost(ExtraForgeTags.Items.COBALT_ORES, ExtraForgeTags.Items.COBALT_RAW_MATERIALS, 1, recipeSaver, Minestuck.MOD_ID);
		oreCost(ExtraForgeTags.Items.ARDITE_ORES, ExtraForgeTags.Items.ARDITE_RAW_MATERIALS, 1, recipeSaver, Minestuck.MOD_ID);
	}
	
	public static void oreCost(TagKey<Item> ores, TagKey<Item> material, float multiplier, RecipeOutput recipeOutput, String modId)
	{
		SourceGristCostBuilder.of(ores).source(material).multiplier(multiplier).grist(BUILD, 4).buildFor(recipeOutput, modId);
	}
	
	public static void oreCost(TagKey<Item> ores, Item material, float multiplier, RecipeOutput recipeOutput, String modId)
	{
		SourceGristCostBuilder.of(ores).source(material).multiplier(multiplier).grist(BUILD, 4).buildFor(recipeOutput, modId);
	}
}
