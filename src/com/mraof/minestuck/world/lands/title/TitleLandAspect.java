package com.mraof.minestuck.world.lands.title;

import com.mraof.minestuck.util.EnumAspect;
import com.mraof.minestuck.world.LandDimension;
import com.mraof.minestuck.world.lands.ILandAspect;
import com.mraof.minestuck.world.lands.terrain.TerrainLandAspect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;

public abstract class TitleLandAspect extends ForgeRegistryEntry<TitleLandAspect> implements ILandAspect<TitleLandAspect>
{
	private final ResourceLocation groupName;
	private final EnumAspect aspectType;
	private final boolean pickedAtRandom;
	
	protected TitleLandAspect(EnumAspect aspectType)
	{
		this(aspectType, null, true);
	}
	
	protected TitleLandAspect(EnumAspect aspectType, boolean pickedAtRandom)
	{
		this(aspectType, null, pickedAtRandom);
	}
	
	protected TitleLandAspect(EnumAspect aspectType, ResourceLocation groupName)
	{
		this(aspectType, groupName, true);
	}
	
	protected TitleLandAspect(EnumAspect aspectType, ResourceLocation groupName, boolean pickedAtRandom)
	{
		this.aspectType = aspectType;
		this.groupName = groupName;
		this.pickedAtRandom = pickedAtRandom;
	}
	
	public boolean isAspectCompatible(TerrainLandAspect aspect)
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
	public EnumAspect getType()
	{
		return aspectType;
	}
	
	public void prepareWorldProvider(LandDimension worldProvider)
	{}
}
