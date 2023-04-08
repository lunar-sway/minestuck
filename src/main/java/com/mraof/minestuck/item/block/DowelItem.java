package com.mraof.minestuck.item.block;

import com.mraof.minestuck.block.CruxiteDowelBlock;
import com.mraof.minestuck.item.AlchemizedColored;
import com.mraof.minestuck.alchemy.AlchemyHelper;
import com.mraof.minestuck.blockentity.ItemStackBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.List;

public class DowelItem extends BlockItem implements AlchemizedColored
{
	
	public DowelItem(Block blockIn, Properties builder)
	{
		super(blockIn, builder);
	}
	
	@Override
	public int getMaxStackSize(ItemStack stack)
	{
		if(stack.hasTag())
			return 16;
		else return 64;
	}
	
	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flagIn)
	{
		if(AlchemyHelper.hasDecodedItem(stack))
		{
			ItemStack containedStack = AlchemyHelper.getDecodedItem(stack);
			
			if(!containedStack.isEmpty())
			{
				tooltip.add(Component.literal("(").append(containedStack.getHoverName()).append(")").withStyle(ChatFormatting.GRAY));
			}
			else
			{
				tooltip.add(Component.literal("(").append(Component.translatable(getDescriptionId() + ".invalid")).append(")").withStyle(ChatFormatting.GRAY));
			}
		}
	}
	
	@Nullable
	@Override
	protected BlockState getPlacementState(BlockPlaceContext context)
	{
		BlockState state = super.getPlacementState(context);
		if(state == null)
			return null;
		
		ItemStack stack = context.getItemInHand();
		if(AlchemyHelper.hasDecodedItem(stack))
			state = state.setValue(CruxiteDowelBlock.DOWEL_TYPE, CruxiteDowelBlock.Type.TOTEM);
		else
			state = state.setValue(CruxiteDowelBlock.DOWEL_TYPE, CruxiteDowelBlock.Type.DOWEL);
		return state;
	}
	
	@Override
	protected boolean updateCustomBlockEntityTag(BlockPos pos, Level level, @Nullable Player player, ItemStack stack, BlockState state)
	{
		BlockEntity be = level.getBlockEntity(pos);
		if(be instanceof ItemStackBlockEntity)
		{
			ItemStack newStack = stack.copy();
			newStack.setCount(1);
			((ItemStackBlockEntity) be).setStack(newStack);
		}
		return true;
	}
}