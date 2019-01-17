package com.mraof.minestuck.world.gen.structure.temple;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureStart;

public class StructureTempleStart extends StructureStart
{
	public StructureTempleStart(World world, Random random, int chunkX, int chunkZ)
	{
		super(chunkX, chunkZ);
		
		ComponentTempleStartPiece startPiece = new ComponentTempleStartPiece(0, (chunkX << 4) + 0, (chunkZ << 4) + 0, world);
		startPiece.buildComponent(startPiece, this.components, random);
	}

}
