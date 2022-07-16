package com.mraof.minestuck.world.gen.feature.structure;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.ScatteredStructurePiece;

import java.util.Random;

public abstract class CoreCompatibleScatteredStructurePiece extends ScatteredStructurePiece
{
	private boolean hasBeenCompleted = false;
	
	public CoreCompatibleScatteredStructurePiece(IStructurePieceType structurePieceType, Random random, int minX, int minY, int minZ, int sizeX, int sizeY, int sizeZ)
	{
		super(structurePieceType, random, minX, minY, minZ, sizeX, sizeY, sizeZ);
	}
	
	public CoreCompatibleScatteredStructurePiece(IStructurePieceType structurePieceType, CompoundNBT nbt)
	{
		super(structurePieceType, nbt);
		hasBeenCompleted = nbt.getBoolean("hasBeenCompleted");
	}
	
	@Override
	protected void addAdditionalSaveData(CompoundNBT tagCompound) //actually writeAdditional
	{
		tagCompound.putBoolean("hasBeenCompleted", hasBeenCompleted);
		super.addAdditionalSaveData(tagCompound);
	}
	
	/**
	 * Refers to whether the end of the structure has been reached, via the activation of a WRITE type StructureCoreTileEntity
	 */
	public boolean hasBeenCompleted() {
		return hasBeenCompleted;
	}
	
	public void nowCompleted() {
		hasBeenCompleted = true;
	}
}