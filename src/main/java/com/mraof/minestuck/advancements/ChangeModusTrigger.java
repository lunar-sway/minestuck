package com.mraof.minestuck.advancements;

import com.google.gson.JsonObject;
import com.mraof.minestuck.inventory.captchalogue.Modus;
import com.mraof.minestuck.inventory.captchalogue.ModusType;
import com.mraof.minestuck.inventory.captchalogue.ModusTypes;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ChangeModusTrigger extends SimpleCriterionTrigger<ChangeModusTrigger.Instance>
{
	@Override
	protected Instance createInstance(JsonObject json, Optional<ContextAwarePredicate> predicate, DeserializationContext context)
	{
		ModusType<?> modusType = json.has("modus") ? ModusTypes.REGISTRY.get(new ResourceLocation(GsonHelper.getAsString(json, "modus"))) : null;
		return new Instance(predicate, modusType);
	}
	
	public void trigger(ServerPlayer player, Modus modus)
	{
		trigger(player, instance -> instance.test(modus.getType()));
	}
	
	public static class Instance extends AbstractCriterionTriggerInstance
	{
		@Nullable
		private final ModusType<?> modusType;
		
		@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
		public Instance(Optional<ContextAwarePredicate> predicate, @Nullable ModusType<?> modusType)
		{
			super(predicate);
			this.modusType = modusType;
		}
		
		public static Criterion<Instance> any()
		{
			return MSCriteriaTriggers.CHANGE_MODUS.createCriterion(new Instance(Optional.empty(), null));
		}
		
		public static Criterion<Instance> to(ModusType<?> type)
		{
			return MSCriteriaTriggers.CHANGE_MODUS.createCriterion(new Instance(Optional.empty(), type));
		}
		
		public boolean test(ModusType<?> modusType)
		{
			return this.modusType == null || this.modusType.equals(modusType);
		}
		
		@Override
		public JsonObject serializeToJson()
		{
			JsonObject json = super.serializeToJson();
			if(modusType != null)
				json.addProperty("modus", String.valueOf(ModusTypes.REGISTRY.getKey(modusType)));
			return json;
		}
	}
}