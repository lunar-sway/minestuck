package com.mraof.minestuck.world.lands.structure.village;

import com.google.common.collect.Lists;
import com.mraof.minestuck.entity.consort.EntityConsort;
import com.mraof.minestuck.entity.consort.EnumConsort;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.world.MinestuckDimensionHandler;
import com.mraof.minestuck.world.lands.LandAspectRegistry;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.template.TemplateManager;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class ConsortVillageComponents
{
	public static void registerComponents()
	{
		MapGenStructureIO.registerStructure(MapGenConsortVillage.Start.class, "MinestuckConsortVillage");
		ConsortVillageCenter.register();
		
		MapGenStructureIO.registerStructureComponent(PipeHouse1.class, "MinestuckCVPiH1");
		MapGenStructureIO.registerStructureComponent(HighPipeHouse1.class, "MinestuckCVHPiH1");
		MapGenStructureIO.registerStructureComponent(SmallTowerStore.class, "MinestuckCVSToSt");
		
		MapGenStructureIO.registerStructureComponent(LoweredShellHouse1.class, "MinestuckCVLShH1");
		MapGenStructureIO.registerStructureComponent(TurtleMarketBuilding1.class, "MinestuckCVTuMB1");
		MapGenStructureIO.registerStructureComponent(TurtleTemple1.class, "MinestuckCVTuTe1");
		
		MapGenStructureIO.registerStructureComponent(HighNakHousing1.class, "MinestuckCVHNaH1");
		MapGenStructureIO.registerStructureComponent(HighNakMarket1.class, "MinestuckCVHNaM1");
		
		MapGenStructureIO.registerStructureComponent(SmallTent1.class, "MinestuckCVSmTe1");
		MapGenStructureIO.registerStructureComponent(LargeTent1.class, "MinestuckCVLaTe1");
		MapGenStructureIO.registerStructureComponent(SmallTentStore.class, "MinestuckCVSmTeSt");
		
		MapGenStructureIO.registerStructureComponent(VillagePath.class, "MinestuckCVPth");
	}
	
	public static List<PieceWeight> getStructureVillageWeightedPieceList(Random random, EnumConsort consortType, LandAspectRegistry.AspectCombination landAspects)
	{
		List<PieceWeight> list = Lists.newArrayList();
		switch (consortType)
		{
			case SALAMANDER:
			default:
				list.add(new PieceWeight(PipeHouse1.class, 3, MathHelper.getInt(random, 5, 8)));
				list.add(new PieceWeight(HighPipeHouse1.class, 6, MathHelper.getInt(random, 2, 4)));
				list.add(new PieceWeight(SmallTowerStore.class, 10, MathHelper.getInt(random, 1, 3)));
				break;
			case TURTLE:
				list.add(new PieceWeight(LoweredShellHouse1.class, 3, MathHelper.getInt(random, 5, 8)));
				list.add(new PieceWeight(TurtleMarketBuilding1.class, 10, MathHelper.getInt(random, 0, 2)));
				list.add(new PieceWeight(TurtleTemple1.class, 10, MathHelper.getInt(random, 1, 1)));
				break;
			case IGUANA:
				list.add(new PieceWeight(SmallTent1.class, 3, MathHelper.getInt(random, 5, 8)));
				list.add(new PieceWeight(LargeTent1.class, 10, MathHelper.getInt(random, 1, 2)));
				list.add(new PieceWeight(SmallTentStore.class, 8, MathHelper.getInt(random, 2, 3)));
				break;
			case NAKAGATOR:
				list.add(new PieceWeight(HighNakHousing1.class, 6, MathHelper.getInt(random, 3, 5)));
				list.add(new PieceWeight(HighNakMarket1.class, 10, MathHelper.getInt(random, 1, 2)));
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
	
	private static StructureComponent generateAndAddComponent(ConsortVillageCenter.VillageCenter start, List<StructureComponent> structureComponents, Random rand, int structureMinX, int structureMinY, int structureMinZ, EnumFacing facing)
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
	
	private static ConsortVillagePiece generateComponent(ConsortVillageCenter.VillageCenter start, List<StructureComponent> structureComponents, Random rand, int structureMinX, int structureMinY, int structureMinZ, EnumFacing facing)
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
	
	private static ConsortVillagePiece findAndCreateComponentFactory(ConsortVillageCenter.VillageCenter start, PieceWeight weight, List<StructureComponent> structureComponents, Random rand, int structureMinX, int structureMinY, int structureMinZ, EnumFacing facing)
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
		else if(pieceClass == SmallTowerStore.class)
		{
			villagePiece = SmallTowerStore.createPiece(start, structureComponents, rand, structureMinX, structureMinY, structureMinZ, facing);
		}
		else if(pieceClass == LoweredShellHouse1.class)
		{
			villagePiece = LoweredShellHouse1.createPiece(start, structureComponents, rand, structureMinX, structureMinY, structureMinZ, facing);
		}
		else if(pieceClass == TurtleMarketBuilding1.class)
		{
			villagePiece = TurtleMarketBuilding1.createPiece(start, structureComponents, rand, structureMinX, structureMinY, structureMinZ, facing);
		}
		else if(pieceClass == TurtleTemple1.class)
		{
			villagePiece = TurtleTemple1.createPiece(start, structureComponents, rand, structureMinX, structureMinY, structureMinZ, facing);
		}
		else if(pieceClass == HighNakHousing1.class)
		{
			villagePiece = HighNakHousing1.createPiece(start, structureComponents, rand, structureMinX, structureMinY, structureMinZ, facing);
		}
		else if(pieceClass == HighNakMarket1.class)
		{
			villagePiece = HighNakMarket1.createPiece(start, structureComponents, rand, structureMinX, structureMinY, structureMinZ, facing);
		}
		else if(pieceClass == SmallTent1.class)
		{
			villagePiece = SmallTent1.createPiece(start, structureComponents, rand, structureMinX, structureMinY, structureMinZ, facing);
		}
		else if(pieceClass == LargeTent1.class)
		{
			villagePiece = LargeTent1.createPiece(start, structureComponents, rand, structureMinX, structureMinY, structureMinZ, facing);
		}
		else if(pieceClass == SmallTentStore.class)
		{
			villagePiece = SmallTentStore.createPiece(start, structureComponents, rand, structureMinX, structureMinY, structureMinZ, facing);
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
		protected void readStructureFromNBT(NBTTagCompound tagCompound, TemplateManager p_143011_2_)
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
		
		protected StructureComponent getNextComponentNN(ConsortVillageCenter.VillageCenter start, List<StructureComponent> structureComponents, Random rand, int offsetY, int offsetXZ)
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
		
		protected StructureComponent getNextComponentPP(ConsortVillageCenter.VillageCenter start, List<StructureComponent> structureComponents, Random rand, int offsetY, int offsetXZ)
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
		
		protected void clearFront(World world, StructureBoundingBox structureBB, int minX, int maxX, int y, int z)
		{
			for(int x = minX; x <= maxX; x++)
				if(structureBB.isVecInside(new Vec3i(this.getXWithOffset(x, z), this.getYWithOffset(y), this.getZWithOffset(x, z))))
				{
					this.fillWithAir(world, structureBB, x, y, z, x, y + 4, z);
					BlockPos pos = new BlockPos(this.getXWithOffset(x, z - 1), this.getYWithOffset(y), this.getZWithOffset(x, z - 1));
					int i = 0;
					for(int yOffset = 0; yOffset <= 4; yOffset++)
						if(world.getBlockState(pos.up(yOffset)).isBlockNormalCube())
							i++;
						else break;
					if(i >= 2)
						this.fillWithBlocks(world, structureBB, x, y, z, x, y + i - 1, z, Blocks.LADDER.getDefaultState(), Blocks.LADDER.getDefaultState(), false);
				}
		}
		
		protected void placeRoadtile(int x, int z, StructureBoundingBox boundingBox, World worldIn, IBlockState pathBlock)
		{
			BlockPos blockpos = new BlockPos(x, 64, z);
			
			if (boundingBox.isVecInside(blockpos))
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
		
		protected void blockPillar(int x, int y, int z, StructureBoundingBox boundingBox, World world, IBlockState block)
		{
			BlockPos pos = new BlockPos(this.getXWithOffset(x, z), this.getYWithOffset(y), this.getZWithOffset(x, z));
			
			if(!boundingBox.isVecInside(pos))
				return;
			
			while(pos.getY() >= world.getSeaLevel())
			{
				world.setBlockState(pos, block, 2);
				
				pos = pos.down();
				
				if(world.getBlockState(pos).isOpaqueCube())
					break;
			}
		}
		
		protected void spawnConsort(int x, int y, int z, StructureBoundingBox boundingBox, World world)
		{
			spawnConsort(x, y, z, boundingBox, world, EnumConsort.MerchantType.NONE);
		}
		
		protected void spawnConsort(int x, int y, int z, StructureBoundingBox boundingBox, World world, EnumConsort.MerchantType type)
		{
			BlockPos pos = new BlockPos(this.getXWithOffset(x, z), this.getYWithOffset(y), this.getZWithOffset(x, z));
			
			if(boundingBox.isVecInside(pos))
			{
				LandAspectRegistry.AspectCombination aspects = MinestuckDimensionHandler.getAspects(world.provider.getDimension());
				if(aspects == null)
				{
					Debug.warn("Tried to spawn a consort in a building that is being generated outside of a land dimension.");
					return;
				}
				
				Class<? extends EntityConsort> c = aspects.aspectTerrain.getConsortType().getConsortClass();
				
				try
				{
					EntityConsort consort = c.getConstructor(World.class).newInstance(world);
					consort.setPosition(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
					
					consort.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(consort)), null);
					if(type != EnumConsort.MerchantType.NONE)
						consort.merchantType = type;
					//TODO More preparations, such as home location or set type by parameter
					world.spawnEntity(consort);
				} catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	/////////////////////////Salamander
	
	public static class PipeHouse1 extends ConsortVillagePiece
	{
		public PipeHouse1()
		{
		
		}
		
		public PipeHouse1(ConsortVillageCenter.VillageCenter start, Random rand, StructureBoundingBox boundingBox, EnumFacing facing)
		{
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
			IBlockState floorBlock = provider.blockRegistry.getBlockState("upper");
			IBlockState doorBlock = provider.blockRegistry.getBlockState("village_door");
			
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
			generateDoor(worldIn, structureBoundingBoxIn, randomIn, 3, 1, 1, EnumFacing.NORTH, (BlockDoor) doorBlock.getBlock());
			
			this.spawnConsort(2, 1, 3, structureBoundingBoxIn, worldIn);
			this.spawnConsort(3, 1, 4,structureBoundingBoxIn, worldIn);
			return true;
		}
	}
	
	public static class HighPipeHouse1 extends ConsortVillagePiece
	{
		public HighPipeHouse1()
		{
		
		}
		
		public HighPipeHouse1(ConsortVillageCenter.VillageCenter start, Random rand, StructureBoundingBox boundingBox, EnumFacing facing)
		{
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
			IBlockState floorBlock = provider.blockRegistry.getBlockState("upper");
			IBlockState doorBlock = provider.blockRegistry.getBlockState("village_door");
			
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
			
			generateDoor(worldIn, structureBoundingBoxIn, randomIn, 3, 1, 1, EnumFacing.NORTH, (BlockDoor) doorBlock.getBlock());
			
			this.spawnConsort(2, 1, 4,structureBoundingBoxIn, worldIn);
			this.spawnConsort(3, 1, 5,structureBoundingBoxIn, worldIn);
			this.spawnConsort(4, 1, 4,structureBoundingBoxIn, worldIn);
			
			return true;
		}
	}
	
	public static class SmallTowerStore extends ConsortVillagePiece
	{
		public SmallTowerStore()
		{
		
		}
		
		public SmallTowerStore(ConsortVillageCenter.VillageCenter start, Random rand, StructureBoundingBox boundingBox, EnumFacing facing)
		{
			this.setCoordBaseMode(facing);
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
			
			this.setBlockState(worldIn, torch.withProperty(BlockTorch.FACING, EnumFacing.SOUTH), 1, 3, 5, structureBoundingBoxIn);
			this.setBlockState(worldIn, torch.withProperty(BlockTorch.FACING, EnumFacing.SOUTH), 5, 3, 5, structureBoundingBoxIn);
			
			generateDoor(worldIn, structureBoundingBoxIn, randomIn, 3, 1, 1, EnumFacing.NORTH, (BlockDoor) doorBlock.getBlock());
			
			this.spawnConsort(3, 1, 5,structureBoundingBoxIn, worldIn, EnumConsort.MerchantType.FOOD);
			
			return true;
		}
	}
	
	/////////////////////////Turtle
	
	public static class LoweredShellHouse1 extends ConsortVillagePiece
	{
		public LoweredShellHouse1()
		{
		
		}
		
		public LoweredShellHouse1(ConsortVillageCenter.VillageCenter start, Random rand, StructureBoundingBox boundingBox, EnumFacing facing)
		{
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
			
			this.spawnConsort(2, 1, 5, structureBoundingBoxIn, worldIn);
			this.spawnConsort(5, 1, 5, structureBoundingBoxIn, worldIn);
			
			return true;
		}
	}
	
	public static class TurtleMarketBuilding1 extends ConsortVillagePiece
	{
		public TurtleMarketBuilding1()
		{
		
		}
		
		public TurtleMarketBuilding1(ConsortVillageCenter.VillageCenter start, Random rand, StructureBoundingBox boundingBox, EnumFacing facing)
		{
			this.setCoordBaseMode(facing);
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
			
			this.spawnConsort(4, 1, 15, structureBoundingBoxIn, worldIn, EnumConsort.MerchantType.FOOD);
			this.spawnConsort(9, 1, 15, structureBoundingBoxIn, worldIn, EnumConsort.MerchantType.FOOD);
			
			return true;
		}
	}
	
	public static class TurtleTemple1 extends ConsortVillagePiece
	{
		public TurtleTemple1()
		{
		
		}
		
		public TurtleTemple1(ConsortVillageCenter.VillageCenter start, Random rand, StructureBoundingBox boundingBox, EnumFacing facing)
		{
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
			
			this.generateDoor(worldIn, structureBoundingBoxIn, randomIn, 5, 1, 1, EnumFacing.NORTH, Blocks.IRON_DOOR);
			
			this.setBlockState(worldIn, Blocks.STONE_BUTTON.getDefaultState().withRotation(Rotation.CLOCKWISE_180), 6, 1, 0, structureBoundingBoxIn);
			this.setBlockState(worldIn, Blocks.STONE_BUTTON.getDefaultState(), 4, 1, 2, structureBoundingBoxIn);
			
			this.spawnConsort(4, 0, 10, structureBoundingBoxIn, worldIn);
			this.spawnConsort(5, 0, 10, structureBoundingBoxIn, worldIn);
			this.spawnConsort(6, 0, 10, structureBoundingBoxIn, worldIn);
			
			return true;
		}
	}
	
	/////////////////////////Nakagators
	
	public static class HighNakHousing1 extends ConsortVillagePiece
	{
		public HighNakHousing1()
		{
		
		}
		
		public HighNakHousing1(ConsortVillageCenter.VillageCenter start, Random rand, StructureBoundingBox boundingBox, EnumFacing facing)
		{
			this.setCoordBaseMode(facing);
			this.boundingBox = boundingBox;
		}
		
		public static HighNakHousing1 createPiece(ConsortVillageCenter.VillageCenter start, List<StructureComponent> componentList, Random rand, int x, int y, int z, EnumFacing facing)
		{
			StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, 8, 13, 9, facing);
			return StructureComponent.findIntersecting(componentList, structureboundingbox) == null ? new HighNakHousing1(start, rand, structureboundingbox, facing) : null;
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
			IBlockState buildBlock = provider.blockRegistry.getBlockState("structure_primary");
			IBlockState stairs1 = provider.blockRegistry.getStairs("structure_primary_stairs", EnumFacing.NORTH, false);
			IBlockState stairs2 = provider.blockRegistry.getStairs("structure_primary_stairs", EnumFacing.SOUTH, false);
			IBlockState doorBlock = provider.blockRegistry.getBlockState("village_door");
			IBlockState floorBlock = provider.blockRegistry.getBlockState("ground");
			
			//Floor
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, -1, 1, 7, 0, 8, floorBlock, floorBlock, false);
			
			//Base walls and second/third floors
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 1, 7, 12, 1, buildBlock, buildBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 8, 7, 12, 8, buildBlock, buildBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 2, 0, 12, 7, buildBlock, buildBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 1, 2, 7, 12, 7, buildBlock, buildBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 4, 2, 6, 4, 7, buildBlock, buildBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 8, 2, 6, 8, 7, buildBlock, buildBlock, false);
			
			//Remove blocks in front of the building
			this.clearFront(worldIn, structureBoundingBoxIn, 2, 5, 1, 0);
			
			//First floor clear, doors, windows and furnishing
			this.fillWithAir(worldIn, structureBoundingBoxIn, 1, 1, 2, 6, 3, 7);
			this.generateDoor(worldIn, structureBoundingBoxIn, randomIn, 3, 1, 1, EnumFacing.NORTH, (BlockDoor) doorBlock.getBlock());
			this.generateDoor(worldIn, structureBoundingBoxIn, randomIn, 4, 1, 1, EnumFacing.NORTH, (BlockDoor) doorBlock.getBlock());
			this.setBlockState(worldIn, Blocks.AIR.getDefaultState(), 0, 2, 3, structureBoundingBoxIn);
			this.setBlockState(worldIn, Blocks.AIR.getDefaultState(), 0, 2, 5, structureBoundingBoxIn);
			this.setBlockState(worldIn, Blocks.AIR.getDefaultState(), 3, 2, 8, structureBoundingBoxIn);
			this.setBlockState(worldIn, Blocks.AIR.getDefaultState(), 5, 2, 8, structureBoundingBoxIn);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 5, 2, 1, 5, buildBlock, buildBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 1, 6, 3, 1, 7, buildBlock, buildBlock, false);
			
			//First to second floor stairs
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 1, 4, 6, 1, 7, buildBlock, buildBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 2, 5, 6, 2, 7, buildBlock, buildBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 3, 6, 6, 3, 7, buildBlock, buildBlock, false);
			this.setBlockState(worldIn, stairs1, 6, 1, 3, structureBoundingBoxIn);
			this.setBlockState(worldIn, stairs1, 6, 2, 4, structureBoundingBoxIn);
			this.setBlockState(worldIn, stairs1, 6, 3, 5, structureBoundingBoxIn);
			this.setBlockState(worldIn, stairs1, 6, 4, 6, structureBoundingBoxIn);
			this.fillWithAir(worldIn, structureBoundingBoxIn, 6, 4, 2, 6, 4, 5);
			
			//Second floor windows
			this.setBlockState(worldIn, Blocks.AIR.getDefaultState(), 2, 6, 1, structureBoundingBoxIn);
			this.setBlockState(worldIn, Blocks.AIR.getDefaultState(), 5, 6, 1, structureBoundingBoxIn);
			this.setBlockState(worldIn, Blocks.AIR.getDefaultState(), 0, 6, 3, structureBoundingBoxIn);
			this.setBlockState(worldIn, Blocks.AIR.getDefaultState(), 0, 6, 5, structureBoundingBoxIn);
			this.setBlockState(worldIn, Blocks.AIR.getDefaultState(), 2, 6, 8, structureBoundingBoxIn);
			this.setBlockState(worldIn, Blocks.AIR.getDefaultState(), 5, 6, 8, structureBoundingBoxIn);
			this.setBlockState(worldIn, Blocks.AIR.getDefaultState(), 7, 6, 3, structureBoundingBoxIn);
			this.setBlockState(worldIn, Blocks.AIR.getDefaultState(), 7, 6, 6, structureBoundingBoxIn);
			
			//Second to third floor stairs
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 5, 2, 1, 5, 5, buildBlock, buildBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 6, 2, 1, 6, 4, buildBlock, buildBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 7, 2, 1, 7, 3, buildBlock, buildBlock, false);
			this.setBlockState(worldIn, stairs2, 1, 5, 6, structureBoundingBoxIn);
			this.setBlockState(worldIn, stairs2, 1, 6, 5, structureBoundingBoxIn);
			this.setBlockState(worldIn, stairs2, 1, 7, 4, structureBoundingBoxIn);
			this.setBlockState(worldIn, stairs2, 1, 8, 3, structureBoundingBoxIn);
			this.fillWithAir(worldIn, structureBoundingBoxIn, 1, 8, 4, 1, 8, 7);
			
			//Third floor windows
			this.setBlockState(worldIn, Blocks.AIR.getDefaultState(), 2, 10, 1, structureBoundingBoxIn);
			this.setBlockState(worldIn, Blocks.AIR.getDefaultState(), 5, 10, 1, structureBoundingBoxIn);
			this.setBlockState(worldIn, Blocks.AIR.getDefaultState(), 0, 10, 3, structureBoundingBoxIn);
			this.setBlockState(worldIn, Blocks.AIR.getDefaultState(), 0, 10, 5, structureBoundingBoxIn);
			this.setBlockState(worldIn, Blocks.AIR.getDefaultState(), 7, 10, 3, structureBoundingBoxIn);
			this.setBlockState(worldIn, Blocks.AIR.getDefaultState(), 7, 10, 6, structureBoundingBoxIn);
			
			//Consorts
			this.spawnConsort(2, 1, 6, structureBoundingBoxIn, worldIn);
			this.spawnConsort(3, 5, 3, structureBoundingBoxIn, worldIn);
			this.spawnConsort(5, 9, 6, structureBoundingBoxIn, worldIn);
			
			return true;
		}
	}
	
	public static class HighNakMarket1 extends ConsortVillagePiece
	{
		public HighNakMarket1()
		{
		
		}
		
		public HighNakMarket1(ConsortVillageCenter.VillageCenter start, Random rand, StructureBoundingBox boundingBox, EnumFacing facing)
		{
			this.setCoordBaseMode(facing);
			this.boundingBox = boundingBox;
		}
		
		public static HighNakMarket1 createPiece(ConsortVillageCenter.VillageCenter start, List<StructureComponent> componentList, Random rand, int x, int y, int z, EnumFacing facing)
		{
			StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, 12, 14, 10, facing);
			return StructureComponent.findIntersecting(componentList, structureboundingbox) == null ? new HighNakMarket1(start, rand, structureboundingbox, facing) : null;
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
			IBlockState buildBlock = provider.blockRegistry.getBlockState("structure_primary");
			IBlockState stairs1 = provider.blockRegistry.getStairs("structure_primary_stairs", EnumFacing.SOUTH, false);
			IBlockState stairs2 = provider.blockRegistry.getStairs("structure_primary_stairs", EnumFacing.NORTH, false);
			IBlockState doorBlock = provider.blockRegistry.getBlockState("village_door");
			IBlockState floorBlock = provider.blockRegistry.getBlockState("ground");
			IBlockState fence = provider.blockRegistry.getBlockState("village_fence");
			
			//Floor
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, -1, 1, 8, 0, 6, floorBlock, floorBlock, false);
			
			//Base walls and floors
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, -1, 7, 9, 13, 7, buildBlock, buildBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, -1, 1, 2, 13, 6, buildBlock, buildBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 9, -1, 1, 9, 13, 6, buildBlock, buildBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 4, 1, 8, 4, 6, buildBlock, buildBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 8, 1, 8, 8, 6, buildBlock, buildBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 12, 2, 8, 12, 6, buildBlock, buildBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 12, 1, 8, 13, 1, buildBlock, buildBlock, false);
			
			//Remove blocks in front of passages
			this.clearFront(worldIn, structureBoundingBoxIn, 2, 9, 1, 0);
			this.fillWithAir(worldIn, structureBoundingBoxIn, 10, 1, 8, 11, 4, 9);
			
			//Floor furnishing and doors
			this.fillWithAir(worldIn, structureBoundingBoxIn, 3, 1, 1, 8, 3, 6);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 1, 4, 8, 1, 4, buildBlock, buildBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 5, 4, 8, 5, 4, buildBlock, buildBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 5, 1, 8, 5, 1, fence, fence, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 9, 4, 8, 9, 4, buildBlock, buildBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 9, 1, 8, 9, 1, fence, fence, false);
			this.generateDoor(worldIn, structureBoundingBoxIn, randomIn, 9, 5, 2, EnumFacing.WEST, (BlockDoor) doorBlock.getBlock());
			this.fillWithAir(worldIn, structureBoundingBoxIn, 2, 5, 2, 2, 6, 2);
			this.fillWithAir(worldIn, structureBoundingBoxIn, 9, 9, 2, 9, 10, 2);
			
			//Stairs 1
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 10, 4, 1, 11, 4, 4, buildBlock, buildBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 11, 5, 1, 11, 5, 4, fence, fence, false);
			this.setBlockState(worldIn, fence, 10, 5, 1, structureBoundingBoxIn);
			this.setBlockState(worldIn, stairs1, 10, 1, 7, structureBoundingBoxIn);
			this.setBlockState(worldIn, buildBlock, 11, 1, 7, structureBoundingBoxIn);
			this.setBlockState(worldIn, fence, 11, 2, 7, structureBoundingBoxIn);
			this.setBlockState(worldIn, stairs1, 10, 2, 6, structureBoundingBoxIn);
			this.setBlockState(worldIn, buildBlock, 11, 2, 6, structureBoundingBoxIn);
			this.setBlockState(worldIn, fence, 11, 3, 6, structureBoundingBoxIn);
			this.setBlockState(worldIn, stairs1, 10, 3, 5, structureBoundingBoxIn);
			this.setBlockState(worldIn, buildBlock, 11, 3, 5, structureBoundingBoxIn);
			this.setBlockState(worldIn, fence, 11, 4, 5, structureBoundingBoxIn);
			this.setBlockState(worldIn, stairs1, 10, 4, 4, structureBoundingBoxIn);
			
			//Stairs 2
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 4, 1, 1, 4, 3, buildBlock, buildBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 5, 1, 0, 5, 3, fence, fence, false);
			this.setBlockState(worldIn, fence, 1, 5, 1, structureBoundingBoxIn);
			this.setBlockState(worldIn, stairs2, 1, 5, 4, structureBoundingBoxIn);
			this.setBlockState(worldIn, buildBlock, 0, 5, 4, structureBoundingBoxIn);
			this.setBlockState(worldIn, fence, 0, 6, 4, structureBoundingBoxIn);
			this.setBlockState(worldIn, stairs2, 1, 6, 5, structureBoundingBoxIn);
			this.setBlockState(worldIn, buildBlock, 0, 6, 5, structureBoundingBoxIn);
			this.setBlockState(worldIn, fence, 0, 7, 5, structureBoundingBoxIn);
			this.setBlockState(worldIn, stairs2, 1, 7, 6, structureBoundingBoxIn);
			this.setBlockState(worldIn, buildBlock, 0, 7, 6, structureBoundingBoxIn);
			this.setBlockState(worldIn, fence, 0, 8, 6, structureBoundingBoxIn);
			this.setBlockState(worldIn, stairs2, 1, 8, 7, structureBoundingBoxIn);
			this.setBlockState(worldIn, buildBlock, 0, 8, 7, structureBoundingBoxIn);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 9, 7, 0, 9, 8, fence, fence, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 8, 8, 11, 8, 9, buildBlock, buildBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 9, 9, 11, 9, 9, fence, fence, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 10, 8, 1, 11, 8, 7, buildBlock, buildBlock, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 11, 9, 1, 11, 9, 8, fence, fence, false);
			this.setBlockState(worldIn, fence, 10, 9, 1, structureBoundingBoxIn);
			
			this.spawnConsort(5, 1, 5, structureBoundingBoxIn, worldIn, EnumConsort.MerchantType.FOOD);
			this.spawnConsort(6, 5, 5, structureBoundingBoxIn, worldIn, EnumConsort.MerchantType.FOOD);
			this.spawnConsort(5, 9, 5, structureBoundingBoxIn, worldIn, EnumConsort.MerchantType.FOOD);
			
			return true;
		}
	}
	
	/////////////////////////Iguanas
	
	public static class SmallTent1 extends ConsortVillagePiece
	{
		private int woolType = 1;
		
		public SmallTent1()
		{
		
		}
		
		public SmallTent1(ConsortVillageCenter.VillageCenter start, Random rand, StructureBoundingBox boundingBox, EnumFacing facing)
		{
			this.setCoordBaseMode(facing);
			this.boundingBox = boundingBox;
			woolType = 1 + rand.nextInt(3);
		}
		
		public static SmallTent1 createPiece(ConsortVillageCenter.VillageCenter start, List<StructureComponent> componentList, Random rand, int x, int y, int z, EnumFacing facing)
		{
			StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, 9, 6, 6, facing);
			return StructureComponent.findIntersecting(componentList, structureboundingbox) == null ? new SmallTent1(start, rand, structureboundingbox, facing) : null;
		}
		
		@Override
		protected void writeStructureToNBT(NBTTagCompound tagCompound)
		{
			super.writeStructureToNBT(tagCompound);
			tagCompound.setInteger("Wool", this.woolType);
		}
		
		@Override
		protected void readStructureFromNBT(NBTTagCompound tagCompound, TemplateManager p_143011_2_)
		{
			super.readStructureFromNBT(tagCompound, p_143011_2_);
			this.woolType = tagCompound.getInteger("Wool");
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
			IBlockState fence = provider.blockRegistry.getBlockState("village_fence");
			IBlockState surface = provider.blockRegistry.getBlockState("surface");
			IBlockState dirt = Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.COARSE_DIRT);
			IBlockState wool = provider.blockRegistry.getBlockState("structure_wool_"+woolType);
			
			//Floor
			this.fillWithAir(worldIn, structureBoundingBoxIn, 1, 1, 1, 7, 6, 5);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 1, 7, 0, 5, surface, surface, false);
			for(int x = 1; x < 8; x++)
				for(int z = 1; z < 6; z++)
					if(randomIn.nextFloat() < 0.15f)
						this.setBlockState(worldIn, dirt, x, 0, z,  structureBoundingBoxIn);
			
			//Remove blocks in front of the building
			this.clearFront(worldIn, structureBoundingBoxIn, 1, 7, 1, 0);
			
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 1, 3, 4, 4, 3, fence, fence, false);
			
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 1, 0, 1, 5, wool, wool, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 8, 1, 1, 8, 1, 5, wool, wool, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 2, 1, 1, 2, 5, wool, wool, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 2, 1, 7, 2, 5, wool, wool, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 3, 1, 2, 3, 5, wool, wool, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 3, 1, 6, 3, 5, wool, wool, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 4, 1, 3, 4, 5, wool, wool, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 4, 1, 5, 4, 5, wool, wool, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 5, 1, 4, 5, 5, wool, wool, false);
			
			this.spawnConsort(3, 1, 3, structureBoundingBoxIn, worldIn);
			
			return true;
		}
	}
	
	public static class LargeTent1 extends ConsortVillagePiece
	{
		private int woolType = 1;
		
		public LargeTent1()
		{
		
		}
		
		public LargeTent1(ConsortVillageCenter.VillageCenter start, Random rand, StructureBoundingBox boundingBox, EnumFacing facing)
		{
			this.setCoordBaseMode(facing);
			this.boundingBox = boundingBox;
			woolType = 1 + rand.nextInt(3);
		}
		
		public static LargeTent1 createPiece(ConsortVillageCenter.VillageCenter start, List<StructureComponent> componentList, Random rand, int x, int y, int z, EnumFacing facing)
		{
			StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, 12, 8, 16, facing);
			return StructureComponent.findIntersecting(componentList, structureboundingbox) == null ? new LargeTent1(start, rand, structureboundingbox, facing) : null;
		}
		
		@Override
		protected void writeStructureToNBT(NBTTagCompound tagCompound)
		{
			super.writeStructureToNBT(tagCompound);
			tagCompound.setInteger("Wool", this.woolType);
		}
		
		@Override
		protected void readStructureFromNBT(NBTTagCompound tagCompound, TemplateManager p_143011_2_)
		{
			super.readStructureFromNBT(tagCompound, p_143011_2_);
			this.woolType = tagCompound.getInteger("Wool");
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
			IBlockState fence = provider.blockRegistry.getBlockState("village_fence");
			IBlockState planks = provider.blockRegistry.getBlockState("structure_planks");
			IBlockState surface = provider.blockRegistry.getBlockState("surface");
			IBlockState dirt = Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.COARSE_DIRT);
			IBlockState wool = provider.blockRegistry.getBlockState("structure_wool_"+woolType);
			IBlockState torch = provider.blockRegistry.getBlockState("torch");
			
			//Floor
			this.fillWithAir(worldIn, structureBoundingBoxIn, 1, 1, 1, 10, 6, 15);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 1, 10, 0, 15, surface, surface, false);
			for(int x = 1; x < 11; x++)
				for(int z = 1; z < 16; z++)
					if(randomIn.nextFloat() < 0.15f)
						this.setBlockState(worldIn, dirt, x, 0, z,  structureBoundingBoxIn);
			
			//Remove blocks in front of the building
			this.clearFront(worldIn, structureBoundingBoxIn, 2, 9, 1, 0);
			
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 2, 1, 4, 2, fence, fence, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 10, 1, 2, 10, 4, 2, fence, fence, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 14, 1, 4, 14, fence, fence, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 10, 1, 14, 10, 4, 14, fence, fence, false);
			
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 5, 6, 9, 5, 6, fence, fence, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 6, 7, 8, 6, 7, fence, fence, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 6, 9, 8, 6, 9, fence, fence, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 5, 11, 9, 5, 11, fence, fence, false);
			
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 1, 1, 4, 1, wool, wool, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 2, 1, 2, 4, 1, wool, wool, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 3, 1, 3, 4, 1, wool, wool, false);
			this.setBlockState(worldIn, wool, 4, 4, 1, structureBoundingBoxIn);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 10, 1, 1, 10, 4, 1, wool, wool, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 9, 2, 1, 9, 4, 1, wool, wool, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 8, 3, 1, 8, 4, 1, wool, wool, false);
			this.setBlockState(worldIn, wool, 7, 4, 1, structureBoundingBoxIn);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 15, 1, 4, 15, wool, wool, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 2, 15, 2, 4, 15, wool, wool, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 3, 15, 3, 4, 15, wool, wool, false);
			this.setBlockState(worldIn, wool, 4, 4, 15, structureBoundingBoxIn);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 10, 1, 15, 10, 4, 15, wool, wool, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 9, 2, 15, 9, 4, 15, wool, wool, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 8, 3, 15, 8, 4, 15, wool, wool, false);
			this.setBlockState(worldIn, wool, 7, 4, 15, structureBoundingBoxIn);
			
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 2, 0, 4, 14, wool, wool, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 11, 1, 2, 11, 4, 14, wool, wool, false);
			
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 5, 2, 10, 5, 2, wool, wool, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 5, 14, 10, 5, 14, wool, wool, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 5, 3, 1, 5, 13, wool, wool, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 10, 5, 3, 10, 5, 13, wool, wool, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 6, 3, 9, 6, 3, wool, wool, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 6, 13, 9, 6, 13, wool, wool, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 6, 4, 2, 6, 12, wool, wool, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 9, 6, 4, 9, 6, 12, wool, wool, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 7, 4, 8, 7, 12, wool, wool, false);
			
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 3, 1, 1, 13, planks, planks, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 10, 1, 3, 10, 1, 13, planks, planks, false);
			
			this.setBlockState(worldIn, torch.withProperty(BlockTorch.FACING, EnumFacing.EAST), 1, 3, 5, structureBoundingBoxIn);
			this.setBlockState(worldIn, torch.withProperty(BlockTorch.FACING, EnumFacing.EAST), 1, 3, 11, structureBoundingBoxIn);
			this.setBlockState(worldIn, torch.withProperty(BlockTorch.FACING, EnumFacing.WEST), 10, 3, 5, structureBoundingBoxIn);
			this.setBlockState(worldIn, torch.withProperty(BlockTorch.FACING, EnumFacing.WEST), 10, 3, 11, structureBoundingBoxIn);
			
			this.spawnConsort(2, 1, 5, structureBoundingBoxIn, worldIn);
			this.spawnConsort(2, 1, 11, structureBoundingBoxIn, worldIn);
			this.spawnConsort(9, 1, 5, structureBoundingBoxIn, worldIn);
			this.spawnConsort(9, 1, 11, structureBoundingBoxIn, worldIn);
			
			return true;
		}
	}
	
	public static class SmallTentStore extends ConsortVillagePiece
	{
		private int woolType = 1;
		
		public SmallTentStore()
		{
		
		}
		
		public SmallTentStore(ConsortVillageCenter.VillageCenter start, Random rand, StructureBoundingBox boundingBox, EnumFacing facing)
		{
			this.setCoordBaseMode(facing);
			this.boundingBox = boundingBox;
			woolType = 1 + rand.nextInt(3);
		}
		
		public static SmallTentStore createPiece(ConsortVillageCenter.VillageCenter start, List<StructureComponent> componentList, Random rand, int x, int y, int z, EnumFacing facing)
		{
			StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, 7, 7, 7, facing);
			return StructureComponent.findIntersecting(componentList, structureboundingbox) == null ? new SmallTentStore(start, rand, structureboundingbox, facing) : null;
		}
		
		@Override
		protected void writeStructureToNBT(NBTTagCompound tagCompound)
		{
			super.writeStructureToNBT(tagCompound);
			tagCompound.setInteger("Wool", this.woolType);
		}
		
		@Override
		protected void readStructureFromNBT(NBTTagCompound tagCompound, TemplateManager p_143011_2_)
		{
			super.readStructureFromNBT(tagCompound, p_143011_2_);
			this.woolType = tagCompound.getInteger("Wool");
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
			IBlockState fence = provider.blockRegistry.getBlockState("village_fence");
			IBlockState planks = provider.blockRegistry.getBlockState("structure_planks");
			IBlockState surface = provider.blockRegistry.getBlockState("surface");
			IBlockState dirt = Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.COARSE_DIRT);
			IBlockState wool = provider.blockRegistry.getBlockState("structure_wool_"+woolType);
			IBlockState torch = provider.blockRegistry.getBlockState("torch");
			
			//Floor
			this.fillWithAir(worldIn, structureBoundingBoxIn, 1, 1, 1, 5, 5, 5);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 1, 5, 0, 5, surface, surface, false);
			for(int x = 1; x < 6; x++)
				for(int z = 1; z < 6; z++)
					if(randomIn.nextFloat() < 0.15f)
						this.setBlockState(worldIn, dirt, x, 0, z,  structureBoundingBoxIn);
			
			//Remove blocks in front of the building
			this.clearFront(worldIn, structureBoundingBoxIn, 1, 5, 1, 0);
			
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 1, 1, 3, 1, fence, fence, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 5, 1, 3, 5, fence, fence, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 1, 1, 5, 3, 1, fence, fence, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 1, 5, 5, 3, 5, fence, fence, false);
			
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 1, 1, 4, 1, 1, planks, planks, false);
			
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 1, 0, 3, 5, wool, wool, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 1, 1, 6, 3, 5, wool, wool, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 6, 5, 3, 6, wool, wool, false);
			
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 4, 1, 5, 4, 1, wool, wool, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 4, 5, 5, 4, 5, wool, wool, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 4, 2, 1, 4, 4, wool, wool, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 4, 2, 5, 4, 4, wool, wool, false);
			
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 5, 2, 2, 5, 4, wool, wool, false);
			this.fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 5, 2, 4, 5, 4, wool, wool, false);
			this.setBlockState(worldIn, wool, 3, 5, 2, structureBoundingBoxIn);
			this.setBlockState(worldIn, wool, 3, 6, 3, structureBoundingBoxIn);
			this.setBlockState(worldIn, wool, 3, 5, 4, structureBoundingBoxIn);
			
			this.setBlockState(worldIn, torch.withProperty(BlockTorch.FACING, EnumFacing.EAST), 1, 2, 4, structureBoundingBoxIn);
			this.setBlockState(worldIn, torch.withProperty(BlockTorch.FACING, EnumFacing.WEST), 5, 2, 4, structureBoundingBoxIn);
			
			this.spawnConsort(3, 1, 2, structureBoundingBoxIn, worldIn, EnumConsort.MerchantType.FOOD);
			
			return true;
		}
	}
	
	/////////////////////////Utility
	
	public static class VillagePath extends ConsortVillagePiece
	{
		private int length;
		
		public VillagePath()
		{
		}
		
		public VillagePath(ConsortVillageCenter.VillageCenter start, Random rand, StructureBoundingBox boundingBox, EnumFacing facing)
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
		protected void readStructureFromNBT(NBTTagCompound tagCompound, TemplateManager p_143011_2_)
		{
			super.readStructureFromNBT(tagCompound, p_143011_2_);
			this.length = tagCompound.getInteger("Length");
		}
		
		@Override
		public void buildComponent(StructureComponent componentIn, List<StructureComponent> listIn, Random rand)
		{
			boolean flag = false;
			
			for (int i = rand.nextInt(5); i < this.length - 8; i += 2 + rand.nextInt(5))
			{
				StructureComponent newPiece = this.getNextComponentNN((ConsortVillageCenter.VillageCenter)componentIn, listIn, rand, 0, i);
				
				if (newPiece != null)
				{
					i += Math.max(newPiece.getBoundingBox().getXSize(), newPiece.getBoundingBox().getZSize());
					flag = true;
				}
			}
			
			for (int j = rand.nextInt(5); j < this.length - 8; j += 2 + rand.nextInt(5))
			{
				StructureComponent newPiece = this.getNextComponentPP((ConsortVillageCenter.VillageCenter) componentIn, listIn, rand, 0, j);
				
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
						ConsortVillageComponents.generateAndAddRoadPiece((ConsortVillageCenter.VillageCenter) componentIn, listIn, rand, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ, EnumFacing.WEST);
						break;
					case SOUTH:
						ConsortVillageComponents.generateAndAddRoadPiece((ConsortVillageCenter.VillageCenter) componentIn, listIn, rand, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.maxZ - 2, EnumFacing.WEST);
						break;
					case WEST:
						ConsortVillageComponents.generateAndAddRoadPiece((ConsortVillageCenter.VillageCenter) componentIn, listIn, rand, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH);
						break;
					case EAST:
						ConsortVillageComponents.generateAndAddRoadPiece((ConsortVillageCenter.VillageCenter) componentIn, listIn, rand, this.boundingBox.maxX - 2, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH);
				}
			}
			
			if (flag && rand.nextInt(3) > 0 && enumfacing != null)
			{
				switch (enumfacing)
				{
					case NORTH:
					default:
						ConsortVillageComponents.generateAndAddRoadPiece((ConsortVillageCenter.VillageCenter) componentIn, listIn, rand, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ, EnumFacing.EAST);
						break;
					case SOUTH:
						ConsortVillageComponents.generateAndAddRoadPiece((ConsortVillageCenter.VillageCenter) componentIn, listIn, rand, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.maxZ - 2, EnumFacing.EAST);
						break;
					case WEST:
						ConsortVillageComponents.generateAndAddRoadPiece((ConsortVillageCenter.VillageCenter) componentIn, listIn, rand, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH);
						break;
					case EAST:
						ConsortVillageComponents.generateAndAddRoadPiece((ConsortVillageCenter.VillageCenter) componentIn, listIn, rand, this.boundingBox.maxX - 2, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH);
				}
			}
		}
		
		public static StructureBoundingBox findPieceBox(ConsortVillageCenter.VillageCenter start, List<StructureComponent> components, Random rand, int x, int y, int z, EnumFacing facing)
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
					placeRoadtile(i, j, structureBoundingBoxIn, worldIn, pathBlock);
				}
			}
			
			return true;
		}
	}
	
	
	protected static StructureComponent generateAndAddRoadPiece(ConsortVillageCenter.VillageCenter start, List<StructureComponent> componentList, Random rand, int x, int y, int z, EnumFacing facing)
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