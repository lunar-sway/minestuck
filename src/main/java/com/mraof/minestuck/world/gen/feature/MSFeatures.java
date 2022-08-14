package com.mraof.minestuck.world.gen.feature;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.world.gen.feature.structure.FrogTempleStructure;
import com.mraof.minestuck.world.gen.feature.structure.GateStructure;
import com.mraof.minestuck.world.gen.feature.structure.ImpDungeonStructure;
import com.mraof.minestuck.world.gen.feature.structure.SmallRuinStructure;
import com.mraof.minestuck.world.gen.feature.structure.castle.CastleStructure;
import com.mraof.minestuck.world.gen.feature.structure.village.ConsortVillageStructure;
import com.mraof.minestuck.world.gen.feature.tree.EndTreeFeature;
import com.mraof.minestuck.world.gen.feature.tree.LeaflessTreeFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.DiskConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.ProbabilityFeatureConfiguration;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

import javax.annotation.Nonnull;

@ObjectHolder(Minestuck.MOD_ID)
@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class MSFeatures
{
	public static final GateStructure LAND_GATE = getNull();
	public static final StructureFeature<NoneFeatureConfiguration> FROG_TEMPLE = getNull();
	public static final StructureFeature<NoneFeatureConfiguration> SMALL_RUIN = getNull();
	public static final StructureFeature<NoneFeatureConfiguration> IMP_DUNGEON = getNull();
	public static final StructureFeature<NoneFeatureConfiguration> CONSORT_VILLAGE = getNull();
	public static final StructureFeature<NoneFeatureConfiguration> SKAIA_CASTLE = getNull();
	
	public static final Feature<NoneFeatureConfiguration> END_TREE = getNull();
	public static final Feature<BlockStateConfiguration> LEAFLESS_TREE = getNull();
	
	public static final Feature<NoneFeatureConfiguration> RETURN_NODE = getNull();
	public static final Feature<NoneFeatureConfiguration> FIRE_FIELD = getNull();
	public static final Feature<ProbabilityFeatureConfiguration> CAKE = getNull();
	public static final Feature<BlockStateConfiguration> PILLAR = getNull();
	public static final Feature<BlockStateConfiguration> LARGE_PILLAR = getNull();
	public static final Feature<BlockStateConfiguration> BLOCK_BLOB = getNull();
	public static final Feature<RandomRockBlockBlobConfig> RANDOM_ROCK_BLOCK_BLOB = getNull();
	public static final Feature<DiskConfiguration> SURFACE_DISK = getNull();
	public static final Feature<DiskConfiguration> GRASSY_SURFACE_DISK = getNull();
	public static final Feature<NoneFeatureConfiguration> OCEAN_RUNDOWN = getNull();
	public static final Feature<NoneFeatureConfiguration> RABBIT_PLACEMENT = getNull();
	
	public static final Feature<NoneFeatureConfiguration> SMALL_LIBRARY = getNull();
	public static final Feature<NoneFeatureConfiguration> CAKE_PEDESTAL = getNull();
	public static final Feature<NoneFeatureConfiguration> COG = getNull();
	public static final Feature<NoneFeatureConfiguration> FLOOR_COG = getNull();
	public static final Feature<NoneFeatureConfiguration> OASIS = getNull();
	public static final Feature<NoneFeatureConfiguration> MESA = getNull();
	public static final Feature<NoneFeatureConfiguration> ROCK_SPIKE = getNull();
	public static final Feature<NoneFeatureConfiguration> PARCEL_PYXIS = getNull();
	public static final Feature<NoneFeatureConfiguration> SURFACE_FOSSIL = getNull();
	public static final Feature<NoneFeatureConfiguration> BUCKET = getNull();
	public static final Feature<NoneFeatureConfiguration> BROKEN_SWORD = getNull();
	public static final Feature<NoneFeatureConfiguration> TOWER = getNull();
	public static final Feature<BlockStateConfiguration> STONE_MOUND = getNull();
	
	@Nonnull
	@SuppressWarnings("ConstantConditions")
	private static <T> T getNull()
	{
		return null;
	}
	
	@SubscribeEvent
	public static void registerStructures(RegistryEvent.Register<StructureFeature<?>> event)
	{
		IForgeRegistry<StructureFeature<?>> registry = event.getRegistry();
		
		register(registry, new GateStructure(NoneFeatureConfiguration.CODEC).setRegistryName("land_gate"));
		register(registry, new FrogTempleStructure(NoneFeatureConfiguration.CODEC).setRegistryName("frog_temple"));
		register(registry, new SmallRuinStructure(NoneFeatureConfiguration.CODEC).setRegistryName("small_ruin"));
		register(registry, new ImpDungeonStructure(NoneFeatureConfiguration.CODEC).setRegistryName("imp_dungeon"));
		register(registry, new ConsortVillageStructure(NoneFeatureConfiguration.CODEC).setRegistryName("consort_village"));
		register(registry, new CastleStructure(NoneFeatureConfiguration.CODEC).setRegistryName("skaia_castle"));
	}
	
	private static void register(IForgeRegistry<StructureFeature<?>> registry, StructureFeature<?> structure)
	{
		registry.register(structure);
	}
	
	@SubscribeEvent
	public static void registerFeatures(RegistryEvent.Register<Feature<?>> event)
	{
		IForgeRegistry<Feature<?>> registry = event.getRegistry();
		
		registry.register(new EndTreeFeature(NoneFeatureConfiguration.CODEC).setRegistryName("end_tree"));
		registry.register(new LeaflessTreeFeature(BlockStateConfiguration.CODEC).setRegistryName("leafless_tree"));
		
		registry.register(new ReturnNodeFeature(NoneFeatureConfiguration.CODEC).setRegistryName("return_node"));
		registry.register(new FireFieldFeature(NoneFeatureConfiguration.CODEC).setRegistryName("fire_field"));
		registry.register(new CakeFeature(ProbabilityFeatureConfiguration.CODEC).setRegistryName("cake"));
		registry.register(new PillarFeature(BlockStateConfiguration.CODEC, false).setRegistryName("pillar"));
		registry.register(new PillarFeature(BlockStateConfiguration.CODEC, true).setRegistryName("large_pillar"));
		registry.register(new ConditionFreeBlobFeature(BlockStateConfiguration.CODEC).setRegistryName("block_blob"));
		registry.register(new RandomRockConditionFreeBlobFeature(RandomRockBlockBlobConfig.CODEC).setRegistryName("random_rock_block_blob"));
		registry.register(new SurfaceDiskFeature(DiskConfiguration.CODEC, false).setRegistryName("surface_disk"));
		registry.register(new SurfaceDiskFeature(DiskConfiguration.CODEC, true).setRegistryName("grassy_surface_disk"));
		registry.register(new OceanRundownFeature(NoneFeatureConfiguration.CODEC).setRegistryName("ocean_rundown"));
		registry.register(new RabbitPlacementFeature(NoneFeatureConfiguration.CODEC).setRegistryName("rabbit_placement"));
		
		registry.register(new SmallLibraryFeature(NoneFeatureConfiguration.CODEC).setRegistryName("small_library"));
		registry.register(new CakePedestalFeature(NoneFeatureConfiguration.CODEC).setRegistryName("cake_pedestal"));
		registry.register(new CogFeature(NoneFeatureConfiguration.CODEC).setRegistryName("cog"));
		registry.register(new FloorCogFeature(NoneFeatureConfiguration.CODEC).setRegistryName("floor_cog"));
		registry.register(new OasisFeature(NoneFeatureConfiguration.CODEC).setRegistryName("oasis"));
		registry.register(new MesaFeature(NoneFeatureConfiguration.CODEC).setRegistryName("mesa"));
		registry.register(new RockSpikeFeature(NoneFeatureConfiguration.CODEC).setRegistryName("rock_spike"));
		registry.register(new ParcelPyxisFeature(NoneFeatureConfiguration.CODEC).setRegistryName("parcel_pyxis"));
		registry.register(new SurfaceFossilsFeature(NoneFeatureConfiguration.CODEC).setRegistryName("surface_fossil"));
		registry.register(new BucketFeature(NoneFeatureConfiguration.CODEC).setRegistryName("bucket"));
		registry.register(new BrokenSwordFeature(NoneFeatureConfiguration.CODEC).setRegistryName("broken_sword"));
		registry.register(new TowerFeature(NoneFeatureConfiguration.CODEC).setRegistryName("tower"));
		registry.register(new StoneMoundFeature(BlockStateConfiguration.CODEC).setRegistryName("stone_mound"));
		
	}
}