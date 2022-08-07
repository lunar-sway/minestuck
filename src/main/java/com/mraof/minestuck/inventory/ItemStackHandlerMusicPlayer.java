package com.mraof.minestuck.inventory;

import com.mraof.minestuck.util.MSTags;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class ItemStackHandlerMusicPlayer extends ItemStackHandler
{
	public ItemStackHandlerMusicPlayer() {
		super(1);
	}
	
	@Override
	public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
		if (stack.isEmpty()) return false;
		return stack.is(MSTags.Items.CASSETTES);
	}
}
