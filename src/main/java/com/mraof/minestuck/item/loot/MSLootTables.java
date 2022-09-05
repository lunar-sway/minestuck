package com.mraof.minestuck.item.loot;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.item.loot.conditions.ConsortLootCondition;
import com.mraof.minestuck.item.loot.conditions.LandTypeLootCondition;
import com.mraof.minestuck.item.loot.functions.SetBoondollarCount;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

public class MSLootTables
{
	public static final ResourceLocation DUNGEON_LOOT_INJECT = new ResourceLocation("minestuck", "chests/injections/dungeon_inject");
	public static final ResourceLocation FROG_TEMPLE_CHEST = new ResourceLocation("minestuck", "chests/frog_temple");
	public static final ResourceLocation BASIC_MEDIUM_CHEST = new ResourceLocation("minestuck", "chests/medium_basic");
	public static final ResourceLocation CONSORT_JUNK_REWARD = new ResourceLocation("minestuck", "gameplay/consort_junk");
	public static final ResourceLocation CONSORT_FOOD_STOCK = new ResourceLocation("minestuck", "gameplay/consort_food");
	public static final ResourceLocation CONSORT_GENERAL_STOCK = new ResourceLocation("minestuck", "gameplay/consort_general");
	
	private static LootItemConditionType LAND_TYPE_CONDITION;
	private static LootItemConditionType CONSORT_CONDITION;
	private static LootItemFunctionType SET_BOONDOLLAR_FUNCTION;
	private static LootPoolEntryType LAND_TABLE_ENTRY;
	
	public static void registerLootSerializers()
	{
		LAND_TYPE_CONDITION = registerCondition("land_aspect", new LandTypeLootCondition.Serializer());
		CONSORT_CONDITION = registerCondition("consort", new ConsortLootCondition.Serializer());
		SET_BOONDOLLAR_FUNCTION = registerFunction("set_boondollar_count", new SetBoondollarCount.Serializer());
		LAND_TABLE_ENTRY = registerEntry("land_table", new LandTableLootEntry.SerializerImpl());
	}
	
	public static LootItemConditionType landTypeConditionType()
	{
		return LAND_TYPE_CONDITION;
	}
	
	public static LootItemConditionType consortConditionType()
	{
		return CONSORT_CONDITION;
	}
	
	public static LootItemFunctionType setBoondollarFunctionType()
	{
		return SET_BOONDOLLAR_FUNCTION;
	}
	
	public static LootPoolEntryType landTableEntryType()
	{
		return LAND_TABLE_ENTRY;
	}
	
	//Currently does not have a forge registry, so we'll have to register it this way
	
	private static LootItemConditionType registerCondition(String name, Serializer<? extends LootItemCondition> serializer)
	{
		return Registry.register(Registry.LOOT_CONDITION_TYPE, new ResourceLocation(Minestuck.MOD_ID, name), new LootItemConditionType(serializer));
	}
	
	private static LootItemFunctionType registerFunction(String name, Serializer<? extends LootItemFunction> serializer)
	{
		return Registry.register(Registry.LOOT_FUNCTION_TYPE, new ResourceLocation(Minestuck.MOD_ID, name), new LootItemFunctionType(serializer));
	}
	
	private static LootPoolEntryType registerEntry(String name, Serializer<? extends LootPoolEntryContainer> serializer)
	{
		return Registry.register(Registry.LOOT_POOL_ENTRY_TYPE, new ResourceLocation(Minestuck.MOD_ID, name), new LootPoolEntryType(serializer));
	}
}