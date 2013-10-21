package com.mraof.minestuck.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemInWorldManager;
import net.minecraft.item.ItemStack;
import net.minecraft.world.EnumGameType;
import net.minecraft.world.World;

public class SburbServerManager extends ItemInWorldManager{
	
	public SburbServerManager(World par1World) {
		super(par1World);
		this.setGameType(EnumGameType.CREATIVE);
	}
	
}
