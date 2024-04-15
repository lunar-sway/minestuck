package com.mraof.minestuck.computer.editmode;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.alchemy.AlchemyHelper;
import com.mraof.minestuck.api.alchemy.GristSet;
import com.mraof.minestuck.api.alchemy.GristTypes;
import com.mraof.minestuck.api.alchemy.ImmutableGristSet;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.item.block.MiniCruxtruderItem;
import com.mraof.minestuck.skaianet.SburbHandler;
import com.mraof.minestuck.skaianet.SburbPlayerData;
import com.mraof.minestuck.skaianet.SkaianetData;
import com.mraof.minestuck.util.ColorHandler;
import com.mraof.minestuck.world.storage.MSExtraData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.event.TickEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;

/**
 * This class will be used to keep track of all deployable
 * items accessible by the server.
 * @author kirderf1
 */
@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus= Mod.EventBusSubscriber.Bus.FORGE)
public final class DeployList
{
	public static final IAvailabilityCondition HAS_NOT_ENTERED = playerData -> !playerData.hasEntered();
	
	private static final ArrayList<DeployEntry> allList = new ArrayList<>();
	private static final ArrayList<DeployEntry> deployList = new ArrayList<>();
	private static final ArrayList<DeployEntry> atheneumList = new ArrayList<>();
	
	public enum EntryLists
	{
		ALL {
			@Override
			public ArrayList<DeployEntry> getList() { return allList; }
		},
		DEPLOY {
			@Override
			public ArrayList<DeployEntry> getList() { return deployList; }
		},
		ATHENEUM {
			@Override
			public ArrayList<DeployEntry> getList() { return atheneumList; }
		};
		
		public ArrayList<DeployEntry> getList() { return new ArrayList<>(); }
	}
	
