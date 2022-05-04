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
import net.minecraft.loot.*;
import net.minecraft.loot.functions.SetCount;
import net.minecraft.loot.functions.SetDamage;
import net.minecraft.loot.functions.SetNBT;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.util.ResourceLocation;

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
		lootProcessor.accept(COLD_CAKE, LootTable.lootTable()
				.withPool(LootPool.lootPool().name("blue_cake").setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(MSBlocks.BLUE_CAKE).apply(SetCount.setCount(RandomValueRange.between(1, 3)))))
				.withPool(LootPool.lootPool().name("cold_cake").setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(MSBlocks.COLD_CAKE).apply(SetCount.setCount(RandomValueRange.between(1, 3))))));
		lootProcessor.accept(HOT_CAKE, LootTable.lootTable()
				.withPool(LootPool.lootPool().name("red_cake").setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(MSBlocks.RED_CAKE).apply(SetCount.setCount(RandomValueRange.between(1, 3)))))
				.withPool(LootPool.lootPool().name("hot_cake").setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(MSBlocks.HOT_CAKE).apply(SetCount.setCount(RandomValueRange.between(1, 3))))));
		
		lootProcessor.accept(MSLootTables.CONSORT_GENERAL_STOCK, LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL).setRolls(ConstantRange.exactly(4))
						.add(LandTableLootEntry.builder(MSLootTables.CONSORT_GENERAL_STOCK).setPool(ITEM_POOL))
						.add(ItemLootEntry.lootTableItem(MSItems.CARVING_TOOL).setWeight(10).apply(SetCount.setCount(RandomValueRange.between(1, 5))))
						.add(ItemLootEntry.lootTableItem(MSBlocks.MINI_FROG_STATUE).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(1, 5))))
						.add(ItemLootEntry.lootTableItem(MSBlocks.MINI_WIZARD_STATUE).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(1, 5))))
						.add(ItemLootEntry.lootTableItem(MSBlocks.MINI_TYPHEUS_STATUE).setWeight(1).apply(SetCount.setCount(ConstantRange.exactly(1))))
						.add(ItemLootEntry.lootTableItem(MSItems.STONE_SLAB).setWeight(10).apply(SetCount.setCount(RandomValueRange.between(1, 5))))
						.add(ItemLootEntry.lootTableItem(MSItems.THRESH_DVD).setWeight(5).apply(SetCount.setCount(RandomValueRange.between(1, 3))))
						.add(ItemLootEntry.lootTableItem(MSItems.CREW_POSTER).setWeight(5).apply(SetCount.setCount(RandomValueRange.between(1, 3))))
						.add(ItemLootEntry.lootTableItem(MSItems.SBAHJ_POSTER).setWeight(5).apply(SetCount.setCount(RandomValueRange.between(1, 3))))
						.add(ItemLootEntry.lootTableItem(MSItems.GAMEBRO_MAGAZINE).setWeight(5).when(ConsortLootCondition.builder(EnumConsort.SALAMANDER, EnumConsort.NAKAGATOR)))
						.add(ItemLootEntry.lootTableItem(MSItems.GAMEGRL_MAGAZINE).setWeight(5).when(ConsortLootCondition.builder(EnumConsort.TURTLE, EnumConsort.IGUANA)))
						.add(ItemLootEntry.lootTableItem(MSItems.ACE_OF_CLUBS).setWeight(2).when(ConsortLootCondition.builder(EnumConsort.SALAMANDER)))
						.add(ItemLootEntry.lootTableItem(MSItems.ACE_OF_SPADES).setWeight(2).when(ConsortLootCondition.builder(EnumConsort.NAKAGATOR)))
						.add(ItemLootEntry.lootTableItem(MSItems.ACE_OF_HEARTS).setWeight(2).when(ConsortLootCondition.builder(EnumConsort.TURTLE)))
						.add(ItemLootEntry.lootTableItem(MSItems.ACE_OF_DIAMONDS).setWeight(2).when(ConsortLootCondition.builder(EnumConsort.IGUANA)))
						.add(ItemLootEntry.lootTableItem(MSItems.CLUBS_SUITARANG).setWeight(2).apply(SetCount.setCount(RandomValueRange.between(1, 5))))
						.add(ItemLootEntry.lootTableItem(MSItems.DIAMONDS_SUITARANG).setWeight(2).apply(SetCount.setCount(RandomValueRange.between(1, 5))))
						.add(ItemLootEntry.lootTableItem(MSItems.HEARTS_SUITARANG).setWeight(2).apply(SetCount.setCount(RandomValueRange.between(1, 5))))
						.add(ItemLootEntry.lootTableItem(MSItems.SPADES_SUITARANG).setWeight(2).apply(SetCount.setCount(RandomValueRange.between(1, 5))))
						.add(ItemLootEntry.lootTableItem(MSItems.MUSIC_DISC_EMISSARY_OF_DANCE).setWeight(1))
						.add(ItemLootEntry.lootTableItem(MSItems.MUSIC_DISC_DANCE_STAB_DANCE).setWeight(1))
						.add(ItemLootEntry.lootTableItem(MSItems.ELECTRIC_AUTOHARP).setWeight(1))
						.add(ItemLootEntry.lootTableItem(MSItems.BATTERY).setWeight(2).apply(SetCount.setCount(RandomValueRange.between(1,4))))
						.add(ItemLootEntry.lootTableItem(MSItems.GRIMOIRE).setWeight(1).when(ConsortLootCondition.builder(EnumConsort.TURTLE)))
						.add(ItemLootEntry.lootTableItem(MSItems.CRUMPLY_HAT).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(1, 5))).when(ConsortLootCondition.builder(EnumConsort.SALAMANDER))))
				.withPool(LootPool.lootPool().name(BLOCK_POOL).setRolls(ConstantRange.exactly(2))
						.add(LandTableLootEntry.builder(MSLootTables.CONSORT_GENERAL_STOCK).setPool(BLOCK_POOL))));
		
		lootProcessor.accept(locationFor(LandTypes.FOREST, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL).setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(MSItems.COPSE_CRUSHER).setWeight(3))
						.add(ItemLootEntry.lootTableItem(Items.OAK_SAPLING).setWeight(2).apply(SetCount.setCount(RandomValueRange.between(5, 10))))
						.add(ItemLootEntry.lootTableItem(Items.SPRUCE_SAPLING).setWeight(2).apply(SetCount.setCount(RandomValueRange.between(5, 10))))
						.add(ItemLootEntry.lootTableItem(Items.BIRCH_SAPLING).setWeight(2).apply(SetCount.setCount(RandomValueRange.between(5, 10))))
						.add(ItemLootEntry.lootTableItem(Items.JUNGLE_SAPLING).setWeight(2).apply(SetCount.setCount(RandomValueRange.between(5, 10))))
						.add(ItemLootEntry.lootTableItem(Items.DARK_OAK_SAPLING).setWeight(2).apply(SetCount.setCount(RandomValueRange.between(5, 10))))
						.add(ItemLootEntry.lootTableItem(Items.ACACIA_SAPLING).setWeight(2).apply(SetCount.setCount(RandomValueRange.between(5, 10))))
						.add(ItemLootEntry.lootTableItem(Items.EMERALD).setWeight(5).apply(SetCount.setCount(RandomValueRange.between(2, 4)))))
				.withPool(LootPool.lootPool().name(BLOCK_POOL).setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(MSBlocks.VINE_LOG).setWeight(10).apply(SetCount.setCount(RandomValueRange.between(8, 20))))
						.add(ItemLootEntry.lootTableItem(MSBlocks.FLOWERY_VINE_LOG).setWeight(6).apply(SetCount.setCount(RandomValueRange.between(8, 20))))
						.add(ItemLootEntry.lootTableItem(Items.DARK_OAK_LOG).setWeight(6).apply(SetCount.setCount(RandomValueRange.between(8, 20))))
						.add(ItemLootEntry.lootTableItem(Items.OAK_PLANKS).setWeight(6).apply(SetCount.setCount(RandomValueRange.between(8, 20))))));
		lootProcessor.accept(locationFor(LandTypes.TAIGA, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL).setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(MSItems.COPSE_CRUSHER).setWeight(3))
						.add(ItemLootEntry.lootTableItem(Items.OAK_SAPLING).setWeight(2).apply(SetCount.setCount(RandomValueRange.between(5, 10))))
						.add(ItemLootEntry.lootTableItem(Items.SPRUCE_SAPLING).setWeight(2).apply(SetCount.setCount(RandomValueRange.between(5, 10))))
						.add(ItemLootEntry.lootTableItem(Items.BIRCH_SAPLING).setWeight(2).apply(SetCount.setCount(RandomValueRange.between(5, 10))))
						.add(ItemLootEntry.lootTableItem(Items.JUNGLE_SAPLING).setWeight(2).apply(SetCount.setCount(RandomValueRange.between(5, 10))))
						.add(ItemLootEntry.lootTableItem(Items.DARK_OAK_SAPLING).setWeight(2).apply(SetCount.setCount(RandomValueRange.between(5, 10))))
						.add(ItemLootEntry.lootTableItem(Items.ACACIA_SAPLING).setWeight(2).apply(SetCount.setCount(RandomValueRange.between(5, 10))))
						.add(ItemLootEntry.lootTableItem(Items.EMERALD).setWeight(5).apply(SetCount.setCount(RandomValueRange.between(2, 4)))))
				.withPool(LootPool.lootPool().name(BLOCK_POOL).setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(Items.SPRUCE_LOG).setWeight(6).apply(SetCount.setCount(RandomValueRange.between(8, 20))))
						.add(ItemLootEntry.lootTableItem(Items.DARK_OAK_LOG).setWeight(6).apply(SetCount.setCount(RandomValueRange.between(8, 20))))
						.add(ItemLootEntry.lootTableItem(Items.OAK_PLANKS).setWeight(6).apply(SetCount.setCount(RandomValueRange.between(8, 20))))));
		
		lootProcessor.accept(locationFor(LandTypes.FROST, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL).setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(MSItems.KATANA).setWeight(3))
						.add(ItemLootEntry.lootTableItem(Items.DIAMOND).setWeight(3).apply(SetCount.setCount(RandomValueRange.between(2, 5))))
						.add(ItemLootEntry.lootTableItem(Items.PRISMARINE_CRYSTALS).setWeight(6).apply(SetCount.setCount(RandomValueRange.between(2, 5))))
						.add(ItemLootEntry.lootTableItem(Items.PRISMARINE_SHARD).setWeight(10).apply(SetCount.setCount(RandomValueRange.between(5, 10)))))
				.withPool(LootPool.lootPool().name(BLOCK_POOL).setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(MSBlocks.FROST_BRICKS).setWeight(10).apply(SetCount.setCount(RandomValueRange.between(8, 20))))
						.add(ItemLootEntry.lootTableItem(MSBlocks.FROST_TILE).setWeight(6).apply(SetCount.setCount(RandomValueRange.between(8, 20))))
						.add(ItemLootEntry.lootTableItem(Items.PRISMARINE).setWeight(10).apply(SetCount.setCount(RandomValueRange.between(8, 20))))
						.add(ItemLootEntry.lootTableItem(Items.PRISMARINE_BRICKS).setWeight(6).apply(SetCount.setCount(RandomValueRange.between(8, 20))))
						.add(ItemLootEntry.lootTableItem(MSBlocks.FROST_LOG).setWeight(6).apply(SetCount.setCount(RandomValueRange.between(8, 20))))));
		
		lootProcessor.accept(locationFor(LandTypes.FUNGI, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL).setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(Items.REDSTONE).setWeight(10).apply(SetCount.setCount(RandomValueRange.between(5, 10)))))
				.withPool(LootPool.lootPool().name(BLOCK_POOL).setRolls(ConstantRange.exactly(1))));
		lootProcessor.accept(locationFor(LandTypes.END, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL).setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(Items.ELYTRA).setWeight(3))
						.add(ItemLootEntry.lootTableItem(Items.OBSIDIAN).setWeight(10).apply(SetCount.setCount(RandomValueRange.between(5, 10))))
						.add(ItemLootEntry.lootTableItem(Items.DRAGON_BREATH).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(2, 5)))))
				.withPool(LootPool.lootPool().name(BLOCK_POOL).setRolls(ConstantRange.exactly(1))));
		
		lootProcessor.accept(locationFor(LandTypes.HEAT, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL).setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(MSItems.FIRE_POKER).setWeight(3))
						.add(ItemLootEntry.lootTableItem(Items.QUARTZ).setWeight(10).apply(SetCount.setCount(RandomValueRange.between(5, 10))))
						.add(ItemLootEntry.lootTableItem(Items.BLAZE_POWDER).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(2, 5))))
						.add(ItemLootEntry.lootTableItem(Items.NETHER_BRICK).setWeight(10).apply(SetCount.setCount(RandomValueRange.between(5, 10)))))
				.withPool(LootPool.lootPool().name(BLOCK_POOL).setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(MSBlocks.CAST_IRON).setWeight(10).apply(SetCount.setCount(RandomValueRange.between(8, 20))))
						.add(ItemLootEntry.lootTableItem(MSBlocks.CHISELED_CAST_IRON).setWeight(6).apply(SetCount.setCount(RandomValueRange.between(8, 20))))
						.add(ItemLootEntry.lootTableItem(Items.NETHER_BRICKS).setWeight(10).apply(SetCount.setCount(RandomValueRange.between(8, 20))))
						.add(ItemLootEntry.lootTableItem(Items.RED_NETHER_BRICKS).setWeight(6).apply(SetCount.setCount(RandomValueRange.between(8, 20))))));
		
		lootProcessor.accept(locationFor(LandTypes.ROCK, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL).setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(MSItems.METAL_BAT).setWeight(3))
						.add(ItemLootEntry.lootTableItem(Items.COAL).setWeight(10).apply(SetCount.setCount(RandomValueRange.between(5, 10))))
						.add(ItemLootEntry.lootTableItem(Items.CLAY_BALL).setWeight(10).apply(SetCount.setCount(RandomValueRange.between(5, 10))))
						.add(ItemLootEntry.lootTableItem(Items.IRON_INGOT).setWeight(10).apply(SetCount.setCount(RandomValueRange.between(5, 10)))))
				.withPool(LootPool.lootPool().name(BLOCK_POOL).setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(MSBlocks.COARSE_STONE).setWeight(10).apply(SetCount.setCount(RandomValueRange.between(8, 20))))
						.add(ItemLootEntry.lootTableItem(MSBlocks.CHISELED_COARSE_STONE).setWeight(6).apply(SetCount.setCount(RandomValueRange.between(8, 20))))
						.add(ItemLootEntry.lootTableItem(Items.POLISHED_ANDESITE).setWeight(10).apply(SetCount.setCount(RandomValueRange.between(8, 20))))
						.add(ItemLootEntry.lootTableItem(Items.POLISHED_GRANITE).setWeight(6).apply(SetCount.setCount(RandomValueRange.between(8, 20))))));
		lootProcessor.accept(locationFor(LandTypes.PETRIFICATION, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL).setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(MSItems.METAL_BAT).setWeight(3))
						.add(ItemLootEntry.lootTableItem(Items.COAL).setWeight(10).apply(SetCount.setCount(RandomValueRange.between(5, 10))))
						.add(ItemLootEntry.lootTableItem(Items.CLAY_BALL).setWeight(10).apply(SetCount.setCount(RandomValueRange.between(5, 10))))
						.add(ItemLootEntry.lootTableItem(Items.IRON_INGOT).setWeight(10).apply(SetCount.setCount(RandomValueRange.between(5, 10)))))
				.withPool(LootPool.lootPool().name(BLOCK_POOL).setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(MSBlocks.COARSE_STONE).setWeight(10).apply(SetCount.setCount(RandomValueRange.between(8, 20))))
						.add(ItemLootEntry.lootTableItem(MSBlocks.CHISELED_COARSE_STONE).setWeight(6).apply(SetCount.setCount(RandomValueRange.between(8, 20))))
						.add(ItemLootEntry.lootTableItem(Items.POLISHED_ANDESITE).setWeight(10).apply(SetCount.setCount(RandomValueRange.between(8, 20))))
						.add(ItemLootEntry.lootTableItem(Items.POLISHED_GRANITE).setWeight(6).apply(SetCount.setCount(RandomValueRange.between(8, 20))))));
		
		lootProcessor.accept(locationFor(LandTypes.SAND, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL).setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(MSItems.CACTACEAE_CUTLASS).setWeight(3))
						.add(ItemLootEntry.lootTableItem(Items.GOLD_INGOT).setWeight(7).apply(SetCount.setCount(RandomValueRange.between(5, 10))))
						.add(ItemLootEntry.lootTableItem(Items.LEATHER).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(2, 5)))))
				.withPool(LootPool.lootPool().name(BLOCK_POOL).setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(Items.CACTUS).setWeight(6).apply(SetCount.setCount(RandomValueRange.between(8, 20))))
						.add(ItemLootEntry.lootTableItem(MSBlocks.WOODEN_CACTUS).setWeight(3).apply(SetCount.setCount(RandomValueRange.between(8, 20))))
						.add(ItemLootEntry.lootTableItem(Items.SANDSTONE).setWeight(10).apply(SetCount.setCount(RandomValueRange.between(8, 20))))
						.add(ItemLootEntry.lootTableItem(Items.SMOOTH_SANDSTONE).setWeight(10).apply(SetCount.setCount(RandomValueRange.between(8, 20))))));
		lootProcessor.accept(locationFor(LandTypes.RED_SAND, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL).setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(MSItems.CACTACEAE_CUTLASS).setWeight(3))
						.add(ItemLootEntry.lootTableItem(Items.GOLD_INGOT).setWeight(7).apply(SetCount.setCount(RandomValueRange.between(5, 10))))
						.add(ItemLootEntry.lootTableItem(Items.LEATHER).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(2, 5)))))
				.withPool(LootPool.lootPool().name(BLOCK_POOL).setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(Items.CACTUS).setWeight(6).apply(SetCount.setCount(RandomValueRange.between(8, 20))))
						.add(ItemLootEntry.lootTableItem(MSBlocks.WOODEN_CACTUS).setWeight(3).apply(SetCount.setCount(RandomValueRange.between(8, 20))))
						.add(ItemLootEntry.lootTableItem(Items.RED_SANDSTONE).setWeight(10).apply(SetCount.setCount(RandomValueRange.between(8, 20))))
						.add(ItemLootEntry.lootTableItem(Items.SMOOTH_RED_SANDSTONE).setWeight(10).apply(SetCount.setCount(RandomValueRange.between(8, 20))))));
		lootProcessor.accept(locationFor(LandTypes.LUSH_DESERTS, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL).setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(MSItems.CACTACEAE_CUTLASS).setWeight(3))
						.add(ItemLootEntry.lootTableItem(Items.GOLD_INGOT).setWeight(7).apply(SetCount.setCount(RandomValueRange.between(5, 10))))
						.add(ItemLootEntry.lootTableItem(Items.LEATHER).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(2, 5)))))
				.withPool(LootPool.lootPool().name(BLOCK_POOL).setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(Items.CACTUS).setWeight(6).apply(SetCount.setCount(RandomValueRange.between(8, 20))))
						.add(ItemLootEntry.lootTableItem(MSBlocks.WOODEN_CACTUS).setWeight(3).apply(SetCount.setCount(RandomValueRange.between(8, 20))))
						.add(ItemLootEntry.lootTableItem(Items.SANDSTONE).setWeight(10).apply(SetCount.setCount(RandomValueRange.between(8, 20))))
						.add(ItemLootEntry.lootTableItem(Items.SMOOTH_SANDSTONE).setWeight(10).apply(SetCount.setCount(RandomValueRange.between(8, 20))))));
		
		lootProcessor.accept(locationFor(LandTypes.SANDSTONE, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL).setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(Items.GOLDEN_SWORD).setWeight(3))
						.add(ItemLootEntry.lootTableItem(Items.REDSTONE).setWeight(10).apply(SetCount.setCount(RandomValueRange.between(5, 10))))
						.add(ItemLootEntry.lootTableItem(Items.GUNPOWDER).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(2, 5)))))
				.withPool(LootPool.lootPool().name(BLOCK_POOL).setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(Items.SMOOTH_SANDSTONE).setWeight(10).apply(SetCount.setCount(RandomValueRange.between(8, 20))))
						.add(ItemLootEntry.lootTableItem(Items.CHISELED_SANDSTONE).setWeight(6).apply(SetCount.setCount(RandomValueRange.between(8, 20))))
						.add(ItemLootEntry.lootTableItem(Items.STONE_BRICKS).setWeight(10).apply(SetCount.setCount(RandomValueRange.between(8, 20))))
						.add(ItemLootEntry.lootTableItem(Items.CHISELED_STONE_BRICKS).setWeight(6).apply(SetCount.setCount(RandomValueRange.between(8, 20))))));
		lootProcessor.accept(locationFor(LandTypes.RED_SANDSTONE, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL).setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(Items.GOLDEN_SWORD).setWeight(3))
						.add(ItemLootEntry.lootTableItem(Items.REDSTONE).setWeight(10).apply(SetCount.setCount(RandomValueRange.between(5, 10))))
						.add(ItemLootEntry.lootTableItem(Items.GUNPOWDER).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(2, 5)))))
				.withPool(LootPool.lootPool().name(BLOCK_POOL).setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(Items.SMOOTH_RED_SANDSTONE).setWeight(10).apply(SetCount.setCount(RandomValueRange.between(8, 20))))
						.add(ItemLootEntry.lootTableItem(Items.CHISELED_RED_SANDSTONE).setWeight(6).apply(SetCount.setCount(RandomValueRange.between(8, 20))))
						.add(ItemLootEntry.lootTableItem(Items.STONE_BRICKS).setWeight(10).apply(SetCount.setCount(RandomValueRange.between(8, 20))))
						.add(ItemLootEntry.lootTableItem(Items.CHISELED_STONE_BRICKS).setWeight(6).apply(SetCount.setCount(RandomValueRange.between(8, 20))))));
		
		lootProcessor.accept(locationFor(LandTypes.SHADE, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL).setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(MSItems.POGO_CLUB).setWeight(3))
						.add(ItemLootEntry.lootTableItem(Items.LAPIS_LAZULI).setWeight(10).apply(SetCount.setCount(RandomValueRange.between(5, 10))))
						.add(ItemLootEntry.lootTableItem(Items.FEATHER).setWeight(6).apply(SetCount.setCount(RandomValueRange.between(5, 10))))
						.add(ItemLootEntry.lootTableItem(Items.FLINT).setWeight(6).apply(SetCount.setCount(RandomValueRange.between(5, 10)))))
				.withPool(LootPool.lootPool().name(BLOCK_POOL).setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(MSBlocks.GLOWING_LOG).setWeight(10).apply(SetCount.setCount(RandomValueRange.between(8, 20))))
						.add(ItemLootEntry.lootTableItem(MSBlocks.GLOWING_PLANKS).setWeight(6).apply(SetCount.setCount(RandomValueRange.between(8, 20))))
						.add(ItemLootEntry.lootTableItem(MSBlocks.SHADE_BRICKS).setWeight(10).apply(SetCount.setCount(RandomValueRange.between(8, 20))))
						.add(ItemLootEntry.lootTableItem(MSBlocks.SMOOTH_SHADE_STONE).setWeight(6).apply(SetCount.setCount(RandomValueRange.between(8, 20))))));
		
		lootProcessor.accept(locationFor(LandTypes.WOOD, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL).setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(Items.STONE_AXE).setWeight(3))
						.add(ItemLootEntry.lootTableItem(Items.EMERALD).setWeight(5).apply(SetCount.setCount(RandomValueRange.between(2, 4))))
						.add(ItemLootEntry.lootTableItem(Items.SLIME_BALL).setWeight(6).apply(SetCount.setCount(RandomValueRange.between(2, 5))))
						.add(ItemLootEntry.lootTableItem(Items.CHARCOAL).setWeight(10).apply(SetCount.setCount(RandomValueRange.between(5, 10)))))
				.withPool(LootPool.lootPool().name(BLOCK_POOL).setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(Items.OAK_LOG).setWeight(10).apply(SetCount.setCount(RandomValueRange.between(8, 20))))
						.add(ItemLootEntry.lootTableItem(Items.SPRUCE_LOG).setWeight(6).apply(SetCount.setCount(RandomValueRange.between(8, 20))))
						.add(ItemLootEntry.lootTableItem(Items.BIRCH_LOG).setWeight(6).apply(SetCount.setCount(RandomValueRange.between(8, 20))))
						.add(ItemLootEntry.lootTableItem(Items.JUNGLE_LOG).setWeight(10).apply(SetCount.setCount(RandomValueRange.between(8, 20))))));
		
		lootProcessor.accept(locationFor(LandTypes.RAINBOW, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL).setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(Items.CLAY_BALL).setWeight(10).apply(SetCount.setCount(RandomValueRange.between(5, 10))))
						.add(ItemLootEntry.lootTableItem(Items.PAPER).setWeight(10).apply(SetCount.setCount(RandomValueRange.between(5, 10))))
						.add(ItemLootEntry.lootTableItem(Items.EGG).setWeight(3).apply(SetCount.setCount(RandomValueRange.between(1, 3)))))
				.withPool(LootPool.lootPool().name(BLOCK_POOL).setRolls(ConstantRange.exactly(1))));
		
		lootProcessor.accept(locationFor(LandTypes.FLORA, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL).setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(Items.EMERALD).setWeight(5).apply(SetCount.setCount(RandomValueRange.between(2, 4))))
						.add(ItemLootEntry.lootTableItem(Items.QUARTZ).setWeight(10).apply(SetCount.setCount(RandomValueRange.between(5, 10))))
						.add(ItemLootEntry.lootTableItem(Items.PRISMARINE_CRYSTALS).setWeight(6).apply(SetCount.setCount(RandomValueRange.between(2, 5)))))
				.withPool(LootPool.lootPool().name(BLOCK_POOL).setRolls(ConstantRange.exactly(1))));
		
		lootProcessor.accept(locationFor(LandTypes.FROGS, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL).setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(MSItems.BUG_NET).setWeight(5)))
				.withPool(LootPool.lootPool().name(BLOCK_POOL).setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(Items.LILY_PAD).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(2, 5))))));
		
		lootProcessor.accept(locationFor(LandTypes.WIND, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL).setRolls(ConstantRange.exactly(1)))
				.withPool(LootPool.lootPool().name(BLOCK_POOL).setRolls(ConstantRange.exactly(1))));
		
		lootProcessor.accept(locationFor(LandTypes.LIGHT, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL).setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(MSBlocks.GLOWYSTONE_DUST).setWeight(6).apply(SetCount.setCount(RandomValueRange.between(2, 5)))))
				.withPool(LootPool.lootPool().name(BLOCK_POOL).setRolls(ConstantRange.exactly(1))));
		
		lootProcessor.accept(locationFor(LandTypes.CLOCKWORK, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL).setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(Items.CLOCK).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(2, 5)))))
				.withPool(LootPool.lootPool().name(BLOCK_POOL).setRolls(ConstantRange.exactly(1))));
		
		lootProcessor.accept(locationFor(LandTypes.SILENCE, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL).setRolls(ConstantRange.exactly(1)))
				.withPool(LootPool.lootPool().name(BLOCK_POOL).setRolls(ConstantRange.exactly(1))));
		
		lootProcessor.accept(locationFor(LandTypes.THUNDER, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL).setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(MSItems.IRON_CANE).setWeight(3)))
				.withPool(LootPool.lootPool().name(BLOCK_POOL).setRolls(ConstantRange.exactly(1))));
		
		lootProcessor.accept(locationFor(LandTypes.PULSE, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL).setRolls(ConstantRange.exactly(1)))
				.withPool(LootPool.lootPool().name(BLOCK_POOL).setRolls(ConstantRange.exactly(1))));
		
		lootProcessor.accept(locationFor(LandTypes.THOUGHT, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL).setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(Items.BOOK).setWeight(10).apply(SetCount.setCount(RandomValueRange.between(2, 5)))))
				.withPool(LootPool.lootPool().name(BLOCK_POOL).setRolls(ConstantRange.exactly(1))));
		
		lootProcessor.accept(locationFor(LandTypes.BUCKETS, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL).setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(Items.BUCKET).setWeight(10).apply(SetCount.setCount(RandomValueRange.between(2, 5)))))
				.withPool(LootPool.lootPool().name(BLOCK_POOL).setRolls(ConstantRange.exactly(1))));
		
		lootProcessor.accept(locationFor(LandTypes.CAKE, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL).setRolls(ConstantRange.exactly(1)))
				.withPool(LootPool.lootPool().name(BLOCK_POOL).setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(MSBlocks.SUGAR_CUBE).setWeight(6).apply(SetCount.setCount(RandomValueRange.between(5, 10))))
						.add(ItemLootEntry.lootTableItem(MSBlocks.FUCHSIA_CAKE).setWeight(2).apply(SetCount.setCount(RandomValueRange.between(1, 3))))));
		
		lootProcessor.accept(locationFor(LandTypes.RABBITS, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL).setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(Items.RABBIT_FOOT).setWeight(5).apply(SetCount.setCount(RandomValueRange.between(2, 5)))))
				.withPool(LootPool.lootPool().name(BLOCK_POOL).setRolls(ConstantRange.exactly(1))));
		
		lootProcessor.accept(locationFor(LandTypes.MONSTERS, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL).setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(Items.ROTTEN_FLESH).setWeight(10).apply(SetCount.setCount(RandomValueRange.between(5, 10)))))
				.withPool(LootPool.lootPool().name(BLOCK_POOL).setRolls(ConstantRange.exactly(1))));
		lootProcessor.accept(locationFor(LandTypes.UNDEAD, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL).setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(Items.ROTTEN_FLESH).setWeight(10).apply(SetCount.setCount(RandomValueRange.between(5, 10)))))
				.withPool(LootPool.lootPool().name(BLOCK_POOL).setRolls(ConstantRange.exactly(1))));
		
		lootProcessor.accept(locationFor(LandTypes.TOWERS, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL).setRolls(ConstantRange.exactly(1)))
				.withPool(LootPool.lootPool().name(BLOCK_POOL).setRolls(ConstantRange.exactly(1))));
		
		
		lootProcessor.accept(MSLootTables.CONSORT_FOOD_STOCK, LootTable.lootTable()
				.withPool(LootPool.lootPool().name(MAIN_POOL).setRolls(ConstantRange.exactly(5))
						.add(LandTableLootEntry.builder(MSLootTables.CONSORT_FOOD_STOCK).setPool(MAIN_POOL))
						.add(ItemLootEntry.lootTableItem(MSItems.GRASSHOPPER).setWeight(5).apply(SetCount.setCount(RandomValueRange.between(3, 10)))))
				.withPool(LootPool.lootPool().name(SPECIAL_POOL).setRolls(RandomValueRange.between(1, 2))
						.add(LandTableLootEntry.builder(MSLootTables.CONSORT_FOOD_STOCK).setPool(SPECIAL_POOL))
						.add(ItemLootEntry.lootTableItem(Items.PUMPKIN_PIE).setWeight(3).apply(SetCount.setCount(RandomValueRange.between(1, 2))))
						.add(ItemLootEntry.lootTableItem(Items.GOLDEN_APPLE).setWeight(1))
						.add(ItemLootEntry.lootTableItem(MSItems.CANDY_CORN).setWeight(1).apply(SetCount.setCount(RandomValueRange.between(1, 4))))
						.add(TagLootEntry.expandTag(MSTags.Items.GRIST_CANDY).setWeight(1).apply(SetCount.setCount(RandomValueRange.between(1, 4))))));
		
		//Iguana
		lootProcessor.accept(locationFor(LandTypes.FOREST, MSLootTables.CONSORT_FOOD_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(MAIN_POOL).setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(MSItems.ONION).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(3, 10))))
						.add(ItemLootEntry.lootTableItem(MSItems.CONE_OF_FLIES).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(3, 10))))
						.add(ItemLootEntry.lootTableItem(Items.APPLE).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(3, 10))))
						.add(ItemLootEntry.lootTableItem(MSItems.CHOCOLATE_BEETLE).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(3, 10))))
						.add(ItemLootEntry.lootTableItem(Items.WHEAT).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(3, 10))))
						.add(ItemLootEntry.lootTableItem(Items.WHEAT_SEEDS).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(3, 10))))
						.add(ItemLootEntry.lootTableItem(Items.COCOA_BEANS).setWeight(2).apply(SetCount.setCount(RandomValueRange.between(3, 6)))))
				.withPool(LootPool.lootPool().name(SPECIAL_POOL).setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(Items.COOKIE).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(1, 3))))
						.add(ItemLootEntry.lootTableItem(MSBlocks.APPLE_CAKE).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(1, 3))))));
		lootProcessor.accept(locationFor(LandTypes.TAIGA, MSLootTables.CONSORT_FOOD_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(MAIN_POOL).setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(MSItems.ONION).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(3, 10))))
						.add(ItemLootEntry.lootTableItem(MSItems.CONE_OF_FLIES).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(3, 10))))
						.add(ItemLootEntry.lootTableItem(Items.APPLE).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(3, 10))))
						.add(ItemLootEntry.lootTableItem(MSItems.CHOCOLATE_BEETLE).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(3, 10))))
						.add(ItemLootEntry.lootTableItem(Items.WHEAT).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(3, 10))))
						.add(ItemLootEntry.lootTableItem(Items.WHEAT_SEEDS).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(3, 10))))
						.add(ItemLootEntry.lootTableItem(Items.COCOA_BEANS).setWeight(2).apply(SetCount.setCount(RandomValueRange.between(3, 6)))))
				.withPool(LootPool.lootPool().name(SPECIAL_POOL).setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(Items.COOKIE).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(1, 3))))
						.add(ItemLootEntry.lootTableItem(MSBlocks.APPLE_CAKE).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(1, 3))))));
		lootProcessor.accept(locationFor(LandTypes.FROST, MSLootTables.CONSORT_FOOD_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(MAIN_POOL).setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(MSItems.ONION).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(3, 10))))
						.add(ItemLootEntry.lootTableItem(MSItems.CONE_OF_FLIES).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(3, 10))))
						.add(ItemLootEntry.lootTableItem(Items.APPLE).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(3, 10))))
						.add(ItemLootEntry.lootTableItem(MSItems.CHOCOLATE_BEETLE).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(3, 10))))
						.add(ItemLootEntry.lootTableItem(Items.WHEAT).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(3, 10))))
						.add(ItemLootEntry.lootTableItem(Items.WHEAT_SEEDS).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(3, 10))))
						.add(TableLootEntry.lootTableReference(COLD_CAKE).setWeight(2)))
				.withPool(LootPool.lootPool().name(SPECIAL_POOL).setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(Items.COOKIE).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(1, 3))))
						.add(ItemLootEntry.lootTableItem(MSBlocks.APPLE_CAKE).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(1, 3))))));
		lootProcessor.accept(locationFor(LandTypes.FLORA, MSLootTables.CONSORT_FOOD_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(MAIN_POOL).setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(MSItems.ONION).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(3, 10))))
						.add(ItemLootEntry.lootTableItem(MSItems.CONE_OF_FLIES).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(3, 10))))
						.add(ItemLootEntry.lootTableItem(Items.APPLE).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(3, 10))))
						.add(ItemLootEntry.lootTableItem(MSItems.CHOCOLATE_BEETLE).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(3, 10))))
						.add(ItemLootEntry.lootTableItem(Items.WHEAT).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(3, 10))))
						.add(ItemLootEntry.lootTableItem(Items.WHEAT_SEEDS).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(3, 10))))
						.add(ItemLootEntry.lootTableItem(MSItems.STRAWBERRY_CHUNK).setWeight(5).apply(SetCount.setCount(RandomValueRange.between(8, 12)))))
				.withPool(LootPool.lootPool().name(SPECIAL_POOL).setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(Items.COOKIE).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(1, 3))))
						.add(ItemLootEntry.lootTableItem(MSBlocks.APPLE_CAKE).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(1, 3))))));
		
		//Salamander
		lootProcessor.accept(locationFor(LandTypes.FUNGI, MSLootTables.CONSORT_FOOD_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(MAIN_POOL).setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(MSItems.JAR_OF_BUGS).setWeight(5).apply(SetCount.setCount(RandomValueRange.between(3, 10))))
						.add(ItemLootEntry.lootTableItem(MSItems.BUG_ON_A_STICK).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(3, 10))))
						.add(ItemLootEntry.lootTableItem(MSItems.CONE_OF_FLIES).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(3, 10))))
						.add(ItemLootEntry.lootTableItem(Items.POTATO).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(3, 10))))
						.add(ItemLootEntry.lootTableItem(Items.MUSHROOM_STEW).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(3, 10))))
						.add(ItemLootEntry.lootTableItem(Items.RED_MUSHROOM).setWeight(4).apply(SetCount.setCount(RandomValueRange.between(3, 6))))
						.add(ItemLootEntry.lootTableItem(Items.BROWN_MUSHROOM).setWeight(4).apply(SetCount.setCount(RandomValueRange.between(3, 6))))
						.add(ItemLootEntry.lootTableItem(MSItems.FUNGAL_SPORE).setWeight(10).apply(SetCount.setCount(RandomValueRange.between(8, 12))))
						.add(ItemLootEntry.lootTableItem(MSItems.SPOREO).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(1, 4)))))
				.withPool(LootPool.lootPool().name(SPECIAL_POOL).setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(Items.POISONOUS_POTATO).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(1, 3))))
						.add(ItemLootEntry.lootTableItem(Items.MELON).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(3, 6))))));
		lootProcessor.accept(locationFor(LandTypes.SHADE, MSLootTables.CONSORT_FOOD_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(MAIN_POOL).setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(MSItems.JAR_OF_BUGS).setWeight(5).apply(SetCount.setCount(RandomValueRange.between(3, 10))))
						.add(ItemLootEntry.lootTableItem(MSItems.BUG_ON_A_STICK).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(3, 10))))
						.add(ItemLootEntry.lootTableItem(MSItems.CONE_OF_FLIES).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(3, 10))))
						.add(ItemLootEntry.lootTableItem(Items.POTATO).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(3, 10))))
						.add(ItemLootEntry.lootTableItem(Items.MUSHROOM_STEW).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(3, 10))))
						.add(ItemLootEntry.lootTableItem(MSBlocks.GLOWING_MUSHROOM).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(3, 6)))))
				.withPool(LootPool.lootPool().name(SPECIAL_POOL).setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(Items.POISONOUS_POTATO).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(1, 3))))
						.add(ItemLootEntry.lootTableItem(Items.MELON).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(3, 6))))));
		lootProcessor.accept(locationFor(LandTypes.WOOD, MSLootTables.CONSORT_FOOD_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(MAIN_POOL).setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(MSItems.JAR_OF_BUGS).setWeight(5).apply(SetCount.setCount(RandomValueRange.between(3, 10))))
						.add(ItemLootEntry.lootTableItem(MSItems.BUG_ON_A_STICK).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(3, 10))))
						.add(ItemLootEntry.lootTableItem(MSItems.CONE_OF_FLIES).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(3, 10))))
						.add(ItemLootEntry.lootTableItem(Items.POTATO).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(3, 10))))
						.add(ItemLootEntry.lootTableItem(Items.MUSHROOM_STEW).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(3, 10))))
						.add(ItemLootEntry.lootTableItem(Items.RED_MUSHROOM).setWeight(4).apply(SetCount.setCount(RandomValueRange.between(3, 6))))
						.add(ItemLootEntry.lootTableItem(Items.BROWN_MUSHROOM).setWeight(4).apply(SetCount.setCount(RandomValueRange.between(3, 6)))))
				.withPool(LootPool.lootPool().name(SPECIAL_POOL).setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(Items.POISONOUS_POTATO).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(1, 3))))
						.add(ItemLootEntry.lootTableItem(Items.MELON).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(3, 6))))));
		
		//Nakagator
		lootProcessor.accept(locationFor(LandTypes.HEAT, MSLootTables.CONSORT_FOOD_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(MAIN_POOL).setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(MSItems.ONION).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(3, 10))))
						.add(ItemLootEntry.lootTableItem(MSItems.CHOCOLATE_BEETLE).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(3, 10))))
						.add(ItemLootEntry.lootTableItem(Items.BEETROOT_SOUP).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(3, 10))))
						.add(ItemLootEntry.lootTableItem(Items.BEEF).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(3, 10))))
						.add(ItemLootEntry.lootTableItem(MSItems.STEAK_SWORD).setWeight(3))
						.add(ItemLootEntry.lootTableItem(Items.BEETROOT).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(3, 10))))
						.add(TableLootEntry.lootTableReference(HOT_CAKE).setWeight(2)))
				.withPool(LootPool.lootPool().name(SPECIAL_POOL).setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(MSItems.TAB).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(3, 6))))
						.add(ItemLootEntry.lootTableItem(MSItems.IRRADIATED_STEAK).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(1, 3))))));
		lootProcessor.accept(locationFor(LandTypes.ROCK, MSLootTables.CONSORT_FOOD_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(MAIN_POOL).setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(MSItems.ONION).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(3, 10))))
						.add(ItemLootEntry.lootTableItem(MSItems.CHOCOLATE_BEETLE).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(3, 10))))
						.add(ItemLootEntry.lootTableItem(Items.BEETROOT_SOUP).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(3, 10))))
						.add(ItemLootEntry.lootTableItem(Items.BEEF).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(3, 10))))
						.add(ItemLootEntry.lootTableItem(MSItems.STEAK_SWORD).setWeight(3))
						.add(ItemLootEntry.lootTableItem(Items.BEETROOT).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(3, 10))))
						.add(ItemLootEntry.lootTableItem(MSItems.ROCK_COOKIE).setWeight(5).apply(SetCount.setCount(RandomValueRange.between(8, 12)))))
				.withPool(LootPool.lootPool().name(SPECIAL_POOL).setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(MSItems.TAB).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(3, 6))))
						.add(ItemLootEntry.lootTableItem(MSItems.IRRADIATED_STEAK).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(1, 3))))));
		lootProcessor.accept(locationFor(LandTypes.PETRIFICATION, MSLootTables.CONSORT_FOOD_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(MAIN_POOL).setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(MSItems.ONION).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(3, 10))))
						.add(ItemLootEntry.lootTableItem(MSItems.CHOCOLATE_BEETLE).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(3, 10))))
						.add(ItemLootEntry.lootTableItem(Items.BEETROOT_SOUP).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(3, 10))))
						.add(ItemLootEntry.lootTableItem(Items.BEEF).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(3, 10))))
						.add(ItemLootEntry.lootTableItem(MSItems.STEAK_SWORD).setWeight(3))
						.add(ItemLootEntry.lootTableItem(Items.BEETROOT).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(3, 10))))
						.add(ItemLootEntry.lootTableItem(MSItems.ROCK_COOKIE).setWeight(5).apply(SetCount.setCount(RandomValueRange.between(8, 12)))))
				.withPool(LootPool.lootPool().name(SPECIAL_POOL).setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(MSItems.TAB).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(3, 6))))
						.add(ItemLootEntry.lootTableItem(MSItems.IRRADIATED_STEAK).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(1, 3))))));
		lootProcessor.accept(locationFor(LandTypes.END, MSLootTables.CONSORT_FOOD_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(MAIN_POOL).setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(MSItems.ONION).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(3, 10))))
						.add(ItemLootEntry.lootTableItem(MSItems.CHOCOLATE_BEETLE).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(3, 10))))
						.add(ItemLootEntry.lootTableItem(Items.BEETROOT_SOUP).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(3, 10))))
						.add(ItemLootEntry.lootTableItem(Items.BEEF).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(3, 10))))
						.add(ItemLootEntry.lootTableItem(MSItems.STEAK_SWORD).setWeight(3))
						.add(ItemLootEntry.lootTableItem(Items.BEETROOT).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(3, 10))))
						.add(ItemLootEntry.lootTableItem(Items.CHORUS_FRUIT).setWeight(5).apply(SetCount.setCount(RandomValueRange.between(8, 12)))))
				.withPool(LootPool.lootPool().name(SPECIAL_POOL).setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(MSItems.TAB).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(3, 6))))
						.add(ItemLootEntry.lootTableItem(MSItems.IRRADIATED_STEAK).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(1, 3))))));
		
		//Turtle
		lootProcessor.accept(locationFor(LandTypes.SAND, MSLootTables.CONSORT_FOOD_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(MAIN_POOL).setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(MSItems.JAR_OF_BUGS).setWeight(5).apply(SetCount.setCount(RandomValueRange.between(3, 10))))
						.add(ItemLootEntry.lootTableItem(MSItems.BUG_ON_A_STICK).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(3, 10))))
						.add(ItemLootEntry.lootTableItem(Items.POTATO).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(3, 10))))
						.add(ItemLootEntry.lootTableItem(MSItems.SALAD).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(3, 10))))
						.add(ItemLootEntry.lootTableItem(Items.CARROT).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(3, 10))))
						.add(ItemLootEntry.lootTableItem(Items.POTION).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(3, 6))).apply(SetNBT.setTag(waterNBT())))
						.add(ItemLootEntry.lootTableItem(MSItems.DESERT_FRUIT).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(15, 20)))))
				.withPool(LootPool.lootPool().name(SPECIAL_POOL).setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(MSBlocks.GOLD_SEEDS).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(1, 3))))
						.add(ItemLootEntry.lootTableItem(Items.COD).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(1, 3))))));
		lootProcessor.accept(locationFor(LandTypes.RED_SAND, MSLootTables.CONSORT_FOOD_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(MAIN_POOL).setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(MSItems.JAR_OF_BUGS).setWeight(5).apply(SetCount.setCount(RandomValueRange.between(3, 10))))
						.add(ItemLootEntry.lootTableItem(MSItems.BUG_ON_A_STICK).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(3, 10))))
						.add(ItemLootEntry.lootTableItem(Items.POTATO).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(3, 10))))
						.add(ItemLootEntry.lootTableItem(MSItems.SALAD).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(3, 10))))
						.add(ItemLootEntry.lootTableItem(Items.CARROT).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(3, 10))))
						.add(ItemLootEntry.lootTableItem(Items.POTION).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(3, 6))).apply(SetNBT.setTag(waterNBT())))
						.add(ItemLootEntry.lootTableItem(MSItems.DESERT_FRUIT).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(15, 20)))))
				.withPool(LootPool.lootPool().name(SPECIAL_POOL).setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(MSBlocks.GOLD_SEEDS).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(1, 3))))
						.add(ItemLootEntry.lootTableItem(Items.COD).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(1, 3))))));
		lootProcessor.accept(locationFor(LandTypes.LUSH_DESERTS, MSLootTables.CONSORT_FOOD_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(MAIN_POOL).setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(MSItems.JAR_OF_BUGS).setWeight(5).apply(SetCount.setCount(RandomValueRange.between(3, 10))))
						.add(ItemLootEntry.lootTableItem(MSItems.BUG_ON_A_STICK).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(3, 10))))
						.add(ItemLootEntry.lootTableItem(Items.POTATO).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(3, 10))))
						.add(ItemLootEntry.lootTableItem(MSItems.SALAD).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(3, 10))))
						.add(ItemLootEntry.lootTableItem(Items.CARROT).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(3, 10))))
						.add(ItemLootEntry.lootTableItem(Items.POTION).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(3, 6))).apply(SetNBT.setTag(waterNBT())))
						.add(ItemLootEntry.lootTableItem(MSItems.DESERT_FRUIT).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(15, 20)))))
				.withPool(LootPool.lootPool().name(SPECIAL_POOL).setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(MSBlocks.GOLD_SEEDS).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(1, 3))))
						.add(ItemLootEntry.lootTableItem(Items.COD).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(1, 3))))));
		lootProcessor.accept(locationFor(LandTypes.SANDSTONE, MSLootTables.CONSORT_FOOD_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(MAIN_POOL).setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(MSItems.JAR_OF_BUGS).setWeight(5).apply(SetCount.setCount(RandomValueRange.between(3, 10))))
						.add(ItemLootEntry.lootTableItem(MSItems.BUG_ON_A_STICK).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(3, 10))))
						.add(ItemLootEntry.lootTableItem(Items.POTATO).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(3, 10))))
						.add(ItemLootEntry.lootTableItem(MSItems.SALAD).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(3, 10))))
						.add(ItemLootEntry.lootTableItem(Items.CARROT).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(3, 10)))))
				.withPool(LootPool.lootPool().name(SPECIAL_POOL).setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(MSBlocks.GOLD_SEEDS).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(1, 3))))
						.add(ItemLootEntry.lootTableItem(Items.COD).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(1, 3))))));
		lootProcessor.accept(locationFor(LandTypes.RED_SANDSTONE, MSLootTables.CONSORT_FOOD_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(MAIN_POOL).setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(MSItems.JAR_OF_BUGS).setWeight(5).apply(SetCount.setCount(RandomValueRange.between(3, 10))))
						.add(ItemLootEntry.lootTableItem(MSItems.BUG_ON_A_STICK).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(3, 10))))
						.add(ItemLootEntry.lootTableItem(Items.POTATO).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(3, 10))))
						.add(ItemLootEntry.lootTableItem(MSItems.SALAD).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(3, 10))))
						.add(ItemLootEntry.lootTableItem(Items.CARROT).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(3, 10)))))
				.withPool(LootPool.lootPool().name(SPECIAL_POOL).setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(MSBlocks.GOLD_SEEDS).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(1, 3))))
						.add(ItemLootEntry.lootTableItem(Items.COD).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(1, 3))))));
		lootProcessor.accept(locationFor(LandTypes.RAINBOW, MSLootTables.CONSORT_FOOD_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(MAIN_POOL).setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(MSItems.JAR_OF_BUGS).setWeight(5).apply(SetCount.setCount(RandomValueRange.between(3, 10))))
						.add(ItemLootEntry.lootTableItem(MSItems.BUG_ON_A_STICK).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(3, 10))))
						.add(ItemLootEntry.lootTableItem(Items.POTATO).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(3, 10))))
						.add(ItemLootEntry.lootTableItem(MSItems.SALAD).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(3, 10))))
						.add(ItemLootEntry.lootTableItem(Items.CARROT).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(3, 10))))
						.add(ItemLootEntry.lootTableItem(Items.BREAD).setWeight(5).apply(SetCount.setCount(RandomValueRange.between(8, 12)))))
				.withPool(LootPool.lootPool().name(SPECIAL_POOL).setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(MSBlocks.GOLD_SEEDS).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(1, 3))))
						.add(ItemLootEntry.lootTableItem(Items.COD).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(1, 3))))));
		lootProcessor.accept(locationFor(LandTypes.RAIN, MSLootTables.CONSORT_FOOD_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(MAIN_POOL).setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(MSItems.JAR_OF_BUGS).setWeight(5).apply(SetCount.setCount(RandomValueRange.between(3, 10))))
						.add(ItemLootEntry.lootTableItem(MSItems.BUG_ON_A_STICK).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(3, 10))))
						.add(ItemLootEntry.lootTableItem(Items.POTATO).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(3, 10))))
						.add(ItemLootEntry.lootTableItem(MSItems.SALAD).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(3, 10))))
						.add(ItemLootEntry.lootTableItem(Items.CARROT).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(3, 10))))
						.add(ItemLootEntry.lootTableItem(Items.MELON).setWeight(5).apply(SetCount.setCount(RandomValueRange.between(8, 12)))))
				.withPool(LootPool.lootPool().name(SPECIAL_POOL).setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(MSBlocks.GOLD_SEEDS).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(1, 3))))
						.add(ItemLootEntry.lootTableItem(Items.COD).setWeight(8).apply(SetCount.setCount(RandomValueRange.between(1, 3))))));
		
		lootProcessor.accept(locationFor(LandTypes.FROGS, MSLootTables.CONSORT_FOOD_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(MAIN_POOL).setRolls(ConstantRange.exactly(1)))
				.withPool(LootPool.lootPool().name(SPECIAL_POOL).setRolls(ConstantRange.exactly(1))));
		
		lootProcessor.accept(locationFor(LandTypes.WIND, MSLootTables.CONSORT_FOOD_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(MAIN_POOL).setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(MSItems.COTTON_CANDY_FAYGO).setWeight(2).apply(SetCount.setCount(RandomValueRange.between(1, 3)))))
				.withPool(LootPool.lootPool().name(SPECIAL_POOL).setRolls(ConstantRange.exactly(1))));
		
		lootProcessor.accept(locationFor(LandTypes.LIGHT, MSLootTables.CONSORT_FOOD_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(MAIN_POOL).setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(MSItems.ORANGE_FAYGO).setWeight(2).apply(SetCount.setCount(RandomValueRange.between(1, 3)))))
				.withPool(LootPool.lootPool().name(SPECIAL_POOL).setRolls(ConstantRange.exactly(1))));
		
		lootProcessor.accept(locationFor(LandTypes.CLOCKWORK, MSLootTables.CONSORT_FOOD_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(MAIN_POOL).setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(MSItems.CANDY_APPLE_FAYGO).setWeight(2).apply(SetCount.setCount(RandomValueRange.between(1, 3)))))
				.withPool(LootPool.lootPool().name(SPECIAL_POOL).setRolls(ConstantRange.exactly(1))));
		
		lootProcessor.accept(locationFor(LandTypes.SILENCE, MSLootTables.CONSORT_FOOD_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(MAIN_POOL).setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(MSItems.FAYGO_COLA).setWeight(2).apply(SetCount.setCount(RandomValueRange.between(1, 3)))))
				.withPool(LootPool.lootPool().name(SPECIAL_POOL).setRolls(ConstantRange.exactly(1))));
		
		lootProcessor.accept(locationFor(LandTypes.THUNDER, MSLootTables.CONSORT_FOOD_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(MAIN_POOL).setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(MSItems.PEACH_FAYGO).setWeight(2).apply(SetCount.setCount(RandomValueRange.between(1, 3)))))
				.withPool(LootPool.lootPool().name(SPECIAL_POOL).setRolls(ConstantRange.exactly(1))));
		
		lootProcessor.accept(locationFor(LandTypes.PULSE, MSLootTables.CONSORT_FOOD_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(MAIN_POOL).setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(MSItems.BEEF_SWORD).setWeight(4)).when(ConsortLootCondition.builder(EnumConsort.NAKAGATOR))
						.add(ItemLootEntry.lootTableItem(MSItems.REDPOP_FAYGO).setWeight(2).apply(SetCount.setCount(RandomValueRange.between(1, 3)))))
				.withPool(LootPool.lootPool().name(SPECIAL_POOL).setRolls(ConstantRange.exactly(1))));
		
		lootProcessor.accept(locationFor(LandTypes.THOUGHT, MSLootTables.CONSORT_FOOD_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(MAIN_POOL).setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(MSItems.MOON_MIST_FAYGO).setWeight(2).apply(SetCount.setCount(RandomValueRange.between(1, 3)))))
				.withPool(LootPool.lootPool().name(SPECIAL_POOL).setRolls(ConstantRange.exactly(1))));
		
		lootProcessor.accept(locationFor(LandTypes.BUCKETS, MSLootTables.CONSORT_FOOD_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(MAIN_POOL).setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(Items.MILK_BUCKET).setWeight(2).apply(SetCount.setCount(RandomValueRange.between(1, 3)))))
				.withPool(LootPool.lootPool().name(SPECIAL_POOL).setRolls(ConstantRange.exactly(1))));
		
		lootProcessor.accept(locationFor(LandTypes.CAKE, MSLootTables.CONSORT_FOOD_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(MAIN_POOL).setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(Items.SUGAR).setWeight(2).apply(SetCount.setCount(RandomValueRange.between(3, 6)))))
				.withPool(LootPool.lootPool().name(SPECIAL_POOL).setRolls(ConstantRange.exactly(1))));
		
		lootProcessor.accept(locationFor(LandTypes.RABBITS, MSLootTables.CONSORT_FOOD_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(MAIN_POOL).setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(Items.RABBIT_STEW).setWeight(2).apply(SetCount.setCount(RandomValueRange.between(2, 4)))))
				.withPool(LootPool.lootPool().name(SPECIAL_POOL).setRolls(ConstantRange.exactly(1))));
		
		lootProcessor.accept(locationFor(LandTypes.MONSTERS, MSLootTables.CONSORT_FOOD_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(MAIN_POOL).setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(MSItems.GRAPE_FAYGO).setWeight(2).apply(SetCount.setCount(RandomValueRange.between(1, 3)))))
				.withPool(LootPool.lootPool().name(SPECIAL_POOL).setRolls(ConstantRange.exactly(1))));
		lootProcessor.accept(locationFor(LandTypes.UNDEAD, MSLootTables.CONSORT_FOOD_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(MAIN_POOL).setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(MSItems.GRAPE_FAYGO).setWeight(2).apply(SetCount.setCount(RandomValueRange.between(1, 3)))))
				.withPool(LootPool.lootPool().name(SPECIAL_POOL).setRolls(ConstantRange.exactly(1))));
		
		lootProcessor.accept(locationFor(LandTypes.TOWERS, MSLootTables.CONSORT_FOOD_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(MAIN_POOL).setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(MSItems.CREME_SODA_FAYGO).setWeight(2).apply(SetCount.setCount(RandomValueRange.between(1, 3)))))
				.withPool(LootPool.lootPool().name(SPECIAL_POOL).setRolls(ConstantRange.exactly(1))));
		
		
		lootProcessor.accept(MSLootTables.CONSORT_JUNK_REWARD, LootTable.lootTable()
				.withPool(LootPool.lootPool().name("main").setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(Items.OAK_PLANKS).setWeight(2))
						.add(ItemLootEntry.lootTableItem(Items.SPRUCE_PLANKS).setWeight(2))
						.add(ItemLootEntry.lootTableItem(Items.BIRCH_PLANKS).setWeight(2))
						.add(ItemLootEntry.lootTableItem(Items.JUNGLE_PLANKS).setWeight(2))
						.add(ItemLootEntry.lootTableItem(Items.DARK_OAK_PLANKS).setWeight(2))
						.add(ItemLootEntry.lootTableItem(Items.ACACIA_PLANKS).setWeight(2))
						.add(ItemLootEntry.lootTableItem(Items.WOODEN_AXE).setWeight(5))
						.add(ItemLootEntry.lootTableItem(Items.WOODEN_PICKAXE).setWeight(5))
						.add(ItemLootEntry.lootTableItem(Items.WOODEN_HOE).setWeight(5))
						.add(ItemLootEntry.lootTableItem(Items.WOODEN_SHOVEL).setWeight(5))
						.add(ItemLootEntry.lootTableItem(Items.ROTTEN_FLESH).setWeight(8))
						.add(ItemLootEntry.lootTableItem(MSBlocks.GENERIC_OBJECT).setWeight(10))
						.add(ItemLootEntry.lootTableItem(Items.POISONOUS_POTATO).setWeight(10))
						.add(ItemLootEntry.lootTableItem(Items.RABBIT_HIDE).setWeight(10))
						.add(ItemLootEntry.lootTableItem(Items.DEAD_BUSH).setWeight(10))
						.add(ItemLootEntry.lootTableItem(Items.OAK_STAIRS).setWeight(5))
						.add(ItemLootEntry.lootTableItem(Items.FLOWER_POT).setWeight(5))
						.add(ItemLootEntry.lootTableItem(Items.BIRCH_BUTTON).setWeight(5))
						.add(ItemLootEntry.lootTableItem(MSItems.SORD).setWeight(10))
						.add(ItemLootEntry.lootTableItem(MSItems.CROWBAR).setWeight(3).apply(SetDamage.setDamage(RandomValueRange.between(0.05F, 0.7F))))
						.add(ItemLootEntry.lootTableItem(Items.GOLDEN_SHOVEL).setWeight(3).apply(SetDamage.setDamage(RandomValueRange.between(0.05F, 0.05F))))
						.add(ItemLootEntry.lootTableItem(Items.GOLDEN_AXE).setWeight(3).apply(SetDamage.setDamage(RandomValueRange.between(0.05F, 0.05F))))
						.add(ItemLootEntry.lootTableItem(Items.GOLDEN_SWORD).setWeight(3).apply(SetDamage.setDamage(RandomValueRange.between(0.05F, 0.05F))))
						.add(ItemLootEntry.lootTableItem(Items.GOLDEN_PICKAXE).setWeight(3).apply(SetDamage.setDamage(RandomValueRange.between(0.05F, 0.05F))))
						.add(ItemLootEntry.lootTableItem(MSItems.SBAHJ_POSTER).setWeight(10))
						.add(ItemLootEntry.lootTableItem(MSItems.CREW_POSTER).setWeight(10))
						.add(ItemLootEntry.lootTableItem(MSItems.THRESH_DVD).setWeight(10))
						.add(ItemLootEntry.lootTableItem(MSItems.ONION).setWeight(5).when(ConsortLootCondition.builder(EnumConsort.NAKAGATOR)))
						.add(ItemLootEntry.lootTableItem(MSItems.TAB).setWeight(5).when(ConsortLootCondition.builder(EnumConsort.SALAMANDER)))
						.add(ItemLootEntry.lootTableItem(MSItems.JAR_OF_BUGS).setWeight(5).when(ConsortLootCondition.builder(EnumConsort.TURTLE)))
						.add(ItemLootEntry.lootTableItem(MSItems.GRASSHOPPER).setWeight(5).when(ConsortLootCondition.builder(EnumConsort.IGUANA)))));
		
	}
	
	private static CompoundNBT waterNBT()
	{
		return Objects.requireNonNull(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.WATER).getTag());
	}
}