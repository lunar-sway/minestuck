package com.mraof.minestuck.event;

import com.google.common.collect.Sets;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.world.storage.loot.MSLootTables;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.TableLootEntry;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTables;
import net.minecraft.world.storage.loot.TableLootEntry;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.util.List;
import java.util.Set;

@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class MSLootEvents
{
	private static final Set<ResourceLocation> OVERWORLD_LOOT_INJECT = Sets.newHashSet(LootTables.SIMPLE_DUNGEON, LootTables.ABANDONED_MINESHAFT, LootTables.DESERT_PYRAMID, LootTables.JUNGLE_TEMPLE, LootTables.WOODLAND_MANSION, LootTables.UNDERWATER_RUIN_BIG, LootTables.SPAWN_BONUS_CHEST);
	private static final Set<ResourceLocation> TIER_ONE_LOOT_INJECT = Sets.newHashSet(MSLootTables.TIER_ONE_MEDIUM_CHEST);
	
	@SubscribeEvent
	public static void onLootLoad(LootTableLoadEvent event) //created using Upgrade Aquatic "LootEvents" and Mystical World "LootHandler" for reference
	{
		Debug.debugf("%s", event.getName());
		
		if(OVERWORLD_LOOT_INJECT.contains(event.getName()))
		{
			LootPool pool = LootPool.lootPool().add(TableLootEntry.lootTableReference(MSLootTables.OVERWORLD_DUNGEON_LOOT_INJECT)).name("overworld_dungeon_loot_inject").build();
			event.getTable().addPool(pool);
		}
		
		if(MSLootTables.TIER_ONE_MEDIUM_CHEST == event.getName())
		{
			LootPool pool = event.getTable().getPool("boondollars");
			Debug.debugf("name = %s, lootTableKeys = %s", event.getName(), event.getLootTableManager().getLootTableKeys());
			
			//addEntry(pool, ItemLootEntry.lootTableItem(UAItems.PIKE.get()).setWeight(11).when(IN_SWAMP.or(IN_SWAMP_HILLS).or(IN_RIVER)).build());
			//addEntry(pool, TableLootEntry.builder(MSLootTables.TIER_ONE_DUNGEON_LOOT_INJECT).build());
			//LootPool pool = LootPool.builder().addEntry(TableLootEntry.builder(MSLootTables.TIER_ONE_DUNGEON_LOOT_INJECT)).name("tier_one_dungeon_loot_inject").build();
			//event.getTable().addPool(pool);
		}
	}
	
	@SuppressWarnings("unchecked")
	private static void addEntry(LootPool pool, LootEntry entry)
	{
		try
		{
			List<LootEntry> lootEntries = (List<LootEntry>) ObfuscationReflectionHelper.findField(LootPool.class, "field_186453_a").get(pool);
			if(lootEntries.stream().anyMatch(e -> e == entry))
			{
				throw new RuntimeException("Attempted to add a duplicate entry to pool: " + entry);
			}
			lootEntries.add(entry);
		} catch(IllegalAccessException e)
		{
			e.printStackTrace();
		}
	}
}
