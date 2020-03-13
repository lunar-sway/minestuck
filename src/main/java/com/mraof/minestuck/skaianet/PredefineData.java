package com.mraof.minestuck.skaianet;

import com.mraof.minestuck.player.Title;
import com.mraof.minestuck.world.lands.LandTypes;
import com.mraof.minestuck.world.lands.terrain.TerrainLandType;
import com.mraof.minestuck.world.lands.title.TitleLandType;
import net.minecraft.command.CommandSource;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;

public final class PredefineData
{
	public static String TITLE_ALREADY_SET = "minestuck.predefine.title_already_set";
	public static String TITLE_ALREADY_USED = "minestuck.predefine.title_already_used";
	public static String RESETTING_TERRAIN_TYPE = "minestuck.predefine.resetting_terrain_type";
	public static String INCOMPATIBLE_LAND = "minestuck.predefine.incompatible_land";
	public static String INVALID_LAND_ORDER = "minestuck.predefine.invalid_land_order";
	
	private final Session session;
	private boolean lockedToSession;
	private Title title;
	private TerrainLandType terrainLandType;
	private TitleLandType titleLandType;
	
	PredefineData(Session session)
	{
		this.session = session;
	}
	
	PredefineData read(CompoundNBT nbt)
	{
		lockedToSession = nbt.getBoolean("locked");
		title = Title.tryRead(nbt, "title");
		if(nbt.contains("landTerrain", Constants.NBT.TAG_STRING))
			terrainLandType = LandTypes.TERRAIN_REGISTRY.getValue(ResourceLocation.tryCreate(nbt.getString("landTerrain")));
		if(nbt.contains("landTitle", Constants.NBT.TAG_STRING))
			titleLandType = LandTypes.TITLE_REGISTRY.getValue(ResourceLocation.tryCreate(nbt.getString("landTitle")));
		
		return this;
	}
	
	CompoundNBT write()
	{
		CompoundNBT nbt = new CompoundNBT();
		nbt.putBoolean("locked", lockedToSession);
		if(title != null)
			title.write(nbt, "title");
		if(terrainLandType != null)
			nbt.putString("landTerrain", terrainLandType.getRegistryName().toString());
		if(titleLandType != null)
			nbt.putString("landTitle", titleLandType.getRegistryName().toString());
		
		return nbt;
	}
	
	public void predefineTitle(@Nonnull Title title, CommandSource source) throws SkaianetException
	{
		if(title.equals(this.title))
			throw new SkaianetException(TITLE_ALREADY_SET, title.asTextComponent());
		if(session.isTitleUsed(title))
			throw new SkaianetException(TITLE_ALREADY_USED, title.asTextComponent());
		else	//TODO Take a look at the title land type and warn if it's not connected to the set land type
			this.title = title;
	}
	
	public void predefineTerrainLand(TerrainLandType landType, CommandSource source) throws SkaianetException
	{
		if(titleLandType == null)
			throw new SkaianetException(INVALID_LAND_ORDER);
		else if(!titleLandType.isAspectCompatible(landType))
			throw new SkaianetException(INCOMPATIBLE_LAND, titleLandType.getRegistryName());
		else
			this.terrainLandType = landType;
	}
	
	public void predefineTitleLand(TitleLandType landType, CommandSource source) throws SkaianetException
	{
		if(terrainLandType != null && !landType.isAspectCompatible(terrainLandType))
		{
			source.sendFeedback(new TranslationTextComponent(RESETTING_TERRAIN_TYPE, terrainLandType.getRegistryName()).setStyle(new Style().setColor(TextFormatting.YELLOW)), true);
			terrainLandType = null;
		}
		this.titleLandType = landType;
	}
	
	public boolean isLockedToSession()
	{
		return lockedToSession;
	}
	
	public Title getTitle()
	{
		return title;
	}
	
	public TerrainLandType getTerrainLandType()
	{
		return terrainLandType;
	}
	
	public TitleLandType getTitleLandType()
	{
		return titleLandType;
	}
}