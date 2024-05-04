package com.mraof.minestuck.block.redstone;

import com.mojang.serialization.MapCodec;
import com.mraof.minestuck.block.BlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DiodeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.BiPredicate;

/**
 * Allows redstone signals to be filtered through a specified logic gate operation, including the ability for the power state to be locked in place as is possible with repeater blocks.
 * All of these functions are achievable with one or more vanilla blocks but this new set is intended to present a more uniform look and compact solution to larger circuits
 */
public class LogicGateBlock extends DiodeBlock
{
	private final State logicOperator;
	
	public LogicGateBlock(Properties properties, LogicGateBlock.State gateState)
	{
		super(properties);
		this.logicOperator = gateState;
		this.registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(POWERED, false));
	}
	
	@Override
	protected MapCodec<LogicGateBlock> codec()
	{
		return null; //todo
	}
	
	@Override
	protected boolean shouldTurnOn(Level level, BlockPos pos, BlockState state)
	{
		Direction leftInput = state.getValue(FACING).getCounterClockWise();
		Direction rightInput = state.getValue(FACING).getClockWise();
		boolean leftInputSendingPower = getAlternateSignalAt(level, pos, leftInput) > 0;
		boolean rightInputSendingPower = getAlternateSignalAt(level, pos, rightInput) > 0;
		
		return logicOperator.operation.test(leftInputSendingPower, rightInputSendingPower);
	}
	
	protected int getAlternateSignalAt(LevelReader level, BlockPos pos, Direction side)
	{
		return level.getSignal(pos.relative(side), side); //replaces protected method in DiodeBlock of the same name
	}
	
	@Override
	public boolean isLocked(LevelReader level, BlockPos pos, BlockState state)
	{
		Direction behind = state.getValue(FACING);
		return getAlternateSignalAt(level, pos, behind) > 0;
	}
	
	@Override
	protected int getDelay(BlockState state)
	{
		return 2;
	}
	
	@Override
	public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos)
	{
		return true; //default for RedstoneDiode means it cannot rest on air, now it can
	}
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
	{
		return Shapes.block(); //collision shape for other redstone diodes is not a full block, now it is
	}
	
	@Override
	public void animateTick(BlockState stateIn, Level level, BlockPos pos, RandomSource rand)
	{
		if(shouldTurnOn(level, pos, stateIn))
			BlockUtil.spawnParticlesAroundSolidBlock(level, pos, () -> DustParticleOptions.REDSTONE);
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		super.createBlockStateDefinition(builder);
		builder.add(POWERED);
		builder.add(FACING);
	}
	
	public enum State implements StringRepresentable
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