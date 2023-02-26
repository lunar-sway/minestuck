package com.mraof.minestuck.block.machine;

import com.mraof.minestuck.block.MSBlockShapes;
import com.mraof.minestuck.block.MSProperties;
import com.mraof.minestuck.blockentity.MSBlockEntityTypes;
import com.mraof.minestuck.blockentity.machine.GristWidgetBlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class GristWidgetBlock extends SmallMachineBlock<GristWidgetBlockEntity>
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
}