package com.mraof.minestuck.world.gen.structure.castle;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;

public class StructureCastlePieces
{

	public static StructurePiece getNextValidComponent(
			CastleStartPiece startPiece,
			StructurePieceAccessor accessor, RandomSource par2Random, int x, int y, int z, int par6, StructureCastlePieces.Type type)
	{
		{
			CastlePiece newPiece = getNextComponent(startPiece, par2Random, x, y, z, par6, type);

			if (newPiece != null)
			{
				accessor.addPiece(newPiece);
				startPiece.pendingPieces.add(newPiece);
			}

			return newPiece;
		}
	}

	public static CastlePiece getNextComponent(CastleStartPiece startPiece, RandomSource random, int x, int y, int z, int par6, Type type)
	{
		return switch(type)
				{
					case SOLID -> CastleSolidPiece.findValidPlacement(startPiece.isBlack, x, y, z);
					case WALL -> CastleWallPiece.findValidPlacement(startPiece.isBlack, x, y, z, par6, false);
					case ROOM -> CastleRoomPiece.findValidPlacement(startPiece.isBlack, x, y, z);
					case RANDOM_ROOM ->
							CastleRoomPiece.createRandomRoom(startPiece.isBlack, startPiece.bottom, x, y, z, random);
				};
	}
	
	public enum Type
	{
		SOLID,
		WALL,
		ROOM,
		RANDOM_ROOM,
	}
}
