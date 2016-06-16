package com.mraof.minestuck.util;

import net.minecraft.util.math.BlockPos;

public class Location
{
	public BlockPos pos;
	public int dim;

	public Location()
	{
		this(0, -1, 0, 0);
	}
	public Location(int x, int y, int z, int dim)
	{
		this(new BlockPos(x, y, z), dim);
		this.dim = dim;
	}
	
	public Location(BlockPos pos, int dim)
	{
		this.pos = pos;
		this.dim = dim;
	}
	
	@Override
	public String toString()
	{
		return "Dim " + dim + ": " + pos.getX() + " " + pos.getY() + " " + pos.getZ();
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
