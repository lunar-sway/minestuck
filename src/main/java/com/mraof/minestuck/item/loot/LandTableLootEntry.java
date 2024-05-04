package com.mraof.minestuck.item.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mraof.minestuck.world.lands.LandTypePair;
import com.mraof.minestuck.world.lands.LandTypes;
import com.mraof.minestuck.world.lands.terrain.TerrainLandType;
import com.mraof.minestuck.world.lands.title.TitleLandType;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.*;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntry;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.fml.util.ObfuscationReflectionHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class LandTableLootEntry extends LootPoolEntryContainer
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	public static final Codec<LandTableLootEntry> CODEC = RecordCodecBuilder.create(instance ->
			instance.group(
					ResourceLocation.CODEC.fieldOf("name").forGetter(entry -> entry.table),
					Codec.STRING.fieldOf("pool").forGetter(entry -> entry.poolName)
			).and(commonFields(instance).t1()).apply(instance, LandTableLootEntry::new));
	
	private final ResourceLocation table;
	private final String poolName;
	
	private LandTableLootEntry(ResourceLocation table, String pool, List<LootItemCondition> conditions)
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
			ResourceLocation terrainTableName = getTerrainTableName(aspects.getTerrain());
			ResourceLocation titleTableName = getTitleTableName(aspects.getTitle());
			
			expandFrom(terrainTableName, context, lootGenCollector);
			expandFrom(titleTableName, context, lootGenCollector);
			
			return true;
		}
		
		return false;
	}
	
	private ResourceLocation getTerrainTableName(TerrainLandType terrainType)
	{
		return new ResourceLocation(table.getNamespace(), table.getPath() + "/terrain/"
				+ Objects.requireNonNull(LandTypes.TERRAIN_REGISTRY.getKey(terrainType)).toString().replace(':', '/'));
	}
	
	private ResourceLocation getTitleTableName(TitleLandType titleType)
	{
		return new ResourceLocation(table.getNamespace(), table.getPath() + "/title/"
				+ Objects.requireNonNull(LandTypes.TITLE_REGISTRY.getKey(titleType)).toString().replace(':', '/'));
	}
	
	private void expandFrom(ResourceLocation tableName, LootContext context, Consumer<LootPoolEntry> lootGenCollector)
	{
		LootTable lootTable = context.getResolver().getLootTable(tableName);
		LootPool pool = lootTable.getPool(poolName);
		if(pool == null)
		{
			LOGGER.warn("Could not find pool by name {} in loot table {}", poolName, tableName);
			return;
		}
		
		List<LootPoolEntryContainer> entries = accessWithReflection(pool);
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
	}
	
	@Override
	public void validate(ValidationContext context)
	{
		super.validate(context);
		for(TerrainLandType type : LandTypes.TERRAIN_REGISTRY)
			validateRecursiveTable(context, getTerrainTableName(type));
		for(TitleLandType type : LandTypes.TITLE_REGISTRY)
			validateRecursiveTable(context, getTitleTableName(type));
	}
	
	private void validateRecursiveTable(ValidationContext context, ResourceLocation tableName)
	{
		LootDataId<LootTable> tableId = new LootDataId<>(LootDataType.TABLE, tableName);
		if (context.hasVisitedElement(tableId))
			context.reportProblem("Table %s is recursively called".formatted(tableName));
		else {
			super.validate(context);
			context.resolver().getElementOptional(tableId).ifPresentOrElse(
					table -> table.validate(context.enterElement("->{%s}".formatted(tableName), tableId)),
					() -> {});
		}
	}
	
	private static final Field lootEntries = getLootEntryField();
	
	@Nullable
	private static Field getLootEntryField()
	{
		try
		{
			return ObfuscationReflectionHelper.findField(LootPool.class, "entries");
		} catch(ObfuscationReflectionHelper.UnableToFindFieldException e)
		{
			LOGGER.error("Unable to get field for lootPool.lootEntries. Will be unable to fully insert loot from land type loot tables.", e);
			return null;
		}
	}
	
	@Nullable
	private List<LootPoolEntryContainer> accessWithReflection(LootPool pool)
	{
		if(lootEntries == null)
			return null;
		else
		{
			try
			{
				return (List<LootPoolEntryContainer>) lootEntries.get(pool);
			} catch(Exception e)
			{
				LOGGER.error("Got exception when accessing loot entries field for loot pool. Will use simpler behaviour for this time.", e);
				return null;
			}
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