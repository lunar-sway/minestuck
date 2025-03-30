package com.mraof.minestuck.util;

import net.minecraft.core.BlockPos;

/**
 * A simple x/z coordinate pair
 */
public record CoordPair(int x, int z)
{
	public BlockPos atY(int y)
	{
		return new BlockPos(this.x, y, this.z);
	}
	
	public CoordPair north()
	{
		return new CoordPair(x, z - 1);
	}
	
	public CoordPair south()
	{
		return new CoordPair(x, z + 1);
	}
	
	public CoordPair west()
	{
		return new CoordPair(x - 1, z);
	}
	
	public CoordPair east()
	{
		return new CoordPair(x + 1, z);
	}
}
