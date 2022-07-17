package com.mraof.minestuck.advancements;

import com.google.gson.JsonObject;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.entity.consort.ConsortEntity;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;

public class ConsortTalkTrigger extends SimpleCriterionTrigger<ConsortTalkTrigger.Instance>
{
	private static final ResourceLocation ID = new ResourceLocation(Minestuck.MOD_ID, "consort_talk");
	
	@Override
	public ResourceLocation getId()
	{
		return ID;
	}
	
	@Override
	protected Instance createInstance(JsonObject json, EntityPredicate.Composite predicate, DeserializationContext context)
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
		private final String message;
		public Instance(EntityPredicate.Composite predicate, String message)
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
			return new Instance(EntityPredicate.Composite.ANY, message);
		}
		
		public boolean test(String message)
		{
			return this.message == null || this.message.equals(message);
		}
		
		@Override
		public JsonObject serializeToJson(SerializationContext context)
		{
			JsonObject json = super.serializeToJson(context);
			if(message != null)
				json.addProperty("message", message);
			
			return json;
		}
	}
	
}