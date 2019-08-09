package com.mraof.minestuck.network.skaianet;

import com.mraof.minestuck.tileentity.ComputerTileEntity;
import com.mraof.minestuck.util.IdentifierHandler;
import com.mraof.minestuck.util.IdentifierHandler.PlayerIdentifier;
import com.mraof.minestuck.util.Location;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.dimension.DimensionType;

public class ComputerData
{
	Location location;
	PlayerIdentifier owner;
	//Client only
	private int ownerId;
	
	public static ComputerData createData(ComputerTileEntity te)
	{
		if(!te.getWorld().isRemote)
			return new ComputerData(te.owner, new Location(te.getPos(), te.getWorld().dimension.getType()));
		else return new ComputerData(te.ownerId, new Location(te.getPos(), te.getWorld().dimension.getType()));
	}
	
	private ComputerData(int ownerId, Location location)
	{
		this.ownerId = ownerId;
		this.location = location;
	}
	
	public ComputerData(PlayerIdentifier owner, Location location)
	{
		this.owner = owner;
		this.location = location;
	}
	
	ComputerData()
	{}
	
	ComputerData read(CompoundNBT nbt)
	{
		owner = IdentifierHandler.load(nbt, "name");
		location = Location.fromNBT(nbt);
		return this;
	}
	
	CompoundNBT write()
	{
		CompoundNBT c = new CompoundNBT();
		owner.saveToNBT(c, "name");
		return c;
	}
	
	public Location getLocation() {return location;}
	public BlockPos getPos() {return location.pos;}
	public DimensionType getDimension() {return location.dim;}
	public PlayerIdentifier getOwner() {return owner;}
	public int getOwnerId() {return ownerId;}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj instanceof ComputerData)
		{
			ComputerData otherData = (ComputerData) obj;
			return this.owner.equals(otherData.owner) && this.location.equals(otherData.location);
		}
		return false;
	}
	
}