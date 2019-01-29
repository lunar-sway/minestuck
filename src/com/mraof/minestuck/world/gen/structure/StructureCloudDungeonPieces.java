package com.mraof.minestuck.world.gen.structure;

import java.util.List;
import java.util.Random;

import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.gen.structure.StructureComponent;

public class StructureCloudDungeonPieces 
{

	public static StructureComponent getNextValidComponent(
			ComponentCloudDungeonPiece cloudStartPiece,
			List<ComponentCloudDungeonPiece> par2List, Random par2Random, int x, int y, int z, int par6, int componentType)
	{
		{
			ComponentCloudDungeonPiece newPiece = getNextComponent(cloudStartPiece, par2List, par2Random, x, y, z, par6, componentType);
			
			if (newPiece != null)
			{
				par2List.add(newPiece);
//				Debug.print(startPiece.pendingPieces);
//				Debug.print(newPiece);
				cloudStartPiece.pendingPieces.add(newPiece);
			}
			
			return newPiece;
		}
	}
	
	public static ComponentCloudDungeonPiece getNextComponent(ComponentCloudDungeonPiece cloudStartPiece, List<ComponentCloudDungeonPiece> par2List, Random par2Random, int x, int y, int z, int par6, int location) 
	{
		switch(location)
		{
		case 0:
		case 2:
		case 6:
		case 8:
			return ComponentCloudDungeonCornerPiece.findValidPlacement(par2List, cloudStartPiece, x, y, z, par6, location);
		case 1:
		case 3:
		case 5:
		case 7:
			return ComponentCloudDungeonEdgePiece.findValidPlacement(par2List, cloudStartPiece, x, y, z, par6, location);
		default:
			return ComponentCloudDungeonCenterPiece.findValidPlacement(par2List, cloudStartPiece, x, y, z, par6, location);
		}
	}
	
	public static void registerComponents()
	{
		MapGenStructureIO.registerStructureComponent(ComponentCloudDungeonCenterPiece.class, "CloudDungeonCenter");
		MapGenStructureIO.registerStructureComponent(ComponentCloudDungeonEdgePiece.class, "CloudDungeonEdge");
		MapGenStructureIO.registerStructureComponent(ComponentCloudDungeonCornerPiece.class, "CloudDungeonCorner");
	}
	
}
