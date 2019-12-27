package com.mraof.minestuck.world.storage;

import com.mraof.minestuck.Minestuck;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.storage.WorldSavedData;

public class MinestuckSaveHandler extends WorldSavedData    //TODO https://mcforge.readthedocs.io/en/1.13.x/datastorage/worldsaveddata/
{
	public static final String DATA_NAME = Minestuck.MOD_ID + "_data";
	
	public MinestuckSaveHandler()
	{
		super(DATA_NAME);
	}
	
	@Override
	public void read(CompoundNBT nbt)
	{
	
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound)
	{
		return compound;
	}
	
	/*public void onWorldSave(WorldEvent.Save event)
	{
			NBTTagCompound nbt = new NBTTagCompound();
			
			ServerEditHandler.saveData(nbt);    //Keep this before skaianet
			
			
			NBTTagList list = new NBTTagList();
			for(PostEntryTask task : ServerEventHandler.tickTasks)
				list.appendTag(task.toNBTTagCompound());
			if(list.tagCount() > 0)
				nbt.setTag("tickTasks", list);
			
		
	}
	public void onWorldLoad(WorldEvent.Load event)
	{
				ServerEditHandler.loadData(nbt);
				
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
