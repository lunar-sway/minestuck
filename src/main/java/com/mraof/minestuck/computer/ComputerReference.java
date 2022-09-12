package com.mraof.minestuck.computer;

import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import net.minecraft.core.GlobalPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;

import java.util.Objects;

public interface ComputerReference
{
	static ComputerReference of(ComputerBlockEntity be)
	{
		return new BEComputerReference(GlobalPos.of(Objects.requireNonNull(be.getLevel()).dimension(), be.getBlockPos()));
	}
	
	static ComputerReference read(CompoundTag nbt)
	{
		String type = nbt.getString("type");
		if(type.equals("block_entity"))
			return BEComputerReference.create(nbt);
		else throw new IllegalStateException("Invalid computer type: " + type);
	}
	
	default CompoundTag write(CompoundTag nbt)
	{
		return nbt;
	}
	
	//TODO look over usages to limit force loading of dimensions, it is used in the checkData function of SkaianetHandler which is itself called indirectly once per tick on the server side
	ISburbComputer getComputer(MinecraftServer server);
	
	boolean matches(ISburbComputer computer);
	
	boolean isInNether();
	
	GlobalPos getPosForEditmode();
}