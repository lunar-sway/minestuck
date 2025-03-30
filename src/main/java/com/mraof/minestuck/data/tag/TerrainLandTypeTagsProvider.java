package com.mraof.minestuck.data.tag;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.world.lands.LandTypes;
import com.mraof.minestuck.world.lands.terrain.TerrainLandType;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.concurrent.CompletableFuture;

import static com.mraof.minestuck.util.MSTags.TerrainLandTypes.*;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class TerrainLandTypeTagsProvider extends IntrinsicHolderTagsProvider<TerrainLandType>
{
	public TerrainLandTypeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper)
	{
		super(output, LandTypes.TERRAIN_KEY, lookupProvider, TerrainLandTypeTagsProvider::keyForLandType, Minestuck.MOD_ID, existingFileHelper);
	}
	
	private static ResourceKey<TerrainLandType> keyForLandType(TerrainLandType landType)
	{
		return LandTypes.TERRAIN_REGISTRY.getResourceKey(landType).orElseThrow();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	protected void addTags(HolderLookup.Provider provider)
	{
		this.tag(FOREST).add(LandTypes.FOREST.get(), LandTypes.TAIGA.get());
		this.tag(ROCK).add(LandTypes.ROCK.get(), LandTypes.PETRIFICATION.get());
		this.tag(SAND).add(LandTypes.SAND.get(), LandTypes.RED_SAND.get(), LandTypes.LUSH_DESERTS.get());
		this.tag(SANDSTONE).add(LandTypes.SANDSTONE.get(), LandTypes.RED_SANDSTONE.get());
		this.tag(IS_DESOLATE).addTags(SAND, SANDSTONE, ROCK);
		this.tag(IS_DANGEROUS).add(LandTypes.HEAT.get());
		this.tag(IS_FLUID_IMPORTANT).add(LandTypes.HEAT.get());
	}
	
	@Override
	public String getName()
	{
		return "Minestuck Terrain Land Type Tags";
	}
}
