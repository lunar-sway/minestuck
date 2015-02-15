package com.mraof.minestuck.network;

import io.netty.buffer.ByteBuf;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

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
		Debug.print("Consumed land packet with bytes "+landDimensions.length);
		return this;
	}

	@Override
	public void execute(EntityPlayer player) 
	{
		if(Minestuck.isServerRunning)
			return;	//The dimensions is already registered
		
		for(byte dimensionId : MinestuckSaveHandler.lands)
			if(!containsId(dimensionId) && DimensionManager.isDimensionRegistered(dimensionId))
				DimensionManager.unregisterDimension(dimensionId);
		
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
	
	private boolean containsId(byte id)
	{
		for(byte b : landDimensions)
			if(b == id)
				return true;
		return false;
	}
	
	@Override
	public EnumSet<Side> getSenderSide() {
		return EnumSet.of(Side.SERVER);
	}

}
