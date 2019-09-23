package com.mraof.minestuck.network.skaianet;

import com.mraof.minestuck.util.EnumAspect;
import com.mraof.minestuck.util.EnumClass;
import com.mraof.minestuck.util.Title;
import com.mraof.minestuck.world.lands.LandAspectRegistry;
import com.mraof.minestuck.world.lands.terrain.TerrainLandAspect;
import com.mraof.minestuck.world.lands.title.TitleLandAspect;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;

class PredefineData
{
	Title title;
	TerrainLandAspect landTerrain;
	TitleLandAspect landTitle;
	
	PredefineData read(CompoundNBT nbt)
	{
		if(nbt.contains("titleAspect", 99))
			title = new Title(EnumClass.values()[nbt.getByte("titleClass")], EnumAspect.values()[nbt.getByte("titleAspect")]);
		if(nbt.contains("landTerrain", 8))
			landTerrain = LandAspectRegistry.TERRAIN_REGISTRY.getValue(ResourceLocation.tryCreate(nbt.getString("landTerrain")));
		if(nbt.contains("landTitle", 8))
			landTitle = LandAspectRegistry.TITLE_REGISTRY.getValue(ResourceLocation.tryCreate(nbt.getString("landTitle")));
		
		return this;
	}
	
	CompoundNBT write()
	{
		CompoundNBT nbt = new CompoundNBT();
		if(title != null)
		{
			nbt.putByte("titleClass", (byte) title.getHeroClass().ordinal());
			nbt.putByte("titleAspect", (byte) title.getHeroAspect().ordinal());
		}
		if(landTerrain != null)
			nbt.putString("landTerrain", landTerrain.getRegistryName().toString());
		if(landTitle != null)
			nbt.putString("landTitle", landTitle.getRegistryName().toString());
		
		return nbt;
	}
}