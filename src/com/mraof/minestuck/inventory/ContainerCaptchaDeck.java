package com.mraof.minestuck.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class ContainerCaptchaDeck extends Container {

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return true;
	}

}
