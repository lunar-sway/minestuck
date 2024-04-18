package com.mraof.minestuck.block.machine;

import com.mraof.minestuck.blockentity.machine.IOwnable;
import com.mraof.minestuck.blockentity.machine.MachineProcessBlockEntity;
import com.mraof.minestuck.player.IdentifierHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.function.Supplier;

public class SmallMachineBlock<T extends MachineProcessBlockEntity> extends MachineProcessBlock implements EntityBlock
{
	private final Map<Direction, VoxelShape> shape;
	private final Supplier<BlockEntityType<T>> entityType;
	
	public SmallMachineBlock(Map<Direction, VoxelShape> shape, Supplier<BlockEntityType<T>> entityType, Properties properties)
	{
		super(properties);
		this.shape = shape;
		this.entityType = entityType;
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
	{
		return shape.get(state.getValue(FACING));
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit)
	{
		if(player instanceof ServerPlayer serverPlayer)
		{
			BlockEntity blockEntity = level.getBlockEntity(pos);
			if(blockEntity != null && blockEntity.getType() == this.entityType.get())
			{
				if(blockEntity instanceof IOwnable ownable)
					ownable.setOwner(IdentifierHandler.encode(player));
				if(blockEntity instanceof MenuProvider menuProvider)
					serverPlayer.openMenu(menuProvider, pos);
			}
		}
		return InteractionResult.sidedSuccess(level.isClientSide);
	}
	
	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return entityType.get().create(pos, state);
	}
	
	@Nullable
	@Override
	public <E extends BlockEntity> BlockEntityTicker<E> getTicker(Level level, BlockState state, BlockEntityType<E> placedType)
	{
		return createMachineTicker(level, placedType, entityType.get());
	}
}