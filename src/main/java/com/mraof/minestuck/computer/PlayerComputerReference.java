package com.mraof.minestuck.computer;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerIdentifier;
import com.mraof.minestuck.skaianet.SkaianetData;
import net.minecraft.core.GlobalPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.util.Objects;

final class PlayerComputerReference implements ComputerReference
{
	static final MapCodec<PlayerComputerReference> CODEC = CompoundTag.CODEC
			.flatXmap(
					tag -> IdentifierHandler.load(tag, "owner").map(PlayerComputerReference::new),
					ref -> {
						CompoundTag tag = new CompoundTag();
						ref.owner.saveToNBT(tag, "owner");
						return DataResult.success(tag);
					}
			).fieldOf("player");
	final PlayerIdentifier owner;
	PlayerComputerReference(PlayerIdentifier owner)
	{
		this.owner = Objects.requireNonNull(owner);
	}
	
	@Override
	public ISburbComputer getComputer(MinecraftServer server)
	{
		return SkaianetData.get(server).getLaptopForOwner(owner);
	}
	
	@Override
	public boolean matches(ISburbComputer computer)
	{
		return owner.equals(computer.getOwner());
	}
	
	@Override
	public boolean isInNether()
	{
		return false;
	}
	
	@Override
	public GlobalPos getPosForEditmode()
	{
		MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
		if(server != null)
		{
			ISburbComputer computer = getComputer(server);
			if(computer instanceof ComputerBlockEntity be)
				return GlobalPos.of(
						Objects.requireNonNull(be.getLevel()).dimension(),
						be.getBlockPos());
		}
		throw new IllegalStateException("Laptop is not in the world!!! cannot get editmode position");
	}
	
	@Override
	public boolean allowsTemporaryAbsence()
	{
		return true;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		return owner.equals(((PlayerComputerReference) o).owner);
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(owner);
	}
}