package com.mraof.minestuck.item.block;

import com.mraof.minestuck.blockentity.TransportalizerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.List;

public class TransportalizerItem extends BlockItem
{
	public TransportalizerItem(Block blockIn, Properties builder)
	{
		super(blockIn, builder);
	}
	
	@Override
	protected boolean updateCustomBlockEntityTag(BlockPos pos, Level level, @Nullable Player player, ItemStack stack, BlockState state)
	{
		BlockEntity be = level.getBlockEntity(pos);
		if(stack.hasTag() && be instanceof TransportalizerBlockEntity transportalizer)
		{
			transportalizer.setId(stack.getTag().getString("idString"));
			transportalizer.setDestId(stack.getTag().getString("destId"));
		}
		return true;
	}
	
	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flagIn)
	{
		if (!stack.hasTag())
			return;
		if (stack.getTag().contains("idString", Tag.TAG_STRING))
			tooltip.add(Component.translatable("block.minestuck.transportalizer.idString", stack.getTag().getString("idString")));
		if (stack.getTag().contains("destId", Tag.TAG_STRING))
			tooltip.add(Component.translatable("block.minestuck.transportalizer.destId", stack.getTag().getString("destId")));
	}
}