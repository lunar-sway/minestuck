package com.mraof.minestuck.block.plant;

import com.mraof.minestuck.block.MSBlocks;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.AttachedStemBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.StemBlock;
import net.minecraft.world.level.block.StemGrownBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

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
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		builder.add(FACING);
	}
	
	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context)
	{
		Direction direction = context.getNearestLookingDirection();
		return this.defaultBlockState().setValue(FACING, direction);
	}
	
	@Override
	public StemBlock getStem()
	{
		return MSBlocks.STRAWBERRY_STEM.get();
	}
	
	@Override
	public AttachedStemBlock getAttachedStem()
	{
		return MSBlocks.ATTACHED_STRAWBERRY_STEM.get();
	}
}