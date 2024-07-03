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
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;

public class IguanaVillagePieces
{
	/**
	 * Helper function for adding village centers associated with iguanas.
	 */
	public static void addCenters(ILandType.CenterRegister register)
	{
	}
	
	/**
	 * Helper function for adding village pieces associated with iguanas.
	 */
	public static void addPieces(ILandType.PieceRegister register, RandomSource random)
	{
		register.add(SmallTent1::createPiece, 3, Mth.nextInt(random, 5, 8));
		register.add(LargeTent1::createPiece, 10, Mth.nextInt(random, 1, 2));
		register.add(SmallTentStore::createPiece, 8, Mth.nextInt(random, 2, 3));
	}
	
	public static class SmallTent1 extends ConsortVillagePieces.ConsortVillagePiece
	{
		private int woolType = 1;
		
		SmallTent1(ConsortVillageCenter.VillageCenter start, RandomSource rand, BoundingBox boundingBox, Direction facing)
		{
			super(MSStructures.ConsortVillage.SMALL_VILLAGE_TENT_1_PIECE.get(), 0, boundingBox, 1);
			setOrientation(facing);
			woolType = 1 + rand.nextInt(3);
		}
		
		public SmallTent1(CompoundTag nbt)
		{
			super(MSStructures.ConsortVillage.SMALL_VILLAGE_TENT_1_PIECE.get(), nbt, 1);
			this.woolType = nbt.getInt("Wool");
		}
		
		public static SmallTent1 createPiece(ConsortVillageCenter.VillageCenter start, StructurePieceAccessor accessor, RandomSource rand, int x, int y, int z, Direction facing)
		{
			BoundingBox boundingBox = BoundingBox.orientBox(x, y, z, 0, 0, 0, 9, 6, 6, facing);
			return accessor.findCollisionPiece(boundingBox) == null ? new SmallTent1(start, rand, boundingBox, facing) : null;
		}
		
		@Override
		protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tagCompound)
		{
			super.addAdditionalSaveData(context, tagCompound);
			tagCompound.putInt("Wool", woolType);
		}

