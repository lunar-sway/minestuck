package com.mraof.minestuck.advancements;

import com.google.gson.JsonObject;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class EventTrigger extends SimpleCriterionTrigger<EventTrigger.Instance>
{
	@Override
	protected Instance createInstance(JsonObject json, Optional<ContextAwarePredicate> predicate, DeserializationContext context)
	{
		return new Instance(predicate);
	}
	
	public void trigger(ServerPlayer player)
	{
		trigger(player, Instance::test);
	}
	
	public static class Instance extends AbstractCriterionTriggerInstance
	{
		public static Criterion<Instance> sburbConnection()
		{
			return MSCriteriaTriggers.SBURB_CONNECTION.createCriterion(new Instance(Optional.empty()));
		}
		
		public static Criterion<Instance> cruxiteArtifact()
		{
			return MSCriteriaTriggers.CRUXITE_ARTIFACT.createCriterion(new Instance(Optional.empty()));
		}
		
		public static Criterion<Instance> returnNode()
		{
			return MSCriteriaTriggers.RETURN_NODE.createCriterion(new Instance(Optional.empty()));
		}
		
		public static Criterion<Instance> melonOverload()
		{
			return MSCriteriaTriggers.MELON_OVERLOAD.createCriterion(new Instance(Optional.empty()));
		}
		
		public static Criterion<Instance> buyOutShop()
		{
			return MSCriteriaTriggers.BUY_OUT_SHOP.createCriterion(new Instance(Optional.empty()));
		}
		
		@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
		public Instance(Optional<ContextAwarePredicate> predicate)
		{
			super(predicate);
		}
		
		public boolean test()
		{
			return true;
		}
	}
}