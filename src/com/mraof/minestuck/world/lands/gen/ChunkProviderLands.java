package com.mraof.minestuck.world.lands.gen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.Vec3;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.BiomeGenBase.SpawnListEntry;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraftforge.common.ChestGenHooks;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.entity.consort.EntityIguana;
import com.mraof.minestuck.entity.consort.EntityNakagator;
import com.mraof.minestuck.entity.consort.EntitySalamander;
import com.mraof.minestuck.util.AlchemyRecipeHandler;
import com.mraof.minestuck.world.WorldProviderLands;
import com.mraof.minestuck.network.skaianet.SessionHandler;
import com.mraof.minestuck.world.biome.BiomeGenMinestuck;
import com.mraof.minestuck.world.lands.LandAspectRegistry;
import com.mraof.minestuck.world.lands.decorator.ILandDecorator;
import com.mraof.minestuck.world.lands.structure.LandStructureHandler;
import com.mraof.minestuck.world.lands.terrain.TerrainAspect;
import com.mraof.minestuck.world.lands.title.TitleAspect;

public class ChunkProviderLands implements IChunkProvider 
{
	List<SpawnListEntry> consortList;
	public List<SpawnListEntry> monsterList;
	World landWorld;
	Random random;
	Vec3 skyColor;
	long seed;
	public TerrainAspect aspect1;
	public TitleAspect aspect2;
	public LandAspectRegistry helper;
	public int nameIndex1, nameIndex2;
	public boolean nameOrder;

	public final Map<String, ChestGenHooks> lootMap = new HashMap<String, ChestGenHooks>();
	public IBlockState surfaceBlock;
	public IBlockState upperBlock;
	public IBlockState groundBlock;
	public IBlockState oceanBlock;
	public IBlockState riverBlock;
	public ArrayList<ILandDecorator> decorators;
	public ILandTerrainGen terrainGenerator;
	public LandStructureHandler structureHandler;
	public int dayCycle;
	public int weatherType;	//-1:No weather &1: Force rain &2: If thunder &4: Force thunder
	public float rainfall, temperature;
	public float oceanChance;
	protected BiomeGenBase biomeGenLands;

	@SuppressWarnings("unchecked")
	public ChunkProviderLands(World worldObj, WorldProviderLands worldProvider, boolean clientSide)
	{
		
		aspect1 = worldProvider.landAspects.aspectTerrain;
		aspect2 = worldProvider.landAspects.aspectTitle;
		
		NBTTagCompound landDataTag = (NBTTagCompound) worldObj.getWorldInfo().getAdditionalProperty("LandData");
		
		this.landWorld = worldObj;
		
		this.consortList = new ArrayList<SpawnListEntry>();
		this.monsterList = new ArrayList<SpawnListEntry>();
		this.consortList.add(new SpawnListEntry(EntityNakagator.class, 2, 1, 10));
		this.consortList.add(new SpawnListEntry(EntitySalamander.class, 2, 1, 10));
		this.consortList.add(new SpawnListEntry(EntityIguana.class, 2, 1, 10));
		
		this.dayCycle = aspect1.getDayCycleMode();
		this.skyColor = aspect1.getFogColor();
		this.weatherType = aspect1.getWeatherType();
		this.rainfall = aspect1.getRainfall();
		this.temperature = aspect1.getTemperature();
		this.oceanChance = aspect1.getOceanChance();
		
		if(!clientSide)
		{
			seed = worldObj.getSeed()*worldObj.provider.getDimensionId();
			helper = new LandAspectRegistry(seed);
			
			Random rand = new Random(seed);
			nameIndex1 = rand.nextInt(aspect1.getNames().length);
			nameIndex2 = rand.nextInt(aspect2.getNames().length);
			nameOrder = rand.nextBoolean();
			
			this.random = new Random(seed);
			this.terrainGenerator = aspect1.createTerrainGenerator(this, random);
			this.structureHandler = new LandStructureHandler(this);
			this.surfaceBlock = aspect1.getSurfaceBlock();
			this.upperBlock = aspect1.getUpperBlock();
			this.groundBlock = aspect1.getGroundBlock();
			this.oceanBlock = aspect1.getOceanBlock();
			this.riverBlock = aspect1.getRiverBlock();
			this.decorators = helper.pickSubset(aspect1.getOptionalDecorators(), 3, 5);
			this.decorators.addAll(aspect1.getRequiredDecorators());
			sortDecorators();
			
			List<WeightedRandomChestContent> list = new ArrayList<WeightedRandomChestContent>(AlchemyRecipeHandler.basicMediumChest);
			aspect1.modifyChestContent(list, AlchemyRecipeHandler.BASIC_MEDIUM_CHEST);
			lootMap.put(AlchemyRecipeHandler.BASIC_MEDIUM_CHEST, new ChestGenHooks(null, list, 0, 0));	//Item count is handled separately by the structure
		}
	}
	
