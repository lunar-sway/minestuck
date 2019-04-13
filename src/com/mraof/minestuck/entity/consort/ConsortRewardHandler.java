package com.mraof.minestuck.entity.consort;

import com.google.common.collect.Maps;
import com.mraof.minestuck.alchemy.GristType;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.Pair;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.mraof.minestuck.block.MinestuckBlocks.*;
import static com.mraof.minestuck.item.MinestuckItems.*;
import static net.minecraft.init.Blocks.*;
import static net.minecraft.init.Items.*;

public class ConsortRewardHandler
{
	private static final Map<ItemStack, PriceVariation> prices = Maps.newHashMap();
	
	public static void registerMinestuckPrices()
	{
		
		ConsortRewardHandler.registerPrice(new ItemStack(onion), 9, 15);
		ConsortRewardHandler.registerPrice(new ItemStack(jarOfBugs), 12, 18);
		ConsortRewardHandler.registerPrice(new ItemStack(bugOnAStick), 4, 6);
		ConsortRewardHandler.registerPrice(new ItemStack(coneOfFlies), 4, 6);
		ConsortRewardHandler.registerPrice(new ItemStack(grasshopper), 90, 110);
		ConsortRewardHandler.registerPrice(new ItemStack(salad), 10, 14);
		ConsortRewardHandler.registerPrice(new ItemStack(chocolateBeetle), 30, 35);
		ConsortRewardHandler.registerPrice(new ItemStack(desertFruit), 2, 6);
		ConsortRewardHandler.registerPrice(new ItemStack(glowingMushroom), 10, 15);
		ConsortRewardHandler.registerPrice(new ItemStack(coldCake), 400, 400);
		ConsortRewardHandler.registerPrice(new ItemStack(blueCake), 400, 400);
		ConsortRewardHandler.registerPrice(new ItemStack(hotCake), 400, 400);
		ConsortRewardHandler.registerPrice(new ItemStack(redCake), 400, 400);
		ConsortRewardHandler.registerPrice(new ItemStack(fuchsiaCake), 1500, 1500);
		ConsortRewardHandler.registerPrice(new ItemStack(rockCookie), 15, 20);
		ConsortRewardHandler.registerPrice(new ItemStack(strawberryChunk), 100, 150);
		ConsortRewardHandler.registerPrice(new ItemStack(beverage, 1, 0), 200, 200);
		for(int i = 1; i <= 9; i++)
			ConsortRewardHandler.registerPrice(new ItemStack(beverage, 1, i), 100, 100);
		ConsortRewardHandler.registerPrice(new ItemStack(goldSeeds), 300, 400);
		ConsortRewardHandler.registerPrice(new ItemStack(appleCake), 100, 140);
		ConsortRewardHandler.registerPrice(new ItemStack(irradiatedSteak), 70, 80);
		ConsortRewardHandler.registerPrice(new ItemStack(candy,1 ,0), 100, 150);
		for(GristType type : GristType.values())
			if(type.equals(GristType.Build))
				ConsortRewardHandler.registerPrice(new ItemStack(candy, 1, type.getId()), 90, 120);
			else ConsortRewardHandler.registerPrice(new ItemStack(candy, 1, type.getId()), (int) ((1 - type.getRarity())*250), (int) ((1 - type.getRarity())*300));
		
		ConsortRewardHandler.registerPrice(new ItemStack(carvingTool), 60, 90);
		ConsortRewardHandler.registerPrice(new ItemStack(frogStatueReplica), 200, 250);
		ConsortRewardHandler.registerPrice(new ItemStack(stoneSlab), 20, 30);
		ConsortRewardHandler.registerPrice(new ItemStack(threshDvd), 350, 400);
		ConsortRewardHandler.registerPrice(new ItemStack(crewPoster), 350, 400);
		ConsortRewardHandler.registerPrice(new ItemStack(sbahjPoster), 350, 400);
		ConsortRewardHandler.registerPrice(new ItemStack(recordEmissaryOfDance), 1000, 1000);
		ConsortRewardHandler.registerPrice(new ItemStack(recordDanceStab), 1000, 1000);
		ConsortRewardHandler.registerPrice(new ItemStack(recordRetroBattle), 1000, 1000);
		ConsortRewardHandler.registerPrice(new ItemStack(crumplyHat), 80, 100);
		ConsortRewardHandler.registerPrice(new ItemStack(grimoire), 666, 666);
		ConsortRewardHandler.registerPrice(new ItemStack(pogoClub), 900, 1200);
		ConsortRewardHandler.registerPrice(new ItemStack(metalBat), 400, 500);
		ConsortRewardHandler.registerPrice(new ItemStack(firePoker), 1500, 2000);
		ConsortRewardHandler.registerPrice(new ItemStack(copseCrusher), 1000, 1500);
		ConsortRewardHandler.registerPrice(new ItemStack(katana), 400, 500);
		ConsortRewardHandler.registerPrice(new ItemStack(cactusCutlass), 500, 700);
		ConsortRewardHandler.registerPrice(new ItemStack(steakSword), 350, 650);
		ConsortRewardHandler.registerPrice(new ItemStack(beefSword), 250, 625);
		ConsortRewardHandler.registerPrice(new ItemStack(glowystoneDust), 20, 40);
		ConsortRewardHandler.registerPrice(new ItemStack(ironCane), 300, 400);
		ConsortRewardHandler.registerPrice(new ItemStack(glowingLog), 20, 32);
		ConsortRewardHandler.registerPrice(new ItemStack(glowingPlanks), 5, 8);
		ConsortRewardHandler.registerPrice(new ItemStack(stone, 1, 0), 5, 8);
		ConsortRewardHandler.registerPrice(new ItemStack(stone, 1, 1), 8, 15);
		ConsortRewardHandler.registerPrice(new ItemStack(stone, 1, 2), 5, 8);
		ConsortRewardHandler.registerPrice(new ItemStack(stone, 1, 3), 5, 8);
		ConsortRewardHandler.registerPrice(new ItemStack(stone, 1, 4), 5, 8);
		ConsortRewardHandler.registerPrice(new ItemStack(stone, 1, 5), 5, 8);
		ConsortRewardHandler.registerPrice(new ItemStack(stone, 1, 7), 5, 8);
		ConsortRewardHandler.registerPrice(new ItemStack(stone, 1, 8), 8, 15);
		ConsortRewardHandler.registerPrice(new ItemStack(log, 1, 0), 20, 32);
		ConsortRewardHandler.registerPrice(new ItemStack(log, 1, 1), 25, 40);
		ConsortRewardHandler.registerPrice(new ItemStack(log, 1, 2), 20, 32);
		ConsortRewardHandler.registerPrice(new ItemStack(woodenCactus), 50, 60);
		ConsortRewardHandler.registerPrice(new ItemStack(sugarCube), 200, 240);
		ConsortRewardHandler.registerPrice(new ItemStack(fungalSpore), 1, 4);
		ConsortRewardHandler.registerPrice(new ItemStack(sporeo), 15, 25);
		ConsortRewardHandler.registerPrice(new ItemStack(morelMushroom), 40, 80);
		ConsortRewardHandler.registerPrice(new ItemStack(paradisesPortabello), 400, 600);
		ConsortRewardHandler.registerPrice(new ItemStack(bugNet), 500, 600);
		
		ConsortRewardHandler.registerPrice(new ItemStack(WATERLILY), 24, 31);
		ConsortRewardHandler.registerPrice(new ItemStack(POTATO), 12, 15);
		ConsortRewardHandler.registerPrice(new ItemStack(MUSHROOM_STEW), 95, 130);
		ConsortRewardHandler.registerPrice(new ItemStack(CARROT), 15, 18);
		ConsortRewardHandler.registerPrice(new ItemStack(APPLE), 25, 30);
		ConsortRewardHandler.registerPrice(new ItemStack(Items.WHEAT), 10, 15);
		ConsortRewardHandler.registerPrice(new ItemStack(WHEAT_SEEDS), 15, 20);
		ConsortRewardHandler.registerPrice(new ItemStack(BEETROOT_SOUP), 70, 90);
		ConsortRewardHandler.registerPrice(new ItemStack(BEETROOT), 10, 15);
		ConsortRewardHandler.registerPrice(new ItemStack(BEEF), 110, 130);
		ConsortRewardHandler.registerPrice(new ItemStack(RED_MUSHROOM), 15, 20);
		ConsortRewardHandler.registerPrice(new ItemStack(BROWN_MUSHROOM), 10, 15);
		ConsortRewardHandler.registerPrice(new ItemStack(DYE, 1, 3), 25, 35);
		ConsortRewardHandler.registerPrice(new ItemStack(POTIONITEM), 5, 10);
		ConsortRewardHandler.registerPrice(new ItemStack(FISH, 1, 1), 40, 60);
		ConsortRewardHandler.registerPrice(new ItemStack(MILK_BUCKET), 40, 50);
		ConsortRewardHandler.registerPrice(new ItemStack(EGG), 30, 45);
		ConsortRewardHandler.registerPrice(new ItemStack(SUGAR), 50, 80);
		ConsortRewardHandler.registerPrice(new ItemStack(RABBIT_STEW), 130, 150);
		ConsortRewardHandler.registerPrice(new ItemStack(POISONOUS_POTATO), 50, 60);
		ConsortRewardHandler.registerPrice(new ItemStack(MELON), 70, 80);
		ConsortRewardHandler.registerPrice(new ItemStack(FISH, 1, 0), 90, 100);
		ConsortRewardHandler.registerPrice(new ItemStack(COOKIE), 120, 150);
		ConsortRewardHandler.registerPrice(new ItemStack(PUMPKIN_PIE), 120, 160);
		ConsortRewardHandler.registerPrice(new ItemStack(GOLDEN_APPLE), 2500, 2500);
		ConsortRewardHandler.registerPrice(new ItemStack(DYE, 1, 4), 25, 35);
		ConsortRewardHandler.registerPrice(new ItemStack(FEATHER), 25, 35);
		ConsortRewardHandler.registerPrice(new ItemStack(FLINT), 5, 10);
		ConsortRewardHandler.registerPrice(new ItemStack(STONE_AXE), 250, 300);
		ConsortRewardHandler.registerPrice(new ItemStack(EMERALD), 400, 500);
		ConsortRewardHandler.registerPrice(new ItemStack(SLIME_BALL), 30, 40);
		ConsortRewardHandler.registerPrice(new ItemStack(COAL, 1, 0), 70, 90);
		ConsortRewardHandler.registerPrice(new ItemStack(COAL, 1, 1), 50, 70);
		ConsortRewardHandler.registerPrice(new ItemStack(CLAY_BALL), 5, 10);
		ConsortRewardHandler.registerPrice(new ItemStack(IRON_INGOT), 90, 120);
		ConsortRewardHandler.registerPrice(new ItemStack(QUARTZ), 60, 80);
		ConsortRewardHandler.registerPrice(new ItemStack(BLAZE_POWDER), 80, 100);
		ConsortRewardHandler.registerPrice(new ItemStack(NETHERBRICK), 5, 10);
		ConsortRewardHandler.registerPrice(new ItemStack(SAPLING, 1, 0), 60, 90);
		ConsortRewardHandler.registerPrice(new ItemStack(SAPLING, 1, 1), 60, 90);
		ConsortRewardHandler.registerPrice(new ItemStack(SAPLING, 1, 2), 60, 90);
		ConsortRewardHandler.registerPrice(new ItemStack(SAPLING, 1, 3), 60, 90);
		ConsortRewardHandler.registerPrice(new ItemStack(SAPLING, 1, 4), 60, 90);
		ConsortRewardHandler.registerPrice(new ItemStack(SAPLING, 1, 5), 60, 90);
		ConsortRewardHandler.registerPrice(new ItemStack(DIAMOND), 800, 1200);
		ConsortRewardHandler.registerPrice(new ItemStack(PRISMARINE_CRYSTALS), 100, 150);
		ConsortRewardHandler.registerPrice(new ItemStack(PRISMARINE_SHARD), 10, 15);
		ConsortRewardHandler.registerPrice(new ItemStack(GOLD_INGOT), 120, 180);
		ConsortRewardHandler.registerPrice(new ItemStack(LEATHER), 65, 80);
		ConsortRewardHandler.registerPrice(new ItemStack(GOLDEN_SWORD), 900, 1200);
		ConsortRewardHandler.registerPrice(new ItemStack(REDSTONE), 30, 40);
		ConsortRewardHandler.registerPrice(new ItemStack(GUNPOWDER), 50, 65);
		ConsortRewardHandler.registerPrice(new ItemStack(BUCKET), 50, 65);
		ConsortRewardHandler.registerPrice(new ItemStack(CLOCK), 150, 200);
		ConsortRewardHandler.registerPrice(new ItemStack(RABBIT_FOOT), 80, 100);
		ConsortRewardHandler.registerPrice(new ItemStack(BOOK), 50, 65);
		ConsortRewardHandler.registerPrice(new ItemStack(ROTTEN_FLESH), 1, 5);
		ConsortRewardHandler.registerPrice(new ItemStack(LOG, 1, 0), 20, 32);
		ConsortRewardHandler.registerPrice(new ItemStack(LOG, 1, 1), 20, 32);
		ConsortRewardHandler.registerPrice(new ItemStack(LOG, 1, 2), 20, 32);
		ConsortRewardHandler.registerPrice(new ItemStack(LOG, 1, 3), 20, 32);
		ConsortRewardHandler.registerPrice(new ItemStack(STONE, 1, 2), 5, 10);
		ConsortRewardHandler.registerPrice(new ItemStack(STONE, 1, 6), 5, 10);
		ConsortRewardHandler.registerPrice(new ItemStack(NETHER_BRICK), 5, 10);
		ConsortRewardHandler.registerPrice(new ItemStack(RED_NETHER_BRICK), 10, 15);
		ConsortRewardHandler.registerPrice(new ItemStack(PLANKS, 1, 0), 5, 8);
		ConsortRewardHandler.registerPrice(new ItemStack(PRISMARINE, 1, 0), 5, 10);
		ConsortRewardHandler.registerPrice(new ItemStack(PRISMARINE, 1, 1), 10, 15);
		ConsortRewardHandler.registerPrice(new ItemStack(CACTUS), 30, 40);
		ConsortRewardHandler.registerPrice(new ItemStack(SANDSTONE, 1, 0), 5, 10);
		ConsortRewardHandler.registerPrice(new ItemStack(SANDSTONE, 1, 1), 10, 15);
		ConsortRewardHandler.registerPrice(new ItemStack(RED_SANDSTONE, 1, 2), 5, 10);
		ConsortRewardHandler.registerPrice(new ItemStack(RED_SANDSTONE, 1, 0), 5, 10);
		ConsortRewardHandler.registerPrice(new ItemStack(RED_SANDSTONE, 1, 1), 10, 15);
		ConsortRewardHandler.registerPrice(new ItemStack(RED_SANDSTONE, 1, 2), 5, 10);
		ConsortRewardHandler.registerPrice(new ItemStack(STONEBRICK, 1, 0), 5, 10);
		ConsortRewardHandler.registerPrice(new ItemStack(STONEBRICK, 1, 3), 10, 15);
		ConsortRewardHandler.registerPrice(new ItemStack(BREAD), 90, 130);
		ConsortRewardHandler.registerPrice(new ItemStack(CHORUS_FRUIT), 420, 420);
		ConsortRewardHandler.registerPrice(new ItemStack(DRAGON_BREATH), 50, 100);
		ConsortRewardHandler.registerPrice(new ItemStack(EGG), 50, 100);
		ConsortRewardHandler.registerPrice(new ItemStack(ELYTRA), 500, 1000);
		ConsortRewardHandler.registerPrice(new ItemStack(OBSIDIAN), 8, 20);
		ConsortRewardHandler.registerPrice(new ItemStack(PAPER), 5, 20);

	}
	
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
			return min + rand.nextInt(max - min + 1);
		}
	}
	
	public static List<Pair<ItemStack, Integer>> generateStock(ResourceLocation lootTable, EntityConsort consort, Random rand)
	{
		LootContext.Builder contextBuilder = new LootContext.Builder((WorldServer) consort.world).withLootedEntity(consort);
		List<ItemStack> itemStacks = consort.world.getLootTableManager().getLootTableFromLocation(lootTable).generateLootForPools(rand, contextBuilder.build());
		List<Pair<ItemStack, Integer>> itemPriceList = new ArrayList<>();
		stackLoop:
		for (ItemStack stack : itemStacks)
		{
			for (Pair<ItemStack, Integer> pair : itemPriceList)
			{
				if (ItemStack.areItemsEqual(pair.object1, stack) && ItemStack.areItemStackTagsEqual(pair.object1, stack))
				{
					pair.object1.grow(stack.getCount());
					continue stackLoop;
				}
			}
			
			int price = getPrice(stack, rand);
			if (price >= 0 && itemPriceList.size() < 9)
				itemPriceList.add(new Pair<>(stack, price));
			if (price < 0)
				Debug.warn("Couldn't find a boondollar price for " + stack);
		}
		return itemPriceList;
	}
}
