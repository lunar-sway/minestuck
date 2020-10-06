package com.mraof.minestuck.advancements;

import com.google.gson.JsonObject;
import com.mraof.minestuck.Minestuck;
import net.minecraft.advancements.criterion.AbstractCriterionTrigger;
import net.minecraft.advancements.criterion.CriterionInstance;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.loot.ConditionArrayParser;
import net.minecraft.loot.ConditionArraySerializer;
import net.minecraft.util.ResourceLocation;

import java.util.Objects;

public class TreeModusRootTrigger extends AbstractCriterionTrigger<TreeModusRootTrigger.Instance>
{
	private static final ResourceLocation ID = new ResourceLocation(Minestuck.MOD_ID, "tree_modus_root");
	
	@Override
	public ResourceLocation getId()
	{
		return ID;
	}
	
	@Override
	protected Instance deserializeTrigger(JsonObject json, EntityPredicate.AndPredicate predicate, ConditionArrayParser conditionsParser)
	{
		MinMaxBounds.IntBound count = MinMaxBounds.IntBound.fromJson(json.get("count"));
		return new Instance(predicate, count);
	}
	
	public void trigger(ServerPlayerEntity player, int count)
	{
		triggerListeners(player, instance -> instance.test(count));
	}
	
	public static class Instance extends CriterionInstance
	{
		private final MinMaxBounds.IntBound count;
		public Instance(EntityPredicate.AndPredicate predicate, MinMaxBounds.IntBound count)
		{
			super(ID, predicate);
			this.count = Objects.requireNonNull(count);
		}
		
		public static Instance count(MinMaxBounds.IntBound count)
		{
			return new Instance(EntityPredicate.AndPredicate.ANY_AND, count);
		}
		
		public boolean test(int count)
		{
			return this.count.test(count);
		}
		
		@Override
		public JsonObject serialize(ConditionArraySerializer conditions)
		{
			JsonObject json = super.serialize(conditions);
			json.add("count", count.serialize());
			
			return json;
		}
	}
}