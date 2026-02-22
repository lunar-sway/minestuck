package com.mraof.minestuck.item.loot;

import com.mojang.serialization.MapCodec;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.item.loot.conditions.ConsortLootCondition;
import com.mraof.minestuck.item.loot.conditions.LandTypeLootCondition;
import com.mraof.minestuck.item.loot.functions.SetBoondollarCount;
import com.mraof.minestuck.item.loot.functions.SetSburbCodeFragments;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class MSLootTables
{
	public static final ResourceKey<LootTable> BLANK_DISK_DUNGEON_LOOT_INJECT = resourceKey("chests/injections/blank_disk_dungeon_inject");
	public static final ResourceKey<LootTable> SBURB_CODE_LIBRARY_LOOT_INJECT = resourceKey("chests/injections/sburb_code_library_inject");
	public static final ResourceKey<LootTable> FROG_TEMPLE_CHEST = resourceKey("chests/frog_temple");
	public static final ResourceKey<LootTable> BASIC_MEDIUM_CHEST = resourceKey("chests/medium_basic");
	public static final ResourceKey<LootTable> MEDIUM_SUPPLY_CHEST = resourceKey("chests/medium_supply");
	public static final ResourceKey<LootTable> CONSORT_JUNK_REWARD = resourceKey("gameplay/consort_junk");
	public static final ResourceKey<LootTable> CONSORT_FOOD_STOCK = resourceKey("gameplay/consort_food");
	public static final ResourceKey<LootTable> CONSORT_GENERAL_STOCK = resourceKey("gameplay/consort_general");
	public static final ResourceKey<LootTable> KUNDLER_SUPRISES = resourceKey("gameplay/kundler_suprises");
	public static final ResourceKey<LootTable> LOTUS_FLOWER_DEFAULT = resourceKey("gameplay/lotus_flower_default");
	
	public static final DeferredRegister<LootItemConditionType> CONDITION_REGISTER = DeferredRegister.create(Registries.LOOT_CONDITION_TYPE, Minestuck.MOD_ID);
	public static final Supplier<LootItemConditionType> LAND_TYPE_CONDITION = CONDITION_REGISTER.register("land_type", () -> new LootItemConditionType(LandTypeLootCondition.CODEC));
	public static final Supplier<LootItemConditionType> CONSORT_CONDITION = CONDITION_REGISTER.register("consort", () -> new LootItemConditionType(ConsortLootCondition.CODEC));
	
	public static final DeferredRegister<LootItemFunctionType<?>> FUNCTION_REGISTER = DeferredRegister.create(Registries.LOOT_FUNCTION_TYPE, Minestuck.MOD_ID);
	public static final Supplier<LootItemFunctionType<SetSburbCodeFragments>> SET_SBURB_CODE_FRAGMENT_FUNCTION = FUNCTION_REGISTER.register("set_sburb_code_fragments", () -> new LootItemFunctionType<>(SetSburbCodeFragments.CODEC));
	public static final Supplier<LootItemFunctionType<SetBoondollarCount>> SET_BOONDOLLAR_FUNCTION = FUNCTION_REGISTER.register("set_boondollar_count", () -> new LootItemFunctionType<>(SetBoondollarCount.CODEC));
	
	public static final DeferredRegister<LootPoolEntryType> ENTRY_REGISTER = DeferredRegister.create(Registries.LOOT_POOL_ENTRY_TYPE, Minestuck.MOD_ID);
	public static final Supplier<LootPoolEntryType> LAND_TABLE_ENTRY = ENTRY_REGISTER.register("land_table", () -> new LootPoolEntryType(LandTableLootEntry.CODEC));
	
	public static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> MODIFIER_REGISTER = DeferredRegister.create(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, Minestuck.MOD_ID);
	public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<GristLootModifier>> GRIST_MODIFIER = MODIFIER_REGISTER.register("grist", () -> GristLootModifier.CODEC);
	
	private static ResourceKey<LootTable> resourceKey(String name)
	{
		return ResourceKey.create(Registries.LOOT_TABLE, Minestuck.id(name));
	}
}