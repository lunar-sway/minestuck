package com.mraof.minestuck.world.lands.structure.village;

import com.google.common.collect.Lists;
import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.entity.consort.EnumConsort;
import com.mraof.minestuck.world.lands.LandAspectRegistry;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStoneSlab;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.WeightedRandom;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;

import java.util.List;
import java.util.Random;

/**
 * Created by kirderf1
 */
public class ConsortVillageCenter
{
	public static void register()
	{
		MapGenStructureIO.registerStructureComponent(ConsortVillageCenter.VillageMarketCenter.class, "MinestuckCVCM");
		MapGenStructureIO.registerStructureComponent(ConsortVillageCenter.RockCenter.class, "MinestuckCVCRo");
		MapGenStructureIO.registerStructureComponent(ConsortVillageCenter.CactusPyramidCenter.class, "MinestuckCVCCaPy");
		MapGenStructureIO.registerStructureComponent(ConsortVillageCenter.TurtleWellCenter.class, "MinestuckCVCTuWe");
		MapGenStructureIO.registerStructureComponent(ConsortVillageCenter.RadioTowerCenter.class, "MinestuckCVCRaTo");
		MapGenStructureIO.registerStructureComponent(ConsortVillageCenter.RuinedTowerMushroomCenter.class, "MinestuckCVCRuToM");
		
	}
	
	private static class CenterEntry extends WeightedRandom.Item
	{
		private final Class<? extends VillageCenter> center;
		private CenterEntry(Class<? extends VillageCenter> center, int weight)
		{
			super(weight);
			this.center = center;
		}
	}
	
	public static ConsortVillageCenter.VillageCenter getVillageStart(ChunkProviderLands provider, int x, int z, Random rand, List<ConsortVillageComponents.PieceWeight> list, LandAspectRegistry.AspectCombination landAspects)
	{
		List<CenterEntry> weightList = Lists.newArrayList();
		
		if(landAspects.aspectTerrain.getPrimaryVariant().getPrimaryName().equals("rock"))
			weightList.add(new CenterEntry(RockCenter.class, 5));
		if(landAspects.aspectTerrain.getPrimaryVariant().getPrimaryName().equals("sand"))
			weightList.add(new CenterEntry(CactusPyramidCenter.class, 5));
		if(landAspects.aspectTerrain.getConsortType().equals(EnumConsort.TURTLE))
			weightList.add(new CenterEntry(TurtleWellCenter.class, 5));
		if(landAspects.aspectTerrain.getConsortType().equals(EnumConsort.NAKAGATOR))
			weightList.add(new CenterEntry(RadioTowerCenter.class, 5));
		if(landAspects.aspectTerrain.getConsortType().equals(EnumConsort.SALAMANDER))
			weightList.add(new CenterEntry(RuinedTowerMushroomCenter.class, 5));
		
		if(weightList.isEmpty())
			return new ConsortVillageCenter.VillageMarketCenter(list, x, z, rand);
		else
		{
			Class<? extends VillageCenter> c = WeightedRandom.getRandomItem(rand, weightList).center;
			
			try
			{
				return c.getConstructor(List.class, int.class, int.class, Random.class).newInstance(list, x, z, rand);
			} catch(Exception e)
			{
				e.printStackTrace();
				return new ConsortVillageCenter.VillageMarketCenter(list, x, z, rand);
			}
		}
	}
	
	public abstract static class VillageCenter extends ConsortVillageComponents.ConsortVillagePiece
	{
		public List<StructureComponent> pendingHouses = Lists.<StructureComponent>newArrayList();
		public List<StructureComponent> pendingRoads = Lists.<StructureComponent>newArrayList();
		
		public List<ConsortVillageComponents.PieceWeight> pieceWeightList;
		public ConsortVillageComponents.PieceWeight lastPieceWeightUsed;
		
		protected VillageCenter()
		{
		
		}
		
		protected VillageCenter(List<ConsortVillageComponents.PieceWeight> pieceWeightList)
		{
			this.pieceWeightList = pieceWeightList;
		}
	}
	
	public static class VillageMarketCenter extends VillageCenter
	{
		public VillageMarketCenter()
		{
			spawns = new boolean[5];
		}
		
