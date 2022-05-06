package com.mraof.minestuck.world.gen.feature.structure.village;

import com.mraof.minestuck.entity.consort.EnumConsort;
import com.mraof.minestuck.world.gen.feature.MSStructurePieces;
import com.mraof.minestuck.world.gen.feature.structure.blocks.StructureBlockRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.WallTorchBlock;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.DoorHingeSide;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.List;
import java.util.Random;

public class SalamanderVillagePieces
{
	public static class RuinedTowerMushroomCenter extends ConsortVillageCenter.VillageCenter
	{
		public RuinedTowerMushroomCenter(List<ConsortVillagePieces.PieceWeight> pieceWeightList, int x, int z, Random rand)
		{
			super(MSStructurePieces.MUSHROOM_TOWER_CENTER, pieceWeightList, 0, 0);
			this.setOrientation(Direction.Plane.HORIZONTAL.getRandomDirection(rand));
			
			this.boundingBox = new MutableBoundingBox(x, 64, z, x + 9 - 1, 80, z + 9 - 1);
		}
		
		public RuinedTowerMushroomCenter(TemplateManager templates, CompoundNBT nbt)
		{
			super(MSStructurePieces.MUSHROOM_TOWER_CENTER, nbt, 0);
		}
		
		@Override
		public void addChildren(StructurePiece componentIn, List<StructurePiece> listIn, Random rand)
		{
			ConsortVillagePieces.generateAndAddRoadPiece((ConsortVillageCenter.VillageCenter) componentIn, listIn, rand, boundingBox.x0 + 4, boundingBox.y0, boundingBox.z1 + 1, Direction.SOUTH);
			ConsortVillagePieces.generateAndAddRoadPiece((ConsortVillageCenter.VillageCenter) componentIn, listIn, rand, boundingBox.x0 - 1, boundingBox.y0, boundingBox.z0 + 4, Direction.WEST);
			ConsortVillagePieces.generateAndAddRoadPiece((ConsortVillageCenter.VillageCenter) componentIn, listIn, rand, boundingBox.x0 + 4, boundingBox.y0, boundingBox.z0 - 1, Direction.NORTH);
			ConsortVillagePieces.generateAndAddRoadPiece((ConsortVillageCenter.VillageCenter) componentIn, listIn, rand, boundingBox.x1 + 1, boundingBox.y0, boundingBox.z0 + 4, Direction.EAST);
		}

