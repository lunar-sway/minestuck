package com.mraof.minestuck.world.gen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Decoder;
import com.mojang.serialization.Encoder;
import com.mraof.minestuck.world.gen.feature.structure.blocks.StructureBlockRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.Template;

import javax.annotation.Nullable;

public class StructureBlockRegistryProcessor extends StructureProcessor
{
	public static final Codec<StructureBlockRegistryProcessor> CODEC = Codec.of(Encoder.error("StructureBlockRegistryProcessor is not serializable."), Decoder.error("StructureBlockRegistryProcessor is not serializable."));
	private final StructureBlockRegistry blocks;
	
	public StructureBlockRegistryProcessor(StructureBlockRegistry blocks)
	{
		this.blocks = blocks;
	}
	
	@Nullable
	@Override	//TODO figure out blockpos difference
	public Template.BlockInfo process(IWorldReader world, BlockPos blockPos, BlockPos blockPos2, Template.BlockInfo original, Template.BlockInfo current, PlacementSettings placementSettings, @Nullable Template template)
	{
		BlockState newState = blocks.getTemplateState(original.state);
		return new Template.BlockInfo(current.pos, newState, current.nbt);
	}
	
	@Override
	protected IStructureProcessorType<StructureBlockRegistryProcessor> getType()
	{
		return MSStructureProcessorTypes.BLOCK_REGISTRY;
	}
	
}