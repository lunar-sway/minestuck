package com.mraof.minestuck.world.lands.structure;

import java.util.List;
import java.util.Random;

import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;

public class ImpDungeonComponents
{
	
	public static class EntryCorridor extends StructureComponent
	{
		
		public EntryCorridor()
		{}
		
		public EntryCorridor(EnumFacing coordBaseMode, int posX, int posZ, Random rand, List<StructureComponent> componentList)
		{
			setCoordBaseMode(coordBaseMode);
			
			int xWidth = getCoordBaseMode().getAxis().equals(EnumFacing.Axis.X) ? 8 : 6;
			int zWidth = getCoordBaseMode().getAxis().equals(EnumFacing.Axis.Z) ? 8 : 6;
			
			int height = 50 - rand.nextInt(8);
			int offset = getCoordBaseMode().getAxisDirection().equals(EnumFacing.AxisDirection.POSITIVE) ? 5 : -2;
			int x = posX + (getCoordBaseMode().getAxis().equals(EnumFacing.Axis.Z) ? 0 : offset);
			int z = posZ + (getCoordBaseMode().getAxis().equals(EnumFacing.Axis.X) ? 0 : offset);
			
			this.boundingBox = new StructureBoundingBox(x, height, z, x + xWidth - 1, height + 5, z + zWidth - 1);
		}
		
		@Override
		protected void writeStructureToNBT(NBTTagCompound tagCompound)
		{}
		
		@Override
		protected void readStructureFromNBT(NBTTagCompound tagCompound)
		{}
		
		@Override
		public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
		{
			
			ChunkProviderLands provider = (ChunkProviderLands) worldIn.provider.createChunkGenerator();
			
			IBlockState wallBlock = provider.blockRegistry.getBlockState("structure_primary");
			IBlockState wallDecor = provider.blockRegistry.getBlockState("structure_primary_decorative");
			IBlockState floorBlock = provider.blockRegistry.getBlockState("structure_secondary");
			IBlockState floorDecor = provider.blockRegistry.getBlockState("structure_secondary_decorative");
			IBlockState fluid = provider.blockRegistry.getBlockState("fall_fluid");
			
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 3, 4, 0, 5, floorBlock, floorBlock, false);
			fillWithAir(worldIn, structureBoundingBoxIn, 1, 1, 3, 4, 4, 5);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 0, 4, 3, 0, 4, fluid, fluid, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, -1, 4, 3, -1, 4, floorDecor, floorDecor, false);
			
			fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 2, 0, 5, 6, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 0, 2, 5, 5, 6, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 4, 2, 4, 5, 2, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 4, 6, 4, 5, 6, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 2, 1, 3, 2, wallDecor, wallDecor, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 6, 1, 3, 6, wallDecor, wallDecor, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 0, 2, 4, 3, 2, wallDecor, wallDecor, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 0, 6, 4, 3, 6, wallDecor, wallDecor, false);
			
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 0, 0, 3, 0, 2, floorBlock, floorBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 0, 6, 3, 0, 7, floorBlock, floorBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 4, 0, 3, 4, 1, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 4, 7, 3, 4, 7, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 0, 1, 4, 1, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 1, 0, 4, 4, 1, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 7, 1, 4, 7, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 1, 7, 4, 4, 7, wallBlock, wallBlock, false);
			fillWithAir(worldIn, structureBoundingBoxIn, 2, 1, 0, 3, 3, 2);
			fillWithAir(worldIn, structureBoundingBoxIn, 2, 1, 6, 3, 3, 7);
			
			return true;
		}
	}
	
	
}