		public VillageMarketCenter(List<ConsortVillageComponents.PieceWeight> pieceWeightList, int x, int z, Random rand)
		{
			super(pieceWeightList);
			spawns = new boolean[5];
			setCoordBaseMode(EnumFacing.Plane.HORIZONTAL.random(rand));
			
			if (getCoordBaseMode().getAxis() == EnumFacing.Axis.Z)
			{
				boundingBox = new StructureBoundingBox(x, 64, z, x + 8 - 1, 78, z + 10 - 1);
			} else
			{
				boundingBox = new StructureBoundingBox(x, 64, z, x + 10 - 1, 78, z + 8 - 1);
			}
		}
		
		@Override
		public void buildComponent(StructureComponent componentIn, List<StructureComponent> listIn, Random rand)
		{
			switch(this.getCoordBaseMode())
			{	//The vanilla code for "rotating coords based on facing" is messy
				case NORTH:
					ConsortVillageComponents.generateAndAddRoadPiece((VillageCenter) componentIn, listIn, rand, boundingBox.minX + 3, boundingBox.minY, boundingBox.maxZ + 1, EnumFacing.SOUTH);
					ConsortVillageComponents.generateAndAddRoadPiece((VillageCenter) componentIn, listIn, rand, boundingBox.maxX - 1, boundingBox.minY, boundingBox.minZ - 1, EnumFacing.NORTH);
					ConsortVillageComponents.generateAndAddRoadPiece((VillageCenter) componentIn, listIn, rand, boundingBox.maxX + 1, boundingBox.minY, boundingBox.minZ, EnumFacing.EAST);
					break;
				case EAST:
					ConsortVillageComponents.generateAndAddRoadPiece((VillageCenter) componentIn, listIn, rand, boundingBox.minX - 1, boundingBox.minY, boundingBox.minZ + 3, EnumFacing.WEST);
					ConsortVillageComponents.generateAndAddRoadPiece((VillageCenter) componentIn, listIn, rand, boundingBox.maxX + 1, boundingBox.minY, boundingBox.maxZ - 1, EnumFacing.EAST);
					ConsortVillageComponents.generateAndAddRoadPiece((VillageCenter) componentIn, listIn, rand, boundingBox.maxX - 1, boundingBox.minY, boundingBox.maxZ + 1, EnumFacing.SOUTH);
					break;
				case SOUTH:
					ConsortVillageComponents.generateAndAddRoadPiece((VillageCenter) componentIn, listIn, rand, boundingBox.minX + 3, boundingBox.minY, boundingBox.minZ - 1, EnumFacing.NORTH);
					ConsortVillageComponents.generateAndAddRoadPiece((VillageCenter) componentIn, listIn, rand, boundingBox.maxX - 1, boundingBox.minY, boundingBox.maxZ + 1, EnumFacing.SOUTH);
					ConsortVillageComponents.generateAndAddRoadPiece((VillageCenter) componentIn, listIn, rand, boundingBox.maxX + 1, boundingBox.minY, boundingBox.maxZ - 1, EnumFacing.EAST);
					break;
				case WEST:
					ConsortVillageComponents.generateAndAddRoadPiece((VillageCenter) componentIn, listIn, rand, boundingBox.maxX + 1, boundingBox.minY, boundingBox.minZ + 3, EnumFacing.EAST);
					ConsortVillageComponents.generateAndAddRoadPiece((VillageCenter) componentIn, listIn, rand, boundingBox.minX - 1, boundingBox.minY, boundingBox.maxZ - 1, EnumFacing.WEST);
					ConsortVillageComponents.generateAndAddRoadPiece((VillageCenter) componentIn, listIn, rand, boundingBox.minX, boundingBox.minY, boundingBox.maxZ + 1, EnumFacing.SOUTH);
					break;
			}
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
				
				this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.minY, 0);
			}
			
