package com.mraof.minestuck.world.gen.feature.structure;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.ScatteredStructurePiece;

import java.util.Random;

public abstract class CoreCompatabileScatteredStructurePiece extends ScatteredStructurePiece
{
	private boolean hasBeenUsed = false;
	
	public CoreCompatabileScatteredStructurePiece(IStructurePieceType structurePieceType, Random random, int minX, int minY, int minZ, int sizeX, int sizeY, int sizeZ)
	{
		super(structurePieceType, random, minX, minY, minZ, sizeX, sizeY, sizeZ);
	}
	
	public CoreCompatabileScatteredStructurePiece(IStructurePieceType structurePieceType, CompoundNBT nbt)
	{
		super(structurePieceType, nbt);
		hasBeenUsed = nbt.getBoolean("hasBeenUsed");
	}
	
	@Override
	protected void addAdditionalSaveData(CompoundNBT tagCompound) //actually writeAdditional
	{
		tagCompound.putBoolean("hasBeenUsed", hasBeenUsed);
		super.addAdditionalSaveData(tagCompound);
	}
	
	public boolean hasBeenUsed() {
		return hasBeenUsed;
	}
	
	public void nowUsed() {
		hasBeenUsed = true;
	}
}