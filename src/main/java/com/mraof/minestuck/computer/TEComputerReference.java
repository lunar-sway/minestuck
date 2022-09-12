package com.mraof.minestuck.computer;

import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import net.minecraft.core.GlobalPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

class TEComputerReference implements ComputerReference
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	protected final GlobalPos location;
	
	TEComputerReference(GlobalPos location)
	{
		this.location = location;
	}
	
	static TEComputerReference create(CompoundTag nbt)
	{
		GlobalPos location = GlobalPos.CODEC.parse(NbtOps.INSTANCE, nbt.get("pos")).resultOrPartial(LOGGER::error).orElse(null);
		return new TEComputerReference(location);
	}
	
	@Override
	public CompoundTag write(CompoundTag nbt)
	{
		nbt.putString("type", "block_entity");
		GlobalPos.CODEC.encodeStart(NbtOps.INSTANCE, location).resultOrPartial(LOGGER::error).ifPresent(tag -> nbt.put("pos", tag));
		return nbt;
	}
	
	@Override
	public ISburbComputer getComputer(MinecraftServer server)
	{
		Level level = server.getLevel(location.dimension());
		if(level == null)
			return null;
		BlockEntity be = level.getBlockEntity(location.pos());
		if(be instanceof ISburbComputer)
			return (ISburbComputer) be;
		else return null;
	}
	
	@Override
	public boolean matches(ISburbComputer computer)
	{
		if(computer instanceof ComputerBlockEntity be)
		{
			return location.dimension() == Objects.requireNonNull(be.getLevel()).dimension() && location.pos().equals(be.getBlockPos());
		} else return false;
	}
	
	@Override
	public boolean isInNether()
	{
		return location.dimension() == Level.NETHER;
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