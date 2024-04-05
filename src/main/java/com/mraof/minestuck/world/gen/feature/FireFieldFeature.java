package com.mraof.minestuck.world.gen.feature;

import com.mojang.serialization.Codec;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.world.gen.structure.blocks.StructureBlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class FireFieldFeature extends Feature<NoneFeatureConfiguration>
{
	private static final int BLOCK_COUNT = 96;
	private static final float FIRE_CHANCE = 0.5F;
	
	public FireFieldFeature(Codec<NoneFeatureConfiguration> codec)
	{
		super(codec);
	}
	
	
	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context)
	{
		WorldGenLevel level = context.level();
		BlockPos pos = context.origin();
		RandomSource rand = context.random();
		StructureBlockRegistry blocks = StructureBlockRegistry.getOrDefault(context.chunkGenerator());
		BlockState surface = blocks.getBlockState("surface");
		BlockState upper = blocks.getBlockState("upper");
		
		for(int i2 = 0; i2 < BLOCK_COUNT; i2++)
		{
			BlockPos pos1 = pos.offset(rand.nextInt(8) - rand.nextInt(8), rand.nextInt(4) - rand.nextInt(4), rand.nextInt(8) - rand.nextInt(8));
			BlockState block = level.getBlockState(pos1);
			if(block == surface || block == upper)
			{
				level.setBlock(pos1, MSBlocks.MAGMATIC_IGNEOUS_STONE.get().defaultBlockState(), Block.UPDATE_CLIENTS);
				if(level.isEmptyBlock(pos1.above()) && rand.nextFloat() < FIRE_CHANCE)
					level.setBlock(pos1.above(), Blocks.FIRE.defaultBlockState(), Block.UPDATE_CLIENTS);
			}
		}
		
		return true;
	}
}