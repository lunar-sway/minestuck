package com.mraof.minestuck.world.gen.feature.structure.village;

import com.mraof.minestuck.entity.consort.EnumConsort;
import com.mraof.minestuck.world.gen.feature.MSStructurePieces;
import com.mraof.minestuck.world.gen.feature.structure.blocks.StructureBlockRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.WallTorchBlock;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.List;
import java.util.Random;

public class IguanaVillagePieces
{
	public static class SmallTent1 extends ConsortVillagePieces.ConsortVillagePiece
	{
		private int woolType = 1;
		
		SmallTent1(ConsortVillageCenter.VillageCenter start, Random rand, MutableBoundingBox boundingBox, Direction facing)
		{
			super(MSStructurePieces.SMALL_VILLAGE_TENT_1, 0, 1);
			setOrientation(facing);
			this.boundingBox = boundingBox;
			woolType = 1 + rand.nextInt(3);
		}
		
		public SmallTent1(TemplateManager templates, CompoundNBT nbt)
		{
			super(MSStructurePieces.SMALL_VILLAGE_TENT_1, nbt, 1);
			this.woolType = nbt.getInt("Wool");
		}
		
		public static SmallTent1 createPiece(ConsortVillageCenter.VillageCenter start, List<StructurePiece> componentList, Random rand, int x, int y, int z, Direction facing)
		{
			MutableBoundingBox structureboundingbox = MutableBoundingBox.orientBox(x, y, z, 0, 0, 0, 9, 6, 6, facing);
			return StructurePiece.findCollisionPiece(componentList, structureboundingbox) == null ? new SmallTent1(start, rand, structureboundingbox, facing) : null;
		}
		
		@Override
		protected void addAdditionalSaveData(CompoundNBT tagCompound)
		{
			super.addAdditionalSaveData(tagCompound);
			tagCompound.putInt("Wool", woolType);
		}

		@Override
		public boolean postProcess(ISeedReader worldIn, StructureManager manager, ChunkGenerator chunkGeneratorIn, Random randomIn, MutableBoundingBox structureBoundingBoxIn, ChunkPos chunkPosIn, BlockPos pos)
		{
			if (averageGroundLvl < 0)
			{
				averageGroundLvl = getAverageGroundLevel(worldIn, chunkGeneratorIn, structureBoundingBoxIn);

				if (averageGroundLvl < 0)
				{
					return true;
				}

				boundingBox.move(0, averageGroundLvl - boundingBox.y0 - 1, 0);
			}

			StructureBlockRegistry blocks = StructureBlockRegistry.getOrDefault(chunkGeneratorIn);
			BlockState fence = blocks.getBlockState("village_fence");
			BlockState surface = blocks.getBlockState("surface");
			BlockState dirt = Blocks.COARSE_DIRT.defaultBlockState();
			BlockState wool = blocks.getBlockState("structure_wool_"+woolType);

			//Floor
			generateAirBox(worldIn, structureBoundingBoxIn, 1, 1, 1, 7, 6, 5);
			generateBox(worldIn, structureBoundingBoxIn, 1, 0, 1, 7, 0, 5, surface, surface, false);
			for(int x = 1; x < 8; x++)
				for(int z = 1; z < 6; z++)
					if(randomIn.nextFloat() < 0.15f)
						placeBlock(worldIn, dirt, x, 0, z,  structureBoundingBoxIn);

			//Remove blocks in front of the building
			clearFront(worldIn, structureBoundingBoxIn, 1, 7, 1, 0);

			generateBox(worldIn, structureBoundingBoxIn, 4, 1, 3, 4, 4, 3, fence, fence, false);

			generateBox(worldIn, structureBoundingBoxIn, 0, 1, 1, 0, 1, 5, wool, wool, false);
			generateBox(worldIn, structureBoundingBoxIn, 8, 1, 1, 8, 1, 5, wool, wool, false);
			generateBox(worldIn, structureBoundingBoxIn, 1, 2, 1, 1, 2, 5, wool, wool, false);
			generateBox(worldIn, structureBoundingBoxIn, 7, 2, 1, 7, 2, 5, wool, wool, false);
			generateBox(worldIn, structureBoundingBoxIn, 2, 3, 1, 2, 3, 5, wool, wool, false);
			generateBox(worldIn, structureBoundingBoxIn, 6, 3, 1, 6, 3, 5, wool, wool, false);
			generateBox(worldIn, structureBoundingBoxIn, 3, 4, 1, 3, 4, 5, wool, wool, false);
			generateBox(worldIn, structureBoundingBoxIn, 5, 4, 1, 5, 4, 5, wool, wool, false);
			generateBox(worldIn, structureBoundingBoxIn, 4, 5, 1, 4, 5, 5, wool, wool, false);

			if(!spawns[0])
				spawns[0] = spawnConsort(3, 1, 3, structureBoundingBoxIn, worldIn, chunkGeneratorIn);

			return true;
		}
	}
	
