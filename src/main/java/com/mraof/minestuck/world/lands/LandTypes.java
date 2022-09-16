package com.mraof.minestuck.world.lands;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.world.lands.terrain.*;
import com.mraof.minestuck.world.lands.title.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.NewRegistryEvent;
import net.minecraftforge.registries.ObjectHolder;
import net.minecraftforge.registries.RegistryBuilder;

import javax.annotation.Nonnull;
import java.util.Set;
import java.util.stream.Collectors;

@ObjectHolder(Minestuck.MOD_ID)
@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus=Mod.EventBusSubscriber.Bus.MOD)
public class LandTypes
{
	public static IForgeRegistry<TerrainLandType> TERRAIN_REGISTRY;
	public static IForgeRegistry<TitleLandType> TITLE_REGISTRY;
	
	@ObjectHolder(Minestuck.MOD_ID+":null")
	public static final TerrainLandType TERRAIN_NULL = getNull();
	public static final TerrainLandType FOREST = getNull();
	public static final TerrainLandType TAIGA = getNull();
	public static final TerrainLandType FROST = getNull();
	public static final TerrainLandType FUNGI = getNull();
	public static final TerrainLandType HEAT = getNull();
	public static final TerrainLandType ROCK = getNull();
	public static final TerrainLandType PETRIFICATION = getNull();
	public static final TerrainLandType SAND = getNull();
	public static final TerrainLandType RED_SAND = getNull();
	public static final TerrainLandType LUSH_DESERTS = getNull();
	public static final TerrainLandType SANDSTONE = getNull();
	public static final TerrainLandType RED_SANDSTONE = getNull();
	public static final TerrainLandType SHADE = getNull();
	public static final TerrainLandType WOOD = getNull();
	public static final TerrainLandType RAINBOW = getNull();
	public static final TerrainLandType FLORA = getNull();
	public static final TerrainLandType END = getNull();
	public static final TerrainLandType RAIN = getNull();
	
	@ObjectHolder(Minestuck.MOD_ID+":null")
	public static final TitleLandType TITLE_NULL = getNull();
	public static final TitleLandType FROGS = getNull();
	public static final TitleLandType WIND = getNull();
	public static final TitleLandType LIGHT = getNull();
	public static final TitleLandType CLOCKWORK = getNull();
	public static final TitleLandType SILENCE = getNull();
	public static final TitleLandType THUNDER = getNull();
	public static final TitleLandType PULSE = getNull();
	public static final TitleLandType THOUGHT = getNull();
	public static final TitleLandType BUCKETS = getNull();
	public static final TitleLandType CAKE = getNull();
	public static final TitleLandType RABBITS = getNull();
	public static final TitleLandType MONSTERS = getNull();
	public static final TitleLandType UNDEAD = getNull();
	public static final TitleLandType TOWERS = getNull();
	
	@Nonnull
	@SuppressWarnings("ConstantConditions")
	private static <T> T getNull()
	{
		return null;
	}
	
	@SubscribeEvent
	public static void onRegistryNewRegistry(final NewRegistryEvent event)
	{
		event.create(new RegistryBuilder<TerrainLandType>()
						.setName(new ResourceLocation(Minestuck.MOD_ID, "terrain_land_type"))
						.setType(TerrainLandType.class),
				registry -> TERRAIN_REGISTRY = registry);
		event.create(new RegistryBuilder<TitleLandType>()
						.setName(new ResourceLocation(Minestuck.MOD_ID, "title_land_type"))
						.setType(TitleLandType.class),
				registry -> TITLE_REGISTRY = registry);
	}

	@SubscribeEvent
	public static void registerTerrain(final RegistryEvent.Register<TerrainLandType> event)
	{
		IForgeRegistry<TerrainLandType> registry = event.getRegistry();
		
		registry.register(new NullTerrainLandType().setRegistryName("null"));
		registry.register(ForestLandType.createForest().setRegistryName("forest"));
		registry.register(ForestLandType.createTaiga().setRegistryName("taiga"));
		registry.register(new FrostLandType().setRegistryName("frost"));
		registry.register(new FungiLandType().setRegistryName("fungi"));
		registry.register(new HeatLandType().setRegistryName("heat"));
		registry.register(RockLandType.createRock().setRegistryName("rock"));
		registry.register(RockLandType.createPetrification().setRegistryName("petrification"));
		registry.register(SandLandType.createSand().setRegistryName("sand"));
		registry.register(SandLandType.createRedSand().setRegistryName("red_sand"));
		registry.register(SandLandType.createLushDeserts().setRegistryName("lush_deserts"));
		registry.register(SandstoneLandType.createSandstone().setRegistryName("sandstone"));
		registry.register(SandstoneLandType.createRedSandstone().setRegistryName("red_sandstone"));
		registry.register(new ShadeLandType().setRegistryName("shade"));
		registry.register(new WoodLandType().setRegistryName("wood"));
		registry.register(new RainbowLandType().setRegistryName("rainbow"));
		registry.register(new FloraLandType().setRegistryName("flora"));
		registry.register(new EndLandType().setRegistryName("end"));
		registry.register(new RainLandType().setRegistryName("rain"));
	}
	
	@SubscribeEvent
	@SuppressWarnings("unused")
	public static void registerTitle(final RegistryEvent.Register<TitleLandType> event)
	{
		IForgeRegistry<TitleLandType> registry = event.getRegistry();
		
		registry.register(new NullTitleLandType().setRegistryName("null"));
		registry.register(new FrogsLandType().setRegistryName("frogs"));
		registry.register(new WindLandType().setRegistryName("wind"));
		registry.register(new LightLandType().setRegistryName("light"));
		registry.register(new ClockworkLandType().setRegistryName("clockwork"));
		registry.register(new SilenceLandType().setRegistryName("silence"));
		registry.register(new ThunderLandType().setRegistryName("thunder"));
		registry.register(new PulseLandType().setRegistryName("pulse"));
		registry.register(new ThoughtLandType().setRegistryName("thought"));
		registry.register(new BucketsLandType().setRegistryName("buckets"));
		registry.register(new CakeLandType().setRegistryName("cake"));
		registry.register(new RabbitsLandType().setRegistryName("rabbits"));
		registry.register(new MonstersLandType(MonstersLandType.Variant.MONSTERS).setRegistryName("monsters"));
		registry.register(new MonstersLandType(MonstersLandType.Variant.UNDEAD).setRegistryName("undead"));
		registry.register(new TowersLandType().setRegistryName("towers"));
	}
	
	public static Set<TitleLandType> getCompatibleTitleTypes(TerrainLandType terrain)
	{
		return TITLE_REGISTRY.getValues().stream().filter(landType -> landType.isAspectCompatible(terrain) && landType.canBePickedAtRandom()).collect(Collectors.toSet());
	}
}