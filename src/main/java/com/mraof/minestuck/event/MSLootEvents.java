package com.mraof.minestuck.event;

import com.google.common.collect.Sets;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.world.storage.loot.MSLootTables;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Set;

@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class MSLootEvents
{
	private static final Set<ResourceLocation> LOOT_INJECT = Sets.newHashSet(BuiltInLootTables.SIMPLE_DUNGEON, BuiltInLootTables.ABANDONED_MINESHAFT, BuiltInLootTables.DESERT_PYRAMID, BuiltInLootTables.JUNGLE_TEMPLE, BuiltInLootTables.WOODLAND_MANSION, BuiltInLootTables.UNDERWATER_RUIN_BIG, BuiltInLootTables.SPAWN_BONUS_CHEST);
	
	@SubscribeEvent
	public static void onLootLoad(LootTableLoadEvent event) //created using Upgrade Aquatic "LootEvents" and Mystical World "LootHandler" for reference
	{
		if(LOOT_INJECT.contains(event.getName()))
		{
			LootPool pool = LootPool.lootPool().add(LootTableReference.lootTableReference(MSLootTables.DUNGEON_LOOT_INJECT)).name("dungeon_loot_inject").build();
			event.getTable().addPool(pool);
		}
	}
}
