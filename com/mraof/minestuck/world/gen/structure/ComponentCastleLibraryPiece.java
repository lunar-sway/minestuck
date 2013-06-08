package com.mraof.minestuck.world.gen.structure;

import java.util.List;
import java.util.Random;

import com.mraof.minestuck.Minestuck;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;

public class ComponentCastleLibraryPiece extends ComponentCastleRoomPiece {

	protected ComponentCastleLibraryPiece(int par1, ComponentCastleStartPiece startPiece,	StructureBoundingBox structureBoundingBox) 
	{
		super(par1, startPiece, structureBoundingBox);
	}
	public static ComponentCastleRoomPiece findValidPlacement(List par0List, ComponentCastleStartPiece startPiece, int par2, int par3, int par4, int par5, int par6)
	{
		StructureBoundingBox structureboundingbox = new StructureBoundingBox(par2 + 0, par3 + 0, par4 + 0, par2 + 8 + 0, 0 + 8 + 0, par4 + 8 + 0);
		return new ComponentCastleLibraryPiece(par6, startPiece, structureboundingbox);
	}
	@Override
	public boolean addComponentParts(World world, Random random,StructureBoundingBox structureboundingbox) 
	{
		super.addComponentParts(world, random, structureboundingbox);
		this.fillWithBlocks(world, structureboundingbox, 1, 1, 1, 1, 3, 6, Block.bookShelf.blockID, 0, false);
		this.fillWithBlocks(world, structureboundingbox, 6, 1, 1, 6, 3, 6, Block.bookShelf.blockID, 0, false);
		return true;
	}


}
