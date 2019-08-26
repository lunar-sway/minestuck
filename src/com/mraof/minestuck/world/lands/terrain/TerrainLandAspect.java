package com.mraof.minestuck.world.lands.terrain;

import java.util.List;

import com.mraof.minestuck.entity.consort.ConsortEntity;
import com.mraof.minestuck.world.lands.ILandAspect;
import com.mraof.minestuck.world.lands.decorator.ILandDecorator;
import com.mraof.minestuck.world.lands.structure.GateStructurePillar;
import com.mraof.minestuck.world.lands.structure.IGateStructure;

import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.registries.ForgeRegistryEntry;

public abstract class TerrainLandAspect extends ForgeRegistryEntry<TerrainLandAspect> implements ILandAspect<TerrainLandAspect>
{
	private final ResourceLocation groupName;
	private final boolean pickedAtRandom;
	
	protected TerrainLandAspect()
	{
		this(null, true);
	}
	
	protected TerrainLandAspect(boolean pickedAtRandom)
	{
		this(null, pickedAtRandom);
	}
	
	protected TerrainLandAspect(ResourceLocation groupName)
	{
		this(groupName, true);
	}
	
	protected TerrainLandAspect(ResourceLocation groupName, boolean pickedAtRandom)
	{
		this.groupName = groupName;
		this.pickedAtRandom = pickedAtRandom;
	}
	
	@Deprecated
	public List<ILandDecorator> getDecorators(){return null;}
	
	public float getSkylightBase()
	{
		return 1F;
	}
	
	public abstract Vec3d getFogColor();
	
	public Vec3d getCloudColor()
	{
		return getFogColor();
	}
	
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
	
	@Override
	public IGateStructure getGateStructure()
	{
		return new GateStructurePillar();
	}
	
	public abstract EntityType<? extends ConsortEntity> getConsortType();
	
}