package com.mraof.minestuck.world.gen.structure;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

import java.util.Optional;

public class ImpDungeonStructure extends Structure
{
	public static final Codec<ImpDungeonStructure> CODEC = simpleCodec(ImpDungeonStructure::new);
	
	public ImpDungeonStructure(StructureSettings settings)
	{
		super(settings);
	}
	
	@Override
	public Optional<GenerationStub> findGenerationPoint(GenerationContext context)
	{
		return onTopOfChunkCenter(context, Heightmap.Types.WORLD_SURFACE_WG, builder -> generatePieces(builder, context));
	}
	
	@Override
	public StructureType<?> type()
	{
		return MSStructures.ImpDungeon.TYPE.get();
	}
	
	private static void generatePieces(StructurePiecesBuilder builder, GenerationContext context)
	{
		StructurePiece piece = ImpDungeonEntryPiece.create(context.chunkPos(), context.random());
		builder.addPiece(piece);
		piece.addChildren(piece, builder, context.random());
	}
}
