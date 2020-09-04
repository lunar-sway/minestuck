package com.mraof.minestuck.world.gen.feature.structure.village;

import com.google.common.collect.Lists;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.entity.consort.ConsortEntity;
import com.mraof.minestuck.entity.consort.EnumConsort;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.world.gen.LandGenSettings;
import com.mraof.minestuck.world.gen.feature.MSStructurePieces;
import com.mraof.minestuck.world.gen.feature.structure.ImprovedStructurePiece;
import com.mraof.minestuck.world.gen.feature.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.lands.LandTypePair;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LadderBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.*;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.List;
import java.util.Random;

public class ConsortVillagePieces
{
	public static List<PieceWeight> getStructureVillageWeightedPieceList(Random random, EntityType<? extends ConsortEntity> consortType, LandTypePair landTypes)
	{
		List<PieceWeight> list = Lists.newArrayList();
		if(consortType == MSEntityTypes.SALAMANDER)
		{
			list.add(new PieceWeight(SalamanderVillagePieces.PipeHouse1::createPiece, 3, MathHelper.nextInt(random, 5, 8)));
			list.add(new PieceWeight(SalamanderVillagePieces.HighPipeHouse1::createPiece, 6, MathHelper.nextInt(random, 2, 4)));
			list.add(new PieceWeight(SalamanderVillagePieces.SmallTowerStore::createPiece, 10, MathHelper.nextInt(random, 1, 3)));
		} else if(consortType == MSEntityTypes.TURTLE)
		{
			list.add(new PieceWeight(TurtleVillagePieces.ShellHouse1::createPiece, 3, MathHelper.nextInt(random, 5, 8)));
			list.add(new PieceWeight(TurtleVillagePieces.TurtleMarket1::createPiece, 10, MathHelper.nextInt(random, 0, 2)));
			list.add(new PieceWeight(TurtleVillagePieces.TurtleTemple1::createPiece, 10, MathHelper.nextInt(random, 1, 1)));
		} else if(consortType == MSEntityTypes.IGUANA)
		{
			list.add(new PieceWeight(IguanaVillagePieces.SmallTent1::createPiece, 3, MathHelper.nextInt(random, 5, 8)));
			list.add(new PieceWeight(IguanaVillagePieces.LargeTent1::createPiece, 10, MathHelper.nextInt(random, 1, 2)));
			list.add(new PieceWeight(IguanaVillagePieces.SmallTentStore::createPiece, 8, MathHelper.nextInt(random, 2, 3)));
		} else if(consortType == MSEntityTypes.NAKAGATOR)
		{
			list.add(new PieceWeight(NakagatorVillagePieces.HighNakHousing1::createPiece, 6, MathHelper.nextInt(random, 3, 5)));
			list.add(new PieceWeight(NakagatorVillagePieces.HighNakMarket1::createPiece, 10, MathHelper.nextInt(random, 1, 2)));
			list.add(new PieceWeight(NakagatorVillagePieces.HighNakInn1::createPiece, 15, MathHelper.nextInt(random, 1, 1)));
		}
		
		list.removeIf(pieceWeight -> pieceWeight.villagePiecesLimit == 0);
		
		return list;
	}
	
	//TODO make sure that components don't generate near the ocean
	private static StructurePiece generateAndAddComponent(ConsortVillageCenter.VillageCenter start, List<StructurePiece> structurePieces, Random rand, int structureMinX, int structureMinY, int structureMinZ, Direction facing)
	{
		if (Math.abs(structureMinX - start.getBoundingBox().minX) <= 112 && Math.abs(structureMinZ - start.getBoundingBox().minZ) <= 112)
		{
			StructurePiece villagePiece = generateComponent(start, structurePieces, rand, structureMinX, structureMinY, structureMinZ, facing);
			
			if (villagePiece != null)
			{
				structurePieces.add(villagePiece);
				start.pendingHouses.add(villagePiece);
				return villagePiece;
			}
			else return null;
		}
		else return null;
	}
	
