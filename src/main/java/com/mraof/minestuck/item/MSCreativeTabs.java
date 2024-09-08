package com.mraof.minestuck.item;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.alchemy.AlchemyHelper;
import com.mraof.minestuck.block.AspectTreeBlocks;
import com.mraof.minestuck.block.SkaiaBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class MSCreativeTabs
{
	public static final String MAIN_KEY = "minestuck.item_group.main";
	public static final String LANDS_KEY = "minestuck.item_group.lands";
	public static final String WEAPONS_KEY = "minestuck.item_group.weapons";
	
	public static final DeferredRegister<CreativeModeTab> REGISTER = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Minestuck.MOD_ID);
	
	public static final Supplier<CreativeModeTab> MAIN = REGISTER.register("main", () -> CreativeModeTab.builder()
			.title(Component.translatable(MAIN_KEY)).icon(() -> new ItemStack(MSItems.CLIENT_DISK.get())).displayItems(MSCreativeTabs::buildMainTab).build());
	public static final Supplier<CreativeModeTab> LANDS = REGISTER.register("lands", () -> CreativeModeTab.builder()
			.title(Component.translatable(LANDS_KEY)).icon(() -> new ItemStack(MSItems.GLOWING_MUSHROOM.get())).displayItems(MSCreativeTabs::buildLandsTab).build());
	public static final Supplier<CreativeModeTab> WEAPONS = REGISTER.register("weapons", () -> CreativeModeTab.builder()
			.title(Component.translatable(WEAPONS_KEY)).icon(() -> new ItemStack(MSItems.ZILLYHOO_HAMMER.get())).displayItems(MSCreativeTabs::buildWeaponsTab).build());
	
	private static void buildMainTab(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output output)
	{
		output.accept(BoondollarsItem.setCount(new ItemStack(MSItems.BOONDOLLARS.get()), 1));
		output.accept(BoondollarsItem.setCount(new ItemStack(MSItems.BOONDOLLARS.get()), 10));
		output.accept(BoondollarsItem.setCount(new ItemStack(MSItems.BOONDOLLARS.get()), 100));
		output.accept(BoondollarsItem.setCount(new ItemStack(MSItems.BOONDOLLARS.get()), 1000));
		output.accept(BoondollarsItem.setCount(new ItemStack(MSItems.BOONDOLLARS.get()), 10000));
		
		output.accept(MSItems.RAW_CRUXITE.get());
		output.accept(MSItems.RAW_URANIUM.get());
		output.accept(MSItems.ENERGY_CORE.get());
		
		output.accept(MSItems.CRUXITE_APPLE.get());
		output.accept(MSItems.CRUXITE_POTION.get());
		
		output.accept(MSItems.SBURB_CODE.get());
		output.accept(MSItems.COMPLETED_SBURB_CODE.get());
		output.accept(MSItems.COMPUTER_PARTS.get());
		output.accept(MSItems.BLANK_DISK.get());
		output.accept(MSItems.CLIENT_DISK.get());
		output.accept(MSItems.SERVER_DISK.get());
		
		output.accept(MSItems.CAPTCHA_CARD.get());
		output.accept(AlchemyHelper.createPunchedCard(new ItemStack(MSItems.CRUXITE_APPLE.get())));
		output.accept(MSItems.STACK_MODUS_CARD.get());
		output.accept(MSItems.QUEUE_MODUS_CARD.get());
		output.accept(MSItems.QUEUESTACK_MODUS_CARD.get());
		output.accept(MSItems.TREE_MODUS_CARD.get());
		output.accept(MSItems.HASHMAP_MODUS_CARD.get());
		output.accept(MSItems.SET_MODUS_CARD.get());
		
		output.accept(MSItems.PHLEGM_GUSHERS.get());
		output.accept(MSItems.SORROW_GUSHERS.get());
		
		output.accept(MSItems.SOPOR_SLIME_PIE.get());
		
		output.accept(MSItems.BUG_ON_A_STICK.get());
		output.accept(MSItems.FOOD_CAN.get());
		
		output.accept(MSItems.CANDY_CORN.get());
		output.accept(MSItems.TUIX_BAR.get());
		output.accept(MSItems.BUILD_GUSHERS.get());
		output.accept(MSItems.AMBER_GUMMY_WORM.get());
		output.accept(MSItems.CAULK_PRETZEL.get());
		output.accept(MSItems.CHALK_CANDY_CIGARETTE.get());
		output.accept(MSItems.IODINE_LICORICE.get());
		output.accept(MSItems.SHALE_PEEP.get());
		output.accept(MSItems.TAR_LICORICE.get());
		output.accept(MSItems.COBALT_GUM.get());
		output.accept(MSItems.MARBLE_JAWBREAKER.get());
		output.accept(MSItems.MERCURY_SIXLETS.get());
		output.accept(MSItems.QUARTZ_JELLY_BEAN.get());
		output.accept(MSItems.SULFUR_CANDY_APPLE.get());
		output.accept(MSItems.AMETHYST_HARD_CANDY.get());
		output.accept(MSItems.GARNET_TWIX.get());
		output.accept(MSItems.RUBY_LOLLIPOP.get());
		output.accept(MSItems.RUST_GUMMY_EYE.get());
		output.accept(MSItems.DIAMOND_MINT.get());
		output.accept(MSItems.GOLD_CANDY_RIBBON.get());
		output.accept(MSItems.URANIUM_GUMMY_BEAR.get());
		output.accept(MSItems.ARTIFACT_WARHEAD.get());
		output.accept(MSItems.ZILLIUM_SKITTLES.get());
		output.accept(MSItems.APPLE_JUICE.get());
		output.accept(MSItems.TAB.get());
		output.accept(MSItems.ORANGE_FAYGO.get());
		output.accept(MSItems.CANDY_APPLE_FAYGO.get());
		output.accept(MSItems.FAYGO_COLA.get());
		output.accept(MSItems.COTTON_CANDY_FAYGO.get());
		output.accept(MSItems.CREME_SODA_FAYGO.get());
		output.accept(MSItems.GRAPE_FAYGO.get());
		output.accept(MSItems.MOON_MIST_FAYGO.get());
		output.accept(MSItems.PEACH_FAYGO.get());
		output.accept(MSItems.REDPOP_FAYGO.get());
		output.accept(MSItems.IRRADIATED_STEAK.get());
		output.accept(MSItems.SURPRISE_EMBRYO.get());
		output.accept(MSItems.UNKNOWABLE_EGG.get());
		output.accept(MSItems.BREADCRUMBS.get());
		
		output.accept(MSItems.GUTTER_THUMB_DRIVE.get());
		output.accept(MSItems.ANCIENT_THUMB_DRIVE.get());
		output.accept(MSItems.GUTTER_BALL.get());
		
		output.accept(MSItems.OIL_BUCKET.get());
		output.accept(MSItems.BLOOD_BUCKET.get());
		output.accept(MSItems.BRAIN_JUICE_BUCKET.get());
		output.accept(MSItems.WATER_COLORS_BUCKET.get());
		output.accept(MSItems.ENDER_BUCKET.get());
		output.accept(MSItems.LIGHT_WATER_BUCKET.get());
		output.accept(MSItems.OBSIDIAN_BUCKET.get());
		output.accept(MSItems.CAULK_BUCKET.get());
		output.accept(MSItems.MOLTEN_AMBER_BUCKET.get());
		
		output.accept(MSItems.DICE.get());
		output.accept(MSItems.PLUTONIUM_CORE.get());
		output.accept(MSItems.GRIMOIRE.get());
		output.accept(MSItems.BATTERY.get());
		output.accept(MSItems.BARBASOL.get());
		output.accept(MSItems.CLOTHES_IRON.get());
		output.accept(MSItems.INK_SQUID_PRO_QUO.get());
		output.accept(MSItems.CUEBALL.get());
		output.accept(MSItems.EIGHTBALL.get());
		output.accept(MSItems.THRESH_DVD.get());
		output.accept(MSItems.GAMEBRO_MAGAZINE.get());
		output.accept(MSItems.GAMEGRL_MAGAZINE.get());
		output.accept(MSItems.CREW_POSTER.get());
		output.accept(MSItems.SBAHJ_POSTER.get());
		output.accept(MSItems.BI_DYE.get());
		output.accept(MSItems.LIP_BALM.get());
		output.accept(MSItems.ELECTRIC_AUTOHARP.get());
		output.accept(MSItems.CARDBOARD_TUBE.get());
		output.accept(MSItems.CRYPTID_PHOTO.get());
		output.accept(MSItems.PARTICLE_ACCELERATOR.get());
		
		output.accept(MSItems.CAPTCHAROID_CAMERA.get());
		output.accept(MSItems.LONG_FORGOTTEN_WARHORN.get());
		output.accept(MSItems.BLACK_QUEENS_RING.get());
		output.accept(MSItems.WHITE_QUEENS_RING.get());
		output.accept(MSItems.BARBASOL_BOMB.get());
		output.accept(MSItems.RAZOR_BLADE.get());
		output.accept(MSItems.ICE_SHARD.get());
		output.accept(MSItems.HORN.get());
		output.accept(MSItems.CAKE_MIX.get());
		output.accept(MSItems.TEMPLE_SCANNER.get());
		
		output.accept(MSItems.SCALEMATE_APPLESCAB.get());
		output.accept(MSItems.PLUSH_SALAMANDER.get());
		output.accept(MSItems.PLUSH_NAKAGATOR.get());
		output.accept(MSItems.PLUSH_IGUANA.get());
		output.accept(MSItems.PLUSH_TURTLE.get());
		output.accept(MSItems.PLUSH_MUTATED_CAT.get());
		output.accept(MSItems.URANIUM_POWERED_STICK.get());
		output.accept(MSItems.IRON_BOAT.get());
		output.accept(MSItems.GOLD_BOAT.get());
		output.accept(MSItems.COCOA_WART.get());
		output.accept(MSItems.HORSE_CLOCK.get());
		
		output.accept(MSItems.MUSIC_DISC_EMISSARY_OF_DANCE.get());
		output.accept(MSItems.MUSIC_DISC_DANCE_STAB_DANCE.get());
		output.accept(MSItems.MUSIC_DISC_RETRO_BATTLE.get());
		output.accept(MSItems.CASSETTE_13.get());
		output.accept(MSItems.CASSETTE_CAT.get());
		output.accept(MSItems.CASSETTE_BLOCKS.get());
		output.accept(MSItems.CASSETTE_CHIRP.get());
		output.accept(MSItems.CASSETTE_FAR.get());
		output.accept(MSItems.CASSETTE_MALL.get());
		output.accept(MSItems.CASSETTE_MELLOHI.get());
		output.accept(MSItems.CASSETTE_DANCE_STAB.get());
		output.accept(MSItems.CASSETTE_RETRO_BATTLE.get());
		output.accept(MSItems.CASSETTE_EMISSARY.get());
		output.accept(MSItems.CASSETTE_11.get());
		output.accept(MSItems.CASSETTE_PIGSTEP.get());
		output.accept(MSItems.CASSETTE_STAL.get());
		output.accept(MSItems.CASSETTE_STRAD.get());
		output.accept(MSItems.CASSETTE_WAIT.get());
		output.accept(MSItems.CASSETTE_WARD.get());
		output.accept(MSItems.CASSETTE_OTHERSIDE.get());
		output.accept(MSItems.CASSETTE_5.get());
		
		output.accept(MSItems.CRUXTRUDER.get());
		output.accept(MSItems.CRUXTRUDER_LID.get());
		output.accept(MSItems.TOTEM_LATHE.get());
		output.accept(MSItems.ALCHEMITER.get());
		output.accept(MSItems.PUNCH_DESIGNIX.get());
		output.accept(MSItems.MINI_CRUXTRUDER.get());
		output.accept(MSItems.MINI_TOTEM_LATHE.get());
		output.accept(MSItems.MINI_ALCHEMITER.get());
		output.accept(MSItems.MINI_PUNCH_DESIGNIX.get());
		output.accept(MSItems.HOLOPAD.get());
		output.accept(MSItems.INTELLIBEAM_LASERSTATION.get());
		output.accept(MSItems.CRUXITE_DOWEL.get());
		output.accept(MSItems.LOTUS_TIME_CAPSULE.get());
		
		output.accept(SkaiaBlocks.BLACK_CHESS_DIRT);
		output.accept(SkaiaBlocks.WHITE_CHESS_DIRT);
		output.accept(SkaiaBlocks.DARK_GRAY_CHESS_DIRT);
		output.accept(SkaiaBlocks.LIGHT_GRAY_CHESS_DIRT);
		output.accept(SkaiaBlocks.SKAIA_PORTAL);
		output.accept(SkaiaBlocks.BLACK_CHESS_BRICKS);
		output.accept(SkaiaBlocks.BLACK_CHESS_BRICK_WALL);
		output.accept(SkaiaBlocks.DARK_GRAY_CHESS_BRICKS);
		output.accept(SkaiaBlocks.DARK_GRAY_CHESS_BRICK_WALL);
		
		output.accept(SkaiaBlocks.LIGHT_GRAY_CHESS_BRICKS);
		output.accept(SkaiaBlocks.LIGHT_GRAY_CHESS_BRICK_WALL);
		
		output.accept(SkaiaBlocks.WHITE_CHESS_BRICKS);
		output.accept(SkaiaBlocks.WHITE_CHESS_BRICK_WALL);
		
		output.accept(SkaiaBlocks.BLACK_CHESS_BRICK_SMOOTH);
		output.accept(SkaiaBlocks.BLACK_CHESS_BRICK_SMOOTH_STAIRS);
		output.accept(SkaiaBlocks.BLACK_CHESS_BRICK_SMOOTH_SLAB);
		output.accept(SkaiaBlocks.BLACK_CHESS_BRICK_SMOOTH_WALL);
		output.accept(SkaiaBlocks.BLACK_CHESS_BRICK_SMOOTH_BUTTON);
		output.accept(SkaiaBlocks.BLACK_CHESS_BRICK_SMOOTH_PRESSURE_PLATE);
		
		output.accept(SkaiaBlocks.DARK_GRAY_CHESS_BRICK_SMOOTH);
		output.accept(SkaiaBlocks.DARK_GRAY_CHESS_BRICK_SMOOTH_STAIRS);
		output.accept(SkaiaBlocks.DARK_GRAY_CHESS_BRICK_SMOOTH_SLAB);
		output.accept(SkaiaBlocks.DARK_GRAY_CHESS_BRICK_SMOOTH_WALL);
		output.accept(SkaiaBlocks.DARK_GRAY_CHESS_BRICK_SMOOTH_BUTTON);
		output.accept(SkaiaBlocks.DARK_GRAY_CHESS_BRICK_SMOOTH_PRESSURE_PLATE);
		
		output.accept(SkaiaBlocks.LIGHT_GRAY_CHESS_BRICK_SMOOTH);
		output.accept(SkaiaBlocks.LIGHT_GRAY_CHESS_BRICK_SMOOTH_STAIRS);
		output.accept(SkaiaBlocks.LIGHT_GRAY_CHESS_BRICK_SMOOTH_SLAB);
		output.accept(SkaiaBlocks.LIGHT_GRAY_CHESS_BRICK_SMOOTH_WALL);
		output.accept(SkaiaBlocks.LIGHT_GRAY_CHESS_BRICK_SMOOTH_BUTTON);
		output.accept(SkaiaBlocks.LIGHT_GRAY_CHESS_BRICK_SMOOTH_PRESSURE_PLATE);
		
		output.accept(SkaiaBlocks.WHITE_CHESS_BRICK_SMOOTH);
		output.accept(SkaiaBlocks.WHITE_CHESS_BRICK_SMOOTH_STAIRS);
		output.accept(SkaiaBlocks.WHITE_CHESS_BRICK_SMOOTH_SLAB);
		output.accept(SkaiaBlocks.WHITE_CHESS_BRICK_SMOOTH_WALL);
		output.accept(SkaiaBlocks.WHITE_CHESS_BRICK_SMOOTH_BUTTON);
		output.accept(SkaiaBlocks.WHITE_CHESS_BRICK_SMOOTH_PRESSURE_PLATE);
		
		output.accept(SkaiaBlocks.BLACK_CHESS_BRICK_TRIM);
		output.accept(SkaiaBlocks.DARK_GRAY_CHESS_BRICK_TRIM);
		output.accept(SkaiaBlocks.LIGHT_GRAY_CHESS_BRICK_TRIM);
		output.accept(SkaiaBlocks.WHITE_CHESS_BRICK_TRIM);
		
		output.accept(SkaiaBlocks.CHECKERED_STAINED_GLASS);
		output.accept(SkaiaBlocks.BLACK_CROWN_STAINED_GLASS);
		output.accept(SkaiaBlocks.BLACK_PAWN_STAINED_GLASS);
		output.accept(SkaiaBlocks.WHITE_CROWN_STAINED_GLASS);
		output.accept(SkaiaBlocks.WHITE_PAWN_STAINED_GLASS);
		
		output.accept(MSItems.CRUXITE_BLOCK.get());
		output.accept(MSItems.CRUXITE_STAIRS.get());
		output.accept(MSItems.CRUXITE_SLAB.get());
		output.accept(MSItems.CRUXITE_WALL.get());
		output.accept(MSItems.CRUXITE_BUTTON.get());
		output.accept(MSItems.CRUXITE_PRESSURE_PLATE.get());
		output.accept(MSItems.CRUXITE_DOOR.get());
		output.accept(MSItems.CRUXITE_TRAPDOOR.get());
		output.accept(MSItems.POLISHED_CRUXITE_BLOCK.get());
		output.accept(MSItems.POLISHED_CRUXITE_STAIRS.get());
		output.accept(MSItems.POLISHED_CRUXITE_SLAB.get());
		output.accept(MSItems.POLISHED_CRUXITE_WALL.get());
		output.accept(MSItems.CRUXITE_BRICKS.get());
		output.accept(MSItems.CRUXITE_BRICK_STAIRS.get());
		output.accept(MSItems.CRUXITE_BRICK_SLAB.get());
		output.accept(MSItems.CRUXITE_BRICK_WALL.get());
		output.accept(MSItems.SMOOTH_CRUXITE_BLOCK.get());
		output.accept(MSItems.CHISELED_CRUXITE_BLOCK.get());
		output.accept(MSItems.CRUXITE_PILLAR.get());
		output.accept(MSItems.CRUXITE_LAMP.get());
		
		output.accept(MSItems.URANIUM_BLOCK.get());
		output.accept(MSItems.URANIUM_STAIRS.get());
		output.accept(MSItems.URANIUM_SLAB.get());
		output.accept(MSItems.URANIUM_WALL.get());
		output.accept(MSItems.URANIUM_BUTTON.get());
		output.accept(MSItems.URANIUM_PRESSURE_PLATE.get());
		
		output.accept(MSItems.GENERIC_OBJECT.get());
		
		output.accept(MSItems.GREEN_STONE.get());
		output.accept(MSItems.GREEN_STONE_WALL.get());
		output.accept(MSItems.GREEN_STONE_BUTTON.get());
		output.accept(MSItems.GREEN_STONE_PRESSURE_PLATE.get());
		
		output.accept(MSItems.GREEN_STONE_BRICKS.get());
		output.accept(MSItems.GREEN_STONE_BRICK_WALL.get());
		
		output.accept(MSItems.GREEN_STONE_COLUMN.get());
		output.accept(MSItems.POLISHED_GREEN_STONE.get());
		output.accept(MSItems.POLISHED_GREEN_STONE_STAIRS.get());
		output.accept(MSItems.POLISHED_GREEN_STONE_SLAB.get());
		output.accept(MSItems.POLISHED_GREEN_STONE_WALL.get());
		output.accept(MSItems.CHISELED_GREEN_STONE_BRICKS.get());
		
		output.accept(MSItems.HORIZONTAL_GREEN_STONE_BRICKS.get());
		output.accept(MSItems.HORIZONTAL_GREEN_STONE_BRICK_STAIRS.get());
		output.accept(MSItems.HORIZONTAL_GREEN_STONE_BRICK_SLAB.get());
		output.accept(MSItems.HORIZONTAL_GREEN_STONE_BRICK_WALL.get());
		
		output.accept(MSItems.VERTICAL_GREEN_STONE_BRICKS.get());
		output.accept(MSItems.VERTICAL_GREEN_STONE_BRICK_STAIRS.get());
		output.accept(MSItems.VERTICAL_GREEN_STONE_BRICK_SLAB.get());
		output.accept(MSItems.VERTICAL_GREEN_STONE_BRICK_WALL.get());
		
		output.accept(MSItems.GREEN_STONE_BRICK_EMBEDDED_LADDER.get());
		output.accept(MSItems.GREEN_STONE_BRICK_TRIM.get());
		output.accept(MSItems.GREEN_STONE_BRICK_FROG.get());
		output.accept(MSItems.GREEN_STONE_BRICK_IGUANA_LEFT.get());
		output.accept(MSItems.GREEN_STONE_BRICK_IGUANA_RIGHT.get());
		output.accept(MSItems.GREEN_STONE_BRICK_LOTUS.get());
		output.accept(MSItems.GREEN_STONE_BRICK_NAK_LEFT.get());
		output.accept(MSItems.GREEN_STONE_BRICK_NAK_RIGHT.get());
		output.accept(MSItems.GREEN_STONE_BRICK_SALAMANDER_LEFT.get());
		output.accept(MSItems.GREEN_STONE_BRICK_SALAMANDER_RIGHT.get());
		output.accept(MSItems.GREEN_STONE_BRICK_SKAIA.get());
		output.accept(MSItems.GREEN_STONE_BRICK_TURTLE.get());
		
		output.accept(SkaiaBlocks.BLACK_CHESS_BRICK_STAIRS);
		output.accept(SkaiaBlocks.DARK_GRAY_CHESS_BRICK_STAIRS);
		output.accept(SkaiaBlocks.LIGHT_GRAY_CHESS_BRICK_STAIRS);
		output.accept(SkaiaBlocks.WHITE_CHESS_BRICK_STAIRS);
		output.accept(MSItems.GREEN_STONE_STAIRS.get());
		output.accept(MSItems.GREEN_STONE_BRICK_STAIRS.get());
		output.accept(MSItems.STEEP_GREEN_STONE_BRICK_STAIRS_BASE.get());
		output.accept(MSItems.STEEP_GREEN_STONE_BRICK_STAIRS_TOP.get());
		output.accept(SkaiaBlocks.BLACK_CHESS_BRICK_SLAB);
		output.accept(SkaiaBlocks.DARK_GRAY_CHESS_BRICK_SLAB);
		output.accept(SkaiaBlocks.LIGHT_GRAY_CHESS_BRICK_SLAB);
		output.accept(SkaiaBlocks.WHITE_CHESS_BRICK_SLAB);
		output.accept(MSItems.GREEN_STONE_SLAB.get());
		output.accept(MSItems.GREEN_STONE_BRICK_SLAB.get());
		
		output.accept(MSItems.WIRELESS_REDSTONE_TRANSMITTER.get());
		output.accept(MSItems.WIRELESS_REDSTONE_RECEIVER.get());
		output.accept(MSItems.SOLID_SWITCH.get());
		output.accept(MSItems.VARIABLE_SOLID_SWITCH.get());
		output.accept(MSItems.ONE_SECOND_INTERVAL_TIMED_SOLID_SWITCH.get());
		output.accept(MSItems.TWO_SECOND_INTERVAL_TIMED_SOLID_SWITCH.get());
		output.accept(MSItems.ITEM_MAGNET.get());
		output.accept(MSItems.REDSTONE_CLOCK.get());
		output.accept(MSItems.ROTATOR.get());
		output.accept(MSItems.BLOCK_TELEPORTER.get());
		output.accept(MSItems.TOGGLER.get());
		output.accept(MSItems.FALL_PAD.get());
		output.accept(MSItems.FRAGILE_STONE.get());
		output.accept(MSItems.SPIKES.get());
		output.accept(MSItems.RETRACTABLE_SPIKES.get());
		output.accept(MSItems.BLOCK_PRESSURE_PLATE.get());
		
		output.accept(MSItems.COMPUTER.get());
		output.accept(MSItems.LAPTOP.get());
		output.accept(MSItems.CROCKERTOP.get());
		output.accept(MSItems.HUBTOP.get());
		output.accept(MSItems.LUNCHTOP.get());
		output.accept(MSItems.OLD_COMPUTER.get());
		output.accept(MSItems.TRANSPORTALIZER.get());
		output.accept(MSItems.TRANS_PORTALIZER.get());
		output.accept(MSItems.SENDIFICATOR.get());
		output.accept(MSItems.GRIST_WIDGET.get());
		output.accept(MSItems.URANIUM_COOKER.get());
		output.accept(MSItems.GRIST_COLLECTOR.get());
		output.accept(MSItems.ANTHVIL.get());
		output.accept(MSItems.SKAIANET_DENIER.get());
		output.accept(MSItems.POWER_HUB.get());
		
		output.accept(MSItems.GOLD_SEEDS.get());
		
		output.accept(MSItems.APPLE_CAKE.get());
		output.accept(MSItems.BLUE_CAKE.get());
		output.accept(MSItems.COLD_CAKE.get());
		output.accept(MSItems.RED_CAKE.get());
		output.accept(MSItems.HOT_CAKE.get());
		output.accept(MSItems.REVERSE_CAKE.get());
		output.accept(MSItems.FUCHSIA_CAKE.get());
		output.accept(MSItems.NEGATIVE_CAKE.get());
		output.accept(MSItems.CARROT_CAKE.get());
		output.accept(MSItems.CHOCOLATEY_CAKE.get());
		
		output.accept(MSItems.PRIMED_TNT.get());
		output.accept(MSItems.UNSTABLE_TNT.get());
		output.accept(MSItems.INSTANT_TNT.get());
		output.accept(MSItems.WOODEN_EXPLOSIVE_BUTTON.get());
		output.accept(MSItems.STONE_EXPLOSIVE_BUTTON.get());
		
		output.accept(MSItems.BLENDER.get());
		output.accept(MSItems.CHESSBOARD.get());
		output.accept(MSItems.MINI_FROG_STATUE.get());
		output.accept(MSItems.MINI_WIZARD_STATUE.get());
		output.accept(MSItems.MINI_TYPHEUS_STATUE.get());
		output.accept(MSItems.CASSETTE_PLAYER.get());
		output.accept(MSItems.GLOWYSTONE_DUST.get());
		output.accept(MSItems.MIRROR.get());
		
		//DERIVATIVE BLOCKS
		
		output.accept(AspectTreeBlocks.BLOOD_ASPECT_LOG.get());
		output.accept(AspectTreeBlocks.BLOOD_ASPECT_WOOD.get());
		output.accept(AspectTreeBlocks.BLOOD_ASPECT_STRIPPED_LOG.get());
		output.accept(AspectTreeBlocks.BLOOD_ASPECT_STRIPPED_WOOD.get());
		output.accept(AspectTreeBlocks.BLOOD_ASPECT_PLANKS.get());
		output.accept(AspectTreeBlocks.BLOOD_ASPECT_CARVED_PLANKS.get());
		output.accept(AspectTreeBlocks.BLOOD_ASPECT_STAIRS.get());
		output.accept(AspectTreeBlocks.BLOOD_ASPECT_SLAB.get());
		output.accept(AspectTreeBlocks.BLOOD_ASPECT_FENCE.get());
		output.accept(AspectTreeBlocks.BLOOD_ASPECT_FENCE_GATE.get());
		output.accept(AspectTreeBlocks.BLOOD_ASPECT_DOOR.get());
		output.accept(AspectTreeBlocks.BLOOD_ASPECT_TRAPDOOR.get());
		output.accept(AspectTreeBlocks.BLOOD_ASPECT_PRESSURE_PLATE.get());
		output.accept(AspectTreeBlocks.BLOOD_ASPECT_BUTTON.get());
		output.accept(AspectTreeBlocks.BLOOD_ASPECT_SIGN.get());
		output.accept(AspectTreeBlocks.BLOOD_ASPECT_HANGING_SIGN.get());
		output.accept(AspectTreeBlocks.BLOOD_ASPECT_BOOKSHELF.get());
		output.accept(AspectTreeBlocks.BLOOD_ASPECT_LADDER.get());
		output.accept(AspectTreeBlocks.BLOOD_ASPECT_LEAVES.get());
		output.accept(AspectTreeBlocks.BLOOD_ASPECT_SAPLING.get());
		
		output.accept(AspectTreeBlocks.BREATH_ASPECT_LOG.get());
		output.accept(AspectTreeBlocks.BREATH_ASPECT_WOOD.get());
		output.accept(AspectTreeBlocks.BREATH_ASPECT_STRIPPED_LOG.get());
		output.accept(AspectTreeBlocks.BREATH_ASPECT_STRIPPED_WOOD.get());
		output.accept(AspectTreeBlocks.BREATH_ASPECT_PLANKS.get());
		output.accept(AspectTreeBlocks.BREATH_ASPECT_CARVED_PLANKS.get());
		output.accept(AspectTreeBlocks.BREATH_ASPECT_STAIRS.get());
		output.accept(AspectTreeBlocks.BREATH_ASPECT_SLAB.get());
		output.accept(AspectTreeBlocks.BREATH_ASPECT_FENCE.get());
		output.accept(AspectTreeBlocks.BREATH_ASPECT_FENCE_GATE.get());
		output.accept(AspectTreeBlocks.BREATH_ASPECT_DOOR.get());
		output.accept(AspectTreeBlocks.BREATH_ASPECT_TRAPDOOR.get());
		output.accept(AspectTreeBlocks.BREATH_ASPECT_PRESSURE_PLATE.get());
		output.accept(AspectTreeBlocks.BREATH_ASPECT_BUTTON.get());
		output.accept(AspectTreeBlocks.BREATH_ASPECT_SIGN.get());
		output.accept(AspectTreeBlocks.BREATH_ASPECT_HANGING_SIGN.get());
		output.accept(AspectTreeBlocks.BREATH_ASPECT_BOOKSHELF.get());
		output.accept(AspectTreeBlocks.BREATH_ASPECT_LADDER.get());
		output.accept(AspectTreeBlocks.BREATH_ASPECT_LEAVES.get());
		output.accept(AspectTreeBlocks.BREATH_ASPECT_SAPLING.get());
		
		output.accept(AspectTreeBlocks.DOOM_ASPECT_LOG.get());
		output.accept(AspectTreeBlocks.DOOM_ASPECT_WOOD.get());
		output.accept(AspectTreeBlocks.DOOM_ASPECT_STRIPPED_LOG.get());
		output.accept(AspectTreeBlocks.DOOM_ASPECT_STRIPPED_WOOD.get());
		output.accept(AspectTreeBlocks.DOOM_ASPECT_PLANKS.get());
		output.accept(AspectTreeBlocks.DOOM_ASPECT_CARVED_PLANKS.get());
		output.accept(AspectTreeBlocks.DOOM_ASPECT_STAIRS.get());
		output.accept(AspectTreeBlocks.DOOM_ASPECT_SLAB.get());
		output.accept(AspectTreeBlocks.DOOM_ASPECT_FENCE.get());
		output.accept(AspectTreeBlocks.DOOM_ASPECT_FENCE_GATE.get());
		output.accept(AspectTreeBlocks.DOOM_ASPECT_DOOR.get());
		output.accept(AspectTreeBlocks.DOOM_ASPECT_TRAPDOOR.get());
		output.accept(AspectTreeBlocks.DOOM_ASPECT_PRESSURE_PLATE.get());
		output.accept(AspectTreeBlocks.DOOM_ASPECT_BUTTON.get());
		output.accept(AspectTreeBlocks.DOOM_ASPECT_SIGN.get());
		output.accept(AspectTreeBlocks.DOOM_ASPECT_HANGING_SIGN.get());
		output.accept(AspectTreeBlocks.DOOM_ASPECT_BOOKSHELF.get());
		output.accept(AspectTreeBlocks.DOOM_ASPECT_LADDER.get());
		output.accept(AspectTreeBlocks.DOOM_ASPECT_LEAVES.get());
		output.accept(AspectTreeBlocks.DOOM_ASPECT_SAPLING.get());
		
		output.accept(AspectTreeBlocks.HEART_ASPECT_LOG.get());
		output.accept(AspectTreeBlocks.HEART_ASPECT_WOOD.get());
		output.accept(AspectTreeBlocks.HEART_ASPECT_STRIPPED_LOG.get());
		output.accept(AspectTreeBlocks.HEART_ASPECT_STRIPPED_WOOD.get());
		output.accept(AspectTreeBlocks.HEART_ASPECT_PLANKS.get());
		output.accept(AspectTreeBlocks.HEART_ASPECT_CARVED_PLANKS.get());
		output.accept(AspectTreeBlocks.HEART_ASPECT_STAIRS.get());
		output.accept(AspectTreeBlocks.HEART_ASPECT_SLAB.get());
		output.accept(AspectTreeBlocks.HEART_ASPECT_FENCE.get());
		output.accept(AspectTreeBlocks.HEART_ASPECT_FENCE_GATE.get());
		output.accept(AspectTreeBlocks.HEART_ASPECT_DOOR.get());
		output.accept(AspectTreeBlocks.HEART_ASPECT_TRAPDOOR.get());
		output.accept(AspectTreeBlocks.HEART_ASPECT_PRESSURE_PLATE.get());
		output.accept(AspectTreeBlocks.HEART_ASPECT_BUTTON.get());
		output.accept(AspectTreeBlocks.HEART_ASPECT_SIGN.get());
		output.accept(AspectTreeBlocks.HEART_ASPECT_HANGING_SIGN.get());
		output.accept(AspectTreeBlocks.HEART_ASPECT_BOOKSHELF.get());
		output.accept(AspectTreeBlocks.HEART_ASPECT_LADDER.get());
		output.accept(AspectTreeBlocks.HEART_ASPECT_LEAVES.get());
		output.accept(AspectTreeBlocks.HEART_ASPECT_SAPLING.get());
		
		output.accept(AspectTreeBlocks.HOPE_ASPECT_LOG.get());
		output.accept(AspectTreeBlocks.HOPE_ASPECT_WOOD.get());
		output.accept(AspectTreeBlocks.HOPE_ASPECT_STRIPPED_LOG.get());
		output.accept(AspectTreeBlocks.HOPE_ASPECT_STRIPPED_WOOD.get());
		output.accept(AspectTreeBlocks.HOPE_ASPECT_PLANKS.get());
		output.accept(AspectTreeBlocks.HOPE_ASPECT_CARVED_PLANKS.get());
		output.accept(AspectTreeBlocks.HOPE_ASPECT_STAIRS.get());
		output.accept(AspectTreeBlocks.HOPE_ASPECT_SLAB.get());
		output.accept(AspectTreeBlocks.HOPE_ASPECT_FENCE.get());
		output.accept(AspectTreeBlocks.HOPE_ASPECT_FENCE_GATE.get());
		output.accept(AspectTreeBlocks.HOPE_ASPECT_DOOR.get());
		output.accept(AspectTreeBlocks.HOPE_ASPECT_TRAPDOOR.get());
		output.accept(AspectTreeBlocks.HOPE_ASPECT_PRESSURE_PLATE.get());
		output.accept(AspectTreeBlocks.HOPE_ASPECT_BUTTON.get());
		output.accept(AspectTreeBlocks.HOPE_ASPECT_SIGN.get());
		output.accept(AspectTreeBlocks.HOPE_ASPECT_HANGING_SIGN.get());
		output.accept(AspectTreeBlocks.HOPE_ASPECT_BOOKSHELF.get());
		output.accept(AspectTreeBlocks.HOPE_ASPECT_LADDER.get());
		output.accept(AspectTreeBlocks.HOPE_ASPECT_LEAVES.get());
		output.accept(AspectTreeBlocks.HOPE_ASPECT_SAPLING.get());
		
		output.accept(AspectTreeBlocks.LIFE_ASPECT_LOG.get());
		output.accept(AspectTreeBlocks.LIFE_ASPECT_WOOD.get());
		output.accept(AspectTreeBlocks.LIFE_ASPECT_STRIPPED_LOG.get());
		output.accept(AspectTreeBlocks.LIFE_ASPECT_STRIPPED_WOOD.get());
		output.accept(AspectTreeBlocks.LIFE_ASPECT_PLANKS.get());
		output.accept(AspectTreeBlocks.LIFE_ASPECT_CARVED_PLANKS.get());
		output.accept(AspectTreeBlocks.LIFE_ASPECT_STAIRS.get());
		output.accept(AspectTreeBlocks.LIFE_ASPECT_SLAB.get());
		output.accept(AspectTreeBlocks.LIFE_ASPECT_FENCE.get());
		output.accept(AspectTreeBlocks.LIFE_ASPECT_FENCE_GATE.get());
		output.accept(AspectTreeBlocks.LIFE_ASPECT_DOOR.get());
		output.accept(AspectTreeBlocks.LIFE_ASPECT_TRAPDOOR.get());
		output.accept(AspectTreeBlocks.LIFE_ASPECT_PRESSURE_PLATE.get());
		output.accept(AspectTreeBlocks.LIFE_ASPECT_BUTTON.get());
		output.accept(AspectTreeBlocks.LIFE_ASPECT_SIGN.get());
		output.accept(AspectTreeBlocks.LIFE_ASPECT_HANGING_SIGN.get());
		output.accept(AspectTreeBlocks.LIFE_ASPECT_BOOKSHELF.get());
		output.accept(AspectTreeBlocks.LIFE_ASPECT_LADDER.get());
		output.accept(AspectTreeBlocks.LIFE_ASPECT_LEAVES.get());
		output.accept(AspectTreeBlocks.LIFE_ASPECT_SAPLING.get());
		
		output.accept(AspectTreeBlocks.LIGHT_ASPECT_LOG.get());
		output.accept(AspectTreeBlocks.LIGHT_ASPECT_WOOD.get());
		output.accept(AspectTreeBlocks.LIGHT_ASPECT_STRIPPED_LOG.get());
		output.accept(AspectTreeBlocks.LIGHT_ASPECT_STRIPPED_WOOD.get());
		output.accept(AspectTreeBlocks.LIGHT_ASPECT_PLANKS.get());
		output.accept(AspectTreeBlocks.LIGHT_ASPECT_CARVED_PLANKS.get());
		output.accept(AspectTreeBlocks.LIGHT_ASPECT_STAIRS.get());
		output.accept(AspectTreeBlocks.LIGHT_ASPECT_SLAB.get());
		output.accept(AspectTreeBlocks.LIGHT_ASPECT_FENCE.get());
		output.accept(AspectTreeBlocks.LIGHT_ASPECT_FENCE_GATE.get());
		output.accept(AspectTreeBlocks.LIGHT_ASPECT_DOOR.get());
		output.accept(AspectTreeBlocks.LIGHT_ASPECT_TRAPDOOR.get());
		output.accept(AspectTreeBlocks.LIGHT_ASPECT_PRESSURE_PLATE.get());
		output.accept(AspectTreeBlocks.LIGHT_ASPECT_BUTTON.get());
		output.accept(AspectTreeBlocks.LIGHT_ASPECT_SIGN.get());
		output.accept(AspectTreeBlocks.LIGHT_ASPECT_HANGING_SIGN.get());
		output.accept(AspectTreeBlocks.LIGHT_ASPECT_BOOKSHELF.get());
		output.accept(AspectTreeBlocks.LIGHT_ASPECT_LADDER.get());
		output.accept(AspectTreeBlocks.LIGHT_ASPECT_LEAVES.get());
		output.accept(AspectTreeBlocks.LIGHT_ASPECT_SAPLING.get());
		
		output.accept(AspectTreeBlocks.MIND_ASPECT_LOG.get());
		output.accept(AspectTreeBlocks.MIND_ASPECT_WOOD.get());
		output.accept(AspectTreeBlocks.MIND_ASPECT_STRIPPED_LOG.get());
		output.accept(AspectTreeBlocks.MIND_ASPECT_STRIPPED_WOOD.get());
		output.accept(AspectTreeBlocks.MIND_ASPECT_PLANKS.get());
		output.accept(AspectTreeBlocks.MIND_ASPECT_CARVED_PLANKS.get());
		output.accept(AspectTreeBlocks.MIND_ASPECT_STAIRS.get());
		output.accept(AspectTreeBlocks.MIND_ASPECT_SLAB.get());
		output.accept(AspectTreeBlocks.MIND_ASPECT_FENCE.get());
		output.accept(AspectTreeBlocks.MIND_ASPECT_FENCE_GATE.get());
		output.accept(AspectTreeBlocks.MIND_ASPECT_DOOR.get());
		output.accept(AspectTreeBlocks.MIND_ASPECT_TRAPDOOR.get());
		output.accept(AspectTreeBlocks.MIND_ASPECT_PRESSURE_PLATE.get());
		output.accept(AspectTreeBlocks.MIND_ASPECT_BUTTON.get());
		output.accept(AspectTreeBlocks.MIND_ASPECT_SIGN.get());
		output.accept(AspectTreeBlocks.MIND_ASPECT_HANGING_SIGN.get());
		output.accept(AspectTreeBlocks.MIND_ASPECT_BOOKSHELF.get());
		output.accept(AspectTreeBlocks.MIND_ASPECT_LADDER.get());
		output.accept(AspectTreeBlocks.MIND_ASPECT_LEAVES.get());
		output.accept(AspectTreeBlocks.MIND_ASPECT_SAPLING.get());
		
		output.accept(AspectTreeBlocks.RAGE_ASPECT_LOG.get());
		output.accept(AspectTreeBlocks.RAGE_ASPECT_WOOD.get());
		output.accept(AspectTreeBlocks.RAGE_ASPECT_STRIPPED_LOG.get());
		output.accept(AspectTreeBlocks.RAGE_ASPECT_STRIPPED_WOOD.get());
		output.accept(AspectTreeBlocks.RAGE_ASPECT_PLANKS.get());
		output.accept(AspectTreeBlocks.RAGE_ASPECT_CARVED_PLANKS.get());
		output.accept(AspectTreeBlocks.RAGE_ASPECT_STAIRS.get());
		output.accept(AspectTreeBlocks.RAGE_ASPECT_SLAB.get());
		output.accept(AspectTreeBlocks.RAGE_ASPECT_FENCE.get());
		output.accept(AspectTreeBlocks.RAGE_ASPECT_FENCE_GATE.get());
		output.accept(AspectTreeBlocks.RAGE_ASPECT_DOOR.get());
		output.accept(AspectTreeBlocks.RAGE_ASPECT_TRAPDOOR.get());
		output.accept(AspectTreeBlocks.RAGE_ASPECT_PRESSURE_PLATE.get());
		output.accept(AspectTreeBlocks.RAGE_ASPECT_BUTTON.get());
		output.accept(AspectTreeBlocks.RAGE_ASPECT_SIGN.get());
		output.accept(AspectTreeBlocks.RAGE_ASPECT_HANGING_SIGN.get());
		output.accept(AspectTreeBlocks.RAGE_ASPECT_BOOKSHELF.get());
		output.accept(AspectTreeBlocks.RAGE_ASPECT_LADDER.get());
		output.accept(AspectTreeBlocks.RAGE_ASPECT_LEAVES.get());
		output.accept(AspectTreeBlocks.RAGE_ASPECT_SAPLING.get());
		
		output.accept(AspectTreeBlocks.SPACE_ASPECT_LOG.get());
		output.accept(AspectTreeBlocks.SPACE_ASPECT_WOOD.get());
		output.accept(AspectTreeBlocks.SPACE_ASPECT_STRIPPED_LOG.get());
		output.accept(AspectTreeBlocks.SPACE_ASPECT_STRIPPED_WOOD.get());
		output.accept(AspectTreeBlocks.SPACE_ASPECT_PLANKS.get());
		output.accept(AspectTreeBlocks.SPACE_ASPECT_CARVED_PLANKS.get());
		output.accept(AspectTreeBlocks.SPACE_ASPECT_STAIRS.get());
		output.accept(AspectTreeBlocks.SPACE_ASPECT_SLAB.get());
		output.accept(AspectTreeBlocks.SPACE_ASPECT_FENCE.get());
		output.accept(AspectTreeBlocks.SPACE_ASPECT_FENCE_GATE.get());
		output.accept(AspectTreeBlocks.SPACE_ASPECT_DOOR.get());
		output.accept(AspectTreeBlocks.SPACE_ASPECT_TRAPDOOR.get());
		output.accept(AspectTreeBlocks.SPACE_ASPECT_PRESSURE_PLATE.get());
		output.accept(AspectTreeBlocks.SPACE_ASPECT_BUTTON.get());
		output.accept(AspectTreeBlocks.SPACE_ASPECT_SIGN.get());
		output.accept(AspectTreeBlocks.SPACE_ASPECT_HANGING_SIGN.get());
		output.accept(AspectTreeBlocks.SPACE_ASPECT_BOOKSHELF.get());
		output.accept(AspectTreeBlocks.SPACE_ASPECT_LADDER.get());
		output.accept(AspectTreeBlocks.SPACE_ASPECT_LEAVES.get());
		output.accept(AspectTreeBlocks.SPACE_ASPECT_SAPLING.get());
		
		output.accept(AspectTreeBlocks.TIME_ASPECT_LOG.get());
		output.accept(AspectTreeBlocks.TIME_ASPECT_WOOD.get());
		output.accept(AspectTreeBlocks.TIME_ASPECT_STRIPPED_LOG.get());
		output.accept(AspectTreeBlocks.TIME_ASPECT_STRIPPED_WOOD.get());
		output.accept(AspectTreeBlocks.TIME_ASPECT_PLANKS.get());
		output.accept(AspectTreeBlocks.TIME_ASPECT_CARVED_PLANKS.get());
		output.accept(AspectTreeBlocks.TIME_ASPECT_STAIRS.get());
		output.accept(AspectTreeBlocks.TIME_ASPECT_SLAB.get());
		output.accept(AspectTreeBlocks.TIME_ASPECT_FENCE.get());
		output.accept(AspectTreeBlocks.TIME_ASPECT_FENCE_GATE.get());
		output.accept(AspectTreeBlocks.TIME_ASPECT_DOOR.get());
		output.accept(AspectTreeBlocks.TIME_ASPECT_TRAPDOOR.get());
		output.accept(AspectTreeBlocks.TIME_ASPECT_PRESSURE_PLATE.get());
		output.accept(AspectTreeBlocks.TIME_ASPECT_BUTTON.get());
		output.accept(AspectTreeBlocks.TIME_ASPECT_SIGN.get());
		output.accept(AspectTreeBlocks.TIME_ASPECT_HANGING_SIGN.get());
		output.accept(AspectTreeBlocks.TIME_ASPECT_BOOKSHELF.get());
		output.accept(AspectTreeBlocks.TIME_ASPECT_LADDER.get());
		output.accept(AspectTreeBlocks.TIME_ASPECT_LEAVES.get());
		output.accept(AspectTreeBlocks.TIME_ASPECT_SAPLING.get());
		
		output.accept(AspectTreeBlocks.VOID_ASPECT_LOG.get());
		output.accept(AspectTreeBlocks.VOID_ASPECT_WOOD.get());
		output.accept(AspectTreeBlocks.VOID_ASPECT_STRIPPED_LOG.get());
		output.accept(AspectTreeBlocks.VOID_ASPECT_STRIPPED_WOOD.get());
		output.accept(AspectTreeBlocks.VOID_ASPECT_PLANKS.get());
		output.accept(AspectTreeBlocks.VOID_ASPECT_CARVED_PLANKS.get());
		output.accept(AspectTreeBlocks.VOID_ASPECT_STAIRS.get());
		output.accept(AspectTreeBlocks.VOID_ASPECT_SLAB.get());
		output.accept(AspectTreeBlocks.VOID_ASPECT_FENCE.get());
		output.accept(AspectTreeBlocks.VOID_ASPECT_FENCE_GATE.get());
		output.accept(AspectTreeBlocks.VOID_ASPECT_DOOR.get());
		output.accept(AspectTreeBlocks.VOID_ASPECT_TRAPDOOR.get());
		output.accept(AspectTreeBlocks.VOID_ASPECT_PRESSURE_PLATE.get());
		output.accept(AspectTreeBlocks.VOID_ASPECT_BUTTON.get());
		output.accept(AspectTreeBlocks.VOID_ASPECT_SIGN.get());
		output.accept(AspectTreeBlocks.VOID_ASPECT_HANGING_SIGN.get());
		output.accept(AspectTreeBlocks.VOID_ASPECT_BOOKSHELF.get());
		output.accept(AspectTreeBlocks.VOID_ASPECT_LADDER.get());
		output.accept(AspectTreeBlocks.VOID_ASPECT_LEAVES.get());
		output.accept(AspectTreeBlocks.VOID_ASPECT_SAPLING.get());
		
		output.accept(MSItems.PERFECTLY_GENERIC_STAIRS.get());
		output.accept(MSItems.PERFECTLY_GENERIC_SLAB.get());
		output.accept(MSItems.PERFECTLY_GENERIC_WALL.get());
		output.accept(MSItems.PERFECTLY_GENERIC_FENCE.get());
		output.accept(MSItems.PERFECTLY_GENERIC_FENCE_GATE.get());
		output.accept(MSItems.PERFECTLY_GENERIC_BUTTON.get());
		output.accept(MSItems.PERFECTLY_GENERIC_PRESSURE_PLATE.get());
		output.accept(MSItems.PERFECTLY_GENERIC_DOOR.get());
		output.accept(MSItems.PERFECTLY_GENERIC_TRAPDOOR.get());
		output.accept(MSItems.PERFECTLY_GENERIC_HANGING_SIGN.get());
		output.accept(MSItems.PERFECTLY_GENERIC_SIGN.get());
	}
	
	private static void buildLandsTab(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output output)
	{
		output.accept(MSItems.CHOCOLATE_BEETLE.get());
		output.accept(MSItems.CONE_OF_FLIES.get());
		output.accept(MSItems.GRASSHOPPER.get());
		output.accept(MSItems.CICADA.get());
		output.accept(MSItems.JAR_OF_BUGS.get());
		output.accept(MSItems.BUG_MAC.get());
		output.accept(MSItems.ONION.get());
		output.accept(MSItems.SALAD.get());
		output.accept(MSItems.DESERT_FRUIT.get());
		output.accept(MSItems.ROCK_COOKIE.get());
		output.accept(MSItems.WOODEN_CARROT.get());
		output.accept(MSItems.FUNGAL_SPORE.get());
		output.accept(MSItems.SPOREO.get());
		output.accept(MSItems.MOREL_MUSHROOM.get());
		output.accept(MSItems.SUSHROOM.get());
		output.accept(MSItems.FRENCH_FRY.get());
		output.accept(MSItems.STRAWBERRY_CHUNK.get());
		output.accept(MSItems.GRUB_SAUCE.get());
		output.accept(MSItems.GOLDEN_GRASSHOPPER.get());
		output.accept(MSItems.BUG_NET.get());
		output.accept(MSItems.FROG.get());
		// FROG TYPES
		for(int i : new int[]{1, 2, 5, 6})
		{
			ItemStack item = new ItemStack(MSItems.FROG.get());
			item.getOrCreateTag().putInt("Type", i);
			output.accept(item);
		}
		output.accept(MSItems.CARVING_TOOL.get());
		output.accept(MSItems.CRUMPLY_HAT.get());
		output.accept(MSItems.STONE_EYEBALLS.get());
		output.accept(MSItems.STONE_TABLET.get());
		
		output.accept(MSItems.SHOP_POSTER.get());
		output.accept(MSItems.FLARP_MANUAL.get());
		output.accept(MSItems.SASSACRE_TEXT.get());
		output.accept(MSItems.WISEGUY.get());
		output.accept(MSItems.TABLESTUCK_MANUAL.get());
		output.accept(MSItems.TILLDEATH_HANDBOOK.get());
		output.accept(MSItems.BINARY_CODE.get());
		output.accept(MSItems.NONBINARY_CODE.get());
		
		output.accept(MSItems.STONE_CRUXITE_ORE.get());
		output.accept(MSItems.NETHERRACK_CRUXITE_ORE.get());
		output.accept(MSItems.COBBLESTONE_CRUXITE_ORE.get());
		output.accept(MSItems.SANDSTONE_CRUXITE_ORE.get());
		output.accept(MSItems.RED_SANDSTONE_CRUXITE_ORE.get());
		output.accept(MSItems.END_STONE_CRUXITE_ORE.get());
		output.accept(MSItems.SHADE_STONE_CRUXITE_ORE.get());
		output.accept(MSItems.PINK_STONE_CRUXITE_ORE.get());
		output.accept(MSItems.MYCELIUM_STONE_CRUXITE_ORE.get());
		output.accept(MSItems.UNCARVED_WOOD_CRUXITE_ORE.get());
		output.accept(MSItems.BLACK_STONE_CRUXITE_ORE.get());
		
		output.accept(MSItems.STONE_URANIUM_ORE.get());
		output.accept(MSItems.DEEPSLATE_URANIUM_ORE.get());
		output.accept(MSItems.NETHERRACK_URANIUM_ORE.get());
		output.accept(MSItems.COBBLESTONE_URANIUM_ORE.get());
		output.accept(MSItems.SANDSTONE_URANIUM_ORE.get());
		output.accept(MSItems.RED_SANDSTONE_URANIUM_ORE.get());
		output.accept(MSItems.END_STONE_URANIUM_ORE.get());
		output.accept(MSItems.SHADE_STONE_URANIUM_ORE.get());
		output.accept(MSItems.PINK_STONE_URANIUM_ORE.get());
		output.accept(MSItems.MYCELIUM_STONE_URANIUM_ORE.get());
		output.accept(MSItems.UNCARVED_WOOD_URANIUM_ORE.get());
		output.accept(MSItems.BLACK_STONE_URANIUM_ORE.get());
		
		output.accept(MSItems.NETHERRACK_COAL_ORE.get());
		output.accept(MSItems.SHADE_STONE_COAL_ORE.get());
		output.accept(MSItems.PINK_STONE_COAL_ORE.get());
		
		output.accept(MSItems.END_STONE_IRON_ORE.get());
		output.accept(MSItems.SANDSTONE_IRON_ORE.get());
		output.accept(MSItems.RED_SANDSTONE_IRON_ORE.get());
		output.accept(MSItems.UNCARVED_WOOD_IRON_ORE.get());
		
		output.accept(MSItems.SANDSTONE_GOLD_ORE.get());
		output.accept(MSItems.RED_SANDSTONE_GOLD_ORE.get());
		output.accept(MSItems.SHADE_STONE_GOLD_ORE.get());
		output.accept(MSItems.PINK_STONE_GOLD_ORE.get());
		output.accept(MSItems.BLACK_STONE_GOLD_ORE.get());
		
		output.accept(MSItems.END_STONE_REDSTONE_ORE.get());
		output.accept(MSItems.UNCARVED_WOOD_REDSTONE_ORE.get());
		output.accept(MSItems.BLACK_STONE_REDSTONE_ORE.get());
		
		output.accept(MSItems.STONE_QUARTZ_ORE.get());
		output.accept(MSItems.BLACK_STONE_QUARTZ_ORE.get());
		
		output.accept(MSItems.PINK_STONE_LAPIS_ORE.get());
		
		output.accept(MSItems.PINK_STONE_DIAMOND_ORE.get());
		
		output.accept(MSItems.UNCARVED_WOOD_EMERALD_ORE.get());
		
		output.accept(MSItems.BLUE_DIRT.get());
		output.accept(MSItems.THOUGHT_DIRT.get());
		
		output.accept(MSItems.COARSE_STONE.get());
		output.accept(MSItems.COARSE_STONE_WALL.get());
		output.accept(MSItems.COARSE_STONE_BUTTON.get());
		output.accept(MSItems.COARSE_STONE_PRESSURE_PLATE.get());
		
		output.accept(MSItems.CHISELED_COARSE_STONE.get());
		
		output.accept(MSItems.COARSE_STONE_BRICKS.get());
		output.accept(MSItems.COARSE_STONE_BRICK_WALL.get());
		
		output.accept(MSItems.COARSE_STONE_COLUMN.get());
		output.accept(MSItems.CHISELED_COARSE_STONE_BRICKS.get());
		output.accept(MSItems.CRACKED_COARSE_STONE_BRICKS.get());
		output.accept(MSItems.MOSSY_COARSE_STONE_BRICKS.get());
		
		output.accept(MSItems.SHADE_STONE.get());
		output.accept(MSItems.SHADE_WALL.get());
		output.accept(MSItems.SHADE_BUTTON.get());
		output.accept(MSItems.SHADE_PRESSURE_PLATE.get());
		
		output.accept(MSItems.SMOOTH_SHADE_STONE.get());
		output.accept(MSItems.SMOOTH_SHADE_STONE_STAIRS.get());
		output.accept(MSItems.SMOOTH_SHADE_STONE_SLAB.get());
		output.accept(MSItems.SMOOTH_SHADE_STONE_WALL.get());
		
		output.accept(MSItems.SHADE_BRICKS.get());
		output.accept(MSItems.SHADE_BRICK_WALL.get());
		
		output.accept(MSItems.SHADE_COLUMN.get());
		output.accept(MSItems.CHISELED_SHADE_BRICKS.get());
		output.accept(MSItems.CRACKED_SHADE_BRICKS.get());
		
		output.accept(MSItems.MOSSY_SHADE_BRICKS.get());
		output.accept(MSItems.MOSSY_SHADE_BRICK_STAIRS.get());
		output.accept(MSItems.MOSSY_SHADE_BRICK_SLAB.get());
		output.accept(MSItems.MOSSY_SHADE_BRICK_WALL.get());
		
		output.accept(MSItems.BLOOD_SHADE_BRICKS.get());
		output.accept(MSItems.BLOOD_SHADE_BRICK_STAIRS.get());
		output.accept(MSItems.BLOOD_SHADE_BRICK_SLAB.get());
		output.accept(MSItems.BLOOD_SHADE_BRICK_WALL.get());
		
		output.accept(MSItems.TAR_SHADE_BRICKS.get());
		output.accept(MSItems.TAR_SHADE_BRICK_STAIRS.get());
		output.accept(MSItems.TAR_SHADE_BRICK_SLAB.get());
		output.accept(MSItems.TAR_SHADE_BRICK_WALL.get());
		
		output.accept(MSItems.FROST_TILE.get());
		output.accept(MSItems.FROST_TILE_WALL.get());
		
		output.accept(MSItems.CHISELED_FROST_TILE.get());
		
		output.accept(MSItems.FROST_BRICKS.get());
		output.accept(MSItems.FROST_BRICK_WALL.get());
		
		output.accept(MSItems.FROST_COLUMN.get());
		output.accept(MSItems.CHISELED_FROST_BRICKS.get());
		output.accept(MSItems.CRACKED_FROST_BRICKS.get());
		
		output.accept(MSItems.FLOWERY_FROST_BRICKS.get());
		output.accept(MSItems.FLOWERY_FROST_BRICK_STAIRS.get());
		output.accept(MSItems.FLOWERY_FROST_BRICK_SLAB.get());
		output.accept(MSItems.FLOWERY_FROST_BRICK_WALL.get());
		
		output.accept(MSItems.CAST_IRON.get());
		output.accept(MSItems.CAST_IRON_STAIRS.get());
		output.accept(MSItems.CAST_IRON_SLAB.get());
		output.accept(MSItems.CAST_IRON_WALL.get());
		output.accept(MSItems.CAST_IRON_BUTTON.get());
		output.accept(MSItems.CAST_IRON_PRESSURE_PLATE.get());
		
		output.accept(MSItems.CAST_IRON_TILE.get());
		output.accept(MSItems.CAST_IRON_TILE_STAIRS.get());
		output.accept(MSItems.CAST_IRON_TILE_SLAB.get());
		
		output.accept(MSItems.CAST_IRON_SHEET.get());
		output.accept(MSItems.CAST_IRON_SHEET_STAIRS.get());
		output.accept(MSItems.CAST_IRON_SHEET_SLAB.get());
		
		output.accept(MSItems.CHISELED_CAST_IRON.get());
		output.accept(MSItems.CAST_IRON_FRAME.get());
		
		output.accept(MSItems.STEEL_BEAM.get());
		
		output.accept(MSItems.MYCELIUM_COBBLESTONE.get());
		output.accept(MSItems.MYCELIUM_COBBLESTONE_STAIRS.get());
		output.accept(MSItems.MYCELIUM_COBBLESTONE_SLAB.get());
		output.accept(MSItems.MYCELIUM_COBBLESTONE_WALL.get());
		
		output.accept(MSItems.MYCELIUM_STONE.get());
		output.accept(MSItems.MYCELIUM_STONE_WALL.get());
		output.accept(MSItems.MYCELIUM_STONE_BUTTON.get());
		output.accept(MSItems.MYCELIUM_STONE_PRESSURE_PLATE.get());
		
		output.accept(MSItems.POLISHED_MYCELIUM_STONE.get());
		output.accept(MSItems.POLISHED_MYCELIUM_STONE_STAIRS.get());
		output.accept(MSItems.POLISHED_MYCELIUM_STONE_SLAB.get());
		output.accept(MSItems.POLISHED_MYCELIUM_STONE_WALL.get());
		
		output.accept(MSItems.MYCELIUM_BRICKS.get());
		output.accept(MSItems.MYCELIUM_BRICK_WALL.get());
		
		output.accept(MSItems.MYCELIUM_COLUMN.get());
		output.accept(MSItems.CHISELED_MYCELIUM_BRICKS.get());
		output.accept(MSItems.SUSPICIOUS_CHISELED_MYCELIUM_BRICKS.get());
		output.accept(MSItems.CRACKED_MYCELIUM_BRICKS.get());
		
		output.accept(MSItems.MOSSY_MYCELIUM_BRICKS.get());
		output.accept(MSItems.MOSSY_MYCELIUM_BRICK_STAIRS.get());
		output.accept(MSItems.MOSSY_MYCELIUM_BRICK_SLAB.get());
		output.accept(MSItems.MOSSY_MYCELIUM_BRICK_WALL.get());
		
		output.accept(MSItems.FLOWERY_MYCELIUM_BRICKS.get());
		output.accept(MSItems.FLOWERY_MYCELIUM_BRICK_STAIRS.get());
		output.accept(MSItems.FLOWERY_MYCELIUM_BRICK_SLAB.get());
		output.accept(MSItems.FLOWERY_MYCELIUM_BRICK_WALL.get());
		
		output.accept(MSItems.BLACK_STONE.get());
		output.accept(MSItems.BLACK_STONE_STAIRS.get());
		output.accept(MSItems.BLACK_STONE_SLAB.get());
		output.accept(MSItems.BLACK_STONE_WALL.get());
		output.accept(MSItems.BLACK_STONE_BUTTON.get());
		output.accept(MSItems.BLACK_STONE_PRESSURE_PLATE.get());
		
		output.accept(MSItems.POLISHED_BLACK_STONE.get());
		output.accept(MSItems.POLISHED_BLACK_STONE_STAIRS.get());
		output.accept(MSItems.POLISHED_BLACK_STONE_SLAB.get());
		output.accept(MSItems.POLISHED_BLACK_STONE_WALL.get());
		
		output.accept(MSItems.BLACK_COBBLESTONE.get());
		output.accept(MSItems.BLACK_COBBLESTONE_STAIRS.get());
		output.accept(MSItems.BLACK_COBBLESTONE_SLAB.get());
		output.accept(MSItems.BLACK_COBBLESTONE_WALL.get());
		
		output.accept(MSItems.BLACK_STONE_BRICKS.get());
		output.accept(MSItems.BLACK_STONE_BRICK_STAIRS.get());
		output.accept(MSItems.BLACK_STONE_BRICK_SLAB.get());
		output.accept(MSItems.BLACK_STONE_BRICK_WALL.get());
		
		output.accept(MSItems.BLACK_STONE_COLUMN.get());
		output.accept(MSItems.CHISELED_BLACK_STONE_BRICKS.get());
		output.accept(MSItems.CRACKED_BLACK_STONE_BRICKS.get());
		
		output.accept(MSItems.MAGMATIC_BLACK_STONE_BRICKS.get());
		output.accept(MSItems.MAGMATIC_BLACK_STONE_BRICK_STAIRS.get());
		output.accept(MSItems.MAGMATIC_BLACK_STONE_BRICK_SLAB.get());
		output.accept(MSItems.MAGMATIC_BLACK_STONE_BRICK_WALL.get());
		
		output.accept(MSItems.BLACK_SAND.get());
		
		output.accept(MSItems.IGNEOUS_STONE.get());
		output.accept(MSItems.IGNEOUS_STONE_STAIRS.get());
		output.accept(MSItems.IGNEOUS_STONE_SLAB.get());
		output.accept(MSItems.IGNEOUS_STONE_WALL.get());
		output.accept(MSItems.IGNEOUS_STONE_BUTTON.get());
		output.accept(MSItems.IGNEOUS_STONE_PRESSURE_PLATE.get());
		
		output.accept(MSItems.POLISHED_IGNEOUS_STONE.get());
		output.accept(MSItems.POLISHED_IGNEOUS_STAIRS.get());
		output.accept(MSItems.POLISHED_IGNEOUS_SLAB.get());
		output.accept(MSItems.POLISHED_IGNEOUS_WALL.get());
		
		output.accept(MSItems.POLISHED_IGNEOUS_BRICKS.get());
		output.accept(MSItems.POLISHED_IGNEOUS_BRICK_STAIRS.get());
		output.accept(MSItems.POLISHED_IGNEOUS_BRICK_SLAB.get());
		output.accept(MSItems.POLISHED_IGNEOUS_BRICK_WALL.get());
		
		output.accept(MSItems.POLISHED_IGNEOUS_PILLAR.get());
		output.accept(MSItems.CHISELED_IGNEOUS_STONE.get());
		output.accept(MSItems.CRACKED_POLISHED_IGNEOUS_BRICKS.get());
		
		output.accept(MSItems.MAGMATIC_POLISHED_IGNEOUS_BRICKS.get());
		output.accept(MSItems.MAGMATIC_POLISHED_IGNEOUS_BRICK_STAIRS.get());
		output.accept(MSItems.MAGMATIC_POLISHED_IGNEOUS_BRICK_SLAB.get());
		output.accept(MSItems.MAGMATIC_POLISHED_IGNEOUS_BRICK_WALL.get());
		
		output.accept(MSItems.MAGMATIC_IGNEOUS_STONE.get());
		
		output.accept(MSItems.PUMICE_STONE.get());
		output.accept(MSItems.PUMICE_STONE_STAIRS.get());
		output.accept(MSItems.PUMICE_STONE_SLAB.get());
		output.accept(MSItems.PUMICE_STONE_WALL.get());
		output.accept(MSItems.PUMICE_STONE_BUTTON.get());
		output.accept(MSItems.PUMICE_STONE_PRESSURE_PLATE.get());
		
		output.accept(MSItems.PUMICE_BRICKS.get());
		output.accept(MSItems.PUMICE_BRICK_STAIRS.get());
		output.accept(MSItems.PUMICE_BRICK_SLAB.get());
		output.accept(MSItems.PUMICE_BRICK_WALL.get());
		
		output.accept(MSItems.PUMICE_TILES.get());
		output.accept(MSItems.PUMICE_TILE_STAIRS.get());
		output.accept(MSItems.PUMICE_TILE_SLAB.get());
		output.accept(MSItems.PUMICE_TILE_WALL.get());
		
		output.accept(MSItems.HEAT_LAMP.get());
		
		output.accept(MSItems.DECREPIT_STONE_BRICKS.get());
		output.accept(MSItems.DECREPIT_STONE_BRICK_STAIRS.get());
		output.accept(MSItems.DECREPIT_STONE_BRICK_SLAB.get());
		output.accept(MSItems.DECREPIT_STONE_BRICK_WALL.get());
		
		output.accept(MSItems.FLOWERY_MOSSY_COBBLESTONE.get());
		output.accept(MSItems.FLOWERY_MOSSY_COBBLESTONE_STAIRS.get());
		output.accept(MSItems.FLOWERY_MOSSY_COBBLESTONE_SLAB.get());
		output.accept(MSItems.FLOWERY_MOSSY_COBBLESTONE_WALL.get());
		
		output.accept(MSItems.MOSSY_DECREPIT_STONE_BRICKS.get());
		output.accept(MSItems.MOSSY_DECREPIT_STONE_BRICK_STAIRS.get());
		output.accept(MSItems.MOSSY_DECREPIT_STONE_BRICK_SLAB.get());
		output.accept(MSItems.MOSSY_DECREPIT_STONE_BRICK_WALL.get());
		
		output.accept(MSItems.FLOWERY_MOSSY_STONE_BRICKS.get());
		output.accept(MSItems.FLOWERY_MOSSY_STONE_BRICK_STAIRS.get());
		output.accept(MSItems.FLOWERY_MOSSY_STONE_BRICK_SLAB.get());
		output.accept(MSItems.FLOWERY_MOSSY_STONE_BRICK_WALL.get());
		
		output.accept(MSItems.COARSE_END_STONE.get());
		output.accept(MSItems.END_GRASS.get());
		
		output.accept(MSItems.CHALK.get());
		output.accept(MSItems.CHALK_WALL.get());
		output.accept(MSItems.CHALK_BUTTON.get());
		output.accept(MSItems.CHALK_PRESSURE_PLATE.get());
		
		output.accept(MSItems.POLISHED_CHALK.get());
		output.accept(MSItems.POLISHED_CHALK_STAIRS.get());
		output.accept(MSItems.POLISHED_CHALK_SLAB.get());
		output.accept(MSItems.POLISHED_CHALK_WALL.get());
		
		output.accept(MSItems.CHALK_BRICKS.get());
		output.accept(MSItems.CHALK_BRICK_WALL.get());
		
		output.accept(MSItems.CHALK_COLUMN.get());
		output.accept(MSItems.CHISELED_CHALK_BRICKS.get());
		
		output.accept(MSItems.MOSSY_CHALK_BRICKS.get());
		output.accept(MSItems.MOSSY_CHALK_BRICK_STAIRS.get());
		output.accept(MSItems.MOSSY_CHALK_BRICK_SLAB.get());
		output.accept(MSItems.MOSSY_CHALK_BRICK_WALL.get());
		
		output.accept(MSItems.FLOWERY_CHALK_BRICKS.get());
		output.accept(MSItems.FLOWERY_CHALK_BRICK_STAIRS.get());
		output.accept(MSItems.FLOWERY_CHALK_BRICK_SLAB.get());
		output.accept(MSItems.FLOWERY_CHALK_BRICK_WALL.get());
		
		output.accept(MSItems.PINK_STONE.get());
		output.accept(MSItems.PINK_STONE_WALL.get());
		output.accept(MSItems.PINK_STONE_BUTTON.get());
		output.accept(MSItems.PINK_STONE_PRESSURE_PLATE.get());
		
		output.accept(MSItems.PINK_STONE_BRICKS.get());
		output.accept(MSItems.PINK_STONE_BRICK_WALL.get());
		output.accept(MSItems.MOSSY_PINK_STONE_BRICK_STAIRS.get());
		output.accept(MSItems.MOSSY_PINK_STONE_BRICK_SLAB.get());
		output.accept(MSItems.MOSSY_PINK_STONE_BRICK_WALL.get());
		
		output.accept(MSItems.CHISELED_PINK_STONE_BRICKS.get());
		output.accept(MSItems.CRACKED_PINK_STONE_BRICKS.get());
		output.accept(MSItems.MOSSY_PINK_STONE_BRICKS.get());
		
		output.accept(MSItems.POLISHED_PINK_STONE.get());
		output.accept(MSItems.POLISHED_PINK_STONE_STAIRS.get());
		output.accept(MSItems.POLISHED_PINK_STONE_SLAB.get());
		output.accept(MSItems.POLISHED_PINK_STONE_WALL.get());
		
		output.accept(MSItems.PINK_STONE_COLUMN.get());
		
		output.accept(MSItems.BROWN_STONE.get());
		output.accept(MSItems.BROWN_STONE_WALL.get());
		output.accept(MSItems.BROWN_STONE_BUTTON.get());
		output.accept(MSItems.BROWN_STONE_PRESSURE_PLATE.get());
		
		output.accept(MSItems.BROWN_STONE_BRICKS.get());
		output.accept(MSItems.BROWN_STONE_BRICK_WALL.get());
		
		output.accept(MSItems.BROWN_STONE_COLUMN.get());
		output.accept(MSItems.CRACKED_BROWN_STONE_BRICKS.get());
		
		output.accept(MSItems.POLISHED_BROWN_STONE.get());
		output.accept(MSItems.POLISHED_BROWN_STONE_STAIRS.get());
		output.accept(MSItems.POLISHED_BROWN_STONE_SLAB.get());
		output.accept(MSItems.POLISHED_BROWN_STONE_WALL.get());
		
		output.accept(MSItems.SANDSTONE_COLUMN.get());
		output.accept(MSItems.CHISELED_SANDSTONE_COLUMN.get());
		output.accept(MSItems.RED_SANDSTONE_COLUMN.get());
		output.accept(MSItems.CHISELED_RED_SANDSTONE_COLUMN.get());
		
		output.accept(MSItems.CARVED_LOG.get());
		output.accept(MSItems.CARVED_WOODEN_LEAF.get());
		
		output.accept(MSItems.UNCARVED_WOOD.get());
		output.accept(MSItems.UNCARVED_WOOD_STAIRS.get());
		output.accept(MSItems.UNCARVED_WOOD_SLAB.get());
		output.accept(MSItems.UNCARVED_WOOD_BUTTON.get());
		output.accept(MSItems.UNCARVED_WOOD_PRESSURE_PLATE.get());
		output.accept(MSItems.UNCARVED_WOOD_FENCE.get());
		output.accept(MSItems.UNCARVED_WOOD_FENCE_GATE.get());
		
		output.accept(MSItems.CHIPBOARD.get());
		output.accept(MSItems.CHIPBOARD_STAIRS.get());
		output.accept(MSItems.CHIPBOARD_SLAB.get());
		output.accept(MSItems.CHIPBOARD_BUTTON.get());
		output.accept(MSItems.CHIPBOARD_PRESSURE_PLATE.get());
		output.accept(MSItems.CHIPBOARD_FENCE.get());
		output.accept(MSItems.CHIPBOARD_FENCE_GATE.get());
		
		output.accept(MSItems.WOOD_SHAVINGS.get());
		
		output.accept(MSItems.CARVED_HEAVY_PLANKS.get());
		output.accept(MSItems.CARVED_HEAVY_PLANK_STAIRS.get());
		output.accept(MSItems.CARVED_HEAVY_PLANK_SLAB.get());
		
		output.accept(MSItems.CARVED_PLANKS.get());
		output.accept(MSItems.CARVED_STAIRS.get());
		output.accept(MSItems.CARVED_SLAB.get());
		output.accept(MSItems.CARVED_BUTTON.get());
		output.accept(MSItems.CARVED_PRESSURE_PLATE.get());
		output.accept(MSItems.CARVED_FENCE.get());
		output.accept(MSItems.CARVED_FENCE_GATE.get());
		output.accept(MSItems.CARVED_DOOR.get());
		output.accept(MSItems.CARVED_TRAPDOOR.get());
		output.accept(MSItems.CARVED_HANGING_SIGN.get());
		output.accept(MSItems.CARVED_SIGN.get());
		
		output.accept(MSItems.POLISHED_UNCARVED_WOOD.get());
		output.accept(MSItems.POLISHED_UNCARVED_STAIRS.get());
		output.accept(MSItems.POLISHED_UNCARVED_SLAB.get());
		
		output.accept(MSItems.CARVED_KNOTTED_WOOD.get());
		output.accept(MSItems.CARVED_BUSH.get());
		output.accept(MSItems.WOODEN_GRASS.get());
		
		output.accept(MSItems.TREATED_UNCARVED_WOOD.get());
		output.accept(MSItems.TREATED_UNCARVED_WOOD_STAIRS.get());
		output.accept(MSItems.TREATED_UNCARVED_WOOD_SLAB.get());
		output.accept(MSItems.TREATED_UNCARVED_WOOD_BUTTON.get());
		output.accept(MSItems.TREATED_UNCARVED_WOOD_PRESSURE_PLATE.get());
		output.accept(MSItems.TREATED_UNCARVED_WOOD_FENCE.get());
		output.accept(MSItems.TREATED_UNCARVED_WOOD_FENCE_GATE.get());
		
		output.accept(MSItems.TREATED_CHIPBOARD.get());
		output.accept(MSItems.TREATED_CHIPBOARD_STAIRS.get());
		output.accept(MSItems.TREATED_CHIPBOARD_SLAB.get());
		output.accept(MSItems.TREATED_CHIPBOARD_BUTTON.get());
		output.accept(MSItems.TREATED_CHIPBOARD_PRESSURE_PLATE.get());
		output.accept(MSItems.TREATED_CHIPBOARD_FENCE.get());
		output.accept(MSItems.TREATED_CHIPBOARD_FENCE_GATE.get());
		
		output.accept(MSItems.TREATED_WOOD_SHAVINGS.get());
		
		output.accept(MSItems.TREATED_HEAVY_PLANKS.get());
		output.accept(MSItems.TREATED_HEAVY_PLANK_STAIRS.get());
		output.accept(MSItems.TREATED_HEAVY_PLANK_SLAB.get());
		
		output.accept(MSItems.TREATED_PLANKS.get());
		output.accept(MSItems.TREATED_PLANKS_STAIRS.get());
		output.accept(MSItems.TREATED_PLANKS_SLAB.get());
		output.accept(MSItems.TREATED_BUTTON.get());
		output.accept(MSItems.TREATED_PRESSURE_PLATE.get());
		output.accept(MSItems.TREATED_FENCE.get());
		output.accept(MSItems.TREATED_FENCE_GATE.get());
		output.accept(MSItems.TREATED_DOOR.get());
		output.accept(MSItems.TREATED_TRAPDOOR.get());
		output.accept(MSItems.TREATED_HANGING_SIGN.get());
		output.accept(MSItems.TREATED_SIGN.get());
		output.accept(MSItems.TREATED_BOOKSHELF.get());
		output.accept(MSItems.TREATED_LADDER.get());
		
		output.accept(MSItems.POLISHED_TREATED_UNCARVED_WOOD.get());
		output.accept(MSItems.POLISHED_TREATED_UNCARVED_STAIRS.get());
		output.accept(MSItems.POLISHED_TREATED_UNCARVED_SLAB.get());
		
		output.accept(MSItems.TREATED_CARVED_KNOTTED_WOOD.get());
		output.accept(MSItems.TREATED_WOODEN_GRASS.get());
		
		output.accept(MSItems.LACQUERED_UNCARVED_WOOD.get());
		output.accept(MSItems.LACQUERED_UNCARVED_WOOD_STAIRS.get());
		output.accept(MSItems.LACQUERED_UNCARVED_WOOD_SLAB.get());
		output.accept(MSItems.LACQUERED_UNCARVED_WOOD_BUTTON.get());
		output.accept(MSItems.LACQUERED_UNCARVED_WOOD_PRESSURE_PLATE.get());
		output.accept(MSItems.LACQUERED_UNCARVED_WOOD_FENCE.get());
		output.accept(MSItems.LACQUERED_UNCARVED_WOOD_FENCE_GATE.get());
		
		output.accept(MSItems.LACQUERED_CHIPBOARD.get());
		output.accept(MSItems.LACQUERED_CHIPBOARD_STAIRS.get());
		output.accept(MSItems.LACQUERED_CHIPBOARD_SLAB.get());
		output.accept(MSItems.LACQUERED_CHIPBOARD_BUTTON.get());
		output.accept(MSItems.LACQUERED_CHIPBOARD_PRESSURE_PLATE.get());
		output.accept(MSItems.LACQUERED_CHIPBOARD_FENCE.get());
		output.accept(MSItems.LACQUERED_CHIPBOARD_FENCE_GATE.get());
		
		output.accept(MSItems.LACQUERED_WOOD_SHAVINGS.get());
		
		output.accept(MSItems.LACQUERED_HEAVY_PLANKS.get());
		output.accept(MSItems.LACQUERED_HEAVY_PLANK_STAIRS.get());
		output.accept(MSItems.LACQUERED_HEAVY_PLANK_SLAB.get());
		
		output.accept(MSItems.LACQUERED_PLANKS.get());
		output.accept(MSItems.LACQUERED_STAIRS.get());
		output.accept(MSItems.LACQUERED_SLAB.get());
		output.accept(MSItems.LACQUERED_BUTTON.get());
		output.accept(MSItems.LACQUERED_PRESSURE_PLATE.get());
		output.accept(MSItems.LACQUERED_FENCE.get());
		output.accept(MSItems.LACQUERED_FENCE_GATE.get());
		output.accept(MSItems.LACQUERED_DOOR.get());
		output.accept(MSItems.LACQUERED_TRAPDOOR.get());
		output.accept(MSItems.LACQUERED_HANGING_SIGN.get());
		output.accept(MSItems.LACQUERED_SIGN.get());
		
		output.accept(MSItems.POLISHED_LACQUERED_UNCARVED_WOOD.get());
		output.accept(MSItems.POLISHED_LACQUERED_UNCARVED_STAIRS.get());
		output.accept(MSItems.POLISHED_LACQUERED_UNCARVED_SLAB.get());
		
		output.accept(MSItems.LACQUERED_CARVED_KNOTTED_WOOD.get());
		output.accept(MSItems.LACQUERED_WOODEN_MUSHROOM.get());
		
		output.accept(MSItems.WOODEN_LAMP.get());
		
		output.accept(MSItems.DENSE_CLOUD.get());
		output.accept(MSItems.BRIGHT_DENSE_CLOUD.get());
		output.accept(MSItems.SUGAR_CUBE.get());
		output.accept(MSItems.NATIVE_SULFUR.get());
		
		output.accept(MSItems.GLOWING_LOG.get());
		output.accept(MSItems.GLOWING_WOOD.get());
		output.accept(MSItems.STRIPPED_GLOWING_LOG.get());
		output.accept(MSItems.STRIPPED_GLOWING_WOOD.get());
		output.accept(MSItems.GLOWING_PLANKS.get());
		output.accept(MSItems.GLOWING_STAIRS.get());
		output.accept(MSItems.GLOWING_SLAB.get());
		output.accept(MSItems.GLOWING_FENCE.get());
		output.accept(MSItems.GLOWING_FENCE_GATE.get());
		output.accept(MSItems.GLOWING_DOOR.get());
		output.accept(MSItems.GLOWING_TRAPDOOR.get());
		output.accept(MSItems.GLOWING_PRESSURE_PLATE.get());
		output.accept(MSItems.GLOWING_BUTTON.get());
		output.accept(MSItems.GLOWING_SIGN.get());
		output.accept(MSItems.GLOWING_HANGING_SIGN.get());
		output.accept(MSItems.GLOWING_BOOKSHELF.get());
		output.accept(MSItems.GLOWING_LADDER.get());
		
		output.accept(MSItems.FROST_LOG.get());
		output.accept(MSItems.FROST_WOOD.get());
		output.accept(MSItems.STRIPPED_FROST_LOG.get());
		output.accept(MSItems.STRIPPED_FROST_WOOD.get());
		output.accept(MSItems.FROST_PLANKS.get());
		output.accept(MSItems.FROST_STAIRS.get());
		output.accept(MSItems.FROST_SLAB.get());
		output.accept(MSItems.FROST_FENCE.get());
		output.accept(MSItems.FROST_FENCE_GATE.get());
		output.accept(MSItems.FROST_DOOR.get());
		output.accept(MSItems.FROST_TRAPDOOR.get());
		output.accept(MSItems.FROST_PRESSURE_PLATE.get());
		output.accept(MSItems.FROST_BUTTON.get());
		output.accept(MSItems.FROST_SIGN.get());
		output.accept(MSItems.FROST_HANGING_SIGN.get());
		output.accept(MSItems.FROST_BOOKSHELF.get());
		output.accept(MSItems.FROST_LADDER.get());
		output.accept(MSItems.FROST_LEAVES.get());
		output.accept(MSItems.FROST_LEAVES_FLOWERING.get());
		output.accept(MSItems.FROST_SAPLING.get());
		
		output.accept(MSItems.RAINBOW_LOG.get());
		output.accept(MSItems.RAINBOW_WOOD.get());
		output.accept(MSItems.STRIPPED_RAINBOW_LOG.get());
		output.accept(MSItems.STRIPPED_RAINBOW_WOOD.get());
		output.accept(MSItems.RAINBOW_PLANKS.get());
		output.accept(MSItems.RAINBOW_STAIRS.get());
		output.accept(MSItems.RAINBOW_SLAB.get());
		output.accept(MSItems.RAINBOW_FENCE.get());
		output.accept(MSItems.RAINBOW_FENCE_GATE.get());
		output.accept(MSItems.RAINBOW_DOOR.get());
		output.accept(MSItems.RAINBOW_TRAPDOOR.get());
		output.accept(MSItems.RAINBOW_PRESSURE_PLATE.get());
		output.accept(MSItems.RAINBOW_BUTTON.get());
		output.accept(MSItems.RAINBOW_SIGN.get());
		output.accept(MSItems.RAINBOW_HANGING_SIGN.get());
		output.accept(MSItems.RAINBOW_BOOKSHELF.get());
		output.accept(MSItems.RAINBOW_LADDER.get());
		output.accept(MSItems.RAINBOW_LEAVES.get());
		output.accept(MSItems.RAINBOW_SAPLING.get());
		
		output.accept(MSItems.END_LOG.get());
		output.accept(MSItems.END_WOOD.get());
		output.accept(MSItems.STRIPPED_END_LOG.get());
		output.accept(MSItems.STRIPPED_END_WOOD.get());
		output.accept(MSItems.END_PLANKS.get());
		output.accept(MSItems.END_STAIRS.get());
		output.accept(MSItems.END_SLAB.get());
		output.accept(MSItems.END_FENCE.get());
		output.accept(MSItems.END_FENCE_GATE.get());
		output.accept(MSItems.END_DOOR.get());
		output.accept(MSItems.END_TRAPDOOR.get());
		output.accept(MSItems.END_PRESSURE_PLATE.get());
		output.accept(MSItems.END_BUTTON.get());
		output.accept(MSItems.END_SIGN.get());
		output.accept(MSItems.END_HANGING_SIGN.get());
		output.accept(MSItems.END_BOOKSHELF.get());
		output.accept(MSItems.END_LADDER.get());
		output.accept(MSItems.END_LEAVES.get());
		output.accept(MSItems.END_SAPLING.get());
		
		output.accept(MSItems.DEAD_LOG.get());
		output.accept(MSItems.DEAD_WOOD.get());
		output.accept(MSItems.STRIPPED_DEAD_LOG.get());
		output.accept(MSItems.STRIPPED_DEAD_WOOD.get());
		output.accept(MSItems.DEAD_PLANKS.get());
		output.accept(MSItems.DEAD_STAIRS.get());
		output.accept(MSItems.DEAD_SLAB.get());
		output.accept(MSItems.DEAD_FENCE.get());
		output.accept(MSItems.DEAD_FENCE_GATE.get());
		output.accept(MSItems.DEAD_DOOR.get());
		output.accept(MSItems.DEAD_TRAPDOOR.get());
		output.accept(MSItems.DEAD_PRESSURE_PLATE.get());
		output.accept(MSItems.DEAD_BUTTON.get());
		output.accept(MSItems.DEAD_SIGN.get());
		output.accept(MSItems.DEAD_HANGING_SIGN.get());
		output.accept(MSItems.DEAD_BOOKSHELF.get());
		output.accept(MSItems.DEAD_LADDER.get());
		
		output.accept(MSItems.CINDERED_LOG.get());
		output.accept(MSItems.CINDERED_WOOD.get());
		output.accept(MSItems.STRIPPED_CINDERED_LOG.get());
		output.accept(MSItems.STRIPPED_CINDERED_WOOD.get());
		output.accept(MSItems.CINDERED_PLANKS.get());
		output.accept(MSItems.CINDERED_STAIRS.get());
		output.accept(MSItems.CINDERED_SLAB.get());
		output.accept(MSItems.CINDERED_FENCE.get());
		output.accept(MSItems.CINDERED_FENCE_GATE.get());
		output.accept(MSItems.CINDERED_DOOR.get());
		output.accept(MSItems.CINDERED_TRAPDOOR.get());
		output.accept(MSItems.CINDERED_PRESSURE_PLATE.get());
		output.accept(MSItems.CINDERED_BUTTON.get());
		output.accept(MSItems.CINDERED_SIGN.get());
		output.accept(MSItems.CINDERED_HANGING_SIGN.get());
		
		output.accept(MSItems.SHADEWOOD.get());
		output.accept(MSItems.SHADEWOOD_LOG.get());
		output.accept(MSItems.STRIPPED_SHADEWOOD.get());
		output.accept(MSItems.STRIPPED_SHADEWOOD_LOG.get());
		output.accept(MSItems.SHADEWOOD_PLANKS.get());
		output.accept(MSItems.SHADEWOOD_STAIRS.get());
		output.accept(MSItems.SHADEWOOD_SLAB.get());
		output.accept(MSItems.SHADEWOOD_FENCE.get());
		output.accept(MSItems.SHADEWOOD_FENCE_GATE.get());
		output.accept(MSItems.SHADEWOOD_DOOR.get());
		output.accept(MSItems.SHADEWOOD_TRAPDOOR.get());
		output.accept(MSItems.SHADEWOOD_PRESSURE_PLATE.get());
		output.accept(MSItems.SHADEWOOD_BUTTON.get());
		output.accept(MSItems.SHADEWOOD_SIGN.get());
		output.accept(MSItems.SHADEWOOD_HANGING_SIGN.get());
		output.accept(MSItems.SHADEWOOD_LEAVES.get());
		output.accept(MSItems.SHROOMY_SHADEWOOD_LEAVES.get());
		output.accept(MSItems.SHADEWOOD_SAPLING.get());
		
		output.accept(MSItems.ROTTED_SHADEWOOD.get());
		output.accept(MSItems.ROTTED_SHADEWOOD_LOG.get());
		output.accept(MSItems.STRIPPED_ROTTED_SHADEWOOD.get());
		output.accept(MSItems.STRIPPED_ROTTED_SHADEWOOD_LOG.get());
		output.accept(MSItems.SCARRED_SHADEWOOD.get());
		output.accept(MSItems.SCARRED_SHADEWOOD_LOG.get());
		output.accept(MSItems.STRIPPED_SCARRED_SHADEWOOD.get());
		output.accept(MSItems.STRIPPED_SCARRED_SHADEWOOD_LOG.get());
		
		output.accept(MSItems.FLOWERY_VINE_LOG.get());
		output.accept(MSItems.FLOWERY_VINE_WOOD.get());
		
		output.accept(MSItems.PETRIFIED_LOG.get());
		output.accept(MSItems.PETRIFIED_WOOD.get());
		
		output.accept(MSItems.VINE_LOG.get());
		output.accept(MSItems.VINE_WOOD.get());
		
		output.accept(MSItems.GLOWING_MUSHROOM.get());
		output.accept(MSItems.GLOWING_MUSHROOM_VINES.get());
		output.accept(MSItems.DESERT_BUSH.get());
		output.accept(MSItems.BLOOMING_CACTUS.get());
		output.accept(MSItems.SANDY_GRASS.get());
		output.accept(MSItems.TALL_SANDY_GRASS.get());
		output.accept(MSItems.DEAD_FOLIAGE.get());
		output.accept(MSItems.TALL_DEAD_BUSH.get());
		output.accept(MSItems.PETRIFIED_GRASS.get());
		output.accept(MSItems.PETRIFIED_POPPY.get());
		output.accept(MSItems.WOODEN_CACTUS.get());
		output.accept(MSItems.IGNEOUS_SPIKE.get());
		output.accept(MSItems.SINGED_GRASS.get());
		output.accept(MSItems.SINGED_FOLIAGE.get());
		output.accept(MSItems.SULFUR_BUBBLE.get());
		output.accept(MSItems.STRAWBERRY.get());
		output.accept(MSItems.TALL_END_GRASS.get());
		output.accept(MSItems.GLOWFLOWER.get());
		output.accept(MSItems.GLOWY_GOOP.get());
		output.accept(MSItems.COAGULATED_BLOOD.get());
		output.accept(MSItems.PIPE.get());
		output.accept(MSItems.PIPE_INTERSECTION.get());
		output.accept(MSItems.PARCEL_PYXIS.get());
		output.accept(MSItems.PYXIS_LID.get());
		output.accept(MSItems.NAKAGATOR_STATUE.get());
		
		output.accept(MSItems.COARSE_STONE_STAIRS.get());
		output.accept(MSItems.COARSE_STONE_BRICK_STAIRS.get());
		output.accept(MSItems.SHADE_STAIRS.get());
		output.accept(MSItems.SHADE_BRICK_STAIRS.get());
		output.accept(MSItems.FROST_TILE_STAIRS.get());
		output.accept(MSItems.FROST_BRICK_STAIRS.get());
		output.accept(MSItems.MYCELIUM_STAIRS.get());
		output.accept(MSItems.MYCELIUM_BRICK_STAIRS.get());
		output.accept(MSItems.CHALK_STAIRS.get());
		output.accept(MSItems.CHALK_BRICK_STAIRS.get());
		output.accept(MSItems.FLOWERY_MOSSY_STONE_BRICK_STAIRS.get());
		output.accept(MSItems.PINK_STONE_STAIRS.get());
		output.accept(MSItems.PINK_STONE_BRICK_STAIRS.get());
		output.accept(MSItems.BROWN_STONE_STAIRS.get());
		output.accept(MSItems.BROWN_STONE_BRICK_STAIRS.get());
		
		output.accept(MSItems.COARSE_STONE_SLAB.get());
		output.accept(MSItems.COARSE_STONE_BRICK_SLAB.get());
		output.accept(MSItems.CHALK_SLAB.get());
		output.accept(MSItems.CHALK_BRICK_SLAB.get());
		output.accept(MSItems.PINK_STONE_SLAB.get());
		output.accept(MSItems.PINK_STONE_BRICK_SLAB.get());
		output.accept(MSItems.BROWN_STONE_SLAB.get());
		output.accept(MSItems.BROWN_STONE_BRICK_SLAB.get());
		output.accept(MSItems.MYCELIUM_SLAB.get());
		output.accept(MSItems.MYCELIUM_BRICK_SLAB.get());
		output.accept(MSItems.FLOWERY_MOSSY_STONE_BRICK_SLAB.get());
		output.accept(MSItems.FROST_TILE_SLAB.get());
		output.accept(MSItems.FROST_BRICK_SLAB.get());
		output.accept(MSItems.SHADE_SLAB.get());
		output.accept(MSItems.SHADE_BRICK_SLAB.get());
		
		output.accept(MSItems.TRAJECTORY_BLOCK.get());
		output.accept(MSItems.STAT_STORER.get());
		output.accept(MSItems.REMOTE_OBSERVER.get());
		output.accept(MSItems.SUMMONER.get());
		output.accept(MSItems.AREA_EFFECT_BLOCK.get());
		output.accept(MSItems.PLATFORM_GENERATOR.get());
		output.accept(MSItems.PLATFORM_RECEPTACLE.get());
		output.accept(MSItems.REMOTE_COMPARATOR.get());
		output.accept(MSItems.STRUCTURE_CORE.get());
		output.accept(MSItems.PUSHABLE_BLOCK.get());
		output.accept(MSItems.AND_GATE_BLOCK.get());
		output.accept(MSItems.OR_GATE_BLOCK.get());
		output.accept(MSItems.XOR_GATE_BLOCK.get());
		output.accept(MSItems.NAND_GATE_BLOCK.get());
		output.accept(MSItems.NOR_GATE_BLOCK.get());
		output.accept(MSItems.XNOR_GATE_BLOCK.get());
		
		output.accept(MSItems.LARGE_CAKE.get());
		output.accept(MSItems.PINK_FROSTED_TOP_LARGE_CAKE.get());
	}
	
	private static void buildWeaponsTab(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output output)
	{
		output.accept(MSItems.CLAW_HAMMER.get());
		output.accept(MSItems.SLEDGE_HAMMER.get());
		output.accept(MSItems.MAILBOX.get());
		output.accept(MSItems.BLACKSMITH_HAMMER.get());
		output.accept(MSItems.POGO_HAMMER.get());
		output.accept(MSItems.WRINKLEFUCKER.get());
		output.accept(MSItems.TELESCOPIC_SASSACRUSHER.get());
		output.accept(MSItems.DEMOCRATIC_DEMOLITIONER.get());
		output.accept(MSItems.BOOMBOX_BEATER.get());
		output.accept(MSItems.REGI_HAMMER.get());
		output.accept(MSItems.FEAR_NO_ANVIL.get());
		output.accept(MSItems.MELT_MASHER.get());
		output.accept(MSItems.ESTROGEN_EMPOWERED_EVERYTHING_ERADICATOR.get());
		output.accept(MSItems.EEEEEEEEEEEE.get());
		output.accept(MSItems.ZILLYHOO_HAMMER.get());
		output.accept(MSItems.POPAMATIC_VRILLYHOO.get());
		output.accept(MSItems.SCARLET_ZILLYHOO.get());
		output.accept(MSItems.MWRTHWL.get());
		
		output.accept(MSItems.SORD.get());
		output.accept(MSItems.PAPER_SWORD.get());
		output.accept(MSItems.SWONGE.get());
		output.accept(MSItems.PUMORD.get());
		output.accept(MSItems.CACTACEAE_CUTLASS.get());
		output.accept(MSItems.STEAK_SWORD.get());
		output.accept(MSItems.BEEF_SWORD.get());
		output.accept(MSItems.IRRADIATED_STEAK_SWORD.get());
		output.accept(MSItems.MACUAHUITL.get());
		output.accept(MSItems.FROSTY_MACUAHUITL.get());
		output.accept(MSItems.KATANA.get());
		output.accept(MSItems.UNBREAKABLE_KATANA.get());
		output.accept(MSItems.ANGEL_APOCALYPSE.get());
		output.accept(MSItems.FIRE_POKER.get());
		output.accept(MSItems.TOO_HOT_TO_HANDLE.get());
		output.accept(MSItems.CALEDSCRATCH.get());
		output.accept(MSItems.CALEDFWLCH.get());
		output.accept(MSItems.ROYAL_DERINGER.get());
		output.accept(MSItems.CLAYMORE.get());
		output.accept(MSItems.CUTLASS_OF_ZILLYWAIR.get());
		output.accept(MSItems.REGISWORD.get());
		output.accept(MSItems.CRUEL_FATE_CRUCIBLE.get());
		output.accept(MSItems.SCARLET_RIBBITAR.get());
		output.accept(MSItems.DOGG_MACHETE.get());
		output.accept(MSItems.COBALT_SABRE.get());
		output.accept(MSItems.QUANTUM_SABRE.get());
		output.accept(MSItems.SHATTER_BEACON.get());
		output.accept(MSItems.SHATTER_BACON.get());
		output.accept(MSItems.SUBTRACTSHUMIDIRE_ZOMORRODNEGATIVE.get());
		output.accept(MSItems.MUSIC_SWORD.get());
		output.accept(MSItems.PILLOW_TALK.get());
		output.accept(MSItems.KRAKENS_EYE.get());
		output.accept(MSItems.CINNAMON_SWORD.get());
		output.accept(MSItems.UNION_BUSTER.get());
		output.accept(MSItems.CHAINSAW_KATANA.get());
		output.accept(MSItems.THORN_IN_YOUR_SIDE.get());
		output.accept(MSItems.ROSE_PROTOCOL.get());
		
		output.accept(MSItems.DAGGER.get());
		output.accept(MSItems.DIAMOND_DAGGER.get());
		output.accept(MSItems.PIGLINS_PRIDE.get());
		output.accept(MSItems.BASILISK_BREATH_DRAGONSLAYER.get());
		output.accept(MSItems.HALLOWED_SKEWER.get());
		output.accept(MSItems.GENESIS_GODSTABBER.get());
		output.accept(MSItems.NIFE.get());
		output.accept(MSItems.LIGHT_OF_MY_KNIFE.get());
		output.accept(MSItems.THOUSAND_DEGREE_KNIFE.get());
		output.accept(MSItems.STARSHARD_TRI_BLADE.get());
		output.accept(MSItems.TOOTHRIPPER.get());
		output.accept(MSItems.SHADOWRAZOR.get());
		output.accept(MSItems.PRINCESS_PERIL.get());
		
		output.accept(MSItems.HOUSE_KEY.get());
		output.accept(MSItems.KEYBLADE.get());
		output.accept(MSItems.CANDY_KEY.get());
		output.accept(MSItems.LOCKSOFTENER.get());
		output.accept(MSItems.BISEKEYAL.get());
		output.accept(MSItems.LATCHMELTER.get());
		output.accept(MSItems.KEY_TO_THE_MACHINE.get());
		output.accept(MSItems.KEY_TO_THE_CITY.get());
		output.accept(MSItems.INNER_HEART.get());
		output.accept(MSItems.CRIMSON_LEAP.get());
		output.accept(MSItems.LOCH_PICK.get());
		output.accept(MSItems.DRAGON_KEY.get());
		output.accept(MSItems.TRUE_BLUE.get());
		output.accept(MSItems.BLUE_BEAMS.get());
		output.accept(MSItems.INKSPLOCKER_UNLOCKER.get());
		output.accept(MSItems.INKSQUIDDER_DEPTHKEY.get());
		output.accept(MSItems.REGIKEY.get());
		output.accept(MSItems.CLOCKKEEPER.get());
		output.accept(MSItems.HOME_BY_MIDNIGHT.get());
		output.accept(MSItems.NO_TIME_FOR_FLIES.get());
		output.accept(MSItems.NATURES_HEART.get());
		output.accept(MSItems.YALDABAOTHS_KEYTON.get());
		output.accept(MSItems.KEYTAR.get());
		output.accept(MSItems.ALLWEDDOL.get());
		
		output.accept(MSItems.CONDUCTORS_BATON.get());
		output.accept(MSItems.SHARP_NOTE.get());
		output.accept(MSItems.URANIUM_BATON.get());
		output.accept(MSItems.WIND_WAKER.get());
		output.accept(MSItems.CELESTIAL_FULCRUM.get());
		output.accept(MSItems.HYMN_FOR_HORRORTERRORS.get());
		
		output.accept(MSItems.BATLEACKS.get());
		output.accept(MSItems.COPSE_CRUSHER.get());
		output.accept(MSItems.QUENCH_CRUSHER.get());
		output.accept(MSItems.MELONSBANE.get());
		output.accept(MSItems.CROP_CHOP.get());
		output.accept(MSItems.THE_LAST_STRAW.get());
		output.accept(MSItems.BATTLEAXE.get());
		output.accept(MSItems.CANDY_BATTLEAXE.get());
		output.accept(MSItems.CHOCO_LOCO_WOODSPLITTER.get());
		output.accept(MSItems.STEEL_EDGE_CANDYCUTTER.get());
		output.accept(MSItems.BLACKSMITH_BANE.get());
		output.accept(MSItems.REGIAXE.get());
		output.accept(MSItems.GOTHY_AXE.get());
		output.accept(MSItems.SURPRISE_AXE.get());
		output.accept(MSItems.SHOCK_AXE.get());
		output.accept(MSItems.SCRAXE.get());
		output.accept(MSItems.LORENTZ_DISTRANSFORMATIONER.get());
		output.accept(MSItems.PISTON_POWERED_POGO_AXEHAMMER.get());
		output.accept(MSItems.RUBY_CROAK.get());
		output.accept(MSItems.HEPHAESTUS_LUMBERJACK.get());
		output.accept(MSItems.FISSION_FOCUSED_FAULT_FELLER.get());
		output.accept(MSItems.BISECTOR.get());
		output.accept(MSItems.FINE_CHINA_AXE.get());
		
		output.accept(MSItems.FLUORITE_OCTET.get());
		
		output.accept(MSItems.MAKESHIFT_CLAWS_DRAWN.get());
		output.accept(MSItems.CAT_CLAWS_DRAWN.get());
		output.accept(MSItems.COFFEE_CLAWS_DRAWN.get());
		output.accept(MSItems.POGO_CLAWS.get());
		output.accept(MSItems.ATOMIKITTY_KATAR_DRAWN.get());
		output.accept(MSItems.SKELETONIZER_DRAWN.get());
		output.accept(MSItems.SKELETON_DISPLACER_DRAWN.get());
		output.accept(MSItems.TEARS_OF_THE_ENDERLICH_DRAWN.get());
		output.accept(MSItems.LION_LACERATORS_DRAWN.get());
		output.accept(MSItems.ACTION_CLAWS_DRAWN.get());
		
		output.accept(MSItems.LIPSTICK_CHAINSAW.get());
		output.accept(MSItems.CAKESAW.get());
		output.accept(MSItems.MAGENTA_MAULER.get());
		output.accept(MSItems.THISTLEBLOWER.get());
		output.accept(MSItems.HAND_CRANKED_VAMPIRE_ERASER.get());
		output.accept(MSItems.EMERALD_IMMOLATOR.get());
		output.accept(MSItems.OBSIDIATOR.get());
		output.accept(MSItems.DEVILS_DELIGHT.get());
		output.accept(MSItems.DEMONBANE_RAGRIPPER.get());
		output.accept(MSItems.FROSTTOOTH.get());
		
		output.accept(MSItems.WOODEN_LANCE.get());
		output.accept(MSItems.LANEC.get());
		output.accept(MSItems.JOUSTING_LANCE.get());
		output.accept(MSItems.POGO_LANCE.get());
		output.accept(MSItems.LANCELOTS_LOLLY.get());
		output.accept(MSItems.DRAGON_LANCE.get());
		output.accept(MSItems.SKY_PIERCER.get());
		output.accept(MSItems.FIDUSPAWN_LANCE.get());
		output.accept(MSItems.REGILANCE.get());
		output.accept(MSItems.CIGARETTE_LANCE.get());
		
		output.accept(MSItems.LUCERNE_HAMMER.get());
		output.accept(MSItems.LUCERNE_HAMMER_OF_UNDYING.get());
		
		output.accept(MSItems.OBSIDIAN_AXE_KNIFE.get());
		
		output.accept(MSItems.FAN.get());
		output.accept(MSItems.CANDY_FAN.get());
		output.accept(MSItems.SPINES_OF_FLUTHLU.get());
		output.accept(MSItems.RAZOR_FAN.get());
		output.accept(MSItems.MOTOR_FAN.get());
		output.accept(MSItems.ATOMIC_VAPORIZER.get());
		output.accept(MSItems.SHAVING_FAN.get());
		output.accept(MSItems.FIRESTARTER.get());
		output.accept(MSItems.STAR_RAY.get());
		output.accept(MSItems.TYPHONIC_TRIVIALIZER.get());
		
		output.accept(MSItems.SICKLE.get());
		output.accept(MSItems.BISICKLE.get());
		output.accept(MSItems.OW_THE_EDGE.get());
		output.accept(MSItems.HEMEOREAPER.get());
		output.accept(MSItems.THORNY_SUBJECT.get());
		output.accept(MSItems.SNOW_WHITE_DREAM.get());
		output.accept(MSItems.HOMES_SMELL_YA_LATER.get());
		output.accept(MSItems.FUDGESICKLE.get());
		output.accept(MSItems.REGISICKLE.get());
		output.accept(MSItems.HERETICUS_AURURM.get());
		output.accept(MSItems.CLAW_SICKLE.get());
		output.accept(MSItems.CLAW_OF_NRUBYIGLITH.get());
		output.accept(MSItems.CANDY_SICKLE.get());
		
		output.accept(MSItems.SCYTHE.get());
		output.accept(MSItems.MARASCHINO_CHERRY_SCYTHE.get());
		output.accept(MSItems.KISSY_CUTIE_HEART_SPLITTER.get());
		output.accept(MSItems.MUTANT_CUTIE_CELL_CUTTER.get());
		output.accept(MSItems.PROSPECTING_PICKSCYTHE.get());
		output.accept(MSItems.EIGHTBALL_SCYTHE.get());
		output.accept(MSItems.TIME_FLAYER.get());
		output.accept(MSItems.DESTINY_DECIMATOR.get());
		output.accept(MSItems.SUNRAY_HARVESTER.get());
		output.accept(MSItems.GREEN_SUN_RAYREAPER.get());
		output.accept(MSItems.SKAITHE.get());
		
		output.accept(MSItems.HELLBRINGERS_HOE_ACTIVE.get());
		
		output.accept(MSItems.DEUCE_CLUB.get());
		output.accept(MSItems.STALE_BAGUETTE.get());
		output.accept(MSItems.GLUB_CLUB.get());
		output.accept(MSItems.NIGHT_CLUB.get());
		output.accept(MSItems.NIGHTSTICK.get());
		output.accept(MSItems.RED_EYES.get());
		output.accept(MSItems.PRISMARINE_BASHER.get());
		output.accept(MSItems.CLUB_ZERO.get());
		output.accept(MSItems.POGO_CLUB.get());
		output.accept(MSItems.BARBER_BASHER.get());
		output.accept(MSItems.METAL_BAT.get());
		output.accept(MSItems.CRICKET_BAT.get());
		output.accept(MSItems.CLOWN_CLUB.get());
		output.accept(MSItems.DOCTOR_DETERRENT.get());
		output.accept(MSItems.MACE.get());
		output.accept(MSItems.M_ACE.get());
		output.accept(MSItems.M_ACE_OF_CLUBS.get());
		output.accept(MSItems.DESOLATOR_MACE.get());
		output.accept(MSItems.BLAZING_GLORY.get());
		output.accept(MSItems.SPIKED_CLUB.get());
		output.accept(MSItems.RUBIKS_MACE.get());
		output.accept(MSItems.HOME_GROWN_MACE.get());
		output.accept(MSItems.CARNIE_CLUB.get());
		output.accept(MSItems.TOFFEE_CLUB.get());
		
		output.accept(MSItems.HORSE_HITCHER.get());
		output.accept(MSItems.CLUB_OF_FELONY.get());
		output.accept(MSItems.CUESTICK.get());
		output.accept(MSItems.TV_ANTENNA.get());
		
		output.accept(MSItems.BO_STAFF.get());
		output.accept(MSItems.BAMBOO_BEATSTICK.get());
		output.accept(MSItems.TELESCOPIC_BEATDOWN_BRUISER.get());
		output.accept(MSItems.ION_DESTABILIZER.get());
		
		output.accept(MSItems.WIZARD_STAFF.get());
		output.accept(MSItems.BARBERS_MAGIC_TOUCH.get());
		output.accept(MSItems.WATER_STAFF.get());
		output.accept(MSItems.FIRE_STAFF.get());
		output.accept(MSItems.WHITE_KINGS_SCEPTER.get());
		output.accept(MSItems.BLACK_KINGS_SCEPTER.get());
		output.accept(MSItems.PRIME_STAFF.get());
		
		output.accept(MSItems.CANE.get());
		output.accept(MSItems.VAUDEVILLE_HOOK.get());
		output.accept(MSItems.BEAR_POKING_STICK.get());
		output.accept(MSItems.CROWBAR.get());
		output.accept(MSItems.UMBRELLA.get());
		output.accept(MSItems.BARBERS_BEST_FRIEND.get());
		output.accept(MSItems.UPPER_CRUST_CRUST_CANE.get());
		output.accept(MSItems.IRON_CANE.get());
		output.accept(MSItems.KISSY_CUTIE_HEART_HITTER.get());
		output.accept(MSItems.MUTANT_CUTIE_CELL_PUTTER.get());
		output.accept(MSItems.ZEPHYR_CANE.get());
		output.accept(MSItems.SPEAR_CANE.get());
		output.accept(MSItems.PARADISES_PORTABELLO.get());
		output.accept(MSItems.REGI_CANE.get());
		output.accept(MSItems.POGO_CANE.get());
		output.accept(MSItems.CANDY_CANE.get());
		output.accept(MSItems.SHARP_CANDY_CANE.get());
		output.accept(MSItems.PRIM_AND_PROPER_WALKING_POLE.get());
		output.accept(MSItems.DRAGON_CANE.get());
		output.accept(MSItems.CHANCEWYRMS_EXTRA_FORTUNATE_STABBING_IMPLEMENT.get());
		output.accept(MSItems.LESS_PROPER_WALKING_STICK.get());
		output.accept(MSItems.ROCKEFELLERS_WALKING_BLADECANE.get());
		
		output.accept(MSItems.WOODEN_SPOON.get());
		output.accept(MSItems.SILVER_SPOON.get());
		output.accept(MSItems.MELONBALLER.get());
		output.accept(MSItems.SIGHTSEEKER.get());
		output.accept(MSItems.TERRAIN_FLATENATOR.get());
		output.accept(MSItems.NOSFERATU_SPOON.get());
		output.accept(MSItems.THRONGLER.get());
		output.accept(MSItems.WET_MEAT_SHIT_THRONGLER.get());
		output.accept(MSItems.CROCKER_SPOON.get());
		output.accept(MSItems.EDISONS_FURY.get());
		
		output.accept(MSItems.SKAIA_FORK.get());
		output.accept(MSItems.SKAIAN_CROCKER_ROCKER.get());
		output.accept(MSItems.FORK.get());
		output.accept(MSItems.CANDY_FORK.get());
		output.accept(MSItems.TUNING_FORK.get());
		output.accept(MSItems.ELECTRIC_FORK.get());
		output.accept(MSItems.EATING_FORK_GEM.get());
		output.accept(MSItems.DEVIL_FORK.get());
		
		output.accept(MSItems.SPORK.get());
		output.accept(MSItems.GOLDEN_SPORK.get());
		
		output.accept(MSItems.MEATFORK.get());
		output.accept(MSItems.BIDENT.get());
		output.accept(MSItems.DOUBLE_ENDED_TRIDENT.get());
		
		output.accept(MSItems.POINTY_STICK.get());
		output.accept(MSItems.KNITTING_NEEDLE.get());
		
		output.accept(MSItems.WAND.get());
		output.accept(MSItems.NEEDLE_WAND.get());
		output.accept(MSItems.ARTIFUCKER.get());
		output.accept(MSItems.POINTER_WAND.get());
		output.accept(MSItems.POOL_CUE_WAND.get());
		output.accept(MSItems.THORN_OF_OGLOGOTH.get());
		output.accept(MSItems.THISTLE_OF_ZILLYWICH.get());
		output.accept(MSItems.QUILL_OF_ECHIDNA.get());
		
		output.accept(MSItems.SBAHJARANG.get());
		output.accept(MSItems.SHURIKEN.get());
		output.accept(MSItems.CLUBS_SUITARANG.get());
		output.accept(MSItems.DIAMONDS_SUITARANG.get());
		output.accept(MSItems.HEARTS_SUITARANG.get());
		output.accept(MSItems.SPADES_SUITARANG.get());
		
		output.accept(MSItems.CHAKRAM.get());
		output.accept(MSItems.UMBRAL_INFILTRATOR.get());
		
		output.accept(MSItems.SORCERERS_PINBALL.get());
		
		output.accept(MSItems.EMERALD_SWORD.get());
		output.accept(MSItems.EMERALD_AXE.get());
		output.accept(MSItems.EMERALD_PICKAXE.get());
		output.accept(MSItems.EMERALD_SHOVEL.get());
		output.accept(MSItems.EMERALD_HOE.get());
		output.accept(MSItems.MINE_AND_GRIST.get());
		
		output.accept(MSItems.PRISMARINE_HELMET.get());
		output.accept(MSItems.PRISMARINE_CHESTPLATE.get());
		output.accept(MSItems.PRISMARINE_LEGGINGS.get());
		output.accept(MSItems.PRISMARINE_BOOTS.get());
		output.accept(MSItems.IRON_LASS_GLASSES.get());
		output.accept(MSItems.IRON_LASS_CHESTPLATE.get());
		output.accept(MSItems.IRON_LASS_SKIRT.get());
		output.accept(MSItems.IRON_LASS_SHOES.get());
		
		output.accept(MSItems.PROSPIT_CIRCLET.get());
		output.accept(MSItems.PROSPIT_SHIRT.get());
		output.accept(MSItems.PROSPIT_PANTS.get());
		output.accept(MSItems.PROSPIT_SHOES.get());
		output.accept(MSItems.DERSE_CIRCLET.get());
		output.accept(MSItems.DERSE_SHIRT.get());
		output.accept(MSItems.DERSE_PANTS.get());
		output.accept(MSItems.DERSE_SHOES.get());
		
		output.accept(MSItems.AMPHIBEANIE.get());
		output.accept(MSItems.NOSTRILDAMUS.get());
		output.accept(MSItems.PONYTAIL.get());
	}
	
	@SubscribeEvent
	public static void buildSpawnEggs(BuildCreativeModeTabContentsEvent event) {
		if (event.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
			event.accept(MSItems.IMP_SPAWN_EGG);
			event.accept(MSItems.OGRE_SPAWN_EGG);
			event.accept(MSItems.BASILISK_SPAWN_EGG);
			event.accept(MSItems.LICH_SPAWN_EGG);
			
			event.accept(MSItems.SALAMANDER_SPAWN_EGG);
			event.accept(MSItems.TURTLE_SPAWN_EGG);
			event.accept(MSItems.NAKAGATOR_SPAWN_EGG);
			event.accept(MSItems.IGUANA_SPAWN_EGG);
			
			event.accept(MSItems.DERSITE_PAWN_SPAWN_EGG);
			event.accept(MSItems.DERSITE_BISHOP_SPAWN_EGG);
			event.accept(MSItems.DERSITE_ROOK_SPAWN_EGG);
			event.accept(MSItems.PROSPITIAN_PAWN_SPAWN_EGG);
			event.accept(MSItems.PROSPITIAN_BISHOP_SPAWN_EGG);
			event.accept(MSItems.PROSPITIAN_ROOK_SPAWN_EGG);
		}
	}
}
