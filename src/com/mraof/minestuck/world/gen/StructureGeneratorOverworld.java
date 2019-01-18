package com.mraof.minestuck.world.gen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import com.mraof.minestuck.world.gen.structure.StructureTemple;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeOcean;
import net.minecraft.world.biome.BiomePlains;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

public class StructureGeneratorOverworld implements IWorldGenerator
{

	public static StructureTemple frogTemple = new StructureTemple();
	
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
			IChunkProvider chunkProvider) 
	{
		if(world.provider.getDimension() == 0)
		{
			generateStructure(frogTemple, world, random, chunkX, chunkZ, 413, BiomePlains.class);
			
		}
	}
	
	private void generateStructure(WorldGenerator generator, World world, Random rand, int chunkX, int chunkZ, int chance, Class<?>... biomes)
	{
		ArrayList<Class<?>> biomesList = new ArrayList<Class<?>>(Arrays.asList(biomes));
		int x = chunkX*16 + rand.nextInt(15);
		int z = chunkZ*16 + rand.nextInt(15);
		int y = world.getHeight(x, z);
		BlockPos pos = new BlockPos(x, y, z);
		
		Class<?> biome = world.provider.getBiomeForCoords(pos).getClass();
		
		
		//if(world.getWorldType()  != WorldType.FLAT)
		{
			if(biomesList.contains(biome))
			{
				if(rand.nextInt(chance) == 0)
				{
					generator.generate(world, rand, pos);
				}
			}
		}
	}
	
}
