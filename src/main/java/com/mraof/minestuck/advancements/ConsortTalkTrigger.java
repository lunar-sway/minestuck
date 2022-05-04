package com.mraof.minestuck.advancements;

import com.google.gson.JsonObject;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.entity.consort.ConsortEntity;
import net.minecraft.advancements.criterion.AbstractCriterionTrigger;
import net.minecraft.advancements.criterion.CriterionInstance;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.loot.ConditionArrayParser;
import net.minecraft.loot.ConditionArraySerializer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;

public class ConsortTalkTrigger extends AbstractCriterionTrigger<ConsortTalkTrigger.Instance>
{
	private static final ResourceLocation ID = new ResourceLocation(Minestuck.MOD_ID, "consort_talk");
	
	@Override
	public ResourceLocation getId()
	{
		return ID;
	}
	
	@Override
	protected Instance createInstance(JsonObject json, EntityPredicate.AndPredicate predicate, ConditionArrayParser conditionsParser)
	{
		String message = json.has("message") ? JSONUtils.getAsString(json, "message") : null;
		return new Instance(predicate, message);
	}
	
	public void trigger(ServerPlayerEntity player, String message, ConsortEntity consort)
	{
		trigger(player, instance -> instance.test(message));
	}
	
	public static class Instance extends CriterionInstance
	{
		private final String message;
		public Instance(EntityPredicate.AndPredicate predicate, String message)
		{
			super(ID, predicate);
			this.message = message;
		}
		
		public static Instance any()
		{
			return forMessage(null);
		}
		
		public static Instance forMessage(String message)
		{
			return new Instance(EntityPredicate.AndPredicate.ANY, message);
		}
		
		public boolean test(String message)
		{
			return this.message == null || this.message.equals(message);
		}
		
		@Override
		public JsonObject serializeToJson(ConditionArraySerializer conditions)
		{
			JsonObject json = super.serializeToJson(conditions);
			if(message != null)
				json.addProperty("message", message);
			
			return json;
		}
	}
	
}