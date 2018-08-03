package com.mraof.minestuck.world.lands.structure.village;

import com.google.common.collect.Lists;
import com.mraof.minestuck.entity.consort.EntityConsort;
import com.mraof.minestuck.entity.consort.EnumConsort;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.world.MinestuckDimensionHandler;
import com.mraof.minestuck.world.lands.LandAspectRegistry;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;
import com.mraof.minestuck.world.lands.structure.StructureComponentUtil;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.template.TemplateManager;

import java.util.List;
import java.util.Random;

import static com.mraof.minestuck.world.lands.structure.village.ConsortVillageIguana.*;
import static com.mraof.minestuck.world.lands.structure.village.ConsortVillageNakagator.*;
import static com.mraof.minestuck.world.lands.structure.village.ConsortVillageSalamander.*;
import static com.mraof.minestuck.world.lands.structure.village.ConsortVillageTurtle.*;

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
		MapGenStructureIO.registerStructureComponent(HighNakInn1.class, "MinestuckCVHNaInn1");
		
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
				list.add(new PieceWeight(HighNakInn1.class, 15, MathHelper.getInt(random, 1, 1)));
				break;
		}
		
		list.removeIf(pieceWeight -> pieceWeight.villagePiecesLimit == 0);
		
		return list;
	}
	
	//TODO make sure that components don't generate near the ocean
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
		else if(pieceClass == HighNakInn1.class)
		{
			villagePiece = HighNakInn1.createPiece(start, structureComponents, rand, structureMinX, structureMinY, structureMinZ, facing);
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
	
	public static abstract class ConsortVillagePiece extends StructureComponentUtil
	{
		protected int averageGroundLvl = -1;
		protected boolean[] spawns = new boolean[0];
		
		@Override
		protected void writeStructureToNBT(NBTTagCompound tagCompound)
		{
			tagCompound.setInteger("HPos", this.averageGroundLvl);
			
			for(int i = 0; i < spawns.length; i++)
				tagCompound.setBoolean("spawn"+i, spawns[i]);
		}
		
		@Override
		protected void readStructureFromNBT(NBTTagCompound tagCompound, TemplateManager p_143011_2_)
		{
			this.averageGroundLvl = tagCompound.getInteger("HPos");
			
			for(int i = 0; i < spawns.length; i++)
				spawns[i] = tagCompound.getBoolean("spawn"+i);
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
			for (int x = minX; x <= maxX; x++)
				if (structureBB.isVecInside(new Vec3i(this.getXWithOffset(x, z), this.getYWithOffset(y), this.getZWithOffset(x, z))))
				{
					this.fillWithAir(world, structureBB, x, y, z, x, y + 4, z);
					BlockPos pos = new BlockPos(this.getXWithOffset(x, z - 1), this.getYWithOffset(y), this.getZWithOffset(x, z - 1));
					int i = 0;
					for (int yOffset = 0; yOffset <= 4; yOffset++)
					{
						if (world.getBlockState(pos.up(yOffset)).isBlockNormalCube())
							i++;
						else break;
					}
					IBlockState ladder = Blocks.LADDER.getDefaultState().withProperty(BlockHorizontal.FACING, EnumFacing.SOUTH);
					if (i >= 2)
						this.fillWithBlocks(world, structureBB, x, y, z, x, y + i - 1, z, ladder, ladder, false);
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
		
		protected boolean spawnConsort(int x, int y, int z, StructureBoundingBox boundingBox, World world)
		{
			return spawnConsort(x, y, z, boundingBox, world, EnumConsort.MerchantType.NONE, 48);
		}
		
		protected boolean spawnConsort(int x, int y, int z, StructureBoundingBox boundingBox, World world, int maxHomeDistance)
		{
			return spawnConsort(x, y, z, boundingBox, world, EnumConsort.MerchantType.NONE, maxHomeDistance);
		}
		
		protected boolean spawnConsort(int x, int y, int z, StructureBoundingBox boundingBox, World world, EnumConsort.MerchantType type, int maxHomeDistance)
		{
			BlockPos pos = new BlockPos(this.getXWithOffset(x, z), this.getYWithOffset(y), this.getZWithOffset(x, z));
			
			if(boundingBox.isVecInside(pos))
			{
				LandAspectRegistry.AspectCombination aspects = MinestuckDimensionHandler.getAspects(world.provider.getDimension());
				if(aspects == null)
				{
					Debug.warn("Tried to spawn a consort in a building that is being generated outside of a land dimension.");
					return false;
				}
				
				Class<? extends EntityConsort> c = aspects.aspectTerrain.getConsortType().getConsortClass();
				
				try
				{
					EntityConsort consort = c.getConstructor(World.class).newInstance(world);
					consort.setPosition(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
					
					consort.merchantType = type;
					consort.setHomePosAndDistance(pos, maxHomeDistance);
					
					consort.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(consort)), null);
					
					world.spawnEntity(consort);
					return true;
				} catch(Exception e)
				{
					e.printStackTrace();
				}
			}
			return false;
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