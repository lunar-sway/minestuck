package com.mraof.minestuck.item.block;

import com.mraof.minestuck.tileentity.TransportalizerTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class TransportalizerItem extends BlockItem
{
	public TransportalizerItem(Block blockIn, Properties builder)
	{
		super(blockIn, builder);
	}
	
	@Override
	protected boolean updateCustomBlockEntityTag(BlockPos pos, World world, @Nullable PlayerEntity player, ItemStack stack, BlockState state)
	{
		if(stack.hasCustomHoverName() && stack.getHoverName().getString().length() == 4)
		{
			TileEntity te = world.getBlockEntity(pos);
			if(te instanceof TransportalizerTileEntity)
				((TransportalizerTileEntity) te).setId(stack.getHoverName().getString().toUpperCase());
		}
		return true;
	}
}