	public static class LargeTent1 extends ConsortVillagePieces.ConsortVillagePiece
	{
		private int woolType = 1;
		
		LargeTent1(ConsortVillageCenter.VillageCenter start, Random rand, MutableBoundingBox boundingBox, Direction facing)
		{
			super(MSStructurePieces.LARGE_VILLAGE_TENT_1, 0, 4);
			setOrientation(facing);
			this.boundingBox = boundingBox;
			woolType = 1 + rand.nextInt(3);
		}
		
		public LargeTent1(TemplateManager templates, CompoundNBT nbt)
		{
			super(MSStructurePieces.LARGE_VILLAGE_TENT_1, nbt, 4);
			this.woolType = nbt.getInt("Wool");
		}
		
		public static LargeTent1 createPiece(ConsortVillageCenter.VillageCenter start, List<StructurePiece> componentList, Random rand, int x, int y, int z, Direction facing)
		{
			MutableBoundingBox structureboundingbox = MutableBoundingBox.orientBox(x, y, z, 0, 0, 0, 12, 8, 16, facing);
			return StructurePiece.findCollisionPiece(componentList, structureboundingbox) == null ? new LargeTent1(start, rand, structureboundingbox, facing) : null;
		}
		
		@Override
		protected void addAdditionalSaveData(CompoundNBT tagCompound)
		{
			super.addAdditionalSaveData(tagCompound);
			tagCompound.putInt("Wool", woolType);
		}

