package com.mraof.minestuck.item.block;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.item.AlchemizedColored;
import com.mraof.minestuck.blockentity.machine.MiniCruxtruderBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.logging.log4j.LogManager;

import javax.annotation.Nullable;

public class MiniCruxtruderItem extends BlockItem implements AlchemizedColored
{
	public MiniCruxtruderItem(Block blockIn, Properties builder)
	{
		super(blockIn, builder);
	}
	
	@Override
	protected boolean updateCustomBlockEntityTag(BlockPos pos, Level level, @Nullable Player player, ItemStack stack, BlockState state)
	{
		if(stack.hasTag() && stack.getTag().contains("color"))
		{
			BlockEntity be = level.getBlockEntity(pos);
			if(be instanceof MiniCruxtruderBlockEntity blockEntity)
				blockEntity.color = stack.getTag().getInt("color");
			else LogManager.getLogger().warn("Placed miniature cruxtruder, but no appropriate block entity was created. Instead found {}.", be);
		}
		return true;
	}
	
	public static ItemStack getCruxtruderWithColor(int color)
	{
		ItemStack stack = new ItemStack(MSBlocks.MINI_CRUXTRUDER.get());
		stack.setTag(new CompoundTag());
		stack.getTag().putInt("color", color);
		return stack;
	}
}