package com.mraof.minestuck.world.gen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase.SpawnListEntry;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.NoiseGeneratorOctaves;

import com.mraof.minestuck.entity.consort.EntityIguana;
import com.mraof.minestuck.entity.consort.EntityNakagator;
import com.mraof.minestuck.entity.consort.EntitySalamander;
import com.mraof.minestuck.entity.underling.EntityBasilisk;
import com.mraof.minestuck.entity.underling.EntityGiclops;
import com.mraof.minestuck.entity.underling.EntityImp;
import com.mraof.minestuck.entity.underling.EntityOgre;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.world.WorldProviderLands;
import com.mraof.minestuck.network.skaianet.SessionHandler;
import com.mraof.minestuck.world.gen.lands.LandAspectRegistry;
import com.mraof.minestuck.world.gen.lands.decorator.ILandDecorator;
import com.mraof.minestuck.world.gen.lands.terrain.TerrainAspect;
import com.mraof.minestuck.world.gen.lands.title.TitleAspect;

public class ChunkProviderLands implements IChunkProvider 
{
	List<SpawnListEntry> consortList;
	List<SpawnListEntry> underlingList;
	World landWorld;
	Random random;
	Vec3 skyColor;
	long seed;
	private NoiseGeneratorOctaves noiseGens[] = new NoiseGeneratorOctaves[2];
	private NoiseGeneratorTriangle noiseGeneratorTriangle;
	public TerrainAspect aspect1;
	public TitleAspect aspect2;
	public LandAspectRegistry helper;
	public int nameIndex1, nameIndex2;

	public IBlockState surfaceBlock;
	public IBlockState upperBlock;
	public Block oceanBlock;
	public Block riverBlock;
	public TerrainAspect terrainMapper;
	public ArrayList<ILandDecorator> decorators;
	public int dayCycle;
	public int weatherType;	//-1:No weather &1: rainy or snowy &2:If thunder &4:If neverending
	public int spawnX, spawnY, spawnZ;

	@SuppressWarnings("unchecked")
	public ChunkProviderLands(World worldObj, WorldProviderLands worldProvider, long seed)
	{
		seed *= worldObj.provider.getDimensionId();
		this.seed = seed;
		helper = new LandAspectRegistry(seed);
		aspect1 = worldProvider.landAspect.aspect1;
		aspect2 = worldProvider.landAspect.aspect2;
		
		NBTTagCompound landDataTag = (NBTTagCompound) worldObj.getWorldInfo().getAdditionalProperty("LandData");
		
		this.landWorld = worldObj;
		
		if (landDataTag == null)
		{
			spawnX = landWorld.getWorldInfo().getSpawnX();
			spawnY = landWorld.getWorldInfo().getSpawnY();
			spawnZ = landWorld.getWorldInfo().getSpawnZ();
			Random rand = new Random(seed);
			nameIndex1 = rand.nextInt(aspect1.getNames().length);	//Better way to generate these?
			nameIndex2 = rand.nextInt(aspect2.getNames().length);
			saveData();

		}
		else
		{
			spawnX = landDataTag.getInteger("spawnX");
			spawnY = landDataTag.getInteger("spawnY");
			spawnZ = landDataTag.getInteger("spawnZ");
			nameIndex1 = landDataTag.getInteger("aspectName1");
			nameIndex2 = landDataTag.getInteger("aspectName2");
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
		
		this.surfaceBlock = (IBlockState) helper.pickElement(aspect1.getSurfaceBlocks());
		this.upperBlock = (IBlockState) helper.pickElement(aspect1.getUpperBlocks());
		TerrainAspect fluidAspect = aspect1;
		this.oceanBlock = fluidAspect.getOceanBlock();
		this.riverBlock = fluidAspect.getRiverBlock();
		this.terrainMapper = aspect1;
		this.decorators = helper.pickSubset(aspect1.getOptionalDecorators(), 3, 5);
		this.decorators.addAll(aspect1.getRequiredDecorators());
		sortDecorators();
		this.dayCycle = aspect1.getDayCycleMode();
		this.skyColor = aspect1.getFogColor();
		this.weatherType = aspect1.getWeatherType();
	}
	
	public void sortDecorators()	//Called after an aspect have added elements to the decorators list.
	{
		Collections.sort(decorators, new Comparator<ILandDecorator>() {
			@Override
			public int compare(ILandDecorator o1, ILandDecorator o2)
			{
				return Float.compare(o1.getPriority(), o2.getPriority());
			}});
	}
	
	@Override
	public boolean chunkExists(int i, int j) 
	{
		return true;
	}

	@Override
	public Chunk provideChunk(int chunkX, int chunkZ) 
	{
		ChunkPrimer primer = new ChunkPrimer();
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
				primer.setBlockState(x, 0, z, Blocks.bedrock.getDefaultState());
				int y;
				int yMax = topBlock[x << 4 | z] - 2 - topRiverBlock[x << 4 | z];
				for(y = 1; y < yMax; y++)
				{
					//currentBlockOffset = (int) Math.abs(generated1[x + z << 8 + y * 16]);
					primer.setBlockState(x, y, z, upperBlock);
				}

				//location copied from the chunk constructor: x * chunkBlocks.length/256 * 16 | z * blockSize/256 | y
				for(; y < yMax + 2; y++)
				{
					primer.setBlockState(x, y, z, surfaceBlock);
				}

				for(int i = y + topRiverBlock[x << 4 | z]; y < i; y++)
					primer.setBlockState(x, y, z, riverBlock.getDefaultState());

				for(; y < 63; y++)
					primer.setBlockState(x, y, z, oceanBlock.getDefaultState());

			}
		
		Chunk chunk = new Chunk(this.landWorld, primer, chunkX, chunkZ);
		chunk.generateSkylightMap();
		return chunk;
	}
	