		@Override
		public boolean postProcess(ISeedReader worldIn, StructureManager manager, ChunkGenerator chunkGeneratorIn, Random randomIn, MutableBoundingBox structureBoundingBoxIn, ChunkPos chunkPosIn, BlockPos pos)
		{
			if (this.averageGroundLvl < 0)
			{
				this.averageGroundLvl = this.getAverageGroundLevel(worldIn, chunkGeneratorIn, structureBoundingBoxIn);

				if (this.averageGroundLvl < 0)
				{
					return true;
				}

				this.boundingBox.move(0, this.averageGroundLvl - this.boundingBox.y0 - 1, 0);
			}

			StructureBlockRegistry blocks = StructureBlockRegistry.getOrDefault(chunkGeneratorIn);
			BlockState primary = blocks.getBlockState("structure_primary");
			BlockState stairsN = blocks.getStairs("structure_primary_stairs", Direction.NORTH, false);
			BlockState stairsE = blocks.getStairs("structure_primary_stairs", Direction.EAST, false);
			BlockState stairsS = blocks.getStairs("structure_primary_stairs", Direction.SOUTH, false);
			BlockState stairsW = blocks.getStairs("structure_primary_stairs", Direction.WEST, false);
			BlockState secondary = blocks.getBlockState("structure_secondary");
			BlockState ground = blocks.getBlockState("surface");
			BlockState mushroom1 = blocks.getBlockState("mushroom_1");
			BlockState mushroom2 = blocks.getBlockState("mushroom_2");

			this.generateAirBox(worldIn, structureBoundingBoxIn, 3, 1, 2, 5, 7, 6);
			this.generateAirBox(worldIn, structureBoundingBoxIn, 2, 1, 3, 2, 7, 5);
			this.generateAirBox(worldIn, structureBoundingBoxIn, 6, 1, 3, 6, 7, 5);

			this.generateAirBox(worldIn, structureBoundingBoxIn, 4, 2, 1, 4, 4, 1);
			this.generateAirBox(worldIn, structureBoundingBoxIn, 4, 2, 7, 4, 4, 7);
			this.generateAirBox(worldIn, structureBoundingBoxIn, 1, 2, 4, 1, 4, 4);
			this.generateAirBox(worldIn, structureBoundingBoxIn, 7, 2, 4, 7, 4, 4);

			this.generateBox(worldIn, structureBoundingBoxIn, 4, 0, 2, 4, 0, 6, secondary, secondary, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 3, 0, 3, 4, 0, 5, secondary, secondary, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 4, 0, 3, 5, 0, 5, secondary, secondary, false);
			this.placeBlock(worldIn, secondary, 2, 0, 4, structureBoundingBoxIn);
			this.placeBlock(worldIn, secondary, 6, 0, 4, structureBoundingBoxIn);

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

			this.generateBox(worldIn, structureBoundingBoxIn, 2, 1, 0, 6, 1, 0, stairsS, stairsS, false);
			this.placeBlock(worldIn, stairsE, 2, 1, 1, structureBoundingBoxIn);
			this.placeBlock(worldIn, stairsW, 6, 1, 1, structureBoundingBoxIn);
			this.placeBlock(worldIn, stairsS, 1, 1, 1, structureBoundingBoxIn);
			this.placeBlock(worldIn, stairsS, 7, 1, 1, structureBoundingBoxIn);
			this.placeBlock(worldIn, stairsE, 1, 1, 2, structureBoundingBoxIn);
			this.placeBlock(worldIn, stairsW, 7, 1, 2, structureBoundingBoxIn);
			this.placeBlock(worldIn, stairsS, 0, 1, 2, structureBoundingBoxIn);
			this.placeBlock(worldIn, stairsS, 8, 1, 2, structureBoundingBoxIn);
			this.generateBox(worldIn, structureBoundingBoxIn, 0, 1, 3, 0, 1, 5, stairsE, stairsE, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 8, 1, 3, 8, 1, 5, stairsW, stairsW, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 2, 1, 8, 6, 1, 8, stairsN, stairsN, false);
			this.placeBlock(worldIn, stairsE, 2, 1, 7, structureBoundingBoxIn);
			this.placeBlock(worldIn, stairsW, 6, 1, 7, structureBoundingBoxIn);
			this.placeBlock(worldIn, stairsN, 1, 1, 7, structureBoundingBoxIn);
			this.placeBlock(worldIn, stairsN, 7, 1, 7, structureBoundingBoxIn);
			this.placeBlock(worldIn, stairsE, 1, 1, 6, structureBoundingBoxIn);
			this.placeBlock(worldIn, stairsW, 7, 1, 6, structureBoundingBoxIn);
			this.placeBlock(worldIn, stairsN, 0, 1, 6, structureBoundingBoxIn);
			this.placeBlock(worldIn, stairsN, 8, 1, 6, structureBoundingBoxIn);

			this.placeBlock(worldIn, primary, 4, 5, 1, structureBoundingBoxIn);
			this.generateBox(worldIn, structureBoundingBoxIn, 3, 2, 1, 3, 6, 1, primary, primary, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 5, 2, 1, 5, 6, 1, primary, primary, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 2, 2, 2, 2, 7, 2, primary, primary, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 6, 2, 2, 6, 7, 2, primary, primary, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 1, 2, 3, 1, 8, 3, primary, primary, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 7, 2, 3, 7, 8, 3, primary, primary, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 1, 5, 4, 1, 9, 4, primary, primary, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 7, 5, 4, 7, 9, 4, primary, primary, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 1, 2, 5, 1, 10, 5, primary, primary, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 7, 2, 5, 7, 10, 5, primary, primary, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 2, 2, 6, 2, 11, 6, primary, primary, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 6, 2, 6, 6, 11, 6, primary, primary, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 3, 2, 7, 3, 12, 7, primary, primary, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 5, 2, 7, 5, 12, 7, primary, primary, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 4, 5, 7, 4, 7, 7, primary, primary, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 4, 11, 7, 4, 13, 7, primary, primary, false);

			this.placeBlock(worldIn, secondary, 4, 6, 1, structureBoundingBoxIn);
			this.placeBlock(worldIn, secondary, 3, 7, 1, structureBoundingBoxIn);
			this.placeBlock(worldIn, secondary, 5, 7, 1, structureBoundingBoxIn);
			this.placeBlock(worldIn, secondary, 2, 8, 2, structureBoundingBoxIn);
			this.placeBlock(worldIn, secondary, 6, 8, 2, structureBoundingBoxIn);
			this.placeBlock(worldIn, secondary, 1, 9, 3, structureBoundingBoxIn);
			this.placeBlock(worldIn, secondary, 7, 9, 3, structureBoundingBoxIn);
			this.placeBlock(worldIn, secondary, 1, 10, 4, structureBoundingBoxIn);
			this.placeBlock(worldIn, secondary, 7, 10, 4, structureBoundingBoxIn);
			this.placeBlock(worldIn, secondary, 1, 11, 5, structureBoundingBoxIn);
			this.placeBlock(worldIn, secondary, 7, 11, 5, structureBoundingBoxIn);
			this.placeBlock(worldIn, secondary, 2, 12, 6, structureBoundingBoxIn);
			this.placeBlock(worldIn, secondary, 6, 12, 6, structureBoundingBoxIn);
			this.placeBlock(worldIn, secondary, 3, 13, 7, structureBoundingBoxIn);
			this.placeBlock(worldIn, secondary, 5, 13, 7, structureBoundingBoxIn);
			this.placeBlock(worldIn, secondary, 4, 14, 7, structureBoundingBoxIn);

			this.placeBlock(worldIn, randomIn.nextBoolean() ? mushroom1 :mushroom2, 3, 1, 2, structureBoundingBoxIn);
			this.placeBlock(worldIn, randomIn.nextBoolean() ? mushroom1 :mushroom2, 5, 1, 2, structureBoundingBoxIn);
			this.placeBlock(worldIn, randomIn.nextBoolean() ? mushroom1 :mushroom2, 2, 1, 3, structureBoundingBoxIn);
			this.placeBlock(worldIn, randomIn.nextBoolean() ? mushroom1 :mushroom2, 6, 1, 3, structureBoundingBoxIn);
			this.placeBlock(worldIn, randomIn.nextBoolean() ? mushroom1 :mushroom2, 3, 1, 6, structureBoundingBoxIn);
			this.placeBlock(worldIn, randomIn.nextBoolean() ? mushroom1 :mushroom2, 5, 1, 6, structureBoundingBoxIn);
			this.placeBlock(worldIn, randomIn.nextBoolean() ? mushroom1 :mushroom2, 2, 1, 5, structureBoundingBoxIn);
			this.placeBlock(worldIn, randomIn.nextBoolean() ? mushroom1 :mushroom2, 6, 1, 5, structureBoundingBoxIn);

			this.placeBlock(worldIn, randomIn.nextBoolean() ? mushroom1 :mushroom2, 4, 8, 7, structureBoundingBoxIn);
			this.placeBlock(worldIn, randomIn.nextBoolean() ? mushroom1 :mushroom2, 4, 7, 1, structureBoundingBoxIn);
			this.placeBlock(worldIn, randomIn.nextBoolean() ? mushroom1 :mushroom2, 4, 15, 7, structureBoundingBoxIn);
			this.placeBlock(worldIn, randomIn.nextBoolean() ? mushroom1 :mushroom2, 1, 11, 4, structureBoundingBoxIn);
			this.placeBlock(worldIn, randomIn.nextBoolean() ? mushroom1 :mushroom2, 7, 11, 4, structureBoundingBoxIn);

			return true;
		}
	}
	
