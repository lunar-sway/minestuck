package com.mraof.minestuck.world.gen.feature.structure;

import com.mraof.minestuck.world.gen.feature.MSStructurePieces;
import com.mraof.minestuck.world.gen.feature.structure.blocks.StructureBlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;

import java.util.Random;

public class ImpDungeonEntryPiece extends StructurePiece
{
	protected boolean definedHeight = false;
	protected int compoHeight;
	
	public static StructurePiece create(ChunkPos pos, Random rand)
	{
		return new ImpDungeonEntryPiece(pos.getBlockX(rand.nextInt(16)), pos.getBlockZ(rand.nextInt(16)), rand, getRandomHorizontalDirection(rand));
	}
	
	private ImpDungeonEntryPiece(int x, int z, Random rand, Direction orientation)
	{
		super(MSStructurePieces.IMP_ENTRY.get(), 0, makeBoundingBox(x, 64, z, orientation, 6, 4, 11));
		setOrientation(orientation);
	}
	
	public ImpDungeonEntryPiece(CompoundTag nbt)
	{
		super(MSStructurePieces.IMP_ENTRY.get(), nbt);
		definedHeight = nbt.getBoolean("definedHeight");
		compoHeight = nbt.getInt("compoHeight");
	}
	
	@Override
	protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tagCompound)
	{
		tagCompound.putBoolean("definedHeight", definedHeight);
		tagCompound.putInt("compoHeight", compoHeight);
	}
	
	@Override
	public void addChildren(StructurePiece piece, StructurePieceAccessor builder, Random rand)
	{
		ImpDungeonPieces.EntryCorridor corridor = ImpDungeonPieces.EntryCorridor.create(this.getOrientation(), boundingBox.minX(), boundingBox.minZ(), rand);
		compoHeight = corridor.getBoundingBox().maxY() - 1;
		builder.addPiece(corridor);
		corridor.addChildren(piece, builder, rand);
	}
	
	@Override
	public void postProcess(WorldGenLevel level, StructureFeatureManager manager, ChunkGenerator chunkGeneratorIn, Random randomIn, BoundingBox structureBoundingBoxIn, ChunkPos chunkPosIn, BlockPos pos)
	{
		checkHeight(level, structureBoundingBoxIn);
		
		StructureBlockRegistry blocks = StructureBlockRegistry.getOrDefault(chunkGeneratorIn);
		
		BlockState wallBlock = blocks.getBlockState("structure_primary");
		BlockState wallDecor = blocks.getBlockState("structure_primary_decorative");
		BlockState floorBlock = blocks.getBlockState("structure_secondary");
		BlockState floorStairs = blocks.getBlockState("structure_secondary_stairs").rotate(Rotation.CLOCKWISE_180);
		BlockState torch = blocks.getBlockState("wall_torch");
		
		for(int x = 1; x < 5; x++)
			buildFloorTile(floorBlock, x, 0, level, randomIn, structureBoundingBoxIn);
		
		for(int z = 0; z < 5; z++)
		{
			buildFloorTile(wallBlock, 0, z, level, randomIn, structureBoundingBoxIn);
			buildFloorTile(wallBlock, 5, z, level, randomIn, structureBoundingBoxIn);
			buildWall(wallBlock, 0, z, level, randomIn, structureBoundingBoxIn, 0);
			buildWall(wallBlock, 5, z, level, randomIn, structureBoundingBoxIn, 0);
		}
		
		for(int x = 1; x < 5; x++)
			buildWall(wallBlock, x, 5, level, randomIn, structureBoundingBoxIn, 0);
		if(this.getBlock(level, 1, 2, 5, boundingBox) == wallBlock)
			this.placeBlock(level, wallDecor, 1, 2, 5, boundingBox);
		if(this.getBlock(level, 4, 2, 5, boundingBox) == wallBlock)
			this.placeBlock(level, wallDecor, 4, 2, 5, boundingBox);
		
		generateBox(level, structureBoundingBoxIn, 1, 0, 1, 1, 0, 4, floorBlock, floorBlock, false);
		generateBox(level, structureBoundingBoxIn, 4, 0, 1, 4, 0, 4, floorBlock, floorBlock, false);
		generateBox(level, structureBoundingBoxIn, 1, 0, 5, 4, 0, 5, wallBlock, wallBlock, false);
		generateAirBox(level, structureBoundingBoxIn, 1, 1, 0, 4, 4, 4);
		
		generateAirBox(level, structureBoundingBoxIn, 2, 0, 2, 3, 0, 4);
		generateBox(level, structureBoundingBoxIn, 2, 0, 1, 3, 0, 1, floorStairs, floorStairs, false);
		generateAirBox(level, structureBoundingBoxIn, 2, -1, 3, 3, -1, 5);
		generateBox(level, structureBoundingBoxIn, 2, -1, 2, 3, -1, 2, floorStairs, floorStairs, false);
		generateBox(level, structureBoundingBoxIn, 1, -1, 6, 4, -1, 6, wallBlock, wallBlock, false);
		generateAirBox(level, structureBoundingBoxIn, 2, -2, 4, 3, -2, 6);
		generateBox(level, structureBoundingBoxIn, 2, -2, 3, 3, -2, 3, floorStairs, floorStairs, false);
		generateBox(level, structureBoundingBoxIn, 1, -2, 7, 4, -2, 10, wallBlock, wallBlock, false);
		generateAirBox(level, structureBoundingBoxIn, 2, -3, 5, 3, -3, 9);
		generateBox(level, structureBoundingBoxIn, 2, -3, 4, 3, -3, 4, floorStairs, floorStairs, false);
		generateAirBox(level, structureBoundingBoxIn, 2, -4, 6, 3, -4, 9);
		generateBox(level, structureBoundingBoxIn, 2, -4, 5, 3, -4, 5, floorStairs, floorStairs, false);
		generateAirBox(level, structureBoundingBoxIn, 2, -5, 7, 3, -5, 9);
		generateBox(level, structureBoundingBoxIn, 2, -5, 6, 3, -5, 6, floorStairs, floorStairs, false);
		generateBox(level, structureBoundingBoxIn, 1, -6, 6, 4, -6, 8, floorBlock, floorBlock, false);
		
		generateBox(level, structureBoundingBoxIn, 1, compoHeight - boundingBox.minY(), 10, 4, -3, 10, wallBlock, wallBlock, false);
		generateBox(level, structureBoundingBoxIn, 1, -5, 6, 1, -3, 9, wallBlock, wallBlock, false);
		generateBox(level, structureBoundingBoxIn, 1, -1, 2, 1, -1, 5, wallBlock, wallBlock, false);
		generateBox(level, structureBoundingBoxIn, 1, -2, 3, 1, -2, 6, wallBlock, wallBlock, false);
		generateBox(level, structureBoundingBoxIn, 1, -3, 4, 1, -3, 5, wallBlock, wallBlock, false);
		placeBlock(level, wallBlock, 1, -4, 5, structureBoundingBoxIn);
		generateBox(level, structureBoundingBoxIn, 4, -5, 6, 4, -3, 9, wallBlock, wallBlock, false);
		generateBox(level, structureBoundingBoxIn, 4, -1, 2, 4, -1, 5, wallBlock, wallBlock, false);
		generateBox(level, structureBoundingBoxIn, 4, -2, 3, 4, -2, 6, wallBlock, wallBlock, false);
		generateBox(level, structureBoundingBoxIn, 4, -3, 4, 4, -3, 5, wallBlock, wallBlock, false);
		placeBlock(level, wallBlock, 4, -4, 5, structureBoundingBoxIn);
		generateAirBox(level, structureBoundingBoxIn, 2, compoHeight - boundingBox.minY(), 9, 3, -6, 9);
		generateBox(level, structureBoundingBoxIn, 1, compoHeight - boundingBox.minY(), 9, 1, -6, 9, wallBlock, wallBlock, false);
		generateBox(level, structureBoundingBoxIn, 4, compoHeight - boundingBox.minY(), 9, 4, -6, 9, wallBlock, wallBlock, false);
		generateBox(level, structureBoundingBoxIn, 1, compoHeight - boundingBox.minY(), 8, 4, -7, 8, wallBlock, wallBlock, false);
		
		placeBlock(level, torch.setValue(WallTorchBlock.FACING, Direction.EAST), 2, -3, 8, structureBoundingBoxIn);
		placeBlock(level, torch.setValue(WallTorchBlock.FACING, Direction.WEST), 3, -3, 8, structureBoundingBoxIn);
	}
	
	protected void checkHeight(LevelAccessor level, BoundingBox bb)
	{
		if(definedHeight)
			return;
		int height = 0;
		int i = 0;
		
		for(int x = boundingBox.minX(); x <= boundingBox.maxX(); x++)
			for(int z = boundingBox.minZ(); z <= boundingBox.maxZ(); z++)
			{
				if(!bb.isInside(new BlockPos(x, 64, z)))
					continue;
				
				int y = Math.max(62, level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, new BlockPos(x, 0, z)).getY());
				height += y;
				i++;
			}
		
		height /= i;
		boundingBox.move(0, height - boundingBox.minY(), 0);
		definedHeight = true;
	}
	
	protected void buildWall(BlockState block, int x, int z, WorldGenLevel level, Random rand, BoundingBox boundingBox, int minY)
	{
		float f = 0.5F + z * 0.2F;
		for(int y = 1; y < 4; y++)
		{
			if(y > minY && rand.nextFloat() >= f)
				return;
			
			this.placeBlock(level, block, x, y, z, boundingBox);
			
			f -= 0.5F;
		}
	}
	
	protected void buildFloorTile(BlockState block, int x, int z, WorldGenLevel level, Random rand, BoundingBox boundingBox)
	{
		int y = 0;
		
		do
		{
			this.placeBlock(level, block, x, y, z, boundingBox);
			y--;
		} while(this.boundingBox.minY() + y >= 0 && !this.getBlock(level, x, y, z, boundingBox).getMaterial().isSolid());
	}
}
