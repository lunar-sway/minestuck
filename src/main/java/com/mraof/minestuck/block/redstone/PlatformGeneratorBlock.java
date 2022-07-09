package com.mraof.minestuck.block.redstone;

import com.mraof.minestuck.block.MSDirectionalBlock;
import com.mraof.minestuck.effects.CreativeShockEffect;
import com.mraof.minestuck.tileentity.redstone.PlatformGeneratorTileEntity;
import com.mraof.minestuck.util.ParticlesAroundSolidBlock;
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
 * When powered, the tile entity creates a line of platform blocks in the direction it is facing.
 * These blocks will generate even if there is a physical barrier between the generator and the end of the line, but only replace air or fluid blocks.
 * Right clicking the block toggles whether the generated platform blocks are visible
 */
public class PlatformGeneratorBlock extends MSDirectionalBlock
{
	public static final IntegerProperty POWER = BlockStateProperties.POWER;
	public static final BooleanProperty POWERED = BlockStateProperties.POWERED; //used for texture purposes
	public static final BooleanProperty INVISIBLE_MODE = BlockStateProperties.ENABLED;
	
	public PlatformGeneratorBlock(Properties properties)
	{
		super(properties);
		registerDefaultState(stateDefinition.any().setValue(FACING, Direction.UP).setValue(POWER, 0).setValue(POWERED, false).setValue(INVISIBLE_MODE, false));
	}
	
	@Override
	public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
	{
		if(!CreativeShockEffect.doesCreativeShockLimit(player, CreativeShockEffect.LIMIT_MACHINE_INTERACTIONS))
		{
			worldIn.setBlock(pos, state.cycle(INVISIBLE_MODE), Constants.BlockFlags.DEFAULT);
			worldIn.playSound(null, pos, SoundEvents.UI_BUTTON_CLICK, SoundCategory.BLOCKS, 0.5F, state.getValue(INVISIBLE_MODE) ? 1.5F : 0.5F);
		
			return ActionResultType.SUCCESS;
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
		return new PlatformGeneratorTileEntity();
	}
	
	@Override
	public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
	{
		super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
		updatePower(worldIn, pos);
	}
	
	@Override
	public void onPlace(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving)
	{
		super.onPlace(state, worldIn, pos, oldState, isMoving);
		updatePower(worldIn, pos);
	}
	
	public void updatePower(World worldIn, BlockPos pos)
	{
		if(!worldIn.isClientSide)
		{
			BlockState state = worldIn.getBlockState(pos);
			int powerInt = worldIn.getBestNeighborSignal(pos);
			
			if(state.getValue(POWER) != powerInt)
				worldIn.setBlockAndUpdate(pos, state.setValue(POWER, powerInt));
			else worldIn.sendBlockUpdated(pos, state, state, 2);
			
			if(state.getValue(POWERED) != powerInt > 0)
				worldIn.setBlockAndUpdate(pos, state.setValue(POWERED, powerInt > 0));
			else worldIn.sendBlockUpdated(pos, state, state, 2);
		}
	}
	
	@Override
	public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand)
	{
		if(rand.nextInt(15) < stateIn.getValue(POWER))
		{
			ParticlesAroundSolidBlock.spawnParticles(worldIn, pos, () -> RedstoneParticleData.REDSTONE);
		}
	}
	
	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder)
	{
		super.createBlockStateDefinition(builder);
		builder.add(POWER);
		builder.add(POWERED);
		builder.add(INVISIBLE_MODE);
	}
}
