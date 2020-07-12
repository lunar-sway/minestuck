package com.mraof.minestuck.world.lands;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.player.EnumAspect;
import com.mraof.minestuck.player.PlayerIdentifier;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.world.MSDimensionTypes;
import com.mraof.minestuck.world.lands.terrain.*;
import com.mraof.minestuck.world.lands.title.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.ResourceLocationException;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@ObjectHolder(Minestuck.MOD_ID)
@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus=Mod.EventBusSubscriber.Bus.MOD)
public class LandTypes
{
	private static final ResourceLocation TERRAIN_GROUP = new ResourceLocation(Minestuck.MOD_ID, "terrain_group_map");
	private static final ResourceLocation TITLE_GROUP = new ResourceLocation(Minestuck.MOD_ID, "title_group_map");
	
	public static IForgeRegistry<TerrainLandType> TERRAIN_REGISTRY;
	public static IForgeRegistry<TitleLandType> TITLE_REGISTRY;
	
	private static Map<ResourceLocation, List<TerrainLandType>> terrainGroupMap;
	private static Map<ResourceLocation, List<TitleLandType>> titleGroupMap;
	
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
	
	private Random random;
	
	@Nonnull
	@SuppressWarnings("ConstantConditions")
	private static <T> T getNull()
	{
		return null;
	}
	
	@SubscribeEvent	@SuppressWarnings("unchecked")
	public static void onRegistryNewRegistry(final RegistryEvent.NewRegistry event)
	{
		TERRAIN_REGISTRY = new RegistryBuilder<TerrainLandType>()
				.setName(new ResourceLocation(Minestuck.MOD_ID, "terrain_land_type"))
				.setType(TerrainLandType.class)
				.addCallback(TerrainCallbacks.INSTANCE)
				.create();
		terrainGroupMap = TERRAIN_REGISTRY.getSlaveMap(TERRAIN_GROUP, Map.class);
		TITLE_REGISTRY = new RegistryBuilder<TitleLandType>()
				.setName(new ResourceLocation(Minestuck.MOD_ID, "title_land_type"))
				.setType(TitleLandType.class)
				.addCallback(TitleCallbacks.INSTANCE)
				.create();
		titleGroupMap = TITLE_REGISTRY.getSlaveMap(TITLE_GROUP, Map.class);
	}

