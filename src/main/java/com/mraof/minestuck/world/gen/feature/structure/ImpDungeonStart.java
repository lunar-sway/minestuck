package com.mraof.minestuck.world.gen.feature.structure;

import com.mraof.minestuck.world.gen.feature.MSStructurePieces;
import com.mraof.minestuck.world.gen.feature.structure.blocks.StructureBlockRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.WallTorchBlock;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.List;
import java.util.Random;

public class ImpDungeonStart extends StructureStart<NoFeatureConfig>
{
	public ImpDungeonStart(Structure<NoFeatureConfig> structure, int chunkX, int chunkZ, MutableBoundingBox boundingBox, int reference, long seed)
	{
		super(structure, chunkX, chunkZ, boundingBox, reference, seed);
	}
	
	@Override
	public void generatePieces(DynamicRegistries registries, ChunkGenerator generator, TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn, NoFeatureConfig config)
	{
		EntryPiece piece = new EntryPiece(chunkX, chunkZ, random, pieces);
		pieces.add(piece);
		piece.addChildren(piece, pieces, random);
		calculateBoundingBox();
	}
	
	public static class EntryPiece extends StructurePiece
	{
		protected boolean definedHeight = false;
		protected int compoHeight;
		
		public EntryPiece(int chunkX, int chunkZ, Random rand, List<StructurePiece> componentList)
		{
			super(MSStructurePieces.IMP_ENTRY, 0);
			int x = chunkX*16 + rand.nextInt(16);
			int z = chunkZ*16 + rand.nextInt(16);
			setOrientation(Direction.Plane.HORIZONTAL.getRandomDirection(rand));
			int xWidth = getOrientation().getAxis().equals(Direction.Axis.X) ? 11 : 6;
			int zWidth = getOrientation().getAxis().equals(Direction.Axis.Z) ? 11 : 6;
			this.boundingBox = new MutableBoundingBox(x, 64, z, x + xWidth - 1, 67, z + zWidth - 1);
			
			//TODO Add component pieces through buildComponent() rather than the constructor.
			ImpDungeonPieces.EntryCorridor corridor = new ImpDungeonPieces.EntryCorridor(this.getOrientation(), x, z, rand, componentList);
			compoHeight = corridor.getBoundingBox().y1 - 1;
			componentList.add(corridor);
		}
		
		public EntryPiece(TemplateManager templates, CompoundNBT nbt)
		{
			super(MSStructurePieces.IMP_ENTRY, nbt);
			definedHeight = nbt.getBoolean("definedHeight");
			compoHeight = nbt.getInt("compoHeight");
		}
		
		@Override
		protected void addAdditionalSaveData(CompoundNBT tagCompound)
		{
			tagCompound.putBoolean("definedHeight", definedHeight);
			tagCompound.putInt("compoHeight", compoHeight);
		}

