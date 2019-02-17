package com.mraof.minestuck.world.lands.structure;

import com.mraof.minestuck.block.BlockGate;
import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.world.biome.BiomeMinestuck;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureStart;

import javax.annotation.Nullable;
import java.util.*;

public class MapGenLandStructure extends MapGenStructure
{
	
	public final static List<StructureEntry> genericStructures = new ArrayList<StructureEntry>();
	public final List<StructureEntry> structures = new ArrayList<StructureEntry>();
	private int totalWeight;
	
	private final ChunkProviderLands chunkProvider;
	
	public static void registerStructures()
	{
		genericStructures.add(new StructureEntry(SmallRuinStart.class, 3, BiomeMinestuck.mediumNormal));
		genericStructures.add(new StructureEntry(ImpDungeonStart.class, 2, BiomeMinestuck.mediumNormal, BiomeMinestuck.mediumRough));
		MapGenStructureIO.registerStructure(SmallRuinStart.class, "MinestuckSmallRuin");
		MapGenStructureIO.registerStructureComponent(SmallRuinStart.SmallRuin.class, "MinestuckSmallRuinCompo");
		MapGenStructureIO.registerStructure(ImpDungeonStart.class, "MinestuckImpDungeon");
		MapGenStructureIO.registerStructureComponent(ImpDungeonStart.EntryComponent.class, "MinestuckIDEntry");
		ImpDungeonComponents.registerComponents();
		
		MapGenStructureIO.registerStructure(CloudDungeonStart.class, "MinestuckCloudDungeon");
		CloudDungeonComponents.registerComponents();
	}
	
	public boolean isInsideStructure(String structureName, BlockPos pos)
	{
		if (this.world == null)
		{
			return false;
		}
		else
		{
			this.initializeStructureData(this.world);
			StructureStart structure = this.getStructureAt(pos);
			return structure != null && structureName.equals(MapGenStructureIO.getStructureStartName(structure));
		}
	}
	
	public MapGenLandStructure(ChunkProviderLands chunkProvider)
	{
		this.world = chunkProvider.landWorld;
		
		structures.addAll(genericStructures);
		this.chunkProvider = chunkProvider;
		
		chunkProvider.aspect1.modifyStructureList(structures);
	}
	
	private static final int MAX_STRUCTURE_DISTANCE = 16;
	private static final int MIN_STRUCTURE_DISTANCE = 4;
	private static final int MAX_NODE_DISTANCE = 9;
	private static final int MIN_NODE_DISTANCE = 4;
	
	@Override
	protected boolean canSpawnStructureAtCoords(int chunkX, int chunkZ)	//This works very much like the scattered features in the overworld
	{
		int x = chunkX;
		int z = chunkZ;
		
		if (x < 0)
			x -= this.MAX_STRUCTURE_DISTANCE - 1;
		if (z < 0)
			z -= this.MAX_STRUCTURE_DISTANCE - 1;
		
		x /= this.MAX_STRUCTURE_DISTANCE;
		z /= this.MAX_STRUCTURE_DISTANCE;
		Random random = this.world.setRandomSeed(x, z, 59273643^world.provider.getDimension());
		x *= this.MAX_STRUCTURE_DISTANCE;
		z *= this.MAX_STRUCTURE_DISTANCE;
		x += random.nextInt(this.MAX_STRUCTURE_DISTANCE - this.MIN_STRUCTURE_DISTANCE);
		z += random.nextInt(this.MAX_STRUCTURE_DISTANCE - this.MIN_STRUCTURE_DISTANCE);
		
		if (chunkX == x && chunkZ == z)
		{
			Random entryRand = world.setRandomSeed(chunkX , chunkZ, 34527185^world.provider.getDimension());
			Biome biome = this.world.getBiomeProvider().getBiome(new BlockPos(new BlockPos(chunkX*16 + 8, 0, chunkZ*16 + 8)));
			StructureEntry entry = getRandomEntry(entryRand);
			
			return !chunkProvider.isBBInSpawn(new StructureBoundingBox(chunkX*16 - 16, chunkZ*16 - 16, chunkX*16 + 32, chunkZ*16 + 32))	//This chunk and the chunks around it.
					&& (entry.biomes.isEmpty() || entry.biomes.contains(biome));
		}
		
		return false;
	}
	
	@Override
	public String getStructureName()
	{
		return "LandFeature";
	}
	
