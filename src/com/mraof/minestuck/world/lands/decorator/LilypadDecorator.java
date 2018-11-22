package com.mraof.minestuck.world.lands.decorator;

import java.util.Random;

import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;
import com.mraof.minestuck.world.lands.structure.blocks.StructureBlockRegistry;

import net.minecraft.block.BlockLilyPad;
import net.minecraft.block.BlockNetherWart;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class LilypadDecorator extends BiomeSpecificDecorator
{

	private final int attempts;
	
	public LilypadDecorator(int tries, Biome... biomes)
	{
		super(biomes);
		this.attempts = tries;
	}
	
	@Override
	public float getPriority() 
	{
		return 0.3f;
	}

	@Override
	public BlockPos generate(World world, Random random, BlockPos pos, ChunkProviderLands provider) 
	{
		pos = world.getHeight(pos).down();
		
        if(world.getBlockState(pos) == provider.getOceanBlock() && world.isAirBlock(pos.up()))
        	world.setBlockState(pos.up(), Blocks.WATERLILY.getDefaultState());
        
        return null;
	}

	@Override
	public int getCount(Random random) {
		int count = 0;
		for (int i = 0; i < attempts; i++)
			if (random.nextDouble() < 0.2)
				count++;
		return count;
	}

}
