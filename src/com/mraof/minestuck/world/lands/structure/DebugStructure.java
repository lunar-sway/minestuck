package com.mraof.minestuck.world.lands.structure;

import java.util.Random;

import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureStart;

public class DebugStructure extends StructureStart
{
	
	public DebugStructure(ChunkProviderLands provider, World world, Random rand, int chunkX, int chunkZ)
	{
		
		
	}
	
	
	
	private static class DebugStructureGen extends StructureComponent
	{
		
		@Override
		public boolean addComponentParts(World worldIn, Random rand, StructureBoundingBox bb)
		{
			
			
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
