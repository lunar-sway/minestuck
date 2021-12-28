package com.mraof.minestuck.world.storage.loot;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.world.storage.loot.conditions.ConsortLootCondition;
import com.mraof.minestuck.world.storage.loot.conditions.LandTypeLootCondition;
import com.mraof.minestuck.world.storage.loot.functions.SetBoondollarCount;
import com.mraof.minestuck.world.storage.loot.functions.SetKeyValues;
import net.minecraft.loot.*;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.loot.functions.ILootFunction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

public class MSLootTables
{
	public static final ResourceLocation OVERWORLD_DUNGEON_LOOT_INJECT = new ResourceLocation("minestuck", "chests/injections/overworld_dungeon_inject");
	public static final ResourceLocation TIER_ONE_DUNGEON_LOOT_INJECT = new ResourceLocation("minestuck", "chests/injections/tier_one_dungeon_inject");
	public static final ResourceLocation FROG_TEMPLE_CHEST = new ResourceLocation("minestuck", "chests/frog_temple");
	public static final ResourceLocation BASIC_MEDIUM_CHEST = new ResourceLocation("minestuck", "chests/medium_basic");
	public static final ResourceLocation TIER_ONE_MEDIUM_CHEST = new ResourceLocation("minestuck", "chests/medium_tier_one");
	public static final ResourceLocation CONSORT_JUNK_REWARD = new ResourceLocation("minestuck", "gameplay/consort_junk");
	public static final ResourceLocation CONSORT_FOOD_STOCK = new ResourceLocation("minestuck", "gameplay/consort_food");
	public static final ResourceLocation CONSORT_GENERAL_STOCK = new ResourceLocation("minestuck", "gameplay/consort_general");
	
	private static LootConditionType LAND_TYPE_CONDITION;
	private static LootConditionType CONSORT_CONDITION;
	private static LootFunctionType SET_BOONDOLLAR_FUNCTION;
	private static LootFunctionType SET_KEY_VALUES_FUNCTION;
	private static LootPoolEntryType LAND_TABLE_ENTRY;
	
	public static void registerLootSerializers()
	{
		LAND_TYPE_CONDITION = registerCondition("land_aspect", new LandTypeLootCondition.Serializer());
		CONSORT_CONDITION = registerCondition("consort", new ConsortLootCondition.Serializer());
		SET_BOONDOLLAR_FUNCTION = registerFunction("set_boondollar_count", new SetBoondollarCount.Serializer());
		SET_KEY_VALUES_FUNCTION = registerFunction("set_key_value", new SetKeyValues.Serializer());
		LAND_TABLE_ENTRY = registerEntry("land_table", new LandTableLootEntry.SerializerImpl());
	}
	
	public static LootConditionType landTypeConditionType()
	{
		return LAND_TYPE_CONDITION;
	}
	
	public static LootConditionType consortConditionType()
	{
		return CONSORT_CONDITION;
	}
	
	public static LootFunctionType setBoondollarFunctionType()
	{
		return SET_BOONDOLLAR_FUNCTION;
	}
	
	public static LootFunctionType setKeyValuesFunctionType()
	{
		return SET_KEY_VALUES_FUNCTION;
	}
	
	public static LootPoolEntryType landTableEntryType()
	{
		return LAND_TABLE_ENTRY;
	}
	
	//Currently does not have a forge registry, so we'll have to register it this way
	
	private static LootConditionType registerCondition(String name, ILootSerializer<? extends ILootCondition> serializer)
	{
		return Registry.register(Registry.LOOT_CONDITION_TYPE, new ResourceLocation(Minestuck.MOD_ID, name), new LootConditionType(serializer));
	}
	
	private static LootFunctionType registerFunction(String name, ILootSerializer<? extends ILootFunction> serializer)
	{
		return Registry.register(Registry.LOOT_FUNCTION_TYPE, new ResourceLocation(Minestuck.MOD_ID, name), new LootFunctionType(serializer));
	}
	
	private static LootPoolEntryType registerEntry(String name, ILootSerializer<? extends LootEntry> serializer)
	{
		return Registry.register(Registry.LOOT_POOL_ENTRY_TYPE, new ResourceLocation(Minestuck.MOD_ID, name), new LootPoolEntryType(serializer));
	}
}