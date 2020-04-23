package com.mraof.minestuck.data.loot_table;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.entity.consort.EnumConsort;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.util.MSTags;
import com.mraof.minestuck.world.lands.LandTypes;
import com.mraof.minestuck.world.storage.loot.LandTableLootEntry;
import com.mraof.minestuck.world.storage.loot.MSLootTables;
import com.mraof.minestuck.world.storage.loot.conditions.ConsortLootCondition;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.*;
import net.minecraft.world.storage.loot.functions.SetCount;
import net.minecraft.world.storage.loot.functions.SetDamage;
import net.minecraft.world.storage.loot.functions.SetNBT;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.mraof.minestuck.data.loot_table.MSChestLootTables.locationFor;

public class MSGiftLootTables implements Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>
{
	private static final ResourceLocation COLD_CAKE = new ResourceLocation("minestuck", "gameplay/special/cold_cake");
	private static final ResourceLocation HOT_CAKE = new ResourceLocation("minestuck", "gameplay/special/hot_cake");
	
	//Pools in consort general stock
	public static final String ITEM_POOL = "item", BLOCK_POOL = "block";
	public static final String MAIN_POOL = "main", SPECIAL_POOL = "special";
	
	@Override
	public void accept(BiConsumer<ResourceLocation, LootTable.Builder> lootProcessor)
	{
		lootProcessor.accept(COLD_CAKE, LootTable.builder()
				.addLootPool(LootPool.builder().name("blue_cake").rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(MSBlocks.BLUE_CAKE).acceptFunction(SetCount.builder(RandomValueRange.of(1, 3)))))
				.addLootPool(LootPool.builder().name("cold_cake").rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(MSBlocks.COLD_CAKE).acceptFunction(SetCount.builder(RandomValueRange.of(1, 3))))));
		lootProcessor.accept(HOT_CAKE, LootTable.builder()
				.addLootPool(LootPool.builder().name("red_cake").rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(MSBlocks.RED_CAKE).acceptFunction(SetCount.builder(RandomValueRange.of(1, 3)))))
				.addLootPool(LootPool.builder().name("hot_cake").rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(MSBlocks.HOT_CAKE).acceptFunction(SetCount.builder(RandomValueRange.of(1, 3))))));
		
		lootProcessor.accept(MSLootTables.CONSORT_GENERAL_STOCK, LootTable.builder()
				.addLootPool(LootPool.builder().name(ITEM_POOL).rolls(ConstantRange.of(4))
						.addEntry(LandTableLootEntry.builder(MSLootTables.CONSORT_GENERAL_STOCK).setPool(ITEM_POOL))
						.addEntry(ItemLootEntry.builder(MSItems.CARVING_TOOL).weight(10).acceptFunction(SetCount.builder(RandomValueRange.of(1, 5))))
						.addEntry(ItemLootEntry.builder(MSBlocks.MINI_FROG_STATUE).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(1, 5))))
						.addEntry(ItemLootEntry.builder(MSItems.STONE_SLAB).weight(10).acceptFunction(SetCount.builder(RandomValueRange.of(1, 5))))
						.addEntry(ItemLootEntry.builder(MSItems.THRESH_DVD).weight(5).acceptFunction(SetCount.builder(RandomValueRange.of(1, 3))))
						.addEntry(ItemLootEntry.builder(MSItems.CREW_POSTER).weight(5).acceptFunction(SetCount.builder(RandomValueRange.of(1, 3))))
						.addEntry(ItemLootEntry.builder(MSItems.SBAHJ_POSTER).weight(5).acceptFunction(SetCount.builder(RandomValueRange.of(1, 3))))
						.addEntry(ItemLootEntry.builder(MSItems.GAMEBRO_MAGAZINE).weight(5).acceptCondition(ConsortLootCondition.builder(EnumConsort.SALAMANDER, EnumConsort.NAKAGATOR)))
						.addEntry(ItemLootEntry.builder(MSItems.GAMEGRL_MAGAZINE).weight(5).acceptCondition(ConsortLootCondition.builder(EnumConsort.TURTLE, EnumConsort.IGUANA)))
						.addEntry(ItemLootEntry.builder(MSItems.MUSIC_DISC_EMISSARY_OF_DANCE).weight(1))
						.addEntry(ItemLootEntry.builder(MSItems.MUSIC_DISC_DANCE_STAB_DANCE).weight(1))
						.addEntry(ItemLootEntry.builder(MSItems.BATTERY).weight(2).acceptFunction(SetCount.builder(RandomValueRange.of(1,4))))
						.addEntry(ItemLootEntry.builder(MSItems.GRIMOIRE).weight(1).acceptCondition(ConsortLootCondition.builder(EnumConsort.TURTLE)))
						.addEntry(ItemLootEntry.builder(MSItems.CRUMPLY_HAT).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(1, 5))).acceptCondition(ConsortLootCondition.builder(EnumConsort.SALAMANDER))))
				.addLootPool(LootPool.builder().name(BLOCK_POOL).rolls(ConstantRange.of(2))
						.addEntry(LandTableLootEntry.builder(MSLootTables.CONSORT_GENERAL_STOCK).setPool(BLOCK_POOL))));
		
		lootProcessor.accept(locationFor(LandTypes.FOREST, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.builder()
				.addLootPool(LootPool.builder().name(ITEM_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(MSItems.COPSE_CRUSHER).weight(3))
						.addEntry(ItemLootEntry.builder(Items.OAK_SAPLING).weight(2).acceptFunction(SetCount.builder(RandomValueRange.of(5, 10))))
						.addEntry(ItemLootEntry.builder(Items.SPRUCE_SAPLING).weight(2).acceptFunction(SetCount.builder(RandomValueRange.of(5, 10))))
						.addEntry(ItemLootEntry.builder(Items.BIRCH_SAPLING).weight(2).acceptFunction(SetCount.builder(RandomValueRange.of(5, 10))))
						.addEntry(ItemLootEntry.builder(Items.JUNGLE_SAPLING).weight(2).acceptFunction(SetCount.builder(RandomValueRange.of(5, 10))))
						.addEntry(ItemLootEntry.builder(Items.DARK_OAK_SAPLING).weight(2).acceptFunction(SetCount.builder(RandomValueRange.of(5, 10))))
						.addEntry(ItemLootEntry.builder(Items.ACACIA_SAPLING).weight(2).acceptFunction(SetCount.builder(RandomValueRange.of(5, 10))))
						.addEntry(ItemLootEntry.builder(Items.EMERALD).weight(5).acceptFunction(SetCount.builder(RandomValueRange.of(2, 4)))))
				.addLootPool(LootPool.builder().name(BLOCK_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(MSBlocks.VINE_LOG).weight(10).acceptFunction(SetCount.builder(RandomValueRange.of(8, 20))))
						.addEntry(ItemLootEntry.builder(MSBlocks.FLOWERY_VINE_LOG).weight(6).acceptFunction(SetCount.builder(RandomValueRange.of(8, 20))))
						.addEntry(ItemLootEntry.builder(Items.DARK_OAK_LOG).weight(6).acceptFunction(SetCount.builder(RandomValueRange.of(8, 20))))
						.addEntry(ItemLootEntry.builder(Items.OAK_PLANKS).weight(6).acceptFunction(SetCount.builder(RandomValueRange.of(8, 20))))));
		lootProcessor.accept(locationFor(LandTypes.TAIGA, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.builder()
				.addLootPool(LootPool.builder().name(ITEM_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(MSItems.COPSE_CRUSHER).weight(3))
						.addEntry(ItemLootEntry.builder(Items.OAK_SAPLING).weight(2).acceptFunction(SetCount.builder(RandomValueRange.of(5, 10))))
						.addEntry(ItemLootEntry.builder(Items.SPRUCE_SAPLING).weight(2).acceptFunction(SetCount.builder(RandomValueRange.of(5, 10))))
						.addEntry(ItemLootEntry.builder(Items.BIRCH_SAPLING).weight(2).acceptFunction(SetCount.builder(RandomValueRange.of(5, 10))))
						.addEntry(ItemLootEntry.builder(Items.JUNGLE_SAPLING).weight(2).acceptFunction(SetCount.builder(RandomValueRange.of(5, 10))))
						.addEntry(ItemLootEntry.builder(Items.DARK_OAK_SAPLING).weight(2).acceptFunction(SetCount.builder(RandomValueRange.of(5, 10))))
						.addEntry(ItemLootEntry.builder(Items.ACACIA_SAPLING).weight(2).acceptFunction(SetCount.builder(RandomValueRange.of(5, 10))))
						.addEntry(ItemLootEntry.builder(Items.EMERALD).weight(5).acceptFunction(SetCount.builder(RandomValueRange.of(2, 4)))))
				.addLootPool(LootPool.builder().name(BLOCK_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(Items.SPRUCE_LOG).weight(6).acceptFunction(SetCount.builder(RandomValueRange.of(8, 20))))
						.addEntry(ItemLootEntry.builder(Items.DARK_OAK_LOG).weight(6).acceptFunction(SetCount.builder(RandomValueRange.of(8, 20))))
						.addEntry(ItemLootEntry.builder(Items.OAK_PLANKS).weight(6).acceptFunction(SetCount.builder(RandomValueRange.of(8, 20))))));
		
		lootProcessor.accept(locationFor(LandTypes.FROST, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.builder()
				.addLootPool(LootPool.builder().name(ITEM_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(MSItems.KATANA).weight(3))
						.addEntry(ItemLootEntry.builder(Items.DIAMOND).weight(3).acceptFunction(SetCount.builder(RandomValueRange.of(2, 5))))
						.addEntry(ItemLootEntry.builder(Items.PRISMARINE_CRYSTALS).weight(6).acceptFunction(SetCount.builder(RandomValueRange.of(2, 5))))
						.addEntry(ItemLootEntry.builder(Items.PRISMARINE_SHARD).weight(10).acceptFunction(SetCount.builder(RandomValueRange.of(5, 10)))))
				.addLootPool(LootPool.builder().name(BLOCK_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(MSBlocks.FROST_BRICKS).weight(10).acceptFunction(SetCount.builder(RandomValueRange.of(8, 20))))
						.addEntry(ItemLootEntry.builder(MSBlocks.FROST_TILE).weight(6).acceptFunction(SetCount.builder(RandomValueRange.of(8, 20))))
						.addEntry(ItemLootEntry.builder(Items.PRISMARINE).weight(10).acceptFunction(SetCount.builder(RandomValueRange.of(8, 20))))
						.addEntry(ItemLootEntry.builder(Items.PRISMARINE_BRICKS).weight(6).acceptFunction(SetCount.builder(RandomValueRange.of(8, 20))))
						.addEntry(ItemLootEntry.builder(MSBlocks.FROST_LOG).weight(6).acceptFunction(SetCount.builder(RandomValueRange.of(8, 20))))));
		
		lootProcessor.accept(locationFor(LandTypes.FUNGI, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.builder()
				.addLootPool(LootPool.builder().name(ITEM_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(Items.REDSTONE).weight(10).acceptFunction(SetCount.builder(RandomValueRange.of(5, 10)))))
				.addLootPool(LootPool.builder().name(BLOCK_POOL).rolls(ConstantRange.of(1))));
		lootProcessor.accept(locationFor(LandTypes.END, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.builder()
				.addLootPool(LootPool.builder().name(ITEM_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(Items.ELYTRA).weight(3))
						.addEntry(ItemLootEntry.builder(Items.OBSIDIAN).weight(10).acceptFunction(SetCount.builder(RandomValueRange.of(5, 10))))
						.addEntry(ItemLootEntry.builder(Items.DRAGON_BREATH).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(2, 5)))))
				.addLootPool(LootPool.builder().name(BLOCK_POOL).rolls(ConstantRange.of(1))));
		
		lootProcessor.accept(locationFor(LandTypes.HEAT, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.builder()
				.addLootPool(LootPool.builder().name(ITEM_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(MSItems.FIRE_POKER).weight(3))
						.addEntry(ItemLootEntry.builder(Items.QUARTZ).weight(10).acceptFunction(SetCount.builder(RandomValueRange.of(5, 10))))
						.addEntry(ItemLootEntry.builder(Items.BLAZE_POWDER).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(2, 5))))
						.addEntry(ItemLootEntry.builder(Items.NETHER_BRICK).weight(10).acceptFunction(SetCount.builder(RandomValueRange.of(5, 10)))))
				.addLootPool(LootPool.builder().name(BLOCK_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(MSBlocks.CAST_IRON).weight(10).acceptFunction(SetCount.builder(RandomValueRange.of(8, 20))))
						.addEntry(ItemLootEntry.builder(MSBlocks.CHISELED_CAST_IRON).weight(6).acceptFunction(SetCount.builder(RandomValueRange.of(8, 20))))
						.addEntry(ItemLootEntry.builder(Items.NETHER_BRICKS).weight(10).acceptFunction(SetCount.builder(RandomValueRange.of(8, 20))))
						.addEntry(ItemLootEntry.builder(Items.RED_NETHER_BRICKS).weight(6).acceptFunction(SetCount.builder(RandomValueRange.of(8, 20))))));
		
		lootProcessor.accept(locationFor(LandTypes.ROCK, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.builder()
				.addLootPool(LootPool.builder().name(ITEM_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(MSItems.METAL_BAT).weight(3))
						.addEntry(ItemLootEntry.builder(Items.COAL).weight(10).acceptFunction(SetCount.builder(RandomValueRange.of(5, 10))))
						.addEntry(ItemLootEntry.builder(Items.CLAY_BALL).weight(10).acceptFunction(SetCount.builder(RandomValueRange.of(5, 10))))
						.addEntry(ItemLootEntry.builder(Items.IRON_INGOT).weight(10).acceptFunction(SetCount.builder(RandomValueRange.of(5, 10)))))
				.addLootPool(LootPool.builder().name(BLOCK_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(MSBlocks.COARSE_STONE).weight(10).acceptFunction(SetCount.builder(RandomValueRange.of(8, 20))))
						.addEntry(ItemLootEntry.builder(MSBlocks.CHISELED_COARSE_STONE).weight(6).acceptFunction(SetCount.builder(RandomValueRange.of(8, 20))))
						.addEntry(ItemLootEntry.builder(Items.POLISHED_ANDESITE).weight(10).acceptFunction(SetCount.builder(RandomValueRange.of(8, 20))))
						.addEntry(ItemLootEntry.builder(Items.POLISHED_GRANITE).weight(6).acceptFunction(SetCount.builder(RandomValueRange.of(8, 20))))));
		lootProcessor.accept(locationFor(LandTypes.PETRIFICATION, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.builder()
				.addLootPool(LootPool.builder().name(ITEM_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(MSItems.METAL_BAT).weight(3))
						.addEntry(ItemLootEntry.builder(Items.COAL).weight(10).acceptFunction(SetCount.builder(RandomValueRange.of(5, 10))))
						.addEntry(ItemLootEntry.builder(Items.CLAY_BALL).weight(10).acceptFunction(SetCount.builder(RandomValueRange.of(5, 10))))
						.addEntry(ItemLootEntry.builder(Items.IRON_INGOT).weight(10).acceptFunction(SetCount.builder(RandomValueRange.of(5, 10)))))
				.addLootPool(LootPool.builder().name(BLOCK_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(MSBlocks.COARSE_STONE).weight(10).acceptFunction(SetCount.builder(RandomValueRange.of(8, 20))))
						.addEntry(ItemLootEntry.builder(MSBlocks.CHISELED_COARSE_STONE).weight(6).acceptFunction(SetCount.builder(RandomValueRange.of(8, 20))))
						.addEntry(ItemLootEntry.builder(Items.POLISHED_ANDESITE).weight(10).acceptFunction(SetCount.builder(RandomValueRange.of(8, 20))))
						.addEntry(ItemLootEntry.builder(Items.POLISHED_GRANITE).weight(6).acceptFunction(SetCount.builder(RandomValueRange.of(8, 20))))));
		
		lootProcessor.accept(locationFor(LandTypes.SAND, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.builder()
				.addLootPool(LootPool.builder().name(ITEM_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(MSItems.CACTACEAE_CUTLASS).weight(3))
						.addEntry(ItemLootEntry.builder(Items.GOLD_INGOT).weight(7).acceptFunction(SetCount.builder(RandomValueRange.of(5, 10))))
						.addEntry(ItemLootEntry.builder(Items.LEATHER).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(2, 5)))))
				.addLootPool(LootPool.builder().name(BLOCK_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(Items.CACTUS).weight(6).acceptFunction(SetCount.builder(RandomValueRange.of(8, 20))))
						.addEntry(ItemLootEntry.builder(MSBlocks.WOODEN_CACTUS).weight(3).acceptFunction(SetCount.builder(RandomValueRange.of(8, 20))))
						.addEntry(ItemLootEntry.builder(Items.SANDSTONE).weight(10).acceptFunction(SetCount.builder(RandomValueRange.of(8, 20))))
						.addEntry(ItemLootEntry.builder(Items.SMOOTH_SANDSTONE).weight(10).acceptFunction(SetCount.builder(RandomValueRange.of(8, 20))))));
		lootProcessor.accept(locationFor(LandTypes.RED_SAND, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.builder()
				.addLootPool(LootPool.builder().name(ITEM_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(MSItems.CACTACEAE_CUTLASS).weight(3))
						.addEntry(ItemLootEntry.builder(Items.GOLD_INGOT).weight(7).acceptFunction(SetCount.builder(RandomValueRange.of(5, 10))))
						.addEntry(ItemLootEntry.builder(Items.LEATHER).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(2, 5)))))
				.addLootPool(LootPool.builder().name(BLOCK_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(Items.CACTUS).weight(6).acceptFunction(SetCount.builder(RandomValueRange.of(8, 20))))
						.addEntry(ItemLootEntry.builder(MSBlocks.WOODEN_CACTUS).weight(3).acceptFunction(SetCount.builder(RandomValueRange.of(8, 20))))
						.addEntry(ItemLootEntry.builder(Items.RED_SANDSTONE).weight(10).acceptFunction(SetCount.builder(RandomValueRange.of(8, 20))))
						.addEntry(ItemLootEntry.builder(Items.SMOOTH_RED_SANDSTONE).weight(10).acceptFunction(SetCount.builder(RandomValueRange.of(8, 20))))));
		lootProcessor.accept(locationFor(LandTypes.LUSH_DESERTS, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.builder()
				.addLootPool(LootPool.builder().name(ITEM_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(MSItems.CACTACEAE_CUTLASS).weight(3))
						.addEntry(ItemLootEntry.builder(Items.GOLD_INGOT).weight(7).acceptFunction(SetCount.builder(RandomValueRange.of(5, 10))))
						.addEntry(ItemLootEntry.builder(Items.LEATHER).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(2, 5)))))
				.addLootPool(LootPool.builder().name(BLOCK_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(Items.CACTUS).weight(6).acceptFunction(SetCount.builder(RandomValueRange.of(8, 20))))
						.addEntry(ItemLootEntry.builder(MSBlocks.WOODEN_CACTUS).weight(3).acceptFunction(SetCount.builder(RandomValueRange.of(8, 20))))
						.addEntry(ItemLootEntry.builder(Items.SANDSTONE).weight(10).acceptFunction(SetCount.builder(RandomValueRange.of(8, 20))))
						.addEntry(ItemLootEntry.builder(Items.SMOOTH_SANDSTONE).weight(10).acceptFunction(SetCount.builder(RandomValueRange.of(8, 20))))));
		
		lootProcessor.accept(locationFor(LandTypes.SANDSTONE, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.builder()
				.addLootPool(LootPool.builder().name(ITEM_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(Items.GOLDEN_SWORD).weight(3))
						.addEntry(ItemLootEntry.builder(Items.REDSTONE).weight(10).acceptFunction(SetCount.builder(RandomValueRange.of(5, 10))))
						.addEntry(ItemLootEntry.builder(Items.GUNPOWDER).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(2, 5)))))
				.addLootPool(LootPool.builder().name(BLOCK_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(Items.SMOOTH_SANDSTONE).weight(10).acceptFunction(SetCount.builder(RandomValueRange.of(8, 20))))
						.addEntry(ItemLootEntry.builder(Items.CHISELED_SANDSTONE).weight(6).acceptFunction(SetCount.builder(RandomValueRange.of(8, 20))))
						.addEntry(ItemLootEntry.builder(Items.STONE_BRICKS).weight(10).acceptFunction(SetCount.builder(RandomValueRange.of(8, 20))))
						.addEntry(ItemLootEntry.builder(Items.CHISELED_STONE_BRICKS).weight(6).acceptFunction(SetCount.builder(RandomValueRange.of(8, 20))))));
		lootProcessor.accept(locationFor(LandTypes.RED_SANDSTONE, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.builder()
				.addLootPool(LootPool.builder().name(ITEM_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(Items.GOLDEN_SWORD).weight(3))
						.addEntry(ItemLootEntry.builder(Items.REDSTONE).weight(10).acceptFunction(SetCount.builder(RandomValueRange.of(5, 10))))
						.addEntry(ItemLootEntry.builder(Items.GUNPOWDER).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(2, 5)))))
				.addLootPool(LootPool.builder().name(BLOCK_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(Items.SMOOTH_RED_SANDSTONE).weight(10).acceptFunction(SetCount.builder(RandomValueRange.of(8, 20))))
						.addEntry(ItemLootEntry.builder(Items.CHISELED_RED_SANDSTONE).weight(6).acceptFunction(SetCount.builder(RandomValueRange.of(8, 20))))
						.addEntry(ItemLootEntry.builder(Items.STONE_BRICKS).weight(10).acceptFunction(SetCount.builder(RandomValueRange.of(8, 20))))
						.addEntry(ItemLootEntry.builder(Items.CHISELED_STONE_BRICKS).weight(6).acceptFunction(SetCount.builder(RandomValueRange.of(8, 20))))));
		
		lootProcessor.accept(locationFor(LandTypes.SHADE, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.builder()
				.addLootPool(LootPool.builder().name(ITEM_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(MSItems.POGO_CLUB).weight(3))
						.addEntry(ItemLootEntry.builder(Items.LAPIS_LAZULI).weight(10).acceptFunction(SetCount.builder(RandomValueRange.of(5, 10))))
						.addEntry(ItemLootEntry.builder(Items.FEATHER).weight(6).acceptFunction(SetCount.builder(RandomValueRange.of(5, 10))))
						.addEntry(ItemLootEntry.builder(Items.FLINT).weight(6).acceptFunction(SetCount.builder(RandomValueRange.of(5, 10)))))
				.addLootPool(LootPool.builder().name(BLOCK_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(MSBlocks.GLOWING_LOG).weight(10).acceptFunction(SetCount.builder(RandomValueRange.of(8, 20))))
						.addEntry(ItemLootEntry.builder(MSBlocks.GLOWING_PLANKS).weight(6).acceptFunction(SetCount.builder(RandomValueRange.of(8, 20))))
						.addEntry(ItemLootEntry.builder(MSBlocks.SHADE_BRICKS).weight(10).acceptFunction(SetCount.builder(RandomValueRange.of(8, 20))))
						.addEntry(ItemLootEntry.builder(MSBlocks.SMOOTH_SHADE_STONE).weight(6).acceptFunction(SetCount.builder(RandomValueRange.of(8, 20))))));
		
		lootProcessor.accept(locationFor(LandTypes.WOOD, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.builder()
				.addLootPool(LootPool.builder().name(ITEM_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(Items.STONE_AXE).weight(3))
						.addEntry(ItemLootEntry.builder(Items.EMERALD).weight(5).acceptFunction(SetCount.builder(RandomValueRange.of(2, 4))))
						.addEntry(ItemLootEntry.builder(Items.SLIME_BALL).weight(6).acceptFunction(SetCount.builder(RandomValueRange.of(2, 5))))
						.addEntry(ItemLootEntry.builder(Items.CHARCOAL).weight(10).acceptFunction(SetCount.builder(RandomValueRange.of(5, 10)))))
				.addLootPool(LootPool.builder().name(BLOCK_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(Items.OAK_LOG).weight(10).acceptFunction(SetCount.builder(RandomValueRange.of(8, 20))))
						.addEntry(ItemLootEntry.builder(Items.SPRUCE_LOG).weight(6).acceptFunction(SetCount.builder(RandomValueRange.of(8, 20))))
						.addEntry(ItemLootEntry.builder(Items.BIRCH_LOG).weight(6).acceptFunction(SetCount.builder(RandomValueRange.of(8, 20))))
						.addEntry(ItemLootEntry.builder(Items.JUNGLE_LOG).weight(10).acceptFunction(SetCount.builder(RandomValueRange.of(8, 20))))));
		
		lootProcessor.accept(locationFor(LandTypes.RAINBOW, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.builder()
				.addLootPool(LootPool.builder().name(ITEM_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(Items.CLAY_BALL).weight(10).acceptFunction(SetCount.builder(RandomValueRange.of(5, 10))))
						.addEntry(ItemLootEntry.builder(Items.PAPER).weight(10).acceptFunction(SetCount.builder(RandomValueRange.of(5, 10))))
						.addEntry(ItemLootEntry.builder(Items.EGG).weight(3).acceptFunction(SetCount.builder(RandomValueRange.of(1, 3)))))
				.addLootPool(LootPool.builder().name(BLOCK_POOL).rolls(ConstantRange.of(1))));
		
		lootProcessor.accept(locationFor(LandTypes.FLORA, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.builder()
				.addLootPool(LootPool.builder().name(ITEM_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(Items.EMERALD).weight(5).acceptFunction(SetCount.builder(RandomValueRange.of(2, 4))))
						.addEntry(ItemLootEntry.builder(Items.QUARTZ).weight(10).acceptFunction(SetCount.builder(RandomValueRange.of(5, 10))))
						.addEntry(ItemLootEntry.builder(Items.PRISMARINE_CRYSTALS).weight(6).acceptFunction(SetCount.builder(RandomValueRange.of(2, 5)))))
				.addLootPool(LootPool.builder().name(BLOCK_POOL).rolls(ConstantRange.of(1))));
		
		lootProcessor.accept(locationFor(LandTypes.FROGS, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.builder()
				.addLootPool(LootPool.builder().name(ITEM_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(MSItems.BUG_NET).weight(5)))
				.addLootPool(LootPool.builder().name(BLOCK_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(Items.LILY_PAD).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(2, 5))))));
		
		lootProcessor.accept(locationFor(LandTypes.WIND, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.builder()
				.addLootPool(LootPool.builder().name(ITEM_POOL).rolls(ConstantRange.of(1)))
				.addLootPool(LootPool.builder().name(BLOCK_POOL).rolls(ConstantRange.of(1))));
		
		lootProcessor.accept(locationFor(LandTypes.LIGHT, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.builder()
				.addLootPool(LootPool.builder().name(ITEM_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(MSBlocks.GLOWYSTONE_DUST).weight(6).acceptFunction(SetCount.builder(RandomValueRange.of(2, 5)))))
				.addLootPool(LootPool.builder().name(BLOCK_POOL).rolls(ConstantRange.of(1))));
		
		lootProcessor.accept(locationFor(LandTypes.CLOCKWORK, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.builder()
				.addLootPool(LootPool.builder().name(ITEM_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(Items.CLOCK).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(2, 5)))))
				.addLootPool(LootPool.builder().name(BLOCK_POOL).rolls(ConstantRange.of(1))));
		
		lootProcessor.accept(locationFor(LandTypes.SILENCE, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.builder()
				.addLootPool(LootPool.builder().name(ITEM_POOL).rolls(ConstantRange.of(1)))
				.addLootPool(LootPool.builder().name(BLOCK_POOL).rolls(ConstantRange.of(1))));
		
		lootProcessor.accept(locationFor(LandTypes.THUNDER, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.builder()
				.addLootPool(LootPool.builder().name(ITEM_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(MSItems.IRON_CANE).weight(3)))
				.addLootPool(LootPool.builder().name(BLOCK_POOL).rolls(ConstantRange.of(1))));
		
		lootProcessor.accept(locationFor(LandTypes.PULSE, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.builder()
				.addLootPool(LootPool.builder().name(ITEM_POOL).rolls(ConstantRange.of(1)))
				.addLootPool(LootPool.builder().name(BLOCK_POOL).rolls(ConstantRange.of(1))));
		
		lootProcessor.accept(locationFor(LandTypes.THOUGHT, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.builder()
				.addLootPool(LootPool.builder().name(ITEM_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(Items.BOOK).weight(10).acceptFunction(SetCount.builder(RandomValueRange.of(2, 5)))))
				.addLootPool(LootPool.builder().name(BLOCK_POOL).rolls(ConstantRange.of(1))));
		
		lootProcessor.accept(locationFor(LandTypes.BUCKETS, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.builder()
				.addLootPool(LootPool.builder().name(ITEM_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(Items.BUCKET).weight(10).acceptFunction(SetCount.builder(RandomValueRange.of(2, 5)))))
				.addLootPool(LootPool.builder().name(BLOCK_POOL).rolls(ConstantRange.of(1))));
		
		lootProcessor.accept(locationFor(LandTypes.CAKE, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.builder()
				.addLootPool(LootPool.builder().name(ITEM_POOL).rolls(ConstantRange.of(1)))
				.addLootPool(LootPool.builder().name(BLOCK_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(MSBlocks.SUGAR_CUBE).weight(6).acceptFunction(SetCount.builder(RandomValueRange.of(5, 10))))
						.addEntry(ItemLootEntry.builder(MSBlocks.FUCHSIA_CAKE).weight(2).acceptFunction(SetCount.builder(RandomValueRange.of(1, 3))))));
		
		lootProcessor.accept(locationFor(LandTypes.RABBITS, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.builder()
				.addLootPool(LootPool.builder().name(ITEM_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(Items.RABBIT_FOOT).weight(5).acceptFunction(SetCount.builder(RandomValueRange.of(2, 5)))))
				.addLootPool(LootPool.builder().name(BLOCK_POOL).rolls(ConstantRange.of(1))));
		
		lootProcessor.accept(locationFor(LandTypes.MONSTERS, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.builder()
				.addLootPool(LootPool.builder().name(ITEM_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(Items.ROTTEN_FLESH).weight(10).acceptFunction(SetCount.builder(RandomValueRange.of(5, 10)))))
				.addLootPool(LootPool.builder().name(BLOCK_POOL).rolls(ConstantRange.of(1))));
		lootProcessor.accept(locationFor(LandTypes.UNDEAD, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.builder()
				.addLootPool(LootPool.builder().name(ITEM_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(Items.ROTTEN_FLESH).weight(10).acceptFunction(SetCount.builder(RandomValueRange.of(5, 10)))))
				.addLootPool(LootPool.builder().name(BLOCK_POOL).rolls(ConstantRange.of(1))));
		
		lootProcessor.accept(locationFor(LandTypes.TOWERS, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.builder()
				.addLootPool(LootPool.builder().name(ITEM_POOL).rolls(ConstantRange.of(1)))
				.addLootPool(LootPool.builder().name(BLOCK_POOL).rolls(ConstantRange.of(1))));
		
		
		lootProcessor.accept(MSLootTables.CONSORT_FOOD_STOCK, LootTable.builder()
				.addLootPool(LootPool.builder().name(MAIN_POOL).rolls(ConstantRange.of(5))
						.addEntry(LandTableLootEntry.builder(MSLootTables.CONSORT_FOOD_STOCK).setPool(MAIN_POOL))
						.addEntry(ItemLootEntry.builder(MSItems.GRASSHOPPER).weight(5).acceptFunction(SetCount.builder(RandomValueRange.of(3, 10)))))
				.addLootPool(LootPool.builder().name(SPECIAL_POOL).rolls(RandomValueRange.of(1, 2))
						.addEntry(LandTableLootEntry.builder(MSLootTables.CONSORT_FOOD_STOCK).setPool(SPECIAL_POOL))
						.addEntry(ItemLootEntry.builder(Items.PUMPKIN_PIE).weight(3).acceptFunction(SetCount.builder(RandomValueRange.of(1, 2))))
						.addEntry(ItemLootEntry.builder(Items.GOLDEN_APPLE).weight(1))
						.addEntry(ItemLootEntry.builder(MSItems.CANDY_CORN).weight(1).acceptFunction(SetCount.builder(RandomValueRange.of(1, 4))))
						.addEntry(TagLootEntry.func_216176_b(MSTags.Items.GRIST_CANDY).weight(1).acceptFunction(SetCount.builder(RandomValueRange.of(1, 4))))));
		
		//Iguana
		lootProcessor.accept(locationFor(LandTypes.FOREST, MSLootTables.CONSORT_FOOD_STOCK), LootTable.builder()
				.addLootPool(LootPool.builder().name(MAIN_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(MSItems.ONION).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(3, 10))))
						.addEntry(ItemLootEntry.builder(MSItems.CONE_OF_FLIES).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(3, 10))))
						.addEntry(ItemLootEntry.builder(Items.APPLE).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(3, 10))))
						.addEntry(ItemLootEntry.builder(MSItems.CHOCOLATE_BEETLE).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(3, 10))))
						.addEntry(ItemLootEntry.builder(Items.WHEAT).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(3, 10))))
						.addEntry(ItemLootEntry.builder(Items.WHEAT_SEEDS).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(3, 10))))
						.addEntry(ItemLootEntry.builder(Items.COCOA_BEANS).weight(2).acceptFunction(SetCount.builder(RandomValueRange.of(3, 6)))))
				.addLootPool(LootPool.builder().name(SPECIAL_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(Items.COOKIE).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(1, 3))))
						.addEntry(ItemLootEntry.builder(MSBlocks.APPLE_CAKE).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(1, 3))))));
		lootProcessor.accept(locationFor(LandTypes.TAIGA, MSLootTables.CONSORT_FOOD_STOCK), LootTable.builder()
				.addLootPool(LootPool.builder().name(MAIN_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(MSItems.ONION).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(3, 10))))
						.addEntry(ItemLootEntry.builder(MSItems.CONE_OF_FLIES).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(3, 10))))
						.addEntry(ItemLootEntry.builder(Items.APPLE).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(3, 10))))
						.addEntry(ItemLootEntry.builder(MSItems.CHOCOLATE_BEETLE).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(3, 10))))
						.addEntry(ItemLootEntry.builder(Items.WHEAT).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(3, 10))))
						.addEntry(ItemLootEntry.builder(Items.WHEAT_SEEDS).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(3, 10))))
						.addEntry(ItemLootEntry.builder(Items.COCOA_BEANS).weight(2).acceptFunction(SetCount.builder(RandomValueRange.of(3, 6)))))
				.addLootPool(LootPool.builder().name(SPECIAL_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(Items.COOKIE).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(1, 3))))
						.addEntry(ItemLootEntry.builder(MSBlocks.APPLE_CAKE).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(1, 3))))));
		lootProcessor.accept(locationFor(LandTypes.FROST, MSLootTables.CONSORT_FOOD_STOCK), LootTable.builder()
				.addLootPool(LootPool.builder().name(MAIN_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(MSItems.ONION).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(3, 10))))
						.addEntry(ItemLootEntry.builder(MSItems.CONE_OF_FLIES).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(3, 10))))
						.addEntry(ItemLootEntry.builder(Items.APPLE).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(3, 10))))
						.addEntry(ItemLootEntry.builder(MSItems.CHOCOLATE_BEETLE).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(3, 10))))
						.addEntry(ItemLootEntry.builder(Items.WHEAT).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(3, 10))))
						.addEntry(ItemLootEntry.builder(Items.WHEAT_SEEDS).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(3, 10))))
						.addEntry(TableLootEntry.builder(COLD_CAKE).weight(2)))
				.addLootPool(LootPool.builder().name(SPECIAL_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(Items.COOKIE).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(1, 3))))
						.addEntry(ItemLootEntry.builder(MSBlocks.APPLE_CAKE).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(1, 3))))));
		lootProcessor.accept(locationFor(LandTypes.FLORA, MSLootTables.CONSORT_FOOD_STOCK), LootTable.builder()
				.addLootPool(LootPool.builder().name(MAIN_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(MSItems.ONION).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(3, 10))))
						.addEntry(ItemLootEntry.builder(MSItems.CONE_OF_FLIES).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(3, 10))))
						.addEntry(ItemLootEntry.builder(Items.APPLE).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(3, 10))))
						.addEntry(ItemLootEntry.builder(MSItems.CHOCOLATE_BEETLE).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(3, 10))))
						.addEntry(ItemLootEntry.builder(Items.WHEAT).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(3, 10))))
						.addEntry(ItemLootEntry.builder(Items.WHEAT_SEEDS).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(3, 10))))
						.addEntry(ItemLootEntry.builder(MSItems.STRAWBERRY_CHUNK).weight(5).acceptFunction(SetCount.builder(RandomValueRange.of(8, 12)))))
				.addLootPool(LootPool.builder().name(SPECIAL_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(Items.COOKIE).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(1, 3))))
						.addEntry(ItemLootEntry.builder(MSBlocks.APPLE_CAKE).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(1, 3))))));
		
		//Salamander
		lootProcessor.accept(locationFor(LandTypes.FUNGI, MSLootTables.CONSORT_FOOD_STOCK), LootTable.builder()
				.addLootPool(LootPool.builder().name(MAIN_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(MSItems.JAR_OF_BUGS).weight(5).acceptFunction(SetCount.builder(RandomValueRange.of(3, 10))))
						.addEntry(ItemLootEntry.builder(MSItems.BUG_ON_A_STICK).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(3, 10))))
						.addEntry(ItemLootEntry.builder(MSItems.CONE_OF_FLIES).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(3, 10))))
						.addEntry(ItemLootEntry.builder(Items.POTATO).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(3, 10))))
						.addEntry(ItemLootEntry.builder(Items.MUSHROOM_STEW).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(3, 10))))
						.addEntry(ItemLootEntry.builder(Items.RED_MUSHROOM).weight(4).acceptFunction(SetCount.builder(RandomValueRange.of(3, 6))))
						.addEntry(ItemLootEntry.builder(Items.BROWN_MUSHROOM).weight(4).acceptFunction(SetCount.builder(RandomValueRange.of(3, 6))))
						.addEntry(ItemLootEntry.builder(MSItems.FUNGAL_SPORE).weight(10).acceptFunction(SetCount.builder(RandomValueRange.of(8, 12))))
						.addEntry(ItemLootEntry.builder(MSItems.SPOREO).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(1, 4)))))
				.addLootPool(LootPool.builder().name(SPECIAL_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(Items.POISONOUS_POTATO).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(1, 3))))
						.addEntry(ItemLootEntry.builder(Items.MELON).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(3, 6))))));
		lootProcessor.accept(locationFor(LandTypes.SHADE, MSLootTables.CONSORT_FOOD_STOCK), LootTable.builder()
				.addLootPool(LootPool.builder().name(MAIN_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(MSItems.JAR_OF_BUGS).weight(5).acceptFunction(SetCount.builder(RandomValueRange.of(3, 10))))
						.addEntry(ItemLootEntry.builder(MSItems.BUG_ON_A_STICK).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(3, 10))))
						.addEntry(ItemLootEntry.builder(MSItems.CONE_OF_FLIES).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(3, 10))))
						.addEntry(ItemLootEntry.builder(Items.POTATO).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(3, 10))))
						.addEntry(ItemLootEntry.builder(Items.MUSHROOM_STEW).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(3, 10))))
						.addEntry(ItemLootEntry.builder(MSBlocks.GLOWING_MUSHROOM).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(3, 6)))))
				.addLootPool(LootPool.builder().name(SPECIAL_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(Items.POISONOUS_POTATO).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(1, 3))))
						.addEntry(ItemLootEntry.builder(Items.MELON).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(3, 6))))));
		lootProcessor.accept(locationFor(LandTypes.WOOD, MSLootTables.CONSORT_FOOD_STOCK), LootTable.builder()
				.addLootPool(LootPool.builder().name(MAIN_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(MSItems.JAR_OF_BUGS).weight(5).acceptFunction(SetCount.builder(RandomValueRange.of(3, 10))))
						.addEntry(ItemLootEntry.builder(MSItems.BUG_ON_A_STICK).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(3, 10))))
						.addEntry(ItemLootEntry.builder(MSItems.CONE_OF_FLIES).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(3, 10))))
						.addEntry(ItemLootEntry.builder(Items.POTATO).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(3, 10))))
						.addEntry(ItemLootEntry.builder(Items.MUSHROOM_STEW).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(3, 10))))
						.addEntry(ItemLootEntry.builder(Items.RED_MUSHROOM).weight(4).acceptFunction(SetCount.builder(RandomValueRange.of(3, 6))))
						.addEntry(ItemLootEntry.builder(Items.BROWN_MUSHROOM).weight(4).acceptFunction(SetCount.builder(RandomValueRange.of(3, 6)))))
				.addLootPool(LootPool.builder().name(SPECIAL_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(Items.POISONOUS_POTATO).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(1, 3))))
						.addEntry(ItemLootEntry.builder(Items.MELON).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(3, 6))))));
		
		//Nakagator
		lootProcessor.accept(locationFor(LandTypes.HEAT, MSLootTables.CONSORT_FOOD_STOCK), LootTable.builder()
				.addLootPool(LootPool.builder().name(MAIN_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(MSItems.ONION).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(3, 10))))
						.addEntry(ItemLootEntry.builder(MSItems.CHOCOLATE_BEETLE).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(3, 10))))
						.addEntry(ItemLootEntry.builder(Items.BEETROOT_SOUP).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(3, 10))))
						.addEntry(ItemLootEntry.builder(Items.BEEF).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(3, 10))))
						.addEntry(ItemLootEntry.builder(MSItems.STEAK_SWORD).weight(3))
						.addEntry(ItemLootEntry.builder(Items.BEETROOT).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(3, 10))))
						.addEntry(TableLootEntry.builder(HOT_CAKE).weight(2)))
				.addLootPool(LootPool.builder().name(SPECIAL_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(MSItems.TAB).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(3, 6))))
						.addEntry(ItemLootEntry.builder(MSItems.IRRADIATED_STEAK).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(1, 3))))));
		lootProcessor.accept(locationFor(LandTypes.ROCK, MSLootTables.CONSORT_FOOD_STOCK), LootTable.builder()
				.addLootPool(LootPool.builder().name(MAIN_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(MSItems.ONION).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(3, 10))))
						.addEntry(ItemLootEntry.builder(MSItems.CHOCOLATE_BEETLE).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(3, 10))))
						.addEntry(ItemLootEntry.builder(Items.BEETROOT_SOUP).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(3, 10))))
						.addEntry(ItemLootEntry.builder(Items.BEEF).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(3, 10))))
						.addEntry(ItemLootEntry.builder(MSItems.STEAK_SWORD).weight(3))
						.addEntry(ItemLootEntry.builder(Items.BEETROOT).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(3, 10))))
						.addEntry(ItemLootEntry.builder(MSItems.ROCK_COOKIE).weight(5).acceptFunction(SetCount.builder(RandomValueRange.of(8, 12)))))
				.addLootPool(LootPool.builder().name(SPECIAL_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(MSItems.TAB).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(3, 6))))
						.addEntry(ItemLootEntry.builder(MSItems.IRRADIATED_STEAK).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(1, 3))))));
		lootProcessor.accept(locationFor(LandTypes.PETRIFICATION, MSLootTables.CONSORT_FOOD_STOCK), LootTable.builder()
				.addLootPool(LootPool.builder().name(MAIN_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(MSItems.ONION).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(3, 10))))
						.addEntry(ItemLootEntry.builder(MSItems.CHOCOLATE_BEETLE).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(3, 10))))
						.addEntry(ItemLootEntry.builder(Items.BEETROOT_SOUP).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(3, 10))))
						.addEntry(ItemLootEntry.builder(Items.BEEF).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(3, 10))))
						.addEntry(ItemLootEntry.builder(MSItems.STEAK_SWORD).weight(3))
						.addEntry(ItemLootEntry.builder(Items.BEETROOT).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(3, 10))))
						.addEntry(ItemLootEntry.builder(MSItems.ROCK_COOKIE).weight(5).acceptFunction(SetCount.builder(RandomValueRange.of(8, 12)))))
				.addLootPool(LootPool.builder().name(SPECIAL_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(MSItems.TAB).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(3, 6))))
						.addEntry(ItemLootEntry.builder(MSItems.IRRADIATED_STEAK).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(1, 3))))));
		lootProcessor.accept(locationFor(LandTypes.END, MSLootTables.CONSORT_FOOD_STOCK), LootTable.builder()
				.addLootPool(LootPool.builder().name(MAIN_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(MSItems.ONION).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(3, 10))))
						.addEntry(ItemLootEntry.builder(MSItems.CHOCOLATE_BEETLE).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(3, 10))))
						.addEntry(ItemLootEntry.builder(Items.BEETROOT_SOUP).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(3, 10))))
						.addEntry(ItemLootEntry.builder(Items.BEEF).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(3, 10))))
						.addEntry(ItemLootEntry.builder(MSItems.STEAK_SWORD).weight(3))
						.addEntry(ItemLootEntry.builder(Items.BEETROOT).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(3, 10))))
						.addEntry(ItemLootEntry.builder(Items.CHORUS_FRUIT).weight(5).acceptFunction(SetCount.builder(RandomValueRange.of(8, 12)))))
				.addLootPool(LootPool.builder().name(SPECIAL_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(MSItems.TAB).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(3, 6))))
						.addEntry(ItemLootEntry.builder(MSItems.IRRADIATED_STEAK).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(1, 3))))));
		
		//Turtle
		lootProcessor.accept(locationFor(LandTypes.SAND, MSLootTables.CONSORT_FOOD_STOCK), LootTable.builder()
				.addLootPool(LootPool.builder().name(MAIN_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(MSItems.JAR_OF_BUGS).weight(5).acceptFunction(SetCount.builder(RandomValueRange.of(3, 10))))
						.addEntry(ItemLootEntry.builder(MSItems.BUG_ON_A_STICK).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(3, 10))))
						.addEntry(ItemLootEntry.builder(Items.POTATO).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(3, 10))))
						.addEntry(ItemLootEntry.builder(MSItems.SALAD).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(3, 10))))
						.addEntry(ItemLootEntry.builder(Items.CARROT).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(3, 10))))
						.addEntry(ItemLootEntry.builder(Items.POTION).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(3, 6))).acceptFunction(SetNBT.func_215952_a(waterNBT())))
						.addEntry(ItemLootEntry.builder(MSItems.DESERT_FRUIT).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(15, 20)))))
				.addLootPool(LootPool.builder().name(SPECIAL_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(MSBlocks.GOLD_SEEDS).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(1, 3))))
						.addEntry(ItemLootEntry.builder(Items.COD).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(1, 3))))));
		lootProcessor.accept(locationFor(LandTypes.RED_SAND, MSLootTables.CONSORT_FOOD_STOCK), LootTable.builder()
				.addLootPool(LootPool.builder().name(MAIN_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(MSItems.JAR_OF_BUGS).weight(5).acceptFunction(SetCount.builder(RandomValueRange.of(3, 10))))
						.addEntry(ItemLootEntry.builder(MSItems.BUG_ON_A_STICK).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(3, 10))))
						.addEntry(ItemLootEntry.builder(Items.POTATO).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(3, 10))))
						.addEntry(ItemLootEntry.builder(MSItems.SALAD).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(3, 10))))
						.addEntry(ItemLootEntry.builder(Items.CARROT).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(3, 10))))
						.addEntry(ItemLootEntry.builder(Items.POTION).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(3, 6))).acceptFunction(SetNBT.func_215952_a(waterNBT())))
						.addEntry(ItemLootEntry.builder(MSItems.DESERT_FRUIT).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(15, 20)))))
				.addLootPool(LootPool.builder().name(SPECIAL_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(MSBlocks.GOLD_SEEDS).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(1, 3))))
						.addEntry(ItemLootEntry.builder(Items.COD).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(1, 3))))));
		lootProcessor.accept(locationFor(LandTypes.LUSH_DESERTS, MSLootTables.CONSORT_FOOD_STOCK), LootTable.builder()
				.addLootPool(LootPool.builder().name(MAIN_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(MSItems.JAR_OF_BUGS).weight(5).acceptFunction(SetCount.builder(RandomValueRange.of(3, 10))))
						.addEntry(ItemLootEntry.builder(MSItems.BUG_ON_A_STICK).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(3, 10))))
						.addEntry(ItemLootEntry.builder(Items.POTATO).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(3, 10))))
						.addEntry(ItemLootEntry.builder(MSItems.SALAD).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(3, 10))))
						.addEntry(ItemLootEntry.builder(Items.CARROT).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(3, 10))))
						.addEntry(ItemLootEntry.builder(Items.POTION).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(3, 6))).acceptFunction(SetNBT.func_215952_a(waterNBT())))
						.addEntry(ItemLootEntry.builder(MSItems.DESERT_FRUIT).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(15, 20)))))
				.addLootPool(LootPool.builder().name(SPECIAL_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(MSBlocks.GOLD_SEEDS).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(1, 3))))
						.addEntry(ItemLootEntry.builder(Items.COD).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(1, 3))))));
		lootProcessor.accept(locationFor(LandTypes.SANDSTONE, MSLootTables.CONSORT_FOOD_STOCK), LootTable.builder()
				.addLootPool(LootPool.builder().name(MAIN_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(MSItems.JAR_OF_BUGS).weight(5).acceptFunction(SetCount.builder(RandomValueRange.of(3, 10))))
						.addEntry(ItemLootEntry.builder(MSItems.BUG_ON_A_STICK).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(3, 10))))
						.addEntry(ItemLootEntry.builder(Items.POTATO).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(3, 10))))
						.addEntry(ItemLootEntry.builder(MSItems.SALAD).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(3, 10))))
						.addEntry(ItemLootEntry.builder(Items.CARROT).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(3, 10)))))
				.addLootPool(LootPool.builder().name(SPECIAL_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(MSBlocks.GOLD_SEEDS).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(1, 3))))
						.addEntry(ItemLootEntry.builder(Items.COD).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(1, 3))))));
		lootProcessor.accept(locationFor(LandTypes.RED_SANDSTONE, MSLootTables.CONSORT_FOOD_STOCK), LootTable.builder()
				.addLootPool(LootPool.builder().name(MAIN_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(MSItems.JAR_OF_BUGS).weight(5).acceptFunction(SetCount.builder(RandomValueRange.of(3, 10))))
						.addEntry(ItemLootEntry.builder(MSItems.BUG_ON_A_STICK).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(3, 10))))
						.addEntry(ItemLootEntry.builder(Items.POTATO).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(3, 10))))
						.addEntry(ItemLootEntry.builder(MSItems.SALAD).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(3, 10))))
						.addEntry(ItemLootEntry.builder(Items.CARROT).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(3, 10)))))
				.addLootPool(LootPool.builder().name(SPECIAL_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(MSBlocks.GOLD_SEEDS).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(1, 3))))
						.addEntry(ItemLootEntry.builder(Items.COD).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(1, 3))))));
		lootProcessor.accept(locationFor(LandTypes.RAINBOW, MSLootTables.CONSORT_FOOD_STOCK), LootTable.builder()
				.addLootPool(LootPool.builder().name(MAIN_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(MSItems.JAR_OF_BUGS).weight(5).acceptFunction(SetCount.builder(RandomValueRange.of(3, 10))))
						.addEntry(ItemLootEntry.builder(MSItems.BUG_ON_A_STICK).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(3, 10))))
						.addEntry(ItemLootEntry.builder(Items.POTATO).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(3, 10))))
						.addEntry(ItemLootEntry.builder(MSItems.SALAD).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(3, 10))))
						.addEntry(ItemLootEntry.builder(Items.CARROT).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(3, 10))))
						.addEntry(ItemLootEntry.builder(Items.BREAD).weight(5).acceptFunction(SetCount.builder(RandomValueRange.of(8, 12)))))
				.addLootPool(LootPool.builder().name(SPECIAL_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(MSBlocks.GOLD_SEEDS).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(1, 3))))
						.addEntry(ItemLootEntry.builder(Items.COD).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(1, 3))))));
		lootProcessor.accept(locationFor(LandTypes.RAIN, MSLootTables.CONSORT_FOOD_STOCK), LootTable.builder()
				.addLootPool(LootPool.builder().name(MAIN_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(MSItems.JAR_OF_BUGS).weight(5).acceptFunction(SetCount.builder(RandomValueRange.of(3, 10))))
						.addEntry(ItemLootEntry.builder(MSItems.BUG_ON_A_STICK).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(3, 10))))
						.addEntry(ItemLootEntry.builder(Items.POTATO).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(3, 10))))
						.addEntry(ItemLootEntry.builder(MSItems.SALAD).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(3, 10))))
						.addEntry(ItemLootEntry.builder(Items.CARROT).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(3, 10))))
						.addEntry(ItemLootEntry.builder(Items.MELON).weight(5).acceptFunction(SetCount.builder(RandomValueRange.of(8, 12)))))
				.addLootPool(LootPool.builder().name(SPECIAL_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(MSBlocks.GOLD_SEEDS).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(1, 3))))
						.addEntry(ItemLootEntry.builder(Items.COD).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(1, 3))))));
		
		lootProcessor.accept(locationFor(LandTypes.FROGS, MSLootTables.CONSORT_FOOD_STOCK), LootTable.builder()
				.addLootPool(LootPool.builder().name(MAIN_POOL).rolls(ConstantRange.of(1)))
				.addLootPool(LootPool.builder().name(SPECIAL_POOL).rolls(ConstantRange.of(1))));
		
		lootProcessor.accept(locationFor(LandTypes.WIND, MSLootTables.CONSORT_FOOD_STOCK), LootTable.builder()
				.addLootPool(LootPool.builder().name(MAIN_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(MSItems.COTTON_CANDY_FAYGO).weight(2).acceptFunction(SetCount.builder(RandomValueRange.of(1, 3)))))
				.addLootPool(LootPool.builder().name(SPECIAL_POOL).rolls(ConstantRange.of(1))));
		
		lootProcessor.accept(locationFor(LandTypes.LIGHT, MSLootTables.CONSORT_FOOD_STOCK), LootTable.builder()
				.addLootPool(LootPool.builder().name(MAIN_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(MSItems.ORANGE_FAYGO).weight(2).acceptFunction(SetCount.builder(RandomValueRange.of(1, 3)))))
				.addLootPool(LootPool.builder().name(SPECIAL_POOL).rolls(ConstantRange.of(1))));
		
		lootProcessor.accept(locationFor(LandTypes.CLOCKWORK, MSLootTables.CONSORT_FOOD_STOCK), LootTable.builder()
				.addLootPool(LootPool.builder().name(MAIN_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(MSItems.CANDY_APPLE_FAYGO).weight(2).acceptFunction(SetCount.builder(RandomValueRange.of(1, 3)))))
				.addLootPool(LootPool.builder().name(SPECIAL_POOL).rolls(ConstantRange.of(1))));
		
		lootProcessor.accept(locationFor(LandTypes.SILENCE, MSLootTables.CONSORT_FOOD_STOCK), LootTable.builder()
				.addLootPool(LootPool.builder().name(MAIN_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(MSItems.FAYGO_COLA).weight(2).acceptFunction(SetCount.builder(RandomValueRange.of(1, 3)))))
				.addLootPool(LootPool.builder().name(SPECIAL_POOL).rolls(ConstantRange.of(1))));
		
		lootProcessor.accept(locationFor(LandTypes.THUNDER, MSLootTables.CONSORT_FOOD_STOCK), LootTable.builder()
				.addLootPool(LootPool.builder().name(MAIN_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(MSItems.PEACH_FAYGO).weight(2).acceptFunction(SetCount.builder(RandomValueRange.of(1, 3)))))
				.addLootPool(LootPool.builder().name(SPECIAL_POOL).rolls(ConstantRange.of(1))));
		
		lootProcessor.accept(locationFor(LandTypes.PULSE, MSLootTables.CONSORT_FOOD_STOCK), LootTable.builder()
				.addLootPool(LootPool.builder().name(MAIN_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(MSItems.BEEF_SWORD).weight(4)).acceptCondition(ConsortLootCondition.builder(EnumConsort.NAKAGATOR))
						.addEntry(ItemLootEntry.builder(MSItems.REDPOP_FAYGO).weight(2).acceptFunction(SetCount.builder(RandomValueRange.of(1, 3)))))
				.addLootPool(LootPool.builder().name(SPECIAL_POOL).rolls(ConstantRange.of(1))));
		
		lootProcessor.accept(locationFor(LandTypes.THOUGHT, MSLootTables.CONSORT_FOOD_STOCK), LootTable.builder()
				.addLootPool(LootPool.builder().name(MAIN_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(MSItems.MOON_MIST_FAYGO).weight(2).acceptFunction(SetCount.builder(RandomValueRange.of(1, 3)))))
				.addLootPool(LootPool.builder().name(SPECIAL_POOL).rolls(ConstantRange.of(1))));
		
		lootProcessor.accept(locationFor(LandTypes.BUCKETS, MSLootTables.CONSORT_FOOD_STOCK), LootTable.builder()
				.addLootPool(LootPool.builder().name(MAIN_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(Items.MILK_BUCKET).weight(2).acceptFunction(SetCount.builder(RandomValueRange.of(1, 3)))))
				.addLootPool(LootPool.builder().name(SPECIAL_POOL).rolls(ConstantRange.of(1))));
		
		lootProcessor.accept(locationFor(LandTypes.CAKE, MSLootTables.CONSORT_FOOD_STOCK), LootTable.builder()
				.addLootPool(LootPool.builder().name(MAIN_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(Items.SUGAR).weight(2).acceptFunction(SetCount.builder(RandomValueRange.of(3, 6)))))
				.addLootPool(LootPool.builder().name(SPECIAL_POOL).rolls(ConstantRange.of(1))));
		
		lootProcessor.accept(locationFor(LandTypes.RABBITS, MSLootTables.CONSORT_FOOD_STOCK), LootTable.builder()
				.addLootPool(LootPool.builder().name(MAIN_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(Items.RABBIT_STEW).weight(2).acceptFunction(SetCount.builder(RandomValueRange.of(2, 4)))))
				.addLootPool(LootPool.builder().name(SPECIAL_POOL).rolls(ConstantRange.of(1))));
		
		lootProcessor.accept(locationFor(LandTypes.MONSTERS, MSLootTables.CONSORT_FOOD_STOCK), LootTable.builder()
				.addLootPool(LootPool.builder().name(MAIN_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(MSItems.GRAPE_FAYGO).weight(2).acceptFunction(SetCount.builder(RandomValueRange.of(1, 3)))))
				.addLootPool(LootPool.builder().name(SPECIAL_POOL).rolls(ConstantRange.of(1))));
		lootProcessor.accept(locationFor(LandTypes.UNDEAD, MSLootTables.CONSORT_FOOD_STOCK), LootTable.builder()
				.addLootPool(LootPool.builder().name(MAIN_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(MSItems.GRAPE_FAYGO).weight(2).acceptFunction(SetCount.builder(RandomValueRange.of(1, 3)))))
				.addLootPool(LootPool.builder().name(SPECIAL_POOL).rolls(ConstantRange.of(1))));
		
		lootProcessor.accept(locationFor(LandTypes.TOWERS, MSLootTables.CONSORT_FOOD_STOCK), LootTable.builder()
				.addLootPool(LootPool.builder().name(MAIN_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(MSItems.CREME_SODA_FAYGO).weight(2).acceptFunction(SetCount.builder(RandomValueRange.of(1, 3)))))
				.addLootPool(LootPool.builder().name(SPECIAL_POOL).rolls(ConstantRange.of(1))));
		
		
		lootProcessor.accept(MSLootTables.CONSORT_JUNK_REWARD, LootTable.builder()
				.addLootPool(LootPool.builder().name("main").rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(Items.OAK_PLANKS).weight(2))
						.addEntry(ItemLootEntry.builder(Items.SPRUCE_PLANKS).weight(2))
						.addEntry(ItemLootEntry.builder(Items.BIRCH_PLANKS).weight(2))
						.addEntry(ItemLootEntry.builder(Items.JUNGLE_PLANKS).weight(2))
						.addEntry(ItemLootEntry.builder(Items.DARK_OAK_PLANKS).weight(2))
						.addEntry(ItemLootEntry.builder(Items.ACACIA_PLANKS).weight(2))
						.addEntry(ItemLootEntry.builder(Items.WOODEN_AXE).weight(5))
						.addEntry(ItemLootEntry.builder(Items.WOODEN_PICKAXE).weight(5))
						.addEntry(ItemLootEntry.builder(Items.WOODEN_HOE).weight(5))
						.addEntry(ItemLootEntry.builder(Items.WOODEN_SHOVEL).weight(5))
						.addEntry(ItemLootEntry.builder(Items.ROTTEN_FLESH).weight(8))
						.addEntry(ItemLootEntry.builder(MSBlocks.GENERIC_OBJECT).weight(10))
						.addEntry(ItemLootEntry.builder(Items.POISONOUS_POTATO).weight(10))
						.addEntry(ItemLootEntry.builder(Items.RABBIT_HIDE).weight(10))
						.addEntry(ItemLootEntry.builder(Items.DEAD_BUSH).weight(10))
						.addEntry(ItemLootEntry.builder(Items.OAK_STAIRS).weight(5))
						.addEntry(ItemLootEntry.builder(Items.FLOWER_POT).weight(5))
						.addEntry(ItemLootEntry.builder(Items.BIRCH_BUTTON).weight(5))
						.addEntry(ItemLootEntry.builder(MSItems.SORD).weight(10))
						.addEntry(ItemLootEntry.builder(MSItems.CROWBAR).weight(3)).acceptFunction(SetDamage.func_215931_a(RandomValueRange.of(0.05F, 0.7F)))
						.addEntry(ItemLootEntry.builder(Items.GOLDEN_SHOVEL).weight(3).acceptFunction(SetDamage.func_215931_a(RandomValueRange.of(0.05F, 0.05F))))
						.addEntry(ItemLootEntry.builder(Items.GOLDEN_AXE).weight(3).acceptFunction(SetDamage.func_215931_a(RandomValueRange.of(0.05F, 0.05F))))
						.addEntry(ItemLootEntry.builder(Items.GOLDEN_SWORD).weight(3).acceptFunction(SetDamage.func_215931_a(RandomValueRange.of(0.05F, 0.05F))))
						.addEntry(ItemLootEntry.builder(Items.GOLDEN_PICKAXE).weight(3).acceptFunction(SetDamage.func_215931_a(RandomValueRange.of(0.05F, 0.05F))))
						.addEntry(ItemLootEntry.builder(MSItems.SBAHJ_POSTER).weight(10))
						.addEntry(ItemLootEntry.builder(MSItems.CREW_POSTER).weight(10))
						.addEntry(ItemLootEntry.builder(MSItems.THRESH_DVD).weight(10))
						.addEntry(ItemLootEntry.builder(MSItems.ONION).weight(5)).acceptCondition(ConsortLootCondition.builder(EnumConsort.NAKAGATOR))
						.addEntry(ItemLootEntry.builder(MSItems.TAB).weight(5)).acceptCondition(ConsortLootCondition.builder(EnumConsort.SALAMANDER))
						.addEntry(ItemLootEntry.builder(MSItems.JAR_OF_BUGS).weight(5)).acceptCondition(ConsortLootCondition.builder(EnumConsort.TURTLE))
						.addEntry(ItemLootEntry.builder(MSItems.GRASSHOPPER).weight(5)).acceptCondition(ConsortLootCondition.builder(EnumConsort.IGUANA))));
		
	}
	
	private static CompoundNBT waterNBT()
	{
		return Objects.requireNonNull(PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), Potions.WATER).getTag());
	}
}