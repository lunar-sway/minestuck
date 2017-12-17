package com.mraof.minestuck.entity.consort;

import com.google.common.collect.Maps;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.Pair;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ConsortRewardHandler
{
	private static final Map<ItemStack, PriceVariation> prices = Maps.newHashMap();
	
	public static void registerPrice(ItemStack stack, int min, int max)	//Maybe add json support at some point too
	{
		registerPrice(stack, new PriceVariation(min, max));
	}
	
	public static void registerPrice(ItemStack stack, PriceVariation prize)
	{
		prices.put(stack, prize);
	}
	
	public static int getPrice(ItemStack stack, Random rand)
	{
		for(Map.Entry<ItemStack, PriceVariation> entry : prices.entrySet())
			if(entry.getKey().getItem() == stack.getItem() && (entry.getKey().getMetadata() == OreDictionary.WILDCARD_VALUE
					|| entry.getKey().getMetadata() == stack.getMetadata()))
				return entry.getValue().generatePrice(rand);
		return -1;
	}
	
	public static class PriceVariation
	{
		public int min, max;
		
		public PriceVariation(int min, int max)
		{
			this.min = min;
			this.max = max;
		}
		
		public int generatePrice(Random rand)
		{
			return min + rand.nextInt(max - min);
		}
	}
	
	public static List<Pair<ItemStack, Integer>> generateStock(ResourceLocation lootTable, EntityConsort consort, Random rand)
	{
		LootContext.Builder contextBuilder = new LootContext.Builder((WorldServer) consort.world).withLootedEntity(consort);
		List<ItemStack> itemStacks = consort.world.getLootTableManager().getLootTableFromLocation(lootTable).generateLootForPools(rand, contextBuilder.build());
		List<Pair<ItemStack, Integer>> itemPriceList = new ArrayList<>();
		for(ItemStack stack : itemStacks)
		{
			int price = getPrice(stack, rand);
			if(price >= 0 && itemPriceList.size() < 9)
				itemPriceList.add(new Pair<>(stack, price));
			if(price < 0)
				Debug.warn("Couldn't find a boondollar price for "+stack);
		}
		return itemPriceList;
	}
}
