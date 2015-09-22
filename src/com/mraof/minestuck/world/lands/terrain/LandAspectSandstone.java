package com.mraof.minestuck.world.lands.terrain;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.BlockRedSandstone;
import net.minecraft.block.BlockSand;
import net.minecraft.block.BlockSandStone;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.gen.feature.WorldGenDeadBush;

import com.mraof.minestuck.util.AlchemyRecipeHandler;
import com.mraof.minestuck.world.lands.LandAspectRegistry;
import com.mraof.minestuck.world.lands.decorator.ILandDecorator;
import com.mraof.minestuck.world.lands.decorator.SurfaceDecoratorVein;
import com.mraof.minestuck.world.lands.decorator.WorldGenDecorator;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;
import com.mraof.minestuck.world.lands.gen.DefaultTerrainGen;
import com.mraof.minestuck.world.lands.gen.ILandTerrainGen;

public class LandAspectSandstone extends TerrainAspect
{
	
	private final IBlockState upperBlock;
	private final IBlockState[] structureBlocks;
	private final Vec3 skyColor;
	private final String name;
	private final List<TerrainAspect> variations;
	
	public LandAspectSandstone()
	{
		this("Sandstone");
	}
	
	public LandAspectSandstone(String name)
	{
		variations = new ArrayList<TerrainAspect>();
		this.name = name;
		if(name.equals("Sandstone"))
		{
			upperBlock = Blocks.sandstone.getDefaultState();
			structureBlocks = new IBlockState[] {Blocks.sandstone.getDefaultState().withProperty(BlockSandStone.TYPE, BlockSandStone.EnumType.SMOOTH), Blocks.stonebrick.getDefaultState()};
			skyColor = new Vec3(0.9D, 0.7D, 0.05D);
			
			List<WeightedRandomChestContent> list = new ArrayList<WeightedRandomChestContent>();
			list.add(new WeightedRandomChestContent(new ItemStack(Blocks.sandstone, 1, 0), 4, 15, 6));
			list.add(new WeightedRandomChestContent(new ItemStack(Blocks.sandstone, 1, 2), 2, 7, 4));
			list.add(new WeightedRandomChestContent(new ItemStack(Blocks.sandstone, 1, 1), 1, 4, 3));
			list.add(new WeightedRandomChestContent(new ItemStack(Blocks.red_sandstone, 1, 0), 2, 6, 3));
			list.add(new WeightedRandomChestContent(new ItemStack(Blocks.sand, 1, 0), 2, 7, 4));
			
			lootMap.put(AlchemyRecipeHandler.BASIC_MEDIUM_CHEST, list);
			
			variations.add(this);
			variations.add(new LandAspectSandstone("SandstoneRed"));
		}
		else
		{
			upperBlock = Blocks.red_sandstone.getDefaultState();
			structureBlocks = new IBlockState[] {Blocks.red_sandstone.getDefaultState().withProperty(BlockRedSandstone.TYPE, BlockRedSandstone.EnumType.SMOOTH), Blocks.stonebrick.getDefaultState()};
			skyColor = new Vec3(0.9D, 0.5D, 0.05D);
			
			List<WeightedRandomChestContent> list = new ArrayList<WeightedRandomChestContent>();
			list.add(new WeightedRandomChestContent(new ItemStack(Blocks.red_sandstone, 1, 0), 4, 15, 6));
			list.add(new WeightedRandomChestContent(new ItemStack(Blocks.red_sandstone, 1, 2), 2, 7, 4));
			list.add(new WeightedRandomChestContent(new ItemStack(Blocks.red_sandstone, 1, 1), 1, 4, 3));
			list.add(new WeightedRandomChestContent(new ItemStack(Blocks.sandstone, 1, 0), 2, 6, 3));
			list.add(new WeightedRandomChestContent(new ItemStack(Blocks.sand, 1, 1), 2, 7, 4));
			
			lootMap.put(AlchemyRecipeHandler.BASIC_MEDIUM_CHEST, list);
		}
	}
	
	@Override
	public String getPrimaryName()
	{
		return name;
	}
	
	@Override
	public String[] getNames()
	{
		return new String[] {"sandstone", "desertStone"};
	}
	
	@Override
	public IBlockState getUpperBlock()
	{
		return upperBlock;
	}
	
	@Override
	public IBlockState[] getStructureBlocks()
	{
		return structureBlocks;
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
	public List<ILandDecorator> getDecorators()
	{
		List<ILandDecorator> list = new ArrayList<ILandDecorator>();
		IBlockState sand = Blocks.sand.getDefaultState();
		if(name.equals("SandstoneRed"))
				sand = sand.withProperty(BlockSand.VARIANT, BlockSand.EnumType.RED_SAND);
		list.add(new SurfaceDecoratorVein(sand, 10, 32));
		list.add(new WorldGenDecorator(new WorldGenDeadBush(), 15, 0.4F));	//Will be especially uncommon because it only spawns on sand
		return list;
	}
	
	@Override
	public int getDayCycleMode()
	{
		return 0;
	}
	
	@Override
	public float getTemperature()
	{
		return 1.8F;
	}
	
	@Override
	public float getRainfall()
	{
		return 0.0F;
	}
	
	@Override
	public float getOceanChance()
	{
		return 0.1F;
	}
	
	@Override
	public Vec3 getFogColor()
	{
		return skyColor;
	}
	
	@Override
	public TerrainAspect getPrimaryVariant()
	{
		return LandAspectRegistry.fromName("Sandstone");
	}
	
	@Override
	public List<TerrainAspect> getVariations()
	{
		return variations;
	}
	
	@Override
	public ILandTerrainGen createTerrainGenerator(ChunkProviderLands chunkProvider, Random rand)
	{
		DefaultTerrainGen terrainGen = new DefaultTerrainGen(chunkProvider, rand);
		terrainGen.normalVariation = 0.7F;
		terrainGen.oceanVariation = 0.3F;
		return terrainGen;
	}
}