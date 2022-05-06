package com.mraof.minestuck.item.block;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.item.AlchemizedColored;
import com.mraof.minestuck.tileentity.machine.MiniCruxtruderTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;

import javax.annotation.Nullable;

public class MiniCruxtruderItem extends BlockItem implements AlchemizedColored
{
	public MiniCruxtruderItem(Block blockIn, Properties builder)
	{
		super(blockIn, builder);
	}
	
	@Override
	protected boolean updateCustomBlockEntityTag(BlockPos pos, World world, @Nullable PlayerEntity player, ItemStack stack, BlockState state)
	{
		if(stack.hasTag() && stack.getTag().contains("color"))
		{
			TileEntity te = world.getBlockEntity(pos);
			if(te instanceof MiniCruxtruderTileEntity)
				((MiniCruxtruderTileEntity) te).color = stack.getTag().getInt("color");
			else LogManager.getLogger().warn("Placed miniature cruxtruder, but no appropriate tile entity was created. Instead found {}.", te);
		}
		return true;
	}
	
	public static ItemStack getCruxtruderWithColor(int color)
	{
		ItemStack stack = new ItemStack(MSBlocks.MINI_CRUXTRUDER);
		stack.setTag(new CompoundNBT());
		stack.getTag().putInt("color", color);
		return stack;
	}
}