package com.mraof.minestuck.computer.editmode;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.alchemy.AlchemyHelper;
import com.mraof.minestuck.alchemy.GristSet;
import com.mraof.minestuck.alchemy.ImmutableGristSet;
import com.mraof.minestuck.api.alchemy.GristTypes;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.item.block.MiniCruxtruderItem;
import com.mraof.minestuck.skaianet.SburbConnection;
import com.mraof.minestuck.skaianet.SburbHandler;
import com.mraof.minestuck.util.ColorHandler;
import com.mraof.minestuck.world.storage.MSExtraData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.ForgeConfigSpec;

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
public final class DeployList
{
	public static final IAvailabilityCondition HAS_NOT_ENTERED = connection -> !connection.hasEntered();
	
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
		registerItem("cruxtruder", new ItemStack(MSBlocks.CRUXTRUDER), GristSet.EMPTY, GristSet.of(GristTypes.BUILD, 100), 0, EntryLists.DEPLOY);
		registerItem("totem_lathe", new ItemStack(MSBlocks.TOTEM_LATHE), GristSet.EMPTY, GristSet.of(GristTypes.BUILD, 100), 0, EntryLists.DEPLOY);
		registerItem("artifact_card", GristSet.EMPTY, null, 0, HAS_NOT_ENTERED,
				(connection, level) -> AlchemyHelper.createPunchedCard(SburbHandler.getEntryItem(level, connection)), EntryLists.DEPLOY);
		registerItem("alchemiter", new ItemStack(MSBlocks.ALCHEMITER), GristSet.EMPTY, GristSet.of(GristTypes.BUILD, 100), 0, EntryLists.DEPLOY);
		registerItem("punch_designix", 0, null, item(MSBlocks.PUNCH_DESIGNIX),
				(isPrimary, connection) -> GristSet.of(connection.getBaseGrist(), 4), EntryLists.DEPLOY);
		registerItem("portable_cruxtruder", GristSet.of(GristTypes.BUILD, 200), 1, config(MinestuckConfig.SERVER.portableMachines),
				(connection, level) -> MiniCruxtruderItem.getCruxtruderWithColor(ColorHandler.getColorForPlayer(connection.getClientIdentifier(), level)), EntryLists.DEPLOY);
		registerItem("portable_punch_designix", GristSet.of(GristTypes.BUILD, 200), 1, config(MinestuckConfig.SERVER.portableMachines), item(MSBlocks.MINI_PUNCH_DESIGNIX.get()), EntryLists.DEPLOY);
		registerItem("portable_totem_lathe", GristSet.of(GristTypes.BUILD, 200), 1, config(MinestuckConfig.SERVER.portableMachines), item(MSBlocks.MINI_TOTEM_LATHE.get()), EntryLists.DEPLOY);
		registerItem("portable_alchemiter", GristSet.of(GristTypes.BUILD, 300), 1, config(MinestuckConfig.SERVER.portableMachines), item(MSBlocks.MINI_ALCHEMITER.get()), EntryLists.DEPLOY);
		registerItem("holopad", new ItemStack(MSBlocks.HOLOPAD.get()), GristSet.of(GristTypes.BUILD, 4000), 2, EntryLists.DEPLOY);
		registerItem("intellibeam_laserstation", new ItemStack(MSBlocks.INTELLIBEAM_LASERSTATION.get()), GristSet.of(GristTypes.BUILD, 100000), 2, EntryLists.DEPLOY);
		registerItem("card_punched_card", GristSet.of(GristTypes.BUILD, 25), null, 0, config(MinestuckConfig.SERVER.deployCard), (sburbConnection, world) -> AlchemyHelper.createPunchedCard(new ItemStack(MSItems.CAPTCHA_CARD.get())), EntryLists.DEPLOY);
		
