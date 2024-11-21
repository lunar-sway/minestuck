package com.mraof.minestuck.data.loot_table;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public final class MinestuckLootTableProvider
{
	public static LootTableProvider create(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup)
	{
		return new LootTableProvider(output, Set.of(), List.of(
				new LootTableProvider.SubProviderEntry((provider) -> new MSChestLootTables(), LootContextParamSets.CHEST),
				new LootTableProvider.SubProviderEntry(MSBlockLootTables::new, LootContextParamSets.BLOCK),
				new LootTableProvider.SubProviderEntry((provider) -> new MSGiftLootTables(), LootContextParamSets.GIFT),
				new LootTableProvider.SubProviderEntry((provider) -> new MSMiscLootTables(), LootContextParamSets.EMPTY)),
				lookup);
	}
}
