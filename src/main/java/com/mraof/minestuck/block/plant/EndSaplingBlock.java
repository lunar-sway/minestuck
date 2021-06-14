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
	protected static final VoxelShape SHAPE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 12.0D, 14.0D);
	
	
	private final Tree tree = null;//new EndTree(); TODO
	
	public EndSaplingBlock(Properties properties)
	{
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(ALPHA, false).setValue(OMEGA, false));
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext contezt)
	{
		return SHAPE;
	}
	
	@Override
	public boolean isValidBonemealTarget(IBlockReader worldIn, BlockPos pos, BlockState state, boolean isClient)
	{
		return true;
	}
	
	@Override
	public boolean isBonemealSuccess(World worldIn, Random rand, BlockPos pos, BlockState state)
	{
		return true;
	}
	
	/**
	 * Randomly selects one of the two internal booleans, alpha and omega, and toggles it.
	 * If Alpha is true and omega is false, then the tree will generate.
	 */
	@Override
	public void performBonemeal(ServerWorld worldIn, Random rand, BlockPos pos, BlockState state)
	{
		if(worldIn.isClientSide || worldIn.dimensionType().moonPhase(worldIn.getDayTime()) == 4)
		{
			return;
		}
		if(rand.nextFloat() < 0.5)
		{
			state = state.cycle(ALPHA);
		} else
		{
			state = state.cycle(OMEGA);
		}
		
		if(state.getValue(ALPHA) && !state.getValue(OMEGA))
		{
			generateTree(worldIn, pos, state, rand);
		} else
		{
			worldIn.setBlockAndUpdate(pos, state);
		}
	}
	
	private void generateTree(ServerWorld worldIn, BlockPos pos, BlockState state, Random rand)
	{
		if(!net.minecraftforge.event.ForgeEventFactory.saplingGrowTree(worldIn, rand, pos))
			return;
		tree.growTree(worldIn, worldIn.getChunkSource().getGenerator(), pos, state, rand);
	}
	
	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder)
	{
		builder.add(ALPHA, OMEGA);
	}
	
	@Override
	public boolean canSurvive(BlockState state, IWorldReader worldIn, BlockPos pos)
	{
		BlockPos groundPos = pos.below();
		return mayPlaceOn(worldIn.getBlockState(groundPos), worldIn, groundPos);
	}
	
	@Override
	protected boolean mayPlaceOn(BlockState state, IBlockReader worldIn, BlockPos pos)
	{
		return MSTags.Blocks.END_SAPLING_DIRT.contains(state.getBlock());
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random)
	{
		if (!worldIn.isClientSide)
		{
			super.tick(state, worldIn, pos, random);
			
			if (isValidBonemealTarget(worldIn, pos, state, false) && random.nextInt(7) == 0)	//The world is not remote, therefore the side is not client.
			{
				this.performBonemeal(worldIn, random, pos, state);
			}
		}
	}
}