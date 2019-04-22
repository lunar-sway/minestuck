package com.mraof.minestuck.block;

import com.mraof.minestuck.tileentity.TileEntityPunchDesignix;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;
import java.util.Map;

public class BlockPunchDesignixSlot extends BlockPunchDesignix
{
	public static final BooleanProperty HAS_CARD = BooleanProperty.create("has_card");
	
	public BlockPunchDesignixSlot(Properties properties, Map<EnumFacing, VoxelShape> shape)
	{
		super(properties, shape, new BlockPos(0, 0, 0));
	}
	
	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, IBlockState> builder)
	{
		super.fillStateContainer(builder);
		builder.add(HAS_CARD);
	}
	
	@Override
	public boolean hasTileEntity(IBlockState state)
	{
		return true;
	}
	
	@Nullable
	@Override
	public TileEntity createTileEntity(IBlockState state, IBlockReader world)
	{
		return new TileEntityPunchDesignix();
	}
}