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

import com.mraof.minestuck.world.biome.BiomeMinestuck;
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
			upperBlock = Blocks.SANDSTONE.getDefaultState();
			structureBlocks = new IBlockState[] {Blocks.SANDSTONE.getDefaultState().withProperty(BlockSandStone.TYPE, BlockSandStone.EnumType.SMOOTH), Blocks.STONEBRICK.getDefaultState()};
			skyColor = new Vec3d(0.9D, 0.7D, 0.05D);
			
			variations.add(this);
			variations.add(new LandAspectSandstone("sandstone_red"));
		}
		else
		{
			upperBlock = Blocks.RED_SANDSTONE.getDefaultState();
			structureBlocks = new IBlockState[] {Blocks.RED_SANDSTONE.getDefaultState().withProperty(BlockRedSandstone.TYPE, BlockRedSandstone.EnumType.SMOOTH), Blocks.STONEBRICK.getDefaultState()};
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
		if(state.getBlock() == Blocks.STONEBRICK)
			return Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CHISELED);
		else if(state.getBlock() == Blocks.SANDSTONE)
			return Blocks.SANDSTONE.getDefaultState().withProperty(BlockSandStone.TYPE, BlockSandStone.EnumType.CHISELED);
		else return Blocks.RED_SANDSTONE.getDefaultState().withProperty(BlockRedSandstone.TYPE, BlockRedSandstone.EnumType.CHISELED);
	}
	
	@Override
	public List<ILandDecorator> getDecorators()
	{
		List<ILandDecorator> list = new ArrayList<ILandDecorator>();
		IBlockState sand = Blocks.SAND.getDefaultState();
		IBlockState sandstone = Blocks.SANDSTONE.getDefaultState();
		if(name.equals("sandstone_red"))
		{
			sand = sand.withProperty(BlockSand.VARIANT, BlockSand.EnumType.RED_SAND);
			sandstone = Blocks.RED_SANDSTONE.getDefaultState();
		}
		list.add(new SurfaceDecoratorVein(sand, 10, 32));
		list.add(new BlockBlobDecorator(sandstone, 0, 3, BiomeMinestuck.mediumNormal));
		list.add(new BlockBlobDecorator(sandstone, 0, 5, BiomeMinestuck.mediumRough));
		list.add(new WorldGenDecorator(new WorldGenDeadBush(), 15, 0.4F));
		
		list.add(new UndergroundDecoratorVein(upperBlock, 8, 28, 256));
		list.add(new UndergroundDecoratorVein(Blocks.IRON_ORE.getDefaultState(), 24, 9, 64));
		list.add(new UndergroundDecoratorVein(Blocks.REDSTONE_ORE.getDefaultState(), 12, 8, 32));
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
		return 1/10F;
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
		terrainGen.normalVariation = 0.6F;
		terrainGen.oceanVariation = 0.3F;
		return terrainGen;
	}
}