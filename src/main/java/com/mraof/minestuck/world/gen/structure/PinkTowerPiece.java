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
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.neoforged.neoforge.common.world.PieceBeardifierModifier;

public final class PinkTowerPiece extends TemplateStructurePiece implements PieceBeardifierModifier
{
	public PinkTowerPiece(StructureTemplateManager pStructureManager, ResourceLocation pLocation, BlockPos pPos, Rotation pRotation)
	{
		super(MSStructures.PINK_TOWER_PIECE.get(), 0, pStructureManager, pLocation, pLocation.toString(), makeSettings(pRotation), pPos);
	}
	
	public PinkTowerPiece(StructureTemplateManager pStructureManager, CompoundTag pTag) {
		super(MSStructures.PINK_TOWER_PIECE.get(), pTag, pStructureManager, id -> makeSettings(Rotation.valueOf(pTag.getString("Rot"))));
	}
	
	private static StructurePlaceSettings makeSettings(Rotation pRotation) {
		return (new StructurePlaceSettings()).setRotation(pRotation).setMirror(Mirror.NONE);
	}
	
	protected void addAdditionalSaveData(StructurePieceSerializationContext pContext, CompoundTag pTag) {
		super.addAdditionalSaveData(pContext, pTag);
		pTag.putString("Rot", this.placeSettings.getRotation().name());
	}
	
	protected void handleDataMarker(String pName, BlockPos pPos, ServerLevelAccessor pLevel, RandomSource pRandom, BoundingBox pBox) {
	}
	
	@Override
	public BoundingBox getBeardifierBox()
	{
		return this.boundingBox;
	}
	
	@Override
	public TerrainAdjustment getTerrainAdjustment()
	{
		return TerrainAdjustment.BEARD_THIN;
	}
	
	@Override
	public int getGroundLevelDelta()
	{
		return 3;
	}
}
