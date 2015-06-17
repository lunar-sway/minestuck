package com.mraof.minestuck.world.gen;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.Vec3;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase.SpawnListEntry;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.NoiseGeneratorOctaves;

import com.mraof.minestuck.entity.consort.EntityIguana;
import com.mraof.minestuck.entity.consort.EntityNakagator;
import com.mraof.minestuck.entity.consort.EntitySalamander;
import com.mraof.minestuck.entity.underling.EntityBasilisk;
import com.mraof.minestuck.entity.underling.EntityGiclops;
import com.mraof.minestuck.entity.underling.EntityImp;
import com.mraof.minestuck.entity.underling.EntityOgre;
import com.mraof.minestuck.world.gen.lands.BlockWithMetadata;
import com.mraof.minestuck.world.gen.lands.ILandDecorator;
import com.mraof.minestuck.world.gen.lands.LandAspect;
import com.mraof.minestuck.world.gen.lands.LandHelper;
import com.mraof.minestuck.world.storage.MinestuckSaveHandler;

public class ChunkProviderLands implements IChunkProvider 
{
	List<SpawnListEntry> consortList;
	List<SpawnListEntry> underlingList;
	World landWorld;
	Random random;
	Vec3 skyColor;
	private NoiseGeneratorOctaves noiseGens[] = new NoiseGeneratorOctaves[2];
	private NoiseGeneratorTriangle noiseGeneratorTriangle;
	public LandAspect aspect1;
	public LandAspect aspect2;
	public LandHelper helper;
	public int nameIndex1, nameIndex2;

	public BlockWithMetadata surfaceBlock;
	public BlockWithMetadata upperBlock;
	public Block oceanBlock;
	public Block riverBlock;
	public LandAspect terrainMapper;
	public ArrayList<ILandDecorator> decorators;
	public int dayCycle;
	public int spawnX, spawnY, spawnZ;

	@SuppressWarnings("unchecked")
	public ChunkProviderLands(World worldObj, long seed, boolean b)
	{
		seed *= worldObj.provider.dimensionId;
		helper = new LandHelper(seed);
		
		NBTTagCompound landDataTag = MinestuckSaveHandler.landData.get((byte) worldObj.provider.dimensionId);
		
		this.landWorld = worldObj;
		
		if (landDataTag == null)
		{
			spawnX = landWorld.getWorldInfo().getSpawnX();
			spawnY = landWorld.getWorldInfo().getSpawnY();
			spawnZ = landWorld.getWorldInfo().getSpawnZ();
			this.aspect1 = helper.getLandAspect();
			this.aspect2 = helper.getLandAspect(aspect1);
			Random rand = new Random(seed);
			nameIndex1 = rand.nextInt(aspect1.getNames().length);
			nameIndex2 = rand.nextInt(aspect2.getNames().length);
			saveData();
		} else
		{
			aspect1 = LandHelper.fromName(landDataTag.getString("aspect1"));
			aspect2 = LandHelper.fromName(landDataTag.getString("aspect2"));
			nameIndex1 = landDataTag.getInteger("aspectName1");
			nameIndex2 = landDataTag.getInteger("aspectName2");
			spawnX = landDataTag.getInteger("spawnX");
			spawnY = landDataTag.getInteger("spawnY");
			spawnZ = landDataTag.getInteger("spawnZ");
		}
		
		this.random = new Random(seed);
		this.consortList = new ArrayList<SpawnListEntry>();
		this.underlingList = new ArrayList<SpawnListEntry>();
		this.consortList.add(new SpawnListEntry(EntityNakagator.class, 2, 1, 10));
		this.consortList.add(new SpawnListEntry(EntitySalamander.class, 2, 1, 10));
		this.consortList.add(new SpawnListEntry(EntityIguana.class, 2, 1, 10));
		this.underlingList.add(new SpawnListEntry(EntityImp.class, 6, 1, 10));
		this.underlingList.add(new SpawnListEntry(EntityOgre.class, 4, 1, 2));
		this.underlingList.add(new SpawnListEntry(EntityBasilisk.class, 3, 1, 2));
		this.underlingList.add(new SpawnListEntry(EntityGiclops.class, 1, 1, 1));
		this.underlingList.add(new SpawnListEntry(EntityBasilisk.class, 1, 1, 1));
		this.noiseGens[0] = new NoiseGeneratorOctaves(this.random, 7);
		this.noiseGens[1] = new NoiseGeneratorOctaves(this.random, 1);
		noiseGeneratorTriangle = new NoiseGeneratorTriangle(this.random);

		this.surfaceBlock = (BlockWithMetadata) helper.pickElement(helper.pickOne(aspect1, aspect2).getSurfaceBlocks());
		this.upperBlock = (BlockWithMetadata) helper.pickElement(helper.pickOne(aspect1, aspect2).getUpperBlocks());
		LandAspect fluidAspect = helper.pickOne(aspect1, aspect2);
		this.oceanBlock = fluidAspect.getOceanBlock();
		this.riverBlock = fluidAspect.getRiverBlock();
		this.terrainMapper = helper.pickOne(aspect1,aspect2);
		this.decorators = helper.pickSubset(aspect1.getDecorators(),aspect2.getDecorators());
		this.dayCycle = aspect1.getDayCycleMode() | aspect2.getDayCycleMode();
		if(this.dayCycle == 3)
			this.dayCycle = helper.pickOne(aspect1,aspect2).getDayCycleMode();
		Vec3 combinedFogColor = aspect1.getFogColor().addVector(aspect2.getFogColor().xCoord, aspect2.getFogColor().yCoord, aspect2.getFogColor().zCoord);
		this.skyColor = Vec3.createVectorHelper(combinedFogColor.xCoord/2, combinedFogColor.yCoord/2, combinedFogColor.zCoord/2);
	}

