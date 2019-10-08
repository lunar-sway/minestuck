package com.mraof.minestuck.world.gen.feature;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.MSBlocks;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

import javax.annotation.Nonnull;

@ObjectHolder(Minestuck.MOD_ID)
@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus=Mod.EventBusSubscriber.Bus.MOD)
public class MSFeatures
{
	public static final Structure<NoFeatureConfig> IMP_DUNGEON = getNull();	//TODO Rerun advancement provider once these two are reimplemeneted
	public static final Structure<NoFeatureConfig> CONSORT_VILLAGE = getNull();
	
	public static final Feature<NoFeatureConfig> RAINBOW_TREE = getNull();
	public static final Feature<NoFeatureConfig> END_TREE =	 getNull();
	public static final Feature<BushConfig> LEAFLESS_TREE = getNull();
	
	public static final Feature<NoFeatureConfig> FIRE_FIELD = getNull();
	public static final Feature<ProbabilityConfig> CAKE = getNull();
	public static final Feature<BushConfig> PILLAR = getNull();
	public static final Feature<BushConfig> LARGE_PILLAR = getNull();
	public static final Feature<BlockBlobConfig> BLOCK_BLOB = getNull();
	public static final Feature<NoFeatureConfig> STRAWBERRY = getNull();
	public static final Feature<NoFeatureConfig> OCEAN_RUNDOWN = getNull();
	
	public static final Feature<NoFeatureConfig> SMALL_LIBRARY = getNull();
	public static final Feature<NoFeatureConfig> CAKE_PEDESTAL = getNull();
	public static final Feature<NoFeatureConfig> COG = getNull();
	public static final Feature<NoFeatureConfig> FLOOR_COG = getNull();
	public static final Feature<NoFeatureConfig> OASIS = getNull();
	public static final Feature<NoFeatureConfig> MESA = getNull();
	public static final Feature<NoFeatureConfig> ROCK_SPIKE = getNull();
	
	@Nonnull
	@SuppressWarnings("ConstantConditions")
	private static <T> T getNull()
	{
		return null;
	}
	
	@SubscribeEvent
	public static void registerFeatures(RegistryEvent.Register<Feature<?>> event)
	{
		IForgeRegistry<Feature<?>> registry = event.getRegistry();
		
		registry.register(new RainbowTreeFeature(NoFeatureConfig::deserialize, false).setRegistryName("rainbow_tree"));
		registry.register(new EndTreeFeature(NoFeatureConfig::deserialize, false).setRegistryName("end_tree"));
		registry.register(new LeaflessTreeFeature(BushConfig::deserialize).setRegistryName("leafless_tree"));
		
		registry.register(new FireFieldFeature(NoFeatureConfig::deserialize).setRegistryName("fire_field"));
		registry.register(new CakeFeature(ProbabilityConfig::deserialize).setRegistryName("cake"));
		registry.register(new PillarFeature(BushConfig::deserialize, false).setRegistryName("pillar"));
		registry.register(new PillarFeature(BushConfig::deserialize, true).setRegistryName("large_pillar"));
		registry.register(new ConditionFreeBlobFeature(BlockBlobConfig::deserialize).setRegistryName("block_blob"));
		registry.register(new ScatteredPlantFeature(NoFeatureConfig::deserialize, MSBlocks.STRAWBERRY.getDefaultState()).setRegistryName("strawberry"));
		registry.register(new OceanRundownFeature(NoFeatureConfig::deserialize).setRegistryName("ocean_rundown"));
		
		registry.register(new SmallLibraryFeature(NoFeatureConfig::deserialize).setRegistryName("small_library"));
		registry.register(new CakePedestalFeature(NoFeatureConfig::deserialize).setRegistryName("cake_pedestal"));
		registry.register(new CogFeature(NoFeatureConfig::deserialize).setRegistryName("cog"));
		registry.register(new FloorCogFeature(NoFeatureConfig::deserialize).setRegistryName("floor_cog"));
		registry.register(new OasisFeature(NoFeatureConfig::deserialize).setRegistryName("oasis"));
		registry.register(new MesaFeature(NoFeatureConfig::deserialize).setRegistryName("mesa"));
		registry.register(new RockSpikeFeature(NoFeatureConfig::deserialize).setRegistryName("rock_spike"));
	}
}