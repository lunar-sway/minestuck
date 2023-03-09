package com.mraof.minestuck.item.block;

import com.mraof.minestuck.block.StoneTabletBlock;
import com.mraof.minestuck.client.gui.MSScreenFactories;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.blockentity.ItemStackBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.List;

public class StoneTabletItem extends BlockItem //stone slab is the same as stone tablet, both are used in different circumstances
{
	public StoneTabletItem(Block blockIn, Properties properties)
	{
		super(blockIn, properties);
	}
	
	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flagIn)
	{
		super.appendHoverText(stack, level, tooltip, flagIn);
		
		if(hasText(stack))
			tooltip.add(Component.translatable(getDescriptionId() + ".carved").withStyle(ChatFormatting.GRAY));
	}
	
	public static boolean hasText(ItemStack stack)
	{
		CompoundTag nbt = stack.getTag();
		return (nbt != null && nbt.contains("text") && !nbt.getString("text").isEmpty());
	}
	
	@Nullable
	@Override
	protected BlockState getPlacementState(BlockPlaceContext context)
	{
		BlockState state = super.getPlacementState(context);
		if(state == null)
			return null;
		
		ItemStack stack = context.getItemInHand();
		
		state = state.setValue(StoneTabletBlock.FACING, context.getHorizontalDirection()).setValue(StoneTabletBlock.CARVED, hasText(stack));
		return state;
	}
	
	@Override
	protected boolean updateCustomBlockEntityTag(BlockPos pos, Level level, @Nullable Player player, ItemStack stack, BlockState state)
	{
		if(level.getBlockEntity(pos) instanceof ItemStackBlockEntity blockEntity)
		{
			ItemStack newStack = stack.copy();
			newStack.setCount(1);
			blockEntity.setStack(newStack);
		}
		return true;
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player playerIn, InteractionHand handIn)
	{
		ItemStack itemStackIn = playerIn.getItemInHand(handIn);
		
		if(!playerIn.isShiftKeyDown())
		{
			//Display the stone tablet screen
			if(level.isClientSide)
			{
				boolean canEdit = playerIn.getItemInHand(handIn == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND).getItem() == MSItems.CARVING_TOOL.get();
				String text = hasText(itemStackIn) ? itemStackIn.getTag().getString("text") : "";
				MSScreenFactories.displayStoneTabletScreen(playerIn, handIn, text, canEdit);
			}
			return InteractionResultHolder.success(itemStackIn);
		} else
		{
			return super.use(level, playerIn, handIn);
		}
	}
	
	@Override
	public InteractionResult place(BlockPlaceContext context)
	{
		Player playerIn = context.getPlayer();
		if(playerIn == null || playerIn.isShiftKeyDown())
			return super.place(context);
		return InteractionResult.PASS;
	}
}