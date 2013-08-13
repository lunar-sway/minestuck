package com.mraof.minestuck.world.gen.lands;

import com.mraof.minestuck.util.Title;

import net.minecraft.block.Block;

public abstract class LandAspect 
{
		public abstract int[][] getSurfaceBlocks();
		public abstract int[][] getUpperBlocks();
		
		/**
		 * Returns the chance that it will be selected as an aspect, given a player's title. Is a percentage between 0 and 1.
		 * 
		 * @param playerTitle
		 * @return
		 */
		public abstract long getWeight(Title playerTitle);
}
