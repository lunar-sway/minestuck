package com.mraof.minestuck.block.plant;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.item.MSItems;
import net.minecraft.block.*;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;

import javax.annotation.Nullable;

public class StrawberryBlock extends StemGrownBlock
{
	public static final DirectionProperty FACING = BlockStateProperties.FACING;
	
	public StrawberryBlock(Properties properties)
	{
		super(properties);
		
		registerDefaultState(defaultBlockState().setValue(FACING, Direction.UP));
	}
	
	
	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder)
	{
		builder.add(FACING);
	}
	
	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context)
	{
		Direction direction = context.getNearestLookingDirection();
		return this.defaultBlockState().setValue(FACING, direction);
	}
	
	@Override
	public StemBlock getStem()
	{
		return MSBlocks.STRAWBERRY_STEM;
	}
	
	@Override
	public AttachedStemBlock getAttachedStem()
	{
		return MSBlocks.ATTACHED_STRAWBERRY_STEM;
	}
}