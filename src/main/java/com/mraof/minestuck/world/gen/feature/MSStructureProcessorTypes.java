package com.mraof.minestuck.world.gen.feature;

import com.mraof.minestuck.Minestuck;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class MSStructureProcessorTypes
{
	public static final DeferredRegister<StructureProcessorType<?>> REGISTER = DeferredRegister.create(Registries.STRUCTURE_PROCESSOR, Minestuck.MOD_ID);
	
	public static final Supplier<StructureProcessorType<StructureBlockRegistryProcessor>> BLOCK_REGISTRY = REGISTER.register("block_registry", () -> () -> StructureBlockRegistryProcessor.CODEC);
	
}