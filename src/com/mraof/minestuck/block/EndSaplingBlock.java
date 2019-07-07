package com.mraof.minestuck.block;

import java.util.Random;

import net.minecraft.block.*;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class EndSaplingBlock extends BushBlock implements IGrowable
{
	public static final BooleanProperty ALPHA = MinestuckProperties.ALPHA;
	public static final BooleanProperty OMEGA = MinestuckProperties.OMEGA;
	protected static final VoxelShape SHAPE = Block.makeCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 12.0D, 14.0D);
	
	protected EndSaplingBlock(Properties properties)
	{
		super(properties);
		this.setDefaultState(this.stateContainer.getBaseState().with(ALPHA, false).with(OMEGA, false));
	}
	
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext contezt)
	{
		return SHAPE;
	}
	
	@Override
	public boolean canGrow(IBlockReader worldIn, BlockPos pos, BlockState state, boolean isClient)
	{
		return true;
	}
	
	@Override
	public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, BlockState state)
	{
		return true;
	}
	
	/**
	 * Randomly selects one of the two internal booleans, alpha and omega, and toggles it.
	 * If Alpha is true and omega is false, then the tree will generate.
	 */
	@Override
	public void grow(World worldIn, Random rand, BlockPos pos, BlockState state)
	{
		if(worldIn.isRemote || worldIn.getMoonPhase() == 4)
		{
			return;
		}
		boolean alpha = state.get(ALPHA);
		boolean omega = state.get(OMEGA);
		
		if(rand.nextFloat() < 0.5)
		{
			alpha = !alpha;
			state = state.cycle(ALPHA);
		} else
		{
			omega = !omega;
			state = state.cycle(OMEGA);
		}
		
		if(alpha && !omega)
		{
			generateTree(worldIn, pos, state, rand);
		} else
		{
			worldIn.setBlockState(pos, state);
		}
	}
	
	private void generateTree(World worldIn, BlockPos pos, BlockState state, Random rand)
	{
		if(!net.minecraftforge.event.ForgeEventFactory.saplingGrowTree(worldIn, rand, pos))
			return;
		//AbstractTree tree = new EndTree(true);
		//tree.spawn(worldIn, pos, state, rand);
	}
	
	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
	{
		builder.add(ALPHA, OMEGA);
	}
	
	@Override
	protected boolean isValidGround(BlockState state, IBlockReader worldIn, BlockPos pos)
	{
		return state.getBlock() == Blocks.END_STONE || state.getBlock() == MinestuckBlocks.COARSE_END_STONE || state.getBlock() == MinestuckBlocks.END_GRASS;
	}
	
	@Override
	public void tick(BlockState state, World worldIn, BlockPos pos, Random random)
	{
		if (!worldIn.isRemote)
		{
			super.tick(state, worldIn, pos, random);
			
			if (canGrow(worldIn, pos, state, false) && random.nextInt(7) == 0)	//The world is not remote, therefore the side is not client.
			{
				this.grow(worldIn, random, pos, state);
			}
		}
	}
}