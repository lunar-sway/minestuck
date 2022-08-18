package com.mraof.minestuck.world.gen.feature.structure;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.util.MSTags;
import com.mraof.minestuck.world.gen.feature.MSFeatures;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public final class MSConfiguredStructures
{
	public static final DeferredRegister<ConfiguredStructureFeature<?, ?>> REGISTER = DeferredRegister.create(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY, Minestuck.MOD_ID);
	
	public static final RegistryObject<ConfiguredStructureFeature<?, ?>> LAND_GATE = REGISTER.register("land_gate", () -> MSStructures.LAND_GATE.get().configured(FeatureConfiguration.NONE, MSTags.Biomes.HAS_LAND_GATE));
	public static final RegistryObject<ConfiguredStructureFeature<?, ?>> FROG_TEMPLE = REGISTER.register("frog_temple", () -> MSStructures.FROG_TEMPLE.get().configured(FeatureConfiguration.NONE, MSTags.Biomes.HAS_FROG_TEMPLE));
	public static final RegistryObject<ConfiguredStructureFeature<?, ?>> SMALL_RUIN = REGISTER.register("small_ruin", () -> MSStructures.SMALL_RUIN.get().configured(FeatureConfiguration.NONE, MSTags.Biomes.HAS_SMALL_RUIN));
	public static final RegistryObject<ConfiguredStructureFeature<?, ?>> IMP_DUNGEON = REGISTER.register("imp_dungeon", () -> MSStructures.IMP_DUNGEON.get().configured(FeatureConfiguration.NONE, MSTags.Biomes.HAS_IMP_DUNGEON));
	public static final RegistryObject<ConfiguredStructureFeature<?, ?>> CONSORT_VILLAGE = REGISTER.register("consort_village", () -> MSStructures.CONSORT_VILLAGE.get().configured(FeatureConfiguration.NONE, MSTags.Biomes.HAS_CONSORT_VILLAGE));
	public static final RegistryObject<ConfiguredStructureFeature<?, ?>> SKAIA_CASTLE = REGISTER.register("skaia_castle", () -> MSStructures.SKAIA_CASTLE.get().configured(FeatureConfiguration.NONE, MSTags.Biomes.HAS_SKAIA_CASTLE));
}