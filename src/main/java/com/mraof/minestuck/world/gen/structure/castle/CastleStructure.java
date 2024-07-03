package com.mraof.minestuck.world.gen.structure.castle;

import com.mojang.serialization.Codec;
import com.mraof.minestuck.world.gen.structure.MSStructures;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

import java.util.List;
import java.util.Optional;

/**
 * @author mraof
 *
 */
public class CastleStructure extends Structure
{
	public static final Codec<CastleStructure> CODEC = simpleCodec(CastleStructure::new);
	
	public CastleStructure(StructureSettings pSettings)
	{
		super(pSettings);
	}
	
	@Override
	public Optional<GenerationStub> findGenerationPoint(GenerationContext context)
	{
		return onTopOfChunkCenter(context, Heightmap.Types.WORLD_SURFACE_WG, builder -> generatePieces(builder, context));
	}
	
	@Override
	public StructureType<?> type()
	{
		return MSStructures.SkaiaCastle.TYPE.get();
	}
	
	private static void generatePieces(StructurePiecesBuilder builder, GenerationContext context)
	{
		RandomSource random = context.random();
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
		/*TODO this is not performant at all
		BoundingBox boundingBox = builder.getBoundingBox();
		
		int minY = Integer.MAX_VALUE;
		for(int xPos = boundingBox.minX(); xPos <= boundingBox.maxX(); xPos++)
			for(int zPos = boundingBox.minZ(); zPos <= boundingBox.maxZ(); zPos++)
			{
				int posHeight = context.chunkGenerator().getBaseHeight(xPos, zPos, Heightmap.Types.OCEAN_FLOOR_WG, context.heightAccessor(), context.randomState());
				minY = Math.min(minY, posHeight);
			}
		*/int minY = 64;
		builder.offsetPiecesVertically(minY);
	}
}
