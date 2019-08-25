package com.mraof.minestuck.world.lands;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.EnumAspect;
import com.mraof.minestuck.util.IdentifierHandler;
import com.mraof.minestuck.world.ModDimensionTypes;
import com.mraof.minestuck.world.lands.terrain.*;
import com.mraof.minestuck.world.lands.title.*;
import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketBuffer;
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

@ObjectHolder(Minestuck.MOD_ID)
@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus=Mod.EventBusSubscriber.Bus.MOD)
public class LandAspectRegistry
{
	private static final ResourceLocation TERRAIN_PARENT = new ResourceLocation(Minestuck.MOD_ID, "terrain_parent_map");
	private static final ResourceLocation TITLE_PARENT = new ResourceLocation(Minestuck.MOD_ID, "title_parent_map");
	
	public static IForgeRegistry<TerrainLandAspect> TERRAIN_REGISTRY;
	public static IForgeRegistry<TitleLandAspect> TITLE_REGISTRY;
	
	private static Map<TerrainLandAspect, List<TerrainLandAspect>> terrainParentMap;
	private static Map<TitleLandAspect, List<TitleLandAspect>> titleParentMap;
	
	public static final TerrainLandAspect FORESTS = getNull();
	public static final TerrainLandAspect FROST = getNull();
	public static final TerrainLandAspect FUNGI = getNull();
	public static final TerrainLandAspect HEAT = getNull();
	public static final TerrainLandAspect ROCK = getNull();
	public static final TerrainLandAspect SAND = getNull();
	public static final TerrainLandAspect RED_SAND = getNull();
	public static final TerrainLandAspect SANDSTONE = getNull();
	public static final TerrainLandAspect RED_SANDSTONE = getNull();
	public static final TerrainLandAspect SHADE = getNull();
	public static final TerrainLandAspect WOOD = getNull();
	public static final TerrainLandAspect RAINBOW = getNull();
	public static final TerrainLandAspect FLORA = getNull();
	public static final TerrainLandAspect END = getNull();
	public static final TerrainLandAspect RAIN = getNull();
	
	@ObjectHolder(Minestuck.MOD_ID+":null")
	public static final TitleLandAspect TITLE_NULL = getNull();
	public static final TitleLandAspect FROGS = getNull();
	public static final TitleLandAspect WIND = getNull();
	public static final TitleLandAspect LIGHT = getNull();
	public static final TitleLandAspect CLOCKWORK = getNull();
	public static final TitleLandAspect SILENCE = getNull();
	public static final TitleLandAspect THUNDER = getNull();
	public static final TitleLandAspect PULSE = getNull();
	public static final TitleLandAspect THOUGHT = getNull();
	public static final TitleLandAspect BUCKETS = getNull();
	public static final TitleLandAspect CAKE = getNull();
	public static final TitleLandAspect RABBITS = getNull();
	public static final TitleLandAspect MONSTERS = getNull();
	public static final TitleLandAspect TOWERS = getNull();
	
	private Random random;
	
	@Nonnull
	@SuppressWarnings("ConstantConditions")
	private static <T> T getNull()
	{
		return null;
	}
	
	@SubscribeEvent	@SuppressWarnings({"unused", "unchecked"})
	public static void onRegistryNewRegistry(final RegistryEvent.NewRegistry event)
	{
		TERRAIN_REGISTRY = new RegistryBuilder<TerrainLandAspect>()
				.setName(new ResourceLocation(Minestuck.MOD_ID, "terrain_land_aspect"))
				.setType(TerrainLandAspect.class)
				.addCallback(TerrainCallbacks.INSTANCE)
				.create();
		terrainParentMap = TERRAIN_REGISTRY.getSlaveMap(TERRAIN_PARENT, Map.class);
		TITLE_REGISTRY = new RegistryBuilder<TitleLandAspect>()
				.setName(new ResourceLocation(Minestuck.MOD_ID, "title_land_aspect"))
				.setType(TitleLandAspect.class)
				.addCallback(TitleCallbacks.INSTANCE)
				.create();
		titleParentMap = TITLE_REGISTRY.getSlaveMap(TITLE_PARENT, Map.class);
	}

