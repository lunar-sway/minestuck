package com.mraof.minestuck.world.gen.feature;

import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;
import com.mraof.minestuck.world.LandDimension;
import com.mraof.minestuck.world.gen.LandGenSettings;
import com.mraof.minestuck.world.lands.structure.blocks.StructureBlockRegistry;
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
	public static final StructureBlockRegistryProcessor INSTANCE = new StructureBlockRegistryProcessor();
	
	@Nullable
	@Override
	public Template.BlockInfo process(IWorldReader world, BlockPos blockPos, Template.BlockInfo original, Template.BlockInfo current, PlacementSettings placementSettings)
	{
		if(world.getDimension() instanceof LandDimension)
		{
			LandDimension dimension = (LandDimension) world.getDimension();
			LandGenSettings settings = (LandGenSettings) dimension.getWorld().getChunkProvider().getChunkGenerator().getSettings();
			StructureBlockRegistry registry = settings.getBlockRegistry();
			BlockState newState = registry.getTemplateState(original.state);
			return new Template.BlockInfo(current.pos, newState, current.nbt);
		}
		return current;
	}
	
	@Override
	protected IStructureProcessorType getType()
	{
		return MSStructureProcessorTypes.BLOCK_REGISTRY;
	}
	
	@Override
	protected <T> Dynamic<T> serialize0(DynamicOps<T> dynamicOps)
	{
		return new Dynamic<>(dynamicOps, dynamicOps.emptyMap());
	}
}