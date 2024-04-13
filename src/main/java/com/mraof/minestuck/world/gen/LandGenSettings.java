package com.mraof.minestuck.world.gen;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import com.mraof.minestuck.world.biome.LandBiomeAccess;
import com.mraof.minestuck.world.biome.LandBiomeType;
import com.mraof.minestuck.world.gen.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.gen.structure.gate.GateStructure;
import com.mraof.minestuck.world.lands.LandTypePair;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.util.CubicSpline;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

import java.util.List;

public final class LandGenSettings
{
	private final LandTypePair landTypes;
	private final StructureBlockRegistry blockRegistry;
	private GateStructure.PieceFactory gatePiece;
	
	/**
	 * A threshold that determines the split between ocean and inland terrain.
	 * The terrain will be ocean when continentalness is below the threshold.
	 * Thus, a higher threshold will result in more ocean terrain.
	 * At 0, there should be a rough split between ocean and inland terrain occurring.
	 */
	public float oceanThreshold = -0.2F;
	
	/**
	 * A threshold that determines the split between rough and normal terrain.
	 * When the terrain is inland, the terrain will be rough when erosion is below the threshold.
	 * Thus, a higher threshold will result in more rough terrain.
	 * At 0, there should be a mostly equal chance of normal and rough terrain occurring.
	 */
	public float roughThreshold = 0.0F;
	
	public float oceanOffset = -0.12F;
	public float inlandOffset = 0.12F;
	public float inlandAngle = 0.2F;
	
	public float oceanFactor = 6;
	public float normalFactor = 5;
	public float roughFactor = 3;
	
