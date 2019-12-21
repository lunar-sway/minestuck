package com.mraof.minestuck.network.skaianet;

import com.mraof.minestuck.tileentity.ComputerTileEntity;
import com.mraof.minestuck.util.IdentifierHandler;
import com.mraof.minestuck.util.IdentifierHandler.PlayerIdentifier;
import com.mraof.minestuck.util.Location;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.dimension.DimensionType;

import javax.annotation.Nonnull;
import java.util.Objects;

public class ComputerData
{
	//TODO Is this needed anymore?
	
	@Nonnull
	private final Location location;
	@Nonnull
	private final PlayerIdentifier owner;
	
	public static ComputerData createData(ComputerTileEntity te)
	{
		if(!Objects.requireNonNull(te.getWorld()).isRemote)
			return new ComputerData(te.owner, new Location(te.getPos(), te.getWorld().dimension.getType()));
		else throw new IllegalStateException("Should not call createData() from client side");
	}
	
	public ComputerData(PlayerIdentifier owner, Location location)
	{
		this.owner = Objects.requireNonNull(owner);
		this.location = Objects.requireNonNull(location);
	}
	
	ComputerData(CompoundNBT nbt)
	{
		owner = IdentifierHandler.load(nbt, "name");
		location = Objects.requireNonNull(Location.fromNBT(nbt), "Unable to load computer location.");
	}
	
	CompoundNBT write(CompoundNBT nbt)
	{
		owner.saveToNBT(nbt, "name");
		location.toNBT(nbt);
		return nbt;
	}
	
	public Location getLocation() {return location;}
	public BlockPos getPos() {return location.pos;}
	public DimensionType getDimension() {return location.dim;}
	public PlayerIdentifier getOwner() {return owner;}
	
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
	
	@Override
	public int hashCode()
	{
		return Objects.hash(location, owner);
	}
}