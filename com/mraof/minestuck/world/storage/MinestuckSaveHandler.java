package com.mraof.minestuck.world.storage;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.mraof.minestuck.skaianet.SburbConnection;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.world.WorldEvent;

public class MinestuckSaveHandler 
{
	public static List<Byte> lands = Collections.synchronizedList(new ArrayList<Byte>());
	@ForgeSubscribe
	public void onWorldSave(WorldEvent.Save event)
	{
		File landList = event.world.getSaveHandler().getMapFileFromName("minestuckLandList");
		if (landList != null)
		{
			NBTTagCompound nbttagcompound = new NBTTagCompound();

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
			SburbConnection.saveData(event.world.getSaveHandler().getMapFileFromName("connectionList"));
		}
	}
}
