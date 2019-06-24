package com.mraof.minestuck.world.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.mraof.minestuck.Minestuck;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.event.world.WorldEvent;

import com.mraof.minestuck.editmode.ServerEditHandler;
import com.mraof.minestuck.event.ServerEventHandler;
import com.mraof.minestuck.network.skaianet.SkaianetHandler;
import com.mraof.minestuck.util.MinestuckPlayerData;
import com.mraof.minestuck.util.PostEntryTask;
import com.mraof.minestuck.world.MinestuckDimensionHandler;
import com.mraof.minestuck.tileentity.TileEntityTransportalizer;

public class MinestuckSaveHandler extends WorldSavedData    //TODO https://mcforge.readthedocs.io/en/1.13.x/datastorage/worldsaveddata/
{
	public static final String DATA_NAME = Minestuck.MOD_ID + "_data";
	
	public MinestuckSaveHandler()
	{
		super(DATA_NAME);
	}
	
	@Override
	public void read(NBTTagCompound nbt)
	{
	
	}
	
	@Override
	public NBTTagCompound write(NBTTagCompound compound)
	{
		return compound;
	}
	
	/*public void onWorldSave(WorldEvent.Save event)
	{
			NBTTagCompound nbt = new NBTTagCompound();
			
			ServerEditHandler.saveData(nbt);    //Keep this before skaianet
			MinestuckDimensionHandler.saveData(nbt);
			SkaianetHandler.saveData(nbt);
			MinestuckPlayerData.writeToNBT(nbt);
			
			
			NBTTagList list = new NBTTagList();
			for(PostEntryTask task : ServerEventHandler.tickTasks)
				list.appendTag(task.toNBTTagCompound());
			if(list.tagCount() > 0)
				nbt.setTag("tickTasks", list);
			
		
	}
	public void onWorldLoad(WorldEvent.Load event)
	{
				ServerEditHandler.loadData(nbt);
				MinestuckDimensionHandler.loadData(nbt);
				SkaianetHandler.loadData(nbt.getCompoundTag("skaianet"));
				MinestuckPlayerData.readFromNBT(nbt);
				
				ServerEventHandler.tickTasks.clear();
				if(nbt.hasKey("tickTasks", 9))
				{
					NBTTagList list = nbt.getTagList("tickTasks", 10);
					for(int i = 0; i < list.tagCount(); i++)
						ServerEventHandler.tickTasks.add(new PostEntryTask(list.getCompoundTagAt(i)));
				}
				
				return;
		
		ServerEventHandler.tickTasks.clear();
		MinestuckDimensionHandler.loadData(null);
		SkaianetHandler.loadData(null);
		MinestuckPlayerData.readFromNBT(null);
		ServerEditHandler.loadData(null);
	}*/
}
