package com.mraof.minestuck.world.lands.terrain;

import java.util.ArrayList;
import java.util.List;

import com.mraof.minestuck.util.AlchemyRecipeHandler;
import com.mraof.minestuck.world.lands.decorator.ILandDecorator;
import com.mraof.minestuck.world.lands.decorator.SurfaceDecoratorVein;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;
import net.minecraft.util.WeightedRandomChestContent;

public class LandAspectHeat extends TerrainAspect 
{
	IBlockState[] structureBlocks = {Blocks.nether_brick.getDefaultState(), Blocks.obsidian.getDefaultState()};
	static Vec3 skyColor = new Vec3(0.4D, 0.0D, 0.0D);
	
	public LandAspectHeat()
	{
		List<WeightedRandomChestContent> list = new ArrayList<WeightedRandomChestContent>();
		list.add(new WeightedRandomChestContent(new ItemStack(Items.blaze_powder, 1, 0), 2, 8, 6));
		list.add(new WeightedRandomChestContent(new ItemStack(Items.blaze_rod, 1, 0), 1, 3, 4));
		list.add(new WeightedRandomChestContent(new ItemStack(Items.magma_cream, 1, 0), 1, 3, 3));
		list.add(new WeightedRandomChestContent(new ItemStack(Items.brick, 1, 0), 1, 2, 2));
		list.add(new WeightedRandomChestContent(new ItemStack(Items.flint_and_steel, 1, 0), 1, 1, 4));
		list.add(new WeightedRandomChestContent(new ItemStack(Items.lava_bucket, 1, 0), 1, 1, 3));
		list.add(new WeightedRandomChestContent(new ItemStack(Blocks.netherrack, 1, 0), 4, 15, 5));
		
		lootMap.put(AlchemyRecipeHandler.BASIC_MEDIUM_CHEST, list);
	}
	
	@Override
	public IBlockState getUpperBlock() 
	{
		return Blocks.cobblestone.getDefaultState();
	}
	
	@Override
	public IBlockState getGroundBlock()
	{
		return Blocks.netherrack.getDefaultState();
	}
	
	@Override
	public IBlockState getOceanBlock()
	{
		return Blocks.lava.getDefaultState();
	}
	@Override
	public IBlockState getRiverBlock()
	{
		return Blocks.flowing_lava.getDefaultState();
	}

	@Override
	public String getPrimaryName() {
		return "Heat";
	}

	@Override
	public String[] getNames() {
		return new String[] {"heat","flame","fire"};
	}

	@Override
	public List<ILandDecorator> getOptionalDecorators() {
		ArrayList<ILandDecorator> list = new ArrayList<ILandDecorator>();
		list.add(new SurfaceDecoratorVein(Blocks.soul_sand.getDefaultState(), 10, 32));
		list.add(new SurfaceDecoratorVein(Blocks.glowstone.getDefaultState(), 5, 8));
		return list;
	}
	
	@Override
	public List<ILandDecorator> getRequiredDecorators()
	{
		return new ArrayList<ILandDecorator>();
	}
	
	@Override
	public int getDayCycleMode()
	{
		return 0;
	}

	@Override
	public Vec3 getFogColor() 
	{
		return skyColor;
	}
	
	@Override
	public IBlockState[] getStructureBlocks()
	{
		return structureBlocks;
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
	
}
