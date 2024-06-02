package com.mraof.minestuck.world.gen.structure;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

import java.util.Optional;

public class FrogTempleStructure extends Structure
{
	public static final Codec<FrogTempleStructure> CODEC = simpleCodec(FrogTempleStructure::new);
	
	public FrogTempleStructure(StructureSettings settings)
	{
		super(settings);
	}
	
	@Override
	public Optional<GenerationStub> findGenerationPoint(GenerationContext context)
	{
		return onTopOfChunkCenter(context, Heightmap.Types.OCEAN_FLOOR_WG, builder -> generatePieces(builder, context));
	}
	
	@Override
	public StructureType<?> type()
	{
		return MSStructures.FROG_TEMPLE_TYPE.get();
	}
	
	private static void generatePieces(StructurePiecesBuilder builder, GenerationContext context)
	{
		final WorldgenRandom random = context.random();
		FrogTemplePiece mainPiece = new FrogTemplePiece(context.chunkGenerator(), context.heightAccessor(), context.randomState(), random, context.chunkPos().getMinBlockX(), context.chunkPos().getMinBlockZ());
		builder.addPiece(mainPiece);
		
		int y = mainPiece.getBoundingBox().minY(); //determines height of pillars from the variable height of the main structure
		
		int pillarOffset = 40;
		for(int i = 0; i < 2; i++) //x iterate
		{
			for(int j = 0; j < 2; j++) //z iterate
			{
				if(random.nextBoolean())
				{
					FrogTemplePillarPiece pillarPiece = new FrogTemplePillarPiece(random,
							(mainPiece.getBoundingBox().minX() + mainPiece.getBoundingBox().getXSpan() / 2) + (pillarOffset - 2 * i * pillarOffset), y, //uses frog temple location instead of x and z, so it gets the center of the temple structure
							(mainPiece.getBoundingBox().minZ() + mainPiece.getBoundingBox().getZSpan() / 2) + (pillarOffset - 2 * j * pillarOffset)); //center of temple + distance from center with +/- coordinate factor
					builder.addPiece(pillarPiece); //50% chance of generating a pillar for every corner of the frog temple structure
				}
			}
		}
	}
}
