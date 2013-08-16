package com.mraof.minestuck.world.gen.lands;

import com.mraof.minestuck.util.Title;

import net.minecraft.block.Block;

public abstract class LandAspect 
{
		public abstract int[][] getSurfaceBlocks();
		public abstract int[][] getUpperBlocks();
		public abstract double[] generateMainTerrainMap();
		public abstract double[] generateMinorTerrainMap();
		public int getOceanBlock()
		{
			return Block.waterStill.blockID;
		}
		
		/**
		 * Returns the chance that it will be selected as an aspect, given a player's title. Is a percentage between 0 and 1.
		 * 
		 * @param playerTitle
		 * @return
		 */
		public abstract float getRarity(Title playerTitle);
}
