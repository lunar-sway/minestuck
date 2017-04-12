package com.mraof.minestuck.world.lands.structure;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

import com.mraof.minestuck.entity.consort.EnumConsort;
import com.mraof.minestuck.world.lands.LandAspectRegistry;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;

public class ConsortVillageComponents
{
	public static void registerComponents()
	{
		MapGenStructureIO.registerStructure(MapGenConsortVillage.Start.class, "MinestuckConsortVillage");
		MapGenStructureIO.registerStructureComponent(ConsortVillageComponents.VillageMarketCenter.class, "MinestuckCVCM");
		MapGenStructureIO.registerStructureComponent(ConsortVillageComponents.PipeHouse1.class, "MinestuckCVPH1");
		MapGenStructureIO.registerStructureComponent(ConsortVillageComponents.HighPipeHouse1.class, "MinestuckCVHPH1");
		MapGenStructureIO.registerStructureComponent(ConsortVillageComponents.VillagePath.class, "MinestuckCVPth");
	}
	
	public static List<PieceWeight> getStructureVillageWeightedPieceList(Random random, EnumConsort consortType, LandAspectRegistry.AspectCombination landAspects)
	{
		List<PieceWeight> list = Lists.newArrayList();
		switch (consortType)
		{
			case SALAMANDER:
			default:
				list.add(new PieceWeight(PipeHouse1.class, 4, MathHelper.getInt(random, 4, 8)));
				list.add(new PieceWeight(HighPipeHouse1.class, 4, MathHelper.getInt(random, 2, 4)));
				break;
			case TURTLE:
				break;
			case IGUANA:
				break;
			case NAKAGATOR:
				break;
		}
		Iterator<PieceWeight> iterator = list.iterator();
		
		while (iterator.hasNext())
		{
			if (iterator.next().villagePiecesLimit == 0)
			{
				iterator.remove();
			}
		}
		
		return list;
	}
	
	private static StructureComponent generateAndAddComponent(VillageCenter start, List<StructureComponent> structureComponents, Random rand, int structureMinX, int structureMinY, int structureMinZ, EnumFacing facing)
	{
		if (Math.abs(structureMinX - start.getBoundingBox().minX) <= 112 && Math.abs(structureMinZ - start.getBoundingBox().minZ) <= 112)
		{
			StructureComponent villagePiece = generateComponent(start, structureComponents, rand, structureMinX, structureMinY, structureMinZ, facing);
			
			if (villagePiece != null)
			{
				structureComponents.add(villagePiece);
				start.pendingHouses.add(villagePiece);
				return villagePiece;
			}
			else return null;
		}
		else return null;
	}
	
	private static ConsortVillagePiece generateComponent(VillageCenter start, List<StructureComponent> structureComponents, Random rand, int structureMinX, int structureMinY, int structureMinZ, EnumFacing facing)
	{
		int i = updatePieceWeight(start.pieceWeightList);
		
		if (i <= 0)
		{
			return null;
		}
		else
		{
			int j = 0;
			
			while (j < 5)
			{
				++j;
				int k = rand.nextInt(i);
				
				for (PieceWeight pieceWeight : start.pieceWeightList)
				{
					k -= pieceWeight.villagePieceWeight;
					
					if (k < 0)
					{
						if (!pieceWeight.canSpawnMoreVillagePieces() || pieceWeight == start.lastPieceWeightUsed && start.pieceWeightList.size() > 1)
							break;
						
						ConsortVillagePiece villagePiece = findAndCreateComponentFactory(start, pieceWeight, structureComponents, rand, structureMinX, structureMinY, structureMinZ, facing);
						
						if (villagePiece != null)
						{
							pieceWeight.villagePiecesSpawned++;
							start.lastPieceWeightUsed = pieceWeight;
							
							if (!pieceWeight.canSpawnMoreVillagePieces())
								start.pieceWeightList.remove(pieceWeight);
							
							return villagePiece;
						}
					}
				}
			}
			
			return null;
		}
	}
	
