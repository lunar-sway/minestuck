package com.mraof.minestuck.world.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.Template;

import javax.annotation.Nullable;

public class StructureBlockRegistryProcessor extends StructureProcessor
{
	public static final Codec<StructureBlockRegistryProcessor> CODEC = Codec.unit(() -> StructureBlockRegistryProcessor.INSTANCE);
	public static final StructureBlockRegistryProcessor INSTANCE = new StructureBlockRegistryProcessor();
	
	@Nullable
	@Override	//TODO figure out blockpos difference
	public Template.BlockInfo process(IWorldReader world, BlockPos blockPos, BlockPos blockPos2, Template.BlockInfo original, Template.BlockInfo current, PlacementSettings placementSettings, @Nullable Template template)
	{
		/*if(world.getDimension() instanceof LandDimension) TODO how should we get the registry?
		{
			LandDimension dimension = (LandDimension) world.getDimension();
			StructureBlockRegistry registry = dimension.getBlocks();
			BlockState newState = registry.getTemplateState(original.state);
			return new Template.BlockInfo(current.pos, newState, current.nbt);
		}*/
		return current;
	}
	
	@Override
	protected IStructureProcessorType<StructureBlockRegistryProcessor> getType()
	{
		return MSStructureProcessorTypes.BLOCK_REGISTRY;
	}
	
}