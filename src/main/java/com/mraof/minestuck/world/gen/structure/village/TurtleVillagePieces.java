package com.mraof.minestuck.world.gen.structure.village;

import com.mraof.minestuck.entity.consort.EnumConsort;
import com.mraof.minestuck.world.gen.structure.MSStructures;
import com.mraof.minestuck.world.gen.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.lands.ILandType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;

import java.util.List;

public class TurtleVillagePieces
{
	/**
	 * Helper function for adding village centers associated with turtles.
	 */
	public static void addCenters(ILandType.CenterRegister register)
	{
		register.add(TurtleWellCenter::new, 5);
	}
	
	/**
	 * Helper function for adding village pieces associated with turtles.
	 */
	public static void addPieces(ILandType.PieceRegister register, RandomSource random)
	{
		register.add(ShellHouse1::createPiece, 3, Mth.nextInt(random, 5, 8));
		register.add(TurtleMarket1::createPiece, 10, Mth.nextInt(random, 0, 2));
		register.add(TurtleTemple1::createPiece, 10, Mth.nextInt(random, 1, 1));
	}
	
	public static class TurtleWellCenter extends ConsortVillageCenter.VillageCenter
	{
		public TurtleWellCenter(List<ConsortVillagePieces.PieceWeight> pieceWeightList, int x, int z, RandomSource rand)
		{
			super(MSStructures.ConsortVillage.TURTLE_WELL_CENTER_PIECE.get(), pieceWeightList, 0, new BoundingBox(x, 60, z, x + 8 - 1, 70, z + 8 - 1), 0);
			this.setOrientation(getRandomHorizontalDirection(rand));
		}
		
		public TurtleWellCenter(CompoundTag nbt)
		{
			super(MSStructures.ConsortVillage.TURTLE_WELL_CENTER_PIECE.get(), nbt, 0);
		}
		
		@Override
		public void addChildren(StructurePiece componentIn, StructurePieceAccessor builder, RandomSource rand)
		{
			ConsortVillagePieces.generateAndAddRoadPiece((ConsortVillageCenter.VillageCenter) componentIn, builder, rand, boundingBox.minX() + 3, boundingBox.minY(), boundingBox.maxZ() + 1, Direction.SOUTH);
			ConsortVillagePieces.generateAndAddRoadPiece((ConsortVillageCenter.VillageCenter) componentIn, builder, rand, boundingBox.minX() - 1, boundingBox.minY(), boundingBox.minZ() + 3, Direction.WEST);
			ConsortVillagePieces.generateAndAddRoadPiece((ConsortVillageCenter.VillageCenter) componentIn, builder, rand, boundingBox.minX() + 3, boundingBox.minY(), boundingBox.minZ() - 1, Direction.NORTH);
			ConsortVillagePieces.generateAndAddRoadPiece((ConsortVillageCenter.VillageCenter) componentIn, builder, rand, boundingBox.maxX() + 1, boundingBox.minY(), boundingBox.minZ() + 3, Direction.EAST);
		}

