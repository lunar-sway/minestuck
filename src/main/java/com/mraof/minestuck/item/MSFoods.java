package com.mraof.minestuck.item;

import net.minecraft.item.Food;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;

public class MSFoods
{
    //foods
    public static final Food BUG_ON_A_STICK = (new Food.Builder()).hunger(1).saturation(0.1F).build();
    public static final Food CHOCOLATE_BEETLE = (new Food.Builder()).hunger(3).saturation(0.4F).build();
    public static final Food CONE_OF_FLIES = (new Food.Builder()).hunger(2).saturation(0.1F).build();
    public static final Food GRASSHOPPER = (new Food.Builder()).hunger(4).saturation(0.5F).build();
    public static final Food JAR_OF_BUGS = (new Food.Builder()).hunger(3).saturation(0.2F).build();
    public static final Food ONION = (new Food.Builder()).hunger(2).saturation(0.2F).build();
    public static final Food DESERT_FRUIT = (new Food.Builder()).hunger(1).saturation(0.1F).build();
    public static final Food FUNGAL_SPORE = (new Food.Builder()).hunger(1).saturation(0.2F).effect(new EffectInstance(Effects.POISON, 60, 3), 0.7F).build();
    public static final Food SPOREO = (new Food.Builder()).hunger(3).saturation(0.4F).build();
    public static final Food MOREL_MUSHROOM = (new Food.Builder()).hunger(3).saturation(0.9F).build();
    public static final Food FRENCH_FRY = (new Food.Builder()).hunger(1).saturation(0.1F).build();
    public static final Food STRAWBERRY_CHUNK = (new Food.Builder()).hunger(4).saturation(0.5F).build();
    public static final Food IRRADIATED_STEAK = (new Food.Builder()).hunger(4).saturation(0.4F).effect(new EffectInstance(Effects.WITHER, 100, 1), 0.9F).build();
    public static final Food WOODEN_CARROT = (new Food.Builder()).hunger(0).saturation(0.1F).effect(new EffectInstance(Effects.INSTANT_DAMAGE, 0, 0), 0.5F).effect(new EffectInstance(Effects.POISON, 100, 0), 1.0F).build();
    public static final Food SURPRISE_EMBRYO = (new Food.Builder()).hunger(3).saturation(0.2F).build();
    public static final Food UNKNOWABLE_EGG = (new Food.Builder()).hunger(3).saturation(0.3F).build();
    //Candies
    public static final Food CANDY_CORN = (new Food.Builder()).hunger(2).saturation(0.3F).build();
    public static final Food BUILD_GUSHERS = (new Food.Builder()).hunger(2).saturation(0.0F).build();
    public static final Food AMBER_GUMMY_WORM = (new Food.Builder()).hunger(2).saturation(0.1F).build();
    public static final Food CAULK_PRETZEL = (new Food.Builder()).hunger(2).saturation(0.1F).build();
    public static final Food CHALK_CANDY_CIGARETTE = (new Food.Builder()).hunger(2).saturation(0.1F).build();
    public static final Food IODINE_LICORICE = (new Food.Builder()).hunger(2).saturation(0.1F).build();
    public static final Food SHALE_PEEP = (new Food.Builder()).hunger(2).saturation(0.1F).build();
    public static final Food TAR_LICORICE = (new Food.Builder()).hunger(2).saturation(0.1F).build();
    public static final Food COBALT_GUM = (new Food.Builder()).hunger(2).saturation(0.2F).build();
    public static final Food MARBLE_JAWBREAKER = (new Food.Builder()).hunger(2).saturation(0.2F).build();
    public static final Food MERCURY_SIXLETS = (new Food.Builder()).hunger(2).saturation(0.2F).build();
    public static final Food QUARTZ_JELLY_BEAN = (new Food.Builder()).hunger(2).saturation(0.2F).build();
    public static final Food SULFUR_CANDY_APPLE = (new Food.Builder()).hunger(2).saturation(0.2F).build();
    public static final Food AMETHYST_HARD_CANDY = (new Food.Builder()).hunger(2).saturation(0.3F).build();
    public static final Food GARNET_TWIX = (new Food.Builder()).hunger(2).saturation(0.3F).build();
    public static final Food RUBY_LOLLIPOP = (new Food.Builder()).hunger(2).saturation(0.3F).build();
    public static final Food RUST_GUMMY_EYE = (new Food.Builder()).hunger(2).saturation(0.3F).build();
    public static final Food DIAMOND_MINT = (new Food.Builder()).hunger(2).saturation(0.4F).build();
    public static final Food GOLD_CANDY_RIBBON = (new Food.Builder()).hunger(2).saturation(0.4F).build();
    public static final Food URANIUM_GUMMY_BEAR = (new Food.Builder()).hunger(2).saturation(0.4F).build();
    public static final Food ARTIFACT_WARHEAD = (new Food.Builder()).hunger(2).saturation(0.5F).build();
    public static final Food ZILLIUM_SKITTLES = (new Food.Builder()).hunger(2).saturation(0.6F).build();
    //Drinks
    public static final Food TAB = (new Food.Builder()).hunger(1).saturation(0.0F).setAlwaysEdible().build();
    public static final Food FAYGO = (new Food.Builder()).hunger(1).saturation(0.0F).setAlwaysEdible().build();
    public static final Food FAYGO_CANDY_APPLE = (new Food.Builder()).hunger(1).saturation(0.0F).setAlwaysEdible().build();
    public static final Food FAYGO_COLA = (new Food.Builder()).hunger(1).saturation(0.0F).setAlwaysEdible().build();
    public static final Food FAYGO_COTTON_CANDY = (new Food.Builder()).hunger(1).saturation(0.0F).setAlwaysEdible().build();
    public static final Food FAYGO_CREME = (new Food.Builder()).hunger(1).saturation(0.0F).setAlwaysEdible().build();
    public static final Food FAYGO_GRAPE = (new Food.Builder()).hunger(1).saturation(0.0F).setAlwaysEdible().build();
    public static final Food FAYGO_MOON_MIST = (new Food.Builder()).hunger(1).saturation(0.0F).setAlwaysEdible().build();
    public static final Food FAYGO_PEACH = (new Food.Builder()).hunger(1).saturation(0.0F).setAlwaysEdible().build();
    public static final Food FAYGO_REDPOP = (new Food.Builder()).hunger(1).saturation(0.0F).setAlwaysEdible().build();
}
