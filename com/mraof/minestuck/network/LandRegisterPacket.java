package com.mraof.minestuck.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.DimensionManager;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.world.storage.MinestuckSaveHandler;

import cpw.mods.fml.common.network.Player;

public class LandRegisterPacket extends MinestuckPacket
{
	byte[] landDimensions;
	
	public LandRegisterPacket() 
	{
		super(Type.LANDREGISTER);
	}

	@Override
	public byte[] generatePacket(Object... data) 
	{
		ByteArrayDataOutput dat = ByteStreams.newDataOutput();
		landDimensions = (byte[]) data[0];
			dat.write(landDimensions);
		return dat.toByteArray();
	}

	@Override
	public MinestuckPacket consumePacket(byte[] data) 
	{
		ByteArrayDataInput dat = ByteStreams.newDataInput(data);
		landDimensions = new byte[data.length];
		dat.readFully(landDimensions);
		return this;
	}

	@Override
	public void execute(INetworkManager network, MinestuckPacketHandler minestuckPacketHandler, Player player, String userName) 
	{
		if(MinecraftServer.getServer() != null)
			return;	//Nope, no editing the server's land list
		
		MinestuckSaveHandler.lands.clear();
		
		for(byte dimensionId : landDimensions)
		{
			MinestuckSaveHandler.lands.add((byte)dimensionId);
			//Debug.print(dimensionId);
			if(!DimensionManager.isDimensionRegistered(dimensionId))
				DimensionManager.registerDimension(dimensionId, Minestuck.landProviderTypeId);
		}
	}
	
}
