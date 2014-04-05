package com.mraof.minestuck.network;

import io.netty.buffer.ByteBuf;

import java.util.EnumSet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.DimensionManager;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.world.storage.MinestuckSaveHandler;

import cpw.mods.fml.relauncher.Side;

public class LandRegisterPacket extends MinestuckPacket
{
	byte[] landDimensions;
	
	public static MinestuckPacket createPacket() {
		
		LandRegisterPacket packet = new LandRegisterPacket();
		packet.landDimensions = new byte[MinestuckSaveHandler.lands.size()];
		for(int i = 0; i < packet.landDimensions.length; i++)
			packet.landDimensions[i] = MinestuckSaveHandler.lands.get(i);
		
		return packet;
	}
	
	public LandRegisterPacket() 
	{
		super(Type.LANDREGISTER);
	}

	@Override
	public MinestuckPacket generatePacket(Object... dat) 
	{
		data.writeBytes((byte[]) dat[0]);
		return this;
	}

	@Override
	public MinestuckPacket consumePacket(ByteBuf data) 
	{
		
		landDimensions = new byte[data.readableBytes()];
		data.readBytes(landDimensions);
		return this;
	}

	@Override
	public void execute(EntityPlayer player) 
	{
		if(MinecraftServer.getServer() != null)
			return;	//Nope, no editing the server's land list
		
		MinestuckSaveHandler.lands.clear();
		
		for(byte dimensionId : landDimensions)
		{
			MinestuckSaveHandler.lands.add((byte)dimensionId);
			Debug.printf("Adding Land dimension with id of %d", dimensionId);
			if(!DimensionManager.isDimensionRegistered(dimensionId))
				DimensionManager.registerDimension(dimensionId, Minestuck.landProviderTypeId);
		}
	}

	@Override
	public EnumSet<Side> getSenderSide() {
		return EnumSet.of(Side.SERVER);
	}
	
}
