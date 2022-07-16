package com.mraof.minestuck.block.redstone;

import com.mraof.minestuck.block.BlockUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

/**
 * Gives off a full power redstone signal if a block is resting above it or a player is standing on it without crouching
 */
public class BlockPressurePlateBlock extends Block
{
	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
	
	public BlockPressurePlateBlock(Properties properties)
	{
		super(properties);
		this.registerDefaultState(stateDefinition.any().setValue(POWERED, false));
	}
	
	@Override
	public void stepOn(World worldIn, BlockPos pos, Entity entityIn)
	{
		BlockState state = worldIn.getBlockState(pos);
		
		if(entityIn instanceof PlayerEntity)
		{
			tryDepressPlate(worldIn, pos, state, true);
		}
	}
	
	@Override
	public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random)
	{
		super.tick(state, worldIn, pos, random);
		
		if(!worldIn.isClientSide)
		{
			AxisAlignedBB checkBB = new AxisAlignedBB(pos);
			List<PlayerEntity> list = worldIn.getEntitiesOfClass(PlayerEntity.class, checkBB.move(0, 0.5, 0));
			boolean entityStandingOnBlock = list.stream().anyMatch(playerEntity -> playerEntity.isOnGround() && !playerEntity.isCrouching());
			
			if(!entityStandingOnBlock && !isAboveBlockFullyTouching(worldIn, pos.above()) && state.getValue(POWERED))
			{
				worldIn.setBlock(pos, state.setValue(POWERED, false), Constants.BlockFlags.DEFAULT);
				worldIn.playSound(null, pos, SoundEvents.PISTON_EXTEND, SoundCategory.BLOCKS, 0.5F, 1.2F);
			} else if(entityStandingOnBlock)
			{
				worldIn.getBlockTicks().scheduleTick(new BlockPos(pos), this, 20);
			}
		}
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
	public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
	{
		super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
		tryDepressPlate(worldIn, pos, state, false);
	}
	
	@Override
	public void onPlace(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving)
	{
		super.onPlace(state, worldIn, pos, oldState, isMoving);
		tryDepressPlate(worldIn, pos, state, false);
	}
	
	public static boolean isAboveBlockFullyTouching(World worldIn, BlockPos abovePos)
	{
		return worldIn.getBlockState(abovePos).isFaceSturdy(worldIn, abovePos, Direction.DOWN);
	}
	
	/**
	 * Will depress the plate assuming the block is being stepped on by a player or while a suitable block is above it, causing it to become powered
	 */
	public void tryDepressPlate(World worldIn, BlockPos pos, BlockState state, boolean steppedOn)
	{
		BlockPos abovePos = pos.above();
		if((isAboveBlockFullyTouching(worldIn, abovePos) || steppedOn) && !state.getValue(POWERED))
		{
			worldIn.playSound(null, pos, SoundEvents.PISTON_CONTRACT, SoundCategory.BLOCKS, 0.5F, 1.2F);
			worldIn.setBlock(pos, state.setValue(POWERED, true), Constants.BlockFlags.DEFAULT);
		}
		
		worldIn.getBlockTicks().scheduleTick(new BlockPos(pos), this, 20);
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
	}
}