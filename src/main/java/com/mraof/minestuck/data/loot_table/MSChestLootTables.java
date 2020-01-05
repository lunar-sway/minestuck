package com.mraof.minestuck.data.loot_table;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.entity.consort.EnumConsort;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.world.lands.LandTypes;
import com.mraof.minestuck.world.lands.terrain.TerrainLandType;
import com.mraof.minestuck.world.lands.title.TitleLandType;
import com.mraof.minestuck.world.storage.loot.LandTableLootEntry;
import com.mraof.minestuck.world.storage.loot.MSLootTables;
import com.mraof.minestuck.world.storage.loot.conditions.ConsortLootCondition;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.*;
import net.minecraft.world.storage.loot.functions.SetCount;
import net.minecraft.world.storage.loot.functions.SetDamage;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class MSChestLootTables implements Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>
{
	protected static final String ITEM_POOL = "item";
	protected static final String BLOCK_POOL = "block";
	
	@Override
	public void accept(BiConsumer<ResourceLocation, LootTable.Builder> lootProcessor)
	{
		lootProcessor.accept(locationFor(LandTypes.FLORA, MSLootTables.BASIC_MEDIUM_CHEST), LootTable.builder().addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
				.addEntry(ItemLootEntry.builder(Items.DANDELION).weight(3).quality(-1).acceptFunction(SetCount.builder(RandomValueRange.of(1, 4))))
				.addEntry(ItemLootEntry.builder(Items.POPPY).weight(3).quality(-1).acceptFunction(SetCount.builder(RandomValueRange.of(1, 4))))
				.addEntry(ItemLootEntry.builder(Items.BLUE_ORCHID).weight(3).quality(-1).acceptFunction(SetCount.builder(RandomValueRange.of(1, 4))))
				.addEntry(ItemLootEntry.builder(Items.ALLIUM).weight(3).quality(-1).acceptFunction(SetCount.builder(RandomValueRange.of(1, 4))))
				.addEntry(ItemLootEntry.builder(Items.AZURE_BLUET).weight(3).quality(-1).acceptFunction(SetCount.builder(RandomValueRange.of(1, 4))))
				.addEntry(ItemLootEntry.builder(Items.RED_TULIP).weight(3).quality(-1).acceptFunction(SetCount.builder(RandomValueRange.of(1, 4))))
				.addEntry(ItemLootEntry.builder(Items.ORANGE_TULIP).weight(3).quality(-1).acceptFunction(SetCount.builder(RandomValueRange.of(1, 4))))
				.addEntry(ItemLootEntry.builder(Items.WHITE_TULIP).weight(3).quality(-1).acceptFunction(SetCount.builder(RandomValueRange.of(1, 4))))
				.addEntry(ItemLootEntry.builder(Items.PINK_TULIP).weight(3).quality(-1).acceptFunction(SetCount.builder(RandomValueRange.of(1, 4))))
				.addEntry(ItemLootEntry.builder(Items.OXEYE_DAISY).weight(3).quality(-1).acceptFunction(SetCount.builder(RandomValueRange.of(1, 4))))
				.addEntry(ItemLootEntry.builder(Items.SUNFLOWER).weight(6).quality(-2).acceptFunction(SetCount.builder(RandomValueRange.of(1, 3))))
				.addEntry(ItemLootEntry.builder(Items.LILAC).weight(6).quality(-2).acceptFunction(SetCount.builder(RandomValueRange.of(1, 3))))
				.addEntry(ItemLootEntry.builder(Items.ROSE_BUSH).weight(6).quality(-2).acceptFunction(SetCount.builder(RandomValueRange.of(1, 3))))
				.addEntry(ItemLootEntry.builder(Items.PEONY).weight(6).quality(-2).acceptFunction(SetCount.builder(RandomValueRange.of(1, 3))))
				.addEntry(ItemLootEntry.builder(Items.FERN).weight(11).quality(-2).acceptFunction(SetCount.builder(RandomValueRange.of(1, 5))))
				.addEntry(ItemLootEntry.builder(Items.SHEARS).weight(10).quality(-1).acceptFunction(SetDamage.func_215931_a(RandomValueRange.of(0.75F, 1.0F))))
				.addEntry(ItemLootEntry.builder(MSItems.SICKLE).weight(8).quality(-1).acceptFunction(SetDamage.func_215931_a(RandomValueRange.of(0.75F, 1.0F))))
				.addEntry(ItemLootEntry.builder(MSBlocks.FLOWERY_MOSSY_COBBLESTONE).weight(9).quality(0).acceptFunction(SetCount.builder(RandomValueRange.of(8, 24))))
				.addEntry(ItemLootEntry.builder(Items.MOSSY_COBBLESTONE).weight(9).quality(0).acceptFunction(SetCount.builder(RandomValueRange.of(8, 24))))));
		
		
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
						.addEntry(ItemLootEntry.builder(MSItems.GRIMOIRE).weight(1).acceptCondition(ConsortLootCondition.builder(EnumConsort.TURTLE)))
						.addEntry(ItemLootEntry.builder(MSItems.CRUMPLY_HAT).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(1, 5))).acceptCondition(ConsortLootCondition.builder(EnumConsort.SALAMANDER))))
				.addLootPool(LootPool.builder().name(BLOCK_POOL).rolls(ConstantRange.of(2))
						.addEntry(LandTableLootEntry.builder(MSLootTables.CONSORT_GENERAL_STOCK).setPool(BLOCK_POOL))));
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
		lootProcessor.accept(locationFor(LandTypes.FLORA, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.builder()
				.addLootPool(LootPool.builder().name(ITEM_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(Items.EMERALD).weight(5).acceptFunction(SetCount.builder(RandomValueRange.of(2, 4))))
						.addEntry(ItemLootEntry.builder(Items.QUARTZ).weight(10).acceptFunction(SetCount.builder(RandomValueRange.of(5, 10))))
						.addEntry(ItemLootEntry.builder(Items.PRISMARINE_CRYSTALS).weight(6).acceptFunction(SetCount.builder(RandomValueRange.of(2, 5)))))
				.addLootPool(LootPool.builder().name(BLOCK_POOL).rolls(ConstantRange.of(1))));
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
		lootProcessor.accept(locationFor(LandTypes.RAINBOW, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.builder()
				.addLootPool(LootPool.builder().name(ITEM_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(Items.CLAY_BALL).weight(10).acceptFunction(SetCount.builder(RandomValueRange.of(5, 10))))
						.addEntry(ItemLootEntry.builder(Items.PAPER).weight(10).acceptFunction(SetCount.builder(RandomValueRange.of(5, 10))))
						.addEntry(ItemLootEntry.builder(Items.EGG).weight(3).acceptFunction(SetCount.builder(RandomValueRange.of(1, 3)))))
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
		lootProcessor.accept(locationFor(LandTypes.SAND, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.builder()
				.addLootPool(LootPool.builder().name(ITEM_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(MSItems.CACTUS_CUTLASS).weight(3))
						.addEntry(ItemLootEntry.builder(Items.GOLD_INGOT).weight(7).acceptFunction(SetCount.builder(RandomValueRange.of(5, 10))))
						.addEntry(ItemLootEntry.builder(Items.LEATHER).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(2, 5)))))
				.addLootPool(LootPool.builder().name(BLOCK_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(Items.CACTUS).weight(6).acceptFunction(SetCount.builder(RandomValueRange.of(8, 20))))
						.addEntry(ItemLootEntry.builder(MSBlocks.WOODEN_CACTUS).weight(3).acceptFunction(SetCount.builder(RandomValueRange.of(8, 20))))
						.addEntry(ItemLootEntry.builder(Items.SANDSTONE).weight(10).acceptFunction(SetCount.builder(RandomValueRange.of(8, 20))))
						.addEntry(ItemLootEntry.builder(Items.SMOOTH_SANDSTONE).weight(10).acceptFunction(SetCount.builder(RandomValueRange.of(8, 20))))));
		lootProcessor.accept(locationFor(LandTypes.LUSH_DESERTS, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.builder()
				.addLootPool(LootPool.builder().name(ITEM_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(MSItems.CACTUS_CUTLASS).weight(3))
						.addEntry(ItemLootEntry.builder(Items.GOLD_INGOT).weight(7).acceptFunction(SetCount.builder(RandomValueRange.of(5, 10))))
						.addEntry(ItemLootEntry.builder(Items.LEATHER).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(2, 5)))))
				.addLootPool(LootPool.builder().name(BLOCK_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(Items.CACTUS).weight(6).acceptFunction(SetCount.builder(RandomValueRange.of(8, 20))))
						.addEntry(ItemLootEntry.builder(MSBlocks.WOODEN_CACTUS).weight(3).acceptFunction(SetCount.builder(RandomValueRange.of(8, 20))))
						.addEntry(ItemLootEntry.builder(Items.SANDSTONE).weight(10).acceptFunction(SetCount.builder(RandomValueRange.of(8, 20))))
						.addEntry(ItemLootEntry.builder(Items.SMOOTH_SANDSTONE).weight(10).acceptFunction(SetCount.builder(RandomValueRange.of(8, 20))))));
		lootProcessor.accept(locationFor(LandTypes.RED_SAND, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.builder()
				.addLootPool(LootPool.builder().name(ITEM_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(MSItems.CACTUS_CUTLASS).weight(3))
						.addEntry(ItemLootEntry.builder(Items.GOLD_INGOT).weight(7).acceptFunction(SetCount.builder(RandomValueRange.of(5, 10))))
						.addEntry(ItemLootEntry.builder(Items.LEATHER).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(2, 5)))))
				.addLootPool(LootPool.builder().name(BLOCK_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(Items.CACTUS).weight(6).acceptFunction(SetCount.builder(RandomValueRange.of(8, 20))))
						.addEntry(ItemLootEntry.builder(MSBlocks.WOODEN_CACTUS).weight(3).acceptFunction(SetCount.builder(RandomValueRange.of(8, 20))))
						.addEntry(ItemLootEntry.builder(Items.RED_SANDSTONE).weight(10).acceptFunction(SetCount.builder(RandomValueRange.of(8, 20))))
						.addEntry(ItemLootEntry.builder(Items.SMOOTH_RED_SANDSTONE).weight(10).acceptFunction(SetCount.builder(RandomValueRange.of(8, 20))))));
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
		lootProcessor.accept(locationFor(LandTypes.BUCKETS, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.builder()
				.addLootPool(LootPool.builder().name(ITEM_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(Items.BUCKET).weight(10).acceptFunction(SetCount.builder(RandomValueRange.of(2, 5)))))
				.addLootPool(LootPool.builder().name(BLOCK_POOL).rolls(ConstantRange.of(1))));
		lootProcessor.accept(locationFor(LandTypes.CLOCKWORK, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.builder()
				.addLootPool(LootPool.builder().name(ITEM_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(Items.CLOCK).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(2, 5)))))
				.addLootPool(LootPool.builder().name(BLOCK_POOL).rolls(ConstantRange.of(1))));
		lootProcessor.accept(locationFor(LandTypes.LIGHT, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.builder()
				.addLootPool(LootPool.builder().name(ITEM_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(MSBlocks.GLOWYSTONE_DUST).weight(6).acceptFunction(SetCount.builder(RandomValueRange.of(2, 5)))))
				.addLootPool(LootPool.builder().name(BLOCK_POOL).rolls(ConstantRange.of(1))));
		lootProcessor.accept(locationFor(LandTypes.RABBITS, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.builder()
				.addLootPool(LootPool.builder().name(ITEM_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(Items.RABBIT_FOOT).weight(5).acceptFunction(SetCount.builder(RandomValueRange.of(2, 5)))))
				.addLootPool(LootPool.builder().name(BLOCK_POOL).rolls(ConstantRange.of(1))));
		lootProcessor.accept(locationFor(LandTypes.THOUGHT, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.builder()
				.addLootPool(LootPool.builder().name(ITEM_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(Items.BOOK).weight(10).acceptFunction(SetCount.builder(RandomValueRange.of(2, 5)))))
				.addLootPool(LootPool.builder().name(BLOCK_POOL).rolls(ConstantRange.of(1))));
		lootProcessor.accept(locationFor(LandTypes.THUNDER, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.builder()
				.addLootPool(LootPool.builder().name(ITEM_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(MSItems.IRON_CANE).weight(3)))
				.addLootPool(LootPool.builder().name(BLOCK_POOL).rolls(ConstantRange.of(1))));
		lootProcessor.accept(locationFor(LandTypes.MONSTERS, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.builder()
				.addLootPool(LootPool.builder().name(ITEM_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(Items.ROTTEN_FLESH).weight(10).acceptFunction(SetCount.builder(RandomValueRange.of(5, 10)))))
				.addLootPool(LootPool.builder().name(BLOCK_POOL).rolls(ConstantRange.of(1))));
		lootProcessor.accept(locationFor(LandTypes.CAKE, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.builder()
				.addLootPool(LootPool.builder().name(ITEM_POOL).rolls(ConstantRange.of(1)))
				.addLootPool(LootPool.builder().name(BLOCK_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(MSBlocks.SUGAR_CUBE).weight(6).acceptFunction(SetCount.builder(RandomValueRange.of(5, 10))))
						.addEntry(ItemLootEntry.builder(MSBlocks.FUCHSIA_CAKE).weight(2).acceptFunction(SetCount.builder(RandomValueRange.of(1, 3))))));
		lootProcessor.accept(locationFor(LandTypes.FROGS, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.builder()
				.addLootPool(LootPool.builder().name(ITEM_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(MSItems.BUG_NET).weight(5)))
				.addLootPool(LootPool.builder().name(BLOCK_POOL).rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(Items.LILY_PAD).weight(8).acceptFunction(SetCount.builder(RandomValueRange.of(2, 5))))));
	}
	
	protected ResourceLocation locationFor(TerrainLandType landAspect, ResourceLocation baseLoot)
	{
		ResourceLocation landName = Objects.requireNonNull(landAspect.getRegistryName());
		return new ResourceLocation(baseLoot.getNamespace(), baseLoot.getPath() + "/terrain/" + landName.toString().replace(':', '/'));
	}
	
	protected ResourceLocation locationFor(TitleLandType landAspect, ResourceLocation baseLoot)
	{
		ResourceLocation landName = Objects.requireNonNull(landAspect.getRegistryName());
		return new ResourceLocation(baseLoot.getNamespace(), baseLoot.getPath() + "/title/" + landName.toString().replace(':', '/'));
	}
}