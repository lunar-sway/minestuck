package com.mraof.minestuck.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.FMLIndexedMessageToMessageCodec;
import net.minecraftforge.fml.common.network.FMLOutboundHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.client.ClientProxy;
import com.mraof.minestuck.network.MinestuckPacket.Type;
import com.mraof.minestuck.util.Debug;

public class MinestuckChannelHandler extends FMLIndexedMessageToMessageCodec<MinestuckPacket> 
{
	
	public static MinestuckChannelHandler instance = new MinestuckChannelHandler();
	
	public MinestuckChannelHandler() {
		for(Type type : Type.values())
			addDiscriminator(type.ordinal(), type.packetType);
	}
	
	@Override
	public void encodeInto(ChannelHandlerContext ctx, MinestuckPacket msg, ByteBuf target) throws Exception 
	{
		target.writeBytes(msg.data);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf source, MinestuckPacket msg) 
	{
		msg.consumePacket(source);
		switch (FMLCommonHandler.instance().getEffectiveSide()) {
		case CLIENT:
			msg.execute(ClientProxy.getPlayer()); //Prevents the classloader from crashing the server.
			break;
		case SERVER:
			INetHandler netHandler = ctx.channel().attr(NetworkRegistry.NET_HANDLER).get();
			msg.execute(((NetHandlerPlayServer) netHandler).playerEntity);
			break;
		}
	}
	
	public static void sendToServer(MinestuckPacket packet)
	{
		Minestuck.channels.get(Side.CLIENT).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.TOSERVER);
		Minestuck.channels.get(Side.CLIENT).writeOutbound(packet);
	}
	
	public static void sendToPlayer(MinestuckPacket packet, EntityPlayer player)
	{
		Minestuck.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.PLAYER);
		Minestuck.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(player);
		Minestuck.channels.get(Side.SERVER).writeOutbound(packet);
	}
	
	public static void sendToAllPlayers(MinestuckPacket packet)
	{
		Minestuck.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALL);
		Minestuck.channels.get(Side.SERVER).writeOutbound(packet);
	}
	
}
