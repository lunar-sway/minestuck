package com.mraof.minestuck.advancements;

import com.google.gson.JsonObject;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.inventory.captchalogue.Modus;
import com.mraof.minestuck.inventory.captchalogue.ModusType;
import com.mraof.minestuck.inventory.captchalogue.ModusTypes;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;

import java.util.Objects;

public class CaptchalogueTrigger extends SimpleCriterionTrigger<CaptchalogueTrigger.Instance>
{
	private static final ResourceLocation ID = new ResourceLocation(Minestuck.MOD_ID, "captchalogue");
	
	@Override
	public ResourceLocation getId()
	{
		return ID;
	}
	
	@Override
	protected Instance createInstance(JsonObject json, EntityPredicate.Composite predicate, DeserializationContext context)
	{
		ModusType<?> modus = json.has("modus") ? ModusTypes.REGISTRY.getValue(new ResourceLocation(GsonHelper.getAsString(json, "modus"))) : null;
		ItemPredicate item = json.has("item") ? ItemPredicate.fromJson(json.get("item")) : null;
		MinMaxBounds.Ints count = MinMaxBounds.Ints.fromJson(json.get("count"));
		return new Instance(predicate, modus, item, count);
	}
	
	public void trigger(ServerPlayer player, Modus modus, ItemStack item)
	{
		trigger(player, instance -> instance.test(modus.getType(), item, modus.getNonEmptyCards()));
	}
	
	public static class Instance extends AbstractCriterionTriggerInstance
	{
		private final ModusType<?> modus;
		private final ItemPredicate item;
		private final MinMaxBounds.Ints count;
		public Instance(EntityPredicate.Composite predicate, ModusType<?> modus, ItemPredicate item, MinMaxBounds.Ints count)
		{
			super(ID, predicate);
			this.modus = modus;
			this.item = Objects.requireNonNull(item);
			this.count = Objects.requireNonNull(count);
		}
		
		public boolean test(ModusType<?> modus, ItemStack item, int count)
		{
			return (this.modus == null || this.modus.equals(modus)) && this.item.matches(item) && this.count.matches(count);
		}
		
		@Override
		public JsonObject serializeToJson(SerializationContext context)
		{
			JsonObject json = super.serializeToJson(context);
			if(modus != null)
				json.addProperty("modus", modus.getRegistryName().toString());
			json.add("item", item.serializeToJson());
			json.add("count", count.serializeToJson());
			
			return json;
		}
	}
	
}