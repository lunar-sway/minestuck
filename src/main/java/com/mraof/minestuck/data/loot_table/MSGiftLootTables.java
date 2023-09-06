package com.mraof.minestuck.data.loot_table;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.entity.consort.EnumConsort;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.item.loot.LandTableLootEntry;
import com.mraof.minestuck.item.loot.MSLootTables;
import com.mraof.minestuck.item.loot.conditions.ConsortLootCondition;
import com.mraof.minestuck.util.MSTags;
import com.mraof.minestuck.world.lands.LandTypes;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.minecraft.world.level.storage.loot.entries.TagEntry;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemDamageFunction;
import net.minecraft.world.level.storage.loot.functions.SetNbtFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.Objects;
import java.util.function.BiConsumer;

import static com.mraof.minestuck.data.loot_table.MSChestLootTables.locationForTerrain;
import static com.mraof.minestuck.data.loot_table.MSChestLootTables.locationForTitle;

public class MSGiftLootTables implements LootTableSubProvider
{
	private static final ResourceLocation COLD_CAKE = new ResourceLocation("minestuck", "gameplay/special/cold_cake");
	private static final ResourceLocation HOT_CAKE = new ResourceLocation("minestuck", "gameplay/special/hot_cake");
	
	//Pools in consort general stock
	public static final String ITEM_POOL = "item", BLOCK_POOL = "block";
	public static final String MAIN_POOL = "main", SPECIAL_POOL = "special";
	