	public static void registerItems()
	{
		//Deployables
		registerItem("cruxtruder", new ItemStack(MSBlocks.CRUXTRUDER), GristSet.EMPTY, GristTypes.BUILD.get().amount(100), 0, EntryLists.DEPLOY);
		registerItem("totem_lathe", new ItemStack(MSBlocks.TOTEM_LATHE), GristSet.EMPTY, GristTypes.BUILD.get().amount(100), 0, EntryLists.DEPLOY);
		registerItem("artifact_card", GristSet.EMPTY, null, 0, HAS_NOT_ENTERED,
				(playerData, level) -> AlchemyHelper.createPunchedCard(SburbHandler.getEntryItem(level, playerData)), EntryLists.DEPLOY);
		registerItem("alchemiter", new ItemStack(MSBlocks.ALCHEMITER), GristSet.EMPTY, GristTypes.BUILD.get().amount(100), 0, EntryLists.DEPLOY);
		registerItem("punch_designix", 0, null, item(MSBlocks.PUNCH_DESIGNIX),
				(isPrimary, playerData) -> playerData.getBaseGrist().amount(4), EntryLists.DEPLOY);
		registerItem("portable_cruxtruder", GristTypes.BUILD.get().amount(200), 1, config(MinestuckConfig.SERVER.portableMachines),
				(playerData, level) -> MiniCruxtruderItem.getCruxtruderWithColor(ColorHandler.getColorForPlayer(playerData.playerId(), level)), EntryLists.DEPLOY);
		registerItem("portable_punch_designix", GristTypes.BUILD.get().amount(200), 1, config(MinestuckConfig.SERVER.portableMachines), item(MSBlocks.MINI_PUNCH_DESIGNIX.get()), EntryLists.DEPLOY);
		registerItem("portable_totem_lathe", GristTypes.BUILD.get().amount(200), 1, config(MinestuckConfig.SERVER.portableMachines), item(MSBlocks.MINI_TOTEM_LATHE.get()), EntryLists.DEPLOY);
		registerItem("portable_alchemiter", GristTypes.BUILD.get().amount(300), 1, config(MinestuckConfig.SERVER.portableMachines), item(MSBlocks.MINI_ALCHEMITER.get()), EntryLists.DEPLOY);
		registerItem("holopad", new ItemStack(MSBlocks.HOLOPAD.get()), GristTypes.BUILD.get().amount(4000), 2, EntryLists.DEPLOY);
		registerItem("intellibeam_laserstation", new ItemStack(MSBlocks.INTELLIBEAM_LASERSTATION.get()), GristTypes.BUILD.get().amount(100000), 2, EntryLists.DEPLOY);
		registerItem("card_punched_card", GristTypes.BUILD.get().amount(25), null, 0, config(MinestuckConfig.SERVER.deployCard),
				(playerData, world) -> AlchemyHelper.createPunchedCard(new ItemStack(MSItems.CAPTCHA_CARD.get())), EntryLists.DEPLOY);
		
		//Atheneum
		registerItem("cobblestone", new ItemStack(Blocks.COBBLESTONE), GristTypes.BUILD.get().amount(1), 0, EntryLists.ATHENEUM);
		registerItem("mossy_cobblestone", new ItemStack(Blocks.MOSSY_COBBLESTONE), GristTypes.BUILD.get().amount(1), 1, EntryLists.ATHENEUM);
		registerItem("smooth_stone", new ItemStack(Blocks.SMOOTH_STONE), GristTypes.BUILD.get().amount(1), 2, EntryLists.ATHENEUM);
		registerItem("stone_bricks", new ItemStack(Blocks.STONE_BRICKS), GristTypes.BUILD.get().amount(1), 0, EntryLists.ATHENEUM);
		registerItem("chiseled_stone_bricks", new ItemStack(Blocks.CHISELED_STONE_BRICKS), GristTypes.BUILD.get().amount(1), 2, EntryLists.ATHENEUM);
		registerItem("nether_bricks", new ItemStack(Blocks.NETHER_BRICKS), GristSet.of(GristTypes.BUILD.get().amount(1), GristTypes.TAR.get().amount(1)), 3, EntryLists.ATHENEUM);
		registerItem("chiseled_nether_bricks", new ItemStack(Blocks.CHISELED_NETHER_BRICKS), GristSet.of(GristTypes.BUILD.get().amount(1), GristTypes.TAR.get().amount(1)), 4, EntryLists.ATHENEUM);
		registerItem("oak_planks", new ItemStack(Blocks.OAK_PLANKS), GristTypes.BUILD.get().amount(1), 0, EntryLists.ATHENEUM);
		registerItem("birch_planks", new ItemStack(Blocks.BIRCH_PLANKS), GristTypes.BUILD.get().amount(1), 0, EntryLists.ATHENEUM);
		registerItem("spruce_planks", new ItemStack(Blocks.SPRUCE_PLANKS), GristTypes.BUILD.get().amount(1), 0, EntryLists.ATHENEUM);
		registerItem("dark_oak_planks", new ItemStack(Blocks.DARK_OAK_PLANKS), GristTypes.BUILD.get().amount(1), 0, EntryLists.ATHENEUM);
		registerItem("acacia_planks", new ItemStack(Blocks.ACACIA_PLANKS), GristTypes.BUILD.get().amount(1), 0, EntryLists.ATHENEUM);
		registerItem("jungle_planks", new ItemStack(Blocks.JUNGLE_PLANKS), GristTypes.BUILD.get().amount(1), 0, EntryLists.ATHENEUM);
		registerItem("torch", new ItemStack(Blocks.TORCH), GristTypes.BUILD.get().amount(1), 0, EntryLists.ATHENEUM);
		registerItem("ladder", new ItemStack(Blocks.LADDER), GristTypes.BUILD.get().amount(16), 0, EntryLists.ATHENEUM);
		registerItem("oak_door", new ItemStack(Blocks.OAK_DOOR), GristTypes.BUILD.get().amount(2), 0, EntryLists.ATHENEUM);
		registerItem("birch_door", new ItemStack(Blocks.BIRCH_DOOR), GristTypes.BUILD.get().amount(2), 0, EntryLists.ATHENEUM);
		registerItem("spruce_door", new ItemStack(Blocks.SPRUCE_DOOR), GristTypes.BUILD.get().amount(2), 0, EntryLists.ATHENEUM);
		registerItem("dark_oak_door", new ItemStack(Blocks.DARK_OAK_DOOR), GristTypes.BUILD.get().amount(2), 0, EntryLists.ATHENEUM);
		registerItem("acacia_door", new ItemStack(Blocks.ACACIA_DOOR), GristTypes.BUILD.get().amount(2), 0, EntryLists.ATHENEUM);
		registerItem("jungle_door", new ItemStack(Blocks.JUNGLE_DOOR), GristTypes.BUILD.get().amount(2), 0, EntryLists.ATHENEUM);
		registerItem("oak_trapdoor", new ItemStack(Blocks.OAK_TRAPDOOR), GristTypes.BUILD.get().amount(2), 0, EntryLists.ATHENEUM);
		registerItem("birch_trapdoor", new ItemStack(Blocks.BIRCH_TRAPDOOR), GristTypes.BUILD.get().amount(2), 0, EntryLists.ATHENEUM);
		registerItem("spruce_trapdoor", new ItemStack(Blocks.SPRUCE_TRAPDOOR), GristTypes.BUILD.get().amount(2), 0, EntryLists.ATHENEUM);
		registerItem("dark_oak_trapdoor", new ItemStack(Blocks.DARK_OAK_TRAPDOOR), GristTypes.BUILD.get().amount(2), 0, EntryLists.ATHENEUM);
		registerItem("acacia_trapdoor", new ItemStack(Blocks.ACACIA_TRAPDOOR), GristTypes.BUILD.get().amount(2), 0, EntryLists.ATHENEUM);
		registerItem("jungle_trapdoor", new ItemStack(Blocks.JUNGLE_TRAPDOOR), GristTypes.BUILD.get().amount(2), 0, EntryLists.ATHENEUM);
		registerItem("glass", new ItemStack(Blocks.GLASS), GristTypes.BUILD.get().amount(1), 0, EntryLists.ATHENEUM);
		registerItem("white_stained_glass", new ItemStack(Blocks.WHITE_STAINED_GLASS), GristTypes.BUILD.get().amount(2), 2, EntryLists.ATHENEUM); //Stained glass and stained glass panes' atheneum costs are given an amount of build grist equal to the number of grist types their normal costs have. To offset farming potential, it has a connection-tier of two.
		registerItem("orange_stained_glass", new ItemStack(Blocks.ORANGE_STAINED_GLASS), GristTypes.BUILD.get().amount(3), 2, EntryLists.ATHENEUM);
		registerItem("magenta_stained_glass", new ItemStack(Blocks.MAGENTA_STAINED_GLASS), GristTypes.BUILD.get().amount(4), 2, EntryLists.ATHENEUM);
		registerItem("light_blue_stained_glass", new ItemStack(Blocks.LIGHT_BLUE_STAINED_GLASS), GristTypes.BUILD.get().amount(3), 2, EntryLists.ATHENEUM);
		registerItem("yellow_stained_glass", new ItemStack(Blocks.YELLOW_STAINED_GLASS), GristTypes.BUILD.get().amount(2), 2, EntryLists.ATHENEUM);
		registerItem("lime_stained_glass", new ItemStack(Blocks.LIME_STAINED_GLASS), GristTypes.BUILD.get().amount(3), 2, EntryLists.ATHENEUM);
		registerItem("pink_stained_glass", new ItemStack(Blocks.PINK_STAINED_GLASS), GristTypes.BUILD.get().amount(3), 2, EntryLists.ATHENEUM);
		registerItem("gray_stained_glass", new ItemStack(Blocks.GRAY_STAINED_GLASS), GristTypes.BUILD.get().amount(3), 2, EntryLists.ATHENEUM);
		registerItem("light_gray_stained_glass", new ItemStack(Blocks.LIGHT_GRAY_STAINED_GLASS), GristTypes.BUILD.get().amount(3), 2, EntryLists.ATHENEUM);
		registerItem("cyan_stained_glass", new ItemStack(Blocks.CYAN_STAINED_GLASS), GristTypes.BUILD.get().amount(3), 2, EntryLists.ATHENEUM);
		registerItem("purple_stained_glass", new ItemStack(Blocks.PURPLE_STAINED_GLASS), GristTypes.BUILD.get().amount(3), 2, EntryLists.ATHENEUM);
		registerItem("blue_stained_glass", new ItemStack(Blocks.BLUE_STAINED_GLASS), GristTypes.BUILD.get().amount(2), 2, EntryLists.ATHENEUM);
		registerItem("brown_stained_glass", new ItemStack(Blocks.BROWN_STAINED_GLASS), GristTypes.BUILD.get().amount(3), 2, EntryLists.ATHENEUM);
		registerItem("green_stained_glass", new ItemStack(Blocks.GREEN_STAINED_GLASS), GristTypes.BUILD.get().amount(2), 2, EntryLists.ATHENEUM);
		registerItem("red_stained_glass", new ItemStack(Blocks.RED_STAINED_GLASS), GristTypes.BUILD.get().amount(2), 2, EntryLists.ATHENEUM);
		registerItem("black_stained_glass", new ItemStack(Blocks.BLACK_STAINED_GLASS), GristTypes.BUILD.get().amount(2), 2, EntryLists.ATHENEUM);
		registerItem("glass_pane", new ItemStack(Blocks.GLASS_PANE), GristTypes.BUILD.get().amount(1), 0, EntryLists.ATHENEUM);
		registerItem("white_stained_glass_pane", new ItemStack(Blocks.WHITE_STAINED_GLASS_PANE), GristTypes.BUILD.get().amount(2), 2, EntryLists.ATHENEUM);
		registerItem("orange_stained_glass_pane", new ItemStack(Blocks.ORANGE_STAINED_GLASS_PANE), GristTypes.BUILD.get().amount(3), 2, EntryLists.ATHENEUM);
		registerItem("magenta_stained_glass_pane", new ItemStack(Blocks.MAGENTA_STAINED_GLASS_PANE), GristTypes.BUILD.get().amount(4), 2, EntryLists.ATHENEUM);
		registerItem("light_blue_stained_glass_pane", new ItemStack(Blocks.LIGHT_BLUE_STAINED_GLASS_PANE), GristTypes.BUILD.get().amount(3), 2, EntryLists.ATHENEUM);
		registerItem("yellow_stained_glass_pane", new ItemStack(Blocks.YELLOW_STAINED_GLASS_PANE), GristTypes.BUILD.get().amount(2), 2, EntryLists.ATHENEUM);
		registerItem("lime_stained_glass_pane", new ItemStack(Blocks.LIME_STAINED_GLASS_PANE), GristTypes.BUILD.get().amount(3), 2, EntryLists.ATHENEUM);
		registerItem("pink_stained_glass_pane", new ItemStack(Blocks.PINK_STAINED_GLASS_PANE), GristTypes.BUILD.get().amount(3), 2, EntryLists.ATHENEUM);
		registerItem("gray_stained_glass_pane", new ItemStack(Blocks.GRAY_STAINED_GLASS_PANE), GristTypes.BUILD.get().amount(3), 2, EntryLists.ATHENEUM);
		registerItem("light_gray_stained_glass_pane", new ItemStack(Blocks.LIGHT_GRAY_STAINED_GLASS_PANE), GristTypes.BUILD.get().amount(3), 2, EntryLists.ATHENEUM);
		registerItem("cyan_stained_glass_pane", new ItemStack(Blocks.CYAN_STAINED_GLASS_PANE), GristTypes.BUILD.get().amount(3), 2, EntryLists.ATHENEUM);
		registerItem("purple_stained_glass_pane", new ItemStack(Blocks.PURPLE_STAINED_GLASS_PANE), GristTypes.BUILD.get().amount(3), 2, EntryLists.ATHENEUM);
		registerItem("blue_stained_glass_pane", new ItemStack(Blocks.BLUE_STAINED_GLASS_PANE), GristTypes.BUILD.get().amount(2), 2, EntryLists.ATHENEUM);
		registerItem("brown_stained_glass_pane", new ItemStack(Blocks.BROWN_STAINED_GLASS_PANE), GristTypes.BUILD.get().amount(3), 2, EntryLists.ATHENEUM);
		registerItem("green_stained_glass_pane", new ItemStack(Blocks.GREEN_STAINED_GLASS_PANE), GristTypes.BUILD.get().amount(2), 2, EntryLists.ATHENEUM);
		registerItem("red_stained_glass_pane", new ItemStack(Blocks.RED_STAINED_GLASS_PANE), GristTypes.BUILD.get().amount(2), 2, EntryLists.ATHENEUM);
		registerItem("black_stained_glass_pane", new ItemStack(Blocks.BLACK_STAINED_GLASS_PANE), GristTypes.BUILD.get().amount(2), 2, EntryLists.ATHENEUM);
		registerItem("cobblestone_slab", new ItemStack(Blocks.COBBLESTONE_SLAB), GristTypes.BUILD.get().amount(1), 0, EntryLists.ATHENEUM);
		registerItem("mossy_cobblestone_slab", new ItemStack(Blocks.MOSSY_COBBLESTONE_SLAB), GristTypes.BUILD.get().amount(1), 1, EntryLists.ATHENEUM);
		registerItem("smooth_stone_slab", new ItemStack(Blocks.SMOOTH_STONE_SLAB), GristTypes.BUILD.get().amount(1), 2, EntryLists.ATHENEUM);
		registerItem("stone_slab", new ItemStack(Blocks.STONE_SLAB), GristTypes.BUILD.get().amount(1), 0, EntryLists.ATHENEUM);
		registerItem("stone_brick_slab", new ItemStack(Blocks.STONE_BRICK_SLAB), GristTypes.BUILD.get().amount(1), 0, EntryLists.ATHENEUM);
		registerItem("nether_brick_slab", new ItemStack(Blocks.NETHER_BRICK_SLAB), GristSet.of(GristTypes.BUILD.get().amount(1), GristTypes.TAR.get().amount(1)), 3, EntryLists.ATHENEUM);
		registerItem("cobblestone_stairs", new ItemStack(Blocks.COBBLESTONE_STAIRS), GristTypes.BUILD.get().amount(2), 0, EntryLists.ATHENEUM);
		registerItem("mossy_cobblestone_stairs", new ItemStack(Blocks.MOSSY_COBBLESTONE_STAIRS), GristTypes.BUILD.get().amount(2), 1, EntryLists.ATHENEUM);
		registerItem("stone_stairs", new ItemStack(Blocks.STONE_STAIRS), GristTypes.BUILD.get().amount(2), 0, EntryLists.ATHENEUM);
		registerItem("stone_brick_stairs", new ItemStack(Blocks.STONE_BRICK_STAIRS), GristTypes.BUILD.get().amount(2), 0, EntryLists.ATHENEUM);
		registerItem("nether_brick_stairs", new ItemStack(Blocks.NETHER_BRICK_STAIRS), GristSet.of(GristTypes.BUILD.get().amount(2), GristTypes.TAR.get().amount(2)), 3, EntryLists.ATHENEUM);
		registerItem("cobblestone_wall", new ItemStack(Blocks.COBBLESTONE_WALL), GristTypes.BUILD.get().amount(2), 0, EntryLists.ATHENEUM);
		registerItem("mossy_cobblestone_wall", new ItemStack(Blocks.MOSSY_COBBLESTONE_WALL), GristTypes.BUILD.get().amount(2), 1, EntryLists.ATHENEUM);
		registerItem("stone_brick_wall", new ItemStack(Blocks.STONE_BRICK_WALL), GristTypes.BUILD.get().amount(2), 0, EntryLists.ATHENEUM);
		registerItem("nether_brick_wall", new ItemStack(Blocks.NETHER_BRICK_WALL), GristSet.of(GristTypes.BUILD.get().amount(2), GristTypes.TAR.get().amount(2)), 3, EntryLists.ATHENEUM);
		registerItem("nether_brick_fence", new ItemStack(Blocks.NETHER_BRICK_FENCE), GristSet.of(GristTypes.BUILD.get().amount(2), GristTypes.TAR.get().amount(2)), 3, EntryLists.ATHENEUM);
		registerItem("oak_slab", new ItemStack(Blocks.OAK_SLAB), GristTypes.BUILD.get().amount(1), 0, EntryLists.ATHENEUM);
		registerItem("birch_slab", new ItemStack(Blocks.BIRCH_SLAB), GristTypes.BUILD.get().amount(1), 0, EntryLists.ATHENEUM);
		registerItem("spruce_slab", new ItemStack(Blocks.SPRUCE_SLAB), GristTypes.BUILD.get().amount(1), 0, EntryLists.ATHENEUM);
		registerItem("dark_oak_slab", new ItemStack(Blocks.DARK_OAK_SLAB), GristTypes.BUILD.get().amount(1), 0, EntryLists.ATHENEUM);
		registerItem("acacia_slab", new ItemStack(Blocks.ACACIA_SLAB), GristTypes.BUILD.get().amount(1), 0, EntryLists.ATHENEUM);
		registerItem("jungle_slab", new ItemStack(Blocks.JUNGLE_SLAB), GristTypes.BUILD.get().amount(1), 0, EntryLists.ATHENEUM);
		registerItem("oak_stairs", new ItemStack(Blocks.OAK_STAIRS), GristTypes.BUILD.get().amount(2), 0, EntryLists.ATHENEUM);
		registerItem("birch_stairs", new ItemStack(Blocks.BIRCH_STAIRS), GristTypes.BUILD.get().amount(2), 0, EntryLists.ATHENEUM);
		registerItem("spruce_stairs", new ItemStack(Blocks.SPRUCE_STAIRS), GristTypes.BUILD.get().amount(2), 0, EntryLists.ATHENEUM);
		registerItem("dark_oak_stairs", new ItemStack(Blocks.DARK_OAK_STAIRS), GristTypes.BUILD.get().amount(2), 0, EntryLists.ATHENEUM);
		registerItem("acacia_stairs", new ItemStack(Blocks.ACACIA_STAIRS), GristTypes.BUILD.get().amount(2), 0, EntryLists.ATHENEUM);
		registerItem("jungle_stairs", new ItemStack(Blocks.JUNGLE_STAIRS), GristTypes.BUILD.get().amount(2), 0, EntryLists.ATHENEUM);
		registerItem("oak_fence", new ItemStack(Blocks.OAK_FENCE), GristTypes.BUILD.get().amount(2), 0, EntryLists.ATHENEUM);
		registerItem("birch_fence", new ItemStack(Blocks.BIRCH_FENCE), GristTypes.BUILD.get().amount(2), 0, EntryLists.ATHENEUM);
		registerItem("spruce_fence", new ItemStack(Blocks.SPRUCE_FENCE), GristTypes.BUILD.get().amount(2), 0, EntryLists.ATHENEUM);
		registerItem("acacia_fence", new ItemStack(Blocks.ACACIA_FENCE), GristTypes.BUILD.get().amount(2), 0, EntryLists.ATHENEUM);
		registerItem("dark_oak_fence", new ItemStack(Blocks.DARK_OAK_FENCE), GristTypes.BUILD.get().amount(2), 0, EntryLists.ATHENEUM);
		registerItem("jungle_fence", new ItemStack(Blocks.JUNGLE_FENCE), GristTypes.BUILD.get().amount(2), 0, EntryLists.ATHENEUM);
		registerItem("oak_log", new ItemStack(Blocks.OAK_LOG), GristTypes.BUILD.get().amount(4), 0, EntryLists.ATHENEUM);
		registerItem("birch_log", new ItemStack(Blocks.BIRCH_LOG), GristTypes.BUILD.get().amount(4), 0, EntryLists.ATHENEUM);
		registerItem("spruce_log", new ItemStack(Blocks.SPRUCE_LOG), GristTypes.BUILD.get().amount(4), 0, EntryLists.ATHENEUM);
		registerItem("dark_oak_log", new ItemStack(Blocks.DARK_OAK_LOG), GristTypes.BUILD.get().amount(4), 0, EntryLists.ATHENEUM);
		registerItem("acacia_log", new ItemStack(Blocks.ACACIA_LOG), GristTypes.BUILD.get().amount(4), 0, EntryLists.ATHENEUM);
		registerItem("jungle_log", new ItemStack(Blocks.JUNGLE_LOG), GristTypes.BUILD.get().amount(4), 0, EntryLists.ATHENEUM);
		registerItem("andesite", new ItemStack(Blocks.ANDESITE), GristTypes.BUILD.get().amount(1), 0, EntryLists.ATHENEUM);
		registerItem("diorite", new ItemStack(Blocks.DIORITE), GristTypes.BUILD.get().amount(1), 0, EntryLists.ATHENEUM);
		registerItem("granite", new ItemStack(Blocks.GRANITE), GristTypes.BUILD.get().amount(1), 0, EntryLists.ATHENEUM);
		registerItem("cobbled_deepslate", new ItemStack(Blocks.COBBLED_DEEPSLATE), GristTypes.BUILD.get().amount(1), 1, EntryLists.ATHENEUM);
		registerItem("polished_andesite", new ItemStack(Blocks.POLISHED_ANDESITE), GristTypes.BUILD.get().amount(1), 0, EntryLists.ATHENEUM);
		registerItem("polished_diorite", new ItemStack(Blocks.POLISHED_DIORITE), GristTypes.BUILD.get().amount(1), 0, EntryLists.ATHENEUM);
		registerItem("polished_granite", new ItemStack(Blocks.POLISHED_GRANITE), GristTypes.BUILD.get().amount(1), 0, EntryLists.ATHENEUM);
		registerItem("polished_deepslate", new ItemStack(Blocks.POLISHED_DEEPSLATE), GristTypes.BUILD.get().amount(1), 1, EntryLists.ATHENEUM);
		registerItem("deepslate_bricks", new ItemStack(Blocks.DEEPSLATE_BRICKS), GristTypes.BUILD.get().amount(1), 1, EntryLists.ATHENEUM);
		registerItem("deepslate_tiles", new ItemStack(Blocks.DEEPSLATE_TILES), GristTypes.BUILD.get().amount(1), 1, EntryLists.ATHENEUM);
		registerItem("chiseled_deepslate", new ItemStack(Blocks.CHISELED_DEEPSLATE), GristTypes.BUILD.get().amount(1), 2, EntryLists.ATHENEUM);
		registerItem("andesite_slab", new ItemStack(Blocks.ANDESITE_SLAB), GristTypes.BUILD.get().amount(1), 0, EntryLists.ATHENEUM);
		registerItem("diorite_slab", new ItemStack(Blocks.DIORITE_SLAB), GristTypes.BUILD.get().amount(1), 0, EntryLists.ATHENEUM);
		registerItem("granite_slab", new ItemStack(Blocks.GRANITE_SLAB), GristTypes.BUILD.get().amount(1), 0, EntryLists.ATHENEUM);
		registerItem("cobbled_deepslate_slab", new ItemStack(Blocks.COBBLED_DEEPSLATE_SLAB), GristTypes.BUILD.get().amount(1), 1, EntryLists.ATHENEUM);
		registerItem("polished_andesite_slab", new ItemStack(Blocks.POLISHED_ANDESITE_SLAB), GristTypes.BUILD.get().amount(1), 0, EntryLists.ATHENEUM);
		registerItem("polished_diorite_slab", new ItemStack(Blocks.POLISHED_DIORITE_SLAB), GristTypes.BUILD.get().amount(1), 0, EntryLists.ATHENEUM);
		registerItem("polished_granite_slab", new ItemStack(Blocks.POLISHED_GRANITE_SLAB), GristTypes.BUILD.get().amount(1), 0, EntryLists.ATHENEUM);
		registerItem("polished_deepslate_slab", new ItemStack(Blocks.POLISHED_DEEPSLATE_SLAB), GristTypes.BUILD.get().amount(1), 1, EntryLists.ATHENEUM);
		registerItem("deepslate_brick_slab", new ItemStack(Blocks.DEEPSLATE_BRICK_SLAB), GristTypes.BUILD.get().amount(1), 1, EntryLists.ATHENEUM);
		registerItem("deepslate_tile_slab", new ItemStack(Blocks.DEEPSLATE_TILE_SLAB), GristTypes.BUILD.get().amount(1), 1, EntryLists.ATHENEUM);
		registerItem("andesite_stairs", new ItemStack(Blocks.ANDESITE_STAIRS), GristTypes.BUILD.get().amount(2), 0, EntryLists.ATHENEUM);
		registerItem("diorite_stairs", new ItemStack(Blocks.DIORITE_STAIRS), GristTypes.BUILD.get().amount(2), 0, EntryLists.ATHENEUM);
		registerItem("granite_stairs", new ItemStack(Blocks.GRANITE_STAIRS), GristTypes.BUILD.get().amount(2), 0, EntryLists.ATHENEUM);
		registerItem("cobbled_deepslate_stairs", new ItemStack(Blocks.COBBLED_DEEPSLATE_STAIRS), GristTypes.BUILD.get().amount(2), 1, EntryLists.ATHENEUM);
		registerItem("polished_andesite_stairs", new ItemStack(Blocks.POLISHED_ANDESITE_STAIRS), GristTypes.BUILD.get().amount(2), 0, EntryLists.ATHENEUM);
		registerItem("polished_diorite_stairs", new ItemStack(Blocks.POLISHED_DIORITE_STAIRS), GristTypes.BUILD.get().amount(2), 0, EntryLists.ATHENEUM);
		registerItem("polished_granite_stairs", new ItemStack(Blocks.POLISHED_GRANITE_STAIRS), GristTypes.BUILD.get().amount(2), 0, EntryLists.ATHENEUM);
		registerItem("polished_deepslate_stairs", new ItemStack(Blocks.POLISHED_DEEPSLATE_STAIRS), GristTypes.BUILD.get().amount(2), 1, EntryLists.ATHENEUM);
		registerItem("deepslate_brick_stairs", new ItemStack(Blocks.DEEPSLATE_BRICK_STAIRS), GristTypes.BUILD.get().amount(2), 1, EntryLists.ATHENEUM);
		registerItem("deepslate_tile_stairs", new ItemStack(Blocks.DEEPSLATE_TILE_STAIRS), GristTypes.BUILD.get().amount(2), 1, EntryLists.ATHENEUM);
		registerItem("andesite_wall", new ItemStack(Blocks.ANDESITE_WALL), GristTypes.BUILD.get().amount(2), 0, EntryLists.ATHENEUM);
		registerItem("diorite_wall", new ItemStack(Blocks.DIORITE_WALL), GristTypes.BUILD.get().amount(2), 0, EntryLists.ATHENEUM);
		registerItem("granite_wall", new ItemStack(Blocks.GRANITE_WALL), GristTypes.BUILD.get().amount(2), 0, EntryLists.ATHENEUM);
		registerItem("cobbled_deepslate_wall", new ItemStack(Blocks.COBBLED_DEEPSLATE_WALL), GristTypes.BUILD.get().amount(2), 1, EntryLists.ATHENEUM);
		registerItem("polished_deepslate_wall", new ItemStack(Blocks.POLISHED_DEEPSLATE_WALL), GristTypes.BUILD.get().amount(2), 1, EntryLists.ATHENEUM);
		registerItem("deepslate_brick_wall", new ItemStack(Blocks.DEEPSLATE_BRICK_WALL), GristTypes.BUILD.get().amount(2), 1, EntryLists.ATHENEUM);
		registerItem("deepslate_tile_wall", new ItemStack(Blocks.DEEPSLATE_TILE_WALL), GristTypes.BUILD.get().amount(2), 1, EntryLists.ATHENEUM);
		registerItem("white_concrete", new ItemStack(Blocks.WHITE_CONCRETE), GristSet.of(GristTypes.BUILD.get().amount(1), GristTypes.COBALT.get().amount(1), GristTypes.CHALK.get().amount(1)), 2, EntryLists.ATHENEUM);
		registerItem("orange_concrete", new ItemStack(Blocks.ORANGE_CONCRETE), GristSet.of(GristTypes.BUILD.get().amount(1), GristTypes.COBALT.get().amount(1), GristTypes.AMBER.get().amount(1), GristTypes.GARNET.get().amount(1)), 2, EntryLists.ATHENEUM);
		registerItem("magenta_concrete", new ItemStack(Blocks.MAGENTA_CONCRETE), GristSet.of(GristTypes.BUILD.get().amount(1), GristTypes.COBALT.get().amount(1), GristTypes.AMETHYST.get().amount(1), GristTypes.GARNET.get().amount(1)), 2, EntryLists.ATHENEUM);
		registerItem("light_blue_concrete", new ItemStack(Blocks.LIGHT_BLUE_CONCRETE), GristSet.of(GristTypes.BUILD.get().amount(1), GristTypes.COBALT.get().amount(1), GristTypes.CHALK.get().amount(1), GristTypes.AMETHYST.get().amount(1)), 2, EntryLists.ATHENEUM);
		registerItem("yellow_concrete", new ItemStack(Blocks.YELLOW_CONCRETE), GristSet.of(GristTypes.BUILD.get().amount(1), GristTypes.COBALT.get().amount(1), GristTypes.AMBER.get().amount(1)), 2, EntryLists.ATHENEUM);
		registerItem("lime_concrete", new ItemStack(Blocks.LIME_CONCRETE), GristSet.of(GristTypes.BUILD.get().amount(1), GristTypes.COBALT.get().amount(1), GristTypes.CHALK.get().amount(1), GristTypes.AMBER.get().amount(1)), 2, EntryLists.ATHENEUM);
		registerItem("pink_concrete", new ItemStack(Blocks.PINK_CONCRETE), GristSet.of(GristTypes.BUILD.get().amount(1), GristTypes.COBALT.get().amount(1), GristTypes.CHALK.get().amount(1), GristTypes.GARNET.get().amount(1)), 2, EntryLists.ATHENEUM);
		registerItem("gray_concrete", new ItemStack(Blocks.GRAY_CONCRETE), GristSet.of(GristTypes.BUILD.get().amount(1), GristTypes.COBALT.get().amount(1), GristTypes.CHALK.get().amount(1), GristTypes.TAR.get().amount(1)), 2, EntryLists.ATHENEUM);
		registerItem("light_gray_concrete", new ItemStack(Blocks.LIGHT_GRAY_CONCRETE), GristSet.of(GristTypes.BUILD.get().amount(1), GristTypes.COBALT.get().amount(1), GristTypes.CHALK.get().amount(1), GristTypes.TAR.get().amount(1)), 2, EntryLists.ATHENEUM);
		registerItem("cyan_concrete", new ItemStack(Blocks.CYAN_CONCRETE), GristSet.of(GristTypes.BUILD.get().amount(1), GristTypes.COBALT.get().amount(1), GristTypes.AMBER.get().amount(1), GristTypes.AMETHYST.get().amount(1)), 2, EntryLists.ATHENEUM);
		registerItem("purple_concrete", new ItemStack(Blocks.PURPLE_CONCRETE), GristSet.of(GristTypes.BUILD.get().amount(1), GristTypes.COBALT.get().amount(1), GristTypes.GARNET.get().amount(1), GristTypes.AMETHYST.get().amount(1)), 2, EntryLists.ATHENEUM);
		registerItem("blue_concrete", new ItemStack(Blocks.BLUE_CONCRETE), GristSet.of(GristTypes.BUILD.get().amount(1), GristTypes.COBALT.get().amount(1), GristTypes.AMETHYST.get().amount(1)), 2, EntryLists.ATHENEUM);
		registerItem("brown_concrete", new ItemStack(Blocks.BROWN_CONCRETE), GristSet.of(GristTypes.BUILD.get().amount(1), GristTypes.COBALT.get().amount(1), GristTypes.AMBER.get().amount(1), GristTypes.IODINE.get().amount(1)), 2, EntryLists.ATHENEUM);
		registerItem("green_concrete", new ItemStack(Blocks.GREEN_CONCRETE), GristSet.of(GristTypes.BUILD.get().amount(1), GristTypes.COBALT.get().amount(1), GristTypes.AMBER.get().amount(1)), 2, EntryLists.ATHENEUM);
		registerItem("red_concrete", new ItemStack(Blocks.RED_CONCRETE), GristSet.of(GristTypes.BUILD.get().amount(1), GristTypes.COBALT.get().amount(1), GristTypes.GARNET.get().amount(1)), 2, EntryLists.ATHENEUM);
		registerItem("black_concrete", new ItemStack(Blocks.BLACK_CONCRETE), GristSet.of(GristTypes.BUILD.get().amount(1), GristTypes.COBALT.get().amount(1), GristTypes.TAR.get().amount(1)), 2, EntryLists.ATHENEUM);
		
	}
	
