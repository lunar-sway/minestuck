package com.mraof.minestuck.block.machine;

import com.mraof.minestuck.block.CustomVoxelShape;
import com.mraof.minestuck.blockentity.machine.CruxtruderBlockEntity;
import com.mraof.minestuck.util.MSRotationUtil;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Map;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CruxtruderBlock extends MachineBlock implements EntityBlock, EditmodeDestroyable
{
	protected final Map<Direction, VoxelShape> shape;
	protected final boolean hasBlockEntity;
	protected final BlockPos mainPos;
	protected final CruxtruderMultiblock multiblock;
	
	public CruxtruderBlock(CruxtruderMultiblock multiblock, CustomVoxelShape shape, boolean blockEntity, BlockPos mainPos, Properties properties)
	{
		super(properties);
		this.multiblock = multiblock;
		this.shape = shape.createRotatedShapes();
		this.hasBlockEntity = blockEntity;
		this.mainPos = mainPos;
	}
	
	@Override
	protected VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context)
	{
		return shape.get(state.getValue(FACING));
	}
	
	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit)
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
	protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving)
	{
		BlockPos mainPos = getMainPos(state, pos);
		if(level.getBlockEntity(mainPos) instanceof CruxtruderBlockEntity cruxtruder)
		{
			cruxtruder.setBroken();
			if(pos.equals(mainPos))
				cruxtruder.dropItems();
		}
		
		super.onRemove(state, level, pos, newState, isMoving);
	}
	
	@Override
	public void destroyFull(BlockState state, Level level, BlockPos pos)
	{
		var placement = this.multiblock.findPlacementFromTube(level, this.getMainPos(state, pos));
		if(placement.isPresent())
			this.multiblock.removeAt(level, placement.get());
		else
		{
			for(var placementGuess : this.multiblock.guessPlacement(pos, state))
				this.multiblock.removeAt(level, placementGuess);
		}
	}
	
	public BlockPos getMainPos(BlockState state, BlockPos pos)
	{
		Rotation rotation = MSRotationUtil.fromDirection(state.getValue(FACING));
		
		return pos.offset(mainPos.rotate(rotation));
	}
}
