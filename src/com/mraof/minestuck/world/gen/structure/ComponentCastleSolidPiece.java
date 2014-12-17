package com.mraof.minestuck.world.gen.structure;

import java.util.List;
import java.util.Random;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;

import com.mraof.minestuck.Minestuck;

public class ComponentCastleSolidPiece extends ComponentCastlePiece 
{
	public ComponentCastleSolidPiece() {}
	protected ComponentCastleSolidPiece(int par1, ComponentCastleStartPiece startPiece, StructureBoundingBox structureBoundingBox) 
	{
		super(par1, startPiece);
		this.boundingBox = structureBoundingBox;
		this.componentType = 0;
	}

	public static ComponentCastleSolidPiece findValidPlacement(
			List<?> par0List, ComponentCastleStartPiece startPiece, int par2, int par3, int par4, int par5, int par6)
    {
        StructureBoundingBox structureboundingbox = new StructureBoundingBox(par2 + 0, par3 + 0, par4 + 0, par2 + 8 + 0, 0 + 8 + 0, par4 + 8 + 0);
        return new ComponentCastleSolidPiece(par6, startPiece, structureboundingbox);
    }
	@Override
	public boolean addComponentParts(World world, Random random, StructureBoundingBox structureboundingbox) {
		int chessTileMetadata = startPiece.isBlack ? 0 : 1;
    	int chessTileMetadata1 = startPiece.isBlack ? 2 : 3;
        if (startPiece.averageGroundLevel < 0)
        {
             startPiece.averageGroundLevel = startPiece.getAverageGroundLevel(world);

            if (startPiece.averageGroundLevel < 0)
            {
                return true;
            }
            
        }
        if(this.boundingBox.minY < 1)
        	this.boundingBox.offset(0, startPiece.averageGroundLevel - 1, 0);
        
        this.fillWithAlternatingBlocks(world, structureboundingbox, 0, 0, 0, 7 ,7, 7, Minestuck.chessTile, chessTileMetadata,  Minestuck.chessTile, chessTileMetadata1, false);

        return true;
	}
	@Override
	protected void writeStructureToNBT(NBTTagCompound p_143012_1_) {
		// TODO Auto-generated method stub
		
	}
	@Override
	protected void readStructureFromNBT(NBTTagCompound p_143011_1_) {
		// TODO Auto-generated method stub
		
	}

}