	private static ConsortVillagePiece generateComponent(ConsortVillageCenter.VillageCenter start, List<StructurePiece> structurePieces, Random rand, int structureMinX, int structureMinY, int structureMinZ, Direction facing)
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
						
						ConsortVillagePiece villagePiece = pieceWeight.pieceFactory.createPiece(start, structurePieces, rand, structureMinX, structureMinY, structureMinZ, facing);
						
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
	
	public interface PieceFactory
	{
		ConsortVillagePiece createPiece(ConsortVillageCenter.VillageCenter start, List<StructurePiece> componentList, Random rand, int x, int y, int z, Direction facing);
	}
	
	public static class PieceWeight
	{
		public PieceFactory pieceFactory;
		public final int villagePieceWeight;
		public int villagePiecesSpawned;
		public int villagePiecesLimit;
		
		public PieceWeight(PieceFactory pieceFactory, int weight, int limit)
		{
			this.pieceFactory = pieceFactory;
			this.villagePieceWeight = weight;
			this.villagePiecesLimit = limit;
		}
		
		public boolean canSpawnMoreVillagePieces()
		{
			return this.villagePiecesLimit == 0 || this.villagePiecesSpawned < this.villagePiecesLimit;
		}
	}
	
	public static abstract class ConsortVillagePiece extends ImprovedStructurePiece
	{
		protected int averageGroundLvl = -1;
		protected final boolean[] spawns;
		
		protected ConsortVillagePiece(IStructurePieceType structurePieceTypeIn, int componentTypeIn, int spawnCount)
		{
			super(structurePieceTypeIn, componentTypeIn);
			spawns = new boolean[spawnCount];
		}
		
		public ConsortVillagePiece(IStructurePieceType structurePierceTypeIn, CompoundNBT nbt, int spawnCount)
		{
			super(structurePierceTypeIn, nbt);
			this.averageGroundLvl = nbt.getInt("HPos");
			
			spawns = new boolean[spawnCount];
			for(int i = 0; i < spawnCount; i++)
				spawns[i] = nbt.getBoolean("spawn"+i);
		}
		
		@Override
		protected void readAdditional(CompoundNBT tagCompound)
		{
			tagCompound.putInt("HPos", this.averageGroundLvl);
			
			for(int i = 0; i < spawns.length; i++)
				tagCompound.putBoolean("spawn"+i, spawns[i]);
		}
		
		protected StructurePiece getNextComponentNN(ConsortVillageCenter.VillageCenter start, List<StructurePiece> structurePieces, Random rand, int offsetY, int offsetXZ)
		{
			Direction direction = this.getCoordBaseMode();
			
			if (direction != null)
			{
				switch (direction)
				{
					case NORTH:
					default:
						return ConsortVillagePieces.generateAndAddComponent(start, structurePieces, rand, this.boundingBox.minX - 1, this.boundingBox.minY + offsetY, this.boundingBox.minZ + offsetXZ, Direction.WEST);
					case SOUTH:
						return ConsortVillagePieces.generateAndAddComponent(start, structurePieces, rand, this.boundingBox.minX - 1, this.boundingBox.minY + offsetY, this.boundingBox.minZ + offsetXZ, Direction.WEST);
					case WEST:
						return ConsortVillagePieces.generateAndAddComponent(start, structurePieces, rand, this.boundingBox.minX + offsetXZ, this.boundingBox.minY + offsetY, this.boundingBox.minZ - 1, Direction.NORTH);
					case EAST:
						return ConsortVillagePieces.generateAndAddComponent(start, structurePieces, rand, this.boundingBox.minX + offsetXZ, this.boundingBox.minY + offsetY, this.boundingBox.minZ - 1, Direction.NORTH);
				}
			}
			else
			{
				return null;
			}
		}
		
