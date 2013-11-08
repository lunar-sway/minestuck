package com.mraof.minestuck.network;

import net.minecraft.network.INetworkManager;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.mraof.minestuck.Minestuck;

import cpw.mods.fml.common.network.Player;

public class MinestuckConfigPacket extends MinestuckPacket {
	
	int overWorldEditRange;
	int landEditRange;
	
	boolean hardMode;
	
	public MinestuckConfigPacket() {
		super(Type.CONFIG);
	}

	@Override
	public byte[] generatePacket(Object... data) {
		ByteArrayDataOutput dat = ByteStreams.newDataOutput();
		dat.writeInt(Minestuck.overworldEditRange);
		dat.writeInt(Minestuck.landEditRange);
		dat.writeBoolean(Minestuck.hardMode);
		
		return dat.toByteArray();
	}

	@Override
	public MinestuckPacket consumePacket(byte[] data) {
		ByteArrayDataInput dat = ByteStreams.newDataInput(data);
		overWorldEditRange = dat.readInt();
		landEditRange = dat.readInt();
		hardMode = dat.readBoolean();
		return this;
	}

	@Override
	public void execute(INetworkManager network, MinestuckPacketHandler minestuckPacketHandler, Player player, String userName) {
		
		Minestuck.clientOverworldEditRange = this.overWorldEditRange;
		Minestuck.clientLandEditRange = this.landEditRange;
		Minestuck.clientHardMode = this.hardMode;
		
	}

}
