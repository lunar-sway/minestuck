package com.mraof.minestuck.data.tag;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.world.lands.LandTypes;
import com.mraof.minestuck.world.lands.terrain.TerrainLandType;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeRegistryTagsProvider;
import org.jetbrains.annotations.Nullable;

import static com.mraof.minestuck.util.MSTags.TerrainLandTypes.*;

@MethodsReturnNonnullByDefault
public final class TerrainLandTypeTagsProvider extends ForgeRegistryTagsProvider<TerrainLandType>
{
	public TerrainLandTypeTagsProvider(DataGenerator generator, @Nullable ExistingFileHelper existingFileHelper)
	{
		super(generator, LandTypes.TERRAIN_REGISTRY.get(), Minestuck.MOD_ID, existingFileHelper);
	}
	
	@Override
	protected void addTags()
	{
		this.tag(FOREST).add(LandTypes.FOREST.get(), LandTypes.TAIGA.get());
		this.tag(ROCK).add(LandTypes.ROCK.get(), LandTypes.PETRIFICATION.get());
		this.tag(SAND).add(LandTypes.SAND.get(), LandTypes.RED_SAND.get(), LandTypes.LUSH_DESERTS.get());
		this.tag(SANDSTONE).add(LandTypes.SANDSTONE.get(), LandTypes.RED_SANDSTONE.get());
	}
	
	@Override
	public String getName()
	{
		return "Minestuck Terrain Land Type Tags";
	}
}