		@Override
		public void postProcess(WorldGenLevel level, StructureManager manager, ChunkGenerator chunkGeneratorIn, RandomSource randomIn, BoundingBox boundingBox, ChunkPos chunkPosIn, BlockPos pos)
		{
			if (averageGroundLvl < 0)
			{
				averageGroundLvl = getAverageGroundLevel(level, chunkGeneratorIn, boundingBox);

				if (averageGroundLvl < 0)
				{
					return;
				}

				this.boundingBox.move(0, averageGroundLvl - this.boundingBox.minY() - 1, 0);
			}

			StructureBlockRegistry blocks = StructureBlockRegistry.getOrDefault(chunkGeneratorIn);
			BlockState fence = blocks.getBlockState("village_fence");
			BlockState surface = blocks.getBlockState("surface");
			BlockState dirt = Blocks.COARSE_DIRT.defaultBlockState();
			BlockState wool = blocks.getBlockState("structure_wool_"+woolType);

			//Floor
			generateAirBox(level, boundingBox, 1, 1, 1, 7, 6, 5);
			generateBox(level, boundingBox, 1, 0, 1, 7, 0, 5, surface, surface, false);
			for(int x = 1; x < 8; x++)
				for(int z = 1; z < 6; z++)
					if(randomIn.nextFloat() < 0.15f)
						placeBlock(level, dirt, x, 0, z,  boundingBox);

			//Remove blocks in front of the building
			clearFront(level, boundingBox, 1, 7, 1, 0);

			generateBox(level, boundingBox, 4, 1, 3, 4, 4, 3, fence, fence, false);

			generateBox(level, boundingBox, 0, 1, 1, 0, 1, 5, wool, wool, false);
			generateBox(level, boundingBox, 8, 1, 1, 8, 1, 5, wool, wool, false);
			generateBox(level, boundingBox, 1, 2, 1, 1, 2, 5, wool, wool, false);
			generateBox(level, boundingBox, 7, 2, 1, 7, 2, 5, wool, wool, false);
			generateBox(level, boundingBox, 2, 3, 1, 2, 3, 5, wool, wool, false);
			generateBox(level, boundingBox, 6, 3, 1, 6, 3, 5, wool, wool, false);
			generateBox(level, boundingBox, 3, 4, 1, 3, 4, 5, wool, wool, false);
			generateBox(level, boundingBox, 5, 4, 1, 5, 4, 5, wool, wool, false);
			generateBox(level, boundingBox, 4, 5, 1, 4, 5, 5, wool, wool, false);

			if(!spawns[0])
				spawns[0] = spawnConsort(3, 1, 3, boundingBox, level, chunkGeneratorIn);
		}
	}
	
	public static class LargeTent1 extends ConsortVillagePieces.ConsortVillagePiece
	{
		private int woolType = 1;
		
		LargeTent1(ConsortVillageCenter.VillageCenter start, RandomSource rand, BoundingBox boundingBox, Direction facing)
		{
			super(MSStructures.ConsortVillage.LARGE_VILLAGE_TENT_1_PIECE.get(), 0, boundingBox, 4);
			setOrientation(facing);
			woolType = 1 + rand.nextInt(3);
		}
		
		public LargeTent1(CompoundTag nbt)
		{
			super(MSStructures.ConsortVillage.LARGE_VILLAGE_TENT_1_PIECE.get(), nbt, 4);
			this.woolType = nbt.getInt("Wool");
		}
		
		public static LargeTent1 createPiece(ConsortVillageCenter.VillageCenter start, StructurePieceAccessor accessor, RandomSource rand, int x, int y, int z, Direction facing)
		{
			BoundingBox boundingBox = BoundingBox.orientBox(x, y, z, 0, 0, 0, 12, 8, 16, facing);
			return accessor.findCollisionPiece(boundingBox) == null ? new LargeTent1(start, rand, boundingBox, facing) : null;
		}
		
		@Override
		protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tagCompound)
		{
			super.addAdditionalSaveData(context, tagCompound);
			tagCompound.putInt("Wool", woolType);
		}

		@Override
		public void postProcess(WorldGenLevel level, StructureManager manager, ChunkGenerator chunkGeneratorIn, RandomSource randomIn, BoundingBox boundingBox, ChunkPos chunkPosIn, BlockPos pos)
		{
			if (this.averageGroundLvl < 0)
			{
				this.averageGroundLvl = this.getAverageGroundLevel(level, chunkGeneratorIn, boundingBox);

				if (this.averageGroundLvl < 0)
				{
					return;
				}

				this.boundingBox.move(0, this.averageGroundLvl - this.boundingBox.minY() - 1, 0);
			}

			StructureBlockRegistry blocks = StructureBlockRegistry.getOrDefault(chunkGeneratorIn);
			BlockState fence = blocks.getBlockState("village_fence");
			BlockState planks = blocks.getBlockState("structure_planks");
			BlockState surface = blocks.getBlockState("surface");
			BlockState dirt = Blocks.COARSE_DIRT.defaultBlockState();
			BlockState wool = blocks.getBlockState("structure_wool_"+woolType);
			BlockState torch = blocks.getBlockState("wall_torch");

			//Floor
			this.generateAirBox(level, boundingBox, 1, 1, 1, 10, 6, 15);
			this.generateBox(level, boundingBox, 1, 0, 1, 10, 0, 15, surface, surface, false);
			for(int x = 1; x < 11; x++)
				for(int z = 1; z < 16; z++)
					if(randomIn.nextFloat() < 0.15f)
						this.placeBlock(level, dirt, x, 0, z,  boundingBox);

			//Remove blocks in front of the building
			this.clearFront(level, boundingBox, 2, 9, 1, 0);

			this.generateBox(level, boundingBox, 1, 1, 2, 1, 4, 2, fence, fence, false);
			this.generateBox(level, boundingBox, 10, 1, 2, 10, 4, 2, fence, fence, false);
			this.generateBox(level, boundingBox, 1, 1, 14, 1, 4, 14, fence, fence, false);
			this.generateBox(level, boundingBox, 10, 1, 14, 10, 4, 14, fence, fence, false);

			this.generateBox(level, boundingBox, 2, 5, 6, 9, 5, 6, fence, fence, false);
			this.generateBox(level, boundingBox, 3, 6, 7, 8, 6, 7, fence, fence, false);
			this.generateBox(level, boundingBox, 3, 6, 9, 8, 6, 9, fence, fence, false);
			this.generateBox(level, boundingBox, 2, 5, 11, 9, 5, 11, fence, fence, false);

			this.generateBox(level, boundingBox, 1, 1, 1, 1, 4, 1, wool, wool, false);
			this.generateBox(level, boundingBox, 2, 2, 1, 2, 4, 1, wool, wool, false);
			this.generateBox(level, boundingBox, 3, 3, 1, 3, 4, 1, wool, wool, false);
			this.placeBlock(level, wool, 4, 4, 1, boundingBox);
			this.generateBox(level, boundingBox, 10, 1, 1, 10, 4, 1, wool, wool, false);
			this.generateBox(level, boundingBox, 9, 2, 1, 9, 4, 1, wool, wool, false);
			this.generateBox(level, boundingBox, 8, 3, 1, 8, 4, 1, wool, wool, false);
			this.placeBlock(level, wool, 7, 4, 1, boundingBox);
			this.generateBox(level, boundingBox, 1, 1, 15, 1, 4, 15, wool, wool, false);
			this.generateBox(level, boundingBox, 2, 2, 15, 2, 4, 15, wool, wool, false);
			this.generateBox(level, boundingBox, 3, 3, 15, 3, 4, 15, wool, wool, false);
			this.placeBlock(level, wool, 4, 4, 15, boundingBox);
			this.generateBox(level, boundingBox, 10, 1, 15, 10, 4, 15, wool, wool, false);
			this.generateBox(level, boundingBox, 9, 2, 15, 9, 4, 15, wool, wool, false);
			this.generateBox(level, boundingBox, 8, 3, 15, 8, 4, 15, wool, wool, false);
			this.placeBlock(level, wool, 7, 4, 15, boundingBox);

			this.generateBox(level, boundingBox, 0, 1, 2, 0, 4, 14, wool, wool, false);
			this.generateBox(level, boundingBox, 11, 1, 2, 11, 4, 14, wool, wool, false);

			this.generateBox(level, boundingBox, 1, 5, 2, 10, 5, 2, wool, wool, false);
			this.generateBox(level, boundingBox, 1, 5, 14, 10, 5, 14, wool, wool, false);
			this.generateBox(level, boundingBox, 1, 5, 3, 1, 5, 13, wool, wool, false);
			this.generateBox(level, boundingBox, 10, 5, 3, 10, 5, 13, wool, wool, false);
			this.generateBox(level, boundingBox, 2, 6, 3, 9, 6, 3, wool, wool, false);
			this.generateBox(level, boundingBox, 2, 6, 13, 9, 6, 13, wool, wool, false);
			this.generateBox(level, boundingBox, 2, 6, 4, 2, 6, 12, wool, wool, false);
			this.generateBox(level, boundingBox, 9, 6, 4, 9, 6, 12, wool, wool, false);
			this.generateBox(level, boundingBox, 3, 7, 4, 8, 7, 12, wool, wool, false);

			this.generateBox(level, boundingBox, 1, 1, 3, 1, 1, 13, planks, planks, false);
			this.generateBox(level, boundingBox, 10, 1, 3, 10, 1, 13, planks, planks, false);

			this.placeBlock(level, torch.setValue(WallTorchBlock.FACING, Direction.EAST), 1, 3, 5, boundingBox);
			this.placeBlock(level, torch.setValue(WallTorchBlock.FACING, Direction.EAST), 1, 3, 11, boundingBox);
			this.placeBlock(level, torch.setValue(WallTorchBlock.FACING, Direction.WEST), 10, 3, 5, boundingBox);
			this.placeBlock(level, torch.setValue(WallTorchBlock.FACING, Direction.WEST), 10, 3, 11, boundingBox);

			if(!spawns[0])
				spawns[0] = spawnConsort(2, 1, 5, boundingBox, level, chunkGeneratorIn);
			if(!spawns[1])
				spawns[1] = spawnConsort(2, 1, 11, boundingBox, level, chunkGeneratorIn);
			if(!spawns[2])
				spawns[2] = spawnConsort(9, 1, 5, boundingBox, level, chunkGeneratorIn);
			if(!spawns[3])
				spawns[3] = spawnConsort(9, 1, 11, boundingBox, level, chunkGeneratorIn);
		}
	}
	
	public static class SmallTentStore extends ConsortVillagePieces.ConsortVillagePiece
	{
		private int woolType = 1;
		
		SmallTentStore(ConsortVillageCenter.VillageCenter start, RandomSource rand, BoundingBox boundingBox, Direction facing)
		{
			super(MSStructures.ConsortVillage.SMALL_TENT_STORE_PIECE.get(), 0, boundingBox, 1);
			setOrientation(facing);
			woolType = 1 + rand.nextInt(3);
		}
		
		public SmallTentStore(CompoundTag nbt)
		{
			super(MSStructures.ConsortVillage.SMALL_TENT_STORE_PIECE.get(), nbt, 1);
			this.woolType = nbt.getInt("Wool");
		}
		
		public static SmallTentStore createPiece(ConsortVillageCenter.VillageCenter start, StructurePieceAccessor accessor, RandomSource rand, int x, int y, int z, Direction facing)
		{
			BoundingBox boundingBox = BoundingBox.orientBox(x, y, z, 0, 0, 0, 7, 7, 7, facing);
			return accessor.findCollisionPiece(boundingBox) == null ? new SmallTentStore(start, rand, boundingBox, facing) : null;
		}
		
		@Override
		protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tagCompound)
		{
			super.addAdditionalSaveData(context, tagCompound);
			tagCompound.putInt("Wool", this.woolType);
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
			BlockState fence = blocks.getBlockState("village_fence");
			BlockState planks = blocks.getBlockState("structure_planks");
			BlockState plankSlab = blocks.getBlockState("structure_planks_slab");
			BlockState surface = blocks.getBlockState("surface");
			BlockState dirt = Blocks.COARSE_DIRT.defaultBlockState();
			BlockState wool = blocks.getBlockState("structure_wool_"+woolType);
			BlockState torch = blocks.getBlockState("wall_torch");

			//Floor
			this.generateAirBox(level, structureBoundingBoxIn, 1, 1, 1, 5, 5, 5);
			this.generateBox(level, structureBoundingBoxIn, 1, 0, 1, 5, 0, 5, surface, surface, false);
			for(int x = 1; x < 6; x++)
				for(int z = 1; z < 6; z++)
					if(randomIn.nextFloat() < 0.15f)
						this.placeBlock(level, dirt, x, 0, z,  structureBoundingBoxIn);

			//Remove blocks in front of the building
			this.clearFront(level, structureBoundingBoxIn, 1, 5, 1, 0);

			this.generateBox(level, structureBoundingBoxIn, 1, 1, 1, 1, 3, 1, fence, fence, false);
			this.generateBox(level, structureBoundingBoxIn, 1, 1, 5, 1, 3, 5, fence, fence, false);
			this.generateBox(level, structureBoundingBoxIn, 5, 1, 1, 5, 3, 1, fence, fence, false);
			this.generateBox(level, structureBoundingBoxIn, 5, 1, 5, 5, 3, 5, fence, fence, false);

			this.generateBox(level, structureBoundingBoxIn, 2, 1, 1, 4, 1, 1, planks, planks, false);
			placeBlock(level, plankSlab, 3, 1, 2, structureBoundingBoxIn);

			this.generateBox(level, structureBoundingBoxIn, 0, 1, 1, 0, 3, 5, wool, wool, false);
			this.generateBox(level, structureBoundingBoxIn, 6, 1, 1, 6, 3, 5, wool, wool, false);
			this.generateBox(level, structureBoundingBoxIn, 1, 1, 6, 5, 3, 6, wool, wool, false);

			this.generateBox(level, structureBoundingBoxIn, 1, 4, 1, 5, 4, 1, wool, wool, false);
			this.generateBox(level, structureBoundingBoxIn, 1, 4, 5, 5, 4, 5, wool, wool, false);
			this.generateBox(level, structureBoundingBoxIn, 1, 4, 2, 1, 4, 4, wool, wool, false);
			this.generateBox(level, structureBoundingBoxIn, 5, 4, 2, 5, 4, 4, wool, wool, false);

			generateBox(level, structureBoundingBoxIn, 2, 5, 2, 2, 5, 4, wool, wool, false);
			generateBox(level, structureBoundingBoxIn, 4, 5, 2, 4, 5, 4, wool, wool, false);
			placeBlock(level, wool, 3, 5, 2, structureBoundingBoxIn);
			placeBlock(level, wool, 3, 6, 3, structureBoundingBoxIn);
			placeBlock(level, wool, 3, 5, 4, structureBoundingBoxIn);

			placeBlock(level, torch.setValue(WallTorchBlock.FACING, Direction.EAST), 1, 2, 4, structureBoundingBoxIn);
			placeBlock(level, torch.setValue(WallTorchBlock.FACING, Direction.WEST), 5, 2, 4, structureBoundingBoxIn);

			if(!spawns[0])
				spawns[0] = spawnConsort(3, 2, 2, structureBoundingBoxIn, level, chunkGeneratorIn, EnumConsort.getRandomMerchant(randomIn), 1);
		}
	}
}
