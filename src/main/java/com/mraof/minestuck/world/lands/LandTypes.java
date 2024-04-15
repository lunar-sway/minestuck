package com.mraof.minestuck.world.lands;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.world.lands.terrain.*;
import com.mraof.minestuck.world.lands.title.*;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class LandTypes
{
	public static final ResourceKey<Registry<TerrainLandType>> TERRAIN_KEY = ResourceKey.createRegistryKey(new ResourceLocation(Minestuck.MOD_ID, "terrain_land_type"));
	public static final ResourceKey<Registry<TitleLandType>> TITLE_KEY = ResourceKey.createRegistryKey(new ResourceLocation(Minestuck.MOD_ID, "title_land_type"));
	
	public static final DeferredRegister<TerrainLandType> TERRAIN_REGISTER = DeferredRegister.create(TERRAIN_KEY.location(), Minestuck.MOD_ID);
	public static final DeferredRegister<TitleLandType> TITLE_REGISTER = DeferredRegister.create(TITLE_KEY.location(), Minestuck.MOD_ID);
	
	public static final Registry<TerrainLandType> TERRAIN_REGISTRY = TERRAIN_REGISTER.makeRegistry(builder -> builder.sync(true));
	public static final Registry<TitleLandType> TITLE_REGISTRY = TITLE_REGISTER.makeRegistry(builder -> builder.sync(true));
	
	public static final Supplier<TerrainLandType> TERRAIN_NULL = TERRAIN_REGISTER.register("null", NullTerrainLandType::new);
	public static final Supplier<TerrainLandType> FOREST = TERRAIN_REGISTER.register("forest", ForestLandType::createForest);
	public static final Supplier<TerrainLandType> TAIGA = TERRAIN_REGISTER.register("taiga", ForestLandType::createTaiga);
	public static final Supplier<TerrainLandType> FROST = TERRAIN_REGISTER.register("frost", FrostLandType::new);
	public static final Supplier<TerrainLandType> FUNGI = TERRAIN_REGISTER.register("fungi", FungiLandType::new);
	public static final Supplier<TerrainLandType> HEAT = TERRAIN_REGISTER.register("heat", HeatLandType::new);
	public static final Supplier<TerrainLandType> ROCK = TERRAIN_REGISTER.register("rock", RockLandType::createRock);
	public static final Supplier<TerrainLandType> PETRIFICATION = TERRAIN_REGISTER.register("petrification", RockLandType::createPetrification);
	public static final Supplier<TerrainLandType> SAND = TERRAIN_REGISTER.register("sand", SandLandType::createSand);
	public static final Supplier<TerrainLandType> RED_SAND = TERRAIN_REGISTER.register("red_sand", SandLandType::createRedSand);
	public static final Supplier<TerrainLandType> LUSH_DESERTS = TERRAIN_REGISTER.register("lush_deserts", SandLandType::createLushDeserts);
	public static final Supplier<TerrainLandType> SANDSTONE = TERRAIN_REGISTER.register("sandstone", SandstoneLandType::createSandstone);
	public static final Supplier<TerrainLandType> RED_SANDSTONE = TERRAIN_REGISTER.register("red_sandstone", SandstoneLandType::createRedSandstone);
	public static final Supplier<TerrainLandType> SHADE = TERRAIN_REGISTER.register("shade", ShadeLandType::new);
	public static final Supplier<TerrainLandType> WOOD = TERRAIN_REGISTER.register("wood", WoodLandType::new);
	public static final Supplier<TerrainLandType> RAINBOW = TERRAIN_REGISTER.register("rainbow", RainbowLandType::new);
	public static final Supplier<TerrainLandType> FLORA = TERRAIN_REGISTER.register("flora", FloraLandType::new);
	public static final Supplier<TerrainLandType> END = TERRAIN_REGISTER.register("end", EndLandType::new);
	public static final Supplier<TerrainLandType> RAIN = TERRAIN_REGISTER.register("rain", RainLandType::new);
	
	public static final Supplier<TitleLandType> TITLE_NULL = TITLE_REGISTER.register("null", NullTitleLandType::new);
	public static final Supplier<TitleLandType> FROGS = TITLE_REGISTER.register("frogs", FrogsLandType::new);
	public static final Supplier<TitleLandType> WIND = TITLE_REGISTER.register("wind", WindLandType::new);
	public static final Supplier<TitleLandType> LIGHT = TITLE_REGISTER.register("light", LightLandType::new);
	public static final Supplier<TitleLandType> CLOCKWORK = TITLE_REGISTER.register("clockwork", ClockworkLandType::new);
	public static final Supplier<TitleLandType> SILENCE = TITLE_REGISTER.register("silence", SilenceLandType::new);
	public static final Supplier<TitleLandType> THUNDER = TITLE_REGISTER.register("thunder", ThunderLandType::new);
	public static final Supplier<TitleLandType> PULSE = TITLE_REGISTER.register("pulse", PulseLandType::new);
	public static final Supplier<TitleLandType> THOUGHT = TITLE_REGISTER.register("thought", ThoughtLandType::new);
	public static final Supplier<TitleLandType> BUCKETS = TITLE_REGISTER.register("buckets", BucketsLandType::new);
	public static final Supplier<TitleLandType> CAKE = TITLE_REGISTER.register("cake", CakeLandType::new);
	public static final Supplier<TitleLandType> RABBITS = TITLE_REGISTER.register("rabbits", RabbitsLandType::new);
	public static final Supplier<TitleLandType> MONSTERS = TITLE_REGISTER.register("monsters", () -> new MonstersLandType(MonstersLandType.Variant.MONSTERS));
	public static final Supplier<TitleLandType> UNDEAD = TITLE_REGISTER.register("undead", () -> new MonstersLandType(MonstersLandType.Variant.UNDEAD));
	public static final Supplier<TitleLandType> TOWERS = TITLE_REGISTER.register("towers", TowersLandType::new);
}