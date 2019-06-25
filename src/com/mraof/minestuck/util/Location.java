package com.mraof.minestuck.util;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.dimension.DimensionType;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nullable;

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
		Validate.notNull(pos, "Position cannot be null for locations!");
		Validate.notNull(dim, "Dimension type cannot be null for locations!");
		this.pos = pos;
		this.dim = dim;
	}
	
	public Location(TileEntity te)
	{
		this.pos = te.getPos();
		this.dim = te.getWorld().getDimension().getType();
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
		
		ResourceLocation dimName = ResourceLocation.makeResourceLocation(nbt.getString("dim"));
		if(dimName == null)
		{
			Debug.warnf("Could not parse dimension name %s. This is not good!", nbt.getString("dim"));
			return null;
		}
		
		DimensionType dimension = DimensionType.byName(dimName);
		if(dimension == null)
		{
			Debug.warnf("Could not find dimension by name %s.", dimName);
			return null;
		}
		
		return new Location(blockPos, dimension);
	}
	
	public static Location fromBuffer(PacketBuffer buffer)
	{
		BlockPos pos = buffer.readBlockPos();
		DimensionType dimension = DimensionType.getById(buffer.readInt());
		
		return new Location(pos, dimension);
	}
	
	@Nullable
	public NBTTagCompound toNBT(NBTTagCompound nbt)
	{
		nbt.setInt("x", pos.getX());
		nbt.setInt("y", pos.getY());
		nbt.setInt("z", pos.getZ());
		ResourceLocation dimName = DimensionType.func_212678_a(dim);
		if(dimName != null)
			nbt.setString("dim", dimName.toString());
		else
		{
			Debug.warnf("Could not save dimension %s. Could not get dimension name!", dim);
			return null;
		}
		
		return nbt;
	}
	
	public void toBuffer(PacketBuffer buffer)
	{
		buffer.writeBlockPos(pos);
		buffer.writeInt(dim.getId());
	}
}
