package com.mraof.minestuck.world.gen.feature.structure.village;

import com.google.common.collect.Lists;
import com.mraof.minestuck.entity.consort.ConsortEntity;
import com.mraof.minestuck.entity.consort.EnumConsort;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.world.gen.feature.MSStructurePieces;
import com.mraof.minestuck.world.gen.feature.structure.ImprovedStructurePiece;
import com.mraof.minestuck.world.gen.feature.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.lands.ILandType;
import com.mraof.minestuck.world.lands.LandTypePair;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LadderBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraftforge.common.util.Constants;

import java.util.List;
import java.util.Random;

public class ConsortVillagePieces
{
	public static List<PieceWeight> getStructureVillageWeightedPieceList(Random random, LandTypePair landTypes)
	{
		List<PieceWeight> list = Lists.newArrayList();
		
		ILandType.PieceRegister register = (factory, weight, limit) -> list.add(new PieceWeight(factory, weight, limit));
		landTypes.getTerrain().addVillagePieces(register, random);
		landTypes.getTitle().addVillagePieces(register, random);
		
		list.removeIf(pieceWeight -> pieceWeight.villagePiecesLimit == 0);
		
		return list;
	}
	
	//TODO make sure that components don't generate near the ocean
	private static StructurePiece generateAndAddComponent(ConsortVillageCenter.VillageCenter start, List<StructurePiece> structurePieces, Random rand, int structureMinX, int structureMinY, int structureMinZ, Direction facing)
	{
		if (Math.abs(structureMinX - start.getBoundingBox().x0) <= 112 && Math.abs(structureMinZ - start.getBoundingBox().z0) <= 112)
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
		protected void addAdditionalSaveData(CompoundNBT tagCompound)
		{
			tagCompound.putInt("HPos", this.averageGroundLvl);
			
			for(int i = 0; i < spawns.length; i++)
				tagCompound.putBoolean("spawn"+i, spawns[i]);
		}
		
		protected StructurePiece getNextComponentNN(ConsortVillageCenter.VillageCenter start, List<StructurePiece> structurePieces, Random rand, int offsetY, int offsetXZ)
		{
			Direction direction = this.getOrientation();
			
			if (direction != null)
			{
				switch (direction)
				{
					case NORTH:
					default:
						return ConsortVillagePieces.generateAndAddComponent(start, structurePieces, rand, this.boundingBox.x0 - 1, this.boundingBox.y0 + offsetY, this.boundingBox.z0 + offsetXZ, Direction.WEST);
					case SOUTH:
						return ConsortVillagePieces.generateAndAddComponent(start, structurePieces, rand, this.boundingBox.x0 - 1, this.boundingBox.y0 + offsetY, this.boundingBox.z0 + offsetXZ, Direction.WEST);
					case WEST:
						return ConsortVillagePieces.generateAndAddComponent(start, structurePieces, rand, this.boundingBox.x0 + offsetXZ, this.boundingBox.y0 + offsetY, this.boundingBox.z0 - 1, Direction.NORTH);
					case EAST:
						return ConsortVillagePieces.generateAndAddComponent(start, structurePieces, rand, this.boundingBox.x0 + offsetXZ, this.boundingBox.y0 + offsetY, this.boundingBox.z0 - 1, Direction.NORTH);
				}
			}
			else
			{
				return null;
			}
		}
		
		protected StructurePiece getNextComponentPP(ConsortVillageCenter.VillageCenter start, List<StructurePiece> structurePieces, Random rand, int offsetY, int offsetXZ)
		{
			Direction direction = this.getOrientation();
			
			if (direction != null)
			{
				switch (direction)
				{
					case NORTH:
					default:
						return ConsortVillagePieces.generateAndAddComponent(start, structurePieces, rand, this.boundingBox.x1 + 1, this.boundingBox.y0 + offsetY, this.boundingBox.z0 + offsetXZ, Direction.EAST);
					case SOUTH:
						return ConsortVillagePieces.generateAndAddComponent(start, structurePieces, rand, this.boundingBox.x1 + 1, this.boundingBox.y0 + offsetY, this.boundingBox.z0 + offsetXZ, Direction.EAST);
					case WEST:
						return ConsortVillagePieces.generateAndAddComponent(start, structurePieces, rand, this.boundingBox.x0 + offsetXZ, this.boundingBox.y0 + offsetY, this.boundingBox.z1 + 1, Direction.SOUTH);
					case EAST:
						return ConsortVillagePieces.generateAndAddComponent(start, structurePieces, rand, this.boundingBox.x0 + offsetXZ, this.boundingBox.y0 + offsetY, this.boundingBox.z1 + 1, Direction.SOUTH);
				}
			}
			else
			{
				return null;
			}
		}
		
		protected void clearFront(ISeedReader world, MutableBoundingBox structureBB, int minX, int maxX, int y, int z)
		{
			for (int x = minX; x <= maxX; x++)
				if (structureBB.isInside(new Vector3i(this.getWorldX(x, z), this.getWorldY(y), this.getWorldZ(x, z))))
				{
					this.generateAirBox(world, structureBB, x, y, z, x, y + 4, z);
					BlockPos pos = new BlockPos(this.getWorldX(x, z - 1), this.getWorldY(y), this.getWorldZ(x, z - 1));
					int i = 0;
					for (int yOffset = 0; yOffset <= 4; yOffset++)
					{
						if (world.getBlockState(pos.above(yOffset)).canOcclude())
							i++;
						else break;
					}
					BlockState ladder = Blocks.LADDER.defaultBlockState().setValue(LadderBlock.FACING, Direction.SOUTH);
					if (i >= 2)
						this.generateBox(world, structureBB, x, y, z, x, y + i - 1, z, ladder, ladder, false);
				}
		}
		
		protected void placeRoadtile(int x, int z, MutableBoundingBox boundingBox, IWorld worldIn, BlockState pathBlock)
		{
			BlockPos blockpos = new BlockPos(x, 64, z);
			
			if (boundingBox.isInside(blockpos))
			{
				blockpos = worldIn.getHeightmapPos(Heightmap.Type.WORLD_SURFACE_WG, blockpos).below();
				
				if (blockpos.getY() < worldIn.getSeaLevel())
				{
					blockpos = new BlockPos(blockpos.getX(), worldIn.getSeaLevel() - 1, blockpos.getZ());
				}
				
				while (blockpos.getY() >= worldIn.getSeaLevel() - 1)
				{
					BlockState state = worldIn.getBlockState(blockpos);
					
					if (state.getMaterial().isLiquid() || state.canOcclude())
					{
						worldIn.setBlock(blockpos, pathBlock, Constants.BlockFlags.BLOCK_UPDATE);
						break;
					}
					
					blockpos = blockpos.below();
				}
			}
		}
		
		protected void blockPillar(int x, int y, int z, MutableBoundingBox boundingBox, IWorld world, BlockState block)
		{
			BlockPos pos = new BlockPos(this.getWorldX(x, z), this.getWorldY(y), this.getWorldZ(x, z));
			
			if(!boundingBox.isInside(pos))
				return;
			
			while(pos.getY() >= world.getSeaLevel())
			{
				world.setBlock(pos, block, Constants.BlockFlags.BLOCK_UPDATE);
				
				pos = pos.below();
				
				if(world.getBlockState(pos).canOcclude())
					break;
			}
		}
		
		protected boolean spawnConsort(int x, int y, int z, MutableBoundingBox boundingBox, ISeedReader world, ChunkGenerator chunkGenerator)
		{
			return spawnConsort(x, y, z, boundingBox, world, chunkGenerator, EnumConsort.MerchantType.NONE, 48);
		}
		
		protected boolean spawnConsort(int x, int y, int z, MutableBoundingBox boundingBox, ISeedReader world, ChunkGenerator chunkGenerator, int maxHomeDistance)
		{
			return spawnConsort(x, y, z, boundingBox, world, chunkGenerator, EnumConsort.MerchantType.NONE, maxHomeDistance);
		}
		
		protected boolean spawnConsort(int x, int y, int z, MutableBoundingBox boundingBox, ISeedReader world, ChunkGenerator chunkGenerator, EnumConsort.MerchantType type, int maxHomeDistance)
		{
			BlockPos pos = new BlockPos(this.getWorldX(x, z), this.getWorldY(y), this.getWorldZ(x, z));
			
			if(boundingBox.isInside(pos))
			{
				LandTypePair landTypes = LandTypePair.getTypes(chunkGenerator);
				
				EntityType<? extends ConsortEntity> consortType = landTypes.getTerrain().getConsortType();
				
				try
				{
					ConsortEntity consort = consortType.create(world.getLevel());
					if(consort == null)
					{
						Debug.warnf("Unable to create consort entity %s from a world.", consortType);
						return false;
					}
					
					consort.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
					
					consort.merchantType = type;
					consort.restrictTo(pos, maxHomeDistance);
					
					consort.finalizeSpawn(world, world.getCurrentDifficultyAt(pos), SpawnReason.STRUCTURE, null, null);
					
					world.addFreshEntity(consort);
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
			this.setOrientation(facing);
			this.boundingBox = boundingBox;
			this.length = Math.max(boundingBox.getXSpan(), boundingBox.getZSpan());
		}
		
		public VillagePath(TemplateManager templates, CompoundNBT nbt)
		{
			super(MSStructurePieces.VILLAGE_PATH, nbt, 0);
			this.length = nbt.getInt("Length");
		}
		
		@Override
		protected void addAdditionalSaveData(CompoundNBT tagCompound)
		{
			super.addAdditionalSaveData(tagCompound);
			tagCompound.putInt("Length", this.length);
		}
		
		@Override
		public void addChildren(StructurePiece componentIn, List<StructurePiece> listIn, Random rand)
		{
			boolean flag = false;
			
			for (int i = rand.nextInt(5); i < this.length - 8; i += 2 + rand.nextInt(5))
			{
				StructurePiece newPiece = this.getNextComponentNN((ConsortVillageCenter.VillageCenter)componentIn, listIn, rand, 0, i);
				
				if (newPiece != null)
				{
					i += Math.max(newPiece.getBoundingBox().getXSpan(), newPiece.getBoundingBox().getZSpan());
					flag = true;
				}
			}
			
			for (int j = rand.nextInt(5); j < this.length - 8; j += 2 + rand.nextInt(5))
			{
				StructurePiece newPiece = this.getNextComponentPP((ConsortVillageCenter.VillageCenter) componentIn, listIn, rand, 0, j);
				
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
						ConsortVillagePieces.generateAndAddRoadPiece((ConsortVillageCenter.VillageCenter) componentIn, listIn, rand, this.boundingBox.x0 - 1, this.boundingBox.y0, this.boundingBox.z0, Direction.WEST);
						break;
					case SOUTH:
						ConsortVillagePieces.generateAndAddRoadPiece((ConsortVillageCenter.VillageCenter) componentIn, listIn, rand, this.boundingBox.x0 - 1, this.boundingBox.y0, this.boundingBox.z1 - 2, Direction.WEST);
						break;
					case WEST:
						ConsortVillagePieces.generateAndAddRoadPiece((ConsortVillageCenter.VillageCenter) componentIn, listIn, rand, this.boundingBox.x0, this.boundingBox.y0, this.boundingBox.z0 - 1, Direction.NORTH);
						break;
					case EAST:
						ConsortVillagePieces.generateAndAddRoadPiece((ConsortVillageCenter.VillageCenter) componentIn, listIn, rand, this.boundingBox.x1 - 2, this.boundingBox.y0, this.boundingBox.z0 - 1, Direction.NORTH);
				}
			}
			
			if (flag && rand.nextInt(3) > 0 && direction != null)
			{
				switch (direction)
				{
					case NORTH:
					default:
						ConsortVillagePieces.generateAndAddRoadPiece((ConsortVillageCenter.VillageCenter) componentIn, listIn, rand, this.boundingBox.x1 + 1, this.boundingBox.y0, this.boundingBox.z0, Direction.EAST);
						break;
					case SOUTH:
						ConsortVillagePieces.generateAndAddRoadPiece((ConsortVillageCenter.VillageCenter) componentIn, listIn, rand, this.boundingBox.x1 + 1, this.boundingBox.y0, this.boundingBox.z1 - 2, Direction.EAST);
						break;
					case WEST:
						ConsortVillagePieces.generateAndAddRoadPiece((ConsortVillageCenter.VillageCenter) componentIn, listIn, rand, this.boundingBox.x0, this.boundingBox.y0, this.boundingBox.z1 + 1, Direction.SOUTH);
						break;
					case EAST:
						ConsortVillagePieces.generateAndAddRoadPiece((ConsortVillageCenter.VillageCenter) componentIn, listIn, rand, this.boundingBox.x1 - 2, this.boundingBox.y0, this.boundingBox.z1 + 1, Direction.SOUTH);
				}
			}
		}

		@Override
		public boolean postProcess(ISeedReader worldIn, StructureManager manager, ChunkGenerator chunkGeneratorIn, Random randomIn, MutableBoundingBox structureBoundingBoxIn, ChunkPos chunkPosIn, BlockPos pos)
		{
			StructureBlockRegistry blocks = StructureBlockRegistry.getOrDefault(chunkGeneratorIn);
			BlockState pathBlock = blocks.getBlockState("village_path");

			for (int i = this.boundingBox.x0; i <= this.boundingBox.x1; ++i)
			{
				for (int j = this.boundingBox.z0; j <= this.boundingBox.z1; ++j)
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
				MutableBoundingBox mutableBoundingBox = MutableBoundingBox.orientBox(x, y, z, 0, 0, 0, 2, 3, i, facing);
				
				if(StructurePiece.findCollisionPiece(components, mutableBoundingBox) == null)
					return mutableBoundingBox;
			}
			
			return null;
		}
	}
	
	
	protected static StructurePiece generateAndAddRoadPiece(ConsortVillageCenter.VillageCenter start, List<StructurePiece> componentList, Random rand, int x, int y, int z, Direction direction)
	{
		if (Math.abs(x - start.getBoundingBox().x0) <= 112 && Math.abs(z - start.getBoundingBox().z0) <= 112)
		{
			MutableBoundingBox mutableBoundingBox = VillagePath.findPieceBox(start, componentList, rand, x, y, z, direction);
			
			if (mutableBoundingBox != null && mutableBoundingBox.y0 > 10)
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