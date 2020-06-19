package com.mraof.minestuck.world.biome;

import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.world.gen.MSSurfaceBuilders;
import net.minecraft.entity.EntityClassification;
import net.minecraft.world.gen.surfacebuilders.ConfiguredSurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;

public class ProspitBiome extends AbstractBiome
{
	protected ProspitBiome()
	{
		super(new Builder().precipitation(RainType.NONE).category(Category.NONE).depth(0.1F).scale(0.2F).temperature(0.4F).downfall(0.5F).waterColor(0x3F76E4).waterFogColor(0x050533));
		
	}
	
	protected void init()
	{
		this.surfaceBuilder = new ConfiguredSurfaceBuilder<>(MSSurfaceBuilders.PROSPIT.get(), SurfaceBuilder.AIR_CONFIG);
		this.addSpawn(EntityClassification.CREATURE, new SpawnListEntry(MSEntityTypes.PROSPITIAN_CITIZEN, 2, 5, 20));
	}
}