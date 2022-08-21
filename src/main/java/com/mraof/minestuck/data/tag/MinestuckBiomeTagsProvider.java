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
	
	@Override
	protected void addTags()
	{
		this.tag(HAS_SKAIA_CASTLE).add(MSBiomes.SKAIA);
	}
}
