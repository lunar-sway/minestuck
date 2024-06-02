package com.mraof.minestuck.world.gen.structure.village;

import com.google.common.collect.Lists;
import com.mraof.minestuck.entity.consort.EnumConsort;
import com.mraof.minestuck.world.gen.structure.MSStructures;
import com.mraof.minestuck.world.gen.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.lands.ILandType;
import com.mraof.minestuck.world.lands.LandTypePair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.util.random.WeightedRandom;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;

import java.util.List;

/**
 * Created by kirderf1
 */
public class ConsortVillageCenter
{
	private static class CenterEntry extends WeightedEntry.IntrusiveBase
	{
		private final CenterFactory center;
		private CenterEntry(CenterFactory center, int weight)
		{
			super(weight);
			this.center = center;
		}
	}
	
	public static ConsortVillageCenter.VillageCenter getVillageStart(int x, int z, RandomSource rand, List<ConsortVillagePieces.PieceWeight> list, LandTypePair landTypes)
	{
		List<CenterEntry> weightList = Lists.newArrayList();
		
		ILandType.CenterRegister register = (factory, weight) -> weightList.add(new CenterEntry(factory, weight));
		landTypes.getTerrain().addVillageCenters(register);
		landTypes.getTitle().addVillageCenters(register);
		
		if(weightList.isEmpty())
			return ConsortVillageCenter.VillageMarketCenter.create(list, x, z, rand);
		else
		{
			return WeightedRandom.getRandomItem(rand, weightList).orElseThrow().center.createPiece(list, x, z, rand);
		}
	}
	
	public interface CenterFactory
	{
		VillageCenter createPiece(List<ConsortVillagePieces.PieceWeight> pieceWeightList, int x, int z, RandomSource rand);
	}
	
	public abstract static class VillageCenter extends ConsortVillagePieces.ConsortVillagePiece
	{
		public List<StructurePiece> pendingHouses = Lists.newArrayList();
		public List<StructurePiece> pendingRoads = Lists.newArrayList();
		
		public List<ConsortVillagePieces.PieceWeight> pieceWeightList;
		public ConsortVillagePieces.PieceWeight lastPieceWeightUsed;
		
		public VillageCenter(StructurePieceType type, List<ConsortVillagePieces.PieceWeight> pieceWeightList, int genDepth, BoundingBox boundingBox, int spawnCount)
		{
			super(type, genDepth, boundingBox, spawnCount);
			this.pieceWeightList = pieceWeightList;
		}
		
		public VillageCenter(StructurePieceType type, CompoundTag nbt, int spawnCount)
		{
			super(type, nbt, spawnCount);
		}
	}
	
	public static class VillageMarketCenter extends VillageCenter
	{
		public static VillageCenter create(List<ConsortVillagePieces.PieceWeight> pieceWeightList, int x, int z, RandomSource rand)
		{
			return new VillageMarketCenter(pieceWeightList, x, z, getRandomHorizontalDirection(rand));
		}
		
		private VillageMarketCenter(List<ConsortVillagePieces.PieceWeight> pieceWeightList, int x, int z, Direction orientation)
		{
			super(MSStructures.ConsortVillage.MARKET_CENTER_PIECE.get(), pieceWeightList, 0, makeBoundingBox(x, 64, z, orientation, 8, 15, 10), 5);
			setOrientation(orientation);
		}
		
