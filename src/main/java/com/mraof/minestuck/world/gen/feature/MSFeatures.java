package com.mraof.minestuck.world.gen.feature;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.world.gen.feature.tree.EndTreeFeature;
import com.mraof.minestuck.world.gen.feature.tree.LeaflessTreeFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.DiskConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.ProbabilityFeatureConfiguration;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class MSFeatures
{
	public static final DeferredRegister<Feature<?>> REGISTER = DeferredRegister.create(ForgeRegistries.FEATURES, Minestuck.MOD_ID);
	
	public static final RegistryObject<Feature<NoneFeatureConfiguration>> RETURN_NODE = REGISTER.register("return_node", () -> new ReturnNodeFeature(NoneFeatureConfiguration.CODEC));
	
	public static final RegistryObject<Feature<NoneFeatureConfiguration>> SURFACE_FOSSIL = REGISTER.register("surface_fossil", () -> new SurfaceFossilsFeature(NoneFeatureConfiguration.CODEC));
	public static final RegistryObject<Feature<NoneFeatureConfiguration>> BROKEN_SWORD = REGISTER.register("broken_sword", () -> new BrokenSwordFeature(NoneFeatureConfiguration.CODEC));
	public static final RegistryObject<Feature<NoneFeatureConfiguration>> BUCKET = REGISTER.register("bucket", () -> new BucketFeature(NoneFeatureConfiguration.CODEC));
	public static final RegistryObject<Feature<NoneFeatureConfiguration>> CAKE_PEDESTAL = REGISTER.register("cake_pedestal", () -> new CakePedestalFeature(NoneFeatureConfiguration.CODEC));
	public static final RegistryObject<Feature<NoneFeatureConfiguration>> COG = REGISTER.register("cog", () -> new CogFeature(NoneFeatureConfiguration.CODEC));
	public static final RegistryObject<Feature<NoneFeatureConfiguration>> FLOOR_COG = REGISTER.register("floor_cog", () -> new FloorCogFeature(NoneFeatureConfiguration.CODEC));
	public static final RegistryObject<Feature<NoneFeatureConfiguration>> SMALL_LIBRARY = REGISTER.register("small_library", () -> new SmallLibraryFeature(NoneFeatureConfiguration.CODEC));
	public static final RegistryObject<Feature<NoneFeatureConfiguration>> TOWER = REGISTER.register("tower", () -> new TowerFeature(NoneFeatureConfiguration.CODEC));
	public static final RegistryObject<Feature<NoneFeatureConfiguration>> PARCEL_PYXIS = REGISTER.register("parcel_pyxis", () -> new ParcelPyxisFeature(NoneFeatureConfiguration.CODEC));
	
	public static final RegistryObject<Feature<NoneFeatureConfiguration>> OASIS = REGISTER.register("oasis", () -> new OasisFeature(NoneFeatureConfiguration.CODEC));
	public static final RegistryObject<Feature<NoneFeatureConfiguration>> OCEAN_RUNDOWN = REGISTER.register("ocean_rundown", () -> new OceanRundownFeature(NoneFeatureConfiguration.CODEC));
	
	public static final RegistryObject<Feature<NoneFeatureConfiguration>> FIRE_FIELD = REGISTER.register("fire_field", () -> new FireFieldFeature(NoneFeatureConfiguration.CODEC));
	public static final RegistryObject<Feature<DiskConfiguration>> SURFACE_DISK = REGISTER.register("surface_disk", () -> new SurfaceDiskFeature(DiskConfiguration.CODEC, false));
	public static final RegistryObject<Feature<DiskConfiguration>> GRASSY_SURFACE_DISK = REGISTER.register("grassy_surface_disk", () -> new SurfaceDiskFeature(DiskConfiguration.CODEC, true));
	
	
	public static final RegistryObject<Feature<NoneFeatureConfiguration>> MESA = REGISTER.register("mesa", () -> new MesaFeature(NoneFeatureConfiguration.CODEC));
	public static final RegistryObject<Feature<NoneFeatureConfiguration>> ROCK_SPIKE = REGISTER.register("rock_spike", () -> new RockSpikeFeature(NoneFeatureConfiguration.CODEC));
	public static final RegistryObject<Feature<BlockStateConfiguration>> STONE_MOUND = REGISTER.register("stone_mound", () -> new StoneMoundFeature(BlockStateConfiguration.CODEC));
	public static final RegistryObject<Feature<BlockStateConfiguration>> BLOCK_BLOB = REGISTER.register("block_blob", () -> new ConditionFreeBlobFeature(BlockStateConfiguration.CODEC));
	public static final RegistryObject<Feature<RandomRockBlockBlobConfig>> RANDOM_ROCK_BLOCK_BLOB = REGISTER.register("random_rock_block_blob", () -> new RandomRockConditionFreeBlobFeature(RandomRockBlockBlobConfig.CODEC));
	public static final RegistryObject<Feature<BlockStateConfiguration>> PILLAR = REGISTER.register("pillar", () -> new PillarFeature(BlockStateConfiguration.CODEC, false));
	public static final RegistryObject<Feature<BlockStateConfiguration>> LARGE_PILLAR = REGISTER.register("large_pillar", () -> new PillarFeature(BlockStateConfiguration.CODEC, true));
	
	public static final RegistryObject<Feature<NoneFeatureConfiguration>> END_TREE = REGISTER.register("end_tree", () -> new EndTreeFeature(NoneFeatureConfiguration.CODEC));
	public static final RegistryObject<Feature<BlockStateConfiguration>> LEAFLESS_TREE = REGISTER.register("leafless_tree", () -> new LeaflessTreeFeature(BlockStateConfiguration.CODEC));
	
	public static final RegistryObject<Feature<ProbabilityFeatureConfiguration>> CAKE = REGISTER.register("cake", () -> new CakeFeature(ProbabilityFeatureConfiguration.CODEC));
	
	public static final RegistryObject<Feature<NoneFeatureConfiguration>> RABBIT_PLACEMENT = REGISTER.register("rabbit_placement", () -> new RabbitPlacementFeature(NoneFeatureConfiguration.CODEC));
	
}