	public static class PipeHouse1 extends ConsortVillagePieces.ConsortVillagePiece
	{
		public PipeHouse1(ConsortVillageCenter.VillageCenter start, Random rand, MutableBoundingBox boundingBox, Direction facing)
		{
			super(MSStructurePieces.PIPE_HOUSE_1, 0, 2);
			this.setOrientation(facing);
			this.boundingBox = boundingBox;
		}
		
		public PipeHouse1(TemplateManager templates, CompoundNBT nbt)
		{
			super(MSStructurePieces.PIPE_HOUSE_1, nbt, 2);
		}
		
		public static PipeHouse1 createPiece(ConsortVillageCenter.VillageCenter start, List<StructurePiece> componentList, Random rand, int x, int y, int z, Direction facing)
		{
			MutableBoundingBox structureboundingbox = MutableBoundingBox.orientBox(x, y, z, 0, 0, 0, 6, 5, 7, facing);
			return StructurePiece.findCollisionPiece(componentList, structureboundingbox) == null ? new PipeHouse1(start, rand, structureboundingbox, facing) : null;
		}

		@Override
		public boolean postProcess(ISeedReader worldIn, StructureManager manager, ChunkGenerator chunkGeneratorIn, Random randomIn, MutableBoundingBox structureBoundingBoxIn, ChunkPos chunkPosIn, BlockPos pos)
		{
			if (this.averageGroundLvl < 0)
			{
				this.averageGroundLvl = this.getAverageGroundLevel(worldIn, chunkGeneratorIn, structureBoundingBoxIn);

				if (this.averageGroundLvl < 0)
				{
					return true;
				}

				this.boundingBox.move(0, this.averageGroundLvl - this.boundingBox.y0 - 1, 0);
			}

			StructureBlockRegistry blocks = StructureBlockRegistry.getOrDefault(chunkGeneratorIn);
			BlockState wallBlock = blocks.getBlockState("structure_primary");
			BlockState floorBlock = blocks.getBlockState("salamander_floor");
			BlockState doorBlock = blocks.getBlockState("village_door");
			BlockState torch = blocks.getBlockState("wall_torch");

			this.generateAirBox(worldIn, structureBoundingBoxIn, 1, 1, 2, 4, 5, 5);
			this.clearFront(worldIn, structureBoundingBoxIn, 1, 4, 1, 0);
			this.generateBox(worldIn, structureBoundingBoxIn, 1,0,1,4,0, 6, floorBlock, floorBlock, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 0,0,2,0,0, 5, floorBlock, floorBlock, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 5,0,2,5,0, 5, floorBlock, floorBlock, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 1,1,1,2,2, 1, wallBlock, wallBlock, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 4,1,1,4,2, 1, wallBlock, wallBlock, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 1,3,1,4,5, 1, wallBlock, wallBlock, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 0,1,2,0,5, 5, wallBlock, wallBlock, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 1,1,6,4,5, 6, wallBlock, wallBlock, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 5,1,2,5,1, 5, wallBlock, wallBlock, false);
			this.placeBlock(worldIn, wallBlock, 5, 2, 2, structureBoundingBoxIn);
			this.placeBlock(worldIn, wallBlock, 5, 2, 5, structureBoundingBoxIn);
			this.generateBox(worldIn, structureBoundingBoxIn, 5,3,2,5,5, 5, wallBlock, wallBlock, false);
			generateDoor(worldIn, structureBoundingBoxIn, randomIn, 3, 1, 1, Direction.SOUTH, doorBlock.getBlock(), DoorHingeSide.LEFT);

			placeBlock(worldIn, torch.setValue(WallTorchBlock.FACING, Direction.EAST), 1, 3, 4, structureBoundingBoxIn);

			if(!spawns[0])
				spawns[0] = spawnConsort(2, 1, 3, structureBoundingBoxIn, worldIn, chunkGeneratorIn);
			if(!spawns[1])
				spawns[1] = spawnConsort(3, 1, 4,structureBoundingBoxIn, worldIn, chunkGeneratorIn);
			return true;
		}
	}
	
