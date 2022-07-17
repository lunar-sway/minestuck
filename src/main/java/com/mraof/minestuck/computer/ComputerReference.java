package com.mraof.minestuck.computer;

import com.mraof.minestuck.tileentity.ComputerTileEntity;
import net.minecraft.core.GlobalPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;

import java.util.Objects;

public interface ComputerReference
{
	static ComputerReference of(ComputerTileEntity te)
	{
		return new TEComputerReference(GlobalPos.of(Objects.requireNonNull(te.getLevel()).dimension(), te.getBlockPos()));
	}
	
	static ComputerReference read(CompoundTag nbt)
	{
		String type = nbt.getString("type");
		if(type.equals("tile_entity"))
			return TEComputerReference.create(nbt);
		else throw new IllegalStateException("Invalid computer type: " + type);
	}
	
	default CompoundTag write(CompoundTag nbt)
	{
		return nbt;
	}
	
	//TODO look over usages to limit force loading of dimensions
	ISburbComputer getComputer(MinecraftServer server);
	
	boolean matches(ISburbComputer computer);
	
	boolean isInNether();
	
	GlobalPos getPosForEditmode();
}