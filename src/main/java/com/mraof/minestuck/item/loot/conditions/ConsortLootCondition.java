package com.mraof.minestuck.item.loot.conditions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mraof.minestuck.entity.consort.EnumConsort;
import com.mraof.minestuck.item.loot.MSLootTables;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

import java.util.List;

@MethodsReturnNonnullByDefault
public class ConsortLootCondition implements LootItemCondition
{
	public static final Codec<ConsortLootCondition> CODEC = RecordCodecBuilder.create(instance ->
			instance.group(EnumConsort.SINGLE_OR_LIST_CODEC.fieldOf("consort").forGetter(condition -> condition.consorts))
					.apply(instance, ConsortLootCondition::new));
	
	private final List<EnumConsort> consorts;
	
	public ConsortLootCondition(List<EnumConsort> consorts)
	{
		this.consorts = consorts;
	}
	
	@Override
	public LootItemConditionType getType()
	{
		return MSLootTables.CONSORT_CONDITION.get();
	}
	
	@Override
	public boolean test(LootContext context)
	{
		Entity entity = context.getParamOrNull(LootContextParams.THIS_ENTITY);
		if(entity != null)
			for(EnumConsort type : consorts)
				if(type.isConsort(entity))
					return true;
		return false;
	}
	
	public static Builder builder(EnumConsort... consorts)
	{
		return () -> new ConsortLootCondition(List.of(consorts));
	}
}
