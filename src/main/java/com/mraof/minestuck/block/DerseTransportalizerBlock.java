package com.mraof.minestuck.block;


import com.mraof.minestuck.tileentity.DerseTransportalizerTileEntity;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class DerseTransportalizerBlock extends ContainerBlock
{
	public static final VoxelShape SHAPE = MSBlockShapes.DERSE_TRANSPORTALIZER.create(Direction.NORTH);

	public DerseTransportalizerBlock(Properties properties)
	{
		super(properties);
	}

	@Nullable
	@Override
	public TileEntity createNewTileEntity(IBlockReader worldIn)
	{
		DerseTransportalizerTileEntity tileEntity = new DerseTransportalizerTileEntity();
		return tileEntity;
	}

	@Override
	@SuppressWarnings("deprecation")
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
	{
		return SHAPE;
	}

	@Override
	public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn)
	{
		if (!entityIn.isPassenger() && !entityIn.isBeingRidden() && !worldIn.isRemote && entityIn.timeUntilPortal == 0)
		{
			DerseTransportalizerTileEntity portal = (DerseTransportalizerTileEntity) worldIn.getTileEntity(pos);
			portal.teleportEntity(entityIn);
		}
	}

	@Override
	public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player)
	{
		return ItemStack.EMPTY;
	}

	@Override
	@SuppressWarnings("deprecation")
	public BlockRenderType getRenderType(BlockState state)
	{
		return BlockRenderType.MODEL;
	}
}
