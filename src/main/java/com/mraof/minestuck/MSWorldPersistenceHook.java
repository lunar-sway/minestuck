package com.mraof.minestuck;

import com.mraof.minestuck.skaianet.SkaianetHandler;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.storage.IServerConfiguration;
import net.minecraft.world.storage.SaveFormat;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.WorldPersistenceHooks;

/**
 * This class loads and saves any information that should be loaded and ready at the start of world load,
 * such as land aspect information that is stored with skaianet data.
 */
public class MSWorldPersistenceHook implements WorldPersistenceHooks.WorldPersistenceHook
{
	@Override
	public String getModId()
	{
		return Minestuck.MOD_ID;
	}
	
	@Override
	public CompoundNBT getDataForWriting(SaveFormat.LevelSave levelSave, IServerConfiguration serverInfo)
	{
		CompoundNBT data = new CompoundNBT();
		CompoundNBT skaianetData = SkaianetHandler.write();
		if(skaianetData != null)
			data.put("skaianet", skaianetData);
		return data;
	}
	
	@Override
	public void readData(SaveFormat.LevelSave levelSave, IServerConfiguration serverInfo, CompoundNBT tag)
	{
		if(tag.contains("skaianet", Constants.NBT.TAG_COMPOUND))
			SkaianetHandler.init(tag.getCompound("skaianet"));
	}
}