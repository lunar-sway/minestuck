package com.mraof.minestuck.network.skaianet;

import com.mraof.minestuck.util.EnumAspect;
import com.mraof.minestuck.util.EnumClass;
import com.mraof.minestuck.util.Title;
import com.mraof.minestuck.world.lands.LandAspectRegistry;
import com.mraof.minestuck.world.lands.terrain.TerrainLandAspect;
import com.mraof.minestuck.world.lands.title.TitleLandAspect;
import net.minecraft.nbt.NBTTagCompound;

class PredefineData
{
	Title title;
	TerrainLandAspect landTerrain;
	TitleLandAspect landTitle;
	
	PredefineData read(NBTTagCompound nbt)
	{
		if(nbt.hasKey("titleAspect", 99))
			title = new Title(EnumClass.values()[nbt.getByte("titleClass")], EnumAspect.values()[nbt.getByte("titleAspect")]);
		if(nbt.hasKey("landTerrain", 8))
			landTerrain = LandAspectRegistry.fromNameTerrain(nbt.getString("landTerrain"));
		if(nbt.hasKey("landTitle", 8))
			landTitle = LandAspectRegistry.fromNameTitle(nbt.getString("landTitle"));
		
		return this;
	}
	
	NBTTagCompound write()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		if(title != null)
		{
			nbt.setByte("titleClass", (byte) title.getHeroClass().ordinal());
			nbt.setByte("titleAspect", (byte) title.getHeroAspect().ordinal());
		}
		if(landTerrain != null)
			nbt.setString("landTerrain", landTerrain.getPrimaryName());
		if(landTitle != null)
			nbt.setString("landTitle", landTitle.getPrimaryName());
		
		return nbt;
	}
}