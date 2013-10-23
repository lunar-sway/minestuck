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

public class SburbServerController extends PlayerControllerMP{
	
	public SburbServerController(Minecraft mc, NetClientHandler netHandler) {
		super(mc, netHandler);
		this.setGameType(EnumGameType.CREATIVE);
	}
	
	public static void add(String username){	//Currently only for testing purposes
		Debug.print("Adding player...");
		Minecraft mc = Minecraft.getMinecraft();
//		mc.playerController = new SburbServerController(mc, mc.getNetHandler());
		Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = "Minestuck";
		packet.data = MinestuckPacket.makePacket(Type.SBURB_EDIT, username);
		packet.length = packet.data.length;
		PacketDispatcher.sendPacketToServer(packet);
	}
	
	@Override
	public void attackEntity(EntityPlayer player, Entity entity) {}	//Server can't punch entities
	
}
