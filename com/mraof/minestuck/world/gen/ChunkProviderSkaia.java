/**
 * 
 */
package com.mraof.minestuck.world.gen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.biome.SpawnListEntry;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.NoiseGeneratorOctaves;
import net.minecraftforge.event.terraingen.TerrainGen;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.entity.carapacian.EntityBlackBishop;
import com.mraof.minestuck.entity.carapacian.EntityBlackPawn;
import com.mraof.minestuck.entity.carapacian.EntityWhiteBishop;
import com.mraof.minestuck.entity.carapacian.EntityWhitePawn;
import com.mraof.minestuck.entity.consort.EntityConsort;
import com.mraof.minestuck.entity.underling.EntityImp;
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
    
	List spawnableWhiteList;
	List spawnableBlackList;

	public ChunkProviderSkaia(World world, long seed, boolean structures)
	{
		this.skaiaWorld = world;
        this.random = new Random(seed);
        this.spawnableBlackList = new ArrayList();
        this.spawnableWhiteList = new ArrayList();
        this.spawnableBlackList.add(new SpawnListEntry(EntityBlackPawn.class, 2, 1, 10));
        this.spawnableBlackList.add(new SpawnListEntry(EntityBlackBishop.class, 1, 1, 1));
        this.spawnableWhiteList.add(new SpawnListEntry(EntityWhitePawn.class, 2, 1, 10));
        this.spawnableWhiteList.add(new SpawnListEntry(EntityWhiteBishop.class, 1, 1, 1));
        this.noiseGen1 = new NoiseGeneratorOctaves(this.random, 7);
        this.noiseGen2 = new NoiseGeneratorOctaves(this.random, 3);
        this.noiseGen3 = new NoiseGeneratorOctaves(this.random, 8);
        this.noiseGen4 = new NoiseGeneratorOctaves(this.random, 10);
        this.noiseGen5 = new NoiseGeneratorOctaves(this.random, 16);

        NoiseGeneratorOctaves[] noiseGens = {noiseGen1, noiseGen2, noiseGen3, noiseGen4, noiseGen5};
        noiseGens = TerrainGen.getModdedNoiseGenerators(world, this.random, noiseGens);
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
		short[] chunkIds = new short[65536];
		byte[] chunkMetadata = new byte[65536];
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
		short chessTileId = (short)Minestuck.chessTile.blockID;
		for(int x = 0; x < 16; x++)
			for(int z = 0; z < 16; z++)
				for(int y = 0; y <= topBlock[x * 16 + z]; y++)
				{
					chunkIds[x + z * 16 + y * 256] = chessTileId;
					chunkMetadata[x + z * 16 + y * 256] = chessTileMetadata;
				}
		//y * 256, z * 16, x
		Chunk chunk = new Chunk(this.skaiaWorld, chunkIds, chunkMetadata, chunkX, chunkZ);
		this.castleGenerator.generate(this, skaiaWorld, chunkX, chunkZ, new byte[65536]);
		return chunk;
	}

	@Override
	public Chunk loadChunk(int var1, int var2) 
	{
		return this.provideChunk(var1, var2);
	}

	@Override
	public void populate(IChunkProvider var1, int var2, int var3) 
	{
		this.castleGenerator.generateStructuresInChunk(skaiaWorld, random, var2, var3);
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
	public List getPossibleCreatures(EnumCreatureType par1EnumCreatureType, int var2, int var3, int var4) 
	{
		return (par1EnumCreatureType == EnumCreatureType.monster || par1EnumCreatureType == EnumCreatureType.creature) ? (var2 < 0 ? this.spawnableBlackList : this.spawnableWhiteList) : null;
	}

	@Override
	public ChunkPosition findClosestStructure(World var1, String var2, int var3, int var4, int var5) 
	{
		return null;
	}

	@Override
	public int getLoadedChunkCount() {
		return 0;
	}

	@Override
	public void recreateStructures(int var1, int var2) {
		
	}
	@Override
	public boolean unloadQueuedChunks() {
		return false;
	}
	@Override
	public void func_104112_b() {
		
	}

}
