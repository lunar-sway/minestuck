package com.mraof.minestuck.world.storage.loot.conditions;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSyntaxException;
import com.mraof.minestuck.world.MSDimensions;
import com.mraof.minestuck.world.lands.ILandType;
import com.mraof.minestuck.world.lands.LandTypes;
import com.mraof.minestuck.world.lands.LandTypePair;
import com.mraof.minestuck.world.LandDimension;
import com.mraof.minestuck.world.lands.terrain.TerrainLandType;
import com.mraof.minestuck.world.lands.title.TitleLandType;

import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.ILootCondition;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

public class LandAspectLootCondition implements ILootCondition
{
	
	private final Set<ResourceLocation> terrainGroups;
	private final Set<ResourceLocation> titleGroups;
	private final Set<TerrainLandType> terrainAspects;
	private final Set<TitleLandType> titleAspects;
	private final boolean inverted;
	
	private LandAspectLootCondition(Set<ResourceLocation> terrainGroups, Set<ResourceLocation> titleGroups,
									Set<TerrainLandType> terrainAspects, Set<TitleLandType> titleAspects, boolean inverted)
	{
		this.terrainGroups = terrainGroups;
		this.titleGroups = titleGroups;
		this.terrainAspects = terrainAspects;
		this.titleAspects = titleAspects;
		this.inverted = inverted;
	}
	
	@Override
	public boolean test(LootContext context)
	{
		ServerWorld world = context.getWorld();
		
		if(world != null && MSDimensions.isLandDimension(world.getDimension().getType()))
		{
			LandTypePair aspects = ((LandDimension) world.dimension).landTypes;
			
			if(terrainAspects.contains(aspects.terrain) || titleAspects.contains(aspects.title)
					|| terrainGroups.contains(aspects.terrain.getGroup()) || titleGroups.contains(aspects.title.getGroup()))
					return !inverted;
		}
		
		return inverted;
	}
	
	public static class Serializer extends ILootCondition.AbstractSerializer<LandAspectLootCondition>
	{
		public Serializer()
		{
			super(new ResourceLocation("minestuck", "land_aspect"), LandAspectLootCondition.class);
		}
		
		@Override
		public void serialize(JsonObject json, LandAspectLootCondition value, JsonSerializationContext context)
		{
			serializeSet(json, "terrain_group", value.terrainGroups, ResourceLocation::toString);
			serializeSet(json, "title_group", value.titleGroups, ResourceLocation::toString);
			serializeSet(json, "terrain_aspect", value.terrainAspects, aspect -> aspect.getRegistryName().toString());
			serializeSet(json, "title_aspect", value.titleAspects, aspect -> aspect.getRegistryName().toString());
			
			json.addProperty("inverse", value.inverted);
		}
		@Override
		public LandAspectLootCondition deserialize(JsonObject json, JsonDeserializationContext context)
		{
			Set<ResourceLocation> terrainGroups = deserializeSet(json, "terrain_group", ResourceLocation::new);
			Set<ResourceLocation> titleGroups = deserializeSet(json, "title_group", ResourceLocation::new);
			Set<TerrainLandType> terrainAspects = deserializeSet(json, "terrain_aspect", s -> LandTypes.TERRAIN_REGISTRY.getValue(new ResourceLocation(s)));
			Set<TitleLandType> titleAspects = deserializeSet(json, "title_aspect", s -> LandTypes.TITLE_REGISTRY.getValue(new ResourceLocation(s)));
			boolean inverted = JSONUtils.getBoolean(json, "inverse", false);
			return new LandAspectLootCondition(terrainGroups, titleGroups, terrainAspects, titleAspects, inverted);
		}
		
		private static ILandType getAspect(String aspectName)
		{
			ILandType aspect = LandTypes.TERRAIN_REGISTRY.getValue(ResourceLocation.tryCreate(aspectName));
			if(aspect == null)
				aspect = LandTypes.TITLE_REGISTRY.getValue(ResourceLocation.tryCreate(aspectName));
			if(aspect == null)
				throw new JsonSyntaxException("\"" + aspectName + "\" is not a valid land aspect.");
			return aspect;
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
					String str = JSONUtils.getString(list.get(i), name);
					builder.add(Objects.requireNonNull(fromString.apply(str), "Unable to parse "+str+" for type "+name));
				}
				return builder.build();
			} else
			{
				String str = JSONUtils.getString(json, name);
				return ImmutableSet.of(Objects.requireNonNull(fromString.apply(str), "Unable to parse "+str+" for type "+name));
			}
		} else return Collections.emptySet();
	}
}