package com.mraof.minestuck.world.gen.lands;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.Vec3;

public class LandAspectFrost extends LandAspect 
{
	BlockWithMetadata[] surfaceBlocks = {new BlockWithMetadata(Blocks.grass)};
	private BlockWithMetadata[] upperBlocks = {new BlockWithMetadata(Blocks.stone)};
	static Vec3 skyColor = Vec3.createVectorHelper(0.45D, 0.5D, 0.98D);

	@Override
	public BlockWithMetadata[] getSurfaceBlocks() 
	{
		return surfaceBlocks;
	}

	@Override
	public BlockWithMetadata[] getUpperBlocks() 
	{
		return upperBlocks ;
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
	public Block getRiverBlock() 
	{
		return Blocks.ice;
	}

	@Override
	public String getPrimaryName() 
	{
		return "Frost";
	}

	@Override
	public String[] getNames() {
		return new String[] {"Frost","Cold","Ice"};
	}

	@Override
	public ArrayList<ILandDecorator> getDecorators() {
		ArrayList<ILandDecorator> list = new ArrayList<ILandDecorator>();
		list.add(new DecoratorVein(Blocks.dirt, 10, 32));
		list.add(new DecoratorVein(Blocks.ice, 5, 8));
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
