package com.mraof.minestuck.network;

import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.relauncher.Side;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.world.MinestuckDimensionHandler;
import com.mraof.minestuck.world.gen.lands.LandAspectRegistry;
import com.mraof.minestuck.world.storage.MinestuckSaveHandler;

public class LandRegisterPacket extends MinestuckPacket
{
	public HashMap<Byte, LandAspectRegistry.AspectCombination> aspectMap;
	public HashMap<Byte, BlockPos> spawnMap;
	
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
			BlockPos spawn = MinestuckDimensionHandler.getSpawn(entry.getKey());
			data.writeInt(spawn.getX());
			data.writeInt(spawn.getY());
			data.writeInt(spawn.getZ());
		}
		return this;
	}

	@Override
	public MinestuckPacket consumePacket(ByteBuf data) 
	{
		aspectMap = new HashMap<Byte, LandAspectRegistry.AspectCombination>();
		spawnMap = new HashMap<Byte, BlockPos>();
		while(data.readableBytes() > 0)
		{
			byte dim = data.readByte();
			String aspect1 = readLine(data);
			String aspect2 = readLine(data);
			BlockPos spawn = new BlockPos(data.readInt(), data.readInt(), data.readInt());
			aspectMap.put(dim, new LandAspectRegistry.AspectCombination(LandAspectRegistry.fromName(aspect1), LandAspectRegistry.fromName2(aspect2)));
			spawnMap.put(dim, spawn);
		}
		
		return this;
	}
	
	@Override
	public void execute(EntityPlayer player) 
	{
		MinestuckDimensionHandler.onLandPacket(this);
	}
	
	@Override
	public EnumSet<Side> getSenderSide() {
		return EnumSet.of(Side.SERVER);
	}

}
