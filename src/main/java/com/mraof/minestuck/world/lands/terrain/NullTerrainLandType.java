package com.mraof.minestuck.world.lands.terrain;

import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.entity.consort.ConsortEntity;
import com.mraof.minestuck.world.gen.structure.blocks.StructureBlockRegistry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.Vec3;

public class NullTerrainLandType extends TerrainLandType
{
	private static final Vec3 fogColor = new Vec3(1, 1, 1);
	
	public NullTerrainLandType()
	{
		super(false);
	}
	
	@Override
	public void registerBlocks(StructureBlockRegistry blocks)
	{
	
	}
	
	@Override
	public Vec3 getFogColor()
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