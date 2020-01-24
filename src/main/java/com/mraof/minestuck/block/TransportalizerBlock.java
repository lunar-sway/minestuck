package com.mraof.minestuck.block;

import com.mraof.minestuck.client.gui.MSScreenFactories;
import com.mraof.minestuck.tileentity.TransportalizerTileEntity;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
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
	public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn)
	{
		if (!worldIn.isRemote && !entityIn.isPassenger() && !entityIn.isBeingRidden() && entityIn.isNonBoss())
		{
			if(entityIn.timeUntilPortal == 0)
				((TransportalizerTileEntity) worldIn.getTileEntity(pos)).teleport(entityIn);
			else entityIn.timeUntilPortal = entityIn.getPortalCooldown();
		}
	}
	
	@Override
	public BlockRenderType getRenderType(BlockState state)
	{
		return BlockRenderType.MODEL;
	}
	
	@Override
	public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
	{
		TransportalizerTileEntity tileEntity = (TransportalizerTileEntity) worldIn.getTileEntity(pos);

		if (tileEntity == null || player.isSneaking())
		{
			return false;
		}

		if(worldIn.isRemote)
			MSScreenFactories.displayTransportalizerScreen(tileEntity);

		return true;
	}
}