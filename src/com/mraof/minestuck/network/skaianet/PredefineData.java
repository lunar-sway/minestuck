package com.mraof.minestuck.network.skaianet;

import net.minecraft.nbt.NBTTagCompound;

import com.mraof.minestuck.util.EnumAspect;
import com.mraof.minestuck.util.EnumClass;
import com.mraof.minestuck.world.lands.LandAspectRegistry;
import com.mraof.minestuck.world.lands.terrain.TerrainLandAspect;
import com.mraof.minestuck.world.lands.title.TitleLandAspect;

public class PredefineData
{
	
	EnumAspect titleAspect;
	EnumClass titleClass;
	TerrainLandAspect landTerrain;
	TitleLandAspect landTitle;
	
	PredefineData read(NBTTagCompound nbt)
	{
		if(nbt.hasKey("titleAspect", 99))
			titleAspect = EnumAspect.values()[nbt.getByte("titleAspect")];
		if(nbt.hasKey("titleClass", 99))
			titleClass = EnumClass.values()[nbt.getByte("titleClass")];
		if(nbt.hasKey("landTerrain", 8))
			landTerrain = LandAspectRegistry.fromNameTerrain(nbt.getString("landTerrain"));
		if(nbt.hasKey("landTitle", 8))
			landTitle = LandAspectRegistry.fromNameTitle(nbt.getString("landTitle"));
		
		return this;
	}
	
	NBTTagCompound write()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		if(titleAspect != null)
			nbt.setByte("titleAspect", (byte) titleAspect.ordinal());
		if(titleClass != null)
			nbt.setByte("titleClass", (byte) titleClass.ordinal());
		if(landTerrain != null)
			nbt.setString("landTerrain", landTerrain.getPrimaryName());
		if(landTitle != null)
			nbt.setString("landTitle", landTitle.getPrimaryName());
		
		return nbt;
	}
}