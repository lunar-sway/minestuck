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
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.gen.feature.WorldGenDeadBush;

import com.mraof.minestuck.world.lands.LandAspectRegistry;
import com.mraof.minestuck.world.lands.decorator.BlockBlobDecorator;
import com.mraof.minestuck.world.lands.decorator.ILandDecorator;
import com.mraof.minestuck.world.lands.decorator.SurfaceDecoratorVein;
import com.mraof.minestuck.world.lands.decorator.UndergroundDecoratorVein;
import com.mraof.minestuck.world.lands.decorator.WorldGenDecorator;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;
import com.mraof.minestuck.world.lands.gen.DefaultTerrainGen;
import com.mraof.minestuck.world.lands.gen.ILandTerrainGen;

public class LandAspectSandstone extends TerrainLandAspect
{
	
	private final IBlockState upperBlock;
	private final IBlockState[] structureBlocks;
	private final Vec3d skyColor;
	private final String name;
	private final List<TerrainLandAspect> variations;
	
	public LandAspectSandstone()
	{
		this("sandstone");
	}
	
	public LandAspectSandstone(String name)
	{
		variations = new ArrayList<TerrainLandAspect>();
		this.name = name;
		if(name.equals("sandstone"))
		{
			upperBlock = Blocks.sandstone.getDefaultState();
			structureBlocks = new IBlockState[] {Blocks.sandstone.getDefaultState().withProperty(BlockSandStone.TYPE, BlockSandStone.EnumType.SMOOTH), Blocks.stonebrick.getDefaultState()};
			skyColor = new Vec3d(0.9D, 0.7D, 0.05D);
			
			variations.add(this);
			variations.add(new LandAspectSandstone("sandstone_red"));
		}
		else
		{
			upperBlock = Blocks.red_sandstone.getDefaultState();
			structureBlocks = new IBlockState[] {Blocks.red_sandstone.getDefaultState().withProperty(BlockRedSandstone.TYPE, BlockRedSandstone.EnumType.SMOOTH), Blocks.stonebrick.getDefaultState()};
			skyColor = new Vec3d(0.9D, 0.5D, 0.05D);
			
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
		IBlockState sandstone = Blocks.sandstone.getDefaultState();
		if(name.equals("sandstone_red"))
		{
			sand = sand.withProperty(BlockSand.VARIANT, BlockSand.EnumType.RED_SAND);
			sandstone = Blocks.red_sandstone.getDefaultState();
		}
		list.add(new SurfaceDecoratorVein(sand, 10, 32));
		list.add(new BlockBlobDecorator(sandstone, 0));
		list.add(new WorldGenDecorator(new WorldGenDeadBush(), 15, 0.4F));	//Will be especially uncommon because it only spawns on sand
		
		list.add(new UndergroundDecoratorVein(upperBlock, 8, 28, 256));
		list.add(new UndergroundDecoratorVein(Blocks.iron_ore.getDefaultState(), 24, 9, 64));
		list.add(new UndergroundDecoratorVein(Blocks.redstone_ore.getDefaultState(), 12, 8, 32));
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
	public Vec3d getFogColor()
	{
		return skyColor;
	}
	
	@Override
	public TerrainLandAspect getPrimaryVariant()
	{
		return LandAspectRegistry.fromNameTerrain("sandstone");
	}
	
	@Override
	public List<TerrainLandAspect> getVariations()
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