package com.mraof.minestuck.world.gen.feature.structure.castle;

import com.mraof.minestuck.world.gen.feature.MSStructurePieces;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.Random;

public class CastleLibraryPiece extends CastleRoomPiece
{
	public CastleLibraryPiece(boolean isBlack, MutableBoundingBox structureBoundingBox)
	{
		super(MSStructurePieces.SKAIA_CASTLE_LIBRARY, isBlack, structureBoundingBox);
	}
	
	public CastleLibraryPiece(TemplateManager templates, CompoundNBT nbt)
	{
		super(MSStructurePieces.SKAIA_CASTLE_LIBRARY, nbt);
	}
	
	public static CastleLibraryPiece findValidPlacement(boolean isBlack, int x, int y, int z)
	{
		MutableBoundingBox structureboundingbox = new MutableBoundingBox(x, y, z, x + 8, y + 8, z + 8);
		return new CastleLibraryPiece(isBlack, structureboundingbox);
	}
	
	@Override
	public boolean addComponentParts(IWorld world, Random random, MutableBoundingBox structureBoundingBox, ChunkPos chunkPosIn)
	{
		super.addComponentParts(world, random, structureBoundingBox, chunkPosIn);
		this.fillWithBlocks(world, structureBoundingBox, 1, 1, 1, 1, 3, 6, Blocks.BOOKSHELF.getDefaultState(), Blocks.AIR.getDefaultState(), false);
		this.fillWithBlocks(world, structureBoundingBox, 6, 1, 1, 6, 3, 6, Blocks.BOOKSHELF.getDefaultState(), Blocks.AIR.getDefaultState(), false);
		return true;
	}
	
}
