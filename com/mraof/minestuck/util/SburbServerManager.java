package com.mraof.minestuck.util;

import com.mraof.minestuck.grist.GristHelper;
import com.mraof.minestuck.grist.GristRegistry;
import com.mraof.minestuck.grist.GristType;
import com.mraof.minestuck.tracker.MinestuckPlayerTracker;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemInWorldManager;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.Packet53BlockChange;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.EnumGameType;
import net.minecraft.world.World;

public class SburbServerManager extends ItemInWorldManager{
	
	String client;
	
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
		if(stack.getItem() instanceof ItemBlock && client != null) {
			if(!GristHelper.canAfford(client, stack) || !super.activateBlockOrUseItem(entityPlayer, world, stack, par4, par5, par6, par7, par8, par9, par10))
				return false;
			GristHelper.decrease(client, GristRegistry.getGristConversion(stack));
			MinestuckPlayerTracker.updateGristCache(client);
			thisPlayerMP.playerNetServerHandler.sendPacketToPlayer(new Packet53BlockChange(par4, par5, par6, theWorld));
			return true;
		}
		return false;
	}
	
	@Override
	public boolean tryHarvestBlock(int par1, int par2, int par3) {
		Block block = Block.blocksList[theWorld.getBlockId(par1, par2, par3)];
		if(block == null || block.getBlockHardness(theWorld, par1, par2, par3) < 0)
			return false;
		int grist = GristHelper.getGrist(client, GristType.Build);
		if(grist > 0 && super.tryHarvestBlock(par1, par2, par3)) {
			GristHelper.setGrist(client, GristType.Build, grist-1);
			MinestuckPlayerTracker.updateGristCache(client);
			return true;
		} else return false;
	}
	
}
