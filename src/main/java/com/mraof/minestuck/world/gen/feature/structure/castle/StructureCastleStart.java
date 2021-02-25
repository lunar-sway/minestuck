package com.mraof.minestuck.world.gen.feature.structure.castle;

import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.List;

/**
 * @author mraof
 *
 */
public class StructureCastleStart extends StructureStart
{
	public StructureCastleStart(Structure<?> structureIn, int chunkX, int chunkZ, MutableBoundingBox boundsIn, int referenceIn, long seed)
	{
		super(structureIn, chunkX, chunkZ, boundsIn, referenceIn, seed);
	}
	
	@Override
	public void init(ChunkGenerator<?> generator, TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn)
	{
		boolean isBlack = rand.nextBoolean();
		
		
		CastleStartPiece startPiece = new CastleStartPiece((chunkX << 4), (chunkZ << 4), isBlack);
		this.components.add(startPiece);
		startPiece.buildComponent(startPiece, this.components, rand);
		List<CastlePiece> pendingPieces = startPiece.pendingPieces;
		while(!pendingPieces.isEmpty())
		{
			int k = rand.nextInt(pendingPieces.size());
			CastlePiece structurePiece = pendingPieces.remove(k);
			structurePiece.buildComponent(startPiece, this.components, rand);
		}
		recalculateStructureSize();
		
		int minY = Integer.MAX_VALUE;
		for(int xPos = bounds.minX; xPos <= bounds.maxX; xPos++)
			for(int zPos = bounds.minZ; zPos <= bounds.maxZ; zPos++)
			{
				int posHeight = generator.getHeight(xPos, zPos, Heightmap.Type.OCEAN_FLOOR_WG);
				minY = Math.min(minY, posHeight);
			}
		
		for(StructurePiece piece : components)
			piece.offset(0, minY, 0);
	}
	
	
}
