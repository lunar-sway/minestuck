package com.mraof.minestuck.world.lands.terrain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.mraof.minestuck.util.AlchemyRecipeHandler;
import com.mraof.minestuck.world.lands.ILandAspect;
import com.mraof.minestuck.world.lands.decorator.ILandDecorator;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;
import com.mraof.minestuck.world.lands.gen.DefaultTerrainGen;
import com.mraof.minestuck.world.lands.gen.LandTerrainGenBase;
import com.mraof.minestuck.world.lands.structure.LandStructureHandler;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.Vec3;
import net.minecraft.util.WeightedRandomChestContent;

public abstract class TerrainAspect implements ILandAspect<TerrainAspect>
{
	
	public final Map<String, List<WeightedRandomChestContent>> lootMap = new HashMap<String, List<WeightedRandomChestContent>>();
	
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
		 * Returns the block that is a part of the land's ocean.
		 * @return
		 */
		public IBlockState getOceanBlock()
		{
			return Blocks.water.getDefaultState();
		}
		
		public IBlockState getRiverBlock()
		{
			return getOceanBlock();
		}
		
		/**
		 * Returns a list of possible worldgen structures a land can use.
		 * @return
		 */
		public abstract List<ILandDecorator> getOptionalDecorators();
		
		public abstract List<ILandDecorator> getRequiredDecorators();
		
		/**
		 *  Returns a integer representing how they land's day cycle will proceed.
		 *  
		 *  0 = Normal day cycle;
		 *  1 = Always day;
		 *  2 = Always night.
		 */
		public abstract int getDayCycleMode();

		public abstract Vec3 getFogColor();
	
	public IBlockState[] getStructureBlocks()
	{
		return getUpperBlocks();
	}
	
	public int getWeatherType()
	{
		return -1;
	}
	
	@Override
	public List<TerrainAspect> getVariations()
	{
		ArrayList<TerrainAspect> list = new ArrayList<TerrainAspect>();
		list.add(this);
		return list;
	}
	
	@Override
	public TerrainAspect getPrimaryVariant()
	{
		return this;
	}
	
	/**
	 * Returns a block state for use in decoration based on an other block state
	 * @param state The state that will be primary used alongside the returned state. This parameter will always be a block state provided from getStructureBlocks()
	 * @return The block to be used for decorative details in structures
	 */
	public IBlockState getDecorativeBlockFor(IBlockState state)
	{
		return state;
	}
	
	public LandTerrainGenBase createTerrainGenerator(ChunkProviderLands chunkProvider, Random rand)
	{
		return new DefaultTerrainGen(chunkProvider, rand);
	}
	
	public void modifyStructureList(List<LandStructureHandler.StructureEntry> list)
	{}
	
	public void modifyChestContent(List<WeightedRandomChestContent> content, String lootType)
	{
		if(lootMap.containsKey(lootType))
			content.addAll(lootMap.get(lootType));
		else content.addAll(lootMap.get(AlchemyRecipeHandler.BASIC_MEDIUM_CHEST)); //Default value
	}
	
}