		@Override
		public boolean postProcess(ISeedReader worldIn, StructureManager manager, ChunkGenerator chunkGeneratorIn, Random randomIn, MutableBoundingBox structureBoundingBoxIn, ChunkPos chunkPosIn, BlockPos pos)
		{
			if (this.averageGroundLvl < 0)
			{
				this.averageGroundLvl = this.getAverageGroundLevel(worldIn, chunkGeneratorIn, structureBoundingBoxIn);

				if (this.averageGroundLvl < 0)
				{
					return true;
				}

				this.boundingBox.move(0, this.averageGroundLvl - this.boundingBox.y0 - 1, 0);
			}

			StructureBlockRegistry blocks = StructureBlockRegistry.getOrDefault(chunkGeneratorIn);
			BlockState fence = blocks.getBlockState("village_fence");
			BlockState planks = blocks.getBlockState("structure_planks");
			BlockState surface = blocks.getBlockState("surface");
			BlockState dirt = Blocks.COARSE_DIRT.defaultBlockState();
			BlockState wool = blocks.getBlockState("structure_wool_"+woolType);
			BlockState torch = blocks.getBlockState("wall_torch");

			//Floor
			this.generateAirBox(worldIn, structureBoundingBoxIn, 1, 1, 1, 10, 6, 15);
			this.generateBox(worldIn, structureBoundingBoxIn, 1, 0, 1, 10, 0, 15, surface, surface, false);
			for(int x = 1; x < 11; x++)
				for(int z = 1; z < 16; z++)
					if(randomIn.nextFloat() < 0.15f)
						this.placeBlock(worldIn, dirt, x, 0, z,  structureBoundingBoxIn);

			//Remove blocks in front of the building
			this.clearFront(worldIn, structureBoundingBoxIn, 2, 9, 1, 0);

			this.generateBox(worldIn, structureBoundingBoxIn, 1, 1, 2, 1, 4, 2, fence, fence, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 10, 1, 2, 10, 4, 2, fence, fence, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 1, 1, 14, 1, 4, 14, fence, fence, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 10, 1, 14, 10, 4, 14, fence, fence, false);

			this.generateBox(worldIn, structureBoundingBoxIn, 2, 5, 6, 9, 5, 6, fence, fence, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 3, 6, 7, 8, 6, 7, fence, fence, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 3, 6, 9, 8, 6, 9, fence, fence, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 2, 5, 11, 9, 5, 11, fence, fence, false);

			this.generateBox(worldIn, structureBoundingBoxIn, 1, 1, 1, 1, 4, 1, wool, wool, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 2, 2, 1, 2, 4, 1, wool, wool, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 3, 3, 1, 3, 4, 1, wool, wool, false);
			this.placeBlock(worldIn, wool, 4, 4, 1, structureBoundingBoxIn);
			this.generateBox(worldIn, structureBoundingBoxIn, 10, 1, 1, 10, 4, 1, wool, wool, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 9, 2, 1, 9, 4, 1, wool, wool, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 8, 3, 1, 8, 4, 1, wool, wool, false);
			this.placeBlock(worldIn, wool, 7, 4, 1, structureBoundingBoxIn);
			this.generateBox(worldIn, structureBoundingBoxIn, 1, 1, 15, 1, 4, 15, wool, wool, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 2, 2, 15, 2, 4, 15, wool, wool, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 3, 3, 15, 3, 4, 15, wool, wool, false);
			this.placeBlock(worldIn, wool, 4, 4, 15, structureBoundingBoxIn);
			this.generateBox(worldIn, structureBoundingBoxIn, 10, 1, 15, 10, 4, 15, wool, wool, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 9, 2, 15, 9, 4, 15, wool, wool, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 8, 3, 15, 8, 4, 15, wool, wool, false);
			this.placeBlock(worldIn, wool, 7, 4, 15, structureBoundingBoxIn);

			this.generateBox(worldIn, structureBoundingBoxIn, 0, 1, 2, 0, 4, 14, wool, wool, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 11, 1, 2, 11, 4, 14, wool, wool, false);

			this.generateBox(worldIn, structureBoundingBoxIn, 1, 5, 2, 10, 5, 2, wool, wool, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 1, 5, 14, 10, 5, 14, wool, wool, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 1, 5, 3, 1, 5, 13, wool, wool, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 10, 5, 3, 10, 5, 13, wool, wool, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 2, 6, 3, 9, 6, 3, wool, wool, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 2, 6, 13, 9, 6, 13, wool, wool, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 2, 6, 4, 2, 6, 12, wool, wool, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 9, 6, 4, 9, 6, 12, wool, wool, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 3, 7, 4, 8, 7, 12, wool, wool, false);

			this.generateBox(worldIn, structureBoundingBoxIn, 1, 1, 3, 1, 1, 13, planks, planks, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 10, 1, 3, 10, 1, 13, planks, planks, false);

			this.placeBlock(worldIn, torch.setValue(WallTorchBlock.FACING, Direction.EAST), 1, 3, 5, structureBoundingBoxIn);
			this.placeBlock(worldIn, torch.setValue(WallTorchBlock.FACING, Direction.EAST), 1, 3, 11, structureBoundingBoxIn);
			this.placeBlock(worldIn, torch.setValue(WallTorchBlock.FACING, Direction.WEST), 10, 3, 5, structureBoundingBoxIn);
			this.placeBlock(worldIn, torch.setValue(WallTorchBlock.FACING, Direction.WEST), 10, 3, 11, structureBoundingBoxIn);

			if(!spawns[0])
				spawns[0] = spawnConsort(2, 1, 5, structureBoundingBoxIn, worldIn, chunkGeneratorIn);
			if(!spawns[1])
				spawns[1] = spawnConsort(2, 1, 11, structureBoundingBoxIn, worldIn, chunkGeneratorIn);
			if(!spawns[2])
				spawns[2] = spawnConsort(9, 1, 5, structureBoundingBoxIn, worldIn, chunkGeneratorIn);
			if(!spawns[3])
				spawns[3] = spawnConsort(9, 1, 11, structureBoundingBoxIn, worldIn, chunkGeneratorIn);

			return true;
		}
	}
	
	public static class SmallTentStore extends ConsortVillagePieces.ConsortVillagePiece
	{
		private int woolType = 1;
		
		SmallTentStore(ConsortVillageCenter.VillageCenter start, Random rand, MutableBoundingBox boundingBox, Direction facing)
		{
			super(MSStructurePieces.SMALL_TENT_STORE, 0, 1);
			setOrientation(facing);
			this.boundingBox = boundingBox;
			woolType = 1 + rand.nextInt(3);
		}
		
