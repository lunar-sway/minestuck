package com.mraof.minestuck.advancements;

import com.google.gson.JsonObject;
import com.mraof.minestuck.Minestuck;
import net.minecraft.advancements.criterion.AbstractCriterionTrigger;
import net.minecraft.advancements.criterion.CriterionInstance;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.loot.ConditionArrayParser;
import net.minecraft.util.ResourceLocation;

public class EventTrigger extends AbstractCriterionTrigger<EventTrigger.Instance>
{
	public static final ResourceLocation SBURB_CONNECTION_ID = new ResourceLocation(Minestuck.MOD_ID, "sburb_connection");
	public static final ResourceLocation CRUXITE_ARTIFACT_ID = new ResourceLocation(Minestuck.MOD_ID, "cruxite_artifact");
	public static final ResourceLocation MELON_OVERLOAD_ID = new ResourceLocation(Minestuck.MOD_ID, "melon_overload");
	
	private final ResourceLocation id;
	
	public EventTrigger(ResourceLocation id)
	{
		this.id = id;
	}
	
	@Override
	public ResourceLocation getId()
	{
		return id;
	}
	
	@Override
	protected Instance createInstance(JsonObject json, EntityPredicate.AndPredicate predicate, ConditionArrayParser conditionsParser)
	{
		return new Instance(predicate, id);
	}
	
	public void trigger(ServerPlayerEntity player)
	{
		trigger(player, Instance::test);
	}
	
	public static class Instance extends CriterionInstance
	{
		public static Instance sburbConnection()
		{
			return new Instance(EntityPredicate.AndPredicate.ANY, SBURB_CONNECTION_ID);
		}
		
		public static Instance cruxiteArtifact()
		{
			return new Instance(EntityPredicate.AndPredicate.ANY, CRUXITE_ARTIFACT_ID);
		}
		
		public static Instance melonOverload()
		{
			return new Instance(EntityPredicate.AndPredicate.ANY, MELON_OVERLOAD_ID);
		}
		public Instance(EntityPredicate.AndPredicate predicate, ResourceLocation id)
		{
			super(id, predicate);
		}
		
		public boolean test()
		{
			return true;
		}
	}
}