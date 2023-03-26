package com.mraof.minestuck.world.lands;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.world.lands.terrain.*;
import com.mraof.minestuck.world.lands.title.*;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class LandTypes
{
	public static final ResourceKey<Registry<TerrainLandType>> TERRAIN_KEY = ResourceKey.createRegistryKey(new ResourceLocation(Minestuck.MOD_ID, "terrain_land_type"));
	public static final ResourceKey<Registry<TitleLandType>> TITLE_KEY = ResourceKey.createRegistryKey(new ResourceLocation(Minestuck.MOD_ID, "title_land_type"));
	
	public static final DeferredRegister<TerrainLandType> TERRAIN_REGISTER = DeferredRegister.create(TERRAIN_KEY.location(), Minestuck.MOD_ID);
	public static final DeferredRegister<TitleLandType> TITLE_REGISTER = DeferredRegister.create(TITLE_KEY.location(), Minestuck.MOD_ID);
	
	public static final Supplier<IForgeRegistry<TerrainLandType>> TERRAIN_REGISTRY = TERRAIN_REGISTER.makeRegistry(() -> new RegistryBuilder<TerrainLandType>().hasTags());
	public static final Supplier<IForgeRegistry<TitleLandType>> TITLE_REGISTRY = TITLE_REGISTER.makeRegistry(() -> new RegistryBuilder<TitleLandType>().hasTags());
	
	public static final RegistryObject<TerrainLandType> TERRAIN_NULL = TERRAIN_REGISTER.register("null", NullTerrainLandType::new);
	public static final RegistryObject<TerrainLandType> FOREST = TERRAIN_REGISTER.register("forest", ForestLandType::createForest);
	public static final RegistryObject<TerrainLandType> TAIGA = TERRAIN_REGISTER.register("taiga", ForestLandType::createTaiga);
	public static final RegistryObject<TerrainLandType> FROST = TERRAIN_REGISTER.register("frost", FrostLandType::new);
	public static final RegistryObject<TerrainLandType> FUNGI = TERRAIN_REGISTER.register("fungi", FungiLandType::new);
	public static final RegistryObject<TerrainLandType> HEAT = TERRAIN_REGISTER.register("heat", HeatLandType::new);
	public static final RegistryObject<TerrainLandType> ROCK = TERRAIN_REGISTER.register("rock", RockLandType::createRock);
	public static final RegistryObject<TerrainLandType> PETRIFICATION = TERRAIN_REGISTER.register("petrification", RockLandType::createPetrification);
	public static final RegistryObject<TerrainLandType> SAND = TERRAIN_REGISTER.register("sand", SandLandType::createSand);
	public static final RegistryObject<TerrainLandType> RED_SAND = TERRAIN_REGISTER.register("red_sand", SandLandType::createRedSand);
	public static final RegistryObject<TerrainLandType> LUSH_DESERTS = TERRAIN_REGISTER.register("lush_deserts", SandLandType::createLushDeserts);
	public static final RegistryObject<TerrainLandType> SANDSTONE = TERRAIN_REGISTER.register("sandstone", SandstoneLandType::createSandstone);
	public static final RegistryObject<TerrainLandType> RED_SANDSTONE = TERRAIN_REGISTER.register("red_sandstone", SandstoneLandType::createRedSandstone);
	public static final RegistryObject<TerrainLandType> SHADE = TERRAIN_REGISTER.register("shade", ShadeLandType::new);
	public static final RegistryObject<TerrainLandType> WOOD = TERRAIN_REGISTER.register("wood", WoodLandType::new);
	public static final RegistryObject<TerrainLandType> RAINBOW = TERRAIN_REGISTER.register("rainbow", RainbowLandType::new);
	public static final RegistryObject<TerrainLandType> FLORA = TERRAIN_REGISTER.register("flora", FloraLandType::new);
	public static final RegistryObject<TerrainLandType> END = TERRAIN_REGISTER.register("end", EndLandType::new);
	public static final RegistryObject<TerrainLandType> RAIN = TERRAIN_REGISTER.register("rain", RainLandType::new);
	
	public static final RegistryObject<TitleLandType> TITLE_NULL = TITLE_REGISTER.register("null", NullTitleLandType::new);
	public static final RegistryObject<TitleLandType> FROGS = TITLE_REGISTER.register("frogs", FrogsLandType::new);
	public static final RegistryObject<TitleLandType> WIND = TITLE_REGISTER.register("wind", WindLandType::new);
	public static final RegistryObject<TitleLandType> LIGHT = TITLE_REGISTER.register("light", LightLandType::new);
	public static final RegistryObject<TitleLandType> CLOCKWORK = TITLE_REGISTER.register("clockwork", ClockworkLandType::new);
	public static final RegistryObject<TitleLandType> SILENCE = TITLE_REGISTER.register("silence", SilenceLandType::new);
	public static final RegistryObject<TitleLandType> THUNDER = TITLE_REGISTER.register("thunder", ThunderLandType::new);
	public static final RegistryObject<TitleLandType> PULSE = TITLE_REGISTER.register("pulse", PulseLandType::new);
	public static final RegistryObject<TitleLandType> THOUGHT = TITLE_REGISTER.register("thought", ThoughtLandType::new);
	public static final RegistryObject<TitleLandType> BUCKETS = TITLE_REGISTER.register("buckets", BucketsLandType::new);
	public static final RegistryObject<TitleLandType> CAKE = TITLE_REGISTER.register("cake", CakeLandType::new);
	public static final RegistryObject<TitleLandType> RABBITS = TITLE_REGISTER.register("rabbits", RabbitsLandType::new);
	public static final RegistryObject<TitleLandType> MONSTERS = TITLE_REGISTER.register("monsters", () -> new MonstersLandType(MonstersLandType.Variant.MONSTERS));
	public static final RegistryObject<TitleLandType> UNDEAD = TITLE_REGISTER.register("undead", () -> new MonstersLandType(MonstersLandType.Variant.UNDEAD));
	public static final RegistryObject<TitleLandType> TOWERS = TITLE_REGISTER.register("towers", TowersLandType::new);
	
	public static Set<TitleLandType> getCompatibleTitleTypes(TerrainLandType terrain)
	{
		return TITLE_REGISTRY.get().getValues().stream().filter(landType -> landType.isAspectCompatible(terrain) && landType.canBePickedAtRandom()).collect(Collectors.toSet());
	}
}