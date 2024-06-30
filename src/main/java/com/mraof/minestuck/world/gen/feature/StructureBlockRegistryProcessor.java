package com.mraof.minestuck.world.gen.feature;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mraof.minestuck.world.gen.structure.blocks.StructureBlockRegistry;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class StructureBlockRegistryProcessor extends StructureProcessor
{
	static final Logger LOGGER = LogUtils.getLogger();
	public static final Codec<StructureBlockRegistryProcessor> CODEC = Codec.unit(() -> StructureBlockRegistryProcessor.INSTANCE);
	public static final StructureBlockRegistryProcessor INSTANCE = new StructureBlockRegistryProcessor();
	
	private StructureBlockRegistryProcessor()
	{
	}
	
	//public static final Codec<StructureBlockRegistryProcessor> CODEC = Codec.of(Encoder.error("StructureBlockRegistryProcessor is not serializable."), Decoder.error("StructureBlockRegistryProcessor is not serializable."));
	//private final StructureBlockRegistry blocks;
	
	/*public StructureBlockRegistryProcessor(StructureBlockRegistry blocks)
	{
		this.blocks = blocks;
	}
	
	public static StructureBlockRegistryProcessor from(FeaturePlaceContext<?> context)
	{
		return new StructureBlockRegistryProcessor(StructureBlockRegistry.getOrDefault(context.chunkGenerator()));
	}*/
	
	@Override
	public List<StructureTemplate.StructureBlockInfo> finalizeProcessing(ServerLevelAccessor levelAccessor, BlockPos pOffset, BlockPos pPos, List<StructureTemplate.StructureBlockInfo> originals, List<StructureTemplate.StructureBlockInfo> currents, StructurePlaceSettings placementSettings)
	{
		ServerLevel serverLevel = levelAccessor.getLevel();
		ChunkGenerator chunkGenerator = serverLevel.getChunkSource().getGenerator();
		StructureBlockRegistry blockRegistry = StructureBlockRegistry.getOrDefault(chunkGenerator);
		
		//Registry<StructureProcessorType<?>> processorTypes = serverLevel.registryAccess().registryOrThrow(Registries.STRUCTURE_PROCESSOR);
		//LOGGER.debug("processorTypes = {}", processorTypes);
		
		for(int i = 0; i < originals.size(); i++)
		{
			StructureTemplate.StructureBlockInfo original = originals.get(i);
			StructureTemplate.StructureBlockInfo current = currents.get(i);
			
			if(original.state() != current.state())
				continue;
			
			BlockState newState = blockRegistry.getTemplateState(current.state());
			currents.set(i, new StructureTemplate.StructureBlockInfo(current.pos(), newState, current.nbt()));
			//LOGGER.debug("newState = {}", newState);
		}
		
		return currents;
		//return super.finalizeProcessing(levelAccessor, pOffset, pPos, originals, currents, placementSettings);
	}
	
	/*@Nullable
	@Override	//TODO figure out blockpos difference
	public StructureTemplate.StructureBlockInfo process(LevelReader level, BlockPos blockPos, BlockPos blockPos2, StructureTemplate.StructureBlockInfo original, StructureTemplate.StructureBlockInfo current, StructurePlaceSettings placementSettings, @Nullable StructureTemplate template)
	{
		if(original.state() != current.state())
			return current;
		BlockState newState = blocks.getTemplateState(current.state());
		return new StructureTemplate.StructureBlockInfo(current.pos(), newState, current.nbt());
	}*/
	
	@Override
	protected StructureProcessorType<StructureBlockRegistryProcessor> getType()
	{
		return MSStructureProcessorTypes.BLOCK_REGISTRY.get();
	}
	
}