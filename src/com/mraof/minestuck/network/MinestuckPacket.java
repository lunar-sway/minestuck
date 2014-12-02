package com.mraof.minestuck.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.EnumSet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;

import com.mraof.minestuck.util.Debug;

public abstract class MinestuckPacket 
{
	public enum Type
	{
		GRISTCACHE(GristCachePacket.class),
		MACHINE_STATE(MachineStatePacket.class),
		GOBUTTON(GoButtonPacket.class),
		TITLE(TitlePacket.class),
		SBURB_CONNECT(SburbConnectPacket.class),
		SBURB_CLOSE(SburbConnectClosedPacket.class),
		LANDREGISTER(LandRegisterPacket.class),
		CLEAR(ClearMessagePacket.class),
		SBURB_INFO(SkaianetInfoPacket.class),
		CLIENT_EDIT(ClientEditPacket.class),
		SERVER_EDIT(ServerEditPacket.class),
		CONFIG(MinestuckConfigPacket.class), 
		INFO(MinestuckInfoPacket.class),
		TRANSPORTALIZER(TransportalizerPacket.class),
		CONTAINER(MiscContainerPacket.class),
		INVENTORY(InventoryChangedPacket.class),
		CAPTCHA(CaptchaDeckPacket.class);
		
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
	
	protected ByteBuf data = Unpooled.buffer();
	
	public MinestuckPacket()
	{
	}
	public MinestuckPacket(Type type)
	{
		this.type = type;
	}
    public static MinestuckPacket makePacket(Type type, Object... dat)
    {
        return type.make().generatePacket(dat);
    }
    
	String readLine(ByteBuf data) {
		StringBuilder str = new StringBuilder();
		while(data.readableBytes() > 1) {
			char c = data.readChar();
//			Debug.print("Read char "+c+", packet "+this.getClass().getName());
//			Debug.print(((short)c)+","+((short)".client".charAt(data.readerIndex()/2)));
			if(c == '\n') {
				break;
			} else str.append(c);
		}
		
		return str.toString();
	}
	
	void writeString(ByteBuf data, String str) {
		for(int i = 0; i < str.length(); i++)
			data.writeChar(str.charAt(i));
	}
	
    public abstract MinestuckPacket generatePacket(Object... data);

    public abstract MinestuckPacket consumePacket(ByteBuf data);
    public abstract void execute(EntityPlayer player);
    public abstract EnumSet<Side> getSenderSide();
}
