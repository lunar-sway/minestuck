package com.mraof.minestuck.item;

import com.mraof.minestuck.effects.MSEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

public class MSFoods
{
    //gushers
    public static final FoodProperties PHLEGM_GUSHERS = (new FoodProperties.Builder()).nutrition(2).saturationMod(0.2F).alwaysEat().build();
    public static final FoodProperties SORROW_GUSHERS = (new FoodProperties.Builder()).nutrition(0).saturationMod(0.0F).alwaysEat().effect(() -> new MobEffectInstance(MobEffects.HARM, 1), 1.0F).effect(() -> new MobEffectInstance(MobEffects.POISON, 200, 2), 1.0F).build();
    //foods
    public static final FoodProperties BUG_ON_A_STICK = (new FoodProperties.Builder()).nutrition(3).saturationMod(0.2F).build();
    public static final FoodProperties CHOCOLATE_BEETLE = (new FoodProperties.Builder()).nutrition(5).saturationMod(0.5F).build();
    public static final FoodProperties CONE_OF_FLIES = (new FoodProperties.Builder()).nutrition(2).saturationMod(0.1F).build();
    public static final FoodProperties GRASSHOPPER = (new FoodProperties.Builder()).nutrition(4).saturationMod(0.5F).build();
    public static final FoodProperties CICADA = (new FoodProperties.Builder()).nutrition(5).saturationMod(0.5F).build();
    public static final FoodProperties JAR_OF_BUGS = (new FoodProperties.Builder()).nutrition(6).saturationMod(0.4F).build();
    public static final FoodProperties BUG_MAC = (new FoodProperties.Builder()).nutrition(10).saturationMod(0.9F).build();
    public static final FoodProperties ONION = (new FoodProperties.Builder()).nutrition(3).saturationMod(0.2F).build();
    public static final FoodProperties DESERT_FRUIT = (new FoodProperties.Builder()).nutrition(3).saturationMod(0.2F).build();
    public static final FoodProperties FUNGAL_SPORE = (new FoodProperties.Builder()).nutrition(1).saturationMod(0.2F).effect(() -> new MobEffectInstance(MobEffects.POISON, 60, 3), 0.7F).build();
    public static final FoodProperties SPOREO = (new FoodProperties.Builder()).nutrition(3).saturationMod(0.4F).build();
    public static final FoodProperties MOREL_MUSHROOM = (new FoodProperties.Builder()).nutrition(3).saturationMod(0.9F).build();
    public static final FoodProperties FRENCH_FRY = (new FoodProperties.Builder()).nutrition(1).saturationMod(0.1F).build();
    public static final FoodProperties STRAWBERRY_CHUNK = (new FoodProperties.Builder()).nutrition(4).saturationMod(0.5F).build();
    public static final FoodProperties FOOD_CAN = (new FoodProperties.Builder()).nutrition(4).saturationMod(0.6F).build();
    public static final FoodProperties IRRADIATED_STEAK = (new FoodProperties.Builder()).nutrition(10).saturationMod(0.9F).effect(() -> new MobEffectInstance(MobEffects.WITHER, 100, 1), 0.9F).build();
    public static final FoodProperties WOODEN_CARROT = (new FoodProperties.Builder()).nutrition(0).saturationMod(0.1F).effect(() -> new MobEffectInstance(MobEffects.HARM, 1), 0.5F).effect(() -> new MobEffectInstance(MobEffects.POISON, 100), 1.0F).build();
    public static final FoodProperties SURPRISE_EMBRYO = (new FoodProperties.Builder()).nutrition(3).saturationMod(0.2F).build();
    public static final FoodProperties UNKNOWABLE_EGG = (new FoodProperties.Builder()).nutrition(3).saturationMod(0.3F).build();
    public static final FoodProperties BREADCRUMBS = (new FoodProperties.Builder()).nutrition(1).saturationMod(0.1F).fast().build();
    public static final FoodProperties SALAD = new FoodProperties.Builder().nutrition(1).saturationMod(0.6F).build();
    public static final FoodProperties SOPOR_SLIME_PIE = (new FoodProperties.Builder()).nutrition(1).saturationMod(0.0F).build();
    //Candies
    public static final FoodProperties CANDY_CORN = (new FoodProperties.Builder()).nutrition(2).saturationMod(0.3F).build();
    public static final FoodProperties TUIX_BAR = (new FoodProperties.Builder()).nutrition(2).saturationMod(0.6F).build();
    public static final FoodProperties BUILD_GUSHERS = (new FoodProperties.Builder()).nutrition(2).saturationMod(0.0F).build();
    public static final FoodProperties AMBER_GUMMY_WORM = (new FoodProperties.Builder()).nutrition(2).saturationMod(0.1F).build();
    public static final FoodProperties CAULK_PRETZEL = (new FoodProperties.Builder()).nutrition(2).saturationMod(0.1F).build();
    public static final FoodProperties CHALK_CANDY_CIGARETTE = (new FoodProperties.Builder()).nutrition(2).saturationMod(0.1F).build();
    public static final FoodProperties IODINE_LICORICE = (new FoodProperties.Builder()).nutrition(2).saturationMod(0.1F).build();
    public static final FoodProperties SHALE_PEEP = (new FoodProperties.Builder()).nutrition(2).saturationMod(0.1F).build();
    public static final FoodProperties TAR_LICORICE = (new FoodProperties.Builder()).nutrition(2).saturationMod(0.1F).build();
    public static final FoodProperties COBALT_GUM = (new FoodProperties.Builder()).nutrition(2).saturationMod(0.2F).build();
    public static final FoodProperties MARBLE_JAWBREAKER = (new FoodProperties.Builder()).nutrition(2).saturationMod(0.2F).build();
    public static final FoodProperties MERCURY_SIXLETS = (new FoodProperties.Builder()).nutrition(2).saturationMod(0.2F).build();
    public static final FoodProperties QUARTZ_JELLY_BEAN = (new FoodProperties.Builder()).nutrition(2).saturationMod(0.2F).build();
    public static final FoodProperties SULFUR_CANDY_APPLE = (new FoodProperties.Builder()).nutrition(2).saturationMod(0.2F).build();
    public static final FoodProperties AMETHYST_HARD_CANDY = (new FoodProperties.Builder()).nutrition(2).saturationMod(0.3F).build();
    public static final FoodProperties GARNET_TWIX = (new FoodProperties.Builder()).nutrition(2).saturationMod(0.3F).build();
    public static final FoodProperties RUBY_LOLLIPOP = (new FoodProperties.Builder()).nutrition(2).saturationMod(0.3F).build();
    public static final FoodProperties RUST_GUMMY_EYE = (new FoodProperties.Builder()).nutrition(2).saturationMod(0.3F).build();
    public static final FoodProperties DIAMOND_MINT = (new FoodProperties.Builder()).nutrition(2).saturationMod(0.4F).build();
    public static final FoodProperties GOLD_CANDY_RIBBON = (new FoodProperties.Builder()).nutrition(2).saturationMod(0.4F).build();
    public static final FoodProperties URANIUM_GUMMY_BEAR = (new FoodProperties.Builder()).nutrition(2).saturationMod(0.4F).build();
    public static final FoodProperties ARTIFACT_WARHEAD = (new FoodProperties.Builder()).nutrition(2).saturationMod(0.5F).build();
    public static final FoodProperties ZILLIUM_SKITTLES = (new FoodProperties.Builder()).nutrition(2).saturationMod(0.6F).build();
    //Drinks
    public static final FoodProperties APPLE_JUICE = (new FoodProperties.Builder()).nutrition(1).saturationMod(0.4F).alwaysEat().build();
    public static final FoodProperties TAB = (new FoodProperties.Builder()).nutrition(1).saturationMod(0.0F).alwaysEat().build();
    public static final FoodProperties FAYGO = (new FoodProperties.Builder()).nutrition(1).saturationMod(0.0F).alwaysEat().build();
    public static final FoodProperties FAYGO_CANDY_APPLE = (new FoodProperties.Builder()).nutrition(1).saturationMod(0.0F).alwaysEat().build();
    public static final FoodProperties FAYGO_COLA = (new FoodProperties.Builder()).nutrition(1).saturationMod(0.0F).alwaysEat().build();
    public static final FoodProperties FAYGO_COTTON_CANDY = (new FoodProperties.Builder()).nutrition(1).saturationMod(0.0F).alwaysEat().build();
    public static final FoodProperties FAYGO_CREME = (new FoodProperties.Builder()).nutrition(1).saturationMod(0.0F).alwaysEat().build();
    public static final FoodProperties FAYGO_GRAPE = (new FoodProperties.Builder()).nutrition(1).saturationMod(0.0F).alwaysEat().build();
    public static final FoodProperties FAYGO_MOON_MIST = (new FoodProperties.Builder()).nutrition(1).saturationMod(0.0F).alwaysEat().build();
    public static final FoodProperties FAYGO_PEACH = (new FoodProperties.Builder()).nutrition(1).saturationMod(0.0F).alwaysEat().build();
    public static final FoodProperties FAYGO_REDPOP = (new FoodProperties.Builder()).nutrition(1).saturationMod(0.0F).alwaysEat().build();
    public static final FoodProperties GRUB_SAUCE = (new FoodProperties.Builder()).nutrition(1).saturationMod(0.1F).alwaysEat().build();
}
