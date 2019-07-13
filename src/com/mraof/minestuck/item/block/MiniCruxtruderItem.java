package com.mraof.minestuck.item.block;

import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.tileentity.TileEntityMiniCruxtruder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class MiniCruxtruderItem extends BlockItem
{
	public MiniCruxtruderItem(Block blockIn, Properties builder)
	{
		super(blockIn, builder);
	}
	
	@Override
	protected boolean onBlockPlaced(BlockPos pos, World world, @Nullable PlayerEntity player, ItemStack stack, BlockState state)
	{
		if(stack.hasTag() && stack.getTag().contains("color"))
		{
			TileEntity te = world.getTileEntity(pos);
			if(te instanceof TileEntityMiniCruxtruder)
				((TileEntityMiniCruxtruder) te).color = stack.getTag().getInt("color");
		}
		return true;
	}
	
	public static ItemStack getCruxtruderWithColor(int color)
	{
		ItemStack stack = new ItemStack(MinestuckBlocks.MINI_CRUXTRUDER);
		stack.setTag(new CompoundNBT());
		stack.getTag().putInt("color", color);
		return stack;
	}
}