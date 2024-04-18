package com.mraof.minestuck.block;

import com.mojang.serialization.MapCodec;
import com.mraof.minestuck.blockentity.SkaiaPortalBlockEntity;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SkaiaPortalBlock extends BaseEntityBlock
{
	protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D);
	
	public SkaiaPortalBlock(Properties properties)
	{
		super(properties);
	}
	
	@Override
	protected MapCodec<SkaiaPortalBlock> codec()
	{
		return null; //todo
	}
	
	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new SkaiaPortalBlockEntity(pos, state);
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
			if(level.getBlockEntity(pos) instanceof  SkaiaPortalBlockEntity portal)
				portal.teleportEntity(entityIn);
		}
	}
	
	@Override
	public ItemStack getCloneItemStack(BlockState state, HitResult target, LevelReader level, BlockPos pos, Player player)
	{
		return ItemStack.EMPTY;
	}
}