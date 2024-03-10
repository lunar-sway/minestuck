package com.mraof.minestuck.data.worldgen;

import com.mraof.minestuck.util.MSTags;
import com.mraof.minestuck.world.biome.LandBiomeType;
import com.mraof.minestuck.world.gen.structure.FrogTempleStructure;
import com.mraof.minestuck.world.gen.structure.ImpDungeonStructure;
import com.mraof.minestuck.world.gen.structure.LargeUnfinishedTableStructure;
import com.mraof.minestuck.world.gen.structure.SmallRuinStructure;
import com.mraof.minestuck.world.gen.structure.castle.CastleStructure;
import com.mraof.minestuck.world.gen.structure.gate.GateStructure;
import com.mraof.minestuck.world.gen.structure.village.ConsortVillageStructure;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;

import java.util.Map;

import static com.mraof.minestuck.world.gen.structure.MSStructures.*;

public final class MSStructureProvider
{
	public static void register(BootstapContext<Structure> context)
	{
		HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
		
		context.register(FROG_TEMPLE, new FrogTempleStructure(new Structure.StructureSettings(biomes.getOrThrow(MSTags.Biomes.HAS_FROG_TEMPLE), Map.of(), GenerationStep.Decoration.SURFACE_STRUCTURES, TerrainAdjustment.NONE)));
		
		context.register(LAND_GATE, new GateStructure(new Structure.StructureSettings(biomes.getOrThrow(MSTags.Biomes.HAS_LAND_GATE), Map.of(), GenerationStep.Decoration.SURFACE_STRUCTURES, TerrainAdjustment.NONE)));
		context.register(SMALL_RUIN, new SmallRuinStructure(new Structure.StructureSettings(biomes.getOrThrow(MSTags.Biomes.HAS_SMALL_RUIN), Map.of(), GenerationStep.Decoration.SURFACE_STRUCTURES, TerrainAdjustment.NONE)));
		context.register(IMP_DUNGEON, new ImpDungeonStructure(new Structure.StructureSettings(biomes.getOrThrow(MSTags.Biomes.HAS_IMP_DUNGEON), Map.of(), GenerationStep.Decoration.SURFACE_STRUCTURES, TerrainAdjustment.NONE)));
		context.register(CONSORT_VILLAGE, new ConsortVillageStructure(new Structure.StructureSettings(biomes.getOrThrow(MSTags.Biomes.HAS_CONSORT_VILLAGE), Map.of(), GenerationStep.Decoration.SURFACE_STRUCTURES, TerrainAdjustment.NONE)));
		
		context.register(LARGE_UNFINISHED_TABLE, new LargeUnfinishedTableStructure(new Structure.StructureSettings(biomes.getOrThrow(MSTags.Biomes.LAND_ROUGH), Map.of(), GenerationStep.Decoration.SURFACE_STRUCTURES, TerrainAdjustment.NONE)));
		
		context.register(SKAIA_CASTLE, new CastleStructure(new Structure.StructureSettings(biomes.getOrThrow(MSTags.Biomes.HAS_SKAIA_CASTLE), Map.of(), GenerationStep.Decoration.SURFACE_STRUCTURES, TerrainAdjustment.NONE)));
	}
}
