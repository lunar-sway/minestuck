package com.mraof.minestuck.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.util.BoondollarPriceManager;
import com.mraof.minestuck.util.BoondollarPricing;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.loot.IRandomRange;
import net.minecraft.loot.RandomValueRange;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.mraof.minestuck.block.MSBlocks.*;
import static com.mraof.minestuck.item.MSItems.*;
import static net.minecraft.item.Items.*;

public class BoondollarPricingProvider implements IDataProvider
{
	private static final Logger LOGGER = LogManager.getLogger();
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	
	private final Map<ResourceLocation, BoondollarPricing> pricings = new HashMap<>();
	private final DataGenerator dataGenerator;
	private final String modid;
	
	public BoondollarPricingProvider(DataGenerator dataGenerator, String modid)
	{
		this.dataGenerator = dataGenerator;
		this.modid = modid;
	}
	
	protected void registerPricings()
	{
		add(ONION, 9, 15);
		add(JAR_OF_BUGS, 12, 18);
		add(BUG_ON_A_STICK, 4, 6);
		add(CONE_OF_FLIES, 4, 6);
		add(GRASSHOPPER, 90, 110);
		add(SALAD, 10, 14);
		add(CHOCOLATE_BEETLE, 30, 35);
		add(DESERT_FRUIT, 2, 6);
		add(GLOWING_MUSHROOM, 10, 15);
		add(COLD_CAKE, 400, 400);
		add(BLUE_CAKE, 400, 400);
		add(HOT_CAKE, 400, 400);
		add(RED_CAKE, 400, 400);
		add(FUCHSIA_CAKE, 1500, 1500);
		add(ROCK_COOKIE, 15, 20);
		add(STRAWBERRY_CHUNK, 100, 150);
		add(TAB, 200, 200);
		add(ORANGE_FAYGO, 100, 100);
		add(CANDY_APPLE_FAYGO, 100, 100);
		add(FAYGO_COLA, 100, 100);
		add(COTTON_CANDY_FAYGO, 100, 100);
		add(CREME_SODA_FAYGO, 100, 100);
		add(GRAPE_FAYGO, 100, 100);
		add(MOON_MIST_FAYGO, 100, 100);
		add(PEACH_FAYGO, 100, 100);
		add(REDPOP_FAYGO, 100, 100);
		add(GOLD_SEEDS, 300, 400);
		add(APPLE_CAKE, 100, 140);
		add(IRRADIATED_STEAK, 70, 80);
		add(CANDY_CORN, 100, 150);
		add(BUILD_GUSHERS, 90, 120);
		add(AMBER_GUMMY_WORM, 125, 150);
		add(CAULK_PRETZEL, 125, 150);
		add(CHALK_CANDY_CIGARETTE, 125, 150);
		add(IODINE_LICORICE, 125, 150);
		add(SHALE_PEEP, 125, 150);
		add(TAR_LICORICE, 125, 150);
		add(COBALT_GUM, 150, 180);
		add(MARBLE_JAWBREAKER, 150, 180);
		add(MERCURY_SIXLETS, 150, 180);
		add(QUARTZ_JELLY_BEAN, 150, 180);
		add(SULFUR_CANDY_APPLE, 150, 180);
		add(AMETHYST_HARD_CANDY, 175, 210);
		add(GARNET_TWIX, 175, 210);
		add(RUBY_CROAK, 175, 210);
		add(RUST_GUMMY_EYE, 175, 210);
		add(DIAMOND_MINT, 200, 240);
		add(GOLD_CANDY_RIBBON, 200, 240);
		add(URANIUM_GUMMY_BEAR, 200, 240);
		add(ARTIFACT_WARHEAD, 225, 270);
		add(ZILLIUM_SKITTLES, 250, 300);
		
		add(CARVING_TOOL, 60, 90);
		add(MINI_FROG_STATUE, 200, 250);
		add(MINI_WIZARD_STATUE, 200, 250);
		add(MINI_TYPHEUS_STATUE, 10000, 12000);
		add(MSItems.STONE_SLAB, 20, 30);
		add(THRESH_DVD, 350, 400);
		add(CREW_POSTER, 350, 400);
		add(SBAHJ_POSTER, 350, 400);
		add(GAMEBRO_MAGAZINE, 450, 600);
		add(GAMEGRL_MAGAZINE, 450, 600);
		add(MUSIC_DISC_EMISSARY_OF_DANCE, 1000, 1000);
		add(MUSIC_DISC_DANCE_STAB_DANCE, 1000, 1000);
		add(MUSIC_DISC_RETRO_BATTLE, 1000, 1000);
		add(CRUMPLY_HAT, 80, 100);
		add(BATTERY, 10, 100);
		add(GRIMOIRE, 666, 666);
		add(ACE_OF_SPADES, 3000, 5000);
		add(ACE_OF_HEARTS, 3000, 5000);
		add(ACE_OF_DIAMONDS, 3000, 5000);
		add(ACE_OF_CLUBS, 3000, 5000);
		add(CLUBS_SUITARANG, 30, 50);
		add(DIAMONDS_SUITARANG, 30, 50);
		add(HEARTS_SUITARANG, 30, 50);
		add(SPADES_SUITARANG, 30, 50);
		add(POGO_CLUB, 900, 1200);
		add(METAL_BAT, 400, 500);
		add(FIRE_POKER, 1500, 2000);
		add(COPSE_CRUSHER, 1000, 1500);
		add(KATANA, 400, 500);
		add(CACTACEAE_CUTLASS, 500, 700);
		add(STEAK_SWORD, 350, 650);
		add(BEEF_SWORD, 250, 625);
		add(GLOWSTONE_DUST, 20, 40);
		add(IRON_CANE, 300, 400);
		add(GLOWING_LOG, 20, 32);
		add(GLOWING_PLANKS, 5, 8);
		add(COARSE_STONE, 5, 8);
		add(CHISELED_COARSE_STONE, 8, 15);
		add(SHADE_BRICKS, 5, 8);
		add(SMOOTH_SHADE_STONE, 5, 8);
		add(FROST_BRICKS, 5, 8);
		add(FROST_TILE, 5, 8);
		add(CAST_IRON, 5, 8);
		add(CHISELED_CAST_IRON, 8, 15);
		add(VINE_LOG, 20, 32);
		add(FLOWERY_VINE_LOG, 25, 40);
		add(FROST_LOG, 20, 32);
		add(WOODEN_CACTUS, 50, 60);
		add(SUGAR_CUBE, 200, 240);
		add(FUNGAL_SPORE, 1, 4);
		add(SPOREO, 15, 25);
		add(MOREL_MUSHROOM, 40, 80);
		add(PARADISES_PORTABELLO, 400, 600);
		add(BUG_NET, 500, 600);
		
		add(LILY_PAD, 24, 31);
		add(POTATO, 12, 15);
		add(MUSHROOM_STEW, 95, 130);
		add(CARROT, 15, 18);
		add(APPLE, 25, 30);
		add(WHEAT, 10, 15);
		add(WHEAT_SEEDS, 15, 20);
		add(BEETROOT_SOUP, 70, 90);
		add(BEETROOT, 10, 15);
		add(BEEF, 110, 130);
		add(RED_MUSHROOM, 15, 20);
		add(BROWN_MUSHROOM, 10, 15);
		add(COCOA_BEANS, 25, 35);
		add(POTION, 5, 10);
		add(SALMON, 40, 60);
		add(MILK_BUCKET, 40, 50);
		add(EGG, 30, 45);
		add(SUGAR, 50, 80);
		add(RABBIT_STEW, 130, 150);
		add(POISONOUS_POTATO, 50, 60);
		add(MELON, 70, 80);
		add(COD, 90, 100);
		add(COOKIE, 120, 150);
		add(PUMPKIN_PIE, 120, 160);
		add(GOLDEN_APPLE, 2500, 2500);
		add(LAPIS_LAZULI, 25, 35);
		add(FEATHER, 25, 35);
		add(FLINT, 5, 10);
		add(STONE_AXE, 250, 300);
		add(EMERALD, 400, 500);
		add(SLIME_BALL, 30, 40);
		add(COAL, 70, 90);
		add(CHARCOAL, 50, 70);
		add(CLAY_BALL, 5, 10);
		add(IRON_INGOT, 90, 120);
		add(QUARTZ, 60, 80);
		add(BLAZE_POWDER, 80, 100);
		add(NETHER_BRICK, 5, 10);
		add(OAK_SAPLING, 60, 90);
		add(SPRUCE_SAPLING, 60, 90);
		add(BIRCH_SAPLING, 60, 90);
		add(JUNGLE_SAPLING, 60, 90);
		add(ACACIA_SAPLING, 60, 90);
		add(DARK_OAK_SAPLING, 60, 90);
		add(DIAMOND, 800, 1200);
		add(PRISMARINE_CRYSTALS, 100, 150);
		add(PRISMARINE_SHARD, 10, 15);
		add(GOLD_INGOT, 120, 180);
		add(LEATHER, 65, 80);
		add(GOLDEN_SWORD, 900, 1200);
		add(REDSTONE, 30, 40);
		add(GUNPOWDER, 50, 65);
		add(BUCKET, 50, 65);
		add(CLOCK, 150, 200);
		add(RABBIT_FOOT, 80, 100);
		add(BOOK, 50, 65);
		add(ROTTEN_FLESH, 1, 5);
		add(OAK_LOG, 20, 32);
		add(SPRUCE_LOG, 20, 32);
		add(BIRCH_LOG, 20, 32);
		add(JUNGLE_LOG, 20, 32);
		add(POLISHED_GRANITE, 5, 10);
		add(POLISHED_ANDESITE, 5, 10);
		add(NETHER_BRICKS, 5, 10);
		add(RED_NETHER_BRICKS, 10, 15);
		add(OAK_PLANKS, 5, 8);
		add(PRISMARINE, 5, 10);
		add(PRISMARINE_BRICKS, 10, 15);
		add(CACTUS, 30, 40);
		add(SANDSTONE, 5, 10);
		add(CHISELED_SANDSTONE, 10, 15);
		add(SMOOTH_SANDSTONE, 5, 10);
		add(RED_SANDSTONE, 5, 10);
		add(CHISELED_RED_SANDSTONE, 10, 15);
		add(SMOOTH_RED_SANDSTONE, 5, 10);
		add(STONE_BRICKS, 5, 10);
		add(CHISELED_STONE_BRICKS, 10, 15);
		add(BREAD, 90, 130);
		add(CHORUS_FRUIT, 420, 420);
		add(DRAGON_BREATH, 50, 100);
		add(EGG, 50, 100);
		add(ELYTRA, 500, 1000);
		add(OBSIDIAN, 8, 20);
		add(PAPER, 5, 20);
	}
	