	@Override
	public void generate(BiConsumer<ResourceLocation, LootTable.Builder> lootProcessor)
	{
		lootProcessor.accept(COLD_CAKE, LootTable.lootTable()
				.withPool(LootPool.lootPool().name("blue_cake").setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(MSBlocks.BLUE_CAKE.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3)))))
				.withPool(LootPool.lootPool().name("cold_cake").setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(MSBlocks.COLD_CAKE.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))));
		lootProcessor.accept(HOT_CAKE, LootTable.lootTable()
				.withPool(LootPool.lootPool().name("red_cake").setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(MSBlocks.RED_CAKE.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3)))))
				.withPool(LootPool.lootPool().name("hot_cake").setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(MSBlocks.HOT_CAKE.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))));
		
		lootProcessor.accept(MSLootTables.CONSORT_GENERAL_STOCK, LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL).setRolls(ConstantValue.exactly(4))
						.add(LandTableLootEntry.builder(MSLootTables.CONSORT_GENERAL_STOCK).setPool(ITEM_POOL))
						.add(LootItem.lootTableItem(MSItems.CARVING_TOOL.get()).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 5))))
						.add(LootItem.lootTableItem(MSBlocks.MINI_FROG_STATUE.get()).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 5))))
						.add(LootItem.lootTableItem(MSBlocks.MINI_WIZARD_STATUE.get()).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 5))))
						.add(LootItem.lootTableItem(MSBlocks.MINI_TYPHEUS_STATUE.get()).setWeight(1))
						.add(LootItem.lootTableItem(MSItems.STONE_TABLET.get()).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 5))))
						.add(LootItem.lootTableItem(MSItems.THRESH_DVD.get()).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))
						.add(LootItem.lootTableItem(MSItems.CREW_POSTER.get()).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))
						.add(LootItem.lootTableItem(MSItems.SBAHJ_POSTER.get()).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))
						.add(LootItem.lootTableItem(MSItems.GAMEBRO_MAGAZINE.get()).setWeight(5).when(ConsortLootCondition.builder(EnumConsort.SALAMANDER, EnumConsort.NAKAGATOR)))
						.add(LootItem.lootTableItem(MSItems.GAMEGRL_MAGAZINE.get()).setWeight(5).when(ConsortLootCondition.builder(EnumConsort.TURTLE, EnumConsort.IGUANA)))
						.add(LootItem.lootTableItem(MSItems.ACE_OF_CLUBS.get()).setWeight(2).when(ConsortLootCondition.builder(EnumConsort.SALAMANDER)))
						.add(LootItem.lootTableItem(MSItems.ACE_OF_SPADES.get()).setWeight(2).when(ConsortLootCondition.builder(EnumConsort.NAKAGATOR)))
						.add(LootItem.lootTableItem(MSItems.ACE_OF_HEARTS.get()).setWeight(2).when(ConsortLootCondition.builder(EnumConsort.TURTLE)))
						.add(LootItem.lootTableItem(MSItems.ACE_OF_DIAMONDS.get()).setWeight(2).when(ConsortLootCondition.builder(EnumConsort.IGUANA)))
						.add(LootItem.lootTableItem(MSItems.CLUBS_SUITARANG.get()).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 5))))
						.add(LootItem.lootTableItem(MSItems.DIAMONDS_SUITARANG.get()).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 5))))
						.add(LootItem.lootTableItem(MSItems.HEARTS_SUITARANG.get()).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 5))))
						.add(LootItem.lootTableItem(MSItems.SPADES_SUITARANG.get()).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 5))))
						.add(LootItem.lootTableItem(MSItems.MUSIC_DISC_EMISSARY_OF_DANCE.get()).setWeight(1))
						.add(LootItem.lootTableItem(MSItems.MUSIC_DISC_DANCE_STAB_DANCE.get()).setWeight(1))
						.add(LootItem.lootTableItem(MSItems.ELECTRIC_AUTOHARP.get()).setWeight(1))
						.add(LootItem.lootTableItem(MSItems.BATTERY.get()).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(1,4))))
						.add(LootItem.lootTableItem(MSItems.GRIMOIRE.get()).setWeight(1).when(ConsortLootCondition.builder(EnumConsort.TURTLE)))
						.add(LootItem.lootTableItem(MSItems.PLUSH_IGUANA.get()).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))).when(ConsortLootCondition.builder(EnumConsort.IGUANA)))
						.add(LootItem.lootTableItem(MSItems.PLUSH_NAKAGATOR.get()).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))).when(ConsortLootCondition.builder(EnumConsort.NAKAGATOR)))
						.add(LootItem.lootTableItem(MSItems.PLUSH_SALAMANDER.get()).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))).when(ConsortLootCondition.builder(EnumConsort.SALAMANDER)))
						.add(LootItem.lootTableItem(MSItems.PLUSH_TURTLE.get()).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))).when(ConsortLootCondition.builder(EnumConsort.TURTLE)))
						.add(LootItem.lootTableItem(MSItems.CRUMPLY_HAT.get()).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 5))).when(ConsortLootCondition.builder(EnumConsort.SALAMANDER))))
				.withPool(LootPool.lootPool().name(BLOCK_POOL).setRolls(ConstantValue.exactly(2))
						.add(LandTableLootEntry.builder(MSLootTables.CONSORT_GENERAL_STOCK).setPool(BLOCK_POOL))));
		
		lootProcessor.accept(locationForTerrain(LandTypes.FOREST, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL).setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(MSItems.COPSE_CRUSHER.get()).setWeight(3))
						.add(LootItem.lootTableItem(Items.OAK_SAPLING).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(5, 10))))
						.add(LootItem.lootTableItem(Items.SPRUCE_SAPLING).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(5, 10))))
						.add(LootItem.lootTableItem(Items.BIRCH_SAPLING).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(5, 10))))
						.add(LootItem.lootTableItem(Items.JUNGLE_SAPLING).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(5, 10))))
						.add(LootItem.lootTableItem(Items.DARK_OAK_SAPLING).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(5, 10))))
						.add(LootItem.lootTableItem(Items.ACACIA_SAPLING).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(5, 10))))
						.add(LootItem.lootTableItem(Items.EMERALD).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 4)))))
				.withPool(LootPool.lootPool().name(BLOCK_POOL).setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(MSBlocks.VINE_LOG.get()).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(8, 20))))
						.add(LootItem.lootTableItem(MSBlocks.FLOWERY_VINE_LOG.get()).setWeight(6).apply(SetItemCountFunction.setCount(UniformGenerator.between(8, 20))))
						.add(LootItem.lootTableItem(Items.DARK_OAK_LOG).setWeight(6).apply(SetItemCountFunction.setCount(UniformGenerator.between(8, 20))))
						.add(LootItem.lootTableItem(Items.OAK_PLANKS).setWeight(6).apply(SetItemCountFunction.setCount(UniformGenerator.between(8, 20))))));
		lootProcessor.accept(locationForTerrain(LandTypes.TAIGA, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL).setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(MSItems.COPSE_CRUSHER.get()).setWeight(3))
						.add(LootItem.lootTableItem(Items.OAK_SAPLING).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(5, 10))))
						.add(LootItem.lootTableItem(Items.SPRUCE_SAPLING).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(5, 10))))
						.add(LootItem.lootTableItem(Items.BIRCH_SAPLING).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(5, 10))))
						.add(LootItem.lootTableItem(Items.JUNGLE_SAPLING).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(5, 10))))
						.add(LootItem.lootTableItem(Items.DARK_OAK_SAPLING).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(5, 10))))
						.add(LootItem.lootTableItem(Items.ACACIA_SAPLING).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(5, 10))))
						.add(LootItem.lootTableItem(Items.EMERALD).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 4)))))
				.withPool(LootPool.lootPool().name(BLOCK_POOL).setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(Items.SPRUCE_LOG).setWeight(6).apply(SetItemCountFunction.setCount(UniformGenerator.between(8, 20))))
						.add(LootItem.lootTableItem(Items.DARK_OAK_LOG).setWeight(6).apply(SetItemCountFunction.setCount(UniformGenerator.between(8, 20))))
						.add(LootItem.lootTableItem(Items.OAK_PLANKS).setWeight(6).apply(SetItemCountFunction.setCount(UniformGenerator.between(8, 20))))));
		
		lootProcessor.accept(locationForTerrain(LandTypes.FROST, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL).setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(MSItems.KATANA.get()).setWeight(3))
						.add(LootItem.lootTableItem(Items.DIAMOND).setWeight(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 5))))
						.add(LootItem.lootTableItem(Items.PRISMARINE_CRYSTALS).setWeight(6).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 5))))
						.add(LootItem.lootTableItem(Items.PRISMARINE_SHARD).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(5, 10)))))
				.withPool(LootPool.lootPool().name(BLOCK_POOL).setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(MSBlocks.FROST_BRICKS.get()).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(8, 20))))
						.add(LootItem.lootTableItem(MSBlocks.FROST_TILE.get()).setWeight(6).apply(SetItemCountFunction.setCount(UniformGenerator.between(8, 20))))
						.add(LootItem.lootTableItem(Items.PRISMARINE).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(8, 20))))
						.add(LootItem.lootTableItem(Items.PRISMARINE_BRICKS).setWeight(6).apply(SetItemCountFunction.setCount(UniformGenerator.between(8, 20))))
						.add(LootItem.lootTableItem(MSBlocks.FROST_LOG.get()).setWeight(6).apply(SetItemCountFunction.setCount(UniformGenerator.between(8, 20))))));
		
		lootProcessor.accept(locationForTerrain(LandTypes.FUNGI, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL).setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(Items.REDSTONE).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(5, 10)))))
				.withPool(LootPool.lootPool().name(BLOCK_POOL).setRolls(ConstantValue.exactly(1))));
		lootProcessor.accept(locationForTerrain(LandTypes.END, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL).setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(Items.ELYTRA).setWeight(3))
						.add(LootItem.lootTableItem(Items.OBSIDIAN).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(5, 10))))
						.add(LootItem.lootTableItem(Items.DRAGON_BREATH).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 5)))))
				.withPool(LootPool.lootPool().name(BLOCK_POOL).setRolls(ConstantValue.exactly(1))));
		
		lootProcessor.accept(locationForTerrain(LandTypes.HEAT, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL).setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(MSItems.FIRE_POKER.get()).setWeight(3))
						.add(LootItem.lootTableItem(Items.QUARTZ).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(5, 10))))
						.add(LootItem.lootTableItem(Items.BLAZE_POWDER).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 5))))
						.add(LootItem.lootTableItem(Items.NETHER_BRICK).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(5, 10)))))
				.withPool(LootPool.lootPool().name(BLOCK_POOL).setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(MSBlocks.CAST_IRON.get()).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(8, 20))))
						.add(LootItem.lootTableItem(MSBlocks.CHISELED_CAST_IRON.get()).setWeight(6).apply(SetItemCountFunction.setCount(UniformGenerator.between(8, 20))))
						.add(LootItem.lootTableItem(Items.NETHER_BRICKS).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(8, 20))))
						.add(LootItem.lootTableItem(Items.RED_NETHER_BRICKS).setWeight(6).apply(SetItemCountFunction.setCount(UniformGenerator.between(8, 20))))));
		
		lootProcessor.accept(locationForTerrain(LandTypes.ROCK, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL).setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(MSItems.METAL_BAT.get()).setWeight(3))
						.add(LootItem.lootTableItem(Items.COAL).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(5, 10))))
						.add(LootItem.lootTableItem(Items.CLAY_BALL).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(5, 10))))
						.add(LootItem.lootTableItem(Items.IRON_INGOT).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(5, 10)))))
				.withPool(LootPool.lootPool().name(BLOCK_POOL).setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(MSBlocks.COARSE_STONE.get()).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(8, 20))))
						.add(LootItem.lootTableItem(MSBlocks.CHISELED_COARSE_STONE.get()).setWeight(6).apply(SetItemCountFunction.setCount(UniformGenerator.between(8, 20))))
						.add(LootItem.lootTableItem(Items.POLISHED_ANDESITE).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(8, 20))))
						.add(LootItem.lootTableItem(Items.POLISHED_GRANITE).setWeight(6).apply(SetItemCountFunction.setCount(UniformGenerator.between(8, 20))))));
		lootProcessor.accept(locationForTerrain(LandTypes.PETRIFICATION, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL).setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(MSItems.METAL_BAT.get()).setWeight(3))
						.add(LootItem.lootTableItem(Items.COAL).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(5, 10))))
						.add(LootItem.lootTableItem(Items.CLAY_BALL).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(5, 10))))
						.add(LootItem.lootTableItem(Items.IRON_INGOT).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(5, 10)))))
				.withPool(LootPool.lootPool().name(BLOCK_POOL).setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(MSBlocks.COARSE_STONE.get()).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(8, 20))))
						.add(LootItem.lootTableItem(MSBlocks.CHISELED_COARSE_STONE.get()).setWeight(6).apply(SetItemCountFunction.setCount(UniformGenerator.between(8, 20))))
						.add(LootItem.lootTableItem(Items.POLISHED_ANDESITE).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(8, 20))))
						.add(LootItem.lootTableItem(Items.POLISHED_GRANITE).setWeight(6).apply(SetItemCountFunction.setCount(UniformGenerator.between(8, 20))))));
		
		lootProcessor.accept(locationForTerrain(LandTypes.SAND, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL).setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(MSItems.CACTACEAE_CUTLASS.get()).setWeight(3))
						.add(LootItem.lootTableItem(Items.GOLD_INGOT).setWeight(7).apply(SetItemCountFunction.setCount(UniformGenerator.between(5, 10))))
						.add(LootItem.lootTableItem(Items.LEATHER).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 5)))))
				.withPool(LootPool.lootPool().name(BLOCK_POOL).setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(Items.CACTUS).setWeight(6).apply(SetItemCountFunction.setCount(UniformGenerator.between(8, 20))))
						.add(LootItem.lootTableItem(MSBlocks.WOODEN_CACTUS.get()).setWeight(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(8, 20))))
						.add(LootItem.lootTableItem(Items.SANDSTONE).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(8, 20))))
						.add(LootItem.lootTableItem(Items.SMOOTH_SANDSTONE).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(8, 20))))));
		lootProcessor.accept(locationForTerrain(LandTypes.RED_SAND, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL).setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(MSItems.CACTACEAE_CUTLASS.get()).setWeight(3))
						.add(LootItem.lootTableItem(Items.GOLD_INGOT).setWeight(7).apply(SetItemCountFunction.setCount(UniformGenerator.between(5, 10))))
						.add(LootItem.lootTableItem(Items.LEATHER).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 5)))))
				.withPool(LootPool.lootPool().name(BLOCK_POOL).setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(Items.CACTUS).setWeight(6).apply(SetItemCountFunction.setCount(UniformGenerator.between(8, 20))))
						.add(LootItem.lootTableItem(MSBlocks.WOODEN_CACTUS.get()).setWeight(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(8, 20))))
						.add(LootItem.lootTableItem(Items.RED_SANDSTONE).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(8, 20))))
						.add(LootItem.lootTableItem(Items.SMOOTH_RED_SANDSTONE).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(8, 20))))));
		lootProcessor.accept(locationForTerrain(LandTypes.LUSH_DESERTS, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL).setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(MSItems.CACTACEAE_CUTLASS.get()).setWeight(3))
						.add(LootItem.lootTableItem(Items.GOLD_INGOT).setWeight(7).apply(SetItemCountFunction.setCount(UniformGenerator.between(5, 10))))
						.add(LootItem.lootTableItem(Items.LEATHER).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 5)))))
				.withPool(LootPool.lootPool().name(BLOCK_POOL).setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(Items.CACTUS).setWeight(6).apply(SetItemCountFunction.setCount(UniformGenerator.between(8, 20))))
						.add(LootItem.lootTableItem(MSBlocks.WOODEN_CACTUS.get()).setWeight(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(8, 20))))
						.add(LootItem.lootTableItem(Items.SANDSTONE).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(8, 20))))
						.add(LootItem.lootTableItem(Items.SMOOTH_SANDSTONE).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(8, 20))))));
		
		lootProcessor.accept(locationForTerrain(LandTypes.SANDSTONE, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL).setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(Items.GOLDEN_SWORD).setWeight(3))
						.add(LootItem.lootTableItem(Items.REDSTONE).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(5, 10))))
						.add(LootItem.lootTableItem(Items.GUNPOWDER).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 5)))))
				.withPool(LootPool.lootPool().name(BLOCK_POOL).setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(Items.SMOOTH_SANDSTONE).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(8, 20))))
						.add(LootItem.lootTableItem(Items.CHISELED_SANDSTONE).setWeight(6).apply(SetItemCountFunction.setCount(UniformGenerator.between(8, 20))))
						.add(LootItem.lootTableItem(Items.STONE_BRICKS).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(8, 20))))
						.add(LootItem.lootTableItem(Items.CHISELED_STONE_BRICKS).setWeight(6).apply(SetItemCountFunction.setCount(UniformGenerator.between(8, 20))))));
		lootProcessor.accept(locationForTerrain(LandTypes.RED_SANDSTONE, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL).setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(Items.GOLDEN_SWORD).setWeight(3))
						.add(LootItem.lootTableItem(Items.REDSTONE).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(5, 10))))
						.add(LootItem.lootTableItem(Items.GUNPOWDER).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 5)))))
				.withPool(LootPool.lootPool().name(BLOCK_POOL).setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(Items.SMOOTH_RED_SANDSTONE).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(8, 20))))
						.add(LootItem.lootTableItem(Items.CHISELED_RED_SANDSTONE).setWeight(6).apply(SetItemCountFunction.setCount(UniformGenerator.between(8, 20))))
						.add(LootItem.lootTableItem(Items.STONE_BRICKS).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(8, 20))))
						.add(LootItem.lootTableItem(Items.CHISELED_STONE_BRICKS).setWeight(6).apply(SetItemCountFunction.setCount(UniformGenerator.between(8, 20))))));
		
		lootProcessor.accept(locationForTerrain(LandTypes.SHADE, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL).setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(MSItems.POGO_CLUB.get()).setWeight(3))
						.add(LootItem.lootTableItem(Items.LAPIS_LAZULI).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(5, 10))))
						.add(LootItem.lootTableItem(Items.FEATHER).setWeight(6).apply(SetItemCountFunction.setCount(UniformGenerator.between(5, 10))))
						.add(LootItem.lootTableItem(Items.FLINT).setWeight(6).apply(SetItemCountFunction.setCount(UniformGenerator.between(5, 10)))))
				.withPool(LootPool.lootPool().name(BLOCK_POOL).setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(MSBlocks.GLOWING_LOG.get()).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(8, 20))))
						.add(LootItem.lootTableItem(MSBlocks.GLOWING_PLANKS.get()).setWeight(6).apply(SetItemCountFunction.setCount(UniformGenerator.between(8, 20))))
						.add(LootItem.lootTableItem(MSBlocks.SHADE_BRICKS.get()).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(8, 20))))
						.add(LootItem.lootTableItem(MSBlocks.SMOOTH_SHADE_STONE.get()).setWeight(6).apply(SetItemCountFunction.setCount(UniformGenerator.between(8, 20))))));
		
		lootProcessor.accept(locationForTerrain(LandTypes.WOOD, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL).setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(Items.STONE_AXE).setWeight(3))
						.add(LootItem.lootTableItem(Items.EMERALD).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 4))))
						.add(LootItem.lootTableItem(Items.SLIME_BALL).setWeight(6).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 5))))
						.add(LootItem.lootTableItem(Items.CHARCOAL).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(5, 10)))))
				.withPool(LootPool.lootPool().name(BLOCK_POOL).setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(Items.OAK_LOG).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(8, 20))))
						.add(LootItem.lootTableItem(Items.SPRUCE_LOG).setWeight(6).apply(SetItemCountFunction.setCount(UniformGenerator.between(8, 20))))
						.add(LootItem.lootTableItem(Items.BIRCH_LOG).setWeight(6).apply(SetItemCountFunction.setCount(UniformGenerator.between(8, 20))))
						.add(LootItem.lootTableItem(Items.JUNGLE_LOG).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(8, 20))))));
		
		lootProcessor.accept(locationForTerrain(LandTypes.RAINBOW, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL).setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(Items.CLAY_BALL).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(5, 10))))
						.add(LootItem.lootTableItem(Items.PAPER).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(5, 10))))
						.add(LootItem.lootTableItem(Items.EGG).setWeight(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3)))))
				.withPool(LootPool.lootPool().name(BLOCK_POOL).setRolls(ConstantValue.exactly(1))));
		
		lootProcessor.accept(locationForTerrain(LandTypes.FLORA, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL).setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(Items.EMERALD).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 4))))
						.add(LootItem.lootTableItem(Items.QUARTZ).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(5, 10))))
						.add(LootItem.lootTableItem(Items.PRISMARINE_CRYSTALS).setWeight(6).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 5)))))
				.withPool(LootPool.lootPool().name(BLOCK_POOL).setRolls(ConstantValue.exactly(1))));
		
		lootProcessor.accept(locationForTitle(LandTypes.FROGS, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL).setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(MSItems.BUG_NET.get()).setWeight(5)))
				.withPool(LootPool.lootPool().name(BLOCK_POOL).setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(Items.LILY_PAD).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 5))))));
		
		lootProcessor.accept(locationForTitle(LandTypes.WIND, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL).setRolls(ConstantValue.exactly(1)))
				.withPool(LootPool.lootPool().name(BLOCK_POOL).setRolls(ConstantValue.exactly(1))));
		
		lootProcessor.accept(locationForTitle(LandTypes.LIGHT, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL).setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(MSBlocks.GLOWYSTONE_DUST.get()).setWeight(6).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 5)))))
				.withPool(LootPool.lootPool().name(BLOCK_POOL).setRolls(ConstantValue.exactly(1))));
		
		lootProcessor.accept(locationForTitle(LandTypes.CLOCKWORK, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL).setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(Items.CLOCK).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 5)))))
				.withPool(LootPool.lootPool().name(BLOCK_POOL).setRolls(ConstantValue.exactly(1))));
		
		lootProcessor.accept(locationForTitle(LandTypes.SILENCE, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL).setRolls(ConstantValue.exactly(1)))
				.withPool(LootPool.lootPool().name(BLOCK_POOL).setRolls(ConstantValue.exactly(1))));
		
		lootProcessor.accept(locationForTitle(LandTypes.THUNDER, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL).setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(MSItems.IRON_CANE.get()).setWeight(3)))
				.withPool(LootPool.lootPool().name(BLOCK_POOL).setRolls(ConstantValue.exactly(1))));
		
		lootProcessor.accept(locationForTitle(LandTypes.PULSE, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL).setRolls(ConstantValue.exactly(1)))
				.withPool(LootPool.lootPool().name(BLOCK_POOL).setRolls(ConstantValue.exactly(1))));
		
		lootProcessor.accept(locationForTitle(LandTypes.THOUGHT, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL).setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(Items.BOOK).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 5)))))
				.withPool(LootPool.lootPool().name(BLOCK_POOL).setRolls(ConstantValue.exactly(1))));
		
		lootProcessor.accept(locationForTitle(LandTypes.BUCKETS, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL).setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(Items.BUCKET).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 5)))))
				.withPool(LootPool.lootPool().name(BLOCK_POOL).setRolls(ConstantValue.exactly(1))));
		
		lootProcessor.accept(locationForTitle(LandTypes.CAKE, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL).setRolls(ConstantValue.exactly(1)))
				.withPool(LootPool.lootPool().name(BLOCK_POOL).setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(MSBlocks.SUGAR_CUBE.get()).setWeight(6).apply(SetItemCountFunction.setCount(UniformGenerator.between(5, 10))))
						.add(LootItem.lootTableItem(MSBlocks.FUCHSIA_CAKE.get()).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))));
		
		lootProcessor.accept(locationForTitle(LandTypes.RABBITS, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL).setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(Items.RABBIT_FOOT).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 5)))))
				.withPool(LootPool.lootPool().name(BLOCK_POOL).setRolls(ConstantValue.exactly(1))));
		
		lootProcessor.accept(locationForTitle(LandTypes.MONSTERS, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL).setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(Items.ROTTEN_FLESH).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(5, 10)))))
				.withPool(LootPool.lootPool().name(BLOCK_POOL).setRolls(ConstantValue.exactly(1))));
		lootProcessor.accept(locationForTitle(LandTypes.UNDEAD, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL).setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(Items.ROTTEN_FLESH).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(5, 10)))))
				.withPool(LootPool.lootPool().name(BLOCK_POOL).setRolls(ConstantValue.exactly(1))));
		
		lootProcessor.accept(locationForTitle(LandTypes.TOWERS, MSLootTables.CONSORT_GENERAL_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL).setRolls(ConstantValue.exactly(1)))
				.withPool(LootPool.lootPool().name(BLOCK_POOL).setRolls(ConstantValue.exactly(1))));
		
		
		lootProcessor.accept(MSLootTables.CONSORT_FOOD_STOCK, LootTable.lootTable()
				.withPool(LootPool.lootPool().name(MAIN_POOL).setRolls(ConstantValue.exactly(5))
						.add(LandTableLootEntry.builder(MSLootTables.CONSORT_FOOD_STOCK).setPool(MAIN_POOL))
						.add(LootItem.lootTableItem(MSItems.GRASSHOPPER.get()).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 10)))))
				.withPool(LootPool.lootPool().name(SPECIAL_POOL).setRolls(UniformGenerator.between(1, 2))
						.add(LandTableLootEntry.builder(MSLootTables.CONSORT_FOOD_STOCK).setPool(SPECIAL_POOL))
						.add(LootItem.lootTableItem(Items.PUMPKIN_PIE).setWeight(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))))
						.add(LootItem.lootTableItem(Items.GOLDEN_APPLE).setWeight(1))
						.add(LootItem.lootTableItem(MSItems.CANDY_CORN.get()).setWeight(1).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))))
						.add(TagEntry.expandTag(MSTags.Items.GRIST_CANDY).setWeight(1).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))))));
		
		//Iguana
		lootProcessor.accept(locationForTerrain(LandTypes.FOREST, MSLootTables.CONSORT_FOOD_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(MAIN_POOL).setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(MSItems.ONION.get()).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 10))))
						.add(LootItem.lootTableItem(MSItems.CONE_OF_FLIES.get()).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 10))))
						.add(LootItem.lootTableItem(Items.APPLE).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 10))))
						.add(LootItem.lootTableItem(MSItems.CHOCOLATE_BEETLE.get()).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 10))))
						.add(LootItem.lootTableItem(Items.WHEAT).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 10))))
						.add(LootItem.lootTableItem(Items.WHEAT_SEEDS).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 10))))
						.add(LootItem.lootTableItem(Items.COCOA_BEANS).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 6)))))
				.withPool(LootPool.lootPool().name(SPECIAL_POOL).setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(Items.COOKIE).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))
						.add(LootItem.lootTableItem(MSBlocks.APPLE_CAKE.get()).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))));
		lootProcessor.accept(locationForTerrain(LandTypes.TAIGA, MSLootTables.CONSORT_FOOD_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(MAIN_POOL).setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(MSItems.ONION.get()).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 10))))
						.add(LootItem.lootTableItem(MSItems.CONE_OF_FLIES.get()).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 10))))
						.add(LootItem.lootTableItem(Items.APPLE).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 10))))
						.add(LootItem.lootTableItem(MSItems.CHOCOLATE_BEETLE.get()).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 10))))
						.add(LootItem.lootTableItem(Items.WHEAT).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 10))))
						.add(LootItem.lootTableItem(Items.WHEAT_SEEDS).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 10))))
						.add(LootItem.lootTableItem(Items.COCOA_BEANS).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 6)))))
				.withPool(LootPool.lootPool().name(SPECIAL_POOL).setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(Items.COOKIE).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))
						.add(LootItem.lootTableItem(MSBlocks.APPLE_CAKE.get()).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))));
		lootProcessor.accept(locationForTerrain(LandTypes.FROST, MSLootTables.CONSORT_FOOD_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(MAIN_POOL).setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(MSItems.ONION.get()).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 10))))
						.add(LootItem.lootTableItem(MSItems.CONE_OF_FLIES.get()).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 10))))
						.add(LootItem.lootTableItem(Items.APPLE).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 10))))
						.add(LootItem.lootTableItem(MSItems.CHOCOLATE_BEETLE.get()).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 10))))
						.add(LootItem.lootTableItem(Items.WHEAT).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 10))))
						.add(LootItem.lootTableItem(Items.WHEAT_SEEDS).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 10))))
						.add(LootTableReference.lootTableReference(COLD_CAKE).setWeight(2)))
				.withPool(LootPool.lootPool().name(SPECIAL_POOL).setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(Items.COOKIE).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))
						.add(LootItem.lootTableItem(MSBlocks.APPLE_CAKE.get()).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))));
		lootProcessor.accept(locationForTerrain(LandTypes.FLORA, MSLootTables.CONSORT_FOOD_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(MAIN_POOL).setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(MSItems.ONION.get()).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 10))))
						.add(LootItem.lootTableItem(MSItems.CONE_OF_FLIES.get()).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 10))))
						.add(LootItem.lootTableItem(Items.APPLE).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 10))))
						.add(LootItem.lootTableItem(MSItems.CHOCOLATE_BEETLE.get()).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 10))))
						.add(LootItem.lootTableItem(Items.WHEAT).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 10))))
						.add(LootItem.lootTableItem(Items.WHEAT_SEEDS).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 10))))
						.add(LootItem.lootTableItem(MSItems.STRAWBERRY_CHUNK.get()).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(8, 12)))))
				.withPool(LootPool.lootPool().name(SPECIAL_POOL).setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(Items.COOKIE).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))
						.add(LootItem.lootTableItem(MSBlocks.APPLE_CAKE.get()).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))));
		
		//Salamander
		lootProcessor.accept(locationForTerrain(LandTypes.FUNGI, MSLootTables.CONSORT_FOOD_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(MAIN_POOL).setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(MSItems.JAR_OF_BUGS.get()).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 10))))
						.add(LootItem.lootTableItem(MSItems.BUG_ON_A_STICK.get()).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 10))))
						.add(LootItem.lootTableItem(MSItems.CONE_OF_FLIES.get()).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 10))))
						.add(LootItem.lootTableItem(Items.POTATO).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 10))))
						.add(LootItem.lootTableItem(Items.MUSHROOM_STEW).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 10))))
						.add(LootItem.lootTableItem(Items.RED_MUSHROOM).setWeight(4).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 6))))
						.add(LootItem.lootTableItem(Items.BROWN_MUSHROOM).setWeight(4).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 6))))
						.add(LootItem.lootTableItem(MSItems.FUNGAL_SPORE.get()).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(8, 12))))
						.add(LootItem.lootTableItem(MSItems.SPOREO.get()).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4)))))
				.withPool(LootPool.lootPool().name(SPECIAL_POOL).setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(Items.POISONOUS_POTATO).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))
						.add(LootItem.lootTableItem(Items.MELON).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 6))))));
		lootProcessor.accept(locationForTerrain(LandTypes.SHADE, MSLootTables.CONSORT_FOOD_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(MAIN_POOL).setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(MSItems.JAR_OF_BUGS.get()).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 10))))
						.add(LootItem.lootTableItem(MSItems.BUG_ON_A_STICK.get()).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 10))))
						.add(LootItem.lootTableItem(MSItems.CONE_OF_FLIES.get()).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 10))))
						.add(LootItem.lootTableItem(Items.POTATO).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 10))))
						.add(LootItem.lootTableItem(Items.MUSHROOM_STEW).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 10))))
						.add(LootItem.lootTableItem(MSBlocks.GLOWING_MUSHROOM.get()).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 6)))))
				.withPool(LootPool.lootPool().name(SPECIAL_POOL).setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(Items.POISONOUS_POTATO).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))
						.add(LootItem.lootTableItem(Items.MELON).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 6))))));
		lootProcessor.accept(locationForTerrain(LandTypes.WOOD, MSLootTables.CONSORT_FOOD_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(MAIN_POOL).setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(MSItems.JAR_OF_BUGS.get()).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 10))))
						.add(LootItem.lootTableItem(MSItems.BUG_ON_A_STICK.get()).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 10))))
						.add(LootItem.lootTableItem(MSItems.CONE_OF_FLIES.get()).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 10))))
						.add(LootItem.lootTableItem(Items.POTATO).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 10))))
						.add(LootItem.lootTableItem(Items.MUSHROOM_STEW).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 10))))
						.add(LootItem.lootTableItem(Items.RED_MUSHROOM).setWeight(4).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 6))))
						.add(LootItem.lootTableItem(Items.BROWN_MUSHROOM).setWeight(4).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 6)))))
				.withPool(LootPool.lootPool().name(SPECIAL_POOL).setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(Items.POISONOUS_POTATO).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))
						.add(LootItem.lootTableItem(Items.MELON).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 6))))));
		
		//Nakagator
		lootProcessor.accept(locationForTerrain(LandTypes.HEAT, MSLootTables.CONSORT_FOOD_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(MAIN_POOL).setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(MSItems.ONION.get()).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 10))))
						.add(LootItem.lootTableItem(MSItems.CHOCOLATE_BEETLE.get()).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 10))))
						.add(LootItem.lootTableItem(Items.BEETROOT_SOUP).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 10))))
						.add(LootItem.lootTableItem(Items.BEEF).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 10))))
						.add(LootItem.lootTableItem(MSItems.STEAK_SWORD.get()).setWeight(3))
						.add(LootItem.lootTableItem(Items.BEETROOT).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 10))))
						.add(LootTableReference.lootTableReference(HOT_CAKE).setWeight(2)))
				.withPool(LootPool.lootPool().name(SPECIAL_POOL).setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(MSItems.TAB.get()).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 6))))
						.add(LootItem.lootTableItem(MSItems.IRRADIATED_STEAK.get()).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))));
		lootProcessor.accept(locationForTerrain(LandTypes.ROCK, MSLootTables.CONSORT_FOOD_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(MAIN_POOL).setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(MSItems.ONION.get()).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 10))))
						.add(LootItem.lootTableItem(MSItems.CHOCOLATE_BEETLE.get()).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 10))))
						.add(LootItem.lootTableItem(Items.BEETROOT_SOUP).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 10))))
						.add(LootItem.lootTableItem(Items.BEEF).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 10))))
						.add(LootItem.lootTableItem(MSItems.STEAK_SWORD.get()).setWeight(3))
						.add(LootItem.lootTableItem(Items.BEETROOT).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 10))))
						.add(LootItem.lootTableItem(MSItems.ROCK_COOKIE.get()).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(8, 12)))))
				.withPool(LootPool.lootPool().name(SPECIAL_POOL).setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(MSItems.TAB.get()).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 6))))
						.add(LootItem.lootTableItem(MSItems.IRRADIATED_STEAK.get()).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))));
		lootProcessor.accept(locationForTerrain(LandTypes.PETRIFICATION, MSLootTables.CONSORT_FOOD_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(MAIN_POOL).setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(MSItems.ONION.get()).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 10))))
						.add(LootItem.lootTableItem(MSItems.CHOCOLATE_BEETLE.get()).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 10))))
						.add(LootItem.lootTableItem(Items.BEETROOT_SOUP).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 10))))
						.add(LootItem.lootTableItem(Items.BEEF).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 10))))
						.add(LootItem.lootTableItem(MSItems.STEAK_SWORD.get()).setWeight(3))
						.add(LootItem.lootTableItem(Items.BEETROOT).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 10))))
						.add(LootItem.lootTableItem(MSItems.ROCK_COOKIE.get()).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(8, 12)))))
				.withPool(LootPool.lootPool().name(SPECIAL_POOL).setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(MSItems.TAB.get()).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 6))))
						.add(LootItem.lootTableItem(MSItems.IRRADIATED_STEAK.get()).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))));
		lootProcessor.accept(locationForTerrain(LandTypes.END, MSLootTables.CONSORT_FOOD_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(MAIN_POOL).setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(MSItems.ONION.get()).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 10))))
						.add(LootItem.lootTableItem(MSItems.CHOCOLATE_BEETLE.get()).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 10))))
						.add(LootItem.lootTableItem(Items.BEETROOT_SOUP).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 10))))
						.add(LootItem.lootTableItem(Items.BEEF).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 10))))
						.add(LootItem.lootTableItem(MSItems.STEAK_SWORD.get()).setWeight(3))
						.add(LootItem.lootTableItem(Items.BEETROOT).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 10))))
						.add(LootItem.lootTableItem(Items.CHORUS_FRUIT).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(8, 12)))))
				.withPool(LootPool.lootPool().name(SPECIAL_POOL).setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(MSItems.TAB.get()).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 6))))
						.add(LootItem.lootTableItem(MSItems.IRRADIATED_STEAK.get()).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))));
		
		//Turtle
		lootProcessor.accept(locationForTerrain(LandTypes.SAND, MSLootTables.CONSORT_FOOD_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(MAIN_POOL).setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(MSItems.JAR_OF_BUGS.get()).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 10))))
						.add(LootItem.lootTableItem(MSItems.BUG_ON_A_STICK.get()).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 10))))
						.add(LootItem.lootTableItem(Items.POTATO).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 10))))
						.add(LootItem.lootTableItem(MSItems.SALAD.get()).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 10))))
						.add(LootItem.lootTableItem(Items.CARROT).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 10))))
						.add(LootItem.lootTableItem(Items.POTION).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 6))).apply(SetNbtFunction.setTag(waterNBT())))
						.add(LootItem.lootTableItem(MSItems.DESERT_FRUIT.get()).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(15, 20)))))
				.withPool(LootPool.lootPool().name(SPECIAL_POOL).setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(MSBlocks.GOLD_SEEDS.get()).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))
						.add(LootItem.lootTableItem(Items.COD).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))));
		lootProcessor.accept(locationForTerrain(LandTypes.RED_SAND, MSLootTables.CONSORT_FOOD_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(MAIN_POOL).setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(MSItems.JAR_OF_BUGS.get()).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 10))))
						.add(LootItem.lootTableItem(MSItems.BUG_ON_A_STICK.get()).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 10))))
						.add(LootItem.lootTableItem(Items.POTATO).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 10))))
						.add(LootItem.lootTableItem(MSItems.SALAD.get()).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 10))))
						.add(LootItem.lootTableItem(Items.CARROT).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 10))))
						.add(LootItem.lootTableItem(Items.POTION).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 6))).apply(SetNbtFunction.setTag(waterNBT())))
						.add(LootItem.lootTableItem(MSItems.DESERT_FRUIT.get()).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(15, 20)))))
				.withPool(LootPool.lootPool().name(SPECIAL_POOL).setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(MSBlocks.GOLD_SEEDS.get()).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))
						.add(LootItem.lootTableItem(Items.COD).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))));
		lootProcessor.accept(locationForTerrain(LandTypes.LUSH_DESERTS, MSLootTables.CONSORT_FOOD_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(MAIN_POOL).setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(MSItems.JAR_OF_BUGS.get()).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 10))))
						.add(LootItem.lootTableItem(MSItems.BUG_ON_A_STICK.get()).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 10))))
						.add(LootItem.lootTableItem(Items.POTATO).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 10))))
						.add(LootItem.lootTableItem(MSItems.SALAD.get()).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 10))))
						.add(LootItem.lootTableItem(Items.CARROT).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 10))))
						.add(LootItem.lootTableItem(Items.POTION).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 6))).apply(SetNbtFunction.setTag(waterNBT())))
						.add(LootItem.lootTableItem(MSItems.DESERT_FRUIT.get()).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(15, 20)))))
				.withPool(LootPool.lootPool().name(SPECIAL_POOL).setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(MSBlocks.GOLD_SEEDS.get()).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))
						.add(LootItem.lootTableItem(Items.COD).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))));
		lootProcessor.accept(locationForTerrain(LandTypes.SANDSTONE, MSLootTables.CONSORT_FOOD_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(MAIN_POOL).setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(MSItems.JAR_OF_BUGS.get()).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 10))))
						.add(LootItem.lootTableItem(MSItems.BUG_ON_A_STICK.get()).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 10))))
						.add(LootItem.lootTableItem(Items.POTATO).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 10))))
						.add(LootItem.lootTableItem(MSItems.SALAD.get()).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 10))))
						.add(LootItem.lootTableItem(Items.CARROT).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 10)))))
				.withPool(LootPool.lootPool().name(SPECIAL_POOL).setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(MSBlocks.GOLD_SEEDS.get()).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))
						.add(LootItem.lootTableItem(Items.COD).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))));
		lootProcessor.accept(locationForTerrain(LandTypes.RED_SANDSTONE, MSLootTables.CONSORT_FOOD_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(MAIN_POOL).setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(MSItems.JAR_OF_BUGS.get()).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 10))))
						.add(LootItem.lootTableItem(MSItems.BUG_ON_A_STICK.get()).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 10))))
						.add(LootItem.lootTableItem(Items.POTATO).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 10))))
						.add(LootItem.lootTableItem(MSItems.SALAD.get()).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 10))))
						.add(LootItem.lootTableItem(Items.CARROT).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 10)))))
				.withPool(LootPool.lootPool().name(SPECIAL_POOL).setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(MSBlocks.GOLD_SEEDS.get()).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))
						.add(LootItem.lootTableItem(Items.COD).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))));
		lootProcessor.accept(locationForTerrain(LandTypes.RAINBOW, MSLootTables.CONSORT_FOOD_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(MAIN_POOL).setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(MSItems.JAR_OF_BUGS.get()).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 10))))
						.add(LootItem.lootTableItem(MSItems.BUG_ON_A_STICK.get()).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 10))))
						.add(LootItem.lootTableItem(Items.POTATO).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 10))))
						.add(LootItem.lootTableItem(MSItems.SALAD.get()).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 10))))
						.add(LootItem.lootTableItem(Items.CARROT).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 10))))
						.add(LootItem.lootTableItem(Items.BREAD).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(8, 12)))))
				.withPool(LootPool.lootPool().name(SPECIAL_POOL).setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(MSBlocks.GOLD_SEEDS.get()).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))
						.add(LootItem.lootTableItem(Items.COD).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))));
		lootProcessor.accept(locationForTerrain(LandTypes.RAIN, MSLootTables.CONSORT_FOOD_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(MAIN_POOL).setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(MSItems.JAR_OF_BUGS.get()).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 10))))
						.add(LootItem.lootTableItem(MSItems.BUG_ON_A_STICK.get()).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 10))))
						.add(LootItem.lootTableItem(Items.POTATO).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 10))))
						.add(LootItem.lootTableItem(MSItems.SALAD.get()).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 10))))
						.add(LootItem.lootTableItem(Items.CARROT).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 10))))
						.add(LootItem.lootTableItem(Items.MELON).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(8, 12)))))
				.withPool(LootPool.lootPool().name(SPECIAL_POOL).setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(MSBlocks.GOLD_SEEDS.get()).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))
						.add(LootItem.lootTableItem(Items.COD).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))));
		
		lootProcessor.accept(locationForTitle(LandTypes.FROGS, MSLootTables.CONSORT_FOOD_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(MAIN_POOL).setRolls(ConstantValue.exactly(1)))
				.withPool(LootPool.lootPool().name(SPECIAL_POOL).setRolls(ConstantValue.exactly(1))));
		
		lootProcessor.accept(locationForTitle(LandTypes.WIND, MSLootTables.CONSORT_FOOD_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(MAIN_POOL).setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(MSItems.COTTON_CANDY_FAYGO.get()).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3)))))
				.withPool(LootPool.lootPool().name(SPECIAL_POOL).setRolls(ConstantValue.exactly(1))));
		
		lootProcessor.accept(locationForTitle(LandTypes.LIGHT, MSLootTables.CONSORT_FOOD_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(MAIN_POOL).setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(MSItems.ORANGE_FAYGO.get()).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3)))))
				.withPool(LootPool.lootPool().name(SPECIAL_POOL).setRolls(ConstantValue.exactly(1))));
		
		lootProcessor.accept(locationForTitle(LandTypes.CLOCKWORK, MSLootTables.CONSORT_FOOD_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(MAIN_POOL).setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(MSItems.CANDY_APPLE_FAYGO.get()).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3)))))
				.withPool(LootPool.lootPool().name(SPECIAL_POOL).setRolls(ConstantValue.exactly(1))));
		
		lootProcessor.accept(locationForTitle(LandTypes.SILENCE, MSLootTables.CONSORT_FOOD_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(MAIN_POOL).setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(MSItems.FAYGO_COLA.get()).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3)))))
				.withPool(LootPool.lootPool().name(SPECIAL_POOL).setRolls(ConstantValue.exactly(1))));
		
		lootProcessor.accept(locationForTitle(LandTypes.THUNDER, MSLootTables.CONSORT_FOOD_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(MAIN_POOL).setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(MSItems.PEACH_FAYGO.get()).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3)))))
				.withPool(LootPool.lootPool().name(SPECIAL_POOL).setRolls(ConstantValue.exactly(1))));
		
		lootProcessor.accept(locationForTitle(LandTypes.PULSE, MSLootTables.CONSORT_FOOD_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(MAIN_POOL).setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(MSItems.BEEF_SWORD.get()).setWeight(4)).when(ConsortLootCondition.builder(EnumConsort.NAKAGATOR))
						.add(LootItem.lootTableItem(MSItems.REDPOP_FAYGO.get()).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3)))))
				.withPool(LootPool.lootPool().name(SPECIAL_POOL).setRolls(ConstantValue.exactly(1))));
		
		lootProcessor.accept(locationForTitle(LandTypes.THOUGHT, MSLootTables.CONSORT_FOOD_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(MAIN_POOL).setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(MSItems.MOON_MIST_FAYGO.get()).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3)))))
				.withPool(LootPool.lootPool().name(SPECIAL_POOL).setRolls(ConstantValue.exactly(1))));
		
		lootProcessor.accept(locationForTitle(LandTypes.BUCKETS, MSLootTables.CONSORT_FOOD_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(MAIN_POOL).setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(Items.MILK_BUCKET).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3)))))
				.withPool(LootPool.lootPool().name(SPECIAL_POOL).setRolls(ConstantValue.exactly(1))));
		
		lootProcessor.accept(locationForTitle(LandTypes.CAKE, MSLootTables.CONSORT_FOOD_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(MAIN_POOL).setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(Items.SUGAR).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 6)))))
				.withPool(LootPool.lootPool().name(SPECIAL_POOL).setRolls(ConstantValue.exactly(1))));
		
		lootProcessor.accept(locationForTitle(LandTypes.RABBITS, MSLootTables.CONSORT_FOOD_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(MAIN_POOL).setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(Items.RABBIT_STEW).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 4)))))
				.withPool(LootPool.lootPool().name(SPECIAL_POOL).setRolls(ConstantValue.exactly(1))));
		
		lootProcessor.accept(locationForTitle(LandTypes.MONSTERS, MSLootTables.CONSORT_FOOD_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(MAIN_POOL).setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(MSItems.GRAPE_FAYGO.get()).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3)))))
				.withPool(LootPool.lootPool().name(SPECIAL_POOL).setRolls(ConstantValue.exactly(1))));
		lootProcessor.accept(locationForTitle(LandTypes.UNDEAD, MSLootTables.CONSORT_FOOD_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(MAIN_POOL).setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(MSItems.GRAPE_FAYGO.get()).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3)))))
				.withPool(LootPool.lootPool().name(SPECIAL_POOL).setRolls(ConstantValue.exactly(1))));
		
		lootProcessor.accept(locationForTitle(LandTypes.TOWERS, MSLootTables.CONSORT_FOOD_STOCK), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(MAIN_POOL).setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(MSItems.CREME_SODA_FAYGO.get()).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3)))))
				.withPool(LootPool.lootPool().name(SPECIAL_POOL).setRolls(ConstantValue.exactly(1))));
		
		
		lootProcessor.accept(MSLootTables.CONSORT_JUNK_REWARD, LootTable.lootTable()
				.withPool(LootPool.lootPool().name("main").setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(Items.OAK_PLANKS).setWeight(2))
						.add(LootItem.lootTableItem(Items.SPRUCE_PLANKS).setWeight(2))
						.add(LootItem.lootTableItem(Items.BIRCH_PLANKS).setWeight(2))
						.add(LootItem.lootTableItem(Items.JUNGLE_PLANKS).setWeight(2))
						.add(LootItem.lootTableItem(Items.DARK_OAK_PLANKS).setWeight(2))
						.add(LootItem.lootTableItem(Items.ACACIA_PLANKS).setWeight(2))
						.add(LootItem.lootTableItem(Items.WOODEN_AXE).setWeight(5))
						.add(LootItem.lootTableItem(Items.WOODEN_PICKAXE).setWeight(5))
						.add(LootItem.lootTableItem(Items.WOODEN_HOE).setWeight(5))
						.add(LootItem.lootTableItem(Items.WOODEN_SHOVEL).setWeight(5))
						.add(LootItem.lootTableItem(Items.ROTTEN_FLESH).setWeight(8))
						.add(LootItem.lootTableItem(MSBlocks.GENERIC_OBJECT.get()).setWeight(10))
						.add(LootItem.lootTableItem(Items.POISONOUS_POTATO).setWeight(10))
						.add(LootItem.lootTableItem(Items.RABBIT_HIDE).setWeight(10))
						.add(LootItem.lootTableItem(Items.DEAD_BUSH).setWeight(10))
						.add(LootItem.lootTableItem(Items.OAK_STAIRS).setWeight(5))
						.add(LootItem.lootTableItem(Items.FLOWER_POT).setWeight(5))
						.add(LootItem.lootTableItem(Items.BIRCH_BUTTON).setWeight(5))
						.add(LootItem.lootTableItem(MSItems.SORD.get()).setWeight(10))
						.add(LootItem.lootTableItem(MSItems.ANCIENT_THUMB_DRIVE.get()).setWeight(1))
						.add(LootItem.lootTableItem(MSItems.CROWBAR.get()).setWeight(3).apply(SetItemDamageFunction.setDamage(UniformGenerator.between(0.05F, 0.7F))))
						.add(LootItem.lootTableItem(Items.GOLDEN_SHOVEL).setWeight(3).apply(SetItemDamageFunction.setDamage(UniformGenerator.between(0.05F, 0.05F))))
						.add(LootItem.lootTableItem(Items.GOLDEN_AXE).setWeight(3).apply(SetItemDamageFunction.setDamage(UniformGenerator.between(0.05F, 0.05F))))
						.add(LootItem.lootTableItem(Items.GOLDEN_SWORD).setWeight(3).apply(SetItemDamageFunction.setDamage(UniformGenerator.between(0.05F, 0.05F))))
						.add(LootItem.lootTableItem(Items.GOLDEN_PICKAXE).setWeight(3).apply(SetItemDamageFunction.setDamage(UniformGenerator.between(0.05F, 0.05F))))
						.add(LootItem.lootTableItem(MSItems.SBAHJ_POSTER.get()).setWeight(10))
						.add(LootItem.lootTableItem(MSItems.CREW_POSTER.get()).setWeight(10))
						.add(LootItem.lootTableItem(MSItems.THRESH_DVD.get()).setWeight(10))
						.add(LootItem.lootTableItem(MSItems.ONION.get()).setWeight(5).when(ConsortLootCondition.builder(EnumConsort.NAKAGATOR)))
						.add(LootItem.lootTableItem(MSItems.TAB.get()).setWeight(5).when(ConsortLootCondition.builder(EnumConsort.SALAMANDER)))
						.add(LootItem.lootTableItem(MSItems.JAR_OF_BUGS.get()).setWeight(5).when(ConsortLootCondition.builder(EnumConsort.TURTLE)))
						.add(LootItem.lootTableItem(MSItems.GRASSHOPPER.get()).setWeight(5).when(ConsortLootCondition.builder(EnumConsort.IGUANA)))));
		
	}
	
	private static CompoundTag waterNBT()
	{
		return Objects.requireNonNull(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.WATER).getTag());
	}
}