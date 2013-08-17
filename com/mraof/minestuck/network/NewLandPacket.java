package com.mraof.minestuck.network;

import java.util.Hashtable;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.mraof.minestuck.entity.item.EntityGrist;
import com.mraof.minestuck.util.GristType;
import com.mraof.minestuck.util.Title;
import com.mraof.minestuck.world.gen.lands.LandAspect;
import com.mraof.minestuck.world.gen.lands.LandHelper;

import cpw.mods.fml.common.network.Player;

public class NewLandPacket extends MinestuckPacket 
{
	public String aspect1;
	public String aspect2;
	public int dimension;
	public NewLandPacket() 
	{
		super(Type.NEWLAND);
	}

	@Override
	public byte[] generatePacket(Object... data) 
	{
		ByteArrayDataOutput dat = ByteStreams.newDataOutput();
		dat.writeChars((String)data[0]);
		dat.writeChars((String)data[1]);
		dat.writeInt((Integer)data[2]);
		return dat.toByteArray();
	}

	@Override
	public MinestuckPacket consumePacket(byte[] data) 
	{
		ByteArrayDataInput dat = ByteStreams.newDataInput(data);
		aspect1 = dat.readLine();
		aspect2 = dat.readLine();
		dimension = dat.readInt();
		return this;
	}

	@Override
	public void execute(INetworkManager network, MinestuckPacketHandler handler, Player player, String userName)
	{
		World world = (World) DimensionManager.getWorld(dimension);
		LandHelper helper = new LandHelper(world.getSeed());
		LandAspect aspect1 = helper.getLandAspect(new Title(0,0));
		LandAspect aspect2 = helper.getLandAspect(new Title(0,0),aspect1);
		Map<String, NBTBase> dataTag = new Hashtable<String,NBTBase>();
		dataTag.put("LandData",LandHelper.toNBT(aspect1,aspect2));
		world.getWorldInfo().setAdditionalProperties(dataTag);
	}
}
