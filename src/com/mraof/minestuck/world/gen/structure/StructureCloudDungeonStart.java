/**
 * 
 */
package com.mraof.minestuck.world.gen.structure;

import java.util.ArrayList;
import java.util.Random;

import com.mraof.minestuck.util.Debug;

import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureStart;

public class StructureCloudDungeonStart extends StructureStart
{
	
	@SuppressWarnings("unchecked")
	public StructureCloudDungeonStart(World world, Random random, int chunkX, int chunkZ)
	{
		super(chunkX, chunkZ);
		Debug.debug("StructureCastleStart Running");
		ComponentCloudDungeonCenterPiece startPiece = new ComponentCloudDungeonCenterPiece(0, (chunkX << 4) + 0, (chunkZ << 4) + 0);
		this.components.add(startPiece);
		startPiece.buildComponent(startPiece, this.components, random);
		ArrayList<ComponentCloudDungeonPiece> pendingPieces = startPiece.pendingPieces;
		Debug.debug(pendingPieces.toString());
		Debug.debug(startPiece.getBoundingBox().minX + ", " + startPiece.getBoundingBox().minY + ", " + startPiece.getBoundingBox().minZ + ", " + startPiece.getBoundingBox().maxX + ", " + startPiece.getBoundingBox().maxY + ", " + startPiece.getBoundingBox().maxZ);
		while(!pendingPieces.isEmpty())
		{
			int k = random.nextInt(pendingPieces.size());
			StructureComponent structurecomponent = (StructureComponent)pendingPieces.remove(k);
			structurecomponent.buildComponent(startPiece, this.components, random);
		}
		this.updateBoundingBox();
	}
	
	
}
