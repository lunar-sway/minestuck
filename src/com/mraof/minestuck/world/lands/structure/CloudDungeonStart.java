/**
 * 
 */
package com.mraof.minestuck.world.lands.structure;

import java.util.List;
import java.util.Random;

import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;
import com.mraof.minestuck.world.lands.structure.CloudDungeonComponents.CloudDungeonComponent;

import net.minecraft.block.BlockTorch;
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
import net.minecraft.world.gen.structure.template.TemplateManager;

public class CloudDungeonStart extends StructureStart
{
	public CloudDungeonStart()
	{
		
	}
	
	public CloudDungeonStart(ChunkProviderLands provider, World world, Random rand, int chunkX, int chunkZ)
	{
		super(chunkX, chunkZ);
		
		components.add(new EntryComponent(provider, chunkX, chunkZ, rand, components));
		updateBoundingBox();
		
		//TODO add an appropriate gate check? I don't know what "appropriate gate check" means, but the imp dungeon has this comment
	}
	
	public static class EntryComponent extends CloudDungeonComponent
	{
		protected boolean definedHeight = false;
		protected int compoHeight;
		
		public EntryComponent()
		{
			
		}
		
		public EntryComponent(ChunkProviderLands provider, int chunkX, int chunkZ, Random rand, List<StructureComponent> componentList)
		{
			int x = chunkX*16 + rand.nextInt(16);
			int z = chunkZ*16 + rand.nextInt(16);
			setCoordBaseMode(EnumFacing.Plane.HORIZONTAL.random(rand));
			int xWidth = getCoordBaseMode().getAxis().equals(EnumFacing.Axis.X) ? 11 : 6;
			int zWidth = getCoordBaseMode().getAxis().equals(EnumFacing.Axis.Z) ? 11 : 6;
			this.boundingBox = new StructureBoundingBox(x, 64, z, x + xWidth - 1, 67, z + zWidth - 1);
			
			CloudDungeonComponents.Entrance entry = new CloudDungeonComponents.Entrance(this.getCoordBaseMode(), x, z, rand, componentList);
			componentList.add(entry);
		}
		
		@Override protected void writeStructureToNBT(NBTTagCompound tagCompound) {}
		@Override protected void readStructureFromNBT(NBTTagCompound tagCompound, TemplateManager templates) {}
		
		@Override
		public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
		{
			ChunkProviderLands provider = (ChunkProviderLands) worldIn.provider.createChunkGenerator();
			
			IBlockState wallBlock = Blocks.DIAMOND_BLOCK.getDefaultState();
			IBlockState wallDecor = Blocks.DIAMOND_BLOCK.getDefaultState();
			IBlockState floorBlock = Blocks.DIAMOND_BLOCK.getDefaultState();
			IBlockState floorDecor = Blocks.DIAMOND_BLOCK.getDefaultState();
			IBlockState fluid = provider.blockRegistry.getBlockState("fall_fluid");
			
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 4, 4, 0, 6, floorBlock, floorBlock, false);
			fillWithAir(worldIn, structureBoundingBoxIn, 1, 1, 4, 4, 4, 6);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 0, 5, 3, 0, 5, fluid, fluid, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, -1, 5, 3, -1, 5, floorDecor, floorDecor, false);
			
			fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 3, 0, 5, 7, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 0, 3, 5, 5, 7, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 4, 3, 4, 5, 3, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 4, 7, 4, 5, 7, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 3, 1, 3, 3, wallDecor, wallDecor, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 7, 1, 3, 7, wallDecor, wallDecor, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 0, 3, 4, 3, 3, wallDecor, wallDecor, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 0, 7, 4, 3, 7, wallDecor, wallDecor, false);
			
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 0, 0, 3, 0, 3, floorBlock, floorBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 0, 7, 3, 0, 9, floorBlock, floorBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 4, 0, 3, 4, 2, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 4, 8, 3, 4, 9, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 0, 1, 4, 2, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 1, 0, 4, 4, 2, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 8, 1, 4, 9, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 1, 8, 4, 4, 9, wallBlock, wallBlock, false);
			fillWithAir(worldIn, structureBoundingBoxIn, 2, 1, 0, 3, 3, 3);
			fillWithAir(worldIn, structureBoundingBoxIn, 2, 1, 7, 3, 3, 9);
			
			return true;
		}
		
		@Override
		protected boolean connectFrom(EnumFacing facing)
		{
			return false;
		}
	}
}
