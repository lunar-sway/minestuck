package com.mraof.minestuck.block;

import com.mraof.minestuck.item.MinestuckItems;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.*;
import javax.annotation.Nullable;

public class BlockStrawberry extends BlockStemGrown
{
	public static final DirectionProperty FACING = BlockStateProperties.FACING;
	
	protected BlockStrawberry(Properties properties)
	{
		super(properties);
		
		setDefaultState(getDefaultState().with(FACING, EnumFacing.UP));
	}
	
	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, IBlockState> builder)
	{
		builder.add(FACING);
	}
	
	@Nullable
	@Override
	public IBlockState getStateForPlacement(BlockItemUseContext context)
	{
		EnumFacing facing = context.getNearestLookingDirection();
		return this.getDefaultState().with(FACING, facing);
	}
	
	@Override
	public BlockStem getStem()
	{
		return (BlockStem) MinestuckBlocks.STRAWBERRY_STEM;
	}
	
	@Override
	public BlockAttachedStem getAttachedStem()
	{
		return (BlockAttachedStem) MinestuckBlocks.ATTACHED_STRAWBERRY_STEM;
	}
	
	public static class AttachedStem extends BlockAttachedStem
	{
		public AttachedStem(BlockStemGrown crop, Properties properties)
		{
			super(crop, properties);
		}
		
		@Override
		protected Item getSeeds()
		{
			return MinestuckItems.strawberryChunk;
		}
	}
	
	public static class Stem extends BlockStem
	{
		public Stem(BlockStemGrown crop, Properties properties)
		{
			super(crop, properties);
		}
		
		@Nullable
		@Override
		protected Item getSeedItem()
		{
			return MinestuckItems.strawberryChunk;
		}
	}
}