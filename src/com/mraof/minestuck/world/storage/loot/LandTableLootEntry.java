package com.mraof.minestuck.world.storage.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.world.MSDimensions;
import com.mraof.minestuck.world.lands.LandAspects;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.*;
import net.minecraft.world.storage.loot.conditions.ILootCondition;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

public class LandTableLootEntry extends LootEntry
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	private final ResourceLocation table;
	private final String poolName;
	
	public LandTableLootEntry(ResourceLocation table, String pool, ILootCondition[] conditions)
	{
		super(conditions);
		this.table = table;
		poolName = pool;
	}
	
	@Override
	public boolean expand(LootContext context, Consumer<ILootGenerator> lootGenCollector)
	{
		LandAspects aspects = MSDimensions.getAspects(context.getWorld().getServer(), context.getWorld().dimension.getType());
		if(test(context) && aspects != null)
		{
			ResourceLocation terrainTableName = new ResourceLocation(table.getNamespace(), table.getPath() + "/" + Objects.requireNonNull(aspects.terrain.getRegistryName()).toString().replace(':', '/'));
			ResourceLocation titleTableName = new ResourceLocation(table.getNamespace(), table.getPath() + "/" + Objects.requireNonNull(aspects.title.getRegistryName()).toString().replace(':', '/'));
			
			expandFrom(terrainTableName, context, lootGenCollector);
			expandFrom(titleTableName, context, lootGenCollector);
			
			return true;
		}
		
		return false;
	}
	
	private void expandFrom(ResourceLocation tableName, LootContext context, Consumer<ILootGenerator> lootGenCollector)
	{
		LootTable lootTable = context.getLootTableManager().getLootTableFromLocation(tableName);
		if(context.addLootTable(lootTable))
		{
			LootPool pool = lootTable.getPool(poolName);
			if(pool != null)
			{
				for(LootEntry entry : pool.lootEntries)
					entry.expand(context, lootGenCollector);
			} else
			{
				LOGGER.warn("Could not find pool by name {} in loot table {}", poolName, tableName);
			}
		} else
		{
			LOGGER.warn("Detected infinite loop in loot tables");
		}
	}
	
	@Override
	public void func_216142_a(ValidationResults p_216142_1_, Function<ResourceLocation, LootTable> p_216142_2_, Set<ResourceLocation> p_216142_3_, LootParameterSet p_216142_4_)
	{
		super.func_216142_a(p_216142_1_, p_216142_2_, p_216142_3_, p_216142_4_);
	}
	
	public static class SerializerImpl extends Serializer<LandTableLootEntry>
	{
		public SerializerImpl()
		{
			super(new ResourceLocation(Minestuck.MOD_ID, "land_table"), LandTableLootEntry.class);
		}
		
		public void serialize(JsonObject json, LandTableLootEntry entryIn, JsonSerializationContext context)
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
		
		protected BuilderImpl func_212845_d_()
		{
			return this;
		}
		
		public BuilderImpl setPool(String pool)
		{
			this.pool = pool;
			return this;
		}
		
		public LootEntry build()
		{
			if(pool == null)
				throw new IllegalArgumentException("Pool not set");
			return new LandTableLootEntry(table, pool, this.func_216079_f());
		}
	}
}