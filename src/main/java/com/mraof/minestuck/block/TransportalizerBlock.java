package com.mraof.minestuck.block;

import com.mraof.minestuck.client.gui.MSScreenFactories;
import com.mraof.minestuck.tileentity.TransportalizerTileEntity;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class TransportalizerBlock extends MachineBlock
{
	public static final VoxelShape SHAPE = MSBlockShapes.TRANSPORTALIZER.create(Direction.NORTH);
	
	public TransportalizerBlock(Properties properties)
	{
		super(properties);
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
	{
		return SHAPE;
	}
	
	@Override
	public boolean hasTileEntity(BlockState state)
	{
		return true;
	}
	
	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world)
	{
		return new TransportalizerTileEntity();
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn)
	{
		TileEntity tileEntity = worldIn.getTileEntity(pos);
		if(tileEntity instanceof TransportalizerTileEntity)
			((TransportalizerTileEntity) tileEntity).onCollision(entityIn);
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public BlockRenderType getRenderType(BlockState state)
	{
		return BlockRenderType.MODEL;
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
	{
		TransportalizerTileEntity tileEntity = (TransportalizerTileEntity) worldIn.getTileEntity(pos);

		if (tileEntity == null || player.isSneaking())
		{
			return ActionResultType.FAIL;
		}

		if(worldIn.isRemote)
			MSScreenFactories.displayTransportalizerScreen(tileEntity);

		return ActionResultType.SUCCESS;
	}
}