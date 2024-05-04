package com.mraof.minestuck.block;

import com.mraof.minestuck.blockentity.ItemStackBlockEntity;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CruxiteDowelBlock extends Block implements EntityBlock
{
	public static final VoxelShape DOWEL_SHAPE = Block.box(5, 0, 5, 11, 8, 11);
	
	public static final EnumProperty<Type> DOWEL_TYPE = MSProperties.DOWEL_BLOCK;
	
	public CruxiteDowelBlock(Properties properties)
	{
		super(properties);
		registerDefaultState(this.stateDefinition.any().setValue(DOWEL_TYPE, Type.DOWEL));
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
	{
		return DOWEL_SHAPE;
	}
	
	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		ItemStackBlockEntity be = new ItemStackBlockEntity(pos, state);
		be.setStack(new ItemStack(this));
		return be;
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder)
	{
		if(builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY) instanceof ItemStackBlockEntity stackEntity)
			builder = builder.withDynamicDrop(ItemStackBlockEntity.ITEM_DYNAMIC,
					consumer -> consumer.accept(stackEntity.getStack()));
		
		return super.getDrops(state, builder);
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
	{
		if(!level.isClientSide)
			dropDowel(level, pos);
		return  InteractionResult.SUCCESS;
	}
	
	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context)
	{
		return context.getClickedFace() == Direction.UP ? defaultBlockState() : null;
	}
	
	@Override
	public ItemStack getCloneItemStack(BlockState state, HitResult target, LevelReader level, BlockPos pos, Player player)
	{
		if (level.getBlockEntity(pos) instanceof ItemStackBlockEntity stackEntity)
		{
			ItemStack dowel = stackEntity.getStack();
			if(!dowel.isEmpty())
				return dowel.copy();
		}
		return super.getCloneItemStack(state, target, level, pos, player);
	}
	
	public static void dropDowel(Level level, BlockPos pos)
	{
		if (level.getBlockEntity(pos) instanceof ItemStackBlockEntity stackEntity)
		{
			ItemStack stack = stackEntity.getStack();
			popResource(level, pos, stack);
		}
		level.removeBlock(pos, false);
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		builder.add(DOWEL_TYPE);
	}
	
	public enum Type implements StringRepresentable
	{
		DOWEL,
		TOTEM;
		
		
		@Override
		public String getSerializedName()
		{
			return this.name().toLowerCase();
		}
	}
}
