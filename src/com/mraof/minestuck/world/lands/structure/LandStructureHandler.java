package com.mraof.minestuck.world.lands.structure;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.BlockGate;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureStart;

public class LandStructureHandler extends MapGenStructure
{
	
	public final static List<StructureEntry> genericStructures = new ArrayList<StructureEntry>();
	public final List<StructureEntry> structures = new ArrayList<StructureEntry>();
	private float totalRarity;
	
	private final ChunkProviderLands chunkProvider;
	
	public static void registerStructures()
	{
		genericStructures.add(new StructureEntry(SmallRuinStart.class, 1));
		MapGenStructureIO.registerStructure(SmallRuinStart.class, "minestuckSmallRuin");
		MapGenStructureIO.registerStructureComponent(SmallRuinStart.SmallRuin.class, "minestuckSmallRuinCompo");
	}
	
	public LandStructureHandler(ChunkProviderLands chunkProvider)
	{
		structures.addAll(genericStructures);
		this.chunkProvider = chunkProvider;
		
		chunkProvider.aspect1.modifyStructureList(structures);
	}
	
	private static final int MAX_STRUCTURE_DISTANCE = 15;
	private static final int MIN_STRUCTURE_DISTANCE = 4;
	private static final int MAX_NODE_DISTANCE = 5;
	private static final int MIN_NODE_DISTANCE = 2;
	
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
		Random random = this.worldObj.setRandomSeed(x, z, 59273643^worldObj.provider.getDimensionId());
		x *= this.MAX_STRUCTURE_DISTANCE;
		z *= this.MAX_STRUCTURE_DISTANCE;
		x += random.nextInt(this.MAX_STRUCTURE_DISTANCE - this.MIN_STRUCTURE_DISTANCE);
		z += random.nextInt(this.MAX_STRUCTURE_DISTANCE - this.MIN_STRUCTURE_DISTANCE);
		
		if (chunkX == x && chunkZ == z)
			return !chunkProvider.isBBInSpawn(new StructureBoundingBox(chunkX*16 - 16, chunkZ*16 - 16, chunkX*16 + 32, chunkZ*16 + 32));	//This chunk and the chunks around it.
		
		return false;
	}
	
	@Override
	public String getStructureName()
	{
		return "Basic Land Feature";
	}
	
	@Override
	protected StructureStart getStructureStart(int chunkX, int chunkZ)
	{
		int x = chunkX;
		int z = chunkZ;
		
		if (x < 0)
			x -= this.MAX_STRUCTURE_DISTANCE - 1;
		if (z < 0)
			z -= this.MAX_STRUCTURE_DISTANCE - 1;
		
		x /= this.MAX_STRUCTURE_DISTANCE;
		z /= this.MAX_STRUCTURE_DISTANCE;
		Random rand = worldObj.setRandomSeed(x , z, 34527185^worldObj.provider.getDimensionId());
		
		if(totalRarity == 0)
			for(StructureEntry entry : structures)
				totalRarity += entry.rarity;
		
		float index = rand.nextFloat()*totalRarity;
		
		for(StructureEntry entry : structures)
		{
			if(index < entry.rarity)
				return entry.createInstance(chunkProvider, worldObj, rand, chunkX, chunkZ);
			index -= entry.rarity;
		}
		
		return null;
	}
	
	public static class StructureEntry
	{
		public final Class<? extends StructureStart> structureStart;
		public final float rarity;
		
		public StructureEntry(Class<? extends StructureStart> structure, int rarity)
		{
			this.structureStart = structure;
			this.rarity = rarity;
		}
		
		private StructureStart createInstance(ChunkProviderLands chunkProvider, World world, Random rand, int chunkX, int chunkZ)
		{
			try
			{
				return structureStart.getConstructor(ChunkProviderLands.class, World.class, Random.class, int.class, int.class).newInstance(chunkProvider, world, rand, chunkX, chunkZ);
			}
			catch(Exception e)
			{
				e.printStackTrace();
				Debug.print("Failed to create structure for "+structureStart.getName());
				return null;
			}
		}
	}
	
	public void placeReturnNodes(World world, Random rand, ChunkCoordIntPair coords)
	{
		int x = coords.chunkXPos;
		int z = coords.chunkZPos;
		
		if (x < 0)
			x -= this.MAX_NODE_DISTANCE - 1;
		if (z < 0)
			z -= this.MAX_NODE_DISTANCE - 1;
		
		x /= this.MAX_NODE_DISTANCE;
		z /= this.MAX_NODE_DISTANCE;
		Random random = world.setRandomSeed(x, z, 32698602^world.provider.getDimensionId());
		x *= this.MAX_NODE_DISTANCE;
		z *= this.MAX_NODE_DISTANCE;
		x += random.nextInt(this.MAX_NODE_DISTANCE - this.MIN_NODE_DISTANCE);
		z += random.nextInt(this.MAX_NODE_DISTANCE - this.MIN_NODE_DISTANCE);
		
		if(coords.chunkXPos == x && coords.chunkZPos == z)
		{
			int xPos = x*16 + 8 + random.nextInt(16);
			int zPos = z*16 + 8 + random.nextInt(16);
			int maxY = 0;
			for(int i = 0; i < 4; i++)
			{
				int y = world.getTopSolidOrLiquidBlock(new BlockPos(xPos + (i % 2), 0, zPos + i/2)).getY();
				Block block = world.getBlockState(new BlockPos(xPos + (i % 2), y, zPos + i/2)).getBlock();
				if(block.getMaterial().isLiquid() || block == Blocks.ice)
					return;
				if(y > maxY)
					maxY = y;
			}
			for(int i = 0; i < 4; i++)
			{
				BlockPos pos = new BlockPos(xPos + (i % 2), maxY, zPos + i/2);
				if(!world.getBlockState(pos).getBlock().isReplaceable(world, pos))
					return;
			}
			
			for(int i = 0; i < 4; i++)
			{
				BlockPos pos = new BlockPos(xPos + (i % 2), maxY, zPos + i/2);
				if(i == 3)
				{
					world.setBlockState(pos, Minestuck.returnNode.getDefaultState().cycleProperty(BlockGate.isMainComponent), 2);
					//Do something with the tile entity?
				} else world.setBlockState(pos, Minestuck.returnNode.getDefaultState(), 2);
			}
		}
	}
	
}
