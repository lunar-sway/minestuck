package com.mraof.minestuck.data;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import com.mraof.minestuck.api.alchemy.GristSet;
import com.mraof.minestuck.api.alchemy.GristTypes;
import com.mraof.minestuck.computer.editmode.DeployList.EntryLists;
import com.mraof.minestuck.computer.editmode.DeployListLoader.DeployDataEntry;
import com.mraof.minestuck.computer.editmode.DeployListLoader.DeployDataList;
import com.mraof.minestuck.computer.editmode.DeployListLoader.GristCost;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;

import javax.annotation.ParametersAreNonnullByDefault;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class DeployListProvider implements DataProvider
{
	private final Map<ResourceLocation, DeployDataList> lists = new HashMap<>();
	private final PackOutput output;
	private final String modid;
	
	public DeployListProvider(PackOutput output, String modid)
	{
		this.output = output;
		this.modid = modid;
	}
	
	protected void registerEntries()
	{
		add("oak", new DeployDataList(17,
				new DeployDataEntry(new ItemStack(Blocks.OAK_LOG), 0, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(4))), false),
				new DeployDataEntry(new ItemStack(Blocks.OAK_PLANKS), 0, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(1))), false),
				new DeployDataEntry(new ItemStack(Blocks.OAK_STAIRS), 0, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(2))), false),
				new DeployDataEntry(new ItemStack(Blocks.OAK_SLAB), 0, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(1))), false),
				new DeployDataEntry(new ItemStack(Blocks.OAK_FENCE), 0, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(2))), false),
				new DeployDataEntry(new ItemStack(Blocks.OAK_DOOR), 0, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(2))), false),
				new DeployDataEntry(new ItemStack(Blocks.OAK_TRAPDOOR), 0, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(2))), false)
		));
		add("spruce", new DeployDataList(16,
				new DeployDataEntry(new ItemStack(Blocks.SPRUCE_LOG), 0, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(4))), false),
				new DeployDataEntry(new ItemStack(Blocks.SPRUCE_PLANKS), 0, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(1))), false),
				new DeployDataEntry(new ItemStack(Blocks.SPRUCE_STAIRS), 0, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(2))), false),
				new DeployDataEntry(new ItemStack(Blocks.SPRUCE_SLAB), 0, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(1))), false),
				new DeployDataEntry(new ItemStack(Blocks.SPRUCE_FENCE), 0, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(2))), false),
				new DeployDataEntry(new ItemStack(Blocks.SPRUCE_DOOR), 0, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(2))), false),
				new DeployDataEntry(new ItemStack(Blocks.SPRUCE_TRAPDOOR), 0, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(2))), false)
		));
		add("birch", new DeployDataList(15,
				new DeployDataEntry(new ItemStack(Blocks.BIRCH_LOG), 0, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(4))), false),
				new DeployDataEntry(new ItemStack(Blocks.BIRCH_PLANKS), 0, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(1))), false),
				new DeployDataEntry(new ItemStack(Blocks.BIRCH_STAIRS), 0, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(2))), false),
				new DeployDataEntry(new ItemStack(Blocks.BIRCH_SLAB), 0, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(1))), false),
				new DeployDataEntry(new ItemStack(Blocks.BIRCH_FENCE), 0, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(2))), false),
				new DeployDataEntry(new ItemStack(Blocks.BIRCH_DOOR), 0, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(2))), false),
				new DeployDataEntry(new ItemStack(Blocks.BIRCH_TRAPDOOR), 0, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(2))), false)
		));
		add("jungle", new DeployDataList(14,
				new DeployDataEntry(new ItemStack(Blocks.JUNGLE_LOG), 0, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(4))), false),
				new DeployDataEntry(new ItemStack(Blocks.JUNGLE_PLANKS), 0, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(1))), false),
				new DeployDataEntry(new ItemStack(Blocks.JUNGLE_STAIRS), 0, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(2))), false),
				new DeployDataEntry(new ItemStack(Blocks.JUNGLE_SLAB), 0, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(1))), false),
				new DeployDataEntry(new ItemStack(Blocks.JUNGLE_FENCE), 0, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(2))), false),
				new DeployDataEntry(new ItemStack(Blocks.JUNGLE_DOOR), 0, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(2))), false),
				new DeployDataEntry(new ItemStack(Blocks.JUNGLE_TRAPDOOR), 0, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(2))), false)
		));
		add("acacia", new DeployDataList(13,
				new DeployDataEntry(new ItemStack(Blocks.ACACIA_LOG), 0, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(4))), false),
				new DeployDataEntry(new ItemStack(Blocks.ACACIA_PLANKS), 0, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(1))), false),
				new DeployDataEntry(new ItemStack(Blocks.ACACIA_STAIRS), 0, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(2))), false),
				new DeployDataEntry(new ItemStack(Blocks.ACACIA_SLAB), 0, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(1))), false),
				new DeployDataEntry(new ItemStack(Blocks.ACACIA_FENCE), 0, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(2))), false),
				new DeployDataEntry(new ItemStack(Blocks.ACACIA_DOOR), 0, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(2))), false),
				new DeployDataEntry(new ItemStack(Blocks.ACACIA_TRAPDOOR), 0, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(2))), false)
		));
		add("dark_oak", new DeployDataList(12,
				new DeployDataEntry(new ItemStack(Blocks.DARK_OAK_LOG), 0, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(4))), false),
				new DeployDataEntry(new ItemStack(Blocks.DARK_OAK_PLANKS), 0, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(1))), false),
				new DeployDataEntry(new ItemStack(Blocks.DARK_OAK_STAIRS), 0, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(2))), false),
				new DeployDataEntry(new ItemStack(Blocks.DARK_OAK_SLAB), 0, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(1))), false),
				new DeployDataEntry(new ItemStack(Blocks.DARK_OAK_FENCE), 0, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(2))), false),
				new DeployDataEntry(new ItemStack(Blocks.DARK_OAK_DOOR), 0, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(2))), false),
				new DeployDataEntry(new ItemStack(Blocks.DARK_OAK_TRAPDOOR), 0, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(2))), false)
		));
		add("torch", new DeployDataList(11,
				new DeployDataEntry(new ItemStack(Blocks.TORCH), 0, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(1))), false)
		));
		add("ladder", new DeployDataList(10,
				new DeployDataEntry(new ItemStack(Blocks.LADDER), 0, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(16))), false)
		));
		add("stone", new DeployDataList(9,
				new DeployDataEntry(new ItemStack(Blocks.STONE), 0, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(1))), false),
				new DeployDataEntry(new ItemStack(Blocks.STONE_STAIRS), 0, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(2))), false),
				new DeployDataEntry(new ItemStack(Blocks.STONE_SLAB), 0, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(1))), false),
				new DeployDataEntry(new ItemStack(Blocks.COBBLESTONE), 0, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(1))), false),
				new DeployDataEntry(new ItemStack(Blocks.COBBLESTONE_STAIRS), 0, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(2))), false),
				new DeployDataEntry(new ItemStack(Blocks.COBBLESTONE_SLAB), 0, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(1))), false),
				new DeployDataEntry(new ItemStack(Blocks.COBBLESTONE_WALL), 0, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(2))), false),
				new DeployDataEntry(new ItemStack(Blocks.MOSSY_COBBLESTONE), 1, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(1))), false),
				new DeployDataEntry(new ItemStack(Blocks.MOSSY_COBBLESTONE_STAIRS), 1, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(2))), false),
				new DeployDataEntry(new ItemStack(Blocks.MOSSY_COBBLESTONE_SLAB), 1, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(1))), false),
				new DeployDataEntry(new ItemStack(Blocks.MOSSY_COBBLESTONE_WALL), 1, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(2))), false),
				new DeployDataEntry(new ItemStack(Blocks.SMOOTH_STONE), 2, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(1))), false),
				new DeployDataEntry(new ItemStack(Blocks.SMOOTH_STONE_SLAB), 2, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(1))), false),
				new DeployDataEntry(new ItemStack(Blocks.STONE_BRICKS), 0, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(1))), false),
				new DeployDataEntry(new ItemStack(Blocks.STONE_BRICK_STAIRS), 0, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(2))), false),
				new DeployDataEntry(new ItemStack(Blocks.STONE_BRICK_SLAB), 0, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(1))), false),
				new DeployDataEntry(new ItemStack(Blocks.STONE_BRICK_WALL), 0, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(2))), false),
				new DeployDataEntry(new ItemStack(Blocks.CHISELED_STONE_BRICKS), 2, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(1))), false)
		));
		add("granite", new DeployDataList(8,
				new DeployDataEntry(new ItemStack(Blocks.GRANITE), 0, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(1))), false),
				new DeployDataEntry(new ItemStack(Blocks.GRANITE_STAIRS), 0, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(2))), false),
				new DeployDataEntry(new ItemStack(Blocks.GRANITE_SLAB), 0, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(1))), false),
				new DeployDataEntry(new ItemStack(Blocks.GRANITE_WALL), 0, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(2))), false),
				new DeployDataEntry(new ItemStack(Blocks.POLISHED_GRANITE), 0, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(1))), false),
				new DeployDataEntry(new ItemStack(Blocks.POLISHED_GRANITE_STAIRS), 0, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(2))), false),
				new DeployDataEntry(new ItemStack(Blocks.POLISHED_GRANITE_SLAB), 0, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(1))), false)
		));
		add("diorite", new DeployDataList(7,
				new DeployDataEntry(new ItemStack(Blocks.DIORITE), 0, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(1))), false),
				new DeployDataEntry(new ItemStack(Blocks.DIORITE_STAIRS), 0, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(2))), false),
				new DeployDataEntry(new ItemStack(Blocks.DIORITE_SLAB), 0, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(1))), false),
				new DeployDataEntry(new ItemStack(Blocks.DIORITE_WALL), 0, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(2))), false),
				new DeployDataEntry(new ItemStack(Blocks.POLISHED_DIORITE), 0, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(1))), false),
				new DeployDataEntry(new ItemStack(Blocks.POLISHED_DIORITE_STAIRS), 0, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(2))), false),
				new DeployDataEntry(new ItemStack(Blocks.POLISHED_DIORITE_SLAB), 0, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(1))), false)
		));
		add("andesite", new DeployDataList(6,
				new DeployDataEntry(new ItemStack(Blocks.ANDESITE), 0, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(1))), false),
				new DeployDataEntry(new ItemStack(Blocks.ANDESITE_STAIRS), 0, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(2))), false),
				new DeployDataEntry(new ItemStack(Blocks.ANDESITE_SLAB), 0, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(1))), false),
				new DeployDataEntry(new ItemStack(Blocks.ANDESITE_WALL), 0, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(2))), false),
				new DeployDataEntry(new ItemStack(Blocks.POLISHED_ANDESITE), 0, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(1))), false),
				new DeployDataEntry(new ItemStack(Blocks.POLISHED_ANDESITE_STAIRS), 0, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(2))), false),
				new DeployDataEntry(new ItemStack(Blocks.POLISHED_ANDESITE_SLAB), 0, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(1))), false)
		));
		add("deepslate", new DeployDataList(5,
				new DeployDataEntry(new ItemStack(Blocks.COBBLED_DEEPSLATE), 1, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(1))), false),
				new DeployDataEntry(new ItemStack(Blocks.COBBLED_DEEPSLATE_STAIRS), 1, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(2))), false),
				new DeployDataEntry(new ItemStack(Blocks.COBBLED_DEEPSLATE_SLAB), 1, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(1))), false),
				new DeployDataEntry(new ItemStack(Blocks.COBBLED_DEEPSLATE_WALL), 1, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(2))), false),
				new DeployDataEntry(new ItemStack(Blocks.CHISELED_DEEPSLATE), 2, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(1))), false),
				new DeployDataEntry(new ItemStack(Blocks.POLISHED_DEEPSLATE), 1, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(1))), false),
				new DeployDataEntry(new ItemStack(Blocks.POLISHED_DEEPSLATE_STAIRS), 1, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(2))), false),
				new DeployDataEntry(new ItemStack(Blocks.POLISHED_DEEPSLATE_SLAB), 1, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(1))), false),
				new DeployDataEntry(new ItemStack(Blocks.POLISHED_DEEPSLATE_WALL), 1, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(2))), false),
				new DeployDataEntry(new ItemStack(Blocks.DEEPSLATE_BRICKS), 1, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(1))), false),
				new DeployDataEntry(new ItemStack(Blocks.DEEPSLATE_BRICK_STAIRS), 1, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(2))), false),
				new DeployDataEntry(new ItemStack(Blocks.DEEPSLATE_BRICK_SLAB), 1, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(1))), false),
				new DeployDataEntry(new ItemStack(Blocks.DEEPSLATE_BRICK_WALL), 1, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(2))), false),
				new DeployDataEntry(new ItemStack(Blocks.DEEPSLATE_TILES), 1, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(1))), false),
				new DeployDataEntry(new ItemStack(Blocks.DEEPSLATE_TILE_STAIRS), 1, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(2))), false),
				new DeployDataEntry(new ItemStack(Blocks.DEEPSLATE_TILE_SLAB), 1, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(1))), false),
				new DeployDataEntry(new ItemStack(Blocks.DEEPSLATE_TILE_WALL), 1, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(2))), false)
		));
		add("nether_brick", new DeployDataList(4,
				new DeployDataEntry(new ItemStack(Blocks.NETHER_BRICKS), 3, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(1), GristTypes.TAR.get().amount(1))), false),
				new DeployDataEntry(new ItemStack(Blocks.NETHER_BRICK_STAIRS), 3, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(2), GristTypes.TAR.get().amount(2))), false),
				new DeployDataEntry(new ItemStack(Blocks.NETHER_BRICK_SLAB), 3, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(1), GristTypes.TAR.get().amount(1))), false),
				new DeployDataEntry(new ItemStack(Blocks.NETHER_BRICK_WALL), 3, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(2), GristTypes.TAR.get().amount(2))), false),
				new DeployDataEntry(new ItemStack(Blocks.NETHER_BRICK_FENCE), 3, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(2), GristTypes.TAR.get().amount(2))), false),
				new DeployDataEntry(new ItemStack(Blocks.CHISELED_NETHER_BRICKS), 4, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(1), GristTypes.TAR.get().amount(1))), false)
		));
		add("glass", new DeployDataList(3,
				new DeployDataEntry(new ItemStack(Blocks.GLASS), 0, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(1))), false),
				new DeployDataEntry(new ItemStack(Blocks.WHITE_STAINED_GLASS), 2, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(2))), false),
				new DeployDataEntry(new ItemStack(Blocks.LIGHT_GRAY_STAINED_GLASS), 2, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(3))), false),
				new DeployDataEntry(new ItemStack(Blocks.GRAY_STAINED_GLASS), 2, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(3))), false),
				new DeployDataEntry(new ItemStack(Blocks.BLACK_STAINED_GLASS), 2, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(2))), false),
				new DeployDataEntry(new ItemStack(Blocks.BROWN_STAINED_GLASS), 2, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(3))), false),
				new DeployDataEntry(new ItemStack(Blocks.RED_STAINED_GLASS), 2, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(2))), false),
				new DeployDataEntry(new ItemStack(Blocks.ORANGE_STAINED_GLASS), 2, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(3))), false),
				new DeployDataEntry(new ItemStack(Blocks.YELLOW_STAINED_GLASS), 2, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(2))), false),
				new DeployDataEntry(new ItemStack(Blocks.LIME_STAINED_GLASS), 2, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(3))), false),
				new DeployDataEntry(new ItemStack(Blocks.GREEN_STAINED_GLASS), 2, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(2))), false),
				new DeployDataEntry(new ItemStack(Blocks.CYAN_STAINED_GLASS), 2, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(3))), false),
				new DeployDataEntry(new ItemStack(Blocks.LIGHT_BLUE_STAINED_GLASS), 2, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(3))), false),
				new DeployDataEntry(new ItemStack(Blocks.BLUE_STAINED_GLASS), 2, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(2))), false),
				new DeployDataEntry(new ItemStack(Blocks.PURPLE_STAINED_GLASS), 2, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(3))), false),
				new DeployDataEntry(new ItemStack(Blocks.MAGENTA_STAINED_GLASS), 2, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(4))), false),
				new DeployDataEntry(new ItemStack(Blocks.PINK_STAINED_GLASS), 2, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(3))), false)
		));
		add("glass_pane", new DeployDataList(2,
				new DeployDataEntry(new ItemStack(Blocks.GLASS_PANE), 0, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(1))), false),
				new DeployDataEntry(new ItemStack(Blocks.WHITE_STAINED_GLASS_PANE), 2, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(2))), false),
				new DeployDataEntry(new ItemStack(Blocks.LIGHT_GRAY_STAINED_GLASS_PANE), 2, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(3))), false),
				new DeployDataEntry(new ItemStack(Blocks.GRAY_STAINED_GLASS_PANE), 2, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(3))), false),
				new DeployDataEntry(new ItemStack(Blocks.BLACK_STAINED_GLASS_PANE), 2, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(2))), false),
				new DeployDataEntry(new ItemStack(Blocks.BROWN_STAINED_GLASS_PANE), 2, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(3))), false),
				new DeployDataEntry(new ItemStack(Blocks.RED_STAINED_GLASS_PANE), 2, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(2))), false),
				new DeployDataEntry(new ItemStack(Blocks.ORANGE_STAINED_GLASS_PANE), 2, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(3))), false),
				new DeployDataEntry(new ItemStack(Blocks.YELLOW_STAINED_GLASS_PANE), 2, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(2))), false),
				new DeployDataEntry(new ItemStack(Blocks.LIME_STAINED_GLASS_PANE), 2, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(3))), false),
				new DeployDataEntry(new ItemStack(Blocks.GREEN_STAINED_GLASS_PANE), 2, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(2))), false),
				new DeployDataEntry(new ItemStack(Blocks.CYAN_STAINED_GLASS_PANE), 2, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(3))), false),
				new DeployDataEntry(new ItemStack(Blocks.LIGHT_BLUE_STAINED_GLASS_PANE), 2, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(3))), false),
				new DeployDataEntry(new ItemStack(Blocks.BLUE_STAINED_GLASS_PANE), 2, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(2))), false),
				new DeployDataEntry(new ItemStack(Blocks.PURPLE_STAINED_GLASS_PANE), 2, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(3))), false),
				new DeployDataEntry(new ItemStack(Blocks.MAGENTA_STAINED_GLASS_PANE), 2, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(4))), false),
				new DeployDataEntry(new ItemStack(Blocks.PINK_STAINED_GLASS_PANE), 2, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(3))), false)
		));
		add("concrete", new DeployDataList(1,
				new DeployDataEntry(new ItemStack(Blocks.WHITE_CONCRETE), 2, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(1), GristTypes.COBALT.get().amount(1), GristTypes.CHALK.get().amount(1))), false),
				new DeployDataEntry(new ItemStack(Blocks.LIGHT_GRAY_CONCRETE), 2, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(1), GristTypes.COBALT.get().amount(1), GristTypes.CHALK.get().amount(1), GristTypes.TAR.get().amount(1))), false),
				new DeployDataEntry(new ItemStack(Blocks.GRAY_CONCRETE), 2, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(1), GristTypes.COBALT.get().amount(1), GristTypes.CHALK.get().amount(1), GristTypes.TAR.get().amount(1))), false),
				new DeployDataEntry(new ItemStack(Blocks.BLACK_CONCRETE), 2, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(1), GristTypes.COBALT.get().amount(1), GristTypes.TAR.get().amount(1))), false),
				new DeployDataEntry(new ItemStack(Blocks.BROWN_CONCRETE), 2, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(1), GristTypes.COBALT.get().amount(1), GristTypes.AMBER.get().amount(1), GristTypes.IODINE.get().amount(1))), false),
				new DeployDataEntry(new ItemStack(Blocks.RED_CONCRETE), 2, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(1), GristTypes.COBALT.get().amount(1), GristTypes.GARNET.get().amount(1))), false),
				new DeployDataEntry(new ItemStack(Blocks.ORANGE_CONCRETE), 2, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(1), GristTypes.COBALT.get().amount(1), GristTypes.AMBER.get().amount(1), GristTypes.GARNET.get().amount(1))), false),
				new DeployDataEntry(new ItemStack(Blocks.YELLOW_CONCRETE), 2, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(1), GristTypes.COBALT.get().amount(1), GristTypes.AMBER.get().amount(1))), false),
				new DeployDataEntry(new ItemStack(Blocks.LIME_CONCRETE), 2, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(1), GristTypes.COBALT.get().amount(1), GristTypes.CHALK.get().amount(1), GristTypes.AMBER.get().amount(1))), false),
				new DeployDataEntry(new ItemStack(Blocks.GREEN_CONCRETE), 2, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(1), GristTypes.COBALT.get().amount(1), GristTypes.AMBER.get().amount(1))), false),
				new DeployDataEntry(new ItemStack(Blocks.CYAN_CONCRETE), 2, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(1), GristTypes.COBALT.get().amount(1), GristTypes.AMBER.get().amount(1), GristTypes.AMETHYST.get().amount(1))), false),
				new DeployDataEntry(new ItemStack(Blocks.LIGHT_BLUE_CONCRETE), 2, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(1), GristTypes.COBALT.get().amount(1), GristTypes.CHALK.get().amount(1), GristTypes.AMETHYST.get().amount(1))), false),
				new DeployDataEntry(new ItemStack(Blocks.BLUE_CONCRETE), 2, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(1), GristTypes.COBALT.get().amount(1), GristTypes.AMETHYST.get().amount(1))), false),
				new DeployDataEntry(new ItemStack(Blocks.PURPLE_CONCRETE), 2, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(1), GristTypes.COBALT.get().amount(1), GristTypes.GARNET.get().amount(1), GristTypes.AMETHYST.get().amount(1))), false),
				new DeployDataEntry(new ItemStack(Blocks.MAGENTA_CONCRETE), 2, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(1), GristTypes.COBALT.get().amount(1), GristTypes.AMETHYST.get().amount(1), GristTypes.GARNET.get().amount(1))), false),
				new DeployDataEntry(new ItemStack(Blocks.PINK_CONCRETE), 2, EntryLists.ATHENEUM, new GristCost(GristSet.of(GristTypes.BUILD.get().amount(1), GristTypes.COBALT.get().amount(1), GristTypes.CHALK.get().amount(1), GristTypes.GARNET.get().amount(1))), false)
		));
	}
	
	protected void add(String name, DeployDataList entries)
	{
		add(ResourceLocation.fromNamespaceAndPath(modid, name), entries);
	}
	
	protected void add(ResourceLocation name, DeployDataList entries)
	{
		lists.put(name, entries);
	}
	
	@Override
	public CompletableFuture<?> run(CachedOutput cache)
	{
		registerEntries();
		
		Path outputPath = output.getOutputFolder();
		List<CompletableFuture<?>> futures = new ArrayList<>(lists.size());
		
		for(Map.Entry<ResourceLocation, DeployDataList> entry : lists.entrySet())
		{
			Path listPath = getPath(outputPath, entry.getKey());
			JsonElement jsonData = DeployDataList.CODEC.encodeStart(JsonOps.INSTANCE, entry.getValue()).getOrThrow();
			futures.add(DataProvider.saveStable(cache, jsonData, listPath));
		}
		
		return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
	}
	
	private static Path getPath(Path outputPath, ResourceLocation id)
	{
		return outputPath.resolve("data/" + id.getNamespace() + "/minestuck/deploy_list/" + id.getPath() + ".json");
	}
	
	@Override
	public String getName()
	{
		return "Atheneum Entries";
	}
}
