package com.mraof.minestuck.block.redstone;

import com.mraof.minestuck.block.MSDirectionalBlock;
import com.mraof.minestuck.block.MSProperties;
import com.mraof.minestuck.tileentity.redstone.TogglerTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.Property;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.ItemTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;

/**
 * Toggles the state of the block it is facing upon receiving a new redstone signal if that block has the MACHINE_TOGGLE property from MSProperties
 */
public class TogglerBlock extends MSDirectionalBlock
{
	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
	public static final BooleanProperty DISCHARGE = MSProperties.MACHINE_TOGGLE;
	
	public TogglerBlock(Properties properties)
	{
		super(properties);
		this.registerDefaultState(stateDefinition.any().setValue(FACING, Direction.UP).setValue(POWERED, false).setValue(DISCHARGE, false));
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
		return new TogglerTileEntity();
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
	{
		worldIn.setBlock(pos, state.cycle(DISCHARGE), Constants.BlockFlags.DEFAULT);
		if(state.getValue(DISCHARGE))
			worldIn.playSound(null, pos, SoundEvents.PISTON_EXTEND, SoundCategory.BLOCKS, 0.5F, 1.2F);
		else
			worldIn.playSound(null, pos, SoundEvents.PISTON_CONTRACT, SoundCategory.BLOCKS, 0.5F, 1.2F);
		return ActionResultType.SUCCESS;
	}
	
	@Override
	public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
	{
		super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
		if(fromPos != pos.relative(state.getValue(FACING)))
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
			boolean hasPower = false;
			Direction stateFacing = state.getValue(FACING);
			
			for(Direction direction : Direction.values()) //checks for a signal in any direction except the one it is facing
			{
				if(direction != stateFacing && worldIn.getSignal(pos.relative(direction), direction) > 0)
				{
					hasPower = true;
					break;
				}
			}
			
			boolean isPoweredBeforeUpdate = state.getValue(POWERED);
			
			if(state.getValue(POWERED) != hasPower)
				worldIn.setBlockAndUpdate(pos, state.setValue(POWERED, hasPower));
			else worldIn.sendBlockUpdated(pos, state, state, 2);
			
			if(!isPoweredBeforeUpdate && hasPower)
			{
				BlockPos facingPos = pos.relative(state.getValue(FACING));
				BlockState facingState = worldIn.getBlockState(facingPos);
				
				if(state.getValue(DISCHARGE))
				{
					boolean makeSound = false;
					
					//removing redstone power from powered blocks
					for(Property<?> property : facingState.getProperties())
					{
						if(facingState.isSignalSource())
						{
							if(property.equals(BlockStateProperties.POWERED) || property.equals(BlockStateProperties.POWER))
							{
								if(property.equals(BlockStateProperties.POWERED))
									worldIn.setBlock(facingPos, facingState.setValue(BlockStateProperties.POWERED, false), Constants.BlockFlags.DEFAULT);
								if(property.equals(BlockStateProperties.POWER))
									worldIn.setBlock(facingPos, facingState.setValue(BlockStateProperties.POWER, 0), Constants.BlockFlags.DEFAULT);
								
								if(!worldIn.getBlockState(pos.relative(state.getValue(FACING).getOpposite())).getBlock().asItem().is(ItemTags.WOOL)) //wont make a toggle sound if the toggler is "muted" by a wool block
								{
									makeSound = true;
								}
							}
						}
					}
					
					if(makeSound)
					{
						worldIn.playSound(null, facingPos, SoundEvents.UI_BUTTON_CLICK, SoundCategory.BLOCKS, 0.5F, 0.5F);
					}
				} else
				{
					for(Property<?> property : facingState.getProperties())
					{
						if(property.equals(MSProperties.MACHINE_TOGGLE) && !(facingState.getBlock() instanceof PlatformBlock)) //if it has the property and is not a platform block(platform block property should be toggled by the generator)
						{
							worldIn.setBlock(facingPos, facingState.cycle(MSProperties.MACHINE_TOGGLE), Constants.BlockFlags.DEFAULT);
							
							if(!worldIn.getBlockState(pos.relative(state.getValue(FACING).getOpposite())).getBlock().asItem().is(ItemTags.WOOL)) //wont make a toggle sound if the toggler is "muted" by a wool block
							{
								if(facingState.getValue(MSProperties.MACHINE_TOGGLE))
									worldIn.playSound(null, facingPos, SoundEvents.UI_BUTTON_CLICK, SoundCategory.BLOCKS, 0.5F, 1.5F);
								else
									worldIn.playSound(null, facingPos, SoundEvents.UI_BUTTON_CLICK, SoundCategory.BLOCKS, 0.5F, 0.5F);
							}
							
							break;
						}
					}
				}
			}
		}
	}
	
	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder)
	{
		super.createBlockStateDefinition(builder);
		builder.add(POWERED);
		builder.add(DISCHARGE);
	}
}