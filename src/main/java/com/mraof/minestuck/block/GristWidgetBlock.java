package com.mraof.minestuck.block;

import com.mraof.minestuck.tileentity.GristWidgetTileEntity;
import com.mraof.minestuck.tileentity.MSTileEntityTypes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

public class GristWidgetBlock extends SmallMachineBlock<GristWidgetTileEntity>
{
	public static final BooleanProperty HAS_CARD = MSProperties.HAS_CARD;
	
	public GristWidgetBlock(Properties properties)
	{
		super(MSBlockShapes.GRIST_WIDGET.createRotatedShapes(), MSTileEntityTypes.GRIST_WIDGET, properties);
		setDefaultState(getStateContainer().getBaseState().with(FACING, Direction.NORTH).with(HAS_CARD, false));
	}
	
	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
	{
		super.fillStateContainer(builder);
		builder.add(HAS_CARD);
	}
	
	public static void updateItem(boolean b, World world, BlockPos pos)
	{
		BlockState oldState = world.getBlockState(pos);
		if(oldState.getBlock() instanceof GristWidgetBlock)
			world.setBlockState(pos, oldState.with(HAS_CARD, b), Constants.BlockFlags.BLOCK_UPDATE);
	}
}