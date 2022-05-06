package com.mraof.minestuck.world.gen.feature;

import com.mojang.serialization.Codec;
import com.mraof.minestuck.world.gen.feature.structure.blocks.StructureBlockRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class OceanRundownFeature extends Feature<NoFeatureConfig>
{
	
	public OceanRundownFeature(Codec<NoFeatureConfig> codec)
	{
		super(codec);
	}
	
	@Override
	public boolean place(ISeedReader world, ChunkGenerator generator, Random rand, BlockPos pos, NoFeatureConfig config)
	{
		BlockPos pos2, pos3;
		if(generator.getBiomeSource().getBiomesWithin(pos.getX(), world.getSeaLevel(), pos.getZ(), 3)
				.stream().anyMatch(biome -> biome.getBiomeCategory() == Biome.Category.OCEAN))
		{
			return false;
		}
		//Look for ocean and pick pos2 and pos3
		List<BlockPos> oceanPos = new ArrayList<>();
		for(int posX = 0; posX < 16; posX++)
		{
			for(int posZ = 0; posZ < 16; posZ++)
			{
				if(generator.getBiomeSource().getNoiseBiome(pos.getX() + posX - 8 >> 2, pos.getY(), pos.getZ() + posZ - 8 >> 2)
						.getBiomeCategory() == Biome.Category.OCEAN)
					oceanPos.add(pos.offset(posX - 8, 0, posZ - 8));
			}
		}
		if(oceanPos.size() < 10)
			return false;
		pos2 = oceanPos.remove(rand.nextInt(oceanPos.size()));
		pos3 = oceanPos.get(rand.nextInt(oceanPos.size()));
		
		BlockState fluid = StructureBlockRegistry.getOrDefault(generator).getBlockState("ocean");
		int minX = Math.min(pos.getX(), Math.min(pos2.getX(), pos3.getX()));
		int maxX = Math.max(pos.getX(), Math.max(pos2.getX(), pos3.getX()));
		for(int posX = minX; posX <= maxX; posX++)
		{
			int z1, z2;
			if(pos.getX() == posX)
			{
				z1 = pos.getZ();
				if(differentSign(pos2.getX() - posX, pos3.getX() - posX))
					z2 = lineposZ(pos2, pos3, posX);
				else z2 = z1;
			} else if(pos2.getX() == posX)
			{
				z1 = pos2.getZ();
				if(differentSign(pos.getX() - posX, pos3.getX() - posX))
					z2 = lineposZ(pos, pos3, posX);
				else z2 = z1;
			} else if(pos3.getX() == posX)
			{
				z1 = pos3.getZ();
				if(differentSign(pos2.getX() - posX, pos.getX() - posX))
					z2 = lineposZ(pos2, pos, posX);
				else z2 = z1;
			} else if(differentSign(pos.getX() - posX, pos2.getX() - posX))
			{
				z1 = lineposZ(pos, pos2, posX);
				if(differentSign(pos.getX() - posX, pos3.getX() - posX))
					z2 = lineposZ(pos, pos3, posX);
				else z2 = lineposZ(pos2, pos3, posX);
			} else
			{
				z1 = lineposZ(pos, pos3, posX);
				z2 = lineposZ(pos2, pos3, posX);
			}
			if(z1 > z2)
			{
				int tempZ = z2;
				z2 = z1;
				z1 = tempZ;
			}
			for(int posZ = z1; posZ <= z2; posZ++)
			{
				BlockPos groundPos = world.getHeightmapPos(Heightmap.Type.WORLD_SURFACE_WG, new BlockPos(posX, 0, posZ));
				if(!world.getBlockState(groundPos).getMaterial().isLiquid())
					setBlock(world, groundPos.below(), fluid);
			}
		}
		
		return true;
	}
	
	private static int lineposZ(BlockPos p1, BlockPos p2, int x)
	{
		return p1.getZ() + (int)((x - p1.getX())*((p2.getZ() - p1.getZ())/(double)(p2.getX() - p1.getX())));
	}
	
	private static boolean differentSign(int a, int b)
	{
		return a < 0 && b > 0 || a > 0 && b < 0;
	}
}