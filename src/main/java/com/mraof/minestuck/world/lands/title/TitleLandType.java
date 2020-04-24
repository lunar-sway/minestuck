package com.mraof.minestuck.world.lands.title;

import com.mraof.minestuck.player.EnumAspect;
import com.mraof.minestuck.world.lands.ILandType;
import com.mraof.minestuck.world.lands.terrain.TerrainLandType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;

public abstract class TitleLandType extends ForgeRegistryEntry<TitleLandType> implements ILandType<TitleLandType>
{
	private final ResourceLocation groupName;
	private final EnumAspect aspectType;
	private final boolean pickedAtRandom;
	
	protected TitleLandType(EnumAspect aspectType)
	{
		this(aspectType, null, true);
	}
	
	protected TitleLandType(EnumAspect aspectType, boolean pickedAtRandom)
	{
		this(aspectType, null, pickedAtRandom);
	}
	
	protected TitleLandType(EnumAspect aspectType, ResourceLocation groupName)
	{
		this(aspectType, groupName, true);
	}
	
	protected TitleLandType(EnumAspect aspectType, ResourceLocation groupName, boolean pickedAtRandom)
	{
		this.aspectType = aspectType;
		this.groupName = groupName;
		this.pickedAtRandom = pickedAtRandom;
	}
	
	public boolean isAspectCompatible(TerrainLandType aspect)
	{
		return true;
	}
	
	@Override
	public boolean canBePickedAtRandom()
	{
		return pickedAtRandom;
	}
	
	@Override
	public ResourceLocation getGroup()
	{
		if(groupName == null)
			return this.getRegistryName();
		else return groupName;
	}
	
	@Nullable
	public EnumAspect getAspect()
	{
		return aspectType;
	}
}
