package com.mraof.minestuck.world.lands.terrain;

import java.util.List;

import com.mraof.minestuck.entity.consort.ConsortEntity;
import com.mraof.minestuck.world.lands.ILandAspect;
import com.mraof.minestuck.world.lands.decorator.ILandDecorator;
import com.mraof.minestuck.world.lands.structure.GateStructurePillar;
import com.mraof.minestuck.world.lands.structure.IGateStructure;
import com.mraof.minestuck.world.lands.structure.blocks.StructureBlockRegistry;

import net.minecraft.entity.EntityType;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;

public abstract class TerrainLandAspect extends ForgeRegistryEntry<TerrainLandAspect> implements ILandAspect<TerrainLandAspect>
{
	private final TerrainLandAspect parent;
	private final boolean pickedAtRandom;
	
	protected TerrainLandAspect(TerrainLandAspect parent)
	{
		this(parent, true);
	}
	
	protected TerrainLandAspect(TerrainLandAspect parent, boolean pickedAtRandom)
	{
		if(parent != null && parent.getParent() != null)
			throw new IllegalArgumentException("Provided parent landspect is actually a child!");
		this.parent = parent;
		this.pickedAtRandom = pickedAtRandom;
	}

	public abstract void registerBlocks(StructureBlockRegistry registry);
	
	@Deprecated
	public List<ILandDecorator> getDecorators(){return null;}
	
	/**
	 * @return
	 */
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
	
	@Deprecated
	public int getWeatherType()
	{
		return -1;
	}
	
	@Deprecated
	public float getTemperature()
	{
		return 0.7F;
	}
	
	@Override
	public boolean canBePickedAtRandom()
	{
		return pickedAtRandom;
	}
	
	@Nullable
	@Override
	public TerrainLandAspect getParent()
	{
		return parent;
	}
	
	//public void modifyStructureList(List<MapGenLandStructure.StructureEntry> list)
	//{}
	
	@Override
	public IGateStructure getGateStructure()
	{
		return new GateStructurePillar();
	}
	
	public abstract EntityType<? extends ConsortEntity> getConsortType();
	
}