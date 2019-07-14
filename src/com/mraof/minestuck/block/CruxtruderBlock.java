package com.mraof.minestuck.block;

import com.mraof.minestuck.block.multiblock.MachineMultiblock;
import com.mraof.minestuck.tileentity.CruxtruderTileEntity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class CruxtruderBlock extends MultiMachineBlock
{
	public static final VoxelShape TUBE_SHAPE = Block.makeCuboidShape(2, 0, 2, 14, 16, 14);
	
	protected final VoxelShape shape;
	protected final boolean hasTileEntity;
	protected final BlockPos mainPos;
	
	public CruxtruderBlock(MachineMultiblock machine, VoxelShape shape, boolean tileEntity, BlockPos mainPos, Properties properties)
	{
		super(machine, properties);
		this.shape = shape;
		this.hasTileEntity = tileEntity;
		this.mainPos = mainPos;
	}
	
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
	{
		return shape;
	}
	
	@Override
	public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
	{
		if(hasTileEntity && (state.get(FACING) == hit.getFace() || hit.getFace() == Direction.UP))
		{
			if(worldIn.isRemote)
				return true;
			
			TileEntity te = worldIn.getTileEntity(pos);
			if(te instanceof CruxtruderTileEntity)
				((CruxtruderTileEntity) te).onRightClick(player, hit.getFace() == Direction.UP);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean hasTileEntity(BlockState state)
	{
		return hasTileEntity;
	}
	
	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world)
	{
		if(hasTileEntity)
			return new CruxtruderTileEntity();
		else return null;
	}
	
	@Override
	public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving)
	{
		BlockPos MainPos = getMainPos(state, pos);
		TileEntity te = worldIn.getTileEntity(MainPos);
		if(te instanceof CruxtruderTileEntity)
		{
			((CruxtruderTileEntity) te).destroy();
		}
		
		super.onReplaced(state, worldIn, pos, newState, isMoving);
	}
	
	public BlockPos getMainPos(BlockState state, BlockPos pos)
	{
		Rotation rotation = rotationFromDirection(state.get(FACING));
		
		return pos.add(mainPos.rotate(rotation));
	}
}