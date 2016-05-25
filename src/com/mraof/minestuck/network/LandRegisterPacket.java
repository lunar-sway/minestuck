package com.mraof.minestuck.network;

import io.netty.buffer.ByteBuf;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;

import com.mraof.minestuck.world.MinestuckDimensionHandler;
import com.mraof.minestuck.world.lands.LandAspectRegistry;

public class LandRegisterPacket extends MinestuckPacket
{
	public HashMap<Integer, LandAspectRegistry.AspectCombination> aspectMap;
	public HashMap<Integer, BlockPos> spawnMap;
	
	@Override
	public MinestuckPacket generatePacket(Object... dat) 
	{
		for(Map.Entry<Integer, LandAspectRegistry.AspectCombination> entry : MinestuckDimensionHandler.getLandSet())
		{
			this.data.writeInt(entry.getKey());
			writeString(data, entry.getValue().aspectTerrain.getPrimaryName()+"\n");
			writeString(data, entry.getValue().aspectTitle.getPrimaryName()+"\n");
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
		aspectMap = new HashMap<Integer, LandAspectRegistry.AspectCombination>();
		spawnMap = new HashMap<Integer, BlockPos>();
		while(data.readableBytes() > 0)
		{
			int dim = data.readInt();
			String aspect1 = readLine(data);
			String aspect2 = readLine(data);
			BlockPos spawn = new BlockPos(data.readInt(), data.readInt(), data.readInt());
			aspectMap.put(dim, new LandAspectRegistry.AspectCombination(LandAspectRegistry.fromNameTerrain(aspect1), LandAspectRegistry.fromNameTitle(aspect2)));
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
