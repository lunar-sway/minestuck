package com.mraof.minestuck.computer;

import com.mraof.minestuck.tileentity.ComputerTileEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.GlobalPos;

import java.util.Objects;

public interface ComputerReference
{
	static ComputerReference of(ComputerTileEntity te)
	{
		return new TEComputerReference(GlobalPos.getPosition(Objects.requireNonNull(te.getWorld()).getDimensionKey(), te.getPos()));
	}
	
	static ComputerReference read(CompoundNBT nbt)
	{
		String type = nbt.getString("type");
		if(type.equals("tile_entity"))
			return TEComputerReference.create(nbt);
		else throw new IllegalStateException("Invalid computer type: " + type);
	}
	
	default CompoundNBT write(CompoundNBT nbt)
	{
		return nbt;
	}
	
	//TODO look over usages to limit force loading of dimensions
	ISburbComputer getComputer(MinecraftServer server);
	
	boolean matches(ISburbComputer computer);
	
	boolean isInNether();
	
	GlobalPos getPosForEditmode();
}