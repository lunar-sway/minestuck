package com.mraof.minestuck.network;

import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.relauncher.Side;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.world.MinestuckDimensionHandler;
import com.mraof.minestuck.world.gen.lands.LandAspectRegistry;
import com.mraof.minestuck.world.storage.MinestuckSaveHandler;

public class LandRegisterPacket extends MinestuckPacket
{
	public ArrayList<Byte> ids;
	public ArrayList<LandAspectRegistry.AspectCombination> aspects;
	
	public LandRegisterPacket() 
	{
		super(Type.LANDREGISTER);
	}

	@Override
	public MinestuckPacket generatePacket(Object... dat) 
	{
		for(Map.Entry<Byte, LandAspectRegistry.AspectCombination> entry : MinestuckDimensionHandler.getLandSet())
		{
			this.data.writeByte(entry.getKey());
			writeString(data, entry.getValue().aspect1.getPrimaryName()+"\n");
			writeString(data, entry.getValue().aspect2.getPrimaryName()+"\n");
		}
		return this;
	}

	@Override
	public MinestuckPacket consumePacket(ByteBuf data) 
	{
		ids = new ArrayList<Byte>();
		aspects = new ArrayList<LandAspectRegistry.AspectCombination>();
		while(data.readableBytes() > 0)
		{
			byte dim = data.readByte();
			String aspect1 = readLine(data);
			String aspect2 = readLine(data);
			ids.add(dim);
			aspects.add(new LandAspectRegistry.AspectCombination(LandAspectRegistry.fromName(aspect1), LandAspectRegistry.fromName2(aspect2)));
		}
		
		return this;
	}

	@Override
	public void execute(EntityPlayer player) 
	{
		if(MinecraftServer.getServer() != null && MinecraftServer.getServer().isServerRunning())
			return;	//Nope, no editing the server's land list
		
		MinestuckDimensionHandler.onLandPacket(this);
	}

	@Override
	public EnumSet<Side> getSenderSide() {
		return EnumSet.of(Side.SERVER);
	}

}
