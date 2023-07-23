package com.mraof.minestuck.data.loot_table;

import com.mraof.minestuck.item.IncompleteSburbCodeItem;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.item.loot.MSLootTables;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetNbtFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.function.BiConsumer;

public class MSMiscLootTables implements LootTableSubProvider
{
	@Override
	public void generate(BiConsumer<ResourceLocation, LootTable.Builder> lootProcessor)
	{
		lootProcessor.accept(MSLootTables.KUNDLER_SUPRISES, LootTable.lootTable()
				.withPool(LootPool.lootPool().name("computer").setRolls(ConstantValue.exactly(1)) //has only one item generate, with equal chance for each
						.add(LootItem.lootTableItem(Items.MELON_SLICE).setWeight(10).setQuality(0))
						.add(LootItem.lootTableItem(Items.STICK).setWeight(10).setQuality(0))
						.add(LootItem.lootTableItem(Items.EGG).setWeight(10).setQuality(0))
						.add(LootItem.lootTableItem(Items.DIRT).setWeight(10).setQuality(0))
						.add(LootItem.lootTableItem(Items.PUMPKIN).setWeight(10).setQuality(0))
						.add(LootItem.lootTableItem(Items.COBBLESTONE).setWeight(10).setQuality(0))
						.add(LootItem.lootTableItem(Items.REDSTONE).setWeight(10).setQuality(0))
						.add(LootItem.lootTableItem(MSItems.SURPRISE_EMBRYO.get()).setWeight(10).setQuality(0))
						.add(LootItem.lootTableItem(MSItems.GAMEGRL_MAGAZINE.get()).setWeight(10).setQuality(0))
						.add(LootItem.lootTableItem(MSItems.GAMEBRO_MAGAZINE.get()).setWeight(10).setQuality(0))
						.add(LootItem.lootTableItem(Items.DEAD_HORN_CORAL).setWeight(10).setQuality(0))
				)
		);
		
		lootProcessor.accept(MSLootTables.LOTUS_FLOWER_DEFAULT, LootTable.lootTable()
				.withPool(LootPool.lootPool().name("computer").setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(MSItems.COMPUTER_PARTS.get()).setWeight(10).setQuality(3)))
				.withPool(LootPool.lootPool().name("code").setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(MSItems.SBURB_CODE.get()).setWeight(10).setQuality(3)).apply(SetNbtFunction.setTag(IncompleteSburbCodeItem.giveParadoxInfo())))
		);
	}
}