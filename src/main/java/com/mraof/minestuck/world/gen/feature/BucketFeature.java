package com.mraof.minestuck.world.gen.feature;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.world.gen.structure.blocks.StructureBlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;

public class BucketFeature extends Feature<NoneFeatureConfiguration>
{
	private static final ResourceLocation STRUCTURE_BUCKET_0 = new ResourceLocation(Minestuck.MOD_ID, "bucket_0");
	private static final ResourceLocation STRUCTURE_BUCKET_1 = new ResourceLocation(Minestuck.MOD_ID, "bucket_1");
	private static final ResourceLocation STRUCTURE_BUCKET_WITH_HANDLE_0 = new ResourceLocation(Minestuck.MOD_ID, "bucket_with_handle_0");
	private static final ResourceLocation STRUCTURE_BUCKET_WITH_HANDLE_1 = new ResourceLocation(Minestuck.MOD_ID, "bucket_with_handle_1");
	
	public BucketFeature(Codec<NoneFeatureConfiguration> codec)
	{
		super(codec);
	}
	
	
	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context)
	{
		BlockPos pos = context.origin();
		WorldGenLevel level = context.level();
		RandomSource rand = context.random();
		
		StructureTemplate template = level.getLevel().getStructureManager().getOrCreate(pickTemplate(rand));
		TemplatePlacement placement = TemplatePlacement.centeredWithRandomRotation(template, pos, rand);
		
		StructurePlaceSettings settings = new StructurePlaceSettings()
				.addProcessor(new RuleProcessor(ImmutableList.of(
						new ProcessorRule(new BlockMatchTest(Blocks.BLUE_STAINED_GLASS), AlwaysTrueTest.INSTANCE, pickBucketFluid(context))
				)));
		
		placement.placeWithStructureBlockRegistry(context, settings);
		
		return true;
	}
	
	private static ResourceLocation pickTemplate(RandomSource rand)
	{
		if(rand.nextFloat() < 0.6F)
			return rand.nextFloat() < 0.7F ? STRUCTURE_BUCKET_0 : STRUCTURE_BUCKET_1;
		else
			return rand.nextFloat() < 0.7F ? STRUCTURE_BUCKET_WITH_HANDLE_0 : STRUCTURE_BUCKET_WITH_HANDLE_1;
	}
	
	private static BlockState pickBucketFluid(FeaturePlaceContext<NoneFeatureConfiguration> context)
	{
		RandomSource rand = context.random();
		if(rand.nextBoolean())
		{
			StructureBlockRegistry blocks = StructureBlockRegistry.getOrDefault(context.chunkGenerator());
			return blocks.getBlockState(rand.nextBoolean() ? "ocean" : "river");
		}
		
		SimpleWeightedRandomList.Builder<BlockState> list = SimpleWeightedRandomList.builder();
		list.add(Blocks.AIR.defaultBlockState(), 50);
		for(Fluid fluid : BuiltInRegistries.FLUID)
		{
			FluidState fluidState = fluid.defaultFluidState();
			if(!fluidState.isSource())
				continue;
			
			BlockState fluidBlock = fluidState.createLegacyBlock();
			switch(fluid.getFluidType().getRarity())
			{
				case COMMON -> list.add(fluidBlock, 50);
				case UNCOMMON -> list.add(fluidBlock, 10);
				case RARE -> list.add(fluidBlock, 1);
			}
		}
		
		return list.build().getRandomValue(rand).orElseThrow();
	}
}