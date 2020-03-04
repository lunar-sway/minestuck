package com.mraof.minestuck.skaianet;

import com.mraof.minestuck.player.Title;
import com.mraof.minestuck.world.lands.LandTypes;
import com.mraof.minestuck.world.lands.terrain.TerrainLandType;
import com.mraof.minestuck.world.lands.title.TitleLandType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;

public final class PredefineData
{
	boolean lockedToSession;
	Title title;
	TerrainLandType landTerrain;
	TitleLandType landTitle;
	
	PredefineData read(CompoundNBT nbt)
	{
		lockedToSession = nbt.getBoolean("locked");
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
		nbt.putBoolean("locked", lockedToSession);
		if(title != null)
			title.write(nbt, "title");
		if(landTerrain != null)
			nbt.putString("landTerrain", landTerrain.getRegistryName().toString());
		if(landTitle != null)
			nbt.putString("landTitle", landTitle.getRegistryName().toString());
		
		return nbt;
	}
	
	public void predefineTitle(Title title) throws SkaianetException
	{
		//TODO Make a call to session that checks for duplicate titles. Also throw exception if title.equals(this.title)
		this.title = title;
	}
	
	public void predefineTerrainLand(TerrainLandType landType) throws SkaianetException
	{
		//TODO Make sure that it is compatible with the title land type
		this.landTerrain = landType;
	}
	
	public void predefineTitleLand(TitleLandType landType) throws SkaianetException
	{
		//TODO Make sure that the potential terrain land type is compatible with this type
		this.landTitle = landType;
	}
}