package com.mraof.minestuck.world.gen.feature.structure;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

import java.util.Random;

public class SmallRuinStructure extends StructureFeature<NoneFeatureConfiguration>
{
	public SmallRuinStructure(Codec<NoneFeatureConfiguration> codec)
	{
		super(codec, PieceGeneratorSupplier.simple(PieceGeneratorSupplier.checkForBiomeOnTop(Heightmap.Types.WORLD_SURFACE_WG), SmallRuinStructure::generatePieces));
	}
	
	@Override
	public GenerationStep.Decoration step()
	{
		return GenerationStep.Decoration.SURFACE_STRUCTURES;
	}
	
	private static void generatePieces(StructurePiecesBuilder builder, PieceGenerator.Context<NoneFeatureConfiguration> context)
	{
		Random random = context.random();
		int x = context.chunkPos().getBlockX(random.nextInt(16)), z = context.chunkPos().getBlockZ(random.nextInt(16));
		SmallRuinPiece piece = new SmallRuinPiece(random, x, z, 0.5F);
		builder.addPiece(piece);
	}
}