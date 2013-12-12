package com.mraof.minestuck.network;

import java.util.EnumSet;

import net.minecraft.network.INetworkManager;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.util.UsernameHandler;

import cpw.mods.fml.common.network.Player;
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
	public byte[] generatePacket(Object... data) {
		ByteArrayDataOutput dat = ByteStreams.newDataOutput();
		dat.writeInt(Minestuck.overworldEditRange);
		dat.writeInt(Minestuck.landEditRange);
		dat.writeBoolean(Minestuck.hardMode);
		if(UsernameHandler.host != null)
			dat.write(UsernameHandler.host.getBytes());
		dat.write('\n');
		
		return dat.toByteArray();
	}

	@Override
	public MinestuckPacket consumePacket(byte[] data, Side side) {
		ByteArrayDataInput dat = ByteStreams.newDataInput(data);
		overWorldEditRange = dat.readInt();
		landEditRange = dat.readInt();
		hardMode = dat.readBoolean();
		lanHost = dat.readLine();
		if(lanHost.isEmpty())
			lanHost = null;
		return this;
	}

	@Override
	public void execute(INetworkManager network, MinestuckPacketHandler minestuckPacketHandler, Player player, String userName) {
		
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
