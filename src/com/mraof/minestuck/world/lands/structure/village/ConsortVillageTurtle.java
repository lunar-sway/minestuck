package com.mraof.minestuck.world.lands.structure.village;

import com.mraof.minestuck.entity.consort.EnumConsort;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;
import net.minecraft.util.Rotation;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class ConsortVillageTurtle
{
	/*public static class TurtleWellCenter extends ConsortVillageCenter.VillageCenter
	{
		public TurtleWellCenter()
		{
			super();
		}
		
		public TurtleWellCenter(List<ConsortVillageComponents.PieceWeight> pieceWeightList, int x, int z, Random rand)
		{
			super(pieceWeightList);
			this.setCoordBaseMode(EnumFacing.Plane.HORIZONTAL.random(rand));
			
			this.boundingBox = new StructureBoundingBox(x, 60, z, x + 8 - 1, 70, z + 8 - 1);
		}
		
		@Override
		public void buildComponent(StructureComponent componentIn, List<StructureComponent> listIn, Random rand)
		{
			ConsortVillageComponents.generateAndAddRoadPiece((ConsortVillageCenter.VillageCenter) componentIn, listIn, rand, boundingBox.minX + 3, boundingBox.minY, boundingBox.maxZ + 1, EnumFacing.SOUTH);
			ConsortVillageComponents.generateAndAddRoadPiece((ConsortVillageCenter.VillageCenter) componentIn, listIn, rand, boundingBox.minX - 1, boundingBox.minY, boundingBox.minZ + 3, EnumFacing.WEST);
			ConsortVillageComponents.generateAndAddRoadPiece((ConsortVillageCenter.VillageCenter) componentIn, listIn, rand, boundingBox.minX + 3, boundingBox.minY, boundingBox.minZ - 1, EnumFacing.NORTH);
			ConsortVillageComponents.generateAndAddRoadPiece((ConsortVillageCenter.VillageCenter) componentIn, listIn, rand, boundingBox.maxX + 1, boundingBox.minY, boundingBox.minZ + 3, EnumFacing.EAST);
		}
		
		@Override
		public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
		{
			if (this.averageGroundLvl < 0)
			{
				this.averageGroundLvl = this.getAverageGroundLevel(worldIn, structureBoundingBoxIn);
				
				if (this.averageGroundLvl < 0)
				{
					return true;
				}
				
				this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.minY - 5, 0);
			}
			
			ChunkProviderLands provider = (ChunkProviderLands) worldIn.provider.createChunkGenerator();
			IBlockState primary = provider.blockRegistry.getBlockState("structure_primary");
			IBlockState secondary = provider.blockRegistry.getBlockState("structure_secondary");
			IBlockState fluid = provider.blockRegistry.getBlockState("ocean");
			IBlockState stairsN = provider.blockRegistry.getStairs("structure_primary_stairs", EnumFacing.NORTH, false);
			IBlockState stairsE = provider.blockRegistry.getStairs("structure_primary_stairs", EnumFacing.EAST, false);
			IBlockState stairsS = provider.blockRegistry.getStairs("structure_primary_stairs", EnumFacing.SOUTH, false);
			IBlockState stairsW = provider.blockRegistry.getStairs("structure_primary_stairs", EnumFacing.WEST, false);
			
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 4, 0, 2, 4, 0, primary, primary, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 4, 1, 0, 4, 2, primary, primary, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 4, 0, 7, 4, 0, primary, primary, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 4, 1, 7, 4, 2, primary, primary, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 4, 7, 2, 4, 7, primary, primary, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 4, 5, 0, 4, 6, primary, primary, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 4, 7, 7, 4, 7, primary, primary, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 4, 5, 7, 4, 6, primary, primary, false);
			
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 4, 0, 4, 4, 0, stairsN, stairsN, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 4, 7, 4, 4, 7, stairsS, stairsS, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 4, 3, 0, 4, 4, stairsW, stairsW, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 4, 3, 7, 4, 4, stairsE, stairsE, false);
			
			this.fillWithAir(worldIn, structureBoundingBoxIn, 1, 5, 0, 6, 7, 0);
			this.fillWithAir(worldIn, structureBoundingBoxIn, 1, 5, 7, 6, 7, 7);
			this.fillWithAir(worldIn, structureBoundingBoxIn, 0, 5, 1, 0, 7, 6);
			this.fillWithAir(worldIn, structureBoundingBoxIn, 7, 5, 1, 7, 7, 6);
			
			this.fillWithAir(worldIn, structureBoundingBoxIn, 1, 4, 1, 6, 8, 6);
			this.fillWithAir(worldIn, structureBoundingBoxIn, 2, 3, 2, 5, 3, 5);
			
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 5, 0, 0, 7, 0, primary, primary, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 5, 7, 0, 7, 7, primary, primary, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 5, 0, 7, 7, 0, primary, primary, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 5, 7, 7, 7, 7, primary, primary, false);
			
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 8, 0, 7, 8, 0, primary, primary, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 8, 7, 7, 8, 7, primary, primary, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 8, 1, 0, 8, 6, primary, primary, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 8, 1, 7, 8, 6, primary, primary, false);
			this.setBlockState(worldIn, primary, 1, 8, 1, structureBoundingBoxIn);
			this.setBlockState(worldIn, primary, 1, 8, 6, structureBoundingBoxIn);
			this.setBlockState(worldIn, primary, 6, 8, 1, structureBoundingBoxIn);
			this.setBlockState(worldIn, primary, 6, 8, 6, structureBoundingBoxIn);
			
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 9, 1, 6, 9, 6, primary, primary, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 10, 2, 5, 10, 5, primary, primary, false);
			
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 1, 6, 3, 1, secondary, secondary, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 6, 6, 3, 6, secondary, secondary, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 2, 1, 3, 5, secondary, secondary, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 1, 2, 6, 3, 5, secondary, secondary, false);
			
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 3, 1, 4, 3, 1, stairsN, stairsN, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 3, 6, 4, 3, 6, stairsS, stairsS, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 3, 3, 1, 3, 4, stairsW, stairsW, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 3, 3, 6, 3, 4, stairsE, stairsE, false);
			
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 0, 2, 5, 1, 5, secondary, secondary, false);
			
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 2, 2, 5, 2, 5, fluid, fluid, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 1, 3, 4, 1, 4, fluid, fluid, false);
			
			return true;
		}
		
	}
	
	public static class LoweredShellHouse1 extends ConsortVillageComponents.ConsortVillagePiece
	{
		public LoweredShellHouse1()
		{
			spawns = new boolean[2];
		}
		
		public LoweredShellHouse1(ConsortVillageCenter.VillageCenter start, Random rand, StructureBoundingBox boundingBox, EnumFacing facing)
		{
			this();
			this.setCoordBaseMode(facing);
			this.boundingBox = boundingBox;
		}
		
		public static LoweredShellHouse1 createPiece(ConsortVillageCenter.VillageCenter start, List<StructureComponent> componentList, Random rand, int x, int y, int z, EnumFacing facing)
		{
			StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, -2, 0, 8, 5, 9, facing);
			return StructureComponent.findIntersecting(componentList, structureboundingbox) == null ? new LoweredShellHouse1(start, rand, structureboundingbox, facing) : null;
		}
		
		@Override
		public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
		{
			if (this.averageGroundLvl < 0)
			{
				this.averageGroundLvl = this.getAverageGroundLevel(worldIn, structureBoundingBoxIn);
				
				if (this.averageGroundLvl < 0)
				{
					return true;
				}
				
				this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.minY - 3, 0);
			}
			
			ChunkProviderLands provider = (ChunkProviderLands) worldIn.provider.createChunkGenerator();
			IBlockState buildBlock = provider.blockRegistry.getBlockState("structure_primary");
			IBlockState lightBlock = provider.blockRegistry.getBlockState("light_block");
			
			this.fillWithAir(worldIn, structureBoundingBoxIn, 1, 1, 2, 6, 4, 7);
			this.fillWithAir(worldIn, structureBoundingBoxIn, 3, 3, 1, 4, 4, 1);
			this.fillWithAir(worldIn, structureBoundingBoxIn, 2, 5, 6, 2, 5, 6);
			
			this.clearFront(worldIn, structureBoundingBoxIn, 2, 5, 3, 0);
			
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 1, 7, 0, 8, buildBlock, buildBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 8, 7, 4, 8, buildBlock, buildBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 1, 0, 4, 7, buildBlock, buildBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 1, 1, 7, 4, 7, buildBlock, buildBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 1, 6, 2, 1, buildBlock, buildBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 3, 1, 2, 4, 1, buildBlock, buildBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 3, 1, 6, 4, 1, buildBlock, buildBlock, false);
			
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 1, 2, 4, 1, 2, buildBlock, buildBlock, false);
			
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 5, 1, 4, 5, 1, buildBlock, buildBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 5, 2, 6, 5, 2, buildBlock, buildBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 5, 3, 1, 5, 7, buildBlock, buildBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 5, 3, 6, 5, 7, buildBlock, buildBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 5, 7, 5, 5, 7, buildBlock, buildBlock, false);
			
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 6, 3, 5, 6, 6, buildBlock, buildBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 6, 4, 4, 6, 5, lightBlock, lightBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 7, 4, 4, 7, 5, buildBlock, buildBlock, false);
			
			if(!spawns[0])
				spawns[0] = spawnConsort(2, 1, 5, structureBoundingBoxIn, worldIn);
			if(!spawns[1])
				spawns[1] = spawnConsort(5, 1, 5, structureBoundingBoxIn, worldIn);
			
			return true;
		}
	}
	
	public static class TurtleMarketBuilding1 extends ConsortVillageComponents.ConsortVillagePiece
	{
		public TurtleMarketBuilding1()
		{
			spawns = new boolean[2];
		}
		
		public TurtleMarketBuilding1(ConsortVillageCenter.VillageCenter start, Random rand, StructureBoundingBox boundingBox, EnumFacing facing)
		{
			this();
			setCoordBaseMode(facing);
			this.boundingBox = boundingBox;
		}
		
		public static TurtleMarketBuilding1 createPiece(ConsortVillageCenter.VillageCenter start, List<StructureComponent> componentList, Random rand, int x, int y, int z, EnumFacing facing)
		{
			StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, 14, 7, 19, facing);
			return StructureComponent.findIntersecting(componentList, structureboundingbox) == null ? new TurtleMarketBuilding1(start, rand, structureboundingbox, facing) : null;
		}
		
		@Override
		public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
		{
			if (this.averageGroundLvl < 0)
			{
				this.averageGroundLvl = this.getAverageGroundLevel(worldIn, structureBoundingBoxIn);
				
				if (this.averageGroundLvl < 0)
				{
					return true;
				}
				
				this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.minY - 1, 0);
			}
			
			ChunkProviderLands provider = (ChunkProviderLands) worldIn.provider.createChunkGenerator();
			IBlockState primaryBlock = provider.blockRegistry.getBlockState("structure_primary");
			IBlockState primaryDecorBlock = provider.blockRegistry.getBlockState("structure_primary_decorative");
			IBlockState secondaryBlock = provider.blockRegistry.getBlockState("structure_secondary");
			IBlockState lightBlock = provider.blockRegistry.getBlockState("light_block");
			IBlockState glassBlock = provider.blockRegistry.getBlockState("glass");
			
			this.fillWithAir(worldIn, structureBoundingBoxIn, 6, 1, 1, 7, 3, 3);
			this.fillWithAir(worldIn, structureBoundingBoxIn, 1, 1, 4, 12, 4, 13);
			this.fillWithAir(worldIn, structureBoundingBoxIn, 1, 2, 14, 12, 4, 14);
			this.fillWithAir(worldIn, structureBoundingBoxIn, 1, 1, 15, 12, 4, 17);
			this.fillWithAir(worldIn, structureBoundingBoxIn, 2, 5, 3, 11, 5, 16);
			this.clearFront(worldIn, structureBoundingBoxIn, 5, 8, 2, 0);
			
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, -1, 4, 12, 0, 17, primaryBlock, primaryBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, -1, 1, 8, 0, 3, secondaryBlock, secondaryBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 1, 1, 5, 3, 3, secondaryBlock, secondaryBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 8, 1, 1, 8, 3, 3, secondaryBlock, secondaryBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 4, 1, 7, 4, 3, secondaryBlock, secondaryBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, -1, 3, 4, 1, 3, secondaryBlock, secondaryBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 9, -1, 3, 13, 1, 3, secondaryBlock, secondaryBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, -1, 4, 0, 1, 18, secondaryBlock, secondaryBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 13, -1, 4, 13, 1, 18, secondaryBlock, secondaryBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, -1, 18, 12, 1, 18, secondaryBlock, secondaryBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 3, 4, 3, 3, primaryBlock, primaryBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 9, 2, 3, 13, 3, 3, primaryBlock, primaryBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 4, 0, 3, 18, primaryBlock, primaryBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 13, 2, 4, 13, 3, 18, primaryBlock, primaryBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 2, 18, 12, 3, 18, primaryBlock, primaryBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 4, 3, 5, 4, 3, glassBlock, glassBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 8, 4, 3, 13, 4, 3, glassBlock, glassBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 4, 4, 0, 4, 18, glassBlock, glassBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 13, 4, 4, 13, 4, 18, glassBlock, glassBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 4, 18, 12, 4, 18, glassBlock, glassBlock, false);
			
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 5, 4, 12, 5, 4, primaryBlock, primaryBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 5, 5, 1, 5, 17, primaryBlock, primaryBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 12, 5, 5, 12, 5, 17, primaryBlock, primaryBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 5, 17, 11, 5, 17, primaryBlock, primaryBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 6, 5, 11, 6, 16, primaryBlock, primaryBlock, false);
			
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 14, 12, 1, 14, secondaryBlock, secondaryBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 0, 4, 7, 0, 7, primaryDecorBlock, primaryDecorBlock, false);
			this.setBlockState(worldIn, primaryDecorBlock, 5, 0, 8, structureBoundingBoxIn);
			this.setBlockState(worldIn, primaryDecorBlock, 8, 0, 8, structureBoundingBoxIn);
			this.setBlockState(worldIn, primaryDecorBlock, 4, 0, 9, structureBoundingBoxIn);
			this.setBlockState(worldIn, primaryDecorBlock, 9, 0, 9, structureBoundingBoxIn);
			this.setBlockState(worldIn, primaryDecorBlock, 3, 0, 10, structureBoundingBoxIn);
			this.setBlockState(worldIn, primaryDecorBlock, 10, 0, 10, structureBoundingBoxIn);
			this.setBlockState(worldIn, primaryDecorBlock, 4, 0, 11, structureBoundingBoxIn);
			this.setBlockState(worldIn, primaryDecorBlock, 9, 0, 11, structureBoundingBoxIn);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 0, 12, 8, 0, 12, primaryDecorBlock, primaryDecorBlock, false);
			
			this.setBlockState(worldIn, lightBlock, 5, 1, 1, structureBoundingBoxIn);
			this.setBlockState(worldIn, lightBlock, 8, 1, 1, structureBoundingBoxIn);
			this.setBlockState(worldIn, lightBlock, 1, 0, 4, structureBoundingBoxIn);
			this.setBlockState(worldIn, lightBlock, 12, 0, 4, structureBoundingBoxIn);
			this.setBlockState(worldIn, lightBlock, 1, 0, 13, structureBoundingBoxIn);
			this.setBlockState(worldIn, lightBlock, 12, 0, 13, structureBoundingBoxIn);
			this.setBlockState(worldIn, lightBlock, 1, 0, 17, structureBoundingBoxIn);
			this.setBlockState(worldIn, lightBlock, 12, 0, 17, structureBoundingBoxIn);
			this.setBlockState(worldIn, lightBlock, 2, 6, 5, structureBoundingBoxIn);
			this.setBlockState(worldIn, lightBlock, 11, 6, 5, structureBoundingBoxIn);
			this.setBlockState(worldIn, lightBlock, 2, 6, 16, structureBoundingBoxIn);
			this.setBlockState(worldIn, lightBlock, 11, 6, 16, structureBoundingBoxIn);
			
			if(!spawns[0])
				spawns[0] = spawnConsort(4, 1, 15, structureBoundingBoxIn, worldIn, EnumConsort.getRandomMerchant(randomIn), 1);
			if(!spawns[1])
				spawns[1] = spawnConsort(9, 1, 15, structureBoundingBoxIn, worldIn, EnumConsort.getRandomMerchant(randomIn), 1);
			
			return true;
		}
	}
	
	public static class TurtleTemple1 extends ConsortVillageComponents.ConsortVillagePiece
	{
		public TurtleTemple1()
		{
			spawns = new boolean[3];
		}
		
		public TurtleTemple1(ConsortVillageCenter.VillageCenter start, Random rand, StructureBoundingBox boundingBox, EnumFacing facing)
		{
			this();
			this.setCoordBaseMode(facing);
			this.boundingBox = boundingBox;
		}
		
		public static TurtleTemple1 createPiece(ConsortVillageCenter.VillageCenter start, List<StructureComponent> componentList, Random rand, int x, int y, int z, EnumFacing facing)
		{
			StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, -1, 0, 11, 6, 14, facing);
			return StructureComponent.findIntersecting(componentList, structureboundingbox) == null ? new TurtleTemple1(start, rand, structureboundingbox, facing) : null;
		}
		
		@Override
		public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
		{
			if (this.averageGroundLvl < 0)
			{
				this.averageGroundLvl = this.getAverageGroundLevel(worldIn, structureBoundingBoxIn);
				
				if (this.averageGroundLvl < 0)
				{
					return true;
				}
				
				this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.minY - 1, 0);
			}
			
			ChunkProviderLands provider = (ChunkProviderLands) worldIn.provider.createChunkGenerator();
			IBlockState primaryBlock = provider.blockRegistry.getBlockState("structure_primary");
			IBlockState secondaryBlock = provider.blockRegistry.getBlockState("structure_secondary");
			IBlockState lightBlock = provider.blockRegistry.getBlockState("light_block");
			IBlockState glassBlock1 = provider.blockRegistry.getBlockState("stained_glass_1");
			IBlockState glassBlock2 = provider.blockRegistry.getBlockState("stained_glass_2");
			
			this.fillWithAir(worldIn, structureBoundingBoxIn, 1, 1, 2, 9, 5, 4);
			this.fillWithAir(worldIn, structureBoundingBoxIn, 1, 1, 5, 9, 4, 6);
			this.fillWithAir(worldIn, structureBoundingBoxIn, 1, 0, 7, 9, 3, 12);
			this.clearFront(worldIn, structureBoundingBoxIn, 4, 6, 1, 0);
			
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 2, 9, 0, 6, primaryBlock, primaryBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, -1, 6, 9, -1, 12, primaryBlock, primaryBlock, false);
			
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 1, 10, 1, 1, secondaryBlock, secondaryBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 2, 1, 6, 3, 1, secondaryBlock, secondaryBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 2, 0, 1, 4, secondaryBlock, secondaryBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 10, 0, 2, 10, 1, 4, secondaryBlock, secondaryBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 5, 0, 1, 6, primaryBlock, primaryBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, -1, 7, 0, 3, 13, primaryBlock, primaryBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 10, 0, 5, 10, 1, 6, primaryBlock, primaryBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 10, -1, 7, 10, 3, 13, primaryBlock, primaryBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, -1, 13, 9, 3, 13, primaryBlock, primaryBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 4, 1, 9, 5, 1, primaryBlock, primaryBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 1, 0, 5, 4, primaryBlock, primaryBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 4, 5, 0, 4, 6, primaryBlock, primaryBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 10, 2, 1, 10, 5, 4, primaryBlock, primaryBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 10, 4, 5, 10, 4, 6, primaryBlock, primaryBlock, false);
			
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 4, 7, 9, 4, 12, primaryBlock, primaryBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 5, 5, 9, 5, 6, primaryBlock, primaryBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 6, 2, 9, 6, 4, primaryBlock, primaryBlock, false);
			
			this.setBlockState(worldIn, lightBlock, 1, 0, 2, structureBoundingBoxIn);
			this.setBlockState(worldIn, lightBlock, 9, 0, 2, structureBoundingBoxIn);
			this.setBlockState(worldIn, lightBlock, 1, -1, 12, structureBoundingBoxIn);
			this.setBlockState(worldIn, lightBlock, 9, -1, 12, structureBoundingBoxIn);
			
			this.setBlockState(worldIn, glassBlock1, 1, 2, 1, structureBoundingBoxIn);
			this.setBlockState(worldIn, glassBlock1, 1, 3, 1, structureBoundingBoxIn);
			this.setBlockState(worldIn, glassBlock2, 2, 2, 1, structureBoundingBoxIn);
			this.setBlockState(worldIn, glassBlock2, 2, 3, 1, structureBoundingBoxIn);
			this.setBlockState(worldIn, glassBlock1, 2, 4, 1, structureBoundingBoxIn);
			this.setBlockState(worldIn, glassBlock1, 3, 2, 1, structureBoundingBoxIn);
			this.setBlockState(worldIn, glassBlock1, 3, 3, 1, structureBoundingBoxIn);
			
			this.setBlockState(worldIn, glassBlock1, 7, 2, 1, structureBoundingBoxIn);
			this.setBlockState(worldIn, glassBlock1, 7, 3, 1, structureBoundingBoxIn);
			this.setBlockState(worldIn, glassBlock2, 8, 2, 1, structureBoundingBoxIn);
			this.setBlockState(worldIn, glassBlock2, 8, 3, 1, structureBoundingBoxIn);
			this.setBlockState(worldIn, glassBlock1, 8, 4, 1, structureBoundingBoxIn);
			this.setBlockState(worldIn, glassBlock1, 9, 2, 1, structureBoundingBoxIn);
			this.setBlockState(worldIn, glassBlock1, 9, 3, 1, structureBoundingBoxIn);
			
			this.setBlockState(worldIn, glassBlock2, 0, 2, 4, structureBoundingBoxIn);
			this.setBlockState(worldIn, glassBlock1, 0, 3, 4, structureBoundingBoxIn);
			this.setBlockState(worldIn, glassBlock1, 0, 2, 5, structureBoundingBoxIn);
			this.setBlockState(worldIn, glassBlock2, 0, 3, 5, structureBoundingBoxIn);
			this.setBlockState(worldIn, glassBlock2, 0, 2, 6, structureBoundingBoxIn);
			this.setBlockState(worldIn, glassBlock1, 0, 3, 6, structureBoundingBoxIn);
			
			this.setBlockState(worldIn, glassBlock2, 10, 2, 4, structureBoundingBoxIn);
			this.setBlockState(worldIn, glassBlock1, 10, 3, 4, structureBoundingBoxIn);
			this.setBlockState(worldIn, glassBlock1, 10, 2, 5, structureBoundingBoxIn);
			this.setBlockState(worldIn, glassBlock2, 10, 3, 5, structureBoundingBoxIn);
			this.setBlockState(worldIn, glassBlock2, 10, 2, 6, structureBoundingBoxIn);
			this.setBlockState(worldIn, glassBlock1, 10, 3, 6, structureBoundingBoxIn);
			
			generateDoor(worldIn, structureBoundingBoxIn, randomIn, 5, 1, 1, EnumFacing.SOUTH, Blocks.IRON_DOOR);
			
			setBlockState(worldIn, Blocks.STONE_BUTTON.getDefaultState(), 6, 1, 0, structureBoundingBoxIn);
			setBlockState(worldIn, Blocks.STONE_BUTTON.getDefaultState().withRotation(Rotation.CLOCKWISE_180), 4, 1, 2, structureBoundingBoxIn);
			
			if(!spawns[0])
				spawns[0] = spawnConsort(4, 0, 10, structureBoundingBoxIn, worldIn);
			if(!spawns[1])
				spawns[1] = spawnConsort(5, 0, 10, structureBoundingBoxIn, worldIn);
			if(!spawns[2])
				spawns[2] = spawnConsort(6, 0, 10, structureBoundingBoxIn, worldIn);
			
			return true;
		}
	}*/
}
