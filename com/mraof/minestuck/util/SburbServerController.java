package com.mraof.minestuck.util;

import com.mraof.minestuck.grist.GristHelper;
import com.mraof.minestuck.grist.GristRegistry;
import com.mraof.minestuck.grist.GristStorage;
import com.mraof.minestuck.grist.GristType;
import com.mraof.minestuck.network.MinestuckPacket;
import com.mraof.minestuck.network.MinestuckPacket.Type;
import com.mraof.minestuck.tracker.MinestuckPlayerTracker;

import cpw.mods.fml.common.network.PacketDispatcher;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.NetClientHandler;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemInWorldManager;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Vec3;
import net.minecraft.world.EnumGameType;
import net.minecraft.world.World;

public class SburbServerController extends PlayerControllerMP {
	
	public String client;
	
	public SburbServerController(Minecraft mc, NetClientHandler netHandler) {
		super(mc, netHandler);
		this.setGameType(EnumGameType.CREATIVE);
	}
	
	@Override
	public boolean onPlayerRightClick(EntityPlayer entityPlayer, World world, ItemStack stack, int par4, int par5, int par6, int par7, Vec3 par8Vec3) {
		if(stack != null && stack.getItem() instanceof ItemBlock)
			if(!GristHelper.canAfford(GristStorage.getClientGrist(), GristRegistry.getGristConversion(stack)) || !super.onPlayerRightClick(entityPlayer, world, stack, par4, par5, par6, par7, par8Vec3))
				return false;
			else return true;
		return false;
	}
	
	@Override
	public boolean onPlayerDestroyBlock(int par1, int par2, int par3, int par4) {
		Block block = Block.blocksList[Minecraft.getMinecraft().theWorld.getBlockId(par1, par2, par3)];
		if(block.getBlockHardness(Minecraft.getMinecraft().theWorld, par1, par2, par3) < 0)
			return false;
		
		int grist = GristHelper.getGrist(client, GristType.Build);
		
		return grist != 0 && super.onPlayerDestroyBlock(par1, par2, par3, par4);
	}
	
	@Override
	public void attackEntity(EntityPlayer player, Entity entity) {}
	
}