		public VillageMarketCenter(CompoundTag nbt)
		{
			super(MSStructures.ConsortVillage.MARKET_CENTER_PIECE.get(), nbt, 5);
		}
		
		
		@Override
		public void addChildren(StructurePiece componentIn, StructurePieceAccessor accessor, RandomSource rand)
		{
			switch(this.getOrientation())
			{	//The vanilla code for "rotating coords based on facing" is messy
				case NORTH:
					ConsortVillagePieces.generateAndAddRoadPiece((VillageCenter) componentIn, accessor, rand, boundingBox.minX() + 3, boundingBox.minY(), boundingBox.maxZ() + 1, Direction.SOUTH);
					ConsortVillagePieces.generateAndAddRoadPiece((VillageCenter) componentIn, accessor, rand, boundingBox.maxX() - 1, boundingBox.minY(), boundingBox.minZ() - 1, Direction.NORTH);
					ConsortVillagePieces.generateAndAddRoadPiece((VillageCenter) componentIn, accessor, rand, boundingBox.maxX() + 1, boundingBox.minY(), boundingBox.minZ(), Direction.EAST);
					break;
				case EAST:
					ConsortVillagePieces.generateAndAddRoadPiece((VillageCenter) componentIn, accessor, rand, boundingBox.minX() - 1, boundingBox.minY(), boundingBox.minZ() + 3, Direction.WEST);
					ConsortVillagePieces.generateAndAddRoadPiece((VillageCenter) componentIn, accessor, rand, boundingBox.maxX() + 1, boundingBox.minY(), boundingBox.maxZ() - 1, Direction.EAST);
					ConsortVillagePieces.generateAndAddRoadPiece((VillageCenter) componentIn, accessor, rand, boundingBox.maxX() - 1, boundingBox.minY(), boundingBox.maxZ() + 1, Direction.SOUTH);
					break;
				case SOUTH:
					ConsortVillagePieces.generateAndAddRoadPiece((VillageCenter) componentIn, accessor, rand, boundingBox.minX() + 3, boundingBox.minY(), boundingBox.minZ() - 1, Direction.NORTH);
					ConsortVillagePieces.generateAndAddRoadPiece((VillageCenter) componentIn, accessor, rand, boundingBox.maxX() - 1, boundingBox.minY(), boundingBox.maxZ() + 1, Direction.SOUTH);
					ConsortVillagePieces.generateAndAddRoadPiece((VillageCenter) componentIn, accessor, rand, boundingBox.maxX() + 1, boundingBox.minY(), boundingBox.maxZ() - 1, Direction.EAST);
					break;
				case WEST:
					ConsortVillagePieces.generateAndAddRoadPiece((VillageCenter) componentIn, accessor, rand, boundingBox.maxX() + 1, boundingBox.minY(), boundingBox.minZ() + 3, Direction.EAST);
					ConsortVillagePieces.generateAndAddRoadPiece((VillageCenter) componentIn, accessor, rand, boundingBox.minX() - 1, boundingBox.minY(), boundingBox.maxZ() - 1, Direction.WEST);
					ConsortVillagePieces.generateAndAddRoadPiece((VillageCenter) componentIn, accessor, rand, boundingBox.minX(), boundingBox.minY(), boundingBox.maxZ() + 1, Direction.SOUTH);
					break;
				default:
			}
		}

