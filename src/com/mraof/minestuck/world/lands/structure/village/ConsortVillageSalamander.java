package com.mraof.minestuck.world.lands.structure.village;

import com.mraof.minestuck.entity.consort.EnumConsort;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;

import java.util.List;
import java.util.Random;

public class ConsortVillageSalamander
{
	public static class RuinedTowerMushroomCenter extends ConsortVillageCenter.VillageCenter
	{
		public RuinedTowerMushroomCenter()
		{
			super();
		}
		
		public RuinedTowerMushroomCenter(List<ConsortVillageComponents.PieceWeight> pieceWeightList, int x, int z, Random rand)
		{
			super(pieceWeightList);
			this.setCoordBaseMode(EnumFacing.Plane.HORIZONTAL.random(rand));
			
			this.boundingBox = new StructureBoundingBox(x, 64, z, x + 9 - 1, 80, z + 9 - 1);
		}
		
		@Override
		public void buildComponent(StructureComponent componentIn, List<StructureComponent> listIn, Random rand)
		{
			ConsortVillageComponents.generateAndAddRoadPiece((ConsortVillageCenter.VillageCenter) componentIn, listIn, rand, boundingBox.minX + 4, boundingBox.minY, boundingBox.maxZ + 1, EnumFacing.SOUTH);
			ConsortVillageComponents.generateAndAddRoadPiece((ConsortVillageCenter.VillageCenter) componentIn, listIn, rand, boundingBox.minX - 1, boundingBox.minY, boundingBox.minZ + 4, EnumFacing.WEST);
			ConsortVillageComponents.generateAndAddRoadPiece((ConsortVillageCenter.VillageCenter) componentIn, listIn, rand, boundingBox.minX + 4, boundingBox.minY, boundingBox.minZ - 1, EnumFacing.NORTH);
			ConsortVillageComponents.generateAndAddRoadPiece((ConsortVillageCenter.VillageCenter) componentIn, listIn, rand, boundingBox.maxX + 1, boundingBox.minY, boundingBox.minZ + 4, EnumFacing.EAST);
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
			IBlockState primary = provider.blockRegistry.getBlockState("structure_primary");
			IBlockState stairsN = provider.blockRegistry.getStairs("structure_primary_stairs", EnumFacing.NORTH, false);
			IBlockState stairsE = provider.blockRegistry.getStairs("structure_primary_stairs", EnumFacing.EAST, false);
			IBlockState stairsS = provider.blockRegistry.getStairs("structure_primary_stairs", EnumFacing.SOUTH, false);
			IBlockState stairsW = provider.blockRegistry.getStairs("structure_primary_stairs", EnumFacing.WEST, false);
			IBlockState secondary = provider.blockRegistry.getBlockState("structure_secondary");
			IBlockState ground = provider.blockRegistry.getBlockState("surface");
			IBlockState mushroom1 = provider.blockRegistry.getBlockState("mushroom_1");
			IBlockState mushroom2 = provider.blockRegistry.getBlockState("mushroom_2");
			
			this.fillWithAir(worldIn, structureBoundingBoxIn, 3, 1, 2, 5, 7, 6);
			this.fillWithAir(worldIn, structureBoundingBoxIn, 2, 1, 3, 2, 7, 5);
			this.fillWithAir(worldIn, structureBoundingBoxIn, 6, 1, 3, 6, 7, 5);
			
			this.fillWithAir(worldIn, structureBoundingBoxIn, 4, 2, 1, 4, 4, 1);
			this.fillWithAir(worldIn, structureBoundingBoxIn, 4, 2, 7, 4, 4, 7);
			this.fillWithAir(worldIn, structureBoundingBoxIn, 1, 2, 4, 1, 4, 4);
			this.fillWithAir(worldIn, structureBoundingBoxIn, 7, 2, 4, 7, 4, 4);
			
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 0, 2, 4, 0, 6, secondary, secondary, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 0, 3, 4, 0, 5, secondary, secondary, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 0, 3, 5, 0, 5, secondary, secondary, false);
			this.setBlockState(worldIn, secondary, 2, 0, 4, structureBoundingBoxIn);
			this.setBlockState(worldIn, secondary, 6, 0, 4, structureBoundingBoxIn);
			
