package com.mraof.minestuck.network;

import io.netty.buffer.ByteBuf;

import java.util.EnumSet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.relauncher.Side;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.world.storage.MinestuckSaveHandler;

public class LandRegisterPacket extends MinestuckPacket
{
	byte[] landDimensions;
	
	public LandRegisterPacket() 
	{
		super(Type.LANDREGISTER);
	}

	@Override
	public MinestuckPacket generatePacket(Object... dat) 
	{
		for(Object obj : dat)
			data.writeByte((Byte) obj);
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
		if(MinecraftServer.getServer() != null && MinecraftServer.getServer().isServerRunning())
			return;	//Nope, no editing the server's land list

		MinestuckSaveHandler.lands.clear();

		for(byte dimensionId : landDimensions)
		{
			MinestuckSaveHandler.lands.add((byte)dimensionId);
			if(!DimensionManager.isDimensionRegistered(dimensionId))
			{
				Debug.printf("Adding Land dimension with id of %d", dimensionId);
				DimensionManager.registerDimension(dimensionId, Minestuck.landProviderTypeId);
			}
		}
	}

	@Override
	public EnumSet<Side> getSenderSide() {
		return EnumSet.of(Side.SERVER);
	}

}
