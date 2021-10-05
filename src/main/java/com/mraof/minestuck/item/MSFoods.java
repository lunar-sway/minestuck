package com.mraof.minestuck.item;

import net.minecraft.item.Food;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;

public class MSFoods
{
    //gushers
    public static final Food PHLEGM_GUSHERS = (new Food.Builder()).nutrition(2).saturationMod(0.2F).alwaysEat().build();
    public static final Food SORROW_GUSHERS = (new Food.Builder()).nutrition(0).saturationMod(0.0F).alwaysEat().effect(new EffectInstance(Effects.HARM, 1), 1.0F).effect(new EffectInstance(Effects.POISON, 200, 2), 1.0F).build();
    //foods
    public static final Food BUG_ON_A_STICK = (new Food.Builder()).nutrition(3).saturationMod(0.2F).build();
    public static final Food CHOCOLATE_BEETLE = (new Food.Builder()).nutrition(5).saturationMod(0.5F).build();
    public static final Food CONE_OF_FLIES = (new Food.Builder()).nutrition(2).saturationMod(0.1F).build();
    public static final Food GRASSHOPPER = (new Food.Builder()).nutrition(4).saturationMod(0.5F).build();
    public static final Food CICADA = (new Food.Builder()).nutrition(5).saturationMod(0.5F).build();
    public static final Food JAR_OF_BUGS = (new Food.Builder()).nutrition(6).saturationMod(0.4F).build();
    public static final Food BUG_MAC = (new Food.Builder()).nutrition(10).saturationMod(0.9F).build();
    public static final Food ONION = (new Food.Builder()).nutrition(3).saturationMod(0.2F).build();
    public static final Food DESERT_FRUIT = (new Food.Builder()).nutrition(3).saturationMod(0.2F).build();
    public static final Food FUNGAL_SPORE = (new Food.Builder()).nutrition(1).saturationMod(0.2F).effect(new EffectInstance(Effects.POISON, 60, 3), 0.7F).build();
    public static final Food SPOREO = (new Food.Builder()).nutrition(3).saturationMod(0.4F).build();
    public static final Food MOREL_MUSHROOM = (new Food.Builder()).nutrition(3).saturationMod(0.9F).build();
    public static final Food FRENCH_FRY = (new Food.Builder()).nutrition(1).saturationMod(0.1F).build();
    public static final Food STRAWBERRY_CHUNK = (new Food.Builder()).nutrition(4).saturationMod(0.5F).build();
    public static final Food FOOD_CAN = (new Food.Builder()).nutrition(4).saturationMod(0.6F).build();
    public static final Food IRRADIATED_STEAK = (new Food.Builder()).nutrition(10).saturationMod(0.9F).effect(new EffectInstance(Effects.WITHER, 100, 1), 0.9F).build();
    public static final Food WOODEN_CARROT = (new Food.Builder()).nutrition(0).saturationMod(0.1F).effect(new EffectInstance(Effects.HARM, 1), 0.5F).effect(new EffectInstance(Effects.POISON, 100), 1.0F).build();
    public static final Food SURPRISE_EMBRYO = (new Food.Builder()).nutrition(3).saturationMod(0.2F).build();
    public static final Food UNKNOWABLE_EGG = (new Food.Builder()).nutrition(3).saturationMod(0.3F).build();
    public static final Food BREADCRUMBS = (new Food.Builder()).nutrition(1).saturationMod(0.1F).fast().build();
    public static final Food SALAD = new Food.Builder().nutrition(1).saturationMod(0.6F).build();
    //Candies
    public static final Food CANDY_CORN = (new Food.Builder()).nutrition(2).saturationMod(0.3F).build();
    public static final Food TUIX_BAR = (new Food.Builder()).nutrition(2).saturationMod(0.6F).build();
    public static final Food BUILD_GUSHERS = (new Food.Builder()).nutrition(2).saturationMod(0.0F).build();
    public static final Food AMBER_GUMMY_WORM = (new Food.Builder()).nutrition(2).saturationMod(0.1F).build();
    public static final Food CAULK_PRETZEL = (new Food.Builder()).nutrition(2).saturationMod(0.1F).build();
    public static final Food CHALK_CANDY_CIGARETTE = (new Food.Builder()).nutrition(2).saturationMod(0.1F).build();
    public static final Food IODINE_LICORICE = (new Food.Builder()).nutrition(2).saturationMod(0.1F).build();
    public static final Food SHALE_PEEP = (new Food.Builder()).nutrition(2).saturationMod(0.1F).build();
    public static final Food TAR_LICORICE = (new Food.Builder()).nutrition(2).saturationMod(0.1F).build();
    public static final Food COBALT_GUM = (new Food.Builder()).nutrition(2).saturationMod(0.2F).build();
    public static final Food MARBLE_JAWBREAKER = (new Food.Builder()).nutrition(2).saturationMod(0.2F).build();
    public static final Food MERCURY_SIXLETS = (new Food.Builder()).nutrition(2).saturationMod(0.2F).build();
    public static final Food QUARTZ_JELLY_BEAN = (new Food.Builder()).nutrition(2).saturationMod(0.2F).build();
    public static final Food SULFUR_CANDY_APPLE = (new Food.Builder()).nutrition(2).saturationMod(0.2F).build();
    public static final Food AMETHYST_HARD_CANDY = (new Food.Builder()).nutrition(2).saturationMod(0.3F).build();
    public static final Food GARNET_TWIX = (new Food.Builder()).nutrition(2).saturationMod(0.3F).build();
    public static final Food RUBY_LOLLIPOP = (new Food.Builder()).nutrition(2).saturationMod(0.3F).build();
    public static final Food RUST_GUMMY_EYE = (new Food.Builder()).nutrition(2).saturationMod(0.3F).build();
    public static final Food DIAMOND_MINT = (new Food.Builder()).nutrition(2).saturationMod(0.4F).build();
    public static final Food GOLD_CANDY_RIBBON = (new Food.Builder()).nutrition(2).saturationMod(0.4F).build();
    public static final Food URANIUM_GUMMY_BEAR = (new Food.Builder()).nutrition(2).saturationMod(0.4F).build();
    public static final Food ARTIFACT_WARHEAD = (new Food.Builder()).nutrition(2).saturationMod(0.5F).build();
    public static final Food ZILLIUM_SKITTLES = (new Food.Builder()).nutrition(2).saturationMod(0.6F).build();
    //Drinks
    public static final Food APPLE_JUICE = (new Food.Builder()).nutrition(1).saturationMod(0.4F).alwaysEat().build();
    public static final Food TAB = (new Food.Builder()).nutrition(1).saturationMod(0.0F).alwaysEat().build();
    public static final Food FAYGO = (new Food.Builder()).nutrition(1).saturationMod(0.0F).alwaysEat().build();
    public static final Food FAYGO_CANDY_APPLE = (new Food.Builder()).nutrition(1).saturationMod(0.0F).alwaysEat().build();
    public static final Food FAYGO_COLA = (new Food.Builder()).nutrition(1).saturationMod(0.0F).alwaysEat().build();
    public static final Food FAYGO_COTTON_CANDY = (new Food.Builder()).nutrition(1).saturationMod(0.0F).alwaysEat().build();
    public static final Food FAYGO_CREME = (new Food.Builder()).nutrition(1).saturationMod(0.0F).alwaysEat().build();
    public static final Food FAYGO_GRAPE = (new Food.Builder()).nutrition(1).saturationMod(0.0F).alwaysEat().build();
    public static final Food FAYGO_MOON_MIST = (new Food.Builder()).nutrition(1).saturationMod(0.0F).alwaysEat().build();
    public static final Food FAYGO_PEACH = (new Food.Builder()).nutrition(1).saturationMod(0.0F).alwaysEat().build();
    public static final Food FAYGO_REDPOP = (new Food.Builder()).nutrition(1).saturationMod(0.0F).alwaysEat().build();
    public static final Food GRUB_SAUCE = (new Food.Builder()).nutrition(1).saturationMod(0.1F).alwaysEat().build();
}
