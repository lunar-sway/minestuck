package com.mraof.minestuck.block;

import com.mraof.minestuck.block.machine.MultiMachineBlock;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Map;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class LotusTimeCapsuleBlock extends MultiMachineBlock<LotusTimeCapsuleMultiblock>
{
	protected final Map<Direction, VoxelShape> shape;
	
	public LotusTimeCapsuleBlock(LotusTimeCapsuleMultiblock machine, CustomVoxelShape shape, Properties properties)
	{
		super(machine, properties);
		this.shape = shape.createRotatedShapes();
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
	{
		return shape.get(state.getValue(FACING));
	}
	
	@Override
	public BlockState mirror(BlockState state, Mirror mirror)
	{
		Direction direction = state.getValue(FACING);
		if(mirror != Mirror.NONE)
		{
			boolean clockwise = (mirror == Mirror.LEFT_RIGHT) ^ (direction.getAxis() == Direction.Axis.X); //fixes generation issue
			if(clockwise)
				return state.setValue(FACING, direction.getClockWise());
			else
				return state.setValue(FACING, direction.getCounterClockWise());
		}
		return state;
	}
}