	public static void registerItem(String name, ItemStack stack, ImmutableGristSet cost, int tier, EntryLists entryList)
	{
		registerItem(name, stack, cost, cost, tier, entryList);
	}
	
	/**
	 * Register the specific item as deployable.
	 * Note: Not thread-safe. Make sure to only call this on the main thread
	 * @param stack The item to be registered.
	 * The itemstack can have nbt tags, with the exception of the display tag.
	 * @param cost1 How much it costs the first time deployed.
	 * @param cost2 How much it costs after the first times. Null if only deployable once.
	 * First cost will always be used when not in hardmode.
	 * @param tier The tier of the item; what connection position required in an unfinished chain to deploy.
	 * All will be available to all players when the chain is complete.
	 * @param entryList Enum defining which list the item is in. (I.E. Deployables or Atheneum).
	 * You cannot directly register items to EntryLists.ALL, as it is simply a list of all entries, regardless of category.
	 */
	public static void registerItem(String name, ItemStack stack, ImmutableGristSet cost1, ImmutableGristSet cost2, int tier, EntryLists entryList)
	{
		registerItem(name, cost1, cost2, tier, null, (connection, world) -> stack, entryList);
	}
	
	/**
	 * Not thread-safe. Make sure to only call this on the main thread
	 */
	public static void registerItem(String name, ImmutableGristSet cost, int tier, IAvailabilityCondition condition,
									BiFunction<SburbPlayerData, Level, ItemStack> item, EntryLists entryList)
	{
		registerItem(name, tier, condition, item, (isPrimary, playerData) -> cost, entryList);
	}
	
