package com.mraof.minestuck.block;

import com.mraof.minestuck.block.multiblock.MultiblockMachine;
import com.mraof.minestuck.tileentity.TileEntityCruxtruder;

import net.minecraft.block.Block;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockCruxtruder extends BlockMultiMachine
{
	public static final VoxelShape TUBE_SHAPE = Block.makeCuboidShape(2, 0, 2, 14, 16, 14);
	
	protected final VoxelShape shape;
	protected final boolean hasTileEntity, fullCube;
	protected final BlockPos mainPos;
	
	public BlockCruxtruder(MultiblockMachine machine, VoxelShape shape, boolean tileEntity, boolean fullCube, BlockPos mainPos, Properties properties)
	{
		super(machine, properties);
		this.shape = shape;
		this.hasTileEntity = tileEntity;
		this.fullCube = fullCube;
		this.mainPos = mainPos;
	}
	
	@Override
	public VoxelShape getShape(IBlockState state, IBlockReader worldIn, BlockPos pos)
	{
		return shape;
	}
	
	@Override
	public boolean onBlockActivated(IBlockState state, World worldIn, BlockPos pos, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if(hasTileEntity && (state.get(FACING) == side || side == EnumFacing.UP))
		{
			if(worldIn.isRemote)
				return true;
			
			TileEntity te = worldIn.getTileEntity(pos);
			if(te instanceof TileEntityCruxtruder)
				((TileEntityCruxtruder) te).onRightClick(player, side == EnumFacing.UP);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean hasTileEntity(IBlockState state)
	{
		return hasTileEntity;
	}
	
	@Nullable
	@Override
	public TileEntity createTileEntity(IBlockState state, IBlockReader world)
	{
		if(hasTileEntity)
			return new TileEntityCruxtruder();
		else return null;
	}
	
	@Override
	public void onReplaced(IBlockState state, World worldIn, BlockPos pos, IBlockState newState, boolean isMoving)
	{
		BlockPos MainPos = getMainPos(state, pos);
		TileEntity te = worldIn.getTileEntity(MainPos);
		if(te instanceof TileEntityCruxtruder)
		{
			((TileEntityCruxtruder) te).destroy();
		}
		
		super.onReplaced(state, worldIn, pos, newState, isMoving);
	}
	
	@Override
	public boolean isFullCube(IBlockState state)
	{
		return fullCube;
	}
	
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockReader worldIn, IBlockState state, BlockPos pos, EnumFacing face)
	{
		if(fullCube)
			return BlockFaceShape.SOLID;
		return BlockFaceShape.UNDEFINED;
	}
	
	@Override
	public boolean isSolid(IBlockState state)
	{
		return fullCube;
	}
	
	@Override
	public void getDrops(IBlockState state, NonNullList<ItemStack> drops, World world, BlockPos pos, int fortune)
	{}
	
	public BlockPos getMainPos(IBlockState state, BlockPos pos)
	{
		Rotation rotation = rotationFromFacing(state.get(FACING));
		
		return pos.add(mainPos.rotate(rotation));
	}
}