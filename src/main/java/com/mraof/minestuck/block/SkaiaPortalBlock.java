package com.mraof.minestuck.block;

import com.mraof.minestuck.tileentity.SkaiaPortalTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class SkaiaPortalBlock extends BaseEntityBlock
{
	protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D);
	
	public SkaiaPortalBlock(Properties properties)
	{
		super(properties);
	}
	
	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new SkaiaPortalTileEntity(pos, state);
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
	{
		return SHAPE;
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public void entityInside(BlockState state, Level level, BlockPos pos, Entity entityIn)
	{
		if (!entityIn.isPassenger() && !entityIn.isVehicle() && !level.isClientSide && !entityIn.isOnPortalCooldown())
		{
			if(level.getBlockEntity(pos) instanceof  SkaiaPortalTileEntity portal)
				portal.teleportEntity(entityIn);
		}
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state)
	{
		return ItemStack.EMPTY;
	}
}