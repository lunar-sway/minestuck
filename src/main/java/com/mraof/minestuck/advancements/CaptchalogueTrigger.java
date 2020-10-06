package com.mraof.minestuck.advancements;

import com.google.gson.JsonObject;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.inventory.captchalogue.Modus;
import com.mraof.minestuck.inventory.captchalogue.ModusType;
import com.mraof.minestuck.inventory.captchalogue.ModusTypes;
import net.minecraft.advancements.criterion.*;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.ConditionArrayParser;
import net.minecraft.loot.ConditionArraySerializer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;

import java.util.Objects;

public class CaptchalogueTrigger extends AbstractCriterionTrigger<CaptchalogueTrigger.Instance>
{
	private static final ResourceLocation ID = new ResourceLocation(Minestuck.MOD_ID, "captchalogue");
	
	@Override
	public ResourceLocation getId()
	{
		return ID;
	}
	
	@Override
	protected Instance deserializeTrigger(JsonObject json, EntityPredicate.AndPredicate predicate, ConditionArrayParser conditionsParser)
	{
		ModusType<?> modus = json.has("modus") ? ModusTypes.REGISTRY.getValue(new ResourceLocation(JSONUtils.getString(json, "modus"))) : null;
		ItemPredicate item = json.has("item") ? ItemPredicate.deserialize(json.get("item")) : null;
		MinMaxBounds.IntBound count = MinMaxBounds.IntBound.fromJson(json.get("count"));
		return new Instance(predicate, modus, item, count);
	}
	
	public void trigger(ServerPlayerEntity player, Modus modus, ItemStack item)
	{
		triggerListeners(player, instance -> instance.test(modus.getType(), item, modus.getNonEmptyCards()));
	}
	
	public static class Instance extends CriterionInstance
	{
		private final ModusType<?> modus;
		private final ItemPredicate item;
		private final MinMaxBounds.IntBound count;
		public Instance(EntityPredicate.AndPredicate predicate, ModusType<?> modus, ItemPredicate item, MinMaxBounds.IntBound count)
		{
			super(ID, predicate);
			this.modus = modus;
			this.item = Objects.requireNonNull(item);
			this.count = Objects.requireNonNull(count);
		}
		
		public boolean test(ModusType<?> modus, ItemStack item, int count)
		{
			return (this.modus == null || this.modus.equals(modus)) && this.item.test(item) && this.count.test(count);
		}
		
		@Override
		public JsonObject serialize(ConditionArraySerializer conditions)
		{
			JsonObject json = super.serialize(conditions);
			if(modus != null)
				json.addProperty("modus", modus.getRegistryName().toString());
			json.add("item", item.serialize());
			json.add("count", count.serialize());
			
			return json;
		}
	}
	
}