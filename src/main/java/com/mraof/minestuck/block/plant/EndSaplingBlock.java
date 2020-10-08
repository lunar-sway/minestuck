package com.mraof.minestuck.block.plant;

import com.mraof.minestuck.block.MSProperties;
import com.mraof.minestuck.util.MSTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BushBlock;
import net.minecraft.block.IGrowable;
import net.minecraft.block.trees.Tree;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class EndSaplingBlock extends BushBlock implements IGrowable
{
	public static final BooleanProperty ALPHA = MSProperties.ALPHA;
	public static final BooleanProperty OMEGA = MSProperties.OMEGA;
	protected static final VoxelShape SHAPE = Block.makeCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 12.0D, 14.0D);
	
	
	private final Tree tree = null;//new EndTree(); TODO
	
	public EndSaplingBlock(Properties properties)
	{
		super(properties);
		this.setDefaultState(this.stateContainer.getBaseState().with(ALPHA, false).with(OMEGA, false));
	}
	
	@Override
	@SuppressWarnings("deprecation")
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
	public void grow(ServerWorld worldIn, Random rand, BlockPos pos, BlockState state)
	{
		if(worldIn.isRemote || worldIn.getDimensionType().getMoonPhase(worldIn.getDayTime()) == 4)
		{
			return;
		}
		if(rand.nextFloat() < 0.5)
		{
			state = state.func_235896_a_(ALPHA);	//cycle
		} else
		{
			state = state.func_235896_a_(OMEGA);
		}
		
		if(state.get(ALPHA) && !state.get(OMEGA))
		{
			generateTree(worldIn, pos, state, rand);
		} else
		{
			worldIn.setBlockState(pos, state);
		}
	}
	
	private void generateTree(ServerWorld worldIn, BlockPos pos, BlockState state, Random rand)
	{
		if(!net.minecraftforge.event.ForgeEventFactory.saplingGrowTree(worldIn, rand, pos))
			return;
		tree.attemptGrowTree(worldIn, worldIn.getChunkProvider().getChunkGenerator(), pos, state, rand);
	}
	
	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
	{
		builder.add(ALPHA, OMEGA);
	}
	
	@Override
	public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos)
	{
		BlockPos groundPos = pos.down();
		return isValidGround(worldIn.getBlockState(groundPos), worldIn, groundPos);
	}
	
	@Override
	protected boolean isValidGround(BlockState state, IBlockReader worldIn, BlockPos pos)
	{
		return MSTags.Blocks.END_SAPLING_DIRT.contains(state.getBlock());
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random)
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