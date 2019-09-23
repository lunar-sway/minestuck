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
		getBuilder(Fluids.OIL).add(OIL, FLOWING_OIL);
		getBuilder(Fluids.BLOOD).add(BLOOD, FLOWING_BLOOD);
		getBuilder(Fluids.BRAIN_JUICE).add(BRAIN_JUICE, FLOWING_BRAIN_JUICE);
		getBuilder(Fluids.WATER_COLORS).add(WATER_COLORS, FLOWING_WATER_COLORS);
		getBuilder(Fluids.ENDER).add(ENDER, FLOWING_ENDER);
		getBuilder(Fluids.LIGHT_WATER).add(LIGHT_WATER, FLOWING_LIGHT_WATER);
	}
	
	@Override
	public String getName()
	{
		return "Minestuck Fluid Tags";
	}
}
