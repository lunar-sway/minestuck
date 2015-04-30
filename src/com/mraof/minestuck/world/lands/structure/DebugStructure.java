package com.mraof.minestuck.world.lands.structure;

import java.util.Random;

import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;

import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureStart;

public class DebugStructure extends StructureStart
{
	
	public DebugStructure()
	{}
	
	public DebugStructure(ChunkProviderLands provider, World world, Random rand, int chunkX, int chunkZ)
	{
		super(chunkX, chunkZ);
		
		components.add(new DebugStructureGen(chunkX, chunkZ));
		updateBoundingBox();
	}
	
	static class DebugStructureGen extends StructureComponent
	{
		
		public DebugStructureGen()
		{}
		
		public DebugStructureGen(int chunkX, int chunkZ)
		{
			coordBaseMode = EnumFacing.NORTH;
			
			boundingBox = new StructureBoundingBox(chunkX*16, 120, chunkZ*16, chunkX*16 + 16, 121, chunkZ*16 + 16);
		}
		
		@Override
		public boolean addComponentParts(World worldIn, Random rand, StructureBoundingBox bb)
		{
			func_175804_a(worldIn, bb, 0, 0, 0, 15, 0, 15, Blocks.stonebrick.getDefaultState(), Blocks.stonebrick.getDefaultState(), false);
			
			return true;
		}

		@Override
		protected void writeStructureToNBT(NBTTagCompound nbt)
		{}
		
		@Override
		protected void readStructureFromNBT(NBTTagCompound nbt)
		{}
	}
	
}
