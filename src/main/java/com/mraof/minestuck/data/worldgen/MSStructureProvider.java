package com.mraof.minestuck.data.worldgen;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.util.MSTags;
import com.mraof.minestuck.world.biome.MSBiomes;
import com.mraof.minestuck.world.gen.structure.*;
import com.mraof.minestuck.world.gen.structure.castle.CastleStructure;
import com.mraof.minestuck.world.gen.structure.gate.GateStructure;
import com.mraof.minestuck.world.gen.structure.gate.LandGatePlacement;
import com.mraof.minestuck.world.gen.structure.village.ConsortVillageStructure;
import com.mraof.minestuck.world.gen.structure.wfc.ProspitWFCDemoStructure;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.heightproviders.ConstantHeight;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.structures.JigsawStructure;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.mraof.minestuck.world.gen.structure.MSStructures.*;

public final class MSStructureProvider
{
	public static void registerStructures(BootstrapContext<Structure> context)
	{
		HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
		HolderGetter<StructureTemplatePool> pools = context.lookup(Registries.TEMPLATE_POOL);
		
		// Overworld
		context.register(FROG_TEMPLE, new FrogTempleStructure(new Structure.StructureSettings(biomes.getOrThrow(MSTags.Biomes.HAS_FROG_TEMPLE),
				Map.of(), GenerationStep.Decoration.SURFACE_STRUCTURES, TerrainAdjustment.NONE)));
		
		// Land
		context.register(LAND_GATE, new GateStructure(new Structure.StructureSettings(biomes.getOrThrow(MSTags.Biomes.HAS_LAND_GATE),
				Map.of(), GenerationStep.Decoration.SURFACE_STRUCTURES, TerrainAdjustment.NONE)));
		context.register(SMALL_RUIN, new SmallRuinStructure(new Structure.StructureSettings(biomes.getOrThrow(MSTags.Biomes.HAS_SMALL_RUIN),
				Map.of(), GenerationStep.Decoration.SURFACE_STRUCTURES, TerrainAdjustment.NONE)));
		context.register(PROSPIT_BUNKER, jigsaw(biomes, pools, MSTags.Biomes.HAS_PROSPIT_BUNKER, PROSPIT_BUNKER_START_POOL));
		context.register(DERSE_BUNKER, jigsaw(biomes, pools, MSTags.Biomes.HAS_DERSE_BUNKER, DERSE_BUNKER_START_POOL));
		context.register(IMP_BUNKER, jigsaw(biomes, pools, MSTags.Biomes.HAS_IMP_BUNKER, IMP_BUNKER_START_POOL));
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
		
		context.register(SKAIAN_CATHEDRAL, new JigsawStructure(new Structure.StructureSettings(biomes.getOrThrow(MSTags.Biomes.HAS_SKAIAN_CATHEDRAL),
				Map.of(), GenerationStep.Decoration.SURFACE_STRUCTURES, TerrainAdjustment.NONE), pools.getOrThrow(ResourceKey.create(Registries.TEMPLATE_POOL,
				Minestuck.id("skaian_cathedral/front"))), 7, ConstantHeight.of(VerticalAnchor.absolute(-6)),
				false, Heightmap.Types.WORLD_SURFACE_WG));
		
		context.register(ProspitWFCDemoStructure.STRUCTURE, new ProspitWFCDemoStructure.TerrainStructure(
				new Structure.StructureSettings(HolderSet.direct(biomes.getOrThrow(MSBiomes.PROSPIT_WFC_DEMO)),
						Map.of(), GenerationStep.Decoration.SURFACE_STRUCTURES, TerrainAdjustment.NONE)));
	}
	
	public static void registerStructureSets(BootstrapContext<StructureSet> context)
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
		
		context.register(key("skaian_cathedral"), new StructureSet(structures.getOrThrow(SKAIAN_CATHEDRAL),
				new RandomSpreadStructurePlacement(40, 10, RandomSpreadType.LINEAR, 1481098009)));
		
		context.register(key("prospit_wfc_demo"), new StructureSet(structures.getOrThrow(ProspitWFCDemoStructure.STRUCTURE),
				new ProspitWFCDemoStructure.FixedPlacement()));
	}
	
	private static JigsawStructure jigsaw(HolderGetter<Biome> biomes, HolderGetter<StructureTemplatePool> templatePools, TagKey<Biome> biome, ResourceKey<StructureTemplatePool> startPool)
	{
		return new JigsawStructure(
				new Structure.StructureSettings.Builder(biomes.getOrThrow(biome))
						.generationStep(GenerationStep.Decoration.SURFACE_STRUCTURES)
						.build(),
				templatePools.getOrThrow(startPool),
				Optional.empty(),
				20,
				ConstantHeight.of(VerticalAnchor.absolute(0)),
				false,
				Optional.of(Heightmap.Types.WORLD_SURFACE_WG),
				128,
				List.of(),
				JigsawStructure.DEFAULT_DIMENSION_PADDING,
				JigsawStructure.DEFAULT_LIQUID_SETTINGS
		);
	}
	
	private static ResourceKey<StructureSet> key(String path)
	{
		return ResourceKey.create(Registries.STRUCTURE_SET, ResourceLocation.fromNamespaceAndPath(Minestuck.MOD_ID, path));
	}
}
