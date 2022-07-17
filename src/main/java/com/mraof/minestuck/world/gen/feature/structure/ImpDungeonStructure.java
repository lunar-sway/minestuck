package com.mraof.minestuck.world.gen.feature.structure;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

public class ImpDungeonStructure extends StructureFeature<NoneFeatureConfiguration>
{
	public ImpDungeonStructure(Codec<NoneFeatureConfiguration> codec)
	{
		super(codec, PieceGeneratorSupplier.simple(PieceGeneratorSupplier.checkForBiomeOnTop(Heightmap.Types.WORLD_SURFACE_WG), ImpDungeonStructure::generatePieces));
	}
	
	@Override
	public GenerationStep.Decoration step()
	{
		return GenerationStep.Decoration.SURFACE_STRUCTURES;	//Could probably also count as an underground structure, but I'm guessing the surface component takes importance
	}
	
	private static void generatePieces(StructurePiecesBuilder builder, PieceGenerator.Context<NoneFeatureConfiguration> context)
	{
		StructurePiece piece = ImpDungeonEntryPiece.create(context.chunkPos(), context.random());
		builder.addPiece(piece);
		piece.addChildren(piece, builder, context.random());
	}
}