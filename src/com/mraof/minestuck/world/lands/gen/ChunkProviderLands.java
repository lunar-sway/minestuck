package com.mraof.minestuck.world.lands.gen;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.block.BlockGate;
import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.network.skaianet.SburbHandler;
import com.mraof.minestuck.tileentity.TileEntityGate;
import com.mraof.minestuck.world.GateHandler;
import com.mraof.minestuck.world.WorldProviderLands;
import com.mraof.minestuck.world.biome.BiomeMinestuck;
import com.mraof.minestuck.world.lands.LandAspectRegistry;
import com.mraof.minestuck.world.lands.decorator.ILandDecorator;
import com.mraof.minestuck.world.lands.structure.DefaultGatePlacement;
import com.mraof.minestuck.world.lands.structure.IGateStructure;
import com.mraof.minestuck.world.lands.structure.MapGenLandStructure;
import com.mraof.minestuck.world.lands.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.lands.structure.village.MapGenConsortVillage;
import com.mraof.minestuck.world.lands.terrain.TerrainLandAspect;
import com.mraof.minestuck.world.lands.title.TitleLandAspect;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.BiomeProperties;
import net.minecraft.world.biome.Biome.SpawnListEntry;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.StructureBoundingBox;

import javax.annotation.Nullable;
import java.util.*;

public class ChunkProviderLands implements IChunkGenerator
{
	List<SpawnListEntry> consortList;
	public List<SpawnListEntry> monsterList;
	public List<SpawnListEntry> animalList;
	public List<SpawnListEntry> waterMobsList;
	public List<SpawnListEntry> ambientMobsList;
	public World landWorld;
	Random random;
	long seed;
	public TerrainLandAspect aspect1;
	public TitleLandAspect aspect2;
	public LandAspectRegistry helper;
	public int nameIndex1, nameIndex2;
	public boolean nameOrder;
	
	public final StructureBlockRegistry blockRegistry;
	public List<ILandDecorator> decorators;
	public ILandTerrainGen terrainGenerator;
	public MapGenLandStructure structureHandler;
	public MapGenConsortVillage villageHandler;
	public MapGenStructure customHandler;
	public int weatherType;	//-1:No weather &1: Force rain &2: If thunder &4: Force thunder
	public float rainfall, temperature;
	public float oceanChance;
	public float roughChance;
	protected Biome biomeLands;
	
	public boolean generatingStructure;
	public final WorldProviderLands worldProvider;

	@SuppressWarnings("unchecked")
	public ChunkProviderLands(World worldObj, WorldProviderLands worldProvider, boolean clientSide)
	{
		this.worldProvider = worldProvider;
		
		aspect1 = worldProvider.landAspects.aspectTerrain;
		aspect2 = worldProvider.landAspects.aspectTitle;

		this.landWorld = worldObj;
		
		this.consortList = new ArrayList<SpawnListEntry>();
		this.monsterList = new ArrayList<SpawnListEntry>();
		this.animalList = new ArrayList<SpawnListEntry>();
		this.ambientMobsList = new ArrayList<SpawnListEntry>();
		this.waterMobsList = new ArrayList<SpawnListEntry>();
		this.consortList.add(new SpawnListEntry(aspect1.getConsortType().getConsortClass(), 2, 1, 10));
		
		this.weatherType = aspect1.getWeatherType();
		this.rainfall = aspect1.getRainfall();
		this.temperature = aspect1.getTemperature();
		
		if(!clientSide)
		{
			seed = worldObj.getSeed()*worldObj.provider.getDimension();
			helper = new LandAspectRegistry(seed);
			
			Random rand = new Random(seed);
			nameIndex1 = rand.nextInt(aspect1.getNames().length);
			nameIndex2 = rand.nextInt(aspect2.getNames().length);
			nameOrder = rand.nextBoolean();
			
			this.oceanChance = aspect1.getOceanChance();
			this.roughChance = aspect1.getRoughChance();
			
			this.random = new Random(seed);
			blockRegistry = new StructureBlockRegistry();
			this.terrainGenerator = aspect1.createTerrainGenerator(this, random);
			this.structureHandler = new MapGenLandStructure(this);
			this.villageHandler = new MapGenConsortVillage(this);
			this.customHandler = aspect1.customMapGenStructure(this);
			aspect1.registerBlocks(blockRegistry);
			this.decorators = new ArrayList<ILandDecorator>();
			this.decorators.addAll(aspect1.getDecorators());
			sortDecorators();
		} else blockRegistry = null;
	}
	
