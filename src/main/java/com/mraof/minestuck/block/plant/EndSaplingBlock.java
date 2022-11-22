package com.mraof.minestuck.block.plant;

import com.mraof.minestuck.block.MSProperties;
import com.mraof.minestuck.util.MSTags;
import com.mraof.minestuck.world.gen.feature.tree.EndTree;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Random;

public class EndSaplingBlock extends BushBlock implements BonemealableBlock
{
	public static final BooleanProperty ALPHA = MSProperties.ALPHA;
	public static final BooleanProperty OMEGA = MSProperties.OMEGA;
	protected static final VoxelShape SHAPE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 12.0D, 14.0D);
	
	private final AbstractTreeGrower tree = new EndTree();
	
	public EndSaplingBlock(Properties properties)
	{
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(ALPHA, false).setValue(OMEGA, false));
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
	{
		return SHAPE;
	}
	
	@Override
	public boolean isValidBonemealTarget(BlockGetter level, BlockPos pos, BlockState state, boolean isClient)
	{
		return true;
	}
	
	@Override
	public boolean isBonemealSuccess(Level level, Random rand, BlockPos pos, BlockState state)
	{
		return true;
	}
	
	/**
	 * Randomly selects one of the two internal booleans, alpha and omega, and toggles it.
	 * If Alpha is true and omega is false, then the tree will generate.
	 */
	@Override
	public void performBonemeal(ServerLevel level, Random rand, BlockPos pos, BlockState state)
	{
		if(level.isClientSide || level.dimensionType().moonPhase(level.getDayTime()) == 4)
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
			generateTree(level, pos, state, rand);
		} else
		{
			level.setBlockAndUpdate(pos, state);
		}
	}
	
	private void generateTree(ServerLevel level, BlockPos pos, BlockState state, Random rand)
	{
		if(!net.minecraftforge.event.ForgeEventFactory.saplingGrowTree(level, rand, pos))
			return;
		tree.growTree(level, level.getChunkSource().getGenerator(), pos, state, rand);
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		builder.add(ALPHA, OMEGA);
	}
	
	@Override
	public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos)
	{
		BlockPos groundPos = pos.below();
		return mayPlaceOn(level.getBlockState(groundPos), level, groundPos);
	}
	
	@Override
	protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos)
	{
		return state.is(MSTags.Blocks.END_SAPLING_DIRT);
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public void tick(BlockState state, ServerLevel level, BlockPos pos, Random random)
	{
		if (!level.isClientSide)
		{
			super.tick(state, level, pos, random);
			
			if (isValidBonemealTarget(level, pos, state, false) && random.nextInt(7) == 0)	//The world is not remote, therefore the side is not client.
			{
				this.performBonemeal(level, random, pos, state);
			}
		}
	}
}