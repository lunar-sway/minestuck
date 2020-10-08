package com.mraof.minestuck.world.gen.feature;

import com.mojang.serialization.Codec;
import com.mraof.minestuck.world.gen.feature.structure.blocks.StructureBlockRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraftforge.common.util.Constants;

import java.util.Random;

public class FireFieldFeature extends Feature<NoFeatureConfig>
{
	private static final int BLOCK_COUNT = 96;
	private static final float FIRE_CHANCE = 0.5F;
	
	public FireFieldFeature(Codec<NoFeatureConfig> codec)
	{
		super(codec);
	}
	
	@Override
	public boolean func_241855_a(ISeedReader world, ChunkGenerator generator, Random rand, BlockPos pos, NoFeatureConfig config)
	{
		StructureBlockRegistry blocks = StructureBlockRegistry.getOrDefault(generator);
		BlockState surface = blocks.getBlockState("surface");
		BlockState upper = blocks.getBlockState("upper");
		
		for(int i2 = 0; i2 < BLOCK_COUNT; i2++)
		{
			BlockPos pos1 = pos.add(rand.nextInt(8) - rand.nextInt(8), rand.nextInt(4) - rand.nextInt(4), rand.nextInt(8) - rand.nextInt(8));
			BlockState block = world.getBlockState(pos1);
			if(block == surface || block == upper)
			{
				world.setBlockState(pos1, Blocks.NETHERRACK.getDefaultState(), Constants.BlockFlags.BLOCK_UPDATE);
				if(world.isAirBlock(pos1.up()) && rand.nextFloat() < FIRE_CHANCE)
					world.setBlockState(pos1.up(), Blocks.FIRE.getDefaultState(), Constants.BlockFlags.BLOCK_UPDATE);
			}
		}
		
		return true;
	}
}