	LandGenSettings(LandTypePair landTypes)
	{
		this.landTypes = landTypes;
		
		blockRegistry = StructureBlockRegistry.init(landTypes);
		
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
	
	Holder<NoiseGeneratorSettings> createDimensionSettings(HolderGetter<NormalNoise.NoiseParameters> noises, HolderGetter<DensityFunction> densityFunctions)
	{
		//includes the y-range at which generation occurs, with the values used here set in resources/data/minestuck/dimension_type/land.json
		NoiseSettings noiseSettings = NoiseSettings.create(-64, 384, 1, 2);
		
		SurfaceRules.RuleSource bedrockFloor = SurfaceRules.ifTrue(SurfaceRules.verticalGradient("bedrock_floor", VerticalAnchor.bottom(), VerticalAnchor.aboveBottom(5)), SurfaceRules.state(Blocks.BEDROCK.defaultBlockState()));
		
		SurfaceRules.RuleSource surfaceRule = SurfaceRules.sequence(bedrockFloor, landTypes.getTerrain().getSurfaceRule(blockRegistry));
		
		NoiseGeneratorSettings settings = new NoiseGeneratorSettings(noiseSettings, blockRegistry.getBlockState("ground"), blockRegistry.getBlockState("ocean"),
				makeLandNoiseRouter(noises, densityFunctions),
				surfaceRule, List.of(), 64, false, true, false, false);
		
		return Holder.direct(settings);
	}
	
	public boolean hasOceanTerrain()
	{
		return this.oceanThreshold > -0.95;
	}
	
	private float getOceanThreshold(float offset)
	{
		return this.hasOceanTerrain() ? this.oceanThreshold + offset : -1.0F;
	}
	
	public boolean hasRoughTerrain()
	{
		return this.roughThreshold > -0.95;
	}
	
	private float getRoughThreshold(float offset)
	{
		return this.hasRoughTerrain() ? this.roughThreshold + offset : -1.0F;
	}
	
	private NoiseRouter makeLandNoiseRouter(HolderGetter<NormalNoise.NoiseParameters> noises, HolderGetter<DensityFunction> densityFunctions)
	{
		DensityFunctions.Spline.Coordinate continents = new DensityFunctions.Spline.Coordinate(densityFunctions.getOrThrow(MSDensityFunctions.LAND_CONTINENTS));
		DensityFunctions.Spline.Coordinate erosion = new DensityFunctions.Spline.Coordinate(densityFunctions.getOrThrow(MSDensityFunctions.LAND_EROSION));
		
		DensityFunction depth = MSDensityFunctions.depth(offset(continents));
		DensityFunction factor = factor(continents, erosion);
		DensityFunction initialDensity = MSDensityFunctions.initialDensity(depth, factor);
		DensityFunction finalDensity = MSDensityFunctions.finalDensity(depth, factor, DensityFunctions.zero(), noises.getOrThrow(Noises.JAGGED), 320);
		
		return new NoiseRouter(
				DensityFunctions.zero(), DensityFunctions.constant(-1), DensityFunctions.zero(), DensityFunctions.zero(),	// aquifer info
				DensityFunctions.zero(), DensityFunctions.zero(), MSDensityFunctions.get(densityFunctions, MSDensityFunctions.LAND_CONTINENTS), MSDensityFunctions.get(densityFunctions, MSDensityFunctions.LAND_EROSION), depth, DensityFunctions.zero(), // biome parameters
				initialDensity, finalDensity,	// terrain and surface height
				DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero());	// ore vein info
	}
	
	private DensityFunction offset(DensityFunctions.Spline.Coordinate continents)
	{
		var builder = CubicSpline.builder(continents);
		
		if(this.hasOceanTerrain())
			builder.addPoint(this.getOceanThreshold(-0.2F), this.oceanOffset, 0);
		builder.addPoint(this.getOceanThreshold(0.2F), this.inlandOffset, this.inlandAngle);
		
		return DensityFunctions.add(DensityFunctions.constant(-0.50375), DensityFunctions.spline(builder.build()));
	}
	
	private DensityFunction factor(DensityFunctions.Spline.Coordinate continents, DensityFunctions.Spline.Coordinate erosion)
	{
		var inlandBuilder = CubicSpline.builder(erosion);
		if(this.hasRoughTerrain())
			inlandBuilder.addPoint(this.getRoughThreshold(-0.05F), this.roughFactor, 0);
		inlandBuilder.addPoint(this.getRoughThreshold(0.05F), this.normalFactor, 0);
		
		var builder = CubicSpline.builder(continents);
		if(this.hasOceanTerrain())
			builder.addPoint(this.getOceanThreshold(-0.1F), this.oceanFactor, 0);
		builder.addPoint(this.getOceanThreshold(0.1F), inlandBuilder.build());
		
		return DensityFunctions.spline(builder.build());
	}
	
	public Climate.ParameterList<Holder<Biome>> createBiomeParameters(LandBiomeAccess biomes)
	{
		ImmutableList.Builder<Pair<Climate.ParameterPoint, Holder<Biome>>> builder = ImmutableList.builder();
		
		if(this.hasOceanTerrain())
			builder.add(Pair.of(simpleParameterPoint(Climate.Parameter.span(-1, this.getOceanThreshold(0)), Climate.Parameter.span(-1, 1)), biomes.fromType(LandBiomeType.OCEAN)));
		
		Climate.Parameter inlandContinentalness = Climate.Parameter.span(this.getOceanThreshold(0), 1);
		if(this.hasRoughTerrain())
			builder.add(Pair.of(simpleParameterPoint(inlandContinentalness, Climate.Parameter.span(-1, this.getRoughThreshold(0))), biomes.fromType(LandBiomeType.ROUGH)));
		
		builder.add(Pair.of(simpleParameterPoint(inlandContinentalness, Climate.Parameter.span(this.getRoughThreshold(0), 1)), biomes.fromType(LandBiomeType.NORMAL)));
		
		return new Climate.ParameterList<>(builder.build());
	}
	
	private static Climate.ParameterPoint simpleParameterPoint(Climate.Parameter continents, Climate.Parameter erosion)
	{
		return Climate.parameters(Climate.Parameter.point(0), Climate.Parameter.point(0), continents, erosion, Climate.Parameter.point(0), Climate.Parameter.point(0), 0);
	}
}