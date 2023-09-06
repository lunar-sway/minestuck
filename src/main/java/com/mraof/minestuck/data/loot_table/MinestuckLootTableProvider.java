package com.mraof.minestuck.data.loot_table;

import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Set;

public final class MinestuckLootTableProvider
{
	public static LootTableProvider create(PackOutput output)
	{
		return new LootTableProvider(output, Set.of(), List.of(
				new LootTableProvider.SubProviderEntry(MSChestLootTables::new, LootContextParamSets.CHEST),
				new LootTableProvider.SubProviderEntry(MSBlockLootTables::new, LootContextParamSets.BLOCK),
				new LootTableProvider.SubProviderEntry(MSGiftLootTables::new, LootContextParamSets.GIFT),
				new LootTableProvider.SubProviderEntry(MSMiscLootTables::new, LootContextParamSets.EMPTY)));
	}
}
