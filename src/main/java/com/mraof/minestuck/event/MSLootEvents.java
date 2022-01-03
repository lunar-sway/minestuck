package com.mraof.minestuck.event;

import com.google.common.collect.Sets;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.world.storage.loot.MSLootTables;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.TableLootEntry;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Set;

@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class MSLootEvents
{
	private static final Set<ResourceLocation> LOOT_INJECT = Sets.newHashSet(LootTables.SIMPLE_DUNGEON, LootTables.ABANDONED_MINESHAFT, LootTables.DESERT_PYRAMID, LootTables.JUNGLE_TEMPLE, LootTables.WOODLAND_MANSION, LootTables.UNDERWATER_RUIN_BIG, LootTables.SPAWN_BONUS_CHEST);
	
	@SubscribeEvent
	public static void onLootLoad(LootTableLoadEvent event) //created using Upgrade Aquatic "LootEvents" and Mystical World "LootHandler" for reference
	{
		if(LOOT_INJECT.contains(event.getName()))
		{
			LootPool pool = LootPool.lootPool().add(TableLootEntry.lootTableReference(MSLootTables.DUNGEON_LOOT_INJECT)).name("dungeon_loot_inject").build();
			event.getTable().addPool(pool);
		}
	}
}
