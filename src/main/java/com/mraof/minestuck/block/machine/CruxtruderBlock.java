package com.mraof.minestuck.block.machine;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.blockentity.machine.CruxtruderBlockEntity;
import com.mraof.minestuck.util.CustomVoxelShape;
import com.mraof.minestuck.util.MSRotationUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.Map;

public class CruxtruderBlock extends MultiMachineBlock implements EntityBlock
{
	protected final Map<Direction, VoxelShape> shape;
	protected final boolean hasBlockEntity;
	protected final BlockPos mainPos;
	
	public CruxtruderBlock(MachineMultiblock machine, CustomVoxelShape shape, boolean blockEntity, BlockPos mainPos, Properties properties)
	{
		super(machine, properties);
		this.shape = shape.createRotatedShapes();
		this.hasBlockEntity = blockEntity;
		this.mainPos = mainPos;
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context)
	{
		return shape.get(state.getValue(FACING));
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit)
	{
		if(hasBlockEntity && (state.getValue(FACING) == hit.getDirection() || hit.getDirection() == Direction.UP))
		{
			if(level.isClientSide)
				return InteractionResult.SUCCESS;
			
			if(level.getBlockEntity(pos) instanceof CruxtruderBlockEntity cruxtruder)
				cruxtruder.onRightClick(player, hit.getDirection() == Direction.UP);
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}
	
	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		if(hasBlockEntity)
			return new CruxtruderBlockEntity(pos, state);
		else return null;
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving)
	{
		BlockPos MainPos = getMainPos(state, pos);
		if(level.getBlockEntity(MainPos) instanceof CruxtruderBlockEntity cruxtruder)
		{
			cruxtruder.destroy();
		}
		
		super.onRemove(state, level, pos, newState, isMoving);
	}
	
	@Override
	public void findAndDestroyConnected(BlockState state, Level level, BlockPos pos)
	{
		var placement = MSBlocks.CRUXTRUDER.findPlacementFromTube(level, this.getMainPos(state, pos));
		if(placement.isPresent())
			MSBlocks.CRUXTRUDER.removeAt(level, placement.get());
		else
		{
			for(var placementGuess : MSBlocks.CRUXTRUDER.guessPlacement(pos, state))
				MSBlocks.CRUXTRUDER.removeAt(level, placementGuess);
		}
	}
	
	public BlockPos getMainPos(BlockState state, BlockPos pos)
	{
		Rotation rotation = MSRotationUtil.fromDirection(state.getValue(FACING));
		
		return pos.offset(mainPos.rotate(rotation));
	}
}