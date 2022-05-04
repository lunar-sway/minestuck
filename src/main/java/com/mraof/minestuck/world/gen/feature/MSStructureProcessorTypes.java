package com.mraof.minestuck.world.gen.feature;

import com.mraof.minestuck.Minestuck;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.StructureProcessor;

public class MSStructureProcessorTypes
{
	static IStructureProcessorType<StructureBlockRegistryProcessor> BLOCK_REGISTRY;
	
	/**
	 * Should only be called by {@link com.mraof.minestuck.world.gen.feature.MSFeatures} on feature registry.
	 */
	static void init()
	{
		BLOCK_REGISTRY = register("block_registry", () -> StructureBlockRegistryProcessor.CODEC);
	}
	
	private static <T extends StructureProcessor> IStructureProcessorType<T> register(String name, IStructureProcessorType<T> type)
	{
		return Registry.register(Registry.STRUCTURE_PROCESSOR, Minestuck.MOD_ID+":"+name, type);
	}
}