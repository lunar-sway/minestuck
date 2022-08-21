package com.mraof.minestuck.world.gen.feature.structure.castle;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.world.gen.feature.MSStructurePieces;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;

import java.util.ArrayList;
import java.util.Random;

public class CastleStartPiece extends CastlePiece
{

    /** List of other Castle components linked to this room. */

    protected boolean bottom;
    public int averageGroundLevel = -1;
    public int castleWidth, castleLength, x, z, totalPieces;
    public ArrayList<CastlePiece> pendingPieces = new ArrayList<>();
    
    protected CastleStartPiece(int x, int z, boolean isBlack)
    {
        super(MSStructurePieces.SKAIA_CASTLE_START.get(), 0, new BoundingBox(x, 0, z, x, 74, z), isBlack);
        this.x = x;
        this.z = z;
        if(pendingPieces == null)pendingPieces = new ArrayList<>();
        this.bottom = true;
    }
    
    public CastleStartPiece(CompoundTag nbt)
    {
        super(MSStructurePieces.SKAIA_CASTLE_START.get(), nbt);
    }
    
    @Override
    public void addChildren(StructurePiece componentIn, StructurePieceAccessor accessor, Random rand)
    {
		this.castleWidth = (rand.nextInt(12) + 4) * 16;
		this.castleLength = (rand.nextInt(24) + 8) * 16;
		this.genDepth = 1;
		this.getNextComponentNormal(this, accessor, rand, 8, 0, true);
		this.genDepth = 2;
		for(int depth = 8; depth < this.castleLength; depth += 8)
			this.getNextComponentNormal(this, accessor, rand,  0, depth, true);
		this.genDepth = 0;
		this.getNextComponentNormal(this, accessor, rand, 0, -8, 0);
		
	}
    
    @Override
    public void postProcess(WorldGenLevel level, StructureFeatureManager manager, ChunkGenerator generator, Random random, BoundingBox structureBoundingBox, ChunkPos chunkPosIn, BlockPos pos)
	{
		BlockState chessTile = (isBlack ? MSBlocks.BLACK_CHESS_DIRT.get() : MSBlocks.WHITE_CHESS_DIRT.get()).defaultBlockState();
		BlockState chessTile1 = (isBlack ? MSBlocks.DARK_GRAY_CHESS_DIRT.get() : MSBlocks.LIGHT_GRAY_CHESS_DIRT.get()).defaultBlockState();
		if(this.averageGroundLevel < 0)
		{
			this.averageGroundLevel = this.getAverageGroundLevel(level, structureBoundingBox);
		
			if(this.averageGroundLevel < 0)
				return;
		
		}
		this.boundingBox.move(0, this.averageGroundLevel - 1, 0);
		this.fillWithAlternatingBlocks(level, structureBoundingBox, 0, 0, 0, 7, 6, 7, chessTile, chessTile1, false);
		this.fillWithAlternatingBlocks(level, structureBoundingBox, 0, 0, 0, 7, 7, 0, chessTile, chessTile1, false);
		this.fillWithAlternatingBlocks(level, structureBoundingBox, 0, 0, 7, 7, 7, 7, chessTile, chessTile1, false);
		this.generateAirBox(level, structureBoundingBox, 2, 1, 0, 5, 5, 7);
	}
}