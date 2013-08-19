package com.mraof.minestuck.world.storage;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.world.WorldEvent;

public class MinestuckSaveHandler 
{
	@ForgeSubscribe
	public void onWorldSave(WorldEvent.Save event)
	{
		File landList = event.world.getSaveHandler().getMapFileFromName("minestuckLandList");
		if (landList != null)
        {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
//            Iterator iterator = this.idCounts.keySet().iterator();

//            while (iterator.hasNext())
//            {
//                String s1 = (String)iterator.next();
//                short short1 = ((Short)this.idCounts.get(s1)).shortValue();
//                nbttagcompound.setShort(s1, short1);
//            }

            try {
            	DataOutputStream dataoutputstream = new DataOutputStream(new FileOutputStream(landList));
            	CompressedStreamTools.write(nbttagcompound, dataoutputstream);
				dataoutputstream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
	}
}
