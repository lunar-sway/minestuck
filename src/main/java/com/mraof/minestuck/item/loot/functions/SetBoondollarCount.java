package com.mraof.minestuck.item.loot.functions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mraof.minestuck.item.BoondollarsItem;
import com.mraof.minestuck.item.loot.MSLootTables;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SetBoondollarCount extends LootItemConditionalFunction
{
	public static final Codec<SetBoondollarCount> CODEC = RecordCodecBuilder.create(instance ->
			commonFields(instance)
					.and(NumberProviders.CODEC.fieldOf("count").forGetter(function -> function.countRange))
					.apply(instance, SetBoondollarCount::new));
	
	private final NumberProvider countRange;
	
	public SetBoondollarCount(List<LootItemCondition> conditionsIn, NumberProvider countRangeIn)
	{
		super(conditionsIn);
		this.countRange = countRangeIn;
	}
	
	@Override
	public LootItemFunctionType getType()
	{
		return MSLootTables.SET_BOONDOLLAR_FUNCTION.get();
	}
	
	@Override
	protected ItemStack run(ItemStack stack, LootContext context)
	{
		return BoondollarsItem.setCount(stack, countRange.getInt(context));
	}
	
	public static LootItemConditionalFunction.Builder<?> builder(NumberProvider range)
	{
		return simpleBuilder((conditions) -> new SetBoondollarCount(conditions, range));
	}
}
