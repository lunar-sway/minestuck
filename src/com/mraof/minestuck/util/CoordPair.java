package com.mraof.minestuck.util;

public class CoordPair
{
	public final int x;
	public final int z;
	public CoordPair(int x, int z)
	{
		this.x = x;
		this.z = z;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		return obj instanceof CoordPair && ((CoordPair) obj).x == x && ((CoordPair) obj).z == z;
	}
	
	@Override
	public int hashCode()
	{
		return (Integer.hashCode(x) << 16) | (Integer.hashCode(z) >> 16);
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