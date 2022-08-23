package com.mraof.minestuck.data.tag;

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
	public MinestuckItemTagsProvider(DataGenerator dataGenerator, BlockTagsProvider blockTagProvider, @Nullable ExistingFileHelper existingFileHelper)
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

		tag(ItemTags.MUSIC_DISCS).add(MUSIC_DISC_DANCE_STAB_DANCE.get(), MUSIC_DISC_EMISSARY_OF_DANCE.get(), MUSIC_DISC_RETRO_BATTLE.get());
		tag(DUSTS).add(MSBlocks.GLOWYSTONE_DUST.get().asItem());
		tag(RODS).add(URANIUM_POWERED_STICK.get());
		tag(ExtraForgeTags.Items.URANIUM_CHUNKS).add(RAW_URANIUM.get());
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
		
		tag(GRIST_CANDY).add(BUILD_GUSHERS.get(), AMBER_GUMMY_WORM.get(), CAULK_PRETZEL.get(), CHALK_CANDY_CIGARETTE.get(), IODINE_LICORICE.get(), SHALE_PEEP.get(), TAR_LICORICE.get(), COBALT_GUM.get(), MARBLE_JAWBREAKER.get(), MERCURY_SIXLETS.get(), QUARTZ_JELLY_BEAN.get(), SULFUR_CANDY_APPLE.get(), AMETHYST_HARD_CANDY.get(), GARNET_TWIX.get(), RUBY_LOLLIPOP.get(), RUST_GUMMY_EYE.get(), DIAMOND_MINT.get(), GOLD_CANDY_RIBBON.get(), URANIUM_GUMMY_BEAR.get(), ARTIFACT_WARHEAD.get(), ZILLIUM_SKITTLES.get());
		tag(MSTags.Items.FAYGO).add(MSItems.ORANGE_FAYGO.get(), CANDY_APPLE_FAYGO.get(), FAYGO_COLA.get(), COTTON_CANDY_FAYGO.get(), CREME_SODA_FAYGO.get(), GRAPE_FAYGO.get(), MOON_MIST_FAYGO.get(), PEACH_FAYGO.get(), REDPOP_FAYGO.get());
		tag(MODUS_CARD).add(STACK_MODUS_CARD.get(), QUEUE_MODUS_CARD.get(), QUEUESTACK_MODUS_CARD.get(), TREE_MODUS_CARD.get(), HASHMAP_MODUS_CARD.get(), SET_MODUS_CARD.get());
		tag(CASSETTES).add(MSItems.CASSETTE_MELLOHI.get(), CASSETTE_13.get(), CASSETTE_BLOCKS.get(), CASSETTE_CAT.get(), CASSETTE_CHIRP.get(), CASSETTE_FAR.get(), CASSETTE_MALL.get(), CASSETTE_DANCE_STAB.get(), CASSETTE_RETRO_BATTLE.get(), CASSETTE_EMISSARY.get());
		tag(BUGS).add(BUG_ON_A_STICK.get(), CHOCOLATE_BEETLE.get(), CONE_OF_FLIES.get(), GRASSHOPPER.get(), CICADA.get(), JAR_OF_BUGS.get());
		tag(CREATIVE_SHOCK_RIGHT_CLICK_LIMIT).add(Items.CHORUS_FRUIT);
	}

	@Override
	public String getName()
	{
		return "Minestuck Item Tags";
	}
}
