package com.mraof.minestuck.util;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemInWorldManager;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.Packet53BlockChange;
import net.minecraft.world.EnumGameType;
import net.minecraft.world.World;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.item.ItemMachine;
import com.mraof.minestuck.network.skaianet.SburbConnection;
import com.mraof.minestuck.network.skaianet.SkaianetHandler;
import com.mraof.minestuck.tracker.MinestuckPlayerTracker;

public class SburbEditManager extends ItemInWorldManager{
	
	String client;
	
	public SburbEditManager(World par1World, EntityPlayerMP player) {
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
		if(stack == null) return false;
		
		if(stack.getItem() instanceof ItemMachine && stack.getItemDamage() < 4) {
			SburbConnection c = ServerEditHandler.getData(thisPlayerMP.username).connection;
			GristSet cost;
			if(stack.getItemDamage() == 1)
				cost = new GristSet(GristType.Shale, 4);
			else cost = Minestuck.hardMode && c.givenItems()[stack.getItemDamage()]?new GristSet(GristType.Build, 100):new GristSet();
			if(GristHelper.canAfford(GristStorage.getGristSet(client), cost) && super.activateBlockOrUseItem(entityPlayer, world, stack, par4, par5, par6, par7, par8, par9, par10)) {
				c.givenItems()[stack.getItemDamage()] = true;
				if(!c.isMain())
					SkaianetHandler.giveItems(client);
				if(!cost.isEmpty())
					GristHelper.decrease(client, cost);
				return true;
			}
		} else if(stack.getItem() instanceof ItemBlock && GristHelper.canAfford(client, stack)
				&& super.activateBlockOrUseItem(entityPlayer, world, stack, par4, par5, par6, par7, par8, par9, par10)) {
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
