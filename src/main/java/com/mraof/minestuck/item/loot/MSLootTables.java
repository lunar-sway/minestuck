package com.mraof.minestuck.item.loot;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.item.loot.conditions.ConsortLootCondition;
import com.mraof.minestuck.item.loot.conditions.LandTypeLootCondition;
import com.mraof.minestuck.item.loot.functions.SetBoondollarCount;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class MSLootTables
{
	public static final ResourceLocation DUNGEON_LOOT_INJECT = new ResourceLocation("minestuck", "chests/injections/dungeon_inject");
	public static final ResourceLocation FROG_TEMPLE_CHEST = new ResourceLocation("minestuck", "chests/frog_temple");
	public static final ResourceLocation BASIC_MEDIUM_CHEST = new ResourceLocation("minestuck", "chests/medium_basic");
	public static final ResourceLocation CONSORT_JUNK_REWARD = new ResourceLocation("minestuck", "gameplay/consort_junk");
	public static final ResourceLocation CONSORT_FOOD_STOCK = new ResourceLocation("minestuck", "gameplay/consort_food");
	public static final ResourceLocation CONSORT_GENERAL_STOCK = new ResourceLocation("minestuck", "gameplay/consort_general");
	public static final ResourceLocation KUNDLER_SUPRISES = new ResourceLocation("minestuck", "gameplay/kundler_suprises");
	
	public static final DeferredRegister<LootItemConditionType> CONDITION_REGISTER = DeferredRegister.create(Registry.LOOT_ITEM_REGISTRY, Minestuck.MOD_ID);
	public static final RegistryObject<LootItemConditionType> LAND_TYPE_CONDITION = CONDITION_REGISTER.register("land_type", () -> new LootItemConditionType(new LandTypeLootCondition.Serializer()));
	public static final RegistryObject<LootItemConditionType> CONSORT_CONDITION = CONDITION_REGISTER.register("consort", () -> new LootItemConditionType(new ConsortLootCondition.Serializer()));
	
	public static final DeferredRegister<LootItemFunctionType> FUNCTION_REGISTER = DeferredRegister.create(Registry.LOOT_FUNCTION_REGISTRY, Minestuck.MOD_ID);
	public static final RegistryObject<LootItemFunctionType> SET_BOONDOLLAR_FUNCTION = FUNCTION_REGISTER.register("set_boondollar_count", () -> new LootItemFunctionType(new SetBoondollarCount.Serializer()));
	
	public static final DeferredRegister<LootPoolEntryType> ENTRY_REGISTER = DeferredRegister.create(Registry.LOOT_ENTRY_REGISTRY, Minestuck.MOD_ID);
	public static final RegistryObject<LootPoolEntryType> LAND_TABLE_ENTRY = ENTRY_REGISTER.register("land_table", () -> new LootPoolEntryType(new LandTableLootEntry.SerializerImpl()));
}