	public static class HighPipeHouse1 extends ConsortVillagePieces.ConsortVillagePiece
	{
		public HighPipeHouse1(ConsortVillageCenter.VillageCenter start, Random rand, MutableBoundingBox boundingBox, Direction facing)
		{
			super(MSStructurePieces.HIGH_PIPE_HOUSE_1, 0, 3);
			this.setOrientation(facing);
			this.boundingBox = boundingBox;
		}
		
		public HighPipeHouse1(TemplateManager templates, CompoundNBT nbt)
		{
			super(MSStructurePieces.HIGH_PIPE_HOUSE_1, nbt, 3);
		}
		
		public static HighPipeHouse1 createPiece(ConsortVillageCenter.VillageCenter start, List<StructurePiece> componentList, Random rand, int x, int y, int z, Direction facing)
		{
			MutableBoundingBox structureboundingbox = MutableBoundingBox.orientBox(x, y, z, 0, 0, 0, 7, 13, 8, facing);
			return StructurePiece.findCollisionPiece(componentList, structureboundingbox) == null ? new HighPipeHouse1(start, rand, structureboundingbox, facing) : null;
		}

		@Override
		public boolean postProcess(ISeedReader worldIn, StructureManager manager, ChunkGenerator chunkGeneratorIn, Random randomIn, MutableBoundingBox structureBoundingBoxIn, ChunkPos chunkPosIn, BlockPos pos)
		{
			if (this.averageGroundLvl < 0)
			{
				this.averageGroundLvl = this.getAverageGroundLevel(worldIn, chunkGeneratorIn, structureBoundingBoxIn);

				if (this.averageGroundLvl < 0)
				{
					return true;
				}

				this.boundingBox.move(0, this.averageGroundLvl - this.boundingBox.y0 - 1, 0);
			}

			StructureBlockRegistry blocks = StructureBlockRegistry.getOrDefault(chunkGeneratorIn);
			BlockState wallBlock = blocks.getBlockState("structure_primary");
			BlockState floorBlock = blocks.getBlockState("salamander_floor");
			BlockState doorBlock = blocks.getBlockState("village_door");
			BlockState torch = blocks.getBlockState("wall_torch");

			this.generateAirBox(worldIn, structureBoundingBoxIn, 1, 1, 2, 5, 13, 6);
			this.clearFront(worldIn, structureBoundingBoxIn, 1, 5, 1, 0);
			this.generateBox(worldIn, structureBoundingBoxIn, 1,0,1,5,0, 7, floorBlock, floorBlock, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 0,0,2,0,0, 6, floorBlock, floorBlock, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 6,0,2,6,0, 6, floorBlock, floorBlock, false);

			this.generateBox(worldIn, structureBoundingBoxIn, 1,1,1,2,13, 1, wallBlock, wallBlock, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 4,1,1,5,13, 1, wallBlock, wallBlock, false);
			this.placeBlock(worldIn, wallBlock, 3, 3, 1, structureBoundingBoxIn);
			this.placeBlock(worldIn, wallBlock, 3, 5, 1, structureBoundingBoxIn);
			this.placeBlock(worldIn, wallBlock, 3, 7, 1, structureBoundingBoxIn);
			this.placeBlock(worldIn, wallBlock, 3, 9, 1, structureBoundingBoxIn);
			this.generateBox(worldIn, structureBoundingBoxIn, 3,11,1,3,13, 1, wallBlock, wallBlock, false);

			this.generateBox(worldIn, structureBoundingBoxIn, 1,1,7,2,13, 7, wallBlock, wallBlock, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 4,1,7,5,13, 7, wallBlock, wallBlock, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 3,1,7,3,3, 7, wallBlock, wallBlock, false);
			this.placeBlock(worldIn, wallBlock, 3, 5, 7, structureBoundingBoxIn);
			this.placeBlock(worldIn, wallBlock, 3, 7, 7, structureBoundingBoxIn);
			this.placeBlock(worldIn, wallBlock, 3, 9, 7, structureBoundingBoxIn);
			this.generateBox(worldIn, structureBoundingBoxIn, 3,11,7,3,13, 7, wallBlock, wallBlock, false);

			this.generateBox(worldIn, structureBoundingBoxIn, 0,3,2,0,13, 3, wallBlock, wallBlock, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 0,3,5,0,13, 6, wallBlock, wallBlock, false);
			this.placeBlock(worldIn, wallBlock, 0, 3, 4, structureBoundingBoxIn);
			this.placeBlock(worldIn, wallBlock, 0, 5, 4, structureBoundingBoxIn);
			this.placeBlock(worldIn, wallBlock, 0, 7, 4, structureBoundingBoxIn);
			this.placeBlock(worldIn, wallBlock, 0, 9, 4, structureBoundingBoxIn);
			this.generateBox(worldIn, structureBoundingBoxIn, 0,11,4,0,13, 4, wallBlock, wallBlock, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 0,1,2,0,1, 6, wallBlock, wallBlock, false);
			this.placeBlock(worldIn, wallBlock, 0, 2, 2, structureBoundingBoxIn);
			this.placeBlock(worldIn, wallBlock, 0, 2, 6, structureBoundingBoxIn);
			this.generateAirBox(worldIn, structureBoundingBoxIn, 0, 2, 3, 0, 2, 5);

			this.generateBox(worldIn, structureBoundingBoxIn, 6,3,2,6,13, 3, wallBlock, wallBlock, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 6,3,5,6,13, 6, wallBlock, wallBlock, false);
			this.placeBlock(worldIn, wallBlock, 6, 3, 4, structureBoundingBoxIn);
			this.placeBlock(worldIn, wallBlock, 6, 5, 4, structureBoundingBoxIn);
			this.placeBlock(worldIn, wallBlock, 6, 7, 4, structureBoundingBoxIn);
			this.placeBlock(worldIn, wallBlock, 6, 9, 4, structureBoundingBoxIn);
			this.generateBox(worldIn, structureBoundingBoxIn, 6,11,4,6,13, 4, wallBlock, wallBlock, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 6,1,2,6,1, 6, wallBlock, wallBlock, false);
			this.placeBlock(worldIn, wallBlock, 6, 2, 2, structureBoundingBoxIn);
			this.placeBlock(worldIn, wallBlock, 6, 2, 6, structureBoundingBoxIn);
			this.generateAirBox(worldIn, structureBoundingBoxIn, 6, 2, 3, 6, 2, 5);

			generateDoor(worldIn, structureBoundingBoxIn, randomIn, 3, 1, 1, Direction.SOUTH, doorBlock.getBlock(), DoorHingeSide.LEFT);
			this.placeBlock(worldIn, torch.setValue(WallTorchBlock.FACING, Direction.NORTH), 3, 3, 6, structureBoundingBoxIn);
			this.placeBlock(worldIn, torch.setValue(WallTorchBlock.FACING, Direction.SOUTH), 3, 7, 2, structureBoundingBoxIn);

			if(!spawns[0])
				spawns[0] = this.spawnConsort(2, 1, 4,structureBoundingBoxIn, worldIn, chunkGeneratorIn);
			if(!spawns[1])
				spawns[1] = this.spawnConsort(3, 1, 5,structureBoundingBoxIn, worldIn, chunkGeneratorIn);
			if(!spawns[2])
				spawns[2] = this.spawnConsort(4, 1, 4,structureBoundingBoxIn, worldIn, chunkGeneratorIn);

			return true;
		}
	}
	
