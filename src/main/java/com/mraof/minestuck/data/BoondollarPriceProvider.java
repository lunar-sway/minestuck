package com.mraof.minestuck.data;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import com.mraof.minestuck.entity.consort.BoondollarPriceRecipe;
import com.mraof.minestuck.item.MSItems;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.ParametersAreNonnullByDefault;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import static com.mraof.minestuck.item.MSItems.*;
import static net.minecraft.world.item.Items.*;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class BoondollarPriceProvider implements DataProvider
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	private final Map<ResourceLocation, BoondollarPriceRecipe> recipes = new HashMap<>();
	private final PackOutput output;
	private final String modid;
	
	public BoondollarPriceProvider(PackOutput output, String modid)
	{
		this.output = output;
		this.modid = modid;
	}
	
	protected void registerPrices()
	{
		add(ONION.get(), 9, 15);
		add(JAR_OF_BUGS.get(), 12, 18);
		add(BUG_ON_A_STICK.get(), 4, 6);
		add(CONE_OF_FLIES.get(), 4, 6);
		add(GRASSHOPPER.get(), 90, 110);
		add(SALAD.get(), 10, 14);
		add(CHOCOLATE_BEETLE.get(), 30, 35);
		add(DESERT_FRUIT.get(), 2, 6);
		add(MSItems.GLOWING_MUSHROOM.get(), 10, 15);
		add(MSItems.COLD_CAKE.get(), 400);
		add(MSItems.BLUE_CAKE.get(), 400);
		add(MSItems.HOT_CAKE.get(), 400);
		add(MSItems.RED_CAKE.get(), 400);
		add(MSItems.FUCHSIA_CAKE.get(), 1500);
		add(ROCK_COOKIE.get(), 15, 20);
		add(STRAWBERRY_CHUNK.get(), 100, 150);
		add(TAB.get(), 200);
		add(ORANGE_FAYGO.get(), 100);
		add(CANDY_APPLE_FAYGO.get(), 100);
		add(FAYGO_COLA.get(), 100);
		add(COTTON_CANDY_FAYGO.get(), 100);
		add(CREME_SODA_FAYGO.get(), 100);
		add(GRAPE_FAYGO.get(), 100);
		add(MOON_MIST_FAYGO.get(), 100);
		add(PEACH_FAYGO.get(), 100);
		add(REDPOP_FAYGO.get(), 100);
		add(MSItems.GOLD_SEEDS.get(), 300, 400);
		add(MSItems.APPLE_CAKE.get(), 100, 140);
		add(IRRADIATED_STEAK.get(), 70, 80);
		add(CANDY_CORN.get(), 100, 150);
		add(BUILD_GUSHERS.get(), 90, 120);
		add(AMBER_GUMMY_WORM.get(), 125, 150);
		add(CAULK_PRETZEL.get(), 125, 150);
		add(CHALK_CANDY_CIGARETTE.get(), 125, 150);
		add(IODINE_LICORICE.get(), 125, 150);
		add(SHALE_PEEP.get(), 125, 150);
		add(TAR_LICORICE.get(), 125, 150);
		add(COBALT_GUM.get(), 150, 180);
		add(MARBLE_JAWBREAKER.get(), 150, 180);
		add(MERCURY_SIXLETS.get(), 150, 180);
		add(QUARTZ_JELLY_BEAN.get(), 150, 180);
		add(SULFUR_CANDY_APPLE.get(), 150, 180);
		add(AMETHYST_HARD_CANDY.get(), 175, 210);
		add(GARNET_TWIX.get(), 175, 210);
		add(RUBY_CROAK.get(), 175, 210);
		add(RUST_GUMMY_EYE.get(), 175, 210);
		add(DIAMOND_MINT.get(), 200, 240);
		add(GOLD_CANDY_RIBBON.get(), 200, 240);
		add(URANIUM_GUMMY_BEAR.get(), 200, 240);
		add(ARTIFACT_WARHEAD.get(), 225, 270);
		add(ZILLIUM_SKITTLES.get(), 250, 300);
		
		add(CARVING_TOOL.get(), 60, 90);
		add(MSItems.MINI_FROG_STATUE.get(), 200, 250);
		add(MSItems.MINI_WIZARD_STATUE.get(), 200, 250);
		add(MSItems.MINI_TYPHEUS_STATUE.get(), 10000, 12000);
		add(MSItems.STONE_TABLET.get(), 20, 30);
		add(THRESH_DVD.get(), 350, 400);
		add(CREW_POSTER.get(), 350, 400);
		add(SBAHJ_POSTER.get(), 350, 400);
		add(GAMEBRO_MAGAZINE.get(), 450, 600);
		add(GAMEGRL_MAGAZINE.get(), 450, 600);
		add(MUSIC_DISC_EMISSARY_OF_DANCE.get(), 1000);
		add(MUSIC_DISC_DANCE_STAB_DANCE.get(), 1000);
		add(MUSIC_DISC_RETRO_BATTLE.get(), 1000);
		add(CRUMPLY_HAT.get(), 80, 100);
		add(PLUSH_IGUANA.get(), 40, 100);
		add(PLUSH_NAKAGATOR.get(), 40, 100);
		add(PLUSH_SALAMANDER.get(), 40, 100);
		add(PLUSH_TURTLE.get(), 40, 100);
		add(BATTERY.get(), 10, 100);
		add(GRIMOIRE.get(), 666);
		add(ACE_OF_SPADES.get(), 3000, 5000);
		add(ACE_OF_HEARTS.get(), 3000, 5000);
		add(ACE_OF_DIAMONDS.get(), 3000, 5000);
		add(ACE_OF_CLUBS.get(), 3000, 5000);
		add(CLUBS_SUITARANG.get(), 30, 50);
		add(DIAMONDS_SUITARANG.get(), 30, 50);
		add(HEARTS_SUITARANG.get(), 30, 50);
		add(SPADES_SUITARANG.get(), 30, 50);
		add(POGO_CLUB.get(), 900, 1200);
		add(METAL_BAT.get(), 400, 500);
		add(FIRE_POKER.get(), 1500, 2000);
		add(COPSE_CRUSHER.get(), 1000, 1500);
		add(KATANA.get(), 400, 500);
		add(CACTACEAE_CUTLASS.get(), 500, 700);
		add(STEAK_SWORD.get(), 350, 650);
		add(BEEF_SWORD.get(), 250, 625);
		add(MSItems.GLOWYSTONE_DUST.get(), 20, 40);
		add(IRON_CANE.get(), 300, 400);
		add(MSItems.GLOWING_LOG.get(), 20, 32);
		add(MSItems.GLOWING_PLANKS.get(), 5, 8);
		add(MSItems.COARSE_STONE.get(), 5, 8);
		add(MSItems.CHISELED_COARSE_STONE.get(), 8, 15);
		add(MSItems.SHADE_BRICKS.get(), 5, 8);
		add(MSItems.SMOOTH_SHADE_STONE.get(), 5, 8);
		add(MSItems.FROST_BRICKS.get(), 5, 8);
		add(MSItems.FROST_TILE.get(), 5, 8);
		add(MSItems.CAST_IRON.get(), 5, 8);
		add(MSItems.CHISELED_CAST_IRON.get(), 8, 15);
		add(MSItems.VINE_LOG.get(), 20, 32);
		add(MSItems.FLOWERY_VINE_LOG.get(), 25, 40);
		add(MSItems.FROST_LOG.get(), 20, 32);
		add(MSItems.WOODEN_CACTUS.get(), 50, 60);
		add(MSItems.SUGAR_CUBE.get(), 200, 240);
		add(FUNGAL_SPORE.get(), 1, 4);
		add(SPOREO.get(), 15, 25);
		add(MOREL_MUSHROOM.get(), 40, 80);
		add(PARADISES_PORTABELLO.get(), 400, 600);
		add(BUG_NET.get(), 500, 600);
		
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
		add(GOLDEN_APPLE, 2500);
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
		add(CHORUS_FRUIT, 420);
		add(DRAGON_BREATH, 50, 100);
		add(EGG, 50, 100);
		add(ELYTRA, 500, 1000);
		add(OBSIDIAN, 8, 20);
		add(PAPER, 5, 20);
	}
	
	protected void add(ItemLike item, int value)
	{
		//Just set the name manually if this throws an exception
		add(Ingredient.of(item), ConstantInt.of(value), Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(item.asItem())).getPath());
	}
	
	protected void add(ItemLike item, int min, int max)
	{
		//Just set the name manually if this throws an exception
		add(Ingredient.of(item), UniformInt.of(min, max), Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(item.asItem())).getPath());
	}
	
	protected void add(Ingredient ingredient, IntProvider range, String name)
	{
		add(new BoondollarPriceRecipe(ingredient, range), new ResourceLocation(modid, name));
	}
	
	protected void add(BoondollarPriceRecipe pricing, ResourceLocation name)
	{
		recipes.put(name, pricing);
	}
	
	@Override
	public CompletableFuture<?> run(CachedOutput cache)
	{
		registerPrices();
		
		Path outputPath = output.getOutputFolder();
		List<CompletableFuture<?>> futures = new ArrayList<>(recipes.size());
		
		for(Map.Entry<ResourceLocation, BoondollarPriceRecipe> entry : recipes.entrySet())
		{
			Path pricingPath = getPath(outputPath, entry.getKey());
			JsonElement jsonData = BoondollarPriceRecipe.CODEC.encodeStart(JsonOps.INSTANCE, entry.getValue())
					.getOrThrow(false, message -> LOGGER.error("Problem encoding boondollar price {}: {}", entry.getKey(), message));
			futures.add(DataProvider.saveStable(cache, jsonData, pricingPath));
		}
		return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
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