		protected StructurePiece getNextComponentPP(ConsortVillageCenter.VillageCenter start, List<StructurePiece> structurePieces, Random rand, int offsetY, int offsetXZ)
		{
			Direction direction = this.getCoordBaseMode();
			
			if (direction != null)
			{
				switch (direction)
				{
					case NORTH:
					default:
						return ConsortVillagePieces.generateAndAddComponent(start, structurePieces, rand, this.boundingBox.maxX + 1, this.boundingBox.minY + offsetY, this.boundingBox.minZ + offsetXZ, Direction.EAST);
					case SOUTH:
						return ConsortVillagePieces.generateAndAddComponent(start, structurePieces, rand, this.boundingBox.maxX + 1, this.boundingBox.minY + offsetY, this.boundingBox.minZ + offsetXZ, Direction.EAST);
					case WEST:
						return ConsortVillagePieces.generateAndAddComponent(start, structurePieces, rand, this.boundingBox.minX + offsetXZ, this.boundingBox.minY + offsetY, this.boundingBox.maxZ + 1, Direction.SOUTH);
					case EAST:
						return ConsortVillagePieces.generateAndAddComponent(start, structurePieces, rand, this.boundingBox.minX + offsetXZ, this.boundingBox.minY + offsetY, this.boundingBox.maxZ + 1, Direction.SOUTH);
				}
			}
			else
			{
				return null;
			}
		}
		
		protected void clearFront(IWorld world, MutableBoundingBox structureBB, int minX, int maxX, int y, int z)
		{
			for (int x = minX; x <= maxX; x++)
				if (structureBB.isVecInside(new Vec3i(this.getXWithOffset(x, z), this.getYWithOffset(y), this.getZWithOffset(x, z))))
				{
					this.fillWithAir(world, structureBB, x, y, z, x, y + 4, z);
					BlockPos pos = new BlockPos(this.getXWithOffset(x, z - 1), this.getYWithOffset(y), this.getZWithOffset(x, z - 1));
					int i = 0;
					for (int yOffset = 0; yOffset <= 4; yOffset++)
					{
						if (world.getBlockState(pos.up(yOffset)).isSolid())
							i++;
						else break;
					}
					BlockState ladder = Blocks.LADDER.getDefaultState().with(LadderBlock.FACING, Direction.SOUTH);
					if (i >= 2)
						this.fillWithBlocks(world, structureBB, x, y, z, x, y + i - 1, z, ladder, ladder, false);
				}
		}
		
		protected void placeRoadtile(int x, int z, MutableBoundingBox boundingBox, IWorld worldIn, BlockState pathBlock)
		{
			BlockPos blockpos = new BlockPos(x, 64, z);
			
			if (boundingBox.isVecInside(blockpos))
			{
				blockpos = worldIn.getHeight(Heightmap.Type.WORLD_SURFACE_WG, blockpos).down();
				
				if (blockpos.getY() < worldIn.getSeaLevel())
				{
					blockpos = new BlockPos(blockpos.getX(), worldIn.getSeaLevel() - 1, blockpos.getZ());
				}
				
				while (blockpos.getY() >= worldIn.getSeaLevel() - 1)
				{
					BlockState state = worldIn.getBlockState(blockpos);
					
					if (state.getMaterial().isLiquid() || state.isSolid())
					{
						worldIn.setBlockState(blockpos, pathBlock, 2);
						break;
					}
					
					blockpos = blockpos.down();
				}
			}
		}
		
		protected void blockPillar(int x, int y, int z, MutableBoundingBox boundingBox, IWorld world, BlockState block)
		{
			BlockPos pos = new BlockPos(this.getXWithOffset(x, z), this.getYWithOffset(y), this.getZWithOffset(x, z));
			
			if(!boundingBox.isVecInside(pos))
				return;
			
			while(pos.getY() >= world.getSeaLevel())
			{
				world.setBlockState(pos, block, 2);
				
				pos = pos.down();
				
				if(world.getBlockState(pos).isSolid())
					break;
			}
		}
		
		protected boolean spawnConsort(int x, int y, int z, MutableBoundingBox boundingBox, IWorld world, ChunkGenerator<?> chunkGenerator)
		{
			return spawnConsort(x, y, z, boundingBox, world, chunkGenerator, EnumConsort.MerchantType.NONE, 48);
		}
		
