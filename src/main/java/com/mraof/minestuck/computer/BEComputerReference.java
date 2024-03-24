package com.mraof.minestuck.computer;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Objects;

final class BEComputerReference implements ComputerReference
{
	static final Codec<BEComputerReference> CODEC = RecordCodecBuilder.create(instance ->
			instance.group(GlobalPos.CODEC.fieldOf("pos").forGetter(reference -> reference.location))
					.apply(instance, BEComputerReference::new));
	
	private final GlobalPos location;
	
	BEComputerReference(GlobalPos location)
	{
		this.location = Objects.requireNonNull(location);
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
		BEComputerReference that = (BEComputerReference) o;
		return location.equals(that.location);
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(location);
	}
}