package com.mraof.minestuck.world.gen.feature.structure.castle;

import net.minecraft.world.gen.feature.structure.StructurePiece;

import java.util.List;
import java.util.Random;

public class StructureCastlePieces
{

	public static StructurePiece getNextValidComponent(
			CastleStartPiece startPiece,
			List<StructurePiece> par2List, Random par2Random, int x, int y, int z, int par6, int componentType)
	{
		{
			CastlePiece newPiece = getNextComponent(startPiece, par2Random, x, y, z, par6, componentType);

			if (newPiece != null)
			{
				par2List.add(newPiece);
				startPiece.pendingPieces.add(newPiece);
			}

			return newPiece;
		}
	}

	public static CastlePiece getNextComponent(CastleStartPiece startPiece, Random par2Random, int x, int y, int z, int par6, int componentType)
	{
		switch(componentType)
		{
		case 0:
			return CastleSolidPiece.findValidPlacement(startPiece.isBlack, x, y, z);
		case 1:
			return CastleWallPiece.findValidPlacement(startPiece.isBlack, x, y, z, par6, false);
		case 2:
			return CastleRoomPiece.findValidPlacement(startPiece.isBlack, x, y, z);
		case 3:
			return CastleRoomPiece.createRandomRoom(startPiece.isBlack, startPiece.bottom, x, y, z, par2Random);
		default:
			return null;
		}
	}
}