	/**
	 * Not thread-safe. Make sure to only call this on the main thread
	 */
	public static void registerItem(String name, ImmutableGristSet cost1, ImmutableGristSet cost2, int tier, IAvailabilityCondition condition,
									BiFunction<SburbPlayerData, Level, ItemStack> item, EntryLists entryList)
	{
		registerItem(name, tier, condition, item, (isPrimary, playerData) -> isPrimary ? cost1 : cost2, entryList);
	}
	
	/**
	 * Not thread-safe. Make sure to only call this on the main thread
	 */
	public static void registerItem(String name, int tier, IAvailabilityCondition condition,
									BiFunction<SburbPlayerData, Level, ItemStack> item,
									BiFunction<Boolean, SburbPlayerData, GristSet> grist, EntryLists entryList)
	{
		if(entryList == EntryLists.ALL)
			throw new IllegalArgumentException("Not allowed to add items to allList directly!");
		if(containsEntry(name))
			throw new IllegalStateException("Item stack already added to the deploy list: "+name);
		entryList.getList().add(new DeployEntry(name, tier, condition, item, grist, entryList));
		allList.add(new DeployEntry(name, tier, condition, item, grist, entryList));
	}
	
	public static List<DeployEntry> getItemList(MinecraftServer server, SburbPlayerData playerData)
	{
		return getItemList(server, playerData, EntryLists.ALL);
	}
	
