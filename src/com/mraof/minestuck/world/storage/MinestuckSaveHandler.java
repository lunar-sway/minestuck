package com.mraof.minestuck.world.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.mraof.minestuck.event.ServerEventHandler;
import com.mraof.minestuck.network.skaianet.SkaianetHandler;
import com.mraof.minestuck.util.MinestuckPlayerData;
import com.mraof.minestuck.util.PostEntryTask;
import com.mraof.minestuck.world.MinestuckDimensionHandler;
import com.mraof.minestuck.tileentity.TileEntityTransportalizer;

public class MinestuckSaveHandler 
{
	
	@SubscribeEvent
	public void onWorldSave(WorldEvent.Save event)
	{
		if(event.world.provider.getDimensionId() != 0)	//Only save one time each world-save instead of one per dimension each world-save.
			return;

		File dataFile = event.world.getSaveHandler().getMapFileFromName("MinestuckData");
		if (dataFile != null)
		{
			NBTTagCompound nbt = new NBTTagCompound();
			
			MinestuckDimensionHandler.saveData(nbt);
			TileEntityTransportalizer.saveTransportalizers(nbt);
			SkaianetHandler.saveData(nbt);
			MinestuckPlayerData.writeToNBT(nbt);
			
			
			NBTTagList list = new NBTTagList();
			for(PostEntryTask task : ServerEventHandler.tickTasks)
				list.appendTag(task.toNBTTagCompound());
			if(list.tagCount() > 0)
				nbt.setTag("tickTasks", list);
			
			try {
				CompressedStreamTools.writeCompressed(nbt, new FileOutputStream(dataFile));
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load event)
	{
		if(event.world.provider.getDimensionId() != 0 || event.world.isRemote)
			return;
		ISaveHandler saveHandler = event.world.getSaveHandler();
		File dataFile = saveHandler.getMapFileFromName("MinestuckData");
		if(dataFile != null && dataFile.exists())
		{
			NBTTagCompound nbt = null;
			try
			{
				nbt = CompressedStreamTools.readCompressed(new FileInputStream(dataFile));
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
			if(nbt != null)
			{
				MinestuckDimensionHandler.loadData(nbt);
				SkaianetHandler.loadData(nbt.getCompoundTag("skaianet"));
				MinestuckPlayerData.readFromNBT(nbt);
				TileEntityTransportalizer.loadTransportalizers(nbt.getCompoundTag("transportalizers"));
				
				ServerEventHandler.tickTasks.clear();
				if(nbt.hasKey("tickTasks", 9))
				{
					NBTTagList list = nbt.getTagList("tickTasks", 10);
					for(int i = 0; i < list.tagCount(); i++)
						ServerEventHandler.tickTasks.add(new PostEntryTask(list.getCompoundTagAt(i)));
				}
				
				return;
			}
		}
		
		ServerEventHandler.tickTasks.clear();
		SkaianetHandler.loadData(null);
		MinestuckPlayerData.readFromNBT(null);
	}
	
}