	protected void add(IItemProvider item, int min, int max)
	{
		//Just set the name manually if this throws an exception
		add(Ingredient.of(item), new RandomValueRange(min, max), Objects.requireNonNull(item.asItem().getRegistryName()).getPath());
	}
	
	protected void add(Ingredient ingredient, IRandomRange range, String name)
	{
		add(new BoondollarPricing(ingredient, range), new ResourceLocation(modid, name));
	}
	
	protected void add(BoondollarPricing pricing, ResourceLocation name)
	{
		pricings.put(name, pricing);
	}
	
	@Override
	public void run(DirectoryCache cache)
	{
		registerPricings();
		
		Path outputPath = dataGenerator.getOutputFolder();
		
		for(Map.Entry<ResourceLocation, BoondollarPricing> entry : pricings.entrySet())
		{
			Path pricingPath = getPath(outputPath, entry.getKey());
			try
			{
				IDataProvider.save(GSON, cache, BoondollarPriceManager.parsePrice(entry.getValue()), pricingPath);
			} catch(IOException e)
			{
				LOGGER.error("Couldn't save boondollar pricing {}", pricingPath, e);
			}
		}
	}
	
	private static Path getPath(Path outputPath, ResourceLocation id)
	{
		return outputPath.resolve("data/" + id.getNamespace() + "/minestuck/boondollar_prices/" + id.getPath() + ".json");
	}
	
	@Override
	public String getName()
	{
		return "Boondollar pricings";
	}
}