		@Override
		public void postProcess(WorldGenLevel level, StructureManager manager, ChunkGenerator chunkGeneratorIn, RandomSource randomIn, BoundingBox structureBoundingBoxIn, ChunkPos chunkPosIn, BlockPos pos)
		{
			if (this.averageGroundLvl < 0)
			{
				this.averageGroundLvl = this.getAverageGroundLevel(level, chunkGeneratorIn, structureBoundingBoxIn);

				if (this.averageGroundLvl < 0)
				{
					return;
				}

				this.boundingBox.move(0, this.averageGroundLvl - this.boundingBox.minY() - 5, 0);
			}

			StructureBlockRegistry blocks = StructureBlockRegistry.getOrDefault(chunkGeneratorIn);
			BlockState primary = blocks.getBlockState("structure_primary");
			BlockState secondary = blocks.getBlockState("structure_secondary");
			BlockState fluid = blocks.getBlockState("ocean");
			BlockState stairsN = blocks.getStairs("structure_primary_stairs", Direction.NORTH, false);
			BlockState stairsE = blocks.getStairs("structure_primary_stairs", Direction.EAST, false);
			BlockState stairsS = blocks.getStairs("structure_primary_stairs", Direction.SOUTH, false);
			BlockState stairsW = blocks.getStairs("structure_primary_stairs", Direction.WEST, false);

			this.generateBox(level, structureBoundingBoxIn, 0, 4, 0, 2, 4, 0, primary, primary, false);
			this.generateBox(level, structureBoundingBoxIn, 0, 4, 1, 0, 4, 2, primary, primary, false);
			this.generateBox(level, structureBoundingBoxIn, 5, 4, 0, 7, 4, 0, primary, primary, false);
			this.generateBox(level, structureBoundingBoxIn, 7, 4, 1, 7, 4, 2, primary, primary, false);
			this.generateBox(level, structureBoundingBoxIn, 0, 4, 7, 2, 4, 7, primary, primary, false);
			this.generateBox(level, structureBoundingBoxIn, 0, 4, 5, 0, 4, 6, primary, primary, false);
			this.generateBox(level, structureBoundingBoxIn, 5, 4, 7, 7, 4, 7, primary, primary, false);
			this.generateBox(level, structureBoundingBoxIn, 7, 4, 5, 7, 4, 6, primary, primary, false);

			this.generateBox(level, structureBoundingBoxIn, 3, 4, 0, 4, 4, 0, stairsN, stairsN, false);
			this.generateBox(level, structureBoundingBoxIn, 3, 4, 7, 4, 4, 7, stairsS, stairsS, false);
			this.generateBox(level, structureBoundingBoxIn, 0, 4, 3, 0, 4, 4, stairsW, stairsW, false);
			this.generateBox(level, structureBoundingBoxIn, 7, 4, 3, 7, 4, 4, stairsE, stairsE, false);

			this.generateAirBox(level, structureBoundingBoxIn, 1, 5, 0, 6, 7, 0);
			this.generateAirBox(level, structureBoundingBoxIn, 1, 5, 7, 6, 7, 7);
			this.generateAirBox(level, structureBoundingBoxIn, 0, 5, 1, 0, 7, 6);
			this.generateAirBox(level, structureBoundingBoxIn, 7, 5, 1, 7, 7, 6);

			this.generateAirBox(level, structureBoundingBoxIn, 1, 4, 1, 6, 8, 6);
			this.generateAirBox(level, structureBoundingBoxIn, 2, 3, 2, 5, 3, 5);

			this.generateBox(level, structureBoundingBoxIn, 0, 5, 0, 0, 7, 0, primary, primary, false);
			this.generateBox(level, structureBoundingBoxIn, 0, 5, 7, 0, 7, 7, primary, primary, false);
			this.generateBox(level, structureBoundingBoxIn, 7, 5, 0, 7, 7, 0, primary, primary, false);
			this.generateBox(level, structureBoundingBoxIn, 7, 5, 7, 7, 7, 7, primary, primary, false);

			this.generateBox(level, structureBoundingBoxIn, 0, 8, 0, 7, 8, 0, primary, primary, false);
			this.generateBox(level, structureBoundingBoxIn, 0, 8, 7, 7, 8, 7, primary, primary, false);
			this.generateBox(level, structureBoundingBoxIn, 0, 8, 1, 0, 8, 6, primary, primary, false);
			this.generateBox(level, structureBoundingBoxIn, 7, 8, 1, 7, 8, 6, primary, primary, false);
			this.placeBlock(level, primary, 1, 8, 1, structureBoundingBoxIn);
			this.placeBlock(level, primary, 1, 8, 6, structureBoundingBoxIn);
			this.placeBlock(level, primary, 6, 8, 1, structureBoundingBoxIn);
			this.placeBlock(level, primary, 6, 8, 6, structureBoundingBoxIn);

			this.generateBox(level, structureBoundingBoxIn, 1, 9, 1, 6, 9, 6, primary, primary, false);
			this.generateBox(level, structureBoundingBoxIn, 2, 10, 2, 5, 10, 5, primary, primary, false);

			this.generateBox(level, structureBoundingBoxIn, 1, 1, 1, 6, 3, 1, secondary, secondary, false);
			this.generateBox(level, structureBoundingBoxIn, 1, 1, 6, 6, 3, 6, secondary, secondary, false);
			this.generateBox(level, structureBoundingBoxIn, 1, 1, 2, 1, 3, 5, secondary, secondary, false);
			this.generateBox(level, structureBoundingBoxIn, 6, 1, 2, 6, 3, 5, secondary, secondary, false);

			this.generateBox(level, structureBoundingBoxIn, 3, 3, 1, 4, 3, 1, stairsN, stairsN, false);
			this.generateBox(level, structureBoundingBoxIn, 3, 3, 6, 4, 3, 6, stairsS, stairsS, false);
			this.generateBox(level, structureBoundingBoxIn, 1, 3, 3, 1, 3, 4, stairsW, stairsW, false);
			this.generateBox(level, structureBoundingBoxIn, 6, 3, 3, 6, 3, 4, stairsE, stairsE, false);

			this.generateBox(level, structureBoundingBoxIn, 2, 0, 2, 5, 1, 5, secondary, secondary, false);

			this.generateBox(level, structureBoundingBoxIn, 2, 2, 2, 5, 2, 5, fluid, fluid, false);
			this.generateBox(level, structureBoundingBoxIn, 3, 1, 3, 4, 1, 4, fluid, fluid, false);
		}
		
	}
	
