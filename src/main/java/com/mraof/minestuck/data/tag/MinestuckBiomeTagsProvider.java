package com.mraof.minestuck.data.tag;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.world.biome.MSBiomes;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.tags.BiomeTags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.concurrent.CompletableFuture;

import static com.mraof.minestuck.util.MSTags.Biomes.*;

@ParametersAreNonnullByDefault
public class MinestuckBiomeTagsProvider extends BiomeTagsProvider
{
	public MinestuckBiomeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper)
	{
		super(output, lookupProvider, Minestuck.MOD_ID, existingFileHelper);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void addTags(HolderLookup.Provider provider)
	{
		this.tag(HAS_FROG_TEMPLE).addTag(BiomeTags.IS_OVERWORLD);
		
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