	public static List<DeployEntry> getItemList(MinecraftServer server, SburbPlayerData playerData, EntryLists entryList)
	{
		int tier = SburbHandler.availableTier(server, playerData.playerId());
		ArrayList<DeployEntry> itemList = new ArrayList<>();
		for(DeployEntry entry : entryList.getList())
			if(entry.isAvailable(playerData, tier))
				itemList.add(entry);
		
		return itemList;
	}
	
	@Nonnull
	static ItemStack cleanStack(ItemStack stack)
	{
		if(stack.isEmpty())
			return ItemStack.EMPTY;
		stack = stack.copy();
		stack.setCount(1);
		if(stack.hasTag() && stack.getTag().isEmpty())
			stack.setTag(null);
		return stack;
	}
	
	public static boolean containsEntry(String name)
	{
		return containsEntry(name, EntryLists.ALL);
	}
	
	public static boolean containsEntry(String name, EntryLists entryList)
	{
		return getEntryForName(name, entryList) != null;
	}
	
	public static boolean containsItemStack(ItemStack stack, SburbPlayerData playerData, Level level)
	{
		return containsItemStack(stack, playerData, level, EntryLists.ALL);
	}
	
	public static boolean containsItemStack(ItemStack stack, SburbPlayerData playerData, Level level, EntryLists entryList)
	{
		return getEntryForItem(stack, playerData, level, entryList) != null;
	}
	
