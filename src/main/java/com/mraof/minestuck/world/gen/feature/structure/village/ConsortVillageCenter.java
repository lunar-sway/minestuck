package com.mraof.minestuck.world.gen.feature.structure.village;

import com.google.common.collect.Lists;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.entity.consort.EnumConsort;
import com.mraof.minestuck.world.gen.feature.MSStructurePieces;
import com.mraof.minestuck.world.gen.feature.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.lands.LandTypePair;
import com.mraof.minestuck.world.lands.terrain.RockLandType;
import com.mraof.minestuck.world.lands.terrain.SandLandType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SlabBlock;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.SlabType;
import net.minecraft.util.Direction;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.List;
import java.util.Random;

/**
 * Created by kirderf1
 */
public class ConsortVillageCenter
{
	private static class CenterEntry extends WeightedRandom.Item
	{
		private final CenterFactory center;
		private CenterEntry(CenterFactory center, int weight)
		{
			super(weight);
			this.center = center;
		}
	}
	
	public static ConsortVillageCenter.VillageCenter getVillageStart(int x, int z, Random rand, List<ConsortVillagePieces.PieceWeight> list, LandTypePair landTypes)
	{
		List<CenterEntry> weightList = Lists.newArrayList();
		
		//TODO Let land types provide these instead
		if(landTypes.terrain.getGroup().equals(RockLandType.GROUP_NAME))
			weightList.add(new CenterEntry(RockCenter::new, 5));
		if(landTypes.terrain.getGroup().equals(SandLandType.GROUP_NAME))
			weightList.add(new CenterEntry(CactusPyramidCenter::new, 5));
		if(landTypes.terrain.getConsortType().equals(MSEntityTypes.TURTLE))
			weightList.add(new CenterEntry(TurtleVillagePieces.TurtleWellCenter::new, 5));
		if(landTypes.terrain.getConsortType().equals(MSEntityTypes.NAKAGATOR))
			weightList.add(new CenterEntry(NakagatorVillagePieces.RadioTowerCenter::new, 5));
		if(landTypes.terrain.getConsortType().equals(MSEntityTypes.SALAMANDER))
			weightList.add(new CenterEntry(SalamanderVillagePieces.RuinedTowerMushroomCenter::new, 5));
		
		if(weightList.isEmpty())
			return new ConsortVillageCenter.VillageMarketCenter(list, x, z, rand);
		else
		{
			return WeightedRandom.getRandomItem(rand, weightList).center.createPiece(list, x, z, rand);
		}
	}
	
	public interface CenterFactory
	{
		VillageCenter createPiece(List<ConsortVillagePieces.PieceWeight> pieceWeightList, int x, int z, Random rand);
	}
	
	public abstract static class VillageCenter extends ConsortVillagePieces.ConsortVillagePiece
	{
		public List<StructurePiece> pendingHouses = Lists.newArrayList();
		public List<StructurePiece> pendingRoads = Lists.newArrayList();
		
		public List<ConsortVillagePieces.PieceWeight> pieceWeightList;
		public ConsortVillagePieces.PieceWeight lastPieceWeightUsed;
		
		public VillageCenter(IStructurePieceType structurePieceTypeIn, List<ConsortVillagePieces.PieceWeight> pieceWeightList, int componentTypeIn, int spawnCount)
		{
			super(structurePieceTypeIn, componentTypeIn, spawnCount);
			this.pieceWeightList = pieceWeightList;
		}
		
		public VillageCenter(IStructurePieceType structurePierceTypeIn, CompoundNBT nbt, int spawnCount)
		{
			super(structurePierceTypeIn, nbt, spawnCount);
		}
	}
	
