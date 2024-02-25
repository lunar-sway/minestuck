package com.mraof.minestuck.entity.dialogue;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;
import java.util.Locale;

/**
 * Encapsulates a List of Condition and a Type which determines what kind of logical operation to test all the Condition collectively with.
 */
public record Conditions(List<Condition> conditionList, Type type)
{
	public static Codec<Conditions> CODEC = RecordCodecBuilder.create(instance ->
			instance.group(Condition.LIST_CODEC.fieldOf("condition_list").forGetter(Conditions::conditionList),
							Type.CODEC.fieldOf("type").forGetter(Conditions::type))
					.apply(instance, Conditions::new));
	
	public boolean testWithContext(LivingEntity entity, ServerPlayer player)
	{
		return type.context.test(entity, player, conditionList);
	}
	
	public enum Type implements StringRepresentable
	{
		ALL((npc, player, conditions) ->
		{
			for(Condition condition : conditions)
			{
				if(!condition.testCondition(npc, player))
				{
					return false;
				}
			}
			
			return true;
		}),
		ANY((npc, player, conditions) ->
		{
			for(Condition condition : conditions)
			{
				if(condition.testCondition(npc, player))
				{
					return true;
				}
			}
			
			return false;
		}),
		ONE((npc, player, conditions) ->
		{
			boolean passedCondition = false;
			
			for(Condition condition : conditions)
			{
				if(condition.testCondition(npc, player))
				{
					if(passedCondition)
						return false;
					
					passedCondition = true;
				}
			}
			
			return passedCondition;
		}),
		NONE((npc, player, conditions) ->
		{
			for(Condition condition : conditions)
			{
				if(condition.testCondition(npc, player))
				{
					return false;
				}
			}
			
			return true;
		});
		
		public static final Codec<Type> CODEC = Codec.STRING.xmap(Type::valueOf, Type::name);
		
		private final TriPredicate<LivingEntity, ServerPlayer, List<Condition>> context;
		
		Type(TriPredicate<LivingEntity, ServerPlayer, List<Condition>> context)
		{
			this.context = context;
		}
		
		@Override
		public String getSerializedName()
		{
			return this.name().toLowerCase(Locale.ROOT);
		}
		
		interface TriPredicate<A, B, C>
		{
			boolean test(A a, B b, C c);
		}
	}
}