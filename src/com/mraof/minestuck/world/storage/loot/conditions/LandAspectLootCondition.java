package com.mraof.minestuck.world.storage.loot.conditions;

import java.lang.reflect.Field;
import java.util.Random;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import com.mraof.minestuck.world.MinestuckDimensionHandler;
import com.mraof.minestuck.world.lands.LandAspectRegistry.AspectCombination;

import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;

public class LandAspectLootCondition implements LootCondition
{
	
	private final String landAspectName;
	private final boolean inverted;
	
	public LandAspectLootCondition(String landAspect, boolean inverted)
	{
		this.landAspectName = landAspect;
		this.inverted = inverted;
	}
	
	@Override
	public boolean testCondition(Random rand, LootContext context)
	{
		WorldServer world = null;
		try	//TODO Wait for forge to add a proper getter
		{
			Field[] fields = context.getClass().getDeclaredFields();
			for(Field field : fields)
				if(field.getType().equals(WorldServer.class))
				{
					field.setAccessible(true);
					world = (WorldServer) field.get(context);
				}
		} catch(Exception e)
		{}
		
		if(world != null && MinestuckDimensionHandler.isLandDimension(world.provider.getDimension()))
		{
			AspectCombination aspects = MinestuckDimensionHandler.getAspects(world.provider.getDimension());
			if(aspects.aspectTerrain.getPrimaryName().equals(landAspectName) || aspects.aspectTitle.getPrimaryName().equals(landAspectName))
				return !inverted;
		}
		
		return inverted;
	}
	
	public static class Serializer extends LootCondition.Serializer<LandAspectLootCondition>
	{
		public Serializer()
		{
			super(new ResourceLocation("minestuck", "land_aspect"), LandAspectLootCondition.class);
		}
		
		@Override
		public void serialize(JsonObject json, LandAspectLootCondition value, JsonSerializationContext context)
		{
			
			json.addProperty("land_aspect", value.landAspectName);
		}
		@Override
		public LandAspectLootCondition deserialize(JsonObject json, JsonDeserializationContext context)
		{
			String landAspect = JsonUtils.getString(json, "land_aspect");
			boolean inverted = JsonUtils.getBoolean(json, "inverse", false);
			return new LandAspectLootCondition(landAspect, inverted);
		}
	}
}