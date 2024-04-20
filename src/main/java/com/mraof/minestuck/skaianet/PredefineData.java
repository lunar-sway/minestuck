package com.mraof.minestuck.skaianet;

import com.mraof.minestuck.player.EnumAspect;
import com.mraof.minestuck.player.PlayerIdentifier;
import com.mraof.minestuck.player.Title;
import com.mraof.minestuck.world.lands.LandTypes;
import com.mraof.minestuck.world.lands.gen.LandTypeSelection;
import com.mraof.minestuck.world.lands.terrain.TerrainLandType;
import com.mraof.minestuck.world.lands.title.TitleLandType;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public final class PredefineData
{
	public static final String TITLE_ALREADY_SET = "minestuck.predefine.title_already_set";
	public static final String RESETTING_TERRAIN_TYPE = "minestuck.predefine.resetting_terrain_type";
	public static final String GENERATED_TITLE = "minestuck.predefine.generated_title";
	public static final String CHANGED_TITLE = "minestuck.predefine.changed_title";
	public static final String GENERATED_TITLE_LAND = "minestuck.predefine.generated_title_land";
	public static final String CHANGED_TITLE_LAND = "minestuck.predefine.changed_title_land";
	
	private final PlayerIdentifier player;
	private Title title;
	private TerrainLandType terrainLandType;
	private TitleLandType titleLandType;
	
	PredefineData(PlayerIdentifier player)
	{
		this.player = player;
	}
	
	PredefineData read(CompoundTag nbt)
	{
		title = Title.tryRead(nbt, "title");
		if(nbt.contains("landTerrain", Tag.TAG_STRING))
			terrainLandType = LandTypes.TERRAIN_REGISTRY.get(ResourceLocation.tryParse(nbt.getString("landTerrain")));
		if(nbt.contains("landTitle", Tag.TAG_STRING))
			titleLandType = LandTypes.TITLE_REGISTRY.get(ResourceLocation.tryParse(nbt.getString("landTitle")));
		
		return this;
	}
	
	CompoundTag write()
	{
		CompoundTag nbt = new CompoundTag();
		if(title != null)
			title.write(nbt, "title");
		if(terrainLandType != null)
			nbt.putString("landTerrain", LandTypes.TERRAIN_REGISTRY.getKey(terrainLandType).toString());
		if(titleLandType != null)
			nbt.putString("landTitle", LandTypes.TITLE_REGISTRY.getKey(titleLandType).toString());
		
		return nbt;
	}
	
	public void predefineTitle(@Nonnull Title title)
	{
		//TODO Take a look at the title land type and warn if it's not connected to the set land type
		this.title = title;
	}
	
	public void predefineTerrainLand(TerrainLandType landType, CommandSourceStack source)
	{
		forceVerifyTitleLand(landType, source);
		
		this.terrainLandType = landType;
	}
	
	public void predefineTitleLand(TitleLandType landType, CommandSourceStack source)
	{
		forceVerifyTitle(Collections.singleton(landType), source);
		
		if(terrainLandType != null && !landType.isAspectCompatible(terrainLandType))
		{
			source.sendSuccess(() -> Component.translatable(RESETTING_TERRAIN_TYPE, LandTypes.TERRAIN_REGISTRY.getKey(terrainLandType)).withStyle(ChatFormatting.YELLOW), true);
			terrainLandType = null;
		}
		this.titleLandType = landType;
	}
	
	private void forceVerifyTitle(Set<TitleLandType> availableTypes, CommandSourceStack source)
	{
		Set<EnumAspect> availableAspects = availableTypes.stream()
				.flatMap(titleType -> LandTypeSelection.compatibleAspects(titleType).stream())
				.collect(Collectors.toSet());
		if(title == null || !availableAspects.contains(title.heroAspect()))
		{
			Title previous = title;
			title = Generator.generateTitle(player, availableAspects, SkaianetData.get(source.getServer()));
			
			if(!availableAspects.contains(title.heroAspect()))
			{
				terrainLandType = null; titleLandType = null;
				throw new IllegalStateException("Generated title did not meet requirements!");
			}
			
			if(previous == null)
				source.sendSuccess(() -> Component.translatable(GENERATED_TITLE, title.asTextComponent()), true);
			else source.sendSuccess(() -> Component.translatable(CHANGED_TITLE, previous.asTextComponent(), title.asTextComponent()).withStyle(ChatFormatting.YELLOW), true);
		}
	}
	
	private void forceVerifyTitleLand(TerrainLandType type, CommandSourceStack source)
	{
		if(titleLandType == null || !titleLandType.isAspectCompatible(type))
		{
			Set<TitleLandType> availableTypes = LandTypeSelection.compatibleTitleTypes(type);
			forceVerifyTitle(availableTypes, source);
			
			//title should be assumed to be non-null after this point
			availableTypes = LandTypeSelection.compatibleTitleTypes(type, title.heroAspect());
			if(availableTypes.isEmpty())
			{
				terrainLandType = null; titleLandType = null;
				throw new IllegalStateException("Had no title land types to generate when some were expected.");
			}
			
			SkaianetData skaianetData = SkaianetData.get(source.getServer());
			TitleLandType previous = titleLandType;
			List<PlayerIdentifier> otherPlayers = skaianetData.sessionHandler.playersToCheckForDataSelection(player).toList();
			titleLandType = Generator.generateWeightedTitleLandType(otherPlayers, title.heroAspect(), type, skaianetData);
			
			if(!titleLandType.isAspectCompatible(type))
			{
				terrainLandType = null;
				throw new IllegalStateException("Generated title land type did not meet requirements!");
			}
			
			if(previous == null)
				source.sendSuccess(() -> Component.translatable(GENERATED_TITLE_LAND, LandTypes.TITLE_REGISTRY.getKey(titleLandType)), true);
			else source.sendSuccess(() -> Component.translatable(CHANGED_TITLE_LAND, LandTypes.TITLE_REGISTRY.getKey(previous), LandTypes.TITLE_REGISTRY.getKey(titleLandType)).withStyle(ChatFormatting.YELLOW), true);
		}
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