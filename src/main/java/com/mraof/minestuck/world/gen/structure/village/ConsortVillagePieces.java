package com.mraof.minestuck.world.gen.structure.village;

import com.google.common.collect.Lists;
import com.mraof.minestuck.entity.consort.ConsortEntity;
import com.mraof.minestuck.entity.consort.EnumConsort;
import com.mraof.minestuck.world.gen.structure.ImprovedStructurePiece;
import com.mraof.minestuck.world.gen.structure.MSStructures;
import com.mraof.minestuck.world.gen.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.lands.ILandType;
import com.mraof.minestuck.world.lands.LandTypePair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class ConsortVillagePieces
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	public static List<PieceWeight> getStructureVillageWeightedPieceList(RandomSource random, LandTypePair landTypes)
	{
		List<PieceWeight> list = Lists.newArrayList();
		
		ILandType.PieceRegister register = (factory, weight, limit) -> list.add(new PieceWeight(factory, weight, limit));
		landTypes.getTerrain().addVillagePieces(register, random);
		landTypes.getTitle().addVillagePieces(register, random);
		
		list.removeIf(pieceWeight -> pieceWeight.villagePiecesLimit == 0);
		
		return list;
	}
	
	//TODO make sure that components don't generate near the ocean
	private static StructurePiece generateAndAddComponent(ConsortVillageCenter.VillageCenter start, StructurePieceAccessor accessor, RandomSource rand, int structureMinX, int structureMinY, int structureMinZ, Direction facing)
	{
		if (Math.abs(structureMinX - start.getBoundingBox().minX()) <= 112 && Math.abs(structureMinZ - start.getBoundingBox().minZ()) <= 112)
		{
			StructurePiece villagePiece = generateComponent(start, accessor, rand, structureMinX, structureMinY, structureMinZ, facing);
			
			if (villagePiece != null)
			{
				accessor.addPiece(villagePiece);
				start.pendingHouses.add(villagePiece);
				return villagePiece;
			}
			else return null;
		}
		else return null;
	}
	
	private static ConsortVillagePiece generateComponent(ConsortVillageCenter.VillageCenter start, StructurePieceAccessor accessor, RandomSource rand, int structureMinX, int structureMinY, int structureMinZ, Direction facing)
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
						
						ConsortVillagePiece villagePiece = pieceWeight.pieceFactory.createPiece(start, accessor, rand, structureMinX, structureMinY, structureMinZ, facing);
						
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
		ConsortVillagePiece createPiece(ConsortVillageCenter.VillageCenter start, StructurePieceAccessor accessor, RandomSource rand, int x, int y, int z, Direction facing);
	}
	
	public static class PieceWeight
	{
		public PieceFactory pieceFactory;
		public final int villagePieceWeight;
		public int villagePiecesSpawned;
		public int villagePiecesLimit;
		
		private PieceWeight(PieceFactory pieceFactory, int weight, int limit)
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
		
		protected ConsortVillagePiece(StructurePieceType structurePieceTypeIn, int genDepth, BoundingBox boundingBox, int spawnCount)
		{
			super(structurePieceTypeIn, genDepth, boundingBox);
			spawns = new boolean[spawnCount];
		}
		
		public ConsortVillagePiece(StructurePieceType structurePierceTypeIn, CompoundTag nbt, int spawnCount)
		{
			super(structurePierceTypeIn, nbt);
			this.averageGroundLvl = nbt.getInt("HPos");
			
			spawns = new boolean[spawnCount];
			for(int i = 0; i < spawnCount; i++)
				spawns[i] = nbt.getBoolean("spawn"+i);
		}
		
		@Override
		protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tagCompound)
		{
			tagCompound.putInt("HPos", this.averageGroundLvl);
			
			for(int i = 0; i < spawns.length; i++)
				tagCompound.putBoolean("spawn"+i, spawns[i]);
		}
		
		protected StructurePiece getNextComponentNN(ConsortVillageCenter.VillageCenter start, StructurePieceAccessor accessor, RandomSource rand, int offsetY, int offsetXZ)
		{
			Direction direction = this.getOrientation();
			
			if (direction != null)
			{
				switch (direction)
				{
					case NORTH:
					default:
						return ConsortVillagePieces.generateAndAddComponent(start, accessor, rand, this.boundingBox.minX() - 1, this.boundingBox.minY() + offsetY, this.boundingBox.minZ() + offsetXZ, Direction.WEST);
					case SOUTH:
						return ConsortVillagePieces.generateAndAddComponent(start, accessor, rand, this.boundingBox.minX() - 1, this.boundingBox.minY() + offsetY, this.boundingBox.minZ() + offsetXZ, Direction.WEST);
					case WEST:
						return ConsortVillagePieces.generateAndAddComponent(start, accessor, rand, this.boundingBox.minX() + offsetXZ, this.boundingBox.minY() + offsetY, this.boundingBox.minZ() - 1, Direction.NORTH);
					case EAST:
						return ConsortVillagePieces.generateAndAddComponent(start, accessor, rand, this.boundingBox.minX() + offsetXZ, this.boundingBox.minY() + offsetY, this.boundingBox.minZ() - 1, Direction.NORTH);
				}
			}
			else
			{
				return null;
			}
		}
		
		protected StructurePiece getNextComponentPP(ConsortVillageCenter.VillageCenter start, StructurePieceAccessor accessor, RandomSource rand, int offsetY, int offsetXZ)
		{
			Direction direction = this.getOrientation();
			
			if (direction != null)
			{
				switch (direction)
				{
					case NORTH:
					default:
						return ConsortVillagePieces.generateAndAddComponent(start, accessor, rand, this.boundingBox.maxX() + 1, this.boundingBox.minY() + offsetY, this.boundingBox.minZ() + offsetXZ, Direction.EAST);
					case SOUTH:
						return ConsortVillagePieces.generateAndAddComponent(start, accessor, rand, this.boundingBox.maxX() + 1, this.boundingBox.minY() + offsetY, this.boundingBox.minZ() + offsetXZ, Direction.EAST);
					case WEST:
						return ConsortVillagePieces.generateAndAddComponent(start, accessor, rand, this.boundingBox.minX() + offsetXZ, this.boundingBox.minY() + offsetY, this.boundingBox.maxZ() + 1, Direction.SOUTH);
					case EAST:
						return ConsortVillagePieces.generateAndAddComponent(start, accessor, rand, this.boundingBox.minX() + offsetXZ, this.boundingBox.minY() + offsetY, this.boundingBox.maxZ() + 1, Direction.SOUTH);
				}
			}
			else
			{
				return null;
			}
		}
		
		protected void clearFront(WorldGenLevel level, BoundingBox structureBB, int minX, int maxX, int y, int z)
		{
			for (int x = minX; x <= maxX; x++)
				if (structureBB.isInside(new Vec3i(this.getWorldX(x, z), this.getWorldY(y), this.getWorldZ(x, z))))
				{
					this.generateAirBox(level, structureBB, x, y, z, x, y + 4, z);
					BlockPos pos = new BlockPos(this.getWorldX(x, z - 1), this.getWorldY(y), this.getWorldZ(x, z - 1));
					int i = 0;
					for (int yOffset = 0; yOffset <= 4; yOffset++)
					{
						if (level.getBlockState(pos.above(yOffset)).canOcclude())
							i++;
						else break;
					}
					BlockState ladder = Blocks.LADDER.defaultBlockState().setValue(LadderBlock.FACING, Direction.SOUTH);
					if (i >= 2)
						this.generateBox(level, structureBB, x, y, z, x, y + i - 1, z, ladder, ladder, false);
				}
		}
		
		protected void placeRoadtile(int x, int z, BoundingBox boundingBox, LevelAccessor level, BlockState pathBlock)
		{
			BlockPos blockpos = new BlockPos(x, 64, z);
			
			if (boundingBox.isInside(blockpos))
			{
				blockpos = level.getHeightmapPos(Heightmap.Types.WORLD_SURFACE_WG, blockpos).below();
				
				if (blockpos.getY() < level.getSeaLevel())
				{
					blockpos = new BlockPos(blockpos.getX(), level.getSeaLevel() - 1, blockpos.getZ());
				}
				
				while (blockpos.getY() >= level.getSeaLevel() - 1)
				{
					BlockState state = level.getBlockState(blockpos);
					
					if (!state.getFluidState().isEmpty() || state.canOcclude())
					{
						level.setBlock(blockpos, pathBlock, Block.UPDATE_CLIENTS);
						break;
					}
					
					blockpos = blockpos.below();
				}
			}
		}
		
		protected void blockPillar(int x, int y, int z, BoundingBox boundingBox, LevelAccessor level, BlockState block)
		{
			BlockPos pos = new BlockPos(this.getWorldX(x, z), this.getWorldY(y), this.getWorldZ(x, z));
			
			if(!boundingBox.isInside(pos))
				return;
			
			while(pos.getY() >= level.getSeaLevel())
			{
				level.setBlock(pos, block, Block.UPDATE_CLIENTS);
				
				pos = pos.below();
				
				if(level.getBlockState(pos).canOcclude())
					break;
			}
		}
		
		protected boolean spawnConsort(int x, int y, int z, BoundingBox boundingBox, WorldGenLevel level, ChunkGenerator chunkGenerator)
		{
			return spawnConsort(x, y, z, boundingBox, level, chunkGenerator, EnumConsort.MerchantType.NONE, 48);
		}
		
		protected boolean spawnConsort(int x, int y, int z, BoundingBox boundingBox, WorldGenLevel level, ChunkGenerator chunkGenerator, int maxHomeDistance)
		{
			return spawnConsort(x, y, z, boundingBox, level, chunkGenerator, EnumConsort.MerchantType.NONE, maxHomeDistance);
		}
		
		protected boolean spawnConsort(int x, int y, int z, BoundingBox boundingBox, WorldGenLevel level, ChunkGenerator chunkGenerator, EnumConsort.MerchantType type, int maxHomeDistance)
		{
			BlockPos pos = new BlockPos(this.getWorldX(x, z), this.getWorldY(y), this.getWorldZ(x, z));
			
			if(boundingBox.isInside(pos))
			{
				LandTypePair landTypes = LandTypePair.getTypesOrDefaulted(chunkGenerator);
				
				EntityType<? extends ConsortEntity> consortType = landTypes.getTerrain().getConsortType();
				
				try
				{
					ConsortEntity consort = consortType.create(level.getLevel());
					if(consort == null)
					{
						LOGGER.warn("Unable to create consort entity {} from a level.", consortType);
						return false;
					}
					
					consort.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
					
					consort.merchantType = type;
					consort.restrictTo(pos, maxHomeDistance);
					
					consort.finalizeSpawn(level, level.getCurrentDifficultyAt(pos), MobSpawnType.STRUCTURE, null, null);
					
					level.addFreshEntity(consort);
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
		
		VillagePath(ConsortVillageCenter.VillageCenter start, RandomSource rand, BoundingBox boundingBox, Direction facing)
		{
			super(MSStructures.ConsortVillage.VILLAGE_PATH_PIECE.get(), 0, boundingBox, 0);
			this.setOrientation(facing);
			this.length = Math.max(boundingBox.getXSpan(), boundingBox.getZSpan());
		}
		
		public VillagePath(CompoundTag nbt)
		{
			super(MSStructures.ConsortVillage.VILLAGE_PATH_PIECE.get(), nbt, 0);
			this.length = nbt.getInt("Length");
		}
		
		@Override
		protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tagCompound)
		{
			super.addAdditionalSaveData(context, tagCompound);
			tagCompound.putInt("Length", this.length);
		}
		
		@Override
		public void addChildren(StructurePiece componentIn, StructurePieceAccessor accessor, RandomSource rand)
		{
			boolean flag = false;
			
			for (int i = rand.nextInt(5); i < this.length - 8; i += 2 + rand.nextInt(5))
			{
				StructurePiece newPiece = this.getNextComponentNN((ConsortVillageCenter.VillageCenter)componentIn, accessor, rand, 0, i);
				
				if (newPiece != null)
				{
					i += Math.max(newPiece.getBoundingBox().getXSpan(), newPiece.getBoundingBox().getZSpan());
					flag = true;
				}
			}
			
			for (int j = rand.nextInt(5); j < this.length - 8; j += 2 + rand.nextInt(5))
			{
				StructurePiece newPiece = this.getNextComponentPP((ConsortVillageCenter.VillageCenter) componentIn, accessor, rand, 0, j);
				
				if (newPiece != null)
				{
					j += Math.max(newPiece.getBoundingBox().getXSpan(), newPiece.getBoundingBox().getZSpan());
					flag = true;
				}
			}
			
			Direction direction = this.getOrientation();
			
			if (flag && rand.nextInt(3) > 0 && direction != null)
			{
				switch (direction)
				{
					case NORTH:
					default:
						ConsortVillagePieces.generateAndAddRoadPiece((ConsortVillageCenter.VillageCenter) componentIn, accessor, rand, this.boundingBox.minX() - 1, this.boundingBox.minY(), this.boundingBox.minZ(), Direction.WEST);
						break;
					case SOUTH:
						ConsortVillagePieces.generateAndAddRoadPiece((ConsortVillageCenter.VillageCenter) componentIn, accessor, rand, this.boundingBox.minX() - 1, this.boundingBox.minY(), this.boundingBox.maxZ() - 2, Direction.WEST);
						break;
					case WEST:
						ConsortVillagePieces.generateAndAddRoadPiece((ConsortVillageCenter.VillageCenter) componentIn, accessor, rand, this.boundingBox.minX(), this.boundingBox.minY(), this.boundingBox.minZ() - 1, Direction.NORTH);
						break;
					case EAST:
						ConsortVillagePieces.generateAndAddRoadPiece((ConsortVillageCenter.VillageCenter) componentIn, accessor, rand, this.boundingBox.maxX() - 2, this.boundingBox.minY(), this.boundingBox.minZ() - 1, Direction.NORTH);
				}
			}
			
			if (flag && rand.nextInt(3) > 0 && direction != null)
			{
				switch (direction)
				{
					case NORTH:
					default:
						ConsortVillagePieces.generateAndAddRoadPiece((ConsortVillageCenter.VillageCenter) componentIn, accessor, rand, this.boundingBox.maxX() + 1, this.boundingBox.minY(), this.boundingBox.minZ(), Direction.EAST);
						break;
					case SOUTH:
						ConsortVillagePieces.generateAndAddRoadPiece((ConsortVillageCenter.VillageCenter) componentIn, accessor, rand, this.boundingBox.maxX() + 1, this.boundingBox.minY(), this.boundingBox.maxZ() - 2, Direction.EAST);
						break;
					case WEST:
						ConsortVillagePieces.generateAndAddRoadPiece((ConsortVillageCenter.VillageCenter) componentIn, accessor, rand, this.boundingBox.minX(), this.boundingBox.minY(), this.boundingBox.maxZ() + 1, Direction.SOUTH);
						break;
					case EAST:
						ConsortVillagePieces.generateAndAddRoadPiece((ConsortVillageCenter.VillageCenter) componentIn, accessor, rand, this.boundingBox.maxX() - 2, this.boundingBox.minY(), this.boundingBox.maxZ() + 1, Direction.SOUTH);
				}
			}
		}

		@Override
		public void postProcess(WorldGenLevel level, StructureManager manager, ChunkGenerator chunkGeneratorIn, RandomSource randomIn, BoundingBox structureBoundingBoxIn, ChunkPos chunkPosIn, BlockPos pos)
		{
			StructureBlockRegistry blocks = StructureBlockRegistry.getOrDefault(chunkGeneratorIn);
			BlockState pathBlock = blocks.getBlockState("village_path");

			for (int i = this.boundingBox.minX(); i <= this.boundingBox.maxX(); ++i)
			{
				for (int j = this.boundingBox.minZ(); j <= this.boundingBox.maxZ(); ++j)
				{
					placeRoadtile(i, j, structureBoundingBoxIn, level, pathBlock);
				}
			}
		}

		public static BoundingBox findPieceBox(ConsortVillageCenter.VillageCenter start, StructurePieceAccessor accessor, RandomSource rand, int x, int y, int z, Direction facing)
		{
			for(int i = 7 * Mth.nextInt(rand, 3, 5); i >= 7; i -= 7)
			{
				BoundingBox boundingBox = BoundingBox.orientBox(x, y, z, 0, 0, 0, 2, 3, i, facing);
				
				if(accessor.findCollisionPiece(boundingBox) == null)
					return boundingBox;
			}
			
			return null;
		}
	}
	
	
	protected static StructurePiece generateAndAddRoadPiece(ConsortVillageCenter.VillageCenter start, StructurePieceAccessor accessor, RandomSource rand, int x, int y, int z, Direction direction)
	{
		if (Math.abs(x - start.getBoundingBox().minX()) <= 112 && Math.abs(z - start.getBoundingBox().minZ()) <= 112)
		{
			BoundingBox boundingBox = VillagePath.findPieceBox(start, accessor, rand, x, y, z, direction);
			
			if (boundingBox != null && boundingBox.minY() > 10)
			{
				StructurePiece structurePiece = new VillagePath(start, rand, boundingBox, direction);
				accessor.addPiece(structurePiece);
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
