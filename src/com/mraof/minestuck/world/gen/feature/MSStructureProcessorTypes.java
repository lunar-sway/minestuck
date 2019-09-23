package com.mraof.minestuck.world.gen.feature;

import com.mraof.minestuck.Minestuck;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;

public class MSStructureProcessorTypes
{
	static final IStructureProcessorType BLOCK_REGISTRY = register("block_registry", dynamic -> StructureBlockRegistryProcessor.INSTANCE);
	
	public static void call()
	{
	}
	
	private static IStructureProcessorType register(String name, IStructureProcessorType type)
	{
		return Registry.register(Registry.STRUCTURE_PROCESSOR, Minestuck.MOD_ID+":"+name, type);
	}
}