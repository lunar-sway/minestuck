package com.mraof.minestuck.advancements;

import com.google.gson.JsonObject;
import com.mraof.minestuck.Minestuck;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class IntellibeamLaserstationTrigger extends SimpleCriterionTrigger<IntellibeamLaserstationTrigger.Instance>
{
	private static final ResourceLocation ID = new ResourceLocation(Minestuck.MOD_ID, "intellibeam_laserstation");
	
	@Override
	protected Instance createInstance(JsonObject json, ContextAwarePredicate predicate, DeserializationContext context)
	{
		ItemPredicate item = ItemPredicate.fromJson(json.get("item"));
		return new Instance(predicate, item);
	}
	
	public void trigger(ServerPlayer player, ItemStack item)
	{
		trigger(player, instance -> instance.test(item));
	}
	
	@Override
	public ResourceLocation getId()
	{
		return ID;
	}
	
	public static class Instance extends AbstractCriterionTriggerInstance
	{
		private final ItemPredicate item;
		
		public Instance(ContextAwarePredicate predicate, ItemPredicate item)
		{
			super(ID, predicate);
			this.item = item;
		}
		
		public static Instance any()
		{
			return create(ItemPredicate.ANY);
		}
		
		public static Instance create(ItemPredicate item)
		{
			return new Instance(ContextAwarePredicate.ANY, item);
		}
		
		public boolean test(ItemStack item)
		{
			return this.item.matches(item);
		}
		
		@Override
		public JsonObject serializeToJson(SerializationContext context)
		{
			JsonObject json = super.serializeToJson(context);
			json.add("item", this.item.serializeToJson());
			return json;
		}
	}
}
