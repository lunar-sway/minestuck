package com.mraof.minestuck.world.lands.title;

import com.mraof.minestuck.util.EnumAspect;
import com.mraof.minestuck.world.lands.LandDimension;
import com.mraof.minestuck.world.lands.decorator.structure.CogDecorator;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;
import com.mraof.minestuck.world.lands.structure.blocks.StructureBlockRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.Vec3d;

public class ClockworkLandAspect extends TitleLandAspect
{
	public ClockworkLandAspect()
	{
		super(EnumAspect.TIME);
	}
	
	@Override
	public String[] getNames()
	{
		return new String[]{"clockwork", "gear"};
	}
	
	@Override
	public void registerBlocks(StructureBlockRegistry registry)
	{
		registry.setBlockState("structure_wool_2", Blocks.LIGHT_GRAY_WOOL.getDefaultState());
		registry.setBlockState("carpet", Blocks.RED_CARPET.getDefaultState());
	}
	
	@Override
	public void prepareWorldProvider(LandDimension worldProvider)
	{
		worldProvider.mergeFogColor(new Vec3d(0.5, 0.5, 0.5), 0.5F);
	}
	
	//@Override
	public void prepareChunkProviderServer(ChunkProviderLands chunkProvider)
	{
		chunkProvider.decorators.add(new CogDecorator());
		//chunkProvider.sortDecorators();
	}
	
}