	public void createBiomeGen()
	{
		BiomeProperties properties = new BiomeProperties(((WorldProviderLands)this.landWorld.provider).getDimensionName()).setTemperature(temperature).setRainfall(rainfall).setBaseBiome("medium");
		if(temperature <= 0.1)
			properties.setSnowEnabled();
		biomeLands = new BiomeMinestuck(properties).setRegistryName("minestuck", "medium");
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
	public Chunk generateChunk(int x, int z)
	{
		ChunkPrimer primer = terrainGenerator.createChunk(x, z);
		
		Chunk chunk = new Chunk(this.landWorld, primer, x, z);
		chunk.generateSkylightMap();
		
		Biome[] biomes = landWorld.getBiomeProvider().getBiomes(null, x * 16, z * 16, 16, 16);
		
		byte[] chunkBiomes = chunk.getBiomeArray();
		for(int i = 0; i < chunkBiomes.length; i++)
			chunkBiomes[i] = (byte) Biome.getIdForBiome(biomes[i]);
		
		structureHandler.generate(landWorld, x, z, primer);
		villageHandler.generate(landWorld, x, z, primer);
		if(customHandler != null)
			customHandler.generate(landWorld, x, z, primer);
		return chunk;
	}
	
	@Override
	public void populate(int chunkX, int chunkZ) 
	{
		ChunkPos coord = new ChunkPos(chunkX, chunkZ);
		
		BlockPos gatePos = GateHandler.getGatePos(-1, landWorld.provider.getDimension());
		
		boolean generatingGate = false;
		if(gatePos != null)
			if(gatePos.getX() >= (chunkX << 4) && gatePos.getX() < (chunkX << 4) + 32 && gatePos.getZ() >= (chunkZ << 4) && gatePos.getZ() < (chunkZ << 4) + 32)
			{
				generatingGate = true;
				this.generatingStructure = true;
			}
		
		this.random.setSeed(getSeedFor(chunkX, chunkZ));
		
		this.generatingStructure = structureHandler.generateStructure(landWorld, random, coord);
		this.generatingStructure |= villageHandler.generateStructure(landWorld, random, coord);
		if(customHandler != null)
			this.generatingStructure |= customHandler.generateStructure(landWorld, random, coord);
		
		BlockPos pos = null;
		for (Object decorator : decorators)
		{
			BlockPos tempPos = ((ILandDecorator) decorator).generate(landWorld, random, chunkX,  chunkZ, this);
			if(tempPos != null)
				pos = tempPos;
		}
		
		if(!generatingGate)
			structureHandler.placeReturnNodes(landWorld, random, coord, pos);
		else if(gatePos.getX() >= (chunkX << 4) + 8 && gatePos.getX() < (chunkX << 4) + 24 && gatePos.getZ() >= (chunkZ << 4) + 8 && gatePos.getZ() < (chunkZ << 4) + 24)
		{
			IGateStructure gate1 = aspect1.getGateStructure();
			IGateStructure gate2 = aspect2.getGateStructure();
			IGateStructure structure;
			if(gate1 != null && gate2 != null)
				structure = random.nextBoolean() ? gate1 : gate2;
			else if(gate1 != null)
				structure = gate1;
			else if(gate2 != null)
				structure = gate2;
			else structure = new DefaultGatePlacement();
			
			gatePos = structure.generateGateStructure(landWorld, gatePos, this);
			
			GateHandler.setDefiniteGatePos(-1, landWorld.provider.getDimension(), gatePos);
			for(int x = -1; x <= 1; x++)
				for(int z = -1; z <= 1; z++)
				{
					if(x == 0 && z == 0)
					{
						landWorld.setBlockState(gatePos, MinestuckBlocks.gate.getDefaultState().cycleProperty(BlockGate.isMainComponent), 2);
						TileEntityGate tileEntity = (TileEntityGate) landWorld.getTileEntity(gatePos);
						tileEntity.gateCount = -1;
					} else landWorld.setBlockState(gatePos.add(x, 0, z), MinestuckBlocks.gate.getDefaultState(), 2);
				}
		}
		
		this.generatingStructure = false;
	}
	
	@Override
	public boolean generateStructures(Chunk chunkIn, int x, int z)
	{
		return false;
	}
	
	public long getSeedFor(int chunkX, int chunkZ)
	{
		random.setSeed(seed);
		long i1 = random.nextLong() / 2L * 2L + 1L;
		long j1 = random.nextLong() / 2L * 2L + 1L;
		return ((long)chunkX * i1 + (long)chunkZ * j1) ^ seed;
	}
	
	@Override
	public List getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos)	//This was called "getPossibleCreatures" for future reference
	{
		List<SpawnListEntry> list = new ArrayList<SpawnListEntry>();
		
		switch(creatureType)
		{
		case MONSTER:
			list.addAll(this.monsterList);
			list.addAll(SburbHandler.getUnderlingList(pos, landWorld));
			break;
		case CREATURE:
			list.addAll(this.consortList);
			list.addAll(animalList);
			break;
		case AMBIENT:
			list.addAll(this.ambientMobsList);
			break;
		case WATER_CREATURE:
			list.addAll(this.waterMobsList);
			break;
		default:
			list = null;
			break;
		}
		return list;
	}
	
	@Nullable
	@Override
	public BlockPos getNearestStructurePos(World worldIn, String structureName, BlockPos position, boolean findUnexplored)
	{
		return null;	//TODO
	}
	
	@Override
	public boolean isInsideStructure(World worldIn, String structureName, BlockPos pos)
	{
		if(structureName.equals(villageHandler.getStructureName()))
			return villageHandler.isInsideStructure(pos);
		if(structureName.equals(structureHandler.getStructureName()))
			return structureHandler.isInsideStructure(pos);
		
		return structureHandler.isInsideStructure(structureName, pos);
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
	
	public Biome getBiomeGen()
	{
		return biomeLands;
	}
	
	public IBlockState getGroundBlock()
	{
		return blockRegistry.getBlockState("ground");
	}
	
	public IBlockState getUpperBlock()
	{
		return blockRegistry.getBlockState("upper");
	}
	
	public IBlockState getSurfaceBlock()
	{
		return blockRegistry.getBlockState("surface");
	}
	
	public IBlockState getOceanBlock()
	{
		return blockRegistry.getBlockState("ocean");
	}
}
