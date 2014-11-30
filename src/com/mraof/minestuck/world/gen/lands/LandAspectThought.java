package com.mraof.minestuck.world.gen.lands;

import java.util.ArrayList;
import java.util.Random;

import com.mraof.minestuck.Minestuck;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.Vec3;

public class LandAspectThought extends PrimaryAspect 
{
	BlockWithMetadata[] surfaceBlocks = {new BlockWithMetadata(Minestuck.coloredDirt, (byte) 1)};
	private BlockWithMetadata[] upperBlocks = {new BlockWithMetadata(Blocks.stone)};
	static Vec3 skyColor = Vec3.createVectorHelper(0.66, 0.39, 0.2);

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
	public Block getOceanBlock() 
	{
		return Minestuck.blockBrainJuice;
	}

	@Override
	public String getPrimaryName() 
	{
		return "Thought";
	}

	@Override
	public String[] getNames() {
		return new String[] {"Thought"};
	}

	@Override
	public ArrayList<ILandDecorator> getDecorators() {
		ArrayList<ILandDecorator> list = new ArrayList<ILandDecorator>();
		return list;
	}

	@Override
	public int getDayCycleMode() {
		return 0;
	}

	@Override
	public Vec3 getFogColor() 
	{
		return skyColor;
	}

}
