package com.mraof.minestuck.world.lands.terrain;

import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.world.gen.structure.blocks.StructureBlockRegistry;
import net.minecraft.world.phys.Vec3;

public class NullTerrainLandType extends TerrainLandType
{
	private static final Vec3 fogColor = new Vec3(1, 1, 1);
	
	public NullTerrainLandType()
	{
		super(new Builder(() -> MSEntityTypes.SALAMANDER).unavailable());
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
	public String[] getNames()
	{
		return new String[] {"null"};
	}
}