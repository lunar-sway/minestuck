package com.mraof.minestuck.inventory.musicplayer;

import com.mraof.minestuck.util.MSTags;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class CassetteItemHandler extends ItemStackHandler
{
	public CassetteItemHandler(int numberOfSlots) {
		super(numberOfSlots);
	}
	
	@Override
	public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
		return stack.is(MSTags.Items.CASSETTES);
	}
}
