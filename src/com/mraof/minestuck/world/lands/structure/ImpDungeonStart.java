package com.mraof.minestuck.world.lands.structure;

import java.util.Random;

import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureStart;

public class ImpDungeonStart extends StructureStart
{
	
	public ImpDungeonStart()
	{}
	
	public ImpDungeonStart(ChunkProviderLands provider, World world, Random rand, int chunkX, int chunkZ)
	{
		super(chunkX, chunkZ);
		
		components.add(new EntryComponent(provider, chunkX, chunkZ, rand));
		updateBoundingBox();
		
		//TODO add an appropriate gate check
	}
	
	public static class EntryComponent extends StructureComponent
	{
		protected boolean definedHeight  = false;
		
		public EntryComponent()
		{}
		
		public EntryComponent(ChunkProviderLands provider, int chunkX, int chunkZ, Random rand)
		{
			int x = chunkX*16 + rand.nextInt(16);
			int z = chunkZ*16 + rand.nextInt(16);
			setCoordBaseMode(EnumFacing.Plane.HORIZONTAL.random(rand));
			int xWidth = getCoordBaseMode().getAxis().equals(EnumFacing.Axis.X) ? 11 : 6;
			int zWidth = getCoordBaseMode().getAxis().equals(EnumFacing.Axis.Z) ? 11 : 6;
			this.boundingBox = new StructureBoundingBox(x, 64, z, x + xWidth - 1, 67, z + zWidth - 1);
			
			//Add other components
		}
		
		@Override
		protected void writeStructureToNBT(NBTTagCompound tagCompound)
		{
			tagCompound.setBoolean("definedHeight", definedHeight);
		}
		
		@Override
		protected void readStructureFromNBT(NBTTagCompound tagCompound)
		{
			definedHeight = tagCompound.getBoolean("definedHeight");
		}
		
		@Override
		public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
		{
			if(!checkHeight(worldIn, boundingBox) || this.isLiquidInStructureBoundingBox(worldIn, boundingBox))
				return false;
			
			ChunkProviderLands provider = (ChunkProviderLands) worldIn.provider.createChunkGenerator();
			
			IBlockState wallBlock = provider.blockRegistry.getBlockState("structure_primary");
			IBlockState wallDecor = provider.blockRegistry.getBlockState("structure_primary_decorative");
			IBlockState floorBlock = provider.blockRegistry.getBlockState("structure_secondary");
			IBlockState floorStairs = provider.blockRegistry.getBlockState("structure_secondary_stairs").withRotation(Rotation.CLOCKWISE_180);
			
			for(int x = 1; x < 5; x++)
				buildFloorTile(floorBlock, x, 0, worldIn, randomIn, structureBoundingBoxIn);
			
			for(int z = 0; z < 5; z++)
			{
				buildFloorTile(floorBlock, 0, z, worldIn, randomIn, structureBoundingBoxIn);
				buildFloorTile(floorBlock, 5, z, worldIn, randomIn, structureBoundingBoxIn);
				buildWall(wallBlock, 0, z, worldIn, randomIn, structureBoundingBoxIn, 0);
				buildWall(wallBlock, 5, z, worldIn, randomIn, structureBoundingBoxIn, 0);
			}
			
			for(int x = 1; x < 5; x++)
				buildWall(wallBlock, x, 5, worldIn, randomIn, structureBoundingBoxIn, 0);
			if(this.getBlockStateFromPos(worldIn, 1, 2, 5, boundingBox) == wallBlock)
				this.setBlockState(worldIn, wallDecor, 1, 2, 5, boundingBox);
			if(this.getBlockStateFromPos(worldIn, 4, 2, 5, boundingBox) == wallBlock)
				this.setBlockState(worldIn, wallDecor, 4, 2, 5, boundingBox);
			
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 1, 1, 0, 4, floorBlock, floorBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 0, 1, 4, 0, 4, floorBlock, floorBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 5, 4, 0, 5, floorBlock, floorBlock, false);
			fillWithAir(worldIn, structureBoundingBoxIn, 1, 1, 0, 4, 4, 4);
			
