package com.mraof.minestuck.data.tag;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.item.MSItemTypes;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.item.weapon.MagicAOERightClickEffect;
import com.mraof.minestuck.item.weapon.MagicRangedRightClickEffect;
import com.mraof.minestuck.item.weapon.WeaponItem;
import com.mraof.minestuck.util.ExtraForgeTags;
import com.mraof.minestuck.util.MSTags;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import static com.mraof.minestuck.item.MSItems.*;
import static com.mraof.minestuck.util.MSTags.Items.*;
import static net.minecraft.tags.ItemTags.*;
import static net.neoforged.neoforge.common.Tags.Items.*;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class MinestuckItemTagsProvider extends ItemTagsProvider
{
	public MinestuckItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagsProvider.TagLookup<Block>> blockTagProvider, @Nullable ExistingFileHelper existingFileHelper)
	{
		super(output, lookupProvider, blockTagProvider, Minestuck.MOD_ID, existingFileHelper);
	}
	
	@Override
	protected void addTags(HolderLookup.Provider provider)
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
		copy(BlockTags.LOGS, LOGS);
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
		copy(BlockTags.EMERALD_ORES, ORES_EMERALD);
		copy(Tags.Blocks.STONE, STONE);
		copy(Tags.Blocks.STORAGE_BLOCKS, STORAGE_BLOCKS);
		copy(ExtraForgeTags.Blocks.URANIUM_ORES, ExtraForgeTags.Items.URANIUM_ORES);
		copy(ExtraForgeTags.Blocks.URANIUM_STORAGE_BLOCKS, ExtraForgeTags.Items.URANIUM_STORAGE_BLOCKS);
		copy(MSTags.Blocks.GLOWING_LOGS, GLOWING_LOGS);
		copy(MSTags.Blocks.FROST_LOGS, FROST_LOGS);
		copy(MSTags.Blocks.RAINBOW_LOGS, RAINBOW_LOGS);
		copy(MSTags.Blocks.END_LOGS, END_LOGS);
		copy(MSTags.Blocks.VINE_LOGS, VINE_LOGS);
		copy(MSTags.Blocks.FLOWERY_VINE_LOGS, FLOWERY_VINE_LOGS);
		copy(MSTags.Blocks.DEAD_LOGS, DEAD_LOGS);
		copy(MSTags.Blocks.CINDERED_LOGS, CINDERED_LOGS);
		copy(MSTags.Blocks.PETRIFIED_LOGS, PETRIFIED_LOGS);
		copy(MSTags.Blocks.ASPECT_LOGS, ASPECT_LOGS);
		copy(MSTags.Blocks.ASPECT_WOOD, ASPECT_WOOD);
		copy(MSTags.Blocks.ASPECT_PLANKS, ASPECT_PLANKS);
		copy(MSTags.Blocks.ASPECT_SLABS, ASPECT_SLABS);
		copy(MSTags.Blocks.ASPECT_LEAVES, ASPECT_LEAVES);
		copy(MSTags.Blocks.ASPECT_SAPLINGS, ASPECT_SAPLINGS);
		copy(MSTags.Blocks.SHADEWOOD_LOGS, SHADEWOOD_LOGS);
		copy(MSTags.Blocks.SHADEWOOD_LEAVES, MSTags.Items.SHADEWOOD_LEAVES);
		copy(MSTags.Blocks.CRUXITE_ORES, CRUXITE_ORES);
		copy(MSTags.Blocks.URANIUM_ORES, URANIUM_ORES);
		copy(MSTags.Blocks.COAL_ORES, MSTags.Items.COAL_ORES);
		copy(MSTags.Blocks.IRON_ORES, MSTags.Items.IRON_ORES);
		copy(MSTags.Blocks.GOLD_ORES, MSTags.Items.GOLD_ORES);
		copy(MSTags.Blocks.REDSTONE_ORES, MSTags.Items.REDSTONE_ORES);
		copy(MSTags.Blocks.QUARTZ_ORES, QUARTZ_ORES);
		copy(MSTags.Blocks.LAPIS_ORES, MSTags.Items.LAPIS_ORES);
		copy(MSTags.Blocks.DIAMOND_ORES, MSTags.Items.DIAMOND_ORES);
		copy(MSTags.Blocks.EMERALD_ORES, MSTags.Items.EMERALD_ORES);
		copy(MSTags.Blocks.CRUXITE_STORAGE_BLOCKS, CRUXITE_STORAGE_BLOCKS);
		
		tag(SWORDS).add(EMERALD_SWORD.get(), SORD.get(), PAPER_SWORD.get(), CACTACEAE_CUTLASS.get(), STEAK_SWORD.get(), BEEF_SWORD.get(), IRRADIATED_STEAK_SWORD.get(), MACUAHUITL.get(), FROSTY_MACUAHUITL.get(), KATANA.get(), UNBREAKABLE_KATANA.get(), ANGEL_APOCALYPSE.get(), FIRE_POKER.get(), TOO_HOT_TO_HANDLE.get(), CALEDSCRATCH.get(), CALEDFWLCH.get(), ROYAL_DERINGER.get(), CLAYMORE.get(), CUTLASS_OF_ZILLYWAIR.get(), REGISWORD.get(), CRUEL_FATE_CRUCIBLE.get(), SCARLET_RIBBITAR.get(), DOGG_MACHETE.get(), COBALT_SABRE.get(), QUANTUM_SABRE.get(), SHATTER_BEACON.get(), SHATTER_BACON.get(), SUBTRACTSHUMIDIRE_ZOMORRODNEGATIVE.get(), MUSIC_SWORD.get(), PILLOW_TALK.get(), KRAKENS_EYE.get(), CINNAMON_SWORD.get());
		tag(AXES).add(EMERALD_AXE.get(), BATLEACKS.get(), COPSE_CRUSHER.get(), QUENCH_CRUSHER.get(), MELONSBANE.get(), CROP_CHOP.get(), THE_LAST_STRAW.get(), BATTLEAXE.get(), CANDY_BATTLEAXE.get(), CHOCO_LOCO_WOODSPLITTER.get(), STEEL_EDGE_CANDYCUTTER.get(), BLACKSMITH_BANE.get(), REGIAXE.get(), GOTHY_AXE.get(), SURPRISE_AXE.get(), SHOCK_AXE.get(), SHOCK_AXE_UNPOWERED.get(), SCRAXE.get(), LORENTZ_DISTRANSFORMATIONER.get(), PISTON_POWERED_POGO_AXEHAMMER.get(), RUBY_CROAK.get(), HEPHAESTUS_LUMBERJACK.get(), FISSION_FOCUSED_FAULT_FELLER.get(), BISECTOR.get(), FINE_CHINA_AXE.get(),
				LIPSTICK_CHAINSAW.get(), CAKESAW.get(), MAGENTA_MAULER.get(), THISTLEBLOWER.get(), EMERALD_IMMOLATOR.get(), OBSIDIATOR.get(), DEVILS_DELIGHT.get(), DEMONBANE_RAGRIPPER.get(), FROSTTOOTH.get());
		tag(PICKAXES).add(EMERALD_PICKAXE.get(), MINE_AND_GRIST.get(), PROSPECTING_PICKSCYTHE.get());
		tag(SHOVELS).add(EMERALD_SHOVEL.get(), WOODEN_SPOON.get(), SILVER_SPOON.get(), MELONBALLER.get(), TERRAIN_FLATENATOR.get(), NOSFERATU_SPOON.get(), THRONGLER.get(), CROCKER_SPOON.get());
		tag(HOES).add(EMERALD_HOE.get(), HELLBRINGERS_HOE_INACTIVE.get(), HELLBRINGERS_HOE_ACTIVE.get());
		tag(Tags.Items.TOOLS_TRIDENTS).add(BIDENT.get(), MEATFORK.get(), DOUBLE_ENDED_TRIDENT.get());
		tag(Tags.Items.ARMORS_HELMETS).add(PRISMARINE_HELMET.get(), IRON_LASS_GLASSES.get());
		tag(Tags.Items.ARMORS_CHESTPLATES).add(PRISMARINE_CHESTPLATE.get(), IRON_LASS_CHESTPLATE.get());
		tag(Tags.Items.ARMORS_LEGGINGS).add(PRISMARINE_LEGGINGS.get(), IRON_LASS_SKIRT.get());
		tag(Tags.Items.ARMORS_BOOTS).add(PRISMARINE_BOOTS.get(), IRON_LASS_SHOES.get());

		tag(ItemTags.MUSIC_DISCS).add(MUSIC_DISC_DANCE_STAB_DANCE.get(), MUSIC_DISC_EMISSARY_OF_DANCE.get(), MUSIC_DISC_RETRO_BATTLE.get());
		tag(DUSTS).add(MSBlocks.GLOWYSTONE_DUST.get().asItem());
		tag(RODS).add(URANIUM_POWERED_STICK.get());
		tag(ExtraForgeTags.Items.URANIUM_CHUNKS).add(RAW_URANIUM.get());
		tag(ExtraForgeTags.Items.TIN_ORES);
		tag(ExtraForgeTags.Items.SILVER_ORES);
		tag(ExtraForgeTags.Items.LEAD_ORES);
		tag(ExtraForgeTags.Items.GALENA_ORES);
		tag(ExtraForgeTags.Items.ZINC_ORES);
		tag(ExtraForgeTags.Items.NICKEL_ORES);
		tag(ExtraForgeTags.Items.ALUMINIUM_ORES);
		tag(ExtraForgeTags.Items.ALUMINUM_ORES);
		tag(ExtraForgeTags.Items.COBALT_ORES);
		tag(ExtraForgeTags.Items.ARDITE_ORES);
		tag(ExtraForgeTags.Items.TIN_RAW_MATERIALS);
		tag(ExtraForgeTags.Items.SILVER_RAW_MATERIALS);
		tag(ExtraForgeTags.Items.LEAD_RAW_MATERIALS);
		tag(ExtraForgeTags.Items.GALENA_RAW_MATERIALS);
		tag(ExtraForgeTags.Items.ZINC_RAW_MATERIALS);
		tag(ExtraForgeTags.Items.NICKEL_RAW_MATERIALS);
		tag(ExtraForgeTags.Items.ALUMINIUM_RAW_MATERIALS);
		tag(ExtraForgeTags.Items.ALUMINUM_RAW_MATERIALS);
		tag(ExtraForgeTags.Items.COBALT_RAW_MATERIALS);
		tag(ExtraForgeTags.Items.ARDITE_RAW_MATERIALS);
		tag(ExtraForgeTags.Items.URANIUM_INGOTS);
		tag(ExtraForgeTags.Items.TIN_INGOTS);
		tag(ExtraForgeTags.Items.BRASS_INGOTS);
		tag(ExtraForgeTags.Items.SILVER_INGOTS);
		tag(ExtraForgeTags.Items.ELECTRUM_INGOTS);
		tag(ExtraForgeTags.Items.LEAD_INGOTS);
		tag(ExtraForgeTags.Items.NICKEL_INGOTS);
		tag(ExtraForgeTags.Items.ZINC_INGOTS);
		tag(ExtraForgeTags.Items.INVAR_INGOTS);
		tag(ExtraForgeTags.Items.ALUMINIUM_INGOTS);
		tag(ExtraForgeTags.Items.COBALT_INGOTS);
		tag(ExtraForgeTags.Items.ARDITE_INGOTS);
		tag(ExtraForgeTags.Items.RED_ALLOY_INGOTS);
		
		tag(GRIST_CANDY).add(BUILD_GUSHERS.get(), AMBER_GUMMY_WORM.get(), CAULK_PRETZEL.get(), CHALK_CANDY_CIGARETTE.get(), IODINE_LICORICE.get(), SHALE_PEEP.get(), TAR_LICORICE.get(), COBALT_GUM.get(), MARBLE_JAWBREAKER.get(), MERCURY_SIXLETS.get(), QUARTZ_JELLY_BEAN.get(), SULFUR_CANDY_APPLE.get(), AMETHYST_HARD_CANDY.get(), GARNET_TWIX.get(), RUBY_LOLLIPOP.get(), RUST_GUMMY_EYE.get(), DIAMOND_MINT.get(), GOLD_CANDY_RIBBON.get(), URANIUM_GUMMY_BEAR.get(), ARTIFACT_WARHEAD.get(), ZILLIUM_SKITTLES.get());
		tag(MSTags.Items.FAYGO).add(MSItems.ORANGE_FAYGO.get(), CANDY_APPLE_FAYGO.get(), FAYGO_COLA.get(), COTTON_CANDY_FAYGO.get(), CREME_SODA_FAYGO.get(), GRAPE_FAYGO.get(), MOON_MIST_FAYGO.get(), PEACH_FAYGO.get(), REDPOP_FAYGO.get());
		tag(MODUS_CARD).add(STACK_MODUS_CARD.get(), QUEUE_MODUS_CARD.get(), QUEUESTACK_MODUS_CARD.get(), TREE_MODUS_CARD.get(), HASHMAP_MODUS_CARD.get(), SET_MODUS_CARD.get());
		tag(CASSETTES).add(MSItems.CASSETTE_MELLOHI.get(), CASSETTE_13.get(), CASSETTE_BLOCKS.get(), CASSETTE_CAT.get(), CASSETTE_CHIRP.get(), CASSETTE_FAR.get(), CASSETTE_MALL.get(), CASSETTE_DANCE_STAB.get(), CASSETTE_RETRO_BATTLE.get(), CASSETTE_EMISSARY.get(), CASSETTE_11.get(), CASSETTE_PIGSTEP.get(), CASSETTE_STAL.get(), CASSETTE_STRAD.get(), CASSETTE_WAIT.get(), CASSETTE_WARD.get(), CASSETTE_OTHERSIDE.get(), CASSETTE_5.get());
		tag(BUGS).add(BUG_ON_A_STICK.get(), CHOCOLATE_BEETLE.get(), CONE_OF_FLIES.get(), GRASSHOPPER.get(), CICADA.get(), JAR_OF_BUGS.get());
		tag(CONSORT_SNACKS).add(Items.COOKIE).addTag(BUGS);
		tag(MAGIC_WEAPON).add(MSItems.REGISTER.getEntries().stream().map(Supplier::get).filter(item -> item instanceof WeaponItem weapon && (weapon.getItemRightClickEffect() instanceof MagicRangedRightClickEffect || weapon.getItemRightClickEffect() instanceof MagicAOERightClickEffect)).toArray(Item[]::new));
		tag(CREATIVE_SHOCK_RIGHT_CLICK_LIMIT).add(Items.CHORUS_FRUIT);
		tag(UNREADABLE).add(CRUEL_FATE_CRUCIBLE.get(), ROYAL_DERINGER.get(), TRANSPORTALIZER.get(), TRANS_PORTALIZER.get(), FEAR_NO_ANVIL.get(), TYPHONIC_TRIVIALIZER.get(), QUILL_OF_ECHIDNA.get(), UMBRAL_INFILTRATOR.get(), FLUORITE_OCTET.get(), CLIENT_DISK.get(), SERVER_DISK.get(), GUTTER_THUMB_DRIVE.get(), GUTTER_BALL.get(), CAPTCHA_CARD.get(), CUEBALL.get(), BLACK_QUEENS_RING.get(), WHITE_QUEENS_RING.get(), BLACK_KINGS_SCEPTER.get(), WHITE_KINGS_SCEPTER.get()).add(Items.DRAGON_EGG).add(Items.DRAGON_HEAD).add(Items.DRAGON_BREATH).add(Items.NETHER_STAR).add(Items.COMMAND_BLOCK).add(Items.COMMAND_BLOCK_MINECART).add(Items.CHAIN_COMMAND_BLOCK).add(Items.REPEATING_COMMAND_BLOCK).add(Items.END_CRYSTAL).add(Items.EXPERIENCE_BOTTLE).add(Items.ELYTRA).add(Items.TOTEM_OF_UNDYING).add(Items.BEACON).add(Items.BEDROCK).add(Items.PUMPKIN);
		tag(LEGENDARY).add(MSItems.REGISTER.getEntries().stream().map(Supplier::get).filter(item -> item instanceof WeaponItem weapon && (weapon.getTier() == MSItemTypes.DENIZEN_TIER || weapon.getTier() == MSItemTypes.ZILLY_TIER || weapon.getTier() == MSItemTypes.WELSH_TIER)).toArray(Item[]::new));
	}

	@Override
	public String getName()
	{
		return "Minestuck Item Tags";
	}
}
