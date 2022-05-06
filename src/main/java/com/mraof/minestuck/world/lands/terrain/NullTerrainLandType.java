package com.mraof.minestuck.world.lands.terrain;

import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.entity.consort.ConsortEntity;
import com.mraof.minestuck.world.gen.feature.structure.blocks.StructureBlockRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.vector.Vector3d;

public class NullTerrainLandType extends TerrainLandType
{
	private static final Vector3d fogColor = new Vector3d(1, 1, 1);
	
	public NullTerrainLandType()
	{
		super(false);
	}
	
	@Override
	public void registerBlocks(StructureBlockRegistry blocks)
	{
	
	}
	
	@Override
	public Vector3d getFogColor()
	{
		return fogColor;
	}
	
	@Override
	public EntityType<? extends ConsortEntity> getConsortType()
	{
		return MSEntityTypes.SALAMANDER;
	}
	
	@Override
	public String[] getNames()
	{
		return new String[] {"null"};
	}
}