	private static int updatePieceWeight(List<PieceWeight> list)
	{
		boolean flag = false;
		int totalWeight = 0;
		
		for (PieceWeight pieceWeight : list)
		{
			if (pieceWeight.villagePiecesLimit > 0 && pieceWeight.villagePiecesSpawned < pieceWeight.villagePiecesLimit)
				flag = true;
			
			totalWeight += pieceWeight.villagePieceWeight;
		}
		
		return flag ? totalWeight : -1;
	}
	
	private static ConsortVillagePiece findAndCreateComponentFactory(VillageCenter start, PieceWeight weight, List<StructureComponent> structureComponents, Random rand, int structureMinX, int structureMinY, int structureMinZ, EnumFacing facing)
	{
		Class <? extends ConsortVillagePiece> pieceClass = weight.villagePieceClass;
		ConsortVillagePiece villagePiece = null;
		
		if (pieceClass == PipeHouse1.class)
		{
			villagePiece = PipeHouse1.createPiece(start, structureComponents, rand, structureMinX, structureMinY, structureMinZ, facing);
		}
		else if (pieceClass == HighPipeHouse1.class)
		{
			villagePiece = HighPipeHouse1.createPiece(start, structureComponents, rand, structureMinX, structureMinY, structureMinZ, facing);
		}
		
		return villagePiece;
	}
	
	public static class PieceWeight
	{
		public Class <? extends ConsortVillagePiece> villagePieceClass;
		public final int villagePieceWeight;
		public int villagePiecesSpawned;
		public int villagePiecesLimit;
		
		public PieceWeight(Class <? extends ConsortVillagePiece> pieceClass, int weight, int limit)
		{
			this.villagePieceClass = pieceClass;
			this.villagePieceWeight = weight;
			this.villagePiecesLimit = limit;
		}
		
		public boolean canSpawnMoreVillagePieces()
		{
			return this.villagePiecesLimit == 0 || this.villagePiecesSpawned < this.villagePiecesLimit;
		}
	}
	
	public static abstract class ConsortVillagePiece extends StructureComponent
	{
		protected int averageGroundLvl = -1;
		
		@Override
		protected void writeStructureToNBT(NBTTagCompound tagCompound)
		{
			tagCompound.setInteger("HPos", this.averageGroundLvl);
		}
		
		@Override
		protected void readStructureFromNBT(NBTTagCompound tagCompound)
		{
			this.averageGroundLvl = tagCompound.getInteger("HPos");
		}
		
		protected int getAverageGroundLevel(World worldIn, StructureBoundingBox structurebb)
		{
			int i = 0;
			int j = 0;
			BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
			
			for (int k = this.boundingBox.minZ; k <= this.boundingBox.maxZ; ++k)
			{
				for (int l = this.boundingBox.minX; l <= this.boundingBox.maxX; ++l)
				{
					blockpos$mutableblockpos.setPos(l, 64, k);
					
					if (structurebb.isVecInside(blockpos$mutableblockpos))
					{
						i += Math.max(worldIn.getTopSolidOrLiquidBlock(blockpos$mutableblockpos).getY(),
								worldIn.provider.getAverageGroundLevel() - 1);
						++j;
					}
				}
			}
			
			if (j == 0)
			{
				return -1;
			} else
			{
				return i / j;
			}
		}
		
		protected StructureComponent getNextComponentNN(VillageCenter start, List<StructureComponent> structureComponents, Random rand, int offsetY, int offsetXZ)
		{
			EnumFacing enumfacing = this.getCoordBaseMode();
			
			if (enumfacing != null)
			{
				switch (enumfacing)
				{
					case NORTH:
					default:
						return ConsortVillageComponents.generateAndAddComponent(start, structureComponents, rand, this.boundingBox.minX - 1, this.boundingBox.minY + offsetY, this.boundingBox.minZ + offsetXZ, EnumFacing.WEST);
					case SOUTH:
						return ConsortVillageComponents.generateAndAddComponent(start, structureComponents, rand, this.boundingBox.minX - 1, this.boundingBox.minY + offsetY, this.boundingBox.minZ + offsetXZ, EnumFacing.WEST);
					case WEST:
						return ConsortVillageComponents.generateAndAddComponent(start, structureComponents, rand, this.boundingBox.minX + offsetXZ, this.boundingBox.minY + offsetY, this.boundingBox.minZ - 1, EnumFacing.NORTH);
					case EAST:
						return ConsortVillageComponents.generateAndAddComponent(start, structureComponents, rand, this.boundingBox.minX + offsetXZ, this.boundingBox.minY + offsetY, this.boundingBox.minZ - 1, EnumFacing.NORTH);
				}
			}
			else
			{
				return null;
			}
		}
		