	public static class ShellHouse1 extends ConsortVillagePieces.ConsortVillagePiece
	{
		ShellHouse1(ConsortVillageCenter.VillageCenter start, RandomSource rand, BoundingBox boundingBox, Direction facing)
		{
			super(MSStructures.ConsortVillage.SHELL_HOUSE_1_PIECE.get(), 0, boundingBox, 2);
			setOrientation(facing);
		}
		
		public ShellHouse1(CompoundTag nbt)
		{
			super(MSStructures.ConsortVillage.SHELL_HOUSE_1_PIECE.get(), nbt, 2);
		}
		
		public static ShellHouse1 createPiece(ConsortVillageCenter.VillageCenter start, StructurePieceAccessor accessor, RandomSource rand, int x, int y, int z, Direction facing)
		{
			BoundingBox boundingBox = BoundingBox.orientBox(x, y, z, 0, -2, 0, 8, 5, 9, facing);
			return accessor.findCollisionPiece(boundingBox) == null ? new ShellHouse1(start, rand, boundingBox, facing) : null;
		}

		@Override
		public void postProcess(WorldGenLevel level, StructureManager manager, ChunkGenerator chunkGeneratorIn, RandomSource randomIn, BoundingBox structureBoundingBoxIn, ChunkPos chunkPosIn, BlockPos pos)
		{
			if (this.averageGroundLvl < 0)
			{
				this.averageGroundLvl = this.getAverageGroundLevel(level, chunkGeneratorIn, structureBoundingBoxIn);

				if (this.averageGroundLvl < 0)
				{
					return;
				}

				this.boundingBox.move(0, this.averageGroundLvl - this.boundingBox.minY() - 3, 0);
			}

			StructureBlockRegistry blocks = StructureBlockRegistry.getOrDefault(chunkGeneratorIn);
			BlockState buildBlock = blocks.getBlockState("structure_primary");
			BlockState lightBlock = blocks.getBlockState("light_block");

			this.generateAirBox(level, structureBoundingBoxIn, 1, 1, 2, 6, 4, 7);
			this.generateAirBox(level, structureBoundingBoxIn, 3, 3, 1, 4, 4, 1);
			this.generateAirBox(level, structureBoundingBoxIn, 2, 5, 6, 2, 5, 6);

			this.clearFront(level, structureBoundingBoxIn, 2, 5, 3, 0);

			this.generateBox(level, structureBoundingBoxIn, 0, 0, 1, 7, 0, 8, buildBlock, buildBlock, false);
			this.generateBox(level, structureBoundingBoxIn, 0, 1, 8, 7, 4, 8, buildBlock, buildBlock, false);
			this.generateBox(level, structureBoundingBoxIn, 0, 1, 1, 0, 4, 7, buildBlock, buildBlock, false);
			this.generateBox(level, structureBoundingBoxIn, 7, 1, 1, 7, 4, 7, buildBlock, buildBlock, false);
			this.generateBox(level, structureBoundingBoxIn, 1, 1, 1, 6, 2, 1, buildBlock, buildBlock, false);
			this.generateBox(level, structureBoundingBoxIn, 1, 3, 1, 2, 4, 1, buildBlock, buildBlock, false);
			this.generateBox(level, structureBoundingBoxIn, 5, 3, 1, 6, 4, 1, buildBlock, buildBlock, false);

			this.generateBox(level, structureBoundingBoxIn, 3, 1, 2, 4, 1, 2, buildBlock, buildBlock, false);

			this.generateBox(level, structureBoundingBoxIn, 3, 5, 1, 4, 5, 1, buildBlock, buildBlock, false);
			this.generateBox(level, structureBoundingBoxIn, 1, 5, 2, 6, 5, 2, buildBlock, buildBlock, false);
			this.generateBox(level, structureBoundingBoxIn, 1, 5, 3, 1, 5, 7, buildBlock, buildBlock, false);
			this.generateBox(level, structureBoundingBoxIn, 6, 5, 3, 6, 5, 7, buildBlock, buildBlock, false);
			this.generateBox(level, structureBoundingBoxIn, 2, 5, 7, 5, 5, 7, buildBlock, buildBlock, false);

			this.generateBox(level, structureBoundingBoxIn, 2, 6, 3, 5, 6, 6, buildBlock, buildBlock, false);
			this.generateBox(level, structureBoundingBoxIn, 3, 6, 4, 4, 6, 5, lightBlock, lightBlock, false);
			this.generateBox(level, structureBoundingBoxIn, 3, 7, 4, 4, 7, 5, buildBlock, buildBlock, false);

			if(!spawns[0])
				spawns[0] = spawnConsort(2, 1, 5, structureBoundingBoxIn, level, chunkGeneratorIn);
			if(!spawns[1])
				spawns[1] = spawnConsort(5, 1, 5, structureBoundingBoxIn, level, chunkGeneratorIn);
		}
	}
	
