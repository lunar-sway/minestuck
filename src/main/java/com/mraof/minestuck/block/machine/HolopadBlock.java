package com.mraof.minestuck.block.machine;

import com.mraof.minestuck.block.BlockUtil;
import com.mraof.minestuck.block.CustomVoxelShape;
import com.mraof.minestuck.block.MSProperties;
import com.mraof.minestuck.blockentity.HolopadBlockEntity;
import com.mraof.minestuck.blockentity.MSBlockEntityTypes;
import net.minecraft.MethodsReturnNonnullByDefault;
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
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Map;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class HolopadBlock extends MachineBlock implements EntityBlock
{
	public static final Map<Direction, VoxelShape> SHAPE = new CustomVoxelShape(
			new double[]{2, 0, 1, 14, 7, 13},
			new double[]{4, 0, 13, 12, 10, 16}
	).createRotatedShapes();
	
	public static final BooleanProperty HAS_CARD = MSProperties.HAS_CARD;
	
	public HolopadBlock(Properties builder)
	{
		super(builder);
		registerDefaultState(defaultBlockState().setValue(HAS_CARD, false));
	}
	
	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new HolopadBlockEntity(pos, state);
	}
	
	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> placedType)
	{
		return level.isClientSide ? BlockUtil.checkTypeForTicker(placedType, MSBlockEntityTypes.HOLOPAD.get(), HolopadBlockEntity::clientTick) : null;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit)
	{
		if(player.isShiftKeyDown()) return InteractionResult.PASS;
		if(level.isClientSide)
			return InteractionResult.SUCCESS;
		
		if(level.getBlockEntity(pos) instanceof HolopadBlockEntity holopad)
			holopad.onRightClick(player);
		return InteractionResult.SUCCESS;
	}
	
	@Override
	public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player)
	{
		HolopadBlockEntity be = (HolopadBlockEntity) level.getBlockEntity(pos);
		
		if(be != null && !level.isClientSide)
		{
			be.dropItem(true, level, pos, be.getCard());
		}
		
		return super.playerWillDestroy(level, pos, state, player);
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		super.createBlockStateDefinition(builder);
		builder.add(HAS_CARD);
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
	{
		return SHAPE.get(state.getValue(FACING));
	}
}
