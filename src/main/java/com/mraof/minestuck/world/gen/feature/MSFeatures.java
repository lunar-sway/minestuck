package com.mraof.minestuck.world.gen.feature;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.world.gen.feature.tree.EndTreeFeature;
import com.mraof.minestuck.world.gen.feature.tree.LeaflessTreeFeature;
import com.mraof.minestuck.world.gen.feature.tree.OrnateShadewoodTreeFeature;
import com.mraof.minestuck.world.gen.feature.tree.TreeStumpFeature;
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
	
	public static final RegistryObject<Feature<SimpleTemplateFeature.Config>> SIMPLE_TEMPLATE = REGISTER.register("simple_template", () -> new SimpleTemplateFeature(SimpleTemplateFeature.Config.CODEC));
	
	public static final RegistryObject<Feature<NoneFeatureConfiguration>> FLOOR_COG = REGISTER.register("floor_cog", () -> new FloorCogFeature(NoneFeatureConfiguration.CODEC));
	
	public static final RegistryObject<Feature<NoneFeatureConfiguration>> SURFACE_FOSSIL = REGISTER.register("surface_fossil", () -> new SurfaceFossilsFeature(NoneFeatureConfiguration.CODEC));
	public static final RegistryObject<Feature<NoneFeatureConfiguration>> BROKEN_SWORD = REGISTER.register("broken_sword", () -> new BrokenSwordFeature(NoneFeatureConfiguration.CODEC));
	public static final RegistryObject<Feature<NoneFeatureConfiguration>> BUCKET = REGISTER.register("bucket", () -> new BucketFeature(NoneFeatureConfiguration.CODEC));
	public static final RegistryObject<Feature<NoneFeatureConfiguration>> SMALL_LIBRARY = REGISTER.register("small_library", () -> new SmallLibraryFeature(NoneFeatureConfiguration.CODEC));
	public static final RegistryObject<Feature<NoneFeatureConfiguration>> TOWER = REGISTER.register("tower", () -> new TowerFeature(NoneFeatureConfiguration.CODEC));
	public static final RegistryObject<Feature<NoneFeatureConfiguration>> PARCEL_PYXIS = REGISTER.register("parcel_pyxis", () -> new ParcelPyxisFeature(NoneFeatureConfiguration.CODEC));
	public static final RegistryObject<Feature<NoneFeatureConfiguration>> FROG_RUINS = REGISTER.register("frog_ruins", () -> new FrogRuinFeature(NoneFeatureConfiguration.CODEC));
	public static final RegistryObject<Feature<NoneFeatureConfiguration>> CARVED_CHERRY_TREE = REGISTER.register("carved_cherry_tree", () -> new CarvedCherryTreeFeature(NoneFeatureConfiguration.CODEC));
	public static final RegistryObject<Feature<NoneFeatureConfiguration>> CARVED_HOUSE = REGISTER.register("carved_house", () -> new CarvedHouseFeature(NoneFeatureConfiguration.CODEC));
	public static final RegistryObject<Feature<NoneFeatureConfiguration>> CARVED_LOG = REGISTER.register("carved_log", () -> new CarvedLogFeature(NoneFeatureConfiguration.CODEC));
	public static final RegistryObject<Feature<NoneFeatureConfiguration>> LARGE_CARVED_LOG = REGISTER.register("large_carved_log", () -> new LargeCarvedLogFeature(NoneFeatureConfiguration.CODEC));
	public static final RegistryObject<Feature<NoneFeatureConfiguration>> TREATED_CHAIR = REGISTER.register("treated_chair", () -> new TreatedChairFeature(NoneFeatureConfiguration.CODEC));
	public static final RegistryObject<Feature<NoneFeatureConfiguration>> TREATED_TABLE = REGISTER.register("treated_table", () -> new TreatedTableFeature(NoneFeatureConfiguration.CODEC));
	public static final RegistryObject<Feature<NoneFeatureConfiguration>> UNFINISHED_CARVED_BOOKSHELF = REGISTER.register("unfinished_carved_bookshelf", () -> new UnfinishedCarvedBookshelfFeature(NoneFeatureConfiguration.CODEC));
	public static final RegistryObject<Feature<NoneFeatureConfiguration>> UNFINISHED_CARVED_CHAIR = REGISTER.register("unfinished_carved_chair", () -> new UnfinishedCarvedChairFeature(NoneFeatureConfiguration.CODEC));
	public static final RegistryObject<Feature<NoneFeatureConfiguration>> UNFINISHED_CARVED_CREEPER = REGISTER.register("unfinished_carved_creeper", () -> new UnfinishedCarvedCreeperFeature(NoneFeatureConfiguration.CODEC));
	public static final RegistryObject<Feature<NoneFeatureConfiguration>> UNFINISHED_CARVED_DRAWER = REGISTER.register("unfinished_carved_drawer", () -> new UnfinishedCarvedDrawerFeature(NoneFeatureConfiguration.CODEC));
	public static final RegistryObject<Feature<NoneFeatureConfiguration>> UNFINISHED_CARVED_SALAMANDER = REGISTER.register("unfinished_carved_salamander", () -> new UnfinishedCarvedSalamanderFeature(NoneFeatureConfiguration.CODEC));
	public static final RegistryObject<Feature<NoneFeatureConfiguration>> UNFINISHED_CARVED_TABLE = REGISTER.register("unfinished_carved_table", () -> new UnfinishedCarvedTableFeature(NoneFeatureConfiguration.CODEC));
	public static final RegistryObject<Feature<NoneFeatureConfiguration>> UNFINISHED_CARVED_TREE = REGISTER.register("unfinished_carved_tree", () -> new UnfinishedCarvedTreeFeature(NoneFeatureConfiguration.CODEC));
	public static final RegistryObject<Feature<NoneFeatureConfiguration>> UNFINISHED_CARVED_TUNNEL = REGISTER.register("unfinished_carved_tunnel", () -> new UnfinishedCarvedTunnelFeature(NoneFeatureConfiguration.CODEC));
	public static final RegistryObject<Feature<NoneFeatureConfiguration>> WOOD_SHAVINGS_PILE = REGISTER.register("wood_shavings_pile", () -> new WoodShavingsPileFeature(NoneFeatureConfiguration.CODEC));
	public static final RegistryObject<Feature<NoneFeatureConfiguration>> WOODEN_CACTUS_PAIR = REGISTER.register("wooden_cactus_pair", () -> new WoodenCactusPairFeature(NoneFeatureConfiguration.CODEC));
	public static final RegistryObject<Feature<NoneFeatureConfiguration>> MASSIVE_CHAIR = REGISTER.register("massive_chair", () -> new MassiveChairFeature(NoneFeatureConfiguration.CODEC));
	public static final RegistryObject<Feature<NoneFeatureConfiguration>> MASSIVE_STOOL = REGISTER.register("massive_stool", () -> new MassiveStoolFeature(NoneFeatureConfiguration.CODEC));
	
	public static final RegistryObject<Feature<NoneFeatureConfiguration>> OASIS = REGISTER.register("oasis", () -> new OasisFeature(NoneFeatureConfiguration.CODEC));
	public static final RegistryObject<Feature<NoneFeatureConfiguration>> OCEAN_RUNDOWN = REGISTER.register("ocean_rundown", () -> new OceanRundownFeature(NoneFeatureConfiguration.CODEC));
	
	public static final RegistryObject<Feature<NoneFeatureConfiguration>> FIRE_FIELD = REGISTER.register("fire_field", () -> new FireFieldFeature(NoneFeatureConfiguration.CODEC));
	public static final RegistryObject<Feature<DiskConfiguration>> DISK = REGISTER.register("disk", () -> new MSDiskFeature(DiskConfiguration.CODEC, false));
	public static final RegistryObject<Feature<DiskConfiguration>> GRASSY_SURFACE_DISK = REGISTER.register("grassy_surface_disk", () -> new MSDiskFeature(DiskConfiguration.CODEC, true));
	
	
	public static final RegistryObject<Feature<NoneFeatureConfiguration>> MESA = REGISTER.register("mesa", () -> new MesaFeature(NoneFeatureConfiguration.CODEC));
	public static final RegistryObject<Feature<NoneFeatureConfiguration>> ROCK_SPIKE = REGISTER.register("rock_spike", () -> new RockSpikeFeature(NoneFeatureConfiguration.CODEC));
	public static final RegistryObject<Feature<BlockStateConfiguration>> STONE_MOUND = REGISTER.register("stone_mound", () -> new StoneMoundFeature(BlockStateConfiguration.CODEC));
	public static final RegistryObject<Feature<BlockStateConfiguration>> BLOCK_BLOB = REGISTER.register("block_blob", () -> new ConditionFreeBlobFeature(BlockStateConfiguration.CODEC));
	public static final RegistryObject<Feature<RandomRockBlockBlobConfig>> RANDOM_ROCK_BLOCK_BLOB = REGISTER.register("random_rock_block_blob", () -> new RandomRockConditionFreeBlobFeature(RandomRockBlockBlobConfig.CODEC));
	public static final RegistryObject<Feature<BlockStateConfiguration>> SMALL_PILLAR = REGISTER.register("small_pillar", () -> new SmallPillarFeature(BlockStateConfiguration.CODEC));
	public static final RegistryObject<Feature<BlockStateConfiguration>> PILLAR = REGISTER.register("pillar", () -> new PillarFeature(BlockStateConfiguration.CODEC));
	
	public static final RegistryObject<Feature<NoneFeatureConfiguration>> END_TREE = REGISTER.register("end_tree", () -> new EndTreeFeature(NoneFeatureConfiguration.CODEC));
	public static final RegistryObject<Feature<BlockStateConfiguration>> LEAFLESS_TREE = REGISTER.register("leafless_tree", () -> new LeaflessTreeFeature(BlockStateConfiguration.CODEC));
	public static final RegistryObject<Feature<NoneFeatureConfiguration>> ORNATE_SHADEWOOD_TREE = REGISTER.register("ornate_shadewood_tree", () -> new OrnateShadewoodTreeFeature(NoneFeatureConfiguration.CODEC));
	public static final RegistryObject<Feature<NoneFeatureConfiguration>> TREE_STUMP = REGISTER.register("tree_stump", () -> new TreeStumpFeature(NoneFeatureConfiguration.CODEC));
	
	public static final RegistryObject<Feature<ProbabilityFeatureConfiguration>> CAKE = REGISTER.register("cake", () -> new CakeFeature(ProbabilityFeatureConfiguration.CODEC));
	public static final RegistryObject<Feature<NoneFeatureConfiguration>> LARGE_CAKE = REGISTER.register("large_cake", () -> new LargeCakeFeature(NoneFeatureConfiguration.CODEC));
	
	public static final RegistryObject<Feature<NoneFeatureConfiguration>> RABBIT_PLACEMENT = REGISTER.register("rabbit_placement", () -> new RabbitPlacementFeature(NoneFeatureConfiguration.CODEC));
	
}