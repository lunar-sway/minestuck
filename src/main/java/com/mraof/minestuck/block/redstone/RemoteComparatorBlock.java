package com.mraof.minestuck.block.redstone;

import com.mraof.minestuck.block.BlockUtil;
import com.mraof.minestuck.block.MSDirectionalBlock;
import com.mraof.minestuck.block.MSProperties;
import com.mraof.minestuck.effects.CreativeShockEffect;
import com.mraof.minestuck.tileentity.redstone.RemoteComparatorTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.Random;

/**
 * Will send out a redstone signal on its sides if the block it is observing(of a distance set by DISTANCE_1_16) matches the block directly behind it.
 * Checks if the blockstates match as well if CHECK_STATE is true.
 */
public class RemoteComparatorBlock extends MSDirectionalBlock
{
	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
	public static final BooleanProperty CHECK_STATE = MSProperties.MACHINE_TOGGLE;
	public static final IntegerProperty DISTANCE_1_16 = MSProperties.DISTANCE_1_16;
	
	public RemoteComparatorBlock(Properties properties)
	{
		super(properties);
		this.registerDefaultState(stateDefinition.any().setValue(FACING, Direction.UP).setValue(POWERED, false).setValue(DISTANCE_1_16, 1).setValue(CHECK_STATE, false));
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
	{
		if(!CreativeShockEffect.doesCreativeShockLimit(player, CreativeShockEffect.LIMIT_MACHINE_INTERACTIONS))
		{
			if(!player.isCrouching())
			{
				if(state.getValue(DISTANCE_1_16) != 16) //increases property until it gets to the highest value at which it resets
				{
					worldIn.setBlock(pos, state.setValue(DISTANCE_1_16, state.getValue(DISTANCE_1_16) + 1), Constants.BlockFlags.DEFAULT);
					worldIn.playSound(null, pos, SoundEvents.PISTON_EXTEND, SoundCategory.BLOCKS, 0.5F, 1.2F);
				} else
				{
					worldIn.setBlock(pos, state.setValue(DISTANCE_1_16, 1), Constants.BlockFlags.DEFAULT);
					worldIn.playSound(null, pos, SoundEvents.PISTON_CONTRACT, SoundCategory.BLOCKS, 0.5F, 1.2F);
				}
				
				return ActionResultType.sidedSuccess(worldIn.isClientSide);
			} else if(player.isCrouching())
			{
				worldIn.setBlock(pos, state.cycle(CHECK_STATE), Constants.BlockFlags.DEFAULT);
				if(state.getValue(CHECK_STATE))
					worldIn.playSound(null, pos, SoundEvents.UI_BUTTON_CLICK, SoundCategory.BLOCKS, 0.5F, 1.5F);
				else
					worldIn.playSound(null, pos, SoundEvents.UI_BUTTON_CLICK, SoundCategory.BLOCKS, 0.5F, 0.5F);
				
				return ActionResultType.sidedSuccess(worldIn.isClientSide);
			}
		}
		
		return ActionResultType.PASS;
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
		return new RemoteComparatorTileEntity();
	}
	
	public static boolean isMatch(World world, BlockPos comparatorPos)
	{
		BlockState comparatorState = world.getBlockState(comparatorPos);
		BlockPos posToCheck = comparatorPos.relative(comparatorState.getValue(FACING), comparatorState.getValue(DISTANCE_1_16));
		
		if(world.isAreaLoaded(posToCheck, 0) && world.isAreaLoaded(comparatorPos, 1))
		{
			BlockState checkingState = world.getBlockState(posToCheck);
			BlockState referenceState = world.getBlockState(comparatorPos.relative(comparatorState.getValue(FACING).getOpposite())); //state behind the comparator
			
			if(comparatorState.getValue(CHECK_STATE))
			{
				return checkingState == referenceState;
			} else
			{
				return checkingState.getBlock() == referenceState.getBlock();
			}
		} else
			return false;
	}
	
	@Override
	public boolean isSignalSource(BlockState state)
	{
		return state.getValue(POWERED);
	}
	
	@Override
	public int getSignal(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side)
	{
		return blockState.getValue(POWERED) ? 15 : 0;
	}
	
	@Override
	public boolean canConnectRedstone(BlockState state, IBlockReader world, BlockPos pos, @Nullable Direction side)
	{
		return true;
	}
	
	@Override
	public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand)
	{
		if(stateIn.getValue(POWERED))
			BlockUtil.spawnParticlesAroundSolidBlock(worldIn, pos, () -> RedstoneParticleData.REDSTONE);
	}
	
	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder)
	{
		super.createBlockStateDefinition(builder);
		builder.add(POWERED);
		builder.add(CHECK_STATE);
		builder.add(DISTANCE_1_16);
	}
}