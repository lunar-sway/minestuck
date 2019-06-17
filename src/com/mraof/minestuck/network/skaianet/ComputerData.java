package com.mraof.minestuck.network.skaianet;

import com.mraof.minestuck.tileentity.TileEntityComputer;
import com.mraof.minestuck.util.IdentifierHandler;
import com.mraof.minestuck.util.IdentifierHandler.PlayerIdentifier;
import com.mraof.minestuck.util.Location;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ComputerData
{
	Location location;
	PlayerIdentifier owner;
	@OnlyIn(Dist.CLIENT)
	private int ownerId;
	
	public static ComputerData createData(TileEntityComputer te)
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
	
	ComputerData read(NBTTagCompound nbt)
	{
		owner = IdentifierHandler.load(nbt, "name");
		location = Location.fromNBT(nbt);
		return this;
	}
	
	NBTTagCompound write()
	{
		NBTTagCompound c = new NBTTagCompound();
		owner.saveToNBT(c, "name");
		return c;
	}
	
	public Location getLocation() {return location;}
	public BlockPos getPos() {return location.pos;}
	public DimensionType getDimension() {return location.dim;}
	public PlayerIdentifier getOwner() {return owner;}
	@OnlyIn(Dist.CLIENT) public int getOwnerId() {return ownerId;}
	
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