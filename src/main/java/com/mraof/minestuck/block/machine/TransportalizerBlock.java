package com.mraof.minestuck.block.machine;

import com.mraof.minestuck.block.BlockUtil;
import com.mraof.minestuck.block.MSBlockShapes;
import com.mraof.minestuck.blockentity.MSBlockEntityTypes;
import com.mraof.minestuck.client.gui.MSScreenFactories;
import com.mraof.minestuck.blockentity.TransportalizerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class TransportalizerBlock extends MachineBlock implements EntityBlock
{
	public static final VoxelShape SHAPE = MSBlockShapes.TRANSPORTALIZER.create(Direction.NORTH);
	
	public TransportalizerBlock(Properties properties)
	{
		super(properties);
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
	{
		return SHAPE;
	}
	
	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new TransportalizerBlockEntity(pos, state);
	}
	
	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> placedType)
	{
		return !level.isClientSide ? BlockUtil.checkTypeForTicker(placedType, MSBlockEntityTypes.TRANSPORTALIZER.get(), TransportalizerBlockEntity::transportalizerTick) : null;
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public void entityInside(BlockState state, Level level, BlockPos pos, Entity entityIn)
	{
		if(level.getBlockEntity(pos) instanceof TransportalizerBlockEntity transportalizer)
			transportalizer.onCollision(entityIn);
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public RenderShape getRenderShape(BlockState state)
	{
		return RenderShape.MODEL;
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit)
	{
		TransportalizerBlockEntity tileEntity = (TransportalizerBlockEntity) level.getBlockEntity(pos);

		if (tileEntity == null || player.isShiftKeyDown())
		{
			return InteractionResult.PASS;
		}

		if(level.isClientSide)
			MSScreenFactories.displayTransportalizerScreen(tileEntity);

		return InteractionResult.SUCCESS;
	}
}