	public static DeployEntry getEntryForName(String name)
	{
		return getEntryForName(name, EntryLists.ALL);
	}
	
	public static DeployEntry getEntryForName(String name, EntryLists entryList)
	{
		for(DeployEntry entry : entryList.getList())
			if(entry.getName().equals(name))
				return entry;
		return null;
	}
	
	public static DeployEntry getEntryForItem(ItemStack stack, SburbPlayerData playerData, Level level)
	{
		return getEntryForItem(stack, playerData, level, EntryLists.ALL);
	}
	
	public static DeployEntry getEntryForItem(ItemStack stack, SburbPlayerData playerData, Level level, EntryLists entryList)
	{
		stack = cleanStack(stack);
		for(DeployEntry entry : entryList.getList())
			if(ItemStack.matches(stack, entry.getItemStack(playerData, level)))
				return entry;
		return null;
	}
	
	
	public interface IAvailabilityCondition
	{
		boolean test(SburbPlayerData playerData);
	}
	
	public static IAvailabilityCondition config(ModConfigSpec.BooleanValue config)
	{
		return playerData -> config.get();
	}
	
	public static BiFunction<SburbPlayerData, Level, ItemStack> item(ItemLike item)
	{
		return (playerData, world) -> new ItemStack(item);
	}
	
