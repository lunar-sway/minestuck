package com.mraof.minestuck.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.dimension.DimensionType;

public class Location
{
	public BlockPos pos;
	public DimensionType dim;

	public Location()
	{
		this(0, -1, 0, DimensionType.OVERWORLD);
	}
	public Location(int x, int y, int z, DimensionType dim)
	{
		this(new BlockPos(x, y, z), dim);
		this.dim = dim;
	}
	
	public Location(BlockPos pos, DimensionType dim)
	{
		this.pos = pos;
		this.dim = dim;
	}
	
	@Override
	public String toString()
	{
		return "Dim " + dim.toString() + ": " + pos.getX() + " " + pos.getY() + " " + pos.getZ();
	}

	@Override
	public boolean equals(Object obj)
	{
		if(obj.getClass() != this.getClass())
			return false;
		Location loc = (Location) obj;
		return loc.pos.equals(this.pos) && loc.dim == this.dim;
	}
}
