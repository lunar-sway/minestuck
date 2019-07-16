package com.mraof.minestuck.world.storage.loot.conditions;

import java.util.Random;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSyntaxException;
import com.mraof.minestuck.world.MinestuckDimensionHandler;
import com.mraof.minestuck.world.lands.ILandAspect;
import com.mraof.minestuck.world.lands.LandAspectRegistry;
import com.mraof.minestuck.world.lands.LandAspects;
import com.mraof.minestuck.world.lands.LandDimension;
import com.mraof.minestuck.world.lands.terrain.TerrainLandAspect;
import com.mraof.minestuck.world.lands.title.TitleLandAspect;

import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.ServerWorld;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.ILootCondition;

public class LandAspectLootCondition implements ILootCondition
{
	
	private final ILandAspect[] landAspectNames;
	private final boolean inverted, includeSubtypes;
	
	public LandAspectLootCondition(ILandAspect[] landAspect, boolean inverted, boolean subtypes)
	{
		this.landAspectNames = landAspect;
		this.inverted = inverted;
		this.includeSubtypes = subtypes;
	}
	
	@Override
	public boolean test(LootContext context)
	{
		ServerWorld world = context.getWorld();
		
		if(world != null && MinestuckDimensionHandler.isLandDimension(world.getDimension().getType()))
		{
			LandAspects aspects = ((LandDimension) world.dimension).landAspects;
			
			TerrainLandAspect terrain = includeSubtypes ? aspects.aspectTerrain.getPrimaryVariant() : aspects.aspectTerrain;
			TitleLandAspect title = includeSubtypes ? aspects.aspectTitle.getPrimaryVariant() : aspects.aspectTitle;
			
			for(ILandAspect aspect : landAspectNames)
				if(terrain == aspect || title == aspect)
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
			if(value.landAspectNames.length == 1)
				json.addProperty("land_aspect", value.landAspectNames[0].getPrimaryName());
			else
			{
				JsonArray list = new JsonArray();
				for(ILandAspect aspect : value.landAspectNames)
					list.add(new JsonPrimitive(aspect.getPrimaryName()));
				
				json.add("land_aspect", list);
			}
			
			json.addProperty("inverse", value.inverted);
			json.addProperty("subtypes", value.includeSubtypes);
		}
		@Override
		public LandAspectLootCondition deserialize(JsonObject json, JsonDeserializationContext context)
		{
			ILandAspect[] landAspects;
			if(json.has("land_aspect") && json.get("land_aspect").isJsonArray())
			{
				JsonArray list = json.getAsJsonArray("land_aspect");
				landAspects = new ILandAspect[list.size()];
				for(int i = 0; i < list.size(); i++)
					landAspects[i] = getAspect(JSONUtils.getString(list.get(i), "land_aspect"));
				
			} else landAspects = new ILandAspect[] {getAspect(JSONUtils.getString(json, "land_aspect"))};
			boolean inverted = JSONUtils.getBoolean(json, "inverse", false);
			boolean subtypes = JSONUtils.getBoolean(json, "subtypes", true);
			return new LandAspectLootCondition(landAspects, inverted, subtypes);
		}
		
		private static ILandAspect getAspect(String aspectName)
		{
			ILandAspect aspect = LandAspectRegistry.fromNameTerrain(aspectName, true);
			if(aspect == null)
				aspect = LandAspectRegistry.fromNameTitle(aspectName, true);
			if(aspect == null)
				throw new JsonSyntaxException("\"" + aspectName + "\" is not a valid land aspect.");
			return aspect;
		}
		
	}
}