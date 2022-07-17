package com.mraof.minestuck.world.gen.feature.structure.castle;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

import java.util.List;
import java.util.Random;

/**
 * @author mraof
 *
 */
public class CastleStructure extends StructureFeature<NoneFeatureConfiguration>
{
	public CastleStructure(Codec<NoneFeatureConfiguration> configCodec)
	{
		super(configCodec, PieceGeneratorSupplier.simple(PieceGeneratorSupplier.checkForBiomeOnTop(Heightmap.Types.WORLD_SURFACE_WG), CastleStructure::generatePieces));
	}
	
	@Override
	public GenerationStep.Decoration step()
	{
		return GenerationStep.Decoration.SURFACE_STRUCTURES;
	}
	
	private static void generatePieces(StructurePiecesBuilder builder, PieceGenerator.Context<NoneFeatureConfiguration> context)
	{
		Random random = context.random();
		boolean isBlack = random.nextBoolean();
		
		CastleStartPiece startPiece = new CastleStartPiece(context.chunkPos().getMinBlockX(), context.chunkPos().getMinBlockZ(), isBlack);
		builder.addPiece(startPiece);
		startPiece.addChildren(startPiece, builder, random);
		List<CastlePiece> pendingPieces = startPiece.pendingPieces;
		while(!pendingPieces.isEmpty())
		{
			int k = random.nextInt(pendingPieces.size());
			CastlePiece structurePiece = pendingPieces.remove(k);
			structurePiece.addChildren(startPiece, builder, random);
		}
		BoundingBox boundingBox = builder.getBoundingBox();
		
		int minY = Integer.MAX_VALUE;
		for(int xPos = boundingBox.minX(); xPos <= boundingBox.maxX(); xPos++)
			for(int zPos = boundingBox.minZ(); zPos <= boundingBox.maxZ(); zPos++)
			{
				int posHeight = context.chunkGenerator().getBaseHeight(xPos, zPos, Heightmap.Types.OCEAN_FLOOR_WG, context.heightAccessor());
				minY = Math.min(minY, posHeight);
			}
		
		builder.offsetPiecesVertically(minY);
	}
}