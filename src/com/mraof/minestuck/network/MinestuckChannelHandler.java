package com.mraof.minestuck.network;

import java.util.EnumMap;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.client.ClientProxy;
import com.mraof.minestuck.network.MinestuckPacket.Type;
import com.mraof.minestuck.util.Debug;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.FMLIndexedMessageToMessageCodec;
import cpw.mods.fml.common.network.FMLOutboundHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;

public class MinestuckChannelHandler extends FMLIndexedMessageToMessageCodec<MinestuckPacket> 
{
	
	public static MinestuckChannelHandler instance = new MinestuckChannelHandler();
	public static EnumMap<Side, FMLEmbeddedChannel> channels;
	
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
	}
	
	private static class MinestuckPacketHandler extends SimpleChannelInboundHandler<MinestuckPacket>
	{
		private final Side side;
		private MinestuckPacketHandler(Side side)
		{
			this.side = side;
		}
		@Override
		protected void channelRead0(ChannelHandlerContext ctx, MinestuckPacket msg) throws Exception
		{
			switch (side)
			{
			case CLIENT:
				msg.execute(ClientProxy.getPlayer()); //Prevents the classloader from crashing the server.
				break;
			case SERVER:
				INetHandler netHandler = ctx.channel().attr(NetworkRegistry.NET_HANDLER).get();
				msg.execute(((NetHandlerPlayServer) netHandler).playerEntity);
				break;
			}
		}
	}
	
	public static void setupChannel()
	{
		if(channels == null)
		{
			channels = NetworkRegistry.INSTANCE.newChannel("Minestuck", MinestuckChannelHandler.instance);
			String targetName = channels.get(Side.CLIENT).findChannelHandlerNameForType(MinestuckChannelHandler.class);
			channels.get(Side.CLIENT).pipeline().addAfter(targetName, "MinestuckPacketHandler", new MinestuckPacketHandler(Side.CLIENT));
			targetName = channels.get(Side.SERVER).findChannelHandlerNameForType(MinestuckChannelHandler.class);	//Not sure if this is necessary
			channels.get(Side.SERVER).pipeline().addAfter(targetName, "MinestuckPacketHandler", new MinestuckPacketHandler(Side.SERVER));
		}
	}
	
	public static void sendToServer(MinestuckPacket packet)
	{
		channels.get(Side.CLIENT).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.TOSERVER);
		channels.get(Side.CLIENT).writeOutbound(packet);
	}
	
	public static void sendToPlayer(MinestuckPacket packet, EntityPlayer player)
	{
		channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.PLAYER);
		channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(player);
		channels.get(Side.SERVER).writeOutbound(packet);
	}
	
	public static void sendToAllPlayers(MinestuckPacket packet)
	{
		channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALL);
		channels.get(Side.SERVER).writeOutbound(packet);
	}
	
}
