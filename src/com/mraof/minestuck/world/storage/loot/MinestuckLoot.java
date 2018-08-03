package com.mraof.minestuck.world.storage.loot;

import com.mraof.minestuck.world.storage.loot.conditions.ConsortLootCondition;
import com.mraof.minestuck.world.storage.loot.conditions.LandAspectLootCondition;
import com.mraof.minestuck.world.storage.loot.functions.SetBoondollarCount;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.conditions.LootConditionManager;
import net.minecraft.world.storage.loot.functions.LootFunctionManager;

public class MinestuckLoot
{
	public static final ResourceLocation BASIC_MEDIUM_CHEST = new ResourceLocation("minestuck", "chests/medium_basic");
	public static final ResourceLocation CONSORT_JUNK_REWARD = new ResourceLocation("minestuck", "gameplay/consort_junk");
	public static final ResourceLocation CONSORT_FOOD_STOCK = new ResourceLocation("minestuck", "gameplay/consort_food");
	public static final ResourceLocation CONSORT_GENERAL_STOCK = new ResourceLocation("minestuck", "gameplay/consort_general");
	
	public static void registerLootClasses()
	{
		LootConditionManager.registerCondition(new LandAspectLootCondition.Serializer());
		LootConditionManager.registerCondition(new ConsortLootCondition.Serializer());
		LootFunctionManager.registerFunction(new SetBoondollarCount.Serializer());
	}
}