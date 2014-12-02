package com.mraof.minestuck.world.gen.lands;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.Vec3;

public abstract class LandAspect 
{
		/**
		 * Returns the blocks that can possibly be use in the land's underground blocks.
		 * @return
		 */
		public abstract IBlockState[] getSurfaceBlocks();
		
		/**
		 * Returns the blocks that can possibly be use in the land's topmost layer of blocks.
		 * @return
		 */
		public abstract IBlockState[] getUpperBlocks();
		
		/**
		 * Unused currently. Will be used to generate the land's terrain.
		 * @return
		 */
		public abstract double[] generateTerrainMap();
		
		/**
		 * Returns the block that is a part of the land's ocean.
		 * @return
		 */
		public Block getOceanBlock()
		{
			return Blocks.water;
		}
		
		public Block getRiverBlock()
		{
			return getOceanBlock();
		}
		
		/**
		 * Returns the chance that it will be selected as an aspect. Is a percentage between 0 and 1.
		 * 
		 * @param playerTitle
		 * @return
		 */
		public abstract float getRarity();
		
		/**
		 * Returns a string that represents a unique name for a land, Used in saving and loading land data.
		 * @return
		 */
		public abstract String getPrimaryName();
		
		/**
		 * Returns a list of strings used in giving a land a random name.
		 */
		public abstract String[] getNames();
		
		/**
		 * Returns a list of possible worldgen structures a land can use.
		 * @return
		 */
		public abstract ArrayList<ILandDecorator> getDecorators();
		
		/**
		 *  Returns a integer representing how they land's day cycle will proceed.
		 *  
		 *  0 = Normal day cycle;
		 *  1 = Always day;
		 *  2 = Always night.
		 */
		public abstract int getDayCycleMode();

		public abstract Vec3 getFogColor();
}