		@Override
		public void postProcess(WorldGenLevel level, StructureManager manager, ChunkGenerator chunkGeneratorIn, RandomSource randomIn, BoundingBox box, ChunkPos chunkPosIn, BlockPos pos)
		{
			if (this.averageGroundLvl < 0)
			{
				this.averageGroundLvl = this.getAverageGroundLevel(level, chunkGeneratorIn, box);

				if (this.averageGroundLvl < 0)
				{
					return;
				}

				this.boundingBox.move(0, this.averageGroundLvl - this.boundingBox.minY(), 0);
			}

			StructureBlockRegistry blocks = StructureBlockRegistry.getOrDefault(chunkGeneratorIn);
			BlockState floorBlock = blocks.getBlockState("structure_secondary");
			BlockState plankBlock = blocks.getBlockState("structure_planks");
			BlockState plankSlab0 = blocks.getBlockState("structure_planks_slab");
			BlockState plankSlab1 = plankSlab0.setValue(SlabBlock.TYPE, SlabType.TOP);
			BlockState torch = blocks.getBlockState("torch");


			this.generateBox(level, box, 2, -1, 0, 5, 0, 0, floorBlock, floorBlock, false);
			this.generateBox(level, box, 1, -1, 1, 6, 0, 1, floorBlock, floorBlock, false);
			this.generateBox(level, box, 0, -1, 2, 1, 0, 7, floorBlock, floorBlock, false);
			this.placeBlock(level, floorBlock, 1, 0, 8, box);
			this.placeBlock(level, floorBlock, 1, -1, 8, box);
			this.generateBox(level, box, 2, -1, 2, 7, 0, 9, floorBlock, floorBlock, false);

			this.generateBox(level, box, 2, -1, -1, 5, -1, -1, floorBlock, floorBlock, false);
			this.generateBox(level, box, 6, -1, 10, 7, -1, 10, floorBlock, floorBlock, false);
			this.generateBox(level, box, 8, -1, 8, 8, -1, 9, floorBlock, floorBlock, false);

			this.generateBox(level, box, 1, 1, 2, 1, 1, 6, plankBlock, plankBlock, false);
			this.placeBlock(level, plankBlock, 0, 1, 2, box);
			this.placeBlock(level, plankSlab0, 0, 1, 3, box);
			this.placeBlock(level, plankBlock, 0, 1, 4, box);
			this.placeBlock(level, plankSlab0, 0, 1, 5, box);
			this.placeBlock(level, plankBlock, 0, 1, 6, box);
			this.generateBox(level, box, 1, 2, 2, 1, 3, 2, plankBlock, plankBlock, false);
			this.generateBox(level, box, 1, 2, 6, 1, 3, 6, plankBlock, plankBlock, false);

			this.generateBox(level, box, 6, 1, 2, 6, 1, 6, plankBlock, plankBlock, false);
			this.placeBlock(level, plankBlock, 7, 1, 2, box);
			this.placeBlock(level, plankSlab0, 7, 1, 3, box);
			this.placeBlock(level, plankBlock, 7, 1, 4, box);
			this.placeBlock(level, plankSlab0, 7, 1, 5, box);
			this.placeBlock(level, plankBlock, 7, 1, 6, box);
			this.generateBox(level, box, 6, 2, 2, 6, 3, 2, plankBlock, plankBlock, false);
			this.generateBox(level, box, 6, 2, 6, 6, 3, 6, plankBlock, plankBlock, false);

			this.generateBox(level, box, 2, 1, 8, 2, 3, 8, plankBlock, plankBlock, false);
			this.generateBox(level, box, 4, 1, 8, 4, 3, 8, plankBlock, plankBlock, false);
			this.placeBlock(level, plankBlock, 3, 1, 8, box);
			this.placeBlock(level, plankBlock, 2, 1, 9, box);
			this.placeBlock(level, plankSlab0, 3, 1, 9, box);
			this.placeBlock(level, plankBlock, 4, 1, 9, box);

			this.generateBox(level, box, 1, 3, 1, 6, 3, 1, plankSlab1, plankSlab1, false);
			this.generateBox(level, box, 2, 3, 2, 5, 3, 6, plankSlab1, plankSlab1, false);
			this.generateBox(level, box, 0, 3, 2, 0, 3, 6, plankSlab1, plankSlab1, false);
			this.generateBox(level, box, 7, 3, 2, 7, 3, 6, plankSlab1, plankSlab1, false);
			this.generateBox(level, box, 1, 3, 3, 1, 3, 5, plankSlab1, plankSlab1, false);
			this.generateBox(level, box, 6, 3, 3, 6, 3, 5, plankSlab1, plankSlab1, false);
			this.generateBox(level, box, 1, 3, 7, 6, 3, 7, plankSlab1, plankSlab1, false);
			this.generateBox(level, box, 5, 3, 8, 6, 3, 8, plankSlab1, plankSlab1, false);
			this.generateBox(level, box, 2, 3, 9, 4, 3, 9, plankSlab1, plankSlab1, false);
			this.placeBlock(level, plankSlab1, 3, 3, 8, box);

			this.generateBox(level, box, 2, 4, 2, 5, 4,  2, plankSlab0, plankSlab0, false);
			this.generateBox(level, box, 1, 4, 3, 6, 4,  5, plankSlab0, plankSlab0, false);
			this.generateBox(level, box, 2, 4, 6, 5, 4,  6, plankSlab0, plankSlab0, false);
			this.generateBox(level, box, 3, 4, 7, 4, 4,  7, plankSlab0, plankSlab0, false);
			this.placeBlock(level, plankSlab0, 3, 4, 8, box);

			this.generateAirBox(level, box, 2, 1, 0, 5, 3, 0);
			this.generateAirBox(level, box, 1, 1, 1, 6, 2, 1);
			this.generateAirBox(level, box, 2, 1, 2, 5, 2, 7);
			this.generateAirBox(level, box, 0, 2, 3, 1, 2, 5);
			this.generateAirBox(level, box, 6, 2, 3, 7, 2, 5);
			this.generateAirBox(level, box, 2, 2, 9, 4, 2, 9);
			this.placeBlock(level, Blocks.AIR.defaultBlockState(), 3, 2, 8, box);
			this.generateAirBox(level, box, 0, 1, 7, 1, 2, 9);
			this.generateAirBox(level, box, 5, 1, 7, 7, 2, 9);

			placeBlock(level, torch, 1, 2, 4, box);
			placeBlock(level, torch, 6, 2, 4, box);

			if(!spawns[0])
				spawns[0] = spawnConsort(0, 2, 3, box, level, chunkGeneratorIn, EnumConsort.MerchantType.FOOD, 1);
			if(!spawns[1])
				spawns[1] = spawnConsort(0, 2, 5, box, level, chunkGeneratorIn, EnumConsort.MerchantType.FOOD, 1);
			if(!spawns[2])
				spawns[2] = spawnConsort(7, 2, 3, box, level, chunkGeneratorIn, EnumConsort.MerchantType.GENERAL, 1);
			if(!spawns[3])
				spawns[3] = spawnConsort(7, 2, 5, box, level, chunkGeneratorIn, EnumConsort.MerchantType.GENERAL, 1);
			if(!spawns[4])
				spawns[4] = spawnConsort(3, 2, 9, box, level, chunkGeneratorIn, EnumConsort.getRandomMerchant(randomIn), 1);
		}
		
	}
	
