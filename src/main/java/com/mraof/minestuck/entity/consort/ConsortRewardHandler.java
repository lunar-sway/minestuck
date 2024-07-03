package com.mraof.minestuck.entity.consort;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ConsortRewardHandler
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	public static List<Pair<ItemStack, Integer>> generateStock(ResourceLocation lootTable, ConsortEntity consort, RandomSource rand)
	{
		LootParams.Builder contextBuilder = new LootParams.Builder((ServerLevel) consort.level())
				.withParameter(LootContextParams.THIS_ENTITY, consort).withParameter(LootContextParams.ORIGIN, consort.position());
		List<ItemStack> itemStacks = Objects.requireNonNull(consort.getServer()).getLootData()
				.getLootTable(lootTable).getRandomItems(contextBuilder.create(LootContextParamSets.GIFT));
		List<Pair<ItemStack, Integer>> itemPriceList = new ArrayList<>();
		stackLoop:
		for (ItemStack stack : itemStacks)
		{
			for (Pair<ItemStack, Integer> pair : itemPriceList)
			{
				if (ItemStack.isSameItemSameTags(pair.getKey(), stack))
				{
					pair.getKey().grow(stack.getCount());
					continue stackLoop;
				}
			}
			
			Optional<Integer> price = BoondollarPrices.getInstance().findPrice(stack, rand);
			if (price.isPresent() && itemPriceList.size() < 9)
				itemPriceList.add(Pair.of(stack, price.get()));
			if (!price.isPresent())
				LOGGER.warn("Couldn't find a boondollar price for {}", stack);
		}
		return itemPriceList;
	}
}
