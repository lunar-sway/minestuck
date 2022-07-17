package com.mraof.minestuck;

/**
 * This class loads and saves any information that should be loaded and ready at the start of world load,
 * such as land aspect information that is stored with skaianet data.
 */
/*TODO
public class MSWorldPersistenceHook implements WorldPersistenceHooks.WorldPersistenceHook
{
	@Override
	public String getModId()
	{
		return Minestuck.MOD_ID;
	}
	
	@Override
	public CompoundTag getDataForWriting(SaveFormat.LevelSave levelSave, IServerConfiguration serverInfo)
	{
		CompoundTag data = new CompoundTag();
		CompoundTag skaianetData = SkaianetHandler.write();
		if(skaianetData != null)
			data.put("skaianet", skaianetData);
		return data;
	}
	
	@Override
	public void readData(SaveFormat.LevelSave levelSave, IServerConfiguration serverInfo, CompoundTag tag)
	{
		if(tag.contains("skaianet", Tag.TAG_COMPOUND))
			SkaianetHandler.init(tag.getCompound("skaianet"));
	}
}
*/