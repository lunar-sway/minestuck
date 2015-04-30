package com.mraof.minestuck.world.lands.structure;

import java.util.Random;

import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureStart;

public class SmallRuinStart extends StructureStart
{
	
	public SmallRuinStart()
	{}
	
	public SmallRuinStart(ChunkProviderLands provider, World world, Random rand, int chunkX, int chunkZ)
	{
		super(chunkX, chunkZ);
		
		components.add(new SmallRuin(provider, chunkX, chunkZ, rand));
		updateBoundingBox();
	}
	
	public static class SmallRuin extends StructureComponent
	{
		
		protected boolean hasTorches;
		
		public SmallRuin()
		{}
		
		protected SmallRuin(ChunkProviderLands provider, int chunkX, int chunkZ, Random rand)
		{
			int x = chunkX*16 + rand.nextInt(16);
			int z = chunkZ*16 + rand.nextInt(16);
			this.coordBaseMode = EnumFacing.Plane.HORIZONTAL.random(rand);
			int xWidth = coordBaseMode.getAxis().equals(EnumFacing.Axis.X) ? 10 : 7;
			int zWidth = coordBaseMode.getAxis().equals(EnumFacing.Axis.Z) ? 10 : 7;
			this.boundingBox = new StructureBoundingBox(x, 64, z, x + xWidth - 1, 67, z + zWidth - 1);
			
			float torchChance = provider.dayCycle == 0 ? 0.4F : provider.dayCycle == 1 ? 0F : 0.9F;
			
			hasTorches = rand.nextFloat() < torchChance;
			
		}
		
		@Override
		public boolean addComponentParts(World worldIn, Random rand, StructureBoundingBox boundingBox)
		{
			if(!checkHeight(worldIn) || this.isLiquidInStructureBoundingBox(worldIn, boundingBox))
				return false;
			
			
			return true;
		}
		
		protected boolean checkHeight(World worldIn)
		{
			if(coordBaseMode.getAxis().equals(EnumFacing.Axis.X))
			{
				int z = coordBaseMode.getAxisDirection().equals(EnumFacing.AxisDirection.NEGATIVE) ? boundingBox.minZ : boundingBox.maxZ;
				
				int minY = 256, maxY = 0;
				for(int x = boundingBox.minX; x <= boundingBox.maxX; x++)
				{
					
				}
				
				return true;
			} else
			{
				
				return true;
			}
		}
		
		@Override
		protected void writeStructureToNBT(NBTTagCompound nbt)
		{
			nbt.setBoolean("hasTorches", hasTorches);
		}
		
		@Override
		protected void readStructureFromNBT(NBTTagCompound nbt)
		{
			hasTorches = nbt.getBoolean("hasTorches");
		}
		
	}
}