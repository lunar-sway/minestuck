package com.mraof.minestuck.world.storage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.world.WorldEvent;

import com.mraof.minestuck.network.skaianet.SkaianetHandler;
import com.mraof.minestuck.util.MinestuckPlayerData;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class MinestuckSaveHandler 
{
	public static List<Byte> lands = Collections.synchronizedList(new ArrayList<Byte>());
	@SubscribeEvent
	public void onWorldSave(WorldEvent.Save event)
	{
		if(event.world.provider.dimensionId != 0)	//Only save one time each world-save instead of one per dimension each world-save.
			return;
		
		File dataFile = event.world.getSaveHandler().getMapFileFromName("MinestuckData");
		if (dataFile != null) {
			NBTTagCompound nbt = new NBTTagCompound();
			byte[] landArray = new byte[lands.size()];
			for(int i = 0; i < lands.size(); i++)
				landArray[i] = lands.get(i);
			nbt.setByteArray("landList", landArray);
			
			SkaianetHandler.saveData(nbt);
			
			MinestuckPlayerData.writeToNBT(nbt);
			
			try {
				CompressedStreamTools.writeCompressed(nbt, new FileOutputStream(dataFile));
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
		
		String[] oldFiles = {"gristCache", "minestuckLandList", "connectionList"};
		for(String s : oldFiles) {
			dataFile = event.world.getSaveHandler().getMapFileFromName("gristCache");
			if(dataFile != null && dataFile.exists())
				dataFile.delete();
		}
		
	}
}
