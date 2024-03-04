package com.mraof.minestuck.advancements;

import com.google.gson.JsonObject;
import com.mraof.minestuck.Minestuck;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.Objects;

public class EcheladderTrigger extends SimpleCriterionTrigger<EcheladderTrigger.Instance>
{
	public static final ResourceLocation ID = new ResourceLocation(Minestuck.MOD_ID, "echeladder");
	
	@Override
	protected Instance createInstance(JsonObject json, ContextAwarePredicate predicate, DeserializationContext deserializationContext)
	{
		MinMaxBounds.Ints rung = MinMaxBounds.Ints.fromJson(json.get("rung"));
		return new EcheladderTrigger.Instance(predicate, rung);
	}
	
	public void trigger(ServerPlayer player, int rung)
	{
		trigger(player, instance -> instance.test(rung));
	}
	
	@Override
	public ResourceLocation getId()
	{
		return ID;
	}
	
	public static class Instance extends AbstractCriterionTriggerInstance
	{
		private final MinMaxBounds.Ints rung;
		
		public Instance(ContextAwarePredicate predicate, MinMaxBounds.Ints rung)
		{
			super(ID, predicate);
			this.rung = Objects.requireNonNull(rung);
		}
		
		public static Instance rung(MinMaxBounds.Ints rung)
		{
			return new Instance(ContextAwarePredicate.ANY, rung);
		}
		
		public boolean test(int count)
		{
			return this.rung.matches(count);
		}

		@Override
		public JsonObject serializeToJson(SerializationContext context)
		{
			JsonObject json = super.serializeToJson(context);
			json.add("rung", rung.serializeToJson());

			return json;
		}
	}
}
