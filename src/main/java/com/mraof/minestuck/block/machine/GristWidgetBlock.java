package com.mraof.minestuck.block.machine;

import com.mraof.minestuck.block.MSBlockShapes;
import com.mraof.minestuck.block.MSProperties;
import com.mraof.minestuck.blockentity.MSBlockEntityTypes;
import com.mraof.minestuck.blockentity.machine.GristWidgetTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class GristWidgetBlock extends SmallMachineBlock<GristWidgetTileEntity>
{
	public static final BooleanProperty HAS_CARD = MSProperties.HAS_CARD;
	
	public GristWidgetBlock(Properties properties)
	{
		super(MSBlockShapes.GRIST_WIDGET.createRotatedShapes(), MSBlockEntityTypes.GRIST_WIDGET, properties);
		registerDefaultState(getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(HAS_CARD, false));
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		super.createBlockStateDefinition(builder);
		builder.add(HAS_CARD);
	}
	
	public static void updateItem(boolean b, Level level, BlockPos pos)
	{
		BlockState oldState = level.getBlockState(pos);
		if(oldState.getBlock() instanceof GristWidgetBlock)
			level.setBlock(pos, oldState.setValue(HAS_CARD, b), Block.UPDATE_CLIENTS);
	}
}