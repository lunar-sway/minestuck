package com.mraof.minestuck.item.block;

import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.tileentity.TileEntityMiniCruxtruder;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class ItemMiniCruxtruder extends ItemBlock
{
	public ItemMiniCruxtruder(Block blockIn, Properties builder)
	{
		super(blockIn, builder);
	}
	
	@Override
	protected boolean onBlockPlaced(BlockPos pos, World world, @Nullable EntityPlayer player, ItemStack stack, IBlockState state)
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
		stack.setTag(new NBTTagCompound());
		stack.getTag().putInt("color", color);
		return stack;
	}
}