package com.mraof.minestuck.world.gen.feature.structure.village;

import com.mraof.minestuck.entity.consort.EnumConsort;
import com.mraof.minestuck.world.gen.feature.MSStructurePieces;
import com.mraof.minestuck.world.gen.feature.structure.blocks.StructureBlockRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.DoorHingeSide;
import net.minecraft.util.Direction;
import net.minecraft.util.Rotation;
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

public class TurtleVillagePieces
{
	public static class TurtleWellCenter extends ConsortVillageCenter.VillageCenter
	{
		public TurtleWellCenter(List<ConsortVillagePieces.PieceWeight> pieceWeightList, int x, int z, Random rand)
		{
			super(MSStructurePieces.TURTLE_WELL_CENTER, pieceWeightList, 0, 0);
			this.setOrientation(Direction.Plane.HORIZONTAL.getRandomDirection(rand));
			
			this.boundingBox = new MutableBoundingBox(x, 60, z, x + 8 - 1, 70, z + 8 - 1);
		}
		
		public TurtleWellCenter(TemplateManager templates, CompoundNBT nbt)
		{
			super(MSStructurePieces.TURTLE_WELL_CENTER, nbt, 0);
		}
		
		@Override
		public void addChildren(StructurePiece componentIn, List<StructurePiece> listIn, Random rand)
		{
			ConsortVillagePieces.generateAndAddRoadPiece((ConsortVillageCenter.VillageCenter) componentIn, listIn, rand, boundingBox.x0 + 3, boundingBox.y0, boundingBox.z1 + 1, Direction.SOUTH);
			ConsortVillagePieces.generateAndAddRoadPiece((ConsortVillageCenter.VillageCenter) componentIn, listIn, rand, boundingBox.x0 - 1, boundingBox.y0, boundingBox.z0 + 3, Direction.WEST);
			ConsortVillagePieces.generateAndAddRoadPiece((ConsortVillageCenter.VillageCenter) componentIn, listIn, rand, boundingBox.x0 + 3, boundingBox.y0, boundingBox.z0 - 1, Direction.NORTH);
			ConsortVillagePieces.generateAndAddRoadPiece((ConsortVillageCenter.VillageCenter) componentIn, listIn, rand, boundingBox.x1 + 1, boundingBox.y0, boundingBox.z0 + 3, Direction.EAST);
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

				this.boundingBox.move(0, this.averageGroundLvl - this.boundingBox.y0 - 5, 0);
			}

			StructureBlockRegistry blocks = StructureBlockRegistry.getOrDefault(chunkGeneratorIn);
			BlockState primary = blocks.getBlockState("structure_primary");
			BlockState secondary = blocks.getBlockState("structure_secondary");
			BlockState fluid = blocks.getBlockState("ocean");
			BlockState stairsN = blocks.getStairs("structure_primary_stairs", Direction.NORTH, false);
			BlockState stairsE = blocks.getStairs("structure_primary_stairs", Direction.EAST, false);
			BlockState stairsS = blocks.getStairs("structure_primary_stairs", Direction.SOUTH, false);
			BlockState stairsW = blocks.getStairs("structure_primary_stairs", Direction.WEST, false);

			this.generateBox(worldIn, structureBoundingBoxIn, 0, 4, 0, 2, 4, 0, primary, primary, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 0, 4, 1, 0, 4, 2, primary, primary, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 5, 4, 0, 7, 4, 0, primary, primary, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 7, 4, 1, 7, 4, 2, primary, primary, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 0, 4, 7, 2, 4, 7, primary, primary, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 0, 4, 5, 0, 4, 6, primary, primary, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 5, 4, 7, 7, 4, 7, primary, primary, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 7, 4, 5, 7, 4, 6, primary, primary, false);

			this.generateBox(worldIn, structureBoundingBoxIn, 3, 4, 0, 4, 4, 0, stairsN, stairsN, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 3, 4, 7, 4, 4, 7, stairsS, stairsS, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 0, 4, 3, 0, 4, 4, stairsW, stairsW, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 7, 4, 3, 7, 4, 4, stairsE, stairsE, false);

			this.generateAirBox(worldIn, structureBoundingBoxIn, 1, 5, 0, 6, 7, 0);
			this.generateAirBox(worldIn, structureBoundingBoxIn, 1, 5, 7, 6, 7, 7);
			this.generateAirBox(worldIn, structureBoundingBoxIn, 0, 5, 1, 0, 7, 6);
			this.generateAirBox(worldIn, structureBoundingBoxIn, 7, 5, 1, 7, 7, 6);

