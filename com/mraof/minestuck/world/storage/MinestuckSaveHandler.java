package com.mraof.minestuck.world.storage;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.world.WorldEvent;

import com.mraof.minestuck.network.skaianet.SkaianetHandler;
import com.mraof.minestuck.util.GristStorage;

public class MinestuckSaveHandler 
{
	public static List<Byte> lands = Collections.synchronizedList(new ArrayList<Byte>());
	@ForgeSubscribe
	public void onWorldSave(WorldEvent.Save event)
	{
		if(event.world.provider.dimensionId != 0)	//Only save one time each world-save instead of one per dimension each world-save.
			return;
		
		File landList = event.world.getSaveHandler().getMapFileFromName("minestuckLandList");
		if (landList != null)
		{
			try 
			{
				DataOutputStream dataoutputstream = new DataOutputStream(new FileOutputStream(landList));
				for(byte landId : lands)
				{
					dataoutputstream.writeByte(landId);
				}
				dataoutputstream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		SkaianetHandler.saveData(event.world.getSaveHandler().getMapFileFromName("connectionList"));
		
		File gristcache = event.world.getSaveHandler().getMapFileFromName("gristCache");
		if(gristcache != null) {
			try{
				NBTTagCompound nbt = new NBTTagCompound();
				GristStorage.writeToNBT(nbt);
				CompressedStreamTools.writeCompressed(nbt, new FileOutputStream(gristcache));
			} catch(IOException e){
				e.printStackTrace();
			}
		}
	}
}