	@SubscribeEvent
	@SuppressWarnings("unused")
	public static void registerTerrain(final RegistryEvent.Register<TerrainLandAspect> event)
	{
		IForgeRegistry<TerrainLandAspect> registry = event.getRegistry();
		
		registry.registerAll(ForestLandAspect.createTypes());
		registry.register(new FrostLandAspect().setRegistryName("frost"));
		registry.register(new FungiLandAspect().setRegistryName("fungi"));
		registry.register(new HeatLandAspect().setRegistryName("heat"));
		registry.registerAll(RockLandAspect.createTypes());
		registry.registerAll(SandLandAspect.createTypes());
		registry.registerAll(SandstoneLandAspect.createTypes());
		registry.register(new ShadeLandAspect().setRegistryName("shade"));
		registry.register(new WoodLandAspect().setRegistryName("wood"));
		registry.register(new RainbowLandAspect().setRegistryName("rainbow"));
		registry.register(new FloraLandAspect().setRegistryName("flora"));
		registry.register(new EndLandAspect().setRegistryName("end"));
		registry.register(new RainLandAspect().setRegistryName("rain"));
	}
	
	@SubscribeEvent
	@SuppressWarnings("unused")
	public static void registerTitle(final RegistryEvent.Register<TitleLandAspect> event)
	{
		IForgeRegistry<TitleLandAspect> registry = event.getRegistry();
		
		registry.register(new LandAspectNull().setRegistryName("null"));
		registry.register(new LandAspectFrogs().setRegistryName("frogs"));
		registry.register(new LandAspectWind().setRegistryName("wind"));
		registry.register(new LandAspectLight().setRegistryName("light"));
		registry.register(new LandAspectClockwork().setRegistryName("clockwork"));
		registry.register(new LandAspectSilence().setRegistryName("silence"));
		registry.register(new LandAspectThunder().setRegistryName("thunder"));
		registry.register(new LandAspectPulse().setRegistryName("pulse"));
		registry.register(new LandAspectThought().setRegistryName("thought"));
		registry.register(new LandAspectBuckets().setRegistryName("buckets"));
		registry.register(new LandAspectCake().setRegistryName("cake"));
		registry.register(new LandAspectRabbits().setRegistryName("rabbits"));
		registry.registerAll(LandAspectMonsters.createTypes());
		registry.register(new LandAspectTowers().setRegistryName("towers"));
	}
	
	public LandAspectRegistry(long seed)
	{
		random = new Random(seed);
	}
	
	/**
	 * Generates a random land aspect, weighted based on a player's title.
	 * @return
	 */
	public TerrainLandAspect getTerrainAspect(TitleLandAspect aspect2, List<TerrainLandAspect> usedAspects)
	{
		TerrainLandAspect aspect = selectRandomAspect(usedAspects, terrainParentMap, aspect2::isAspectCompatible);
		if(aspect != null)
			return aspect;
		else throw new IllegalStateException("No land aspect is compatible with the title aspect "+aspect2.getRegistryName()+"!");
	}
	
	public TitleLandAspect getTitleAspect(TerrainLandAspect aspectTerrain, EnumAspect titleAspect, List<TitleLandAspect> usedAspects)
	{
		TitleLandAspect landAspect;
		if(aspectTerrain != null)
		{
			landAspect = selectRandomAspect(usedAspects, titleParentMap, aspect -> aspect.getType() == titleAspect && aspect.isAspectCompatible(aspectTerrain));
			if(landAspect == null)
			{
				Debug.warnf("Failed to find a title land aspect compatible with land aspect \"%s\". Forced to use a poorly compatible land aspect instead.", aspectTerrain.getRegistryName());
				landAspect = selectRandomAspect(usedAspects, titleParentMap, aspect -> aspect.getType() == titleAspect);
			}
		} else landAspect = selectRandomAspect(usedAspects, titleParentMap, aspect -> aspect.getType() == titleAspect);
		
		if(landAspect != null)
			return landAspect;
		else return TITLE_NULL;
	}
	
	private <A extends ILandAspect> A selectRandomAspect(List<A> usedAspects, Map<A, List<A>> parentMap, Predicate<A> condition)
	{
		List<List<A>> list = Lists.newArrayList();
		for(A aspect : parentMap.getOrDefault(null, Collections.emptyList()))
		{
			List<A> variantList = Lists.newArrayList(parentMap.getOrDefault(aspect, Collections.emptyList()));
			variantList.removeIf(condition.negate());
			if(!variantList.isEmpty())
				list.add(variantList);
		}
		
		List<A> variantList = pickOneFromUsage(list, usedAspects, (variants, used) -> variants.get(0).getParentOrThis().equals(used.getParentOrThis()));
		if(variantList == null)
			return null;
		return pickOneFromUsage(variantList, usedAspects, Object::equals);
	}
	
