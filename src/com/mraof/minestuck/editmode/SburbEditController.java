//package com.mraof.minestuck.editmode;
//
//import net.minecraft.block.Block;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.multiplayer.PlayerControllerMP;
//import net.minecraft.client.network.NetHandlerPlayClient;
//import net.minecraft.entity.player.EntityPlayer;
//import net.minecraft.item.ItemBlock;
//import net.minecraft.item.ItemStack;
//import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
//import net.minecraft.util.Vec3;
//import net.minecraft.world.World;
//import net.minecraft.world.WorldSettings.GameType;
//
//import com.mraof.minestuck.Minestuck;
//import com.mraof.minestuck.item.ItemMachine;
//import com.mraof.minestuck.network.skaianet.SkaiaClient;
//import com.mraof.minestuck.util.GristHelper;
//import com.mraof.minestuck.util.GristRegistry;
//import com.mraof.minestuck.util.GristStorage;
//import com.mraof.minestuck.util.GristType;
//
//public class SburbEditController extends PlayerControllerMP {
//	
//	public NetHandlerPlayClient netHandler;
//	
//	public SburbEditController(Minecraft mc, NetHandlerPlayClient netHandler) {
//		super(mc, netHandler);
//		this.netHandler = netHandler;
//		this.setGameType(GameType.CREATIVE);
//	}
//	
//	@Override
//	public boolean isInCreativeMode() {	//Possibly fixes inventory type without any problems.
//		return false;
//	}
//	@Override
//	public boolean onPlayerRightClick(EntityPlayer entityPlayer, World world, ItemStack stack, int par4, int par5, int par6, int par7, Vec3 par8Vec3) {
//		float f = (float)par8Vec3.xCoord - (float)par4;
//		float f1 = (float)par8Vec3.yCoord - (float)par5;
//		float f2 = (float)par8Vec3.zCoord - (float)par6;
//		
//		if(stack != null && stack.getItem() instanceof ItemBlock && (GristHelper.canAfford(GristStorage.getClientGrist(), GristRegistry.getGristConversion(stack))
//				|| stack.getItem() instanceof ItemMachine && stack.getItemDamage() < 4 && (stack.getItemDamage() != 1 || GristStorage.getClientGrist().getGrist(GristType.Shale) >= 4) &&	//TODO This if-statement could require a cleanup
//				(!Minestuck.clientHardMode || stack.getItemDamage() == 1 || !SkaiaClient.getClientConnection(ClientEditHandler.client).givenItems()[stack.getItemDamage()] || GristStorage.getClientGrist().getGrist(GristType.Build) >= 100))) {
//				ItemBlock item = (ItemBlock)stack.getItem();
//				if(!item.func_150936_a(world, par4, par5, par6, par7, entityPlayer, stack))
//					return false;
//				
//				netHandler.addToSendQueue(new C08PacketPlayerBlockPlacement(par4, par5, par6, par7, entityPlayer.inventory.getCurrentItem(), f, f1, f2));
//				
//				int d = stack.getItemDamage();
//				int size = stack.stackSize;
//				boolean result = stack.tryPlaceItemIntoWorld(entityPlayer, world, par4, par5, par6, par7, f, f1, f2);
//				stack.setItemDamage(d);
//				stack.stackSize = size;
//				
//				if(result && stack.getItem() instanceof ItemMachine && stack.getItemDamage() < 4)
//					ClientEditHandler.givenItems[stack.getItemDamage()] = true;
//				return result;
//			}
//		return false;
//	}
//	
//	@Override
//	public boolean onPlayerDestroyBlock(int par1, int par2, int par3, int par4) {
//		Block block = Minecraft.getMinecraft().theWorld.getBlock(par1, par2, par3);
//		if(block.getBlockHardness(Minecraft.getMinecraft().theWorld, par1, par2, par3) < 0)
//			return false;
//		
//		int grist = GristStorage.getClientGrist().getGrist(GristType.Build);
//		
//		return grist != 0 && super.onPlayerDestroyBlock(par1, par2, par3, par4);
//	}
//	
//}
