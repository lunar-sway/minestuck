package com.mraof.minestuck.world.gen.structure.gate;

import com.mojang.serialization.Codec;
import com.mraof.minestuck.world.gen.LandChunkGenerator;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

import java.util.Random;

public class GateStructure extends StructureFeature<NoneFeatureConfiguration>
{
	public GateStructure(Codec<NoneFeatureConfiguration> codec)
	{
		super(codec, PieceGeneratorSupplier.simple(GateStructure::checkLocation, GateStructure::generatePieces));
	}
	
	@Override
	public GenerationStep.Decoration step()
	{
		return GenerationStep.Decoration.SURFACE_STRUCTURES;
	}
	
	private static boolean checkLocation(PieceGeneratorSupplier.Context<NoneFeatureConfiguration> context)
	{
		return true;
	}
	
	private static void generatePieces(StructurePiecesBuilder builder, PieceGenerator.Context<NoneFeatureConfiguration> context)
	{
		ChunkGenerator generator = context.chunkGenerator();
		ChunkPos pos = context.chunkPos();
		PieceFactory factory = getFactory(generator);
		
		if(factory == null)
			factory = GatePillarPiece::new;
		
		builder.addPiece(factory.create(generator, context.heightAccessor(), context.random(), pos.getMinBlockX(), pos.getMinBlockZ()));
	}
	
	public interface PieceFactory
	{
		GatePiece create(ChunkGenerator generator, LevelHeightAccessor level, Random rand, int minX, int minZ);
	}
	
	private static PieceFactory getFactory(ChunkGenerator generator)
	{
		if (generator instanceof LandChunkGenerator)
			return ((LandChunkGenerator) generator).gatePiece;
		else return null;
	}
}