	static CompoundTag getDeployListTag(MinecraftServer server, SburbPlayerData playerData)
	{
		return getDeployListTag(server, playerData, EntryLists.ALL);
	}
	
	static CompoundTag getDeployListTag(MinecraftServer server, SburbPlayerData playerData, EntryLists entryList)
	{
		CompoundTag nbt = new CompoundTag();
		ListTag tagList = new ListTag();
		nbt.put("l", tagList);
		int tier = SburbHandler.availableTier(server, playerData.playerId());
		for(int i = 0; i < entryList.getList().size(); i++)
		{
			DeployEntry entry = entryList.getList().get(i);
			entry.tryAddDeployTag(playerData, server.getLevel(Level.OVERWORLD), tier, tagList, i);
		}
		return nbt;
	}
	
	private static long lastDay;
	
	@SubscribeEvent
	public static void serverStarting(ServerStartingEvent event)
	{
		lastDay = event.getServer().overworld().getGameTime() / 24000L;
	}
	
	@SubscribeEvent
	public static void onServerTick(TickEvent.ServerTickEvent event)
	{
		if(event.phase == TickEvent.Phase.END && !MinestuckConfig.SERVER.hardMode.get())
		{
			MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
			long currentDay = server.overworld().getGameTime() / 24000L;
			if(currentDay != lastDay)
			{
				lastDay = currentDay;
				SkaianetData.get(server).allPlayerData().forEach(SburbPlayerData::resetGivenItems);
			}
		}
	}
	
	/**
	 * Should be called any time that the conditions of deploy list entries might have changed for players.
	 */
	public static void onConditionsUpdated(MinecraftServer server)
	{
		MSExtraData.get(server).forEach(EditData::sendGivenItemsToEditor);
	}
	
	public static List<ItemStack> getEditmodeTools()
	{
		return Collections.emptyList();
	}
}