	public static class VillageMarketCenter extends VillageCenter
	{
		public VillageMarketCenter(List<ConsortVillagePieces.PieceWeight> pieceWeightList, int x, int z, Random rand)
		{
			super(MSStructurePieces.MARKET_CENTER, pieceWeightList, 0, 5);
			setCoordBaseMode(Direction.Plane.HORIZONTAL.random(rand));
			
			if (getCoordBaseMode().getAxis() == Direction.Axis.Z)
			{
				boundingBox = new MutableBoundingBox(x, 64, z, x + 8 - 1, 78, z + 10 - 1);
			} else
			{
				boundingBox = new MutableBoundingBox(x, 64, z, x + 10 - 1, 78, z + 8 - 1);
			}
		}
		
		public VillageMarketCenter(TemplateManager templates, CompoundNBT nbt)
		{
			super(MSStructurePieces.MARKET_CENTER, nbt, 5);
		}
		
		
		@Override
		public void buildComponent(StructurePiece componentIn, List<StructurePiece> listIn, Random rand)
		{
			switch(this.getCoordBaseMode())
			{	//The vanilla code for "rotating coords based on facing" is messy
				case NORTH:
					ConsortVillagePieces.generateAndAddRoadPiece((VillageCenter) componentIn, listIn, rand, boundingBox.minX + 3, boundingBox.minY, boundingBox.maxZ + 1, Direction.SOUTH);
					ConsortVillagePieces.generateAndAddRoadPiece((VillageCenter) componentIn, listIn, rand, boundingBox.maxX - 1, boundingBox.minY, boundingBox.minZ - 1, Direction.NORTH);
					ConsortVillagePieces.generateAndAddRoadPiece((VillageCenter) componentIn, listIn, rand, boundingBox.maxX + 1, boundingBox.minY, boundingBox.minZ, Direction.EAST);
					break;
				case EAST:
					ConsortVillagePieces.generateAndAddRoadPiece((VillageCenter) componentIn, listIn, rand, boundingBox.minX - 1, boundingBox.minY, boundingBox.minZ + 3, Direction.WEST);
					ConsortVillagePieces.generateAndAddRoadPiece((VillageCenter) componentIn, listIn, rand, boundingBox.maxX + 1, boundingBox.minY, boundingBox.maxZ - 1, Direction.EAST);
					ConsortVillagePieces.generateAndAddRoadPiece((VillageCenter) componentIn, listIn, rand, boundingBox.maxX - 1, boundingBox.minY, boundingBox.maxZ + 1, Direction.SOUTH);
					break;
				case SOUTH:
					ConsortVillagePieces.generateAndAddRoadPiece((VillageCenter) componentIn, listIn, rand, boundingBox.minX + 3, boundingBox.minY, boundingBox.minZ - 1, Direction.NORTH);
					ConsortVillagePieces.generateAndAddRoadPiece((VillageCenter) componentIn, listIn, rand, boundingBox.maxX - 1, boundingBox.minY, boundingBox.maxZ + 1, Direction.SOUTH);
					ConsortVillagePieces.generateAndAddRoadPiece((VillageCenter) componentIn, listIn, rand, boundingBox.maxX + 1, boundingBox.minY, boundingBox.maxZ - 1, Direction.EAST);
					break;
				case WEST:
					ConsortVillagePieces.generateAndAddRoadPiece((VillageCenter) componentIn, listIn, rand, boundingBox.maxX + 1, boundingBox.minY, boundingBox.minZ + 3, Direction.EAST);
					ConsortVillagePieces.generateAndAddRoadPiece((VillageCenter) componentIn, listIn, rand, boundingBox.minX - 1, boundingBox.minY, boundingBox.maxZ - 1, Direction.WEST);
					ConsortVillagePieces.generateAndAddRoadPiece((VillageCenter) componentIn, listIn, rand, boundingBox.minX, boundingBox.minY, boundingBox.maxZ + 1, Direction.SOUTH);
					break;
				default:
			}
		}

		@Override
		public boolean create(IWorld worldIn, ChunkGenerator<?> chunkGeneratorIn, Random randomIn, MutableBoundingBox structureBoundingBoxIn, ChunkPos chunkPosIn)
		{
			if (this.averageGroundLvl < 0)
			{
				this.averageGroundLvl = this.getAverageGroundLevel(worldIn, chunkGeneratorIn, structureBoundingBoxIn);

				if (this.averageGroundLvl < 0)
				{
					return true;
				}

				this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.minY, 0);
			}

