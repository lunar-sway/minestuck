package com.mraof.minestuck.world.lands.structure;

import java.util.List;
import java.util.Random;

import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;

import net.minecraft.block.BlockTorch;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureStart;
import net.minecraft.world.gen.structure.template.TemplateManager;

public class ImpDungeonStart extends StructureStart
{
	
	public ImpDungeonStart()
	{}
	
	public ImpDungeonStart(ChunkProviderLands provider, World world, Random rand, int chunkX, int chunkZ)
	{
		super(chunkX, chunkZ);
		
		components.add(new EntryComponent(provider, chunkX, chunkZ, rand, components));
		updateBoundingBox();
		
		//TODO add an appropriate gate check
	}
	
	public static class EntryComponent extends StructureComponent
	{
		protected boolean definedHeight = false;
		protected int compoHeight;
		
		public EntryComponent()
		{}
		
		public EntryComponent(ChunkProviderLands provider, int chunkX, int chunkZ, Random rand, List<StructureComponent> componentList)
		{
			int x = chunkX*16 + rand.nextInt(16);
			int z = chunkZ*16 + rand.nextInt(16);
			setCoordBaseMode(EnumFacing.Plane.HORIZONTAL.random(rand));
			int xWidth = getCoordBaseMode().getAxis().equals(EnumFacing.Axis.X) ? 11 : 6;
			int zWidth = getCoordBaseMode().getAxis().equals(EnumFacing.Axis.Z) ? 11 : 6;
			this.boundingBox = new StructureBoundingBox(x, 64, z, x + xWidth - 1, 67, z + zWidth - 1);
			
			ImpDungeonComponents.EntryCorridor corridor = new ImpDungeonComponents.EntryCorridor(this.getCoordBaseMode(), x, z, rand, componentList);
			compoHeight = corridor.getBoundingBox().maxY - 1;
			componentList.add(corridor);
		}
		
		@Override
		protected void writeStructureToNBT(NBTTagCompound tagCompound)
		{
			tagCompound.setBoolean("definedHeight", definedHeight);
			tagCompound.setInteger("compoHeight", compoHeight);
		}
		
		@Override
		protected void readStructureFromNBT(NBTTagCompound tagCompound, TemplateManager templates)
		{
			definedHeight = tagCompound.getBoolean("definedHeight");
			compoHeight = tagCompound.getInteger("compoHeight");
		}
		
		@Override
		public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
		{
			checkHeight(worldIn, structureBoundingBoxIn);
			
			ChunkProviderLands provider = (ChunkProviderLands) worldIn.provider.createChunkGenerator();
			
			IBlockState wallBlock = provider.blockRegistry.getBlockState("structure_primary");
			IBlockState wallDecor = provider.blockRegistry.getBlockState("structure_primary_decorative");
			IBlockState floorBlock = provider.blockRegistry.getBlockState("structure_secondary");
			IBlockState floorStairs = provider.blockRegistry.getBlockState("structure_secondary_stairs").withRotation(Rotation.CLOCKWISE_180);
			IBlockState torch = provider.blockRegistry.getBlockState("torch");
			
			for(int x = 1; x < 5; x++)
				buildFloorTile(floorBlock, x, 0, worldIn, randomIn, structureBoundingBoxIn);
			
			for(int z = 0; z < 5; z++)
			{
				buildFloorTile(wallBlock, 0, z, worldIn, randomIn, structureBoundingBoxIn);
				buildFloorTile(wallBlock, 5, z, worldIn, randomIn, structureBoundingBoxIn);
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
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 5, 4, 0, 5, wallBlock, wallBlock, false);
			fillWithAir(worldIn, structureBoundingBoxIn, 1, 1, 0, 4, 4, 4);
			
			fillWithAir(worldIn, structureBoundingBoxIn, 2, 0, 2, 3, 0, 4);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 0, 1, 3, 0, 1, floorStairs, floorStairs, false);
			fillWithAir(worldIn, structureBoundingBoxIn, 2, -1, 3, 3, -1, 5);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, -1, 2, 3, -1, 2, floorStairs, floorStairs, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, -1, 6, 4, -1, 6, wallBlock, wallBlock, false);
			fillWithAir(worldIn, structureBoundingBoxIn, 2, -2, 4, 3, -2, 6);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, -2, 3, 3, -2, 3, floorStairs, floorStairs, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, -2, 7, 4, -2, 10, wallBlock, wallBlock, false);
			fillWithAir(worldIn, structureBoundingBoxIn, 2, -3, 5, 3, -3, 9);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, -3, 4, 3, -3, 4, floorStairs, floorStairs, false);
			fillWithAir(worldIn, structureBoundingBoxIn, 2, -4, 6, 3, -4, 9);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, -4, 5, 3, -4, 5, floorStairs, floorStairs, false);
			fillWithAir(worldIn, structureBoundingBoxIn, 2, -5, 7, 3, -5, 9);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, -5, 6, 3, -5, 6, floorStairs, floorStairs, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, -6, 6, 4, -6, 8, floorBlock, floorBlock, false);
			
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, compoHeight - boundingBox.minY, 10, 4, -3, 10, wallBlock, wallBlock, false);
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
			fillWithAir(worldIn, structureBoundingBoxIn, 2, compoHeight - boundingBox.minY, 9, 3, -6, 9);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, compoHeight - boundingBox.minY, 9, 1, -6, 9, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 4, compoHeight - boundingBox.minY, 9, 4, -6, 9, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, compoHeight - boundingBox.minY, 8, 4, -7, 8, wallBlock, wallBlock, false);
			
			setBlockState(worldIn, torch.withProperty(BlockTorch.FACING, EnumFacing.EAST), 2, -3, 8, structureBoundingBoxIn);
			setBlockState(worldIn, torch.withProperty(BlockTorch.FACING, EnumFacing.WEST), 3, -3, 8, structureBoundingBoxIn);
			
			return true;
		}
		
		protected void checkHeight(World worldIn, StructureBoundingBox bb)
		{
			if(definedHeight)
				return;
			int height = 0;
			int i = 0;
			
			for(int x = boundingBox.minX; x <= boundingBox.maxX; x++)
				for(int z = boundingBox.minZ; z <= boundingBox.maxZ; z++)
				{
					if(!bb.isVecInside(new BlockPos(x, 64, z)))
						continue;
					
					int y = Math.max(62, worldIn.getTopSolidOrLiquidBlock(new BlockPos(x, 0, z)).getY());
					height += y;
					i++;
				}
			
			height /= i;
			boundingBox.offset(0, height - boundingBox.minY, 0);
			definedHeight = true;
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