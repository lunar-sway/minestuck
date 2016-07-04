package com.mraof.minestuck.world.lands.title;

import java.util.Random;

import net.minecraft.block.BlockPumpkin;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import com.mraof.minestuck.world.lands.decorator.SingleBlockDecorator;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;
import com.mraof.minestuck.world.lands.terrain.TerrainLandAspect;

public class LandAspectSilence extends TitleLandAspect
{
	
	@Override
	public String getPrimaryName()
	{
		return "silence";
	}
	
	@Override
	public String[] getNames()
	{
		return new String[]{"silence"};
	}
	
	@Override
	protected void prepareChunkProvider(ChunkProviderLands chunkProvider)
	{
		chunkProvider.dayCycle = 2;
		
		chunkProvider.mergeFogColor(new Vec3d(0, 0, 0.1), 0.5F);
		
		if(chunkProvider.decorators != null)
		{
			chunkProvider.decorators.add(new PumpkinDecorator());
		}
	}
	
	@Override
	public boolean isAspectCompatible(TerrainLandAspect aspect)
	{
		return (aspect.getWeatherType() == -1 || (aspect.getWeatherType() & 1) != 0)/*rain is noisy*/ && aspect.getDayCycleMode() != 1;
	}
	
	private static class PumpkinDecorator extends SingleBlockDecorator
	{
		@Override
		public IBlockState pickBlock(Random random)
		{
			return Blocks.PUMPKIN.getDefaultState().withProperty(BlockPumpkin.FACING, EnumFacing.Plane.HORIZONTAL.random(random));
		}
		@Override
		public int getCount(Random random)
		{
			return random.nextFloat() < 0.01 ? 1 : 0;
		}
		@Override
		public boolean canPlace(BlockPos pos, World world)
		{
			return !world.getBlockState(pos.down()).getMaterial().isLiquid();
		}
	}
}