package com.mraof.minestuck.block;

import com.mraof.minestuck.tileentity.TileEntitySkaiaPortal;
import com.mraof.minestuck.world.MinestuckDimensionHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockSkaiaPortal extends BlockContainer
{
	protected static final VoxelShape SHAPE = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D);
	
	public BlockSkaiaPortal(Properties properties)
	{
		super(properties);
	}
	
	@Nullable
	@Override
	public TileEntity createNewTileEntity(IBlockReader worldIn)
	{
		TileEntitySkaiaPortal tileEntity = new TileEntitySkaiaPortal();
		return tileEntity;
	}
	
	@Override
	public VoxelShape getShape(IBlockState state, IBlockReader worldIn, BlockPos pos)
	{
		return SHAPE;
	}
	
	@Override
	public boolean isFullCube(IBlockState state)
	{
		return super.isFullCube(state);
	}
	
	@Override
	public void onEntityCollision(IBlockState state, World worldIn, BlockPos pos, Entity entityIn)
	{
		if (!entityIn.isPassenger() && !entityIn.isBeingRidden() && !worldIn.isRemote && entityIn.timeUntilPortal == 0)
		{
			TileEntitySkaiaPortal portal = (TileEntitySkaiaPortal) worldIn.getTileEntity(pos);
			portal.teleportEntity(entityIn);
		}
	}
	
	@Override
	public int quantityDropped(IBlockState state, Random random)
	{
		return 0;
	}
	
	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, EntityPlayer player)
	{
		return ItemStack.EMPTY;
	}
	
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockReader worldIn, IBlockState state, BlockPos pos, EnumFacing face)
	{
		return BlockFaceShape.UNDEFINED;
	}
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.INVISIBLE;
	}
	
//	/**
//	 * Called upon block activation (right click on the block.)
//	 */
//	@Override
//	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
//	{
//		if (worldIn.isRemote)
//		{
//			return true;
//		}
//		int newDimension = ((TileEntityGatePortal) worldIn.getTileEntity(pos)).destination.dim + 1;
//		if(worldIn.provider.getDimensionId() != newDimension && DimensionManager.isDimensionRegistered(newDimension))
//		{
//			this.destinationDimension = newDimension;
//			((TileEntityGatePortal) worldIn.getTileEntity(pos)).destination.dim = newDimension;
//		}
//		
//		return true;
//	}
	
	public void setDestinationDimension(World world, int x, int y, int z, DimensionType destinationDimension)
	{
		((TileEntitySkaiaPortal) world.getTileEntity(new BlockPos(x, y, z))).destination.dim = destinationDimension;
	}
}
