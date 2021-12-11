package com.mraof.minestuck.block.redstone;

import com.mraof.minestuck.block.MSProperties;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.RedstoneDiodeBlock;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

public class LogicGateBlock extends RedstoneDiodeBlock
{
	public static final EnumProperty<LogicGateBlock.State> STATE = MSProperties.LOGIC_STATE;
	
	public LogicGateBlock(Properties properties, LogicGateBlock.State gateState)
	{
		super(properties);
		this.setDefaultState(this.stateContainer.getBaseState().with(STATE, gateState).with(HORIZONTAL_FACING, Direction.NORTH).with(POWERED, false));
	}
	
	//TODO power output direction seems reversed to the block model
	//TODO make sure it doesnt need a solid block to stand on
	//TODO allow power sources other than vanilla as valid input if possible
	//TODO add Inverter block for NOT gate functions, use that block as a crafting ingredient for NAND/NOR/XNOR
	
	@Override
	protected boolean shouldBePowered(World worldIn, BlockPos pos, BlockState state)
	{
		Direction leftInput = state.get(HORIZONTAL_FACING).rotateYCCW();
		Direction rightInput = state.get(HORIZONTAL_FACING).rotateY();
		boolean leftInputSendingPower = getPowerOnSide(worldIn, pos.offset(leftInput), leftInput) > 0;
		boolean rightInputSendingPower = getPowerOnSide(worldIn, pos.offset(rightInput), rightInput) > 0;
		State assignedLogicState = state.get(STATE);
		
		if(assignedLogicState == State.AND)
			return leftInputSendingPower && rightInputSendingPower;
		else if(assignedLogicState == State.OR)
			return leftInputSendingPower || rightInputSendingPower;
		else if(assignedLogicState == State.XOR)
			return (leftInputSendingPower && !rightInputSendingPower) || (!leftInputSendingPower && rightInputSendingPower);
		else if(assignedLogicState == State.NAND)
			return !leftInputSendingPower || !rightInputSendingPower;
		else if(assignedLogicState == State.NOR)
			return !leftInputSendingPower && !rightInputSendingPower;
		else //XNOR
			return (!leftInputSendingPower && !rightInputSendingPower) || (leftInputSendingPower && rightInputSendingPower);
	}
	
	@Override
	public boolean isLocked(IWorldReader worldIn, BlockPos pos, BlockState state)
	{
		Direction behind = state.get(HORIZONTAL_FACING).getOpposite();
		return getPowerOnSide(worldIn, pos.offset(behind), behind) > 0;
	}
	
	@Override
	protected int getDelay(BlockState state)
	{
		return 2;
	}
	
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
	{
		return Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
	}
	
	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
	{
		super.fillStateContainer(builder);
		builder.add(STATE);
		builder.add(POWERED);
		builder.add(HORIZONTAL_FACING);
	}
	
	public enum State implements IStringSerializable
	{
		AND,
		OR,
		XOR,
		NAND,
		NOR,
		XNOR;
		
		@Override
		public String getName()
		{
			return name().toLowerCase();
		}
	}
}