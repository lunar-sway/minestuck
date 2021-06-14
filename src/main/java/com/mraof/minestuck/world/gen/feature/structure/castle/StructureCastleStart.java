package com.mraof.minestuck.world.gen.feature.structure.castle;

import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.List;

/**
 * @author mraof
 *
 */
public class StructureCastleStart extends StructureStart<NoFeatureConfig>
{
	public StructureCastleStart(Structure<NoFeatureConfig> structureIn, int chunkX, int chunkZ, MutableBoundingBox boundsIn, int referenceIn, long seed)
	{
		super(structureIn, chunkX, chunkZ, boundsIn, referenceIn, seed);
	}
	
	@Override
	public void generatePieces(DynamicRegistries registries, ChunkGenerator generator, TemplateManager templateManager, int chunkX, int chunkZ, Biome biome, NoFeatureConfig config)
	{
		boolean isBlack = random.nextBoolean();
		
		
		CastleStartPiece startPiece = new CastleStartPiece((chunkX << 4), (chunkZ << 4), isBlack);
		this.pieces.add(startPiece);
		startPiece.addChildren(startPiece, this.pieces, random);
		List<CastlePiece> pendingPieces = startPiece.pendingPieces;
		while(!pendingPieces.isEmpty())
		{
			int k = random.nextInt(pendingPieces.size());
			CastlePiece structurePiece = pendingPieces.remove(k);
			structurePiece.addChildren(startPiece, this.pieces, random);
		}
		calculateBoundingBox();
		
		int minY = Integer.MAX_VALUE;
		for(int xPos = boundingBox.x0; xPos <= boundingBox.x1; xPos++)
			for(int zPos = boundingBox.z0; zPos <= boundingBox.z1; zPos++)
			{
				int posHeight = generator.getBaseHeight(xPos, zPos, Heightmap.Type.OCEAN_FLOOR_WG);
				minY = Math.min(minY, posHeight);
			}
		
		for(StructurePiece piece : pieces)
			piece.move(0, minY, 0);
	}
	
	
}
