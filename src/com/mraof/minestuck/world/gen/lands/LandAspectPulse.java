package com.mraof.minestuck.world.gen.lands;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.Vec3;

import com.mraof.minestuck.Minestuck;

public class LandAspectPulse extends LandAspect 
{
	BlockWithMetadata[] upperBlocks = {new BlockWithMetadata(Blocks.netherrack)};
	BlockWithMetadata[] surfaceBlocks = {new BlockWithMetadata(Blocks.obsidian)};
	static Vec3 skyColor = Vec3.createVectorHelper(0.36D, 0.01D, 0.01D);
	
	@Override
	public BlockWithMetadata[] getSurfaceBlocks() 
	{
		return surfaceBlocks;
	}

	@Override
	public BlockWithMetadata[] getUpperBlocks() {
		return upperBlocks;
	}
	
	@Override
	public Block getOceanBlock() 
	{
		return Minestuck.blockBlood;
	}

	@Override
	public double[] generateTerrainMap() {
		return null;
	}

	@Override
	public float getRarity() {
		return 0.5F;
	}

	@Override
	public String getPrimaryName() {
		return "Pulse";
	}

	@Override
	public String[] getNames() {
		return new String[] {"Pulse"};
	}

	@Override
	public ArrayList<ILandDecorator> getDecorators() {
		ArrayList<ILandDecorator> list = new ArrayList<ILandDecorator>();
		list.add(new DecoratorVein(Blocks.nether_brick, 10, 32));
//		list.add(new DecoratorVein(Block.ice, 5, 8));
		return list;
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
}
