package com.mraof.minestuck.world.storage;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.world.WorldEvent;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.network.skaianet.SkaianetHandler;

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
			SkaianetHandler.saveData(event.world.getSaveHandler().getMapFileFromName("connectionList"),event.world.getSaveHandler().getMapFileFromName("waitingConnections"));
		}
	}
	@ForgeSubscribe
	public void postStitch(TextureStitchEvent.Post event)
	{
	    Minestuck.fluidOil.setIcons(Minestuck.blockOil.getBlockTextureFromSide(0), Minestuck.blockOil.getBlockTextureFromSide(1));
	}
}
