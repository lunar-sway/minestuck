package com.mraof.minestuck.world.gen;

import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.entity.ModEntityTypes;
import com.mraof.minestuck.world.gen.structure.MapGenCastle;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome.SpawnListEntry;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.NoiseGeneratorOctaves;
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
public class ChunkProviderSkaia //implements IChunkGenerator
{
	World skaiaWorld;
	Random random;
	private NoiseGeneratorOctaves noiseGen1;
	private NoiseGeneratorOctaves noiseGen2;
	private NoiseGeneratorOctaves noiseGen3;
	public NoiseGeneratorOctaves noiseGen4;
	public NoiseGeneratorOctaves noiseGen5;

	private MapGenCastle castleGenerator = new MapGenCastle();

	double[] noiseData1;
	double[] noiseData2;
	double[] noiseData3;
	double[] noiseData4;
	double[] noiseData5;

	List<SpawnListEntry> spawnableWhiteList;
	List<SpawnListEntry> spawnableBlackList;

	public ChunkProviderSkaia(World world, long seed, boolean structures)
	{
		this.skaiaWorld = world;
		this.random = new Random(seed);
		this.spawnableBlackList = new ArrayList<>();
		this.spawnableWhiteList = new ArrayList<>();
		this.spawnableBlackList.add(new SpawnListEntry(ModEntityTypes.DERSITE_PAWN, 2, 1, 10));
		this.spawnableBlackList.add(new SpawnListEntry(ModEntityTypes.DERSITE_BISHOP, 1, 1, 1));
		this.spawnableBlackList.add(new SpawnListEntry(ModEntityTypes.DERSITE_ROOK, 1, 1, 1));
		this.spawnableWhiteList.add(new SpawnListEntry(ModEntityTypes.PROSPITIAN_PAWN, 2, 1, 10));
		this.spawnableWhiteList.add(new SpawnListEntry(ModEntityTypes.PROSPITIAN_BISHOP, 1, 1, 1));
		this.spawnableWhiteList.add(new SpawnListEntry(ModEntityTypes.PROSPITIAN_ROOK, 1, 1, 1));
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
	
	/*
	@Override
	public Chunk generateChunk(int x, int z)
	{
		ChunkPrimer primer = new ChunkPrimer();
		double[] generated0 = new double[256];
		double[] generated1 = new double[256];
		double[] generated2 = new double[256];
		int[] topBlock = new int[256];

		generated0 = this.noiseGen1.generateNoiseOctaves(generated0, x*16, 10, z*16, 16, 1, 16, .1, 0, .1);
		generated1 = this.noiseGen5.generateNoiseOctaves(generated1, x*16, 10, z*16, 16, 1, 16, .04, 0, .04);
		generated2 = this.noiseGen2.generateNoiseOctaves(generated2, x*16, 10, z*16, 16, 1, 16, .01, 0, .01);
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
		//y * 256, z * 16, x
		Chunk chunk = new Chunk(this.skaiaWorld, primer, x, z);
//		this.castleGenerator.generate(skaiaWorld, x, z, primer);
		chunk.generateSkylightMap();
		return chunk;
	}
	
	@Override
	public void populate(int var2, int var3) 
	{
//		this.castleGenerator.generateStructure(skaiaWorld, random, new ChunkPos(var2, var3));	//was called "generateStructuresInChunk"
	}
	
	@Override
	public boolean generateStructures(Chunk chunkIn, int x, int z)
	{
		return false;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public List getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos)
	{
		return (creatureType == EnumCreatureType.MONSTER || creatureType == EnumCreatureType.CREATURE) ? (pos.getX() < 0 ? this.spawnableBlackList : this.spawnableWhiteList) : null;
	}
	
	@Nullable
	@Override
	public BlockPos getNearestStructurePos(World worldIn, String structureName, BlockPos position, boolean findUnexplored)
	{
		return null;
	}
	
	@Override
	public boolean isInsideStructure(World worldIn, String structureName, BlockPos pos)
	{
		return false;
	}
	
	@Override
	public void recreateStructures(Chunk chunk, int p_180514_2_, int p_180514_3_) {}
	*/
}
