package com.mraof.minestuck.data.worldgen;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.util.MSTags;
import com.mraof.minestuck.world.gen.structure.*;
import com.mraof.minestuck.world.gen.structure.castle.CastleStructure;
import com.mraof.minestuck.world.gen.structure.gate.GateStructure;
import com.mraof.minestuck.world.gen.structure.gate.LandGatePlacement;
import com.mraof.minestuck.world.gen.structure.village.ConsortVillageStructure;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;

import java.util.Map;

import static com.mraof.minestuck.world.gen.structure.MSStructures.*;

public final class MSStructureProvider
{
	public static void registerStructures(BootstapContext<Structure> context)
	{
		HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
		
		// Overworld
		context.register(FROG_TEMPLE, new FrogTempleStructure(new Structure.StructureSettings(biomes.getOrThrow(MSTags.Biomes.HAS_FROG_TEMPLE),
				Map.of(), GenerationStep.Decoration.SURFACE_STRUCTURES, TerrainAdjustment.NONE)));
		
		// Land
		context.register(LAND_GATE, new GateStructure(new Structure.StructureSettings(biomes.getOrThrow(MSTags.Biomes.HAS_LAND_GATE),
				Map.of(), GenerationStep.Decoration.SURFACE_STRUCTURES, TerrainAdjustment.NONE)));
		context.register(SMALL_RUIN, new SmallRuinStructure(new Structure.StructureSettings(biomes.getOrThrow(MSTags.Biomes.HAS_SMALL_RUIN),
				Map.of(), GenerationStep.Decoration.SURFACE_STRUCTURES, TerrainAdjustment.NONE)));
		context.register(ImpDungeon.KEY, new ImpDungeonStructure(new Structure.StructureSettings(biomes.getOrThrow(MSTags.Biomes.HAS_IMP_DUNGEON),
				Map.of(), GenerationStep.Decoration.SURFACE_STRUCTURES, TerrainAdjustment.NONE)));
		context.register(ConsortVillage.KEY, new ConsortVillageStructure(new Structure.StructureSettings(biomes.getOrThrow(MSTags.Biomes.HAS_CONSORT_VILLAGE),
				Map.of(), GenerationStep.Decoration.SURFACE_STRUCTURES, TerrainAdjustment.NONE)));
		
		context.register(LARGE_WOOD_OBJECT, new LargeWoodObjectStructure(new Structure.StructureSettings(biomes.getOrThrow(MSTags.Biomes.LAND_ROUGH),
				Map.of(), GenerationStep.Decoration.SURFACE_STRUCTURES, TerrainAdjustment.NONE)));
		context.register(PINK_TOWER, new PinkTowerStructure(new Structure.StructureSettings(biomes.getOrThrow(MSTags.Biomes.LAND_NORMAL),
				Map.of(), GenerationStep.Decoration.SURFACE_STRUCTURES, TerrainAdjustment.BEARD_THIN)));
		
		// Skaia
		context.register(SkaiaCastle.KEY, new CastleStructure(new Structure.StructureSettings(biomes.getOrThrow(MSTags.Biomes.HAS_SKAIA_CASTLE),
				Map.of(), GenerationStep.Decoration.SURFACE_STRUCTURES, TerrainAdjustment.NONE)));
	}
	
	public static void registerStructureSets(BootstapContext<StructureSet> context)
	{
		HolderGetter<Structure> structures = context.lookup(Registries.STRUCTURE);
		
		// Overworld
		context.register(key("frog_temple"), new StructureSet(structures.getOrThrow(FROG_TEMPLE),
				new RandomSpreadStructurePlacement(140, 92, RandomSpreadType.LINEAR, 41361201)));
		
		// Land
		context.register(key("land_gate"), new StructureSet(structures.getOrThrow(LAND_GATE),
				new LandGatePlacement()));
		
		// Skaia
		context.register(key("skaia_castle"), new StructureSet(structures.getOrThrow(SkaiaCastle.KEY),
				new RandomSpreadStructurePlacement(50, 40, RandomSpreadType.LINEAR, 6729346)));
	}
	
	private static ResourceKey<StructureSet> key(String path)
	{
		return ResourceKey.create(Registries.STRUCTURE_SET, new ResourceLocation(Minestuck.MOD_ID, path));
	}
}
