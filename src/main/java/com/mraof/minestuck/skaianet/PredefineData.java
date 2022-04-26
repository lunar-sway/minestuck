package com.mraof.minestuck.skaianet;

import com.mraof.minestuck.player.EnumAspect;
import com.mraof.minestuck.player.PlayerIdentifier;
import com.mraof.minestuck.player.Title;
import com.mraof.minestuck.world.lands.LandTypes;
import com.mraof.minestuck.world.lands.terrain.TerrainLandType;
import com.mraof.minestuck.world.lands.title.TitleLandType;
import net.minecraft.command.CommandSource;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public final class PredefineData
{
	public static final String TITLE_ALREADY_SET = "minestuck.predefine.title_already_set";
	public static final String TITLE_ALREADY_USED = "minestuck.predefine.title_already_used";
	public static final String RESETTING_TERRAIN_TYPE = "minestuck.predefine.resetting_terrain_type";
	public static final String GENERATED_TITLE = "minestuck.predefine.generated_title";
	public static final String CHANGED_TITLE = "minestuck.predefine.changed_title";
	public static final String GENERATED_TITLE_LAND = "minestuck.predefine.generated_title_land";
	public static final String CHANGED_TITLE_LAND = "minestuck.predefine.changed_title_land";
	
	private final PlayerIdentifier player;
	private final Session session;
	private boolean lockedToSession;
	private Title title;
	private TerrainLandType terrainLandType;
	private TitleLandType titleLandType;
	
	PredefineData(PlayerIdentifier player, Session session)
	{
		this.player = player;
		this.session = session;
	}
	
	PredefineData read(CompoundNBT nbt)
	{
		lockedToSession = nbt.getBoolean("locked");
		title = Title.tryRead(nbt, "title");
		if(nbt.contains("landTerrain", Constants.NBT.TAG_STRING))
			terrainLandType = LandTypes.TERRAIN_REGISTRY.getValue(ResourceLocation.tryParse(nbt.getString("landTerrain")));
		if(nbt.contains("landTitle", Constants.NBT.TAG_STRING))
			titleLandType = LandTypes.TITLE_REGISTRY.getValue(ResourceLocation.tryParse(nbt.getString("landTitle")));
		
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
			throw new SkaianetException(TITLE_ALREADY_SET, title.asTextComponent());	//TODO when predefining with define, you wouldn't want this exception to get in the way. Fix this.
		if(session.isTitleUsed(title))
			throw new SkaianetException(TITLE_ALREADY_USED, title.asTextComponent());
		else	//TODO Take a look at the title land type and warn if it's not connected to the set land type
			this.title = title;
	}
	
	public void predefineTerrainLand(TerrainLandType landType, CommandSource source) throws SkaianetException
	{
		forceVerifyTitleLand(landType, source);
		
		this.terrainLandType = landType;
	}
	
	public void predefineTitleLand(TitleLandType landType, CommandSource source) throws SkaianetException
	{
		forceVerifyTitle(Collections.singleton(landType), source);
		
		if(terrainLandType != null && !landType.isAspectCompatible(terrainLandType))
		{
			source.sendSuccess(new TranslationTextComponent(RESETTING_TERRAIN_TYPE, terrainLandType.getRegistryName()).withStyle(TextFormatting.YELLOW), true);
			terrainLandType = null;
		}
		this.titleLandType = landType;
	}
	
	private void forceVerifyTitle(Set<TitleLandType> availableTypes, CommandSource source) throws SkaianetException
	{
		Set<EnumAspect> availableAspects = availableTypes.stream().map(TitleLandType::getAspect).filter(Objects::nonNull).collect(Collectors.toSet());
		if (availableAspects.isEmpty())
			availableAspects = EnumAspect.valuesSet();
		
		if(title == null || !availableAspects.contains(title.getHeroAspect()))
		{
			Title previous = title;
			title = Generator.generateTitle(session, availableAspects, player);
			
			if(!availableAspects.contains(title.getHeroAspect()))
			{
				terrainLandType = null; titleLandType = null;
				throw new IllegalStateException("Generated title did not meet requirements!");
			}
			
			if(previous == null)
				source.sendSuccess(new TranslationTextComponent(GENERATED_TITLE, title.asTextComponent()), true);
			else source.sendSuccess(new TranslationTextComponent(CHANGED_TITLE, previous.asTextComponent(), title.asTextComponent()).withStyle(TextFormatting.YELLOW), true);
		}
	}
	
	private void forceVerifyTitleLand(TerrainLandType type, CommandSource source) throws SkaianetException
	{
		if(titleLandType == null || !titleLandType.isAspectCompatible(type))
		{
			Set<TitleLandType> availableTypes = LandTypes.getCompatibleTitleTypes(type);
			forceVerifyTitle(availableTypes, source);
			
			//title should be assumed to be non-null after this point
			availableTypes.removeIf(landType -> landType.getAspect() != title.getHeroAspect());
			if(availableTypes.isEmpty())
			{
				terrainLandType = null; titleLandType = null;
				throw new IllegalStateException("Had no title land types to generate when some were expected.");
			}
			
			TitleLandType previous = titleLandType;
			titleLandType = Generator.generateWeightedTitleLandType(session, title.getHeroAspect(), type, player);
			
			if(!titleLandType.isAspectCompatible(type))
			{
				terrainLandType = null;
				throw new IllegalStateException("Generated title land type did not meet requirements!");
			}
			
			if(previous == null)
				source.sendSuccess(new TranslationTextComponent(GENERATED_TITLE_LAND, titleLandType.getRegistryName()), true);
			else source.sendSuccess(new TranslationTextComponent(CHANGED_TITLE_LAND, previous.getRegistryName(), titleLandType.getRegistryName()).withStyle(TextFormatting.YELLOW), true);
		}
	}
	
	public boolean isLockedToSession()
	{
		return lockedToSession;
	}
	
	public PlayerIdentifier getPlayer()
	{
		return player;
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