package com.mraof.minestuck.advancements;

import com.google.gson.JsonObject;
import com.mraof.minestuck.entity.consort.ConsortEntity;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ConsortTalkTrigger extends SimpleCriterionTrigger<ConsortTalkTrigger.Instance>
{
	@Override
	protected Instance createInstance(JsonObject json, Optional<ContextAwarePredicate> predicate, DeserializationContext context)
	{
		String message = json.has("message") ? GsonHelper.getAsString(json, "message") : null;
		return new Instance(predicate, message);
	}
	
	public void trigger(ServerPlayer player, String message, ConsortEntity consort)
	{
		trigger(player, instance -> instance.test(message));
	}
	
	public static class Instance extends AbstractCriterionTriggerInstance
	{
		@Nullable
		private final String message;
		
		@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
		public Instance(Optional<ContextAwarePredicate> predicate, @Nullable String message)
		{
			super(predicate);
			this.message = message;
		}
		
		public static Criterion<Instance> any()
		{
			return forMessage(null);
		}
		
		public static Criterion<Instance> forMessage(@Nullable String message)
		{
			return MSCriteriaTriggers.CONSORT_TALK.createCriterion(new Instance(Optional.empty(), message));
		}
		
		public boolean test(String message)
		{
			return this.message == null || this.message.equals(message);
		}
		
		@Override
		public JsonObject serializeToJson()
		{
			JsonObject json = super.serializeToJson();
			if(message != null)
				json.addProperty("message", message);
			
			return json;
		}
	}
	
}