	@Override
	public boolean chunkExists(int i, int j) 
	{
		return true;
	}

	@Override
	public Chunk provideChunk(int chunkX, int chunkZ) 
	{
		Block[] chunkBlocks = new Block[65536];
		byte[] chunkMetadata = new byte[65536];
		double[] heightMap = new double[256];
		double[] heightMapTriangles = new double[256];
		double[] riverHeightMap = new double[256];
		int[] topBlock = new int[256];
		int[] topRiverBlock = new int[256];

		heightMap = this.noiseGens[0].generateNoiseOctaves(heightMap, chunkX * 16, 10, chunkZ * 16, 16, 1, 16, .1, 0, .1);
		riverHeightMap = this.noiseGens[1].generateNoiseOctaves(riverHeightMap, chunkX * 16, 1, chunkZ * 16, 16, 1, 16, .003, 0, .003);
		heightMapTriangles = noiseGeneratorTriangle.generateNoiseTriangle(heightMap, chunkX * 16, chunkZ * 16, 16, 16);

		for(int i = 0; i < 256; i++)
		{
			topRiverBlock[i] = (int) (.025 / ((5 * riverHeightMap[i]) * (5 * riverHeightMap[i]) + 0.005));
		}

		for(int i = 0; i < 256; i++)
		{
			int y = (int) (96 + heightMap[i] + heightMapTriangles[i]);
			topBlock[i] = ((y & 511) <= 255  ? y & 255 : 255 - y & 255) - topRiverBlock[i];
		}


		for(int x = 0; x < 16; x++)
			for(int z = 0; z < 16; z++)
			{
				chunkBlocks[x << 12 | z << 8] = Blocks.bedrock;
				int y;
				int yMax = topBlock[x << 4 | z] - 2;
				for(y = 1; y < yMax; y++)
				{
					//currentBlockOffset = (int) Math.abs(generated1[x + z << 8 + y * 16]);
					chunkBlocks[x << 12 | z << 8 | y] = upperBlock.block;
					chunkMetadata[x << 12 | z << 8 | y] = upperBlock.metadata;
				}

				//location copied from the chunk constructor: x * chunkBlocks.length/256 * 16 | z * blockSize/256 | y
				for(; y < yMax + 2; y++)
				{
					chunkBlocks[x << 12 | z << 8 | y] = surfaceBlock.block;
					chunkMetadata[x << 12 | z << 8 | y] = surfaceBlock.metadata;
				}

				for(int i = y + topRiverBlock[x * 16 + z]; y < i; y++)
					chunkBlocks[x << 12 | z << 8 | y] = this.riverBlock;

				for(; y < 63; y++)
					chunkBlocks[x << 12 | z << 8 | y] = this.oceanBlock;

			}
		Chunk chunk = new Chunk(this.landWorld, chunkBlocks, chunkMetadata, chunkX, chunkZ);
		return chunk;
	}

	@Override
	public Chunk loadChunk(int chunkX, int chunkZ) 
	{
		return this.provideChunk(chunkX, chunkZ);
	}

	@Override
	public void populate(IChunkProvider ichunkprovider, int i, int j) 
	{

		Chunk chunk = this.provideChunk(i, j);
		if (!chunk.isTerrainPopulated)
		{
			chunk.isTerrainPopulated = true;

			if (ichunkprovider != null)
			{
				ichunkprovider.populate(ichunkprovider, i, j);
				for (Object decorator : decorators) {
					((ILandDecorator) decorator).generate(landWorld, random, i,  j, this);
				}
				chunk.setChunkModified();
			}
		}
	}

	@Override
	public boolean saveChunks(boolean flag, IProgressUpdate iprogressupdate) {
		return true;
	}

	@Override
	public boolean unloadQueuedChunks() {
		return false;
	}

	@Override
	public boolean canSave() {
		return true;
	}

	@Override
	public String makeString() {
		return "LandRandomLevelSource";
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List getPossibleCreatures(EnumCreatureType enumcreaturetype, int i, int j, int k) 
	{
		return enumcreaturetype == EnumCreatureType.creature ? this.consortList : (enumcreaturetype == EnumCreatureType.monster ? this.underlingList : null);
	}

	@Override
	public int getLoadedChunkCount() {
		return 0;
	}

	@Override
	public void recreateStructures(int i, int j) {
	}

	@Override
	public void saveExtraData() {
	}

	@Override
	/**
	 * Redirected to in World.findClosestStructure()
	 * Only used in vanilla by ender eye when looking for a stronghold.
	 * var1: The world object; var2: The name of the structure type;
	 * var3: xCoord; var4: yCoord; var5: zCoord;
	 */
	public ChunkPosition func_147416_a(World var1, String var2, int var3, int var4, int var5) {
		return null;
	}

	public Vec3 getFogColor()
	{
		return this.skyColor;
	}
	
	public void saveData()
	{
		
		NBTTagCompound nbt = LandHelper.toNBT(aspect1,aspect2);
		nbt.setInteger("spawnX", spawnX);
		nbt.setInteger("spawnY", spawnY);
		nbt.setInteger("spawnZ", spawnZ);
		nbt.setInteger("aspectName1", nameIndex1);
		nbt.setInteger("aspectName2", nameIndex2);
		
		MinestuckSaveHandler.landData.put((byte) landWorld.provider.dimensionId, nbt);
	}
	
}
