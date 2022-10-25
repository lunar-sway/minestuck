package com.mraof.minestuck.block.machine;

import com.mraof.minestuck.block.BlockUtil;
import com.mraof.minestuck.block.MSProperties;
import com.mraof.minestuck.blockentity.HolopadBlockEntity;
import com.mraof.minestuck.blockentity.MSBlockEntityTypes;
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
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.Map;

public class HolopadBlock extends MachineBlock implements EntityBlock
{
	public static final Map<Direction, VoxelShape> SHAPE = createRotatedShapes(2, 0, 1, 14, 6, 13);
	public static final Map<Direction, VoxelShape> COLLISION_SHAPE;
	protected static final AABB HOLOPAD_TOP_AABB = new AABB(3/16F, 6/16F, 2.6/16F, 13/16F, 7/16F, 12.6/16F);
	protected static final AABB HOLOPAD_CARDSLOT_AABB = new AABB(4/16F, 0F, 13.8/16F, 12/16F, 10.1/16F, 15.94/16F);
	
	public static final BooleanProperty HAS_CARD = MSProperties.HAS_CARD;
	
	static
	{
		VoxelShape topShape = Block.box(3, 6, 3, 13, 7, 13);
		COLLISION_SHAPE = createRotatedShapes(4, 0, 14, 12, 10, 16);
		COLLISION_SHAPE.replaceAll((enumFacing, shape) -> Shapes.or(shape, topShape));
	}
	
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
	public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player)
	{
		HolopadBlockEntity be = (HolopadBlockEntity) level.getBlockEntity(pos);
		
		if(be != null && !level.isClientSide)
		{
			be.dropItem(true, level, pos, be.getCard());
		}
		
		super.playerWillDestroy(level, pos, state, player);
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
	
	@Override
	@SuppressWarnings("deprecation")
	public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
	{
		return COLLISION_SHAPE.get(state.getValue(FACING));
	}
	
	
}