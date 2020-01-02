package com.mraof.minestuck.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.FluidTagsProvider;

import static com.mraof.minestuck.fluid.MSFluids.*;
import com.mraof.minestuck.util.MSTags.Fluids;

public class MinestuckFluidTagsProvider extends FluidTagsProvider
{
	public MinestuckFluidTagsProvider(DataGenerator generator)
	{
		super(generator);
	}
	
	@Override
	protected void registerTags()
	{
		getBuilder(Fluids.OIL).add(OIL.get(), FLOWING_OIL.get());
		getBuilder(Fluids.BLOOD).add(BLOOD.get(), FLOWING_BLOOD.get());
		getBuilder(Fluids.BRAIN_JUICE).add(BRAIN_JUICE.get(), FLOWING_BRAIN_JUICE.get());
		getBuilder(Fluids.WATER_COLORS).add(WATER_COLORS.get(), FLOWING_WATER_COLORS.get());
		getBuilder(Fluids.ENDER).add(ENDER.get(), FLOWING_ENDER.get());
		getBuilder(Fluids.LIGHT_WATER).add(LIGHT_WATER.get(), FLOWING_LIGHT_WATER.get());
	}
	
	@Override
	public String getName()
	{
		return "Minestuck Fluid Tags";
	}
}