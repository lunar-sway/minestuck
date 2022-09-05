package com.mraof.minestuck.item.loot.conditions;

import com.google.common.collect.ImmutableSet;
import com.google.gson.*;
import com.mraof.minestuck.item.loot.MSLootTables;
import com.mraof.minestuck.world.lands.LandTypePair;
import com.mraof.minestuck.world.lands.LandTypes;
import com.mraof.minestuck.world.lands.terrain.TerrainLandType;
import com.mraof.minestuck.world.lands.title.TitleLandType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

public class LandTypeLootCondition implements LootItemCondition
{
	
	private final Set<ResourceLocation> terrainGroups;
	private final Set<ResourceLocation> titleGroups;
	private final Set<TerrainLandType> terrainTypes;
	private final Set<TitleLandType> titleTypes;
	private final boolean inverted;
	
	private LandTypeLootCondition(Set<ResourceLocation> terrainGroups, Set<ResourceLocation> titleGroups,
								  Set<TerrainLandType> terrainTypes, Set<TitleLandType> titleTypes, boolean inverted)
	{
		this.terrainGroups = terrainGroups;
		this.titleGroups = titleGroups;
		this.terrainTypes = terrainTypes;
		this.titleTypes = titleTypes;
		this.inverted = inverted;
	}
	
	@Override
	public LootItemConditionType getType()
	{
		return MSLootTables.landTypeConditionType();
	}
	
	@Override
	public boolean test(LootContext context)
	{
		ServerLevel level = context.getLevel();
		
		LandTypePair aspects = LandTypePair.getTypes(level).orElse(null);
		
		if(aspects != null && (terrainTypes.contains(aspects.getTerrain()) || titleTypes.contains(aspects.getTitle())
				|| terrainGroups.contains(aspects.getTerrain().getGroup()) || titleGroups.contains(aspects.getTitle().getGroup())))
				return !inverted;
		
		return inverted;
	}
	
	public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<LandTypeLootCondition>
	{
		@Override
		public void serialize(JsonObject json, LandTypeLootCondition value, JsonSerializationContext context)
		{
			serializeSet(json, "terrain_group", value.terrainGroups, ResourceLocation::toString);
			serializeSet(json, "title_group", value.titleGroups, ResourceLocation::toString);
			serializeSet(json, "terrain_type", value.terrainTypes, type -> type.getRegistryName().toString());
			serializeSet(json, "title_type", value.titleTypes, type -> type.getRegistryName().toString());
			
			json.addProperty("inverse", value.inverted);
		}
		@Override
		public LandTypeLootCondition deserialize(JsonObject json, JsonDeserializationContext context)
		{
			Set<ResourceLocation> terrainGroups = deserializeSet(json, "terrain_group", ResourceLocation::new);
			Set<ResourceLocation> titleGroups = deserializeSet(json, "title_group", ResourceLocation::new);
			Set<TerrainLandType> terrainTypes = deserializeSet(json, "terrain_type", s -> LandTypes.TERRAIN_REGISTRY.getValue(new ResourceLocation(s)));
			Set<TitleLandType> titleTypes = deserializeSet(json, "title_type", s -> LandTypes.TITLE_REGISTRY.getValue(new ResourceLocation(s)));
			boolean inverted = GsonHelper.getAsBoolean(json, "inverse", false);
			return new LandTypeLootCondition(terrainGroups, titleGroups, terrainTypes, titleTypes, inverted);
		}
	}
	
	private static <T> void serializeSet(JsonObject json, String name, Set<T> set, Function<T, String> toString)
	{
		if(!set.isEmpty())
		{
			if(set.size() == 1)
				json.addProperty(name, toString.apply(set.iterator().next()));
			else
			{
				JsonArray list = new JsonArray();
				for(T entry : set)
					list.add(new JsonPrimitive(toString.apply(entry)));
				
				json.add(name, list);
			}
		}
	}
	
	private static <T> Set<T> deserializeSet(JsonObject json, String name, Function<String, T> fromString)
	{
		if(json.has(name))
		{
			if(json.get(name).isJsonArray())
			{
				JsonArray list = json.getAsJsonArray(name);
				ImmutableSet.Builder<T> builder = ImmutableSet.builder();
				for(int i = 0; i < list.size(); i++)
				{
					String str = GsonHelper.convertToString(list.get(i), name);
					builder.add(Objects.requireNonNull(fromString.apply(str), "Unable to parse "+str+" for type "+name));
				}
				return builder.build();
			} else
			{
				String str = GsonHelper.getAsString(json, name);
				return ImmutableSet.of(Objects.requireNonNull(fromString.apply(str), "Unable to parse "+str+" for type "+name));
			}
		} else return Collections.emptySet();
	}
}