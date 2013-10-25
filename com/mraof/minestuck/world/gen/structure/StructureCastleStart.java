/**
 * 
 */
package com.mraof.minestuck.world.gen.structure;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureStart;

/**
 * @author mraof
 *
 */
public class StructureCastleStart extends StructureStart
{
    public StructureCastleStart(World world, Random random, int chunkX, int chunkZ, boolean isBlack)
    {
//    	Debug.print("StructureCastleStart Running");
        ComponentCastleStartPiece startPiece = new ComponentCastleStartPiece(0, (chunkX << 4) + 0, (chunkZ << 4) + 0, isBlack);
        this.components.add(startPiece);
        startPiece.buildComponent(startPiece, this.components, random);
        ArrayList pendingPieces = startPiece.pendingPieces;
//        Debug.print(pendingPieces.toString());
//        Debug.print(startPiece.getBoundingBox().minX + ", " + startPiece.getBoundingBox().minY + ", " + startPiece.getBoundingBox().minZ + ", " + startPiece.getBoundingBox().maxX + ", " + startPiece.getBoundingBox().maxY + ", " + startPiece.getBoundingBox().maxZ);
        while(!pendingPieces.isEmpty())
        {
            int k = random.nextInt(pendingPieces.size());
            StructureComponent structurecomponent = (StructureComponent)pendingPieces.remove(k);
            structurecomponent.buildComponent(startPiece, this.components, random);
        }
        this.updateBoundingBox();
    }
    

}
