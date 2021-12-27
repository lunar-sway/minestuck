package com.mraof.minestuck.computer;

import com.mraof.minestuck.tileentity.ComputerTileEntity;
import com.mraof.minestuck.util.Debug;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTDynamicOps;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.world.World;

import java.util.Objects;

class TEComputerReference implements ComputerReference
{
	protected final GlobalPos location;
	
	TEComputerReference(GlobalPos location)
	{
		this.location = location;
	}
	
	static TEComputerReference create(CompoundNBT nbt)
	{
		GlobalPos location = GlobalPos.CODEC.parse(NBTDynamicOps.INSTANCE, nbt.get("pos")).resultOrPartial(Debug::error).orElse(null);
		return new TEComputerReference(location);
	}
	
	@Override
	public CompoundNBT write(CompoundNBT nbt)
	{
		nbt.putString("type", "tile_entity");
		GlobalPos.CODEC.encodeStart(NBTDynamicOps.INSTANCE, location).resultOrPartial(Debug::error).ifPresent(tag -> nbt.put("pos", tag));
		return nbt;
	}
	
	@Override
	public ISburbComputer getComputer(MinecraftServer server)
	{
		World world = server.getLevel(location.dimension());
		if(world == null)
			return null;
		TileEntity te = world.getBlockEntity(location.pos());
		if(te instanceof ISburbComputer)
			return (ISburbComputer) te;
		else return null;
	}
	
	@Override
	public boolean matches(ISburbComputer computer)
	{
		if(computer instanceof ComputerTileEntity)
		{
			ComputerTileEntity te = (ComputerTileEntity) computer;
			return location.dimension() == Objects.requireNonNull(te.getLevel()).dimension() && location.pos().equals(te.getBlockPos());
		} else return false;
	}
	
	@Override
	public boolean isInNether()
	{
		return location.dimension() == World.NETHER;
	}
	
	@Override
	public GlobalPos getPosForEditmode()
	{
		return location;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		TEComputerReference that = (TEComputerReference) o;
		return location.equals(that.location);
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(location);
	}
}