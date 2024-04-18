package com.mraof.minestuck.block;

import com.mraof.minestuck.blockentity.ItemStackBlockEntity;
import com.mraof.minestuck.item.MSItems;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
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
public class EmergingCruxiteDowelBlock extends Block implements EntityBlock
{
	public static final VoxelShape CRUXTRUDER_SHAPE = Block.box(5, 0, 5, 11, 5, 11);
	
	public EmergingCruxiteDowelBlock(Properties properties)
	{
		super(properties);
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
	{
		return CRUXTRUDER_SHAPE;
	}
	
	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		ItemStackBlockEntity be = new ItemStackBlockEntity(pos, state);
		be.setStack(new ItemStack(MSItems.CRUXITE_DOWEL.get()));
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
			CruxiteDowelBlock.dropDowel(level, pos);
		return  InteractionResult.SUCCESS;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos currentPos, BlockPos neighborPos)
	{
		return direction == Direction.DOWN && !this.canSurvive(state, level, currentPos)
				? Blocks.AIR.defaultBlockState()
				: super.updateShape(state, direction, neighborState, level, currentPos, neighborPos);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos)
	{
		return level.getBlockState(pos.below()).is(MSBlocks.CRUXTRUDER.TUBE.get());
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
		return new ItemStack(MSItems.CRUXITE_DOWEL.get());
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public PushReaction getPistonPushReaction(BlockState state)
	{
		return PushReaction.DESTROY;
	}
}