			ChunkProviderLands provider = (ChunkProviderLands) worldIn.provider.createChunkGenerator();
			IBlockState floorBlock = provider.blockRegistry.getBlockState("structure_secondary");
			IBlockState plankBlock = provider.blockRegistry.getBlockState("structure_planks");
			IBlockState plankSlab0 = provider.blockRegistry.getBlockState("structure_planks_slab");
			IBlockState plankSlab1 = plankSlab0.withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.TOP);
			IBlockState torch = provider.blockRegistry.getBlockState("torch");
			
			
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
				spawns[0] = spawnConsort(0, 2, 3, boundingBox, worldIn, EnumConsort.MerchantType.FOOD, 1);
			if(!spawns[1])
				spawns[1] = spawnConsort(0, 2, 5, boundingBox, worldIn, EnumConsort.MerchantType.FOOD, 1);
			if(!spawns[2])
				spawns[2] = spawnConsort(7, 2, 3, boundingBox, worldIn, EnumConsort.MerchantType.GENERAL, 1);
			if(!spawns[3])
				spawns[3] = spawnConsort(7, 2, 5, boundingBox, worldIn, EnumConsort.MerchantType.GENERAL, 1);
			if(!spawns[4])
				spawns[4] = spawnConsort(3, 2, 9, boundingBox, worldIn, EnumConsort.getRandomMerchant(randomIn), 1);
			
			return true;
		}
		
	}
	
	public static class RockCenter extends VillageCenter
	{
		public RockCenter()
		{
			super();
		}
		
		public RockCenter(List<ConsortVillageComponents.PieceWeight> pieceWeightList, int x, int z, Random rand)
		{
			super(pieceWeightList);
			this.setCoordBaseMode(EnumFacing.Plane.HORIZONTAL.random(rand));
			
			this.boundingBox = new StructureBoundingBox(x, 64, z, x + 7 - 1, 68, z + 7 - 1);
		}
		
		@Override
		public void buildComponent(StructureComponent componentIn, List<StructureComponent> listIn, Random rand)
		{
			ConsortVillageComponents.generateAndAddRoadPiece((VillageCenter) componentIn, listIn, rand, boundingBox.minX + 3, boundingBox.minY, boundingBox.maxZ + 1, EnumFacing.SOUTH);
			ConsortVillageComponents.generateAndAddRoadPiece((VillageCenter) componentIn, listIn, rand, boundingBox.minX - 1, boundingBox.minY, boundingBox.minZ + 3, EnumFacing.WEST);
			ConsortVillageComponents.generateAndAddRoadPiece((VillageCenter) componentIn, listIn, rand, boundingBox.minX + 3, boundingBox.minY, boundingBox.minZ - 1, EnumFacing.NORTH);
			ConsortVillageComponents.generateAndAddRoadPiece((VillageCenter) componentIn, listIn, rand, boundingBox.maxX + 1, boundingBox.minY, boundingBox.minZ + 3, EnumFacing.EAST);
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
			IBlockState road = provider.blockRegistry.getBlockState("village_path");
			IBlockState rock = provider.blockRegistry.getBlockState("ground");
			
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
		public CactusPyramidCenter()
		{
			super();
		}
		
		public CactusPyramidCenter(List<ConsortVillageComponents.PieceWeight> pieceWeightList, int x, int z, Random rand)
		{
			super(pieceWeightList);
			this.setCoordBaseMode(EnumFacing.Plane.HORIZONTAL.random(rand));
			
			this.boundingBox = new StructureBoundingBox(x, 64, z, x + 16 - 1, 73, z + 16 - 1);
		}
		
		@Override
		public void buildComponent(StructureComponent componentIn, List<StructureComponent> listIn, Random rand)
		{
			ConsortVillageComponents.generateAndAddRoadPiece((VillageCenter) componentIn, listIn, rand, boundingBox.minX + 7, boundingBox.minY, boundingBox.maxZ + 1, EnumFacing.SOUTH);
			ConsortVillageComponents.generateAndAddRoadPiece((VillageCenter) componentIn, listIn, rand, boundingBox.minX - 1, boundingBox.minY, boundingBox.minZ + 7, EnumFacing.WEST);
			ConsortVillageComponents.generateAndAddRoadPiece((VillageCenter) componentIn, listIn, rand, boundingBox.minX + 7, boundingBox.minY, boundingBox.minZ - 1, EnumFacing.NORTH);
			ConsortVillageComponents.generateAndAddRoadPiece((VillageCenter) componentIn, listIn, rand, boundingBox.maxX + 1, boundingBox.minY, boundingBox.minZ + 7, EnumFacing.EAST);
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
				
				this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.minY - 2, 0);
			}
			
			ChunkProviderLands provider = (ChunkProviderLands) worldIn.provider.createChunkGenerator();
			IBlockState road = provider.blockRegistry.getBlockState("village_path");
			IBlockState surface = provider.blockRegistry.getBlockState("surface");
			IBlockState cactus = MinestuckBlocks.woodenCactus.getDefaultState();
			
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
	
	public static class TurtleWellCenter extends VillageCenter
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
			ConsortVillageComponents.generateAndAddRoadPiece((VillageCenter) componentIn, listIn, rand, boundingBox.minX + 3, boundingBox.minY, boundingBox.maxZ + 1, EnumFacing.SOUTH);
			ConsortVillageComponents.generateAndAddRoadPiece((VillageCenter) componentIn, listIn, rand, boundingBox.minX - 1, boundingBox.minY, boundingBox.minZ + 3, EnumFacing.WEST);
			ConsortVillageComponents.generateAndAddRoadPiece((VillageCenter) componentIn, listIn, rand, boundingBox.minX + 3, boundingBox.minY, boundingBox.minZ - 1, EnumFacing.NORTH);
			ConsortVillageComponents.generateAndAddRoadPiece((VillageCenter) componentIn, listIn, rand, boundingBox.maxX + 1, boundingBox.minY, boundingBox.minZ + 3, EnumFacing.EAST);
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
	
	public static class RadioTowerCenter extends VillageCenter
	{
		public RadioTowerCenter()
		{
			super();
		}
		
		public RadioTowerCenter(List<ConsortVillageComponents.PieceWeight> pieceWeightList, int x, int z, Random rand)
		{
			super(pieceWeightList);
			this.setCoordBaseMode(EnumFacing.Plane.HORIZONTAL.random(rand));
			
			this.boundingBox = new StructureBoundingBox(x, 64, z, x + 8 - 1, 90, z + 8 - 1);
		}
		
		@Override
		public void buildComponent(StructureComponent componentIn, List<StructureComponent> listIn, Random rand)
		{
			ConsortVillageComponents.generateAndAddRoadPiece((VillageCenter) componentIn, listIn, rand, boundingBox.minX + 3, boundingBox.minY, boundingBox.maxZ + 1, EnumFacing.SOUTH);
			ConsortVillageComponents.generateAndAddRoadPiece((VillageCenter) componentIn, listIn, rand, boundingBox.minX - 1, boundingBox.minY, boundingBox.minZ + 3, EnumFacing.WEST);
			ConsortVillageComponents.generateAndAddRoadPiece((VillageCenter) componentIn, listIn, rand, boundingBox.minX + 3, boundingBox.minY, boundingBox.minZ - 1, EnumFacing.NORTH);
			ConsortVillageComponents.generateAndAddRoadPiece((VillageCenter) componentIn, listIn, rand, boundingBox.maxX + 1, boundingBox.minY, boundingBox.minZ + 3, EnumFacing.EAST);
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
			IBlockState secondary = provider.blockRegistry.getBlockState("structure_secondary");
			IBlockState secondaryDecor = provider.blockRegistry.getBlockState("structure_secondary_decorative");
			IBlockState fence = provider.blockRegistry.getBlockState("village_fence");
			IBlockState topBlock = Blocks.QUARTZ_BLOCK.getDefaultState();
			IBlockState topSlab0 = Blocks.STONE_SLAB.getDefaultState().withProperty(BlockStoneSlab.VARIANT, BlockStoneSlab.EnumType.QUARTZ);
			IBlockState topSlab1 = Blocks.STONE_SLAB.getDefaultState().withProperty(BlockStoneSlab.VARIANT, BlockStoneSlab.EnumType.QUARTZ).withProperty(BlockStoneSlab.HALF, BlockStoneSlab.EnumBlockHalf.TOP);
			
			this.fillWithAir(worldIn, structureBoundingBoxIn, 1, 1, 1, 6, 6, 6);
			this.fillWithAir(worldIn, structureBoundingBoxIn, 3, 1, 0, 4, 2, 0);
			this.fillWithAir(worldIn, structureBoundingBoxIn, 3, 1, 7, 4, 2, 7);
			this.fillWithAir(worldIn, structureBoundingBoxIn, 0, 1, 2, 0, 2, 5);
			this.fillWithAir(worldIn, structureBoundingBoxIn, 7, 1, 2, 7, 2, 5);
			
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 0, 7, 0, 7, secondary, secondary, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 0, 2, 5, 0, 5, secondaryDecor, secondaryDecor, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 0, 1, 4, 0, 1, secondaryDecor, secondaryDecor, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 0, 6, 4, 0, 6, secondaryDecor, secondaryDecor, false);
			
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 0, 2, 7, 0, secondary, secondary, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 3, 0, 4, 7, 0, secondary, secondary, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 1, 0, 7, 7, 0, secondary, secondary, false);
			
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 7, 2, 7, 7, secondary, secondary, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 3, 7, 4, 7, 7, secondary, secondary, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 1, 7, 7, 7, 7, secondary, secondary, false);
			
			this.setBlockState(worldIn, secondary, 0, 1, 1, structureBoundingBoxIn);
			this.setBlockState(worldIn, secondary, 0, 1, 6, structureBoundingBoxIn);
			this.setBlockState(worldIn, secondary, 0, 2, 1, structureBoundingBoxIn);
			this.setBlockState(worldIn, secondary, 0, 2, 6, structureBoundingBoxIn);
			this.setBlockState(worldIn, secondary, 0, 3, 2, structureBoundingBoxIn);
			this.setBlockState(worldIn, secondary, 0, 3, 5, structureBoundingBoxIn);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 4, 3, 0, 5, 4, secondary, secondary, false);
			this.setBlockState(worldIn, secondary, 0, 6, 2, structureBoundingBoxIn);
			this.setBlockState(worldIn, secondary, 0, 6, 5, structureBoundingBoxIn);
			this.setBlockState(worldIn, secondary, 0, 7, 1, structureBoundingBoxIn);
			this.setBlockState(worldIn, secondary, 0, 7, 6, structureBoundingBoxIn);
			
			this.setBlockState(worldIn, secondary, 7, 1, 1, structureBoundingBoxIn);
			this.setBlockState(worldIn, secondary, 7, 1, 6, structureBoundingBoxIn);
			this.setBlockState(worldIn, secondary, 7, 2, 1, structureBoundingBoxIn);
			this.setBlockState(worldIn, secondary, 7, 2, 6, structureBoundingBoxIn);
			this.setBlockState(worldIn, secondary, 7, 3, 2, structureBoundingBoxIn);
			this.setBlockState(worldIn, secondary, 7, 3, 5, structureBoundingBoxIn);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 4, 3, 7, 5, 4, secondary, secondary, false);
			this.setBlockState(worldIn, secondary, 7, 6, 2, structureBoundingBoxIn);
			this.setBlockState(worldIn, secondary, 7, 6, 5, structureBoundingBoxIn);
			this.setBlockState(worldIn, secondary, 7, 7, 1, structureBoundingBoxIn);
			this.setBlockState(worldIn, secondary, 7, 7, 6, structureBoundingBoxIn);
			
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 7, 1, 6, 7, 6, secondary, secondary, false);
			
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 8, 1, 1, 13, 6, secondary, secondary, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 8, 1, 6, 13, 6, secondary, secondary, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 13, 2, 5, 13, 5, secondary, secondary, false);
			
			this.setBlockState(worldIn, secondary, 2, 9, 1, structureBoundingBoxIn);
			this.setBlockState(worldIn, secondary, 5, 9, 1, structureBoundingBoxIn);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 10, 1, 4, 11, 1, secondary, secondary, false);
			this.setBlockState(worldIn, secondary, 2, 12, 1, structureBoundingBoxIn);
			this.setBlockState(worldIn, secondary, 5, 12, 1, structureBoundingBoxIn);
			
			this.setBlockState(worldIn, secondary, 2, 9, 6, structureBoundingBoxIn);
			this.setBlockState(worldIn, secondary, 5, 9, 6, structureBoundingBoxIn);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 10, 6, 4, 11, 6, secondary, secondary, false);
			this.setBlockState(worldIn, secondary, 2, 12, 6, structureBoundingBoxIn);
			this.setBlockState(worldIn, secondary, 5, 12, 6, structureBoundingBoxIn);
			
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 14, 1, 6, 19, 1, secondary, secondary, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 14, 6, 6, 19, 6, secondary, secondary, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 19, 2, 5, 20, 5, secondary, secondary, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 20, 1, 5, 20, 1, secondary, secondary, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 20, 6, 5, 20, 6, secondary, secondary, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 20, 2, 1, 20, 5, secondary, secondary, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 20, 2, 6, 20, 5, secondary, secondary, false);
			
			this.setBlockState(worldIn, secondary, 1, 15, 2, structureBoundingBoxIn);
			this.setBlockState(worldIn, secondary, 1, 15, 5, structureBoundingBoxIn);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 16, 3, 1, 17, 4, secondary, secondary, false);
			this.setBlockState(worldIn, secondary, 1, 18, 2, structureBoundingBoxIn);
			this.setBlockState(worldIn, secondary, 1, 18, 5, structureBoundingBoxIn);
			
			this.setBlockState(worldIn, secondary, 6, 15, 2, structureBoundingBoxIn);
			this.setBlockState(worldIn, secondary, 6, 15, 5, structureBoundingBoxIn);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 16, 3, 6, 17, 4, secondary, secondary, false);
			this.setBlockState(worldIn, secondary, 6, 18, 2, structureBoundingBoxIn);
			this.setBlockState(worldIn, secondary, 6, 18, 5, structureBoundingBoxIn);
			
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 21, 3, 4, 22, 4, fence, fence, false);
			
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 23, 3, 4, 23, 4, topBlock, topBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 24, 2, 4, 24, 2, topSlab0, topSlab0, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 24, 5, 4, 24, 5, topSlab0, topSlab0, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 24, 3, 2, 24, 4, topSlab0, topSlab0, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 24, 3, 5, 24, 4, topSlab0, topSlab0, false);
			
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 24, 1, 4, 24, 1, topSlab1, topSlab1, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 24, 6, 4, 24, 6, topSlab1, topSlab1, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 24, 3, 1, 24, 4, topSlab1, topSlab1, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 24, 3, 6, 24, 4, topSlab1, topSlab1, false);
			this.setBlockState(worldIn, topSlab1, 2, 24, 2, structureBoundingBoxIn);
			this.setBlockState(worldIn, topSlab1, 2, 24, 5, structureBoundingBoxIn);
			this.setBlockState(worldIn, topSlab1, 5, 24, 2, structureBoundingBoxIn);
			this.setBlockState(worldIn, topSlab1, 5, 24, 5, structureBoundingBoxIn);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 25, 1, 4, 25, 1, topSlab0, topSlab0, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 25, 6, 4, 25, 6, topSlab0, topSlab0, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 25, 3, 1, 25, 4, topSlab0, topSlab0, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 25, 3, 6, 25, 4, topSlab0, topSlab0, false);
			this.setBlockState(worldIn, topSlab0, 2, 25, 2, structureBoundingBoxIn);
			this.setBlockState(worldIn, topSlab0, 2, 25, 5, structureBoundingBoxIn);
			this.setBlockState(worldIn, topSlab0, 5, 25, 2, structureBoundingBoxIn);
			this.setBlockState(worldIn, topSlab0, 5, 25, 5, structureBoundingBoxIn);
			
			return true;
		}
	}
	
	public static class RuinedTowerMushroomCenter extends VillageCenter
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
			ConsortVillageComponents.generateAndAddRoadPiece((VillageCenter) componentIn, listIn, rand, boundingBox.minX + 4, boundingBox.minY, boundingBox.maxZ + 1, EnumFacing.SOUTH);
			ConsortVillageComponents.generateAndAddRoadPiece((VillageCenter) componentIn, listIn, rand, boundingBox.minX - 1, boundingBox.minY, boundingBox.minZ + 4, EnumFacing.WEST);
			ConsortVillageComponents.generateAndAddRoadPiece((VillageCenter) componentIn, listIn, rand, boundingBox.minX + 4, boundingBox.minY, boundingBox.minZ - 1, EnumFacing.NORTH);
			ConsortVillageComponents.generateAndAddRoadPiece((VillageCenter) componentIn, listIn, rand, boundingBox.maxX + 1, boundingBox.minY, boundingBox.minZ + 4, EnumFacing.EAST);
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
}