	public static class RockCenter extends VillageCenter
	{
		public RockCenter(List<ConsortVillagePieces.PieceWeight> pieceWeightList, int x, int z, RandomSource rand)
		{
			super(MSStructures.ConsortVillage.ROCK_CENTER_PIECE.get(), pieceWeightList, 0, new BoundingBox(x, 64, z, x + 7 - 1, 68, z + 7 - 1), 0);
			this.setOrientation(Direction.Plane.HORIZONTAL.getRandomDirection(rand));
		}
		
		public RockCenter(CompoundTag nbt)
		{
			super(MSStructures.ConsortVillage.ROCK_CENTER_PIECE.get(), nbt, 0);
		}
		
		@Override
		public void addChildren(StructurePiece componentIn, StructurePieceAccessor accessor, RandomSource rand)
		{
			ConsortVillagePieces.generateAndAddRoadPiece((VillageCenter) componentIn, accessor, rand, boundingBox.minX() + 3, boundingBox.minY(), boundingBox.maxZ() + 1, Direction.SOUTH);
			ConsortVillagePieces.generateAndAddRoadPiece((VillageCenter) componentIn, accessor, rand, boundingBox.minX() - 1, boundingBox.minY(), boundingBox.minZ() + 3, Direction.WEST);
			ConsortVillagePieces.generateAndAddRoadPiece((VillageCenter) componentIn, accessor, rand, boundingBox.minX() + 3, boundingBox.minY(), boundingBox.minZ() - 1, Direction.NORTH);
			ConsortVillagePieces.generateAndAddRoadPiece((VillageCenter) componentIn, accessor, rand, boundingBox.maxX() + 1, boundingBox.minY(), boundingBox.minZ() + 3, Direction.EAST);
		}