			this.generateAirBox(worldIn, structureBoundingBoxIn, 1, 4, 1, 6, 8, 6);
			this.generateAirBox(worldIn, structureBoundingBoxIn, 2, 3, 2, 5, 3, 5);

			this.generateBox(worldIn, structureBoundingBoxIn, 0, 5, 0, 0, 7, 0, primary, primary, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 0, 5, 7, 0, 7, 7, primary, primary, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 7, 5, 0, 7, 7, 0, primary, primary, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 7, 5, 7, 7, 7, 7, primary, primary, false);

			this.generateBox(worldIn, structureBoundingBoxIn, 0, 8, 0, 7, 8, 0, primary, primary, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 0, 8, 7, 7, 8, 7, primary, primary, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 0, 8, 1, 0, 8, 6, primary, primary, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 7, 8, 1, 7, 8, 6, primary, primary, false);
			this.placeBlock(worldIn, primary, 1, 8, 1, structureBoundingBoxIn);
			this.placeBlock(worldIn, primary, 1, 8, 6, structureBoundingBoxIn);
			this.placeBlock(worldIn, primary, 6, 8, 1, structureBoundingBoxIn);
			this.placeBlock(worldIn, primary, 6, 8, 6, structureBoundingBoxIn);

			this.generateBox(worldIn, structureBoundingBoxIn, 1, 9, 1, 6, 9, 6, primary, primary, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 2, 10, 2, 5, 10, 5, primary, primary, false);

			this.generateBox(worldIn, structureBoundingBoxIn, 1, 1, 1, 6, 3, 1, secondary, secondary, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 1, 1, 6, 6, 3, 6, secondary, secondary, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 1, 1, 2, 1, 3, 5, secondary, secondary, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 6, 1, 2, 6, 3, 5, secondary, secondary, false);

			this.generateBox(worldIn, structureBoundingBoxIn, 3, 3, 1, 4, 3, 1, stairsN, stairsN, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 3, 3, 6, 4, 3, 6, stairsS, stairsS, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 1, 3, 3, 1, 3, 4, stairsW, stairsW, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 6, 3, 3, 6, 3, 4, stairsE, stairsE, false);

			this.generateBox(worldIn, structureBoundingBoxIn, 2, 0, 2, 5, 1, 5, secondary, secondary, false);

			this.generateBox(worldIn, structureBoundingBoxIn, 2, 2, 2, 5, 2, 5, fluid, fluid, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 3, 1, 3, 4, 1, 4, fluid, fluid, false);

