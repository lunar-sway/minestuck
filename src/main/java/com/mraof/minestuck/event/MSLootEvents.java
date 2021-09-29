package com.mraof.minestuck.event;

import com.google.common.collect.Sets;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.world.storage.loot.MSLootTables;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTables;
import net.minecraft.world.storage.loot.TableLootEntry;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Set;

@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class MSLootEvents
{
	private static final Set<ResourceLocation> LOOT_INJECT = Sets.newHashSet(LootTables.CHESTS_SIMPLE_DUNGEON, LootTables.CHESTS_ABANDONED_MINESHAFT, LootTables.CHESTS_DESERT_PYRAMID, LootTables.CHESTS_JUNGLE_TEMPLE, LootTables.CHESTS_WOODLAND_MANSION, LootTables.CHESTS_UNDERWATER_RUIN_BIG, LootTables.CHESTS_SPAWN_BONUS_CHEST);
	
	@SubscribeEvent
	public static void onLootLoad(LootTableLoadEvent event) //created using Upgrade Aquatic "LootEvents" and Mystical World "LootHandler" for reference
	{
		if(LOOT_INJECT.contains(event.getName()))
		{
			LootPool pool = LootPool.builder().addEntry(TableLootEntry.builder(MSLootTables.DUNGEON_LOOT_INJECT)).name("dungeon_loot_inject").build();
			event.getTable().addPool(pool);
		}
		
		
	}
}
