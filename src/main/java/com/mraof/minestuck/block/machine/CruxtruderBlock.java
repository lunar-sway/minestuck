package com.mraof.minestuck.block.machine;

import com.mraof.minestuck.block.CruxtruderLidBlock;
import com.mraof.minestuck.block.MSBlockShapes;
import com.mraof.minestuck.blockentity.machine.CruxtruderBlockEntity;
import com.mraof.minestuck.util.CustomVoxelShape;
import com.mraof.minestuck.util.MSRotationUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.TextComponent;
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
	
	/**
	 * Destroys and then checks which blocks are connected to the given block in the multiblock structure, then repeats the process for those blocks, until the entire structure is destroyed.
	 * @param state The blockstate of the block being currently destroyed.
	 * @param level The server level/world
	 * @param pos The position of the block currently being destroyed.
	 */
	@Override
	public void findAndDestroyConnected(BlockState state, Level level, BlockPos pos)
	{
		
		if(state.isAir() || !(state.getBlock() instanceof CruxtruderBlock || state.getBlock() instanceof CruxtruderLidBlock))
			return;
		else
			level.destroyBlock(pos, false);
		
		BlockPos offsetPos;
		if(state.getBlock().getRegistryName().getPath().equals("cruxtruder_corner"))
		{
			offsetPos = new BlockPos(1, 0, 0).rotate(MSRotationUtil.fromDirection(state.getValue(FACING)));
			findAndDestroyConnected(level.getBlockState(pos.offset(offsetPos)), level, pos.offset(offsetPos));

			offsetPos = new BlockPos(0, 0, 1).rotate(MSRotationUtil.fromDirection(state.getValue(FACING)));
			findAndDestroyConnected(level.getBlockState(pos.offset(offsetPos)), level, pos.offset(offsetPos));
			
		}
		else if(state.getBlock().getRegistryName().getPath().equals("cruxtruder_side"))
		{
			offsetPos = new BlockPos(1, 0, 0).rotate(MSRotationUtil.fromDirection(state.getValue(FACING)));
			findAndDestroyConnected(level.getBlockState(pos.offset(offsetPos)), level, pos.offset(offsetPos));
			
			offsetPos = new BlockPos(-1, 0, 0).rotate(MSRotationUtil.fromDirection(state.getValue(FACING)));
			findAndDestroyConnected(level.getBlockState(pos.offset(offsetPos)), level, pos.offset(offsetPos));
			
			offsetPos = new BlockPos(0, 0, 1).rotate(MSRotationUtil.fromDirection(state.getValue(FACING)));
			findAndDestroyConnected(level.getBlockState(pos.offset(offsetPos)), level, pos.offset(offsetPos));
			
		}
		else if(state.getBlock().getRegistryName().getPath().equals("cruxtruder_center"))
		{
			offsetPos = new BlockPos(1, 0, 0);
			findAndDestroyConnected(level.getBlockState(pos.offset(offsetPos)), level, pos.offset(offsetPos));
			
			offsetPos = new BlockPos(-1, 0, 0);
			findAndDestroyConnected(level.getBlockState(pos.offset(offsetPos)), level, pos.offset(offsetPos));
			
			offsetPos = new BlockPos(0, 1, 0);
			findAndDestroyConnected(level.getBlockState(pos.offset(offsetPos)), level, pos.offset(offsetPos));
			
			offsetPos = new BlockPos(0, 0, 1);
			findAndDestroyConnected(level.getBlockState(pos.offset(offsetPos)), level, pos.offset(offsetPos));
			
			offsetPos = new BlockPos(0, 0, -1);
			findAndDestroyConnected(level.getBlockState(pos.offset(offsetPos)), level, pos.offset(offsetPos));
			
		}
		else if(state.getBlock().getRegistryName().getPath().equals("cruxtruder_tube"))
		{
			offsetPos = new BlockPos(0, 1, 0);
			findAndDestroyConnected(level.getBlockState(pos.offset(offsetPos)), level, pos.offset(offsetPos));
			
			offsetPos = new BlockPos(0, -1, 0);
			findAndDestroyConnected(level.getBlockState(pos.offset(offsetPos)), level, pos.offset(offsetPos));
			
		}
		else if(state.getBlock() instanceof CruxtruderLidBlock)
		{
			offsetPos = new BlockPos(0, -1, 0);
			findAndDestroyConnected(level.getBlockState(pos.offset(offsetPos)), level, pos.offset(offsetPos));
			
		}
	}
	
	public BlockPos getMainPos(BlockState state, BlockPos pos)
	{
		Rotation rotation = MSRotationUtil.fromDirection(state.getValue(FACING));
		
		return pos.offset(mainPos.rotate(rotation));
	}
}