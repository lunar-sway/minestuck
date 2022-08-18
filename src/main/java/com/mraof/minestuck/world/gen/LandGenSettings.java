package com.mraof.minestuck.world.gen;

import com.mraof.minestuck.world.gen.feature.structure.GateStructure;
import com.mraof.minestuck.world.gen.feature.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.lands.LandTypePair;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.CubicSpline;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.TerrainShaper;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.synth.BlendedNoise;

public class LandGenSettings
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
		NoiseSettings noiseSettings = NoiseSettings.create(0, 256, new NoiseSamplingSettings(1, 1, 80, 160),
				new NoiseSlider(-1, 2, 0), new NoiseSlider(1, 3, 0), 1, 2,
				new TerrainShaper(CubicSpline.constant(0.05F), CubicSpline.constant(5), CubicSpline.constant(0)));
		
		SurfaceRules.RuleSource bedrockFloor = SurfaceRules.ifTrue(SurfaceRules.verticalGradient("bedrock_floor", VerticalAnchor.bottom(), VerticalAnchor.aboveBottom(5)), SurfaceRules.state(Blocks.BEDROCK.defaultBlockState()));
		
		ResourceKey<Biome> roughBiome = landTypes.getTerrain().getBiomeSet().ROUGH;
		
		SurfaceRules.RuleSource surfaceBlock = SurfaceRules.sequence(
				SurfaceRules.ifTrue(SurfaceRules.isBiome(roughBiome), SurfaceRules.state(blockRegistry.getBlockState("surface_rough"))),
				SurfaceRules.state(blockRegistry.getBlockState("surface")));
		SurfaceRules.RuleSource surface = SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, SurfaceRules.ifTrue(SurfaceRules.waterBlockCheck(0, 0), surfaceBlock));
		
		SurfaceRules.RuleSource upper = SurfaceRules.ifTrue(SurfaceRules.UNDER_FLOOR, SurfaceRules.ifTrue(SurfaceRules.waterStartCheck(-6, -1), SurfaceRules.state(blockRegistry.getBlockState("upper"))));
		// This surface rule targets the ocean surface by being positioned after "surface" and "upper", thus only placing blocks on surfaces where "surface" or "upper" doesn't
		SurfaceRules.RuleSource ocean_surface = SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, SurfaceRules.state(blockRegistry.getBlockState("ocean_surface")));
		
		SurfaceRules.RuleSource surfaceRule = SurfaceRules.sequence(bedrockFloor, surface, upper, ocean_surface);
		
		DensityFunction offset = DensityFunctions.terrainShaperSpline(DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.TerrainShaperSpline.SplineType.OFFSET, -0.81, 2.5);
		DensityFunction depth = DensityFunctions.add(DensityFunctions.yClampedGradient(0, 256, 1, -1), offset);
		DensityFunction factor = DensityFunctions.terrainShaperSpline(DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.TerrainShaperSpline.SplineType.FACTOR, 0.0, 8.0);
		DensityFunction initialDensity = DensityFunctions.mul(DensityFunctions.constant(4), DensityFunctions.mul(depth, factor).quarterNegative());
		DensityFunction finalDensity = DensityFunctions.interpolated(DensityFunctions.slide(noiseSettings, DensityFunctions.add(BlendedNoise.UNSEEDED, initialDensity))).squeeze();
		
		NoiseGeneratorSettings settings = new NoiseGeneratorSettings(noiseSettings, blockRegistry.getBlockState("ground"), blockRegistry.getBlockState("ocean"),
				new NoiseRouterWithOnlyNoises(DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero(), initialDensity, finalDensity, DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero()),
				surfaceRule, 64, false, false, false, false);
		
		return Holder.direct(settings);
	}
}