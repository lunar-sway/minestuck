package com.mraof.minestuck.item.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.mraof.minestuck.world.lands.LandTypePair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntry;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.function.Consumer;

public class LandTableLootEntry extends LootPoolEntryContainer
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	private final ResourceLocation table;
	private final String poolName;
	
	private LandTableLootEntry(ResourceLocation table, String pool, LootItemCondition[] conditions)
	{
		super(conditions);
		this.table = table;
		poolName = pool;
	}
	
	@Override
	public LootPoolEntryType getType()
	{
		return MSLootTables.LAND_TABLE_ENTRY.get();
	}
	
	@Override
	public boolean expand(LootContext context, Consumer<LootPoolEntry> lootGenCollector)
	{
		LandTypePair aspects = LandTypePair.getTypes(context.getLevel()).orElse(null);
		if(canRun(context) && aspects != null)
		{
			ResourceLocation terrainTableName = new ResourceLocation(table.getNamespace(), table.getPath() + "/terrain/" + Objects.requireNonNull(aspects.getTerrain().getRegistryName()).toString().replace(':', '/'));
			ResourceLocation titleTableName = new ResourceLocation(table.getNamespace(), table.getPath() + "/title/" + Objects.requireNonNull(aspects.getTitle().getRegistryName()).toString().replace(':', '/'));
			
			expandFrom(terrainTableName, context, lootGenCollector);
			expandFrom(titleTableName, context, lootGenCollector);
			
			return true;
		}
		
		return false;
	}
	
	private void expandFrom(ResourceLocation tableName, LootContext context, Consumer<LootPoolEntry> lootGenCollector)
	{
		LootTable lootTable = context.getLootTable(tableName);
		//noinspection ConstantConditions
		if(lootTable == null)
		{
			LOGGER.warn("Could not find loot table {}", tableName);
			return;
		}
		if(context.addVisitedTable(lootTable))
		{
			LootPool pool = lootTable.getPool(poolName);
			//noinspection ConstantConditions
			if(pool != null)
			{
				LootPoolEntryContainer[] entries = accessWithReflection(pool);
				if(entries != null)
				{
					for(LootPoolEntryContainer entry : entries)
						entry.expand(context, lootGenCollector);
				} else
				{
					lootGenCollector.accept(new LootPoolEntry()
					{
						@Override
						public int getWeight(float v)
						{
							return 30;
						}
						
						@Override
						public void createItemStack(Consumer<ItemStack> consumer, LootContext lootContext)
						{
							pool.addRandomItems(consumer, lootContext);
						}
					});
				}
			} else
			{
				LOGGER.warn("Could not find pool by name {} in loot table {}", poolName, tableName);
			}
			context.removeVisitedTable(lootTable);
		} else
		{
			LOGGER.warn("Detected infinite loop in loot tables");
		}
	}
	
	private static final Field lootEntries = getLootEntryField();
	
	private static Field getLootEntryField()
	{
		try
		{
			return ObfuscationReflectionHelper.findField(LootPool.class, "f_79023_");
		} catch(ObfuscationReflectionHelper.UnableToFindFieldException e)
		{
			LOGGER.error("Unable to get field for lootPool.lootEntries. Will be unable to fully insert loot from land type loot tables.", e);
			return null;
		}
	}
	
	private LootPoolEntryContainer[] accessWithReflection(LootPool pool)
	{
		if(lootEntries == null)
			return null;
		else
		{
			try
			{
				return (LootPoolEntryContainer[]) lootEntries.get(pool);
			} catch(Exception e)
			{
				LOGGER.error("Got exception when accessing loot entries field for loot pool. Will use simpler behaviour for this time.", e);
				return null;
			}
		}
	}
	
	public static class SerializerImpl extends Serializer<LandTableLootEntry>
	{
		@Override
		public void serializeCustom(JsonObject json, LandTableLootEntry entryIn, JsonSerializationContext context)
		{
			json.addProperty("name", entryIn.table.toString());
			json.addProperty("pool", entryIn.poolName);
		}
		
		public final LandTableLootEntry deserializeCustom(JsonObject json, JsonDeserializationContext context, LootItemCondition[] conditions)
		{
			ResourceLocation table = new ResourceLocation(GsonHelper.getAsString(json, "name"));
			String pool = GsonHelper.getAsString(json, "pool");
			return new LandTableLootEntry(table, pool, conditions);
		}
	}
	
	public static BuilderImpl builder(ResourceLocation table)
	{
		return new BuilderImpl(table);
	}
	
	public static class BuilderImpl extends Builder<BuilderImpl>
	{
		private final ResourceLocation table;
		private String pool;
		
		public BuilderImpl(ResourceLocation table)
		{
			this.table = table;
		}
		
		@Override
		protected BuilderImpl getThis()
		{
			return this;
		}
		
		public BuilderImpl setPool(String pool)
		{
			this.pool = pool;
			return this;
		}
		
		@Override
		public LootPoolEntryContainer build()
		{
			if(pool == null)
				throw new IllegalArgumentException("Pool not set");
			return new LandTableLootEntry(table, pool, this.getConditions());
		}
	}
}