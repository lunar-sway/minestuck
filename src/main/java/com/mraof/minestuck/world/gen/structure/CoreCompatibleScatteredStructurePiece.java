package com.mraof.minestuck.world.gen.structure;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.levelgen.structure.ScatteredFeaturePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;

public abstract class CoreCompatibleScatteredStructurePiece extends ScatteredFeaturePiece
{
	private boolean hasBeenCompleted = false;
	
	public CoreCompatibleScatteredStructurePiece(StructurePieceType structurePieceType, int minX, int minY, int minZ, int sizeX, int sizeY, int sizeZ, Direction orientation)
	{
		super(structurePieceType, minX, minY, minZ, sizeX, sizeY, sizeZ, orientation);
	}
	
	public CoreCompatibleScatteredStructurePiece(StructurePieceType structurePieceType, CompoundTag nbt)
	{
		super(structurePieceType, nbt);
		hasBeenCompleted = nbt.getBoolean("hasBeenCompleted");
	}
	
	@Override
	protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tagCompound)
	{
		tagCompound.putBoolean("hasBeenCompleted", hasBeenCompleted);
		super.addAdditionalSaveData(context, tagCompound);
	}
	
	/**
	 * Refers to whether the end of the structure has been reached, via the activation of a WRITE type StructureCoreBlockEntity
	 */
	public boolean hasBeenCompleted() {
		return hasBeenCompleted;
	}
	
	public void nowCompleted() {
		hasBeenCompleted = true;
	}
}