package com.mraof.minestuck.world.gen.structure;

import java.util.List;
import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;

import com.mraof.minestuck.Minestuck;

public class ComponentCastleStaircasePiece extends ComponentCastleRoomPiece 
{

	protected ComponentCastleStaircasePiece(int par1, ComponentCastleStartPiece startPiece,	StructureBoundingBox structureBoundingBox) 
	{
		super(par1, startPiece, structureBoundingBox);
	}
	public static ComponentCastleRoomPiece findValidPlacement(List par0List, ComponentCastleStartPiece startPiece, int par2, int par3, int par4, int par5, int par6)
	{
		StructureBoundingBox structureboundingbox = new StructureBoundingBox(par2 + 0, par3 - 8, par4 + 0, par2 + 8 + 0, 0 + 7 + 0, par4 + 8 + 0);
		return new ComponentCastleStaircasePiece(par6, startPiece, structureboundingbox);
	}
	@Override
	public void buildComponent(StructureComponent par1StructureComponent, List components, Random random) 
	{
		this.direction = random.nextInt(4);
	}
	@Override
	public boolean addComponentParts(World world, Random random, StructureBoundingBox structureboundingbox) {
//		super.addComponentParts(world, random, structureboundingbox);
//		do what that would have done but set the offset correctly
		int chessTileMetadata = startPiece.isBlack ? 0 : 1;
		int chessTileMetadata1 = startPiece.isBlack ? 2 : 3;
		if (startPiece.averageGroundLevel < 0)
		{
			startPiece.averageGroundLevel = startPiece.getAverageGroundLevel(world);

			if (startPiece.averageGroundLevel < 0)
			{
				return true;
			}

//			System.out.println(startPiece.averageGroundLevel);
		}
		if(this.boundingBox.minY < startPiece.averageGroundLevel - 9)
		{
			this.boundingBox.offset(0, startPiece.averageGroundLevel - 9, 0);
		}
		this.fillWithBlocks(world, structureboundingbox, 0, 1, 0, 7, 14, 7, 0, 0, false);
		this.fillWithAlternatingBlocks(world, structureboundingbox, 0, 15, 0, 7, 15, 7, Minestuck.chessTile.blockID, chessTileMetadata, Minestuck.chessTile.blockID, chessTileMetadata1, false);
		for(int step = 0; step < 8; step++) //Come on, step it up!
			switch(this.direction)
			{
			case 0:
				this.fillWithAlternatingBlocks(world, structureboundingbox, 0, step, step, 7, step, step, Minestuck.chessTile.blockID, chessTileMetadata, Minestuck.chessTile.blockID, chessTileMetadata1, false);
				break;
			case 1:
				this.fillWithAlternatingBlocks(world, structureboundingbox, step, step, 0, step, step, 7, Minestuck.chessTile.blockID, chessTileMetadata, Minestuck.chessTile.blockID, chessTileMetadata1, false);
				break;
			case 2:
				this.fillWithAlternatingBlocks(world, structureboundingbox, 0, step, 7 - step, 7, step, 7 - step, Minestuck.chessTile.blockID, chessTileMetadata, Minestuck.chessTile.blockID, chessTileMetadata1, false);
				break;
			case 3:
				this.fillWithAlternatingBlocks(world, structureboundingbox, 7 - step, step, 0, 7 - step, step, 7, Minestuck.chessTile.blockID, chessTileMetadata, Minestuck.chessTile.blockID, chessTileMetadata1, false);
				break;
			}
		return true;
	}

}