		//Atheneum
		registerItem("cobblestone", new ItemStack(Blocks.COBBLESTONE), GristSet.of(GristTypes.BUILD, 1), 0, EntryLists.ATHENEUM);
		registerItem("mossy_cobblestone", new ItemStack(Blocks.MOSSY_COBBLESTONE), GristSet.of(GristTypes.BUILD, 1), 1, EntryLists.ATHENEUM);
		registerItem("smooth_stone", new ItemStack(Blocks.SMOOTH_STONE), GristSet.of(GristTypes.BUILD, 1), 2, EntryLists.ATHENEUM);
		registerItem("stone_bricks", new ItemStack(Blocks.STONE_BRICKS), GristSet.of(GristTypes.BUILD, 1), 0, EntryLists.ATHENEUM);
		registerItem("chiseled_stone_bricks", new ItemStack(Blocks.CHISELED_STONE_BRICKS), GristSet.of(GristTypes.BUILD, 1), 2, EntryLists.ATHENEUM);
		registerItem("nether_bricks", new ItemStack(Blocks.NETHER_BRICKS), GristSet.of(GristTypes.BUILD, 1, GristTypes.TAR, 1), 3, EntryLists.ATHENEUM);
		registerItem("chiseled_nether_bricks", new ItemStack(Blocks.CHISELED_NETHER_BRICKS), GristSet.of(GristTypes.BUILD, 1, GristTypes.TAR, 1), 4, EntryLists.ATHENEUM);
		registerItem("oak_planks", new ItemStack(Blocks.OAK_PLANKS), GristSet.of(GristTypes.BUILD, 1), 0, EntryLists.ATHENEUM);
		registerItem("birch_planks", new ItemStack(Blocks.BIRCH_PLANKS), GristSet.of(GristTypes.BUILD, 1), 0, EntryLists.ATHENEUM);
		registerItem("spruce_planks", new ItemStack(Blocks.SPRUCE_PLANKS), GristSet.of(GristTypes.BUILD, 1), 0, EntryLists.ATHENEUM);
		registerItem("dark_oak_planks", new ItemStack(Blocks.DARK_OAK_PLANKS), GristSet.of(GristTypes.BUILD, 1), 0, EntryLists.ATHENEUM);
		registerItem("acacia_planks", new ItemStack(Blocks.ACACIA_PLANKS), GristSet.of(GristTypes.BUILD, 1), 0, EntryLists.ATHENEUM);
		registerItem("jungle_planks", new ItemStack(Blocks.JUNGLE_PLANKS), GristSet.of(GristTypes.BUILD, 1), 0, EntryLists.ATHENEUM);
		registerItem("torch", new ItemStack(Blocks.TORCH), GristSet.of(GristTypes.BUILD, 1), 0, EntryLists.ATHENEUM);
		registerItem("ladder", new ItemStack(Blocks.LADDER), GristSet.of(GristTypes.BUILD, 16), 0, EntryLists.ATHENEUM);
		registerItem("oak_door", new ItemStack(Blocks.OAK_DOOR), GristSet.of(GristTypes.BUILD, 2), 0, EntryLists.ATHENEUM);
		registerItem("birch_door", new ItemStack(Blocks.BIRCH_DOOR), GristSet.of(GristTypes.BUILD, 2), 0, EntryLists.ATHENEUM);
		registerItem("spruce_door", new ItemStack(Blocks.SPRUCE_DOOR), GristSet.of(GristTypes.BUILD, 2), 0, EntryLists.ATHENEUM);
		registerItem("dark_oak_door", new ItemStack(Blocks.DARK_OAK_DOOR), GristSet.of(GristTypes.BUILD, 2), 0, EntryLists.ATHENEUM);
		registerItem("acacia_door", new ItemStack(Blocks.ACACIA_DOOR), GristSet.of(GristTypes.BUILD, 2), 0, EntryLists.ATHENEUM);
		registerItem("jungle_door", new ItemStack(Blocks.JUNGLE_DOOR), GristSet.of(GristTypes.BUILD, 2), 0, EntryLists.ATHENEUM);
		registerItem("oak_trapdoor", new ItemStack(Blocks.OAK_TRAPDOOR), GristSet.of(GristTypes.BUILD, 2), 0, EntryLists.ATHENEUM);
		registerItem("birch_trapdoor", new ItemStack(Blocks.BIRCH_TRAPDOOR), GristSet.of(GristTypes.BUILD, 2), 0, EntryLists.ATHENEUM);
		registerItem("spruce_trapdoor", new ItemStack(Blocks.SPRUCE_TRAPDOOR), GristSet.of(GristTypes.BUILD, 2), 0, EntryLists.ATHENEUM);
		registerItem("dark_oak_trapdoor", new ItemStack(Blocks.DARK_OAK_TRAPDOOR), GristSet.of(GristTypes.BUILD, 2), 0, EntryLists.ATHENEUM);
		registerItem("acacia_trapdoor", new ItemStack(Blocks.ACACIA_TRAPDOOR), GristSet.of(GristTypes.BUILD, 2), 0, EntryLists.ATHENEUM);
		registerItem("jungle_trapdoor", new ItemStack(Blocks.JUNGLE_TRAPDOOR), GristSet.of(GristTypes.BUILD, 2), 0, EntryLists.ATHENEUM);
		registerItem("glass", new ItemStack(Blocks.GLASS), GristSet.of(GristTypes.BUILD, 1), 0, EntryLists.ATHENEUM);
		registerItem("white_stained_glass", new ItemStack(Blocks.WHITE_STAINED_GLASS), GristSet.of(GristTypes.BUILD, 2), 2, EntryLists.ATHENEUM); //Stained glass and stained glass panes' atheneum costs are given an amount of build grist equal to the number of grist types their normal costs have. To offset farming potential, it has a connection-tier of two.
		registerItem("orange_stained_glass", new ItemStack(Blocks.ORANGE_STAINED_GLASS), GristSet.of(GristTypes.BUILD, 3), 2, EntryLists.ATHENEUM);
		registerItem("magenta_stained_glass", new ItemStack(Blocks.MAGENTA_STAINED_GLASS), GristSet.of(GristTypes.BUILD, 4), 2, EntryLists.ATHENEUM);
		registerItem("light_blue_stained_glass", new ItemStack(Blocks.LIGHT_BLUE_STAINED_GLASS), GristSet.of(GristTypes.BUILD, 3), 2, EntryLists.ATHENEUM);
		registerItem("yellow_stained_glass", new ItemStack(Blocks.YELLOW_STAINED_GLASS), GristSet.of(GristTypes.BUILD, 2), 2, EntryLists.ATHENEUM);
		registerItem("lime_stained_glass", new ItemStack(Blocks.LIME_STAINED_GLASS), GristSet.of(GristTypes.BUILD, 3), 2, EntryLists.ATHENEUM);
		registerItem("pink_stained_glass", new ItemStack(Blocks.PINK_STAINED_GLASS), GristSet.of(GristTypes.BUILD, 3), 2, EntryLists.ATHENEUM);
		registerItem("gray_stained_glass", new ItemStack(Blocks.GRAY_STAINED_GLASS), GristSet.of(GristTypes.BUILD, 3), 2, EntryLists.ATHENEUM);
		registerItem("light_gray_stained_glass", new ItemStack(Blocks.LIGHT_GRAY_STAINED_GLASS), GristSet.of(GristTypes.BUILD, 3), 2, EntryLists.ATHENEUM);
		registerItem("cyan_stained_glass", new ItemStack(Blocks.CYAN_STAINED_GLASS), GristSet.of(GristTypes.BUILD, 3), 2, EntryLists.ATHENEUM);
		registerItem("purple_stained_glass", new ItemStack(Blocks.PURPLE_STAINED_GLASS), GristSet.of(GristTypes.BUILD, 3), 2, EntryLists.ATHENEUM);
		registerItem("blue_stained_glass", new ItemStack(Blocks.BLUE_STAINED_GLASS), GristSet.of(GristTypes.BUILD, 2), 2, EntryLists.ATHENEUM);
		registerItem("brown_stained_glass", new ItemStack(Blocks.BROWN_STAINED_GLASS), GristSet.of(GristTypes.BUILD, 3), 2, EntryLists.ATHENEUM);
		registerItem("green_stained_glass", new ItemStack(Blocks.GREEN_STAINED_GLASS), GristSet.of(GristTypes.BUILD, 2), 2, EntryLists.ATHENEUM);
		registerItem("red_stained_glass", new ItemStack(Blocks.RED_STAINED_GLASS), GristSet.of(GristTypes.BUILD, 2), 2, EntryLists.ATHENEUM);
		registerItem("black_stained_glass", new ItemStack(Blocks.BLACK_STAINED_GLASS), GristSet.of(GristTypes.BUILD, 2), 2, EntryLists.ATHENEUM);
		registerItem("glass_pane", new ItemStack(Blocks.GLASS_PANE), GristSet.of(GristTypes.BUILD, 1), 0, EntryLists.ATHENEUM);
		registerItem("white_stained_glass_pane", new ItemStack(Blocks.WHITE_STAINED_GLASS_PANE), GristSet.of(GristTypes.BUILD, 2), 2, EntryLists.ATHENEUM);
		registerItem("orange_stained_glass_pane", new ItemStack(Blocks.ORANGE_STAINED_GLASS_PANE), GristSet.of(GristTypes.BUILD, 3), 2, EntryLists.ATHENEUM);
		registerItem("magenta_stained_glass_pane", new ItemStack(Blocks.MAGENTA_STAINED_GLASS_PANE), GristSet.of(GristTypes.BUILD, 4), 2, EntryLists.ATHENEUM);
		registerItem("light_blue_stained_glass_pane", new ItemStack(Blocks.LIGHT_BLUE_STAINED_GLASS_PANE), GristSet.of(GristTypes.BUILD, 3), 2, EntryLists.ATHENEUM);
		registerItem("yellow_stained_glass_pane", new ItemStack(Blocks.YELLOW_STAINED_GLASS_PANE), GristSet.of(GristTypes.BUILD, 2), 2, EntryLists.ATHENEUM);
		registerItem("lime_stained_glass_pane", new ItemStack(Blocks.LIME_STAINED_GLASS_PANE), GristSet.of(GristTypes.BUILD, 3), 2, EntryLists.ATHENEUM);
		registerItem("pink_stained_glass_pane", new ItemStack(Blocks.PINK_STAINED_GLASS_PANE), GristSet.of(GristTypes.BUILD, 3), 2, EntryLists.ATHENEUM);
		registerItem("gray_stained_glass_pane", new ItemStack(Blocks.GRAY_STAINED_GLASS_PANE), GristSet.of(GristTypes.BUILD, 3), 2, EntryLists.ATHENEUM);
		registerItem("light_gray_stained_glass_pane", new ItemStack(Blocks.LIGHT_GRAY_STAINED_GLASS_PANE), GristSet.of(GristTypes.BUILD, 3), 2, EntryLists.ATHENEUM);
		registerItem("cyan_stained_glass_pane", new ItemStack(Blocks.CYAN_STAINED_GLASS_PANE), GristSet.of(GristTypes.BUILD, 3), 2, EntryLists.ATHENEUM);
		registerItem("purple_stained_glass_pane", new ItemStack(Blocks.PURPLE_STAINED_GLASS_PANE), GristSet.of(GristTypes.BUILD, 3), 2, EntryLists.ATHENEUM);
		registerItem("blue_stained_glass_pane", new ItemStack(Blocks.BLUE_STAINED_GLASS_PANE), GristSet.of(GristTypes.BUILD, 2), 2, EntryLists.ATHENEUM);
		registerItem("brown_stained_glass_pane", new ItemStack(Blocks.BROWN_STAINED_GLASS_PANE), GristSet.of(GristTypes.BUILD, 3), 2, EntryLists.ATHENEUM);
		registerItem("green_stained_glass_pane", new ItemStack(Blocks.GREEN_STAINED_GLASS_PANE), GristSet.of(GristTypes.BUILD, 2), 2, EntryLists.ATHENEUM);
		registerItem("red_stained_glass_pane", new ItemStack(Blocks.RED_STAINED_GLASS_PANE), GristSet.of(GristTypes.BUILD, 2), 2, EntryLists.ATHENEUM);
		registerItem("black_stained_glass_pane", new ItemStack(Blocks.BLACK_STAINED_GLASS_PANE), GristSet.of(GristTypes.BUILD, 2), 2, EntryLists.ATHENEUM);
		registerItem("cobblestone_slab", new ItemStack(Blocks.COBBLESTONE_SLAB), GristSet.of(GristTypes.BUILD, 1), 0, EntryLists.ATHENEUM);
		registerItem("mossy_cobblestone_slab", new ItemStack(Blocks.MOSSY_COBBLESTONE_SLAB), GristSet.of(GristTypes.BUILD, 1), 1, EntryLists.ATHENEUM);
		registerItem("smooth_stone_slab", new ItemStack(Blocks.SMOOTH_STONE_SLAB), GristSet.of(GristTypes.BUILD, 1), 2, EntryLists.ATHENEUM);
		registerItem("stone_slab", new ItemStack(Blocks.STONE_SLAB), GristSet.of(GristTypes.BUILD, 1), 0, EntryLists.ATHENEUM);
		registerItem("stone_brick_slab", new ItemStack(Blocks.STONE_BRICK_SLAB), GristSet.of(GristTypes.BUILD, 1), 0, EntryLists.ATHENEUM);
		registerItem("nether_brick_slab", new ItemStack(Blocks.NETHER_BRICK_SLAB), GristSet.of(GristTypes.BUILD, 1, GristTypes.TAR, 1), 3, EntryLists.ATHENEUM);
		registerItem("cobblestone_stairs", new ItemStack(Blocks.COBBLESTONE_STAIRS), GristSet.of(GristTypes.BUILD, 2), 0, EntryLists.ATHENEUM);
		registerItem("mossy_cobblestone_stairs", new ItemStack(Blocks.MOSSY_COBBLESTONE_STAIRS), GristSet.of(GristTypes.BUILD, 2), 1, EntryLists.ATHENEUM);
		registerItem("stone_stairs", new ItemStack(Blocks.STONE_STAIRS), GristSet.of(GristTypes.BUILD, 2), 0, EntryLists.ATHENEUM);
		registerItem("stone_brick_stairs", new ItemStack(Blocks.STONE_BRICK_STAIRS), GristSet.of(GristTypes.BUILD, 2), 0, EntryLists.ATHENEUM);
		registerItem("nether_brick_stairs", new ItemStack(Blocks.NETHER_BRICK_STAIRS), GristSet.of(GristTypes.BUILD, 2, GristTypes.TAR, 2), 3, EntryLists.ATHENEUM);
		registerItem("cobblestone_wall", new ItemStack(Blocks.COBBLESTONE_WALL), GristSet.of(GristTypes.BUILD, 2), 0, EntryLists.ATHENEUM);
		registerItem("mossy_cobblestone_wall", new ItemStack(Blocks.MOSSY_COBBLESTONE_WALL), GristSet.of(GristTypes.BUILD, 2), 1, EntryLists.ATHENEUM);
		registerItem("stone_brick_wall", new ItemStack(Blocks.STONE_BRICK_WALL), GristSet.of(GristTypes.BUILD, 2), 0, EntryLists.ATHENEUM);
		registerItem("nether_brick_wall", new ItemStack(Blocks.NETHER_BRICK_WALL), GristSet.of(GristTypes.BUILD, 2, GristTypes.TAR, 2), 3, EntryLists.ATHENEUM);
		registerItem("nether_brick_fence", new ItemStack(Blocks.NETHER_BRICK_FENCE), GristSet.of(GristTypes.BUILD, 2, GristTypes.TAR, 2), 3, EntryLists.ATHENEUM);
		registerItem("oak_slab", new ItemStack(Blocks.OAK_SLAB), GristSet.of(GristTypes.BUILD, 1), 0, EntryLists.ATHENEUM);
		registerItem("birch_slab", new ItemStack(Blocks.BIRCH_SLAB), GristSet.of(GristTypes.BUILD, 1), 0, EntryLists.ATHENEUM);
		registerItem("spruce_slab", new ItemStack(Blocks.SPRUCE_SLAB), GristSet.of(GristTypes.BUILD, 1), 0, EntryLists.ATHENEUM);
		registerItem("dark_oak_slab", new ItemStack(Blocks.DARK_OAK_SLAB), GristSet.of(GristTypes.BUILD, 1), 0, EntryLists.ATHENEUM);
		registerItem("acacia_slab", new ItemStack(Blocks.ACACIA_SLAB), GristSet.of(GristTypes.BUILD, 1), 0, EntryLists.ATHENEUM);
		registerItem("jungle_slab", new ItemStack(Blocks.JUNGLE_SLAB), GristSet.of(GristTypes.BUILD, 1), 0, EntryLists.ATHENEUM);
		registerItem("oak_stairs", new ItemStack(Blocks.OAK_STAIRS), GristSet.of(GristTypes.BUILD, 2), 0, EntryLists.ATHENEUM);
		registerItem("birch_stairs", new ItemStack(Blocks.BIRCH_STAIRS), GristSet.of(GristTypes.BUILD, 2), 0, EntryLists.ATHENEUM);
		registerItem("spruce_stairs", new ItemStack(Blocks.SPRUCE_STAIRS), GristSet.of(GristTypes.BUILD, 2), 0, EntryLists.ATHENEUM);
		registerItem("dark_oak_stairs", new ItemStack(Blocks.DARK_OAK_STAIRS), GristSet.of(GristTypes.BUILD, 2), 0, EntryLists.ATHENEUM);
		registerItem("acacia_stairs", new ItemStack(Blocks.ACACIA_STAIRS), GristSet.of(GristTypes.BUILD, 2), 0, EntryLists.ATHENEUM);
		registerItem("jungle_stairs", new ItemStack(Blocks.JUNGLE_STAIRS), GristSet.of(GristTypes.BUILD, 2), 0, EntryLists.ATHENEUM);
		registerItem("oak_fence", new ItemStack(Blocks.OAK_FENCE), GristSet.of(GristTypes.BUILD, 2), 0, EntryLists.ATHENEUM);
		registerItem("birch_fence", new ItemStack(Blocks.BIRCH_FENCE), GristSet.of(GristTypes.BUILD, 2), 0, EntryLists.ATHENEUM);
		registerItem("spruce_fence", new ItemStack(Blocks.SPRUCE_FENCE), GristSet.of(GristTypes.BUILD, 2), 0, EntryLists.ATHENEUM);
		registerItem("acacia_fence", new ItemStack(Blocks.ACACIA_FENCE), GristSet.of(GristTypes.BUILD, 2), 0, EntryLists.ATHENEUM);
		registerItem("dark_oak_fence", new ItemStack(Blocks.DARK_OAK_FENCE), GristSet.of(GristTypes.BUILD, 2), 0, EntryLists.ATHENEUM);
		registerItem("jungle_fence", new ItemStack(Blocks.JUNGLE_FENCE), GristSet.of(GristTypes.BUILD, 2), 0, EntryLists.ATHENEUM);
		registerItem("oak_log", new ItemStack(Blocks.OAK_LOG), GristSet.of(GristTypes.BUILD, 4), 0, EntryLists.ATHENEUM);
		registerItem("birch_log", new ItemStack(Blocks.BIRCH_LOG), GristSet.of(GristTypes.BUILD, 4), 0, EntryLists.ATHENEUM);
		registerItem("spruce_log", new ItemStack(Blocks.SPRUCE_LOG), GristSet.of(GristTypes.BUILD, 4), 0, EntryLists.ATHENEUM);
		registerItem("dark_oak_log", new ItemStack(Blocks.DARK_OAK_LOG), GristSet.of(GristTypes.BUILD, 4), 0, EntryLists.ATHENEUM);
		registerItem("acacia_log", new ItemStack(Blocks.ACACIA_LOG), GristSet.of(GristTypes.BUILD, 4), 0, EntryLists.ATHENEUM);
		registerItem("jungle_log", new ItemStack(Blocks.JUNGLE_LOG), GristSet.of(GristTypes.BUILD, 4), 0, EntryLists.ATHENEUM);
		registerItem("andesite", new ItemStack(Blocks.ANDESITE), GristSet.of(GristTypes.BUILD, 1), 0, EntryLists.ATHENEUM);
		registerItem("diorite", new ItemStack(Blocks.DIORITE), GristSet.of(GristTypes.BUILD, 1), 0, EntryLists.ATHENEUM);
		registerItem("granite", new ItemStack(Blocks.GRANITE), GristSet.of(GristTypes.BUILD, 1), 0, EntryLists.ATHENEUM);
		registerItem("cobbled_deepslate", new ItemStack(Blocks.COBBLED_DEEPSLATE), GristSet.of(GristTypes.BUILD, 1), 1, EntryLists.ATHENEUM);
		registerItem("polished_andesite", new ItemStack(Blocks.POLISHED_ANDESITE), GristSet.of(GristTypes.BUILD, 1), 0, EntryLists.ATHENEUM);
		registerItem("polished_diorite", new ItemStack(Blocks.POLISHED_DIORITE), GristSet.of(GristTypes.BUILD, 1), 0, EntryLists.ATHENEUM);
		registerItem("polished_granite", new ItemStack(Blocks.POLISHED_GRANITE), GristSet.of(GristTypes.BUILD, 1), 0, EntryLists.ATHENEUM);
		registerItem("polished_deepslate", new ItemStack(Blocks.POLISHED_DEEPSLATE), GristSet.of(GristTypes.BUILD, 1), 1, EntryLists.ATHENEUM);
		registerItem("deepslate_bricks", new ItemStack(Blocks.DEEPSLATE_BRICKS), GristSet.of(GristTypes.BUILD, 1), 1, EntryLists.ATHENEUM);
		registerItem("deepslate_tiles", new ItemStack(Blocks.DEEPSLATE_TILES), GristSet.of(GristTypes.BUILD, 1), 1, EntryLists.ATHENEUM);
		registerItem("chiseled_deepslate", new ItemStack(Blocks.CHISELED_DEEPSLATE), GristSet.of(GristTypes.BUILD, 1), 2, EntryLists.ATHENEUM);
		registerItem("andesite_slab", new ItemStack(Blocks.ANDESITE_SLAB), GristSet.of(GristTypes.BUILD, 1), 0, EntryLists.ATHENEUM);
		registerItem("diorite_slab", new ItemStack(Blocks.DIORITE_SLAB), GristSet.of(GristTypes.BUILD, 1), 0, EntryLists.ATHENEUM);
		registerItem("granite_slab", new ItemStack(Blocks.GRANITE_SLAB), GristSet.of(GristTypes.BUILD, 1), 0, EntryLists.ATHENEUM);
		registerItem("cobbled_deepslate_slab", new ItemStack(Blocks.COBBLED_DEEPSLATE_SLAB), GristSet.of(GristTypes.BUILD, 1), 1, EntryLists.ATHENEUM);
		registerItem("polished_andesite_slab", new ItemStack(Blocks.POLISHED_ANDESITE_SLAB), GristSet.of(GristTypes.BUILD, 1), 0, EntryLists.ATHENEUM);
		registerItem("polished_diorite_slab", new ItemStack(Blocks.POLISHED_DIORITE_SLAB), GristSet.of(GristTypes.BUILD, 1), 0, EntryLists.ATHENEUM);
		registerItem("polished_granite_slab", new ItemStack(Blocks.POLISHED_GRANITE_SLAB), GristSet.of(GristTypes.BUILD, 1), 0, EntryLists.ATHENEUM);
		registerItem("polished_deepslate_slab", new ItemStack(Blocks.POLISHED_DEEPSLATE_SLAB), GristSet.of(GristTypes.BUILD, 1), 1, EntryLists.ATHENEUM);
		registerItem("deepslate_brick_slab", new ItemStack(Blocks.DEEPSLATE_BRICK_SLAB), GristSet.of(GristTypes.BUILD, 1), 1, EntryLists.ATHENEUM);
		registerItem("deepslate_tile_slab", new ItemStack(Blocks.DEEPSLATE_TILE_SLAB), GristSet.of(GristTypes.BUILD, 1), 1, EntryLists.ATHENEUM);
		registerItem("andesite_stairs", new ItemStack(Blocks.ANDESITE_STAIRS), GristSet.of(GristTypes.BUILD, 2), 0, EntryLists.ATHENEUM);
		registerItem("diorite_stairs", new ItemStack(Blocks.DIORITE_STAIRS), GristSet.of(GristTypes.BUILD, 2), 0, EntryLists.ATHENEUM);
		registerItem("granite_stairs", new ItemStack(Blocks.GRANITE_STAIRS), GristSet.of(GristTypes.BUILD, 2), 0, EntryLists.ATHENEUM);
		registerItem("cobbled_deepslate_stairs", new ItemStack(Blocks.COBBLED_DEEPSLATE_STAIRS), GristSet.of(GristTypes.BUILD, 2), 1, EntryLists.ATHENEUM);
		registerItem("polished_andesite_stairs", new ItemStack(Blocks.POLISHED_ANDESITE_STAIRS), GristSet.of(GristTypes.BUILD, 2), 0, EntryLists.ATHENEUM);
		registerItem("polished_diorite_stairs", new ItemStack(Blocks.POLISHED_DIORITE_STAIRS), GristSet.of(GristTypes.BUILD, 2), 0, EntryLists.ATHENEUM);
		registerItem("polished_granite_stairs", new ItemStack(Blocks.POLISHED_GRANITE_STAIRS), GristSet.of(GristTypes.BUILD, 2), 0, EntryLists.ATHENEUM);
		registerItem("polished_deepslate_stairs", new ItemStack(Blocks.POLISHED_DEEPSLATE_STAIRS), GristSet.of(GristTypes.BUILD, 2), 1, EntryLists.ATHENEUM);
		registerItem("deepslate_brick_stairs", new ItemStack(Blocks.DEEPSLATE_BRICK_STAIRS), GristSet.of(GristTypes.BUILD, 2), 1, EntryLists.ATHENEUM);
		registerItem("deepslate_tile_stairs", new ItemStack(Blocks.DEEPSLATE_TILE_STAIRS), GristSet.of(GristTypes.BUILD, 2), 1, EntryLists.ATHENEUM);
		registerItem("andesite_wall", new ItemStack(Blocks.ANDESITE_WALL), GristSet.of(GristTypes.BUILD, 2), 0, EntryLists.ATHENEUM);
		registerItem("diorite_wall", new ItemStack(Blocks.DIORITE_WALL), GristSet.of(GristTypes.BUILD, 2), 0, EntryLists.ATHENEUM);
		registerItem("granite_wall", new ItemStack(Blocks.GRANITE_WALL), GristSet.of(GristTypes.BUILD, 2), 0, EntryLists.ATHENEUM);
		registerItem("cobbled_deepslate_wall", new ItemStack(Blocks.COBBLED_DEEPSLATE_WALL), GristSet.of(GristTypes.BUILD, 2), 1, EntryLists.ATHENEUM);
		registerItem("polished_deepslate_wall", new ItemStack(Blocks.POLISHED_DEEPSLATE_WALL), GristSet.of(GristTypes.BUILD, 2), 1, EntryLists.ATHENEUM);
		registerItem("deepslate_brick_wall", new ItemStack(Blocks.DEEPSLATE_BRICK_WALL), GristSet.of(GristTypes.BUILD, 2), 1, EntryLists.ATHENEUM);
		registerItem("deepslate_tile_wall", new ItemStack(Blocks.DEEPSLATE_TILE_WALL), GristSet.of(GristTypes.BUILD, 2), 1, EntryLists.ATHENEUM);
		registerItem("white_concrete", new ItemStack(Blocks.WHITE_CONCRETE), GristSet.of(GristTypes.BUILD, 1, GristTypes.COBALT, 1, GristTypes.CHALK, 1), 2, EntryLists.ATHENEUM);
		registerItem("orange_concrete", new ItemStack(Blocks.ORANGE_CONCRETE), GristSet.of(GristTypes.BUILD, 1, GristTypes.COBALT, 1, GristTypes.AMBER, 1, GristTypes.GARNET, 1), 2, EntryLists.ATHENEUM);
		registerItem("magenta_concrete", new ItemStack(Blocks.MAGENTA_CONCRETE), GristSet.of(GristTypes.BUILD, 1, GristTypes.COBALT, 1, GristTypes.AMETHYST, 1, GristTypes.GARNET, 1), 2, EntryLists.ATHENEUM);
		registerItem("light_blue_concrete", new ItemStack(Blocks.LIGHT_BLUE_CONCRETE), GristSet.of(GristTypes.BUILD, 1, GristTypes.COBALT, 1, GristTypes.CHALK, 1, GristTypes.AMETHYST, 1), 2, EntryLists.ATHENEUM);
		registerItem("yellow_concrete", new ItemStack(Blocks.YELLOW_CONCRETE), GristSet.of(GristTypes.BUILD, 1, GristTypes.COBALT, 1, GristTypes.AMBER, 1), 2, EntryLists.ATHENEUM);
		registerItem("lime_concrete", new ItemStack(Blocks.LIME_CONCRETE), GristSet.of(GristTypes.BUILD, 1, GristTypes.COBALT, 1, GristTypes.CHALK, 1, GristTypes.AMBER, 1), 2, EntryLists.ATHENEUM);
		registerItem("pink_concrete", new ItemStack(Blocks.PINK_CONCRETE), GristSet.of(GristTypes.BUILD, 1, GristTypes.COBALT, 1, GristTypes.CHALK, 1, GristTypes.GARNET, 1), 2, EntryLists.ATHENEUM);
		registerItem("gray_concrete", new ItemStack(Blocks.GRAY_CONCRETE), GristSet.of(GristTypes.BUILD, 1, GristTypes.COBALT, 1, GristTypes.CHALK, 1, GristTypes.TAR, 1), 2, EntryLists.ATHENEUM);
		registerItem("light_gray_concrete", new ItemStack(Blocks.LIGHT_GRAY_CONCRETE), GristSet.of(GristTypes.BUILD, 1, GristTypes.COBALT, 1, GristTypes.CHALK, 1, GristTypes.TAR, 1), 2, EntryLists.ATHENEUM);
		registerItem("cyan_concrete", new ItemStack(Blocks.CYAN_CONCRETE), GristSet.of(GristTypes.BUILD, 1, GristTypes.COBALT, 1, GristTypes.AMBER, 1, GristTypes.AMETHYST, 1), 2, EntryLists.ATHENEUM);
		registerItem("purple_concrete", new ItemStack(Blocks.PURPLE_CONCRETE), GristSet.of(GristTypes.BUILD, 1, GristTypes.COBALT, 1, GristTypes.GARNET, 1, GristTypes.AMETHYST, 1), 2, EntryLists.ATHENEUM);
		registerItem("blue_concrete", new ItemStack(Blocks.BLUE_CONCRETE), GristSet.of(GristTypes.BUILD, 1, GristTypes.COBALT, 1, GristTypes.AMETHYST, 1), 2, EntryLists.ATHENEUM);
		registerItem("brown_concrete", new ItemStack(Blocks.BROWN_CONCRETE), GristSet.of(GristTypes.BUILD, 1, GristTypes.COBALT, 1, GristTypes.AMBER, 1, GristTypes.IODINE, 1), 2, EntryLists.ATHENEUM);
		registerItem("green_concrete", new ItemStack(Blocks.GREEN_CONCRETE), GristSet.of(GristTypes.BUILD, 1, GristTypes.COBALT, 1, GristTypes.AMBER, 1), 2, EntryLists.ATHENEUM);
		registerItem("red_concrete", new ItemStack(Blocks.RED_CONCRETE), GristSet.of(GristTypes.BUILD, 1, GristTypes.COBALT, 1, GristTypes.GARNET, 1), 2, EntryLists.ATHENEUM);
		registerItem("black_concrete", new ItemStack(Blocks.BLACK_CONCRETE), GristSet.of(GristTypes.BUILD, 1, GristTypes.COBALT, 1, GristTypes.TAR, 1), 2, EntryLists.ATHENEUM);
		
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
	public static void registerItem(String name, ImmutableGristSet cost, int tier, IAvailabilityCondition condition, BiFunction<SburbConnection, Level, ItemStack> item, EntryLists entryList)
	{
		registerItem(name, tier, condition, item, (isPrimary, connection) -> cost, entryList);
	}
	
