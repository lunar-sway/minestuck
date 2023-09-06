package com.mraof.minestuck.advancements;

import com.google.gson.JsonObject;
import com.mraof.minestuck.Minestuck;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class EventTrigger extends SimpleCriterionTrigger<EventTrigger.Instance>
{
	public static final ResourceLocation SBURB_CONNECTION_ID = new ResourceLocation(Minestuck.MOD_ID, "sburb_connection");
	public static final ResourceLocation CRUXITE_ARTIFACT_ID = new ResourceLocation(Minestuck.MOD_ID, "cruxite_artifact");
	public static final ResourceLocation RETURN_NODE_ID = new ResourceLocation(Minestuck.MOD_ID, "return_node");
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
	protected Instance createInstance(JsonObject json, ContextAwarePredicate predicate, DeserializationContext context)
	{
		return new Instance(predicate, id);
	}
	
	public void trigger(ServerPlayer player)
	{
		trigger(player, Instance::test);
	}
	
	public static class Instance extends AbstractCriterionTriggerInstance
	{
		public static Instance sburbConnection()
		{
			return new Instance(ContextAwarePredicate.ANY, SBURB_CONNECTION_ID);
		}
		
		public static Instance cruxiteArtifact()
		{
			return new Instance(ContextAwarePredicate.ANY, CRUXITE_ARTIFACT_ID);
		}
		
		public static Instance returnNode()
		{
			return new Instance(ContextAwarePredicate.ANY, RETURN_NODE_ID);
		}
		
		public static Instance melonOverload()
		{
			return new Instance(ContextAwarePredicate.ANY, MELON_OVERLOAD_ID);
		}
		public Instance(ContextAwarePredicate predicate, ResourceLocation id)
		{
			super(id, predicate);
		}
		
		public boolean test()
		{
			return true;
		}
	}
}