	private <A extends ILandAspect, B> B pickOneFromUsage(List<B> list, List<A> usedAspects, BiPredicate<B, A> matchPredicate)
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
			
			ArrayList<B> unusedEntries = new ArrayList<B>();
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
	public static DimensionType createLand(MinecraftServer server, IdentifierHandler.PlayerIdentifier player, LandAspects aspects)
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
		
		PacketBuffer data = new PacketBuffer(Unpooled.buffer());
		data.clear();
		data.writeRegistryId(aspects.aspectTerrain);
		data.writeRegistryId(aspects.aspectTitle);
		
		return DimensionManager.registerDimension(dimensionName, ModDimensionTypes.LANDS, data, true);
	}
	
	private static class TerrainCallbacks implements IForgeRegistry.AddCallback<TerrainLandAspect>, IForgeRegistry.ClearCallback<TerrainLandAspect>, IForgeRegistry.CreateCallback<TerrainLandAspect>
	{
		private static final TerrainCallbacks INSTANCE = new TerrainCallbacks();
		@Override
		public void onAdd(IForgeRegistryInternal<TerrainLandAspect> owner, RegistryManager stage, int id, TerrainLandAspect obj, @Nullable TerrainLandAspect oldObj)
		{
			if(oldObj != null)
			{
				@SuppressWarnings("unchecked")
				Map<TerrainLandAspect, List<TerrainLandAspect>> terrainParentMap = owner.getSlaveMap(TERRAIN_PARENT, Map.class);
				terrainParentMap.getOrDefault(obj.getParent(), Collections.emptyList()).remove(oldObj);
				terrainParentMap.remove(oldObj);
			}
			
			if(obj.canBePickedAtRandom())
			{
				@SuppressWarnings("unchecked")
				Map<TerrainLandAspect, List<TerrainLandAspect>> terrainParentMap = owner.getSlaveMap(TERRAIN_PARENT, Map.class);
				terrainParentMap.computeIfAbsent(obj.getParent(), terrainLandAspect -> Lists.newArrayList()).add(obj);
				if(obj.getParent() == null)
					terrainParentMap.computeIfAbsent(obj, terrainLandAspect -> Lists.newArrayList()).add(obj);
			}
		}
		
		@Override
		public void onClear(IForgeRegistryInternal<TerrainLandAspect> owner, RegistryManager stage)
		{
			owner.getSlaveMap(TERRAIN_PARENT, Map.class).clear();
		}
		
		@Override
		public void onCreate(IForgeRegistryInternal<TerrainLandAspect> owner, RegistryManager stage)
		{
			owner.setSlaveMap(TERRAIN_PARENT, Maps.newHashMap());
		}
	}
	
	private static class TitleCallbacks implements IForgeRegistry.AddCallback<TitleLandAspect>, IForgeRegistry.ClearCallback<TitleLandAspect>, IForgeRegistry.CreateCallback<TitleLandAspect>
	{
		private static final TitleCallbacks INSTANCE = new TitleCallbacks();
		
		@Override
		public void onAdd(IForgeRegistryInternal<TitleLandAspect> owner, RegistryManager stage, int id, TitleLandAspect obj, @Nullable TitleLandAspect oldObj)
		{
			if(oldObj != null)
			{
				@SuppressWarnings("unchecked")
				Map<TitleLandAspect, List<TitleLandAspect>> titleParentMap = owner.getSlaveMap(TITLE_PARENT, Map.class);
				titleParentMap.getOrDefault(obj.getParent(), Collections.emptyList()).remove(oldObj);
				titleParentMap.remove(oldObj);
			}
			
			if(obj.canBePickedAtRandom())
			{
				@SuppressWarnings("unchecked")
				Map<TitleLandAspect, List<TitleLandAspect>> titleParentMap = owner.getSlaveMap(TITLE_PARENT, Map.class);
				titleParentMap.computeIfAbsent(obj.getParent(), titleLandAspect -> Lists.newArrayList()).add(obj);
				if(obj.getParent() == null)
					titleParentMap.computeIfAbsent(obj, titleLandAspect -> Lists.newArrayList()).add(obj);
			}
		}
		
		@Override
		public void onClear(IForgeRegistryInternal<TitleLandAspect> owner, RegistryManager stage)
		{
			owner.getSlaveMap(TITLE_PARENT, Map.class).clear();
		}
		
		@Override
		public void onCreate(IForgeRegistryInternal<TitleLandAspect> owner, RegistryManager stage)
		{
			owner.setSlaveMap(TITLE_PARENT, Maps.newHashMap());
		}
	}
}