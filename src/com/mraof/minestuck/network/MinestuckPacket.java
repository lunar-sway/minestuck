package com.mraof.minestuck.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.EnumSet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;

public abstract class MinestuckPacket 
{
	public enum Type
	{
		GRISTCACHE(GristCachePacket.class),
		MACHINE_STATE(MachineStatePacket.class),
		GOBUTTON(GoButtonPacket.class),
		ALCHEMITER_PACKET(AlchemiterPacket.class),
		PLAYER_DATA(PlayerDataPacket.class),
		SBURB_CONNECT(SburbConnectPacket.class),
		SBURB_CLOSE(SburbConnectClosedPacket.class),
		LANDREGISTER(LandRegisterPacket.class),
		CLEAR(ClearMessagePacket.class),
		SBURB_INFO(SkaianetInfoPacket.class),
		CLIENT_EDIT(ClientEditPacket.class),
		SERVER_EDIT(ServerEditPacket.class),
		CONFIG(MinestuckConfigPacket.class), 
		TRANSPORTALIZER(TransportalizerPacket.class),
		CONTAINER(MiscContainerPacket.class),
		INVENTORY(InventoryChangedPacket.class),
		CAPTCHA(CaptchaDeckPacket.class),
		SELECTION(SelectionPacket.class),
		DATA_CHECKER(DataCheckerPacket.class),
		EFFECT_TOGGLE(EffectTogglePacket.class),
		;
		
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
	
	protected ByteBuf data = Unpooled.buffer();
	
    public static MinestuckPacket makePacket(Type type, Object... dat)
    {
        return type.make().generatePacket(dat);
    }
	
	public static String readLine(ByteBuf data)
	{
		StringBuilder str = new StringBuilder();
		while(data.readableBytes() > 0)
		{
			char c = data.readChar();
			if(c == '\n')
				break;
			else str.append(c);
		}
		
		return str.toString();
	}
	
	public static void writeString(ByteBuf data, String str)
	{
		for(int i = 0; i < str.length(); i++)
			data.writeChar(str.charAt(i));
	}
	
    public abstract MinestuckPacket generatePacket(Object... data);

    public abstract MinestuckPacket consumePacket(ByteBuf data);
    public abstract void execute(EntityPlayer player);
    public abstract EnumSet<Side> getSenderSide();
}
