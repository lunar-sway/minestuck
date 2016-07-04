package com.mraof.minestuck.world.lands.terrain;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.BlockSilverfish;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.Vec3d;

import com.mraof.minestuck.world.biome.BiomeMinestuck;
import com.mraof.minestuck.world.lands.decorator.BlockBlobDecorator;
import com.mraof.minestuck.world.lands.decorator.ILandDecorator;
import com.mraof.minestuck.world.lands.decorator.UndergroundDecoratorVein;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;
import com.mraof.minestuck.world.lands.gen.DefaultTerrainGen;
import com.mraof.minestuck.world.lands.gen.ILandTerrainGen;

public class LandAspectRock extends TerrainLandAspect
{
	
	private IBlockState[] structureBlocks = {Blocks.COBBLESTONE.getDefaultState(), Blocks.STONEBRICK.getDefaultState()};
	
	@Override
	public IBlockState getSurfaceBlock()
	{
		return Blocks.GRAVEL.getDefaultState();
	}
	
	@Override
	public IBlockState getUpperBlock()
	{
		return Blocks.COBBLESTONE.getDefaultState();
	}
	
	@Override
	public IBlockState[] getStructureBlocks()
	{
		return structureBlocks;
	}
	
	@Override
	public String getPrimaryName()
	{
		return "rock";
	}
	
	@Override
	public String[] getNames()
	{
		return new String[] {"rock", "stone", "ore"};
	}
	
	@Override
	public List<ILandDecorator> getDecorators()
	{
		List<ILandDecorator> list = new ArrayList<ILandDecorator>();
		list.add(new UndergroundDecoratorVein(Blocks.COAL_ORE.getDefaultState(), 20, 17, 128));
		list.add(new UndergroundDecoratorVein(Blocks.IRON_ORE.getDefaultState(), 20, 9, 64));
		list.add(new UndergroundDecoratorVein(Blocks.REDSTONE_ORE.getDefaultState(), 10, 8, 32));
		list.add(new UndergroundDecoratorVein(Blocks.LAPIS_ORE.getDefaultState(), 4, 7, 24));
		list.add(new UndergroundDecoratorVein(Blocks.GOLD_ORE.getDefaultState(), 4, 9, 32));
		list.add(new UndergroundDecoratorVein(Blocks.DIAMOND_ORE.getDefaultState(), 2, 6, 24));
		list.add(new UndergroundDecoratorVein(Blocks.GRAVEL.getDefaultState(), 10, 33, 256));
		list.add(new UndergroundDecoratorVein(Blocks.MONSTER_EGG.getDefaultState().withProperty(BlockSilverfish.VARIANT, BlockSilverfish.EnumType.STONE), 7, 9, 64));
		
		list.add(new BlockBlobDecorator(Blocks.COBBLESTONE.getDefaultState(), 0, 3, BiomeMinestuck.mediumNormal));
		list.add(new BlockBlobDecorator(Blocks.COBBLESTONE.getDefaultState(), 1, 4, BiomeMinestuck.mediumRough));
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
		return 0.3F;
	}
	
	@Override
	public float getRainfall()
	{
		return 0.2F;
	}
	
	@Override
	public float getOceanChance()
	{
		return 1/4F;
	}
	
	@Override
	public Vec3d getFogColor()
	{
		return new Vec3d(0.5, 0.5, 0.55);
	}
	
	@Override
	public IBlockState getDecorativeBlockFor(IBlockState state)
	{
		if(state.getBlock() == Blocks.STONEBRICK)
			return Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CHISELED);
		return state;
	}
	
	@Override
	public ILandTerrainGen createTerrainGenerator(ChunkProviderLands chunkProvider, Random rand)
	{
		DefaultTerrainGen terrainGen = new DefaultTerrainGen(chunkProvider, rand);
		terrainGen.normalVariation = 0.6F;
		terrainGen.roughtVariation = 0.9F;
		terrainGen.oceanVariation = 0.4F;
		return terrainGen;
	}
	
}