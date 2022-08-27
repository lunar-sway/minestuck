package com.mraof.minestuck.data.tag;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.world.biome.MSBiomes;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import static com.mraof.minestuck.util.MSTags.Biomes.*;

public class MinestuckBiomeTagsProvider extends BiomeTagsProvider
{
	public MinestuckBiomeTagsProvider(DataGenerator pGenerator, @Nullable ExistingFileHelper existingFileHelper)
	{
		super(pGenerator, Minestuck.MOD_ID, existingFileHelper);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void addTags()
	{
		this.tag(LAND_NORMAL).add(MSBiomes.DEFAULT_LAND.NORMAL, MSBiomes.NO_RAIN_LAND.NORMAL, MSBiomes.SNOW_LAND.NORMAL, MSBiomes.HIGH_HUMID_LAND.NORMAL);
		this.tag(LAND_OCEAN).add(MSBiomes.DEFAULT_LAND.OCEAN, MSBiomes.NO_RAIN_LAND.OCEAN, MSBiomes.SNOW_LAND.OCEAN, MSBiomes.HIGH_HUMID_LAND.OCEAN);
		this.tag(LAND_ROUGH).add(MSBiomes.DEFAULT_LAND.ROUGH, MSBiomes.NO_RAIN_LAND.ROUGH, MSBiomes.SNOW_LAND.ROUGH, MSBiomes.HIGH_HUMID_LAND.ROUGH);
		this.tag(LAND).addTags(LAND_NORMAL, LAND_OCEAN, LAND_ROUGH);
		
		this.tag(HAS_LAND_GATE).addTag(LAND);
		this.tag(HAS_SMALL_RUIN).addTag(LAND_NORMAL);
		this.tag(HAS_IMP_DUNGEON).addTags(LAND_NORMAL, LAND_ROUGH);
		this.tag(HAS_CONSORT_VILLAGE).addTag(LAND_NORMAL);
		
		this.tag(HAS_SKAIA_CASTLE).add(MSBiomes.SKAIA);
	}
}
