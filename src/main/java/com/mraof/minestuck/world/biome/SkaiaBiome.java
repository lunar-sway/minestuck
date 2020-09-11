package com.mraof.minestuck.world.biome;

import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.world.gen.MSSurfaceBuilders;
import com.mraof.minestuck.world.gen.feature.MSFeatures;
import net.minecraft.entity.EntityClassification;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.surfacebuilders.ConfiguredSurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;

public class SkaiaBiome extends AbstractBiome
{
	protected SkaiaBiome()
	{
		super(new Biome.Builder().precipitation(Biome.RainType.NONE).category(Biome.Category.NONE).depth(0.1F).scale(0.2F).temperature(0.5F).downfall(0.5F).waterColor(0x3F76E4).waterFogColor(0x050533));
		
	}
	
	protected void init()
	{
		this.surfaceBuilder = new ConfiguredSurfaceBuilder<>(MSSurfaceBuilders.SKAIA.get(), SurfaceBuilder.AIR_CONFIG);
		this.addSpawn(EntityClassification.MONSTER, new SpawnListEntry(MSEntityTypes.DERSITE_PAWN, 2, 1, 10));
		this.addSpawn(EntityClassification.MONSTER, new SpawnListEntry(MSEntityTypes.DERSITE_BISHOP, 1, 1, 1));
		this.addSpawn(EntityClassification.MONSTER, new SpawnListEntry(MSEntityTypes.DERSITE_ROOK, 1, 1, 1));
		this.addSpawn(EntityClassification.MONSTER, new SpawnListEntry(MSEntityTypes.PROSPITIAN_PAWN, 2, 1, 10));
		this.addSpawn(EntityClassification.MONSTER, new SpawnListEntry(MSEntityTypes.PROSPITIAN_BISHOP, 1, 1, 1));
		this.addSpawn(EntityClassification.MONSTER, new SpawnListEntry(MSEntityTypes.PROSPITIAN_ROOK, 1, 1, 1));
		
		addStructure(MSFeatures.SKAIA_CASTLE.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
		addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, MSFeatures.SKAIA_CASTLE.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.NOPE.configure(IPlacementConfig.NO_PLACEMENT_CONFIG)));
	}
}