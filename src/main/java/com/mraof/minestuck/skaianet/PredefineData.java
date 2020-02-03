package com.mraof.minestuck.skaianet;

import com.mraof.minestuck.player.Title;
import com.mraof.minestuck.world.lands.LandTypes;
import com.mraof.minestuck.world.lands.terrain.TerrainLandType;
import com.mraof.minestuck.world.lands.title.TitleLandType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;

class PredefineData
{
	Title title;
	TerrainLandType landTerrain;
	TitleLandType landTitle;
	
	PredefineData read(CompoundNBT nbt)
	{
		title = Title.tryRead(nbt, "title");
		if(nbt.contains("landTerrain", Constants.NBT.TAG_STRING))
			landTerrain = LandTypes.TERRAIN_REGISTRY.getValue(ResourceLocation.tryCreate(nbt.getString("landTerrain")));
		if(nbt.contains("landTitle", Constants.NBT.TAG_STRING))
			landTitle = LandTypes.TITLE_REGISTRY.getValue(ResourceLocation.tryCreate(nbt.getString("landTitle")));
		
		return this;
	}
	
	CompoundNBT write()
	{
		CompoundNBT nbt = new CompoundNBT();
		if(title != null)
			title.write(nbt, "title");
		if(landTerrain != null)
			nbt.putString("landTerrain", landTerrain.getRegistryName().toString());
		if(landTitle != null)
			nbt.putString("landTitle", landTitle.getRegistryName().toString());
		
		return nbt;
	}
}