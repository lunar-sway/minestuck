package com.mraof.minestuck.advancements;

import com.google.gson.JsonObject;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.inventory.captchalogue.Modus;
import com.mraof.minestuck.inventory.captchalogue.ModusType;
import com.mraof.minestuck.inventory.captchalogue.ModusTypes;
import net.minecraft.advancements.criterion.AbstractCriterionTrigger;
import net.minecraft.advancements.criterion.CriterionInstance;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.loot.ConditionArrayParser;
import net.minecraft.loot.ConditionArraySerializer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;

public class ChangeModusTrigger extends AbstractCriterionTrigger<ChangeModusTrigger.Instance>
{
	private static final ResourceLocation ID = new ResourceLocation(Minestuck.MOD_ID, "change_modus");
	
	@Override
	public ResourceLocation getId()
	{
		return ID;
	}
	
	@Override
	protected Instance deserializeTrigger(JsonObject json, EntityPredicate.AndPredicate predicate, ConditionArrayParser conditionsParser)
	{
		ModusType<?> modusType = json.has("modus") ? ModusTypes.REGISTRY.getValue(new ResourceLocation(JSONUtils.getString(json, "modus"))) : null;
		return new Instance(predicate, modusType);
	}
	
	public void trigger(ServerPlayerEntity player, Modus modus)
	{
		triggerListeners(player, instance -> instance.test(modus.getType()));
	}
	
	public static class Instance extends CriterionInstance
	{
		private final ModusType<?> modusType;
		public Instance(EntityPredicate.AndPredicate predicate, ModusType<?> modusType)
		{
			super(ID, predicate);
			this.modusType = modusType;
		}
		
		public static Instance any()
		{
			return new Instance(EntityPredicate.AndPredicate.ANY_AND, null);
		}
		
		public static Instance to(ModusType<?> type)
		{
			return new Instance(EntityPredicate.AndPredicate.ANY_AND, type);
		}
		
		public boolean test(ModusType<?> modusType)
		{
			return this.modusType == null || this.modusType.equals(modusType);
		}
		
		@Override
		public JsonObject serialize(ConditionArraySerializer conditions)
		{
			JsonObject json = super.serialize(conditions);
			if(modusType != null)
				json.addProperty("modus", modusType.getRegistryName().toString());
			return json;
		}
	}
}