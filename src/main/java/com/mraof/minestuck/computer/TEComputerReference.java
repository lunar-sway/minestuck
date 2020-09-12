package com.mraof.minestuck.computer;

import com.mojang.datafixers.Dynamic;
import com.mraof.minestuck.tileentity.ComputerTileEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTDynamicOps;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.DimensionManager;

import java.util.Objects;

class TEComputerReference extends ComputerReference
{
	protected final GlobalPos location;
	
	TEComputerReference(GlobalPos location)
	{
		this.location = location;
	}
	
	static TEComputerReference create(CompoundNBT nbt)
	{
		GlobalPos location = GlobalPos.deserialize(new Dynamic<>(NBTDynamicOps.INSTANCE, nbt.getCompound("pos")));
		return new TEComputerReference(location);
	}
	
	@Override
	public CompoundNBT write(CompoundNBT nbt)
	{
		nbt.putString("type", "tile_entity");
		nbt.put("pos", location.serialize(NBTDynamicOps.INSTANCE));
		return super.write(nbt);
	}
	
	@Override
	public ISburbComputer getComputer(MinecraftServer server)
	{
		World world = DimensionManager.getWorld(server, location.getDimension(), false, true);
		if(world == null)
			return null;
		TileEntity te = world.getTileEntity(location.getPos());
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
			return location.getDimension() == Objects.requireNonNull(te.getWorld()).getDimension().getType() && location.getPos().equals(te.getPos());
		} else return false;
	}
	
	@Override
	public boolean isInNether()
	{
		return location.getDimension() == DimensionType.THE_NETHER;
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