	@SubscribeEvent
	public static void registerTerrain(final RegistryEvent.Register<TerrainLandType> event)
	{
		IForgeRegistry<TerrainLandType> registry = event.getRegistry();
		
		registry.register(new NullTerrainLandType().setRegistryName("null"));
		registry.register(new ForestLandType(ForestLandType.Variant.FOREST).setRegistryName("forest"));
		registry.register(new ForestLandType(ForestLandType.Variant.TAIGA).setRegistryName("taiga"));
		registry.register(new FrostLandType().setRegistryName("frost"));
		registry.register(new FungiLandType().setRegistryName("fungi"));
		registry.register(new HeatLandType().setRegistryName("heat"));
		registry.register(new RockLandType(RockLandType.Variant.ROCK).setRegistryName("rock"));
		registry.register(new RockLandType(RockLandType.Variant.PETRIFICATION).setRegistryName("petrification"));
		registry.register(new SandLandType(SandLandType.Variant.SAND).setRegistryName("sand"));
		registry.register(new SandLandType(SandLandType.Variant.RED_SAND).setRegistryName("red_sand"));
		registry.register(new SandLandType(SandLandType.Variant.LUSH_DESERTS).setRegistryName("lush_deserts"));
		registry.register(new SandstoneLandType(SandstoneLandType.Variant.SANDSTONE).setRegistryName("sandstone"));
		registry.register(new SandstoneLandType(SandstoneLandType.Variant.RED_SANDSTONE).setRegistryName("red_sandstone"));
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
	
	public LandTypes(long seed)
	{
		random = new Random(seed);
	}
	
	/**
	 * Generates a random land aspect, weighted based on a player's title.
	 */
	public TerrainLandType getTerrainAspect(TitleLandType aspect2, List<TerrainLandType> usedAspects)
	{
		TerrainLandType aspect = selectRandomAspect(usedAspects, terrainGroupMap, aspect2::isAspectCompatible);
		if(aspect != null)
			return aspect;
		else
		{
			Debug.errorf("No land aspect is compatible with the title aspect %s! Defaulting to null land aspect.", aspect2.getRegistryName());
			return TERRAIN_NULL;
		}
	}
	
	public TitleLandType getTitleAspect(TerrainLandType aspectTerrain, EnumAspect titleAspect, List<TitleLandType> usedAspects)
	{
		TitleLandType landAspect;
		if(aspectTerrain != null)
		{
			landAspect = selectRandomAspect(usedAspects, titleGroupMap, aspect -> aspect.getAspect() == titleAspect && aspect.isAspectCompatible(aspectTerrain));
		} else landAspect = selectRandomAspect(usedAspects, titleGroupMap, aspect -> aspect.getAspect() == titleAspect);
		
		if(landAspect != null)
			return landAspect;
		else return TITLE_NULL;
	}
	
	private <A extends ILandType<?>> A selectRandomAspect(List<A> usedAspects, Map<ResourceLocation, List<A>> groupMap, Predicate<A> condition)
	{
		List<List<A>> list = Lists.newArrayList();
		for(List<A> aspects : groupMap.values())
		{
			List<A> variantList = Lists.newArrayList(aspects);
			variantList.removeIf(condition.negate());
			if(!variantList.isEmpty())
				list.add(variantList);
		}
		
		List<A> groupList = pickOneFromUsage(list, usedAspects, (variants, used) -> variants.get(0).getGroup().equals(used.getGroup()));
		if(groupList == null)
			return null;
		return pickOneFromUsage(groupList, usedAspects, Object::equals);
	}
	
	private <A extends ILandType<?>, B> B pickOneFromUsage(List<B> list, List<A> usedAspects, BiPredicate<B, A> matchPredicate)
	{
		if(list.isEmpty())
			return null;
		else if(list.size() == 1)
			return list.get(0);
		else
		{
			int[] useCount = new int[list.size()];
			for(A usedAspect : usedAspects)
			{
				for(int i = 0; i < list.size(); i++)
					if(matchPredicate.test(list.get(i), usedAspect))
						useCount[i]++;
			}
			
			ArrayList<B> unusedEntries = new ArrayList<>();
			for(int i = 0; i < list.size(); i++)	//Check for unused aspects
				if(useCount[i] == 0)
					unusedEntries.add(list.get(i));
			
			if(unusedEntries.size() > 0)
				return unusedEntries.get(random.nextInt(unusedEntries.size()));
			
			double randCap = 0;
			for(int value : useCount) randCap += 1D / value;
			
			double rand = random.nextDouble()*randCap;
			
			for(int i = 0; i < useCount.length; i++)
				if(rand < 1D/useCount[i])
				{
					return list.get(i);
				}
				else rand -= 1D/useCount[i];
			
			throw new IllegalStateException("This should not happen!");
		}
	}
	
	/**
	 * Registers a new dimension for a land. Returns the type of the new land.
	 * @param player The player whose Land is being created
	 * @param aspects Land aspects that the land should have
	 * @return Returns the dimension of the newly created land.
	 */
	public static DimensionType createLandType(MinecraftServer server, PlayerIdentifier player, LandTypePair aspects)
	{
		String base = "minestuck:land_"+player.getUsername().toLowerCase();
		ResourceLocation dimensionName;
		try
		{
			dimensionName = new ResourceLocation(base);
		} catch(ResourceLocationException e)
		{
			base = "minestuck:land";
			dimensionName = new ResourceLocation(base);
		}
		
		for(int i = 0; DimensionType.byName(dimensionName) != null; i++) {
			dimensionName = new ResourceLocation(base+"_"+i);
		}
		
		return DimensionManager.registerDimension(dimensionName, MSDimensionTypes.LANDS, null, true);
	}
	
	public static Set<TitleLandType> getCompatibleTitleTypes(TerrainLandType terrain)
	{
		return TITLE_REGISTRY.getValues().stream().filter(landType -> landType.isAspectCompatible(terrain) && landType.canBePickedAtRandom()).collect(Collectors.toSet());
	}
	
	private static class TerrainCallbacks implements IForgeRegistry.AddCallback<TerrainLandType>, IForgeRegistry.ClearCallback<TerrainLandType>, IForgeRegistry.CreateCallback<TerrainLandType>
	{
		private static final TerrainCallbacks INSTANCE = new TerrainCallbacks();
		@Override
		public void onAdd(IForgeRegistryInternal<TerrainLandType> owner, RegistryManager stage, int id, TerrainLandType obj, @Nullable TerrainLandType oldObj)
		{
			if(oldObj != null)
			{
				@SuppressWarnings("unchecked")
				Map<ResourceLocation, List<TerrainLandType>> terrainGroupMap = owner.getSlaveMap(TERRAIN_GROUP, Map.class);
				terrainGroupMap.getOrDefault(oldObj.getGroup(), Collections.emptyList()).remove(oldObj);
				if(terrainGroupMap.containsKey(oldObj.getGroup()) && terrainGroupMap.get(oldObj.getGroup()).isEmpty())
					terrainGroupMap.remove(oldObj.getGroup());
			}
			
			if(obj.canBePickedAtRandom())
			{
				@SuppressWarnings("unchecked")
				Map<ResourceLocation, List<TerrainLandType>> terrainGroupMap = owner.getSlaveMap(TERRAIN_GROUP, Map.class);
				terrainGroupMap.computeIfAbsent(obj.getGroup(), terrainLandAspect -> Lists.newArrayList()).add(obj);
			}
		}
		
		@Override
		public void onClear(IForgeRegistryInternal<TerrainLandType> owner, RegistryManager stage)
		{
			owner.getSlaveMap(TERRAIN_GROUP, Map.class).clear();
		}
		
		@Override
		public void onCreate(IForgeRegistryInternal<TerrainLandType> owner, RegistryManager stage)
		{
			owner.setSlaveMap(TERRAIN_GROUP, Maps.newHashMap());
		}
	}
	
	private static class TitleCallbacks implements IForgeRegistry.AddCallback<TitleLandType>, IForgeRegistry.ClearCallback<TitleLandType>, IForgeRegistry.CreateCallback<TitleLandType>
	{
		private static final TitleCallbacks INSTANCE = new TitleCallbacks();
		
		@Override
		public void onAdd(IForgeRegistryInternal<TitleLandType> owner, RegistryManager stage, int id, TitleLandType obj, @Nullable TitleLandType oldObj)
		{
			if(oldObj != null)
			{
				@SuppressWarnings("unchecked")
				Map<ResourceLocation, List<TitleLandType>> titleGroupMap = owner.getSlaveMap(TITLE_GROUP, Map.class);
				titleGroupMap.getOrDefault(oldObj.getGroup(), Collections.emptyList()).remove(oldObj);
				if(titleGroupMap.containsKey(oldObj.getGroup()) && titleGroupMap.get(oldObj.getGroup()).isEmpty())
					titleGroupMap.remove(oldObj.getGroup());
			}
			
			if(obj.canBePickedAtRandom())
			{
				@SuppressWarnings("unchecked")
				Map<ResourceLocation, List<TitleLandType>> titleGroupMap = owner.getSlaveMap(TITLE_GROUP, Map.class);
				titleGroupMap.computeIfAbsent(obj.getGroup(), titleLandAspect -> Lists.newArrayList()).add(obj);
			}
		}
		
		@Override
		public void onClear(IForgeRegistryInternal<TitleLandType> owner, RegistryManager stage)
		{
			owner.getSlaveMap(TITLE_GROUP, Map.class).clear();
		}
		
		@Override
		public void onCreate(IForgeRegistryInternal<TitleLandType> owner, RegistryManager stage)
		{
			owner.setSlaveMap(TITLE_GROUP, Maps.newHashMap());
		}
	}
}