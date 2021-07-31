package com.mraof.minestuck.world.gen;

import com.mraof.minestuck.world.gen.feature.MSFeatures;
import com.mraof.minestuck.world.gen.feature.structure.GateStructure;
import com.mraof.minestuck.world.gen.feature.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.lands.LandTypePair;
import net.minecraft.world.gen.DimensionSettings;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

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
	
	Supplier<DimensionSettings> createDimensionSettings()
	{
		Map<Structure<?>, StructureSeparationSettings> structures = new HashMap<>();
		structures.put(MSFeatures.LAND_GATE, new StructureSeparationSettings(1, 0, 0));
		structures.put(MSFeatures.SMALL_RUIN, new StructureSeparationSettings(16, 4, 59273643));
		structures.put(MSFeatures.IMP_DUNGEON, new StructureSeparationSettings(16, 4, 34527185));
		structures.put(MSFeatures.CONSORT_VILLAGE, new StructureSeparationSettings(24, 5, 10387312));
		
		DimensionStructuresSettings structureSettings = new DimensionStructuresSettings(Optional.empty(), structures);
		
		NoiseSettings noiseSettings = new NoiseSettings(256, new ScalingSettings(1, 1, 80, 160),
				new SlideSettings(-10, 3, 0), new SlideSettings(-30, 0, 0),
				1, 2, 1, -0.46875, true, true, false, false);
		
		DimensionSettings settings = new DimensionSettings(structureSettings, noiseSettings, blockRegistry.getBlockState("ground"),
				blockRegistry.getBlockState("ocean"), -1, 0, 64, false);
		
		return () -> settings;
	}
}