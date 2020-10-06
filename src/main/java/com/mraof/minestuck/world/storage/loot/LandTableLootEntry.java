package com.mraof.minestuck.world.storage.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.mraof.minestuck.world.MSDimensions;
import com.mraof.minestuck.world.lands.LandTypePair;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.*;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class LandTableLootEntry extends LootEntry
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	private final ResourceLocation table;
	private final String poolName;
	
	private LandTableLootEntry(ResourceLocation table, String pool, ILootCondition[] conditions)
	{
		super(conditions);
		this.table = table;
		poolName = pool;
	}
	
	@Override
		public LootPoolEntryType func_230420_a_()	//getType
	{
		return MSLootTables.landTableEntryType();
	}
	
	@Override
	public boolean expand(LootContext context, Consumer<ILootGenerator> lootGenCollector)
	{
		LandTypePair aspects = MSDimensions.getAspects(context.getWorld().getServer(), context.getWorld().getDimensionKey());
		if(test(context) && aspects != null)
		{
			ResourceLocation terrainTableName = new ResourceLocation(table.getNamespace(), table.getPath() + "/terrain/" + Objects.requireNonNull(aspects.terrain.getRegistryName()).toString().replace(':', '/'));
			ResourceLocation titleTableName = new ResourceLocation(table.getNamespace(), table.getPath() + "/title/" + Objects.requireNonNull(aspects.title.getRegistryName()).toString().replace(':', '/'));
			
			expandFrom(terrainTableName, context, lootGenCollector);
			expandFrom(titleTableName, context, lootGenCollector);
			
			return true;
		}
		
		return false;
	}
	
	private void expandFrom(ResourceLocation tableName, LootContext context, Consumer<ILootGenerator> lootGenCollector)
	{
		LootTable lootTable = context.getLootTable(tableName);
		//noinspection ConstantConditions
		if(lootTable == null)
		{
			LOGGER.warn("Could not find loot table {}", tableName);
			return;
		}
		if(context.addLootTable(lootTable))
		{
			LootPool pool = lootTable.getPool(poolName);
			//noinspection ConstantConditions
			if(pool != null)
			{
				List<LootEntry> entries = accessWithReflection(pool);
				if(entries != null)
				{
					for(LootEntry entry : entries)
						entry.expand(context, lootGenCollector);
				} else
				{
					lootGenCollector.accept(new ILootGenerator()
					{
						@Override
						public int getEffectiveWeight(float v)
						{
							return 30;
						}
						
						@Override
						public void func_216188_a(Consumer<ItemStack> consumer, LootContext lootContext)
						{
							pool.generate(consumer, lootContext);
						}
					});
				}
			} else
			{
				LOGGER.warn("Could not find pool by name {} in loot table {}", poolName, tableName);
			}
			context.removeLootTable(lootTable);
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
			return ObfuscationReflectionHelper.findField(LootPool.class, "field_186453_a");
		} catch(ObfuscationReflectionHelper.UnableToFindFieldException e)
		{
			LOGGER.error("Unable to get field for lootPool.lootEntries. Will be unable to fully insert loot from land type loot tables.", e);
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	private List<LootEntry> accessWithReflection(LootPool pool)
	{
		if(lootEntries == null)
			return null;
		else
		{
			try
			{
				return (List<LootEntry>) lootEntries.get(pool);
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
		public void doSerialize(JsonObject json, LandTableLootEntry entryIn, JsonSerializationContext context)
		{
			json.addProperty("name", entryIn.table.toString());
			json.addProperty("pool", entryIn.poolName);
		}
		
		public final LandTableLootEntry deserialize(JsonObject json, JsonDeserializationContext context, ILootCondition[] conditions)
		{
			ResourceLocation table = new ResourceLocation(JSONUtils.getString(json, "name"));
			String pool = JSONUtils.getString(json, "pool");
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
		protected BuilderImpl func_212845_d_()
		{
			return this;
		}
		
		public BuilderImpl setPool(String pool)
		{
			this.pool = pool;
			return this;
		}
		
		@Override
		public LootEntry build()
		{
			if(pool == null)
				throw new IllegalArgumentException("Pool not set");
			return new LandTableLootEntry(table, pool, this.func_216079_f());
		}
	}
}