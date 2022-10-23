package com.mraof.minestuck.world.gen.structure;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.world.gen.structure.castle.CastleStructure;
import com.mraof.minestuck.world.gen.structure.gate.GateStructure;
import com.mraof.minestuck.world.gen.structure.village.ConsortVillageStructure;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public final class MSStructures
{
	public static final DeferredRegister<StructureFeature<?>> REGISTER = DeferredRegister.create(Registry.STRUCTURE_FEATURE_REGISTRY, Minestuck.MOD_ID);
	
	// Overworld
	public static final RegistryObject<StructureFeature<NoneFeatureConfiguration>> FROG_TEMPLE = REGISTER.register("frog_temple", () -> new FrogTempleStructure(NoneFeatureConfiguration.CODEC));
	
	// Land
	public static final RegistryObject<GateStructure> LAND_GATE = REGISTER.register("land_gate", () -> new GateStructure(NoneFeatureConfiguration.CODEC));
	public static final RegistryObject<StructureFeature<NoneFeatureConfiguration>> SMALL_RUIN = REGISTER.register("small_ruin", () -> new SmallRuinStructure(NoneFeatureConfiguration.CODEC));
	public static final RegistryObject<StructureFeature<NoneFeatureConfiguration>> IMP_DUNGEON = REGISTER.register("imp_dungeon", () -> new ImpDungeonStructure(NoneFeatureConfiguration.CODEC));
	public static final RegistryObject<StructureFeature<NoneFeatureConfiguration>> CONSORT_VILLAGE = REGISTER.register("consort_village", () -> new ConsortVillageStructure(NoneFeatureConfiguration.CODEC));
	
	// Skaia
	public static final RegistryObject<StructureFeature<NoneFeatureConfiguration>> SKAIA_CASTLE = REGISTER.register("skaia_castle", () -> new CastleStructure(NoneFeatureConfiguration.CODEC));
}