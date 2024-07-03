package com.mraof.minestuck.world.gen.structure.village;

import com.mojang.serialization.Codec;
import com.mraof.minestuck.world.gen.structure.MSStructures;
import com.mraof.minestuck.world.lands.LandTypePair;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

import java.util.List;
import java.util.Optional;

public class ConsortVillageStructure extends Structure
{
	public static final Codec<ConsortVillageStructure> CODEC = simpleCodec(ConsortVillageStructure::new);
	
	public ConsortVillageStructure(StructureSettings pSettings)
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
		return MSStructures.ConsortVillage.TYPE.get();
	}
	
	private static void generatePieces(StructurePiecesBuilder builder, GenerationContext context)
	{
		RandomSource random = context.random();
		LandTypePair landTypes = LandTypePair.getTypesOrDefaulted(context.chunkGenerator());
		int x = context.chunkPos().getBlockX(random.nextInt(16)), z = context.chunkPos().getBlockZ(random.nextInt(16));
		
		List<ConsortVillagePieces.PieceWeight> pieceWeightList = ConsortVillagePieces.getStructureVillageWeightedPieceList(random, landTypes);
		ConsortVillageCenter.VillageCenter start = ConsortVillageCenter.getVillageStart(x, z, random, pieceWeightList, landTypes);
		builder.addPiece(start);
		start.addChildren(start, builder, random);
		
		while(!start.pendingHouses.isEmpty() || !start.pendingRoads.isEmpty())
		{
			if(!start.pendingRoads.isEmpty())
			{
				int index = random.nextInt(start.pendingRoads.size());
				StructurePiece component = start.pendingRoads.remove(index);
				component.addChildren(start, builder, random);
			} else
			{
				int index = random.nextInt(start.pendingHouses.size());
				StructurePiece component = start.pendingHouses.remove(index);
				component.addChildren(start, builder, random);
			}
		}
	}
}