			fillWithAir(worldIn, structureBoundingBoxIn, 2, 0, 2, 3, 0, 4);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 0, 1, 3, 0, 1, floorStairs, floorStairs, false);
			fillWithAir(worldIn, structureBoundingBoxIn, 2, -1, 3, 3, -1, 5);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, -1, 2, 3, -1, 2, floorStairs, floorStairs, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, -1, 6, 4, -1, 6, floorBlock, floorBlock, false);
			fillWithAir(worldIn, structureBoundingBoxIn, 2, -2, 4, 3, -2, 6);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, -2, 3, 3, -2, 3, floorStairs, floorStairs, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, -2, 7, 4, -2, 10, floorBlock, floorBlock, false);
			fillWithAir(worldIn, structureBoundingBoxIn, 2, -3, 5, 3, -3, 9);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, -3, 4, 3, -3, 4, floorStairs, floorStairs, false);
			fillWithAir(worldIn, structureBoundingBoxIn, 2, -4, 6, 3, -4, 9);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, -4, 5, 3, -4, 5, floorStairs, floorStairs, false);
			fillWithAir(worldIn, structureBoundingBoxIn, 2, -5, 7, 3, -5, 9);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, -5, 6, 3, -5, 6, floorStairs, floorStairs, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, -6, 6, 4, -6, 10, floorBlock, floorBlock, false);
			
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, -5, 10, 4, -3, 10, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, -5, 6, 1, -3, 9, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, -1, 2, 1, -1, 5, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, -2, 3, 1, -2, 6, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, -3, 4, 1, -3, 5, wallBlock, wallBlock, false);
			setBlockState(worldIn, wallBlock, 1, -4, 5, structureBoundingBoxIn);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 4, -5, 6, 4, -3, 9, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 4, -1, 2, 4, -1, 5, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 4, -2, 3, 4, -2, 6, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 4, -3, 4, 4, -3, 5, wallBlock, wallBlock, false);
			setBlockState(worldIn, wallBlock, 4, -4, 5, structureBoundingBoxIn);
			
			
			
			return true;
		}
		
		protected boolean checkHeight(World worldIn, StructureBoundingBox bb)
		{
			if(definedHeight)
				return true;
//			int minY = 256, maxY = 0;
			int height = 0;
			boolean onLand = false;
			int i = 0;
			
			for(int x = boundingBox.minX; x <= boundingBox.maxX; x++)
				for(int z = boundingBox.minZ; z <= boundingBox.maxZ; z++)
				{
					if(!bb.isVecInside(new BlockPos(x, 64, z)))
						continue;
					
					int y = worldIn.getTopSolidOrLiquidBlock(new BlockPos(x, 0, z)).getY();
					/*if(y < minY)
						minY = y;
					if(y > maxY)
						maxY = y;*/
					height += y;
					i++;
					if(!worldIn.getBlockState(new BlockPos(x, y, z)).getBlock().equals(Blocks.ICE))
						onLand = true;	//used to prevent the structure from spawning in an ice-covered sea without any land nearby
				}
				
			if(!onLand || i == 0/* || maxY - minY > 5*/)
				return false;
			
			height /= i;
			boundingBox.offset(0, height - boundingBox.minY, 0);
			definedHeight = true;
			return true;
		}
		
		protected void buildWall(IBlockState block, int x, int z, World world, Random rand, StructureBoundingBox boundingBox, int minY)
		{
			float f = 0.5F + z*0.2F;
			for(int y = 1; y < 4; y++)
			{
				if(y > minY && rand.nextFloat() >= f)
					return;
				
				this.setBlockState(world, block, x, y, z, boundingBox);
				
				f -= 0.5F;
			}
		}
		
		protected void buildFloorTile(IBlockState block, int x, int z, World world, Random rand, StructureBoundingBox boundingBox)
		{
			int y = 0;
			
			do
			{
				this.setBlockState(world, block, x, y, z, boundingBox);
				y--;
			} while(this.boundingBox.minY + y >= 0 && !this.getBlockStateFromPos(world, x, y, z, boundingBox).getMaterial().isSolid());
		}
	}
}