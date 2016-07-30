package com.mraof.minestuck.world.lands.structure;

import java.util.List;
import java.util.Random;

import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;

public class ImpDungeonComponents
{
	
	public static class EntryCorridor extends StructureComponent
	{
		
		boolean[] corridors = new boolean[2];
		
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
			
			this.boundingBox = new StructureBoundingBox(x, height, z, x + xWidth - 1, height + 6, z + zWidth - 1);
			
			BlockPos compoPos = new BlockPos(x + (xWidth/2 - 1), height, z + (zWidth/2 - 1));
			
			boolean[][] compoGen = new boolean[13][13];
			compoGen[6][6] = true;
			
			int xOffset = coordBaseMode.getFrontOffsetX();
			int zOffset = coordBaseMode.getFrontOffsetZ();
			corridors[0] = !generatePart(compoGen, 6 + xOffset, 6 + zOffset, compoPos.add(xOffset*8, 0, zOffset*8), coordBaseMode, rand, componentList, 0);
			corridors[1] = !generatePart(compoGen, 6 - xOffset, 6 - zOffset, compoPos.add(-xOffset*8, 0, -zOffset*8), coordBaseMode.getOpposite(), rand, componentList, 0);
		}
		
		@Override
		protected void writeStructureToNBT(NBTTagCompound tagCompound)
		{
			for(int i = 0; i < corridors.length; i++)
				tagCompound.setBoolean("blocked"+i, corridors[i]);
		}
		
		@Override
		protected void readStructureFromNBT(NBTTagCompound tagCompound)
		{
			for(int i = 0; i < corridors.length; i++)
				corridors[i] = tagCompound.getBoolean("blocked"+i);
		}
		
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
			
			if(corridors[0])
				fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 1, 7, 3, 3, 7, wallBlock, wallBlock, false);
			if(corridors[1])
				fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 1, 0, 3, 3, 0, wallBlock, wallBlock, false);
			
			return true;
		}
	}
	
	public static boolean generatePart(boolean[][] compoGen, int xIndex, int zIndex, BlockPos pos, EnumFacing facing, Random rand, List<StructureComponent> compoList, int index)
	{
		if(xIndex >= compoGen.length || zIndex >= compoGen[0].length
				|| xIndex < 0 || zIndex < 0 || compoGen[xIndex][zIndex])
			return false;
		
		if(rand.nextGaussian() >= (1 - index*0.2))
			return false;
		
		compoGen[xIndex][zIndex] = true;
		
		StructureComponent component;
		
		{
			component = new StraightCorridor(facing, pos, rand, compoGen, xIndex, zIndex, index, compoList);
		}
		
		compoList.add(component);
		
		return true;
	}
	
	public static class StraightCorridor extends StructureComponent
	{
		boolean[] corridors = new boolean[1];
		
		public StraightCorridor()
		{}
		
		public StraightCorridor(EnumFacing coordBaseMode, BlockPos pos, Random rand, boolean[][] compoGen, int xIndex, int zIndex, int index, List<StructureComponent> componentList)
		{
			setCoordBaseMode(coordBaseMode);
			
			int xWidth = getCoordBaseMode().getAxis().equals(EnumFacing.Axis.X) ? 8 : 4;
			int zWidth = getCoordBaseMode().getAxis().equals(EnumFacing.Axis.Z) ? 8 : 4;
			
			int x = pos.getX() - (xWidth/2 - 1);
			int z = pos.getZ() - (zWidth/2 - 1);
			
			this.boundingBox = new StructureBoundingBox(x, pos.getY(), z, x + xWidth - 1, pos.getY() + 4, z + zWidth - 1);
			
			int xOffset = coordBaseMode.getFrontOffsetX();
			int zOffset = coordBaseMode.getFrontOffsetZ();
			corridors[0] = !generatePart(compoGen, xIndex + xOffset, zIndex + zOffset, pos.add(xOffset, 0, zOffset), coordBaseMode, rand, componentList, index + 1);
		}
		
		@Override
		protected void writeStructureToNBT(NBTTagCompound tagCompound)
		{
			for(int i = 0; i < corridors.length; i++)
				tagCompound.setBoolean("blocked"+i, corridors[i]);
		}
		
		@Override
		protected void readStructureFromNBT(NBTTagCompound tagCompound)
		{
			for(int i = 0; i < corridors.length; i++)
				corridors[i] = tagCompound.getBoolean("blocked"+i);
		}
		
		@Override
		public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
		{
			
			ChunkProviderLands provider = (ChunkProviderLands) worldIn.provider.createChunkGenerator();
			
			IBlockState wallBlock = provider.blockRegistry.getBlockState("structure_primary");
			IBlockState floorBlock = provider.blockRegistry.getBlockState("structure_secondary");
			
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 0, 2, 0, 7, floorBlock, floorBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 4, 0, 2, 4, 7, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 0, 0, 4, 7, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 0, 0, 3, 4, 7, wallBlock, wallBlock, false);
			fillWithAir(worldIn, structureBoundingBoxIn, 1, 1, 0, 2, 3, 7);
			
			if(corridors[0])
				fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 7, 2, 3, 7, wallBlock, wallBlock, false);
			
			return true;
		}
	}
}