/**
 * 
 */
package com.mraof.minestuck.world.gen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase.SpawnListEntry;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.NoiseGeneratorOctaves;
import net.minecraftforge.event.terraingen.TerrainGen;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.BlockChessTile;
import com.mraof.minestuck.entity.carapacian.EntityBlackBishop;
import com.mraof.minestuck.entity.carapacian.EntityBlackPawn;
import com.mraof.minestuck.entity.carapacian.EntityBlackRook;
import com.mraof.minestuck.entity.carapacian.EntityWhiteBishop;
import com.mraof.minestuck.entity.carapacian.EntityWhitePawn;
import com.mraof.minestuck.entity.carapacian.EntityWhiteRook;
import com.mraof.minestuck.world.gen.structure.MapGenCastle;

/**
 * @author Mraof
 *
 */
public class ChunkProviderSkaia implements IChunkProvider
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
		this.spawnableBlackList = new ArrayList<SpawnListEntry>();
		this.spawnableWhiteList = new ArrayList<SpawnListEntry>();
		this.spawnableBlackList.add(new SpawnListEntry(EntityBlackPawn.class, 2, 1, 10));
		this.spawnableBlackList.add(new SpawnListEntry(EntityBlackBishop.class, 1, 1, 1));
		this.spawnableBlackList.add(new SpawnListEntry(EntityBlackRook.class, 1, 1, 1));
		this.spawnableWhiteList.add(new SpawnListEntry(EntityWhitePawn.class, 2, 1, 10));
		this.spawnableWhiteList.add(new SpawnListEntry(EntityWhiteBishop.class, 1, 1, 1));
		this.spawnableWhiteList.add(new SpawnListEntry(EntityWhiteRook.class, 1, 1, 1));
		this.noiseGen1 = new NoiseGeneratorOctaves(this.random, 7);
		this.noiseGen2 = new NoiseGeneratorOctaves(this.random, 3);
		this.noiseGen3 = new NoiseGeneratorOctaves(this.random, 8);
		this.noiseGen4 = new NoiseGeneratorOctaves(this.random, 10);
		this.noiseGen5 = new NoiseGeneratorOctaves(this.random, 16);

		NoiseGeneratorOctaves[] noiseGens = {noiseGen1, noiseGen2, noiseGen3, noiseGen4, noiseGen5};
		noiseGens = (NoiseGeneratorOctaves[]) TerrainGen.getModdedNoiseGenerators(world, this.random, noiseGens);
		this.noiseGen1 = noiseGens[0];
		this.noiseGen2 = noiseGens[1];
		this.noiseGen3 = noiseGens[2];
		this.noiseGen4 = noiseGens[3];
		this.noiseGen5 = noiseGens[4];

	}
	@Override
	public boolean chunkExists(int var1, int var2) {
		return true;
	}

	@Override
	public Chunk provideChunk(int chunkX, int chunkZ) 
	{
		ChunkPrimer primer = new ChunkPrimer();
		double[] generated0 = new double[256];
		double[] generated1 = new double[256];
		double[] generated2 = new double[256];
		int[] topBlock = new int[256];

		generated0 = this.noiseGen1.generateNoiseOctaves(generated0, chunkX*16, 10, chunkZ*16, 16, 1, 16, .1, 0, .1);
		generated1 = this.noiseGen5.generateNoiseOctaves(generated1, chunkX*16, 10, chunkZ*16, 16, 1, 16, .04, 0, .04);
		generated2 = this.noiseGen2.generateNoiseOctaves(generated2, chunkX*16, 10, chunkZ*16, 16, 1, 16, .01, 0, .01);
		for(int i = 0; i < 256; i++)
		{
			int y = (int)(128 + generated0[i] + generated1[i] + generated2[i]);
			topBlock[i] = (y&511)<=255  ? y&255 : 255 - y&255;
		}
		byte chessTileMetadata = (byte) ((Math.abs(chunkX) + Math.abs(chunkZ)) % 2);
		IBlockState block = Minestuck.chessTile.getDefaultState().withProperty(BlockChessTile.BLOCK_TYPE, BlockChessTile.BlockType.values()[chessTileMetadata]);
		for(int x = 0; x < 16; x++)
			for(int z = 0; z < 16; z++)
				for(int y = 0; y <= topBlock[x * 16 + z]; y++)
				{
					primer.setBlockState(x, y, z, block);
				}
		//y * 256, z * 16, x
		Chunk chunk = new Chunk(this.skaiaWorld, primer, chunkX, chunkZ);
//		this.castleGenerator.func_175792_a(this, skaiaWorld, chunkX, chunkZ, primer);
		chunk.generateSkylightMap();
		return chunk;
	}
	
	@Override
	public void populate(IChunkProvider var1, int var2, int var3) 
	{
//		this.castleGenerator.func_175794_a(skaiaWorld, random, new ChunkCoordIntPair(var2, var3));	//was called "generateStructuresInChunk"
	}
	
	@Override
	public boolean saveChunks(boolean var1, IProgressUpdate var2) {
		return true;
	}

	@Override
	public boolean canSave() {
		return true;
	}

	@Override
	public String makeString() {
		return "SkaiaRandomLevelSource";
	}
	
	@Override
	public int getLoadedChunkCount() {
		return 0;
	}
	
	@Override
	public boolean unloadQueuedChunks() {
		return false;
	}
	
	@Override
	public void saveExtraData() 
	{
	}
	
	@Override
	public Chunk provideChunk(BlockPos pos)
	{
		return provideChunk(pos.getX() >> 4, pos.getZ() >> 4);
	}
	
	@Override
	public boolean func_177460_a(IChunkProvider p_177460_1_, Chunk p_177460_2_,
			int p_177460_3_, int p_177460_4_) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public List getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos)
	{
		return (creatureType == EnumCreatureType.MONSTER || creatureType == EnumCreatureType.CREATURE) ? (pos.getX() < 0 ? this.spawnableBlackList : this.spawnableWhiteList) : null;
	}
	
	@Override
	public BlockPos getStrongholdGen(World worldIn, String p_180513_2_, BlockPos p_180513_3_)
	{
		return null;
	}
	
	@Override
	public void recreateStructures(Chunk chunk, int p_180514_2_, int p_180514_3_) {}
	
}