		@Override
		public boolean postProcess(ISeedReader worldIn, StructureManager manager, ChunkGenerator chunkGeneratorIn, Random randomIn, MutableBoundingBox structureBoundingBoxIn, ChunkPos chunkPosIn, BlockPos pos)
		{
			checkHeight(worldIn, structureBoundingBoxIn);

			StructureBlockRegistry blocks = StructureBlockRegistry.getOrDefault(chunkGeneratorIn);

			BlockState wallBlock = blocks.getBlockState("structure_primary");
			BlockState wallDecor = blocks.getBlockState("structure_primary_decorative");
			BlockState floorBlock = blocks.getBlockState("structure_secondary");
			BlockState floorStairs = blocks.getBlockState("structure_secondary_stairs").rotate(Rotation.CLOCKWISE_180);
			BlockState torch = blocks.getBlockState("wall_torch");

			for(int x = 1; x < 5; x++)
				buildFloorTile(floorBlock, x, 0, worldIn, randomIn, structureBoundingBoxIn);

			for(int z = 0; z < 5; z++)
			{
				buildFloorTile(wallBlock, 0, z, worldIn, randomIn, structureBoundingBoxIn);
				buildFloorTile(wallBlock, 5, z, worldIn, randomIn, structureBoundingBoxIn);
				buildWall(wallBlock, 0, z, worldIn, randomIn, structureBoundingBoxIn, 0);
				buildWall(wallBlock, 5, z, worldIn, randomIn, structureBoundingBoxIn, 0);
			}

			for(int x = 1; x < 5; x++)
				buildWall(wallBlock, x, 5, worldIn, randomIn, structureBoundingBoxIn, 0);
			if(this.getBlock(worldIn, 1, 2, 5, boundingBox) == wallBlock)
				this.placeBlock(worldIn, wallDecor, 1, 2, 5, boundingBox);
			if(this.getBlock(worldIn, 4, 2, 5, boundingBox) == wallBlock)
				this.placeBlock(worldIn, wallDecor, 4, 2, 5, boundingBox);

			generateBox(worldIn, structureBoundingBoxIn, 1, 0, 1, 1, 0, 4, floorBlock, floorBlock, false);
			generateBox(worldIn, structureBoundingBoxIn, 4, 0, 1, 4, 0, 4, floorBlock, floorBlock, false);
			generateBox(worldIn, structureBoundingBoxIn, 1, 0, 5, 4, 0, 5, wallBlock, wallBlock, false);
			generateAirBox(worldIn, structureBoundingBoxIn, 1, 1, 0, 4, 4, 4);

			generateAirBox(worldIn, structureBoundingBoxIn, 2, 0, 2, 3, 0, 4);
			generateBox(worldIn, structureBoundingBoxIn, 2, 0, 1, 3, 0, 1, floorStairs, floorStairs, false);
			generateAirBox(worldIn, structureBoundingBoxIn, 2, -1, 3, 3, -1, 5);
			generateBox(worldIn, structureBoundingBoxIn, 2, -1, 2, 3, -1, 2, floorStairs, floorStairs, false);
			generateBox(worldIn, structureBoundingBoxIn, 1, -1, 6, 4, -1, 6, wallBlock, wallBlock, false);
			generateAirBox(worldIn, structureBoundingBoxIn, 2, -2, 4, 3, -2, 6);
			generateBox(worldIn, structureBoundingBoxIn, 2, -2, 3, 3, -2, 3, floorStairs, floorStairs, false);
			generateBox(worldIn, structureBoundingBoxIn, 1, -2, 7, 4, -2, 10, wallBlock, wallBlock, false);
			generateAirBox(worldIn, structureBoundingBoxIn, 2, -3, 5, 3, -3, 9);
			generateBox(worldIn, structureBoundingBoxIn, 2, -3, 4, 3, -3, 4, floorStairs, floorStairs, false);
			generateAirBox(worldIn, structureBoundingBoxIn, 2, -4, 6, 3, -4, 9);
			generateBox(worldIn, structureBoundingBoxIn, 2, -4, 5, 3, -4, 5, floorStairs, floorStairs, false);
			generateAirBox(worldIn, structureBoundingBoxIn, 2, -5, 7, 3, -5, 9);
			generateBox(worldIn, structureBoundingBoxIn, 2, -5, 6, 3, -5, 6, floorStairs, floorStairs, false);
			generateBox(worldIn, structureBoundingBoxIn, 1, -6, 6, 4, -6, 8, floorBlock, floorBlock, false);

			generateBox(worldIn, structureBoundingBoxIn, 1, compoHeight - boundingBox.y0, 10, 4, -3, 10, wallBlock, wallBlock, false);
			generateBox(worldIn, structureBoundingBoxIn, 1, -5, 6, 1, -3, 9, wallBlock, wallBlock, false);
			generateBox(worldIn, structureBoundingBoxIn, 1, -1, 2, 1, -1, 5, wallBlock, wallBlock, false);
			generateBox(worldIn, structureBoundingBoxIn, 1, -2, 3, 1, -2, 6, wallBlock, wallBlock, false);
			generateBox(worldIn, structureBoundingBoxIn, 1, -3, 4, 1, -3, 5, wallBlock, wallBlock, false);
			placeBlock(worldIn, wallBlock, 1, -4, 5, structureBoundingBoxIn);
			generateBox(worldIn, structureBoundingBoxIn, 4, -5, 6, 4, -3, 9, wallBlock, wallBlock, false);
			generateBox(worldIn, structureBoundingBoxIn, 4, -1, 2, 4, -1, 5, wallBlock, wallBlock, false);
			generateBox(worldIn, structureBoundingBoxIn, 4, -2, 3, 4, -2, 6, wallBlock, wallBlock, false);
			generateBox(worldIn, structureBoundingBoxIn, 4, -3, 4, 4, -3, 5, wallBlock, wallBlock, false);
			placeBlock(worldIn, wallBlock, 4, -4, 5, structureBoundingBoxIn);
			generateAirBox(worldIn, structureBoundingBoxIn, 2, compoHeight - boundingBox.y0, 9, 3, -6, 9);
			generateBox(worldIn, structureBoundingBoxIn, 1, compoHeight - boundingBox.y0, 9, 1, -6, 9, wallBlock, wallBlock, false);
			generateBox(worldIn, structureBoundingBoxIn, 4, compoHeight - boundingBox.y0, 9, 4, -6, 9, wallBlock, wallBlock, false);
			generateBox(worldIn, structureBoundingBoxIn, 1, compoHeight - boundingBox.y0, 8, 4, -7, 8, wallBlock, wallBlock, false);

			placeBlock(worldIn, torch.setValue(WallTorchBlock.FACING, Direction.EAST), 2, -3, 8, structureBoundingBoxIn);
			placeBlock(worldIn, torch.setValue(WallTorchBlock.FACING, Direction.WEST), 3, -3, 8, structureBoundingBoxIn);

			return true;
		}
		
		protected void checkHeight(IWorld worldIn, MutableBoundingBox bb)
		{
			if(definedHeight)
				return;
			int height = 0;
			int i = 0;
			
			for(int x = boundingBox.x0; x <= boundingBox.x1; x++)
				for(int z = boundingBox.z0; z <= boundingBox.z1; z++)
				{
					if(!bb.isInside(new BlockPos(x, 64, z)))
						continue;
					
					int y = Math.max(62, worldIn.getHeightmapPos(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, new BlockPos(x, 0, z)).getY());
					height += y;
					i++;
				}
			
			height /= i;
			boundingBox.move(0, height - boundingBox.y0, 0);
			definedHeight = true;
		}
		
		protected void buildWall(BlockState block, int x, int z, ISeedReader world, Random rand, MutableBoundingBox boundingBox, int minY)
		{
			float f = 0.5F + z*0.2F;
			for(int y = 1; y < 4; y++)
			{
				if(y > minY && rand.nextFloat() >= f)
					return;
				
				this.placeBlock(world, block, x, y, z, boundingBox);
				
				f -= 0.5F;
			}
		}
		
		protected void buildFloorTile(BlockState block, int x, int z, ISeedReader world, Random rand, MutableBoundingBox boundingBox)
		{
			int y = 0;
			
			do
			{
				this.placeBlock(world, block, x, y, z, boundingBox);
				y--;
			} while(this.boundingBox.y0 + y >= 0 && !this.getBlock(world, x, y, z, boundingBox).getMaterial().isSolid());
		}
	}
}