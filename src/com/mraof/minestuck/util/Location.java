package com.mraof.minestuck.util;

public class Location
{
	public int x;
	public int y;
	public int z;
	public int dim;

	public Location()
	{
		this(0, -1, 0, 0);
	}
	public Location(int x, int y, int z, int dim)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.dim = dim;
	}

	@Override
	public String toString()
	{
		return "Dim " + dim + ": " + x + " " + y + " " + z;
	}

	@Override
	public boolean equals(Object obj)
	{
		if(obj.getClass() != this.getClass())
			return false;
		Location loc = (Location) obj;
		return loc.x == this.x && loc.y == this.y && loc.z == this.z && loc.dim == this.dim;
	}
}
