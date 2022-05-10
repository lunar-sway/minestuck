package com.mraof.minestuck.advancements;

import com.google.gson.JsonObject;
import com.mraof.minestuck.Minestuck;
import net.minecraft.advancements.criterion.AbstractCriterionTrigger;
import net.minecraft.advancements.criterion.CriterionInstance;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.ConditionArrayParser;
import net.minecraft.loot.ConditionArraySerializer;
import net.minecraft.util.ResourceLocation;

import java.util.Objects;

public class PunchDesignixTrigger extends AbstractCriterionTrigger<PunchDesignixTrigger.Instance>
{
	private static final ResourceLocation ID = new ResourceLocation(Minestuck.MOD_ID, "punch_designix");
	
	@Override
	public ResourceLocation getId()
	{
		return ID;
	}
	
	@Override
	protected Instance createInstance(JsonObject json, EntityPredicate.AndPredicate predicate, ConditionArrayParser conditionsParser)
	{
		
		ItemPredicate input = ItemPredicate.fromJson(json.get("input"));
		ItemPredicate target = ItemPredicate.fromJson(json.get("target"));
		ItemPredicate output = ItemPredicate.fromJson(json.get("output"));
		return new Instance(predicate, input, target, output);
	}
	
	public void trigger(ServerPlayerEntity player, ItemStack input, ItemStack target, ItemStack result)
	{
		trigger(player, instance -> instance.test(input, target, result));
	}
	
	public static class Instance extends CriterionInstance
	{
		private final ItemPredicate input;
		private final ItemPredicate target;
		private final ItemPredicate output;
		
		public Instance(EntityPredicate.AndPredicate predicate, ItemPredicate input, ItemPredicate target, ItemPredicate output)
		{
			super(ID, predicate);
			this.input = Objects.requireNonNull(input);
			this.target = Objects.requireNonNull(target);
			this.output = Objects.requireNonNull(output);
		}
		
		public static Instance any()
		{
			return create(ItemPredicate.ANY, ItemPredicate.ANY, ItemPredicate.ANY);
		}
		
		public static Instance create(ItemPredicate input, ItemPredicate target, ItemPredicate output)
		{
			return new Instance(EntityPredicate.AndPredicate.ANY, input, target, output);
		}
		
		public boolean test(ItemStack input, ItemStack target, ItemStack output)
		{
			return this.input.matches(input) && this.target.matches(target) && this.output.matches(output);
		}
		
		@Override
		public JsonObject serializeToJson(ConditionArraySerializer conditions)
		{
			JsonObject json = super.serializeToJson(conditions);
			json.add("input", input.serializeToJson());
			json.add("target", target.serializeToJson());
			json.add("output", output.serializeToJson());
			
			return json;
		}
	}
}