		protected StructureComponent getNextComponentPP(VillageCenter start, List<StructureComponent> structureComponents, Random rand, int offsetY, int offsetXZ)
		{
			EnumFacing enumfacing = this.getCoordBaseMode();
			
			if (enumfacing != null)
			{
				switch (enumfacing)
				{
					case NORTH:
					default:
						return ConsortVillageComponents.generateAndAddComponent(start, structureComponents, rand, this.boundingBox.maxX + 1, this.boundingBox.minY + offsetY, this.boundingBox.minZ + offsetXZ, EnumFacing.EAST);
					case SOUTH:
						return ConsortVillageComponents.generateAndAddComponent(start, structureComponents, rand, this.boundingBox.maxX + 1, this.boundingBox.minY + offsetY, this.boundingBox.minZ + offsetXZ, EnumFacing.EAST);
					case WEST:
						return ConsortVillageComponents.generateAndAddComponent(start, structureComponents, rand, this.boundingBox.minX + offsetXZ, this.boundingBox.minY + offsetY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH);
					case EAST:
						return ConsortVillageComponents.generateAndAddComponent(start, structureComponents, rand, this.boundingBox.minX + offsetXZ, this.boundingBox.minY + offsetY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH);
				}
			}
			else
			{
				return null;
			}
		}
	}
	
	public abstract static class VillageCenter extends ConsortVillagePiece
	{
		public List<StructureComponent> pendingHouses = Lists.<StructureComponent>newArrayList();
		public List<StructureComponent> pendingRoads = Lists.<StructureComponent>newArrayList();
		
		public List<PieceWeight> pieceWeightList;
		public PieceWeight lastPieceWeightUsed;
		
		protected VillageCenter(List<PieceWeight> pieceWeightList)
		{
			this.pieceWeightList = pieceWeightList;
		}
	}
	
	public static class VillageMarketCenter extends VillageCenter
	{
		
		public VillageMarketCenter(List<PieceWeight> pieceWeightList, int x, int z, Random rand)
		{
			super(pieceWeightList);
			this.setCoordBaseMode(EnumFacing.Plane.HORIZONTAL.random(rand));
			
			if (this.getCoordBaseMode().getAxis() == EnumFacing.Axis.Z)
			{
				this.boundingBox = new StructureBoundingBox(x, 64, z, x + 8 - 1, 78, z + 10 - 1);
			} else
			{
				this.boundingBox = new StructureBoundingBox(x, 64, z, x + 10 - 1, 78, z + 8 - 1);
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
					ConsortVillageComponents.generateAndAddRoadPiece((VillageCenter) componentIn, listIn, rand, boundingBox.maxX, boundingBox.minY, boundingBox.maxZ + 1, EnumFacing.SOUTH);
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
			
			
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 0, 0, 5, 0, 0, floorBlock, floorBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 1, 6, 0, 1, floorBlock, floorBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 2, 1, 0, 7, floorBlock, floorBlock, false);
			this.setBlockState(worldIn, floorBlock, 1, 0, 8, structureBoundingBoxIn);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 0, 2, 7, 0, 9, floorBlock, floorBlock, false);
			
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
			
			this.setBlockState(worldIn, torch, 1, 2, 4, structureBoundingBoxIn);
			this.setBlockState(worldIn, torch, 6, 2, 4, structureBoundingBoxIn);
			
