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
import java.util.function.Predicate;
import java.util.stream.Stream;

@MethodsReturnNonnullByDefault
public record ListCondition(List<Condition> conditions, ListType type) implements Condition
{
	static final Codec<ListCondition> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Condition.CODEC.listOf().fieldOf("conditions").forGetter(ListCondition::conditions),
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
		return this.type.conditionMerger.test(this.conditions.stream().map(condition -> condition.test(entity, player)));
	}
	
	@Override
	public boolean isNpcOnly()
	{
		return this.conditions.stream().allMatch(Condition::isNpcOnly);
	}
	
	//TODO Does not make sense linguistically with a hard coded failure tooltip in Condition and a Conditions.Type other than ALL
	@Override
	public Component getFailureTooltip()
	{
		MutableComponent component = Component.empty();
		
		if(!this.conditions.isEmpty())
			component.append(this.conditions.get(0).getFailureTooltip());
		for(int i = 1; i < this.conditions.size(); i++)
			component.append("\n").append(this.conditions.get(i).getFailureTooltip());
		
		return component;
	}
	
	public enum ListType implements StringRepresentable
	{
		ALL(stream -> stream.allMatch(result -> result)),
		ANY(stream -> stream.anyMatch(result -> result)),
		ONE(stream -> stream.filter(result -> result).count() == 1),
		NONE(stream -> stream.noneMatch(result -> result));
		
		public static final Codec<ListType> CODEC = StringRepresentable.fromEnum(ListType::values);
		
		private final Predicate<Stream<Boolean>> conditionMerger;
		
		ListType(Predicate<Stream<Boolean>> conditionMerger)
		{
			this.conditionMerger = conditionMerger;
		}
		
		@Override
		public String getSerializedName()
		{
			return this.name().toLowerCase(Locale.ROOT);
		}
	}
}