	public void createBiomeGen()
	{
		biomeGenLands = new BiomeGenMinestuck(BiomeGenMinestuck.mediumNormal.biomeID, false).setTemperatureRainfall(temperature, rainfall).setBiomeName(this.landWorld.provider.getDimensionName());
		if(temperature <= 0.1)
			biomeGenLands.setEnableSnow();
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
	
	public void mergeFogColor(Vec3 fogColor, float strength)
	{
		double d1 = (this.skyColor.xCoord + fogColor.xCoord*strength)/(1 + strength);
		double d2 = (this.skyColor.yCoord + fogColor.yCoord*strength)/(1 + strength);
		double d3 = (this.skyColor.zCoord + fogColor.zCoord*strength)/(1 + strength);
		this.skyColor = new Vec3(d1, d2, d3);
	}
	
	@Override
	public boolean chunkExists(int i, int j) 
	{
		return true;
	}

	@Override
	public Chunk provideChunk(int chunkX, int chunkZ) 
	{
		ChunkPrimer primer = terrainGenerator.createChunk(chunkX, chunkZ);
		
		Chunk chunk = new Chunk(this.landWorld, primer, chunkX, chunkZ);
		chunk.generateSkylightMap();
		
		BiomeGenBase[] biomes = landWorld.getWorldChunkManager().loadBlockGeneratorData(null, chunkX * 16, chunkZ * 16, 16, 16);
		
		byte[] chunkBiomes = chunk.getBiomeArray();
		for(int i = 0; i < chunkBiomes.length; i++)
			chunkBiomes[i] = (byte) biomes[i].biomeID;
		
		structureHandler.func_175792_a(this, landWorld, chunkX, chunkZ, primer);
		return chunk;
	}
	
	@Override
	public void populate(IChunkProvider ichunkprovider, int chunkX, int chunkZ) 
	{
		
		this.random.setSeed(getSeedFor(chunkX, chunkZ));
		
		ichunkprovider.populate(ichunkprovider, chunkX, chunkZ);
		BlockPos pos = null;
		for (Object decorator : decorators)
		{
			BlockPos tempPos = ((ILandDecorator) decorator).generate(landWorld, random, chunkX,  chunkZ, this);
			if(tempPos != null)
				pos = tempPos;
		}
		
		structureHandler.func_175794_a(landWorld, random, new ChunkCoordIntPair(chunkX, chunkZ));
		structureHandler.placeReturnNodes(landWorld, random, new ChunkCoordIntPair(chunkX, chunkZ), pos);	//TODO generate land gate
		
	}
	
	public long getSeedFor(int chunkX, int chunkZ)
	{
		random.setSeed(seed);
		long i1 = random.nextLong() / 2L * 2L + 1L;
		long j1 = random.nextLong() / 2L * 2L + 1L;
		return ((long)chunkX * i1 + (long)chunkZ * j1) ^ seed;
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
		if(creatureType == EnumCreatureType.MONSTER)
		{
			List<SpawnListEntry> list = new ArrayList<SpawnListEntry>();
			list.addAll(this.monsterList);
			list.addAll(SessionHandler.getUnderlingList(pos, landWorld));
			return list;
		}
		return creatureType == EnumCreatureType.CREATURE ? this.consortList : null;
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
	
	public boolean isPositionInSpawn(int x, int z)
	{
		BlockPos spawn = this.landWorld.getSpawnPoint();
		int radiusSqrd = MinestuckConfig.artifactRange*MinestuckConfig.artifactRange;
		x -= spawn.getX();
		z -= spawn.getZ();
		
		return radiusSqrd >= x*x + z*z;
	}
	
	public boolean isBBInSpawn(StructureBoundingBox boundingBox)
	{
		BlockPos spawn = this.landWorld.getSpawnPoint();
		
		if(boundingBox.minX <= spawn.getX() + MinestuckConfig.artifactRange && boundingBox.maxX >= spawn.getX() - MinestuckConfig.artifactRange
				&& boundingBox.minZ <= spawn.getZ() + MinestuckConfig.artifactRange && boundingBox.maxZ >= spawn.getZ() - MinestuckConfig.artifactRange)
		{
			int radiusSqrd = MinestuckConfig.artifactRange*MinestuckConfig.artifactRange;
			
			if(boundingBox.minX <= spawn.getX() && boundingBox.maxX >= spawn.getX() || boundingBox.minZ <= spawn.getZ() && boundingBox.maxZ >= spawn.getZ())
				return true;
			
			int closestX = Math.min(Math.abs(boundingBox.minX - spawn.getX()), Math.abs(boundingBox.maxX - spawn.getX()));
			int closestZ = Math.min(Math.abs(boundingBox.minZ - spawn.getZ()), Math.abs(boundingBox.maxZ - spawn.getZ()));
			
			return radiusSqrd >= closestX*closestX + closestZ*closestZ;
		}
		
		return false;
	}
	
	public BiomeGenBase getBiomeGen()
	{
		return biomeGenLands;
	}
	
}
