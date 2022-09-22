package com.mraof.minestuck.world.gen.structure.village;

import com.mojang.serialization.Codec;
import com.mraof.minestuck.world.lands.LandTypePair;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

import java.util.List;
import java.util.Random;

public class ConsortVillageStructure extends StructureFeature<NoneFeatureConfiguration>
{
	public ConsortVillageStructure(Codec<NoneFeatureConfiguration> codec)
	{
		super(codec, PieceGeneratorSupplier.simple(PieceGeneratorSupplier.checkForBiomeOnTop(Heightmap.Types.WORLD_SURFACE_WG), ConsortVillageStructure::generatePieces));
	}
	
	@Override
	public GenerationStep.Decoration step()
	{
		return GenerationStep.Decoration.SURFACE_STRUCTURES;
	}
	
	private static void generatePieces(StructurePiecesBuilder builder, PieceGenerator.Context<NoneFeatureConfiguration> context)
	{
		Random random = context.random();
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