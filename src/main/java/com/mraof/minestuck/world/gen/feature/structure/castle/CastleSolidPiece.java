package com.mraof.minestuck.world.gen.feature.structure.castle;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.world.gen.feature.MSStructurePieces;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.Random;

public class CastleSolidPiece extends CastlePiece
{
	protected CastleSolidPiece(boolean isBlack, MutableBoundingBox structureBoundingBox)
	{
		super(MSStructurePieces.SKAIA_CASTLE_SOLID, 0, isBlack);
		this.boundingBox = structureBoundingBox;
	}
	
	public CastleSolidPiece(TemplateManager templates, CompoundNBT nbt)
	{
		super(MSStructurePieces.SKAIA_CASTLE_SOLID, nbt);
	}
	
	@Override
	protected void readAdditional(CompoundNBT nbt)
	{
	
	}
	
	public static CastleSolidPiece findValidPlacement(boolean isBlack, int x, int y, int z)
    {
		MutableBoundingBox structureboundingbox = new MutableBoundingBox(x, y, z, x + 8, y + 8, z + 8);
        return new CastleSolidPiece(isBlack, structureboundingbox);
    }
	
	@Override
	public boolean create(IWorld world, ChunkGenerator<?> chunkGeneratorIn, Random randomIn, MutableBoundingBox structureBoundingBox, ChunkPos chunkPosIn)
	{
		BlockState chessTile = (isBlack ? MSBlocks.BLACK_CHESS_DIRT : MSBlocks.WHITE_CHESS_DIRT).getDefaultState();
		BlockState chessTile1 = (isBlack ? MSBlocks.DARK_GRAY_CHESS_DIRT : MSBlocks.LIGHT_GRAY_CHESS_DIRT).getDefaultState();
  
		this.fillWithAlternatingBlocks(world, structureBoundingBox, 0, 0, 0, 7 ,7, 7, chessTile, chessTile1, false);

        return true;
	}
}