	@Override
	protected StructureStart getStructureStart(int chunkX, int chunkZ)
	{
		Random rand = world.setRandomSeed(chunkX , chunkZ, 34527185^world.provider.getDimension());
		
		return getRandomEntry(rand).createInstance(chunkProvider, world, rand, chunkX, chunkZ);
	}
	
	private StructureEntry getRandomEntry(Random random)
	{
		if(totalWeight == 0)
			totalWeight = WeightedRandom.getTotalWeight(structures);
		
		return WeightedRandom.getRandomItem(rand, structures, totalWeight);
	}
	
	public static class StructureEntry extends WeightedRandom.Item
	{
		public final Class<? extends StructureStart> structureStart;
		public final Set<Biome> biomes;
		
		public StructureEntry(Class<? extends StructureStart> structure, int weight, Biome... biomes)
		{
			super(weight);
			this.structureStart = structure;
			this.biomes = new HashSet<Biome>(Arrays.asList(biomes));
		}
		
		private StructureStart createInstance(ChunkProviderLands chunkProvider, World world, Random rand, int chunkX, int chunkZ)
		{
			try
			{
				return structureStart.getConstructor(ChunkProviderLands.class, World.class, Random.class, int.class, int.class).newInstance(chunkProvider, world, rand, chunkX, chunkZ);
			}
			catch(Exception e)
			{
				Debug.error("Failed to create structure for "+structureStart.getName());
				throw new IllegalStateException(e);	//The best exception I can think about right now.
			}
		}
	}
	
	public void placeReturnNodes(World world, Random rand, ChunkPos coords, BlockPos decoratorPos)
	{
		int x = coords.x;
		int z = coords.z;
		
		if (x < 0)
			x -= this.MAX_NODE_DISTANCE - 1;
		if (z < 0)
			z -= this.MAX_NODE_DISTANCE - 1;
		
		x /= this.MAX_NODE_DISTANCE;
		z /= this.MAX_NODE_DISTANCE;
		Random random = world.setRandomSeed(x, z, 32698602^world.provider.getDimension());
		x *= this.MAX_NODE_DISTANCE;
		z *= this.MAX_NODE_DISTANCE;
		x += random.nextInt(this.MAX_NODE_DISTANCE - this.MIN_NODE_DISTANCE);
		z += random.nextInt(this.MAX_NODE_DISTANCE - this.MIN_NODE_DISTANCE);
		
		if(coords.x == x && coords.z == z)
		{
			BlockPos nodePos;
			if(decoratorPos == null)
			{
				int xPos = x*16 + 8 + random.nextInt(16);
				int zPos = z*16 + 8 + random.nextInt(16);
				int maxY = 0;
				for(int i = 0; i < 4; i++)
				{
					BlockPos pos = world.getTopSolidOrLiquidBlock(new BlockPos(xPos + (i % 2), 0, zPos + i/2));
					IBlockState block = world.getBlockState(pos);
					if(block.getMaterial().isLiquid() || world.getBiomeForCoordsBody(pos) == BiomeMinestuck.mediumOcean)
						return;
					if(pos.getY() > maxY)
						maxY = pos.getY();
				}
				for(int i = 0; i < 4; i++)
				{
					BlockPos pos = new BlockPos(xPos + (i % 2), maxY, zPos + i/2);
					if(world.getBlockState(pos).getBlock().isLeaves(world.getBlockState(pos), world, pos))
						return;
				}
				nodePos = new BlockPos(xPos, maxY, zPos);
			}
			else
			{
				nodePos = decoratorPos;
				Debug.debug("Spawning special node at: "+nodePos);
				}
			
			for(int i = 0; i < 4; i++)
			{
				BlockPos pos = nodePos.add(i % 2, 0, i/2);
				if(i == 3)
				{
					world.setBlockState(pos, MinestuckBlocks.returnNode.getDefaultState().cycleProperty(BlockGate.isMainComponent), 2);
					//Do something with the tile entity?
				} else world.setBlockState(pos, MinestuckBlocks.returnNode.getDefaultState(), 2);
			}
		}
	}
	
	@Nullable
	@Override
	public BlockPos getNearestStructurePos(World worldIn, BlockPos pos, boolean findUnexplored)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	protected ChunkProviderLands getChunkProvider()
	{
		return this.chunkProvider;
	}
}
