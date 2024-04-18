package com.mraof.minestuck.block;

import com.mraof.minestuck.blockentity.ItemStackBlockEntity;
import com.mraof.minestuck.client.gui.MSScreenFactories;
import com.mraof.minestuck.item.block.StoneTabletItem;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class StoneTabletBlock extends CustomShapeBlock implements EntityBlock //stone slab is the same as stone tablet, both are used in different circumstances
{
	public static final BooleanProperty CARVED = MSProperties.CARVED;
	
	public StoneTabletBlock(Properties properties)
	{
		super(properties, MSBlockShapes.STONE_TABLET);
		registerDefaultState(defaultBlockState().setValue(CARVED, false)); //defaultState set in decor block has waterlogged
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
		if(builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY) instanceof ItemStackBlockEntity itemBE)
		{
			builder = builder.withDynamicDrop(ItemStackBlockEntity.ITEM_DYNAMIC, consumer -> consumer.accept(itemBE.getStack()));
		}
		
		return super.getDrops(state, builder);
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
	{
		if(!player.isShiftKeyDown())
		{
			if(level.isClientSide && level.getBlockEntity(pos) instanceof ItemStackBlockEntity itemStackBE)
			{
				String text = StoneTabletItem.hasText(itemStackBE.getStack()) ? itemStackBE.getStack().getTag().getString("text") : "";
				MSScreenFactories.displayStoneTabletScreen(player, hand, text, false);
			}
		} else
		{
			if(!level.isClientSide)
				dropTablet(level, pos);
		}
		
		return InteractionResult.SUCCESS;
	}
	
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context)
	{
		return context.getClickedFace() == Direction.UP ? super.getStateForPlacement(context) : null;
	}
	
	@Override
	public ItemStack getCloneItemStack(BlockState state, HitResult target, LevelReader level, BlockPos pos, Player player)
	{
		if(level.getBlockEntity(pos) instanceof ItemStackBlockEntity blockEntity)
		{
			ItemStack tabletItemStack = blockEntity.getStack();
			if(!tabletItemStack.isEmpty())
				return tabletItemStack.copy();
		}
		return super.getCloneItemStack(state, target, level, pos, player);
	}
	
	public static void dropTablet(Level level, BlockPos pos)
	{
		if(level.getBlockEntity(pos) instanceof ItemStackBlockEntity blockEntity)
		{
			ItemStack stack = blockEntity.getStack();
			popResource(level, pos, stack);
		}
		level.removeBlock(pos, false);
	}
	
	@Override
	public PushReaction getPistonPushReaction(BlockState state)
	{
		return PushReaction.DESTROY;
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		super.createBlockStateDefinition(builder);
		builder.add(CARVED);
	}
}