	public static class TurtleMarket1 extends ConsortVillagePieces.ConsortVillagePiece
	{
		TurtleMarket1(ConsortVillageCenter.VillageCenter start, RandomSource rand, BoundingBox boundingBox, Direction facing)
		{
			super(MSStructures.ConsortVillage.TURTLE_MARKET_1_PIECE.get(), 0, boundingBox, 2);
			setOrientation(facing);
		}
		
		public TurtleMarket1(CompoundTag nbt)
		{
			super(MSStructures.ConsortVillage.TURTLE_MARKET_1_PIECE.get(), nbt, 2);
		}
		
		public static TurtleMarket1 createPiece(ConsortVillageCenter.VillageCenter start, StructurePieceAccessor accessor, RandomSource rand, int x, int y, int z, Direction facing)
		{
			BoundingBox boundingBox = BoundingBox.orientBox(x, y, z, 0, 0, 0, 14, 7, 19, facing);
			return accessor.findCollisionPiece(boundingBox) == null ? new TurtleMarket1(start, rand, boundingBox, facing) : null;
		}

		@Override
		public void postProcess(WorldGenLevel level, StructureManager manager, ChunkGenerator chunkGeneratorIn, RandomSource randomIn, BoundingBox structureBoundingBoxIn, ChunkPos chunkPosIn, BlockPos pos)
		{
			if (this.averageGroundLvl < 0)
			{
				this.averageGroundLvl = this.getAverageGroundLevel(level, chunkGeneratorIn, structureBoundingBoxIn);

				if (this.averageGroundLvl < 0)
				{
					return;
				}

				this.boundingBox.move(0, this.averageGroundLvl - this.boundingBox.minY() - 1, 0);
			}

			StructureBlockRegistry blocks = StructureBlockRegistry.getOrDefault(chunkGeneratorIn);
			BlockState primaryBlock = blocks.getBlockState("structure_primary");
			BlockState primaryDecorBlock = blocks.getBlockState("structure_primary_decorative");
			BlockState secondaryBlock = blocks.getBlockState("structure_secondary");
			BlockState lightBlock = blocks.getBlockState("light_block");
			BlockState glassBlock = blocks.getBlockState("glass");

			this.generateAirBox(level, structureBoundingBoxIn, 6, 1, 1, 7, 3, 3);
			this.generateAirBox(level, structureBoundingBoxIn, 1, 1, 4, 12, 4, 13);
			this.generateAirBox(level, structureBoundingBoxIn, 1, 2, 14, 12, 4, 14);
			this.generateAirBox(level, structureBoundingBoxIn, 1, 1, 15, 12, 4, 17);
			this.generateAirBox(level, structureBoundingBoxIn, 2, 5, 3, 11, 5, 16);
			this.clearFront(level, structureBoundingBoxIn, 5, 8, 2, 0);

			this.generateBox(level, structureBoundingBoxIn, 1, -1, 4, 12, 0, 17, primaryBlock, primaryBlock, false);
			this.generateBox(level, structureBoundingBoxIn, 5, -1, 1, 8, 0, 3, secondaryBlock, secondaryBlock, false);
			this.generateBox(level, structureBoundingBoxIn, 5, 1, 1, 5, 3, 3, secondaryBlock, secondaryBlock, false);
			this.generateBox(level, structureBoundingBoxIn, 8, 1, 1, 8, 3, 3, secondaryBlock, secondaryBlock, false);
			this.generateBox(level, structureBoundingBoxIn, 6, 4, 1, 7, 4, 3, secondaryBlock, secondaryBlock, false);
			this.generateBox(level, structureBoundingBoxIn, 0, -1, 3, 4, 1, 3, secondaryBlock, secondaryBlock, false);
			this.generateBox(level, structureBoundingBoxIn, 9, -1, 3, 13, 1, 3, secondaryBlock, secondaryBlock, false);
			this.generateBox(level, structureBoundingBoxIn, 0, -1, 4, 0, 1, 18, secondaryBlock, secondaryBlock, false);
			this.generateBox(level, structureBoundingBoxIn, 13, -1, 4, 13, 1, 18, secondaryBlock, secondaryBlock, false);
			this.generateBox(level, structureBoundingBoxIn, 1, -1, 18, 12, 1, 18, secondaryBlock, secondaryBlock, false);
			this.generateBox(level, structureBoundingBoxIn, 0, 2, 3, 4, 3, 3, primaryBlock, primaryBlock, false);
			this.generateBox(level, structureBoundingBoxIn, 9, 2, 3, 13, 3, 3, primaryBlock, primaryBlock, false);
			this.generateBox(level, structureBoundingBoxIn, 0, 2, 4, 0, 3, 18, primaryBlock, primaryBlock, false);
			this.generateBox(level, structureBoundingBoxIn, 13, 2, 4, 13, 3, 18, primaryBlock, primaryBlock, false);
			this.generateBox(level, structureBoundingBoxIn, 1, 2, 18, 12, 3, 18, primaryBlock, primaryBlock, false);
			this.generateBox(level, structureBoundingBoxIn, 0, 4, 3, 5, 4, 3, glassBlock, glassBlock, false);
			this.generateBox(level, structureBoundingBoxIn, 8, 4, 3, 13, 4, 3, glassBlock, glassBlock, false);
			this.generateBox(level, structureBoundingBoxIn, 0, 4, 4, 0, 4, 18, glassBlock, glassBlock, false);
			this.generateBox(level, structureBoundingBoxIn, 13, 4, 4, 13, 4, 18, glassBlock, glassBlock, false);
			this.generateBox(level, structureBoundingBoxIn, 1, 4, 18, 12, 4, 18, glassBlock, glassBlock, false);

			this.generateBox(level, structureBoundingBoxIn, 1, 5, 4, 12, 5, 4, primaryBlock, primaryBlock, false);
			this.generateBox(level, structureBoundingBoxIn, 1, 5, 5, 1, 5, 17, primaryBlock, primaryBlock, false);
			this.generateBox(level, structureBoundingBoxIn, 12, 5, 5, 12, 5, 17, primaryBlock, primaryBlock, false);
			this.generateBox(level, structureBoundingBoxIn, 2, 5, 17, 11, 5, 17, primaryBlock, primaryBlock, false);
			this.generateBox(level, structureBoundingBoxIn, 2, 6, 5, 11, 6, 16, primaryBlock, primaryBlock, false);

			this.generateBox(level, structureBoundingBoxIn, 1, 1, 14, 12, 1, 14, secondaryBlock, secondaryBlock, false);
			this.generateBox(level, structureBoundingBoxIn, 6, 0, 4, 7, 0, 7, primaryDecorBlock, primaryDecorBlock, false);
			this.placeBlock(level, primaryDecorBlock, 5, 0, 8, structureBoundingBoxIn);
			this.placeBlock(level, primaryDecorBlock, 8, 0, 8, structureBoundingBoxIn);
			this.placeBlock(level, primaryDecorBlock, 4, 0, 9, structureBoundingBoxIn);
			this.placeBlock(level, primaryDecorBlock, 9, 0, 9, structureBoundingBoxIn);
			this.placeBlock(level, primaryDecorBlock, 3, 0, 10, structureBoundingBoxIn);
			this.placeBlock(level, primaryDecorBlock, 10, 0, 10, structureBoundingBoxIn);
			this.placeBlock(level, primaryDecorBlock, 4, 0, 11, structureBoundingBoxIn);
			this.placeBlock(level, primaryDecorBlock, 9, 0, 11, structureBoundingBoxIn);
			this.generateBox(level, structureBoundingBoxIn, 5, 0, 12, 8, 0, 12, primaryDecorBlock, primaryDecorBlock, false);

			this.placeBlock(level, lightBlock, 5, 1, 1, structureBoundingBoxIn);
			this.placeBlock(level, lightBlock, 8, 1, 1, structureBoundingBoxIn);
			this.placeBlock(level, lightBlock, 1, 0, 4, structureBoundingBoxIn);
			this.placeBlock(level, lightBlock, 12, 0, 4, structureBoundingBoxIn);
			this.placeBlock(level, lightBlock, 1, 0, 13, structureBoundingBoxIn);
			this.placeBlock(level, lightBlock, 12, 0, 13, structureBoundingBoxIn);
			this.placeBlock(level, lightBlock, 1, 0, 17, structureBoundingBoxIn);
			this.placeBlock(level, lightBlock, 12, 0, 17, structureBoundingBoxIn);
			this.placeBlock(level, lightBlock, 2, 6, 5, structureBoundingBoxIn);
			this.placeBlock(level, lightBlock, 11, 6, 5, structureBoundingBoxIn);
			this.placeBlock(level, lightBlock, 2, 6, 16, structureBoundingBoxIn);
			this.placeBlock(level, lightBlock, 11, 6, 16, structureBoundingBoxIn);

			if(!spawns[0])
				spawns[0] = spawnConsort(4, 1, 15, structureBoundingBoxIn, level, chunkGeneratorIn, EnumConsort.getRandomMerchant(randomIn), 1);
			if(!spawns[1])
				spawns[1] = spawnConsort(9, 1, 15, structureBoundingBoxIn, level, chunkGeneratorIn, EnumConsort.getRandomMerchant(randomIn), 1);
		}
	}
	
