package com.mraof.minestuck.block.redstone;

import com.mraof.minestuck.tileentity.redstone.SummonerTileEntity;
import com.mraof.minestuck.util.Debug;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class SummonerBlock extends Block
{
	public static final BooleanProperty TRIGGERED = BlockStateProperties.TRIGGERED;
	
	public SummonerBlock(Properties properties)
	{
		super(properties);
		setDefaultState(getDefaultState().with(TRIGGERED, false));
	}
	
	@Override
	public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
	{
		super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
		boolean blockPowered = worldIn.isBlockPowered(pos) || worldIn.isBlockPowered(pos.up());
		//Debug.debugf("blockPowered = %s, triggered = %s", blockPowered, state.get(TRIGGERED));
		if(!worldIn.isRemote && blockPowered && !state.get(TRIGGERED))
		{
			TileEntity tileEntity = worldIn.getTileEntity(pos);
			Debug.debugf("summonerTileEntity = %s", tileEntity instanceof SummonerTileEntity);
			if(tileEntity instanceof SummonerTileEntity)
			{
				SummonerTileEntity summonerTE = (SummonerTileEntity) tileEntity;
				
				summonerTE.summonEntity(worldIn, state, pos, true);
			}
			//worldIn.getPendingBlockTicks().scheduleTick(pos, this, this.tickRate(worldIn));
			worldIn.setBlockState(pos, state.with(TRIGGERED, true), 4);
		}
	}
	
	@Override
	public boolean hasTileEntity(BlockState state)
	{
		return true;
	}
	
	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world)
	{
		return new SummonerTileEntity();
	}
	
	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
	{
		super.fillStateContainer(builder);
		builder.add(TRIGGERED);
	}
}
