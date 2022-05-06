package com.mraof.minestuck.block;

import com.mraof.minestuck.block.machine.MachineMultiblock;
import com.mraof.minestuck.block.machine.MultiMachineBlock;
import com.mraof.minestuck.util.CustomVoxelShape;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import java.util.Map;

public class LotusTimeCapsuleBlock extends MultiMachineBlock
{
	protected final Map<Direction, VoxelShape> shape;
	
	public LotusTimeCapsuleBlock(MachineMultiblock machine, CustomVoxelShape shape, Properties properties)
	{
		super(machine, properties);
		this.shape = shape.createRotatedShapes();
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
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