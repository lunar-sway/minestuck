package com.mraof.minestuck.block.redstone;

import com.mraof.minestuck.util.ParticlesAroundSolidBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.RedstoneDiodeBlock;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.state.StateContainer;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import java.util.Random;
import java.util.function.BiPredicate;

/**
 * Allows redstone signals to be filtered through a specified logic gate operation, including the ability for the power state to be locked in place as is possible with repeater blocks.
 * All of these functions are achievable with one or more vanilla blocks but this new set is intended to present a more uniform look and compact solution to larger circuits
 */
public class LogicGateBlock extends RedstoneDiodeBlock
{
	private final State logicOperator;
	
	public LogicGateBlock(Properties properties, LogicGateBlock.State gateState)
	{
		super(properties);
		this.logicOperator = gateState;
		this.registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(POWERED, false));
	}
	
	@Override
	protected boolean shouldTurnOn(World worldIn, BlockPos pos, BlockState state)
	{
		Direction leftInput = state.getValue(FACING).getCounterClockWise();
		Direction rightInput = state.getValue(FACING).getClockWise();
		boolean leftInputSendingPower = getAlternateSignalAt(worldIn, pos, leftInput) > 0;
		boolean rightInputSendingPower = getAlternateSignalAt(worldIn, pos, rightInput) > 0;
		
		return logicOperator.operation.test(leftInputSendingPower, rightInputSendingPower);
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
		return VoxelShapes.block(); //collision shape for other redstone diodes is not a full block, now it is
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
		builder.add(POWERED);
		builder.add(FACING);
	}
	
	public enum State implements IStringSerializable
	{
		AND((left, right) -> left && right),
		OR((left, right) -> left || right),
		XOR((left, right) -> left != right),
		NAND(AND.operation.negate()),
		NOR(OR.operation.negate()),
		XNOR(XOR.operation.negate());
		
		private final BiPredicate<Boolean, Boolean> operation;
		
		State(BiPredicate<Boolean, Boolean> operation)
		{
			this.operation = operation;
		}
		
		@Override
		public String getSerializedName()
		{
			return name().toLowerCase();
		}
	}
}