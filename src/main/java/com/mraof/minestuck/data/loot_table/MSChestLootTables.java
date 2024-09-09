package com.mraof.minestuck.data.loot_table;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.item.loot.LandTableLootEntry;
import com.mraof.minestuck.item.loot.MSLootTables;
import com.mraof.minestuck.item.loot.functions.SetBoondollarCount;
import com.mraof.minestuck.item.loot.functions.SetSburbCodeFragments;
import com.mraof.minestuck.util.MSTags;
import com.mraof.minestuck.world.lands.LandTypes;
import com.mraof.minestuck.world.lands.terrain.TerrainLandType;
import com.mraof.minestuck.world.lands.title.TitleLandType;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.minecraft.world.level.storage.loot.entries.TagEntry;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemDamageFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.neoforged.neoforge.common.Tags;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public final class MSChestLootTables implements LootTableSubProvider
{
	public static final String ITEM_POOL = "item";
	public static final ResourceLocation WEAPON_ITEM_TABLE = Minestuck.id("chests/weapon_item");
	public static final ResourceLocation SUPPLY_ITEM_TABLE = Minestuck.id("chests/supply_item");
	public static final ResourceLocation MISC_ITEM_TABLE = Minestuck.id("chests/misc_item");
	public static final ResourceLocation RARE_ITEM_TABLE = Minestuck.id("chests/rare_item");
	
	@Override
	public void generate(BiConsumer<ResourceLocation, LootTable.Builder> lootProcessor)
	{
		lootProcessor.accept(MSLootTables.BLANK_DISK_DUNGEON_LOOT_INJECT, LootTable.lootTable()
				.withPool(LootPool.lootPool().name("minestuck").setRolls(UniformGenerator.between(0, 1))
						.add(LootItem.lootTableItem(MSItems.BLANK_DISK.get()).setWeight(1).setQuality(1).apply(countRange(0, 2)))));
		
		lootProcessor.accept(MSLootTables.SBURB_CODE_LIBRARY_LOOT_INJECT, LootTable.lootTable()
				.withPool(LootPool.lootPool().name("minestuck").setRolls(UniformGenerator.between(0, 1))
						.add(LootItem.lootTableItem(MSItems.SBURB_CODE.get()).setWeight(1).setQuality(1).apply(countRange(0, 1)).apply(SetSburbCodeFragments.builder()))));
		
		lootProcessor.accept(MSLootTables.FROG_TEMPLE_CHEST, LootTable.lootTable()
				.withPool(LootPool.lootPool().name("weapons").setRolls(UniformGenerator.between(0, 2))
						.add(LootItem.lootTableItem(Items.BOW).setWeight(5).setQuality(1).apply(minorDamageRange()))
						.add(LootItem.lootTableItem(Items.STONE_SWORD).setWeight(5).setQuality(-1).apply(minorDamageRange()))
						.add(LootItem.lootTableItem(Items.IRON_SWORD).setWeight(3).setQuality(1).apply(minorDamageRange()))
						.add(LootItem.lootTableItem(MSItems.CLAW_HAMMER.get()).setWeight(5).setQuality(-1).apply(minorDamageRange()))
						.add(LootItem.lootTableItem(MSItems.SICKLE.get()).setWeight(5).setQuality(-1).apply(minorDamageRange()))
						.add(LootItem.lootTableItem(MSItems.KATANA.get()).setWeight(3).setQuality(1).apply(minorDamageRange()))
						.add(LootItem.lootTableItem(MSItems.SLEDGE_HAMMER.get()).setWeight(3).setQuality(1).apply(minorDamageRange()))
						.add(LootItem.lootTableItem(MSItems.WOODEN_SPOON.get()).setWeight(5).setQuality(-2).apply(minorDamageRange()))
						.add(LootItem.lootTableItem(MSItems.SCYTHE.get()).setWeight(1).setQuality(2).apply(minorDamageRange()))
						.add(LootItem.lootTableItem(MSItems.KNITTING_NEEDLE.get()).setWeight(4).setQuality(-1).apply(minorDamageRange()))
						.add(LootItem.lootTableItem(MSItems.SHURIKEN.get()).setWeight(8).setQuality(-2))
						.add(LootItem.lootTableItem(MSItems.METAL_BAT.get()).setWeight(4).setQuality(1).apply(minorDamageRange()))
						.add(LootItem.lootTableItem(MSItems.MACE.get()).setWeight(1).setQuality(1).apply(minorDamageRange()))
						.add(LootItem.lootTableItem(MSItems.JOUSTING_LANCE.get()).setWeight(2).setQuality(1).apply(minorDamageRange()))
						.add(LootItem.lootTableItem(MSItems.FAN.get()).setWeight(4).setQuality(1).apply(minorDamageRange()))
						.add(LootItem.lootTableItem(MSItems.LUCERNE_HAMMER.get()).setWeight(1).setQuality(2).apply(minorDamageRange()))
						.add(LootItem.lootTableItem(MSItems.FORK.get()).setWeight(4).setQuality(-1).apply(minorDamageRange())))
				.withPool(LootPool.lootPool().name("supplies").setRolls(UniformGenerator.between(2, 6))
						.add(LootItem.lootTableItem(Items.GOLD_INGOT).setWeight(2).setQuality(2).apply(countRange(2, 5)))
						.add(LootItem.lootTableItem(Items.DIAMOND).setWeight(1).setQuality(3).apply(countRange(1, 3)))
						.add(LootItem.lootTableItem(Items.PUMPKIN).setWeight(3).setQuality(-1).apply(countRange(1, 5)))
						.add(LootItem.lootTableItem(Items.WRITABLE_BOOK).setWeight(2).setQuality(1).apply(countRange(0, 1)))
						.add(LootItem.lootTableItem(MSItems.BLANK_DISK.get()).setWeight(2).setQuality(3).apply(countRange(1, 2)))
						.add(LootItem.lootTableItem(MSItems.RAW_CRUXITE.get()).setWeight(4).setQuality(0).apply(countRange(1, 7)))
						.add(LootItem.lootTableItem(MSItems.RAW_URANIUM.get()).setWeight(2).setQuality(1).apply(countRange(1, 5)))
						.add(LootItem.lootTableItem(MSItems.BUG_ON_A_STICK.get()).setWeight(4).setQuality(-1).apply(countRange(1, 5)))
						.add(LootItem.lootTableItem(MSItems.CHOCOLATE_BEETLE.get()).setWeight(4).setQuality(-1).apply(countRange(1, 5)))
						.add(LootItem.lootTableItem(MSItems.FOOD_CAN.get()).setWeight(4).setQuality(-1).apply(countRange(1, 5)))
						.add(LootItem.lootTableItem(MSItems.GRASSHOPPER.get()).setWeight(4).setQuality(-1).apply(countRange(1, 5)))
						.add(LootItem.lootTableItem(MSItems.CONE_OF_FLIES.get()).setWeight(4).setQuality(-1).apply(countRange(1, 5)))
						.add(LootItem.lootTableItem(MSItems.GOLDEN_GRASSHOPPER.get()).setWeight(1).setQuality(1).apply(countRange(1, 2)))
						.add(LootItem.lootTableItem(MSBlocks.MINI_FROG_STATUE.get()).setWeight(1).setQuality(1).apply(countRange(0, 1)))
						.add(LootItem.lootTableItem(MSItems.CARVING_TOOL.get()).setWeight(5).setQuality(0).apply(countRange(1, 2)))
						.add(LootItem.lootTableItem(MSItems.STONE_TABLET.get()).setWeight(2).setQuality(0).apply(countRange(1, 2)))
						.add(LootItem.lootTableItem(MSItems.HASHMAP_MODUS_CARD.get()).setWeight(1).setQuality(1).apply(countRange(0, 1)))
						.add(LootItem.lootTableItem(MSItems.QUEUE_MODUS_CARD.get()).setWeight(1).setQuality(1).apply(countRange(0, 1)))
						.add(LootItem.lootTableItem(MSItems.QUEUESTACK_MODUS_CARD.get()).setWeight(1).setQuality(1).apply(countRange(0, 1)))
						.add(LootItem.lootTableItem(MSItems.SET_MODUS_CARD.get()).setWeight(1).setQuality(1).apply(countRange(0, 1)))
						.add(LootItem.lootTableItem(MSItems.STACK_MODUS_CARD.get()).setWeight(1).setQuality(1).apply(countRange(0, 1)))
						.add(LootItem.lootTableItem(MSItems.TREE_MODUS_CARD.get()).setWeight(1).setQuality(1).apply(countRange(0, 1)))));
		
		
		lootProcessor.accept(WEAPON_ITEM_TABLE, LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL)
						.add(LandTableLootEntry.builder(WEAPON_ITEM_TABLE).setPool(ITEM_POOL))
						.add(LootItem.lootTableItem(Items.BOW).setWeight(5).setQuality(1).apply(minorDamageRange()))
						.add(LootItem.lootTableItem(Items.STONE_SWORD).setWeight(5).setQuality(-1).apply(minorDamageRange()))
						.add(LootItem.lootTableItem(Items.IRON_SWORD).setWeight(3).setQuality(1).apply(minorDamageRange()))
						.add(LootItem.lootTableItem(MSItems.CLAW_HAMMER).setWeight(5).setQuality(-1).apply(minorDamageRange()))
						.add(LootItem.lootTableItem(MSItems.SICKLE).setWeight(5).setQuality(-1).apply(minorDamageRange()))
						.add(LootItem.lootTableItem(MSItems.KATANA).setWeight(3).setQuality(1).apply(minorDamageRange()))
						.add(LootItem.lootTableItem(MSItems.LIPSTICK).setWeight(1).setQuality(1).apply(minorDamageRange()))
						.add(LootItem.lootTableItem(MSItems.SLEDGE_HAMMER).setWeight(3).setQuality(1).apply(minorDamageRange()))
						.add(LootItem.lootTableItem(MSItems.WOODEN_SPOON).setWeight(5).setQuality(-2).apply(minorDamageRange()))
						.add(LootItem.lootTableItem(MSItems.SCYTHE).setWeight(1).setQuality(2).apply(minorDamageRange()))
						.add(LootItem.lootTableItem(MSItems.KNITTING_NEEDLE).setWeight(4).setQuality(-1).apply(minorDamageRange()))
						.add(LootItem.lootTableItem(MSItems.SHURIKEN).setWeight(8).setQuality(-2))
						.add(LootItem.lootTableItem(MSItems.METAL_BAT).setWeight(4).setQuality(1).apply(minorDamageRange()))
						.add(LootItem.lootTableItem(MSItems.MACE).setWeight(1).setQuality(1).apply(minorDamageRange()))
						.add(LootItem.lootTableItem(MSItems.JOUSTING_LANCE).setWeight(2).setQuality(1).apply(minorDamageRange()))
						.add(LootItem.lootTableItem(MSItems.FAN).setWeight(4).setQuality(1).apply(minorDamageRange()))
						.add(LootItem.lootTableItem(MSItems.LUCERNE_HAMMER).setWeight(1).setQuality(2).apply(minorDamageRange()))
						.add(LootItem.lootTableItem(MSItems.FORK).setWeight(4).setQuality(-1).apply(minorDamageRange()))
				));
		
		lootProcessor.accept(locationForTerrain(LandTypes.FOREST, WEAPON_ITEM_TABLE), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL)
						.add(LootItem.lootTableItem(MSItems.CANE).setWeight(10).setQuality(-1).apply(minorDamageRange()))
						.add(LootItem.lootTableItem(MSItems.DEUCE_CLUB).setWeight(10).setQuality(-1).apply(minorDamageRange()))
				));
		lootProcessor.accept(locationForTerrain(LandTypes.TAIGA, WEAPON_ITEM_TABLE), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL)
						.add(LootItem.lootTableItem(MSItems.CANE).setWeight(10).setQuality(-1).apply(minorDamageRange()))
						.add(LootItem.lootTableItem(MSItems.DEUCE_CLUB).setWeight(10).setQuality(-1).apply(minorDamageRange()))
				));
		
		lootProcessor.accept(locationForTerrain(LandTypes.FROST, WEAPON_ITEM_TABLE), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL)
				));
		
		lootProcessor.accept(locationForTerrain(LandTypes.FUNGI, WEAPON_ITEM_TABLE), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL)
						.add(LootItem.lootTableItem(MSItems.PARADISES_PORTABELLO).setWeight(3).setQuality(1).apply(minorDamageRange()))
				));
		
		lootProcessor.accept(locationForTerrain(LandTypes.HEAT, WEAPON_ITEM_TABLE), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL)
						.add(LootItem.lootTableItem(MSItems.BLACKSMITH_HAMMER).setWeight(2).setQuality(1).apply(minorDamageRange()))
						.add(LootItem.lootTableItem(MSItems.BISICKLE).setWeight(2).setQuality(1).apply(minorDamageRange()))
						.add(LootItem.lootTableItem(MSItems.TOO_HOT_TO_HANDLE).setWeight(2).setQuality(1).apply(minorDamageRange()))
				));
		
		lootProcessor.accept(locationForTerrain(LandTypes.ROCK, WEAPON_ITEM_TABLE), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL)
				));
		lootProcessor.accept(locationForTerrain(LandTypes.PETRIFICATION, WEAPON_ITEM_TABLE), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL)
				));
		
		lootProcessor.accept(locationForTerrain(LandTypes.SAND, WEAPON_ITEM_TABLE), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL)
						.add(LootItem.lootTableItem(MSItems.CACTACEAE_CUTLASS).setWeight(4).setQuality(2).apply(minorDamageRange()))
						.add(LootItem.lootTableItem(Items.GOLDEN_SWORD).setWeight(1).setQuality(2).apply(minorDamageRange()))
						.add(LootItem.lootTableItem(Items.GOLDEN_AXE).setWeight(1).setQuality(2).apply(minorDamageRange()))
				));
		lootProcessor.accept(locationForTerrain(LandTypes.LUSH_DESERTS, WEAPON_ITEM_TABLE), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL)
						.add(LootItem.lootTableItem(MSItems.CACTACEAE_CUTLASS).setWeight(4).setQuality(2).apply(minorDamageRange()))
						.add(LootItem.lootTableItem(Items.GOLDEN_SWORD).setWeight(1).setQuality(2).apply(minorDamageRange()))
						.add(LootItem.lootTableItem(Items.GOLDEN_AXE).setWeight(1).setQuality(2).apply(minorDamageRange()))
				));
		lootProcessor.accept(locationForTerrain(LandTypes.RED_SAND, WEAPON_ITEM_TABLE), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL)
						.add(LootItem.lootTableItem(MSItems.CACTACEAE_CUTLASS).setWeight(4).setQuality(2).apply(minorDamageRange()))
						.add(LootItem.lootTableItem(Items.GOLDEN_SWORD).setWeight(1).setQuality(2).apply(minorDamageRange()))
						.add(LootItem.lootTableItem(Items.GOLDEN_AXE).setWeight(1).setQuality(2).apply(minorDamageRange()))
				));
		
		lootProcessor.accept(locationForTerrain(LandTypes.SANDSTONE, WEAPON_ITEM_TABLE), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL)
				));
		lootProcessor.accept(locationForTerrain(LandTypes.RED_SANDSTONE, WEAPON_ITEM_TABLE), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL)
				));
		
		lootProcessor.accept(locationForTerrain(LandTypes.SHADE, WEAPON_ITEM_TABLE), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL)
				));
		
		lootProcessor.accept(locationForTerrain(LandTypes.WOOD, WEAPON_ITEM_TABLE), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL)
						.add(LootItem.lootTableItem(MSItems.POGO_HAMMER).setWeight(3).setQuality(0).apply(minorDamageRange()))
				));
		
		lootProcessor.accept(locationForTerrain(LandTypes.RAINBOW, WEAPON_ITEM_TABLE), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL)
						.add(LootItem.lootTableItem(Items.SHEARS).setWeight(10).setQuality(-1).apply(minorDamageRange()))
				));
		
		lootProcessor.accept(locationForTerrain(LandTypes.FLORA, WEAPON_ITEM_TABLE), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL)
						.add(LootItem.lootTableItem(MSItems.SICKLE).setWeight(8).setQuality(-1).apply(minorDamageRange()))
				));
		
		lootProcessor.accept(locationForTerrain(LandTypes.END, WEAPON_ITEM_TABLE), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL)
						.add(LootItem.lootTableItem(Items.STONE_HOE).setWeight(5).setQuality(0).apply(minorDamageRange()))
				));
		
		lootProcessor.accept(locationForTerrain(LandTypes.RAIN, WEAPON_ITEM_TABLE), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL)
				));
		
		lootProcessor.accept(locationForTitle(LandTypes.FROGS, WEAPON_ITEM_TABLE), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL)
				));
		
		lootProcessor.accept(locationForTitle(LandTypes.WIND, WEAPON_ITEM_TABLE), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL)
						.add(LootItem.lootTableItem(MSItems.POGO_HAMMER).setWeight(1).setQuality(2).apply(minorDamageRange()))
				));
		
		lootProcessor.accept(locationForTitle(LandTypes.LIGHT, WEAPON_ITEM_TABLE), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL)
				));
		
		lootProcessor.accept(locationForTitle(LandTypes.CLOCKWORK, WEAPON_ITEM_TABLE), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL)
						.add(LootItem.lootTableItem(MSItems.BISICKLE).setWeight(5).setQuality(0))
				));
		
		lootProcessor.accept(locationForTitle(LandTypes.SILENCE, WEAPON_ITEM_TABLE), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL)
						.add(LootItem.lootTableItem(MSItems.NIGHT_CLUB).setWeight(1).setQuality(1).apply(minorDamageRange()))
				));
		
		lootProcessor.accept(locationForTitle(LandTypes.THUNDER, WEAPON_ITEM_TABLE), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL)
				));
		
		lootProcessor.accept(locationForTitle(LandTypes.PULSE, WEAPON_ITEM_TABLE), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL)
				));
		
		lootProcessor.accept(locationForTitle(LandTypes.THOUGHT, WEAPON_ITEM_TABLE), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL)
						.add(LootItem.lootTableItem(MSItems.SPEAR_CANE).setWeight(1).setQuality(1).apply(minorDamageRange()))
				));
		
		lootProcessor.accept(locationForTitle(LandTypes.BUCKETS, WEAPON_ITEM_TABLE), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL)
				));
		
		lootProcessor.accept(locationForTitle(LandTypes.CAKE, WEAPON_ITEM_TABLE), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL)
						.add(LootItem.lootTableItem(MSItems.SILVER_SPOON).setWeight(5).setQuality(0).apply(minorDamageRange()))
						.add(LootItem.lootTableItem(MSItems.FUDGESICKLE).setWeight(2).setQuality(1).apply(minorDamageRange()))
				));
		
		lootProcessor.accept(locationForTitle(LandTypes.RABBITS, WEAPON_ITEM_TABLE), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL)
				));
		
		lootProcessor.accept(locationForTitle(LandTypes.MONSTERS, WEAPON_ITEM_TABLE), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL)
						.add(LootItem.lootTableItem(MSItems.SPIKED_CLUB).setWeight(2).setQuality(0).apply(minorDamageRange()))
				));
		lootProcessor.accept(locationForTitle(LandTypes.UNDEAD, WEAPON_ITEM_TABLE), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL)
						.add(LootItem.lootTableItem(MSItems.SPIKED_CLUB).setWeight(2).setQuality(0).apply(minorDamageRange()))
				));
		
		lootProcessor.accept(locationForTitle(LandTypes.TOWERS, WEAPON_ITEM_TABLE), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL)
						.add(LootItem.lootTableItem(MSItems.IRON_CANE).setWeight(5).setQuality(1).apply(minorDamageRange()))
				));
		
		
		lootProcessor.accept(SUPPLY_ITEM_TABLE, LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL)
						.add(LandTableLootEntry.builder(SUPPLY_ITEM_TABLE).setPool(ITEM_POOL))
						.add(LootItem.lootTableItem(MSItems.CAPTCHA_CARD).setWeight(20).setQuality(0).apply(countRange(1, 4)))
						.add(LootItem.lootTableItem(Items.ARROW).setWeight(15).setQuality(0).apply(countRange(1, 16)))
						.add(LootItem.lootTableItem(Items.BREAD).setWeight(20).setQuality(0).apply(countRange(1, 3)))
						.add(LootItem.lootTableItem(Items.WHEAT).setWeight(15).setQuality(0).apply(countRange(1, 4)))
						.add(LootItem.lootTableItem(Items.APPLE).setWeight(15).setQuality(0).apply(countRange(1, 3)))
						.add(LootItem.lootTableItem(Items.EXPERIENCE_BOTTLE).setWeight(1).setQuality(2).apply(countRange(1, 2)))
						.add(LootItem.lootTableItem(Items.GOLDEN_APPLE).setWeight(1).setQuality(2))
						.add(LootItem.lootTableItem(Items.ROTTEN_FLESH).setWeight(15).setQuality(-2).apply(countRange(1, 5)))
						.add(LootItem.lootTableItem(Items.BONE).setWeight(12).setQuality(-1).apply(countRange(1, 3)))
						.add(LootItem.lootTableItem(MSItems.PHLEGM_GUSHERS).setWeight(7).setQuality(1).apply(countRange(3, 7)))
				));
		
		lootProcessor.accept(locationForTerrain(LandTypes.FOREST, SUPPLY_ITEM_TABLE), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL)
						.add(LootItem.lootTableItem(Items.STONE_AXE).setWeight(10).setQuality(0).apply(minorDamageRange()))
						.add(LootItem.lootTableItem(Items.IRON_AXE).setWeight(5).setQuality(1).apply(minorDamageRange()))
						.add(LootItem.lootTableItem(Items.DIAMOND_AXE).setWeight(1).setQuality(2).apply(minorDamageRange()))
				));
		lootProcessor.accept(locationForTerrain(LandTypes.TAIGA, SUPPLY_ITEM_TABLE), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL)
						.add(LootItem.lootTableItem(Items.STONE_AXE).setWeight(10).setQuality(0).apply(minorDamageRange()))
						.add(LootItem.lootTableItem(Items.IRON_AXE).setWeight(5).setQuality(1).apply(minorDamageRange()))
						.add(LootItem.lootTableItem(Items.DIAMOND_AXE).setWeight(1).setQuality(2).apply(minorDamageRange()))
				));
		
		lootProcessor.accept(locationForTerrain(LandTypes.FROST, SUPPLY_ITEM_TABLE), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL)
						.add(LootItem.lootTableItem(Items.FLINT_AND_STEEL).setWeight(10).setQuality(0).apply(damageRange(0.0F, 0.25F)))
						.add(LootItem.lootTableItem(Items.COAL).setWeight(8).setQuality(0).apply(countRange(1, 5)))
						.add(LootItem.lootTableItem(Items.DIAMOND).setWeight(1).setQuality(2).apply(countRange(1, 2)))
				));
		
		lootProcessor.accept(locationForTerrain(LandTypes.FUNGI, SUPPLY_ITEM_TABLE), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL)
						.add(LootItem.lootTableItem(MSItems.SPOREO).setWeight(6).setQuality(0).apply(countRange(1, 4)))
						.add(LootItem.lootTableItem(MSItems.MOREL_MUSHROOM).setWeight(1).setQuality(1))
				));
		
		lootProcessor.accept(locationForTerrain(LandTypes.HEAT, SUPPLY_ITEM_TABLE), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL)
						.add(LootItem.lootTableItem(Items.COAL).setWeight(8).setQuality(0).apply(countRange(1, 5)))
						.add(LootItem.lootTableItem(Items.BLAZE_POWDER).setWeight(5).setQuality(1).apply(countRange(1, 3)))
						.add(LootItem.lootTableItem(Items.BLAZE_ROD).setWeight(3).setQuality(1).apply(countRange(1, 2)))
						.add(LootItem.lootTableItem(Items.LAVA_BUCKET).setWeight(6).setQuality(0))
				));
		
		lootProcessor.accept(locationForTerrain(LandTypes.ROCK, SUPPLY_ITEM_TABLE), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL)
						.add(LootItem.lootTableItem(Items.STONE_PICKAXE).setWeight(15).setQuality(0).apply(minorDamageRange()))
						.add(LootItem.lootTableItem(Items.IRON_PICKAXE).setWeight(7).setQuality(1).apply(minorDamageRange()))
						.add(LootItem.lootTableItem(Items.DIAMOND_PICKAXE).setWeight(1).setQuality(2).apply(minorDamageRange()))
				));
		lootProcessor.accept(locationForTerrain(LandTypes.PETRIFICATION, SUPPLY_ITEM_TABLE), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL)
						.add(LootItem.lootTableItem(Items.STONE_PICKAXE).setWeight(15).setQuality(0).apply(minorDamageRange()))
						.add(LootItem.lootTableItem(Items.IRON_PICKAXE).setWeight(7).setQuality(1).apply(minorDamageRange()))
						.add(LootItem.lootTableItem(Items.DIAMOND_PICKAXE).setWeight(1).setQuality(2).apply(minorDamageRange()))
				));
		
		lootProcessor.accept(locationForTerrain(LandTypes.SAND, SUPPLY_ITEM_TABLE), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL)
						.add(LootItem.lootTableItem(Items.GOLD_INGOT).setWeight(6).setQuality(1).apply(countRange(1, 5)))
						.add(LootItem.lootTableItem(Items.GOLDEN_PICKAXE).setWeight(1).setQuality(2).apply(minorDamageRange()))
						.add(LootItem.lootTableItem(Items.GOLDEN_SHOVEL).setWeight(1).setQuality(2).apply(minorDamageRange()))
						.add(LootItem.lootTableItem(Items.GOLDEN_HOE).setWeight(2).setQuality(2).apply(minorDamageRange()))
				));
		lootProcessor.accept(locationForTerrain(LandTypes.LUSH_DESERTS, SUPPLY_ITEM_TABLE), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL)
						.add(LootItem.lootTableItem(Items.GOLD_INGOT).setWeight(6).setQuality(1).apply(countRange(1, 5)))
						.add(LootItem.lootTableItem(Items.GOLDEN_PICKAXE).setWeight(1).setQuality(2).apply(minorDamageRange()))
						.add(LootItem.lootTableItem(Items.GOLDEN_SHOVEL).setWeight(1).setQuality(2).apply(minorDamageRange()))
						.add(LootItem.lootTableItem(Items.GOLDEN_HOE).setWeight(2).setQuality(2).apply(minorDamageRange()))
				));
		lootProcessor.accept(locationForTerrain(LandTypes.RED_SAND, SUPPLY_ITEM_TABLE), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL)
						.add(LootItem.lootTableItem(Items.GOLD_INGOT).setWeight(6).setQuality(1).apply(countRange(1, 5)))
						.add(LootItem.lootTableItem(Items.GOLDEN_PICKAXE).setWeight(1).setQuality(2).apply(minorDamageRange()))
						.add(LootItem.lootTableItem(Items.GOLDEN_SHOVEL).setWeight(1).setQuality(2).apply(minorDamageRange()))
						.add(LootItem.lootTableItem(Items.GOLDEN_HOE).setWeight(2).setQuality(2).apply(minorDamageRange()))
				));
		
		lootProcessor.accept(locationForTerrain(LandTypes.SANDSTONE, SUPPLY_ITEM_TABLE), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL)
						.add(LootItem.lootTableItem(Items.GOLD_INGOT).setWeight(2).setQuality(2).apply(countRange(1, 3)))
						.add(LootItem.lootTableItem(Items.REDSTONE).setWeight(5).setQuality(0).apply(countRange(1, 7)))
						.add(LootItem.lootTableItem(Items.STONE_PICKAXE).setWeight(8).setQuality(0).apply(minorDamageRange()))
						.add(LootItem.lootTableItem(Items.IRON_PICKAXE).setWeight(3).setQuality(1).apply(minorDamageRange()))
				));
		lootProcessor.accept(locationForTerrain(LandTypes.RED_SANDSTONE, SUPPLY_ITEM_TABLE), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL)
						.add(LootItem.lootTableItem(Items.GOLD_INGOT).setWeight(2).setQuality(2).apply(countRange(1, 3)))
						.add(LootItem.lootTableItem(Items.REDSTONE).setWeight(5).setQuality(0).apply(countRange(1, 7)))
						.add(LootItem.lootTableItem(Items.STONE_PICKAXE).setWeight(8).setQuality(0).apply(minorDamageRange()))
						.add(LootItem.lootTableItem(Items.IRON_PICKAXE).setWeight(3).setQuality(1).apply(minorDamageRange()))
				));
		
		lootProcessor.accept(locationForTerrain(LandTypes.SHADE, SUPPLY_ITEM_TABLE), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL)
						.add(LootItem.lootTableItem(Items.RED_MUSHROOM).setWeight(10).setQuality(0).apply(countRange(1, 4)))
						.add(LootItem.lootTableItem(Items.BROWN_MUSHROOM).setWeight(10).setQuality(0).apply(countRange(1, 4)))
						.add(LootItem.lootTableItem(Items.MUSHROOM_STEW).setWeight(5).setQuality(1))
						.add(LootItem.lootTableItem(MSItems.OIL_BUCKET).setWeight(5).setQuality(0))
						.add(LootItem.lootTableItem(Items.TORCH).setWeight(8).setQuality(0).apply(countRange(1, 5)))
				));
		
		lootProcessor.accept(locationForTerrain(LandTypes.WOOD, SUPPLY_ITEM_TABLE), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL)
						.add(LootItem.lootTableItem(Items.IRON_INGOT).setWeight(4).setQuality(2).apply(countRange(1, 3)))
						.add(LootItem.lootTableItem(Items.REDSTONE).setWeight(5).setQuality(0).apply(countRange(1, 7)))
						.add(LootItem.lootTableItem(Items.IRON_AXE).setWeight(3).setQuality(1).apply(minorDamageRange()))
				));
		
		lootProcessor.accept(locationForTerrain(LandTypes.RAINBOW, SUPPLY_ITEM_TABLE), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL)
						.add(LootItem.lootTableItem(Items.LEATHER_CHESTPLATE).setWeight(2).setQuality(0).apply(minorDamageRange()))
						.add(LootItem.lootTableItem(Items.LEATHER_HELMET).setWeight(2).setQuality(0).apply(minorDamageRange()))
						.add(LootItem.lootTableItem(Items.LEATHER_LEGGINGS).setWeight(2).setQuality(0).apply(minorDamageRange()))
						.add(LootItem.lootTableItem(Items.LEATHER_BOOTS).setWeight(2).setQuality(0).apply(minorDamageRange()))
						.add(TagEntry.expandTag(MSTags.Items.FAYGO).setWeight(1).setQuality(0))
				));
		
		lootProcessor.accept(locationForTerrain(LandTypes.FLORA, SUPPLY_ITEM_TABLE), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL)
						.add(LootItem.lootTableItem(Items.SHEARS).setWeight(10).setQuality(-1).apply(minorDamageRange()))
				));
		
		lootProcessor.accept(locationForTerrain(LandTypes.END, SUPPLY_ITEM_TABLE), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL)
						.add(LootItem.lootTableItem(Items.ELYTRA).setWeight(1).setQuality(2).apply(minorDamageRange()))
						.add(LootItem.lootTableItem(Items.ENDER_PEARL).setWeight(10).setQuality(1).apply(countRange(1, 4)))
						.add(LootItem.lootTableItem(Items.CHORUS_FRUIT).setWeight(10).setQuality(-5).apply(countRange(1, 4)))
				));
		
		lootProcessor.accept(locationForTerrain(LandTypes.RAIN, SUPPLY_ITEM_TABLE), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL)
				));
		
		lootProcessor.accept(locationForTitle(LandTypes.FROGS, SUPPLY_ITEM_TABLE), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL)
						.add(LootItem.lootTableItem(MSItems.BUG_NET).setWeight(6).setQuality(2).apply(minorDamageRange()))
						.add(LootItem.lootTableItem(MSItems.GRASSHOPPER).setWeight(11).setQuality(-3).apply(countRange(2, 8)))
						.add(LootItem.lootTableItem(MSItems.GOLDEN_GRASSHOPPER).setWeight(1).setQuality(2))
				));
		
		lootProcessor.accept(locationForTitle(LandTypes.WIND, SUPPLY_ITEM_TABLE), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL)
				));
		
		lootProcessor.accept(locationForTitle(LandTypes.LIGHT, SUPPLY_ITEM_TABLE), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL)
						.add(LootItem.lootTableItem(Items.TORCH).setWeight(10).setQuality(-1))
				));
		
		lootProcessor.accept(locationForTitle(LandTypes.CLOCKWORK, SUPPLY_ITEM_TABLE), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL)
						.add(LootItem.lootTableItem(Items.CLOCK).setWeight(10).setQuality(0).apply(countRange(1, 5)))
						.add(LootItem.lootTableItem(Items.COMPASS).setWeight(5).setQuality(0))
				));
		
		lootProcessor.accept(locationForTitle(LandTypes.SILENCE, SUPPLY_ITEM_TABLE), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL)
				));
		
		lootProcessor.accept(locationForTitle(LandTypes.THUNDER, SUPPLY_ITEM_TABLE), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL)
				));
		
		lootProcessor.accept(locationForTitle(LandTypes.PULSE, SUPPLY_ITEM_TABLE), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL)
						.add(LootItem.lootTableItem(Items.BEEF).setWeight(8).setQuality(0).apply(countRange(1, 3)))
				));
		
		lootProcessor.accept(locationForTitle(LandTypes.THOUGHT, SUPPLY_ITEM_TABLE), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL)
						.add(LootItem.lootTableItem(MSItems.BRAIN_JUICE_BUCKET).setWeight(8).setQuality(-1))
				));
		
		lootProcessor.accept(locationForTitle(LandTypes.BUCKETS, SUPPLY_ITEM_TABLE), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL)
				));
		
		lootProcessor.accept(locationForTitle(LandTypes.CAKE, SUPPLY_ITEM_TABLE), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL)
						.add(LootItem.lootTableItem(Items.CAKE).setWeight(8).setQuality(-1))
						.add(LootItem.lootTableItem(MSBlocks.REVERSE_CAKE).setWeight(1).setQuality(0))
						.add(LootItem.lootTableItem(MSBlocks.APPLE_CAKE).setWeight(7).setQuality(-1))
						.add(LootItem.lootTableItem(MSBlocks.COLD_CAKE).setWeight(5).setQuality(-2))
						.add(LootItem.lootTableItem(MSBlocks.HOT_CAKE).setWeight(5).setQuality(-2))
						.add(LootItem.lootTableItem(MSBlocks.BLUE_CAKE).setWeight(3).setQuality(1))
						.add(LootItem.lootTableItem(MSBlocks.RED_CAKE).setWeight(3).setQuality(1))
						.add(LootItem.lootTableItem(MSBlocks.NEGATIVE_CAKE).setWeight(2).setQuality(1))
						.add(LootItem.lootTableItem(MSBlocks.CARROT_CAKE).setWeight(3).setQuality(1))
						.add(LootItem.lootTableItem(MSBlocks.CHOCOLATEY_CAKE).setWeight(4).setQuality(1))
						.add(LootItem.lootTableItem(Items.COOKIE).setWeight(5).setQuality(0).apply(countRange(2, 5)))
						.add(LootItem.lootTableItem(MSItems.CANDY_CORN).setWeight(3).setQuality(0))
				));
		
		lootProcessor.accept(locationForTitle(LandTypes.RABBITS, SUPPLY_ITEM_TABLE), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL)
						.add(LootItem.lootTableItem(Items.RABBIT).setWeight(10).setQuality(-1).apply(countRange(1, 2)))
						.add(LootItem.lootTableItem(Items.COOKED_RABBIT).setWeight(7).setQuality(0).apply(countRange(1, 2)))
						.add(LootItem.lootTableItem(Items.RABBIT_STEW).setWeight(4).setQuality(1))
						.add(LootItem.lootTableItem(Items.CARROT).setWeight(12).setQuality(0).apply(countRange(1, 5)))
				));
		
		lootProcessor.accept(locationForTitle(LandTypes.MONSTERS, SUPPLY_ITEM_TABLE), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL)
						.add(LootItem.lootTableItem(Items.ENDER_PEARL).setWeight(5).setQuality(2))
				));
		lootProcessor.accept(locationForTitle(LandTypes.UNDEAD, SUPPLY_ITEM_TABLE), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL)
						.add(LootItem.lootTableItem(Items.ENDER_PEARL).setWeight(5).setQuality(2))
				));
		
		lootProcessor.accept(locationForTitle(LandTypes.TOWERS, SUPPLY_ITEM_TABLE), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL)
						.add(LootItem.lootTableItem(Items.LADDER).setWeight(10).setQuality(-1).apply(countRange(3, 10)))
				));
		
		
		lootProcessor.accept(MISC_ITEM_TABLE, LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL)
						.add(LandTableLootEntry.builder(MISC_ITEM_TABLE).setPool(ITEM_POOL))
						.add(LootItem.lootTableItem(Items.IRON_INGOT).setWeight(15).setQuality(1).apply(countRange(1, 5)))
						.add(LootItem.lootTableItem(Items.GOLD_INGOT).setWeight(10).setQuality(1).apply(countRange(1, 4)))
						.add(LootItem.lootTableItem(Items.REDSTONE).setWeight(8).setQuality(1).apply(countRange(1, 3)))
						.add(LootItem.lootTableItem(Items.ENDER_PEARL).setWeight(2).setQuality(2))
						.add(LootItem.lootTableItem(MSItems.FLARP_MANUAL).setWeight(2).setQuality(0))
						.add(LootItem.lootTableItem(MSItems.SASSACRE_TEXT).setWeight(2).setQuality(0))
						.add(LootItem.lootTableItem(MSItems.WISEGUY).setWeight(2).setQuality(0))
						.add(LootItem.lootTableItem(MSItems.TABLESTUCK_MANUAL).setWeight(2).setQuality(0))
						.add(LootItem.lootTableItem(MSItems.TILLDEATH_HANDBOOK).setWeight(2).setQuality(0))
						.add(LootItem.lootTableItem(MSItems.BINARY_CODE).setWeight(2).setQuality(0))
						.add(LootItem.lootTableItem(MSItems.NONBINARY_CODE).setWeight(2).setQuality(0))
						.add(LootItem.lootTableItem(MSItems.INK_SQUID_PRO_QUO).setWeight(8).setQuality(0))
						.add(LootItem.lootTableItem(MSItems.BARBASOL).setWeight(10).setQuality(0).apply(countRange(1, 8)))
						.add(LootItem.lootTableItem(MSBlocks.GENERIC_OBJECT).setWeight(10).setQuality(-1))
						.add(LootItem.lootTableItem(MSItems.RAW_CRUXITE).setWeight(15).setQuality(1).apply(countRange(1, 5)))
						.add(LootItem.lootTableItem(MSItems.RAW_URANIUM).setWeight(12).setQuality(1).apply(countRange(1, 4)))
				));
		
		lootProcessor.accept(locationForTerrain(LandTypes.FOREST, MISC_ITEM_TABLE), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL)
						.add(LootItem.lootTableItem(Items.OAK_SAPLING).setWeight(5).setQuality(-1))
						.add(LootItem.lootTableItem(Items.SPRUCE_SAPLING).setWeight(5).setQuality(-1))
						.add(LootItem.lootTableItem(Items.BIRCH_SAPLING).setWeight(5).setQuality(-1))
						.add(LootItem.lootTableItem(Items.OAK_LOG).setWeight(5).setQuality(-1).apply(countRange(1, 3)))
						.add(LootItem.lootTableItem(Items.SPRUCE_LOG).setWeight(5).setQuality(-1).apply(countRange(1, 3)))
						.add(LootItem.lootTableItem(Items.BIRCH_LOG).setWeight(5).setQuality(-1).apply(countRange(1, 3)))
						.add(LootItem.lootTableItem(Items.JUNGLE_LOG).setWeight(5).setQuality(-1).apply(countRange(1, 3)))
						.add(LootItem.lootTableItem(Items.ACACIA_LOG).setWeight(5).setQuality(-1).apply(countRange(1, 3)))
						.add(LootItem.lootTableItem(Items.DARK_OAK_LOG).setWeight(5).setQuality(-1).apply(countRange(1, 3)))
						.add(LootItem.lootTableItem(Items.OAK_PLANKS).setWeight(3).setQuality(-2).apply(countRange(1, 5)))
						.add(LootItem.lootTableItem(Items.SPRUCE_PLANKS).setWeight(3).setQuality(-2).apply(countRange(1, 5)))
						.add(LootItem.lootTableItem(Items.BIRCH_PLANKS).setWeight(3).setQuality(-2).apply(countRange(1, 5)))
						.add(LootItem.lootTableItem(Items.OAK_LEAVES).setWeight(10).setQuality(-2).apply(countRange(2, 15)))
				));
		lootProcessor.accept(locationForTerrain(LandTypes.TAIGA, MISC_ITEM_TABLE), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL)
						.add(LootItem.lootTableItem(Items.OAK_SAPLING).setWeight(5).setQuality(-1))
						.add(LootItem.lootTableItem(Items.SPRUCE_SAPLING).setWeight(5).setQuality(-1))
						.add(LootItem.lootTableItem(Items.BIRCH_SAPLING).setWeight(5).setQuality(-1))
						.add(LootItem.lootTableItem(Items.OAK_LOG).setWeight(5).setQuality(-1).apply(countRange(1, 3)))
						.add(LootItem.lootTableItem(Items.SPRUCE_LOG).setWeight(5).setQuality(-1).apply(countRange(1, 3)))
						.add(LootItem.lootTableItem(Items.BIRCH_LOG).setWeight(5).setQuality(-1).apply(countRange(1, 3)))
						.add(LootItem.lootTableItem(Items.JUNGLE_LOG).setWeight(5).setQuality(-1).apply(countRange(1, 3)))
						.add(LootItem.lootTableItem(Items.ACACIA_LOG).setWeight(5).setQuality(-1).apply(countRange(1, 3)))
						.add(LootItem.lootTableItem(Items.DARK_OAK_LOG).setWeight(5).setQuality(-1).apply(countRange(1, 3)))
						.add(LootItem.lootTableItem(Items.OAK_PLANKS).setWeight(3).setQuality(-2).apply(countRange(1, 5)))
						.add(LootItem.lootTableItem(Items.SPRUCE_PLANKS).setWeight(3).setQuality(-2).apply(countRange(1, 5)))
						.add(LootItem.lootTableItem(Items.BIRCH_PLANKS).setWeight(3).setQuality(-2).apply(countRange(1, 5)))
						.add(LootItem.lootTableItem(Items.OAK_LEAVES).setWeight(10).setQuality(-2).apply(countRange(2, 15)))
				));
		
		lootProcessor.accept(locationForTerrain(LandTypes.FROST, MISC_ITEM_TABLE), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL)
						.add(LootItem.lootTableItem(Items.SNOWBALL).setWeight(20).setQuality(-1).apply(countRange(1, 10)))
						.add(LootItem.lootTableItem(Items.SNOW).setWeight(10).setQuality(-2).apply(countRange(1, 2)))
						.add(LootItem.lootTableItem(Items.SNOW_BLOCK).setWeight(10).setQuality(-2).apply(countRange(1, 3)))
						.add(LootItem.lootTableItem(Items.SPRUCE_SAPLING).setWeight(15).setQuality(-1))
				));
		
		lootProcessor.accept(locationForTerrain(LandTypes.FUNGI, MISC_ITEM_TABLE), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL)
						.add(LootItem.lootTableItem(Items.BROWN_MUSHROOM).setWeight(8).setQuality(0).apply(countRange(2, 8)))
						.add(LootItem.lootTableItem(Items.RED_MUSHROOM).setWeight(8).setQuality(0).apply(countRange(2, 7)))
						.add(LootItem.lootTableItem(Items.SLIME_BALL).setWeight(5).setQuality(0).apply(countRange(1, 5)))
						.add(LootItem.lootTableItem(MSItems.FUNGAL_SPORE).setWeight(10).setQuality(0).apply(countRange(6, 11)))
						.add(LootItem.lootTableItem(MSItems.SUSHROOM).setWeight(1).setQuality(0))
				));
		
		lootProcessor.accept(locationForTerrain(LandTypes.HEAT, MISC_ITEM_TABLE), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL)
						.add(LootItem.lootTableItem(Items.MAGMA_CREAM).setWeight(5).setQuality(0).apply(countRange(1, 2)))
						.add(LootItem.lootTableItem(Items.BRICK).setWeight(10).setQuality(-1).apply(countRange(1, 8)))
						.add(LootItem.lootTableItem(Items.NETHERRACK).setWeight(15).setQuality(-2).apply(countRange(2, 16)))
						.add(LootItem.lootTableItem(Items.OBSIDIAN).setWeight(5).setQuality(-1).apply(countRange(1, 3)))
						.add(LootItem.lootTableItem(MSBlocks.CAST_IRON).setWeight(5).setQuality(-1).apply(countRange(1, 8)))
						.add(LootItem.lootTableItem(MSBlocks.NATIVE_SULFUR).setWeight(4).setQuality(-1).apply(countRange(1, 6)))
						.add(LootItem.lootTableItem(MSBlocks.CHISELED_CAST_IRON).setWeight(3).setQuality(-1).apply(countRange(1, 3)))
				));
		
		lootProcessor.accept(locationForTerrain(LandTypes.ROCK, MISC_ITEM_TABLE), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL)
						.add(LootItem.lootTableItem(Items.STONE).setWeight(10).setQuality(-2).apply(countRange(1, 7)))
						.add(LootItem.lootTableItem(Items.COBBLESTONE).setWeight(15).setQuality(-2).apply(countRange(2, 16)))
						.add(LootItem.lootTableItem(Items.GRAVEL).setWeight(10).setQuality(-2).apply(countRange(1, 7)))
						.add(LootItem.lootTableItem(Items.BRICK).setWeight(8).setQuality(-1).apply(countRange(1, 6)))
						.add(LootItem.lootTableItem(Items.BRICKS).setWeight(5).setQuality(-1).apply(countRange(1, 4)))
				));
		lootProcessor.accept(locationForTerrain(LandTypes.PETRIFICATION, MISC_ITEM_TABLE), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL)
						.add(LootItem.lootTableItem(Items.STONE).setWeight(10).setQuality(-2).apply(countRange(1, 7)))
						.add(LootItem.lootTableItem(Items.COBBLESTONE).setWeight(15).setQuality(-2).apply(countRange(2, 16)))
						.add(LootItem.lootTableItem(Items.GRAVEL).setWeight(10).setQuality(-2).apply(countRange(1, 7)))
						.add(LootItem.lootTableItem(Items.BRICK).setWeight(8).setQuality(-1).apply(countRange(1, 6)))
						.add(LootItem.lootTableItem(Items.BRICKS).setWeight(5).setQuality(-1).apply(countRange(1, 4)))
				));
		
		lootProcessor.accept(locationForTerrain(LandTypes.SAND, MISC_ITEM_TABLE), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL)
						.add(LootItem.lootTableItem(Items.SAND).setWeight(15).setQuality(-2).apply(countRange(2, 16)))
						.add(LootItem.lootTableItem(Items.RED_SAND).setWeight(5).setQuality(-2).apply(countRange(1, 5)))
						.add(LootItem.lootTableItem(Items.SANDSTONE).setWeight(10).setQuality(-2).apply(countRange(2, 12)))
						.add(LootItem.lootTableItem(Items.SMOOTH_SANDSTONE).setWeight(6).setQuality(-2).apply(countRange(1, 4)))
						.add(LootItem.lootTableItem(Items.RED_SANDSTONE).setWeight(3).setQuality(-2).apply(countRange(1, 4)))
						.add(LootItem.lootTableItem(Items.CACTUS).setWeight(12).setQuality(-1).apply(countRange(1, 5)))
				));
		lootProcessor.accept(locationForTerrain(LandTypes.LUSH_DESERTS, MISC_ITEM_TABLE), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL)
						.add(LootItem.lootTableItem(Items.SAND).setWeight(15).setQuality(-2).apply(countRange(2, 16)))
						.add(LootItem.lootTableItem(Items.RED_SAND).setWeight(5).setQuality(-2).apply(countRange(1, 5)))
						.add(LootItem.lootTableItem(Items.SANDSTONE).setWeight(10).setQuality(-2).apply(countRange(2, 12)))
						.add(LootItem.lootTableItem(Items.SMOOTH_SANDSTONE).setWeight(6).setQuality(-2).apply(countRange(1, 4)))
						.add(LootItem.lootTableItem(Items.RED_SANDSTONE).setWeight(3).setQuality(-2).apply(countRange(1, 4)))
						.add(LootItem.lootTableItem(Items.CACTUS).setWeight(12).setQuality(-1).apply(countRange(1, 5)))
				));
		lootProcessor.accept(locationForTerrain(LandTypes.RED_SAND, MISC_ITEM_TABLE), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL)
						.add(LootItem.lootTableItem(Items.RED_SAND).setWeight(15).setQuality(-2).apply(countRange(2, 16)))
						.add(LootItem.lootTableItem(Items.SAND).setWeight(5).setQuality(-2).apply(countRange(1, 5)))
						.add(LootItem.lootTableItem(Items.RED_SANDSTONE).setWeight(10).setQuality(-2).apply(countRange(2, 12)))
						.add(LootItem.lootTableItem(Items.SMOOTH_RED_SANDSTONE).setWeight(6).setQuality(-2).apply(countRange(1, 4)))
						.add(LootItem.lootTableItem(Items.SANDSTONE).setWeight(3).setQuality(-2).apply(countRange(1, 4)))
						.add(LootItem.lootTableItem(Items.CACTUS).setWeight(12).setQuality(-1).apply(countRange(1, 5)))
				));
		
		lootProcessor.accept(locationForTerrain(LandTypes.SANDSTONE, MISC_ITEM_TABLE), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL)
						.add(LootItem.lootTableItem(Items.SANDSTONE).setWeight(15).setQuality(-2).apply(countRange(2, 16)))
						.add(LootItem.lootTableItem(Items.SMOOTH_SANDSTONE).setWeight(10).setQuality(-2).apply(countRange(1, 8)))
						.add(LootItem.lootTableItem(Items.CHISELED_SANDSTONE).setWeight(4).setQuality(-2).apply(countRange(1, 2)))
						.add(LootItem.lootTableItem(Items.RED_SANDSTONE).setWeight(4).setQuality(-2).apply(countRange(1, 4)))
						.add(LootItem.lootTableItem(Items.SAND).setWeight(8).setQuality(-2).apply(countRange(1, 5)))
				));
		lootProcessor.accept(locationForTerrain(LandTypes.RED_SANDSTONE, MISC_ITEM_TABLE), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL)
						.add(LootItem.lootTableItem(Items.RED_SANDSTONE).setWeight(15).setQuality(-2).apply(countRange(2, 16)))
						.add(LootItem.lootTableItem(Items.SMOOTH_RED_SANDSTONE).setWeight(10).setQuality(-2).apply(countRange(1, 8)))
						.add(LootItem.lootTableItem(Items.CHISELED_RED_SANDSTONE).setWeight(4).setQuality(-2).apply(countRange(1, 2)))
						.add(LootItem.lootTableItem(Items.SANDSTONE).setWeight(4).setQuality(-2).apply(countRange(1, 4)))
						.add(LootItem.lootTableItem(Items.RED_SAND).setWeight(8).setQuality(-2).apply(countRange(1, 5)))
				));
		
		lootProcessor.accept(locationForTerrain(LandTypes.SHADE, MISC_ITEM_TABLE), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL)
						.add(LootItem.lootTableItem(MSBlocks.BLUE_DIRT).setWeight(15).setQuality(-2).apply(countRange(2, 16)))
						.add(LootItem.lootTableItem(MSBlocks.GLOWING_LOG).setWeight(6).setQuality(-1).apply(countRange(1, 3)))
						.add(LootItem.lootTableItem(MSBlocks.GLOWING_MUSHROOM).setWeight(12).setQuality(-2).apply(countRange(1, 5)))
						.add(LootItem.lootTableItem(MSBlocks.GLOWING_MUSHROOM_VINES).setWeight(4).setQuality(-1).apply(countRange(1, 3)))
				));
		
		lootProcessor.accept(locationForTerrain(LandTypes.WOOD, MISC_ITEM_TABLE), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL)
						.add(LootItem.lootTableItem(Items.DARK_OAK_PLANKS).setWeight(5).setQuality(-2).apply(countRange(1, 8)))
						.add(LootItem.lootTableItem(Items.JUNGLE_PLANKS).setWeight(8).setQuality(-2).apply(countRange(2, 16)))
						.add(LootItem.lootTableItem(Items.OAK_PLANKS).setWeight(8).setQuality(-2).apply(countRange(2, 16)))
						.add(LootItem.lootTableItem(Items.DARK_OAK_LOG).setWeight(4).setQuality(-2).apply(countRange(1, 4)))
						.add(LootItem.lootTableItem(Items.JUNGLE_LOG).setWeight(5).setQuality(-2).apply(countRange(1, 8)))
						.add(LootItem.lootTableItem(Items.OAK_LOG).setWeight(4).setQuality(-2).apply(countRange(1, 5)))
						.add(LootItem.lootTableItem(Items.DARK_OAK_SAPLING).setWeight(4).setQuality(-2).apply(countRange(1, 2)))
						.add(LootItem.lootTableItem(Items.JUNGLE_SAPLING).setWeight(4).setQuality(-2).apply(countRange(1, 2)))
						.add(LootItem.lootTableItem(Items.OAK_SAPLING).setWeight(4).setQuality(-2).apply(countRange(1, 4)))
						.add(LootItem.lootTableItem(MSBlocks.GLOWING_LOG).setWeight(4).setQuality(-2).apply(countRange(1, 5)))
						.add(LootItem.lootTableItem(Items.COCOA_BEANS).setWeight(3).setQuality(0))
				));
		
		lootProcessor.accept(locationForTerrain(LandTypes.RAINBOW, MISC_ITEM_TABLE), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL)
						.add(TagEntry.expandTag(Tags.Items.DYES).setWeight(1).setQuality(-3).apply(countRange(1, 8)))
						.add(LootItem.lootTableItem(Items.WHITE_WOOL).setWeight(3).setQuality(-1).apply(countRange(8, 16)))
						.add(LootItem.lootTableItem(Items.GLASS).setWeight(3).setQuality(-1).apply(countRange(8, 16)))
						.add(LootItem.lootTableItem(Items.TERRACOTTA).setWeight(2).setQuality(1).apply(countRange(8, 16)))
				));
		
		lootProcessor.accept(locationForTerrain(LandTypes.FLORA, MISC_ITEM_TABLE), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL)
						.add(LootItem.lootTableItem(Items.DANDELION).setWeight(3).setQuality(-1).apply(countRange(1, 4)))
						.add(LootItem.lootTableItem(Items.POPPY).setWeight(3).setQuality(-1).apply(countRange(1, 4)))
						.add(LootItem.lootTableItem(Items.BLUE_ORCHID).setWeight(3).setQuality(-1).apply(countRange(1, 4)))
						.add(LootItem.lootTableItem(Items.ALLIUM).setWeight(3).setQuality(-1).apply(countRange(1, 4)))
						.add(LootItem.lootTableItem(Items.AZURE_BLUET).setWeight(3).setQuality(-1).apply(countRange(1, 4)))
						.add(LootItem.lootTableItem(Items.RED_TULIP).setWeight(3).setQuality(-1).apply(countRange(1, 4)))
						.add(LootItem.lootTableItem(Items.ORANGE_TULIP).setWeight(3).setQuality(-1).apply(countRange(1, 4)))
						.add(LootItem.lootTableItem(Items.WHITE_TULIP).setWeight(3).setQuality(-1).apply(countRange(1, 4)))
						.add(LootItem.lootTableItem(Items.PINK_TULIP).setWeight(3).setQuality(-1).apply(countRange(1, 4)))
						.add(LootItem.lootTableItem(Items.OXEYE_DAISY).setWeight(3).setQuality(-1).apply(countRange(1, 4)))
						.add(LootItem.lootTableItem(Items.SUNFLOWER).setWeight(6).setQuality(-2).apply(countRange(1, 3)))
						.add(LootItem.lootTableItem(Items.LILAC).setWeight(6).setQuality(-2).apply(countRange(1, 3)))
						.add(LootItem.lootTableItem(Items.ROSE_BUSH).setWeight(6).setQuality(-2).apply(countRange(1, 3)))
						.add(LootItem.lootTableItem(Items.PEONY).setWeight(6).setQuality(-2).apply(countRange(1, 3)))
						.add(LootItem.lootTableItem(Items.FERN).setWeight(11).setQuality(-2).apply(countRange(1, 5)))
						.add(LootItem.lootTableItem(MSBlocks.FLOWERY_MOSSY_COBBLESTONE).setWeight(9).setQuality(0).apply(countRange(8, 24)))
						.add(LootItem.lootTableItem(Items.MOSSY_COBBLESTONE).setWeight(9).setQuality(0).apply(countRange(8, 24)))
				));
		
		lootProcessor.accept(locationForTerrain(LandTypes.END, MISC_ITEM_TABLE), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL)
						.add(LootItem.lootTableItem(Items.DRAGON_BREATH).setWeight(1).setQuality(1))
						.add(LootItem.lootTableItem(Items.SHULKER_SHELL).setWeight(1).setQuality(1))
						.add(LootItem.lootTableItem(Items.END_STONE).setWeight(8).setQuality(-1).apply(countRange(1, 16)))
						.add(LootItem.lootTableItem(Items.END_ROD).setWeight(5).setQuality(0).apply(countRange(1, 2)))
						.add(LootItem.lootTableItem(MSBlocks.END_GRASS).setWeight(5).setQuality(-1).apply(countRange(1, 16)))
						.add(LootItem.lootTableItem(MSBlocks.COARSE_END_STONE).setWeight(8).setQuality(-1).apply(countRange(1, 6)))
				));
		
		lootProcessor.accept(locationForTerrain(LandTypes.RAIN, MISC_ITEM_TABLE), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL)
						.add(LootItem.lootTableItem(Items.BEACON).setWeight(1).setQuality(-2))
				));
		
		lootProcessor.accept(locationForTitle(LandTypes.FROGS, MISC_ITEM_TABLE), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL)
						.add(LootItem.lootTableItem(Items.LILY_PAD).setWeight(9).setQuality(-2).apply(countRange(1, 9)))
						.add(LootItem.lootTableItem(MSBlocks.MINI_FROG_STATUE).setWeight(5).setQuality(3))
						.add(LootItem.lootTableItem(MSItems.THRESH_DVD).setWeight(4).setQuality(2))
				));
		
		lootProcessor.accept(locationForTitle(LandTypes.WIND, MISC_ITEM_TABLE), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL)
						.add(LootItem.lootTableItem(MSItems.CREW_POSTER).setWeight(4).setQuality(2))
				));
		
		lootProcessor.accept(locationForTitle(LandTypes.LIGHT, MISC_ITEM_TABLE), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL)
						.add(LootItem.lootTableItem(Items.GLOWSTONE).setWeight(8).setQuality(-1))
						.add(LootItem.lootTableItem(MSItems.DICE).setWeight(8).setQuality(2))
						.add(LootItem.lootTableItem(Items.GLOWSTONE_DUST).setWeight(12).setQuality(-1).apply(countRange(2, 5)))
						.add(LootItem.lootTableItem(MSItems.SBAHJ_POSTER).setWeight(4).setQuality(2))
				));
		
		lootProcessor.accept(locationForTitle(LandTypes.CLOCKWORK, MISC_ITEM_TABLE), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL)
						.add(LootItem.lootTableItem(Items.REPEATER).setWeight(4).setQuality(0))
						.add(LootItem.lootTableItem(MSItems.SBAHJ_POSTER).setWeight(4).setQuality(2))
				));
		
		lootProcessor.accept(locationForTitle(LandTypes.SILENCE, MISC_ITEM_TABLE), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL)
						.add(LootItem.lootTableItem(Items.PUMPKIN).setWeight(5).setQuality(0))
						.add(LootItem.lootTableItem(MSItems.CREW_POSTER).setWeight(4).setQuality(2))
				));
		
		lootProcessor.accept(locationForTitle(LandTypes.THUNDER, MISC_ITEM_TABLE), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL)
						.add(LootItem.lootTableItem(MSItems.CREW_POSTER).setWeight(4).setQuality(2))
				));
		
		lootProcessor.accept(locationForTitle(LandTypes.PULSE, MISC_ITEM_TABLE), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL)
						.add(LootItem.lootTableItem(Items.REPEATER).setWeight(2).setQuality(0))
						.add(LootItem.lootTableItem(MSItems.RAZOR_BLADE).setWeight(4).setQuality(-1))
						.add(LootItem.lootTableItem(MSItems.THRESH_DVD).setWeight(4).setQuality(2))
				));
		
		lootProcessor.accept(locationForTitle(LandTypes.THOUGHT, MISC_ITEM_TABLE), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL)
						.add(LootItem.lootTableItem(MSBlocks.THOUGHT_DIRT).setWeight(15).setQuality(-1).apply(countRange(2, 10)))
						.add(LootItem.lootTableItem(MSItems.SBAHJ_POSTER).setWeight(4).setQuality(2))
				));
		
		lootProcessor.accept(locationForTitle(LandTypes.BUCKETS, MISC_ITEM_TABLE), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL)
						.add(LootItem.lootTableItem(Items.BUCKET).setWeight(10).setQuality(0))
						.add(LootItem.lootTableItem(Items.WATER_BUCKET).setWeight(8).setQuality(0))
						.add(LootItem.lootTableItem(Items.LAVA_BUCKET).setWeight(5).setQuality(0))
						.add(LootItem.lootTableItem(MSItems.OIL_BUCKET).setWeight(5).setQuality(0))
						.add(LootItem.lootTableItem(MSItems.BLOOD_BUCKET).setWeight(5).setQuality(0))
						.add(LootItem.lootTableItem(MSItems.BRAIN_JUICE_BUCKET).setWeight(5).setQuality(0))
						.add(LootItem.lootTableItem(MSItems.WATER_COLORS_BUCKET).setWeight(5).setQuality(0))
						.add(LootItem.lootTableItem(MSItems.ENDER_BUCKET).setWeight(5).setQuality(0))
						.add(LootItem.lootTableItem(MSItems.OBSIDIAN_BUCKET).setWeight(5).setQuality(0))
						.add(LootItem.lootTableItem(MSItems.THRESH_DVD).setWeight(4).setQuality(2))
				));
		
		lootProcessor.accept(locationForTitle(LandTypes.CAKE, MISC_ITEM_TABLE), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL)
						.add(LootItem.lootTableItem(Items.SUGAR).setWeight(8).setQuality(-1).apply(countRange(1, 3)))
						.add(LootItem.lootTableItem(Items.MILK_BUCKET).setWeight(5).setQuality(0))
						.add(LootItem.lootTableItem(Items.COCOA_BEANS).setWeight(3).setQuality(0))
						.add(LootItem.lootTableItem(MSItems.THRESH_DVD).setWeight(4).setQuality(2))
				));
		
		lootProcessor.accept(locationForTitle(LandTypes.RABBITS, MISC_ITEM_TABLE), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL)
						.add(LootItem.lootTableItem(Items.RABBIT_HIDE).setWeight(8).setQuality(0).apply(countRange(1, 3)))
						.add(LootItem.lootTableItem(Items.RABBIT_FOOT).setWeight(2).setQuality(1))
						.add(LootItem.lootTableItem(MSBlocks.GOLD_SEEDS).setWeight(3).setQuality(0).apply(countRange(1, 3)))
						.add(LootItem.lootTableItem(MSItems.SBAHJ_POSTER).setWeight(4).setQuality(2))
				));
		
		lootProcessor.accept(locationForTitle(LandTypes.MONSTERS, MISC_ITEM_TABLE), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL)
						.add(LootItem.lootTableItem(Items.ROTTEN_FLESH).setWeight(15).setQuality(-2).apply(countRange(1, 3)))
						.add(LootItem.lootTableItem(Items.BONE).setWeight(12).setQuality(-1).apply(countRange(1, 3)))
						.add(LootItem.lootTableItem(Items.GUNPOWDER).setWeight(8).setQuality(0).apply(countRange(1, 3)))
						.add(LootItem.lootTableItem(Items.STRING).setWeight(10).setQuality(0).apply(countRange(1, 3)))
						.add(LootItem.lootTableItem(MSItems.CREW_POSTER).setWeight(4).setQuality(2))
				));
		lootProcessor.accept(locationForTitle(LandTypes.UNDEAD, MISC_ITEM_TABLE), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL)
						.add(LootItem.lootTableItem(Items.ROTTEN_FLESH).setWeight(15).setQuality(-2).apply(countRange(1, 3)))
						.add(LootItem.lootTableItem(Items.BONE).setWeight(12).setQuality(-1).apply(countRange(1, 3)))
						.add(LootItem.lootTableItem(Items.GUNPOWDER).setWeight(8).setQuality(0).apply(countRange(1, 3)))
						.add(LootItem.lootTableItem(Items.STRING).setWeight(10).setQuality(0).apply(countRange(1, 3)))
						.add(LootItem.lootTableItem(MSItems.CREW_POSTER).setWeight(4).setQuality(2))
				));
		
		lootProcessor.accept(locationForTitle(LandTypes.TOWERS, MISC_ITEM_TABLE), LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL)
						.add(LootItem.lootTableItem(MSItems.THRESH_DVD).setWeight(4).setQuality(2))
				));
		
		
		lootProcessor.accept(RARE_ITEM_TABLE, LootTable.lootTable()
				.withPool(LootPool.lootPool().name(ITEM_POOL)
						.add(LootItem.lootTableItem(Items.IRON_INGOT).setWeight(20).setQuality(1).apply(countRange(0, 5)))
						.add(LootItem.lootTableItem(Items.GOLD_INGOT).setWeight(15).setQuality(1).apply(countRange(0, 4)))
						.add(LootItem.lootTableItem(Items.DIAMOND).setWeight(15).setQuality(2).apply(countRange(0, 2)))
						.add(LootItem.lootTableItem(MSItems.GRIMOIRE).setWeight(3).setQuality(1))
						.add(LootItem.lootTableItem(MSBlocks.MINI_WIZARD_STATUE).setWeight(5).setQuality(1))
						.add(LootItem.lootTableItem(MSBlocks.CASSETTE_PLAYER).setWeight(1).setQuality(3))
						.add(LootItem.lootTableItem(MSBlocks.SENDIFICATOR).setWeight(1).setQuality(3))
						.add(LootItem.lootTableItem(MSBlocks.TRANSPORTALIZER).setWeight(2).setQuality(2))
						.add(LootItem.lootTableItem(MSItems.DICE).setWeight(5).setQuality(0))
						.add(LootItem.lootTableItem(MSItems.CUEBALL).setWeight(1).setQuality(4))
						.add(LootItem.lootTableItem(MSItems.CRYPTID_PHOTO).setWeight(1).setQuality(4))
						.add(LootItem.lootTableItem(MSBlocks.COMPUTER).setWeight(1).setQuality(0))
						.add(LootItem.lootTableItem(MSBlocks.LAPTOP).setWeight(1).setQuality(0))
						.add(LootItem.lootTableItem(MSBlocks.CHESSBOARD).setWeight(2).setQuality(0))
						.add(LootItem.lootTableItem(MSItems.CIGARETTE_LANCE).setWeight(2).setQuality(0))
						.add(LootItem.lootTableItem(MSItems.ACE_OF_CLUBS).setWeight(2).setQuality(0))
						.add(LootItem.lootTableItem(MSItems.ACE_OF_DIAMONDS).setWeight(2).setQuality(0))
						.add(LootItem.lootTableItem(MSItems.ACE_OF_HEARTS).setWeight(2).setQuality(0))
						.add(LootItem.lootTableItem(MSItems.ACE_OF_SPADES).setWeight(2).setQuality(0))
						.add(LootItem.lootTableItem(MSItems.ENERGY_CORE).setWeight(10).setQuality(0))
						.add(LootItem.lootTableItem(MSItems.GUTTER_BALL).setWeight(1).setQuality(0))
						.add(LootItem.lootTableItem(MSItems.PLUSH_MUTATED_CAT).setWeight(2).setQuality(1))
				));
		
		
		lootProcessor.accept(MSLootTables.BASIC_MEDIUM_CHEST, LootTable.lootTable()
				.withPool(LootPool.lootPool().name("weapons").setRolls(ConstantValue.exactly(1))
						.add(LootTableReference.lootTableReference(WEAPON_ITEM_TABLE)))
				.withPool(LootPool.lootPool().name("supplies").setRolls(ConstantValue.exactly(3))
						.add(LootTableReference.lootTableReference(SUPPLY_ITEM_TABLE)))
				.withPool(LootPool.lootPool().name("misc").setRolls(UniformGenerator.between(2, 4))
						.add(LootTableReference.lootTableReference(MISC_ITEM_TABLE)))
				.withPool(LootPool.lootPool().name("rare").setRolls(UniformGenerator.between(0, 1))
						.add(LootTableReference.lootTableReference(RARE_ITEM_TABLE)))
				.withPool(LootPool.lootPool().name("boondollars").setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(MSItems.SORROW_GUSHERS).setWeight(7).setQuality(-1).apply(countRange(3, 7)))
						.add(LootItem.lootTableItem(MSItems.BOONDOLLARS).setWeight(10).setQuality(-1).apply(SetBoondollarCount.builder(UniformGenerator.between(5, 50))))
						.add(LootItem.lootTableItem(MSItems.BOONDOLLARS).setWeight(5).setQuality(0).apply(SetBoondollarCount.builder(UniformGenerator.between(50, 250))))
						.add(LootItem.lootTableItem(MSItems.BOONDOLLARS).setWeight(2).setQuality(0).apply(SetBoondollarCount.builder(UniformGenerator.between(250, 1000))))
				));
		
		
	}
	
	public static ResourceLocation locationForTerrain(Supplier<TerrainLandType> landType, ResourceLocation baseLoot)
	{
		ResourceLocation landName = Objects.requireNonNull(LandTypes.TERRAIN_REGISTRY.getKey(landType.get()));
		return baseLoot.withSuffix("/terrain/" + landName.getNamespace() + "/" + landName.getPath());
	}
	
	public static ResourceLocation locationForTitle(Supplier<TitleLandType> landType, ResourceLocation baseLoot)
	{
		ResourceLocation landName = Objects.requireNonNull(LandTypes.TITLE_REGISTRY.getKey(landType.get()));
		return baseLoot.withSuffix("/title/" + landName.getNamespace() + "/" + landName.getPath());
	}
	
	public LootItemFunction.Builder minorDamageRange()
	{
		return damageRange(0.75F, 1.0F);
	}
	
	public LootItemFunction.Builder damageRange(float min, float max)
	{
		return SetItemDamageFunction.setDamage(UniformGenerator.between(min, max));
	}
	
	public LootItemFunction.Builder countRange(float min, float max)
	{
		return SetItemCountFunction.setCount(UniformGenerator.between(min, max));
	}
}
