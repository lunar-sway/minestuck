package com.mraof.minestuck.world.gen;

import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.entity.ModEntityTypes;
import com.mraof.minestuck.world.gen.structure.MapGenCastle;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureType;
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
import net.minecraftforge.event.terraingen.InitNoiseGensEvent;
import net.minecraftforge.event.terraingen.TerrainGen;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Mraof
 *
 */
public class ChunkGeneratorSkaia extends AbstractChunkGenerator<SkaiaGenSettings>
{
	Random random;
	private NoiseGeneratorOctaves noiseGen1;
	private NoiseGeneratorOctaves noiseGen2;
	private NoiseGeneratorOctaves noiseGen3;
	public NoiseGeneratorOctaves noiseGen4;
	public NoiseGeneratorOctaves noiseGen5;
	
	private final SkaiaGenSettings settings;

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
		super(worldIn, biomeProviderIn);
		this.settings = settings;
		this.random = new Random(seed);
		this.spawnableBlackList = new ArrayList<>();
		this.spawnableWhiteList = new ArrayList<>();
		this.noiseGen1 = new NoiseGeneratorOctaves(this.random, 7);
		this.noiseGen2 = new NoiseGeneratorOctaves(this.random, 3);
		this.noiseGen3 = new NoiseGeneratorOctaves(this.random, 8);
		this.noiseGen4 = new NoiseGeneratorOctaves(this.random, 10);
		this.noiseGen5 = new NoiseGeneratorOctaves(this.random, 16);

		InitNoiseGensEvent.Context noiseGens = TerrainGen.getModdedNoiseGenerators(world, this.random, new InitNoiseGensEvent.Context(noiseGen1, noiseGen2, noiseGen3, noiseGen4, noiseGen5));
		this.noiseGen1 = noiseGens.getLPerlin1();
		this.noiseGen2 = noiseGens.getLPerlin2();
		this.noiseGen3 = noiseGens.getPerlin();
		this.noiseGen4 = noiseGens.getScale();
		this.noiseGen5 = noiseGens.getDepth();
		
	}
	
	public void prepareHeights(int x, int z, IChunk primer)
	{
		int[] topBlock = new int[256];
		
		double[] generated0 = this.noiseGen1.func_202647_a(x*16, 10, z*16, 16, 1, 16, .1, 0, .1);
		double[] generated1 = this.noiseGen5.func_202647_a(x*16, 10, z*16, 16, 1, 16, .04, 0, .04);
		double[] generated2 = this.noiseGen2.func_202647_a(x*16, 10, z*16, 16, 1, 16, .01, 0, .01);
		for(int i = 0; i < 256; i++)
		{
			int y = (int)(128 + generated0[i] + generated1[i] + generated2[i]);
			topBlock[i] = (y&511)<=255  ? y&255 : 255 - y&255;
		}
		
		IBlockState block;
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
	public double[] generateNoiseRegion(int x, int z)
	{
		return new double[256];
	}
	
	@Override
	public void makeBase(IChunk chunkIn)
	{
		ChunkPos chunkpos = chunkIn.getPos();
		int i = chunkpos.x;
		int j = chunkpos.z;
		SharedSeedRandom sharedseedrandom = new SharedSeedRandom();
		sharedseedrandom.setBaseChunkSeed(i, j);
		Biome[] abiome = this.biomeProvider.getBiomeBlock(i * 16, j * 16, 16, 16);
		chunkIn.setBiomes(abiome);
		this.prepareHeights(i, j, chunkIn);
		this.buildSurface(chunkIn, abiome, sharedseedrandom, this.world.getSeaLevel());
		this.makeBedrock(chunkIn, sharedseedrandom);
		chunkIn.createHeightMap(Heightmap.Type.WORLD_SURFACE_WG, Heightmap.Type.OCEAN_FLOOR_WG);
		chunkIn.setStatus(ChunkStatus.BASE);
	}
	
	@Override
	public void spawnMobs(WorldGenRegion region)
	{
	}
	
	@Override
	public List<SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos)
	{
		return this.world.getBiome(pos).getSpawns(creatureType);
	}
	
	@Override
	public int spawnMobs(World worldIn, boolean spawnHostileMobs, boolean spawnPeacefulMobs)
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