			return true;
		}
		
	}
	
	public static class PipeHouse1 extends ConsortVillagePiece
	{
		public PipeHouse1()
		{
		
		}
		
		public PipeHouse1(VillageCenter start, Random rand, StructureBoundingBox boundingBox, EnumFacing facing)
		{
			this.setCoordBaseMode(facing);
			this.boundingBox = boundingBox;
		}
		
		public static PipeHouse1 createPiece(VillageCenter start, List<StructureComponent> componentList, Random rand, int x, int y, int z, EnumFacing facing)
		{
			StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, 6, 5, 6, facing);
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
				
				this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.minY, 0);
			}
			
			ChunkProviderLands provider = (ChunkProviderLands) worldIn.provider.createChunkGenerator();
			IBlockState wallBlock = provider.blockRegistry.getBlockState("structure_primary");
			IBlockState floorBlock = provider.blockRegistry.getBlockState("upper");
			IBlockState doorBlock = provider.blockRegistry.getBlockState("village_door");
			
			this.fillWithAir(worldIn, structureBoundingBoxIn, 1, 0, 1, 4, 4, 4);
			this.fillWithAir(worldIn, structureBoundingBoxIn, 1, 0, -1, 4, 4, -1);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1,-1,0,4,-1, 5, floorBlock, floorBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0,-1,1,0,-1, 4, floorBlock, floorBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5,-1,1,5,-1, 4, floorBlock, floorBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1,0,0,2,1, 0, wallBlock, wallBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 4,0,0,4,1, 0, wallBlock, wallBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1,2,0,4,4, 0, wallBlock, wallBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0,0,1,0,4, 4, wallBlock, wallBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1,0,5,4,4, 5, wallBlock, wallBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5,0,1,5,0, 4, wallBlock, wallBlock, false);
			this.setBlockState(worldIn, wallBlock, 5, 1, 1, structureBoundingBoxIn);
			this.setBlockState(worldIn, wallBlock, 5, 1, 4, structureBoundingBoxIn);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5,2,1,5,4, 4, wallBlock, wallBlock, false);
			func_189915_a(worldIn, structureBoundingBoxIn, randomIn, 3, 0, 0, EnumFacing.NORTH, (BlockDoor) doorBlock.getBlock());
			
			return true;
		}
	}
	
	public static class HighPipeHouse1 extends ConsortVillagePiece
	{
		public HighPipeHouse1()
		{
		
		}
		
		public HighPipeHouse1(VillageCenter start, Random rand, StructureBoundingBox boundingBox, EnumFacing facing)
		{
			this.setCoordBaseMode(facing);
			this.boundingBox = boundingBox;
		}
		
		public static HighPipeHouse1 createPiece(VillageCenter start, List<StructureComponent> componentList, Random rand, int x, int y, int z, EnumFacing facing)
		{
			StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, 7, 13, 7, facing);
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
				
				this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.minY, 0);
			}
			
			ChunkProviderLands provider = (ChunkProviderLands) worldIn.provider.createChunkGenerator();
			IBlockState wallBlock = provider.blockRegistry.getBlockState("structure_primary");
			IBlockState floorBlock = provider.blockRegistry.getBlockState("upper");
			IBlockState doorBlock = provider.blockRegistry.getBlockState("village_door");
			
			this.fillWithAir(worldIn, structureBoundingBoxIn, 1, 0, 1, 5, 12, 5);
			this.fillWithAir(worldIn, structureBoundingBoxIn, 1, 0, -1, 5, 4, -1);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1,-1,0,5,-1, 6, floorBlock, floorBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0,-1,1,0,-1, 5, floorBlock, floorBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6,-1,1,6,-1, 5, floorBlock, floorBlock, false);
			
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1,0,0,2,12, 0, wallBlock, wallBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 4,0,0,5,12, 0, wallBlock, wallBlock, false);
			this.setBlockState(worldIn, wallBlock, 3, 2, 0, structureBoundingBoxIn);
			this.setBlockState(worldIn, wallBlock, 3, 4, 0, structureBoundingBoxIn);
			this.setBlockState(worldIn, wallBlock, 3, 6, 0, structureBoundingBoxIn);
			this.setBlockState(worldIn, wallBlock, 3, 8, 0, structureBoundingBoxIn);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3,10,0,3,12, 0, wallBlock, wallBlock, false);
			
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1,0,6,2,12, 6, wallBlock, wallBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 4,0,6,5,12, 6, wallBlock, wallBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3,0,6,3,2, 6, wallBlock, wallBlock, false);
			this.setBlockState(worldIn, wallBlock, 3, 4, 6, structureBoundingBoxIn);
			this.setBlockState(worldIn, wallBlock, 3, 6, 6, structureBoundingBoxIn);
			this.setBlockState(worldIn, wallBlock, 3, 8, 6, structureBoundingBoxIn);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3,10,6,3,12, 6, wallBlock, wallBlock, false);
			
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0,2,1,0,12, 2, wallBlock, wallBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0,2,4,0,12, 5, wallBlock, wallBlock, false);
			this.setBlockState(worldIn, wallBlock, 0, 2, 3, structureBoundingBoxIn);
			this.setBlockState(worldIn, wallBlock, 0, 4, 3, structureBoundingBoxIn);
			this.setBlockState(worldIn, wallBlock, 0, 6, 3, structureBoundingBoxIn);
			this.setBlockState(worldIn, wallBlock, 0, 8, 3, structureBoundingBoxIn);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0,10,3,0,12, 3, wallBlock, wallBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0,0,1,0,0, 5, wallBlock, wallBlock, false);
			this.setBlockState(worldIn, wallBlock, 0, 1, 1, structureBoundingBoxIn);
			this.setBlockState(worldIn, wallBlock, 0, 1, 5, structureBoundingBoxIn);
			
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6,2,1,6,12, 2, wallBlock, wallBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6,2,4,6,12, 5, wallBlock, wallBlock, false);
			this.setBlockState(worldIn, wallBlock, 6, 2, 3, structureBoundingBoxIn);
			this.setBlockState(worldIn, wallBlock, 6, 4, 3, structureBoundingBoxIn);
			this.setBlockState(worldIn, wallBlock, 6, 6, 3, structureBoundingBoxIn);
			this.setBlockState(worldIn, wallBlock, 6, 8, 3, structureBoundingBoxIn);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6,10,3,6,12, 3, wallBlock, wallBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6,0,1,6,0, 5, wallBlock, wallBlock, false);
			this.setBlockState(worldIn, wallBlock, 6, 1, 1, structureBoundingBoxIn);
			this.setBlockState(worldIn, wallBlock, 6, 1, 5, structureBoundingBoxIn);
			
			
			func_189915_a(worldIn, structureBoundingBoxIn, randomIn, 3, 0, 0, EnumFacing.NORTH, (BlockDoor) doorBlock.getBlock());
			
			return true;
		}
	}
	
	public static class VillagePath extends ConsortVillagePiece
	{
		private int length;
		
		public VillagePath()
		{
		}
		
		public VillagePath(VillageCenter start, Random rand, StructureBoundingBox boundingBox, EnumFacing facing)
		{
			this.setCoordBaseMode(facing);
			this.boundingBox = boundingBox;
			this.length = Math.max(boundingBox.getXSize(), boundingBox.getZSize());
		}
		
		@Override
		protected void writeStructureToNBT(NBTTagCompound tagCompound)
		{
			super.writeStructureToNBT(tagCompound);
			tagCompound.setInteger("Length", this.length);
		}
		
		@Override
		protected void readStructureFromNBT(NBTTagCompound tagCompound)
		{
			super.readStructureFromNBT(tagCompound);
			this.length = tagCompound.getInteger("Length");
		}
		
		@Override
		public void buildComponent(StructureComponent componentIn, List<StructureComponent> listIn, Random rand)
		{
			boolean flag = false;
			
			for (int i = rand.nextInt(5); i < this.length - 8; i += 2 + rand.nextInt(5))
			{
				StructureComponent newPiece = this.getNextComponentNN((VillageCenter)componentIn, listIn, rand, 0, i);
				
				if (newPiece != null)
				{
					i += Math.max(newPiece.getBoundingBox().getXSize(), newPiece.getBoundingBox().getZSize());
					flag = true;
				}
			}
			
			for (int j = rand.nextInt(5); j < this.length - 8; j += 2 + rand.nextInt(5))
			{
				StructureComponent newPiece = this.getNextComponentPP((VillageCenter) componentIn, listIn, rand, 0, j);
				
				if (newPiece != null)
				{
					j += Math.max(newPiece.getBoundingBox().getXSize(), newPiece.getBoundingBox().getZSize());
					flag = true;
				}
			}
			
			EnumFacing enumfacing = this.getCoordBaseMode();
			
			if (flag && rand.nextInt(3) > 0 && enumfacing != null)
			{
				switch (enumfacing)
				{
					case NORTH:
					default:
						ConsortVillageComponents.generateAndAddRoadPiece((VillageCenter) componentIn, listIn, rand, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ, EnumFacing.WEST);
						break;
					case SOUTH:
						ConsortVillageComponents.generateAndAddRoadPiece((VillageCenter) componentIn, listIn, rand, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.maxZ - 2, EnumFacing.WEST);
						break;
					case WEST:
						ConsortVillageComponents.generateAndAddRoadPiece((VillageCenter) componentIn, listIn, rand, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH);
						break;
					case EAST:
						ConsortVillageComponents.generateAndAddRoadPiece((VillageCenter) componentIn, listIn, rand, this.boundingBox.maxX - 2, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH);
				}
			}
			
			if (flag && rand.nextInt(3) > 0 && enumfacing != null)
			{
				switch (enumfacing)
				{
					case NORTH:
					default:
						ConsortVillageComponents.generateAndAddRoadPiece((VillageCenter) componentIn, listIn, rand, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ, EnumFacing.EAST);
						break;
					case SOUTH:
						ConsortVillageComponents.generateAndAddRoadPiece((VillageCenter) componentIn, listIn, rand, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.maxZ - 2, EnumFacing.EAST);
						break;
					case WEST:
						ConsortVillageComponents.generateAndAddRoadPiece((VillageCenter) componentIn, listIn, rand, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH);
						break;
					case EAST:
						ConsortVillageComponents.generateAndAddRoadPiece((VillageCenter) componentIn, listIn, rand, this.boundingBox.maxX - 2, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH);
				}
			}
		}
		
		public static StructureBoundingBox findPieceBox(VillageCenter start, List<StructureComponent> components, Random rand, int x, int y, int z, EnumFacing facing)
		{
			for(int i = 7 * MathHelper.getInt(rand, 3, 5); i >= 7; i -= 7)
			{
				StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, 2, 3, i, facing);
				
				if(StructureComponent.findIntersecting(components, structureboundingbox) == null)
					return structureboundingbox;
			}
			
			return null;
		}
		
		@Override
		public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
		{
			ChunkProviderLands provider = (ChunkProviderLands) worldIn.provider.createChunkGenerator();
			IBlockState pathBlock = provider.blockRegistry.getBlockState("village_path");
			
			for (int i = this.boundingBox.minX; i <= this.boundingBox.maxX; ++i)
			{
				for (int j = this.boundingBox.minZ; j <= this.boundingBox.maxZ; ++j)
				{
					BlockPos blockpos = new BlockPos(i, 64, j);
					
					if (structureBoundingBoxIn.isVecInside(blockpos))
					{
						blockpos = worldIn.getTopSolidOrLiquidBlock(blockpos).down();
						
						if (blockpos.getY() < worldIn.getSeaLevel())
						{
							blockpos = new BlockPos(blockpos.getX(), worldIn.getSeaLevel() - 1, blockpos.getZ());
						}
						
						while (blockpos.getY() >= worldIn.getSeaLevel() - 1)
						{
							IBlockState state = worldIn.getBlockState(blockpos);
							
							if (state.getMaterial().isLiquid() || state.isNormalCube())
							{
								worldIn.setBlockState(blockpos, pathBlock, 2);
								break;
							}
							
							blockpos = blockpos.down();
						}
					}
				}
			}
			
			return true;
		}
	}
	
	
	private static StructureComponent generateAndAddRoadPiece(VillageCenter start, List<StructureComponent> componentList, Random rand, int x, int y, int z, EnumFacing facing)
	{
		if (Math.abs(x - start.getBoundingBox().minX) <= 112 && Math.abs(z - start.getBoundingBox().minZ) <= 112)
		{
			StructureBoundingBox structureboundingbox = VillagePath.findPieceBox(start, componentList, rand, x, y, z, facing);
			
			if (structureboundingbox != null && structureboundingbox.minY > 10)
			{
				StructureComponent structurecomponent = new VillagePath(start, rand, structureboundingbox, facing);
				componentList.add(structurecomponent);
				start.pendingRoads.add(structurecomponent);
				return structurecomponent;
			}
			else
			{
				return null;
			}
		}
		else
		{
			return null;
		}
	}
}