
package com.mraof.minestuck.world.lands.terrain;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.mraof.minestuck.util.AlchemyRecipeHandler;
import com.mraof.minestuck.world.lands.LandAspectRegistry;
import com.mraof.minestuck.world.lands.decorator.ILandDecorator;
import com.mraof.minestuck.world.lands.decorator.SurfaceDecoratorVein;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;
import com.mraof.minestuck.world.lands.gen.LandTerrainGenBase;
import com.mraof.minestuck.world.lands.gen.RiverFreeTerrainGen;

import net.minecraft.block.BlockRedSandstone;
import net.minecraft.block.BlockSand;
import net.minecraft.block.BlockSandStone;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;
import net.minecraft.util.WeightedRandomChestContent;

public class LandAspectSand extends TerrainAspect
{
	private final IBlockState[] surfaceBlocks;
	private final IBlockState[] upperBlocks;
	private final IBlockState[] structureBlocks;
	private final Vec3 skyColor;
	private final String name;
	private final List<TerrainAspect> variations;
	
	public LandAspectSand()
	{
		this("Sand");
	}
	
	public LandAspectSand(String variation)
	{
		variations = new ArrayList<TerrainAspect>();
		name = variation;
		
		if(name.equals("Sand"))
		{
			skyColor = new Vec3(0.99D, 0.8D, 0.05D);
			
			surfaceBlocks = new IBlockState[] {Blocks.sand.getDefaultState()};
			upperBlocks = new IBlockState[] {Blocks.sandstone.getDefaultState()};
			structureBlocks = new IBlockState[] {Blocks.sandstone.getDefaultState().withProperty(BlockSandStone.TYPE, BlockSandStone.EnumType.SMOOTH), Blocks.stonebrick.getDefaultState()};
			
			List<WeightedRandomChestContent> list = new ArrayList<WeightedRandomChestContent>();
			list.add(new WeightedRandomChestContent(new ItemStack(Blocks.sand, 1, 0), 4, 15, 5));
			list.add(new WeightedRandomChestContent(new ItemStack(Blocks.sand, 1, 1), 2, 7, 3));
			list.add(new WeightedRandomChestContent(new ItemStack(Blocks.sandstone, 1, 0), 2, 7, 4));
			list.add(new WeightedRandomChestContent(new ItemStack(Blocks.sandstone, 1, 1), 2, 7, 3));
			list.add(new WeightedRandomChestContent(new ItemStack(Blocks.red_sandstone, 1, 0), 1, 3, 2));
			
			this.lootMap.put(AlchemyRecipeHandler.BASIC_MEDIUM_CHEST, list);
			
			variations.add(this);
			variations.add(new LandAspectSand("SandRed"));
		} else
		{
			skyColor = new Vec3(0.99D, 0.6D, 0.05D);
			
			surfaceBlocks = new IBlockState[] {Blocks.sand.getDefaultState().withProperty(BlockSand.VARIANT, BlockSand.EnumType.RED_SAND)};
			upperBlocks = new IBlockState[] {Blocks.red_sandstone.getDefaultState()};
			structureBlocks = new IBlockState[] {Blocks.red_sandstone.getDefaultState().withProperty(BlockRedSandstone.TYPE, BlockRedSandstone.EnumType.SMOOTH), Blocks.stonebrick.getDefaultState()};
			
			List<WeightedRandomChestContent> list = new ArrayList<WeightedRandomChestContent>();
			list.add(new WeightedRandomChestContent(new ItemStack(Blocks.sand, 1, 1), 4, 15, 5));
			list.add(new WeightedRandomChestContent(new ItemStack(Blocks.sand, 1, 0), 2, 7, 3));
			list.add(new WeightedRandomChestContent(new ItemStack(Blocks.red_sandstone, 1, 0), 2, 7, 4));
			list.add(new WeightedRandomChestContent(new ItemStack(Blocks.red_sandstone, 1, 1), 2, 7, 3));
			list.add(new WeightedRandomChestContent(new ItemStack(Blocks.sandstone, 1, 0), 1, 3, 2));
			
			this.lootMap.put(AlchemyRecipeHandler.BASIC_MEDIUM_CHEST, list);
		}
		
	}
	
	@Override
	public IBlockState[] getSurfaceBlocks() 
	{
		return surfaceBlocks;
	}
	
	@Override
	public IBlockState[] getUpperBlocks()
	{
		return upperBlocks;
	}
	
	@Override
	public IBlockState getRiverBlock()
	{
		return surfaceBlocks[0];
	}
	
	@Override
	public String getPrimaryName()
	{
		return name;
	}

	@Override
	public String[] getNames()
	{
		return new String[] {"sand", "dune", "desert"};
	}

	@Override
	public List<ILandDecorator> getOptionalDecorators()
	{
		ArrayList<ILandDecorator> list = new ArrayList<ILandDecorator>();
		if(name.equals("SandRed"))
			list.add(new SurfaceDecoratorVein(Blocks.sand.getDefaultState(), 10, 32));
		return list;
	}
	
	@Override
	public List<ILandDecorator> getRequiredDecorators()
	{
		ArrayList<ILandDecorator> list = new ArrayList<ILandDecorator>();
//		list.add(new LayeredBlockDecorator(Minestuck.layeredSand, false));
		return list;
	}
	
	@Override
	public int getDayCycleMode()
	{
		return 1;
	}
	
	@Override
	public IBlockState[] getStructureBlocks()
	{
		return structureBlocks;
	}
	
	@Override
	public Vec3 getFogColor() 
	{
		return skyColor;
	}
	
	@Override
	public List<TerrainAspect> getVariations()
	{
		return variations;
	}
	
	@Override
	public TerrainAspect getPrimaryVariant()
	{
		return LandAspectRegistry.fromName("Sand");
	}
	
	@Override
	public IBlockState getDecorativeBlockFor(IBlockState state)
	{
		if(state.getBlock() == Blocks.stonebrick)
			return Blocks.stonebrick.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CHISELED);
		else if(state.getBlock() == Blocks.sandstone)
			return Blocks.sandstone.getDefaultState().withProperty(BlockSandStone.TYPE, BlockSandStone.EnumType.CHISELED);
		else return Blocks.red_sandstone.getDefaultState().withProperty(BlockRedSandstone.TYPE, BlockRedSandstone.EnumType.CHISELED);
	}
	
	@Override
	public LandTerrainGenBase createTerrainGenerator(ChunkProviderLands chunkProvider, Random rand)
	{
		return new RiverFreeTerrainGen(chunkProvider, rand);
	}
	
}
