package com.mraof.minestuck.world.gen.structure;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.util.MSTags;
import com.mraof.minestuck.world.gen.structure.castle.CastleStructure;
import com.mraof.minestuck.world.gen.structure.gate.GateStructure;
import com.mraof.minestuck.world.gen.structure.village.ConsortVillageStructure;
import net.minecraft.FieldsAreNonnullByDefault;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraftforge.registries.DeferredRegister;

import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

@FieldsAreNonnullByDefault
public final class MSConfiguredStructures
{
	public static final DeferredRegister<Structure> REGISTER = DeferredRegister.create(Registry.STRUCTURE_REGISTRY, Minestuck.MOD_ID);
	
	// Overworld
	public static final ResourceKey<Structure> FROG_TEMPLE = register("frog_temple", () -> new FrogTempleStructure(new Structure.StructureSettings(BuiltinRegistries.BIOME.getOrCreateTag(MSTags.Biomes.HAS_FROG_TEMPLE), Map.of(), GenerationStep.Decoration.SURFACE_STRUCTURES, TerrainAdjustment.NONE)));
	
	// Land
	public static final ResourceKey<Structure> LAND_GATE = register("land_gate", () -> new GateStructure(new Structure.StructureSettings(BuiltinRegistries.BIOME.getOrCreateTag(MSTags.Biomes.HAS_LAND_GATE), Map.of(), GenerationStep.Decoration.SURFACE_STRUCTURES, TerrainAdjustment.NONE)));
	public static final ResourceKey<Structure> SMALL_RUIN = register("small_ruin", () -> new SmallRuinStructure(new Structure.StructureSettings(BuiltinRegistries.BIOME.getOrCreateTag(MSTags.Biomes.HAS_SMALL_RUIN), Map.of(), GenerationStep.Decoration.SURFACE_STRUCTURES, TerrainAdjustment.NONE)));
	public static final ResourceKey<Structure> IMP_DUNGEON = register("imp_dungeon", () -> new ImpDungeonStructure(new Structure.StructureSettings(BuiltinRegistries.BIOME.getOrCreateTag(MSTags.Biomes.HAS_IMP_DUNGEON), Map.of(), GenerationStep.Decoration.SURFACE_STRUCTURES, TerrainAdjustment.NONE)));
	public static final ResourceKey<Structure> CONSORT_VILLAGE = register("consort_village", () -> new ConsortVillageStructure(new Structure.StructureSettings(BuiltinRegistries.BIOME.getOrCreateTag(MSTags.Biomes.HAS_CONSORT_VILLAGE), Map.of(), GenerationStep.Decoration.SURFACE_STRUCTURES, TerrainAdjustment.NONE)));
	
	// Skaia
	public static final ResourceKey<Structure> SKAIA_CASTLE = register("skaia_castle", () -> new CastleStructure(new Structure.StructureSettings(BuiltinRegistries.BIOME.getOrCreateTag(MSTags.Biomes.HAS_SKAIA_CASTLE), Map.of(), GenerationStep.Decoration.SURFACE_STRUCTURES, TerrainAdjustment.NONE)));
	
	private static ResourceKey<Structure> register(String name, Supplier<Structure> structureSupplier)
	{
		return Objects.requireNonNull(REGISTER.register(name, structureSupplier).getKey());
	}
}