package com.mraof.minestuck.util;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
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
	
	public static Location fromNBT(NBTTagCompound nbt)
	{
		int x = nbt.getInt("x");
		int y = nbt.getInt("y");
		int z = nbt.getInt("z");
		BlockPos blockPos = new BlockPos(x, y, z);
		DimensionType dimension = DimensionType.byName(new ResourceLocation(nbt.getString("dim")));
		return new Location(blockPos, dimension);
	}
	
	public static Location fromBuffer(PacketBuffer buffer)
	{
		BlockPos pos = buffer.readBlockPos();
		DimensionType dimension = DimensionType.getById(buffer.readInt());
		
		return new Location(pos, dimension);
	}
	
	public NBTTagCompound toNBT(NBTTagCompound nbt)
	{
		nbt.setInt("x", pos.getX());
		nbt.setInt("y", pos.getY());
		nbt.setInt("z", pos.getZ());
		nbt.setString("dim", dim.getRegistryName().toString());
		
		return nbt;
	}
	
	public void toBuffer(PacketBuffer buffer)
	{
		buffer.writeBlockPos(pos);
		buffer.writeInt(dim.getId());
	}
}
