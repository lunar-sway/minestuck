package com.mraof.minestuck.world.gen.feature;

import com.mraof.minestuck.Minestuck;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

public class MSStructureProcessorTypes
{
	static StructureProcessorType<StructureBlockRegistryProcessor> BLOCK_REGISTRY;
	
	/**
	 * Should only be called by {@link com.mraof.minestuck.world.gen.feature.MSFeatures} on feature registry.
	 */
	static void init()
	{
		BLOCK_REGISTRY = register("block_registry", () -> StructureBlockRegistryProcessor.CODEC);
	}
	
	private static <T extends StructureProcessor> StructureProcessorType<T> register(String name, StructureProcessorType<T> type)
	{
		return Registry.register(Registry.STRUCTURE_PROCESSOR, Minestuck.MOD_ID+":"+name, type);
	}
}