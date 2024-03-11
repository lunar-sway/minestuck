package com.mraof.minestuck.entity.dialogue;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mraof.minestuck.util.PreservingOptionalFieldCodec;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
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
	public static Codec<Conditions> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Condition.LIST_CODEC.fieldOf("condition_list").forGetter(Conditions::conditionList),
			PreservingOptionalFieldCodec.withDefault(Type.CODEC, "type", Type.ALL).forGetter(Conditions::type)
	).apply(instance, Conditions::new));
	public static final Conditions EMPTY = new Conditions(List.of(), Type.ALL);
	
	public boolean testWithContext(LivingEntity entity, ServerPlayer player)
	{
		return type.context.test(entity, player, conditionList);
	}
	
	//TODO Does not make sense linguistically with a hard coded failure tooltip in Condition and a Conditions.Type other than ALL
	public Component getFailureTooltip()
	{
		MutableComponent component = Component.empty();
		
		if(!this.conditionList.isEmpty())
			component.append(this.conditionList.get(0).getFailureTooltip());
		for(int i = 1; i < this.conditionList.size(); i++)
			component.append("\n").append(this.conditionList.get(i).getFailureTooltip());
		
		return component;
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
		
		public static Type fromInt(int ordinal) //converts int back into enum
		{
			if(0 <= ordinal && ordinal < Type.values().length)
				return Type.values()[ordinal];
			else
				throw new IllegalArgumentException("Invalid ordinal of " + ordinal + " for Conditions type!");
		}
		
		interface TriPredicate<A, B, C>
		{
			boolean test(A a, B b, C c);
		}
	}
}