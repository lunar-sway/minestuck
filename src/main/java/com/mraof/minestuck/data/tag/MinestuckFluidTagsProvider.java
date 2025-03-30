package com.mraof.minestuck.data.tag;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.util.MSTags.Fluids;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.FluidTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

import static com.mraof.minestuck.fluid.MSFluids.*;

public class MinestuckFluidTagsProvider extends FluidTagsProvider
{
	public MinestuckFluidTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper)
	{
		super(output, lookupProvider, Minestuck.MOD_ID, existingFileHelper);
	}
	
	@Override
	protected void addTags(HolderLookup.Provider provider)
	{
		tag(Fluids.OIL).add(OIL.get(), FLOWING_OIL.get());
		tag(Fluids.BLOOD).add(BLOOD.get(), FLOWING_BLOOD.get());
		tag(Fluids.BRAIN_JUICE).add(BRAIN_JUICE.get(), FLOWING_BRAIN_JUICE.get());
		tag(Fluids.WATER_COLORS).add(WATER_COLORS.get(), FLOWING_WATER_COLORS.get());
		tag(Fluids.ENDER).add(ENDER.get(), FLOWING_ENDER.get());
		tag(Fluids.LIGHT_WATER).add(LIGHT_WATER.get(), FLOWING_LIGHT_WATER.get());
	}
	
	@Override
	public String getName()
	{
		return "Minestuck Fluid Tags";
	}
}