package com.mraof.minestuck.advancements;

import com.mojang.serialization.Codec;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ExtraCodecs;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class EventTrigger extends SimpleCriterionTrigger<EventTrigger.Instance>
{
	@Override
	public Codec<Instance> codec()
	{
		return Instance.CODEC;
	}
	
	public void trigger(ServerPlayer player)
	{
		trigger(player, Instance::test);
	}
	
	public record Instance(Optional<ContextAwarePredicate> player) implements SimpleCriterionTrigger.SimpleInstance
	{
		private static final Codec<Instance> CODEC = ExtraCodecs.strictOptionalField(EntityPredicate.ADVANCEMENT_CODEC, "player")
				.codec().xmap(Instance::new, Instance::player);
		
		public static Criterion<Instance> sburbConnection()
		{
			return MSCriteriaTriggers.SBURB_CONNECTION.get().createCriterion(new Instance(Optional.empty()));
		}
		
		public static Criterion<Instance> cruxiteArtifact()
		{
			return MSCriteriaTriggers.CRUXITE_ARTIFACT.get().createCriterion(new Instance(Optional.empty()));
		}
		
		public static Criterion<Instance> returnNode()
		{
			return MSCriteriaTriggers.RETURN_NODE.get().createCriterion(new Instance(Optional.empty()));
		}
		
		public static Criterion<Instance> melonOverload()
		{
			return MSCriteriaTriggers.MELON_OVERLOAD.get().createCriterion(new Instance(Optional.empty()));
		}
		
		public static Criterion<Instance> buyOutShop()
		{
			return MSCriteriaTriggers.BUY_OUT_SHOP.get().createCriterion(new Instance(Optional.empty()));
		}
		
		public boolean test()
		{
			return true;
		}
	}
}
