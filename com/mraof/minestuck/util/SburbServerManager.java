package com.mraof.minestuck.util;

import com.mraof.minestuck.tracker.MinestuckPlayerTracker;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemBlock;
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
	public boolean tryUseItem(EntityPlayer entityPlayer, World world, ItemStack stack) {
		return false;
	}
	
	@Override
	public boolean activateBlockOrUseItem(EntityPlayer entityPlayer, World world, ItemStack stack, int par4, int par5, int par6, int par7, float par8, float par9, float par10) {
		if(stack.getItem() instanceof ItemBlock) {
			if(!GristHelper.canAfford(entityPlayer, stack) || !super.activateBlockOrUseItem(entityPlayer, world, stack, par4, par5, par6, par7, par8, par9, par10))
				return false;
			GristHelper.decrease(entityPlayer, GristRegistry.getGristConversion(stack));
			new MinestuckPlayerTracker().updateGristCache(entityPlayer);
			return true;
		}
		return super.activateBlockOrUseItem(entityPlayer, world, stack, par4, par5, par6, par7, par8, par9, par10);
	}
	
	@Override
	public boolean tryHarvestBlock(int par1, int par2, int par3) {
		int grist = GristHelper.getGrist(thisPlayerMP, GristType.Build);
		if(grist > 0 && super.tryHarvestBlock(par1, par2, par3)) {
			GristHelper.setGrist(thisPlayerMP, GristType.Build, grist-1);
			new MinestuckPlayerTracker().updateGristCache(thisPlayerMP);
			return true;
		} else return false;
	}
	
}