			this.blockPillar(2, 0, 3, structureBoundingBoxIn, worldIn, ground);
			this.blockPillar(3, 0, 2, structureBoundingBoxIn, worldIn, ground);
			this.blockPillar(6, 0, 3, structureBoundingBoxIn, worldIn, ground);
			this.blockPillar(5, 0, 2, structureBoundingBoxIn, worldIn, ground);
			this.blockPillar(2, 0, 5, structureBoundingBoxIn, worldIn, ground);
			this.blockPillar(3, 0, 6, structureBoundingBoxIn, worldIn, ground);
			this.blockPillar(6, 0, 5, structureBoundingBoxIn, worldIn, ground);
			this.blockPillar(5, 0, 6, structureBoundingBoxIn, worldIn, ground);
			
			for(int i = 3; i <= 5; i++)
			{
				this.blockPillar(i, 1, 1, structureBoundingBoxIn, worldIn, primary);
				this.blockPillar(i, 1, 7, structureBoundingBoxIn, worldIn, primary);
				this.blockPillar(1, 1, i, structureBoundingBoxIn, worldIn, primary);
				this.blockPillar(7, 1, i, structureBoundingBoxIn, worldIn, primary);
			}
			this.blockPillar(2, 1, 2, structureBoundingBoxIn, worldIn, primary);
			this.blockPillar(2, 1, 6, structureBoundingBoxIn, worldIn, primary);
			this.blockPillar(6, 1, 2, structureBoundingBoxIn, worldIn, primary);
			this.blockPillar(6, 1, 6, structureBoundingBoxIn, worldIn, primary);
			
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 1, 0, 6, 1, 0, stairsS, stairsS, false);
			this.setBlockState(worldIn, stairsE, 2, 1, 1, structureBoundingBoxIn);
			this.setBlockState(worldIn, stairsW, 6, 1, 1, structureBoundingBoxIn);
			this.setBlockState(worldIn, stairsS, 1, 1, 1, structureBoundingBoxIn);
			this.setBlockState(worldIn, stairsS, 7, 1, 1, structureBoundingBoxIn);
			this.setBlockState(worldIn, stairsE, 1, 1, 2, structureBoundingBoxIn);
			this.setBlockState(worldIn, stairsW, 7, 1, 2, structureBoundingBoxIn);
			this.setBlockState(worldIn, stairsS, 0, 1, 2, structureBoundingBoxIn);
			this.setBlockState(worldIn, stairsS, 8, 1, 2, structureBoundingBoxIn);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 3, 0, 1, 5, stairsE, stairsE, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 8, 1, 3, 8, 1, 5, stairsW, stairsW, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 1, 8, 6, 1, 8, stairsN, stairsN, false);
			this.setBlockState(worldIn, stairsE, 2, 1, 7, structureBoundingBoxIn);
			this.setBlockState(worldIn, stairsW, 6, 1, 7, structureBoundingBoxIn);
			this.setBlockState(worldIn, stairsN, 1, 1, 7, structureBoundingBoxIn);
			this.setBlockState(worldIn, stairsN, 7, 1, 7, structureBoundingBoxIn);
			this.setBlockState(worldIn, stairsE, 1, 1, 6, structureBoundingBoxIn);
			this.setBlockState(worldIn, stairsW, 7, 1, 6, structureBoundingBoxIn);
			this.setBlockState(worldIn, stairsN, 0, 1, 6, structureBoundingBoxIn);
			this.setBlockState(worldIn, stairsN, 8, 1, 6, structureBoundingBoxIn);
			
			this.setBlockState(worldIn, primary, 4, 5, 1, structureBoundingBoxIn);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 2, 1, 3, 6, 1, primary, primary, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 2, 1, 5, 6, 1, primary, primary, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 2, 2, 2, 7, 2, primary, primary, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 2, 2, 6, 7, 2, primary, primary, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 2, 3, 1, 8, 3, primary, primary, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 2, 3, 7, 8, 3, primary, primary, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 5, 4, 1, 9, 4, primary, primary, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 5, 4, 7, 9, 4, primary, primary, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 2, 5, 1, 10, 5, primary, primary, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 2, 5, 7, 10, 5, primary, primary, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 2, 6, 2, 11, 6, primary, primary, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 2, 6, 6, 11, 6, primary, primary, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 2, 7, 3, 12, 7, primary, primary, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 2, 7, 5, 12, 7, primary, primary, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 5, 7, 4, 7, 7, primary, primary, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 11, 7, 4, 13, 7, primary, primary, false);
			
			this.setBlockState(worldIn, secondary, 4, 6, 1, structureBoundingBoxIn);
			this.setBlockState(worldIn, secondary, 3, 7, 1, structureBoundingBoxIn);
			this.setBlockState(worldIn, secondary, 5, 7, 1, structureBoundingBoxIn);
			this.setBlockState(worldIn, secondary, 2, 8, 2, structureBoundingBoxIn);
			this.setBlockState(worldIn, secondary, 6, 8, 2, structureBoundingBoxIn);
			this.setBlockState(worldIn, secondary, 1, 9, 3, structureBoundingBoxIn);
			this.setBlockState(worldIn, secondary, 7, 9, 3, structureBoundingBoxIn);
			this.setBlockState(worldIn, secondary, 1, 10, 4, structureBoundingBoxIn);
			this.setBlockState(worldIn, secondary, 7, 10, 4, structureBoundingBoxIn);
			this.setBlockState(worldIn, secondary, 1, 11, 5, structureBoundingBoxIn);
			this.setBlockState(worldIn, secondary, 7, 11, 5, structureBoundingBoxIn);
			this.setBlockState(worldIn, secondary, 2, 12, 6, structureBoundingBoxIn);
			this.setBlockState(worldIn, secondary, 6, 12, 6, structureBoundingBoxIn);
			this.setBlockState(worldIn, secondary, 3, 13, 7, structureBoundingBoxIn);
			this.setBlockState(worldIn, secondary, 5, 13, 7, structureBoundingBoxIn);
			this.setBlockState(worldIn, secondary, 4, 14, 7, structureBoundingBoxIn);
			
			this.setBlockState(worldIn, randomIn.nextBoolean() ? mushroom1 :mushroom2, 3, 1, 2, structureBoundingBoxIn);
			this.setBlockState(worldIn, randomIn.nextBoolean() ? mushroom1 :mushroom2, 5, 1, 2, structureBoundingBoxIn);
			this.setBlockState(worldIn, randomIn.nextBoolean() ? mushroom1 :mushroom2, 2, 1, 3, structureBoundingBoxIn);
			this.setBlockState(worldIn, randomIn.nextBoolean() ? mushroom1 :mushroom2, 6, 1, 3, structureBoundingBoxIn);
			this.setBlockState(worldIn, randomIn.nextBoolean() ? mushroom1 :mushroom2, 3, 1, 6, structureBoundingBoxIn);
			this.setBlockState(worldIn, randomIn.nextBoolean() ? mushroom1 :mushroom2, 5, 1, 6, structureBoundingBoxIn);
			this.setBlockState(worldIn, randomIn.nextBoolean() ? mushroom1 :mushroom2, 2, 1, 5, structureBoundingBoxIn);
			this.setBlockState(worldIn, randomIn.nextBoolean() ? mushroom1 :mushroom2, 6, 1, 5, structureBoundingBoxIn);
			
			this.setBlockState(worldIn, randomIn.nextBoolean() ? mushroom1 :mushroom2, 4, 8, 7, structureBoundingBoxIn);
			this.setBlockState(worldIn, randomIn.nextBoolean() ? mushroom1 :mushroom2, 4, 7, 1, structureBoundingBoxIn);
			this.setBlockState(worldIn, randomIn.nextBoolean() ? mushroom1 :mushroom2, 4, 15, 7, structureBoundingBoxIn);
			this.setBlockState(worldIn, randomIn.nextBoolean() ? mushroom1 :mushroom2, 1, 11, 4, structureBoundingBoxIn);
			this.setBlockState(worldIn, randomIn.nextBoolean() ? mushroom1 :mushroom2, 7, 11, 4, structureBoundingBoxIn);
			
			return true;
		}
	}
	
	public static class PipeHouse1 extends ConsortVillageComponents.ConsortVillagePiece
	{
		public PipeHouse1()
		{
			spawns = new boolean[2];
		}
		
		public PipeHouse1(ConsortVillageCenter.VillageCenter start, Random rand, StructureBoundingBox boundingBox, EnumFacing facing)
		{
			this();
			this.setCoordBaseMode(facing);
			this.boundingBox = boundingBox;
		}
		
		public static PipeHouse1 createPiece(ConsortVillageCenter.VillageCenter start, List<StructureComponent> componentList, Random rand, int x, int y, int z, EnumFacing facing)
		{
			StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, 6, 5, 7, facing);
			return StructureComponent.findIntersecting(componentList, structureboundingbox) == null ? new PipeHouse1(start, rand, structureboundingbox, facing) : null;
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
			IBlockState wallBlock = provider.blockRegistry.getBlockState("structure_primary");
			IBlockState floorBlock = provider.blockRegistry.getBlockState("salamander_floor");
			IBlockState doorBlock = provider.blockRegistry.getBlockState("village_door");
			IBlockState torch = provider.blockRegistry.getBlockState("torch");
			
			this.fillWithAir(worldIn, structureBoundingBoxIn, 1, 1, 2, 4, 5, 5);
			this.clearFront(worldIn, structureBoundingBoxIn, 1, 4, 1, 0);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1,0,1,4,0, 6, floorBlock, floorBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0,0,2,0,0, 5, floorBlock, floorBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5,0,2,5,0, 5, floorBlock, floorBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1,1,1,2,2, 1, wallBlock, wallBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 4,1,1,4,2, 1, wallBlock, wallBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1,3,1,4,5, 1, wallBlock, wallBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0,1,2,0,5, 5, wallBlock, wallBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1,1,6,4,5, 6, wallBlock, wallBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5,1,2,5,1, 5, wallBlock, wallBlock, false);
			this.setBlockState(worldIn, wallBlock, 5, 2, 2, structureBoundingBoxIn);
			this.setBlockState(worldIn, wallBlock, 5, 2, 5, structureBoundingBoxIn);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5,3,2,5,5, 5, wallBlock, wallBlock, false);
			generateDoor(worldIn, structureBoundingBoxIn, randomIn, 3, 1, 1, EnumFacing.SOUTH, (BlockDoor) doorBlock.getBlock());
			
			setBlockState(worldIn, torch.withProperty(BlockTorch.FACING, EnumFacing.EAST), 1, 3, 4, structureBoundingBoxIn);
			
			if(!spawns[0])
				spawns[0] = spawnConsort(2, 1, 3, structureBoundingBoxIn, worldIn);
			if(!spawns[1])
				spawns[1] = spawnConsort(3, 1, 4,structureBoundingBoxIn, worldIn);
			return true;
		}
	}
	
	public static class HighPipeHouse1 extends ConsortVillageComponents.ConsortVillagePiece
	{
		public HighPipeHouse1()
		{
			spawns = new boolean[3];
		}
		
		public HighPipeHouse1(ConsortVillageCenter.VillageCenter start, Random rand, StructureBoundingBox boundingBox, EnumFacing facing)
		{
			this();
			this.setCoordBaseMode(facing);
			this.boundingBox = boundingBox;
		}
		
		public static HighPipeHouse1 createPiece(ConsortVillageCenter.VillageCenter start, List<StructureComponent> componentList, Random rand, int x, int y, int z, EnumFacing facing)
		{
			StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, 7, 13, 8, facing);
			return StructureComponent.findIntersecting(componentList, structureboundingbox) == null ? new HighPipeHouse1(start, rand, structureboundingbox, facing) : null;
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
			IBlockState wallBlock = provider.blockRegistry.getBlockState("structure_primary");
			IBlockState floorBlock = provider.blockRegistry.getBlockState("salamander_floor");
			IBlockState doorBlock = provider.blockRegistry.getBlockState("village_door");
			IBlockState torch = provider.blockRegistry.getBlockState("torch");
			
			this.fillWithAir(worldIn, structureBoundingBoxIn, 1, 1, 2, 5, 13, 6);
			this.clearFront(worldIn, structureBoundingBoxIn, 1, 5, 1, 0);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1,0,1,5,0, 7, floorBlock, floorBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0,0,2,0,0, 6, floorBlock, floorBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6,0,2,6,0, 6, floorBlock, floorBlock, false);
			
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1,1,1,2,13, 1, wallBlock, wallBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 4,1,1,5,13, 1, wallBlock, wallBlock, false);
			this.setBlockState(worldIn, wallBlock, 3, 3, 1, structureBoundingBoxIn);
			this.setBlockState(worldIn, wallBlock, 3, 5, 1, structureBoundingBoxIn);
			this.setBlockState(worldIn, wallBlock, 3, 7, 1, structureBoundingBoxIn);
			this.setBlockState(worldIn, wallBlock, 3, 9, 1, structureBoundingBoxIn);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3,11,1,3,13, 1, wallBlock, wallBlock, false);
			
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1,1,7,2,13, 7, wallBlock, wallBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 4,1,7,5,13, 7, wallBlock, wallBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3,1,7,3,3, 7, wallBlock, wallBlock, false);
			this.setBlockState(worldIn, wallBlock, 3, 5, 7, structureBoundingBoxIn);
			this.setBlockState(worldIn, wallBlock, 3, 7, 7, structureBoundingBoxIn);
			this.setBlockState(worldIn, wallBlock, 3, 9, 7, structureBoundingBoxIn);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3,11,7,3,13, 7, wallBlock, wallBlock, false);
			
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0,3,2,0,13, 3, wallBlock, wallBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0,3,5,0,13, 6, wallBlock, wallBlock, false);
			this.setBlockState(worldIn, wallBlock, 0, 3, 4, structureBoundingBoxIn);
			this.setBlockState(worldIn, wallBlock, 0, 5, 4, structureBoundingBoxIn);
			this.setBlockState(worldIn, wallBlock, 0, 7, 4, structureBoundingBoxIn);
			this.setBlockState(worldIn, wallBlock, 0, 9, 4, structureBoundingBoxIn);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0,11,4,0,13, 4, wallBlock, wallBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0,1,2,0,1, 6, wallBlock, wallBlock, false);
			this.setBlockState(worldIn, wallBlock, 0, 2, 2, structureBoundingBoxIn);
			this.setBlockState(worldIn, wallBlock, 0, 2, 6, structureBoundingBoxIn);
			this.fillWithAir(worldIn, structureBoundingBoxIn, 0, 2, 3, 0, 2, 5);
			
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6,3,2,6,13, 3, wallBlock, wallBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6,3,5,6,13, 6, wallBlock, wallBlock, false);
			this.setBlockState(worldIn, wallBlock, 6, 3, 4, structureBoundingBoxIn);
			this.setBlockState(worldIn, wallBlock, 6, 5, 4, structureBoundingBoxIn);
			this.setBlockState(worldIn, wallBlock, 6, 7, 4, structureBoundingBoxIn);
			this.setBlockState(worldIn, wallBlock, 6, 9, 4, structureBoundingBoxIn);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6,11,4,6,13, 4, wallBlock, wallBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6,1,2,6,1, 6, wallBlock, wallBlock, false);
			this.setBlockState(worldIn, wallBlock, 6, 2, 2, structureBoundingBoxIn);
			this.setBlockState(worldIn, wallBlock, 6, 2, 6, structureBoundingBoxIn);
			this.fillWithAir(worldIn, structureBoundingBoxIn, 6, 2, 3, 6, 2, 5);
			
			generateDoor(worldIn, structureBoundingBoxIn, randomIn, 3, 1, 1, EnumFacing.SOUTH, (BlockDoor) doorBlock.getBlock());
			this.setBlockState(worldIn, torch.withProperty(BlockTorch.FACING, EnumFacing.NORTH), 3, 3, 6, structureBoundingBoxIn);
			this.setBlockState(worldIn, torch.withProperty(BlockTorch.FACING, EnumFacing.SOUTH), 3, 7, 2, structureBoundingBoxIn);
			
			if(!spawns[0])
				spawns[0] = this.spawnConsort(2, 1, 4,structureBoundingBoxIn, worldIn);
			if(!spawns[1])
				spawns[1] = this.spawnConsort(3, 1, 5,structureBoundingBoxIn, worldIn);
			if(!spawns[2])
				spawns[2] = this.spawnConsort(4, 1, 4,structureBoundingBoxIn, worldIn);
			
			return true;
		}
	}
	
	public static class SmallTowerStore extends ConsortVillageComponents.ConsortVillagePiece
	{
		public SmallTowerStore()
		{
			spawns = new boolean[1];
		}
		
		public SmallTowerStore(ConsortVillageCenter.VillageCenter start, Random rand, StructureBoundingBox boundingBox, EnumFacing facing)
		{
			this();
			setCoordBaseMode(facing);
			this.boundingBox = boundingBox;
		}
		
		public static SmallTowerStore createPiece(ConsortVillageCenter.VillageCenter start, List<StructureComponent> componentList, Random rand, int x, int y, int z, EnumFacing facing)
		{
			StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, 7, 10, 8, facing);
			return StructureComponent.findIntersecting(componentList, structureboundingbox) == null ? new SmallTowerStore(start, rand, structureboundingbox, facing) : null;
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
			IBlockState wallBlock = provider.blockRegistry.getBlockState("structure_primary");
			IBlockState floorBlock = provider.blockRegistry.getBlockState("structure_secondary");
			IBlockState doorBlock = provider.blockRegistry.getBlockState("village_door");
			IBlockState torch = provider.blockRegistry.getBlockState("torch");
			
			this.fillWithAir(worldIn, structureBoundingBoxIn, 1, 1, 2, 5, 9, 6);
			this.clearFront(worldIn, structureBoundingBoxIn, 1, 5, 1, 0);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2,0,1,4,0, 7, floorBlock, floorBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0,0,3,0,0, 5, floorBlock, floorBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6,0,3,6,0, 5, floorBlock, floorBlock, false);
			this.setBlockState(worldIn, floorBlock, 1, 0, 2, structureBoundingBoxIn);
			this.setBlockState(worldIn, floorBlock, 1, 0, 6, structureBoundingBoxIn);
			this.setBlockState(worldIn, floorBlock, 5, 0, 2, structureBoundingBoxIn);
			this.setBlockState(worldIn, floorBlock, 5, 0, 6, structureBoundingBoxIn);
			
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2,1,1,2,7, 1, wallBlock, wallBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 4,1,1,4,7, 1, wallBlock, wallBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3,3,1,3,5, 1, wallBlock, wallBlock, false);
			this.setBlockState(worldIn, wallBlock, 3, 8, 1, structureBoundingBoxIn);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2,1,7,2,7, 7, wallBlock, wallBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 4,1,7,4,7, 7, wallBlock, wallBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3,1,7,3,5, 7, wallBlock, wallBlock, false);
			this.setBlockState(worldIn, wallBlock, 3, 8, 7, structureBoundingBoxIn);
			
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1,1,2,1,6, 2, wallBlock, wallBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0,1,3,0,5, 3, wallBlock, wallBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0,1,4,0,4, 4, wallBlock, wallBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0,1,5,0,5, 5, wallBlock, wallBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1,1,6,1,6, 6, wallBlock, wallBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5,1,2,5,6, 2, wallBlock, wallBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6,1,3,6,5, 3, wallBlock, wallBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6,1,4,6,4, 4, wallBlock, wallBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6,1,5,6,5, 5, wallBlock, wallBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5,1,6,5,6, 6, wallBlock, wallBlock, false);
			
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1,1,4,5,1, 4, wallBlock, wallBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2,4,2,4,4, 6, wallBlock, wallBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1,4,3,1,4, 5, wallBlock, wallBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5,4,3,5,4, 5, wallBlock, wallBlock, false);
			
			this.setBlockState(worldIn, floorBlock, 2, 8, 1, structureBoundingBoxIn);
			this.setBlockState(worldIn, floorBlock, 3, 9, 1, structureBoundingBoxIn);
			this.setBlockState(worldIn, floorBlock, 4, 8, 1, structureBoundingBoxIn);
			this.setBlockState(worldIn, floorBlock, 5, 7, 2, structureBoundingBoxIn);
			this.setBlockState(worldIn, floorBlock, 6, 6, 3, structureBoundingBoxIn);
			this.setBlockState(worldIn, floorBlock, 6, 5, 4, structureBoundingBoxIn);
			this.setBlockState(worldIn, floorBlock, 6, 6, 5, structureBoundingBoxIn);
			this.setBlockState(worldIn, floorBlock, 5, 7, 6, structureBoundingBoxIn);
			this.setBlockState(worldIn, floorBlock, 4, 8, 7, structureBoundingBoxIn);
			this.setBlockState(worldIn, floorBlock, 3, 9, 7, structureBoundingBoxIn);
			this.setBlockState(worldIn, floorBlock, 2, 8, 7, structureBoundingBoxIn);
			this.setBlockState(worldIn, floorBlock, 1, 7, 6, structureBoundingBoxIn);
			this.setBlockState(worldIn, floorBlock, 0, 6, 5, structureBoundingBoxIn);
			this.setBlockState(worldIn, floorBlock, 0, 5, 4, structureBoundingBoxIn);
			this.setBlockState(worldIn, floorBlock, 0, 6, 3, structureBoundingBoxIn);
			this.setBlockState(worldIn, floorBlock, 1, 7, 2, structureBoundingBoxIn);
			
			this.setBlockState(worldIn, torch.withProperty(BlockTorch.FACING, EnumFacing.NORTH), 1, 3, 5, structureBoundingBoxIn);
			this.setBlockState(worldIn, torch.withProperty(BlockTorch.FACING, EnumFacing.NORTH), 5, 3, 5, structureBoundingBoxIn);
			
			generateDoor(worldIn, structureBoundingBoxIn, randomIn, 3, 1, 1, EnumFacing.SOUTH, (BlockDoor) doorBlock.getBlock());
			
			if(!spawns[0])
				spawns[0] = spawnConsort(3, 1, 5,structureBoundingBoxIn, worldIn, EnumConsort.getRandomMerchant(randomIn), 1);
			
			return true;
		}
	}
}