		protected boolean spawnConsort(int x, int y, int z, MutableBoundingBox boundingBox, IWorld world, ChunkGenerator<?> chunkGenerator, int maxHomeDistance)
		{
			return spawnConsort(x, y, z, boundingBox, world, chunkGenerator, EnumConsort.MerchantType.NONE, maxHomeDistance);
		}
		
		protected boolean spawnConsort(int x, int y, int z, MutableBoundingBox boundingBox, IWorld world, ChunkGenerator<?> chunkGenerator, EnumConsort.MerchantType type, int maxHomeDistance)
		{
			BlockPos pos = new BlockPos(this.getXWithOffset(x, z), this.getYWithOffset(y), this.getZWithOffset(x, z));
			
			if(boundingBox.isVecInside(pos))
			{
				
				if(!(chunkGenerator.getSettings() instanceof LandGenSettings))
				{
					Debug.warn("Tried to spawn a consort in a building that is being generated outside of a land dimension.");
					return false;
				}
				
				LandTypePair landTypes = ((LandGenSettings) chunkGenerator.getSettings()).getLandTypes();
				
				EntityType<? extends ConsortEntity> consortType = landTypes.terrain.getConsortType();
				
				try
				{
					ConsortEntity consort = consortType.create(world.getWorld());
					if(consort == null)
					{
						Debug.warnf("Unable to create consort entity %s from a world.", consortType);
						return false;
					}
					
					consort.setPosition(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
					
					consort.merchantType = type;
					consort.setHomePosAndDistance(pos, maxHomeDistance);
					
					consort.onInitialSpawn(world, world.getDifficultyForLocation(new BlockPos(consort)), SpawnReason.STRUCTURE, null, null);
					
					world.addEntity(consort);
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
		
		VillagePath(ConsortVillageCenter.VillageCenter start, Random rand, MutableBoundingBox boundingBox, Direction facing)
		{
			super(MSStructurePieces.VILLAGE_PATH, 0, 0);
			this.setCoordBaseMode(facing);
			this.boundingBox = boundingBox;
			this.length = Math.max(boundingBox.getXSize(), boundingBox.getZSize());
		}
		
		public VillagePath(TemplateManager templates, CompoundNBT nbt)
		{
			super(MSStructurePieces.VILLAGE_PATH, nbt, 0);
			this.length = nbt.getInt("Length");
		}
		
		@Override
		protected void readAdditional(CompoundNBT tagCompound)
		{
			super.readAdditional(tagCompound);
			tagCompound.putInt("Length", this.length);
		}
		
		@Override
		public void buildComponent(StructurePiece componentIn, List<StructurePiece> listIn, Random rand)
		{
			boolean flag = false;
			
			for (int i = rand.nextInt(5); i < this.length - 8; i += 2 + rand.nextInt(5))
			{
				StructurePiece newPiece = this.getNextComponentNN((ConsortVillageCenter.VillageCenter)componentIn, listIn, rand, 0, i);
				
				if (newPiece != null)
				{
					i += Math.max(newPiece.getBoundingBox().getXSize(), newPiece.getBoundingBox().getZSize());
					flag = true;
				}
			}
			
			for (int j = rand.nextInt(5); j < this.length - 8; j += 2 + rand.nextInt(5))
			{
				StructurePiece newPiece = this.getNextComponentPP((ConsortVillageCenter.VillageCenter) componentIn, listIn, rand, 0, j);
				
				if (newPiece != null)
				{
					j += Math.max(newPiece.getBoundingBox().getXSize(), newPiece.getBoundingBox().getZSize());
					flag = true;
				}
			}
			
			Direction direction = this.getCoordBaseMode();
			
			if (flag && rand.nextInt(3) > 0 && direction != null)
			{
				switch (direction)
				{
					case NORTH:
					default:
						ConsortVillagePieces.generateAndAddRoadPiece((ConsortVillageCenter.VillageCenter) componentIn, listIn, rand, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ, Direction.WEST);
						break;
					case SOUTH:
						ConsortVillagePieces.generateAndAddRoadPiece((ConsortVillageCenter.VillageCenter) componentIn, listIn, rand, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.maxZ - 2, Direction.WEST);
						break;
					case WEST:
						ConsortVillagePieces.generateAndAddRoadPiece((ConsortVillageCenter.VillageCenter) componentIn, listIn, rand, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ - 1, Direction.NORTH);
						break;
					case EAST:
						ConsortVillagePieces.generateAndAddRoadPiece((ConsortVillageCenter.VillageCenter) componentIn, listIn, rand, this.boundingBox.maxX - 2, this.boundingBox.minY, this.boundingBox.minZ - 1, Direction.NORTH);
				}
			}
			
			if (flag && rand.nextInt(3) > 0 && direction != null)
			{
				switch (direction)
				{
					case NORTH:
					default:
						ConsortVillagePieces.generateAndAddRoadPiece((ConsortVillageCenter.VillageCenter) componentIn, listIn, rand, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ, Direction.EAST);
						break;
					case SOUTH:
						ConsortVillagePieces.generateAndAddRoadPiece((ConsortVillageCenter.VillageCenter) componentIn, listIn, rand, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.maxZ - 2, Direction.EAST);
						break;
					case WEST:
						ConsortVillagePieces.generateAndAddRoadPiece((ConsortVillageCenter.VillageCenter) componentIn, listIn, rand, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.maxZ + 1, Direction.SOUTH);
						break;
					case EAST:
						ConsortVillagePieces.generateAndAddRoadPiece((ConsortVillageCenter.VillageCenter) componentIn, listIn, rand, this.boundingBox.maxX - 2, this.boundingBox.minY, this.boundingBox.maxZ + 1, Direction.SOUTH);
				}
			}
		}

		@Override
		public boolean create(IWorld worldIn, ChunkGenerator<?> chunkGeneratorIn, Random randomIn, MutableBoundingBox structureBoundingBoxIn, ChunkPos chunkPosIn)
		{
			StructureBlockRegistry blocks = StructureBlockRegistry.getOrDefault(chunkGeneratorIn.getSettings());
			BlockState pathBlock = blocks.getBlockState("village_path");

			for (int i = this.boundingBox.minX; i <= this.boundingBox.maxX; ++i)
			{
				for (int j = this.boundingBox.minZ; j <= this.boundingBox.maxZ; ++j)
				{
					placeRoadtile(i, j, structureBoundingBoxIn, worldIn, pathBlock);
				}
			}

			return true;
		}

		public static MutableBoundingBox findPieceBox(ConsortVillageCenter.VillageCenter start, List<StructurePiece> components, Random rand, int x, int y, int z, Direction facing)
		{
			for(int i = 7 * MathHelper.nextInt(rand, 3, 5); i >= 7; i -= 7)
			{
				MutableBoundingBox mutableBoundingBox = MutableBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, 2, 3, i, facing);
				
				if(StructurePiece.findIntersecting(components, mutableBoundingBox) == null)
					return mutableBoundingBox;
			}
			
			return null;
		}
	}
	
	
	protected static StructurePiece generateAndAddRoadPiece(ConsortVillageCenter.VillageCenter start, List<StructurePiece> componentList, Random rand, int x, int y, int z, Direction direction)
	{
		if (Math.abs(x - start.getBoundingBox().minX) <= 112 && Math.abs(z - start.getBoundingBox().minZ) <= 112)
		{
			MutableBoundingBox mutableBoundingBox = VillagePath.findPieceBox(start, componentList, rand, x, y, z, direction);
			
			if (mutableBoundingBox != null && mutableBoundingBox.minY > 10)
			{
				StructurePiece structurePiece = new VillagePath(start, rand, mutableBoundingBox, direction);
				componentList.add(structurePiece);
				start.pendingRoads.add(structurePiece);
				return structurePiece;
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