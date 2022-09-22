package com.mraof.minestuck.world.gen.feature;

import com.mraof.minestuck.Minestuck;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class MSStructureProcessorTypes
{
	public static final DeferredRegister<StructureProcessorType<?>> REGISTER = DeferredRegister.create(Registry.STRUCTURE_PROCESSOR_REGISTRY, Minestuck.MOD_ID);
	
	public static final RegistryObject<StructureProcessorType<StructureBlockRegistryProcessor>> BLOCK_REGISTRY = REGISTER.register("block_entity", () -> () -> StructureBlockRegistryProcessor.CODEC);
	
}