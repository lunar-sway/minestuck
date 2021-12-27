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

import java.util.ArrayList;
import java.util.List;
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
        super(MSStructurePieces.SKAIA_CASTLE_START, 0, isBlack);
        this.boundingBox = new MutableBoundingBox(x, 0, z, x, 74, z);
        this.x = x;
        this.z = z;
        if(pendingPieces == null)pendingPieces = new ArrayList<>();
        this.bottom = true;
    }
    
    public CastleStartPiece(TemplateManager templates, CompoundNBT nbt)
    {
        super(MSStructurePieces.SKAIA_CASTLE_START, nbt);
    }
    
    @Override
    public void addChildren(StructurePiece componentIn, List<StructurePiece> pieces, Random rand)
    {
		this.castleWidth = (rand.nextInt(12) + 4) * 16;
		this.castleLength = (rand.nextInt(24) + 8) * 16;
		this.genDepth = 1;
		this.getNextComponentNormal(this, pieces, rand, 8, 0, true);
		this.genDepth = 2;
		for(int depth = 8; depth < this.castleLength; depth += 8)
			this.getNextComponentNormal(this, pieces, rand,  0, depth, true);
		this.genDepth = 0;
		this.getNextComponentNormal(this, pieces, rand, 0, -8, 0);
		
	}
    
    @Override
    public boolean postProcess(ISeedReader world, StructureManager manager, ChunkGenerator generator, Random random, MutableBoundingBox structureBoundingBox, ChunkPos chunkPosIn, BlockPos pos)
    {
        BlockState chessTile = (isBlack ? MSBlocks.BLACK_CHESS_DIRT : MSBlocks.WHITE_CHESS_DIRT).defaultBlockState();
        BlockState chessTile1 = (isBlack ? MSBlocks.DARK_GRAY_CHESS_DIRT : MSBlocks.LIGHT_GRAY_CHESS_DIRT).defaultBlockState();
        if (this.averageGroundLevel < 0)
        {
            this.averageGroundLevel = this.getAverageGroundLevel(world, structureBoundingBox);

            if (this.averageGroundLevel < 0)
            {
                return false;
            }

        }
        this.boundingBox.move(0, this.averageGroundLevel - 1, 0);
        if (this.edgesLiquid(world, structureBoundingBox))
        {
            return false;
        }
        else
        {
            this.fillWithAlternatingBlocks(world, structureBoundingBox, 0, 0, 0, 7 ,6, 7, chessTile,  chessTile1, false);
            this.fillWithAlternatingBlocks(world, structureBoundingBox, 0, 0, 0, 7 ,7, 0, chessTile, chessTile1, false);
            this.fillWithAlternatingBlocks(world, structureBoundingBox, 0, 0, 7, 7 ,7, 7, chessTile, chessTile1, false);
            this.generateAirBox(world, structureBoundingBox, 2, 1, 0, 5, 5, 7);

            return true;
        }
    }
}