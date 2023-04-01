package com.mraof.minestuck.block.machine;

import com.mraof.minestuck.block.MSBlockShapes;
import com.mraof.minestuck.block.MSProperties;
import com.mraof.minestuck.blockentity.MSBlockEntityTypes;
import com.mraof.minestuck.blockentity.machine.GristCollectorBlockEntity;
import com.mraof.minestuck.blockentity.machine.GristWidgetBlockEntity;
import com.mraof.minestuck.blockentity.machine.SendificatorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;

public class GristCollectorBlock extends MachineProcessBlock implements EntityBlock
{
	//public static final BooleanProperty HAS_CARD = MSProperties.HAS_CARD;
	
	public GristCollectorBlock(Properties properties)
	{
		super(properties);
		//registerDefaultState(getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(HAS_CARD, false));
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		super.createBlockStateDefinition(builder);
		//builder.add(HAS_CARD);
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit)
	{
		if(player instanceof ServerPlayer serverPlayer)
		{
			if(level.getBlockEntity(pos) instanceof GristCollectorBlockEntity sendificator)
			{
				sendificator.openMenu(serverPlayer);
			}
		}
		return InteractionResult.sidedSuccess(level.isClientSide);
	}
	
	@javax.annotation.Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new GristCollectorBlockEntity(pos, state);
	}
	
	@Nullable
	@Override
	public <E extends BlockEntity> BlockEntityTicker<E> getTicker(Level level, BlockState state, BlockEntityType<E> placedType)
	{
		return createMachineTicker(level, placedType, MSBlockEntityTypes.GRIST_COLLECTOR.get());
	}
}