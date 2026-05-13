package com.mraof.minestuck.computer;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import com.mraof.minestuck.player.PlayerIdentifier;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.MinecraftServer;

import java.util.Objects;

/**
 * The interface for a reference to an in-world computer that can interact with sburb computers.
 * This reference is primarily used to find the actual computer to update data or validate that the computer exists.
 * This interface is sealed because the allowed variants are hardcoded inside the codec.
 */
public sealed interface ComputerReference permits BEComputerReference, PlayerComputerReference
{
	Codec<ComputerReference> CODEC = Codec.STRING.partialDispatch("type",
			reference -> switch(reference)
			{
				case BEComputerReference r -> DataResult.success("block_entity");
				case PlayerComputerReference r -> DataResult.success("player");
			},
			type -> switch(type)
			{
				case "block_entity" -> DataResult.success(BEComputerReference.CODEC);
				case "player" -> DataResult.success(PlayerComputerReference.CODEC);
				default -> DataResult.error(() -> "Unknown computer type: " + type);
			});
	
	static ComputerReference of(ComputerBlockEntity be)
	{
		return new BEComputerReference(GlobalPos.of(
				Objects.requireNonNull(be.getLevel()).dimension(), be.getBlockPos()));
	}
	static ComputerReference forLaptop(PlayerIdentifier owner)
	{
		return new PlayerComputerReference(owner);
	}
	
	ISburbComputer getComputer(MinecraftServer server);
	
	boolean matches(ISburbComputer computer);
	
	boolean isInNether();
	
	GlobalPos getPosForEditmode();
	
	default boolean allowsTemporaryAbsence()
	{
		return false;
	}
}