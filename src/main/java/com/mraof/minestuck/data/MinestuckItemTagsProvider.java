package com.mraof.minestuck.data;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.util.ExtraForgeTags;
import com.mraof.minestuck.util.MSTags;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;

import static com.mraof.minestuck.item.MSItems.*;
import static com.mraof.minestuck.util.MSTags.Items.*;
import static net.minecraft.tags.ItemTags.*;
import static net.minecraftforge.common.Tags.Items.*;

public class MinestuckItemTagsProvider extends ItemTagsProvider
{
	MinestuckItemTagsProvider(DataGenerator dataGenerator, BlockTagsProvider blockTagProvider, @Nullable ExistingFileHelper existingFileHelper)
	{
		super(dataGenerator, blockTagProvider, Minestuck.MOD_ID, existingFileHelper);
	}
	
	@Override
	protected void addTags()
	{
		copy(BlockTags.PLANKS, PLANKS);
		copy(BlockTags.STONE_BRICKS, STONE_BRICKS);
		copy(BlockTags.WOODEN_BUTTONS, WOODEN_BUTTONS);
		copy(BlockTags.BUTTONS, BUTTONS);
		copy(BlockTags.WOODEN_STAIRS, WOODEN_STAIRS);
		copy(BlockTags.WOODEN_SLABS, WOODEN_SLABS);
		copy(BlockTags.SAPLINGS, SAPLINGS);
		copy(BlockTags.STAIRS, STAIRS);
		copy(BlockTags.SLABS, SLABS);
		copy(BlockTags.LEAVES, LEAVES);
		copy(Tags.Blocks.COBBLESTONE, COBBLESTONE);
		copy(Tags.Blocks.ORES, ORES);
		copy(BlockTags.COAL_ORES, ORES_COAL);
		copy(BlockTags.DIAMOND_ORES, ORES_DIAMOND);
		copy(BlockTags.GOLD_ORES, ORES_GOLD);
		copy(BlockTags.IRON_ORES, ORES_IRON);
		copy(BlockTags.LAPIS_ORES, ORES_LAPIS);
		copy(Tags.Blocks.ORES_QUARTZ, ORES_QUARTZ);
		copy(BlockTags.REDSTONE_ORES, ORES_REDSTONE);
		copy(Tags.Blocks.STONE, STONE);
		copy(Tags.Blocks.STORAGE_BLOCKS, STORAGE_BLOCKS);
		copy(ExtraForgeTags.Blocks.URANIUM_ORES, ExtraForgeTags.Items.URANIUM_ORES);
		copy(ExtraForgeTags.Blocks.URANIUM_STORAGE_BLOCKS, ExtraForgeTags.Items.URANIUM_STORAGE_BLOCKS);
		copy(ExtraForgeTags.Blocks.TERRACOTTA, ExtraForgeTags.Items.TERRACOTTA);
		copy(MSTags.Blocks.GLOWING_LOGS, GLOWING_LOGS);
		copy(MSTags.Blocks.FROST_LOGS, FROST_LOGS);
		copy(MSTags.Blocks.RAINBOW_LOGS, RAINBOW_LOGS);
		copy(MSTags.Blocks.END_LOGS, END_LOGS);
		copy(MSTags.Blocks.VINE_LOGS, VINE_LOGS);
		copy(MSTags.Blocks.FLOWERY_VINE_LOGS, FLOWERY_VINE_LOGS);
		copy(MSTags.Blocks.DEAD_LOGS, DEAD_LOGS);
		copy(MSTags.Blocks.PETRIFIED_LOGS, PETRIFIED_LOGS);
		copy(MSTags.Blocks.ASPECT_LOGS, ASPECT_LOGS);
		copy(MSTags.Blocks.ASPECT_PLANKS, ASPECT_PLANKS);
		copy(MSTags.Blocks.ASPECT_LEAVES, ASPECT_LEAVES);
		copy(MSTags.Blocks.ASPECT_SAPLINGS, ASPECT_SAPLINGS);
		copy(MSTags.Blocks.CRUXITE_ORES, CRUXITE_ORES);
		copy(MSTags.Blocks.URANIUM_ORES, URANIUM_ORES);
		copy(MSTags.Blocks.COAL_ORES, MSTags.Items.COAL_ORES);
		copy(MSTags.Blocks.IRON_ORES, MSTags.Items.IRON_ORES);
		copy(MSTags.Blocks.GOLD_ORES, MSTags.Items.GOLD_ORES);
		copy(MSTags.Blocks.REDSTONE_ORES, MSTags.Items.REDSTONE_ORES);
		copy(MSTags.Blocks.QUARTZ_ORES, QUARTZ_ORES);
		copy(MSTags.Blocks.LAPIS_ORES, MSTags.Items.LAPIS_ORES);
		copy(MSTags.Blocks.DIAMOND_ORES, MSTags.Items.DIAMOND_ORES);
		copy(MSTags.Blocks.CRUXITE_STORAGE_BLOCKS, CRUXITE_STORAGE_BLOCKS);

		tag(ItemTags.MUSIC_DISCS).add(MUSIC_DISC_DANCE_STAB_DANCE9j, MUSIC_DISC_EMISSARY_OF_DANCE9j, MUSIC_DISC_RETRO_BATTLE9j);
		tag(DUSTS).add(MSBlocks.GLOWYSTONE_DUST.get().asItem());
		tag(RODS).add(URANIUM_POWERED_STICK9j);
		tag(ExtraForgeTags.Items.URANIUM_CHUNKS).add(RAW_URANIUM9j);
		tag(ExtraForgeTags.Items.COPPER_ORES);
		tag(ExtraForgeTags.Items.TIN_ORES);
		tag(ExtraForgeTags.Items.SILVER_ORES);
		tag(ExtraForgeTags.Items.LEAD_ORES);
		tag(ExtraForgeTags.Items.NICKEL_ORES);
		tag(ExtraForgeTags.Items.ALUMINIUM_ORES);
		tag(ExtraForgeTags.Items.COBALT_ORES);
		tag(ExtraForgeTags.Items.ARDITE_ORES);
		tag(ExtraForgeTags.Items.COPPER_INGOTS);
		tag(ExtraForgeTags.Items.TIN_INGOTS);
		tag(ExtraForgeTags.Items.SILVER_INGOTS);
		tag(ExtraForgeTags.Items.LEAD_INGOTS);
		tag(ExtraForgeTags.Items.NICKEL_INGOTS);
		tag(ExtraForgeTags.Items.INVAR_INGOTS);
		tag(ExtraForgeTags.Items.ALUMINIUM_INGOTS);
		tag(ExtraForgeTags.Items.COBALT_INGOTS);
		tag(ExtraForgeTags.Items.ARDITE_INGOTS);
		tag(ExtraForgeTags.Items.RED_ALLOY_INGOTS);
		
		tag(GRIST_CANDY).add(BUILD_GUSHERS9j, AMBER_GUMMY_WORM9j, CAULK_PRETZEL9j, CHALK_CANDY_CIGARETTE9j, IODINE_LICORICE9j, SHALE_PEEP9j, TAR_LICORICE9j, COBALT_GUM9j, MARBLE_JAWBREAKER9j, MERCURY_SIXLETS9j, QUARTZ_JELLY_BEAN9j, SULFUR_CANDY_APPLE9j, AMETHYST_HARD_CANDY9j, GARNET_TWIX9j, RUBY_LOLLIPOP9j, RUST_GUMMY_EYE9j, DIAMOND_MINT9j, GOLD_CANDY_RIBBON9j, URANIUM_GUMMY_BEAR9j, ARTIFACT_WARHEAD9j, ZILLIUM_SKITTLES9j);
		tag(MSTags.Items.FAYGO).add(MSItems.ORANGE_FAYGO9j, CANDY_APPLE_FAYGO9j, FAYGO_COLA9j, COTTON_CANDY_FAYGO9j, CREME_SODA_FAYGO9j, GRAPE_FAYGO9j, MOON_MIST_FAYGO9j, PEACH_FAYGO9j, REDPOP_FAYGO9j);
		tag(MODUS_CARD).add(STACK_MODUS_CARD9j, QUEUE_MODUS_CARD9j, QUEUESTACK_MODUS_CARD9j, TREE_MODUS_CARD9j, HASHMAP_MODUS_CARD9j, SET_MODUS_CARD9j);
		tag(CASSETTES).add(MSItems.CASSETTE_MELLOHI9j, CASSETTE_139j, CASSETTE_BLOCKS9j, CASSETTE_CAT9j, CASSETTE_CHIRP9j, CASSETTE_FAR9j, CASSETTE_MALL9j, CASSETTE_DANCE_STAB9j, CASSETTE_RETRO_BATTLE9j, CASSETTE_EMISSARY9j);
		tag(BUGS).add(BUG_ON_A_STICK9j, CHOCOLATE_BEETLE9j, CONE_OF_FLIES9j, GRASSHOPPER9j, CICADA9j, JAR_OF_BUGS9j);
		tag(CREATIVE_SHOCK_RIGHT_CLICK_LIMIT).add(Items.CHORUS_FRUIT);
	}

	@Override
	public String getName()
	{
		return "Minestuck Item Tags";
	}
}
