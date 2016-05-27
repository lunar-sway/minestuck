package com.mraof.minestuck.world.lands.terrain;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.BlockSilverfish;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.Vec3d;

import com.mraof.minestuck.world.lands.decorator.BlockBlobDecorator;
import com.mraof.minestuck.world.lands.decorator.ILandDecorator;
import com.mraof.minestuck.world.lands.decorator.UndergroundDecoratorVein;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;
import com.mraof.minestuck.world.lands.gen.DefaultTerrainGen;
import com.mraof.minestuck.world.lands.gen.ILandTerrainGen;

public class LandAspectRock extends TerrainLandAspect
{
	
	private IBlockState[] structureBlocks = {Blocks.cobblestone.getDefaultState(), Blocks.stonebrick.getDefaultState()};
	
	public LandAspectRock()
	{
		/*List<WeightedRandomChestContent> list = new ArrayList<WeightedRandomChestContent>();
		list.add(new WeightedRandomChestContent(new ItemStack(Blocks.stone, 1, 0), 2, 8, 5));
		list.add(new WeightedRandomChestContent(new ItemStack(Blocks.cobblestone, 1, 0), 4, 15, 7));
		list.add(new WeightedRandomChestContent(new ItemStack(Blocks.gravel, 1, 0), 2, 6, 5));
		list.add(new WeightedRandomChestContent(new ItemStack(Items.brick, 1, 0), 2, 6, 4));
		list.add(new WeightedRandomChestContent(new ItemStack(Blocks.brick_block, 1, 0), 1, 3, 3));
		list.add(new WeightedRandomChestContent(new ItemStack(Items.stone_pickaxe, 1, 0), 1, 1, 7));
		list.add(new WeightedRandomChestContent(new ItemStack(Items.iron_pickaxe, 1, 0), 1, 1, 4));
		list.add(new WeightedRandomChestContent(new ItemStack(Items.diamond_pickaxe, 1, 0), 1, 1, 1));
		list.add(new WeightedRandomChestContent(new ItemStack(Items.iron_boots, 1, 0), 1, 1, 2));
		list.add(new WeightedRandomChestContent(new ItemStack(Items.iron_leggings, 1, 0), 1, 1, 2));
		list.add(new WeightedRandomChestContent(new ItemStack(Items.iron_chestplate, 1, 0), 1, 1, 2));
		list.add(new WeightedRandomChestContent(new ItemStack(Items.iron_helmet, 1, 0), 1, 1, 2));
		
		lootMap.put(AlchemyRecipeHandler.BASIC_MEDIUM_CHEST, list);*/
	}
	
	@Override
	public IBlockState getSurfaceBlock()
	{
		return Blocks.gravel.getDefaultState();
	}
	
	@Override
	public IBlockState getUpperBlock()
	{
		return Blocks.cobblestone.getDefaultState();
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
		list.add(new UndergroundDecoratorVein(Blocks.coal_ore.getDefaultState(), 20, 17, 128));
		list.add(new UndergroundDecoratorVein(Blocks.iron_ore.getDefaultState(), 20, 9, 64));
		list.add(new UndergroundDecoratorVein(Blocks.redstone_ore.getDefaultState(), 10, 8, 32));
		list.add(new UndergroundDecoratorVein(Blocks.lapis_ore.getDefaultState(), 4, 7, 24));
		list.add(new UndergroundDecoratorVein(Blocks.gold_ore.getDefaultState(), 4, 9, 32));
		list.add(new UndergroundDecoratorVein(Blocks.diamond_ore.getDefaultState(), 2, 6, 24));
		list.add(new UndergroundDecoratorVein(Blocks.gravel.getDefaultState(), 10, 33, 256));
		list.add(new UndergroundDecoratorVein(Blocks.monster_egg.getDefaultState().withProperty(BlockSilverfish.VARIANT, BlockSilverfish.EnumType.STONE), 7, 9, 64));
		
		list.add(new BlockBlobDecorator(Blocks.cobblestone.getDefaultState(), 0));
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
		return 1/3F;
	}
	
	@Override
	public Vec3d getFogColor()
	{
		return new Vec3d(0.5, 0.5, 0.55);
	}
	
	@Override
	public IBlockState getDecorativeBlockFor(IBlockState state)
	{
		if(state.getBlock() == Blocks.stonebrick)
			return Blocks.stonebrick.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CHISELED);
		return state;
	}
	
	@Override
	public ILandTerrainGen createTerrainGenerator(ChunkProviderLands chunkProvider, Random rand)
	{
		DefaultTerrainGen terrainGen = new DefaultTerrainGen(chunkProvider, rand);
		terrainGen.normalVariation = 0.8F;
		terrainGen.oceanVariation = 0.5F;
		return terrainGen;
	}
	
}