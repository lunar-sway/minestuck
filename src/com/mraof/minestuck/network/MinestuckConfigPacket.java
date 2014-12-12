package com.mraof.minestuck.network;

import io.netty.buffer.ByteBuf;

import java.util.EnumSet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.relauncher.Side;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.editmode.DeployList;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.UsernameHandler;

public class MinestuckConfigPacket extends MinestuckPacket {
	
	int overWorldEditRange;
	int landEditRange;

	boolean hardMode;
	boolean giveItems;
	boolean easyDesignix;
	boolean[] deployValues;
	
	String lanHost;

	public MinestuckConfigPacket() {
		super(Type.CONFIG);
	}

	@Override
	public MinestuckPacket generatePacket(Object... dat) {
		data.writeInt(Minestuck.overworldEditRange);
		data.writeInt(Minestuck.landEditRange);
		data.writeBoolean(Minestuck.hardMode);
		data.writeBoolean(Minestuck.giveItems);
		data.writeBoolean(Minestuck.easyDesignix);
		for(int i = 0; i < Minestuck.deployConfigurations.length; i++)
			data.writeBoolean(Minestuck.deployConfigurations[i]);
		if(UsernameHandler.host != null)
			writeString(data,UsernameHandler.host);
		
		return this;
	}
	
	@Override
	public MinestuckPacket consumePacket(ByteBuf data) {
		overWorldEditRange = data.readInt();
		landEditRange = data.readInt();
		hardMode = data.readBoolean();
		giveItems = data.readBoolean();
		easyDesignix = data.readBoolean();
		deployValues = new boolean[Minestuck.deployConfigurations.length];
		for(int i = 0; i < deployValues.length; i++)
			deployValues[i] = data.readBoolean();
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
		Minestuck.clientGiveItems = this.giveItems;
		Minestuck.clientEasyDesignix = this.easyDesignix;
		if(MinecraftServer.getServer() == null || !MinecraftServer.getServer().isServerRunning())
		{
			UsernameHandler.host = lanHost;
			DeployList.applyConfigValues(deployValues);
		}
		
	}

	@Override
	public EnumSet<Side> getSenderSide() {
		return EnumSet.of(Side.SERVER);
	}

}