	/**
	 * Not thread-safe. Make sure to only call this on the main thread
	 */
	public static void registerItem(String name, ImmutableGristSet cost1, ImmutableGristSet cost2, int tier, IAvailabilityCondition condition, BiFunction<SburbConnection, Level, ItemStack> item, EntryLists entryList)
	{
		registerItem(name, tier, condition, item, (isPrimary, connection) -> isPrimary ? cost1 : cost2, entryList);
	}
	
	/**
	 * Not thread-safe. Make sure to only call this on the main thread
	 */
	public static void registerItem(String name, int tier, IAvailabilityCondition condition, BiFunction<SburbConnection, Level, ItemStack> item, BiFunction<Boolean, SburbConnection, GristSet> grist, EntryLists entryList)
	{
		if(entryList == EntryLists.ALL)
			throw new IllegalArgumentException("Not allowed to add items to allList directly!");
		if(containsEntry(name))
			throw new IllegalStateException("Item stack already added to the deploy list: "+name);
		entryList.getList().add(new DeployEntry(name, tier, condition, item, grist, entryList));
		allList.add(new DeployEntry(name, tier, condition, item, grist, entryList));
	}
	
	public static List<DeployEntry> getItemList(MinecraftServer server, SburbConnection c)
	{
		return getItemList(server, c, EntryLists.ALL);
	}
	