	public static class TurtleTemple1 extends ConsortVillagePieces.ConsortVillagePiece
	{
		TurtleTemple1(ConsortVillageCenter.VillageCenter start, RandomSource rand, BoundingBox boundingBox, Direction facing)
		{
			super(MSStructures.ConsortVillage.TURTLE_TEMPLE_1_PIECE.get(), 0, boundingBox, 3);
			setOrientation(facing);
		}
		
		public TurtleTemple1(CompoundTag nbt)
		{
			super(MSStructures.ConsortVillage.TURTLE_TEMPLE_1_PIECE.get(), nbt, 3);
		}
		
		public static TurtleTemple1 createPiece(ConsortVillageCenter.VillageCenter start, StructurePieceAccessor accessor, RandomSource rand, int x, int y, int z, Direction facing)
		{
			BoundingBox boundingBox = BoundingBox.orientBox(x, y, z, 0, -1, 0, 11, 6, 14, facing);
			return accessor.findCollisionPiece(boundingBox) == null ? new TurtleTemple1(start, rand, boundingBox, facing) : null;
		}

		@Override
		public void postProcess(WorldGenLevel level, StructureManager manager, ChunkGenerator chunkGeneratorIn, RandomSource randomIn, BoundingBox structureBoundingBoxIn, ChunkPos chunkPosIn, BlockPos pos)
		{
			if (this.averageGroundLvl < 0)
			{
				this.averageGroundLvl = this.getAverageGroundLevel(level, chunkGeneratorIn, structureBoundingBoxIn);

				if (this.averageGroundLvl < 0)
				{
					return;
				}

				this.boundingBox.move(0, this.averageGroundLvl - this.boundingBox.minY() - 1, 0);
			}

			StructureBlockRegistry blocks = StructureBlockRegistry.getOrDefault(chunkGeneratorIn);
			BlockState primaryBlock = blocks.getBlockState("structure_primary");
			BlockState secondaryBlock = blocks.getBlockState("structure_secondary");
			BlockState lightBlock = blocks.getBlockState("light_block");
			BlockState glassBlock1 = blocks.getBlockState("stained_glass_1");
			BlockState glassBlock2 = blocks.getBlockState("stained_glass_2");

			this.generateAirBox(level, structureBoundingBoxIn, 1, 1, 2, 9, 5, 4);
			this.generateAirBox(level, structureBoundingBoxIn, 1, 1, 5, 9, 4, 6);
			this.generateAirBox(level, structureBoundingBoxIn, 1, 0, 7, 9, 3, 12);
			this.clearFront(level, structureBoundingBoxIn, 4, 6, 1, 0);

			this.generateBox(level, structureBoundingBoxIn, 1, 0, 2, 9, 0, 6, primaryBlock, primaryBlock, false);
			this.generateBox(level, structureBoundingBoxIn, 1, -1, 6, 9, -1, 12, primaryBlock, primaryBlock, false);

			this.generateBox(level, structureBoundingBoxIn, 0, 0, 1, 10, 1, 1, secondaryBlock, secondaryBlock, false);
			this.generateBox(level, structureBoundingBoxIn, 4, 2, 1, 6, 3, 1, secondaryBlock, secondaryBlock, false);
			this.generateBox(level, structureBoundingBoxIn, 0, 0, 2, 0, 1, 4, secondaryBlock, secondaryBlock, false);
			this.generateBox(level, structureBoundingBoxIn, 10, 0, 2, 10, 1, 4, secondaryBlock, secondaryBlock, false);
			this.generateBox(level, structureBoundingBoxIn, 0, 0, 5, 0, 1, 6, primaryBlock, primaryBlock, false);
			this.generateBox(level, structureBoundingBoxIn, 0, -1, 7, 0, 3, 13, primaryBlock, primaryBlock, false);
			this.generateBox(level, structureBoundingBoxIn, 10, 0, 5, 10, 1, 6, primaryBlock, primaryBlock, false);
			this.generateBox(level, structureBoundingBoxIn, 10, -1, 7, 10, 3, 13, primaryBlock, primaryBlock, false);
			this.generateBox(level, structureBoundingBoxIn, 1, -1, 13, 9, 3, 13, primaryBlock, primaryBlock, false);
			this.generateBox(level, structureBoundingBoxIn, 1, 4, 1, 9, 5, 1, primaryBlock, primaryBlock, false);
			this.generateBox(level, structureBoundingBoxIn, 0, 2, 1, 0, 5, 4, primaryBlock, primaryBlock, false);
			this.generateBox(level, structureBoundingBoxIn, 0, 4, 5, 0, 4, 6, primaryBlock, primaryBlock, false);
			this.generateBox(level, structureBoundingBoxIn, 10, 2, 1, 10, 5, 4, primaryBlock, primaryBlock, false);
			this.generateBox(level, structureBoundingBoxIn, 10, 4, 5, 10, 4, 6, primaryBlock, primaryBlock, false);

			this.generateBox(level, structureBoundingBoxIn, 1, 4, 7, 9, 4, 12, primaryBlock, primaryBlock, false);
			this.generateBox(level, structureBoundingBoxIn, 1, 5, 5, 9, 5, 6, primaryBlock, primaryBlock, false);
			this.generateBox(level, structureBoundingBoxIn, 1, 6, 2, 9, 6, 4, primaryBlock, primaryBlock, false);

			this.placeBlock(level, lightBlock, 1, 0, 2, structureBoundingBoxIn);
			this.placeBlock(level, lightBlock, 9, 0, 2, structureBoundingBoxIn);
			this.placeBlock(level, lightBlock, 1, -1, 12, structureBoundingBoxIn);
			this.placeBlock(level, lightBlock, 9, -1, 12, structureBoundingBoxIn);

			this.placeBlock(level, glassBlock1, 1, 2, 1, structureBoundingBoxIn);
			this.placeBlock(level, glassBlock1, 1, 3, 1, structureBoundingBoxIn);
			this.placeBlock(level, glassBlock2, 2, 2, 1, structureBoundingBoxIn);
			this.placeBlock(level, glassBlock2, 2, 3, 1, structureBoundingBoxIn);
			this.placeBlock(level, glassBlock1, 2, 4, 1, structureBoundingBoxIn);
			this.placeBlock(level, glassBlock1, 3, 2, 1, structureBoundingBoxIn);
			this.placeBlock(level, glassBlock1, 3, 3, 1, structureBoundingBoxIn);

			this.placeBlock(level, glassBlock1, 7, 2, 1, structureBoundingBoxIn);
			this.placeBlock(level, glassBlock1, 7, 3, 1, structureBoundingBoxIn);
			this.placeBlock(level, glassBlock2, 8, 2, 1, structureBoundingBoxIn);
			this.placeBlock(level, glassBlock2, 8, 3, 1, structureBoundingBoxIn);
			this.placeBlock(level, glassBlock1, 8, 4, 1, structureBoundingBoxIn);
			this.placeBlock(level, glassBlock1, 9, 2, 1, structureBoundingBoxIn);
			this.placeBlock(level, glassBlock1, 9, 3, 1, structureBoundingBoxIn);

			this.placeBlock(level, glassBlock2, 0, 2, 4, structureBoundingBoxIn);
			this.placeBlock(level, glassBlock1, 0, 3, 4, structureBoundingBoxIn);
			this.placeBlock(level, glassBlock1, 0, 2, 5, structureBoundingBoxIn);
			this.placeBlock(level, glassBlock2, 0, 3, 5, structureBoundingBoxIn);
			this.placeBlock(level, glassBlock2, 0, 2, 6, structureBoundingBoxIn);
			this.placeBlock(level, glassBlock1, 0, 3, 6, structureBoundingBoxIn);

			this.placeBlock(level, glassBlock2, 10, 2, 4, structureBoundingBoxIn);
			this.placeBlock(level, glassBlock1, 10, 3, 4, structureBoundingBoxIn);
			this.placeBlock(level, glassBlock1, 10, 2, 5, structureBoundingBoxIn);
			this.placeBlock(level, glassBlock2, 10, 3, 5, structureBoundingBoxIn);
			this.placeBlock(level, glassBlock2, 10, 2, 6, structureBoundingBoxIn);
			this.placeBlock(level, glassBlock1, 10, 3, 6, structureBoundingBoxIn);

			generateDoor(level, structureBoundingBoxIn, randomIn, 5, 1, 1, Direction.SOUTH, Blocks.IRON_DOOR, DoorHingeSide.LEFT);

			placeBlock(level, Blocks.STONE_BUTTON.defaultBlockState(), 6, 1, 0, structureBoundingBoxIn);
			placeBlock(level, Blocks.STONE_BUTTON.defaultBlockState().rotate(Rotation.CLOCKWISE_180), 4, 1, 2, structureBoundingBoxIn);

			if(!spawns[0])
				spawns[0] = spawnConsort(4, 0, 10, structureBoundingBoxIn, level, chunkGeneratorIn);
			if(!spawns[1])
				spawns[1] = spawnConsort(5, 0, 10, structureBoundingBoxIn, level, chunkGeneratorIn);
			if(!spawns[2])
				spawns[2] = spawnConsort(6, 0, 10, structureBoundingBoxIn, level, chunkGeneratorIn);
		}
	}
}
