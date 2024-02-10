package com.mraof.minestuck.world.gen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Decoder;
import com.mojang.serialization.Encoder;
import com.mraof.minestuck.world.gen.structure.blocks.StructureBlockRegistry;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class StructureBlockRegistryProcessor extends StructureProcessor
{
	public static final Codec<StructureBlockRegistryProcessor> CODEC = Codec.of(Encoder.error("StructureBlockRegistryProcessor is not serializable."), Decoder.error("StructureBlockRegistryProcessor is not serializable."));
	private final StructureBlockRegistry blocks;
	
	public StructureBlockRegistryProcessor(StructureBlockRegistry blocks)
	{
		this.blocks = blocks;
	}
	
	public static StructureBlockRegistryProcessor from(FeaturePlaceContext<?> context)
	{
		return new StructureBlockRegistryProcessor(StructureBlockRegistry.getOrDefault(context.chunkGenerator()));
	}
	
	@Nullable
	@Override	//TODO figure out blockpos difference
	public StructureTemplate.StructureBlockInfo process(LevelReader level, BlockPos blockPos, BlockPos blockPos2, StructureTemplate.StructureBlockInfo original, StructureTemplate.StructureBlockInfo current, StructurePlaceSettings placementSettings, @Nullable StructureTemplate template)
	{
		BlockState newState = blocks.getTemplateState(original.state());
		return new StructureTemplate.StructureBlockInfo(current.pos(), newState, current.nbt());
	}
	
	@Override
	protected StructureProcessorType<StructureBlockRegistryProcessor> getType()
	{
		return MSStructureProcessorTypes.BLOCK_REGISTRY.get();
	}
	
}