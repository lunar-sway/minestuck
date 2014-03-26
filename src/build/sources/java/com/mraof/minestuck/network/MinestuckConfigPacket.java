package com.mraof.minestuck.network;

import io.netty.buffer.ByteBuf;

import java.util.EnumSet;

import net.minecraft.entity.player.EntityPlayer;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.UsernameHandler;

import cpw.mods.fml.relauncher.Side;

public class MinestuckConfigPacket extends MinestuckPacket {
	
	int overWorldEditRange;
	int landEditRange;
	
	boolean hardMode;
	
	String lanHost;
	
	public MinestuckConfigPacket() {
		super(Type.CONFIG);
	}

	@Override
	public MinestuckPacket generatePacket(Object... dat) {
		data.writeInt(Minestuck.overworldEditRange);
		data.writeInt(Minestuck.landEditRange);
		data.writeBoolean(Minestuck.hardMode);
		if(UsernameHandler.host != null)
			writeString(data,UsernameHandler.host);
		
		return this;
	}
	
	@Override
	public MinestuckPacket consumePacket(ByteBuf data) {
		overWorldEditRange = data.readInt();
		landEditRange = data.readInt();
		hardMode = data.readBoolean();
		lanHost = readLine(data);
		if(lanHost.isEmpty())
			lanHost = null;
		Debug.print("Recived packet! Host is "+lanHost);
		return this;
	}

	@Override
	public void execute(EntityPlayer player) {
		
		Minestuck.clientOverworldEditRange = this.overWorldEditRange;
		Minestuck.clientLandEditRange = this.landEditRange;
		Minestuck.clientHardMode = this.hardMode;
		UsernameHandler.host = lanHost;
		
	}

	@Override
	public EnumSet<Side> getSenderSide() {
		return EnumSet.of(Side.SERVER);
	}

}
