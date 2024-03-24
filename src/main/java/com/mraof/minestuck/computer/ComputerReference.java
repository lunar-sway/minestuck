package com.mraof.minestuck.computer;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.MinecraftServer;

import java.util.Objects;

/**
 * The interface for a reference to an in-world computer that can interact with sburb computers.
 * This reference is primarily used to find the actual computer to update data or validate that the computer exists.
 * This interface is sealed because the allowed variants are hardcoded inside the codec.
 */
public sealed interface ComputerReference permits BEComputerReference
{
	Codec<ComputerReference> CODEC = Codec.STRING.partialDispatch("type",
			reference -> reference instanceof BEComputerReference ? DataResult.success("block_entity") : DataResult.error(() -> "Unknown reference class: " + reference.getClass()),
			type -> type.equals("block_entity") ? DataResult.success(BEComputerReference.CODEC) : DataResult.error(() -> "Unknown computer type: " + type));
	
	static ComputerReference of(ComputerBlockEntity be)
	{
		return new BEComputerReference(GlobalPos.of(Objects.requireNonNull(be.getLevel()).dimension(), be.getBlockPos()));
	}
	
	//TODO look over usages to limit force loading of dimensions, it is used in the checkData function of SkaianetHandler which is itself called indirectly once per tick on the server side
	ISburbComputer getComputer(MinecraftServer server);
	
	boolean matches(ISburbComputer computer);
	
	boolean isInNether();
	
	GlobalPos getPosForEditmode();
}