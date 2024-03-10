package com.mraof.minestuck.world.gen.structure;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

public class LargeUnfinishedTablePiece extends TemplateStructurePiece
{
	public LargeUnfinishedTablePiece(StructureTemplateManager pStructureManager, ResourceLocation pLocation, BlockPos pPos, Rotation pRotation)
	{
		super(MSStructurePieces.LARGE_UNFINISHED_TABLE.get(), 0, pStructureManager, pLocation, pLocation.toString(), makeSettings(pRotation), pPos);
	}
	
	public LargeUnfinishedTablePiece(StructureTemplateManager pStructureManager, CompoundTag pTag) {
		super(MSStructurePieces.LARGE_UNFINISHED_TABLE.get(), pTag, pStructureManager, (p_8711111_) -> {
			return makeSettings(Rotation.valueOf(pTag.getString("Rot")));
		});
	}
	
	private static StructurePlaceSettings makeSettings(Rotation pRotation) {
		return (new StructurePlaceSettings()).setRotation(pRotation).setMirror(Mirror.NONE).addProcessor(BlockIgnoreProcessor.STRUCTURE_AND_AIR);
	}
	
	protected void addAdditionalSaveData(StructurePieceSerializationContext pContext, CompoundTag pTag) {
		super.addAdditionalSaveData(pContext, pTag);
		pTag.putString("Rot", this.placeSettings.getRotation().name());
	}
	
	protected void handleDataMarker(String pName, BlockPos pPos, ServerLevelAccessor pLevel, RandomSource pRandom, BoundingBox pBox) {
	}
	
}