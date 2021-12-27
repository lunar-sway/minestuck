package com.mraof.minestuck.block.redstone;

import com.mraof.minestuck.block.MSProperties;
import com.mraof.minestuck.util.ParticlesAroundSolidBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.RedstoneDiodeBlock;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import java.util.Random;

public class LogicGateBlock extends RedstoneDiodeBlock
{
	public static final EnumProperty<LogicGateBlock.State> STATE = MSProperties.LOGIC_STATE;
	
	public LogicGateBlock(Properties properties, LogicGateBlock.State gateState)
	{
		super(properties);
		this.registerDefaultState(stateDefinition.any().setValue(STATE, gateState).setValue(FACING, Direction.NORTH).setValue(POWERED, false));
	}
	
	//TODO add Inverter block for NOT gate functions, use that block as a crafting ingredient for NAND/NOR/XNOR
	
	
	@Override
	protected boolean shouldTurnOn(World worldIn, BlockPos pos, BlockState state)
	{
		Direction leftInput = state.getValue(FACING).getCounterClockWise();
		Direction rightInput = state.getValue(FACING).getClockWise();
		boolean leftInputSendingPower = getAlternateSignalAt(worldIn, pos, leftInput) > 0;
		boolean rightInputSendingPower = getAlternateSignalAt(worldIn, pos, rightInput) > 0;
		State assignedLogicState = state.getValue(STATE);
		
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
	protected int getAlternateSignalAt(IWorldReader worldIn, BlockPos pos, Direction side)
	{
		return ((World) worldIn).getSignal(pos.relative(side), side);
	}
	
	@Override
	public boolean isLocked(IWorldReader worldIn, BlockPos pos, BlockState state)
	{
		Direction behind = state.getValue(FACING);
		//return getPowerOnSide(worldIn, pos, behind) > 0;
		return getAlternateSignalAt(worldIn, pos, behind) > 0;
	}
	
	@Override
	protected int getDelay(BlockState state)
	{
		return 2;
	}
	
	@Override
	public boolean canSurvive(BlockState state, IWorldReader worldIn, BlockPos pos)
	{
		return true; //default for RedstoneDiode means it cannot rest on air, now it can
	}
	
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
	{
		return Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
	}
	
	@Override
	public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand)
	{
		if(shouldTurnOn(worldIn, pos, stateIn))
			ParticlesAroundSolidBlock.spawnParticles(worldIn, pos, () -> RedstoneParticleData.REDSTONE);
	}
	
	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder)
	{
		super.createBlockStateDefinition(builder);
		builder.add(STATE);
		builder.add(POWERED);
		builder.add(FACING);
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
		public String getSerializedName()
		{
			return name().toLowerCase();
		}
	}
}