package com.mraof.minestuck.world.gen.structure;

import java.util.List;
import java.util.Random;

import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;

public class StructureCastlePieces {

    public static StructureComponent getNextValidComponent(ComponentCastleStartPiece startPiece, List par1List, Random par2Random, int par3, int par4, int par5, int par6, int par7)
    {
        {
            ComponentCastlePiece newPiece = getNextComponent(startPiece, par1List, par2Random, par3, par4, par5, par6, par7);

            if (newPiece != null)
            {
                par1List.add(newPiece);
//                System.out.println(startPiece.pendingPieces);
//                System.out.println(newPiece);
                startPiece.pendingPieces.add(newPiece);
            }

            return newPiece;
        }
    }

	public static ComponentCastlePiece getNextComponent(ComponentCastleStartPiece startPiece, List par1List, Random par2Random, int par3, int par4, int par5, int par6, int i) 
	{
		switch(i)
		{
		case 0:
			return ComponentCastleSolidPiece.findValidPlacement(par1List, startPiece, par3, par4, par5, par6, i);
		case 1:
			return ComponentCastleWallPiece.findValidPlacement(par1List, startPiece, par3, par4, par5, par6, i, false);
		case 2:
			return ComponentCastleRoomPiece.findValidPlacement(par1List, startPiece, par3, par4, par5, par6, i);
		case 3:
			return ComponentCastleRoomPiece.createRandomRoom(par1List, startPiece, par3, par4, par5, par6, i, par2Random);
		default:
			return null;
		}
	}


}
