package com.mraof.minestuck.world.gen.structure;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.util.MSTags;
import com.mraof.minestuck.world.gen.structure.castle.CastleStructure;
import com.mraof.minestuck.world.gen.structure.gate.GateStructure;
import com.mraof.minestuck.world.gen.structure.village.ConsortVillageStructure;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.Map;

public final class MSConfiguredStructures
{
	public static final DeferredRegister<Structure> REGISTER = DeferredRegister.create(Registry.STRUCTURE_REGISTRY, Minestuck.MOD_ID);
	
	// Overworld
	public static final RegistryObject<Structure> FROG_TEMPLE = REGISTER.register("frog_temple", () -> new FrogTempleStructure(new Structure.StructureSettings(BuiltinRegistries.BIOME.getOrCreateTag(MSTags.Biomes.HAS_FROG_TEMPLE), Map.of(), GenerationStep.Decoration.SURFACE_STRUCTURES, TerrainAdjustment.NONE)));
	
	// Land
	public static final RegistryObject<Structure> LAND_GATE = REGISTER.register("land_gate", () -> new GateStructure(new Structure.StructureSettings(BuiltinRegistries.BIOME.getOrCreateTag(MSTags.Biomes.HAS_LAND_GATE), Map.of(), GenerationStep.Decoration.SURFACE_STRUCTURES, TerrainAdjustment.NONE)));
	public static final RegistryObject<Structure> SMALL_RUIN = REGISTER.register("small_ruin", () -> new SmallRuinStructure(new Structure.StructureSettings(BuiltinRegistries.BIOME.getOrCreateTag(MSTags.Biomes.HAS_SMALL_RUIN), Map.of(), GenerationStep.Decoration.SURFACE_STRUCTURES, TerrainAdjustment.NONE)));
	public static final RegistryObject<Structure> IMP_DUNGEON = REGISTER.register("imp_dungeon", () -> new ImpDungeonStructure(new Structure.StructureSettings(BuiltinRegistries.BIOME.getOrCreateTag(MSTags.Biomes.HAS_IMP_DUNGEON), Map.of(), GenerationStep.Decoration.SURFACE_STRUCTURES, TerrainAdjustment.NONE)));
	public static final RegistryObject<Structure> CONSORT_VILLAGE = REGISTER.register("consort_village", () -> new ConsortVillageStructure(new Structure.StructureSettings(BuiltinRegistries.BIOME.getOrCreateTag(MSTags.Biomes.HAS_CONSORT_VILLAGE), Map.of(), GenerationStep.Decoration.SURFACE_STRUCTURES, TerrainAdjustment.NONE)));
	
	// Skaia
	public static final RegistryObject<Structure> SKAIA_CASTLE = REGISTER.register("skaia_castle", () -> new CastleStructure(new Structure.StructureSettings(BuiltinRegistries.BIOME.getOrCreateTag(MSTags.Biomes.HAS_SKAIA_CASTLE), Map.of(), GenerationStep.Decoration.SURFACE_STRUCTURES, TerrainAdjustment.NONE)));
}