	public static List<DeployEntry> getItemList(MinecraftServer server, SburbConnection c, EntryLists entryList)
	{
		int tier = SburbHandler.availableTier(server, c.getClientIdentifier());
		ArrayList<DeployEntry> itemList = new ArrayList<>();
		for(DeployEntry entry : entryList.getList())
			if(entry.isAvailable(c, tier))
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
	
	public static boolean containsItemStack(ItemStack stack, SburbConnection c, Level level)
	{
		return containsItemStack(stack, c, level, EntryLists.ALL);
	}
	
	public static boolean containsItemStack(ItemStack stack, SburbConnection c, Level level, EntryLists entryList)
	{
		return getEntryForItem(stack, c, level, entryList) != null;
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
	
	public static DeployEntry getEntryForItem(ItemStack stack, SburbConnection c, Level level)
	{
		return getEntryForItem(stack, c, level, EntryLists.ALL);
	}
	
	public static DeployEntry getEntryForItem(ItemStack stack, SburbConnection c, Level level, EntryLists entryList)
	{
		stack = cleanStack(stack);
		for(DeployEntry entry : entryList.getList())
			if(ItemStack.matches(stack, entry.getItemStack(c, level)))
				return entry;
		return null;
	}
	
	
	public interface IAvailabilityCondition
	{
		boolean test(SburbConnection connection);
	}
	
	public static IAvailabilityCondition config(ForgeConfigSpec.BooleanValue config)
	{
		return connection -> config.get();
	}
	
	public static BiFunction<SburbConnection, Level, ItemStack> item(ItemLike item)
	{
		return (sburbConnection, world) -> new ItemStack(item);
	}
	
	static CompoundTag getDeployListTag(MinecraftServer server, SburbConnection c)
	{
		return getDeployListTag(server, c, EntryLists.ALL);
	}
	
	static CompoundTag getDeployListTag(MinecraftServer server, SburbConnection c, EntryLists entryList)
	{
		CompoundTag nbt = new CompoundTag();
		ListTag tagList = new ListTag();
		nbt.put("l", tagList);
		int tier = SburbHandler.availableTier(server, c.getClientIdentifier());
		for(int i = 0; i < entryList.getList().size(); i++)
		{
			DeployEntry entry = entryList.getList().get(i);
			entry.tryAddDeployTag(c, server.getLevel(Level.OVERWORLD), tier, tagList, i);
		}
		return nbt;
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