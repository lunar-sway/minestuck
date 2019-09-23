package com.mraof.minestuck.entity.consort;

import com.google.common.collect.Maps;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.Pair;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootParameterSets;
import net.minecraft.world.storage.loot.LootParameters;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.mraof.minestuck.block.MSBlocks.*;
import static com.mraof.minestuck.item.MSItems.*;
import static net.minecraft.item.Items.*;

public class ConsortRewardHandler
{
	private static final Map<ItemStack, PriceVariation> prices = Maps.newHashMap();
	
	public static void registerMinestuckPrices()
	{
		
		ConsortRewardHandler.registerPrice(new ItemStack(ONION), 9, 15);
		ConsortRewardHandler.registerPrice(new ItemStack(JAR_OF_BUGS), 12, 18);
		ConsortRewardHandler.registerPrice(new ItemStack(BUG_ON_A_STICK), 4, 6);
		ConsortRewardHandler.registerPrice(new ItemStack(CONE_OF_FLIES), 4, 6);
		ConsortRewardHandler.registerPrice(new ItemStack(GRASSHOPPER), 90, 110);
		ConsortRewardHandler.registerPrice(new ItemStack(SALAD), 10, 14);
		ConsortRewardHandler.registerPrice(new ItemStack(CHOCOLATE_BEETLE), 30, 35);
		ConsortRewardHandler.registerPrice(new ItemStack(DESERT_FRUIT), 2, 6);
		ConsortRewardHandler.registerPrice(new ItemStack(GLOWING_MUSHROOM), 10, 15);
		ConsortRewardHandler.registerPrice(new ItemStack(COLD_CAKE), 400, 400);
		ConsortRewardHandler.registerPrice(new ItemStack(BLUE_CAKE), 400, 400);
		ConsortRewardHandler.registerPrice(new ItemStack(HOT_CAKE), 400, 400);
		ConsortRewardHandler.registerPrice(new ItemStack(RED_CAKE), 400, 400);
		ConsortRewardHandler.registerPrice(new ItemStack(FUCHSIA_CAKE), 1500, 1500);
		ConsortRewardHandler.registerPrice(new ItemStack(ROCK_COOKIE), 15, 20);
		ConsortRewardHandler.registerPrice(new ItemStack(STRAWBERRY_CHUNK), 100, 150);
		ConsortRewardHandler.registerPrice(new ItemStack(TAB), 200, 200);
		ConsortRewardHandler.registerPrice(new ItemStack(FAYGO), 100, 100);
		ConsortRewardHandler.registerPrice(new ItemStack(FAYGO_CANDY_APPLE), 100, 100);
		ConsortRewardHandler.registerPrice(new ItemStack(FAYGO_COLA), 100, 100);
		ConsortRewardHandler.registerPrice(new ItemStack(FAYGO_COTTON_CANDY), 100, 100);
		ConsortRewardHandler.registerPrice(new ItemStack(FAYGO_CREME), 100, 100);
		ConsortRewardHandler.registerPrice(new ItemStack(FAYGO_GRAPE), 100, 100);
		ConsortRewardHandler.registerPrice(new ItemStack(FAYGO_MOON_MIST), 100, 100);
		ConsortRewardHandler.registerPrice(new ItemStack(FAYGO_PEACH), 100, 100);
		ConsortRewardHandler.registerPrice(new ItemStack(FAYGO_REDPOP), 100, 100);
		ConsortRewardHandler.registerPrice(new ItemStack(GOLD_SEEDS), 300, 400);
		ConsortRewardHandler.registerPrice(new ItemStack(APPLE_CAKE), 100, 140);
		ConsortRewardHandler.registerPrice(new ItemStack(IRRADIATED_STEAK), 70, 80);
		ConsortRewardHandler.registerPrice(new ItemStack(CANDY_CORN), 100, 150);
		ConsortRewardHandler.registerPrice(new ItemStack(BUILD_GUSHERS), 90, 120);
		ConsortRewardHandler.registerPrice(new ItemStack(AMBER_GUMMY_WORM), 125, 150);
		ConsortRewardHandler.registerPrice(new ItemStack(CAULK_PRETZEL), 125, 150);
		ConsortRewardHandler.registerPrice(new ItemStack(CHALK_CANDY_CIGARETTE), 125, 150);
		ConsortRewardHandler.registerPrice(new ItemStack(IODINE_LICORICE), 125, 150);
		ConsortRewardHandler.registerPrice(new ItemStack(SHALE_PEEP), 125, 150);
		ConsortRewardHandler.registerPrice(new ItemStack(TAR_LICORICE), 125, 150);
		ConsortRewardHandler.registerPrice(new ItemStack(COBALT_GUM), 150, 180);
		ConsortRewardHandler.registerPrice(new ItemStack(MARBLE_JAWBREAKER), 150, 180);
		ConsortRewardHandler.registerPrice(new ItemStack(MERCURY_SIXLETS), 150, 180);
		ConsortRewardHandler.registerPrice(new ItemStack(QUARTZ_JELLY_BEAN), 150, 180);
		ConsortRewardHandler.registerPrice(new ItemStack(SULFUR_CANDY_APPLE), 150, 180);
		ConsortRewardHandler.registerPrice(new ItemStack(AMETHYST_HARD_CANDY), 175, 210);
		ConsortRewardHandler.registerPrice(new ItemStack(GARNET_TWIX), 175, 210);
		ConsortRewardHandler.registerPrice(new ItemStack(RUBY_CROAK), 175, 210);
		ConsortRewardHandler.registerPrice(new ItemStack(RUST_GUMMY_EYE), 175, 210);
		ConsortRewardHandler.registerPrice(new ItemStack(DIAMOND_MINT), 200, 240);
		ConsortRewardHandler.registerPrice(new ItemStack(GOLD_CANDY_RIBBON), 200, 240);
		ConsortRewardHandler.registerPrice(new ItemStack(URANIUM_GUMMY_BEAR), 200, 240);
		ConsortRewardHandler.registerPrice(new ItemStack(ARTIFACT_WARHEAD), 225, 270);
		ConsortRewardHandler.registerPrice(new ItemStack(ZILLIUM_SKITTLES), 250, 300);
		
		ConsortRewardHandler.registerPrice(new ItemStack(CARVING_TOOL), 60, 90);
		ConsortRewardHandler.registerPrice(new ItemStack(MINI_FROG_STATUE), 200, 250);
		ConsortRewardHandler.registerPrice(new ItemStack(MSItems.STONE_SLAB), 20, 30);
		ConsortRewardHandler.registerPrice(new ItemStack(THRESH_DVD), 350, 400);
		ConsortRewardHandler.registerPrice(new ItemStack(CREW_POSTER), 350, 400);
		ConsortRewardHandler.registerPrice(new ItemStack(SBAHJ_POSTER), 350, 400);
		ConsortRewardHandler.registerPrice(new ItemStack(RECORD_EMISSARY_OF_DANCE), 1000, 1000);
		ConsortRewardHandler.registerPrice(new ItemStack(RECORD_DANCE_STAB), 1000, 1000);
		ConsortRewardHandler.registerPrice(new ItemStack(RECORD_RETRO_BATTLE), 1000, 1000);
		ConsortRewardHandler.registerPrice(new ItemStack(CRUMPLY_HAT), 80, 100);
		ConsortRewardHandler.registerPrice(new ItemStack(GRIMOIRE), 666, 666);
		ConsortRewardHandler.registerPrice(new ItemStack(POGO_CLUB), 900, 1200);
		ConsortRewardHandler.registerPrice(new ItemStack(METAL_BAT), 400, 500);
		ConsortRewardHandler.registerPrice(new ItemStack(FIRE_POKER), 1500, 2000);
		ConsortRewardHandler.registerPrice(new ItemStack(COPSE_CRUSHER), 1000, 1500);
		ConsortRewardHandler.registerPrice(new ItemStack(KATANA), 400, 500);
		ConsortRewardHandler.registerPrice(new ItemStack(CACTUS_CUTLASS), 500, 700);
		ConsortRewardHandler.registerPrice(new ItemStack(STEAK_SWORD), 350, 650);
		ConsortRewardHandler.registerPrice(new ItemStack(BEEF_SWORD), 250, 625);
		ConsortRewardHandler.registerPrice(new ItemStack(GLOWSTONE_DUST), 20, 40);
		ConsortRewardHandler.registerPrice(new ItemStack(IRON_CANE), 300, 400);
		ConsortRewardHandler.registerPrice(new ItemStack(GLOWING_LOG), 20, 32);
		ConsortRewardHandler.registerPrice(new ItemStack(GLOWING_PLANKS), 5, 8);
		ConsortRewardHandler.registerPrice(new ItemStack(COARSE_STONE), 5, 8);
		ConsortRewardHandler.registerPrice(new ItemStack(CHISELED_COARSE_STONE), 8, 15);
		ConsortRewardHandler.registerPrice(new ItemStack(SHADE_BRICKS), 5, 8);
		ConsortRewardHandler.registerPrice(new ItemStack(SMOOTH_SHADE_STONE), 5, 8);
		ConsortRewardHandler.registerPrice(new ItemStack(FROST_BRICKS), 5, 8);
		ConsortRewardHandler.registerPrice(new ItemStack(FROST_TILE), 5, 8);
		ConsortRewardHandler.registerPrice(new ItemStack(CAST_IRON), 5, 8);
		ConsortRewardHandler.registerPrice(new ItemStack(CHISELED_CAST_IRON), 8, 15);
		ConsortRewardHandler.registerPrice(new ItemStack(VINE_LOG), 20, 32);
		ConsortRewardHandler.registerPrice(new ItemStack(FLOWERY_VINE_LOG), 25, 40);
		ConsortRewardHandler.registerPrice(new ItemStack(FROST_LOG), 20, 32);
		ConsortRewardHandler.registerPrice(new ItemStack(WOODEN_CACTUS), 50, 60);
		ConsortRewardHandler.registerPrice(new ItemStack(SUGAR_CUBE), 200, 240);
		ConsortRewardHandler.registerPrice(new ItemStack(FUNGAL_SPORE), 1, 4);
		ConsortRewardHandler.registerPrice(new ItemStack(SPOREO), 15, 25);
		ConsortRewardHandler.registerPrice(new ItemStack(MOREL_MUSHROOM), 40, 80);
		ConsortRewardHandler.registerPrice(new ItemStack(PARADISES_PORTABELLO), 400, 600);
		ConsortRewardHandler.registerPrice(new ItemStack(BUG_NET), 500, 600);
		
		ConsortRewardHandler.registerPrice(new ItemStack(LILY_PAD), 24, 31);
		ConsortRewardHandler.registerPrice(new ItemStack(POTATO), 12, 15);
		ConsortRewardHandler.registerPrice(new ItemStack(MUSHROOM_STEW), 95, 130);
		ConsortRewardHandler.registerPrice(new ItemStack(CARROT), 15, 18);
		ConsortRewardHandler.registerPrice(new ItemStack(APPLE), 25, 30);
		ConsortRewardHandler.registerPrice(new ItemStack(WHEAT), 10, 15);
		ConsortRewardHandler.registerPrice(new ItemStack(WHEAT_SEEDS), 15, 20);
		ConsortRewardHandler.registerPrice(new ItemStack(BEETROOT_SOUP), 70, 90);
		ConsortRewardHandler.registerPrice(new ItemStack(BEETROOT), 10, 15);
		ConsortRewardHandler.registerPrice(new ItemStack(BEEF), 110, 130);
		ConsortRewardHandler.registerPrice(new ItemStack(RED_MUSHROOM), 15, 20);
		ConsortRewardHandler.registerPrice(new ItemStack(BROWN_MUSHROOM), 10, 15);
		ConsortRewardHandler.registerPrice(new ItemStack(COCOA_BEANS), 25, 35);
		ConsortRewardHandler.registerPrice(new ItemStack(POTION), 5, 10);
		ConsortRewardHandler.registerPrice(new ItemStack(SALMON), 40, 60);
		ConsortRewardHandler.registerPrice(new ItemStack(MILK_BUCKET), 40, 50);
		ConsortRewardHandler.registerPrice(new ItemStack(EGG), 30, 45);
		ConsortRewardHandler.registerPrice(new ItemStack(SUGAR), 50, 80);
		ConsortRewardHandler.registerPrice(new ItemStack(RABBIT_STEW), 130, 150);
		ConsortRewardHandler.registerPrice(new ItemStack(POISONOUS_POTATO), 50, 60);
		ConsortRewardHandler.registerPrice(new ItemStack(MELON), 70, 80);
		ConsortRewardHandler.registerPrice(new ItemStack(COD), 90, 100);
		ConsortRewardHandler.registerPrice(new ItemStack(COOKIE), 120, 150);
		ConsortRewardHandler.registerPrice(new ItemStack(PUMPKIN_PIE), 120, 160);
		ConsortRewardHandler.registerPrice(new ItemStack(GOLDEN_APPLE), 2500, 2500);
		ConsortRewardHandler.registerPrice(new ItemStack(LAPIS_LAZULI), 25, 35);
		ConsortRewardHandler.registerPrice(new ItemStack(FEATHER), 25, 35);
		ConsortRewardHandler.registerPrice(new ItemStack(FLINT), 5, 10);
		ConsortRewardHandler.registerPrice(new ItemStack(STONE_AXE), 250, 300);
		ConsortRewardHandler.registerPrice(new ItemStack(EMERALD), 400, 500);
		ConsortRewardHandler.registerPrice(new ItemStack(SLIME_BALL), 30, 40);
		ConsortRewardHandler.registerPrice(new ItemStack(COAL), 70, 90);
		ConsortRewardHandler.registerPrice(new ItemStack(CHARCOAL), 50, 70);
		ConsortRewardHandler.registerPrice(new ItemStack(CLAY_BALL), 5, 10);
		ConsortRewardHandler.registerPrice(new ItemStack(IRON_INGOT), 90, 120);
		ConsortRewardHandler.registerPrice(new ItemStack(QUARTZ), 60, 80);
		ConsortRewardHandler.registerPrice(new ItemStack(BLAZE_POWDER), 80, 100);
		ConsortRewardHandler.registerPrice(new ItemStack(NETHER_BRICK), 5, 10);
		ConsortRewardHandler.registerPrice(new ItemStack(OAK_SAPLING), 60, 90);
		ConsortRewardHandler.registerPrice(new ItemStack(SPRUCE_SAPLING), 60, 90);
		ConsortRewardHandler.registerPrice(new ItemStack(BIRCH_SAPLING), 60, 90);
		ConsortRewardHandler.registerPrice(new ItemStack(JUNGLE_SAPLING), 60, 90);
		ConsortRewardHandler.registerPrice(new ItemStack(ACACIA_SAPLING), 60, 90);
		ConsortRewardHandler.registerPrice(new ItemStack(DARK_OAK_SAPLING), 60, 90);
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
		ConsortRewardHandler.registerPrice(new ItemStack(OAK_LOG), 20, 32);
		ConsortRewardHandler.registerPrice(new ItemStack(SPRUCE_LOG), 20, 32);
		ConsortRewardHandler.registerPrice(new ItemStack(BIRCH_LOG), 20, 32);
		ConsortRewardHandler.registerPrice(new ItemStack(JUNGLE_LOG), 20, 32);
		ConsortRewardHandler.registerPrice(new ItemStack(POLISHED_GRANITE), 5, 10);
		ConsortRewardHandler.registerPrice(new ItemStack(POLISHED_ANDESITE), 5, 10);
		ConsortRewardHandler.registerPrice(new ItemStack(NETHER_BRICKS), 5, 10);
		ConsortRewardHandler.registerPrice(new ItemStack(RED_NETHER_BRICKS), 10, 15);
		ConsortRewardHandler.registerPrice(new ItemStack(OAK_PLANKS), 5, 8);
		ConsortRewardHandler.registerPrice(new ItemStack(PRISMARINE), 5, 10);
		ConsortRewardHandler.registerPrice(new ItemStack(PRISMARINE_BRICKS), 10, 15);
		ConsortRewardHandler.registerPrice(new ItemStack(CACTUS), 30, 40);
		ConsortRewardHandler.registerPrice(new ItemStack(SANDSTONE), 5, 10);
		ConsortRewardHandler.registerPrice(new ItemStack(CHISELED_SANDSTONE), 10, 15);
		ConsortRewardHandler.registerPrice(new ItemStack(SMOOTH_SANDSTONE), 5, 10);
		ConsortRewardHandler.registerPrice(new ItemStack(RED_SANDSTONE), 5, 10);
		ConsortRewardHandler.registerPrice(new ItemStack(CHISELED_RED_SANDSTONE), 10, 15);
		ConsortRewardHandler.registerPrice(new ItemStack(SMOOTH_RED_SANDSTONE), 5, 10);
		ConsortRewardHandler.registerPrice(new ItemStack(STONE_BRICKS), 5, 10);
		ConsortRewardHandler.registerPrice(new ItemStack(CHISELED_STONE_BRICKS), 10, 15);
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
			if(entry.getKey().getItem() == stack.getItem())
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
	
	public static List<Pair<ItemStack, Integer>> generateStock(ResourceLocation lootTable, ConsortEntity consort, Random rand)
	{
		LootContext.Builder contextBuilder = new LootContext.Builder((ServerWorld) consort.world).withParameter(LootParameters.THIS_ENTITY, consort);
		List<ItemStack> itemStacks = consort.getServer().getLootTableManager().getLootTableFromLocation(lootTable).generate(contextBuilder.build(LootParameterSets.GIFT));
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