package com.mraof.minestuck.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemInWorldManager;
import net.minecraft.item.ItemStack;
import net.minecraft.world.EnumGameType;
import net.minecraft.world.World;

public class SburbServerManager extends ItemInWorldManager{
	
	public SburbServerManager(World par1World, EntityPlayerMP player) {
		super(par1World);
		this.thisPlayerMP = player;
		this.setGameType(EnumGameType.CREATIVE);
	}
	
	@Override
	public boolean tryHarvestBlock(int par1, int par2, int par3) {
		// TODO Auto-generated method stub
		return super.tryHarvestBlock(par1, par2, par3);
	}
	
}
