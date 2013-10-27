package com.mraof.minestuck.util;

import com.mraof.minestuck.network.MinestuckPacket;
import com.mraof.minestuck.network.MinestuckPacket.Type;

import cpw.mods.fml.common.network.PacketDispatcher;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.NetClientHandler;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemInWorldManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.EnumGameType;
import net.minecraft.world.World;

public class SburbServerController extends PlayerControllerMP {
	
	public SburbServerController(Minecraft mc, NetClientHandler netHandler) {
		super(mc, netHandler);
		this.setGameType(EnumGameType.CREATIVE);
	}
	
	@Override
	public boolean onPlayerDestroyBlock(int par1, int par2, int par3, int par4) {
		int grist = GristHelper.getGrist(Minecraft.getMinecraft().thePlayer, GristType.Build);
		
		return grist != 0 && super.onPlayerDestroyBlock(par1, par2, par3, par4);
	}
	
	@Override
	public void attackEntity(EntityPlayer player, Entity entity) {}	//Server can't punch entities
	
}
