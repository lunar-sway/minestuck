package com.mraof.minestuck.world.lands.title;

import com.mraof.minestuck.util.EnumAspect;
import com.mraof.minestuck.world.lands.LandDimension;
import com.mraof.minestuck.world.lands.ILandAspect;
import com.mraof.minestuck.world.lands.structure.IGateStructure;
import com.mraof.minestuck.world.lands.terrain.TerrainLandAspect;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;

public abstract class TitleLandAspect extends ForgeRegistryEntry<TitleLandAspect> implements ILandAspect<TitleLandAspect>
{
	private final TitleLandAspect parent;
	private final EnumAspect aspectType;
	private final boolean pickedAtRandom;
	
	protected TitleLandAspect(TitleLandAspect parent, EnumAspect aspectType)
	{
		this(parent, aspectType, true);
	}
	
	protected TitleLandAspect(TitleLandAspect parent, EnumAspect aspectType, boolean pickedAtRandom)
	{
		if(parent != null && parent.getParent() != null)
			throw new IllegalArgumentException("Provided parent landspect is actually a child!");
		if(parent != null && parent.getType() != aspectType)
			throw new IllegalArgumentException("Should not create a child land aspect to a different aspect type than the parent");
		this.parent = parent;
		this.aspectType = aspectType;
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
	
	@Nullable
	@Override
	public TitleLandAspect getParent()
	{
		return parent;
	}
	
	@Nullable
	public EnumAspect getType()
	{
		return aspectType;
	}
	
	@Override
	public IGateStructure getGateStructure()
	{
		return null;
	}
	
	public void prepareWorldProvider(LandDimension worldProvider)
	{}
}
