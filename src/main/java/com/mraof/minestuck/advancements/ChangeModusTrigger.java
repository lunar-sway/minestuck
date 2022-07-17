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

public class ChangeModusTrigger extends SimpleCriterionTrigger<ChangeModusTrigger.Instance>
{
	private static final ResourceLocation ID = new ResourceLocation(Minestuck.MOD_ID, "change_modus");
	
	@Override
	public ResourceLocation getId()
	{
		return ID;
	}
	
	@Override
	protected Instance createInstance(JsonObject json, EntityPredicate.Composite predicate, DeserializationContext context)
	{
		ModusType<?> modusType = json.has("modus") ? ModusTypes.REGISTRY.getValue(new ResourceLocation(GsonHelper.getAsString(json, "modus"))) : null;
		return new Instance(predicate, modusType);
	}
	
	public void trigger(ServerPlayer player, Modus modus)
	{
		trigger(player, instance -> instance.test(modus.getType()));
	}
	
	public static class Instance extends AbstractCriterionTriggerInstance
	{
		private final ModusType<?> modusType;
		public Instance(EntityPredicate.Composite predicate, ModusType<?> modusType)
		{
			super(ID, predicate);
			this.modusType = modusType;
		}
		
		public static Instance any()
		{
			return new Instance(EntityPredicate.Composite.ANY, null);
		}
		
		public static Instance to(ModusType<?> type)
		{
			return new Instance(EntityPredicate.Composite.ANY, type);
		}
		
		public boolean test(ModusType<?> modusType)
		{
			return this.modusType == null || this.modusType.equals(modusType);
		}
		
		@Override
		public JsonObject serializeToJson(SerializationContext context)
		{
			JsonObject json = super.serializeToJson(context);
			if(modusType != null)
				json.addProperty("modus", modusType.getRegistryName().toString());
			return json;
		}
	}
}