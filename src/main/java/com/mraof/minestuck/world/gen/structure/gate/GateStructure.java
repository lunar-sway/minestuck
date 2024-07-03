package com.mraof.minestuck.world.gen.structure.gate;

import com.mojang.serialization.Codec;
import com.mraof.minestuck.world.gen.LandChunkGenerator;
import com.mraof.minestuck.world.gen.structure.MSStructures;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

import java.util.Optional;

public class GateStructure extends Structure
{
	public static final Codec<GateStructure> CODEC = simpleCodec(GateStructure::new);
	
	public GateStructure(Structure.StructureSettings settings)
	{
		super(settings);
	}
	
	@Override
	public Optional<GenerationStub> findGenerationPoint(GenerationContext context)
	{
		return Optional.of(new GenerationStub(context.chunkPos().getWorldPosition(), builder -> generatePieces(builder, context)));
	}
	
	@Override
	public StructureType<?> type()
	{
		return MSStructures.LAND_GATE_TYPE.get();
	}
	
	private static void generatePieces(StructurePiecesBuilder builder, GenerationContext context)
	{
		ChunkGenerator generator = context.chunkGenerator();
		ChunkPos pos = context.chunkPos();
		PieceFactory factory = getFactory(generator);
		
		if(factory == null)
			factory = GatePillarPiece::new;
		
		builder.addPiece(factory.create(generator, context.heightAccessor(), context.randomState(), context.random(), pos.getMinBlockX(), pos.getMinBlockZ()));
	}
	
	public interface PieceFactory
	{
		GatePiece create(ChunkGenerator generator, LevelHeightAccessor level, RandomState randomState, RandomSource rand, int minX, int minZ);
	}
	
	private static PieceFactory getFactory(ChunkGenerator generator)
	{
		if (generator instanceof LandChunkGenerator)
			return ((LandChunkGenerator) generator).gatePiece;
		else return null;
	}
}
