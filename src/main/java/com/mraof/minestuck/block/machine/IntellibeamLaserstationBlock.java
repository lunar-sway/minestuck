package com.mraof.minestuck.block.machine;

import com.mraof.minestuck.block.BlockUtil;
import com.mraof.minestuck.block.MSBlockShapes;
import com.mraof.minestuck.block.MSProperties;
import com.mraof.minestuck.blockentity.MSBlockEntityTypes;
import com.mraof.minestuck.blockentity.machine.IntellibeamLaserstationBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
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
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.Map;

public class IntellibeamLaserstationBlock extends MachineBlock implements EntityBlock
{
	public static final Map<Direction, VoxelShape> SHAPE = MSBlockShapes.INTELLIBEAM_LASERSTATION.createRotatedShapes();
	public static final BooleanProperty HAS_CARD = MSProperties.HAS_CARD;
	
	public IntellibeamLaserstationBlock(Properties builder)
	{
		super(builder);
		registerDefaultState(defaultBlockState().setValue(HAS_CARD, false));
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit)
	{
		if(level.isClientSide)
			return InteractionResult.SUCCESS;
		
		if(level.getBlockEntity(pos) instanceof IntellibeamLaserstationBlockEntity intellibeam)
			intellibeam.onRightClick(player);
		return InteractionResult.SUCCESS;
	}
	
	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> placedType)
	{
		return !level.isClientSide ? BlockUtil.checkTypeForTicker(placedType, MSBlockEntityTypes.INTELLIBEAM_LASERSTATION.get(), IntellibeamLaserstationBlockEntity::serverTick) : null;
	}
	
	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new IntellibeamLaserstationBlockEntity(pos, state);
	}
	
	@Override
	public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player)
	{
		if(!level.isClientSide && level.getBlockEntity(pos) instanceof IntellibeamLaserstationBlockEntity intellibeamEntity)
		{
			intellibeamEntity.dropCard(true, level, pos, intellibeamEntity.getAnalyzedCard());
		}
		
		return super.playerWillDestroy(level, pos, state, player);
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
	{
		return SHAPE.get(state.getValue(FACING));
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
	{
		return SHAPE.get(state.getValue(FACING));
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		super.createBlockStateDefinition(builder);
		builder.add(HAS_CARD);
	}
}