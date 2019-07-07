package com.mraof.minestuck.block;

import com.mraof.minestuck.tileentity.TileEntitySkaiaPortal;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

import javax.annotation.Nullable;
import java.util.Random;

public class SkaiaPortalBlock extends ContainerBlock
{
	protected static final VoxelShape SHAPE = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D);
	
	public SkaiaPortalBlock(Properties properties)
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
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
	{
		return SHAPE;
	}
	
	@Override
	public boolean isFullCube(IBlockState state)
	{
		return super.isFullCube(state);
	}
	
	@Override
	public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn)
	{
		if (!entityIn.isPassenger() && !entityIn.isBeingRidden() && !worldIn.isRemote && entityIn.timeUntilPortal == 0)
		{
			TileEntitySkaiaPortal portal = (TileEntitySkaiaPortal) worldIn.getTileEntity(pos);
			portal.teleportEntity(entityIn);
		}
	}
	
	@Override
	public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player)
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
}
