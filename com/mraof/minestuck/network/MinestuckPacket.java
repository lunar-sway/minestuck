package com.mraof.minestuck.network;

import java.util.Arrays;

import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.NetHandler;

import com.google.common.primitives.Bytes;
import com.google.common.primitives.UnsignedBytes;

import cpw.mods.fml.common.network.FMLNetworkHandler;
import cpw.mods.fml.common.network.Player;



public abstract class MinestuckPacket 
{
	public enum Type
	{
		GRIST(GristPacket.class);
		Class<? extends MinestuckPacket> packetType;
		private Type(Class<? extends MinestuckPacket> packetClass)
		{
			packetType = packetClass;
		}
		MinestuckPacket make()
		{
			try {
				return this.packetType.newInstance();
			} catch (Exception e) 
			{
				e.printStackTrace();
				return null;
			}
		}
	}
	Type type;
	public MinestuckPacket(Type type)
	{
		this.type = type;
	}
    public static byte[] makePacket(Type type, Object... data)
    {
        byte[] packetData = type.make().generatePacket(data);
        return Bytes.concat(new byte[] { UnsignedBytes.checkedCast(type.ordinal()) }, packetData );
    }

    public static MinestuckPacket readPacket(INetworkManager network, byte[] payload)
    {
        int type = UnsignedBytes.toInt(payload[0]);
        Type eType = Type.values()[type];
        MinestuckPacket pkt;
            pkt = eType.make();
        return pkt.consumePacket(Arrays.copyOfRange(payload, 1, payload.length));
    }
    public abstract byte[] generatePacket(Object... data);

    public abstract MinestuckPacket consumePacket(byte[] data);
    public abstract void execute(INetworkManager network, MinestuckPacketHandler minestuckPacketHandler, Player player, String userName);
}
