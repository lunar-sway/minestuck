package com.mraof.minestuck.world.gen.lands;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.Vec3;

public class LandAspectHeat extends LandAspect 
{
	BlockWithMetadata[] upperBlocks = {new BlockWithMetadata(Blocks.netherrack), new BlockWithMetadata(Blocks.obsidian)};
	BlockWithMetadata[] surfaceBlocks = {new BlockWithMetadata(Blocks.soul_sand), new BlockWithMetadata(Blocks.cobblestone)};
	static Vec3 skyColor = Vec3.createVectorHelper(0.0D, 0.0D, 0.0D);

	@Override
	public BlockWithMetadata[] getSurfaceBlocks() 
	{
		return upperBlocks;
	}

	@Override
	public BlockWithMetadata[] getUpperBlocks() 
	{
		return upperBlocks;
	}

	@Override
	public float getRarity() {
		return 0.5F;
	}

	@Override
	public double[] generateTerrainMap() 
	{
		return null;
	}

	@Override
	public Block getOceanBlock()
	{
		return Blocks.lava;
	}
	@Override
	public Block getRiverBlock() {
		return Blocks.flowing_lava;
	}

	@Override
	public String getPrimaryName() {
		return "Heat";
	}

	@Override
	public String[] getNames() {
		return new String[] {"Heat","Flame","Fire"};
	}

	@Override
	public ArrayList<ILandDecorator> getDecorators() {
		ArrayList<ILandDecorator> list = new ArrayList<ILandDecorator>();
		list.add(new DecoratorVein(Blocks.soul_sand, 10, 32));
		list.add(new DecoratorVein(Blocks.glowstone, 5, 8));
		return list;
	}

	@Override
	public int getDayCycleMode() {
		return (new Random()).nextInt(3); //Random cycle between 0 and 2
	}

	@Override
	public Vec3 getFogColor() 
	{
		return skyColor;
	}
}
