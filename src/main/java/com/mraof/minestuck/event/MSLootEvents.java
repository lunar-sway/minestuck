package com.mraof.minestuck.event;

import com.google.common.collect.Sets;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.util.Debug;
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
	private static ResourceLocation SQUID_TABLE = new ResourceLocation("minecraft", "entities/squid");
	
	@SubscribeEvent
	public static void onLootLoad(LootTableLoadEvent event)
	{
		Debug.debugf("event.getName = %s, DUNGEON_LOOT_INJECT", event.getName(), LOOT_INJECT);
		if(LOOT_INJECT.contains(event.getName()))
		{
			LootPool pool = LootPool.builder().addEntry(TableLootEntry.builder(new ResourceLocation(Minestuck.MOD_ID, "chests/injections/dungeon_inject")).weight(100).quality(1)).name("dungeon_loot_inject").build();
			event.getTable().addPool(pool);
			
			Debug.debugf("event.getTable() = %s", event.getTable());
		}
		
		if(event.getName().equals(SQUID_TABLE))
		{
			event.getTable().addPool(LootPool.builder().addEntry(TableLootEntry.builder(new ResourceLocation(Minestuck.MOD_ID, "entities/squid_inject"))).name("mystical_world_squid_injection").build());
		}
	}
	
	/*private static Set<ResourceLocation> tables = Sets.newHashSet(LootTables.CHESTS_SIMPLE_DUNGEON, LootTables.CHESTS_ABANDONED_MINESHAFT, LootTables.CHESTS_DESERT_PYRAMID, LootTables.CHESTS_JUNGLE_TEMPLE, LootTables.CHESTS_WOODLAND_MANSION);
	private static ResourceLocation squid_table = new ResourceLocation("minecraft", "entities/squid");
	
	public static void onLootLoadGSE(LootTableLoadEvent event) {
		if (tables.contains(event.getName())) {
			event.getTable().addPool(
					LootPool.builder().addEntry(TableLootEntry.builder(new ResourceLocation(Minestuck.MOD_ID, "chests/inject")).weight(100).quality(0)).name("mystical_world_chest_injection").build());
		}
		
		if (event.getName().equals(squid_table)) {
			event.getTable().addPool(LootPool.builder().addEntry(TableLootEntry.builder(new ResourceLocation(Minestuck.MOD_ID, "entities/squid_inject"))).name("mystical_world_squid_injection").build());
		}
	}*/
}