		public SmallTentStore(TemplateManager templates, CompoundNBT nbt)
		{
			super(MSStructurePieces.SMALL_TENT_STORE, nbt, 1);
			this.woolType = nbt.getInt("Wool");
		}
		
		public static SmallTentStore createPiece(ConsortVillageCenter.VillageCenter start, List<StructurePiece> componentList, Random rand, int x, int y, int z, Direction facing)
		{
			MutableBoundingBox structureboundingbox = MutableBoundingBox.orientBox(x, y, z, 0, 0, 0, 7, 7, 7, facing);
			return StructurePiece.findCollisionPiece(componentList, structureboundingbox) == null ? new SmallTentStore(start, rand, structureboundingbox, facing) : null;
		}
		
		@Override
		protected void addAdditionalSaveData(CompoundNBT tagCompound)
		{
			super.addAdditionalSaveData(tagCompound);
			tagCompound.putInt("Wool", this.woolType);
		}

		@Override
		public boolean postProcess(ISeedReader worldIn, StructureManager manager, ChunkGenerator chunkGeneratorIn, Random randomIn, MutableBoundingBox structureBoundingBoxIn, ChunkPos chunkPosIn, BlockPos pos)
		{
			if (this.averageGroundLvl < 0)
			{
				this.averageGroundLvl = this.getAverageGroundLevel(worldIn, chunkGeneratorIn, structureBoundingBoxIn);

				if (this.averageGroundLvl < 0)
				{
					return true;
				}

				this.boundingBox.move(0, this.averageGroundLvl - this.boundingBox.y0 - 1, 0);
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
			this.generateAirBox(worldIn, structureBoundingBoxIn, 1, 1, 1, 5, 5, 5);
			this.generateBox(worldIn, structureBoundingBoxIn, 1, 0, 1, 5, 0, 5, surface, surface, false);
			for(int x = 1; x < 6; x++)
				for(int z = 1; z < 6; z++)
					if(randomIn.nextFloat() < 0.15f)
						this.placeBlock(worldIn, dirt, x, 0, z,  structureBoundingBoxIn);

			//Remove blocks in front of the building
			this.clearFront(worldIn, structureBoundingBoxIn, 1, 5, 1, 0);

			this.generateBox(worldIn, structureBoundingBoxIn, 1, 1, 1, 1, 3, 1, fence, fence, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 1, 1, 5, 1, 3, 5, fence, fence, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 5, 1, 1, 5, 3, 1, fence, fence, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 5, 1, 5, 5, 3, 5, fence, fence, false);

			this.generateBox(worldIn, structureBoundingBoxIn, 2, 1, 1, 4, 1, 1, planks, planks, false);
			placeBlock(worldIn, plankSlab, 3, 1, 2, structureBoundingBoxIn);

			this.generateBox(worldIn, structureBoundingBoxIn, 0, 1, 1, 0, 3, 5, wool, wool, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 6, 1, 1, 6, 3, 5, wool, wool, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 1, 1, 6, 5, 3, 6, wool, wool, false);

			this.generateBox(worldIn, structureBoundingBoxIn, 1, 4, 1, 5, 4, 1, wool, wool, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 1, 4, 5, 5, 4, 5, wool, wool, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 1, 4, 2, 1, 4, 4, wool, wool, false);
			this.generateBox(worldIn, structureBoundingBoxIn, 5, 4, 2, 5, 4, 4, wool, wool, false);

			generateBox(worldIn, structureBoundingBoxIn, 2, 5, 2, 2, 5, 4, wool, wool, false);
			generateBox(worldIn, structureBoundingBoxIn, 4, 5, 2, 4, 5, 4, wool, wool, false);
			placeBlock(worldIn, wool, 3, 5, 2, structureBoundingBoxIn);
			placeBlock(worldIn, wool, 3, 6, 3, structureBoundingBoxIn);
			placeBlock(worldIn, wool, 3, 5, 4, structureBoundingBoxIn);

			placeBlock(worldIn, torch.setValue(WallTorchBlock.FACING, Direction.EAST), 1, 2, 4, structureBoundingBoxIn);
			placeBlock(worldIn, torch.setValue(WallTorchBlock.FACING, Direction.WEST), 5, 2, 4, structureBoundingBoxIn);

			if(!spawns[0])
				spawns[0] = spawnConsort(3, 2, 2, structureBoundingBoxIn, worldIn, chunkGeneratorIn, EnumConsort.getRandomMerchant(randomIn), 1);

			return true;
		}
	}
}