		@Override
		public void postProcess(WorldGenLevel level, StructureManager manager, ChunkGenerator chunkGeneratorIn, RandomSource randomIn, BoundingBox box, ChunkPos chunkPosIn, BlockPos pos)
		{
			if (this.averageGroundLvl < 0)
			{
				this.averageGroundLvl = this.getAverageGroundLevel(level, chunkGeneratorIn, box);

				if (this.averageGroundLvl < 0)
				{
					return;
				}

				this.boundingBox.move(0, this.averageGroundLvl - this.boundingBox.minY() - 1, 0);
			}

			StructureBlockRegistry blocks = StructureBlockRegistry.getOrDefault(chunkGeneratorIn);
			BlockState road = blocks.getBlockState("village_path");
			BlockState rock = blocks.getBlockState("ground");

			for(int x = 0; x < 7; x++)
			{
				for (int z = 0; z < 2; z++)
					this.placeRoadtile(this.boundingBox.minX() + x, this.boundingBox.minZ() + z, box, level, road);

				for (int z = 5; z < 7; z++)
					this.placeRoadtile(this.boundingBox.minX() + x, this.boundingBox.minZ() + z, box, level, road);
			}

			for(int z = 2; z < 5; z++)
			{
				for (int x = 0; x < 2; x++)
					this.placeRoadtile(this.boundingBox.minX() + x, this.boundingBox.minZ() + z, box, level, road);

				for (int x = 5; x < 7; x++)
					this.placeRoadtile(this.boundingBox.minX() + x, this.boundingBox.minZ() + z, box, level, road);
			}
			this.generateAirBox(level, box, 2, 1, 2, 4, 4, 4);

			this.blockPillar(2, randomIn.nextInt(2) + randomIn.nextInt(2), 2, box, level, rock);
			this.blockPillar(4, randomIn.nextInt(2) + randomIn.nextInt(2), 2, box, level, rock);
			this.blockPillar(2, randomIn.nextInt(2) + randomIn.nextInt(2), 4, box, level, rock);
			this.blockPillar(4, randomIn.nextInt(2) + randomIn.nextInt(2), 4, box, level, rock);

			this.blockPillar(3, 2 + (randomIn.nextInt(4) == 3 ? 1 : 0), 2, box, level, rock);
			this.blockPillar(3, 2 + (randomIn.nextInt(4) == 3 ? 1 : 0), 4, box, level, rock);
			this.blockPillar(2, 2 + (randomIn.nextInt(4) == 3 ? 1 : 0), 3, box, level, rock);
			this.blockPillar(4, 2 + (randomIn.nextInt(4) == 3 ? 1 : 0), 3, box, level, rock);
			this.blockPillar(3, 3, 3, box, level, rock);
		}
		
	}
	
	public static class CactusPyramidCenter extends VillageCenter
	{
		public CactusPyramidCenter(List<ConsortVillagePieces.PieceWeight> pieceWeightList, int x, int z, RandomSource rand)
		{
			super(MSStructures.ConsortVillage.CACTUS_PYRAMID_CENTER_PIECE.get(), pieceWeightList, 0, new BoundingBox(x, 64, z, x + 16 - 1, 73, z + 16 - 1), 0);
			this.setOrientation(getRandomHorizontalDirection(rand));
		}
		
		public CactusPyramidCenter(CompoundTag nbt)
		{
			super(MSStructures.ConsortVillage.CACTUS_PYRAMID_CENTER_PIECE.get(), nbt, 0);
		}
		
