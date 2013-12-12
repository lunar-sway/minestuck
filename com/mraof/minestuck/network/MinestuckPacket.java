package com.mraof.minestuck.network;

import java.util.Arrays;
import java.util.EnumSet;

import net.minecraft.network.INetworkManager;

import com.google.common.primitives.Bytes;
import com.google.common.primitives.UnsignedBytes;
import com.mraof.minestuck.util.Debug;

import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;

public abstract class MinestuckPacket 
{
	public enum Type
	{
		GRISTCACHE(GristCachePacket.class),
		COMBOBUTTON(ComboButtonPacket.class),
		GOBUTTON(GoButtonPacket.class),
		TITLE(TitlePacket.class),
		SBURB_CONNECT(SburbConnectPacket.class),
		SBURB_CLOSE(SburbConnectClosedPacket.class),
		LANDREGISTER(LandRegisterPacket.class),
		CLEAR(ClearMessagePacket.class),
		SBURB_INFO(SkaianetInfoPacket.class),
		CLIENT_EDIT(ClientEditPacket.class),
		SERVER_EDIT(ServerEditPacket.class),
		CONFIG(MinestuckConfigPacket.class);
		
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

    public static MinestuckPacket readPacket(INetworkManager network, byte[] payload, Side side)
    {
        int type = UnsignedBytes.toInt(payload[0]);
        Type eType = Type.values()[type];
        MinestuckPacket pkt;
            pkt = eType.make();
            if(!pkt.getSenderSide().contains(side.isClient()?Side.SERVER:Side.CLIENT)) {
            	Debug.printf("Packet on wrong side of type %s, side %s. Discarding packet.",pkt.getClass().getName(),side.toString());
            	return null;
            }
        return pkt.consumePacket(Arrays.copyOfRange(payload, 1, payload.length), side);
    }
    public abstract byte[] generatePacket(Object... data);

    public abstract MinestuckPacket consumePacket(byte[] data, Side side);
    public abstract void execute(INetworkManager network, MinestuckPacketHandler minestuckPacketHandler, Player player, String userName);
    public abstract EnumSet<Side> getSenderSide();
}
