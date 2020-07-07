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
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.List;
import java.util.Random;

public class ImpDungeonStart extends StructureStart
{
	public ImpDungeonStart(Structure<?> structureIn, int chunkX, int chunkZ, Biome biomeIn, MutableBoundingBox boundsIn, int referenceIn, long seed)
	{
		super(structureIn, chunkX, chunkZ, biomeIn, boundsIn, referenceIn, seed);
	}
	
	@Override
	public void init(ChunkGenerator<?> generator, TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn)
	{
		EntryPiece piece = new EntryPiece(chunkX, chunkZ, rand, components);
		components.add(piece);
		piece.buildComponent(piece, components, rand);
		recalculateStructureSize();
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
			setCoordBaseMode(Direction.Plane.HORIZONTAL.random(rand));
			int xWidth = getCoordBaseMode().getAxis().equals(Direction.Axis.X) ? 11 : 6;
			int zWidth = getCoordBaseMode().getAxis().equals(Direction.Axis.Z) ? 11 : 6;
			this.boundingBox = new MutableBoundingBox(x, 64, z, x + xWidth - 1, 67, z + zWidth - 1);
			
			//TODO Add component pieces through buildComponent() rather than the constructor.
			ImpDungeonPieces.EntryCorridor corridor = new ImpDungeonPieces.EntryCorridor(this.getCoordBaseMode(), x, z, rand, componentList);
			compoHeight = corridor.getBoundingBox().maxY - 1;
			componentList.add(corridor);
		}
		
		public EntryPiece(TemplateManager templates, CompoundNBT nbt)
		{
			super(MSStructurePieces.IMP_ENTRY, nbt);
			definedHeight = nbt.getBoolean("definedHeight");
			compoHeight = nbt.getInt("compoHeight");
		}
		
		@Override
		protected void readAdditional(CompoundNBT tagCompound)
		{
			tagCompound.putBoolean("definedHeight", definedHeight);
			tagCompound.putInt("compoHeight", compoHeight);
		}
		
		@Override
		public boolean addComponentParts(IWorld worldIn, Random randomIn, MutableBoundingBox structureBoundingBoxIn, ChunkPos chunkPosIn)
		{
			checkHeight(worldIn, structureBoundingBoxIn);
			
			StructureBlockRegistry blocks = StructureBlockRegistry.getOrDefault(worldIn.getChunkProvider().getChunkGenerator().getSettings());
			
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
			if(this.getBlockStateFromPos(worldIn, 1, 2, 5, boundingBox) == wallBlock)
				this.setBlockState(worldIn, wallDecor, 1, 2, 5, boundingBox);
			if(this.getBlockStateFromPos(worldIn, 4, 2, 5, boundingBox) == wallBlock)
				this.setBlockState(worldIn, wallDecor, 4, 2, 5, boundingBox);
			
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 1, 1, 0, 4, floorBlock, floorBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 0, 1, 4, 0, 4, floorBlock, floorBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 5, 4, 0, 5, wallBlock, wallBlock, false);
			fillWithAir(worldIn, structureBoundingBoxIn, 1, 1, 0, 4, 4, 4);
			
			fillWithAir(worldIn, structureBoundingBoxIn, 2, 0, 2, 3, 0, 4);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 0, 1, 3, 0, 1, floorStairs, floorStairs, false);
			fillWithAir(worldIn, structureBoundingBoxIn, 2, -1, 3, 3, -1, 5);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, -1, 2, 3, -1, 2, floorStairs, floorStairs, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, -1, 6, 4, -1, 6, wallBlock, wallBlock, false);
			fillWithAir(worldIn, structureBoundingBoxIn, 2, -2, 4, 3, -2, 6);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, -2, 3, 3, -2, 3, floorStairs, floorStairs, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, -2, 7, 4, -2, 10, wallBlock, wallBlock, false);
			fillWithAir(worldIn, structureBoundingBoxIn, 2, -3, 5, 3, -3, 9);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, -3, 4, 3, -3, 4, floorStairs, floorStairs, false);
			fillWithAir(worldIn, structureBoundingBoxIn, 2, -4, 6, 3, -4, 9);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, -4, 5, 3, -4, 5, floorStairs, floorStairs, false);
			fillWithAir(worldIn, structureBoundingBoxIn, 2, -5, 7, 3, -5, 9);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, -5, 6, 3, -5, 6, floorStairs, floorStairs, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, -6, 6, 4, -6, 8, floorBlock, floorBlock, false);
			
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, compoHeight - boundingBox.minY, 10, 4, -3, 10, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, -5, 6, 1, -3, 9, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, -1, 2, 1, -1, 5, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, -2, 3, 1, -2, 6, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, -3, 4, 1, -3, 5, wallBlock, wallBlock, false);
			setBlockState(worldIn, wallBlock, 1, -4, 5, structureBoundingBoxIn);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 4, -5, 6, 4, -3, 9, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 4, -1, 2, 4, -1, 5, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 4, -2, 3, 4, -2, 6, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 4, -3, 4, 4, -3, 5, wallBlock, wallBlock, false);
			setBlockState(worldIn, wallBlock, 4, -4, 5, structureBoundingBoxIn);
			fillWithAir(worldIn, structureBoundingBoxIn, 2, compoHeight - boundingBox.minY, 9, 3, -6, 9);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, compoHeight - boundingBox.minY, 9, 1, -6, 9, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 4, compoHeight - boundingBox.minY, 9, 4, -6, 9, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, compoHeight - boundingBox.minY, 8, 4, -7, 8, wallBlock, wallBlock, false);
			
			setBlockState(worldIn, torch.with(WallTorchBlock.HORIZONTAL_FACING, Direction.EAST), 2, -3, 8, structureBoundingBoxIn);
			setBlockState(worldIn, torch.with(WallTorchBlock.HORIZONTAL_FACING, Direction.WEST), 3, -3, 8, structureBoundingBoxIn);
			
			return true;
		}
		
		protected void checkHeight(IWorld worldIn, MutableBoundingBox bb)
		{
			if(definedHeight)
				return;
			int height = 0;
			int i = 0;
			
			for(int x = boundingBox.minX; x <= boundingBox.maxX; x++)
				for(int z = boundingBox.minZ; z <= boundingBox.maxZ; z++)
				{
					if(!bb.isVecInside(new BlockPos(x, 64, z)))
						continue;
					
					int y = Math.max(62, worldIn.getHeight(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, new BlockPos(x, 0, z)).getY());
					height += y;
					i++;
				}
			
			height /= i;
			boundingBox.offset(0, height - boundingBox.minY, 0);
			definedHeight = true;
		}
		
		protected void buildWall(BlockState block, int x, int z, IWorld world, Random rand, MutableBoundingBox boundingBox, int minY)
		{
			float f = 0.5F + z*0.2F;
			for(int y = 1; y < 4; y++)
			{
				if(y > minY && rand.nextFloat() >= f)
					return;
				
				this.setBlockState(world, block, x, y, z, boundingBox);
				
				f -= 0.5F;
			}
		}
		
		protected void buildFloorTile(BlockState block, int x, int z, IWorld world, Random rand, MutableBoundingBox boundingBox)
		{
			int y = 0;
			
			do
			{
				this.setBlockState(world, block, x, y, z, boundingBox);
				y--;
			} while(this.boundingBox.minY + y >= 0 && !this.getBlockStateFromPos(world, x, y, z, boundingBox).getMaterial().isSolid());
		}
	}
}