			return true;
		}
		
	}
	
	public static class ShellHouse1 extends ConsortVillagePieces.ConsortVillagePiece
	{
		ShellHouse1(ConsortVillageCenter.VillageCenter start, Random rand, MutableBoundingBox boundingBox, Direction facing)
		{
			super(MSStructurePieces.SHELL_HOUSE_1, 0, 2);
			setOrientation(facing);
			this.boundingBox = boundingBox;
		}
		
		public ShellHouse1(TemplateManager templates, CompoundNBT nbt)
		{
			super(MSStructurePieces.SHELL_HOUSE_1, nbt, 2);
		}
		
		public static ShellHouse1 createPiece(ConsortVillageCenter.VillageCenter start, List<StructurePiece> componentList, Random rand, int x, int y, int z, Direction facing)
		{
			MutableBoundingBox structureboundingbox = MutableBoundingBox.orientBox(x, y, z, 0, -2, 0, 8, 5, 9, facing);
			return StructurePiece.findCollisionPiece(componentList, structureboundingbox) == null ? new ShellHouse1(start, rand, structureboundingbox, facing) : null;
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

				this.boundingBox.move(0, this.averageGroundLvl - this.boundingBox.y0 - 3, 0);
			}

			StructureBlockRegistry blocks = StructureBlockRegistry.getOrDefault(chunkGeneratorIn);
			BlockState buildBlock = blocks.getBlockState("structure_primary");
			BlockState lightBlock = blocks.getBlockState("light_block");

			this.generateAirBox(worldIn, structureBoundingBoxIn, 1, 1, 2, 6, 4, 7);
			this.generateAirBox(worldIn, structureBoundingBoxIn, 3, 3, 1, 4, 4, 1);
			this.generateAirBox(worldIn, structureBoundingBoxIn, 2, 5, 6, 2, 5, 6);

			this.clearFront(worldIn, structureBoundingBoxIn, 2, 5, 3, 0);

			this.generateBox(worldIn, structureBoundingBoxIn, 0, 0, 1, 7, 0, 8, buildBlock, buildBlock, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 0, 1, 8, 7, 4, 8, buildBlock, buildBlock, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 0, 1, 1, 0, 4, 7, buildBlock, buildBlock, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 7, 1, 1, 7, 4, 7, buildBlock, buildBlock, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 1, 1, 1, 6, 2, 1, buildBlock, buildBlock, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 1, 3, 1, 2, 4, 1, buildBlock, buildBlock, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 5, 3, 1, 6, 4, 1, buildBlock, buildBlock, false);

			this.generateBox(worldIn, structureBoundingBoxIn, 3, 1, 2, 4, 1, 2, buildBlock, buildBlock, false);

			this.generateBox(worldIn, structureBoundingBoxIn, 3, 5, 1, 4, 5, 1, buildBlock, buildBlock, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 1, 5, 2, 6, 5, 2, buildBlock, buildBlock, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 1, 5, 3, 1, 5, 7, buildBlock, buildBlock, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 6, 5, 3, 6, 5, 7, buildBlock, buildBlock, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 2, 5, 7, 5, 5, 7, buildBlock, buildBlock, false);

			this.generateBox(worldIn, structureBoundingBoxIn, 2, 6, 3, 5, 6, 6, buildBlock, buildBlock, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 3, 6, 4, 4, 6, 5, lightBlock, lightBlock, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 3, 7, 4, 4, 7, 5, buildBlock, buildBlock, false);

			if(!spawns[0])
				spawns[0] = spawnConsort(2, 1, 5, structureBoundingBoxIn, worldIn, chunkGeneratorIn);
			if(!spawns[1])
				spawns[1] = spawnConsort(5, 1, 5, structureBoundingBoxIn, worldIn, chunkGeneratorIn);

			return true;
		}
	}
	
	public static class TurtleMarket1 extends ConsortVillagePieces.ConsortVillagePiece
	{
		TurtleMarket1(ConsortVillageCenter.VillageCenter start, Random rand, MutableBoundingBox boundingBox, Direction facing)
		{
			super(MSStructurePieces.TURTLE_MARKET_1, 0, 2);
			setOrientation(facing);
			this.boundingBox = boundingBox;
		}
		
		public TurtleMarket1(TemplateManager templates, CompoundNBT nbt)
		{
			super(MSStructurePieces.TURTLE_MARKET_1, nbt, 2);
		}
		
		public static TurtleMarket1 createPiece(ConsortVillageCenter.VillageCenter start, List<StructurePiece> componentList, Random rand, int x, int y, int z, Direction facing)
		{
			MutableBoundingBox structureboundingbox = MutableBoundingBox.orientBox(x, y, z, 0, 0, 0, 14, 7, 19, facing);
			return StructurePiece.findCollisionPiece(componentList, structureboundingbox) == null ? new TurtleMarket1(start, rand, structureboundingbox, facing) : null;
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
			BlockState primaryBlock = blocks.getBlockState("structure_primary");
			BlockState primaryDecorBlock = blocks.getBlockState("structure_primary_decorative");
			BlockState secondaryBlock = blocks.getBlockState("structure_secondary");
			BlockState lightBlock = blocks.getBlockState("light_block");
			BlockState glassBlock = blocks.getBlockState("glass");

			this.generateAirBox(worldIn, structureBoundingBoxIn, 6, 1, 1, 7, 3, 3);
			this.generateAirBox(worldIn, structureBoundingBoxIn, 1, 1, 4, 12, 4, 13);
			this.generateAirBox(worldIn, structureBoundingBoxIn, 1, 2, 14, 12, 4, 14);
			this.generateAirBox(worldIn, structureBoundingBoxIn, 1, 1, 15, 12, 4, 17);
			this.generateAirBox(worldIn, structureBoundingBoxIn, 2, 5, 3, 11, 5, 16);
			this.clearFront(worldIn, structureBoundingBoxIn, 5, 8, 2, 0);

			this.generateBox(worldIn, structureBoundingBoxIn, 1, -1, 4, 12, 0, 17, primaryBlock, primaryBlock, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 5, -1, 1, 8, 0, 3, secondaryBlock, secondaryBlock, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 5, 1, 1, 5, 3, 3, secondaryBlock, secondaryBlock, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 8, 1, 1, 8, 3, 3, secondaryBlock, secondaryBlock, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 6, 4, 1, 7, 4, 3, secondaryBlock, secondaryBlock, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 0, -1, 3, 4, 1, 3, secondaryBlock, secondaryBlock, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 9, -1, 3, 13, 1, 3, secondaryBlock, secondaryBlock, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 0, -1, 4, 0, 1, 18, secondaryBlock, secondaryBlock, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 13, -1, 4, 13, 1, 18, secondaryBlock, secondaryBlock, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 1, -1, 18, 12, 1, 18, secondaryBlock, secondaryBlock, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 0, 2, 3, 4, 3, 3, primaryBlock, primaryBlock, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 9, 2, 3, 13, 3, 3, primaryBlock, primaryBlock, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 0, 2, 4, 0, 3, 18, primaryBlock, primaryBlock, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 13, 2, 4, 13, 3, 18, primaryBlock, primaryBlock, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 1, 2, 18, 12, 3, 18, primaryBlock, primaryBlock, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 0, 4, 3, 5, 4, 3, glassBlock, glassBlock, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 8, 4, 3, 13, 4, 3, glassBlock, glassBlock, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 0, 4, 4, 0, 4, 18, glassBlock, glassBlock, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 13, 4, 4, 13, 4, 18, glassBlock, glassBlock, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 1, 4, 18, 12, 4, 18, glassBlock, glassBlock, false);

			this.generateBox(worldIn, structureBoundingBoxIn, 1, 5, 4, 12, 5, 4, primaryBlock, primaryBlock, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 1, 5, 5, 1, 5, 17, primaryBlock, primaryBlock, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 12, 5, 5, 12, 5, 17, primaryBlock, primaryBlock, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 2, 5, 17, 11, 5, 17, primaryBlock, primaryBlock, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 2, 6, 5, 11, 6, 16, primaryBlock, primaryBlock, false);

			this.generateBox(worldIn, structureBoundingBoxIn, 1, 1, 14, 12, 1, 14, secondaryBlock, secondaryBlock, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 6, 0, 4, 7, 0, 7, primaryDecorBlock, primaryDecorBlock, false);
			this.placeBlock(worldIn, primaryDecorBlock, 5, 0, 8, structureBoundingBoxIn);
			this.placeBlock(worldIn, primaryDecorBlock, 8, 0, 8, structureBoundingBoxIn);
			this.placeBlock(worldIn, primaryDecorBlock, 4, 0, 9, structureBoundingBoxIn);
			this.placeBlock(worldIn, primaryDecorBlock, 9, 0, 9, structureBoundingBoxIn);
			this.placeBlock(worldIn, primaryDecorBlock, 3, 0, 10, structureBoundingBoxIn);
			this.placeBlock(worldIn, primaryDecorBlock, 10, 0, 10, structureBoundingBoxIn);
			this.placeBlock(worldIn, primaryDecorBlock, 4, 0, 11, structureBoundingBoxIn);
			this.placeBlock(worldIn, primaryDecorBlock, 9, 0, 11, structureBoundingBoxIn);
			this.generateBox(worldIn, structureBoundingBoxIn, 5, 0, 12, 8, 0, 12, primaryDecorBlock, primaryDecorBlock, false);

			this.placeBlock(worldIn, lightBlock, 5, 1, 1, structureBoundingBoxIn);
			this.placeBlock(worldIn, lightBlock, 8, 1, 1, structureBoundingBoxIn);
			this.placeBlock(worldIn, lightBlock, 1, 0, 4, structureBoundingBoxIn);
			this.placeBlock(worldIn, lightBlock, 12, 0, 4, structureBoundingBoxIn);
			this.placeBlock(worldIn, lightBlock, 1, 0, 13, structureBoundingBoxIn);
			this.placeBlock(worldIn, lightBlock, 12, 0, 13, structureBoundingBoxIn);
			this.placeBlock(worldIn, lightBlock, 1, 0, 17, structureBoundingBoxIn);
			this.placeBlock(worldIn, lightBlock, 12, 0, 17, structureBoundingBoxIn);
			this.placeBlock(worldIn, lightBlock, 2, 6, 5, structureBoundingBoxIn);
			this.placeBlock(worldIn, lightBlock, 11, 6, 5, structureBoundingBoxIn);
			this.placeBlock(worldIn, lightBlock, 2, 6, 16, structureBoundingBoxIn);
			this.placeBlock(worldIn, lightBlock, 11, 6, 16, structureBoundingBoxIn);

			if(!spawns[0])
				spawns[0] = spawnConsort(4, 1, 15, structureBoundingBoxIn, worldIn, chunkGeneratorIn, EnumConsort.getRandomMerchant(randomIn), 1);
			if(!spawns[1])
				spawns[1] = spawnConsort(9, 1, 15, structureBoundingBoxIn, worldIn, chunkGeneratorIn, EnumConsort.getRandomMerchant(randomIn), 1);

			return true;
		}
	}
	
	public static class TurtleTemple1 extends ConsortVillagePieces.ConsortVillagePiece
	{
		TurtleTemple1(ConsortVillageCenter.VillageCenter start, Random rand, MutableBoundingBox boundingBox, Direction facing)
		{
			super(MSStructurePieces.TURTLE_TEMPLE_1, 0, 3);
			setOrientation(facing);
			this.boundingBox = boundingBox;
		}
		
		public TurtleTemple1(TemplateManager templates, CompoundNBT nbt)
		{
			super(MSStructurePieces.TURTLE_TEMPLE_1, nbt, 3);
		}
		
		public static TurtleTemple1 createPiece(ConsortVillageCenter.VillageCenter start, List<StructurePiece> componentList, Random rand, int x, int y, int z, Direction facing)
		{
			MutableBoundingBox structureboundingbox = MutableBoundingBox.orientBox(x, y, z, 0, -1, 0, 11, 6, 14, facing);
			return StructurePiece.findCollisionPiece(componentList, structureboundingbox) == null ? new TurtleTemple1(start, rand, structureboundingbox, facing) : null;
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
			BlockState primaryBlock = blocks.getBlockState("structure_primary");
			BlockState secondaryBlock = blocks.getBlockState("structure_secondary");
			BlockState lightBlock = blocks.getBlockState("light_block");
			BlockState glassBlock1 = blocks.getBlockState("stained_glass_1");
			BlockState glassBlock2 = blocks.getBlockState("stained_glass_2");

			this.generateAirBox(worldIn, structureBoundingBoxIn, 1, 1, 2, 9, 5, 4);
			this.generateAirBox(worldIn, structureBoundingBoxIn, 1, 1, 5, 9, 4, 6);
			this.generateAirBox(worldIn, structureBoundingBoxIn, 1, 0, 7, 9, 3, 12);
			this.clearFront(worldIn, structureBoundingBoxIn, 4, 6, 1, 0);

			this.generateBox(worldIn, structureBoundingBoxIn, 1, 0, 2, 9, 0, 6, primaryBlock, primaryBlock, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 1, -1, 6, 9, -1, 12, primaryBlock, primaryBlock, false);

			this.generateBox(worldIn, structureBoundingBoxIn, 0, 0, 1, 10, 1, 1, secondaryBlock, secondaryBlock, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 4, 2, 1, 6, 3, 1, secondaryBlock, secondaryBlock, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 0, 0, 2, 0, 1, 4, secondaryBlock, secondaryBlock, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 10, 0, 2, 10, 1, 4, secondaryBlock, secondaryBlock, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 0, 0, 5, 0, 1, 6, primaryBlock, primaryBlock, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 0, -1, 7, 0, 3, 13, primaryBlock, primaryBlock, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 10, 0, 5, 10, 1, 6, primaryBlock, primaryBlock, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 10, -1, 7, 10, 3, 13, primaryBlock, primaryBlock, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 1, -1, 13, 9, 3, 13, primaryBlock, primaryBlock, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 1, 4, 1, 9, 5, 1, primaryBlock, primaryBlock, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 0, 2, 1, 0, 5, 4, primaryBlock, primaryBlock, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 0, 4, 5, 0, 4, 6, primaryBlock, primaryBlock, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 10, 2, 1, 10, 5, 4, primaryBlock, primaryBlock, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 10, 4, 5, 10, 4, 6, primaryBlock, primaryBlock, false);

			this.generateBox(worldIn, structureBoundingBoxIn, 1, 4, 7, 9, 4, 12, primaryBlock, primaryBlock, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 1, 5, 5, 9, 5, 6, primaryBlock, primaryBlock, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 1, 6, 2, 9, 6, 4, primaryBlock, primaryBlock, false);

			this.placeBlock(worldIn, lightBlock, 1, 0, 2, structureBoundingBoxIn);
			this.placeBlock(worldIn, lightBlock, 9, 0, 2, structureBoundingBoxIn);
			this.placeBlock(worldIn, lightBlock, 1, -1, 12, structureBoundingBoxIn);
			this.placeBlock(worldIn, lightBlock, 9, -1, 12, structureBoundingBoxIn);

			this.placeBlock(worldIn, glassBlock1, 1, 2, 1, structureBoundingBoxIn);
			this.placeBlock(worldIn, glassBlock1, 1, 3, 1, structureBoundingBoxIn);
			this.placeBlock(worldIn, glassBlock2, 2, 2, 1, structureBoundingBoxIn);
			this.placeBlock(worldIn, glassBlock2, 2, 3, 1, structureBoundingBoxIn);
			this.placeBlock(worldIn, glassBlock1, 2, 4, 1, structureBoundingBoxIn);
			this.placeBlock(worldIn, glassBlock1, 3, 2, 1, structureBoundingBoxIn);
			this.placeBlock(worldIn, glassBlock1, 3, 3, 1, structureBoundingBoxIn);

			this.placeBlock(worldIn, glassBlock1, 7, 2, 1, structureBoundingBoxIn);
			this.placeBlock(worldIn, glassBlock1, 7, 3, 1, structureBoundingBoxIn);
			this.placeBlock(worldIn, glassBlock2, 8, 2, 1, structureBoundingBoxIn);
			this.placeBlock(worldIn, glassBlock2, 8, 3, 1, structureBoundingBoxIn);
			this.placeBlock(worldIn, glassBlock1, 8, 4, 1, structureBoundingBoxIn);
			this.placeBlock(worldIn, glassBlock1, 9, 2, 1, structureBoundingBoxIn);
			this.placeBlock(worldIn, glassBlock1, 9, 3, 1, structureBoundingBoxIn);

			this.placeBlock(worldIn, glassBlock2, 0, 2, 4, structureBoundingBoxIn);
			this.placeBlock(worldIn, glassBlock1, 0, 3, 4, structureBoundingBoxIn);
			this.placeBlock(worldIn, glassBlock1, 0, 2, 5, structureBoundingBoxIn);
			this.placeBlock(worldIn, glassBlock2, 0, 3, 5, structureBoundingBoxIn);
			this.placeBlock(worldIn, glassBlock2, 0, 2, 6, structureBoundingBoxIn);
			this.placeBlock(worldIn, glassBlock1, 0, 3, 6, structureBoundingBoxIn);

			this.placeBlock(worldIn, glassBlock2, 10, 2, 4, structureBoundingBoxIn);
			this.placeBlock(worldIn, glassBlock1, 10, 3, 4, structureBoundingBoxIn);
			this.placeBlock(worldIn, glassBlock1, 10, 2, 5, structureBoundingBoxIn);
			this.placeBlock(worldIn, glassBlock2, 10, 3, 5, structureBoundingBoxIn);
			this.placeBlock(worldIn, glassBlock2, 10, 2, 6, structureBoundingBoxIn);
			this.placeBlock(worldIn, glassBlock1, 10, 3, 6, structureBoundingBoxIn);

			generateDoor(worldIn, structureBoundingBoxIn, randomIn, 5, 1, 1, Direction.SOUTH, Blocks.IRON_DOOR, DoorHingeSide.LEFT);

			placeBlock(worldIn, Blocks.STONE_BUTTON.defaultBlockState(), 6, 1, 0, structureBoundingBoxIn);
			placeBlock(worldIn, Blocks.STONE_BUTTON.defaultBlockState().rotate(Rotation.CLOCKWISE_180), 4, 1, 2, structureBoundingBoxIn);

			if(!spawns[0])
				spawns[0] = spawnConsort(4, 0, 10, structureBoundingBoxIn, worldIn, chunkGeneratorIn);
			if(!spawns[1])
				spawns[1] = spawnConsort(5, 0, 10, structureBoundingBoxIn, worldIn, chunkGeneratorIn);
			if(!spawns[2])
				spawns[2] = spawnConsort(6, 0, 10, structureBoundingBoxIn, worldIn, chunkGeneratorIn);

			return true;
		}
	}
}