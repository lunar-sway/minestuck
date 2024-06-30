package com.mraof.minestuck.world.gen.feature;

import com.mojang.serialization.Codec;
import com.mraof.minestuck.world.gen.structure.blocks.StructureBlockRegistry;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class StructureBlockRegistryProcessor extends StructureProcessor
{
	public static final Codec<StructureBlockRegistryProcessor> CODEC = Codec.unit(() -> StructureBlockRegistryProcessor.INSTANCE);
	public static final StructureBlockRegistryProcessor INSTANCE = new StructureBlockRegistryProcessor();
	
	private StructureBlockRegistryProcessor()
	{
	}
	
	@Override
	public List<StructureTemplate.StructureBlockInfo> finalizeProcessing(ServerLevelAccessor levelAccessor, BlockPos pOffset, BlockPos pPos, List<StructureTemplate.StructureBlockInfo> originals, List<StructureTemplate.StructureBlockInfo> currents, StructurePlaceSettings placementSettings)
	{
		ServerLevel serverLevel = levelAccessor.getLevel();
		ChunkGenerator chunkGenerator = serverLevel.getChunkSource().getGenerator();
		StructureBlockRegistry blockRegistry = StructureBlockRegistry.getOrDefault(chunkGenerator);
		
		for(int i = 0; i < originals.size(); i++)
		{
			StructureTemplate.StructureBlockInfo original = originals.get(i);
			StructureTemplate.StructureBlockInfo current = currents.get(i);
			
			if(original.state() != current.state())
				continue;
			
			BlockState newState = blockRegistry.getTemplateState(current.state());
			currents.set(i, new StructureTemplate.StructureBlockInfo(current.pos(), newState, current.nbt()));
		}
		
		return currents;
	}
	
	@Override
	protected StructureProcessorType<StructureBlockRegistryProcessor> getType()
	{
		return MSStructureProcessorTypes.BLOCK_REGISTRY.get();
	}
	
}