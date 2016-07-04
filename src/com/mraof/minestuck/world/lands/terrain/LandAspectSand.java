
package com.mraof.minestuck.world.lands.terrain;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.world.biome.BiomeMinestuck;
import com.mraof.minestuck.world.lands.LandAspectRegistry;
import com.mraof.minestuck.world.lands.decorator.ILandDecorator;
import com.mraof.minestuck.world.lands.decorator.SurfaceDecoratorVein;
import com.mraof.minestuck.world.lands.decorator.UndergroundDecoratorVein;
import com.mraof.minestuck.world.lands.decorator.WorldGenDecorator;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;
import com.mraof.minestuck.world.lands.gen.LandTerrainGenBase;
import com.mraof.minestuck.world.lands.gen.RiverFreeTerrainGen;

import net.minecraft.block.BlockRedSandstone;
import net.minecraft.block.BlockSand;
import net.minecraft.block.BlockSandStone;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.gen.feature.WorldGenCactus;
import net.minecraft.world.gen.feature.WorldGenDeadBush;

public class LandAspectSand extends TerrainLandAspect
{
	private final IBlockState upperBlock;
	private final IBlockState groundBlock;
	private final IBlockState[] structureBlocks;
	private final Vec3d skyColor;
	private final String name;
	private final List<TerrainLandAspect> variations;
	
	public LandAspectSand()
	{
		this("sand");
	}
	
	public LandAspectSand(String variation)
	{
		variations = new ArrayList<TerrainLandAspect>();
		name = variation;
		
		if(name.equals("sand"))
		{
			skyColor = new Vec3d(0.99D, 0.8D, 0.05D);
			
			upperBlock = Blocks.SAND.getDefaultState();
			groundBlock = Blocks.SANDSTONE.getDefaultState();
			structureBlocks = new IBlockState[] {Blocks.SANDSTONE.getDefaultState().withProperty(BlockSandStone.TYPE, BlockSandStone.EnumType.SMOOTH), Blocks.STONEBRICK.getDefaultState()};
			
			variations.add(this);
			variations.add(new LandAspectSand("sand_red"));
		} else
		{
			skyColor = new Vec3d(0.99D, 0.6D, 0.05D);
			
			upperBlock = Blocks.SAND.getDefaultState().withProperty(BlockSand.VARIANT, BlockSand.EnumType.RED_SAND);
			groundBlock = Blocks.RED_SANDSTONE.getDefaultState();
			structureBlocks = new IBlockState[] {Blocks.RED_SANDSTONE.getDefaultState().withProperty(BlockRedSandstone.TYPE, BlockRedSandstone.EnumType.SMOOTH), Blocks.STONEBRICK.getDefaultState()};
			
		}
		
	}
	
	@Override
	public IBlockState getUpperBlock()
	{
		return upperBlock;
	}
	
	@Override
	public IBlockState getGroundBlock()
	{
		return groundBlock;
	}
	
	@Override
	public IBlockState getRiverBlock()
	{
		return upperBlock;
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
	public List<ILandDecorator> getDecorators()
	{
		ArrayList<ILandDecorator> list = new ArrayList<ILandDecorator>();
		if(name.equals("sand_red"))
			list.add(new SurfaceDecoratorVein(Blocks.SAND.getDefaultState(), 10, 32));
		list.add(new WorldGenDecorator(new WorldGenCactus(), 15, 0.4F, BiomeMinestuck.mediumNormal));
		list.add(new WorldGenDecorator(new WorldGenCactus(), 5, 0.4F, BiomeMinestuck.mediumRough));
		list.add(new WorldGenDecorator(new WorldGenDeadBush(), 1, 0.4F, BiomeMinestuck.mediumNormal, BiomeMinestuck.mediumRough));
		
		list.add(new UndergroundDecoratorVein(upperBlock, 8, 28, 256));
		list.add(new UndergroundDecoratorVein((name.equals("sand_red")?MinestuckBlocks.ironOreSandstoneRed:MinestuckBlocks.ironOreSandstone).getDefaultState(), 24, 9, 64));
		list.add(new UndergroundDecoratorVein((name.equals("sand_red")?MinestuckBlocks.goldOreSandstoneRed:MinestuckBlocks.goldOreSandstone).getDefaultState(), 6, 9, 32));
		
		return list;
	}
	
	@Override
	public int getDayCycleMode()
	{
		return 1;
	}
	
	@Override
	public float getTemperature()
	{
		return 2.0F;
	}
	
	@Override
	public float getRainfall()
	{
		return 0.0F;
	}
	
	@Override
	public float getOceanChance()
	{
		return 0F;
	}
	
	@Override
	public IBlockState[] getStructureBlocks()
	{
		return structureBlocks;
	}
	
	@Override
	public Vec3d getFogColor() 
	{
		return skyColor;
	}
	
	@Override
	public List<TerrainLandAspect> getVariations()
	{
		return variations;
	}
	
	@Override
	public TerrainLandAspect getPrimaryVariant()
	{
		return LandAspectRegistry.fromNameTerrain("sand");
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
	public LandTerrainGenBase createTerrainGenerator(ChunkProviderLands chunkProvider, Random rand)
	{
		return new RiverFreeTerrainGen(chunkProvider, rand);
	}
	
}
