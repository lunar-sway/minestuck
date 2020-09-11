package com.mraof.minestuck.world.lands.terrain;

import com.mraof.minestuck.entity.consort.ConsortEntity;
import com.mraof.minestuck.world.lands.ILandType;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.registries.ForgeRegistryEntry;

public abstract class TerrainLandType extends ForgeRegistryEntry<TerrainLandType> implements ILandType<TerrainLandType>
{
	private final ResourceLocation groupName;
	private final boolean pickedAtRandom;
	
	protected TerrainLandType()
	{
		this(null, true);
	}
	
	protected TerrainLandType(boolean pickedAtRandom)
	{
		this(null, pickedAtRandom);
	}
	
	protected TerrainLandType(ResourceLocation groupName)
	{
		this(groupName, true);
	}
	
	protected TerrainLandType(ResourceLocation groupName, boolean pickedAtRandom)
	{
		this.groupName = groupName;
		this.pickedAtRandom = pickedAtRandom;
	}
	
	public float getSkylightBase()
	{
		return 1F;
	}
	
	public abstract Vec3d getFogColor();
	
	public Vec3d getSkyColor()
	{
		return new Vec3d(0, 0, 0);
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
	
	public abstract EntityType<? extends ConsortEntity> getConsortType();
	
}