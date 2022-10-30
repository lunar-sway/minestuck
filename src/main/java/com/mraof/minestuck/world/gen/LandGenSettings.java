package com.mraof.minestuck.world.gen;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import com.mraof.minestuck.world.biome.LandBiomeAccess;
import com.mraof.minestuck.world.biome.LandBiomeType;
import com.mraof.minestuck.world.gen.structure.gate.GateStructure;
import com.mraof.minestuck.world.gen.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.lands.LandTypePair;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.util.CubicSpline;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.TerrainShaper;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.*;

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
	 * At 0, there should be a rough split between normal and rough terrain occurring.
	 */
	public float roughThreshold = -0.2F;
	
	public float oceanOffset = -0.12F;
	public float inlandOffset = 0.05F;
	public float inlandAngle = 0.1F;
	
	public float oceanFactor = 6;
	public float normalFactor = 5;
	public float roughFactor = 3;
	
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
	
	Holder<NoiseGeneratorSettings> createDimensionSettings(Registry<DensityFunction> densityFunctions)
	{
		//includes the y-range at which generation occurs, with the values used here set in resources/data/minestuck/dimension_type/land.json
		NoiseSettings noiseSettings = NoiseSettings.create(-64, 384, new NoiseSamplingSettings(1, 1, 80, 160),
				new NoiseSlider(-1, 2, 0), new NoiseSlider(1, 3, 0), 1, 2,
				this.createTerrainShaper());
		
		SurfaceRules.RuleSource bedrockFloor = SurfaceRules.ifTrue(SurfaceRules.verticalGradient("bedrock_floor", VerticalAnchor.bottom(), VerticalAnchor.aboveBottom(5)), SurfaceRules.state(Blocks.BEDROCK.defaultBlockState()));
		
		SurfaceRules.RuleSource surfaceRule = SurfaceRules.sequence(bedrockFloor, landTypes.getTerrain().getSurfaceRule(blockRegistry));
		
		NoiseGeneratorSettings settings = new NoiseGeneratorSettings(noiseSettings, blockRegistry.getBlockState("ground"), blockRegistry.getBlockState("ocean"),
				MSDensityFunctions.makeLandNoiseRouter(densityFunctions),
				surfaceRule, 64, false, true, false, false);
		
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
	
	private TerrainShaper createTerrainShaper()
	{
		CubicSpline.Builder<TerrainShaper.Point> offsetSpline = CubicSpline.builder(TerrainShaper.Point::continents);
		if(this.hasOceanTerrain())
			offsetSpline.addPoint(this.getOceanThreshold(-0.1F), this.oceanOffset, 0);
		offsetSpline.addPoint(this.getOceanThreshold(0.1F), this.inlandOffset, this.inlandAngle);
		
		CubicSpline.Builder<TerrainShaper.Point> inlandFactorSpline = CubicSpline.builder(TerrainShaper.Point::erosion);
		if(this.hasRoughTerrain())
			inlandFactorSpline.addPoint(this.getRoughThreshold(-0.05F), this.roughFactor, 0);
		inlandFactorSpline.addPoint(this.getRoughThreshold(0.05F), this.normalFactor, 0);
		
		CubicSpline.Builder<TerrainShaper.Point> factorSpline = CubicSpline.builder(TerrainShaper.Point::continents);
		if(this.hasOceanTerrain())
			factorSpline.addPoint(this.getOceanThreshold(-0.1F), this.oceanFactor, 0);
		factorSpline.addPoint(this.getOceanThreshold(0.1F), inlandFactorSpline.build(), 0);
		
		return new TerrainShaper(offsetSpline.build(), factorSpline.build(), CubicSpline.constant(0));
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