	@Override
	public void populate(IChunkProvider ichunkprovider, int i, int j) 
	{
		
		random.setSeed(seed);
		long i1 = random.nextLong() / 2L * 2L + 1L;
		long j1 = random.nextLong() / 2L * 2L + 1L;
		this.random.setSeed((long)i * i1 + (long)j * j1 ^ seed);
		
		Chunk chunk = this.provideChunk(i, j);
		if (!chunk.isTerrainPopulated())
		{
			chunk.setTerrainPopulated(true);

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
	
	@Override
	public int getLoadedChunkCount() {
		return 0;
	}
	
	@Override
	public void saveExtraData() {
	}
	
	public Vec3 getFogColor()
	{
		return this.skyColor;
	}
	
	public void saveData()
	{
		
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setInteger("spawnX", spawnX);
		nbt.setInteger("spawnY", spawnY);
		nbt.setInteger("spawnZ", spawnZ);
		nbt.setInteger("aspectName1", nameIndex1);
		nbt.setInteger("aspectName2", nameIndex2);
		
		Map<String, NBTBase> dataTag = new Hashtable<String,NBTBase>();
		dataTag.put("landData", nbt);
		landWorld.getWorldInfo().setAdditionalProperties(dataTag);
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
	
	@Override
	public List func_177458_a(EnumCreatureType creatureType, BlockPos pos)	//This was called "getPossibleCreatures" for future reference
	{
		return creatureType == EnumCreatureType.CREATURE ? this.consortList : (creatureType == EnumCreatureType.MONSTER ? SessionHandler.getUnderlingList(pos, landWorld)/*this.underlingList*/ : null);
	}
	
	/**
	 * Redirected to in World.findClosestStructure()
	 * Only used in vanilla by ender eye when looking for a stronghold.
	 * var1: The world object; var2: The name of the structure type;
	 * var3: The locators position;
	 */
	@Override
	public BlockPos getStrongholdGen(World worldIn, String structureName, BlockPos pos)
	{
		return null;
	}

	@Override
	public void recreateStructures(Chunk chunk, int p_180514_2_, int p_180514_3_) {}
	
}
