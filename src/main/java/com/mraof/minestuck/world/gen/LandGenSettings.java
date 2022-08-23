package com.mraof.minestuck.world.gen;

import com.mojang.datafixers.util.Pair;
import com.mraof.minestuck.world.biome.ILandBiomeSet;
import com.mraof.minestuck.world.biome.LandBiomeType;
import com.mraof.minestuck.world.gen.structure.GateStructure;
import com.mraof.minestuck.world.gen.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.lands.LandTypePair;
import net.minecraft.core.Holder;
import net.minecraft.util.CubicSpline;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.TerrainShaper;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.synth.BlendedNoise;

import java.util.List;

public final class LandGenSettings
{
	private final LandTypePair landTypes;
	private final StructureBlockRegistry blockRegistry;
	private GateStructure.PieceFactory gatePiece;
	public float oceanChance = 1/3F, roughChance = 1/5F;
	
	LandGenSettings(LandTypePair landTypes)
	{
		this.landTypes = landTypes;
		
		blockRegistry = new StructureBlockRegistry();
		landTypes.getTerrain().registerBlocks(blockRegistry);
		landTypes.getTitle().registerBlocks(blockRegistry);
		
		landTypes.getTerrain().setGenSettings(this);
		landTypes.getTitle().setGenSettings(this);
		
	}
	
	public LandTypePair getLandTypes()
	{
		return landTypes;
	}
	
	public StructureBlockRegistry getBlockRegistry()
	{
		return blockRegistry;
	}
	
	public void setGatePiece(GateStructure.PieceFactory factory)
	{
		gatePiece = factory;
	}
	
	public GateStructure.PieceFactory getGatePiece()
	{
		return gatePiece;
	}
	
	Holder<NoiseGeneratorSettings> createDimensionSettings()
	{
		/*TODO structure settings go elsewhere now
		Map<StructureFeature<?>, StructureFeatureConfiguration> structures = new HashMap<>();
		structures.put(MSFeatures.LAND_GATE, new StructureFeatureConfiguration(1, 0, 0));
		structures.put(MSFeatures.SMALL_RUIN, new StructureFeatureConfiguration(16, 4, 59273643));
		structures.put(MSFeatures.IMP_DUNGEON, new StructureFeatureConfiguration(16, 4, 34527185));
		structures.put(MSFeatures.CONSORT_VILLAGE, new StructureFeatureConfiguration(24, 5, 10387312));
		
		StructureSettings structureSettings = new StructureSettings(Optional.empty(), structures);
		*/
		CubicSpline<TerrainShaper.Point> offsetSpline = CubicSpline.builder(TerrainShaper.Point::continents)
				.addPoint(-0.25F, -0.1F, 0).addPoint(-0.15F, 0.05F, 0).build();
		
		CubicSpline<TerrainShaper.Point> inlandFactorSpline = CubicSpline.builder(TerrainShaper.Point::erosion)
				.addPoint(-0.25F, 3, 0).addPoint(-0.15F, 5, 0).build();
		CubicSpline<TerrainShaper.Point> factorSpline = CubicSpline.builder(TerrainShaper.Point::continents)
				.addPoint(-0.3F, 5, 0).addPoint(-0.1F, inlandFactorSpline, 0).build();
		
		NoiseSettings noiseSettings = NoiseSettings.create(0, 256, new NoiseSamplingSettings(1, 1, 80, 160),
				new NoiseSlider(-1, 2, 0), new NoiseSlider(1, 3, 0), 1, 2,
				new TerrainShaper(offsetSpline, factorSpline, CubicSpline.constant(0)));
		
		SurfaceRules.RuleSource bedrockFloor = SurfaceRules.ifTrue(SurfaceRules.verticalGradient("bedrock_floor", VerticalAnchor.bottom(), VerticalAnchor.aboveBottom(5)), SurfaceRules.state(Blocks.BEDROCK.defaultBlockState()));
		
		SurfaceRules.RuleSource surfaceRule = SurfaceRules.sequence(bedrockFloor, landTypes.getTerrain().getSurfaceRule(blockRegistry));
		
		DensityFunction continents = DensityFunctions.noise(MSNoiseParameters.LAND_CONTINENTS.getHolder().orElseThrow(), 0.25, 0);
		DensityFunction erosion = DensityFunctions.noise(MSNoiseParameters.LAND_EROSION.getHolder().orElseThrow(), 0.25, 0);
		
		DensityFunction offset = DensityFunctions.terrainShaperSpline(continents, erosion, DensityFunctions.zero(), DensityFunctions.TerrainShaperSpline.SplineType.OFFSET, -0.81, 2.5);
		DensityFunction depth = DensityFunctions.add(DensityFunctions.yClampedGradient(0, 256, 1, -1), offset);
		DensityFunction factor = DensityFunctions.terrainShaperSpline(continents, erosion, DensityFunctions.zero(), DensityFunctions.TerrainShaperSpline.SplineType.FACTOR, 0.0, 8.0);
		DensityFunction initialDensity = DensityFunctions.mul(DensityFunctions.constant(4), DensityFunctions.mul(depth, factor).quarterNegative());
		DensityFunction finalDensity = DensityFunctions.interpolated(DensityFunctions.slide(noiseSettings, DensityFunctions.add(BlendedNoise.UNSEEDED, initialDensity))).squeeze();
		
		NoiseGeneratorSettings settings = new NoiseGeneratorSettings(noiseSettings, blockRegistry.getBlockState("ground"), blockRegistry.getBlockState("ocean"),
				new NoiseRouterWithOnlyNoises(DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero(), continents, erosion, depth, DensityFunctions.zero(), initialDensity, finalDensity, DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero()),
				surfaceRule, 64, false, false, false, false);
		
		return Holder.direct(settings);
	}
	
	public Climate.ParameterList<Holder<Biome>> createBiomeParameters(ILandBiomeSet biomes)
	{
		return new Climate.ParameterList<>(List.of(
				Pair.of(simpleParameterPoint(Climate.Parameter.span(-1, -0.2F), Climate.Parameter.span(-1, 1)), biomes.fromType(LandBiomeType.OCEAN)),
				Pair.of(simpleParameterPoint(Climate.Parameter.span(-0.2F, 1), Climate.Parameter.span(-0.2F, 1)), biomes.fromType(LandBiomeType.NORMAL)),
				Pair.of(simpleParameterPoint(Climate.Parameter.span(-0.2F, 1), Climate.Parameter.span(-1, -0.2F)), biomes.fromType(LandBiomeType.ROUGH))
		));
	}
	
	private static Climate.ParameterPoint simpleParameterPoint(Climate.Parameter continents, Climate.Parameter erosion)
	{
		return Climate.parameters(Climate.Parameter.point(0), Climate.Parameter.point(0), continents, erosion, Climate.Parameter.point(0), Climate.Parameter.point(0), 0);
	}
}