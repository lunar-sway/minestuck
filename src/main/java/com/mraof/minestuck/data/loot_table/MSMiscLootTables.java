package com.mraof.minestuck.data.loot_table;

import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.world.storage.loot.MSLootTables;
import net.minecraft.loot.*;
import net.minecraft.util.ResourceLocation;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class MSMiscLootTables implements Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>
{
	@Override
	public void accept(BiConsumer<ResourceLocation, LootTable.Builder> lootProcessor)
	{
		lootProcessor.accept(MSLootTables.LOTUS_FLOWER_DEFAULT, LootTable.lootTable()
				.withPool(LootPool.lootPool().name("computer").setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(MSItems.COMPUTER_PARTS).setWeight(10).setQuality(3)))
				.withPool(LootPool.lootPool().name("code").setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(MSItems.SBURB_CODE).setWeight(10).setQuality(3)))
		);
	}
}