package com.mraof.minestuck.entity.dialogue.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;
import java.util.Locale;

@MethodsReturnNonnullByDefault
public record ListCondition(List<Condition> conditionList, ListType type) implements Condition
{
	static final Codec<ListCondition> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Condition.LIST_CODEC.fieldOf("conditions").forGetter(ListCondition::conditionList),
			ListType.CODEC.fieldOf("list_type").forGetter(ListCondition::type)
	).apply(instance, ListCondition::new));
	
	@Override
	public Codec<ListCondition> codec()
	{
		return CODEC;
	}
	
	@Override
	public boolean test(LivingEntity entity, ServerPlayer player)
	{
		return type.context.test(entity, player, conditionList);
	}
	
	//TODO Does not make sense linguistically with a hard coded failure tooltip in Condition and a Conditions.Type other than ALL
	@Override
	public Component getFailureTooltip()
	{
		MutableComponent component = Component.empty();
		
		if(!this.conditionList.isEmpty())
			component.append(this.conditionList.get(0).getFailureTooltip());
		for(int i = 1; i < this.conditionList.size(); i++)
			component.append("\n").append(this.conditionList.get(i).getFailureTooltip());
		
		return component;
	}
	
	public enum ListType implements StringRepresentable
	{
		ALL((npc, player, conditions) ->
		{
			for(Condition condition : conditions)
			{
				if(!condition.test(npc, player))
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
				if(condition.test(npc, player))
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
				if(condition.test(npc, player))
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
				if(condition.test(npc, player))
				{
					return false;
				}
			}
			
			return true;
		});
		
		public static final Codec<ListType> CODEC = StringRepresentable.fromEnum(ListType::values);
		
		private final ListType.TriPredicate<LivingEntity, ServerPlayer, List<Condition>> context;
		
		ListType(ListType.TriPredicate<LivingEntity, ServerPlayer, List<Condition>> context)
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