			StructureBlockRegistry blocks = StructureBlockRegistry.getOrDefault(chunkGeneratorIn.getSettings());
			BlockState floorBlock = blocks.getBlockState("structure_secondary");
			BlockState plankBlock = blocks.getBlockState("structure_planks");
			BlockState plankSlab0 = blocks.getBlockState("structure_planks_slab");
			BlockState plankSlab1 = plankSlab0.with(SlabBlock.TYPE, SlabType.TOP);
			BlockState torch = blocks.getBlockState("torch");


			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, -1, 0, 5, 0, 0, floorBlock, floorBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, -1, 1, 6, 0, 1, floorBlock, floorBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, -1, 2, 1, 0, 7, floorBlock, floorBlock, false);
			this.setBlockState(worldIn, floorBlock, 1, 0, 8, structureBoundingBoxIn);
			this.setBlockState(worldIn, floorBlock, 1, -1, 8, structureBoundingBoxIn);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, -1, 2, 7, 0, 9, floorBlock, floorBlock, false);

			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, -1, -1, 5, -1, -1, floorBlock, floorBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6, -1, 10, 7, -1, 10, floorBlock, floorBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 8, -1, 8, 8, -1, 9, floorBlock, floorBlock, false);

			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 2, 1, 1, 6, plankBlock, plankBlock, false);
			this.setBlockState(worldIn, plankBlock, 0, 1, 2, structureBoundingBoxIn);
			this.setBlockState(worldIn, plankSlab0, 0, 1, 3, structureBoundingBoxIn);
			this.setBlockState(worldIn, plankBlock, 0, 1, 4, structureBoundingBoxIn);
			this.setBlockState(worldIn, plankSlab0, 0, 1, 5, structureBoundingBoxIn);
			this.setBlockState(worldIn, plankBlock, 0, 1, 6, structureBoundingBoxIn);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 2, 2, 1, 3, 2, plankBlock, plankBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 2, 6, 1, 3, 6, plankBlock, plankBlock, false);

			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 1, 2, 6, 1, 6, plankBlock, plankBlock, false);
			this.setBlockState(worldIn, plankBlock, 7, 1, 2, structureBoundingBoxIn);
			this.setBlockState(worldIn, plankSlab0, 7, 1, 3, structureBoundingBoxIn);
			this.setBlockState(worldIn, plankBlock, 7, 1, 4, structureBoundingBoxIn);
			this.setBlockState(worldIn, plankSlab0, 7, 1, 5, structureBoundingBoxIn);
			this.setBlockState(worldIn, plankBlock, 7, 1, 6, structureBoundingBoxIn);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 2, 2, 6, 3, 2, plankBlock, plankBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 2, 6, 6, 3, 6, plankBlock, plankBlock, false);

			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 1, 8, 2, 3, 8, plankBlock, plankBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 1, 8, 4, 3, 8, plankBlock, plankBlock, false);
			this.setBlockState(worldIn, plankBlock, 3, 1, 8, structureBoundingBoxIn);
			this.setBlockState(worldIn, plankBlock, 2, 1, 9, structureBoundingBoxIn);
			this.setBlockState(worldIn, plankSlab0, 3, 1, 9, structureBoundingBoxIn);
			this.setBlockState(worldIn, plankBlock, 4, 1, 9, structureBoundingBoxIn);

			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 3, 1, 6, 3, 1, plankSlab1, plankSlab1, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 3, 2, 5, 3, 6, plankSlab1, plankSlab1, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 3, 2, 0, 3, 6, plankSlab1, plankSlab1, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 3, 2, 7, 3, 6, plankSlab1, plankSlab1, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 3, 3, 1, 3, 5, plankSlab1, plankSlab1, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 3, 3, 6, 3, 5, plankSlab1, plankSlab1, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 3, 7, 6, 3, 7, plankSlab1, plankSlab1, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 3, 8, 6, 3, 8, plankSlab1, plankSlab1, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 3, 9, 4, 3, 9, plankSlab1, plankSlab1, false);
			this.setBlockState(worldIn, plankSlab1, 3, 3, 8, structureBoundingBoxIn);

			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 4, 2, 5, 4,  2, plankSlab0, plankSlab0, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 4, 3, 6, 4,  5, plankSlab0, plankSlab0, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 4, 6, 5, 4,  6, plankSlab0, plankSlab0, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 4, 7, 4, 4,  7, plankSlab0, plankSlab0, false);
			this.setBlockState(worldIn, plankSlab0, 3, 4, 8, structureBoundingBoxIn);

			this.fillWithAir(worldIn, structureBoundingBoxIn, 2, 1, 0, 5, 3, 0);
			this.fillWithAir(worldIn, structureBoundingBoxIn, 1, 1, 1, 6, 2, 1);
			this.fillWithAir(worldIn, structureBoundingBoxIn, 2, 1, 2, 5, 2, 7);
			this.fillWithAir(worldIn, structureBoundingBoxIn, 0, 2, 3, 1, 2, 5);
			this.fillWithAir(worldIn, structureBoundingBoxIn, 6, 2, 3, 7, 2, 5);
			this.fillWithAir(worldIn, structureBoundingBoxIn, 2, 2, 9, 4, 2, 9);
			this.setBlockState(worldIn, Blocks.AIR.getDefaultState(), 3, 2, 8, structureBoundingBoxIn);
			this.fillWithAir(worldIn, structureBoundingBoxIn, 0, 1, 7, 1, 2, 9);
			this.fillWithAir(worldIn, structureBoundingBoxIn, 5, 1, 7, 7, 2, 9);

			setBlockState(worldIn, torch, 1, 2, 4, structureBoundingBoxIn);
			setBlockState(worldIn, torch, 6, 2, 4, structureBoundingBoxIn);

			if(!spawns[0])
				spawns[0] = spawnConsort(0, 2, 3, boundingBox, worldIn, chunkGeneratorIn, EnumConsort.MerchantType.FOOD, 1);
			if(!spawns[1])
				spawns[1] = spawnConsort(0, 2, 5, boundingBox, worldIn, chunkGeneratorIn, EnumConsort.MerchantType.FOOD, 1);
			if(!spawns[2])
				spawns[2] = spawnConsort(7, 2, 3, boundingBox, worldIn, chunkGeneratorIn, EnumConsort.MerchantType.GENERAL, 1);
			if(!spawns[3])
				spawns[3] = spawnConsort(7, 2, 5, boundingBox, worldIn, chunkGeneratorIn, EnumConsort.MerchantType.GENERAL, 1);
			if(!spawns[4])
				spawns[4] = spawnConsort(3, 2, 9, boundingBox, worldIn, chunkGeneratorIn, EnumConsort.getRandomMerchant(randomIn), 1);

			return true;
		}
		
	}
	
	public static class RockCenter extends VillageCenter
	{
		public RockCenter(List<ConsortVillagePieces.PieceWeight> pieceWeightList, int x, int z, Random rand)
		{
			super(MSStructurePieces.ROCK_CENTER, pieceWeightList, 0, 0);
			this.setCoordBaseMode(Direction.Plane.HORIZONTAL.random(rand));
			
			this.boundingBox = new MutableBoundingBox(x, 64, z, x + 7 - 1, 68, z + 7 - 1);
		}
		
		public RockCenter(TemplateManager templates, CompoundNBT nbt)
		{
			super(MSStructurePieces.ROCK_CENTER, nbt, 0);
		}
		
		@Override
		public void buildComponent(StructurePiece componentIn, List<StructurePiece> listIn, Random rand)
		{
			ConsortVillagePieces.generateAndAddRoadPiece((VillageCenter) componentIn, listIn, rand, boundingBox.minX + 3, boundingBox.minY, boundingBox.maxZ + 1, Direction.SOUTH);
			ConsortVillagePieces.generateAndAddRoadPiece((VillageCenter) componentIn, listIn, rand, boundingBox.minX - 1, boundingBox.minY, boundingBox.minZ + 3, Direction.WEST);
			ConsortVillagePieces.generateAndAddRoadPiece((VillageCenter) componentIn, listIn, rand, boundingBox.minX + 3, boundingBox.minY, boundingBox.minZ - 1, Direction.NORTH);
			ConsortVillagePieces.generateAndAddRoadPiece((VillageCenter) componentIn, listIn, rand, boundingBox.maxX + 1, boundingBox.minY, boundingBox.minZ + 3, Direction.EAST);
		}

		@Override
		public boolean create(IWorld worldIn, ChunkGenerator<?> chunkGeneratorIn, Random randomIn, MutableBoundingBox structureBoundingBoxIn, ChunkPos chunkPosIn)
		{
			if (this.averageGroundLvl < 0)
			{
				this.averageGroundLvl = this.getAverageGroundLevel(worldIn, chunkGeneratorIn, structureBoundingBoxIn);

				if (this.averageGroundLvl < 0)
				{
					return true;
				}

				this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.minY - 1, 0);
			}

			StructureBlockRegistry blocks = StructureBlockRegistry.getOrDefault(chunkGeneratorIn.getSettings());
			BlockState road = blocks.getBlockState("village_path");
			BlockState rock = blocks.getBlockState("ground");

			for(int x = 0; x < 7; x++)
			{
				for (int z = 0; z < 2; z++)
					this.placeRoadtile(this.boundingBox.minX + x, this.boundingBox.minZ + z, structureBoundingBoxIn, worldIn, road);

				for (int z = 5; z < 7; z++)
					this.placeRoadtile(this.boundingBox.minX + x, this.boundingBox.minZ + z, structureBoundingBoxIn, worldIn, road);
			}

			for(int z = 2; z < 5; z++)
			{
				for (int x = 0; x < 2; x++)
					this.placeRoadtile(this.boundingBox.minX + x, this.boundingBox.minZ + z, structureBoundingBoxIn, worldIn, road);

				for (int x = 5; x < 7; x++)
					this.placeRoadtile(this.boundingBox.minX + x, this.boundingBox.minZ + z, structureBoundingBoxIn, worldIn, road);
			}
			this.fillWithAir(worldIn, structureBoundingBoxIn, 2, 1, 2, 4, 4, 4);

			this.blockPillar(2, randomIn.nextInt(2) + randomIn.nextInt(2), 2, boundingBox, worldIn, rock);
			this.blockPillar(4, randomIn.nextInt(2) + randomIn.nextInt(2), 2, boundingBox, worldIn, rock);
			this.blockPillar(2, randomIn.nextInt(2) + randomIn.nextInt(2), 4, boundingBox, worldIn, rock);
			this.blockPillar(4, randomIn.nextInt(2) + randomIn.nextInt(2), 4, boundingBox, worldIn, rock);

			this.blockPillar(3, 2 + (randomIn.nextInt(4) == 3 ? 1 : 0), 2, boundingBox, worldIn, rock);
			this.blockPillar(3, 2 + (randomIn.nextInt(4) == 3 ? 1 : 0), 4, boundingBox, worldIn, rock);
			this.blockPillar(2, 2 + (randomIn.nextInt(4) == 3 ? 1 : 0), 3, boundingBox, worldIn, rock);
			this.blockPillar(4, 2 + (randomIn.nextInt(4) == 3 ? 1 : 0), 3, boundingBox, worldIn, rock);
			this.blockPillar(3, 3, 3, boundingBox, worldIn, rock);

			return true;
		}
		
	}
	
	public static class CactusPyramidCenter extends VillageCenter
	{
		public CactusPyramidCenter(List<ConsortVillagePieces.PieceWeight> pieceWeightList, int x, int z, Random rand)
		{
			super(MSStructurePieces.CACTUS_PYRAMID_CENTER, pieceWeightList, 0, 0);
			this.setCoordBaseMode(Direction.Plane.HORIZONTAL.random(rand));
			
			this.boundingBox = new MutableBoundingBox(x, 64, z, x + 16 - 1, 73, z + 16 - 1);
		}
		
		public CactusPyramidCenter(TemplateManager templates, CompoundNBT nbt)
		{
			super(MSStructurePieces.CACTUS_PYRAMID_CENTER, nbt, 0);
		}
		
		@Override
		public void buildComponent(StructurePiece componentIn, List<StructurePiece> listIn, Random rand)
		{
			ConsortVillagePieces.generateAndAddRoadPiece((VillageCenter) componentIn, listIn, rand, boundingBox.minX + 7, boundingBox.minY, boundingBox.maxZ + 1, Direction.SOUTH);
			ConsortVillagePieces.generateAndAddRoadPiece((VillageCenter) componentIn, listIn, rand, boundingBox.minX - 1, boundingBox.minY, boundingBox.minZ + 7, Direction.WEST);
			ConsortVillagePieces.generateAndAddRoadPiece((VillageCenter) componentIn, listIn, rand, boundingBox.minX + 7, boundingBox.minY, boundingBox.minZ - 1, Direction.NORTH);
			ConsortVillagePieces.generateAndAddRoadPiece((VillageCenter) componentIn, listIn, rand, boundingBox.maxX + 1, boundingBox.minY, boundingBox.minZ + 7, Direction.EAST);
		}

		@Override
		public boolean create(IWorld worldIn, ChunkGenerator<?> chunkGeneratorIn, Random randomIn, MutableBoundingBox structureBoundingBoxIn, ChunkPos chunkPosIn)
		{
			if (this.averageGroundLvl < 0)
			{
				this.averageGroundLvl = this.getAverageGroundLevel(worldIn, chunkGeneratorIn, structureBoundingBoxIn);

				if (this.averageGroundLvl < 0)
				{
					return true;
				}

				this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.minY - 2, 0);
			}

			StructureBlockRegistry blocks = StructureBlockRegistry.getOrDefault(chunkGeneratorIn.getSettings());
			BlockState road = blocks.getBlockState("village_path");
			BlockState surface = blocks.getBlockState("surface");
			BlockState cactus = MSBlocks.WOODEN_CACTUS.getDefaultState();

			for(int x = 1; x < 15; x++)
				for(int z = 1; z < 15; z++)
					this.blockPillar(x, 0, z, structureBoundingBoxIn, worldIn, surface);

			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 2, 2, 13, 2, 13, surface, surface, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 3, 3, 12, 3, 12, surface, surface, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 4, 4, 11, 4, 11, surface, surface, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 5, 5, 10, 5, 10, surface, surface, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 6, 6, 9, 6, 9, surface, surface, false);

			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 7, 6, 6, 9, 6, cactus, cactus, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 9, 7, 6, 9, 9, 6, cactus, cactus, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 7, 9, 6, 9, 9, cactus, cactus, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 9, 7, 9, 9, 9, 9, cactus, cactus, false);

			for(int x = 0; x < 16; x++)
			{
				for (int z = 0; z < 2; z++)
					this.placeRoadtile(this.boundingBox.minX + x, this.boundingBox.minZ + z, structureBoundingBoxIn, worldIn, road);

				for (int z = 14; z < 16; z++)
					this.placeRoadtile(this.boundingBox.minX + x, this.boundingBox.minZ + z, structureBoundingBoxIn, worldIn, road);
			}

			for(int z = 2; z < 14; z++)
			{
				for (int x = 0; x < 2; x++)
					this.placeRoadtile(this.boundingBox.minX + x, this.boundingBox.minZ + z, structureBoundingBoxIn, worldIn, road);

				for (int x = 14; x < 16; x++)
					this.placeRoadtile(this.boundingBox.minX + x, this.boundingBox.minZ + z, structureBoundingBoxIn, worldIn, road);
			}

			return true;
		}
		
	}
}