	public static class SmallTowerStore extends ConsortVillagePieces.ConsortVillagePiece
	{
		public SmallTowerStore(ConsortVillageCenter.VillageCenter start, Random rand, MutableBoundingBox boundingBox, Direction facing)
		{
			super(MSStructurePieces.SMALL_TOWER_STORE, 0, 1);
			setOrientation(facing);
			this.boundingBox = boundingBox;
		}
		
		public SmallTowerStore(TemplateManager templates, CompoundNBT nbt)
		{
			super(MSStructurePieces.SMALL_TOWER_STORE, nbt, 1);
		}
		
		public static SmallTowerStore createPiece(ConsortVillageCenter.VillageCenter start, List<StructurePiece> componentList, Random rand, int x, int y, int z, Direction facing)
		{
			MutableBoundingBox boundingBox = MutableBoundingBox.orientBox(x, y, z, 0, 0, 0, 7, 10, 8, facing);
			return StructurePiece.findCollisionPiece(componentList, boundingBox) == null ? new SmallTowerStore(start, rand, boundingBox, facing) : null;
		}

		@Override
		public boolean postProcess(ISeedReader worldIn, StructureManager manager, ChunkGenerator chunkGeneratorIn, Random randomIn, MutableBoundingBox structureBoundingBoxIn, ChunkPos chunkPosIn, BlockPos pos)
		{
			if (this.averageGroundLvl < 0)
			{
				this.averageGroundLvl = this.getAverageGroundLevel(worldIn, chunkGeneratorIn, structureBoundingBoxIn);

				if (this.averageGroundLvl < 0)
				{
					return true;
				}

				this.boundingBox.move(0, this.averageGroundLvl - this.boundingBox.y0 - 1, 0);
			}

			StructureBlockRegistry blocks = StructureBlockRegistry.getOrDefault(chunkGeneratorIn);
			BlockState wallBlock = blocks.getBlockState("structure_primary");
			BlockState floorBlock = blocks.getBlockState("structure_secondary");
			BlockState doorBlock = blocks.getBlockState("village_door");
			BlockState torch = blocks.getBlockState("wall_torch");

			this.generateAirBox(worldIn, structureBoundingBoxIn, 1, 1, 2, 5, 9, 6);
			this.clearFront(worldIn, structureBoundingBoxIn, 1, 5, 1, 0);
			this.generateBox(worldIn, structureBoundingBoxIn, 2,0,1,4,0, 7, floorBlock, floorBlock, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 0,0,3,0,0, 5, floorBlock, floorBlock, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 6,0,3,6,0, 5, floorBlock, floorBlock, false);
			this.placeBlock(worldIn, floorBlock, 1, 0, 2, structureBoundingBoxIn);
			this.placeBlock(worldIn, floorBlock, 1, 0, 6, structureBoundingBoxIn);
			this.placeBlock(worldIn, floorBlock, 5, 0, 2, structureBoundingBoxIn);
			this.placeBlock(worldIn, floorBlock, 5, 0, 6, structureBoundingBoxIn);

			this.generateBox(worldIn, structureBoundingBoxIn, 2,1,1,2,7, 1, wallBlock, wallBlock, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 4,1,1,4,7, 1, wallBlock, wallBlock, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 3,3,1,3,5, 1, wallBlock, wallBlock, false);
			this.placeBlock(worldIn, wallBlock, 3, 8, 1, structureBoundingBoxIn);
			this.generateBox(worldIn, structureBoundingBoxIn, 2,1,7,2,7, 7, wallBlock, wallBlock, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 4,1,7,4,7, 7, wallBlock, wallBlock, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 3,1,7,3,5, 7, wallBlock, wallBlock, false);
			this.placeBlock(worldIn, wallBlock, 3, 8, 7, structureBoundingBoxIn);

			this.generateBox(worldIn, structureBoundingBoxIn, 1,1,2,1,6, 2, wallBlock, wallBlock, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 0,1,3,0,5, 3, wallBlock, wallBlock, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 0,1,4,0,4, 4, wallBlock, wallBlock, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 0,1,5,0,5, 5, wallBlock, wallBlock, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 1,1,6,1,6, 6, wallBlock, wallBlock, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 5,1,2,5,6, 2, wallBlock, wallBlock, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 6,1,3,6,5, 3, wallBlock, wallBlock, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 6,1,4,6,4, 4, wallBlock, wallBlock, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 6,1,5,6,5, 5, wallBlock, wallBlock, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 5,1,6,5,6, 6, wallBlock, wallBlock, false);

			this.generateBox(worldIn, structureBoundingBoxIn, 1,1,4,5,1, 4, wallBlock, wallBlock, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 2,4,2,4,4, 6, wallBlock, wallBlock, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 1,4,3,1,4, 5, wallBlock, wallBlock, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 5,4,3,5,4, 5, wallBlock, wallBlock, false);

			this.placeBlock(worldIn, floorBlock, 2, 8, 1, structureBoundingBoxIn);
			this.placeBlock(worldIn, floorBlock, 3, 9, 1, structureBoundingBoxIn);
			this.placeBlock(worldIn, floorBlock, 4, 8, 1, structureBoundingBoxIn);
			this.placeBlock(worldIn, floorBlock, 5, 7, 2, structureBoundingBoxIn);
			this.placeBlock(worldIn, floorBlock, 6, 6, 3, structureBoundingBoxIn);
			this.placeBlock(worldIn, floorBlock, 6, 5, 4, structureBoundingBoxIn);
			this.placeBlock(worldIn, floorBlock, 6, 6, 5, structureBoundingBoxIn);
			this.placeBlock(worldIn, floorBlock, 5, 7, 6, structureBoundingBoxIn);
			this.placeBlock(worldIn, floorBlock, 4, 8, 7, structureBoundingBoxIn);
			this.placeBlock(worldIn, floorBlock, 3, 9, 7, structureBoundingBoxIn);
			this.placeBlock(worldIn, floorBlock, 2, 8, 7, structureBoundingBoxIn);
			this.placeBlock(worldIn, floorBlock, 1, 7, 6, structureBoundingBoxIn);
			this.placeBlock(worldIn, floorBlock, 0, 6, 5, structureBoundingBoxIn);
			this.placeBlock(worldIn, floorBlock, 0, 5, 4, structureBoundingBoxIn);
			this.placeBlock(worldIn, floorBlock, 0, 6, 3, structureBoundingBoxIn);
			this.placeBlock(worldIn, floorBlock, 1, 7, 2, structureBoundingBoxIn);

			this.placeBlock(worldIn, torch.setValue(WallTorchBlock.FACING, Direction.NORTH), 1, 3, 5, structureBoundingBoxIn);
			this.placeBlock(worldIn, torch.setValue(WallTorchBlock.FACING, Direction.NORTH), 5, 3, 5, structureBoundingBoxIn);

			generateDoor(worldIn, structureBoundingBoxIn, randomIn, 3, 1, 1, Direction.SOUTH, doorBlock.getBlock(), DoorHingeSide.LEFT);

			if(!spawns[0])
				spawns[0] = spawnConsort(3, 1, 5,structureBoundingBoxIn, worldIn, chunkGeneratorIn, EnumConsort.getRandomMerchant(randomIn), 1);

			return true;
		}
	}
}