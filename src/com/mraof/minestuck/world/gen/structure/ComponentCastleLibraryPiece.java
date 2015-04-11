package com.mraof.minestuck.world.gen.structure;

import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;

public class ComponentCastleLibraryPiece extends ComponentCastleRoomPiece 
{
	public ComponentCastleLibraryPiece() {}
	protected ComponentCastleLibraryPiece(int par1, ComponentCastleStartPiece startPiece,	StructureBoundingBox structureBoundingBox) 
	{
		super(par1, startPiece, structureBoundingBox);
	}
	@Override
	public boolean addComponentParts(World world, Random random,StructureBoundingBox structureboundingbox) 
	{
		super.addComponentParts(world, random, structureboundingbox);
		this.func_175804_a(world, structureboundingbox, 1, 1, 1, 1, 3, 6, Blocks.bookshelf.getDefaultState(), Blocks.air.getDefaultState(), false);	//fillWithBlocks
		this.func_175804_a(world, structureboundingbox, 6, 1, 1, 6, 3, 6, Blocks.bookshelf.getDefaultState(), Blocks.air.getDefaultState(), false);
		return true;
	}


}
