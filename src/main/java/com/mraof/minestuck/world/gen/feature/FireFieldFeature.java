package com.mraof.minestuck.world.gen.feature;

import com.mojang.datafixers.Dynamic;
import com.mraof.minestuck.world.gen.feature.structure.blocks.StructureBlockRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

import java.util.Random;
import java.util.function.Function;

public class FireFieldFeature extends Feature<NoFeatureConfig>
{
	private static final int BLOCK_COUNT = 96;
	private static final float FIRE_CHANCE = 0.5F;
	
	public FireFieldFeature(Function<Dynamic<?>, ? extends NoFeatureConfig> configFactoryIn)
	{
		super(configFactoryIn);
	}
	
	@Override
	public boolean place(IWorld worldIn, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, NoFeatureConfig config)
	{
		StructureBlockRegistry blocks = StructureBlockRegistry.getOrDefault(generator.getSettings());
		BlockState surface = blocks.getBlockState("surface");
		BlockState upper = blocks.getBlockState("upper");
		
		for(int i2 = 0; i2 < BLOCK_COUNT; i2++)
		{
			BlockPos pos1 = pos.add(rand.nextInt(8) - rand.nextInt(8), rand.nextInt(4) - rand.nextInt(4), rand.nextInt(8) - rand.nextInt(8));
			BlockState block = worldIn.getBlockState(pos1);
			if(block == surface || block == upper)
			{
				worldIn.setBlockState(pos1, Blocks.NETHERRACK.getDefaultState(), 2);
				if(worldIn.isAirBlock(pos1.up()) && rand.nextFloat() < FIRE_CHANCE)
					worldIn.setBlockState(pos1.up(), Blocks.FIRE.getDefaultState(), 2);
			}
		}
		
		return true;
	}
}