		@Override
		public void addChildren(StructurePiece componentIn, StructurePieceAccessor accessor, RandomSource rand)
		{
			ConsortVillagePieces.generateAndAddRoadPiece((VillageCenter) componentIn, accessor, rand, boundingBox.minX() + 7, boundingBox.minY(), boundingBox.maxZ() + 1, Direction.SOUTH);
			ConsortVillagePieces.generateAndAddRoadPiece((VillageCenter) componentIn, accessor, rand, boundingBox.minX() - 1, boundingBox.minY(), boundingBox.minZ() + 7, Direction.WEST);
			ConsortVillagePieces.generateAndAddRoadPiece((VillageCenter) componentIn, accessor, rand, boundingBox.minX() + 7, boundingBox.minY(), boundingBox.minZ() - 1, Direction.NORTH);
			ConsortVillagePieces.generateAndAddRoadPiece((VillageCenter) componentIn, accessor, rand, boundingBox.maxX() + 1, boundingBox.minY(), boundingBox.minZ() + 7, Direction.EAST);
		}

		@Override
		public void postProcess(WorldGenLevel level, StructureManager manager, ChunkGenerator chunkGeneratorIn, RandomSource randomIn, BoundingBox structureBoundingBoxIn, ChunkPos chunkPosIn, BlockPos pos)
		{
			if (this.averageGroundLvl < 0)
			{
				this.averageGroundLvl = this.getAverageGroundLevel(level, chunkGeneratorIn, structureBoundingBoxIn);

				if (this.averageGroundLvl < 0)
					return;

				this.boundingBox.move(0, this.averageGroundLvl - this.boundingBox.minY() - 2, 0);
			}

			StructureBlockRegistry blocks = StructureBlockRegistry.getOrDefault(chunkGeneratorIn);
			BlockState road = blocks.getBlockState("village_path");
			BlockState surface = blocks.getBlockState("surface");
			BlockState cactus = Blocks.CACTUS.defaultBlockState(); //todo: replace with land-specific cactus block

			for(int x = 1; x < 15; x++)
				for(int z = 1; z < 15; z++)
					this.blockPillar(x, 0, z, structureBoundingBoxIn, level, surface);

			this.generateBox(level, structureBoundingBoxIn, 2, 2, 2, 13, 2, 13, surface, surface, false);
			this.generateBox(level, structureBoundingBoxIn, 3, 3, 3, 12, 3, 12, surface, surface, false);
			this.generateBox(level, structureBoundingBoxIn, 4, 4, 4, 11, 4, 11, surface, surface, false);
			this.generateBox(level, structureBoundingBoxIn, 5, 5, 5, 10, 5, 10, surface, surface, false);
			this.generateBox(level, structureBoundingBoxIn, 6, 6, 6, 9, 6, 9, surface, surface, false);

			this.generateBox(level, structureBoundingBoxIn, 6, 7, 6, 6, 9, 6, cactus, cactus, false);
			this.generateBox(level, structureBoundingBoxIn, 9, 7, 6, 9, 9, 6, cactus, cactus, false);
			this.generateBox(level, structureBoundingBoxIn, 6, 7, 9, 6, 9, 9, cactus, cactus, false);
			this.generateBox(level, structureBoundingBoxIn, 9, 7, 9, 9, 9, 9, cactus, cactus, false);

			for(int x = 0; x < 16; x++)
			{
				for (int z = 0; z < 2; z++)
					this.placeRoadtile(this.boundingBox.minX() + x, this.boundingBox.minZ() + z, structureBoundingBoxIn, level, road);

				for (int z = 14; z < 16; z++)
					this.placeRoadtile(this.boundingBox.minX() + x, this.boundingBox.minZ() + z, structureBoundingBoxIn, level, road);
			}

			for(int z = 2; z < 14; z++)
			{
				for (int x = 0; x < 2; x++)
					this.placeRoadtile(this.boundingBox.minX() + x, this.boundingBox.minZ() + z, structureBoundingBoxIn, level, road);

				for (int x = 14; x < 16; x++)
					this.placeRoadtile(this.boundingBox.minX() + x, this.boundingBox.minZ() + z, structureBoundingBoxIn, level, road);
			}
		}
		
	}
}
