package com.mraof.minestuck.entity.consort;

import com.mraof.minestuck.util.BoondollarPriceManager;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootParameters;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.server.ServerWorld;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class ConsortRewardHandler
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	public static List<Pair<ItemStack, Integer>> generateStock(ResourceLocation lootTable, ConsortEntity consort, Random rand)
	{
		LootContext.Builder contextBuilder = new LootContext.Builder((ServerWorld) consort.level)
				.withParameter(LootParameters.THIS_ENTITY, consort).withParameter(LootParameters.ORIGIN, consort.position());
		List<ItemStack> itemStacks = Objects.requireNonNull(consort.getServer()).getLootTables()
				.get(lootTable).getRandomItems(contextBuilder.create(LootParameterSets.GIFT));
		List<Pair<ItemStack, Integer>> itemPriceList = new ArrayList<>();
		stackLoop:
		for (ItemStack stack : itemStacks)
		{
			for (Pair<ItemStack, Integer> pair : itemPriceList)
			{
				if (ItemStack.isSame(pair.getKey(), stack) && ItemStack.tagMatches(pair.getKey(), stack))
				{
					pair.getKey().grow(stack.getCount());
					continue stackLoop;
				}
			}
			
			Optional<Integer> price = BoondollarPriceManager.getInstance().findPrice(stack, rand);
			if (price.isPresent() && itemPriceList.size() < 9)
				itemPriceList.add(Pair.of(stack, price.get()));
			if (!price.isPresent())
				LOGGER.warn("Couldn't find a boondollar price for {}", stack);
		}
		return itemPriceList;
	}
}