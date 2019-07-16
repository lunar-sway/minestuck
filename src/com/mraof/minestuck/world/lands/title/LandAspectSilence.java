package com.mraof.minestuck.world.lands.title;

import com.mraof.minestuck.world.lands.LandDimension;
import com.mraof.minestuck.world.lands.decorator.SingleBlockDecorator;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;
import com.mraof.minestuck.world.lands.terrain.TerrainLandAspect;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Random;

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
	public void prepareWorldProvider(LandDimension worldProvider)
	{
		worldProvider.skylightBase = Math.min(1/2F, worldProvider.skylightBase);
		worldProvider.mergeFogColor(new Vec3d(0, 0, 0.1), 0.5F);
	}
	
	@Override
	public void prepareChunkProvider(ChunkProviderLands chunkProvider)
	{
	}
	
	@Override
	public void prepareChunkProviderServer(ChunkProviderLands chunkProvider)
	{
		chunkProvider.blockRegistry.setBlockState("structure_wool_2", Blocks.BLACK_WOOL.getDefaultState());
		chunkProvider.blockRegistry.setBlockState("carpet", Blocks.BLUE_CARPET.getDefaultState());
		chunkProvider.decorators.add(new PumpkinDecorator());
		if(chunkProvider.blockRegistry.getCustomBlock("torch") == null)
			chunkProvider.blockRegistry.setBlockState("torch", Blocks.REDSTONE_TORCH.getDefaultState());
	}
	
	@Override
	public boolean isAspectCompatible(TerrainLandAspect aspect)
	{
		return (aspect.getWeatherType() == -1 || (aspect.getWeatherType() & 1) == 1)/*snow is quiet, rain is noisy*/;
	}
	
	private static class PumpkinDecorator extends SingleBlockDecorator
	{
		@Override
		public BlockState pickBlock(Random random)
		{
			return Blocks.PUMPKIN.getDefaultState();//.with(BlockPumpkin.FACING, EnumFacing.Plane.HORIZONTAL.random(random));
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