package com.mraof.minestuck.item.loot;

import com.mojang.serialization.Codec;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.item.loot.conditions.ConsortLootCondition;
import com.mraof.minestuck.item.loot.conditions.LandTypeLootCondition;
import com.mraof.minestuck.item.loot.functions.SetBoondollarCount;
import com.mraof.minestuck.item.loot.functions.SetSburbCodeFragments;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class MSLootTables
{
	public static final ResourceLocation BLANK_DISK_DUNGEON_LOOT_INJECT = new ResourceLocation("minestuck", "chests/injections/blank_disk_dungeon_inject");
	public static final ResourceLocation SBURB_CODE_LIBRARY_LOOT_INJECT = new ResourceLocation("minestuck", "chests/injections/sburb_code_library_inject");
	public static final ResourceLocation FROG_TEMPLE_CHEST = new ResourceLocation("minestuck", "chests/frog_temple");
	public static final ResourceLocation BASIC_MEDIUM_CHEST = new ResourceLocation("minestuck", "chests/medium_basic");
	public static final ResourceLocation CONSORT_JUNK_REWARD = new ResourceLocation("minestuck", "gameplay/consort_junk");
	public static final ResourceLocation CONSORT_FOOD_STOCK = new ResourceLocation("minestuck", "gameplay/consort_food");
	public static final ResourceLocation CONSORT_GENERAL_STOCK = new ResourceLocation("minestuck", "gameplay/consort_general");
	public static final ResourceLocation KUNDLER_SUPRISES = new ResourceLocation("minestuck", "gameplay/kundler_suprises");
	public static final ResourceLocation LOTUS_FLOWER_DEFAULT = new ResourceLocation("minestuck", "gameplay/lotus_flower_default");
	
	public static final DeferredRegister<LootItemConditionType> CONDITION_REGISTER = DeferredRegister.create(Registries.LOOT_CONDITION_TYPE, Minestuck.MOD_ID);
	public static final Supplier<LootItemConditionType> LAND_TYPE_CONDITION = CONDITION_REGISTER.register("land_type", () -> new LootItemConditionType(LandTypeLootCondition.CODEC));
	public static final Supplier<LootItemConditionType> CONSORT_CONDITION = CONDITION_REGISTER.register("consort", () -> new LootItemConditionType(ConsortLootCondition.CODEC));
	
	public static final DeferredRegister<LootItemFunctionType> FUNCTION_REGISTER = DeferredRegister.create(Registries.LOOT_FUNCTION_TYPE, Minestuck.MOD_ID);
	public static final Supplier<LootItemFunctionType> SET_SBURB_CODE_FRAGMENT_FUNCTION = FUNCTION_REGISTER.register("set_sburb_code_fragments", () -> new LootItemFunctionType(SetSburbCodeFragments.CODEC));
	public static final Supplier<LootItemFunctionType> SET_BOONDOLLAR_FUNCTION = FUNCTION_REGISTER.register("set_boondollar_count", () -> new LootItemFunctionType(SetBoondollarCount.CODEC));
	
	public static final DeferredRegister<LootPoolEntryType> ENTRY_REGISTER = DeferredRegister.create(Registries.LOOT_POOL_ENTRY_TYPE, Minestuck.MOD_ID);
	public static final Supplier<LootPoolEntryType> LAND_TABLE_ENTRY = ENTRY_REGISTER.register("land_table", () -> new LootPoolEntryType(LandTableLootEntry.CODEC));
	
	public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> MODIFIER_REGISTER = DeferredRegister.create(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, Minestuck.MOD_ID);
	public static final Supplier<Codec<GristLootModifier>> GRIST_MODIFIER = MODIFIER_REGISTER.register("grist", () -> GristLootModifier.CODEC);
}