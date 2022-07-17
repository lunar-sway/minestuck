package com.mraof.minestuck.advancements;

import com.google.gson.JsonObject;
import com.mraof.minestuck.Minestuck;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.Objects;

public class TreeModusRootTrigger extends SimpleCriterionTrigger<TreeModusRootTrigger.Instance>
{
	private static final ResourceLocation ID = new ResourceLocation(Minestuck.MOD_ID, "tree_modus_root");
	
	@Override
	public ResourceLocation getId()
	{
		return ID;
	}
	
	@Override
	protected Instance createInstance(JsonObject json, EntityPredicate.Composite predicate, DeserializationContext context)
	{
		MinMaxBounds.Ints count = MinMaxBounds.Ints.fromJson(json.get("count"));
		return new Instance(predicate, count);
	}
	
	public void trigger(ServerPlayer player, int count)
	{
		trigger(player, instance -> instance.test(count));
	}
	
	public static class Instance extends AbstractCriterionTriggerInstance
	{
		private final MinMaxBounds.Ints count;
		public Instance(EntityPredicate.Composite predicate, MinMaxBounds.Ints count)
		{
			super(ID, predicate);
			this.count = Objects.requireNonNull(count);
		}
		
		public static Instance count(MinMaxBounds.Ints count)
		{
			return new Instance(EntityPredicate.Composite.ANY, count);
		}
		
		public boolean test(int count)
		{
			return this.count.matches(count);
		}
		
		@Override
		public JsonObject serializeToJson(SerializationContext context)
		{
			JsonObject json = super.serializeToJson(context);
			json.add("count", count.serializeToJson());
			
			return json;
		}
	}
}