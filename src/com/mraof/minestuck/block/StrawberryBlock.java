package com.mraof.minestuck.block;

import com.mraof.minestuck.item.MinestuckItems;
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
	
	protected StrawberryBlock(Properties properties)
	{
		super(properties);
		
		setDefaultState(getDefaultState().with(FACING, Direction.UP));
	}
	
	
	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
	{
		builder.add(FACING);
	}
	
	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context)
	{
		Direction direction = context.getNearestLookingDirection();
		return this.getDefaultState().with(FACING, direction);
	}
	
	@Override
	public StemBlock getStem()
	{
		return (StemBlock) MinestuckBlocks.STRAWBERRY_STEM;
	}
	
	@Override
	public AttachedStemBlock getAttachedStem()
	{
		return (AttachedStemBlock) MinestuckBlocks.ATTACHED_STRAWBERRY_STEM;
	}
	
	public static class AttachedStem extends AttachedStemBlock
	{
		public AttachedStem(StemGrownBlock crop, Properties properties)
		{
			super(crop, properties);
		}
		
		@Override
		protected Item getSeeds()
		{
			return MinestuckItems.STRAWBERRY_CHUNK;
		}
	}
	
	public static class Stem extends StemBlock
	{
		public Stem(StemGrownBlock crop, Properties properties)
		{
			super(crop, properties);
		}
		
		@Nullable
		@Override
		protected Item getSeedItem()
		{
			return MinestuckItems.STRAWBERRY_CHUNK;
		}
	}
}