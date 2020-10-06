package com.mraof.minestuck.world.gen.feature.structure.castle;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.world.gen.feature.MSStructurePieces;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
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

public class CastleStaircasePiece extends CastleRoomPiece
{
	public CastleStaircasePiece(boolean isBlack, MutableBoundingBox structureBoundingBox)
	{
		super(MSStructurePieces.SKAIA_CASTLE_STAIRCASE, isBlack, structureBoundingBox);
	}
	
	public CastleStaircasePiece(TemplateManager templates, CompoundNBT nbt)
	{
		super(templates, nbt);
	}
	
	public static CastleStaircasePiece findValidPlacement(boolean isBlack, int x, int y, int z)
	{
		MutableBoundingBox structureboundingbox = new MutableBoundingBox(x, y - 8, z, x + 8, y + 7, z + 8);
		return new CastleStaircasePiece(isBlack, structureboundingbox);
	}
	
	@Override
	public void buildComponent(StructurePiece componentIn, List<StructurePiece> pieces, Random rand)
	{
		this.direction = rand.nextInt(4);
	}
	
	@Override
	public boolean func_230383_a_(ISeedReader world, StructureManager manager, ChunkGenerator generator, Random random, MutableBoundingBox structureBoundingBox, ChunkPos chunkPosIn, BlockPos pos)
	{
//		super.create(world, chunkGeneratorIn, randomIn, structureBoundingBox, chunkPosIn);
//		do what that would have done but set the offset correctly
		BlockState chessTile = (isBlack ? MSBlocks.BLACK_CHESS_DIRT : MSBlocks.WHITE_CHESS_DIRT).getDefaultState();
		BlockState chessTile1 = (isBlack ? MSBlocks.DARK_GRAY_CHESS_DIRT : MSBlocks.LIGHT_GRAY_CHESS_DIRT).getDefaultState();
		this.fillWithAir(world, structureBoundingBox, 0, 1, 0, 7, 14, 7);
		this.fillWithAlternatingBlocks(world, structureBoundingBox, 0, 15, 0, 7, 15, 7, chessTile, chessTile1, false);
		for(int step = 0; step < 8; step++) //Come on, step it up!
			switch(this.direction)
			{
			case 0:
				this.fillWithAlternatingBlocks(world, structureBoundingBox, 0, step, step, 7, step, step, chessTile, chessTile1, false);
				break;
			case 1:
				this.fillWithAlternatingBlocks(world, structureBoundingBox, step, step, 0, step, step, 7, chessTile, chessTile1, false);
				break;
			case 2:
				this.fillWithAlternatingBlocks(world, structureBoundingBox, 0, step, 7 - step, 7, step, 7 - step, chessTile, chessTile1, false);
				break;
			case 3:
				this.fillWithAlternatingBlocks(world, structureBoundingBox, 7 - step, step, 0, 7 - step, step, 7, chessTile, chessTile1, false);
				break;
			}
		return true;
	}
}
