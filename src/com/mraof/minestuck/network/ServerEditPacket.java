package com.mraof.minestuck.network;

import io.netty.buffer.ByteBuf;

import java.util.EnumSet;

import net.minecraft.entity.player.EntityPlayer;

import com.mraof.minestuck.editmode.ClientEditHandler;

import cpw.mods.fml.relauncher.Side;

public class ServerEditPacket extends MinestuckPacket {
	
	String target;
	int posX, posZ;
	boolean[] givenItems;
	
	public ServerEditPacket() {
		super(Type.SERVER_EDIT);
	}

	@Override
	public MinestuckPacket generatePacket(Object... dat) {
		if(dat.length == 0) {
			return this;
		}
		if(dat.length == 1) {
			data.writeBoolean(true);
			for(boolean b : (boolean[])dat[0])
				data.writeBoolean(b);
			return this;
		}
		data.writeBoolean(false);
		writeString(data,dat[0].toString()+"\n");
		data.writeInt((Integer)dat[1]);
		data.writeInt((Integer)dat[2]);
		for(boolean b : (boolean[])dat[3])
			data.writeBoolean(b);
		
		return this;
	}

	@Override
	public MinestuckPacket consumePacket(ByteBuf data) {
		if(data.readableBytes() == 0)
			return this;
		if(data.readBoolean()) {
			givenItems = new boolean[data.readableBytes()];
			for(int i = 0; i < givenItems.length; i++) {
				givenItems[i] = data.readBoolean();
			}
			return this;
		}
		target = readLine(data);
		posX = data.readInt();
		posZ = data.readInt();
		givenItems = new boolean[data.readableBytes()];
		for(int i = 0; i < givenItems.length; i++) {
			givenItems[i] = data.readBoolean();
		}
		
		return this;
	}

	@Override
	public void execute(EntityPlayer player) {
		ClientEditHandler.onClientPackage(target, posX, posZ, givenItems);
	}

	@Override
	public EnumSet<Side> getSenderSide() {
		return EnumSet.of(Side.SERVER);
	}

}
