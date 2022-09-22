package com.mraof.minestuck.data.loot_table;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class MinestuckLootTableProvider extends LootTableProvider
{
	public MinestuckLootTableProvider(DataGenerator generator)
	{
		super(generator);
	}
	
	@Override
	protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> getTables()
	{
		return ImmutableList.of(Pair.of(MSChestLootTables::new, LootContextParamSets.CHEST), Pair.of(MSBlockLootTables::new, LootContextParamSets.BLOCK), Pair.of(MSGiftLootTables::new, LootContextParamSets.GIFT), Pair.of(MSMiscLootTables::new, LootContextParamSets.EMPTY));
	}
	
	
	@Override
	protected void validate(Map<ResourceLocation, LootTable> lootTableMap, ValidationContext results)
	{
	
	}
	
	@Override
	public String getName()
	{
		return "Minestuck Loot Tables";
	}
}
