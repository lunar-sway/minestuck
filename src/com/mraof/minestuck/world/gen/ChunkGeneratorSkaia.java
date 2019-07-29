package com.mraof.minestuck.world.gen;

import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.entity.ModEntityTypes;
import com.mraof.minestuck.world.gen.structure.MapGenCastle;
import net.minecraft.block.BlockState;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.SpawnListEntry;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.*;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Mraof
 *
 */
public class ChunkGeneratorSkaia extends ChunkGenerator<SkaiaGenSettings>
{
	Random random;
	private OctavesNoiseGenerator noiseGen1;
	private OctavesNoiseGenerator noiseGen2;
	private OctavesNoiseGenerator noiseGen3;
	public OctavesNoiseGenerator noiseGen4;
	public OctavesNoiseGenerator noiseGen5;

	private MapGenCastle castleGenerator = new MapGenCastle();

	double[] noiseData1;
	double[] noiseData2;
	double[] noiseData3;
	double[] noiseData4;
	double[] noiseData5;

	List<SpawnListEntry> spawnableWhiteList;
	List<SpawnListEntry> spawnableBlackList;
	
	public ChunkGeneratorSkaia(World worldIn, BiomeProvider biomeProviderIn, SkaiaGenSettings settings)
	{
		super(worldIn, biomeProviderIn, settings);
		this.random = new Random(seed);
		this.spawnableBlackList = new ArrayList<>();
		this.spawnableWhiteList = new ArrayList<>();
		this.noiseGen1 = new OctavesNoiseGenerator(this.random, 7);
		this.noiseGen2 = new OctavesNoiseGenerator(this.random, 3);
		this.noiseGen3 = new OctavesNoiseGenerator(this.random, 8);
		this.noiseGen4 = new OctavesNoiseGenerator(this.random, 10);
		this.noiseGen5 = new OctavesNoiseGenerator(this.random, 16);
	}
	
	public void prepareHeights(int x, int z, IChunk primer)
	{
		int[] topBlock = new int[256];
		
		for(int posX = 0; posX < 16; posX++)
		{
			for(int posZ = 0; posZ < 16; posZ++)
			{
				double generated1 = this.noiseGen1.func_215460_a((posX + x*16)*0.1, (posZ + z*16)*0.1, 0.1, 0.1);
				double generated5 = this.noiseGen5.func_215460_a((posX + x*16)*0.04, (posZ + z*16)*0.04, 0.04, 0.04);
				double generated2 = this.noiseGen2.func_215460_a((posX + x*16)*0.01, (posZ + z*16)*0.01, 0.01, 0.01);
				int y = (int) (128 + generated1 + generated5 + generated2);
				topBlock[posX * 16 + posZ] = (y&511)<=255  ? y&255 : 255 - y&255;
			}
		}
		
		BlockState block;
		if((Math.abs(x) + Math.abs(z)) % 2 == 0)
			block = MinestuckBlocks.WHITE_CHESS_DIRT.getDefaultState();
		else block = MinestuckBlocks.BLACK_CHESS_DIRT.getDefaultState();
		
		BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
		for(int posX = 0; posX < 16; posX++)
			for(int posZ = 0; posZ < 16; posZ++)
				for(int posY = 0; posY <= topBlock[posX * 16 + posZ]; posY++)
				{
					pos.setPos(posX, posY, posZ);
					primer.setBlockState(pos, block, false);
				}
		 
	}
	
	@Override
	public void makeBase(IWorld world, IChunk chunkIn)
	{
		ChunkPos chunkpos = chunkIn.getPos();
		int i = chunkpos.x;
		int j = chunkpos.z;
		SharedSeedRandom sharedseedrandom = new SharedSeedRandom();
		sharedseedrandom.setBaseChunkSeed(i, j);
		Biome[] abiome = this.biomeProvider.getBiomeBlock(i * 16, j * 16, 16, 16);
		chunkIn.setBiomes(abiome);
		this.prepareHeights(i, j, chunkIn);/*
		this.buildSurface(chunkIn, abiome, sharedseedrandom, this.world.getSeaLevel());
		this.makeBedrock(chunkIn, sharedseedrandom);
		chunkIn.createHeightMap(Heightmap.Type.WORLD_SURFACE_WG, Heightmap.Type.OCEAN_FLOOR_WG);
		chunkIn.setStatus(ChunkStatus.BASE);*/
	}
	
	@Override
	public void spawnMobs(WorldGenRegion region)
	{
	}
	
	@Override
	public void generateSurface(IChunk p_222535_1_)
	{
	
	}
	
	@Override
	public int func_222529_a(int p_222529_1_, int p_222529_2_, Heightmap.Type p_222529_3_)
	{
		return 0;
	}
	
	@Override
	public int getGroundHeight()
	{
		return this.world.getSeaLevel() + 1;
	}
	
	@Override
	public SkaiaGenSettings getSettings()
	{
		return settings;
	}
}
